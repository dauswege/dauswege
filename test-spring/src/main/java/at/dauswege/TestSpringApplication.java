package at.dauswege;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import at.dauswege.controllers.WeddingController;
import at.dauswege.jobs.MailReceiverJob;
import at.dauswege.service.ImageService;
import at.dauswege.service.MailReceiver;

@SpringBootApplication
@EnableScheduling
@PropertySources({@PropertySource("classpath:viewer.properties"),
    @PropertySource(value = "file:///${viewer.propertiesFile}", ignoreResourceNotFound = true)})
public class TestSpringApplication {

  public static final String PICFOLDER_PROP = "viewer.folder.pics";
  public static final String THUMBNAIL_PROP = "viewer.folder.thumbnails";

  public static void main(String[] args) {
    SpringApplication.run(TestSpringApplication.class, args);
  }

  @Bean
  @Autowired
  public MailReceiverJob mailReceiverJob(MailReceiver mailReceiver) {
    return new MailReceiverJob(mailReceiver);
  }

  @Bean
  @Autowired
  public WeddingController weddingController(Environment environment) {
    return new WeddingController(environment.getProperty(PICFOLDER_PROP),
        environment.getProperty(THUMBNAIL_PROP), new ImageService());
  }

  @Bean
  @Autowired
  public MailReceiver mailReceiver(Environment environment)
      throws IOException, GeneralSecurityException {
    return new MailReceiver("w.u.v.hochzeit@gmail.com", environment.getProperty(PICFOLDER_PROP),
        new ImageService(), environment);
  }

}
