package seedu.address.model.contact;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class EmailTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Email(null));
    }

    @Test
    public void constructor_invalidEmail_throwsIllegalArgumentException() {
        String invalidEmail = "";
        assertThrows(IllegalArgumentException.class, () -> new Email(invalidEmail));
    }

    @Test
    public void isValidEmail() {
        // null email
        assertThrows(NullPointerException.class, () -> Email.isValidEmail(null));

        // invalid email
        assertFalse(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only
        assertFalse(Email.isValidEmail(" peterjack@example.com")); // leading space
        assertFalse(Email.isValidEmail("/")); // forward slash only
        assertFalse(Email.isValidEmail("/example.com")); // starts with forward slash

        // valid email
        assertTrue(Email.isValidEmail("@example.com")); // missing local part
        assertTrue(Email.isValidEmail("peterjackexample.com")); // missing '@' symbol
        assertTrue(Email.isValidEmail("peterjack@")); // missing domain name
        assertTrue(Email.isValidEmail("peter jack@example.com")); // spaces in local part
        assertTrue(Email.isValidEmail("peterjack@exam ple.com")); // spaces in domain name
        assertTrue(Email.isValidEmail("peterjack@example.com ")); // trailing space
        assertTrue(Email.isValidEmail("peterjack@@example.com")); // double '@' symbol
        assertTrue(Email.isValidEmail("peter@jack@example.com")); // multiple '@' symbols
        assertTrue(Email.isValidEmail("peter/jack@example.com")); // forward slash in local part
        assertTrue(Email.isValidEmail("peterjack@example.com/1")); // forward slash in domain name
        assertTrue(Email.isValidEmail("peter/jack@example.com/1")); // forward slash in both
        assertTrue(Email.isValidEmail("a@bc")); // minimal
        assertTrue(Email.isValidEmail("test@localhost")); // alphabets only
        assertTrue(Email.isValidEmail("123@145")); // numeric local part and domain name
        assertTrue(Email.isValidEmail("PeterJack1190@example.com")); // alphanumeric characters
        assertTrue(Email.isValidEmail("a@b")); // single character words
        assertTrue(Email.isValidEmail("peterjack@EXAMPLE.COM")); // uppercase domain
        assertTrue(Email.isValidEmail("PETERJACK@example.com")); // uppercase local part
        assertTrue(Email.isValidEmail("peterjack@example.com")); // period in domain name
        assertTrue(Email.isValidEmail("peter.jack@example.com")); // period in local part
        assertTrue(Email.isValidEmail("peter_jack@example.com")); // underscore in local part
        assertTrue(Email.isValidEmail("peterjack@exam_ple.com")); // underscore in domain name
        assertTrue(Email.isValidEmail("peter+jack@example.com")); // '+' in local part
        assertTrue(Email.isValidEmail("peter-jack@example.com")); // hyphen in local part
        assertTrue(Email.isValidEmail("peterjack@exam-ple.com")); // hyphen in domain name
        assertTrue(Email.isValidEmail("!peter@example.com")); // special character in local part
    }

    @Test
    public void equals() {
        Email email = new Email("valid@email.com");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@email.com")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other@email.com")));
    }
}
