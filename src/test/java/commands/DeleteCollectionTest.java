package commands;
import database.CollectionBD;
import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

class DeleteCollectionTest {

    private MockedConstruction<CollectionBD> mockedCollectionBD;
    private MockedConstruction<DisplayCollection> mockedDisplayCollection;

    @BeforeEach
    void setUp() {
        mockedCollectionBD = Mockito.mockConstruction(CollectionBD.class);
        mockedDisplayCollection = Mockito.mockConstruction(DisplayCollection.class, (mock, context) -> {
            Mockito.doNothing().when(mock).execute();
        });
    }

    @AfterEach
    void tearDown() {
        mockedCollectionBD.close();
        mockedDisplayCollection.close();
    }

    @Test
    void testExecute_NullOrEmptyCollectionName_DoesNothing() {
        DeleteCollection nullName = new DeleteCollection(null);
        DeleteCollection emptyName = new DeleteCollection("  ");

        nullName.execute();
        emptyName.execute();

        CollectionBD bdMock = mockedCollectionBD.constructed().get(0);
        Mockito.verify(bdMock, Mockito.never()).deleteCollection(Mockito.anyString());

        DisplayCollection displayMock = mockedDisplayCollection.constructed().isEmpty() ? null : mockedDisplayCollection.constructed().get(0);
        if (displayMock != null) {
            Mockito.verify(displayMock, Mockito.never()).execute();
        }
    }

    @Test
    void testExecute_ValidCollectionName_DeletesAndShowsMessage() {
        String testName = "MyCollection";

        TestableDeleteCollection deleteCollection = new TestableDeleteCollection(testName);

        CollectionBD bdMock = mockedCollectionBD.constructed().get(0);
        Mockito.doNothing().when(bdMock).deleteCollection(testName);

        deleteCollection.execute();

        Mockito.verify(bdMock).deleteCollection(testName);

        assertEquals("🗑 Колекцію 'MyCollection' видалено.", deleteCollection.alertMessage);

        DisplayCollection displayMock = mockedDisplayCollection.constructed().get(0);
        Mockito.verify(displayMock).execute();
    }

    @Test
    void testPrintInfo_ReturnsCorrectString() {
        DeleteCollection cmd = new DeleteCollection("any");
        assertEquals("Видалити колекцію.", cmd.printInfo());
    }

    private static class TestableDeleteCollection extends DeleteCollection {
        String alertMessage;

        public TestableDeleteCollection(String collectionName) {
            super(collectionName);
        }

        @Override
        protected void showMessage(String msg) {
            this.alertMessage = msg;
        }
    }
}
