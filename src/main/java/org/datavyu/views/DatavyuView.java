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
package org.datavyu.views;

import javafx.embed.swing.JFXPanel;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.datavyu.Configuration;
import org.datavyu.Datavyu;
import org.datavyu.Datavyu.Platform;
import org.datavyu.RecentFiles;
import org.datavyu.controllers.*;
import org.datavyu.controllers.project.ProjectController;
import org.datavyu.event.component.FileDropEvent;
import org.datavyu.event.component.FileDropEventListener;
import org.datavyu.models.db.*;
import org.datavyu.plugins.DataViewer;
import org.datavyu.undoableedits.RemoveCellEdit;
import org.datavyu.undoableedits.RemoveVariableEdit;
import org.datavyu.undoableedits.RunScriptEdit;
import org.datavyu.undoableedits.SpreadsheetUndoManager;
import org.datavyu.util.ArrayDirection;
import org.datavyu.util.DragAndDrop.GhostGlassPane;
import org.datavyu.util.FileFilters.*;
import org.datavyu.util.FileSystemTreeModel;
import org.datavyu.views.discrete.SpreadsheetColumn;
import org.datavyu.views.discrete.SpreadsheetPanel;
import org.datavyu.views.discrete.layouts.SheetLayoutFactory.SheetLayoutType;
import org.jdesktop.application.Action;
import org.jdesktop.application.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/**
 * The main FrameView, representing the interface for Datavyu the user will
 * initially see.
 */
