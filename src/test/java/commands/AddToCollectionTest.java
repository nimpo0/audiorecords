package commands;

import composition.ComposCollection;
import composition.Composition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

public class AddToCollectionTest {

    private ComposCollection collection;
    private ComposCollection allCompositions;
    private AddToCollection addToCollection;

    private ByteArrayInputStream testIn;

    @BeforeEach
    public void setUp() {
        collection = new ComposCollection();
        allCompositions = new ComposCollection();
    }

    @Test
    public void testNoAvailableCompositionsToAdd() {
        testIn = new ByteArrayInputStream("".getBytes());
        Scanner scanner = new Scanner(testIn);

        addToCollection = new AddToCollection(collection, allCompositions, scanner);
        addToCollection.execute();

        assertTrue(collection.isEmpty(), "The collection should remain empty.");
    }

    @Test
    public void testCompositionAlreadyInCollection() {
        Composition comp = new Composition("Symphony No.9", "Classical", "Beethoven", 600, "No lyrics");
        allCompositions.addToAllCompositions(comp);
        collection.addComposition(comp);

        String userInput = "Symphony No.9\n";
        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        addToCollection = new AddToCollection(collection, allCompositions, scanner);
        addToCollection.execute();

        assertEquals(1, collection.getCompositions().size(), "The composition should not be duplicated.");
    }

    @Test
    public void testAddCompositionSuccess() {
        Composition comp = new Composition("Symphony No.9", "Classical", "Beethoven", 600, "No lyrics");
        allCompositions.addToAllCompositions(comp);

        String userInput = "Symphony No.9\n";
        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        addToCollection = new AddToCollection(collection, allCompositions, scanner);
        addToCollection.execute();

        assertEquals(1, collection.getCompositions().size(), "The composition should be added to the collection.");
        assertTrue(collection.containsComposition(comp), "The collection should contain the added composition.");
    }

    @Test
    public void testCompositionNotFound() {
        Composition comp = new Composition("Symphony No.9", "Classical", "Beethoven", 600, "No lyrics");
        allCompositions.addToAllCompositions(comp);

        String userInput = "Something\n";
        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        addToCollection = new AddToCollection(collection, allCompositions, scanner);
        addToCollection.execute();

        assertTrue(collection.isEmpty(), "The collection should remain empty.");
    }
}

