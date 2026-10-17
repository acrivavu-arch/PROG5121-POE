/**
 * @author Andzani
 */
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

/**
 * Message.java
 * PROG5121 POE - Part 2: Sending Messages.
 *
 * Represents one message: validates its recipient and length, generates its
 * ID and hash, and keeps session-wide arrays of sent and stored messages.
 */
public class Message {

    // ===== session-wide data (shared by all Message objects) =====
    private static int totalMessagesSent = 0;
    private static Message[] sentMessages = new Message[0];   // array of every message sent
    private static Message[] storedMessages = new Message[0]; // array of every message stored for later
    private static String jsonFilePath = "messages.json";

    // messages returned by the check methods
    public static final String RECIPIENT_OK = "Cell phone number successfully captured.";
    public static final String LENGTH_OK = "Message ready to send.";

    // ===== data for this one message =====
    private final String messageID;
    private final String recipient;
    private final String messageText;
    private final String messageHash;
    private final int messageNumber;

    public Message(String recipient, String messageText, int messageNumber) {
        this.messageID = generateMessageID();
        this.recipient = recipient;
        this.messageText = messageText;
        this.messageNumber = messageNumber;
        this.messageHash = createMessageHash(this.messageID, messageNumber, messageText);
    }

    /**
     * Builds a random 10-digit ID: a random positive number is padded to 19
     * digits and substring() keeps the first 10 (so leading zeros are allowed).
     */
    private String generateMessageID() {
        long randomNumber = new Random().nextLong() & Long.MAX_VALUE;
        String nineteenDigits = String.format("%019d", randomNumber);
        return nineteenDigits.substring(0, 10);
    }

    /** Text shown to the user when the ID is created. */
    public String messageIDGenerated() {
        return "Message ID generated: " + messageID;
    }

    /** The ID must be no more than ten characters. */
    public static boolean checkMessageID(String id) {
        return id != null && id.length() <= 10;
    }

    /**
    * Adapted from the cell number check in Login.java (Part 1).
    * Reference: GeeksforGeeks, "https://www.geeksforgeeks.org/write-regular-expressions/"
    * GeeksforGeeks. [Online]. Available:
    * https://www.geeksforgeeks.org/write-regular-expressions/
    * [Accessed: Oct. 17, 2026].
    * Recipient must start with an international code (+ and country code)
    * followed by no more than ten digits.
    * Change from Part 1: accepts any country code, not only +27.
    */
    
    public static String checkRecipientCell(String cell) {
        if (cell != null && cell.matches("^\\+[0-9]{1,3}[0-9]{1,10}$")) {
            return RECIPIENT_OK;
        }
        return "Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.";
    }

    /** Message may not exceed 250 characters. */
    public static String checkMessageLength(String message) {
        if (message != null && message.length() <= 250) {
            return LENGTH_OK;
        }
        int excess = (message == null) ? 0 : message.length() - 250;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    /**
     * Hash = first two digits of the ID : message number : first word + last word,
     * all in capitals. The message is split into an array of words so the first
     * and last word are simply words[0] and words[words.length - 1].
     */
    public static String createMessageHash(String id, int msgNumber, String text) {
        String idPart = id.substring(0, 2);
        String[] words = text.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^a-zA-Z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^a-zA-Z0-9]", "");
        return (idPart + ":" + msgNumber + ":" + firstWord + lastWord).toUpperCase();
    }

    /**
     * Acts on the user's choice: "send", "disregard" or "store".
     * Sending adds the message to the sent array and increases the total;
     * storing writes it to the JSON file.
     */
    public String sentMessage(String choice) {
        if (choice == null) {
            return "Invalid option.";
        }
        if (choice.equalsIgnoreCase("send")) {
            totalMessagesSent++;
            sentMessages = Arrays.copyOf(sentMessages, sentMessages.length + 1);
            sentMessages[sentMessages.length - 1] = this;
            return "Message successfully sent.";
        } else if (choice.equalsIgnoreCase("disregard")) {
            return "Press 0 to delete the message.";
        } else if (choice.equalsIgnoreCase("store")) {
            return storeMessage();
        }
        return "Invalid option.";
    }

    /** Returns the details of every message sent while the program is running. */
    public static String printMessages() {
        StringBuilder output = new StringBuilder();
        for (Message sent : sentMessages) {
            output.append(sent.getFullDetails()).append("\n---\n");
        }
        return output.toString();
    }

    public static int returnTotalMessages() {
        return totalMessagesSent;
    }

    /** Full details in the order required: ID, hash, recipient, message. */
    public String getFullDetails() {
        return "Message ID: " + messageID
                + "\nMessage Hash: " + messageHash
                + "\nRecipient: " + recipient
                + "\nMessage: " + messageText;
    }

    // The JSON layout (an array of objects with key/value pairs) follows the
    // JSON syntax defined in [1]. Writing text to a file with FileWriter and
    // try-with-resources is based on [2].
    //
    // [1] ECMA International, "The JSON data interchange syntax," ECMA-404,
    //     2nd ed., Dec. 2017. [Online]. Available:
    //     https://ecma-international.org/publications-and-standards/standards/ecma-404/
    //     [Accessed: Oct. 17, 2026].
    // [2] Oracle, "FileWriter (Java SE 17 & JDK 17)," Java Platform API
    //     Specification. [Online]. Available:
    //     https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/io/FileWriter.html
    //     [Accessed: Oct. 17, 2026].
    public String storeMessage() {
        storedMessages = Arrays.copyOf(storedMessages, storedMessages.length + 1);
        storedMessages[storedMessages.length - 1] = this;

        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.length; i++) {
            Message m = storedMessages[i];
            json.append("  {\n")
                .append("    \"messageID\": \"").append(escapeJson(m.messageID)).append("\",\n")
                .append("    \"messageHash\": \"").append(escapeJson(m.messageHash)).append("\",\n")
                .append("    \"recipient\": \"").append(escapeJson(m.recipient)).append("\",\n")
                .append("    \"message\": \"").append(escapeJson(m.messageText)).append("\"\n")
                .append("  }");
            if (i < storedMessages.length - 1) {
                json.append(",");
            }
            json.append("\n");
        }
        json.append("]\n");

        try (FileWriter writer = new FileWriter(jsonFilePath)) {
            writer.write(json.toString());
            return "Message successfully stored.";
        } catch (IOException e) {
            return "Error storing message: " + e.getMessage();
        }
    }

    // keeps the JSON valid if a message contains quotes, backslashes or new lines
    private static String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"")
                .replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t");
    }

    // ----- helpers used by the unit tests -----
    public static void resetSessionData() {
        totalMessagesSent = 0;
        sentMessages = new Message[0];
        storedMessages = new Message[0];
    }

    public static void setJsonFilePath(String path) {
        jsonFilePath = path;
    }

    // ----- getters -----
    public String getMessageID() { return messageID; }
    public String getRecipient() { return recipient; }
    public String getMessageText() { return messageText; }
    public String getMessageHash() { return messageHash; }
    public int getMessageNumber() { return messageNumber; }
}
