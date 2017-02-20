/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class ImagePlayer */

#ifndef _Included_ImagePlayer
#define _Included_ImagePlayer
#ifdef __cplusplus
extern "C" {
#endif
#undef ImagePlayer_FOCUS_TRAVERSABLE_UNKNOWN
#define ImagePlayer_FOCUS_TRAVERSABLE_UNKNOWN 0L
#undef ImagePlayer_FOCUS_TRAVERSABLE_DEFAULT
#define ImagePlayer_FOCUS_TRAVERSABLE_DEFAULT 1L
#undef ImagePlayer_FOCUS_TRAVERSABLE_SET
#define ImagePlayer_FOCUS_TRAVERSABLE_SET 2L
#undef ImagePlayer_TOP_ALIGNMENT
#define ImagePlayer_TOP_ALIGNMENT 0.0f
#undef ImagePlayer_CENTER_ALIGNMENT
#define ImagePlayer_CENTER_ALIGNMENT 0.5f
#undef ImagePlayer_BOTTOM_ALIGNMENT
#define ImagePlayer_BOTTOM_ALIGNMENT 1.0f
#undef ImagePlayer_LEFT_ALIGNMENT
#define ImagePlayer_LEFT_ALIGNMENT 0.0f
#undef ImagePlayer_RIGHT_ALIGNMENT
#define ImagePlayer_RIGHT_ALIGNMENT 1.0f
#undef ImagePlayer_serialVersionUID
#define ImagePlayer_serialVersionUID -7644114512714619750i64
#undef ImagePlayer_serialVersionUID
#define ImagePlayer_serialVersionUID -2284879212465893870i64
#undef ImagePlayer_serialVersionUID
#define ImagePlayer_serialVersionUID -6199180436635445511i64
/*
 * Class:     ImagePlayer
 * Method:    getFrameBuffer
 * Signature: ()Ljava/nio/ByteBuffer;
 */
JNIEXPORT jobject JNICALL Java_ImagePlayer_getFrameBuffer
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    loadNextFrame
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ImagePlayer_loadNextFrame
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    openMovie
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_ImagePlayer_openMovie
  (JNIEnv *, jobject, jstring);

/*
 * Class:     ImagePlayer
 * Method:    getNumberOfChannels
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ImagePlayer_getNumberOfChannels
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    getHeight
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ImagePlayer_getHeight
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    getWidth
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_ImagePlayer_getWidth
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    getStartTime
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_ImagePlayer_getStartTime
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    getEndTime
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_ImagePlayer_getEndTime
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    getDuration
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_ImagePlayer_getDuration
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    getCurrentTime
 * Signature: ()D
 */
JNIEXPORT jdouble JNICALL Java_ImagePlayer_getCurrentTime
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    rewind
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_ImagePlayer_rewind
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    isForwardPlayback
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ImagePlayer_isForwardPlayback
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    atStartForRead
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ImagePlayer_atStartForRead
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    atEndForRead
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_ImagePlayer_atEndForRead
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    release
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_ImagePlayer_release
  (JNIEnv *, jobject);

/*
 * Class:     ImagePlayer
 * Method:    setPlaybackSpeed
 * Signature: (F)V
 */
JNIEXPORT void JNICALL Java_ImagePlayer_setPlaybackSpeed
  (JNIEnv *, jobject, jfloat);

/*
 * Class:     ImagePlayer
 * Method:    setTime
 * Signature: (D)V
 */
JNIEXPORT void JNICALL Java_ImagePlayer_setTime
  (JNIEnv *, jobject, jdouble);

#ifdef __cplusplus
}
#endif
#endif