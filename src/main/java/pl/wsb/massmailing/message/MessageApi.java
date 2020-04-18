package pl.wsb.massmailing.message;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import pl.wsb.massmailing.dao.entity.Contact;

import java.util.List;

@Route
class MessageApi extends VerticalLayout {

    private MessageService messageService;

    private TextArea allContacts;
    private TextArea emailContent;
    private TextField firstName;
    private TextField lastName;
    private TextField emailAddress;

    MessageApi(MessageService messageService) {
        this.messageService = messageService;

        // fields for display contacts
        allContacts = new TextArea("Display all contacts");
        Button displayContacts = new Button("Show all");

        // settings for display contacts fields
        allContacts.getStyle().set("minWidth", "600px");
        displayContacts.getStyle().set("minWidth", "300px");
        displayContacts.addClickListener(click -> {
            allContacts.setValue(showContacts().toString());
        });

        // fields for send emails
        emailContent = new TextArea("Email content");
        Button sendEmail = new Button("Send messages");

        // settings for send emails fields
        emailContent.getStyle().set("minHeight", "200px");
        emailContent.getStyle().set("minWidth", "600px");
        sendEmail.getStyle().set("minWidth", "300px");
        emailContent.setPlaceholder("Write here ...");
        sendEmail.addClickListener(click -> sendMessages());

        // fields for add new contact
        firstName = new TextField("New contact");
        lastName = new TextField();
        emailAddress = new TextField();
        Button newContact = new Button("Add");

        // settings for add new contact fields
        firstName.getStyle().set("minWidth", "600px");
        lastName.getStyle().set("minWidth", "600px");
        emailAddress.getStyle().set("minWidth", "600px");
        newContact.getStyle().set("minWidth", "300px");
        firstName.setPlaceholder("First name ...");
        lastName.setPlaceholder("Last name ...");
        emailAddress.setPlaceholder("Email address ...");
        newContact.addClickListener(click -> addContact());

        add(allContacts, displayContacts);
        add(emailContent, sendEmail);
        add(firstName, lastName, emailAddress, newContact);
    }

    public List<Contact> showContacts() {
        return messageService.showAllContacts();
    }

    private void sendMessages() {
        messageService.sendMessageToAll(emailContent.getValue());
    }

    private void addContact() {
        messageService.addNewContact(
                firstName.getValue(), lastName.getValue(), emailAddress.getValue()
        );
    }
}
