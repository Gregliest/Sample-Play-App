package utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageResizer {
    /**
     * @return a new image object resized to the given width and height.
     */
    public BufferedImage resizeImage(BufferedImage image, int width, int height) {
        // creates output image
        BufferedImage outputImage = new BufferedImage(width, height, image.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();

        return outputImage;
    }

    /**
     * @return a new image object resized with the given max dimension that (mostly) preserves the aspect ratio.
     */
    public BufferedImage resizeImageToMaxDimension(BufferedImage image, double maxDimension) {
        //Find the scale factor.
        int width = image.getWidth();
        int height = image.getHeight();
        double scaleFactor = width > height ? maxDimension / width : maxDimension / height;

        int scaledWidth = (int) (width * scaleFactor);
        int scaledHeight = (int) (height * scaleFactor);

        return resizeImage(image, scaledWidth, scaledHeight);
    }

    /**
     * @return a jpg file.
     */
    public File fileFromImage(BufferedImage image, String filename) {
        //There's got to be a better way to handle the file conversion that doesn't require a local write.
        File outputfile = new File(filename);
        try {
            ImageIO.write(image, "jpg", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputfile;
    }
}
