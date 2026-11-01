package seedu.address.model.contact;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

public class PhoneTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Phone(null));
    }

    @Test
    public void constructor_invalidPhone_throwsIllegalArgumentException() {
        String invalidPhone = "";
        assertThrows(IllegalArgumentException.class, () -> new Phone(invalidPhone));
    }

    @Test
    public void isValidPhone() {
        // null phone number
        assertThrows(NullPointerException.class, () -> Phone.isValidPhone(null));

        // invalid phone numbers
        assertFalse(Phone.isValidPhone("")); // empty string
        assertFalse(Phone.isValidPhone(" ")); // spaces only
        assertFalse(Phone.isValidPhone("/")); // forward slash only
        assertFalse(Phone.isValidPhone("/911")); // starts with forward slash
        assertFalse(Phone.isValidPhone(" 911")); // leading space

        // valid phone numbers
        assertTrue(Phone.isValidPhone("91")); // less than 3 numbers
        assertTrue(Phone.isValidPhone("phone")); // non-numeric
        assertTrue(Phone.isValidPhone("9011p041")); // alphabets within digits
        assertTrue(Phone.isValidPhone("9312 1534")); // spaces within digits
        assertTrue(Phone.isValidPhone("1234567890123456")); // 16 numbers (more than 15 digits)
        assertTrue(Phone.isValidPhone("123456789012345678901234567890")); // very long numbers
        assertTrue(Phone.isValidPhone("911")); // exactly 3 numbers (lower bound)
        assertTrue(Phone.isValidPhone("93121534"));
        assertTrue(Phone.isValidPhone("124293842033123")); // exactly 15 numbers (upper bound)
        assertTrue(Phone.isValidPhone("911/")); // ends with forward slash
        assertTrue(Phone.isValidPhone("91/1")); // contains forward slash
    }

    @Test
    public void equals() {
        Phone phone = new Phone("999");

        // same values -> returns true
        assertTrue(phone.equals(new Phone("999")));

        // same object -> returns true
        assertTrue(phone.equals(phone));

        // null -> returns false
        assertFalse(phone.equals(null));

        // different types -> returns false
        assertFalse(phone.equals(5.0f));

        // different values -> returns false
        assertFalse(phone.equals(new Phone("995")));
    }
}
