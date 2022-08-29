package com.mist.pngquant;

import com.mist.pngquant.*;
import java.awt.image.*;

/**
 * PngQuant's representation of an Image constructed from BufferedImage.
 */
public class Image extends LiqObject {

    /**
     * Converts BufferedImage to internal representation (pixel data is copied).
     * It's best to use BufferedImage in RGB/RGBA format backed by DataBufferByte.
     * Throws if conversion fails.
     */
    public Image(BufferedImage image) throws PngQuantException {
        this(new PngQuant(), image);
    }

    public Image(PngQuant attr, BufferedImage image) throws PngQuantException {
        handle = handleFromImage(attr, image);

        if (handle == 0) {
            BufferedImage converted = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);
            converted.getGraphics().drawImage(image, 0, 0, null);
            handle = handleFromImage(attr, converted);

            if (handle == 0) {
                throw new PngQuantException();
            }
        }
    }

    //@off
    /*JNI
    #include <libimagequant.h>
    #include <stdlib.h>
    #include <string.h>
    #include <c_util.h>
     */

    public native boolean addFixedColor(int r, int g, int b, int a);/*
        liq_color c = {r,g,b,a};
        return LIQ_OK == liq_image_add_fixed_color(((liq_jni_image*)handle(env, object))->image, c);
    */
    public boolean addFixedColor(int r, int g, int b) {
        return addFixedColor(r, g, b, 255);
    }
    public native int getWidth();/*
        return liq_image_get_width(((liq_jni_image*)handle(env, object))->image);
    */

    public native int getHeight();/*
        return liq_image_get_height(((liq_jni_image*)handle(env, object))->image);
    */

    public void close() {
        if (handle != 0) {
            liq_image_destroy(handle);
            handle = 0;
        }
    }

    private static long handleFromImage(PngQuant attr, BufferedImage image) {
        // The JNI wrapper will accept non-premultiplied ABGR and BGR only.
        int type = image.getType();
        if (type != BufferedImage.TYPE_3BYTE_BGR &&
            type != BufferedImage.TYPE_4BYTE_ABGR &&
            type != BufferedImage.TYPE_4BYTE_ABGR_PRE) return 0;

        WritableRaster raster = image.getRaster();
        ColorModel color = image.getColorModel();
        if (type == BufferedImage.TYPE_4BYTE_ABGR_PRE) color.coerceData(raster, false);

        DataBuffer buffer = raster.getDataBuffer();
        if (buffer instanceof DataBufferByte) {
            byte[] imageData = ((DataBufferByte)buffer).getData();
            return liq_image_create(attr.handle, imageData, imageData.length,
                raster.getWidth(), raster.getHeight(), color.getNumComponents());
        }
        return 0;
    }

    private static native long liq_image_create(long attr, byte[] bitmap, int length, int w, int h, int components);/*
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
    */

    private static native void liq_image_destroy(long handle);/*
        liq_jni_image *jniimg = (liq_jni_image*)handle;
        liq_image_destroy(jniimg->image);
        free(jniimg->data);
        free(jniimg);
    */
}
