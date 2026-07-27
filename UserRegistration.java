package mycontactapp;

/*
 * UC-12: Assign Tags to Contact
 * Description: Assigns one or more tags to a contact using
 * Observer Pattern and maintains bidirectional relationship.
 */

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

//================== Observer Interface ==================
interface Observer {

    void update(String message);
}

//================== UI Observer ==================
class ContactUI implements Observer {

    @Override
    public void update(String message) {
        System.out.println("UI Updated : " + message);
    }
}

//================== Tag Class ==================
class Tag {

    private String tagName;
    private Set<Contact> contacts = new HashSet<>();

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    public void addContact(Contact contact) {
        contacts.add(contact);
    }

    public void removeContact(Contact contact) {
        contacts.remove(contact);
    }

    @Override
    public String toString() {
        return tagName;
    }
}

//================== Association Class ==================
class ContactTag {

    private Contact contact;
    private Tag tag;

    public ContactTag(Contact contact, Tag tag) {
        this.contact = contact;
        this.tag = tag;
    }

    public Contact getContact() {
        return contact;
    }

    public Tag getTag() {
        return tag;
    }
}

//================== Contact Class ==================
class Contact {

    private String name;
    private Set<Tag> tags = new HashSet<>();
    private Set<Observer> observers = new HashSet<>();

    public Contact(String name) {
        this.name = name;
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    private void notifyObservers(String message) {

        for (Observer observer : observers) {
            observer.update(message);
        }
    }

    public void addTag(Tag tag) {

        tags.add(tag);
        tag.addContact(this);

        new ContactTag(this, tag);

        notifyObservers("Tag '" + tag.getTagName() + "' added to " + name);
    }

    public void removeTag(Tag tag) {

        tags.remove(tag);
        tag.removeContact(this);

        notifyObservers("Tag '" + tag.getTagName() + "' removed from " + name);
    }

    public void display() {

        System.out.println("\nContact : " + name);
        System.out.println("Tags    : " + tags);
    }
}

//================== Main Class ==================
public class UserRegistration {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Contact contact = new Contact("Nandini");

        contact.addObserver(new ContactUI());

        try {

            System.out.println("===== Assign Tags =====");

            System.out.print("How Many Tags? : ");
            int n = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= n; i++) {

                System.out.print("Enter Tag " + i + " : ");

                Tag tag = new Tag(sc.nextLine());

                contact.addTag(tag);
            }

            contact.display();

            System.out.print("\nEnter Tag To Remove : ");
            Tag removeTag = new Tag(sc.nextLine());

            contact.removeTag(removeTag);

            contact.display();

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());
        }

        sc.close();
    }
}