package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.ADDRESS_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.EMAIL_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_ADDRESS_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_EMAIL_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NAME_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_PHONE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.PHONE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_NON_EMPTY;
import static seedu.address.logic.commands.CommandTestUtil.PREAMBLE_WHITESPACE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HUSBAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMAIL_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalContacts.AMY;
import static seedu.address.testutil.TypicalContacts.BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.AddContactCommand;
import seedu.address.model.contact.Address;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.ContactBuilder;

public class AddContactCommandParserTest {
    private AddContactCommandParser parser = new AddContactCommandParser();

    @Test
    public void parse_allFieldsPresent_success() {
        Contact expectedContact = new ContactBuilder(BOB).withTags(VALID_TAG_FRIEND).build();

        // whitespace only preamble
        assertParseSuccess(parser, PREAMBLE_WHITESPACE + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND, new AddContactCommand(expectedContact));


        // multiple tags - all accepted
        Contact expectedContactMultipleTags = new ContactBuilder(BOB).withTags(VALID_TAG_FRIEND, VALID_TAG_HUSBAND)
                .build();
        assertParseSuccess(parser,
                NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                new AddContactCommand(expectedContactMultipleTags));
    }

    @Test
    public void parse_repeatedNonTagValue_failure() {
        String validExpectedContactString = NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_FRIEND;

        // multiple names
        assertParseFailure(parser, NAME_DESC_AMY + validExpectedContactString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // multiple phones
        assertParseFailure(parser, PHONE_DESC_AMY + validExpectedContactString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // multiple emails
        assertParseFailure(parser, EMAIL_DESC_AMY + validExpectedContactString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // multiple addresses
        assertParseFailure(parser, ADDRESS_DESC_AMY + validExpectedContactString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // multiple fields repeated
        assertParseFailure(parser,
                validExpectedContactString + PHONE_DESC_AMY + EMAIL_DESC_AMY + NAME_DESC_AMY + ADDRESS_DESC_AMY
                        + validExpectedContactString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME, PREFIX_ADDRESS, PREFIX_EMAIL, PREFIX_PHONE));

        // invalid value followed by valid value

        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + validExpectedContactString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, INVALID_EMAIL_DESC + validExpectedContactString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, INVALID_PHONE_DESC + validExpectedContactString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, INVALID_ADDRESS_DESC + validExpectedContactString,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));

        // valid value followed by invalid value

        // invalid name
        assertParseFailure(parser, validExpectedContactString + INVALID_NAME_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_NAME));

        // invalid email
        assertParseFailure(parser, validExpectedContactString + INVALID_EMAIL_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_EMAIL));

