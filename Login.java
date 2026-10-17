/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Andzani
 */
import java.util.regex.Pattern;

//handles registrations
public class Login {

    //declarations
    private String username;
    private String password;
    private String cellNumber;
    private String firstName;
    private String lastName;
    private boolean registered;//tracks whether the user is registered or not

    //constuctor
    public Login() {
        this.registered = false;
    }

    //checks if the username is valid and meet the requirements
    public boolean checkUserName(String username) {
        if (username == null) {
            return false;
        }
        return username.contains("_") && username.length() <= 5;
    }

    //checks if the password is valid and meet the requirements
    public boolean checkPasswordComplexity(String password) {
        if (password == null) {
            return false;
        }
        String regex = "^(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()\\-_=+\\[\\]{};:'\",.<>/?]).{8,}$";
        return password.matches(regex);

    }

    /**
 * Recipient must start with an international code (+ and country code)
 * followed by no more than ten digits.
 * Adapted from the cell number check in Login.java (Part 1). Change from
 * Part 1: accepts any country code, not only +27.
 * Reference: GeeksforGeeks, "Write Regular Expressions," GeeksforGeeks.
 * [Online]. Available: https://www.geeksforgeeks.org/write-regular-expressions/
 * [Accessed: Sept. 07, 2026].
 */
    public boolean checkCellNumber(String cellNumber) {
        if (cellNumber == null) {
            return false;
        }
        String regex = "^\\+27[0-9]{1,10}$";
        return Pattern.matches(regex, cellNumber);
    }

    /**
     * Attempts to register a new user.
     * Validates the username, password, and cell number first; only
     * stores the user's details and marks them as registered if all
     * three pass.
     *
     * @param username
     * @param password
     * @param cellNumber
     * @param firstName
     * @param lastName
     * @return a message describing the outcome (success or which
     *         validation failed)
     */
    public String registerUser(String username, String password, String cellNumber, String firstName, String lastName) {
        if (!checkUserName(username)) {
            return "Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.";
        }

        if (!checkPasswordComplexity(password)) {
            return "Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.";
        }

        if (!checkCellNumber(cellNumber)) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }

        this.username = username;
        this.cellNumber = cellNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.registered = true;//account marked as registered

        return "Username Registration successful";
    }

    public boolean loginUser(String username, String password) {
        if (!registered) {
            return false;
        }
        return this.username.equals(username) && this.password.equals(password);
    }

    public String returnLoginStatus(boolean loginSuccess) {
        if (loginSuccess) {
            return "";
        }
        return "Username or password incorrect, please try again.";
    }

    //methods that let other classes read the stored values without directly accessing private fields.
    public String getUsername() {
        return username;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isRegistered() {
        return registered;
    }
}
