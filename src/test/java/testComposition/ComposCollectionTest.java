package testComposition;

import composition.Composition;
import composition.ComposCollection;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class ComposCollectionTest {

    private ComposCollection composCollection;
    ByteArrayOutputStream outContent;
    private Composition symphony;
    private Composition concerto;

    @BeforeEach
    void setUp() {
        composCollection = new ComposCollection();
        symphony = new Composition(
                "Symphony No.5",
                "Classical",
                "Ludwig van Beethoven",
                1800,
                "Fate motif lyrics..."
        );
        concerto = new Composition(
                "Piano Concerto No.21",
                "Classical",
                "Wolfgang Amadeus Mozart",
                1500,
                "Elvira Madigan lyrics..."
        );
    }

    @Test
    void testConstructor() {
        assertNotNull(composCollection.getCompositions(), "Compositions list should be initialized");
        assertNotNull(composCollection.getAllCompositions(), "AllCompositions list should be initialized");
    }

    @Test
    void testAddComposition() {
        composCollection.addComposition(symphony);
        assertEquals(1, composCollection.getCompositions().size(), "Compositions list should contain one composition");
        assertTrue(composCollection.getCompositions().contains(symphony), "Compositions list should contain the added composition");
    }

    @Test
    void testAddToAllCompositions() {
        composCollection.addToAllCompositions(concerto);
        assertEquals(1, composCollection.getAllCompositions().size(), "AllCompositions list should contain one composition");
        assertTrue(composCollection.getAllCompositions().contains(concerto), "AllCompositions list should contain the added composition");
    }

    @Test
    void testFindCompositionByName() {
        composCollection.addComposition(symphony);
        composCollection.addComposition(concerto);

        Composition found = composCollection.findCompositionByName("Symphony No.5");
        assertNotNull(found, "Should find the composition by name");
        assertEquals(symphony, found, "Found composition should be the correct one");

        Composition notFound = composCollection.findCompositionByName("Something");
        assertNull(notFound, "Should return null for non-existent composition");
    }

    @Test
    void testFindInAllCompositions() {
        composCollection.addToAllCompositions(symphony);
        composCollection.addToAllCompositions(concerto);

        Composition found = composCollection.findInAllCompositions("Piano Concerto No.21");
        assertNotNull(found, "Should find the composition in allCompositions by name");
        assertEquals(concerto, found, "Found composition should be the correct one");

        Composition notFound = composCollection.findInAllCompositions("Something");
        assertNull(notFound, "Should return null for non-existent composition in allCompositions");
    }

    @Test
    void testDisplayCompositionsWhenEmpty() {
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        composCollection.displayCompositions();

        assertTrue(outContent.toString().contains("The collection is empty."), "Should display 'The collection is empty.' when no compositions exist");

        System.setOut(System.out);
    }

    @Test
    void testDisplayCompositionsWithContent() {
        composCollection.addComposition(symphony);
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        composCollection.displayCompositions();

        assertTrue(outContent.toString().contains("Symphony No.5"), "Should display the composition when it exists");

        System.setOut(System.out);
    }
}
