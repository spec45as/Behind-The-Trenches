package gameframework.libs.other;

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DirectColorModel;
import java.awt.image.Raster;
import java.awt.image.RasterFormatException;
import java.awt.image.WritableRaster;


public final class BlendComposite implements Composite {
    
    public enum BlendingMode {
    	VIOLET,
        RED,
        GREEN,
        BLUE,
        COLOR,
        GRAY
    }

    
    public static final BlendComposite Red = new BlendComposite(BlendingMode.RED);
    public static final BlendComposite Green = new BlendComposite(BlendingMode.GREEN);
    public static final BlendComposite Blue = new BlendComposite(BlendingMode.BLUE);
    public static final BlendComposite Color = new BlendComposite(BlendingMode.COLOR);
    public static final BlendComposite Violet = new BlendComposite(BlendingMode.VIOLET);
    public static final BlendComposite Gray = new BlendComposite(BlendingMode.GRAY);
    

    private final float alpha;
    private final BlendingMode mode;

    private BlendComposite(BlendingMode mode) {
        this(mode, 1.0f);
    }

    private BlendComposite(BlendingMode mode, float alpha) {
        this.mode = mode;

        if (alpha < 0.0f || alpha > 1.0f) {
            throw new IllegalArgumentException(
                    "alpha must be comprised between 0.0f and 1.0f");
        }
        this.alpha = alpha;
    }

    
    public static BlendComposite getInstance(BlendingMode mode) {
        return new BlendComposite(mode);
    }

