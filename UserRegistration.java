package mycontactapp;

/*
 * UC-08: Bulk Contact Operations
 * Description: Performs bulk operations like delete, tag,
 * and export on multiple contacts using Composite Pattern.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//================== Contact Component ==================
interface ContactComponent {

    void delete();

    void addTag(String tag);

    void export();
}

//================== Contact Class ==================
class Contact implements ContactComponent {

    private String name;
    private String phoneNumber;
    private List<String> tags = new ArrayList<>();

    public Contact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
        return name;
    }

    @Override
    public void delete() {
        System.out.println(name + " deleted.");
    }

    @Override
    public void addTag(String tag) {
        tags.add(tag);
        System.out.println("Tag '" + tag + "' added to " + name);
    }

    @Override
    public void export() {
        System.out.println(name + " - " + phoneNumber);
    }

    @Override
    public String toString() {
        return name + " (" + phoneNumber + ")";
    }
}

//================== Composite Class ==================
class ContactGroup implements ContactComponent {

    private List<ContactComponent> contacts = new ArrayList<>();

    public void add(ContactComponent contact) {
        contacts.add(contact);
    }

    @Override
    public void delete() {
        contacts.forEach(ContactComponent::delete);
    }

    @Override
    public void addTag(String tag) {
        contacts.forEach(contact -> contact.addTag(tag));
    }

    @Override
    public void export() {
        contacts.forEach(ContactComponent::export);
    }
}

//================== Main Class ==================
public class UserRegistration {

    public static void main(String[] args) {

        List<Contact> contactList = new ArrayList<>();

        contactList.add(new Contact("Nandini", "9876543210"));
        contactList.add(new Contact("Rahul", "9876501234"));
        contactList.add(new Contact("Priya", "9123456789"));
        contactList.add(new Contact("Ramesh", "9876123456"));

        System.out.println("===== All Contacts =====");

        contactList.forEach(System.out::println);

        // Filtering using Streams API
        List<Contact> selectedContacts = contactList.stream()
                .filter(contact -> contact.getName().startsWith("R"))
                .collect(Collectors.toList());

        ContactGroup group = new ContactGroup();

        selectedContacts.forEach(group::add);

        System.out.println("\n===== Bulk Tag Operation =====");
        group.addTag("Friends");

        System.out.println("\n===== Bulk Export Operation =====");
        group.export();

        System.out.println("\n===== Bulk Delete Operation =====");
        group.delete();
    }
}