        // invalid phone
        assertParseFailure(parser, validExpectedContactString + INVALID_PHONE_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_PHONE));

        // invalid address
        assertParseFailure(parser, validExpectedContactString + INVALID_ADDRESS_DESC,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ADDRESS));
    }

    @Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Contact expectedContact = new ContactBuilder(AMY).withTags().build();
        assertParseSuccess(parser, NAME_DESC_AMY + PHONE_DESC_AMY + EMAIL_DESC_AMY + ADDRESS_DESC_AMY,
                new AddContactCommand(expectedContact));
    }

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddContactCommand.MESSAGE_USAGE);

        // missing name prefix
        assertParseFailure(parser, VALID_NAME_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing phone prefix
        assertParseFailure(parser, NAME_DESC_BOB + VALID_PHONE_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing email prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + VALID_EMAIL_BOB + ADDRESS_DESC_BOB,
                expectedMessage);

        // missing address prefix
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + VALID_ADDRESS_BOB,
                expectedMessage);

        // all prefixes missing
        assertParseFailure(parser, VALID_NAME_BOB + VALID_PHONE_BOB + VALID_EMAIL_BOB + VALID_ADDRESS_BOB,
                expectedMessage);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid name
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC + ADDRESS_DESC_BOB
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC
                + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Address.MESSAGE_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB + ADDRESS_DESC_BOB
                + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB + INVALID_ADDRESS_DESC,
                Name.MESSAGE_CONSTRAINTS);

        // non-empty preamble
        assertParseFailure(parser, PREAMBLE_NON_EMPTY + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddContactCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_allPairsCombinations_success() {
        // TC1: N1, P1, E1, A1, T1
        Contact c1 = new ContactBuilder().withName("John Doe").withPhone("98765432")
                .withEmail("john@example.com").withAddress("123 Clementi Rd, #01-02").withTags().build();
        assertParseSuccess(parser, " n/John Doe p/98765432 e/john@example.com a/123 Clementi Rd, #01-02",
                new AddContactCommand(c1));

        // TC2: N1, P2, E2, A2, T2
        Contact c2 = new ContactBuilder().withName("John Doe").withPhone("123")
                .withEmail("a@b.co").withAddress("X").withTags("colleague").build();
        assertParseSuccess(parser, " n/John Doe p/123 e/a@b.co a/X t/colleague",
                new AddContactCommand(c2));

        // TC3: N1, P3, E3, A3, T3
        Contact c3 = new ContactBuilder().withName("John Doe").withPhone("123456789012345")
                .withEmail("john+doe_test-1@example.com").withAddress("#12-34, 5th Ave., West-St.")
                .withTags("friend", "client").build();
        assertParseSuccess(parser, " n/John Doe p/123456789012345 e/john+doe_test-1@example.com"
                + " a/#12-34, 5th Ave., West-St. t/friend t/client", new AddContactCommand(c3));

        // TC4: N1, P1, E2, A3, T4
        Contact c4 = new ContactBuilder().withName("John Doe").withPhone("98765432")
                .withEmail("a@b.co").withAddress("#12-34, 5th Ave., West-St.")
                .withTags("friend").build();
        assertParseSuccess(parser, " n/John Doe p/98765432 e/a@b.co a/#12-34, 5th Ave., West-St. t/friend t/friend",
                new AddContactCommand(c4));

        // TC5: N2, P1, E3, A2, T4
        Contact c5 = new ContactBuilder().withName("J").withPhone("98765432")
                .withEmail("john+doe_test-1@example.com").withAddress("X")
                .withTags("friend").build();
        assertParseSuccess(parser, " n/J p/98765432 e/john+doe_test-1@example.com a/X t/friend t/friend",
                new AddContactCommand(c5));

        // TC6: N2, P2, E1, A3, T1
        Contact c6 = new ContactBuilder().withName("J").withPhone("123")
                .withEmail("john@example.com").withAddress("#12-34, 5th Ave., West-St.").withTags().build();
        assertParseSuccess(parser, " n/J p/123 e/john@example.com a/#12-34, 5th Ave., West-St.",
                new AddContactCommand(c6));

        // TC7: N2, P3, E2, A1, T3
        Contact c7 = new ContactBuilder().withName("J").withPhone("123456789012345")
                .withEmail("a@b.co").withAddress("123 Clementi Rd, #01-02")
                .withTags("friend", "client").build();
        assertParseSuccess(parser, " n/J p/123456789012345 e/a@b.co a/123 Clementi Rd, #01-02 t/friend t/client",
                new AddContactCommand(c7));

        // TC8: N2, P2, E3, A1, T2
        Contact c8 = new ContactBuilder().withName("J").withPhone("123")
                .withEmail("john+doe_test-1@example.com").withAddress("123 Clementi Rd, #01-02")
                .withTags("colleague").build();
        assertParseSuccess(parser, " n/J p/123 e/john+doe_test-1@example.com a/123 Clementi Rd, #01-02 t/colleague",
                new AddContactCommand(c8));

        // TC9: N3, P1, E2, A3, T2
        Contact c9 = new ContactBuilder().withName("John the 2nd").withPhone("98765432")
                .withEmail("a@b.co").withAddress("#12-34, 5th Ave., West-St.")
                .withTags("colleague").build();
        assertParseSuccess(parser, " n/John the 2nd p/98765432 e/a@b.co a/#12-34, 5th Ave., West-St. t/colleague",
                new AddContactCommand(c9));

        // TC10: N3, P2, E3, A1, T4
        Contact c10 = new ContactBuilder().withName("John the 2nd").withPhone("123")
                .withEmail("john+doe_test-1@example.com").withAddress("123 Clementi Rd, #01-02")
                .withTags("friend").build();
        assertParseSuccess(parser, " n/John the 2nd p/123 e/john+doe_test-1@example.com"
                + " a/123 Clementi Rd, #01-02 t/friend t/friend", new AddContactCommand(c10));

        // TC11: N3, P3, E1, A2, T1
        Contact c11 = new ContactBuilder().withName("John the 2nd").withPhone("123456789012345")
                .withEmail("john@example.com").withAddress("X").withTags().build();
        assertParseSuccess(parser, " n/John the 2nd p/123456789012345 e/john@example.com a/X",
                new AddContactCommand(c11));

        // TC12: N3, P3, E2, A2, T3
        Contact c12 = new ContactBuilder().withName("John the 2nd").withPhone("123456789012345")
                .withEmail("a@b.co").withAddress("X").withTags("friend", "client").build();
        assertParseSuccess(parser, " n/John the 2nd p/123456789012345 e/a@b.co a/X t/friend t/client",
                new AddContactCommand(c12));
    }

    @Test
    public void parse_edgeCaseIsolatedInvalidInputs_failure() {
        String validName = " n/Alice";
        String validPhone = " p/12345678";
        String validEmail = " e/alice@example.com";
        String validAddress = " a/123 Street";

        // Name edge cases: empty, invalid name containing fslash
        assertParseFailure(parser, " n/" + validPhone + validEmail + validAddress, Name.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, " n/Al/ice" + validPhone + validEmail + validAddress, Name.MESSAGE_CONSTRAINTS);

        // Phone edge cases: empty, less than 3 digits, non-digits
        assertParseFailure(parser, validName + " p/" + validEmail + validAddress, Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, validName + " p/12" + validEmail + validAddress, Phone.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, validName + " p/123a" + validEmail + validAddress, Phone.MESSAGE_CONSTRAINTS);

        // Email edge cases: empty, missing @, missing domain, missing local-part, multiple @
        assertParseFailure(parser, validName + validPhone + " e/" + validAddress, Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, validName + validPhone + " e/notanemail" + validAddress, Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, validName + validPhone + " e/alice@" + validAddress, Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, validName + validPhone + " e/@example.com" + validAddress,
                Email.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, validName + validPhone + " e/alice@@example.com" + validAddress,
                Email.MESSAGE_CONSTRAINTS);

        // Address edge cases: empty, whitespace only
        assertParseFailure(parser, validName + validPhone + validEmail + " a/", Address.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, validName + validPhone + validEmail + " a/   ", Address.MESSAGE_CONSTRAINTS);

        // Tag edge cases: empty, whitespace only, contains fslash
        assertParseFailure(parser, validName + validPhone + validEmail + validAddress + " t/",
                Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, validName + validPhone + validEmail + validAddress + " t/   ",
                Tag.MESSAGE_CONSTRAINTS);
        assertParseFailure(parser, validName + validPhone + validEmail + validAddress + " t/tag/1",
                Tag.MESSAGE_CONSTRAINTS);
    }
}
