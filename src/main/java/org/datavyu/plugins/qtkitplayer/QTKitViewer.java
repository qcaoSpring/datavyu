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
package org.datavyu.plugins.qtkitplayer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.datavyu.plugins.quicktime.BaseQuickTimeDataViewer;
import quicktime.std.movies.Track;
import quicktime.std.movies.media.Media;

import java.awt.*;
import java.io.File;


/**
 * The viewer for a quicktime video file.
 * <b>Do not move this class, this is for backward compatibility with 1.07.</b>
 */
public final class QTKitViewer extends BaseQuickTimeDataViewer {

    /**
     * How many milliseconds in a second?
     */
    private static final int MILLI = 1000;
    /**
     * How many frames to check when correcting the FPS.
     */
    private static final int CORRECTIONFRAMES = 5;
    /**
     * The logger for this class.
     */
    private static Logger LOGGER = LogManager.getLogger(QTKitViewer.class);
    private static float FALLBACK_FRAME_RATE = 24.0f;
    long prevSeekTime = -1;
    /**
     * The quicktime movie this viewer is displaying.
     */
    private QTKitPlayer movie;
    /**
     * The visual track for the above quicktime movie.
     */
    private Track visualTrack;
    /**
     * The visual media for the above visual track.
     */
    private Media visualMedia;

    public QTKitViewer(final Frame parent, final boolean modal) {
        super(parent, modal);

        movie = null;
    }

    @Override
    protected void setQTVolume(final float volume) {

        if (movie == null) {
            return;
        }
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                movie.setVolume(volume, movie.id);
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getDuration() {


        return movie.getDuration(movie.id);
    }

    @Override
    protected void setQTDataFeed(final File videoFile) {

        // Ensure that the native hierarchy is set up
        this.addNotify();

        movie = new QTKitPlayer(videoFile);

        this.add(movie, BorderLayout.CENTER);

//        setBounds(getX(), getY(), (int) nativeVideoSize.getWidth(),
//                (int) nativeVideoSize.getHeight());
//


        EventQueue.invokeLater(new Runnable() {
            public void run() {
//                System.out.println(new Dimension(movie.getWidth(), movie.getHeight()));
                try {
                    // Make sure movie is actually loaded
                    movie.setVolume(0.7F, movie.id);
                } catch (Exception e) {
                    // Oops! Back out
                    QTKitPlayer.playerCount -= 1;
                    throw e;
                }
            }
        });

    }

    @Override
    protected Dimension getQTVideoSize() {
        System.err.println(movie.id);
        return new Dimension((int) movie.getMovieWidth(movie.id), (int) movie.getMovieHeight(movie.id));
    }

    @Override
    protected float getQTFPS() {

        return movie.getFPS(movie.id);
    }

    @Override
    public void setPlaybackSpeed(final float rate) {
        super.setPlaybackSpeed(rate);
//        try {
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                movie.setRate(rate, movie.id);
//            }
//        });
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void play() {
        super.play();
        System.err.println("Playing at " + getPlaybackSpeed());

        try {

            if (movie != null) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        if (movie.getRate(movie.id) != 0) {
                            movie.stop(movie.id);
                        }
                        movie.setRate(getPlaybackSpeed(), movie.id);
                    }
                });
            }
        } catch (Exception e) {
            LOGGER.error("Unable to play", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void stop() {
        super.stop();

        System.out.println("HIT STOP");
        final double time = System.currentTimeMillis();
        try {

            if (movie != null) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        System.out.println("EXECUTING STOP");
                        System.out.println(System.currentTimeMillis() - time);
                        movie.stop(movie.id);
                        System.out.println("STOPPED");
                        System.out.println(System.currentTimeMillis() - time);
                    }
                });
            }
        } catch (Exception e) {
            LOGGER.error("Unable to stop", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void seekTo(final long position) {

        try {
            if (movie != null && (prevSeekTime != position)) {
                prevSeekTime = position;
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        boolean wasPlaying = isPlaying();
                        float prevRate = getPlaybackSpeed();
                        if (isPlaying())
                            movie.stop(movie.id);
                        movie.setTime(position, movie.id);
                        if (wasPlaying) {
                            movie.setRate(prevRate, movie.id);
                        }
                    }
                });

            }
        } catch (Exception e) {
            LOGGER.error("Unable to find", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getCurrentTime() {

        try {
            return movie.getCurrentTime(movie.id);
        } catch (Exception e) {
            LOGGER.error("Unable to get time", e);
        }

        return 0;
    }

    @Override
    protected void cleanUp() {
        //TODO
//        movie.release();
    }
}
