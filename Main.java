
public class Main {
  public static void main(String[] args) {
    
    ImageFilter photo = new ImageFilter("examples/image1.jpeg", "examples/image1converted.jpeg");
    photo.convertImage();
    
  }
}
