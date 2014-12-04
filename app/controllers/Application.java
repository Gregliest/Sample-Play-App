package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import utils.ImageResizer;
import utils.TCAmazonS3Client;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class Application extends Controller {
    private static final int THUMBNAIL_SIZE = 100;
    private static final double MAX_DIMENSION = 1000.0;

    public static void index() {
        render();
    }

    public static void uploadPhoto(File imageFile) {

        //Load the image
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            notFound();
        }

        //Create the resized images.
        ImageResizer resizer = new ImageResizer();
        BufferedImage thumbnail = resizer.resizeImage(image, THUMBNAIL_SIZE, THUMBNAIL_SIZE);
        BufferedImage resizedImage = resizer.resizeImageToMaxDimension(image, MAX_DIMENSION);

        //Upload to s3
        TCAmazonS3Client amazonS3 = new TCAmazonS3Client();
        amazonS3.writeThumbToS3(resizer.fileFromImage(thumbnail, "thumbnail.jpg"), "thumbnail");
        amazonS3.writeResizedToS3(resizer.fileFromImage(resizedImage, "resized.jpg"), "resized");

        //Render
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(resizedImage, "png", baos);
        } catch (IOException e) {
            notFound(); //Probably should be a 500
        }
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        Http.Response.current().contentType = "image/png";

        renderBinary(bais);
    }
}