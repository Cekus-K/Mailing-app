package pl.wsb.massmailing.message;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.wsb.massmailing.dao.ContactRepository;
import pl.wsb.massmailing.dao.entity.Contact;

import java.io.*;
import java.util.List;

@Service
class MessageService {

    private ContactRepository contactRepository;

    @Value("${sender}")
    private String senderEmailAddress;
    @Value("${api-key}")
    private String apiKey;

    private MessageService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public void sendMessageToAll(String emailContent) {
        for (Contact contact : contactRepository.findAll()) {
            sendMail(contact.getEmailAddress(), emailContent);
        }
    }

    public Contact addNewContact(String firstName, String lastName, String emailAddress) {
        return contactRepository.save(new Contact(firstName, lastName, emailAddress));
    }

    public List<Contact> showAllContacts() {
        return contactRepository.findAll();
    }

    private void sendMail(String recipient, String emailContent) {
        Email from = new Email(senderEmailAddress);
        String subject = "Message from app for mass email sending";
        Email to = new Email(recipient);
        Content content = new Content("text/html", emailContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(apiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    public void fillDB() {
        if (contactRepository.findByEmailAddress("test@email.com") == null) {
            contactRepository.save(new Contact(
                    "firstName", "lastName", "test@email.com")
            );
        }
    }
}
