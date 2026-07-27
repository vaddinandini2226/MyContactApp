package mycontactapp;

/*
 * UC-03: Profile Management
 * Description: Allows a logged-in user to update profile information,
 * change password, and manage preferences using Command Pattern.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

//================== User Class ==================
class User {

    private String name;
    private String email;
    private String password;
    private String preference;

    public User(String name, String email, String password, String preference) {
        this.name = name;
        this.email = email;
        this.password = PasswordUtil.hashPassword(password);
        this.preference = preference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if (!name.trim().isEmpty()) {
            this.name = name;
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

    public void setPassword(String password) {

        if (password.length() >= 6) {
            this.password = PasswordUtil.hashPassword(password);
        } else {
            System.out.println("Password should contain at least 6 characters.");
        }
    }

    public void setPreference(String preference) {
        this.preference = preference;
    }

    public void displayProfile() {

        System.out.println("\n===== User Profile =====");
        System.out.println("Name       : " + name);
        System.out.println("Email      : " + email);
        System.out.println("Preference : " + preference);
    }
}

//================== Command Interface ==================
interface ProfileCommand {

    void execute();
}

//================== Update Profile Command ==================
class UpdateProfileCommand implements ProfileCommand {

    private User user;
    private String name;
    private String email;

    public UpdateProfileCommand(User user, String name, String email) {
        this.user = user;
        this.name = name;
        this.email = email;
    }

    @Override
    public void execute() {

        user.setName(name);
        user.setEmail(email);

        System.out.println("Profile Updated Successfully.");
    }
}

//================== Change Password Command ==================
class ChangePasswordCommand implements ProfileCommand {

    private User user;
    private String password;

    public ChangePasswordCommand(User user, String password) {
        this.user = user;
        this.password = password;
    }

    @Override
    public void execute() {

        user.setPassword(password);

        System.out.println("Password Updated Successfully.");
    }
}

//================== Update Preference Command ==================
class UpdatePreferenceCommand implements ProfileCommand {

    private User user;
    private String preference;

    public UpdatePreferenceCommand(User user, String preference) {
        this.user = user;
        this.preference = preference;
    }

    @Override
    public void execute() {

        user.setPreference(preference);

        System.out.println("Preference Updated Successfully.");
    }
}

//================== Command Invoker ==================
class ProfileManager {

    public void executeCommand(ProfileCommand command) {
        command.execute();
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

        User user = new User(
                "Nandini",
                "nandini@gmail.com",
                "admin123",
                "Dark Theme");

        ProfileManager manager = new ProfileManager();

        try {

            System.out.println("===== Profile Management =====");
            System.out.println("1. Update Profile");
            System.out.println("2. Change Password");
            System.out.println("3. Update Preference");

            System.out.print("Enter Choice : ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:

                    System.out.print("Enter New Name : ");
                    String name = sc.nextLine();

                    System.out.print("Enter New Email : ");
                    String email = sc.nextLine();

                    manager.executeCommand(
                            new UpdateProfileCommand(user, name, email));

                    break;

                case 2:

                    System.out.print("Enter New Password : ");
                    String password = sc.nextLine();

                    manager.executeCommand(
                            new ChangePasswordCommand(user, password));

                    break;

                case 3:

                    System.out.print("Enter Preference : ");
                    String preference = sc.nextLine();

                    manager.executeCommand(
                            new UpdatePreferenceCommand(user, preference));

                    break;

                default:
                    System.out.println("Invalid Choice.");
            }

            user.displayProfile();

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());
        }

        sc.close();
    }
}