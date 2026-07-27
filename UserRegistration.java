package mycontactapp;

/*
 * UC-09: Search Contacts
 * Description: Searches contacts by name, phone, email, or tags
 * using Specification and Chain of Responsibility patterns.
 */

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

//================== Contact Class ==================
class Contact {

    private String name;
    private String phoneNumber;
    private String email;
    private String tag;

    public Contact(String name, String phoneNumber, String email, String tag) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "\nName  : " + name
                + "\nPhone : " + phoneNumber
                + "\nEmail : " + email
                + "\nTag   : " + tag;
    }
}

//================== Specification Interface ==================
interface SearchCriteria {

    Predicate<Contact> criteria();
}

//================== Search By Name ==================
class NameCriteria implements SearchCriteria {

    private String name;

    public NameCriteria(String name) {
        this.name = name;
    }

    @Override
    public Predicate<Contact> criteria() {

        return contact ->
                contact.getName().equalsIgnoreCase(name);
    }
}

//================== Search By Phone ==================
class PhoneCriteria implements SearchCriteria {

    private String phone;

    public PhoneCriteria(String phone) {
        this.phone = phone;
    }

    @Override
    public Predicate<Contact> criteria() {

        return contact ->
                contact.getPhoneNumber().equals(phone);
    }
}

//================== Search By Email ==================
class EmailCriteria implements SearchCriteria {

    private String email;

    public EmailCriteria(String email) {
        this.email = email;
    }

    @Override
    public Predicate<Contact> criteria() {

        Pattern pattern = Pattern.compile(email, Pattern.CASE_INSENSITIVE);

        return contact ->
                pattern.matcher(contact.getEmail()).find();
    }
}

//================== Search By Tag ==================
class TagCriteria implements SearchCriteria {

    private String tag;

    public TagCriteria(String tag) {
        this.tag = tag;
    }

    @Override
    public Predicate<Contact> criteria() {

        return contact ->
                contact.getTag().equalsIgnoreCase(tag);
    }
}

//================== Chain of Responsibility ==================
abstract class SearchHandler {

    protected SearchHandler nextHandler;

    public void setNextHandler(SearchHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract List<Contact> search(List<Contact> contacts, String value);
}

//================== Name Handler ==================
class NameSearchHandler extends SearchHandler {

    @Override
    public List<Contact> search(List<Contact> contacts, String value) {

        List<Contact> result = contacts.stream()
                .filter(new NameCriteria(value).criteria())
                .collect(Collectors.toList());

        if (!result.isEmpty()) {
            return result;
        }

        if (nextHandler != null) {
            return nextHandler.search(contacts, value);
        }

        return new ArrayList<>();
    }
}

//================== Phone Handler ==================
class PhoneSearchHandler extends SearchHandler {

    @Override
    public List<Contact> search(List<Contact> contacts, String value) {

        List<Contact> result = contacts.stream()
                .filter(new PhoneCriteria(value).criteria())
                .collect(Collectors.toList());

        if (!result.isEmpty()) {
            return result;
        }

        if (nextHandler != null) {
            return nextHandler.search(contacts, value);
        }

        return new ArrayList<>();
    }
}

//================== Email Handler ==================
class EmailSearchHandler extends SearchHandler {

    @Override
    public List<Contact> search(List<Contact> contacts, String value) {

        List<Contact> result = contacts.stream()
                .filter(new EmailCriteria(value).criteria())
                .collect(Collectors.toList());

        if (!result.isEmpty()) {
            return result;
        }

        if (nextHandler != null) {
            return nextHandler.search(contacts, value);
        }

        return new ArrayList<>();
    }
}

//================== Tag Handler ==================
class TagSearchHandler extends SearchHandler {

    @Override
    public List<Contact> search(List<Contact> contacts, String value) {

        List<Contact> result = contacts.stream()
                .filter(new TagCriteria(value).criteria())
                .collect(Collectors.toList());

        if (!result.isEmpty()) {
            return result;
        }

        return new ArrayList<>();
    }
}

//================== Main Class ==================
public class UserRegistration {

    public static void main(String[] args) {

        List<Contact> contacts = new ArrayList<>();

        contacts.add(new Contact("Nandini", "9876543210", "nandini@gmail.com", "Friend"));
        contacts.add(new Contact("Rahul", "9876501234", "rahul@gmail.com", "Office"));
        contacts.add(new Contact("Priya", "9123456789", "priya@gmail.com", "Family"));
        contacts.add(new Contact("Ramesh", "9988776655", "ramesh@gmail.com", "Friend"));

        String searchValue = "Friend";

        SearchHandler nameHandler = new NameSearchHandler();
        SearchHandler phoneHandler = new PhoneSearchHandler();
        SearchHandler emailHandler = new EmailSearchHandler();
        SearchHandler tagHandler = new TagSearchHandler();

        nameHandler.setNextHandler(phoneHandler);
        phoneHandler.setNextHandler(emailHandler);
        emailHandler.setNextHandler(tagHandler);

        List<Contact> result = nameHandler.search(contacts, searchValue);

        System.out.println("===== Search Result =====");

        if (result.isEmpty()) {
            System.out.println("No Contact Found.");
        } else {
            result.forEach(System.out::println);
        }
    }
}