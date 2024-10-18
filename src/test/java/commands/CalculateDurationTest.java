package commands;

import composition.ComposCollection;
import composition.Composition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class CalculateDurationTest {

    private ComposCollection collection;
    private CalculateDuration calculateDuration;

    @BeforeEach
    public void setUp() {
        collection = mock(ComposCollection.class);
        calculateDuration = new CalculateDuration(collection);
    }

    @Test
    public void testCalculateDurationWithEmptyCollection() {
        when(collection.isEmpty()).thenReturn(true);
        calculateDuration.execute();
        verify(collection).isEmpty();
    }

    @Test
    public void testCalculateDurationWithCompositions() {
        Composition comp1 = mock(Composition.class);
        Composition comp2 = mock(Composition.class);
        when(comp1.getComposDuration()).thenReturn(120);
        when(comp2.getComposDuration()).thenReturn(180);

        when(collection.isEmpty()).thenReturn(false);
        when(collection.getCompositions()).thenReturn(List.of(comp1, comp2));

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        calculateDuration.execute();

        String output = outContent.toString();
        assertTrue(output.contains("Total duration of compositions in the collection: 5 min 0 sec."),
                "The total duration should be 5 minutes and 0 seconds");

        System.setOut(System.out);
    }
}
