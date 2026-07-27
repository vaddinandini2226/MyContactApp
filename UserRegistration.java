package mycontactapp;

/*
 * UC-06: Update Contact
 * Description: Updates existing contact information using
 * Command and Memento patterns with validation.
 */

import java.util.Scanner;

//================== Contact Class ==================
class Contact {

    private String name;
    private String phoneNumber;
    private String email;

    public Contact(String name, String phoneNumber, String email) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    // Copy Constructor (Deep Copy)
    public Contact(Contact contact) {
        this.name = contact.name;
        this.phoneNumber = contact.phoneNumber;
        this.email = contact.email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if (!name.trim().isEmpty()) {
            this.name = name;
        } else {
            System.out.println("Invalid Name.");
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {

        if (phoneNumber.matches("\\d{10}")) {
            this.phoneNumber = phoneNumber;
        } else {
            System.out.println("Invalid Phone Number.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            this.email = email;
        } else {
            System.out.println("Invalid Email.");
        }
    }

    @Override
    public String toString() {

        return "\n===== Contact Details ====="
                + "\nName  : " + name
                + "\nPhone : " + phoneNumber
                + "\nEmail : " + email;
    }
}

//================== Memento ==================
class ContactMemento {

    private Contact contact;

    public ContactMemento(Contact contact) {

        // Defensive Copy
        this.contact = new Contact(contact);
    }

    public Contact getSavedContact() {

        // Defensive Copy
        return new Contact(contact);
    }
}

//================== CareTaker ==================
class ContactHistory {

    private ContactMemento memento;

    public void save(Contact contact) {
        memento = new ContactMemento(contact);
    }

    public Contact restore() {
        return memento.getSavedContact();
    }
}

//================== Command Interface ==================
interface Command {

    void execute();

    void undo();
}

//================== Update Command ==================
class UpdateContactCommand implements Command {

    private Contact contact;
    private ContactHistory history;

    private String name;
    private String phone;
    private String email;

    public UpdateContactCommand(Contact contact,
                                ContactHistory history,
                                String name,
                                String phone,
                                String email) {

        this.contact = contact;
        this.history = history;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    @Override
    public void execute() {

        history.save(contact);

        contact.setName(name);
        contact.setPhoneNumber(phone);
        contact.setEmail(email);

        System.out.println("\nContact Updated Successfully.");
    }

    @Override
    public void undo() {

        Contact oldContact = history.restore();

        contact.setName(oldContact.getName());
        contact.setPhoneNumber(oldContact.getPhoneNumber());
        contact.setEmail(oldContact.getEmail());

        System.out.println("\nUndo Successful.");
    }
}

//================== Command Manager ==================
class ContactManager {

    public void executeCommand(Command command) {
        command.execute();
    }

    public void undoCommand(Command command) {
        command.undo();
    }
}

//================== Main Class ==================
public class UserRegistration {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Contact contact = new Contact(
                "Nandini",
                "9876543210",
                "nandini@gmail.com");

        ContactHistory history = new ContactHistory();
        ContactManager manager = new ContactManager();

        try {

            System.out.println(contact);

            System.out.println("\n===== Update Contact =====");

            System.out.print("Enter New Name : ");
            String name = sc.nextLine();

            System.out.print("Enter New Phone : ");
            String phone = sc.nextLine();

            System.out.print("Enter New Email : ");
            String email = sc.nextLine();

            UpdateContactCommand command =
                    new UpdateContactCommand(
                            contact,
                            history,
                            name,
                            phone,
                            email);

            manager.executeCommand(command);

            System.out.println(contact);

            System.out.print("\nUndo Changes? (yes/no) : ");
            String choice = sc.nextLine();

            if (choice.equalsIgnoreCase("yes")) {

                manager.undoCommand(command);

                System.out.println(contact);
            }

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());
        }

        sc.close();
    }
}