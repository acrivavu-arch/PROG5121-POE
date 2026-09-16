/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Andzani
 */
import java.util.Scanner;

public class Main {
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Login login = new Login();
        
        System.out.println("==============Registration==============");
        
        //username do while loop that ask the user until they enter a valid username
        String username;
        do {
            System.out.print("Enter a username (must contain an underscore and is no more than five characters long.):");
            username = scanner.nextLine();
            if (!login.checkUserName(username)){
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            } else{
                System.out.println("Username successfully captured.");
            }
        } while (!login.checkUserName(username));
        
        //password do while loop that ask the user until they enter a valid password
        String password;
        do {
            System.out.print("Enter a password(must have a min of 8 caharacteres, a capital letter. anumber and a special character(eg.!@#$%^&*):");
            password = scanner.nextLine();
            if (!login.checkPasswordComplexity(password)){
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            } else {
                System.out.println("Password successfully captured.");
            }
        } while (!login.checkPasswordComplexity(password));
        
        //cell number do while loop that ask the user until they enter a valid South African cell number
        String cellNumber;
        do {
            System.out.print("Enter your South African cell phone number (e.g. +27634540081) ");
            cellNumber = scanner.nextLine();
            if (!login.checkCellNumber(cellNumber)){
                System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            } else {
                System.out.println("Cell phone number successfully added.");
            }
        } while (!login.checkCellNumber(cellNumber));
        
        //capture names and store them
        System.out.print("Please enter your first name: ");
        String firstName =  scanner.nextLine();
        
        System.out.print("Please enter your last name: ");
        String lastName =  scanner.nextLine();
        
        //registration attempt
        String registrationMessage = login.registerUser(username, password, cellNumber, firstName, lastName);
        System.out.println(registrationMessage);
        
        //Safety check ,just to protect against unexpected issues
        if (!login.isRegistered()){
            System.out.print("Registration failed. Exiting program.");
            scanner.close();
            return;
        }
        
        //Login section
        System.out.println("\n======Login======");
        System.out.print("Enter your username: ");
        String loginUsername = scanner.nextLine();
        System.out.print("Enter your password: ");
        String loginPassword = scanner.nextLine();
        
        //check if the entered credentials against what was registered
        boolean loginSuccess = login.loginUser(loginUsername, loginPassword);
        //display the login welcome or error message
        System.out.println(login.returnLoginStatus(loginSuccess));
        scanner.close();
    }
}
