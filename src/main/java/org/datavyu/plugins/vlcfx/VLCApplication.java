package org.datavyu.plugins.vlcfx;

import com.sun.jna.Memory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.datavyu.Datavyu;
import uk.co.caprica.vlcj.component.DirectMediaPlayerComponent;
import uk.co.caprica.vlcj.player.direct.BufferFormat;
import uk.co.caprica.vlcj.player.direct.BufferFormatCallback;
import uk.co.caprica.vlcj.player.direct.DirectMediaPlayer;
import uk.co.caprica.vlcj.player.direct.format.RV32BufferFormat;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by jesse on 10/21/14.
 */

public class VLCApplication extends Application {
    static {
        Platform.setImplicitExit(false);
        new uk.co.caprica.vlcj.discovery.NativeDiscovery();
    }
    /**
     * Pixel format.
     */
    private final WritablePixelFormat<ByteBuffer> pixelFormat;
    /**
     *
     */
    private final BorderPane borderPane;
    /**
     * Lightweight JavaFX canvas, the video is rendered here.
     */
    private final Canvas canvas;
    File dataFile;
    boolean init = false;
    /**
     * Target width, unless {@link #useSourceSize} is set.
     */
    private int WIDTH = 1920;
    /**
     * Target height, unless {@link #useSourceSize} is set.
     */
    private int HEIGHT = 1080;
    /**
     * Set this to <code>true</code> to resize the display to the dimensions of the
     * video, otherwise it will use {@link #WIDTH} and {@link #HEIGHT}.
     */
    private boolean useSourceSize = true;
    /**
     * The vlcj direct rendering media player component.
     */
    private TestMediaPlayerComponent mediaPlayerComponent;
    private DirectMediaPlayer mp;
    /**
     *
     */
    private Stage stage;
    /**
     *
     */
    private Scene scene;
    /**
     * Pixel writer to update the canvas.
     */
    private PixelWriter pixelWriter;

    private long duration = -1;

    private long lastVlcUpdateTime = -1;
    private long lastTimeSinceVlcUpdate = -1;

    private float fps;

    private double aspect;

    private long prevSeekTime = -1;


    private boolean assumedFps = false;

    static {
//        String tempDir = System.getProperty("java.io.tmpdir");
//        new NativeLibraryManager(tempDir);
//        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), tempDir);
//        new NativeDiscovery().discover();
    }