public final class DatavyuView extends FrameView
        implements FileDropEventListener {

    // Variable for the amount to raise the font size by when zooming.
    public static final int ZOOM_INTERVAL = 2;
    public static final int ZOOM_DEFAULT_SIZE = 14;
    // Variables to set the maximum zoom and minimum zoom.
    public static final int ZOOM_MAX_SIZE = 42;
    public static final int ZOOM_MIN_SIZE = 8;
    /**
     * The directory holding a users favourite scripts.
     */
    static final String FAV_DIR = "favourites";
    /**
     * The logger for this class.
     */
    private static Logger LOGGER = LogManager.getLogger(DatavyuView.class);
    private static boolean redraw = true;
    private final Icon rubyIcon = new ImageIcon(getClass().getResource("/icons/ruby.png"));
    private final Icon opfIcon = new ImageIcon(getClass().getResource("/icons/datavyu.png"));
    String fav_dir_config = Configuration.getInstance().getFavouritesFolder();
    /**
     * undo system elements
     */
    SpreadsheetUndoManager spreadsheetUndoManager; // history list
    UndoableEditSupport undoSupport; // event support
    JPopupMenu popupMenu = new JPopupMenu();
    JMenuItem openInTextEditor = new JMenuItem("Open in text editor");
    JMenuItem openInDatavyu = new JMenuItem("Open in Datavyu");
    /**
     * The spreadsheet panel for this view.
     */
    private SpreadsheetPanel panel;
    private JSplitPane splitPane;
    private DVProgressBar progressBar;
    private OpenTask task;
    /**
     * the code editor's controller
     */
    private VocabEditorC vec = VocabEditorC.getController();
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem ShowAllVariablesMenuItem;
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.JMenuItem updateMenuItem;
    private javax.swing.JMenuItem supportMenuItem;
    private javax.swing.JMenuItem guideMenuItem;
    private javax.swing.JMenuItem citationMenuItem;
    private javax.swing.JMenuItem hotkeysMenuItem;
    private javax.swing.JMenuItem changeVarNameMenuItem;
    private javax.swing.JMenu controllerMenu;
    private javax.swing.JMenuItem deleteCellMenuItem;
    private javax.swing.JMenuItem deleteColumnMenuItem;
    private javax.swing.JMenuItem favScripts;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JMenuItem hideSelectedColumnsMenuItem;
    private javax.swing.JMenuItem historySpreadSheetMenuItem;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem newCellLeftMenuItem;
    private javax.swing.JMenuItem newCellMenuItem;
    private javax.swing.JMenuItem newCellRightMenuItem;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenuItem newVariableMenuItem;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenu openRecentFileMenu;
    private javax.swing.JMenuItem pullMenuItem;
    private javax.swing.JMenuItem pushMenuItem;
    private javax.swing.JMenuItem qtControllerItem;
    private javax.swing.JMenuItem videoConverterMenuItem;
    private javax.swing.JMenuItem recentScriptsHeader;
    private javax.swing.JMenuItem redoSpreadSheetMenuItem;
    private javax.swing.JMenuItem resetZoomMenuItem;
    private javax.swing.JMenu runRecentScriptMenu;
    private javax.swing.JMenuItem runScriptMenuItem;
    private javax.swing.JMenuItem setFavouritesMenuItem;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem exportMenuItem;
    private javax.swing.JMenuItem exportByFrameMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JMenu scriptMenu;
    private ArrayList scriptMenuPermanentsList;
    private javax.swing.JMenuItem showSpreadsheetMenuItem;
    private javax.swing.JMenu spreadsheetMenu;
    private javax.swing.JMenuItem undoSpreadSheetMenuItem;
    private javax.swing.JMenuItem vocabEditorMenuItem;
    private javax.swing.JCheckBoxMenuItem weakTemporalAlignmentMenuItem;
    private javax.swing.JMenuItem zoomInMenuItem;
    private javax.swing.JMenu zoomMenu;
    private javax.swing.JMenuItem zoomOutMenuItem;
    private javax.swing.JMenuItem quickkeysMenuItem;
    private javax.swing.JMenuItem highlightAndFocusMenuItem;

    private boolean quickKeyMode = false;

    private JTabbedPane tabbedPane;
    private JScrollPane fileScrollPane;
    private JSplitPane fileSplitPane;
    private JTree fileDrawer;
    private TreeModel fileTree;
    private JScrollPane favScrollPane;
    private JTree favDrawer;
    // End of variables declaration//GEN-END:variables
    private TreeModel favTree;

    /**
     * Constructor.
     *
     * @param app The SingleFrameApplication that invoked this main FrameView.
     */
    public DatavyuView(final SingleFrameApplication app) {
        super(app);

        KeyboardFocusManager manager = KeyboardFocusManager
                .getCurrentKeyboardFocusManager();

        manager.addKeyEventDispatcher(new KeyEventDispatcher() {

            /**
             * Dispatches the keystroke to the correct action.
             *
             * @param evt The event that triggered this action.
             * @return true if the KeyboardFocusManager should take no further
             * action with regard to the KeyEvent; false otherwise.
             */
            @Override
            public boolean dispatchKeyEvent(final KeyEvent evt) {

                // Pass the keyevent onto the keyswitchboard so that it can
                // route it to the correct action.
//                    spreadsheetMenuSelected(null);

                return Datavyu.getApplication().dispatchKeyEvent(evt);
            }
        });

        // generated GUI builder code
        initComponents();
        new JFXPanel();

        this.getFrame().setGlassPane(new GhostGlassPane());
        this.getFrame().setVisible(true);
        this.getFrame().getGlassPane().setVisible(true);

        // BugzID:492 - Set the shortcut for new cell, so a keystroke that won't
        // get confused for the "carriage return". The shortcut for new cells
        // is handled in Datavyu.java
        newCellMenuItem.setAccelerator(KeyStroke.getKeyStroke('\u21A9'));

        // BugzID:521 + 468 - Define accelerator keys based on Operating system.
        int keyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
        weakTemporalAlignmentMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_T, keyMask));

        // Set zoom in to keyMask + '+'
        zoomInMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,
                keyMask));

        // Set zoom out to keyMask + '-'
        zoomOutMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS,
                keyMask));

        // Set reset zoom to keyMask + '0'
        resetZoomMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_0,
                keyMask));

        // Set the save accelerator to keyMask + 'S'
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                keyMask));

        // Set the save as accelerator to keyMask + shift + 'S'
        saveAsMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                keyMask | InputEvent.SHIFT_MASK));

        // Set the open accelerator to keyMask + 'o';
        openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
                keyMask));

        // Set the new accelerator to keyMask + 'N';
        newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
                keyMask));

        // Set the new accelerator to keyMask + 'L';
        newCellLeftMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
                keyMask));

        // Set the new accelerator to keyMask + 'R';
        newCellRightMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_R, keyMask));

        // Set the show spreadsheet accelrator to F5.
        showSpreadsheetMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_F5, 0));

        // Set the undo accelerator to keyMask + 'Z';
        undoSpreadSheetMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Z, keyMask));

        // Set the redo accelerator to keyMask + 'Y'
        redoSpreadSheetMenuItem.setAccelerator(KeyStroke.getKeyStroke(
                KeyEvent.VK_Y, keyMask));

        // Set delete cells to keyMask + backspace
        deleteCellMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SLASH, keyMask));

        // Set enable quick key mode to keyMask + shift + 'K'
        quickkeysMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_K, keyMask | InputEvent.SHIFT_MASK));
        highlightAndFocusMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, keyMask | InputEvent.SHIFT_MASK));

        if (panel != null) {
            panel.deregisterListeners();
            panel.removeFileDropEventListener(this);
        }

        tabbedPane = new JTabbedPane();

        fileTree = new FileSystemTreeModel(new File("."));
        fileDrawer = new JTree(fileTree);

        favTree = new FileSystemTreeModel(new File(fav_dir_config));
        favDrawer = new JTree(favTree);

        fileDrawer.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree,
                                                          Object value, boolean selected, boolean expanded,
                                                          boolean isLeaf, int row, boolean focused) {
                Component c = super.getTreeCellRendererComponent(tree, value,
                        selected, expanded, isLeaf, row, focused);
                if (tree.getPathForRow(row) != null) {
                    if (convertTreePathToString(tree.getPathForRow(row)).endsWith(".rb")) {
                        setIcon(rubyIcon);
                    } else if (convertTreePathToString(tree.getPathForRow(row)).endsWith(".opf")) {
                        setIcon(opfIcon);
                    }
                }
                return c;
            }

        });

        favDrawer.setCellRenderer(new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(JTree tree,
                                                          Object value, boolean selected, boolean expanded,
                                                          boolean isLeaf, int row, boolean focused) {
                Component c = super.getTreeCellRendererComponent(tree, value,
                        selected, expanded, isLeaf, row, focused);
                if (tree.getPathForRow(row) != null) {
                    if (convertTreePathToString(tree.getPathForRow(row)).endsWith(".rb")) {
                        setIcon(rubyIcon);
                    } else if (convertTreePathToString(tree.getPathForRow(row)).endsWith(".opf")) {
                        setIcon(opfIcon);
                    }
                }
                return c;
            }

        });

        fileDrawer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selRow = fileDrawer.getRowForLocation(e.getX(), e.getY());
                final TreePath selPath = fileDrawer.getPathForLocation(e.getX(), e.getY());

                if (selRow != -1) {
                    String path = convertTreePathToString(selPath);
                    String baseDir;
                    if (Datavyu.getProjectController().getProject().getProjectDirectory() == null) {
                        baseDir = new File(System.getProperty("user.home")).getParent();
                    } else {
//                        baseDir = new File(".").getParent();
                        baseDir = new File(Datavyu.getProjectController().getProject().getProjectDirectory()).getParent();
                    }
                    final File f = new File(baseDir + File.separator + path);

                    if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
                        if (f.isFile()) {
                            if (f.getName().toLowerCase().endsWith(".rb")) {
                                runScript(f);
                            }
                            if (f.getName().toLowerCase().endsWith(".opf")) {
                                System.out.println(f.getAbsolutePath());
                                open(f);
                            }
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        int row = fileDrawer.getClosestRowForLocation(e.getX(), e.getY());
                        fileDrawer.setSelectionRow(row);
                        popupMenu.removeAll();
                        if (f.getName().toLowerCase().endsWith(".rb")) {
                            popupMenu.add(openInTextEditor);
                            for (MouseListener ml : openInTextEditor.getMouseListeners()) {
                                openInTextEditor.removeMouseListener(ml);
                            }
                            openInTextEditor.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {

                                    try {
                                        java.awt.Desktop.getDesktop().edit(f);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    popupMenu.setVisible(false);
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {

                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {

                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {

                                }

                                @Override
                                public void mouseExited(MouseEvent e) {

                                }

                            });
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());

                        } else if (f.getName().toLowerCase().endsWith(".opf")) {
                            popupMenu.add(openInDatavyu);
                            for (MouseListener ml : openInDatavyu.getMouseListeners()) {
                                openInDatavyu.removeMouseListener(ml);
                            }
                            openInDatavyu.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {

                                    try {
                                        open(f);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    popupMenu.setVisible(false);
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {

                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {

                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {

                                }

                                @Override
                                public void mouseExited(MouseEvent e) {

                                }
                            });
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());

                        }


                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        favDrawer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selRow = favDrawer.getRowForLocation(e.getX(), e.getY());
                final TreePath selPath = favDrawer.getPathForLocation(e.getX(), e.getY());

                if (selRow != -1) {
                    String path = convertTreePathToString(selPath);
                    String baseDir = Configuration.getInstance().getFavouritesFolder();
                    baseDir = baseDir.substring(0, baseDir.lastIndexOf(File.separator));
                    final File f = new File(baseDir + File.separator + path);

                    if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 1) {
                    } else if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {

                        if (f.isFile()) {
                            if (f.getName().toLowerCase().endsWith(".rb")) {
                                runScript(f);
                            }
                            if (f.getName().toLowerCase().endsWith(".opf")) {
                                open(f);
                            }
                        }
                    } else if (SwingUtilities.isRightMouseButton(e)) {
                        int row = favDrawer.getClosestRowForLocation(e.getX(), e.getY());
                        favDrawer.setSelectionRow(row);
                        popupMenu.removeAll();
                        if (f.getName().toLowerCase().endsWith(".rb")) {
                            popupMenu.add(openInTextEditor);
                            for (MouseListener ml : openInTextEditor.getMouseListeners()) {
                                openInTextEditor.removeMouseListener(ml);
                            }
                            openInTextEditor.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {

                                    try {
                                        java.awt.Desktop.getDesktop().edit(f);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    popupMenu.setVisible(false);
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {

                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {

                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {

                                }

                                @Override
                                public void mouseExited(MouseEvent e) {

                                }

                            });
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());

                        } else if (f.getName().toLowerCase().endsWith(".opf")) {
                            popupMenu.add(openInDatavyu);
                            for (MouseListener ml : openInDatavyu.getMouseListeners()) {
                                openInDatavyu.removeMouseListener(ml);
                            }
                            openInDatavyu.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {

                                    try {
                                        open(f);
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }
                                    popupMenu.setVisible(false);
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {

                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {

                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {

                                }

                                @Override
                                public void mouseExited(MouseEvent e) {

                                }
                            });
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());

                        }


                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        fileScrollPane = new JScrollPane(fileDrawer);
        favScrollPane = new JScrollPane(favDrawer);
        updateFavDrawerLabel();

        fileSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, fileScrollPane, favScrollPane);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, fileSplitPane, tabbedPane);
        splitPane.setDividerLocation(150);
        Dimension minimumSize = new Dimension(100, 50);

        fileScrollPane.setMinimumSize(minimumSize);
        fileScrollPane.setMaximumSize(minimumSize);
        tabbedPane.setMinimumSize(minimumSize);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane t = (JTabbedPane) e.getSource();

                for (Component tab : t.getComponents()) {
                    if (tab instanceof SpreadsheetPanel) {
                        DataControllerV dv = ((SpreadsheetPanel) tab).getDataController();

                        dv.stopAction();
                        for (DataViewer d : dv.getDataViewers()) {
                            d.setDataViewerVisible(false);
                        }
                        dv.setVisible(false);
                    }
                }

                if (t.getComponentCount() > 0 && t.getSelectedIndex() >= 0) {

                    SpreadsheetPanel sp = (SpreadsheetPanel) t.getSelectedComponent();
                    if (Datavyu.getView() != null) {
                        Datavyu.setProjectController(sp.getProjectController());

                        Datavyu.getDataController().setVisible(false);
                        Datavyu.setDataController(sp.getDataController());
                        Datavyu.getView().panel = sp;
                        sp.revalidate();
                        Datavyu.getView().tabbedPane.revalidate();

                        if (sp.getDataController().shouldBeVisible()) {
                            sp.getDataController().setVisible(true);

                            for (DataViewer d : sp.getDataController().getDataViewers()) {
                                d.setDataViewerVisible(true);
                            }
                        }
                    }

                    String dir = sp.getProjectController().getProject().getProjectDirectory();
                    if (dir == null) {
                        dir = System.getProperty("user.home");
                    }
                    fileTree = new FileSystemTreeModel(new File(dir));
                    fileDrawer.setModel(fileTree);
                    updateTitle();
//                    if(spread)
                    vec.updateView();

//                    fileScrollPane = new JScrollPane(fileDrawer);


                }
            }
        });

        setComponent(splitPane);

        panel = new SpreadsheetPanel(new ProjectController(), null);
        panel.getProjectController().setSpreadsheetPanel(panel);
        panel.setDataController(new DataControllerV(Datavyu.getApplication().getMainFrame(), false));
//        panel.getProjectController().setDatastore(panel.getDatastore());

        Datavyu.setProjectController(panel.getProjectController());
        panel.registerListeners();
        panel.addFileDropEventListener(this);

        tabbedPane.add(panel);
        tabbedPane.setSelectedComponent(panel);
        tabbedPane.setTabComponentAt(0, new TabWithCloseButton(tabbedPane));

        // initialize the undo/redo system
        spreadsheetUndoManager = new SpreadsheetUndoManager();
        undoSupport = new UndoableEditSupport();
        undoSupport.addUndoableEditListener(new UndoAdapter());
        refreshUndoRedo();
        //////

        //Jakrabbit Menu
        pushMenuItem.setVisible(false);
        pullMenuItem.setVisible(false);
        jSeparator10.setVisible(false);

        this.getFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        WindowListener exitListener = new WindowAdapter() {
            @Override
            public void windowIconified(WindowEvent e) {
                Datavyu.getApplication().getMainFrame().setState(e.WINDOW_ICONIFIED);
                Datavyu.getDataController().setVisible(false);
                for (DataViewer dv : Datavyu.getDataController().getDataViewers()) {
                    dv.setDataViewerVisible(false);
                }
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
                Datavyu.getApplication().getMainFrame().setState(e.WINDOW_DEICONIFIED);
                Datavyu.getDataController().setVisible(true);
                for (DataViewer dv : Datavyu.getDataController().getDataViewers()) {
                    dv.setDataViewerVisible(true);
                }
            }

            @Override
            public void windowClosing(WindowEvent e) {
                Datavyu.getApplication().exit();
            }
        };
        this.getFrame().addWindowListener(exitListener);
    }

    public SpreadsheetUndoManager getSpreadsheetUndoManager() {
        return spreadsheetUndoManager;
    }

    public UndoableEditSupport getUndoSupport() {
        return undoSupport;
    }

    @Override
    public javax.swing.JComponent getComponent() {
        return (javax.swing.JComponent) tabbedPane.getSelectedComponent();
    }


    public void checkForAutosavedFile() {
        // Check for autosaved file (crash condition)
        try {
            File tempfile = File.createTempFile("test", "");
            String path = FilenameUtils.getFullPath(tempfile.getPath());
            tempfile.delete();
            File folder = new File(path);
            File[] listOfFiles = folder.listFiles();
            for (File f : listOfFiles) {
                if ((f.isFile()) &&
                        (
                                (FilenameUtils.wildcardMatchOnSystem(f.getName(), "~*.opf")) ||
                                        (FilenameUtils.wildcardMatchOnSystem(f.getName(), "~*.csv"))
                        )
                        ) { // the last time datavyu crashed

                    // Show the Dialog
                    if (JOptionPane.showConfirmDialog(null,
                            "Datavyu has detected an unsaved file. Would you like recover this file ?",
                            "Datavyu",
                            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                        openRecoveredFile(f);
                        this.saveAs();
                    }
                    // delete the recovered file
                    f.delete();
                }
            }
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(DatavyuView.class.getName()).log(Level.SEVERE, null, ex);
        }


        // initialize autosave feature
        AutosaveC.setInterval(1); // five minutes
    }


    /**
     * Update the title of the application.
     */
    public void updateTitle() {
        // Show the project name instead of database.
        JFrame mainFrame = Datavyu.getApplication().getMainFrame();
        ResourceMap rMap = Datavyu.getApplication().getContext()
                .getResourceMap(Datavyu.class);
        String postFix = "  ";
        ProjectController projectController = getSpreadsheetPanel().getProjectController();

        if (projectController.isChanged()) {
            postFix = "*";
        }

        String extension = "";
        final FileFilter lastSaveOption = projectController.getLastSaveOption();

        if (lastSaveOption instanceof SHAPAFilter) {
            extension = ".shapa";
        } else if (lastSaveOption instanceof CSVFilter) {
            extension = ".csv";
        } else if (lastSaveOption instanceof MODBFilter) {
            extension = ".odb";
        } else if (lastSaveOption instanceof OPFFilter) {
            extension = ".opf";
        }

        String projectName = projectController.getProjectName();
        if (projectName == null) {
            projectName = "untitled";
        }


        String title = rMap.getString("Application.title") + " - " + projectName + extension + postFix;
        String tabTitle = projectName + postFix;

        if(isQuickKeyMode()) {
            title = title + " <QUICK KEY MODE>";
        }

        if(isHighlightAndFocusMode()) {
            title = title + " <HIGHLIGHT AND FOCUS MODE>";
        }

        Datavyu.getProjectController().getProject().setDatabaseFileName(projectName + extension);
        mainFrame.setTitle(title);
        this.getSpreadsheetPanel().setName(tabTitle);
        tabbedPane.setTitleAt(tabbedPane.getSelectedIndex(), tabTitle);
//        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.getFrame().setTitle(title);
    }


    /**
     * Action for creating a new project.
     */
    @Action
    public void showNewProjectForm() {

//        if (Datavyu.getApplication().safeQuit()) {
        new NewProjectC();
        // Reset the undo manager
//            resetUndoManager();
//        }
    }

    /**
     * Action for saving the current database as a file.
     */
    @Action
    public void save() {

        try {
            SaveC saveC = new SaveC();

            // If the user has not saved before - invoke the saveAs()
            // controller to force the user to nominate a destination file.
            ProjectController projController = Datavyu.getProjectController();

            if (projController.isNewProject()
                    || (projController.getProjectName() == null)) {
                saveAs();
            } else {
                SaveC saveController = new SaveC();

                // Force people to use new
                if ((projController.getLastSaveOption() instanceof SHAPAFilter)
                        || (projController.getLastSaveOption()
                        instanceof OPFFilter)) {

                    // BugzID:1804 - Need to store the original absolute path of
                    // the
                    // project file so that we can build relative paths to
                    // search when
                    // loading, if the project file is moved around.
                    projController.setOriginalProjectDirectory(
                            projController.getProjectDirectory());

                    projController.updateProject();
                    projController.setLastSaveOption(OPFFilter.INSTANCE);

                    saveController.saveProject(new File(projController.getProjectDirectory(),
                                    projController.getProjectName() + ".opf"),
                            projController.getProject(),
                            projController.getDB()
                    );

                    projController.markProjectAsUnchanged();
                    projController.getDB().markAsUnchanged();

                    // Save content just as a database.
                } else {
                    File file = new File(projController.getProjectDirectory(),
                            projController.getDatabaseFileName());
                    saveC.saveDatabase(file, projController.getDB());

                    projController.markProjectAsUnchanged();

                    projController.getDB().markAsUnchanged();

                }
            }

        } catch (UserWarningException e) {
            e.printStackTrace();
            Datavyu.getApplication().showWarningDialog(e);
        }
    }

    /**
     * Action for saving the current project as a particular file.
     */
    @Action
    public void saveAs() {
        DatavyuFileChooser jd = new DatavyuFileChooser();

        jd.addChoosableFileFilter(MODBFilter.INSTANCE);
        jd.addChoosableFileFilter(OPFFilter.INSTANCE);

        jd.setAcceptAllFileFilterUsed(false);
        jd.setFileFilter(OPFFilter.INSTANCE);

        int result = jd.showSaveDialog(getComponent());

        if (result == JFileChooser.APPROVE_OPTION) {
            save(jd);
        }
        fileTree = new FileSystemTreeModel(new File(jd.getSelectedFile().getParent()));
        fileDrawer.setModel(fileTree);
        updateTitle();
    }

    @Action
    public void toggleQuickKeys() {
        quickKeyMode = !quickKeyMode;
        if(quickKeyMode) {
            quickkeysMenuItem.setText("Disable Quick Key Mode");

        } else {
            quickkeysMenuItem.setText("Enable Quick Key Mode");
        }

        updateTitle();
    }

    @Action
    public void toggleHighlightAndFocusMode() {
        Datavyu.getDataController().getMixerController().enableHighlightAndFocusHandler(null);
        if(Datavyu.getDataController().getCellHighlightAndFocus()) {
            highlightAndFocusMenuItem.setText("Disable Highlight and Focus Mode");

        } else {
            highlightAndFocusMenuItem.setText("Enable Highlight and Focus Mode");
        }

        updateTitle();
    }

    public boolean isQuickKeyMode() {
        return quickKeyMode;
    }

    public boolean isHighlightAndFocusMode() {
        if(Datavyu.getDataController() != null) {
            return Datavyu.getDataController().getCellHighlightAndFocus();
        } else {
            return false;
        }
    }


    public JSplitPane getFileSplitPane() {
        return fileSplitPane;
    }


    /**
     * Action for exporting the current project as a particular file.
     */
    @Action
    public void exportFile() {
        DatavyuFileChooser jd = new DatavyuFileChooser();

        jd.addChoosableFileFilter(CellCSVFilter.INSTANCE);
        jd.setFileFilter(CellCSVFilter.INSTANCE);

        int result = jd.showSaveDialog(getComponent());

        if (result == JFileChooser.APPROVE_OPTION) {
            exportToCSV(jd);
        }
    }

    private void exportToCSV(final DatavyuFileChooser fc) {
        ProjectController projController = Datavyu.getProjectController();
        projController.updateProject();

        try {
            ExportDatabaseFileC exportC = new ExportDatabaseFileC();

            FileFilter filter = fc.getFileFilter();
            String dbFileName = fc.getSelectedFile().getPath();
            if (!dbFileName.endsWith(".csv")) {
                dbFileName = dbFileName.concat(".csv");
            }

            // Only save if the project file does not exists or if the user
            // confirms a file overwrite in the case that the file exists.
            if (!canSave(fc.getSelectedFile().getParent(), dbFileName)) {
                return;
            }
            File f = new File(fc.getSelectedFile().getParent(), dbFileName);

            exportC.exportAsCells(dbFileName, projController.getDB());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Action for exporting the current project as a particular file.
     */
    @Action
    public void exportFileByFrame() {
        DatavyuFileChooser jd = new DatavyuFileChooser();

        jd.addChoosableFileFilter(FrameCSVFilter.INSTANCE);
        jd.setFileFilter(FrameCSVFilter.INSTANCE);

        int result = jd.showSaveDialog(getComponent());

        if (result == JFileChooser.APPROVE_OPTION) {
            exportToCSVByFrame(jd);
        }
    }

    private void exportToCSVByFrame(final DatavyuFileChooser fc) {
        ProjectController projController = Datavyu.getProjectController();
        projController.updateProject();

        try {
            ExportDatabaseFileC exportC = new ExportDatabaseFileC();

            FileFilter filter = fc.getFileFilter();
            String dbFileName = fc.getSelectedFile().getPath();
            if (!dbFileName.endsWith(".csv")) {
                dbFileName = dbFileName.concat(".csv");
            }

            // Only save if the project file does not exists or if the user
            // confirms a file overwrite in the case that the file exists.
            if (!canSave(fc.getSelectedFile().getParent(), dbFileName)) {
                return;
            }
            File f = new File(fc.getSelectedFile().getParent(), dbFileName);

            exportC.exportByFrame(dbFileName, projController.getDB());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean canSave(final String directory, final String file) {
        File newFile = new File(directory, file);

        return ((newFile.exists()
                && Datavyu.getApplication().overwriteExisting())
                || !newFile.exists());
    }

    private void export(final DatavyuFileChooser fc) {
        ProjectController projController = Datavyu.getProjectController();
        projController.updateProject();

        try {
            SaveC saveC = new SaveC();

            FileFilter filter = fc.getFileFilter();

            if (filter instanceof CSVFilter) {
                String dbFileName = fc.getSelectedFile().getName();

                if (!dbFileName.endsWith(".csv")) {
                    dbFileName = dbFileName.concat(".csv");
                }

                // Only save if the project file does not exists or if the user
                // confirms a file overwrite in the case that the file exists.
                if (!canSave(fc.getSelectedFile().getParent(), dbFileName)) {
                    return;
                }

                File f = new File(fc.getSelectedFile().getParent(), dbFileName);
                saveC.saveDatabase(f, projController.getDB());

                projController.getDB().setName(dbFileName);
                projController.setProjectName(dbFileName);
                projController.setProjectDirectory(fc.getSelectedFile().getParent());
                projController.setDatabaseFileName(dbFileName);

                // Save as a ODB database
            } else if (filter instanceof MODBFilter) {
                String dbFileName = fc.getSelectedFile().getName();

                if (!dbFileName.endsWith(".odb")) {
                    dbFileName = dbFileName.concat(".odb");
                }

                // Only save if the project file does not exists or if the user
                // confirms a file overwrite in the case that the file exists.
                if (!canSave(fc.getSelectedFile().getParent(), dbFileName)) {
                    return;
                }

                File f = new File(fc.getSelectedFile().getParent(), dbFileName);
                saveC.saveDatabase(f, projController.getDB());

                if (dbFileName.lastIndexOf('.') != -1) {
                    dbFileName = dbFileName.substring(0,
                            dbFileName.lastIndexOf('.'));
                }

                projController.getDB().setName(dbFileName);
                projController.setProjectDirectory(fc.getSelectedFile()
                        .getParent());
                projController.setDatabaseFileName(dbFileName);

                // Save as a project
            } else if (filter instanceof OPFFilter) {
                String archiveName = fc.getSelectedFile().getName();

                if (!archiveName.endsWith(".opf")) {
                    archiveName = archiveName.concat(".opf");
                }

                // Only save if the project file does not exists or if the user
                // confirms a file overwrite in the case that the file exists.
                if (!canSave(fc.getSelectedFile().getParent(), archiveName)) {
                    return;
                }

                // Send it off to the controller
                projController.setProjectName(archiveName);

                // BugzID:1804 - Need to store the original absolute path of the
                // project file so that we can build relative paths to search
                // when
                // loading, if the project file is moved around.
                projController.setOriginalProjectDirectory(fc.getSelectedFile()
                        .getParent());

                projController.updateProject();
                saveC.saveProject(new File(fc.getSelectedFile().getParent(),
                                archiveName),
                        projController.getProject(),
                        projController.getDB()
                );
                projController.setProjectDirectory(fc.getSelectedFile().getParent());

            }

            projController.setLastSaveOption(filter);
            projController.markProjectAsUnchanged();
            projController.getDB().markAsUnchanged();

        } catch (UserWarningException e) {
            Datavyu.getApplication().showWarningDialog(e);
        }
    }

    private void save(final DatavyuFileChooser fc) {
        ProjectController projController = Datavyu.getProjectController();
        projController.updateProject();

        try {
            SaveC saveC = new SaveC();

            FileFilter filter = fc.getFileFilter();

            if (filter instanceof CSVFilter) {
                String dbFileName = fc.getSelectedFile().getName();

                if (!dbFileName.endsWith(".csv")) {
                    dbFileName = dbFileName.concat(".csv");
                }

                // Only save if the project file does not exists or if the user
                // confirms a file overwrite in the case that the file exists.
                if (!canSave(fc.getSelectedFile().getParent(), dbFileName)) {
                    return;
                }

                File f = new File(fc.getSelectedFile().getParent(), dbFileName);
                saveC.saveDatabase(f, projController.getDB());

                projController.getDB().setName(dbFileName);
                projController.setProjectName(dbFileName);
                projController.setProjectDirectory(fc.getSelectedFile().getParent());
                projController.setDatabaseFileName(dbFileName);

                // Save as a ODB database
            } else if (filter instanceof MODBFilter) {
                String dbFileName = fc.getSelectedFile().getName();

                if (!dbFileName.endsWith(".odb")) {
                    dbFileName = dbFileName.concat(".odb");
                }

                // Only save if the project file does not exists or if the user
                // confirms a file overwrite in the case that the file exists.
                if (!canSave(fc.getSelectedFile().getParent(), dbFileName)) {
                    return;
                }

                File f = new File(fc.getSelectedFile().getParent(), dbFileName);
                saveC.saveDatabase(f, projController.getDB());

                if (dbFileName.lastIndexOf('.') != -1) {
                    dbFileName = dbFileName.substring(0,
                            dbFileName.lastIndexOf('.'));
                }

                projController.getDB().setName(dbFileName);
                projController.setProjectName(dbFileName);
                projController.setProjectDirectory(fc.getSelectedFile()
                        .getParent());
                projController.setDatabaseFileName(dbFileName);

                // Save as a project
            } else if (filter instanceof OPFFilter) {
                String archiveName = fc.getSelectedFile().getName();

                if (!archiveName.endsWith(".opf")) {
                    archiveName = archiveName.concat(".opf");
                }

                // Only save if the project file does not exists or if the user
                // confirms a file overwrite in the case that the file exists.
                if (!canSave(fc.getSelectedFile().getParent(), archiveName)) {
                    return;
                }

                // Send it off to the controller
                projController.setProjectName(archiveName);

                // BugzID:1804 - Need to store the original absolute path of the
                // project file so that we can build relative paths to search
                // when
                // loading, if the project file is moved around.
                projController.setOriginalProjectDirectory(fc.getSelectedFile()
                        .getParent());

                projController.updateProject();

                saveC.saveProject(new File(fc.getSelectedFile().getParent(),
                                archiveName),
                        projController.getProject(),
                        projController.getDB()
                );
                projController.getDB().setName(fc.getSelectedFile().getName());
                projController.setProjectDirectory(fc.getSelectedFile().getParent());
                projController.setDatabaseFileName(archiveName);

            }

            projController.setLastSaveOption(filter);
            projController.markProjectAsUnchanged();
            projController.getDB().markAsUnchanged();
            this.tabbedPane.setTitleAt(this.tabbedPane.getSelectedIndex(), projController.getDB().getName());

        } catch (UserWarningException e) {
            Datavyu.getApplication().showWarningDialog(e);
        }
    }

    public String convertTreePathToString(TreePath tp) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tp.getPath().length; i++) {
            sb.append(File.separatorChar).append(tp.getPath()[i].toString());
        }
        String path = sb.toString();
        return path;
    }

    /**
     * Action for loading a Datavyu project from disk.
     */
    @Action
    public void open() {

        DatavyuFileChooser jd = new DatavyuFileChooser();

        jd.addChoosableFileFilter(SHAPAFilter.INSTANCE);
        jd.addChoosableFileFilter(OPFFilter.INSTANCE);

        jd.setFileFilter(OPFFilter.INSTANCE);
        int result = jd.showOpenDialog(getComponent());

        if (result == JFileChooser.APPROVE_OPTION) {
            open(jd.getSelectedFile());
        }
    }

    /**
     * Simulate loading an Datavyu project from file chooser.
     */
    public void open(final File file) {

        DatavyuFileChooser fc = new DatavyuFileChooser();
        fc.setVisible(false);
        fc.setSelectedFile(file);


        try {
            System.out.println(file.getCanonicalPath());


            if (checkIfFileAlreadyOpen(file.getCanonicalPath())) {
                Datavyu.getApplication().showWarningDialog("Error: File already open.");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String ext = FilenameUtils.getExtension(file.getAbsolutePath());
        if ("shapa".equalsIgnoreCase(ext)) {
            fc.setFileFilter(SHAPAFilter.INSTANCE);
            open(fc);
        } else if ("csv".equalsIgnoreCase(ext)) {
            fc.setFileFilter(CSVFilter.INSTANCE);
            open(fc);
        } else if ("odb".equalsIgnoreCase(ext)) {
            fc.setFileFilter(MODBFilter.INSTANCE);
            open(fc);
        } else if ("opf".equalsIgnoreCase(ext)) {
            fc.setFileFilter(OPFFilter.INSTANCE);
            open(fc);
        }
    }

    /**
     * Helper method for opening a file from disk.
     *
     * @param jd The file chooser to use.
     */
    private void open(final DatavyuFileChooser jd) {

        JFrame mainFrame = Datavyu.getApplication().getMainFrame();
        progressBar = new DVProgressBar(this.getFrame(), false);

        task = new OpenTask(jd);
        task.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                String test = "";
                if ("progress".equals(evt.getPropertyName())) {
                    int val = (Integer) evt.getNewValue();
                    String msg;
                    switch (val) {
                        case 0:
                            msg = "Preparing spreadsheet";
                            break;
                        case 10:
                            msg = "Opening project";
                            break;
                        case 40:
                            msg = "Project opened";
                            break;
                        case 50:
                            msg = "Loading project into spreadsheet";
                            break;
                        case 100:
                            msg = "Completed!";
                            break;
                        default:
                            msg = "Error loading project!";
                    }

                    progressBar.setProgress(val, msg);
                }
            }
        });
        task.execute();
        try {
            ProjectController p = task.get();
            if (p != null) {
                Datavyu.setProjectController(p);
                createNewSpreadsheet(p);
            }

            progressBar.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void openExternalFile(final File f) {
//        Datavyu.getApplication().resetApp();

        DatavyuFileChooser jd = new DatavyuFileChooser();
        jd.setSelectedFile(f);
        jd.setFileFilter(OPFFilter.INSTANCE);

        open(jd);
    }

    /**
     * Method for opening a recovered file from disk.
     *
     * @param f The file to open.
     */
    public void openRecoveredFile(final File f) {
        // Clear the current spreadsheet before loading the new content - we
        // need to clean up resources.
//        clearSpreadsheet();
        String filename = FilenameUtils.getBaseName(f.getAbsolutePath());
        String ext = FilenameUtils.getExtension(f.getAbsolutePath());
        //ext = ext.substring(10);
        //f.renameTo(new File(filename + "." + ext));
        // Opening a project or project archive file
        if (ext.equalsIgnoreCase("opf")) {
            openProject(f);
            // Opening a database file
        } else {
            openDatabase(f);
        }

        // Display any changes to the database.
//        showSpreadsheet();

        // Default is to highlight cells when created - clear selection on load.
        panel.clearCellSelection();
    }

    private OpenC openDatabase(final File databaseFile) {

        // Set the database to the freshly loaded database.
        OpenC openC = new OpenC();
        openC.openDatabase(databaseFile);

        // Make a project for the new database.
        if (openC.getDatastore() != null) {
//            Datavyu.newProjectController();

            return openC;
        }
        return null;
    }

    private OpenC openProject(final File projectFile) {
        // Check to make sure that this project file isn't already open

        OpenC openC = new OpenC();
        openC.openProject(projectFile);


        if ((openC.getProject() != null) && (openC.getDatastore() != null)) {

            // Reset the undo manager
//            resetUndoManager();

            return openC;
        }
        return null;
    }

    /**
     * Handles the event for files being dropped onto a component. Only the
     * first file received will be opened.
     *
     * @param evt The event to handle.
     */
    @Override
    public void filesDropped(final FileDropEvent evt) {

        if (!Datavyu.getApplication().safeQuit()) {
            return;
        }

        DatavyuFileChooser fc = new DatavyuFileChooser();
        fc.setVisible(false);

        for (File file : evt.getFiles()) {
            open(file);
        }
    }

    /**
     * Action for creating a new variable.
     */
    @Action
    public void showNewVariableForm() {
        new NewVariableC();
    }

    /**
     * Action for editing vocabs.
     */
    @Action
    public void showVocabEditor() {
        vec.showView();
    }

    /**
     * Action for showing the variable list.
     */
    @Action
    public void showVariableList() {
        Datavyu.getApplication().showVariableList();
    }

    /**
     * Action for showing the quicktime video controller.
     */
    @Action
    public void showQTVideoController() {
        Datavyu.getApplication().showDataController();
    }

    /**
     * Action for showing the video converter dialog.
     */
    @Action
    public void showVideoConverter() {
        Datavyu.getApplication().showVideoConverter();
    }

    /**
     * Action for showing the about window.
     */
    @Action
    public void showAboutWindow() {
        Datavyu.getApplication().showAboutWindow();
    }

    /**
     * Action for opening the support site
     */
    @Action
    public void openSupportSite() {
        Datavyu.getApplication().openSupportSite();
    }

    /**
     * Action for opening the guide
     */
    @Action
    public void openGuideSite() {
        Datavyu.getApplication().openGuideSite();
    }

    /**
     * Action for showing the update window.
     */
    @Action
    public void showUpdateWindow() {
        Datavyu.getApplication().showUpdateWindow();
    }

    /**
     * Action for showing the citation dialog
     */
    @Action
    public void showCitationDialog() {
        ResourceMap rMap = Application.getInstance(Datavyu.class).getContext()
                .getResourceMap(DatavyuView.class);
        JOptionPane.showMessageDialog(null, rMap.getString("citationText.text"), "How to Cite Datavyu", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Action for showing the list of hotkeys
     */
    @Action
    public void showHotkeysDialog() {
        ResourceMap rMap = Application.getInstance(Datavyu.class).getContext()
                .getResourceMap(DatavyuView.class);
        boolean isMac = Datavyu.getPlatform() == Platform.MAC;

        DefaultTableModel tm = new NoEditTableModel();
        tm.addColumn("Action");
        tm.addColumn("Explanation");
        tm.addColumn("Combo");

        String[] row = new String[3];
        String[] actionArr = rMap.getString("hotkeys.action").split(",");
        String[] explanationArr = rMap.getString("hotkeys.explanation").split(",");
        String[] comboArr;
        if (isMac) {
            comboArr = rMap.getString("hotkeys.combo.mac").split(",");
        } else {
            comboArr = rMap.getString("hotkeys.combo.pc").split(",");
        }
        for (int i = 0; i < actionArr.length; i++) {
            row[0] = actionArr[i];
            if (i < explanationArr.length) row[1] = explanationArr[i];
            else row[1] = "";
            if (i < comboArr.length) row[2] = comboArr[i];
            else row[2] = "";
            tm.insertRow(i, row);
        }

        JTable t = new JTable(tm);
        t.getColumnModel().getColumn(0).setPreferredWidth(40);
        t.getColumnModel().getColumn(2).setPreferredWidth(20);
        JScrollPane jp = new JScrollPane(t);
        JPanel p = new JPanel();
        p.setSize(new java.awt.Dimension(700, 600));
        p.add(jp);
        JOptionPane.showMessageDialog(null, p, "Keyboard Shortcuts", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Clears the contents of the spreadsheet.
     */
    public void clearSpreadsheet() {
//        panel.removeAll();

        // Create a freash spreadsheet component and redraw the component.
//        panel.deregisterListeners();
        panel.removeFileDropEventListener(this);
    }

    public ProjectController createNewSpreadsheet(ProjectController pc) {
        Datavyu.setProjectController(pc);

        // Data controller needs to be registered before load for the cell positioning highlighting
        DataControllerV dcv = new DataControllerV(Datavyu.getApplication().getMainFrame(), false);
        Datavyu.setDataController(dcv);


        pc.setSpreadsheetPanel(new SpreadsheetPanel(pc, null));
        SpreadsheetPanel panel = pc.getSpreadsheetPanel();
        panel.registerListeners();
        panel.addFileDropEventListener(this);
        panel.setDataController(dcv);

        tabbedPane.add(panel);
        tabbedPane.setTabComponentAt(tabbedPane.indexOfComponent(panel), new TabWithCloseButton(tabbedPane));
        tabbedPane.setSelectedComponent(panel);
        panel.clearCellSelection();
        setSheetLayout();
        pc.loadProject();

        return pc; //return value not used
    }

    ;

    //no usages as of 5/5/2014
    public ProjectController createNewSpreadsheet(Datastore ds) {
        ProjectController pc = new ProjectController();
        pc.setDatastore(ds);

        return createNewSpreadsheet(pc);
    }

    public ProjectController createNewSpreadsheet(String name) {
        ProjectController pc = new ProjectController();
        pc.setDatastore(DatastoreFactory.newDatastore());
        pc.setProjectName(name);
        pc = createNewSpreadsheet(pc);

        return pc; //return value not used
    }

    /**
     * Action for invoking a script.
     */
    @Action
    public void runScript() {
        try {
            RunScriptC scriptC = new RunScriptC();
            // record the effect
            UndoableEdit edit = new RunScriptEdit(scriptC.getScriptFilePath());
            // notify the listeners
            Datavyu.getView().getUndoSupport().postEdit(edit);
            scriptC.execute();
        } catch (IOException e) {
            LOGGER.error("Unable run script", e);
        }
    }

    public void runScript(File scriptFile) {
        try {
            RunScriptC scriptC = new RunScriptC(scriptFile);
            // record the effect
            UndoableEdit edit = new RunScriptEdit(scriptC.getScriptFilePath());
            // notify the listeners
            Datavyu.getView().getUndoSupport().postEdit(edit);
            scriptC.execute();
        } catch (IOException e) {
            LOGGER.error("Unable run script", e);
        }
    }

    /**
     * Action for setting the favourites folder
     */
    @Action
    public void setFavouritesFolder() {
        try {
            Configuration config = Configuration.getInstance();
            JFileChooser jd = new JFileChooser();
            FileFilter directoryFilter = new FileFilter() {
                public boolean accept(File file) {
                    return file.isDirectory();
                }

                public String getDescription() {
                    return "Select folder for favourite scripts";
                }
            };

            jd.addChoosableFileFilter(directoryFilter);
            jd.setAcceptAllFileFilterUsed(false);
            jd.setFileFilter(directoryFilter);
            jd.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            int result = jd.showOpenDialog(getComponent());
            String val = null;
            if (result == JFileChooser.APPROVE_OPTION) {
                val = jd.getSelectedFile().toString();
            }
            if (!(val == null)) {
                config.setFavouritesFolder(val);
                fav_dir_config = val;
                populateFavourites(null);
                favTree = new FileSystemTreeModel(new File(fav_dir_config));
                favDrawer.setModel(favTree);
                updateFavDrawerLabel();
            }
        } catch (Exception e) {
            LOGGER.error("Unable set folder", e);
        }
    }

    private void updateFavDrawerLabel() {
        favScrollPane.setColumnHeaderView(new JLabel(fav_dir_config));
    }

    /**
     * Action for removing columns from the database.
     */
    @Action
    public void deleteColumn() {
        Datastore ds = Datavyu.getProjectController().getDB();
        List<Variable> selectedVariables = ds.getSelectedVariables();

        // record the effect
        UndoableEdit edit = new RemoveVariableEdit(selectedVariables);

        // perform the operation
        new DeleteColumnC(selectedVariables);

        // notify the listeners
        Datavyu.getView().getUndoSupport().postEdit(edit);
    }

    /**
     * Action for hiding columns.
     */
    @Action
    public void hideColumn() {
        LOGGER.info("Hidding columns");
        Datastore ds = Datavyu.getProjectController().getDB();

        for (Variable var : ds.getSelectedVariables()) {
            var.setHidden(true);
            var.setSelected(false);
        }

        getComponent().revalidate();
    }

    /**
     * Action for showing all columns.
     */
    @Action
    public void showAllColumns() {
        LOGGER.info("Showing all columns");
        Datastore ds = Datavyu.getProjectController().getDB();

        for (Variable var : ds.getAllVariables()) {
            if (var.isHidden()) {
                var.setHidden(false);
            }
        }

        getComponent().revalidate();
    }

    /**
     * Action for changing variable name.
     */
    @Action
    public void changeColumnName() {
        // Only one column should be selected, but just in case, we'll only
        // change the first column
        Variable var = Datavyu.getProjectController().getDB().getSelectedVariables().get(0);

        for (SpreadsheetColumn sCol : panel.getColumns()) {
            if (sCol.getVariable().equals(var)) {
                sCol.showChangeVarNameDialog();

                break;
            }
        }
    }

    /**
     * Action for removing cells from the database.
     */
    @Action
    public void deleteCells() {
        List<Cell> selectedCells = Datavyu.getProjectController()
                .getDB().getSelectedCells();

        // record the effect
        UndoableEdit edit = new RemoveCellEdit(selectedCells);
        // perform the operation
        new DeleteCellC(selectedCells);

        // notify the listeners
        Datavyu.getView().getUndoSupport().postEdit(edit);
    }

    /**
     * Set the SheetLayoutType for the spreadsheet.
     */
    private void setSheetLayout() {
        SheetLayoutType type = SheetLayoutType.Ordinal;

        if (weakTemporalAlignmentMenuItem.isSelected()) {
            type = SheetLayoutType.WeakTemporal;
        }

        panel.setLayoutType(type);
    }

    public boolean getRedraw() {
        return redraw;
    }

    public void setRedraw(boolean b) {
        redraw = b;
    }

    @Action
    public void safeQuit() {
        Datavyu.getApplication().exit();
        System.exit(0);
    }

    public boolean checkAllTabsForChanges() {
        boolean changes = false;
        for (Component tab : tabbedPane.getComponents()) {
            if (tab instanceof SpreadsheetPanel) {
                SpreadsheetPanel sp = (SpreadsheetPanel) tab;
                if (sp.getProjectController().isChanged()) {
                    changes = true;
                }
            }
        }
        return changes;
    }

    public boolean checkIfFileAlreadyOpen(String filepath) {
        for (Component tab : tabbedPane.getComponents()) {
            if (tab instanceof SpreadsheetPanel) {
                SpreadsheetPanel sp = (SpreadsheetPanel) tab;
                if (sp.getProjectController().getFullPath() != null &&
                        sp.getProjectController().getFullPath().equals(filepath)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Undo Action.
     */
    @Action
    public void history() {
        //JOptionPane.showMessageDialog(null, "Undo History.", "History", JOptionPane.INFORMATION_MESSAGE);
        Datavyu.getApplication().showHistory();
    }

    /**
     * Undo Action.
     */
    @Action
    public void undo() {
        spreadsheetUndoManager.undo();
        refreshUndoRedo();
    }

    /**
     * Redo Action.
     */
    @Action
    public void redo() {
        spreadsheetUndoManager.redo();
        refreshUndoRedo();
    }

    /**
     * Push Action.
     */
    @Action
    public void push() {
/*
        System.out.println("push");
        Jackrabbit jr = Jackrabbit.getJackRabbit();
        jr.push();
*/
    }

    /**
     * Redo Action.
     */
    @Action
    public void pull() {
        System.out.println("pull");
/*
        Jackrabbit jr = Jackrabbit.getJackRabbit();
        jr.pull();
*/
    }

    private void resetUndoManager() {
        spreadsheetUndoManager.discardAllEdits();
        refreshUndoRedo();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        openMenuItem = new javax.swing.JMenuItem();
        openRecentFileMenu = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JSeparator();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exportMenuItem = new javax.swing.JMenuItem();
        exportByFrameMenuItem = new javax.swing.JMenuItem();
        javax.swing.JSeparator fileMenuSeparator = new javax.swing.JSeparator();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        spreadsheetMenu = new javax.swing.JMenu();
        showSpreadsheetMenuItem = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        newVariableMenuItem = new javax.swing.JMenuItem();
        vocabEditorMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        newCellMenuItem = new javax.swing.JMenuItem();
        newCellLeftMenuItem = new javax.swing.JMenuItem();
        newCellRightMenuItem = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        changeVarNameMenuItem = new javax.swing.JMenuItem();
        hideSelectedColumnsMenuItem = new javax.swing.JMenuItem();
        ShowAllVariablesMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        deleteColumnMenuItem = new javax.swing.JMenuItem();
        deleteCellMenuItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        historySpreadSheetMenuItem = new javax.swing.JMenuItem();
        undoSpreadSheetMenuItem = new javax.swing.JMenuItem();
        redoSpreadSheetMenuItem = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        weakTemporalAlignmentMenuItem = new javax.swing.JCheckBoxMenuItem();
        zoomMenu = new javax.swing.JMenu();
        zoomInMenuItem = new javax.swing.JMenuItem();
        zoomOutMenuItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        resetZoomMenuItem = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        pushMenuItem = new javax.swing.JMenuItem();
        pullMenuItem = new javax.swing.JMenuItem();
        controllerMenu = new javax.swing.JMenu();
        qtControllerItem = new javax.swing.JMenuItem();
        videoConverterMenuItem = new javax.swing.JMenuItem();
        scriptMenu = new javax.swing.JMenu();
        runScriptMenuItem = new javax.swing.JMenuItem();
        setFavouritesMenuItem = new javax.swing.JMenuItem();
        runRecentScriptMenu = new javax.swing.JMenu();
        recentScriptsHeader = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        favScripts = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        aboutMenuItem = new javax.swing.JMenuItem();
        updateMenuItem = new javax.swing.JMenuItem();
        supportMenuItem = new javax.swing.JMenuItem();
        guideMenuItem = new javax.swing.JMenuItem();
        citationMenuItem = new javax.swing.JMenuItem();
        hotkeysMenuItem = new javax.swing.JMenuItem();
        quickkeysMenuItem = new javax.swing.JMenuItem();
        highlightAndFocusMenuItem = new javax.swing.JMenuItem();

        scriptMenuPermanentsList = new ArrayList();

        mainPanel.setName("mainPanel");

        jLabel1.setName("jLabel1");

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
                mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(mainPanelLayout.createSequentialGroup()
                                .add(119, 119, 119)
                                .add(jLabel1)
                                .addContainerGap(149, Short.MAX_VALUE))
        );
        mainPanelLayout.setVerticalGroup(
                mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(mainPanelLayout.createSequentialGroup()
                                .add(55, 55, 55)
                                .add(jLabel1)
                                .addContainerGap(184, Short.MAX_VALUE))
        );
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(org.datavyu.Datavyu.class).getContext().getResourceMap(DatavyuView.class);
        resourceMap.injectComponents(mainPanel);

        menuBar.setName("menuBar");

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(org.datavyu.Datavyu.class).getContext().getActionMap(DatavyuView.class, this);
        fileMenu.setAction(actionMap.get("saveAs"));
        fileMenu.setName("fileMenu");

        newMenuItem.setAction(actionMap.get("showNewProjectForm"));
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/datavyu/views/resources/DatavyuView");
        newMenuItem.setText(bundle.getString("file_new.text"));
        newMenuItem.setName("newMenuItem");
        fileMenu.add(newMenuItem);

        openMenuItem.setAction(actionMap.get("open"));
        openMenuItem.setText(bundle.getString("file_open.text"));
        openMenuItem.setName(bundle.getString("file_open.text"));
        fileMenu.add(openMenuItem);

        openRecentFileMenu.setName("openRecentFileMenu");
        openRecentFileMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                openRecentFileMenuMenuSelected(evt);
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
        });

        jMenuItem2.setEnabled(false);
        jMenuItem2.setName("jMenuItem2");
        openRecentFileMenu.add(jMenuItem2);

        fileMenu.add(openRecentFileMenu);

        jSeparator7.setName("jSeparator7");
        fileMenu.add(jSeparator7);

        saveMenuItem.setAction(actionMap.get("save"));
        saveMenuItem.setName("saveMenuItem");
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAction(actionMap.get("saveAs"));
        saveAsMenuItem.setName("saveAsMenuItem");
        fileMenu.add(saveAsMenuItem);

        exportMenuItem.setAction(actionMap.get("exportFile"));
        exportMenuItem.setName("exportMenuItem");
        fileMenu.add(exportMenuItem);

        exportByFrameMenuItem.setAction(actionMap.get("exportFileByFrame"));
        exportByFrameMenuItem.setName("exportByFrameMenuItem");
        fileMenu.add(exportByFrameMenuItem); //uncomment this once it works right!

        fileMenuSeparator.setName("fileMenuSeparator");
        if (Datavyu.getPlatform() != Platform.MAC) {
            fileMenu.add(fileMenuSeparator);
        }

        exitMenuItem.setAction(actionMap.get("safeQuit"));
        exitMenuItem.setName("exitMenuItem");
        if (Datavyu.getPlatform() != Platform.MAC) {
            fileMenu.add(exitMenuItem);
        }

        menuBar.add(fileMenu);

        spreadsheetMenu.setAction(actionMap.get("showQTVideoController"));
        spreadsheetMenu.setName("spreadsheetMenu");
        spreadsheetMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                spreadsheetMenuSelected(evt);
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
        });

        showSpreadsheetMenuItem.setAction(actionMap.get("showSpreadsheet"));
        showSpreadsheetMenuItem.setName("showSpreadsheetMenuItem");
        //spreadsheetMenu.add(showSpreadsheetMenuItem);

        jMenuItem1.setAction(actionMap.get("showNewVariableForm"));
        jMenuItem1.setName("jMenuItem1");
        spreadsheetMenu.add(jMenuItem1);

        newVariableMenuItem.setAction(actionMap.get("showVariableList"));
        newVariableMenuItem.setName("newVariableMenuItem");
        spreadsheetMenu.add(newVariableMenuItem);

        vocabEditorMenuItem.setAction(actionMap.get("showVocabEditor"));
        vocabEditorMenuItem.setName("vocabEditorMenuItem");
        spreadsheetMenu.add(vocabEditorMenuItem);

        jSeparator2.setName("jSeparator2");
        spreadsheetMenu.add(jSeparator2);

        newCellMenuItem.setName("newCellMenuItem");
        newCellMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCellMenuItemActionPerformed(evt);
            }
        });
        spreadsheetMenu.add(newCellMenuItem);

        newCellLeftMenuItem.setName("newCellLeftMenuItem");
        newCellLeftMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCellLeftMenuItemActionPerformed(evt);
            }
        });
        spreadsheetMenu.add(newCellLeftMenuItem);

        newCellRightMenuItem.setName("newCellRightMenuItem");
        newCellRightMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newCellRightMenuItemActionPerformed(evt);
            }
        });
        spreadsheetMenu.add(newCellRightMenuItem);

        jSeparator8.setName("jSeparator8");
        spreadsheetMenu.add(jSeparator8);

        changeVarNameMenuItem.setName("changeVarNameMenuItem");
        changeVarNameMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                changeVarNameMenuItemActionPerformed(evt);
            }
        });
        spreadsheetMenu.add(changeVarNameMenuItem);

        hideSelectedColumnsMenuItem.setName("hideSelectedColumnsMenuItem");
        hideSelectedColumnsMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hideSelectedColumnsMenuItemActionPerformed(evt);
            }
        });
        spreadsheetMenu.add(hideSelectedColumnsMenuItem);

        ShowAllVariablesMenuItem.setName("ShowAllVariablesMenuItem");
        ShowAllVariablesMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ShowAllVariablesMenuItemActionPerformed(evt);
            }
        });
        spreadsheetMenu.add(ShowAllVariablesMenuItem);

        jSeparator3.setName("jSeparator3");
        spreadsheetMenu.add(jSeparator3);

        deleteColumnMenuItem.setAction(actionMap.get("deleteColumn"));
        deleteColumnMenuItem.setName("deleteColumnMenuItem");
        spreadsheetMenu.add(deleteColumnMenuItem);

        deleteCellMenuItem.setAction(actionMap.get("deleteCells"));
        deleteCellMenuItem.setName("deleteCellMenuItem");
        spreadsheetMenu.add(deleteCellMenuItem);

        jSeparator6.setName("jSeparator6");
        spreadsheetMenu.add(jSeparator6);

        historySpreadSheetMenuItem.setAction(actionMap.get("history"));
        historySpreadSheetMenuItem.setName("historySpreadSheetMenuItem");
        spreadsheetMenu.add(historySpreadSheetMenuItem);

        undoSpreadSheetMenuItem.setAction(actionMap.get("undo"));
        undoSpreadSheetMenuItem.setName("undoSpreadSheetMenuItem");
        spreadsheetMenu.add(undoSpreadSheetMenuItem);

        redoSpreadSheetMenuItem.setAction(actionMap.get("redo"));
        redoSpreadSheetMenuItem.setName("redoSpreadSheetMenuItem");
        spreadsheetMenu.add(redoSpreadSheetMenuItem);

        jSeparator9.setName("jSeparator9");
        spreadsheetMenu.add(jSeparator9);

        weakTemporalAlignmentMenuItem.setName("weakTemporalAlignmentMenuItem");
        weakTemporalAlignmentMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                weakTemporalMenuItemActionPerformed(evt);
            }
        });
        spreadsheetMenu.add(weakTemporalAlignmentMenuItem);

        zoomMenu.setName("zoomMenu");

        zoomInMenuItem.setName("zoomInMenuItem");
        zoomInMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomInMenuItemActionPerformed(evt);
            }
        });
        zoomMenu.add(zoomInMenuItem);

        zoomOutMenuItem.setName("zoomOutMenuItem");
        zoomOutMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomOutMenuItemActionPerformed(evt);
            }
        });
        zoomMenu.add(zoomOutMenuItem);

        jSeparator5.setName("jSeparator5");
        zoomMenu.add(jSeparator5);

        resetZoomMenuItem.setName("resetZoomMenuItem");
        resetZoomMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                resetZoomMenuItemActionPerformed(evt);
            }
        });
        zoomMenu.add(resetZoomMenuItem);

        spreadsheetMenu.add(zoomMenu);

        jSeparator10.setName("jSeparator10");
        spreadsheetMenu.add(jSeparator10);

        pushMenuItem.setAction(actionMap.get("push"));
        pushMenuItem.setName("pushMenuItem");
        spreadsheetMenu.add(pushMenuItem);

        pullMenuItem.setAction(actionMap.get("pull"));
        pullMenuItem.setName("pullMenuItem");
        spreadsheetMenu.add(pullMenuItem);

        quickkeysMenuItem.setAction(actionMap.get("toggleQuickKeys"));
        quickkeysMenuItem.setName("quickkeysMenuItem");
        quickkeysMenuItem.setText("Enable Quick Key Mode");
        quickkeysMenuItem.setToolTipText("Create new cells in selected column by hitting a single key, filling in the first argument with the key pressed.");

        highlightAndFocusMenuItem.setAction(actionMap.get("toggleHighlightAndFocusMode"));
        highlightAndFocusMenuItem.setName("highlightAndFocusMenuItem");
        highlightAndFocusMenuItem.setText("Enable Highlight and Focus Mode");
        highlightAndFocusMenuItem.setToolTipText("For the selected column, highlight the cell that the video clock is displaying and select the first uncoded argument.");

        spreadsheetMenu.add(quickkeysMenuItem);
        spreadsheetMenu.add(highlightAndFocusMenuItem);

        menuBar.add(spreadsheetMenu);

        controllerMenu.setName("controllerMenu");

        qtControllerItem.setAction(actionMap.get("showQTVideoController"));
        qtControllerItem.setName("qtControllerItem");
        controllerMenu.add(qtControllerItem);

        videoConverterMenuItem.setAction(actionMap.get("showVideoConverter"));
        videoConverterMenuItem.setName("videoConverterMenuItem");
        controllerMenu.add(videoConverterMenuItem);

        menuBar.add(controllerMenu);

        scriptMenu.setName("scriptMenu");
        scriptMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                populateFavourites(evt);
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
        });

        runScriptMenuItem.setAction(actionMap.get("runScript"));
        runScriptMenuItem.setName("runScriptMenuItem");
        scriptMenu.add(runScriptMenuItem);
        scriptMenuPermanentsList.add("runScriptMenuItem");

        runRecentScriptMenu.setName("runRecentScriptMenu");
        runRecentScriptMenu.addMenuListener(new javax.swing.event.MenuListener() {
            public void menuSelected(javax.swing.event.MenuEvent evt) {
                populateRecentScripts(evt);
            }

            public void menuDeselected(javax.swing.event.MenuEvent evt) {
            }

            public void menuCanceled(javax.swing.event.MenuEvent evt) {
            }
        });

        recentScriptsHeader.setEnabled(false);
        recentScriptsHeader.setName("recentScriptsHeader");
        runRecentScriptMenu.add(recentScriptsHeader);

        scriptMenu.add(runRecentScriptMenu);
        scriptMenuPermanentsList.add("runRecentScriptMenu");

        setFavouritesMenuItem.setName("setFavouritesMenuItem");
        setFavouritesMenuItem.setAction(actionMap.get("setFavouritesFolder"));
        scriptMenu.add(setFavouritesMenuItem);
        scriptMenuPermanentsList.add("setFavouritesMenuItem");

        jSeparator4.setName("jSeparator4");
        scriptMenu.add(jSeparator4);
        scriptMenuPermanentsList.add("jSeparator4");

        favScripts.setEnabled(false);
        favScripts.setName("favScripts");
        scriptMenu.add(favScripts);
        scriptMenuPermanentsList.add("favScripts");

        menuBar.add(scriptMenu);

        helpMenu.setName("helpMenu");

        aboutMenuItem.setAction(actionMap.get("showAboutWindow"));
        aboutMenuItem.setName("aboutMenuItem");
        if (Datavyu.getPlatform() != Platform.MAC) {
            helpMenu.add(aboutMenuItem);
        }

        supportMenuItem.setAction(actionMap.get("openSupportSite"));
        supportMenuItem.setName("supportMenuItem");
        //TODO - don't add this on Macs.  Instead it will be in the "Application Menu"
        //if (Datavyu.getPlatform() != Platform.MAC) {
        helpMenu.add(supportMenuItem);
        //}

        guideMenuItem.setAction(actionMap.get("openGuideSite"));
        guideMenuItem.setName("guideMenuItem");
        //TODO - don't add this on Macs.  Instead it will be in the "Application Menu"
        //if (Datavyu.getPlatform() != Platform.MAC) {
        helpMenu.add(guideMenuItem);
        //}

        updateMenuItem.setAction(actionMap.get("showUpdateWindow"));
        updateMenuItem.setName("updateMenuItem");
        //TODO - don't add this on Macs.  Instead it will be in the "Application Menu"
        //if (Datavyu.getPlatform() != Platform.MAC) {
        helpMenu.add(updateMenuItem);
        //}

        citationMenuItem.setAction(actionMap.get("showCitationDialog"));
        citationMenuItem.setName("citationMenuItem");
        helpMenu.add(citationMenuItem);

        hotkeysMenuItem.setAction(actionMap.get("showHotkeysDialog"));
        hotkeysMenuItem.setName("hotkeysMenuItem");
        helpMenu.add(hotkeysMenuItem);

        menuBar.add(helpMenu);
        resourceMap.injectComponents(menuBar);

        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

    private void openRecentFileMenuMenuSelected(
            final javax.swing.event.MenuEvent evt) { // GEN-FIRST:event_openRecentFileMenuMenuSelected

        // Flush the menu - excluding the top menu item.
        int size = openRecentFileMenu.getMenuComponentCount();

        for (int i = 1; i < size; i++) {
            openRecentFileMenu.remove(1);
        }

        for (File file : RecentFiles.getRecentProjects()) {
            openRecentFileMenu.add(createRecentFileMenuItem(file));
        }

    } // GEN-LAST:event_openRecentFileMenuMenuSelected

    private void hideSelectedColumnsMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) { // GEN-FIRST:event_hideSelectedColumnsMenuItemActionPerformed
        hideColumn();
        this.getSpreadsheetPanel().deselectAll();
    } // GEN-LAST:event_hideSelectedColumnsMenuItemActionPerformed

    private void ShowAllVariablesMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) { // GEN-FIRST:event_ShowAllVariablesMenuItemActionPerformed
        showAllColumns();
        this.getSpreadsheetPanel().deselectAll();
    } // GEN-LAST:event_ShowAllVariablesMenuItemActionPerformed

    private void changeVarNameMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) { // GEN-FIRST:event_changeVarNameMenuItemActionPerformed
        changeColumnName();
    } // GEN-LAST:event_changeVarNameMenuItemActionPerformed

    private void tileWindowsMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) { // GEN-FIRST:event_tileWindowsMenuItemActionPerformed
    } // GEN-LAST:event_tileWindowsMenuItemActionPerformed

    /**
     * The action to invoke when the user selects 'weak temporal alignment'.
     *
     * @param evt The event that fired this action.
     */
    private void weakTemporalMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) {
        setRedraw(true);
        // GEN-FIRST:event_weakTemporalMenuItemActionPerformed
        setSheetLayout();
    } // GEN-LAST:event_weakTemporalMenuItemActionPerformed

    /**
     * The action to invoke when the user selects 'recent scripts' from the
     * scripting menu.
     *
     * @param evt The event that fired this action.
     */
    private void populateRecentScripts(final javax.swing.event.MenuEvent evt) { // GEN-FIRST:event_populateRecentScripts

        // Flush the menu - excluding the top menu item.
        int size = runRecentScriptMenu.getMenuComponentCount();

        for (int i = 1; i < size; i++) {
            runRecentScriptMenu.remove(1);
        }

        for (File f : RecentFiles.getRecentScripts()) {
            runRecentScriptMenu.add(createScriptMenuItemFromFile(f));
        }
    } // GEN-LAST:event_populateRecentScripts

    /**
     * The action to invoke when the user selects 'scripts' from the main menu.
     *
     * @param evt The event that fired this action.
     */
    private void populateFavourites(final javax.swing.event.MenuEvent evt) { // GEN-FIRST:event_populateFavourites

        // Favourite script list starts after the 'favScripts' menu item - which
        // is just a stub for a starting point. Search for the favScripts as the
        // starting point for deleting existing scripts from the menu.
        Component[] list = scriptMenu.getMenuComponents();

        for (Component c : list) {
            /*if (!c.getName().equals("runRecentScriptMenu")
                    && !c.getName().equals("runScriptMenuItem")
                    && !c.getName().equals("favScripts")
                    && !c.getName().equals("jSeparator4"))*/
            if (!scriptMenuPermanentsList.contains(c.getName()))
                scriptMenu.remove(c);
        }

        // Get list of favourite scripts from the favourites folder.
        File favouritesDir = new File(fav_dir_config);
        FilenameFilter rubies = new FilenameFilter() {
            public boolean accept(File file, String s) {
                return s.endsWith(".rb");
            }

            ;
        };
        String[] children = favouritesDir.list(rubies);

        if (children != null) {

            for (String s : children) {
                File f = new File(fav_dir_config + File.separatorChar + s);
                scriptMenu.add(createScriptMenuItemFromFile(f));
            }
        }
    } // GEN-LAST:event_populateFavourites

    /**
     * = Function to 'zoom out' (make font size smaller) by ZOOM_INTERVAL
     * points.
     *
     * @param evt The event that triggered this action.
     */
    private void zoomInMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) { // GEN-FIRST:event_zoomInMenuItemActionPerformed
        zoomIn();
    } // GEN-LAST:event_zoomInMenuItemActionPerformed

    public void zoomIn() {
        changeFontSize(ZOOM_INTERVAL);
    }

    /**
     * Function to 'zoom out' (make font size smaller) by ZOOM_INTERVAL points.
     *
     * @param evt
     */
    private void zoomOutMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) { // GEN-FIRST:event_zoomOutMenuItemActionPerformed
        zoomOut();
    } // GEN-LAST:event_zoomOutMenuItemActionPerformed

    public void zoomOut() {
        changeFontSize(-ZOOM_INTERVAL);
    }

    /**
     * Function to reset the zoom level to the default size.
     *
     * @param evt The event that triggered this action.
     */
    private void resetZoomMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) { // GEN-FIRST:event_resetZoomMenuItemActionPerformed

        Configuration config = Configuration.getInstance();
        Font f = config.getSSDataFont();

        changeFontSize(ZOOM_DEFAULT_SIZE - f.getSize());
    } // GEN-LAST:event_resetZoomMenuItemActionPerformed

    /**
     * The method to invoke when the use selects the spreadsheet menu item.
     *
     * @param evt The event that triggered this action.
     */
    private void spreadsheetMenuSelected(
            final javax.swing.event.MenuEvent evt) { // GEN-FIRST:event_spreadsheetMenuMenuSelected

        ResourceMap rMap = Application.getInstance(Datavyu.class).getContext()
                .getResourceMap(DatavyuView.class);

        int totalNumberOfColumns = Datavyu.getProjectController().getDB().getAllVariables().size();

        if (totalNumberOfColumns == 0) {
            newCellMenuItem.setEnabled(false);
        } else {
            newCellMenuItem.setEnabled(true);
        }

        List<Variable> selectedCols = Datavyu.getProjectController().getDB()
                .getSelectedVariables();

        if (selectedCols.isEmpty()) {
            deleteColumnMenuItem.setEnabled(false);
            hideSelectedColumnsMenuItem.setEnabled(false);
            changeVarNameMenuItem.setEnabled(false);
        } else if (selectedCols.size() == 1) {
            deleteColumnMenuItem.setText(rMap.getString(
                    "deleteColumnMenuItemSingle.text"));
            deleteColumnMenuItem.setEnabled(true);
            hideSelectedColumnsMenuItem.setText(rMap.getString(
                    "hideSelectedColumnsMenuItemSingle.text"));
            hideSelectedColumnsMenuItem.setEnabled(true);
            changeVarNameMenuItem.setEnabled(true);
        } else {
            deleteColumnMenuItem.setText(rMap.getString(
                    "deleteColumnMenuItemPlural.text"));
            deleteColumnMenuItem.setEnabled(true);
            hideSelectedColumnsMenuItem.setText(rMap.getString(
                    "hideSelectedColumnsMenuItemPlural.text"));
            hideSelectedColumnsMenuItem.setEnabled(true);
            changeVarNameMenuItem.setEnabled(false);
        }

        List<Cell> selectedCells = Datavyu.getProjectController().getDB()
                .getSelectedCells();

        if (selectedCells.isEmpty()) {
            deleteCellMenuItem.setEnabled(false);
        } else if (selectedCells.size() == 1) {
            deleteCellMenuItem.setText(rMap.getString(
                    "deleteCellMenuItemSingle.text"));
            deleteCellMenuItem.setEnabled(true);
        } else {
            deleteCellMenuItem.setText(rMap.getString(
                    "deleteCellMenuItemPlural.text"));
            deleteCellMenuItem.setEnabled(true);
        }

        if (panel.getAdjacentSelectedCells(ArrayDirection.LEFT) == 0) {
            newCellLeftMenuItem.setEnabled(false);
        } else if (panel.getAdjacentSelectedCells(ArrayDirection.LEFT) == 1) {
            newCellLeftMenuItem.setText(rMap.getString(
                    "newCellLeftMenuItemSingle.text"));
            newCellLeftMenuItem.setEnabled(true);
        } else {
            newCellLeftMenuItem.setText(rMap.getString(
                    "newCellLeftMenuItemPlural.text"));
            newCellLeftMenuItem.setEnabled(true);
        }

        if (panel.getAdjacentSelectedCells(ArrayDirection.RIGHT) == 0) {
            newCellRightMenuItem.setEnabled(false);
        } else if (panel.getAdjacentSelectedCells(ArrayDirection.RIGHT) == 1) {
            newCellRightMenuItem.setText(rMap.getString(
                    "newCellRightMenuItemSingle.text"));
            newCellRightMenuItem.setEnabled(true);
        } else {
            newCellRightMenuItem.setText(rMap.getString(
                    "newCellRightMenuItemPlural.text"));
            newCellRightMenuItem.setEnabled(true);
        }
    } // GEN-LAST:event_spreadsheetMenuMenuSelected

    /**
     * The action to invoke when the user selects new cell from the menu.
     *
     * @param evt The event that fired this action.
     */
    private void newCellMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) { // GEN-FIRST:event_newCellMenuItemActionPerformed
        CreateNewCellC controller = new CreateNewCellC();
        controller.createDefaultCell();
    } // GEN-LAST:event_newCellMenuItemActionPerformed

    /**
     * The action to invoke when the user selects new cell to the left from the
     * menu.
     *
     * @param evt The event that fired this action.
     */
    private void newCellLeftMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) { // GEN-FIRST:event_newCellLeftMenuItemActionPerformed
        newCellLeft();
    } // GEN-LAST:event_newCellLeftMenuItemActionPerformed

    public void newCellLeft() {
        List<Cell> selectedCells = Datavyu.getProjectController()
                .getDB().getSelectedCells();

        new CreateNewCellC(selectedCells, ArrayDirection.LEFT);
    }

    /**
     * The action to invoke when the user selects new cell to the right from the
     * menu.
     *
     * @param evt The event that fired this action.
     */
    private void newCellRightMenuItemActionPerformed(
            final java.awt.event.ActionEvent evt) { // GEN-FIRST:event_newCellRightMenuItemActionPerformed
        newCellRight();
    } // GEN-LAST:event_newCellRightMenuItemActionPerformed

    public void newCellRight() {
        List<Cell> selectedCells = Datavyu.getProjectController()
                .getDB().getSelectedCells();

        new CreateNewCellC(selectedCells, ArrayDirection.RIGHT);
    }

    /**
     * Changes the font size by adding sizeDif to the current size. Then it
     * creates and revalidates a new panel to show the font update. This will
     * not make the font smaller than smallestSize.
     *
     * @param sizeDif The number to add to the current font size.
     */
    public void changeFontSize(final int sizeDif) {
        Configuration config = Configuration.getInstance();
        Font f = config.getSSDataFont();
        int size = f.getSize();
        size = size + sizeDif;

        if (size < ZOOM_MIN_SIZE) {
            size = ZOOM_MIN_SIZE;
        } else if (size > ZOOM_MAX_SIZE) {
            size = ZOOM_MAX_SIZE;
        }

        config.setSSDataFontSize(size);

        // Create and redraw fresh window pane so all of the fonts are new
        // again.
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Creates a new menu item for running a named script.
     *
     * @param f The file to run when menu item is selected.
     * @return The jmenuitem that can be added to a menu.
     */
    public JMenuItem createScriptMenuItemFromFile(final File f) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setText(f.toString());
        menuItem.setName(f.toString());
        menuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(
                    final java.awt.event.ActionEvent evt) {
                runRecentScript(evt);
            }
        });

        return menuItem;
    }

    /**
     * Creates a new menu item for opening a file.
     *
     * @param file The file to open.
     * @return The menu item associated with the file.
     */
    private JMenuItem createRecentFileMenuItem(final File file) {
        JMenuItem menuItem = new JMenuItem();
        menuItem.setText(file.toString());
        menuItem.setName(file.toString());
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(final ActionEvent e) {
                open(file);
            }
        });

        return menuItem;
    }

    /**
     * The action to invoke when the user selects a recent script to run.
     *
     * @param evt The event that triggered this action.
     */
    private void runRecentScript(final java.awt.event.ActionEvent evt) {

        try {
            // record the effect
            UndoableEdit edit = new RunScriptEdit(evt.getActionCommand());
            ////

            RunScriptC scriptC = new RunScriptC(evt.getActionCommand());

            // notify the listeners
            Datavyu.getView().getUndoSupport().postEdit(edit);
            /////

            scriptC.execute();
        } catch (IOException e) {
            LOGGER.error("Unable to run recent script", e);
        }
    }

    /**
     * Returns SpreadsheetPanel
     *
     * @return SpreadsheetPanel panel
     */
    public SpreadsheetPanel getSpreadsheetPanel() {
        return (SpreadsheetPanel) tabbedPane.getSelectedComponent();
    }

    public void refreshUndoRedo() {

        // refresh undo
        undoSpreadSheetMenuItem.setText(spreadsheetUndoManager.getUndoPresentationName());
        undoSpreadSheetMenuItem.setEnabled(spreadsheetUndoManager.canUndo());

        // refresh redo
        redoSpreadSheetMenuItem.setText(spreadsheetUndoManager.getRedoPresentationName());
        redoSpreadSheetMenuItem.setEnabled(spreadsheetUndoManager.canRedo());

        // Display any changes.
        //showSpreadsheet();

        panel.revalidate();
        panel.repaint();

    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    class OpenTask extends SwingWorker<ProjectController, Void> {
        private DatavyuFileChooser jd;


        public OpenTask(final DatavyuFileChooser jd) {

            this.jd = jd;
        }

        @Override
        public ProjectController doInBackground() {


            if (jd != null && !jd.getSelectedFile().exists()) {
                setProgress(2);
                return null;
            }

            if (jd != null &&
                    tabbedPane != null && Datavyu.getProjectController() != null &&
                    tabbedPane.getTabCount() == 1 &&
                    Datavyu.getProjectController().getProjectName() == null &&
                    !Datavyu.getProjectController().isChanged() &&
                    jd.getSelectedFile().exists()) {
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        tabbedPane.remove(0);
                    }
                });
            }


            setProgress(0);
            FileFilter filter = jd.getFileFilter();
