#include <com_mist_pngquant_Image.h>

//@line:35

    #include <libimagequant.h>
    #include <stdlib.h>
    #include <string.h>
    #include <c_util.h>
     JNIEXPORT jboolean JNICALL Java_com_mist_pngquant_Image_addFixedColor(JNIEnv* env, jobject object, jint r, jint g, jint b, jint a) {


//@line:42

        liq_color c = {r,g,b,a};
        return LIQ_OK == liq_image_add_fixed_color(((liq_jni_image*)handle(env, object))->image, c);
    

}

JNIEXPORT jint JNICALL Java_com_mist_pngquant_Image_getWidth(JNIEnv* env, jobject object) {


//@line:49

        return liq_image_get_width(((liq_jni_image*)handle(env, object))->image);
    

}

JNIEXPORT jint JNICALL Java_com_mist_pngquant_Image_getHeight(JNIEnv* env, jobject object) {


//@line:53

        return liq_image_get_height(((liq_jni_image*)handle(env, object))->image);
    

}

static inline jlong wrapped_Java_com_mist_pngquant_Image_liq_1image_1create
(JNIEnv* env, jclass clazz, jlong attr, jbyteArray obj_bitmap, jint length, jint w, jint h, jint components, char* bitmap) {

//@line:84

        liq_jni_image *jniimg = reinterpret_cast<liq_jni_image*>(malloc(sizeof(liq_jni_image)));
        
        jniimg->data = reinterpret_cast<jbyte*>(malloc(length));
        
        memcpy(jniimg->data, bitmap, length);

        jniimg->image = liq_image_create_custom((liq_attr*)attr, components == 4 ? convert_abgr : convert_bgr, jniimg, w, h, 0);

        if (!jniimg->image) {
            free(jniimg->data);
            free(jniimg);
            return 0;
        }
        return (jlong)jniimg;
    
}

JNIEXPORT jlong JNICALL Java_com_mist_pngquant_Image_liq_1image_1create(JNIEnv* env, jclass clazz, jlong attr, jbyteArray obj_bitmap, jint length, jint w, jint h, jint components) {
	char* bitmap = (char*)env->GetPrimitiveArrayCritical(obj_bitmap, 0);

	jlong JNI_returnValue = wrapped_Java_com_mist_pngquant_Image_liq_1image_1create(env, clazz, attr, obj_bitmap, length, w, h, components, bitmap);

	env->ReleasePrimitiveArrayCritical(obj_bitmap, bitmap, 0);

	return JNI_returnValue;
}

JNIEXPORT void JNICALL Java_com_mist_pngquant_Image_liq_1image_1destroy(JNIEnv* env, jclass clazz, jlong handle) {


//@line:101

        liq_jni_image *jniimg = (liq_jni_image*)handle;
        liq_image_destroy(jniimg->image);
        free(jniimg->data);
        free(jniimg);
    

}

