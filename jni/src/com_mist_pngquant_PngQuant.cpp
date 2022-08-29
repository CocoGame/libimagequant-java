#include <com_mist_pngquant_PngQuant.h>

//@line:64

    #include <libimagequant.h>
    #include <c_util.h>
     JNIEXPORT jboolean JNICALL Java_com_mist_pngquant_PngQuant_setMaxColors(JNIEnv* env, jobject object, jint colors) {


//@line:69

        return LIQ_OK == liq_set_max_colors(reinterpret_cast<liq_attr*>(handle(env, object)), colors);
    

}

JNIEXPORT jboolean JNICALL Java_com_mist_pngquant_PngQuant_setQuality(JNIEnv* env, jobject object, jint q) {


//@line:73

        return LIQ_OK == liq_set_quality(reinterpret_cast<liq_attr*>(handle(env, object)), q/2, q);
    

}

JNIEXPORT jboolean JNICALL Java_com_mist_pngquant_PngQuant_setQuality2(JNIEnv* env, jobject object, jint min, jint max) {


//@line:77

        return LIQ_OK == liq_set_quality(reinterpret_cast<liq_attr*>(handle(env, object)), min, max);
    

}

JNIEXPORT jboolean JNICALL Java_com_mist_pngquant_PngQuant_setSpeed(JNIEnv* env, jobject object, jint speed) {


//@line:81

        return LIQ_OK == liq_set_speed(reinterpret_cast<liq_attr*>(handle(env, object)), speed);
    

}

JNIEXPORT jboolean JNICALL Java_com_mist_pngquant_PngQuant_setMinPosterization(JNIEnv* env, jobject object, jint bits) {


//@line:85

        return LIQ_OK == liq_set_min_posterization(reinterpret_cast<liq_attr*>(handle(env, object)), bits);
    

}

JNIEXPORT jlong JNICALL Java_com_mist_pngquant_PngQuant_liq_1attr_1create(JNIEnv* env, jclass clazz) {


//@line:96

        return (jlong)liq_attr_create();
    

}

JNIEXPORT jlong JNICALL Java_com_mist_pngquant_PngQuant_liq_1attr_1copy(JNIEnv* env, jclass clazz, jlong orig) {


//@line:101

        return (jlong)liq_attr_copy((liq_attr*)orig);
    

}

JNIEXPORT void JNICALL Java_com_mist_pngquant_PngQuant_liq_1attr_1destroy(JNIEnv* env, jclass clazz, jlong handle) {


//@line:105

        return liq_attr_destroy((liq_attr*)handle);
    

}

