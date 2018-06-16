package p;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class Negative
{
    private BufferedImage img;
    public BufferedImage Negative(BufferedImage image) {

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgba = image.getRGB(x, y);
                Color col = new Color(rgba, true);
                col = new Color(255 - col.getRed(),
                        255 - col.getGreen(),
                        255 - col.getBlue());
                image.setRGB(x, y, col.getRGB());
            }
        }
        this.img=image;
        return img;
    }
}
