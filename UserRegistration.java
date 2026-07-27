package mycontactapp;
/*
 * UC-02: User Authentication
 * Description: Authenticates a registered user using different
 * authentication methods and maintains the user session.
 */

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.Scanner;

//================== User Class ==================
class User {

    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = PasswordUtil.hashPassword(password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

//================== Authentication Interface ==================
interface Authentication {

    Optional<User> login(String email, String password);
}

//================== Basic Authentication ==================
class BasicAuth implements Authentication {

    private User user;

    public BasicAuth(User user) {
        this.user = user;
    }

    @Override
    public Optional<User> login(String email, String password) {

        String hashedPassword = PasswordUtil.hashPassword(password);

        if (user.getEmail().equals(email)
                && user.getPassword().equals(hashedPassword)) {

            return Optional.of(user);
        }

        return Optional.empty();
    }
}

//================== OAuth Authentication ==================
class OAuth implements Authentication {

    @Override
    public Optional<User> login(String email, String password) {

        System.out.println("OAuth Authentication Successful.");

        User user = new User(email, password);

        return Optional.of(user);
    }
}

//================== Strategy Pattern ==================
class AuthenticationContext {

    private Authentication authentication;

    public AuthenticationContext(Authentication authentication) {
        this.authentication = authentication;
    }

    public Optional<User> authenticate(String email, String password) {
        return authentication.login(email, password);
    }
}

//================== Singleton Session Manager ==================
class SessionManager {

    private static SessionManager sessionManager;
    private User currentUser;

    private SessionManager() {
    }

    public static SessionManager getInstance() {

        if (sessionManager == null) {
            sessionManager = new SessionManager();
        }

        return sessionManager;
    }

    public void createSession(User user) {
        currentUser = user;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void logout() {
        currentUser = null;
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

        // Registered user (Dummy Data)
        User registeredUser = new User("nandini@gmail.com", "admin123");

        try {

            System.out.println("===== User Authentication =====");

            System.out.print("Enter Email : ");
            String email = sc.nextLine();

            System.out.print("Enter Password : ");
            String password = sc.nextLine();

            System.out.println("\nSelect Authentication Method");
            System.out.println("1. Basic Authentication");
            System.out.println("2. OAuth Authentication");
            System.out.print("Enter Choice : ");

            int choice = sc.nextInt();

            Authentication authentication;

            if (choice == 2) {
                authentication = new OAuth();
            } else {
                authentication = new BasicAuth(registeredUser);
            }

            AuthenticationContext context = new AuthenticationContext(authentication);

            Optional<User> user = context.authenticate(email, password);

            if (user.isPresent()) {

                SessionManager session = SessionManager.getInstance();

                session.createSession(user.get());

                System.out.println("\nLogin Successful.");
                System.out.println("Logged in User : " + session.getCurrentUser().getEmail());

            } else {

                System.out.println("\nInvalid Email or Password.");
            }

        } catch (Exception e) {

            System.out.println("Error : " + e.getMessage());
        }

        sc.close();
    }
}