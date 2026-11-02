package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CONTACT;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.contact.Address;
import seedu.address.model.contact.Email;
import seedu.address.model.contact.Name;
import seedu.address.model.contact.Phone;
import seedu.address.model.followup.FollowupDescription;
import seedu.address.model.story.StoryName;
import seedu.address.model.tag.Tag;

public class ParserUtilTest {
    private static final String INVALID_NAME = " ";
    private static final String INVALID_PHONE = " ";
    private static final String INVALID_ADDRESS = " ";
    private static final String INVALID_EMAIL = " ";
    private static final String INVALID_TAG = " ";

    private static final String VALID_NAME = "Rachel Walker";
    private static final String VALID_PHONE = "123456";
    private static final String VALID_ADDRESS = "123 Main Street #0505";
    private static final String VALID_EMAIL = "rachel@example.com";
    private static final String VALID_TAG_1 = "friend";
    private static final String VALID_TAG_2 = "neighbour";

    private static final String WHITESPACE = " \t\r\n";

    @Test
    public void instantiation() {
        assertNotNull(new ParserUtil());
    }

    @Test
    public void parseIndex_invalidInput_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseIndex("10 a"));
    }

    @Test
    public void parseIndex_outOfRangeInput_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, ()
            -> ParserUtil.parseIndex(Long.toString((long) Integer.MAX_VALUE + 1)));
    }

    @Test
    public void parseIndex_nonPositiveOrMalformedInput_throwsParseException() {
        for (String input : List.of("", WHITESPACE, "0", "-1", "+1", "1.0", "1 0")) {
            assertThrows(ParseException.class, MESSAGE_INVALID_INDEX, () -> ParserUtil.parseIndex(input));
        }
    }

    @Test
    public void parseIndex_maximumInteger_returnsIndex() throws Exception {
        assertEquals(Integer.MAX_VALUE,
                ParserUtil.parseIndex(WHITESPACE + Integer.MAX_VALUE + WHITESPACE).getOneBased());
    }

    @Test
    public void parseStoryName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseStoryName(null));
    }

    @Test
    public void parseStoryName_blankOrInvalidValue_throwsParseException() {
        for (String input : List.of("", WHITESPACE)) {
            assertThrows(ParseException.class, StoryName.MESSAGE_CONSTRAINTS, () -> ParserUtil.parseStoryName(input));
        }
    }

    @Test
    public void parseStoryName_surroundingWhitespace_preservesInternalSpaces() throws Exception {
        assertEquals(new StoryName("Story  1"), ParserUtil.parseStoryName(WHITESPACE + "Story  1" + WHITESPACE));
    }

    @Test
    public void parseStoryName_startsWithSlash_success() throws Exception {
        assertEquals(new StoryName("/"), ParserUtil.parseStoryName("/"));
        assertEquals(new StoryName("/Story"), ParserUtil.parseStoryName("/Story"));
    }

    @Test
    public void parseFollowupDescription_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseFollowupDescription(null));
    }

    @Test
    public void parseFollowupDescription_blankOrInvalidValue_throwsParseException() {
        for (String input : List.of("", WHITESPACE)) {
            assertThrows(ParseException.class, FollowupDescription.MESSAGE_CONSTRAINTS, ()
                    -> ParserUtil.parseFollowupDescription(input));
        }
    }

    @Test
    public void parseFollowupDescription_surroundingWhitespace_preservesInternalSpaces() throws Exception {
        assertEquals(new FollowupDescription("Call  back"),
                ParserUtil.parseFollowupDescription(WHITESPACE + "Call  back" + WHITESPACE));
    }

    @Test
    public void parseFollowupDescription_startsWithSlash_success() throws Exception {
        assertEquals(new FollowupDescription("/"), ParserUtil.parseFollowupDescription("/"));
        assertEquals(new FollowupDescription("/Callback"), ParserUtil.parseFollowupDescription("/Callback"));
    }

    @Test
    public void parseTags_duplicatesAfterTrimming_returnsUniqueTags() throws Exception {
        assertEquals(Set.of(new Tag(VALID_TAG_1)),
                ParserUtil.parseTags(List.of(VALID_TAG_1, WHITESPACE + VALID_TAG_1 + WHITESPACE, VALID_TAG_1)));
    }

    @Test
    public void parseIndex_validInput_success() throws Exception {
        // No whitespaces
        assertEquals(INDEX_FIRST_CONTACT, ParserUtil.parseIndex("1"));

        // Leading and trailing whitespaces
        assertEquals(INDEX_FIRST_CONTACT, ParserUtil.parseIndex("  1  "));
    }

    @Test
    public void parseName_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseName((String) null));
    }

    @Test
    public void parseName_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseName(INVALID_NAME));
    }

    @Test
    public void parseName_validValueWithoutWhitespace_returnsName() throws Exception {
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(VALID_NAME));
    }

    @Test
    public void parseName_validValueWithWhitespace_returnsTrimmedName() throws Exception {
        String nameWithWhitespace = WHITESPACE + VALID_NAME + WHITESPACE;
        Name expectedName = new Name(VALID_NAME);
        assertEquals(expectedName, ParserUtil.parseName(nameWithWhitespace));
    }

    @Test
    public void parseName_startsWithSlash_returnsName() throws Exception {
        Name expectedName = new Name("/Rachel");
        assertEquals(expectedName, ParserUtil.parseName("/Rachel"));
    }

    @Test
    public void parsePhone_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parsePhone((String) null));
    }

    @Test
    public void parsePhone_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parsePhone(INVALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithoutWhitespace_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(VALID_PHONE));
    }

    @Test
    public void parsePhone_validValueWithWhitespace_returnsTrimmedPhone() throws Exception {
        String phoneWithWhitespace = WHITESPACE + VALID_PHONE + WHITESPACE;
        Phone expectedPhone = new Phone(VALID_PHONE);
        assertEquals(expectedPhone, ParserUtil.parsePhone(phoneWithWhitespace));
    }

    @Test
    public void parsePhone_startsWithSlash_returnsPhone() throws Exception {
        Phone expectedPhone = new Phone("/651234");
        assertEquals(expectedPhone, ParserUtil.parsePhone("/651234"));
    }

    @Test
    public void parseAddress_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseAddress((String) null));
    }

    @Test
    public void parseAddress_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseAddress(INVALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithoutWhitespace_returnsAddress() throws Exception {
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(VALID_ADDRESS));
    }

    @Test
    public void parseAddress_validValueWithWhitespace_returnsTrimmedAddress() throws Exception {
        String addressWithWhitespace = WHITESPACE + VALID_ADDRESS + WHITESPACE;
        Address expectedAddress = new Address(VALID_ADDRESS);
        assertEquals(expectedAddress, ParserUtil.parseAddress(addressWithWhitespace));
    }

    @Test
    public void parseEmail_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseEmail((String) null));
    }

    @Test
    public void parseEmail_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseEmail(INVALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithoutWhitespace_returnsEmail() throws Exception {
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(VALID_EMAIL));
    }

    @Test
    public void parseEmail_validValueWithWhitespace_returnsTrimmedEmail() throws Exception {
        String emailWithWhitespace = WHITESPACE + VALID_EMAIL + WHITESPACE;
        Email expectedEmail = new Email(VALID_EMAIL);
        assertEquals(expectedEmail, ParserUtil.parseEmail(emailWithWhitespace));
    }

    @Test
    public void parseEmail_startsWithSlash_returnsEmail() throws Exception {
        Email expectedEmail = new Email("/example.com");
        assertEquals(expectedEmail, ParserUtil.parseEmail("/example.com"));
    }

    @Test
    public void parseTag_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTag(null));
    }

    @Test
    public void parseTag_invalidValue_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTag(INVALID_TAG));
    }

    @Test
    public void parseTag_validValueWithoutWhitespace_returnsTag() throws Exception {
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(VALID_TAG_1));
    }

    @Test
    public void parseTag_validValueWithWhitespace_returnsTrimmedTag() throws Exception {
        String tagWithWhitespace = WHITESPACE + VALID_TAG_1 + WHITESPACE;
        Tag expectedTag = new Tag(VALID_TAG_1);
        assertEquals(expectedTag, ParserUtil.parseTag(tagWithWhitespace));
    }

    @Test
    public void parseTag_startsWithSlash_returnsTag() throws Exception {
        Tag expectedTag = new Tag("/friend");
        assertEquals(expectedTag, ParserUtil.parseTag("/friend"));
    }

    @Test
    public void parseTags_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTags(null));
    }

    @Test
    public void parseTags_collectionWithInvalidTags_throwsParseException() {
        assertThrows(ParseException.class, () -> ParserUtil.parseTags(List.of(VALID_TAG_1, INVALID_TAG)));
    }

    @Test
    public void parseTags_emptyCollection_returnsEmptySet() throws Exception {
        assertTrue(ParserUtil.parseTags(List.of()).isEmpty());
    }

    @Test
    public void parseTags_collectionWithValidTags_returnsTagSet() throws Exception {
        Set<Tag> actualTagSet = ParserUtil.parseTags(List.of(VALID_TAG_1, VALID_TAG_2));
        Set<Tag> expectedTagSet = Set.of(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2));

        assertEquals(expectedTagSet, actualTagSet);
    }

    @Test
    public void parseTagsForEdit_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> ParserUtil.parseTagsForEdit(null));
    }

    @Test
    public void parseTagsForEdit_noValues_returnsEmptyOptional() throws Exception {
        assertEquals(Optional.empty(), ParserUtil.parseTagsForEdit(List.of()));
    }

    @Test
    public void parseTagsForEdit_emptyValue_returnsEmptyTagSet() throws Exception {
        assertEquals(Optional.of(Set.of()), ParserUtil.parseTagsForEdit(List.of("")));
    }

    @Test
    public void parseTagsForEdit_validValues_returnsTagSet() throws Exception {
        assertEquals(Optional.of(Set.of(new Tag(VALID_TAG_1), new Tag(VALID_TAG_2))),
                ParserUtil.parseTagsForEdit(List.of(VALID_TAG_1, VALID_TAG_2)));
    }
}
