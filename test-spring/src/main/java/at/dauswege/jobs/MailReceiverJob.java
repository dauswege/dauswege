package at.dauswege.jobs;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import at.dauswege.service.MailReceiver;

@Component
public class MailReceiverJob {

  private static final Logger LOGGER = LoggerFactory
      .getLogger(MailReceiverJob.class);

  private final MailReceiver mailReceiver;

  public MailReceiverJob(MailReceiver mailReceiver) {
    super();
    this.mailReceiver = mailReceiver;
  }

  @Scheduled(fixedDelay = 30000)
  public void run() {

    LOGGER.info("MailReceiverJob started");

    try {
      mailReceiver.processNewMails();
    } catch (IOException e) {
      LOGGER.error(e.getMessage());
    }
    LOGGER.info("MailReceiverJob finished");
  }

}