     static BlendComposite getInstance(BlendingMode mode, float alpha) {
        return new BlendComposite(mode, alpha);
    }

    
    public BlendComposite derive(BlendingMode mode) {
        return this.mode == mode ? this : new BlendComposite(mode, getAlpha());
    }

    
    public BlendComposite derive(float alpha) {
        return this.alpha == alpha ? this : new BlendComposite(getMode(), alpha);
    }

    
    public float getAlpha() {
        return alpha;
    }

   
    public BlendingMode getMode() {
        return mode;
    }

    
    @Override
    public int hashCode() {
        return Float.floatToIntBits(alpha) * 31 + mode.ordinal();
    }

    
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof BlendComposite)) {
            return false;
        }

        BlendComposite bc = (BlendComposite) obj;
        return mode == bc.mode && alpha == bc.alpha;
    }
    
    private static boolean checkComponentsOrder(ColorModel cm) {
    	if (cm instanceof DirectColorModel && 
                cm.getTransferType() == DataBuffer.TYPE_INT) {
            DirectColorModel directCM = (DirectColorModel) cm;
            
            return directCM.getRedMask() == 0x00FF0000 &&
                   directCM.getGreenMask() == 0x0000FF00 &&
                   directCM.getBlueMask() == 0x000000FF &&
                   (directCM.getNumComponents() != 4 ||
                    directCM.getAlphaMask() == 0xFF000000);
        }
        
        return false;
    }
    
    
    public CompositeContext createContext(ColorModel srcColorModel,
                                          ColorModel dstColorModel,
                                          RenderingHints hints) {
        if (!checkComponentsOrder(srcColorModel) ||
                !checkComponentsOrder(dstColorModel)) {
            throw new RasterFormatException("Incompatible color models");
        }
        
        return new BlendingContext(this);
    }

    private static final class BlendingContext implements CompositeContext {
        private final Blender blender;
        private final BlendComposite composite;

        private BlendingContext(BlendComposite composite) {
            this.composite = composite;
            this.blender = Blender.getBlenderFor(composite);
        }

        public void dispose() {
        }

        public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
            int width = Math.min(src.getWidth(), dstIn.getWidth());
            int height = Math.min(src.getHeight(), dstIn.getHeight());

            float alpha = composite.getAlpha();

            int[] result = new int[4];
            int[] srcPixel = new int[4];
            int[] dstPixel = new int[4];
            int[] srcPixels = new int[width];
            int[] dstPixels = new int[width];

            for (int y = 0; y < height; y++) {
                src.getDataElements(0, y, width, 1, srcPixels);
                dstIn.getDataElements(0, y, width, 1, dstPixels);
                for (int x = 0; x < width; x++) {
                    
                    int pixel = srcPixels[x];
                    srcPixel[0] = (pixel >> 16) & 0xFF;
                    srcPixel[1] = (pixel >>  8) & 0xFF;
                    srcPixel[2] = (pixel      ) & 0xFF;
                    srcPixel[3] = (pixel >> 24) & 0xFF;

                    pixel = dstPixels[x];
                    dstPixel[0] = (pixel >> 16) & 0xFF;
                    dstPixel[1] = (pixel >>  8) & 0xFF;
                    dstPixel[2] = (pixel      ) & 0xFF;
                    dstPixel[3] = (pixel >> 24) & 0xFF;

                    blender.blend(srcPixel, dstPixel, result);

                    dstPixels[x] = ((int) (dstPixel[3] + (result[3] - dstPixel[3]) * alpha) & 0xFF) << 24 |
                                   ((int) (dstPixel[0] + (result[0] - dstPixel[0]) * alpha) & 0xFF) << 16 |
                                   ((int) (dstPixel[1] + (result[1] - dstPixel[1]) * alpha) & 0xFF) <<  8 |
                                    (int) (dstPixel[2] + (result[2] - dstPixel[2]) * alpha) & 0xFF;
                }
                dstOut.setDataElements(0, y, width, 1, dstPixels);
            }
        }
    }

    private static abstract class Blender {
        public abstract void blend(int[] src, int[] dst, int[] result);

        public static Blender getBlenderFor(BlendComposite composite) {
            switch (composite.getMode()) {
                case GREEN:
                    return new Blender() {
                        @Override
                        public void blend(int[] src, int[] dst, int[] result) {
                            result[0] = dst[0];
                            result[1] = (int)(src[1]*0.8);
                            result[2] = dst[2];
                            result[3] = Math.min(255, src[3] + dst[3] - (src[3] * dst[3]) / 255);
                        }
                    };
                case COLOR:
                    return new Blender() {
                        @Override
                        public void blend(int[] src, int[] dst, int[] result) {
                            float[] srcHSL = new float[3];
                            ColorUtilities.RGBtoHSL(src[0], src[1], src[2], srcHSL);
                            float[] dstHSL = new float[3];
                            ColorUtilities.RGBtoHSL(dst[0], dst[1], dst[2], dstHSL);

                            ColorUtilities.HSLtoRGB(srcHSL[0], srcHSL[1], dstHSL[2], result);
                            result[3] = Math.min(255, src[3] + dst[3] - (src[3] * dst[3]) / 255);
                        }
                    };
                case BLUE:
                    return new Blender() {
                        @Override
                        public void blend(int[] src, int[] dst, int[] result) {
                            result[0] = dst[0];
                            result[1] = dst[1];
                            result[2] = src[2];
                            result[3] = Math.min(255, src[3] + dst[3] - (src[3] * dst[3]) / 255);
                        }
                    };
                case RED:
                    return new Blender() {
                        @Override
                        public void blend(int[] src, int[] dst, int[] result) {
                            result[0] = src[0];
                            result[1] = dst[1];
                            result[2] = dst[2];
                            result[3] = Math.min(255, src[3] + dst[3] - (src[3] * dst[3]) / 255);
                        }
                    };
                case VIOLET:
                    return new Blender() {
                        @Override
                        public void blend(int[] src, int[] dst, int[] result) {
                            result[0] = src[0];
                            result[1] = dst[1];
                            result[2] = src[2];
                            result[3] = Math.min(255, (src[3] + dst[3] - (src[3] * dst[3]) / 255));
                        }
                    };
                default:
                   	throw new IllegalArgumentException("Blender not implemented for " +
                        composite.getMode().name());
            }
            
        }
    }
}