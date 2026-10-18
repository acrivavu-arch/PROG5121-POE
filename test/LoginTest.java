/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Andzani
 */
import org.junit.Test;
import static org.junit.Assert.*;

public class LoginTest {
    //Tests username
    @Test
    public void testUsername_CorrectlyFormatted() {
        Login login = new Login();
        //"Kyl_1" has an underscore and 5 characters, and it should return true
        assertEquals(true, login.checkUserName("Kyl_1"));
    }

    @Test
    public void testUsername_IncorrectlyFormatted() {
        Login login = new Login();
        //"Kyle!!!!!!" has no underscore and is more than 5 characters which is too long, and it should return false
        assertEquals(false, login.checkUserName("Kyle!!!!!!"));
    }

    //Password tests
    @Test
    public void testPassword_MeetsComplexity() {
        Login login = new Login();
        // Has more than 8 chars, a capital letter, digits, and special characters, it should return true
        assertEquals(true, login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    public void testPassword_DoesNotMeetComplexity() {
        Login login = new Login();
        // All lowercase, no digit, no special character, it should return false
        assertEquals(false, login.checkPasswordComplexity("password"));
    }

    //cellphone tests
    @Test
    public void testCellPhone_CorrectlyFormatted() {
        Login login = new Login();
        // Starts with +27 and has digits within the allowed length -> true
        assertEquals(true, login.checkCellNumber("+27838968976"));
    }

    @Test
    public void testCellPhone_IncorrectlyFormatted() {
        Login login = new Login();
        // Missing the +27 international code -> false
        assertEquals(false, login.checkCellNumber("08966553"));
    }

    //registration tests
    @Test
    public void testRegisterUser_Success() {
        Login login = new Login();
        // Valid username, password, and cell number should register successfully
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Peters");
        assertTrue(login.isRegistered());
        assertTrue(result.contains("Registration successful"));
    }

    @Test
    public void testRegisterUser_BadUsername() {
        Login login = new Login();
        // Invalid username should block registration and return the exact error message
        String result = login.registerUser("kyle!!!!!!", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Peters");
        assertFalse(login.isRegistered());
        assertEquals("Username is not correctly formatted; please ensure that your username contains an underscore and is no more than five characters in length.", result);
    }

    @Test
    public void testRegisterUser_BadPassword() {
        Login login = new Login();
        // Weak password should block registration and return the exact error message
        String result = login.registerUser("kyl_1", "weakpass", "+27838968976", "Kyle", "Peters");
        assertFalse(login.isRegistered());
        assertEquals("Password is not correctly formatted; please ensure that the password contains at least eight characters, a capital letter, a number, and a special character.", result);
    }

    @Test
    public void testRegisterUser_BadCellNumber() {
        Login login = new Login();
        // Invalid cell number should block registration and return the exact error message
        String result = login.registerUser("kyl_1", "Ch&&sec@ke99!", "08966553", "Kyle", "Peters");
        assertFalse(login.isRegistered());
        assertEquals("Cell phone number incorrectly formatted or does not contain international code.", result);
    }

    //Login tests
    @Test
    public void testLoginUser_Successful() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Peters");
        // Logging in with the exact same details used at registration should succeed
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    public void testLoginUser_Failed() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Peters");
        // Wrong password should fail the login check
        assertFalse(login.loginUser("kyl_1", "WrongPassword1!"));
    }

    //Login status message tests
    @Test
    public void testReturnLoginStatus_Successful() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Peters");
        boolean result = login.loginUser("kyl_1", "Ch&&sec@ke99!");
    assertEquals("", login.returnLoginStatus(result));
    }

    @Test
    public void testReturnLoginStatus_Failed() {
        Login login = new Login();
        login.registerUser("kyl_1", "Ch&&sec@ke99!", "+27838968976", "Kyle", "Peters");
        boolean result = login.loginUser("kyl_1", "WrongPassword1!");
        assertEquals("Username or password incorrect, please try again.", login.returnLoginStatus(result));
    }
}