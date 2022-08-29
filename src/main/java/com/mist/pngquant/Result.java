package com.mist.pngquant;

import com.mist.pngquant.*;
import java.awt.image.*;

/**
 * Quantization result that holds palette and options for remapping.
 */
public class Result extends LiqObject {

    /**
     * Throws when quantization fails (e.g. due to failing to achieve minimum quality)
     */
    public Result(PngQuant pngquant, Image image) throws PngQuantException {
        handle = liq_quantize_image(pngquant.handle, image.handle);
        if (handle == 0) {
            throw new PngQuantException();
        }
    }

    /**
     * @return BufferedImage remapped to palette this Result has been created with or null on failure.
     */
    public BufferedImage getRemapped(Image orig_image) {
        byte[] pal = liq_get_palette(handle);
        IndexColorModel color = new IndexColorModel(8, pal.length/4, pal, 0, true);
        BufferedImage img = new BufferedImage(
            orig_image.getWidth(), orig_image.getHeight(),
            BufferedImage.TYPE_BYTE_INDEXED, color);

        byte[] data = get8bitDataFromImage(img);
        if (data == null) return null;

        if (!liq_write_remapped_image(handle, orig_image.handle, data, data.length)) return null;

        return img;
    }

    //@off
    /*JNI
    #include <libimagequant.h>
    #include <c_util.h>
     */

    public native boolean setDitheringLevel(float dither_level);/*
        return LIQ_OK == liq_set_dithering_level(reinterpret_cast<liq_result*>(handle(env, object)), dither_level);
    */
    
    public native boolean setGamma(double gamma);/*
        return LIQ_OK == liq_set_output_gamma(reinterpret_cast<liq_result*>(handle(env, object)), gamma);
    */
    
    public native double getGamma();/*
        return liq_get_output_gamma(reinterpret_cast<const liq_result*>(handle(env, object)));
    */
    
    public native double getMeanSquareError();/*
        return liq_get_quantization_error(reinterpret_cast<const liq_result*>(handle(env, object)));
    */

    public native int getQuality();/*
        return liq_get_quantization_quality(reinterpret_cast<const liq_result*>(handle(env, object)));
    */

    public void close() {
        if (handle != 0) {
            liq_result_destroy(handle);
            handle = 0;
        }
    }

    private static byte[] get8bitDataFromImage(BufferedImage image) {
        if (image.getType() == BufferedImage.TYPE_BYTE_INDEXED) {
            DataBuffer buffer = image.getRaster().getDataBuffer();
            if (buffer instanceof DataBufferByte) {
                return ((DataBufferByte)buffer).getData();
            }
        }
        return null;
    }

    private static native byte[] liq_get_palette(long handle);/*
        const liq_palette *pal = liq_get_palette((liq_result*)handle);
        jbyteArray arr = env->NewByteArray(pal->count * 4);
        int i;
        for(i=0; i < pal->count; i++) {
            env->SetByteArrayRegion(arr, i*4, 4, ((jbyte*)&pal->entries[i]));
        }
        return arr;
    */
    
    private static native long liq_quantize_image(long attr, long image);/*
        return (jlong)liq_quantize_image((liq_attr*)attr, ((liq_jni_image*)image)->image);
    */
    
    private static native boolean liq_write_remapped_image(long handle, long image, byte[] buffer, int length);/*
        return LIQ_OK == liq_write_remapped_image((liq_result*)handle, ((liq_jni_image*)image)->image, buffer, length);
    */
    
    private static native void liq_result_destroy(long handle);/*
        return liq_result_destroy((liq_result*)handle);
    */
}
