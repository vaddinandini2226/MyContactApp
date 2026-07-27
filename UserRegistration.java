package mycontactapp;

/*
 * UC-04: Create Contact
 * Description: Creates a new contact with multiple phone numbers,
 * email addresses, and optional fields using Builder and Factory patterns.
 */

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

//================== Phone Number ==================
class PhoneNumber {

    private String number;

    public PhoneNumber(String number) {
        this.number = number;
    }

    public String getNumber() {
        return number;
    }
}

//================== Email ==================
class Email {

    private String email;

    public Email(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}

//================== Contact Class ==================
class Contact {

    private UUID contactId;
    private String name;
    private List<PhoneNumber> phoneNumbers;
    private List<Email> emailAddresses;
    private String address;
    private LocalDateTime createdOn;

    public Contact(String name, List<PhoneNumber> phoneNumbers,
                   List<Email> emailAddresses, String address) {

        this.contactId = UUID.randomUUID();
        this.name = name;
        this.phoneNumbers = phoneNumbers;
        this.emailAddresses = emailAddresses;
        this.address = address;
        this.createdOn = LocalDateTime.now();
    }

    public void display() {

        System.out.println("\n===== Contact Details =====");
        System.out.println("Contact ID : " + contactId);
        System.out.println("Name       : " + name);

        System.out.print("Phone No   : ");
        for (PhoneNumber phone : phoneNumbers) {
            System.out.print(phone.getNumber() + " ");
        }

        System.out.print("\nEmail      : ");
        for (Email email : emailAddresses) {
            System.out.print(email.getEmail() + " ");
        }

        System.out.println("\nAddress    : " + address);
        System.out.println("Created On : " + createdOn);
    }
}

//================== Person Contact ==================
class Person extends Contact {

    public Person(String name, List<PhoneNumber> phoneNumbers,
                  List<Email> emailAddresses, String address) {

        super(name, phoneNumbers, emailAddresses, address);
    }
}

//================== Organization Contact ==================
class Organization extends Contact {

    public Organization(String name, List<PhoneNumber> phoneNumbers,
                        List<Email> emailAddresses, String address) {

        super(name, phoneNumbers, emailAddresses, address);
    }
}

//================== Builder Pattern ==================
class ContactBuilder {

    private String name;
    private List<PhoneNumber> phoneNumbers = new ArrayList<>();
    private List<Email> emailAddresses = new ArrayList<>();
    private String address;

    public ContactBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ContactBuilder addPhoneNumber(String phone) {
        phoneNumbers.add(new PhoneNumber(phone));
        return this;
    }

    public ContactBuilder addEmail(String email) {
        emailAddresses.add(new Email(email));
        return this;
    }

    public ContactBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public Contact build() {
        return new Contact(name, phoneNumbers, emailAddresses, address);
    }

    public String getName() {
        return name;
    }

    public List<PhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public List<Email> getEmailAddresses() {
        return emailAddresses;
    }

    public String getAddress() {
        return address;
    }
}

//================== Factory Pattern ==================
class ContactFactory {

    public static Contact createContact(String type, ContactBuilder builder) {

        if (type.equalsIgnoreCase("Organization")) {

            return new Organization(
                    builder.getName(),
                    builder.getPhoneNumbers(),
                    builder.getEmailAddresses(),
                    builder.getAddress());
        }

        return new Person(
                builder.getName(),
                builder.getPhoneNumbers(),
                builder.getEmailAddresses(),
                builder.getAddress());
    }
}

//================== Main Class ==================
public class UserRegistration {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {

            System.out.println("===== Create Contact =====");

            System.out.print("Enter Contact Type (Person/Organization) : ");
            String type = sc.nextLine();

            System.out.print("Enter Name : ");
            String name = sc.nextLine();

            ContactBuilder builder = new ContactBuilder();
            builder.setName(name);

            System.out.print("How Many Phone Numbers? : ");
            int phoneCount = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= phoneCount; i++) {

                System.out.print("Enter Phone " + i + " : ");
                builder.addPhoneNumber(sc.nextLine());
            }

            System.out.print("How Many Email Addresses? : ");
            int emailCount = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= emailCount; i++) {

                System.out.print("Enter Email " + i + " : ");
                builder.addEmail(sc.nextLine());
            }

            System.out.print("Enter Address (Optional) : ");
            builder.setAddress(sc.nextLine());

            Contact contact = ContactFactory.createContact(type, builder);

            System.out.println("\nContact Created Successfully.");

            contact.display();

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());
        }

        sc.close();
    }
}