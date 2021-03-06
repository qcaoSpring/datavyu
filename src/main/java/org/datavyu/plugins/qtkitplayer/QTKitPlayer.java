/*
     File: LayerBackedCanvas.java
 Abstract: A CoreAnimation Layer-backed AWT canvas.
  Version: 2.0
 
 Disclaimer: IMPORTANT:  This Apple software is supplied to you by Apple
 Inc. ("Apple") in consideration of your agreement to the following
 terms, and your use, installation, modification or redistribution of
 this Apple software constitutes acceptance of these terms.  If you do
 not agree with these terms, please do not use, install, modify or
 redistribute this Apple software.
 
 In consideration of your agreement to abide by the following terms, and
 subject to these terms, Apple grants you a personal, non-exclusive
 license, under Apple's copyrights in this original Apple software (the
 "Apple Software"), to use, reproduce, modify and redistribute the Apple
 Software, with or without modifications, in source and/or binary forms;
 provided that if you redistribute the Apple Software in its entirety and
 without modifications, you must retain this notice and the following
 text and disclaimers in all such redistributions of the Apple Software.
 Neither the name, trademarks, service marks or logos of Apple Inc. may
 be used to endorse or promote products derived from the Apple Software
 without specific prior written permission from Apple.  Except as
 expressly stated in this notice, no other rights or licenses, express or
 implied, are granted by Apple herein, including but not limited to any
 patent rights that may be infringed by your derivative works or by other
 works in which the Apple Software may be incorporated.
 
 The Apple Software is provided by Apple on an "AS IS" basis.  APPLE
 MAKES NO WARRANTIES, EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION
 THE IMPLIED WARRANTIES OF NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS
 FOR A PARTICULAR PURPOSE, REGARDING THE APPLE SOFTWARE OR ITS USE AND
 OPERATION ALONE OR IN COMBINATION WITH YOUR PRODUCTS.
 
 IN NO EVENT SHALL APPLE BE LIABLE FOR ANY SPECIAL, INDIRECT, INCIDENTAL
 OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 INTERRUPTION) ARISING IN ANY WAY OUT OF THE USE, REPRODUCTION,
 MODIFICATION AND/OR DISTRIBUTION OF THE APPLE SOFTWARE, HOWEVER CAUSED
 AND WHETHER UNDER THEORY OF CONTRACT, TORT (INCLUDING NEGLIGENCE),
 STRICT LIABILITY OR OTHERWISE, EVEN IF APPLE HAS BEEN ADVISED OF THE
 POSSIBILITY OF SUCH DAMAGE.
 
 Copyright (C) 2011 Apple Inc. All Rights Reserved.
 
 */

package org.datavyu.plugins.qtkitplayer;

import org.datavyu.util.NativeLoader;

import java.awt.*;
import java.io.File;

public class QTKitPlayer extends Canvas {

    protected static int playerCount = 0;

    static {
        // Standard JNI: load the native library

//        System.loadLibrary("QTKitCanvas");

//        System.load("/Users/jesse/Library/Developer/Xcode/DerivedData/JAWTExample-cdhbmpdibiannlgigweelsyjyces/Build/Products/Debug/libQTKitCanvas.jnilib");

    }

    public final int id;
    File fileToLoad;

    public QTKitPlayer(File fileToLoad) {
        super();
        try {
            NativeLoader.LoadNativeLib("QTKitCanvas");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.id = QTKitPlayer.playerCount;
        QTKitPlayer.playerCount += 1;
        this.fileToLoad = fileToLoad;
    }

    public void addNotify() {
        super.addNotify();
        System.out.println("Opening video file: " + fileToLoad.toURI().getPath());
        try {
            addNativeCoreAnimationLayer("file://" + fileToLoad.toURI().getPath());
        } catch (Exception e) {
            System.out.println("ERROR CAUGHT");
            e.printStackTrace();
        }
    }

    // This method is implemented in native code. See NativeCanvas.m
    public native void addNativeCoreAnimationLayer(String path);

    public native void stop(int id);

    public native void play(int id);

    public native void setTime(long time, int id);

    public native void setVolume(float time, int id);

    public native void release(int id);

    public native double getMovieHeight(int id);

    public native double getMovieWidth(int id);

    public native long getCurrentTime(int id);

    public native long getDuration(int id);

    public native float getRate(int id);

    public native void setRate(float rate, int id);

    public native boolean isPlaying(int id);

    public native float getFPS(int id);

}