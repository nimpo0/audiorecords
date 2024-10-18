package commands;

import composition.ComposCollection;
import composition.Composition;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.mockito.Mockito.*;

class DeleteFromCollectionTest {
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private ComposCollection collection;
    private DeleteFromCollection deleteFromCollection;

    @BeforeEach
    void setUp() {
        collection = mock(ComposCollection.class);
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }

    @Test
    void testExecuteEmptyCollection() {
        when(collection.isEmpty()).thenReturn(true);

        testIn = new ByteArrayInputStream("".getBytes());
        Scanner scanner = new Scanner(testIn);
        deleteFromCollection = new DeleteFromCollection(collection, scanner);
        deleteFromCollection.execute();

        String output = testOut.toString();
        assertTrue(output.contains("The collection is empty."));
    }

    @Test
    void testExecuteCompositionSuccessfullyDeleted() {
        when(collection.isEmpty()).thenReturn(false);

        Composition composition = new Composition("Symphony No.5", "Classical", "Beethoven", 1800, "Lyrics...");
        when(collection.findCompositionByName("Symphony No.5")).thenReturn(composition);

        String userInput = "Symphony No.5";
        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        deleteFromCollection = new DeleteFromCollection(collection, scanner);
        deleteFromCollection.execute();

        verify(collection, times(1)).deleteComposition(composition);

        String output = testOut.toString();
        assertTrue(output.contains("Composition \"Symphony No.5\" successfully deleted from the collection."));
    }

    @Test
    void testExecuteCompositionNotFound() {
        when(collection.isEmpty()).thenReturn(false);
        when(collection.findCompositionByName("Symphony No.5")).thenReturn(null);

        String userInput = "Symphony No.5";
        testIn = new ByteArrayInputStream(userInput.getBytes());
        Scanner scanner = new Scanner(testIn);

        deleteFromCollection = new DeleteFromCollection(collection, scanner);
        deleteFromCollection.execute();

        verify(collection, never()).deleteComposition(any(Composition.class));

        String output = testOut.toString();
        assertTrue(output.contains("Composition \"Symphony No.5\" not found in the collection."));
    }
}
