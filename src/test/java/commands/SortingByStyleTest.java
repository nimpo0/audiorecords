package commands;

import composition.ComposCollection;
import database.CompositionBD;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SortingByStyleTest {
    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    private ComposCollection collection;
    private SortingByStyle sortingByStyle;

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
        sortingByStyle = new SortingByStyle(collection, scanner);
        sortingByStyle.execute();

        String output = testOut.toString();
        assertTrue(output.contains("The collection is empty."));
    }

    @Test
    void testExecuteSortAlphabetically() {
        when(collection.isEmpty()).thenReturn(false);

        List<CompositionBD> compositions = new ArrayList<>();
        compositions.add(new CompositionBD("Symphony No.5", "Classical", "Beethoven", 1800, "Lyrics..."));
        compositions.add(new CompositionBD("Piano Concerto No.21", "Romantic", "Mozart", 1800, "Lyrics..."));

        when(collection.getCompositions()).thenReturn(compositions);

        testIn = new ByteArrayInputStream("1".getBytes());
        Scanner scanner = new Scanner(testIn);
        sortingByStyle = new SortingByStyle(collection, scanner);
        sortingByStyle.execute();

        verify(collection, times(1)).getCompositions();
        assertEquals("Classical", compositions.get(0).getStyle());
        assertEquals("Romantic", compositions.get(1).getStyle());

        String output = testOut.toString();
        assertTrue(output.contains("Compositions sorted alphabetically."));
    }

    @Test
    void testExecuteSortReverseAlphabetically() {
        when(collection.isEmpty()).thenReturn(false);

        List<CompositionBD> compositions = new ArrayList<>();
        compositions.add(new CompositionBD("Symphony No.5", "Classical", "Beethoven", 1800, "Lyrics..."));
        compositions.add(new CompositionBD("Piano Concerto No.21", "Romantic", "Mozart", 1800, "Lyrics..."));

        when(collection.getCompositions()).thenReturn(compositions);

        testIn = new ByteArrayInputStream("2".getBytes());
        Scanner scanner = new Scanner(testIn);
        sortingByStyle = new SortingByStyle(collection, scanner);
        sortingByStyle.execute();

        verify(collection, times(1)).getCompositions();
        assertEquals("Romantic", compositions.get(0).getStyle());
        assertEquals("Classical", compositions.get(1).getStyle());

        String output = testOut.toString();
        assertTrue(output.contains("Compositions sorted in reverse alphabetical order."));
    }
}
