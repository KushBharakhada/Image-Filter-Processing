import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.awt.Color;

/**
 * ImageFilter class takes an image as an input and uses the idea of convolution filters
 * and sobel edge detection to find edges in an image.These filters run horizontally
 * and vertically across the image and are then combined. The input image can be
 * RGB but the output will be a greyscale image. The output image will be 2 pixels
 * less in height and width compared to the input image due to the convolution filters.
 * A jpeg file image is produced.
 * Can speed up the process by removing print statements.
 * @author Kush Bharakhada
 */

public class ImageFilter {
  private BufferedImage imageToProcess;
  private String saveImageName;

  /**
   * Reads in the file to be processed. Gives an error if not possible
   * @param file is the image to be read in
   * @param saveImageName is the name that the file should save as
   */
  public ImageFilter(String file, String saveImageName) {
    try {
      imageToProcess = ImageIO.read(new File(file));
      this.saveImageName = saveImageName;
      System.out.println("Image has been loaded [sucessfully]");
    }
    catch (IOException e) {
      System.out.println(e);
    }
  }

  /**
   * Saves the file that has been produced by the program as a jpeg image
   * Gives an error if the file cannot be saved
   * @param image is the processed image to be saved
   */
  public void saveImage(BufferedImage image) {
    try {
      File output = new File(saveImageName);
      ImageIO.write(image, "jpg", output);
      System.out.println("Image has been saved [sucessfully] as " + saveImageName);
    }
    catch (IOException e) {
      System.out.println(e);
    }
  }  
  
  /**
   * RGB value of each pixel in the image to process is retrieved, converted into
   * a greyscale pixel and stored in an array
   */
  public void convertImage() {
    int width = imageToProcess.getWidth();
    int height = imageToProcess.getHeight();
    System.out.println("Retrieving each pixel from the image...");
    System.out.println("Image to process width: " + width);
    System.out.println("Image to process height: " + height);
    int[][] imgArray = new int[height][width];

    for (int y=0; y<height; y++) {
      for (int x=0; x<width; x++) {
        // Obtain RGB values of the pixels
        int colour = imageToProcess.getRGB(x, y);
        int r = (colour & 0xff0000) >> 16;
        int g = (colour & 0xff00) >> 8;
        int b = (colour & 0xff);
        int greyScale = (r + g + b) / 3;
        imgArray[y][x] = greyScale;
      }
    }
    System.out.println("All pixels retrieved [sucessfully]");
    convolutionFilter(imgArray);
  }

