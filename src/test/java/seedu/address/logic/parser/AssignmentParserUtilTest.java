package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;
import static seedu.address.testutil.Assert.assertThrows;

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Contains unit tests for {@code AssignmentParserUtil}.
 */
public class AssignmentParserUtilTest {

    /**
     * Equivalence Partitions for parseIndices inputs:
     * 1. Preamble:
     *    - P1: Empty preamble (valid)
     *    - P_INV1: Non-empty preamble before prefixes ("unexpected", "abc")
     *
     * 2. ContactIndex (ci/):
     *    - CI1: 1 (min positive boundary)
     *    - CI2: 2 (typical positive index)
     *    - CI3: Integer.MAX_VALUE (max positive boundary)
     *    Invalid:
     *    - CI_INV1: 0 (boundary non-positive)
     *    - CI_INV2: -1 (negative index)
     *    - CI_INV3: 2147483648 (boundary overflow)
     *    - CI_INV4: Non-integer string ("abc")
     *    - CI_INV5: Empty or whitespace value ("ci/", "ci/   ")
     *
     * 3. StoryIndex (si/):
     *    - SI1: 1 (min positive boundary)
     *    - SI2: 2 (typical positive index)
     *    - SI3: Integer.MAX_VALUE (max positive boundary)
     *    Invalid:
     *    - SI_INV1: 0 (boundary non-positive)
     *    - SI_INV2: -1 (negative index)
     *    - SI_INV3: 2147483648 (boundary overflow)
     *    - SI_INV4: Non-integer string ("abc")
     *    - SI_INV5: Empty or whitespace value ("si/", "si/   ")
     *
     * 4. Prefix Ordering:
     *    - O1: Standard (ci/ before si/)
     *    - O2: Reversed (si/ before ci/)
     *
     * 5. Missing Prefixes:
     *    - M_INV1: Missing ci/
     *    - M_INV2: Missing si/
     *    - M_INV3: Missing both ci/ and si/
     *
     * 6. Duplicate Prefixes:
     *    - D_INV1: Duplicate ci/
     *    - D_INV2: Duplicate si/
     *    - D_INV3: Duplicate both ci/ and si/
     */
    @Test
    public void parseIndices_allPairsCombinations_success() throws Exception {
        Index[] indices = {
            Index.fromOneBased(1),
            Index.fromOneBased(2),
            Index.fromOneBased(Integer.MAX_VALUE)
        };
        String[] indexStrings = {
            "1",
            "2",
            String.valueOf(Integer.MAX_VALUE)
        };

        // All pairs of (ContactIndex, StoryIndex) partitions in standard prefix order (O1)
        for (int c = 0; c < indices.length; c++) {
            for (int s = 0; s < indices.length; s++) {
                String input = " " + PREFIX_CONTACTINDEX + indexStrings[c] + " " + PREFIX_STORYINDEX + indexStrings[s];
                AssignmentIndices parsed = AssignmentParserUtil.parseIndices(input);
                assertEquals(indices[c], parsed.getContactIndex());
                assertEquals(indices[s], parsed.getStoryIndex());
            }
        }

        // All pairs of (ContactIndex, StoryIndex) partitions in reverse prefix order (O2)
        for (int c = 0; c < indices.length; c++) {
            for (int s = 0; s < indices.length; s++) {
                String input = " " + PREFIX_STORYINDEX + indexStrings[s] + " " + PREFIX_CONTACTINDEX + indexStrings[c];
                AssignmentIndices parsed = AssignmentParserUtil.parseIndices(input);
                assertEquals(indices[c], parsed.getContactIndex());
                assertEquals(indices[s], parsed.getStoryIndex());
            }
        }
    }

    /**
     * Isolated negative test cases covering each invalid equivalence partition in isolation
     * according to Heuristic 2 (no more than one invalid input per test case).
     */
    @Test
    public void parseIndices_unexpectedPreamble_throwsParseException() {
        // P_INV1: Non-empty preamble
        String input = "unexpected " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + "2";
        assertThrows(ParseException.class,
                Messages.getErrorMessageForUnexpectedPreamble("unexpected"), () ->
                AssignmentParserUtil.parseIndices(input));
    }

    @Test
    public void parseIndices_missingPrefixes_throwsParseException() {
        // M_INV1: Missing ci/
        assertThrows(ParseException.class,
                Messages.getErrorMessageForMissingRequiredInputs(PREFIX_CONTACTINDEX.toString()), () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_STORYINDEX + "1"));

        // M_INV2: Missing si/
        assertThrows(ParseException.class,
                Messages.getErrorMessageForMissingRequiredInputs(PREFIX_STORYINDEX.toString()), () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "1"));

        // M_INV3: Missing both
        assertThrows(ParseException.class,
                Messages.getErrorMessageForMissingRequiredInputs(
                        PREFIX_CONTACTINDEX.toString(), PREFIX_STORYINDEX.toString()), () ->
                AssignmentParserUtil.parseIndices(" "));
    }

    @Test
    public void parseIndices_duplicatePrefixes_throwsParseException() {
        // D_INV1: Duplicate ci/
        assertThrows(ParseException.class,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_CONTACTINDEX), () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "1 " + PREFIX_CONTACTINDEX + "2 "
                        + PREFIX_STORYINDEX + "1"));

        // D_INV2: Duplicate si/
        assertThrows(ParseException.class,
                Messages.getErrorMessageForDuplicatePrefixes(PREFIX_STORYINDEX), () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "1 "
                        + PREFIX_STORYINDEX + "1 " + PREFIX_STORYINDEX + "2"));

        // D_INV3: Duplicate both
        assertThrows(ParseException.class, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "1 " + PREFIX_CONTACTINDEX + "2 "
                        + PREFIX_STORYINDEX + "1 " + PREFIX_STORYINDEX + "2"));
    }

    @Test
    public void parseIndices_invalidContactIndex_throwsParseException() {
        // CI_INV1: 0
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "0 " + PREFIX_STORYINDEX + "1"));

        // CI_INV2: -1
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "-1 " + PREFIX_STORYINDEX + "1"));

        // CI_INV3: 2147483648 (overflow)
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "2147483648 "
                        + PREFIX_STORYINDEX + "1"));

        // CI_INV4: Non-integer string
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "abc " + PREFIX_STORYINDEX + "1"));

        // CI_INV5: Whitespace / empty value
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + " " + PREFIX_STORYINDEX + "1"));
    }

    @Test
    public void parseIndices_invalidStoryIndex_throwsParseException() {
        // SI_INV1: 0
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + "0"));

        // SI_INV2: -1
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + "-1"));

        // SI_INV3: 2147483648 (overflow)
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "1 "
                        + PREFIX_STORYINDEX + "2147483648"));

        // SI_INV4: Non-integer string
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + "abc"));

        // SI_INV5: Whitespace / empty value
        assertThrows(ParseException.class, ParserUtil.MESSAGE_INVALID_INDEX, () ->
                AssignmentParserUtil.parseIndices(" " + PREFIX_CONTACTINDEX + "1 " + PREFIX_STORYINDEX + " "));
    }

    @Test
    public void privateConstructor_invocationViaReflection() throws Exception {
        Constructor<AssignmentParserUtil> constructor = AssignmentParserUtil.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        AssignmentParserUtil instance = constructor.newInstance();
        assertNotNull(instance);
    }
}
