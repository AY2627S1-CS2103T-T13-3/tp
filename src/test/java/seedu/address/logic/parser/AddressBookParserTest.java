package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CONTACTINDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_STORYINDEX;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_CONTACT;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_STORY;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.AssignCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.DeleteStoryCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.EditCommand.EditContactDescriptor;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListContactsCommand;
import seedu.address.logic.commands.ListStoriesCommand;
import seedu.address.logic.commands.ShortcutCommand;
import seedu.address.logic.commands.UnassignCommand;
import seedu.address.logic.commands.UnshortcutCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.contact.Contact;
import seedu.address.model.contact.NameContainsKeywordsPredicate;
import seedu.address.testutil.ContactBuilder;
import seedu.address.testutil.ContactUtil;
import seedu.address.testutil.EditContactDescriptorBuilder;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Contact contact = new ContactBuilder().build();
        AddCommand command = (AddCommand) parser.parseCommand(ContactUtil.getAddCommand(contact));
        assertEquals(new AddCommand(contact), command);
    }

    @Test
    public void parseCommand_assign() throws Exception {
        Index contactIndex = Index.fromOneBased(1);
        Index storyIndex = Index.fromOneBased(2);
        AssignCommand command = (AssignCommand) parser.parseCommand(AssignCommand.COMMAND_WORD + " "
                + PREFIX_CONTACTINDEX + contactIndex.getOneBased() + " "
                + PREFIX_STORYINDEX + storyIndex.getOneBased());

        assertEquals(new AssignCommand(storyIndex, contactIndex), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + INDEX_FIRST_CONTACT.getOneBased());
        assertEquals(new DeleteCommand(INDEX_FIRST_CONTACT), command);
    }

    @Test
    public void parseCommand_deleteStory() throws Exception {
        DeleteStoryCommand command = (DeleteStoryCommand) parser.parseCommand(
                DeleteStoryCommand.COMMAND_WORD + " " + INDEX_FIRST_STORY.getOneBased());
        assertEquals(new DeleteStoryCommand(INDEX_FIRST_STORY), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Contact contact = new ContactBuilder().build();
        EditContactDescriptor descriptor = new EditContactDescriptorBuilder(contact).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + INDEX_FIRST_CONTACT.getOneBased() + " " + ContactUtil.getEditContactDescriptorDetails(descriptor));
        assertEquals(new EditCommand(INDEX_FIRST_CONTACT, descriptor), command);
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = List.of("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + keywords.stream().collect(Collectors.joining(" ")));
        assertEquals(new FindCommand(new NameContainsKeywordsPredicate(keywords)), command);
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_listcontacts() throws Exception {
        assertTrue(parser.parseCommand(ListContactsCommand.COMMAND_WORD) instanceof ListContactsCommand);
        assertTrue(parser.parseCommand(ListContactsCommand.COMMAND_WORD + " si/2") instanceof ListContactsCommand);
    }

    @Test
    public void parseCommand_liststories() throws Exception {
        assertTrue(parser.parseCommand(ListStoriesCommand.COMMAND_WORD) instanceof ListStoriesCommand);
        assertTrue(parser.parseCommand(ListStoriesCommand.COMMAND_WORD + " ci/2") instanceof ListStoriesCommand);
    }

    @Test
    public void parseCommand_unassign() throws Exception {
        Index contactIndex = Index.fromOneBased(1);
        Index storyIndex = Index.fromOneBased(2);
        UnassignCommand command = (UnassignCommand) parser.parseCommand(UnassignCommand.COMMAND_WORD + " "
                + PREFIX_CONTACTINDEX + contactIndex.getOneBased() + " "
                + PREFIX_STORYINDEX + storyIndex.getOneBased());

        assertEquals(new UnassignCommand(storyIndex, contactIndex), command);
    }

    @Test
    public void parseCommand_shortcut() throws Exception {
        assertEquals(new ShortcutCommand("ls", "list"),
                parser.parseCommand(ShortcutCommand.COMMAND_WORD + " s/ls c/list"));
        assertEquals(new ShortcutCommand(), parser.parseCommand(ShortcutCommand.COMMAND_WORD));
    }

    @Test
    public void parseCommand_unshortcut() throws Exception {
        assertEquals(new UnshortcutCommand("ls"),
                parser.parseCommand(UnshortcutCommand.COMMAND_WORD + " ls"));
    }

    @Test
    public void parseCommand_shortcut_resolvesCommand() throws Exception {
        AddressBookParser parserWithShortcuts = new AddressBookParser(() -> Map.of("ls", "list"));

        assertTrue(parserWithShortcuts.parseCommand("ls") instanceof ListCommand);
    }

    @Test
    public void parseCommand_shortcutWithArguments_preservesArguments() throws Exception {
        AddressBookParser parserWithShortcuts = new AddressBookParser(() -> Map.of("d", "delete"));

        DeleteCommand command = (DeleteCommand) parserWithShortcuts.parseCommand(
                "d " + INDEX_FIRST_CONTACT.getOneBased());

        assertEquals(new DeleteCommand(INDEX_FIRST_CONTACT), command);
    }

    @Test
    public void parseCommand_shortcutsChangedAfterConstruction_usesLatestShortcuts() throws Exception {
        Map<String, String> shortcuts = new HashMap<>();
        AddressBookParser parserWithShortcuts = new AddressBookParser(() -> shortcuts);
        shortcuts.put("stories", "liststories");

        assertTrue(parserWithShortcuts.parseCommand("stories") instanceof ListStoriesCommand);
    }

    @Test
    public void parseCommand_builtinCommandAlsoStoredAsShortcut_builtinCommandTakesPriority() throws Exception {
        AddressBookParser parserWithShortcuts = new AddressBookParser(() -> Map.of("list", "find"));

        assertTrue(parserWithShortcuts.parseCommand("list") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, () -> parser.parseCommand("unknownCommand"));
    }
}
