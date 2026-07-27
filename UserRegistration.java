package mycontactapp;

/*
 * UC-07: Delete Contact
 * Description: Deletes a contact after user confirmation using
 * Observer Pattern and demonstrates soft delete and hard delete.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//================== Observer Interface ==================
interface Observer {

    void update(String message);
}

//================== Activity Logger ==================
class ActivityLogger implements Observer {

    @Override
    public void update(String message) {
        System.out.println("Logger : " + message);
    }
}

//================== Notification Service ==================
class NotificationService implements Observer {

    @Override
    public void update(String message) {
        System.out.println("Notification : " + message);
    }
}

//================== Contact Class ==================
class Contact {

    private int contactId;
    private String name;
    private String phoneNumber;
    private boolean deleted;

    private List<Observer> observers = new ArrayList<>();

    public Contact(int contactId, String name, String phoneNumber) {
        this.contactId = contactId;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.deleted = false;
    }

    public int getContactId() {
        return contactId;
    }

    public String getName() {
        return name;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers(String message) {

        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    // Soft Delete
    public void softDelete() {

        deleted = true;

        notifyObservers("Contact '" + name + "' moved to recycle bin.");
    }

    // Hard Delete
    public void hardDelete(List<Contact> contacts) {

        contacts.remove(this);

        notifyObservers("Contact '" + name + "' permanently deleted.");
    }

    @Override
    public String toString() {

        return "\nContact ID : " + contactId
                + "\nName       : " + name
                + "\nPhone      : " + phoneNumber
                + "\nDeleted    : " + deleted;
    }
}

//================== Main Class ==================
public class UserRegistration {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        List<Contact> contacts = new ArrayList<>();

        Contact contact = new Contact(
                101,
                "Nandini",
                "9876543210");

        contact.addObserver(new ActivityLogger());
        contact.addObserver(new NotificationService());

        contacts.add(contact);

        try {

            System.out.println("===== Delete Contact =====");

            System.out.println(contact);

            System.out.print("\nAre you sure you want to delete this contact? (yes/no) : ");
            String confirm = sc.nextLine();

            if (confirm.equalsIgnoreCase("yes")) {

                System.out.print("1. Soft Delete\n2. Hard Delete\nEnter Choice : ");
                int choice = Integer.parseInt(sc.nextLine());

                if (choice == 1) {

                    contact.softDelete();

                    System.out.println("\nContact Soft Deleted Successfully.");
                    System.out.println(contact);

                } else if (choice == 2) {

                    contact.hardDelete(contacts);

                    System.out.println("\nContact Hard Deleted Successfully.");
                    System.out.println("Remaining Contacts : " + contacts.size());

                } else {

                    System.out.println("Invalid Choice.");
                }

            } else {

                System.out.println("Delete Operation Cancelled.");
            }

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());
        }

        sc.close();
    }
}