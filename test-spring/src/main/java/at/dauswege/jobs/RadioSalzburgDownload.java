package at.dauswege.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;

// @Component
public class RadioSalzburgDownload implements CommandLineRunner {

  public static String filePathString = "c:\\temp\\rsdownload.txt";

  @Override
  public void run(String... arg0) throws Exception {

    File file = new File(filePathString);
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

    List<String> mp3LinesList = bufferedReader.lines()
        .filter(l -> l.contains("mp3Url"))
        .collect(Collectors.toList());

    Pattern pattern = Pattern.compile(".*mp3Url=(.*)&.*");
    List<String> mp3Urls = new ArrayList<>();
    for (String mp3Line : mp3LinesList) {
      Matcher matcher = pattern.matcher(mp3Line);
      if (matcher.matches()) {
        String mp3Url = matcher.group(1);
        mp3Urls.add(mp3Url);
      }
    }

    for (String mp3UrlString : mp3Urls) {
      URL mp3Url = new URL(mp3UrlString);
      InputStream is = mp3Url.openStream();
      OutputStream outstream = new FileOutputStream(
          new File(
              "c:\\temp\\" + mp3UrlString.substring(StringUtils.lastIndexOf(mp3UrlString, "/"))));
      byte[] buffer = new byte[4096];
      int len;
      while ((len = is.read(buffer)) > 0) {
        outstream.write(buffer, 0, len);
      }
      outstream.close();
    }

    IOUtils.closeQuietly(bufferedReader);
  }

  public static void main(String... arg0) throws Exception {
    RadioSalzburgDownload radioSalzburgDownload = new RadioSalzburgDownload();
    radioSalzburgDownload.run();
  }

}
