package composition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CollectionTest {

    private Collection collection;
    private Composition comp1;
    private Composition comp2;

    @BeforeEach
    void setUp() {
        collection = new Collection(1, "Favorites");
        comp1 = new Composition(1, "Song A", "Pop", 180, "Author A", "Lyrics A", "/path/audioA.mp3");
        comp2 = new Composition(2, "Song B", "Rock", 210, "Author B", "Lyrics B", "/path/audioB.mp3");
    }

    @Test
    void testCollectionName() {
        assertEquals("Favorites", collection.getName());
    }

    @Test
    void testSetName() {
        collection.setName("New Name");
        assertEquals("New Name", collection.getName());
    }

    @Test
    void testSetAndGetCompositions() {
        List<Composition> compositions = Arrays.asList(comp1, comp2);
        collection.setCompositions(compositions);
        assertEquals(2, collection.getCompositions().size());
        assertTrue(collection.getCompositions().contains(comp1));
    }

    @Test
    void testAddComposition() {
        collection.addComposition(comp1);
        collection.addComposition(comp2);

        List<Composition> compositions = collection.getCompositions();
        assertEquals(2, compositions.size());
        assertTrue(compositions.contains(comp1));
        assertTrue(compositions.contains(comp2));
    }

    @Test
    void testToString() {
        String expected = "Collection{id=1, name='Favorites'}";
        assertEquals(expected, collection.toString());
    }
}
