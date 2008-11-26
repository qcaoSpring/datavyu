package au.com.nicta.openshapa.views;

import au.com.nicta.openshapa.cont.ContinuousDataController;
import au.com.nicta.openshapa.cont.ContinuousDataViewer;
import java.io.File;
import org.apache.log4j.Logger;
import quicktime.QTException;
import quicktime.QTSession;
import quicktime.app.view.QTFactory;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.StdQTConstants;
import quicktime.std.clocks.TimeRecord;
import quicktime.std.movies.Movie;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.Media;
import quicktime.std.movies.media.SampleTimeInfo;

/**
 * The viewer for a quicktime video file.
 *
 * @author cfreeman
 */
public final class QTVideoViewer extends java.awt.Frame
implements ContinuousDataViewer {

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(QTVideoViewer.class);

    /** The quicktime movie this viewer is displaying. */
    private Movie movie;

    /** The visual track for the above quicktime movie. */
    private Track visualTrack;

    /** The visual media for the above visual track. */
    private Media visualMedia;

    /** The controller used to perform actions on this viewer. */
    private ContinuousDataController parentController;

    /** The "normal" playback speed. */
    private static final float NORMAL_SPEED = 1.0f;

    /** Fastforward playback speed. */
    private static final float FFORWARD_SPEED = 32.0f;

    /** Rewind playback speed. */
    private static final float RWIND_SPEED = -32.0f;

    /** conversion factor for converting milliseconds to seconds. */
    private static final float MILLI_TO_SECONDS = 0.001f;

    /** The current shuttle speed. */
    private float shuttleSpeed;

    /**
     * Constructor - creates new video viewer.
     *
     * @param controller The controller invoking actions on this continous
     * data viewer.
     */
    public QTVideoViewer(final ContinuousDataController controller) {
        try {
            movie = null;
            shuttleSpeed = 0.0f;
            parentController = controller;

            // Initalise QTJava.
            QTSession.open();
        } catch (QTException e) {
            logger.error("Unable to create QTVideoViewer", e);
        }
        initComponents();
    }

    /**
     * Method to open a video file for playback.
     *
     * @param videoFile The video file that this viewer is going to display to
     * the user.
     */
    public void setVideoFile(final File videoFile) {
        try {
            OpenMovieFile omf = OpenMovieFile.asRead(new QTFile(videoFile));
            movie = Movie.fromFile(omf);
            visualTrack = movie.getIndTrackType(1,
                                       StdQTConstants.visualMediaCharacteristic,
                                       StdQTConstants.movieTrackCharacteristic);
            visualMedia = visualTrack.getMedia();

            this.add(QTFactory.makeQTComponent(movie).asComponent());
            this.pack();
        } catch (QTException e) {
            logger.error("Unable to setVideoFile", e);
        }
    }

    @Override
    public void createNewCell() {
    }

    @Override
    public void jogBack() {
        try {
            this.jog(-1);
        } catch (QTException e) {
            logger.error("Unable to jogBack", e);
        }
    }

    @Override
    public void stop() {
        try {
            if (movie != null) {
                shuttleSpeed = 0.0f;
                movie.stop();
            }
        } catch (QTException e) {
            logger.error("Unable to stop", e);
        }
    }

    @Override
    public void jogForward() {
        try {
            this.jog(1);
        } catch (QTException e) {
            logger.error("Unable to jogForward", e);
        }
    }

    @Override
    public void shuttleBack() {
        try {
            if (movie != null) {
                if (shuttleSpeed == 0.0f) {
                    shuttleSpeed = 1.0f / RWIND_SPEED;
                } else {
                    shuttleSpeed = shuttleSpeed * 2;
                }
                movie.setRate(shuttleSpeed);
            }
        } catch (QTException e) {
            logger.error("Unable to shuttleBack", e);
        }
    }

    @Override
    public void pause() {
        try {
            if (movie != null) {
                shuttleSpeed = 0.0f;
                movie.stop();
            }
        } catch (QTException e) {
            logger.error("pause", e);
        }
    }

    @Override
    public void shuttleForward() {
        try {
            if (movie != null) {
                if (shuttleSpeed == 0.0f) {
                    shuttleSpeed = 1.0f / FFORWARD_SPEED;
                } else {
                    shuttleSpeed = shuttleSpeed * 2;
                }
                movie.setRate(shuttleSpeed);
            }
        } catch (QTException e) {
            logger.error("Unable to shuttleForward", e);
        }
    }

    @Override
    public void rewind() {
        try {
            if (movie != null) {
                shuttleSpeed = 0.0f;
                movie.setRate(RWIND_SPEED);
            }
        } catch (QTException e) {
            logger.error("Unable to rewind", e);
        }
    }

    @Override
    public void play() {
        try {
            if (movie != null) {
                shuttleSpeed = 0.0f;
                movie.setRate(NORMAL_SPEED);
            }
        } catch (QTException e) {
            logger.error("Unable to play", e);
        }
    }

    @Override
    public void forward() {
        try {
            if (movie != null) {
                shuttleSpeed = 0.0f;
                movie.setRate(FFORWARD_SPEED);
            }
        } catch (QTException e) {
            logger.error("Unable to forward", e);
        }
    }

    @Override
    public void setCellOffset() {
    }

    @Override
    public void find(final long milliseconds) {
        try {
            if (movie != null) {
                shuttleSpeed = 0.0f;
                movie.stop();

                float seconds = milliseconds * MILLI_TO_SECONDS;
                long qtime = (long) seconds * movie.getTimeScale();

                TimeRecord time = new TimeRecord(movie.getTimeScale(), qtime);
                movie.setTime(time);
                pack();
            }
        } catch (QTException e) {
            logger.error("Unable to find", e);
        }
    }

    @Override
    public void goBack(final long milliseconds) {
        try {
            if (movie != null) {
                shuttleSpeed = 0.0f;
                movie.stop();

                float curTime = movie.getTime() / (float) movie.getTimeScale();
                float seconds = milliseconds * MILLI_TO_SECONDS;

                seconds = curTime - seconds;
                long qtime = (long) seconds * movie.getTimeScale();

                TimeRecord time = new TimeRecord(movie.getTimeScale(), qtime);
                movie.setTime(time);
                movie.start();
                pack();
            }
        } catch (QTException e) {
            logger.error("Unable to go back", e);
        }
    }

    @Override
    public void setNewCellOnset() {
    }

    @Override
    public void syncCtrl() {
    }

    @Override
    public void sync() {
    }

    @Override
    public void setCellOnset() {
    }

        /**
     * Jogs the movie by a specified number of frames.
     *
     * @param offset The number of frames to jog the movie by.
     *
     * @throws QTException If unable to jog the movie by the specified number
     * of frames.
     */
    private void jog(final int offset) throws QTException {
        if (movie != null) {
            shuttleSpeed = 0.0f;
            movie.stop();

            // Get the current frame.
            SampleTimeInfo sTime = visualMedia.timeToSampleNum(movie.getTime());

            // Get the time of the next frame.
            int t = visualMedia
                    .sampleNumToMediaTime(sTime.sampleNum + offset).time;
            TimeRecord time = new TimeRecord(movie.getTimeScale(), t);

            // Advance the movie to the next frame.
            movie.setTime(time);
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setName("Form"); // NOI18N
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * The action to invoke when the user closes the viewer.
     *
     * @param evt The event that triggered this action.
     */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        parentController.shutdown(this);        
    }//GEN-LAST:event_exitForm

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
