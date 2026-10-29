package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.FindContactsCommand;
import seedu.address.model.contact.NameContainsKeywordsPredicate;

public class FindContactsCommandParserTest {

    private FindContactsCommandParser parser = new FindContactsCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                FindContactsCommand.MESSAGE_USAGE);
        assertParseFailure(parser, "     ", expectedMessage);
        assertParseFailure(parser, "", expectedMessage);
        assertParseFailure(parser, "\t \n \r", expectedMessage);
    }

    @Test
    public void parse_validArgs_returnsFindCommand() {
        // Single keyword
        FindContactsCommand singleKeywordCommand =
                new FindContactsCommand(new NameContainsKeywordsPredicate(List.of("Alice")));
        assertParseSuccess(parser, "Alice", singleKeywordCommand);
        assertParseSuccess(parser, "  Alice  ", singleKeywordCommand);

        // Multiple keywords without leading and trailing whitespaces
        FindContactsCommand expectedFindContactsCommand =
                new FindContactsCommand(new NameContainsKeywordsPredicate(List.of("Alice", "Bob", "Carol")));
        assertParseSuccess(parser, "Alice Bob Carol", expectedFindContactsCommand);

        // Multiple whitespaces between keywords (tabs, newlines)
        assertParseSuccess(parser, " \n Alice \n \t Bob \r Carol \t", expectedFindContactsCommand);

        // Keywords with symbols or numbers
        FindContactsCommand symbolCommand =
                new FindContactsCommand(new NameContainsKeywordsPredicate(List.of("Alice-Bob", "Dr.", "123")));
        assertParseSuccess(parser, "Alice-Bob Dr. 123", symbolCommand);
    }

}