    public VLCApplication(File file) {
        dataFile = file;
        canvas = new Canvas();

        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        pixelFormat = PixelFormat.getByteBgraPreInstance();

        borderPane = new BorderPane();
        borderPane.setCenter(canvas);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public void seek(long time) {

        if (prevSeekTime != time || time != mp.getTime()) {
            System.out.println("SEEKING TO " + time);
            mp.setTime(time);
            prevSeekTime = time;
            System.out.println(mp.getTime());
        }
    }

    public void pause() {
        System.out.println(mp.isPlaying());
        if (mp.isPlaying())
            mp.pause();
        System.out.println(mp.isPlaying());
    }

    public void play() {
        mp.play();
    }

    public void stop() {
        mp.stop();
    }

    public long getCurrentTime() {
//        System.out.println("CURRENT TIME " + mp.getTime());
//        System.out.println("DV TIME " + Datavyu.getDataController().getCurrentTime());
//        System.out.println("POSITION " + mp.getPosition());

        long vlcTime = mp.getTime();
        if (vlcTime == lastVlcUpdateTime) {
            long currentTime = System.currentTimeMillis();
            long timeSinceVlcUpdate = lastTimeSinceVlcUpdate - currentTime;
            lastTimeSinceVlcUpdate = currentTime;
            return vlcTime + timeSinceVlcUpdate;
        } else {
            return mp.getTime();
        }
    }

    public float getFrameRate() {
        if (fps == 0.0f) {
            return 30.0f;
        }
        return fps;
    }

    public long getDuration() {
        if (duration < 0) {
            duration = mp.getLength();
        }
        return duration;
    }

    public float getRate() {
        return (float) mp.getRate();
    }

    public void setRate(float rate) {
        mp.setRate(rate);
    }

    public boolean isVisible() { return stage.isShowing(); }

    public void setVisible(final boolean visible) {
        System.out.println("Running " + visible);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println("Setting " + visible);

                if (!visible) {
                    stage.hide();
                } else {
                    stage.show();
                }
            }
        });

    }

    public boolean isAssumedFps() {
        return assumedFps;
    }

    public void setVolume(double volume) {
        mp.setVolume((int) (volume * 200));
    }

    public boolean isInit() {
        return init;
    }

    public void closeAndDestroy() {

        mp.release();
        mediaPlayerComponent.release();

    }

    public boolean isPlaying() {
        return mp.isPlaying();
    }

    public int getHeight() {
        return (int) stage.getHeight();
    }

    public int getWidth() {
        return (int) stage.getWidth();
    }

    public void start(final Stage primaryStage) {

        this.stage = primaryStage;

        stage.setTitle("Datavyu: " + dataFile.getName());

        scene = new Scene(borderPane);

        System.out.println(com.sun.prism.GraphicsPipeline.getPipeline().getClass().getName());


        mediaPlayerComponent = new TestMediaPlayerComponent();
        mp = mediaPlayerComponent.getMediaPlayer();
        mp.prepareMedia(dataFile.getAbsolutePath());

        mp.play();

        // Wait for it to spin up so we can grab metadata
        while (!mp.isPlaying()) {
        }

        primaryStage.setScene(scene);
        primaryStage.show();

        final ChangeListener<Number> listener = new ChangeListener<Number>() {
            final Timer timer = new Timer(); // uses a timer to call your resize method
            final long delayTime = 200; // delay that has to pass in order to consider an operation done
            TimerTask task = null; // task to execute after defined delay

            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, final Number newValue) {
                if (task != null) { // there was already a task scheduled from the previous operation ...
                    task.cancel(); // cancel it, we have a new size to consider
                }

                task = new TimerTask() // create new task that calls your resize operation
                {
                    @Override
                    public void run() {
                        // here you can place your resize code
                        useSourceSize = false;
                        System.out.println("resize to " + primaryStage.getWidth() + " " + primaryStage.getHeight());

                        if (primaryStage.getWidth() > primaryStage.getHeight()) {
                            WIDTH = (int) primaryStage.getWidth();
                            HEIGHT = (int) (primaryStage.getWidth() / aspect);
                        } else {
                            WIDTH = (int) (primaryStage.getHeight() * aspect);
                            HEIGHT = (int) (primaryStage.getHeight());
                        }


                        mediaPlayerComponent.resizePlayer();
                    }
                };
                // schedule new task
                timer.schedule(task, delayTime);
            }
        };

        aspect = primaryStage.getWidth() / primaryStage.getHeight();

        primaryStage.widthProperty().addListener(listener);
        primaryStage.heightProperty().addListener(listener);



        fps = mp.getFps();
        if (fps == 0) {
            assumedFps = true;
        }

        pause();

        mp.setTime(0);

        init = true;

    }

    private class TestMediaPlayerComponent extends DirectMediaPlayerComponent {

        public TestMediaPlayerComponent() {
            super(new TestBufferFormatCallback());
        }

        @Override
        public void display(DirectMediaPlayer mediaPlayer, Memory[] nativeBuffers, BufferFormat bufferFormat) {
            Memory nativeBuffer = nativeBuffers[0];
//            ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
//            pixelWriter.setPixels(0, 0, bufferFormat.getWidth(), bufferFormat.getHeight(), pixelFormat, byteBuffer, bufferFormat.getPitches()[0]);

            ByteBuffer byteBuffer = nativeBuffer.getByteBuffer(0, nativeBuffer.size());
            pixelWriter.setPixels(0, 0, WIDTH, HEIGHT, pixelFormat, byteBuffer, bufferFormat.getPitches()[0]);
        }

        public void resizePlayer() {
            long timeStamp = Datavyu.getDataController().getCurrentTime();
            boolean isPlaying = mp.isPlaying();
            mp.stop();
            mp.play();
            mp.setTime(timeStamp);
            while (!mp.isPlaying()) {
            }

            System.out.println("Was playing: " + isPlaying);
            if (!isPlaying) {

                mp.pause();
            }

        }
    }

    /**
     * Callback to get the buffer format to use for video playback.
     */
    private class TestBufferFormatCallback implements BufferFormatCallback {

        @Override
        public BufferFormat getBufferFormat(int sourceWidth, int sourceHeight) {
            int width;
            int height;
            if (useSourceSize) {
                width = sourceWidth;
                height = sourceHeight;
            } else {
                width = WIDTH;
                height = HEIGHT;
            }
            canvas.setWidth(width);
            canvas.setHeight(height);
            stage.setWidth(width);
            stage.setHeight(height);
            return new RV32BufferFormat(width, height);
        }
    }


}
