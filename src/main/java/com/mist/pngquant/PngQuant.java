package com.mist.pngquant;

import com.mist.pngquant.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * Starting point for the library. Holds configuration. Equivalent of liq_attr* in libimagequant.
 */
public class PngQuant extends LiqObject {

    /**
     * Single instance can be "recycled" for many remappings.
     */
    public PngQuant() {
        handle = liq_attr_create();
    }

    public PngQuant(PngQuant other) {
        handle = liq_attr_copy(other.handle);
    }

    /**
     * 1-shot quantization and remapping with current settings.
     * @see quantize()
     *
     * @return 8-bit indexed image or null on failure
     */
    public BufferedImage getRemapped(BufferedImage bufimg) {
        try {
            Image liqimg = new Image(this, bufimg);
            BufferedImage remapped = getRemapped(liqimg);
            liqimg.close();
            return remapped;
        } catch(PngQuantException e) {
            return null;
        }
    }

    /** @return remapped image or null on failure */
    public BufferedImage getRemapped(Image liqimg) {
        Result result = quantize(liqimg);
        if (result == null) return null;
        BufferedImage remapped = result.getRemapped(liqimg);
        result.close();
        return remapped;
    }

    /**
     * Performs quantization (chooses optimal palette for the given Image).
     * Returned object can be used to customize remapping and reused to remap other images to the same palette.
     * @link http://pngquant.org/lib/#liq_quantize_image
     *
     * @return null on failure
     */
    public Result quantize(Image img) {
        try {
            return new Result(this, img);
        } catch(PngQuantException e) {
            return null;
        }
    }

    //@off
    /*JNI
    #include <libimagequant.h>
    #include <c_util.h>
     */

    public native boolean setMaxColors(int colors);/*
        return LIQ_OK == liq_set_max_colors(reinterpret_cast<liq_attr*>(handle(env, object)), colors);
    */
    
    public native boolean setQuality(int q);/*
        return LIQ_OK == liq_set_quality(reinterpret_cast<liq_attr*>(handle(env, object)), q/2, q);
    */

    public native boolean setQuality2(int min, int max);/*
        return LIQ_OK == liq_set_quality(reinterpret_cast<liq_attr*>(handle(env, object)), min, max);
    */

    public native boolean setSpeed(int speed);/*
        return LIQ_OK == liq_set_speed(reinterpret_cast<liq_attr*>(handle(env, object)), speed);
    */
    
    public native boolean setMinPosterization(int bits);/*
        return LIQ_OK == liq_set_min_posterization(reinterpret_cast<liq_attr*>(handle(env, object)), bits);
    */
    
    public void close() {
        if (handle != 0) {
            liq_attr_destroy(handle);
            handle = 0;
        }
    }

    private static native long liq_attr_create();/*
        return (jlong)liq_attr_create();
    */


    private static native long liq_attr_copy(long orig);/*
        return (jlong)liq_attr_copy((liq_attr*)orig);
    */

    private static native void liq_attr_destroy(long handle);/*
        return liq_attr_destroy((liq_attr*)handle);
    */
    
    public static void main(String[] args) throws IOException {
    	new SharedLibraryLoader().load("imagequant-java");
    	System.out.println(new File("test/origin.png").exists());
    	PngQuant pngQuant = new PngQuant();
    	pngQuant.setQuality(10);
    	BufferedImage image = ImageIO.read(new File("test/origin.png"));

    	BufferedImage remapped = pngQuant.getRemapped(image);
    	ImageIO.write(remapped, "png", new File("test/1.png "));

	}
}
