package at.dauswege.jobs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RadioSalzburgDownload implements CommandLineRunner {

	public static String filePathString = "c:\\temp\\rsdownload.txt";

	@Override
	public void run(String... arg0) throws Exception {

		File file = new File(filePathString);
		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		bufferedReader.lines().filter(l -> l.contains("mp3Url")).forEach(l -> System.out.println(l));

		IOUtils.closeQuietly(bufferedReader);
	}

	public static void main(String... arg0) throws Exception {
		RadioSalzburgDownload radioSalzburgDownload = new RadioSalzburgDownload();
		radioSalzburgDownload.run();
	}

}
