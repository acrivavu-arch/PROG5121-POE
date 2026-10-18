/**
 * @author Andzani
 */
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.Assert.*;

/**
 * PROG5121 POE - Part 2: unit tests for the Message class,
 * using the test data from the assignment brief.
 */
public class MessageTest {

    private static final String MSG1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String NUM1 = "+27718693002";
    private static final String MSG2 = "Hi Keegan, did you receive the payment?";
    private static final String NUM2 = "08575975889";

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        // clear the totals/arrays and write JSON to a temp file so tests don't affect each other
        Message.resetSessionData();
        Message.setJsonFilePath(tempFolder.newFile("messages.json").getAbsolutePath());
    }

    // ---- message length ----
    @Test
    public void testMessageLength_Success() {
        assertEquals("Message ready to send.", Message.checkMessageLength(MSG1));
    }

    @Test
    public void testMessageLength_Failure() {
        String tooLong = "A".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(tooLong));
    }

    @Test
    public void testMessageLength_ExactlyLimit() {
        assertEquals("Message ready to send.", Message.checkMessageLength("A".repeat(250)));
    }

    // ---- recipient number ----
    @Test
    public void testRecipient_Success() {
        assertEquals("Cell phone number successfully captured.", Message.checkRecipientCell(NUM1));
    }

    @Test
    public void testRecipient_Failure() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain "
                + "an international code. Please correct the number and try again.",
                Message.checkRecipientCell(NUM2));
    }

    // ---- message hash ----
    @Test
    public void testMessageHash_TestCase1() {
        assertEquals("00:0:HITONIGHT", Message.createMessageHash("0012345678", 0, MSG1));
    }

    @Test
    public void testMessageHash_AllMessagesInLoop() {
        // test the remaining hashes in a loop using parallel arrays of test data
        String[] ids = {"0012345678", "0098765432", "1234567890"};
        int[] numbers = {0, 1, 2};
        String[] texts = {MSG1, MSG2, "Thanks"};
        String[] expected = {"00:0:HITONIGHT", "00:1:HIPAYMENT", "12:2:THANKSTHANKS"};

        for (int i = 0; i < texts.length; i++) {
            assertEquals(expected[i], Message.createMessageHash(ids[i], numbers[i], texts[i]));
        }
    }

    // ---- message ID (checked for several messages in a loop) ----
    @Test
    public void testMessageID_IsCreated() {
        Message m = new Message(NUM1, MSG1, 0);
        assertEquals("Message ID generated: " + m.getMessageID(), m.messageIDGenerated());
    }

    @Test
    public void testMessageID_TenDigitsForEveryMessage() {
        for (int i = 0; i < 50; i++) {
            Message m = new Message(NUM1, MSG1, i);
            assertEquals(10, m.getMessageID().length());
            assertTrue(m.getMessageID().matches("[0-9]{10}"));
            assertTrue(Message.checkMessageID(m.getMessageID()));
            // hash must start with the first two digits of this ID and this message number
            assertTrue(m.getMessageHash().startsWith(m.getMessageID().substring(0, 2) + ":" + i + ":"));
        }
    }

    @Test
    public void testMessageID_TooLong() {
        assertFalse(Message.checkMessageID("12345678901"));
    }

    // ---- message details ----
    @Test
    public void testFullDetails_InCorrectOrder() {
        Message m = new Message(NUM1, MSG1, 0);
        String expected = "Message ID: " + m.getMessageID()
                + "\nMessage Hash: " + m.getMessageHash()
                + "\nRecipient: " + NUM1
                + "\nMessage: " + MSG1;
        assertEquals(expected, m.getFullDetails());
    }

    // ---- sentMessage options ----
    @Test
    public void testSentMessage_Send() {
        Message m = new Message(NUM1, MSG1, 0);
        assertEquals("Message successfully sent.", m.sentMessage("send"));
    }

    @Test
    public void testSentMessage_Disregard() {
        Message m = new Message(NUM2, MSG2, 1);
        assertEquals("Press 0 to delete the message.", m.sentMessage("disregard"));
    }

    @Test
    public void testSentMessage_Store() {
        Message m = new Message(NUM1, MSG1, 0);
        assertEquals("Message successfully stored.", m.sentMessage("store"));
    }

    // ---- totals ----
    @Test
    public void testTotalMessages_SendOneDiscardOne() {
        new Message(NUM1, MSG1, 0).sentMessage("send");
        new Message(NUM2, MSG2, 1).sentMessage("disregard");
        assertEquals(1, Message.returnTotalMessages());
    }

    @Test
    public void testPrintMessages_OnlyContainsSent() {
        new Message(NUM1, MSG1, 0).sentMessage("send");
        new Message(NUM2, MSG2, 1).sentMessage("disregard");
        String printed = Message.printMessages();
        assertTrue(printed.contains(MSG1));
        assertFalse(printed.contains(MSG2));
    }

    // ---- JSON storage ----
    @Test
    public void testStoreMessage_WritesJsonFile() throws Exception {
        Message m = new Message(NUM1, "He said \"hi\"", 0);
        m.sentMessage("store");
        String json = Files.readString(Path.of(tempFolder.getRoot().getAbsolutePath(), "messages.json"));
        assertTrue(json.contains(m.getMessageHash()));
        assertTrue(json.contains("He said \\\"hi\\\""));
    }

    @Test
    public void testStoreMessage_TwoMessagesStoredAsArray() throws Exception {
        new Message(NUM1, MSG1, 0).sentMessage("store");
        new Message(NUM2, MSG2, 1).sentMessage("store");
        String json = Files.readString(Path.of(tempFolder.getRoot().getAbsolutePath(), "messages.json"));
        assertTrue(json.startsWith("["));
        assertTrue(json.contains(MSG1) && json.contains(MSG2));
    }
}
