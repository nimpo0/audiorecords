package commands;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import composition.ComposCollection;
import composition.Composition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class FindCompositionsTest {

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private ComposCollection allCompos;
    private FindCompositions findCompositions;

    @BeforeEach
    public void setUp() {
        allCompos = new ComposCollection();
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(System.out);
    }

    @Test
    public void testEmptyCollection() {
        String userInput = "60\n300\n";
        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        findCompositions = new FindCompositions(allCompos, scanner);
        findCompositions.execute();

        String output = testOut.toString();
        assertTrue(output.contains("The collection is empty."),
                "Should indicate the collection is empty.");
    }

    @Test
    public void testInvalidMinDurationInput() {
        Composition comp = new Composition("Symphony No.9", "Classical", "Beethoven", 600, "No lyrics");
        allCompos.addToAllCompositions(comp);

        String userInput = "-10\n60\n600\n";
        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        findCompositions = new FindCompositions(allCompos, scanner);
        findCompositions.execute();

        String output = testOut.toString();
        assertTrue(output.contains("Duration cannot be negative. Please try again."),
                "Should prompt for valid min duration.");

        assertTrue(output.contains("Symphony No.9"), "The composition should be found after correct input.");
    }

    @Test
    public void testInvalidMaxDurationInput() {
        Composition comp = new Composition("Symphony No.9", "Classical", "Beethoven", 600, "No lyrics");
        allCompos.addToAllCompositions(comp);

        String userInput = "60\nabc\n30\n700\n";
        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        findCompositions = new FindCompositions(allCompos, scanner);
        findCompositions.execute();

        String output = testOut.toString();
        assertTrue(output.contains("Invalid input, please enter a whole number."),
                "Should prompt for valid max duration.");

        assertTrue(output.contains("Symphony No.9"), "The composition should be found after correct input.");
    }
}
