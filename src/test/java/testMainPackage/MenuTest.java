package testMainPackage;

import mainPackage.Menu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class MenuTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outContent));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(System.out);
        System.setIn(System.in);
    }

    @Test
    public void testMenuExecutionValidCommands() {
        String userInput = "1\nTest Composition\nClassical\nTest Author\n120\nTest Lyrics\n2\n0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        Menu menu = new Menu();
        menu.start();

        String output = outContent.toString();
        assertTrue(output.contains("=== MENU ==="), "Menu should be printed");
        assertTrue(output.contains("0. Exit"), "Menu should contain an Exit option");
        assertTrue(output.contains("Command executed successfully."), "Should indicate successful command execution");
    }

    @Test
    public void testExitOption() {
        String userInput = "0\n";
        ByteArrayInputStream in = new ByteArrayInputStream(userInput.getBytes());
        System.setIn(in);

        Menu menu = new Menu();
        menu.start();

        String output = outContent.toString();
        assertTrue(output.contains("Exiting the program. Goodbye!"), "Exit message should be displayed");
    }
}
