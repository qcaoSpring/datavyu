/**
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.openshapa.undoableedits;

import com.usermetrix.jclient.Logger;
import com.usermetrix.jclient.UserMetrix;
import java.util.List;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import org.openshapa.controllers.DeleteColumnC;
import org.openshapa.models.db.DeprecatedDatabase;
import org.openshapa.models.db.DeprecatedVariable;
import org.openshapa.models.db.Variable;
import database.Cell;
import database.DataCell;
import database.DataCellTO;
import database.DataColumn;
import database.DataColumnTO;
import database.Database;
import database.MatrixVocabElement;
import database.SystemErrorException;
import java.util.ArrayList;


/**
 *
 */
public class RunScriptEdit extends SpreadsheetEdit {    
    /** The logger for this class. */
    private static final Logger LOGGER = UserMetrix.getLogger(RunScriptEdit.class);

    private Database db;
    private String scriptPath;
    private List<DataColumnTO> colsTO; // DataColumn relevant values 
            
    public RunScriptEdit(String scriptPath) { 
        super();
        this.scriptPath = scriptPath;            
        colsTO = getSpreadsheetState();
        db = controller.getLegacyDB().getDatabase();
    }

    @Override
    public String getPresentationName() {
        /*
        File file = new File(scriptPath);
        String fileName = file.getName();        
        return "Run Script \"" + fileName + "\"";
         * 
         */
        return "Changes due to \"" + this.scriptPath + "\"";
    }

    @Override
    public void undo() throws CannotRedoException {
        super.undo();
        toogleSpreadsheetState();
    }

    @Override 
    public void redo() throws CannotUndoException {        
        super.redo();
        toogleSpreadsheetState();
    } 
    
    private void toogleSpreadsheetState() {
        List<DataColumnTO> tempColsTO = this.getSpreadsheetState();
        setSpreadsheetState(colsTO);
        colsTO = tempColsTO;       
    }
    
    private List<DataColumnTO> getSpreadsheetState() {
         List<DataColumnTO> colsTO = new ArrayList<DataColumnTO>();
         try {            
            List<Variable> vars = new ArrayList(model.getAllVariables());
            List<DataColumn> colsToDelete = new ArrayList<DataColumn>();
            for (Variable var : vars) {
                String name = var.getName();
                for (DataColumn dc : db.getDataColumns()) {
                    if (name.equals(dc.getName())) {
                        colsToDelete.add(dc);    
                    }
                }
            }
     
            // Add the cells to each Column
            for (DataColumn col : colsToDelete) {
                DataColumnTO colTO = new DataColumnTO(col);               
                int numCells = col.getNumCells(); 
                colTO.dataCellsTO.clear();
                for (int i = 0; i < numCells; i++) {
                        Cell c = db.getCell(col.getID(), i+1);
                        DataCellTO cTO = new DataCellTO((DataCell)c);
                        colTO.dataCellsTO.add(cTO);
                }
                colsTO.add(colTO);                    
            }
        } catch (SystemErrorException e) {
            LOGGER.error("Unable to getSpreadsheetState.", e);
        } finally {
            return colsTO;
        }       
    }

    private void setSpreadsheetState(List<DataColumnTO> colsTO) {
        try {           
            new DeleteColumnC(new ArrayList(model.getAllVariables()));

            for (DataColumnTO colTO : colsTO) {    
                DataColumn dc = new DataColumn(db, colTO.name, colTO.itsMveType);

                Variable.type newType;
                if (colTO.itsMveType == MatrixVocabElement.MatrixType.MATRIX) {
                    newType = Variable.type.MATRIX;
                } else if (colTO.itsMveType == MatrixVocabElement.MatrixType.NOMINAL) {
                    newType = Variable.type.NOMINAL;
                } else {
                    newType = Variable.type.TEXT;
                }

                DeprecatedVariable var = new DeprecatedVariable(dc, newType);
                model.addVariable(var);
                dc = db.getDataColumn(dc.getName());             
                for (DataCellTO cellTO : colTO.dataCellsTO) {                 
                    DataCell newCell = new DataCell(db,dc.getID(), dc.getItsMveID());
                    long cellID = ((DeprecatedDatabase) model).getDatabase().appendCell(newCell);
                    newCell = (DataCell)((DeprecatedDatabase) model).getDatabase().getCell(cellID); 
                    newCell.setDataCellData(cellTO);                   
                    db.replaceCell(newCell);  
                }                   
            } 
            unselectAll();            
        } catch (SystemErrorException e) {
                LOGGER.error("Unable to setSpreadsheetState.", e);
        }        
    }
    
}
