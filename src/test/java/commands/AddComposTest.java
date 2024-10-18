package commands;

import composition.ComposCollection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class AddComposTest {
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private ComposCollection allCompos;
    private AddCompos addCompos;

    @BeforeEach
    void setUp() {
        allCompos = mock(ComposCollection.class);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setIn(System.in);
        System.setOut(System.out);
    }

    @Test
    void testExecuteSuccessfulAdd() {
        String userInput = "Piano Concerto No.21\nClassical\nWolfgang Amadeus Mozart\n200\nElvira Madigan lyrics...";

        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        addCompos = new AddCompos(allCompos, scanner);
        addCompos.execute();

        String output = testOut.toString();
        assertTrue(output.contains("Composition successfully added."));
    }

    @Test
    void testExecuteInvalidDurationThenValid() {
        String userInput = "Piano Concerto No.21\nClassical\nWolfgang Amadeus Mozart\n-500\n200\nElvira Madigan lyrics...";

        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        addCompos = new AddCompos(allCompos, scanner);
        addCompos.execute();

        String output = testOut.toString();
        assertTrue(output.contains("Duration must be a positive number. Please try again."));
        assertTrue(output.contains("Composition successfully added."));
    }

    @Test
    void testExecuteNonIntegerDurationThenValid() {
        String userInput = "Piano Concerto No.21\nClassical\nWolfgang Amadeus Mozart\nabc\n200\nElvira Madigan lyrics...";

        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        addCompos = new AddCompos(allCompos, scanner);
        addCompos.execute();

        String output = testOut.toString();
        assertTrue(output.contains("Invalid input, please enter a whole number."));
        assertTrue(output.contains("Composition successfully added."));
    }
}
