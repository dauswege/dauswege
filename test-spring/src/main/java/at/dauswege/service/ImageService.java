package at.dauswege.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(ImageService.class);

  private static final String THUMBNAIL_FOLDER_STRING = "/thumb/";

  public void createThumbnail(File srcImage) {

    String folderPath = srcImage.getParentFile().toString();
    String fileName = srcImage.getName();

    LOGGER.info("Create thumbnail for file {}", srcImage.getAbsolutePath());
    LOGGER.info("folderPath: {} , thumbnailFolder: {} , fileName: {}",
        folderPath, THUMBNAIL_FOLDER_STRING, fileName);

    File thumbnailFolder = new File(folderPath + THUMBNAIL_FOLDER_STRING);
    if (!thumbnailFolder.exists()) {
      thumbnailFolder.mkdirs();
    }

    String targetFilePath = folderPath + THUMBNAIL_FOLDER_STRING + fileName;

    File targetFile = new File(targetFilePath);
    this.createThumbnail(srcImage, targetFile);

  }

  public void createThumbnail(File srcImage, File targetImage) {

    BufferedImage src;
    try {
      src = ImageIO.read(srcImage);
      // BufferedImage thumbnail = Scalr.resize(src, 200);

      // ImageIO.write(thumbnail, "JPEG", targetImage);

    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public void rotateImage(File srcImage) {
    BufferedImage src;
    try {
      src = ImageIO.read(srcImage);
      // BufferedImage thumbnail = Scalr.rotate(src, Rotation.CW_90);
      // ImageIO.write(thumbnail, "JPEG", srcImage);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  // public static void main(String args[]) {
  //
  // File srcImage = new File("c:/temp/pics/DSC_0479.JPG");
  //
  // ImageService thumbnailService = new ImageService();
  // // thumbnailService.createThumbnail(srcImage);
  // thumbnailService.rotateImage(srcImage);
  //
  // }

}
