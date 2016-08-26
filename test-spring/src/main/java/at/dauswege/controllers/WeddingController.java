package at.dauswege.controllers;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import at.dauswege.service.ImageService;

@RestController
@RequestMapping(value = "/req")
public class WeddingController {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(WeddingController.class);

  private final File picFolder;

  private final File thumbnailFolder;

  private static Integer fileCount = 0;


  @Autowired
  public WeddingController(String picFolderPath, String thumbnailFolderString,
      ImageService imageService) {
    picFolder = new File(picFolderPath);
    thumbnailFolder = new File(thumbnailFolderString);
  }

  @ResponseBody
  @RequestMapping(value = "/pic", method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
  public String getPhoto() {

    String fileName = "";
    if (picFolder.isDirectory()) {

      File[] files = picFolder.listFiles(new FileFilter() {

        @Override
        public boolean accept(File pathname) {

          return !pathname.isDirectory();
        }
      });
      if (files.length > 0) {
        if (fileCount >= files.length) {
          fileCount = 0;
        }
        fileName = files[fileCount].getName();
        fileCount++;
      }

    }

    return "pics/" + fileName + "?" + new Date();

  }

  @ResponseBody
  @RequestMapping(value = "/thumbnails", method = RequestMethod.GET)
  public List<String> getThumbnails() {

    List<String> thumbnails = new ArrayList<>();

    if (thumbnailFolder.isDirectory()) {
      File[] files = thumbnailFolder.listFiles();
      if (files.length > 0) {
        for (File file : files) {

          thumbnails.add("pics/thumb/" + file.getName());
        }
      }
    }
    return thumbnails;
  }

  @CrossOrigin(origins = "http://localhost:8100")
  @ResponseBody
  @RequestMapping(method = RequestMethod.GET, path = {"/thumbnailsPaged/{page}"})
  public List<String> getThumbnailsPaged(@PathVariable int page,
      @RequestParam(name = "size") int size) {
    List<String> thumbnails = new ArrayList<>();

    if (thumbnailFolder.isDirectory()) {
      File[] files = thumbnailFolder.listFiles();
      if (files.length > 0) {
        for (File file : files) {

          thumbnails.add("pics/thumb/" + file.getName());
        }
      }
    }

    // thumbnails.sort(new Comparator<String>() {
    // @Override
    // public int compare(String o1, String o2) {
    // return o1.compareTo(o2);
    // }
    // });

    List<String> pagedList = new ArrayList<>();
    int startIndex = (page - 1) * size;
    int endIndex = startIndex + size;
    if (thumbnails.size() >= endIndex + 1) {
      pagedList = thumbnails.subList(startIndex, endIndex);
    } else if (thumbnails.size() >= startIndex) {
      pagedList = thumbnails.subList(startIndex, thumbnails.size());
    }


    return pagedList;
  }

  @CrossOrigin
  @RequestMapping(value = "/rotateImage", method = RequestMethod.PUT)
  public void rotateImage(@RequestBody Map<String, String> pics) {

    String fileName = pics.get("fileName");
    String fileNameParsed = fileName.substring(
        fileName.lastIndexOf("/") + 1, fileName.length());

    File[] files = picFolder.listFiles(new FileFilter() {

      @Override
      public boolean accept(File pathname) {
        System.err.println(pathname.getName() + " - " + fileNameParsed);
        return pathname.getName().equals(fileNameParsed);
      }
    });

    ImageService imageService = new ImageService();
    for (File file : files) {
      LOGGER.info("rotate image: {}", file.getAbsolutePath());
      imageService.rotateImage(file);
      // rotate thumbnails as well
      imageService.rotateImage(new File(thumbnailFolder + "/"
          + file.getName()));
    }

  }

  @CrossOrigin
  @RequestMapping(value = "/deleteImage", method = RequestMethod.PUT)
  public void deleteImage(@RequestBody Map<String, String> pics) throws IOException {

    String fileName = pics.get("fileName");
    Path path = Paths.get(fileName);

    File[] files = picFolder.listFiles(new FileFilter() {

      @Override
      public boolean accept(File pathname) {
        System.err.println(pathname.getName() + " - " + path.getFileName().toString());
        return pathname.getName().equals(path.getFileName().toString());
      }
    });

    for (File file : files) {
      Files.delete(Paths.get(file.toURI()));
      Files.delete(Paths.get(thumbnailFolder + "/" + file.getName()));
    }
  }

}