//            clearSpreadsheet();

            setProgress(10);
            OpenC openC = null;


//            showSpreadsheet(pController, progressBar);

            if ((filter == SHAPAFilter.INSTANCE) || (filter == OPFFilter.INSTANCE)) {
                // Opening a project or project archive file
                openC = openProject(jd.getSelectedFile());

            } else {
                // Opening a database file
                openC = openDatabase(jd.getSelectedFile());
            }

            if (openC == null) {
                setProgress(1);
                return null;
            }

            ProjectController pController = new ProjectController(openC.getProject(), openC.getDatastore());
            pController.setProjectName(jd.getSelectedFile().getName());

            pController.setLastSaveOption(filter);
            pController.setProjectDirectory(jd.getSelectedFile().getParent());
            pController.setDatabaseFileName(jd.getSelectedFile().getName());

            setProgress(40);

            // BugzID:449 - Set filename in spreadsheet window and database if the database name is undefined.


            // Display any changes to the database.
            setProgress(50);


            /* updates the progressBar up to nearly 100% */

            // Default is to highlight cells when created - clear selection on load.
//            panel.clearCellSelection();

            // The project we just opened doesn't really contain any unsaved changes.
            pController.markProjectAsUnchanged();
            pController.getDB().markAsUnchanged();

            // Update the list of recently opened files.
            RecentFiles.rememberProject(jd.getSelectedFile());

            setProgress(100);

//            progressBar.close();
            return pController;
        }
    }

    private class NoEditTableModel extends DefaultTableModel {
        @Override
        public boolean isCellEditable(int r, int c) {
            return false;
        }
    }

    /**
     * An undo/redo adapter. The adapter is notified when
     * an undo edit occur.
     * The adaptor extract the edit from the event, add it
     * to the UndoManager, and refresh the GUI
     */

    private class UndoAdapter implements UndoableEditListener {
        @Override
        public void undoableEditHappened(UndoableEditEvent evt) {
            UndoableEdit edit = evt.getEdit();
            spreadsheetUndoManager.addEdit(edit);
            refreshUndoRedo();
        }
    }
}
