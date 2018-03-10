package utils;

import java.io.File;
import java.io.IOException;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

/**
 * For more information check:
 * https://www.dyclassroom.com/image-processing-project/how-to-read-and-write-image-file-in-java
 * http://stackoverflow.com/questions/12674064/how-to-save-a-bufferedimage-as-a-file
 * http://docs.oracle.com/javase/tutorial/displayCode.html?code=http://docs.oracle.com/javase/tutorial/2d/images/examples/SaveImage.java
 * http://stackoverflow.com/questions/19447104/load-image-from-a-filepath-via-bufferedimage
 *
 * @author Gil
 */
public class ImageUtils {

    //private static final String serverFolderPath = "http://vmedu92.mtacloud.co.il/uploads/";
    public static String saveImage(String imagePath) {
        String fileDecpcriptorUrl = null;
        int width = 963;    //width of the image
        int height = 640;   //height of the image
        BufferedImage image = null;
        File f = null;

        //read image
        try {
            f = new File("D:\\Image\\Cube.png"); //image file path
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            image = ImageIO.read(f);
            System.out.println("Reading complete.");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }

        //write image
        try {
            f = new File("D:\\Image\\Output.png");  //output file path
            ImageIO.write(image, "jpg", f);
            fileDecpcriptorUrl = f.getAbsolutePath();
            System.out.println("Writing complete.");
        } catch (IOException e) {
            System.out.println("Error: " + e);
        } finally {
            return fileDecpcriptorUrl;
        }
    }
    
    public static void loadImage() {
        
    }
}
