package at.dauswege.service;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Rotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import net.coobird.thumbnailator.Thumbnails;

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

    try {
      Thumbnails.of(srcImage)
          .size(200, 200)
          .toFile(targetImage);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }
  // public void createThumbnail(File srcImage, File targetImage) {
  //
  // BufferedImage src;
  // try {
  //
  // ImageMetadata srcMetadata = Imaging.getMetadata(srcImage);
  // TiffField orientationField = null;
  // if (srcMetadata instanceof JpegImageMetadata) {
  // JpegImageMetadata metadata = (JpegImageMetadata) srcMetadata;
  // System.out.println(metadata.toString());
  // orientationField = metadata.findEXIFValue(TiffTagConstants.TIFF_TAG_ORIENTATION);
  // }
  //
  // src = ImageIO.read(srcImage);
  // BufferedImage thumbnail = Scalr.resize(src, 200);
  // // BufferedImage thumbnail = Scalr.resize(src, Method.QUALITY, Mode.FIT_EXACT, 200);
  //
  // ImageIO.write(thumbnail, "JPEG", targetImage);
  //
  // JpegImageMetadata targetMetadata = (JpegImageMetadata) Imaging.getMetadata(targetImage);
  // TiffOutputSet outputSet = null;
  // if (targetMetadata != null) {
  // final TiffImageMetadata exifData = targetMetadata.getExif();
  // if (exifData != null) {
  // outputSet = exifData.getOutputSet();
  // }
  // }
  //
  // if (outputSet == null) {
  // outputSet = new TiffOutputSet();
  // }
  //
  // final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();
  // // exifDirectory.removeField(orientation);
  // // exifDirectory.add()
  //
  //
  //
  // } catch (IOException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // } catch (ImageReadException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // } catch (ImageWriteException e) {
  // // TODO Auto-generated catch block
  // e.printStackTrace();
  // }
  //
  // }

  public void rotateImage(File srcImage) {
    BufferedImage src;
    try {
      src = ImageIO.read(srcImage);
      BufferedImage thumbnail = Scalr.rotate(src, Rotation.CW_90);
      ImageIO.write(thumbnail, "JPEG", srcImage);
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
