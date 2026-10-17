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
            System.out.print("Enter a username (must contain an underscore and is no more than five characters long):");
            username = scanner.nextLine();
            if (!login.checkUserName(username)) {
                System.out.println("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.");
            } else {
                System.out.println("Username successfully captured.");
            }
        } while (!login.checkUserName(username));

        //password do while loop that ask the user until they enter a valid password
        String password;
        do {
            System.out.print("Enter a password(must have a min of 8 chararacteres, a capital letter, number and a special character(eg.!@#$%^&*):");
            password = scanner.nextLine();
            if (!login.checkPasswordComplexity(password)) {
                System.out.println("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.");
            } else {
                System.out.println("Password successfully captured.");
            }
        } while (!login.checkPasswordComplexity(password));

        //cell number do while loop that ask the user until they enter a valid South African cell number
        String cellNumber;
        do {
            System.out.print("Enter your South African cell phone number (e.g. +27634540081): ");
            cellNumber = scanner.nextLine();
            if (!login.checkCellNumber(cellNumber)) {
                System.out.println("Cell phone number incorrectly formatted or does not contain international code.");
            } else {
                System.out.println("Cell phone number successfully added.");
            }
        } while (!login.checkCellNumber(cellNumber));

        //capture names and store them
        System.out.print("Please enter your first name: ");
        String firstName = scanner.nextLine();

        System.out.print("Please enter your last name: ");
        String lastName = scanner.nextLine();

        //registration attempt
        String registrationMessage = login.registerUser(username, password, cellNumber, firstName, lastName);
        System.out.println(registrationMessage);

        //Safety check ,just to protect against unexpected issues
        if (!login.isRegistered()) {
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
        // only show a message if the login fails; on success the app goes straight to "Welcome to QuickChat."
        if (!loginSuccess) {
    System.out.println(login.returnLoginStatus(loginSuccess));
    System.out.println("Cannot proceed without a successful login. Exiting.");
            scanner.close();
            return;
        }

        // ===== PART 2: SENDING MESSAGES =====
        System.out.println("\nWelcome to QuickChat.");

        // the user sets how many messages they want to enter when the application starts
        int numMessages = askNumberOfMessages(scanner);
        int messagesEntered = 0;

        int menuChoice = -1;

        // menu loop - runs until the user selects Quit (3)
        while (menuChoice != 3) {
            System.out.println("\n1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            System.out.print("Choose an option: ");

            String input = scanner.nextLine();
            try {
                menuChoice = Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (1-3).");
                menuChoice = -1;
                continue;
            }

            switch (menuChoice) {
                case 1:
                    if (messagesEntered >= numMessages) {
                        System.out.println("You have already entered all " + numMessages + " messages.");
                    } else {
                        messagesEntered = sendMessagesFlow(scanner, numMessages, messagesEntered);
                    }   break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option, please choose 1, 2, or 3.");
                    break;
            }
        }

        scanner.close();
    }

    /** Keeps asking until the user enters a whole number of 1 or more. */
    private static int askNumberOfMessages(Scanner scanner) {
        int numMessages = 0;
        while (numMessages < 1) {
            System.out.print("How many messages would you like to enter? ");
            try {
                numMessages = Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                numMessages = 0;
            }
            if (numMessages < 1) {
                System.out.println("Please enter a whole number of 1 or more.");
            }
        }
        return numMessages;
    }

    /**
     * Enters the remaining messages (FOR loop, the counter is the message
     * number). Returns how many messages have now been entered.
     */
    private static int sendMessagesFlow(Scanner scanner, int numMessages, int startAt) {
        for (int i = startAt; i < numMessages; i++) {
            System.out.println("\n--- Message " + (i + 1) + " of " + numMessages + " ---");

            String recipient;
            String recipientResult;
            do {
                System.out.print("Enter recipient number (e.g. +27831234567): ");
                recipient = scanner.nextLine().trim();
                recipientResult = new Message(recipient, "placeholder", i).checkRecipientCell(recipient);
                System.out.println(recipientResult);
            } while (!recipientResult.equals("Cell phone number successfully captured."));

            String messageText;
            String lengthResult;
            do {
                System.out.print("Enter your message (max 250 characters): ");
                messageText = scanner.nextLine();
                if (messageText.trim().isEmpty()) {
                    System.out.println("The message cannot be empty.");
                    lengthResult = "";
                    continue;
                }
                lengthResult = new Message(recipient, "placeholder", i).checkMessageLength(messageText);
                System.out.println(lengthResult);
            } while (!lengthResult.equals("Message ready to send."));

            Message message = new Message(recipient, messageText, i);
            System.out.println(message.messageIDGenerated());

            String choice = "";
            while (choice.equals("")) {
                System.out.println("Choose an option:");
                System.out.println("1) Send Message");
                System.out.println("2) Disregard Message");
                System.out.println("3) Store Message to send later");
                System.out.print("Your choice: ");
                String choiceInput = scanner.nextLine().trim();
                if (choiceInput.equals("1")) {
                    choice = "send";
                } else if (choiceInput.equals("2")) {
                    choice = "disregard";
                } else if (choiceInput.equals("3")) {
                    choice = "store";
                } else {
                    System.out.println("Invalid option, please choose 1, 2 or 3.");
                }
            }

            System.out.println(message.sentMessage(choice));

            if (choice.equals("send")) {
                System.out.println("\n" + message.getFullDetails());
            } else if (choice.equals("disregard")) {
                String zero = scanner.nextLine().trim();
                if (zero.equals("0")) {
                    System.out.println("Message deleted.");
                } else {
                    System.out.println("Message was not sent.");
                }
            }
        }

        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());
        return numMessages;
    }
}
