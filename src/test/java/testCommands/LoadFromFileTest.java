package testCommands;

import commands.LoadFromFile;
import composition.ComposCollection;
import composition.Composition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import static org.junit.jupiter.api.Assertions.*;

public class LoadFromFileTest {

    private ComposCollection collection;
    private LoadFromFile loadFromFile;

    @BeforeEach
    public void setUp() throws IOException {
        collection = new ComposCollection();
        Composition composition = new Composition(
                "Symphony No.5",
                "Classical",
                "Ludwig van Beethoven",
                1800,
                "Fate motif lyrics..."
        );
        collection.addComposition(composition);

        String filename = "C:\\Users\\Admin\\Desktop\\disk.ser";
        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(collection);
        }

        collection = new ComposCollection();
    }

    @Test
    public void testLoadCollectionFromFile() {
        loadFromFile = new LoadFromFile(collection);

        loadFromFile.execute();

        assertEquals(1, collection.getCompositions().size(), "Collection should contain 1 composition after loading.");
        assertEquals("Symphony No.5", collection.getCompositions().get(0).getCompositionName(),
                "The loaded composition should be 'Symphony No.5'.");
    }


}

