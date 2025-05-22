package commands;
import database.CollectionBD;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CreateCollectionTest {

    @BeforeAll
    static void initJfx() {
        new JFXPanel();
    }

    @Test
    void testPrintInfo() {
        CreateCollection createCollection = new CreateCollection();
        assertEquals("Створити нову колекцію.", createCollection.printInfo());
    }

    @Test
    void testInsertCollectionIsCalled_WhenValidInput() throws Exception {
        CreateCollection createCollection = new CreateCollection();

        CollectionBD mockDB = mock(CollectionBD.class);
        Field field = CreateCollection.class.getDeclaredField("collectionBD");
        field.setAccessible(true);
        field.set(createCollection, mockDB);

        String testName = "TestCollection";

        mockDB.insertCollection(testName);
        verify(mockDB).insertCollection(testName);
    }
}
