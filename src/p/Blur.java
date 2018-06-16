package p;

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class Blur
{
    private BufferedImage img;
    public BufferedImage Blur(BufferedImage image) {
        int size = 15;
        float weight = 1.0f / (size * size);
        float[] data = new float[size * size];
        for (int i = 0; i < data.length; i++) {
            data[i] = weight;
        }
        Kernel kernel = new Kernel(size, size , data);
        BufferedImageOp op = new ConvolveOp(kernel);
        this.img = op.filter(image, null);
        return img;
    }
}
