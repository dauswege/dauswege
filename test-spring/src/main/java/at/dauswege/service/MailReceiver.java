package at.dauswege.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Base64;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.GmailScopes;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import com.google.api.services.gmail.model.ModifyMessageRequest;

@Component
public class MailReceiver {

  /** Application name. */
  private static final String APPLICATION_NAME = "Gmail API Java Quickstart";

  /** Global instance of the {@link FileDataStoreFactory}. */
  private static FileDataStoreFactory DATA_STORE_FACTORY;

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = JacksonFactory
      .getDefaultInstance();

  /** Global instance of the HTTP transport. */
  private static HttpTransport HTTP_TRANSPORT;

  /** Global instance of the scopes required by this quickstart. */
  private static final List<String> SCOPES = Arrays.asList(
      GmailScopes.GMAIL_LABELS, GmailScopes.GMAIL_MODIFY);

  private static final List<String> LABELS = Arrays.asList("UNREAD");

  private final String userId;// = "w.u.v.hochzeit@gmail.com";

  private final String fileStorage; // = "C:/temp/pics/";

  private final Gmail service;

  private ImageService thumbnailService;


  public MailReceiver(String gmailUser, String picStorage,
      ImageService thumbnailService, Environment env) throws IOException, GeneralSecurityException {
    HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
    DATA_STORE_FACTORY =
        new FileDataStoreFactory(new File(env.getProperty("viewer.google.credentials.store")));
    userId = gmailUser;
    if (picStorage.endsWith("\\") || picStorage.endsWith("/")) {
      fileStorage = picStorage;
    } else {
      fileStorage = picStorage + "/";
    }

    this.thumbnailService = thumbnailService;
    this.service = this.getGmailService();
  }

  /**
   * Creates an authorized Credential object.
   * 
   * @return an authorized Credential object.
   * @throws IOException
   */
  private static Credential authorize() throws IOException {
    // Load client secrets.
    InputStream in = MailReceiver.class
        .getResourceAsStream("/client_secrets.json");
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(
        JSON_FACTORY, new InputStreamReader(in, "UTF-8"));

    // Build flow and trigger user authorization request.
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
            .setDataStoreFactory(DATA_STORE_FACTORY).build();

    Credential credential = new AuthorizationCodeInstalledApp(flow,
        new LocalServerReceiver()).authorize("daftinga@gmail.com");

    return credential;
  }

  /**
   * Build and return an authorized Gmail client service.
   * 
   * @return an authorized Gmail client service
   * @throws IOException
   */
  private Gmail getGmailService() throws IOException {
    Credential credential = authorize();

    return new Gmail.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
        .setApplicationName(APPLICATION_NAME).build();
  }

  public void processNewMails() throws IOException {

    ListMessagesResponse response = service.users().messages().list(userId)
        .setLabelIds(LABELS).execute();

    List<Message> messages = new ArrayList<>();

    while (response.getMessages() != null) {

      messages.addAll(response.getMessages());
      if (response.getNextPageToken() != null) {
        response = service.users().messages().list(userId)
            .setLabelIds(LABELS)
            .setPageToken(response.getNextPageToken()).execute();
      } else {

        break;

      }

    }

    this.processMessageList(messages);

  }

  private void processMessageList(List<Message> messages) throws IOException {

    for (Message message : messages) {
      this.downloadAttachments(service, userId, message.getId());
      this.setMessageRead(service, userId, message.getId());
    }

  }

  private void setMessageRead(Gmail service, String userId, String messageId)
      throws IOException {
    ModifyMessageRequest mods = new ModifyMessageRequest()
        .setRemoveLabelIds(LABELS);

    service.users().messages().modify(userId, messageId, mods).execute();
  }

  private void downloadAttachments(Gmail service, String userId,
      String messageId) throws IOException {

    Message message = service.users().messages().get(userId, messageId)
        .execute();

    List<MessagePart> parts = message.getPayload().getParts();
    for (MessagePart part : parts) {

      if (part.getFilename() != null && part.getFilename().length() > 0
          && part.getMimeType().contains("image")) {

        String filename = part.getFilename();
        String attId = part.getBody().getAttachmentId();

        MessagePartBody attachPart = service.users().messages()
            .attachments().get(userId, messageId, attId).execute();

        byte[] fileByteArray = Base64
            .decodeBase64(attachPart.getData());
        File outFile = new File(fileStorage + filename);
        FileOutputStream outFileStream = new FileOutputStream(outFile);
        outFileStream.write(fileByteArray);
        outFileStream.close();

        // Create thumbnail
        thumbnailService.createThumbnail(outFile);
      }
    }
  }

  // public static void main(String[] args) throws IOException, GeneralSecurityException {
  //
  // MailReceiver mailReceiver = new MailReceiver(
  // "w.u.v.hochzeit@gmail.com", "C:/temp/pics/",
  // new ImageService(), null);
  //
  // mailReceiver.processNewMails();
  //
  // }

}
