package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class CliSyntaxTest {

    @Test
    public void instantiation() {
        CliSyntax cliSyntax = new CliSyntax();
        assertNotNull(cliSyntax);
    }
}
