package mycontactapp;
/*
 * UC-01: User Registration
 * Description: Registers a new user using Builder and Factory patterns
 * with input validation and password hashing.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

//================== User Class ==================
class User {

    private String name;
    private String email;
    private String password;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void display() {
        System.out.println("\n===== User Details =====");
        System.out.println("Name      : " + name);
        System.out.println("Email     : " + email);
        System.out.println("Password  : " + password);
    }
}

//================== Free User ==================
class FreeUser extends User {

    public FreeUser(String name, String email, String password) {
        super(name, email, password);
    }
}

//================== Premium User ==================
class PremiumUser extends User {

    public PremiumUser(String name, String email, String password) {
        super(name, email, password);
    }
}

//================== Builder Pattern ==================
class UserBuilder {

    private String name;
    private String email;
    private String password;

    public UserBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public UserBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public UserBuilder setPassword(String password) {
        this.password = password;
        return this;
    }

    public User build() {
        return new User(name, email, password);
    }
}

//================== Factory Pattern ==================
class UserFactory {

    public static User createUser(String type, String name, String email, String password) {

        UserBuilder builder = new UserBuilder()
                .setName(name)
                .setEmail(email)
                .setPassword(password);

        User user = builder.build();

        if (type.equalsIgnoreCase("Premium")) {
            return new PremiumUser(user.getName(), user.getEmail(), user.getPassword());
        }

        return new FreeUser(user.getName(), user.getEmail(), user.getPassword());
    }
}

//================== Validation ==================
class Validator {

    public static boolean validateEmail(String email) {
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    }
}

//================== Password Utility ==================
class PasswordUtil {

    public static String hashPassword(String password) {

        try {

            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();

            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }

            return sb.toString();

        } catch (NoSuchAlgorithmException e) {
            return password;
        }
    }
}

//================== Main Class ==================
public class UserRegistration {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        try {

            System.out.println("===== User Registration =====");

            System.out.print("Enter Name : ");
            String name = sc.nextLine();

            System.out.print("Enter Email : ");
            String email = sc.nextLine();

            if (!Validator.validateEmail(email)) {
                throw new Exception("Invalid Email Address!");
            }

            System.out.print("Enter Password : ");
            String password = sc.nextLine();

            if (password.length() < 6) {
                throw new Exception("Password must contain at least 6 characters!");
            }

            password = PasswordUtil.hashPassword(password);

            System.out.print("Enter User Type (Free/Premium) : ");
            String type = sc.nextLine();

            User user = UserFactory.createUser(type, name, email, password);

            System.out.println("\nRegistration Successful!");
            user.display();

        } catch (Exception e) {
            System.out.println("Error : " + e.getMessage());
        }

        sc.close();
    }
}