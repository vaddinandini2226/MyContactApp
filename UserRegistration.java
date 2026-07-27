package mycontactapp;

/*
 * UC-05: View Contact
 * Description: Displays complete information of a contact using
 * Decorator Pattern with formatted output.
 */

import java.util.Optional;
import java.util.UUID;

//================== Contact Class ==================
class Contact {

    private UUID contactId;
    private String name;
    private String phoneNumber;
    private String email;
    private String address;

    public Contact(String name, String phoneNumber, String email, String address) {
        this.contactId = UUID.randomUUID();
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
    }

    public UUID getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public Optional<String> getEmail() {
        return Optional.ofNullable(email);
    }

    public Optional<String> getAddress() {
        return Optional.ofNullable(address);
    }

    @Override
    public String toString() {

        return String.format(
                "Contact ID : %s%nName       : %s%nPhone      : %s%nEmail      : %s%nAddress    : %s",
                contactId,
                name,
                phoneNumber,
                getEmail().orElse("Not Available"),
                getAddress().orElse("Not Available"));
    }
}

//================== Immutable View Object ==================
final class ContactView {

    private final Contact contact;

    public ContactView(Contact contact) {
        this.contact = contact;
    }

    public Contact getContact() {
        return contact;
    }
}

//================== Decorator Interface ==================
interface ContactDisplay {

    void display();
}

//================== Basic Display ==================
class BasicDisplay implements ContactDisplay {

    private ContactView contactView;

    public BasicDisplay(ContactView contactView) {
        this.contactView = contactView;
    }

    @Override
    public void display() {
        System.out.println(contactView.getContact());
    }
}

//================== Decorator Class ==================
abstract class ContactDecorator implements ContactDisplay {

    protected ContactDisplay contactDisplay;

    public ContactDecorator(ContactDisplay contactDisplay) {
        this.contactDisplay = contactDisplay;
    }
}

//================== Formatted Display ==================
class FormattedDisplay extends ContactDecorator {

    public FormattedDisplay(ContactDisplay contactDisplay) {
        super(contactDisplay);
    }

    @Override
    public void display() {

        System.out.println("=================================");
        System.out.println("      CONTACT INFORMATION");
        System.out.println("=================================");

        contactDisplay.display();

        System.out.println("=================================");
    }
}

//================== Main Class ==================
public class UserRegistration {

    public static void main(String[] args) {

        Contact contact = new Contact(
                "Nandini",
                "9876543210",
                "nandini@gmail.com",
                "Bangalore");

        ContactView contactView = new ContactView(contact);

        ContactDisplay display =
                new FormattedDisplay(new BasicDisplay(contactView));

        display.display();
    }
}