package mycontactapp;

/*
 * UC-11: Contact Tags
 * Description: Creates custom tags and assigns them to contacts
 * using Flyweight Pattern for shared tag objects.
 */

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

//================== Predefined Tags ==================
enum PredefinedTag {
    FAMILY,
    FRIENDS,
    WORK
}

//================== Tag Class ==================
class Tag {

    private String tagName;

    public Tag(String tagName) {

        if (tagName == null || tagName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tag cannot be empty.");
        }

        this.tagName = tagName;
    }

    public String getTagName() {
        return tagName;
    }

    @Override
    public boolean equals(Object obj) {

        if (this == obj)
            return true;

        if (!(obj instanceof Tag))
            return false;

        Tag tag = (Tag) obj;

        return tagName.equalsIgnoreCase(tag.tagName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tagName.toLowerCase());
    }

    @Override
    public String toString() {
        return tagName;
    }
}

//================== Flyweight Factory ==================
class TagFactory {

    private static HashMap<String, Tag> tags = new HashMap<>();

    public static Tag getTag(String tagName) {

        String key = tagName.toLowerCase();

        if (!tags.containsKey(key)) {
            tags.put(key, new Tag(tagName));
        }

        return tags.get(key);
    }
}

//================== Contact Class ==================
class Contact {

    private String name;
    private Set<Tag> tags = new HashSet<>();

    public Contact(String name) {
        this.name = name;
    }

    public void addTag(Tag tag) {
        tags.add(tag);
    }

    public void display() {

        System.out.println("\nContact Name : " + name);
        System.out.println("Tags         : " + tags);
    }
}

//================== Main Class ==================
public class UserRegistration {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Contact contact = new Contact("Nandini");

        try {

            System.out.println("===== Contact Tags =====");

            EnumSet<PredefinedTag> predefinedTags =
                    EnumSet.allOf(PredefinedTag.class);

            System.out.println("Predefined Tags : " + predefinedTags);

            System.out.print("\nHow Many Custom Tags? : ");
            int n = sc.nextInt();
            sc.nextLine();

            for (int i = 1; i <= n; i++) {

                System.out.print("Enter Tag " + i + " : ");

                String tagName = sc.nextLine();

                Tag tag = TagFactory.getTag(tagName);

                contact.addTag(tag);
            }

            System.out.println("\nTags Assigned Successfully.");

            contact.display();

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());
        }

        sc.close();
    }
}