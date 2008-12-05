package au.com.nicta.openshapa.views;

import au.com.nicta.openshapa.OpenSHAPA;
import au.com.nicta.openshapa.cont.ContinuousDataController;
import au.com.nicta.openshapa.db.TimeStamp;
import au.com.nicta.openshapa.views.continuous.ContinuousDataViewer;
import au.com.nicta.openshapa.views.continuous.QTVideoViewer;
import java.awt.FileDialog;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import org.apache.log4j.Logger;
import org.jdesktop.application.Action;

/**
 * Quicktime video controller.
 *
 * @author cfreeman
 */
public final class QTVideoController extends OpenSHAPADialog
implements ContinuousDataController {

    /**
     * Constructor. Creates a new QTVideoController.
     *
     * @param parent The parent of this form.
     * @param modal Should the dialog be modal or not?
     */
    public QTVideoController(final java.awt.Frame parent, final boolean modal) {
        super(parent, modal);

        initComponents();
        setName(this.getClass().getSimpleName());
        viewers = new Vector<QTVideoViewer>();
    }

    @Override
    public void setCurrentLocation(final long milliseconds) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
            Date originDate = format.parse("00:00:00:000");
            Date currentTime = new Date(originDate.getTime() + milliseconds);

            this.timestampLabel.setText(format.format(currentTime));
        } catch (ParseException e) {
            logger.error("Unable to set current location", e);
        }
    }

    @Override
    public TimeStamp getCurrentLocation() {
        return (this.currentTimestamp);
    }

    /**
     * Shutdowns the specified viewer.
     *
     * @param viewer The viewer to shutdown.
     */
    @Override
    public void shutdown(final ContinuousDataViewer viewer) {
        for (int i = 0; i < this.viewers.size(); i++) {
            if (viewer == this.viewers.elementAt(i)) {
                this.viewers.elementAt(i).dispose();
                this.viewers.remove(viewer);
            }
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        topPanel = new javax.swing.JPanel();
        timestampLabel = new javax.swing.JLabel();
        openVideoButton = new javax.swing.JButton();
        gridButtonPanel = new javax.swing.JPanel();
        syncCtrlButton = new javax.swing.JButton();
        syncButton = new javax.swing.JButton();
        setCellOnsetButton = new javax.swing.JButton();
        setCellOffsetButton = new javax.swing.JButton();
        rewindButton = new javax.swing.JButton();
        playButton = new javax.swing.JButton();
        forwardButton = new javax.swing.JButton();
        goBackButton = new javax.swing.JButton();
        shuttleBackButton = new javax.swing.JButton();
        pauseButton = new javax.swing.JButton();
        shuttleForwardButton = new javax.swing.JButton();
        findButton = new javax.swing.JButton();
        jogBackButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jogForwardButton = new javax.swing.JButton();
        rightTimePanel = new javax.swing.JPanel();
        syncVideoButton = new javax.swing.JButton();
        goBackTextField = new javax.swing.JTextField();
        findTextField = new javax.swing.JTextField();
        bottomPanel = new javax.swing.JPanel();
        leftButtonPanel = new javax.swing.JPanel();
        createNewCellButton = new javax.swing.JButton();
        setNewCellOnsetButton = new javax.swing.JButton();
        fillerPanel = new javax.swing.JPanel();
        timestampSetupButton = new javax.swing.JButton();
        videoProgressBar = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quicktime Video Controller");
        setName(""); // NOI18N

        mainPanel.setBackground(java.awt.Color.white);
        mainPanel.setLayout(new java.awt.BorderLayout(2, 0));

        topPanel.setBackground(java.awt.Color.white);
        topPanel.setLayout(new java.awt.BorderLayout());

        timestampLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        timestampLabel.setText("00:00:00:000");
        timestampLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        topPanel.add(timestampLabel, java.awt.BorderLayout.CENTER);

        openVideoButton.setBackground(java.awt.Color.white);
        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(au.com.nicta.openshapa.OpenSHAPA.class).getContext().getResourceMap(QTVideoController.class);
        openVideoButton.setText(resourceMap.getString("openVideoButton.text")); // NOI18N
        openVideoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                openVideoButtonActionPerformed(evt);
            }
        });
        topPanel.add(openVideoButton, java.awt.BorderLayout.LINE_START);

        mainPanel.add(topPanel, java.awt.BorderLayout.NORTH);

        gridButtonPanel.setBackground(java.awt.Color.white);
        gridButtonPanel.setLayout(new java.awt.GridLayout(4, 4));

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(au.com.nicta.openshapa.OpenSHAPA.class).getContext().getActionMap(QTVideoController.class, this);
        syncCtrlButton.setAction(actionMap.get("syncCtrlAction")); // NOI18N
        syncCtrlButton.setIcon(resourceMap.getIcon("syncCtrlButton.icon")); // NOI18N
        syncCtrlButton.setMaximumSize(new java.awt.Dimension(32, 32));
        syncCtrlButton.setMinimumSize(new java.awt.Dimension(32, 32));
        syncCtrlButton.setPreferredSize(new java.awt.Dimension(32, 32));
        gridButtonPanel.add(syncCtrlButton);

        syncButton.setAction(actionMap.get("syncAction")); // NOI18N
        syncButton.setIcon(resourceMap.getIcon("syncButton.icon")); // NOI18N
        gridButtonPanel.add(syncButton);

        setCellOnsetButton.setAction(actionMap.get("setCellOnsetAction")); // NOI18N
        setCellOnsetButton.setIcon(resourceMap.getIcon("setCellOnsetButton.icon")); // NOI18N
        gridButtonPanel.add(setCellOnsetButton);

        setCellOffsetButton.setAction(actionMap.get("setCellOffsetAction")); // NOI18N
        setCellOffsetButton.setIcon(resourceMap.getIcon("setCellOffsetButton.icon")); // NOI18N
        gridButtonPanel.add(setCellOffsetButton);

        rewindButton.setAction(actionMap.get("rewindAction")); // NOI18N
        rewindButton.setIcon(resourceMap.getIcon("rewindButton.icon")); // NOI18N
        gridButtonPanel.add(rewindButton);

        playButton.setAction(actionMap.get("playAction")); // NOI18N
        playButton.setIcon(resourceMap.getIcon("playButton.icon")); // NOI18N
        gridButtonPanel.add(playButton);

        forwardButton.setAction(actionMap.get("forwardAction")); // NOI18N
        forwardButton.setIcon(resourceMap.getIcon("forwardButton.icon")); // NOI18N
        gridButtonPanel.add(forwardButton);

        goBackButton.setAction(actionMap.get("goBackAction")); // NOI18N
        goBackButton.setIcon(resourceMap.getIcon("goBackButton.icon")); // NOI18N
        gridButtonPanel.add(goBackButton);

        shuttleBackButton.setAction(actionMap.get("shuttleBackAction")); // NOI18N
        shuttleBackButton.setIcon(resourceMap.getIcon("shuttleBackButton.icon")); // NOI18N
        gridButtonPanel.add(shuttleBackButton);

        pauseButton.setAction(actionMap.get("pauseAction")); // NOI18N
        pauseButton.setIcon(resourceMap.getIcon("pauseButton.icon")); // NOI18N
        gridButtonPanel.add(pauseButton);

        shuttleForwardButton.setAction(actionMap.get("shuttleForwardAction")); // NOI18N
        shuttleForwardButton.setIcon(resourceMap.getIcon("shuttleForwardButton.icon")); // NOI18N
        gridButtonPanel.add(shuttleForwardButton);

        findButton.setAction(actionMap.get("findAction")); // NOI18N
        findButton.setIcon(resourceMap.getIcon("findButton.icon")); // NOI18N
        gridButtonPanel.add(findButton);

        jogBackButton.setAction(actionMap.get("jogBackAction")); // NOI18N
        jogBackButton.setIcon(resourceMap.getIcon("jogBackButton.icon")); // NOI18N
        gridButtonPanel.add(jogBackButton);

        stopButton.setAction(actionMap.get("stopAction")); // NOI18N
        stopButton.setIcon(resourceMap.getIcon("stopButton.icon")); // NOI18N
        gridButtonPanel.add(stopButton);

        jogForwardButton.setAction(actionMap.get("jogForwardAction")); // NOI18N
        jogForwardButton.setIcon(resourceMap.getIcon("jogForwardButton.icon")); // NOI18N
        gridButtonPanel.add(jogForwardButton);

        mainPanel.add(gridButtonPanel, java.awt.BorderLayout.CENTER);

        rightTimePanel.setBackground(java.awt.Color.white);
        rightTimePanel.setLayout(new java.awt.GridLayout(4, 1));

        syncVideoButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/QTVideoController/eng/syncVideoButton.png"))); // NOI18N
        syncVideoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                syncVideoButtonActionPerformed(evt);
            }
        });
        rightTimePanel.add(syncVideoButton);

        goBackTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        goBackTextField.setText("00:00:00:000");
        rightTimePanel.add(goBackTextField);

        findTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        findTextField.setText("00:00:00:000");
        rightTimePanel.add(findTextField);

        mainPanel.add(rightTimePanel, java.awt.BorderLayout.EAST);

        bottomPanel.setBackground(java.awt.Color.white);
        bottomPanel.setLayout(new java.awt.BorderLayout());

        leftButtonPanel.setBackground(java.awt.Color.white);
        leftButtonPanel.setLayout(new java.awt.GridBagLayout());

        createNewCellButton.setAction(actionMap.get("createNewCellAction")); // NOI18N
        createNewCellButton.setIcon(resourceMap.getIcon("createNewCellButton.icon")); // NOI18N
        leftButtonPanel.add(createNewCellButton, new java.awt.GridBagConstraints());

        setNewCellOnsetButton.setAction(actionMap.get("setNewCellStopTime")); // NOI18N
        setNewCellOnsetButton.setIcon(resourceMap.getIcon("setNewCellOnsetButton.icon")); // NOI18N
        leftButtonPanel.add(setNewCellOnsetButton, new java.awt.GridBagConstraints());

        bottomPanel.add(leftButtonPanel, java.awt.BorderLayout.WEST);

        fillerPanel.setBackground(java.awt.Color.white);
        fillerPanel.setLayout(new java.awt.BorderLayout());

        timestampSetupButton.setIcon(resourceMap.getIcon("timestampSetupButton.icon")); // NOI18N
        timestampSetupButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                timestampSetupButtonActionPerformed(evt);
            }
        });
        fillerPanel.add(timestampSetupButton, java.awt.BorderLayout.CENTER);

        bottomPanel.add(fillerPanel, java.awt.BorderLayout.EAST);

        videoProgressBar.setBackground(java.awt.Color.white);
        videoProgressBar.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                videoProgressBarStateChanged(evt);
            }
        });
        bottomPanel.add(videoProgressBar, java.awt.BorderLayout.SOUTH);

        mainPanel.add(bottomPanel, java.awt.BorderLayout.SOUTH);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * Action to invoke when the user clicks on the open button.
     *
     * @param evt The event that triggered this action.
     */
    private void openVideoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_openVideoButtonActionPerformed
        jfc = new FileDialog(this, "Select QuickTime Video File",
                             FileDialog.LOAD);
        jfc.setVisible(true);

        if (jfc.getFile() != null && jfc.getDirectory() != null) {
            QTVideoViewer viewer = new QTVideoViewer(this);
            File f = new File(jfc.getDirectory(), jfc.getFile());
            viewer.setVideoFile(f);
            OpenSHAPA.getApplication().show(viewer);

            // Add the QTVideoViewer to the list of viewers we are controlling.
            this.viewers.add(viewer);
        }
    }//GEN-LAST:event_openVideoButtonActionPerformed

    /**
     * Action to invoke when the user clicks on the sync ctrl button.
     */
    @Action
    public void syncCtrlAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).syncCtrl();
        }
    }

    /**
     * Action to invoke when the user clicks on the sync button.
     */
    @Action
    public void syncAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).sync();
        }
    }


    /**
     * Action to invoke when the user clicks the set cell onset button.
     */
    @Action
    public void setCellOnsetAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).setCellStartTime();
        }
    }

    /**
     * Action to invoke when the user clicks on the set cell offest button.
     */
    @Action
    public void setCellOffsetAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).setCellStopTime();
        }
    }

    /**
     * Action to invoke when the user clicks on the rewind button.
     */
    @Action
    public void rewindAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).rewind();
        }
    }

    /**
     * Action to invoke when the user clicks on the play button.
     */
    @Action
    public void playAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).play();
        }
    }

    /**
     * Action to invoke when the user clicks on the fast foward button.
     */
    @Action
    public void forwardAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).forward();
        }
    }

    /**
     * Action to invoke when the user clicks on the go back button.
     */
    @Action
    public void goBackAction() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
            Date videoDate = format.parse(this.goBackTextField.getText());
            Date originDate = format.parse("00:00:00:000");

            // Determine the time in milliseconds.
            long milli = videoDate.getTime() - originDate.getTime();

            for (int i = 0; i < this.viewers.size(); i++) {
                this.viewers.elementAt(i).goBack(milli);
            }
        } catch (ParseException e) {
            logger.error("unable to find within video", e);
        }
    }

    /**
     * Action to inovke when the user clicks on the shuttle back button.
     */
    @Action
    public void shuttleBackAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).shuttleBack();
        }
    }

    /**
     * Action to invoke when the user clicks on the pause button.
     */
    @Action
    public void pauseAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).pause();
        }
    }

    /**
     * Action to invoke when the user clicks on the shuttle forward button.
     */
    @Action
    public void shuttleForwardAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).shuttleForward();
        }
    }

    /**
     * Action to invoke when the user clicks on the find button.
     */
    @Action
    public void findAction() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:SSS");
            Date videoDate = format.parse(this.findTextField.getText());
            Date originDate = format.parse("00:00:00:000");

            // Determine the time in milliseconds.
            long milli = videoDate.getTime() - originDate.getTime();

            for (int i = 0; i < this.viewers.size(); i++) {
                this.viewers.elementAt(i).find(milli);
            }
        } catch (ParseException e) {
            logger.error("unable to find within video", e);
        }
    }

    /**
     * Action to invoke when the user clicks on the jog backwards button.
     */
    @Action
    public void jogBackAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).jogBack();
        }
    }

    /**
     * Action to invoke when the user clicks on the stop button.
     */
    @Action
    public void stopAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).stop();
        }
    }

    /**
     * Action to invoke when the user clicks on the jog forwards button.
     */
    @Action
    public void jogForwardAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).jogForward();
        }
    }

    /**
     * Action to invoke when the user clicks on the new cell button.
     */
    @Action
    public void createNewCellAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).createNewCell();
        }
    }

    /**
     * Action to invoke when the user clicks on the new cell onset button.
     */
    @Action
    public void setNewCellStopTime() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).setNewCellStopTime();
        }
    }

    /**
     * Action to invoke when the user clicks on the sync video button.
     */
    @Action
    public void syncVideoAction() {
        for (int i = 0; i < this.viewers.size(); i++) {
            this.viewers.elementAt(i).sync();
        }
    }

    private void syncVideoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_syncVideoButtonActionPerformed

    }//GEN-LAST:event_syncVideoButtonActionPerformed

    /**
     * Action to invoke when the user clicks on the time stamp setup button.
     *
     * @param evt The event that triggered this action.
     */
    private void timestampSetupButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_timestampSetupButtonActionPerformed
    }//GEN-LAST:event_timestampSetupButtonActionPerformed

    /**
     * Action to invoke when the video progress bar state changes.
     *
     * @param evt The event that triggered this action.
     */
    private void videoProgressBarStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_videoProgressBarStateChanged
        if (!this.videoProgressBar.getValueIsAdjusting()) {
        }
    }//GEN-LAST:event_videoProgressBarStateChanged
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton createNewCellButton;
    private javax.swing.JPanel fillerPanel;
    private javax.swing.JButton findButton;
    private javax.swing.JTextField findTextField;
    private javax.swing.JButton forwardButton;
    private javax.swing.JButton goBackButton;
    private javax.swing.JTextField goBackTextField;
    private javax.swing.JPanel gridButtonPanel;
    private javax.swing.JButton jogBackButton;
    private javax.swing.JButton jogForwardButton;
    private javax.swing.JPanel leftButtonPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JButton openVideoButton;
    private javax.swing.JButton pauseButton;
    private javax.swing.JButton playButton;
    private javax.swing.JButton rewindButton;
    private javax.swing.JPanel rightTimePanel;
    private javax.swing.JButton setCellOffsetButton;
    private javax.swing.JButton setCellOnsetButton;
    private javax.swing.JButton setNewCellOnsetButton;
    private javax.swing.JButton shuttleBackButton;
    private javax.swing.JButton shuttleForwardButton;
    private javax.swing.JButton stopButton;
    private javax.swing.JButton syncButton;
    private javax.swing.JButton syncCtrlButton;
    private javax.swing.JButton syncVideoButton;
    private javax.swing.JLabel timestampLabel;
    private javax.swing.JButton timestampSetupButton;
    private javax.swing.JPanel topPanel;
    private javax.swing.JSlider videoProgressBar;
    // End of variables declaration//GEN-END:variables

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(QTVideoController.class);

    //protected Executive parent = null;
    //private JButton lastButton = null;

    /** The current time stamp on the quicktime video controller. */
    private TimeStamp currentTimestamp = null;

    /** The list of viewers associated with this controller. */
    private Vector<QTVideoViewer> viewers;

    /** The dialog to present to the user when they desire to load a file. */
    private FileDialog jfc;
}
