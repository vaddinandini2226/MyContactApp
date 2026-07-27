package mycontactapp;

/*
 * UC-10: Filter Contacts
 * Description: Applies multiple filters like tag, date added,
 * and frequently contacted using Composite and Strategy patterns.
 */

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

//================== Contact Class ==================
class Contact {

    private String name;
    private String tag;
    private LocalDate dateAdded;
    private int contactCount;

    public Contact(String name, String tag, LocalDate dateAdded, int contactCount) {
        this.name = name;
        this.tag = tag;
        this.dateAdded = dateAdded;
        this.contactCount = contactCount;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public LocalDate getDateAdded() {
        return dateAdded;
    }

    public int getContactCount() {
        return contactCount;
    }

    @Override
    public String toString() {
        return "\nName          : " + name
                + "\nTag           : " + tag
                + "\nDate Added    : " + dateAdded
                + "\nContact Count : " + contactCount;
    }
}

//================== Filter Interface ==================
interface ContactFilter {

    List<Contact> filter(List<Contact> contacts);
}

//================== Tag Filter ==================
class TagFilter implements ContactFilter {

    private String tag;

    public TagFilter(String tag) {
        this.tag = tag;
    }

    @Override
    public List<Contact> filter(List<Contact> contacts) {

        return contacts.stream()
                .filter(contact -> contact.getTag().equalsIgnoreCase(tag))
                .collect(Collectors.toList());
    }
}

//================== Date Filter ==================
class DateFilter implements ContactFilter {

    private LocalDate date;

    public DateFilter(LocalDate date) {
        this.date = date;
    }

    @Override
    public List<Contact> filter(List<Contact> contacts) {

        return contacts.stream()
                .filter(contact -> contact.getDateAdded().isEqual(date))
                .collect(Collectors.toList());
    }
}

//================== Frequently Contacted Filter ==================
class FrequentContactFilter implements ContactFilter {

    private int minimumCount;

    public FrequentContactFilter(int minimumCount) {
        this.minimumCount = minimumCount;
    }

    @Override
    public List<Contact> filter(List<Contact> contacts) {

        return contacts.stream()
                .filter(contact -> contact.getContactCount() >= minimumCount)
                .collect(Collectors.toList());
    }
}

//================== Composite Filter ==================
class CompositeFilter implements ContactFilter {

    private List<ContactFilter> filters = new ArrayList<>();

    public void addFilter(ContactFilter filter) {
        filters.add(filter);
    }

    @Override
    public List<Contact> filter(List<Contact> contacts) {

        List<Contact> result = contacts;

        for (ContactFilter filter : filters) {
            result = filter.filter(result);
        }

        return result;
    }
}

//================== Strategy Interface ==================
interface SortStrategy {

    List<Contact> sort(List<Contact> contacts);
}

//================== Sort By Name ==================
class NameSortStrategy implements SortStrategy {

    @Override
    public List<Contact> sort(List<Contact> contacts) {

        return contacts.stream()
                .sorted(Comparator.comparing(Contact::getName))
                .collect(Collectors.toList());
    }
}

//================== Sort By Contact Count ==================
class ContactCountSortStrategy implements SortStrategy {

    @Override
    public List<Contact> sort(List<Contact> contacts) {

        return contacts.stream()
                .sorted(Comparator.comparing(Contact::getContactCount).reversed())
                .collect(Collectors.toList());
    }
}

//================== Strategy Context ==================
class FilterManager {

    private SortStrategy strategy;

    public FilterManager(SortStrategy strategy) {
        this.strategy = strategy;
    }

    public List<Contact> sortContacts(List<Contact> contacts) {
        return strategy.sort(contacts);
    }
}

//================== Main Class ==================
public class UserRegistration {

    public static void main(String[] args) {

        List<Contact> contacts = new ArrayList<>();

        contacts.add(new Contact("Nandini", "Friend", LocalDate.of(2025, 1, 10), 20));
        contacts.add(new Contact("Rahul", "Office", LocalDate.of(2025, 2, 15), 8));
        contacts.add(new Contact("Priya", "Friend", LocalDate.of(2025, 1, 10), 15));
        contacts.add(new Contact("Ramesh", "Family", LocalDate.of(2025, 3, 20), 25));

        CompositeFilter compositeFilter = new CompositeFilter();

        compositeFilter.addFilter(new TagFilter("Friend"));
        compositeFilter.addFilter(new DateFilter(LocalDate.of(2025, 1, 10)));
        compositeFilter.addFilter(new FrequentContactFilter(10));

        List<Contact> filteredContacts = compositeFilter.filter(contacts);

        FilterManager manager = new FilterManager(new ContactCountSortStrategy());

        filteredContacts = manager.sortContacts(filteredContacts);

        System.out.println("===== Filtered Contacts =====");

        if (filteredContacts.isEmpty()) {
            System.out.println("No Contacts Found.");
        } else {
            filteredContacts.forEach(System.out::println);
        }
    }
}