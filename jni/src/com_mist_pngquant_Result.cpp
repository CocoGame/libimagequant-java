#include <com_mist_pngquant_Result.h>

//@line:40

    #include <libimagequant.h>
    #include <c_util.h>
     JNIEXPORT jboolean JNICALL Java_com_mist_pngquant_Result_setDitheringLevel(JNIEnv* env, jobject object, jfloat dither_level) {


//@line:45

        return LIQ_OK == liq_set_dithering_level(reinterpret_cast<liq_result*>(handle(env, object)), dither_level);
    

}

JNIEXPORT jboolean JNICALL Java_com_mist_pngquant_Result_setGamma(JNIEnv* env, jobject object, jdouble gamma) {


//@line:49

        return LIQ_OK == liq_set_output_gamma(reinterpret_cast<liq_result*>(handle(env, object)), gamma);
    

}

JNIEXPORT jdouble JNICALL Java_com_mist_pngquant_Result_getGamma(JNIEnv* env, jobject object) {


//@line:53

        return liq_get_output_gamma(reinterpret_cast<const liq_result*>(handle(env, object)));
    

}

JNIEXPORT jdouble JNICALL Java_com_mist_pngquant_Result_getMeanSquareError(JNIEnv* env, jobject object) {


//@line:57

        return liq_get_quantization_error(reinterpret_cast<const liq_result*>(handle(env, object)));
    

}

JNIEXPORT jint JNICALL Java_com_mist_pngquant_Result_getQuality(JNIEnv* env, jobject object) {


//@line:61

        return liq_get_quantization_quality(reinterpret_cast<const liq_result*>(handle(env, object)));
    

}

JNIEXPORT jbyteArray JNICALL Java_com_mist_pngquant_Result_liq_1get_1palette(JNIEnv* env, jclass clazz, jlong handle) {


//@line:82

        const liq_palette *pal = liq_get_palette((liq_result*)handle);
        jbyteArray arr = env->NewByteArray(pal->count * 4);
        int i;
        for(i=0; i < pal->count; i++) {
            env->SetByteArrayRegion(arr, i*4, 4, ((jbyte*)&pal->entries[i]));
        }
        return arr;
    

}

JNIEXPORT jlong JNICALL Java_com_mist_pngquant_Result_liq_1quantize_1image(JNIEnv* env, jclass clazz, jlong attr, jlong image) {


//@line:92

        return (jlong)liq_quantize_image((liq_attr*)attr, ((liq_jni_image*)image)->image);
    

}

static inline jboolean wrapped_Java_com_mist_pngquant_Result_liq_1write_1remapped_1image
(JNIEnv* env, jclass clazz, jlong handle, jlong image, jbyteArray obj_buffer, jint length, char* buffer) {

//@line:96

        return LIQ_OK == liq_write_remapped_image((liq_result*)handle, ((liq_jni_image*)image)->image, buffer, length);
    
}

JNIEXPORT jboolean JNICALL Java_com_mist_pngquant_Result_liq_1write_1remapped_1image(JNIEnv* env, jclass clazz, jlong handle, jlong image, jbyteArray obj_buffer, jint length) {
	char* buffer = (char*)env->GetPrimitiveArrayCritical(obj_buffer, 0);

	jboolean JNI_returnValue = wrapped_Java_com_mist_pngquant_Result_liq_1write_1remapped_1image(env, clazz, handle, image, obj_buffer, length, buffer);

	env->ReleasePrimitiveArrayCritical(obj_buffer, buffer, 0);

	return JNI_returnValue;
}

JNIEXPORT void JNICALL Java_com_mist_pngquant_Result_liq_1result_1destroy(JNIEnv* env, jclass clazz, jlong handle) {


//@line:100

        return liq_result_destroy((liq_result*)handle);
    

}

