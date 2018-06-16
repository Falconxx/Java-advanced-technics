package p;

import java.awt.image.BufferedImage;

public class Rotate
{
    public BufferedImage rotateCw( BufferedImage img )
    {
        int width  = img.getWidth();
        int height = img.getHeight();
        BufferedImage newImage = new BufferedImage( height, width, img.getType() );

        for( int i=0 ; i < width ; i++ )
            for( int j=0 ; j < height ; j++ )
                newImage.setRGB( height-1-j, i, img.getRGB(i,j) );
        return newImage;
    }
}