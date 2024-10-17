package testComposition;

import composition.Composition;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CompositionTest {

    private Composition composition;

    @BeforeEach
    void setUp() {
        composition = new Composition(
                "Symphony No.5",
                "Classical",
                "Ludwig van Beethoven",
                1800,
                "Fate motif lyrics..."
        );
    }

    @Test
    void testConstructorAndGetters() {
        assertEquals("Symphony No.5", composition.getCompositionName(), "Composition name should match");
        assertEquals("Classical", composition.getStyle(), "Composition style should match");
        assertEquals("Ludwig van Beethoven", composition.getAuthor(), "Author should match");
        assertEquals(1800, composition.getComposDuration(), "Composition duration should match");
        assertEquals("Fate motif lyrics...", composition.getLyrics(), "Lyrics should match");
    }

    @Test
    void testToString() {
        String expectedSeparator = "+----------------------+-----------------+-----------------+------------+--------------------------------+";
        String expectedContent = String.format("| %-20s | %-15s | %-15s | %-10s | %-30s |%n",
                "Symphony No.5", "Classical", "Ludwig van Beethoven", "1800 s", "Fate motif lyrics...") + expectedSeparator;

        assertEquals(expectedContent, composition.toString(), "toString method should return expected format");
    }


}
