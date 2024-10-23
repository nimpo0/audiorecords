package commands;

import composition.ComposCollection;
import composition.Composition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class SaveToFileTest {

    private ComposCollection collection;
    private SaveToFile saveToFile;

    @BeforeEach
    public void setUp() {
        collection = new ComposCollection();
        Composition composition = new Composition(
                "Symphony No.5",
                "Classical",
                "Ludwig van Beethoven",
                1800,
                "Fate motif lyrics..."
        );
        collection.addComposition(composition);
    }

    @Test
    public void testSaveCollectionToFile() {
        String fixedFilename = "C:\\Users\\Admin\\Desktop\\disk.ser";

        saveToFile = new SaveToFile(collection);
        saveToFile.execute();

        File savedFile = new File(fixedFilename);
        assertTrue(savedFile.exists(), "File should be created after saving.");
        assertTrue(savedFile.length() > 0, "File should not be empty after saving.");
    }
}

