package org.uispec4j;

import org.openshapa.views.discrete.SpreadsheetCell;
import org.openshapa.views.discrete.datavalues.DataValueElementV;
import org.openshapa.views.discrete.datavalues.DataValueElementV.DataValueEditor;
import org.openshapa.views.discrete.datavalues.DataValueV;
import java.awt.Component;
import java.util.Vector;
import junit.framework.Assert;
import org.uispec4j.utils.KeyUtils;



/**
 *
 * @author mmuthukrishna
 */
public class Cell extends AbstractUIComponent {
    /**
     * UISpec4J convention to declare type.
     */
    public static final String TYPE_NAME = "SpreadsheetCell";
    /**
     * UISpec4J convention to declare associated class.
     */
    public static final Class[] SWING_CLASSES = {SpreadsheetCell.class};

    /**
     * Since this is an Adaptor class, this is the class being adapted.
     */
    private SpreadsheetCell ssCell;

    /**
     * Spreadsheet constructor.
     * @param SpreadsheetCell actual SpreadsheetCell class being adapted
     */
    public Cell(final SpreadsheetCell spreadsheetCell) {
        Assert.assertNotNull(spreadsheetCell);
        this.ssCell = spreadsheetCell;
    }

    public Component getAwtComponent() {
        return ssCell;
    }

    public String getDescriptionTypeName() {
        return TYPE_NAME;
    }

    /**
     * returns the ordinal column identifier.
     * @return long ordinal column identifier
     */
    public final long getOrd() {
        return ssCell.getOrdinal().getItsValue();
    }

    /**
     * Returns Timestamp of Onset so that components are easily accessible.
     * @return Timestamp onset timestamp
     */
    public final Timestamp getOnsetTime() {
        return new Timestamp(ssCell.getOnsetDisplay());
    }

    /**
     * Returns Timestamp of Offset so that components are easily accessible.
     * @return Timestamp offset timestamp
     */
    public final Timestamp getOffsetTime() {
        return new Timestamp(ssCell.getOffsetDisplay());
    }

    /**
     * returns the value, which is a Vector of DataValueView.
     * This is a matrix, which may change in the future, but right now,
     * it is not easy to hide this implementation.
     * @return Vector<DataValueView> value as a vector of DataValueView
     */
    public final Vector<DataValueV> getValue() {
        return ssCell.getDataValueV().getChildren();
    }

    public final DataValueElementV getView(int part) {
        DataValueV v = getValue().elementAt(part);
        if (v instanceof DataValueElementV) {
            return (DataValueElementV) v;

        // Can't build DataValueElementV predicate or matrix.
        } else {
            return null;
        }
    }

    public final DataValueEditor getEditor(int part) {
        DataValueElementV v = getView(part);
        if (v != null) {
            return (DataValueEditor) v.getEditor();
        } else {
            return null;
        }
    }

   public final void enterEditorText(int part, String s) {
        for (int i = 0; i < s.length(); i++) {
            pressEditorKey(part, new Key(s.charAt(i)));
        }
    }

    public final void pressEditorKey(int i, Key k) {
        DataValueEditor e = getEditor(i);
        e.focusGained(null);
        KeyUtils.typeKey(e, k);
    }

    public final TextBox getTextBox(int part) {
        DataValueElementV view = getView(part);

        if (view != null) {
            return new TextBox(view.getEditor());

        } else {
            return null;
        }
    }
}