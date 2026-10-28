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

        // blank email
        assertFalse(Email.isValidEmail("")); // empty string
        assertFalse(Email.isValidEmail(" ")); // spaces only

        // missing parts
        assertFalse(Email.isValidEmail("@example")); // missing local part
        assertFalse(Email.isValidEmail("peterjackexample")); // missing '@' symbol
        assertFalse(Email.isValidEmail("peterjack@")); // missing domain name

        // invalid parts
        assertFalse(Email.isValidEmail("peter jack@example")); // spaces in local part
        assertFalse(Email.isValidEmail("peterjack@exam ple")); // spaces in domain name
        assertFalse(Email.isValidEmail(" peterjack@example")); // leading space
        assertFalse(Email.isValidEmail("peterjack@example ")); // trailing space
        assertFalse(Email.isValidEmail("peterjack@@example")); // double '@' symbol
        assertFalse(Email.isValidEmail("peter@jack@example")); // multiple '@' symbols
        assertFalse(Email.isValidEmail("peter/jack@example")); // forward slash in local part
        assertFalse(Email.isValidEmail("peterjack@example/1")); // forward slash in domain name
        assertFalse(Email.isValidEmail("peter/jack@example/1")); // forward slash in both

        // valid email
        assertTrue(Email.isValidEmail("a@bc")); // minimal
        assertTrue(Email.isValidEmail("test@localhost")); // alphabets only
        assertTrue(Email.isValidEmail("123@145")); // numeric local part and domain name
        assertTrue(Email.isValidEmail("PeterJack1190@example")); // alphanumeric characters
        assertTrue(Email.isValidEmail("a@b")); // single character words
        assertTrue(Email.isValidEmail("peterjack@EXAMPLE")); // uppercase domain
        assertTrue(Email.isValidEmail("PETERJACK@example")); // uppercase local part
        assertTrue(Email.isValidEmail("peterjack@example.com")); // period in domain name
        assertTrue(Email.isValidEmail("peter.jack@example")); // period in local part
        assertTrue(Email.isValidEmail("peter_jack@example")); // underscore in local part
        assertTrue(Email.isValidEmail("peterjack@exam_ple")); // underscore in domain name
        assertTrue(Email.isValidEmail("peter+jack@example")); // '+' in local part
        assertTrue(Email.isValidEmail("peter-jack@example")); // hyphen in local part
        assertTrue(Email.isValidEmail("peterjack@exam-ple")); // hyphen in domain name
        assertTrue(Email.isValidEmail("!peter@example")); // special character in local part
    }

    @Test
    public void equals() {
        Email email = new Email("valid@email");

        // same values -> returns true
        assertTrue(email.equals(new Email("valid@email")));

        // same object -> returns true
        assertTrue(email.equals(email));

        // null -> returns false
        assertFalse(email.equals(null));

        // different types -> returns false
        assertFalse(email.equals(5.0f));

        // different values -> returns false
        assertFalse(email.equals(new Email("other@email")));
    }
}