  /**
   * Two filters are run across the image horizontally and vertically. Uses the idea
   * of convolution and sobel edge detector. Both gradients are combined using
   * g = sqrt(gx^2 + gy^2). The values are stored into an array.
   * @param imgArr is the image in a 2D array form with grey pixel values
   */
  public void convolutionFilter(int[][] imgArr) {
    System.out.println("Running filters horizontally and vertically...");
    int[][] filterX = { {-1, 0, 1},
                        {-2, 0, 2},
                        {-1, 0, 1} };

    int[][] filterY = { {-1,-2,-1},
                        { 0, 0, 0},
                        { 1, 2, 1} };

    // The height the filter must move is the image height - 2 as the bottom row
    // of the filter will be at the bottom of the image. If the filter goes full
    // height then the bottom 2 rows of the filter will be out of the image.
    // Same applies for width but horizontally.
    int height = imageToProcess.getHeight() - 2;
    int width = imageToProcess.getWidth() - 2;

    int[][] gx = new int[height][width]; 
    int[][] gy = new int[height][width];

    /*
    * |-----------------------------------|
    * |  valOneX  |  valTwoX  | valThreeX |
    * |-----------------------------------|
    * |  valFourX |  valFiveX |  valSixX  | = |totalX|
    * |-----------------------------------|
    * | valSevenX | valEightX |  valNineX |
    * |-----------------------------------|
    *
    * |--------------------------------------------------------|
    * |  imgArr[y][x]   |  imgArr[y][x+1]   | imgArr[y][x+2]   |
    * |--------------------------------------------------------|
    * |  imgArr[y+1][x] |  imgArr[y+1][x+1] | imgArr[y+1][x+2] |
    * |--------------------------------------------------------|
    * |  imgArr[y+2][x] |  imgArr[y+2][x+1] | imgArr[y+2][x+2] |
    * |--------------------------------------------------------|
    */
    for (int y=0; y<height; y++) {
      for (int x=0; x<width; x++) {
        int valOneX = imgArr[y][x] * filterX[0][0];
        int valTwoX = imgArr[y][x+1] * filterX[0][1];
        int valThreeX = imgArr[y][x+2] * filterX[0][2];
        int valFourX = imgArr[y+1][x] * filterX[1][0];
        int valFiveX = imgArr[y+1][x+1] * filterX[1][1];
        int valSixX = imgArr[y+1][x+2] * filterX[1][2];
        int valSevenX = imgArr[y+2][x] * filterX[2][0];
        int valEightX = imgArr[y+2][x+1] * filterX[2][1];
        int valNineX = imgArr[y+2][x+2] * filterX[2][2];
        // Sum the values obtained from the 9 pixels and store the value into
        // an array as a new single pixel
        int totalX = valOneX + valTwoX + valThreeX + valFourX + valFiveX + valSixX
          + valSevenX + valEightX + valNineX;
        gx[y][x] = totalX;
      }
    }
  
    // Same concept as horizontal but for vertical
    for (int y=0; y<height; y++) {
      for (int x=0; x<width; x++) {
        int valOneY = imgArr[y][x] * filterY[0][0];
        int valTwoY = imgArr[y][x+1] * filterY[0][1];
        int valThreeY = imgArr[y][x+2] * filterY[0][2];
        int valFourY = imgArr[y+1][x] * filterY[1][0];
        int valFiveY = imgArr[y+1][x+1] * filterY[1][1];
        int valSixY = imgArr[y+1][x+2] * filterY[1][2];
        int valSevenY = imgArr[y+2][x] * filterY[2][0];
        int valEightY = imgArr[y+2][x+1] * filterY[2][1];
        int valNineY = imgArr[y+2][x+2] * filterY[2][2];
        int totalY = valOneY + valTwoY + valThreeY + valFourY + valFiveY + valSixY
          + valSevenY + valEightY + valNineY;
        gy[y][x] = totalY;
      }
    }

    int[][] gxgyCombine = new int[height][width]; 
    /*
    * Combine the 2 horizontal and vertical arrays produced by the filters using
    * g = sqrt(gx^2 + gy^2)
    *
    * |-----------------------------------|
    * |  valOneX  |  valTwoX  | valThreeX |
    * |  valOneY  |  valTwoY  | valThreeY |
    * |-----------------------------------|
    * |  valFourX |  valFiveX |  valSixX  |    
    * |  valOneY  |  valTwoY  | valThreeY |
    * |-----------------------------------|
    * | valSevenX | valEightX |  valNineX |
    * |  valOneY  |  valTwoY  | valThreeY |
    * |-----------------------------------|
    */
    for (int y=0; y<height; y++) {
      for (int x=0; x<width; x++) {
        double squared = Math.pow(gx[y][x], 2) + Math.pow(gy[y][x], 2);
        int sqrt = (int)Math.sqrt(squared);
        gxgyCombine[y][x] = sqrt;
      }
    }
    System.out.println("Filters have [sucessfully] combined");
    plotImage(gxgyCombine);
  }

  /**
   * Pixels are plotted as either black, grey or white onto a new image
   * Creates the overall processed image
   * @param imageArray is the array that contains the pixel values of the processed image
   */
  public void plotImage(int[][] imageArray) {
    System.out.println("Plotting the new image...");
    int height = imageArray.length;
    int width = imageArray[1].length;
    System.out.println("New image height: " + height);
    System.out.println("New image width: " + width);
    BufferedImage processedImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
    int white = new Color(255, 255, 255).getRGB();
    int black = new Color(0, 0, 0).getRGB();
    int grey = new Color(100, 100, 100).getRGB();

    for (int y=0; y<height; y++) {
      for (int x=0; x<width; x++) {
        if (imageArray[y][x] < 120)
          processedImage.setRGB(x, y, black);
        else if (imageArray[y][x] < 200)
          processedImage.setRGB(x, y, grey);
        else
          processedImage.setRGB(x, y, white);
      }
    }
    System.out.println("Image has been [successfully] plotted");
    saveImage(processedImage);
  }
}