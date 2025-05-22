package commands;
import database.CollectionCompositionBD;
import org.junit.jupiter.api.*;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class DeleteFromCollectionTest {

    private MockedConstruction<CollectionCompositionBD> mockedCollectionCompositionBD;

    @BeforeEach
    void setUp() {
        mockedCollectionCompositionBD = Mockito.mockConstruction(CollectionCompositionBD.class);
    }

    @AfterEach
    void tearDown() {
        mockedCollectionCompositionBD.close();
    }

    @Test
    void testExecute_CollectionNotFound_ShowsAlert() {
        TestableDeleteFromCollection deleteCmd = new TestableDeleteFromCollection("song", "missingCollection");

        CollectionCompositionBD mockedBD = mockedCollectionCompositionBD.constructed().get(0);
        Mockito.when(mockedBD.getCollectionIdByName("missingCollection")).thenReturn(-1);

        deleteCmd.execute();

        assertEquals("Колекція не знайдена", deleteCmd.alertTitle);
        assertTrue(deleteCmd.alertMessage.contains("missingCollection"));

        Mockito.verify(mockedBD, Mockito.never()).removeCompositionFromCollection(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void testExecute_SuccessfulDeletion_ShowsSuccessAlert() {
        TestableDeleteFromCollection deleteCmd = new TestableDeleteFromCollection("song1", "myCollection");

        CollectionCompositionBD mockedBD = mockedCollectionCompositionBD.constructed().get(0);
        Mockito.when(mockedBD.getCollectionIdByName("myCollection")).thenReturn(1);

        Mockito.doNothing().when(mockedBD).removeCompositionFromCollection("song1", "myCollection");

        deleteCmd.execute();

        Mockito.verify(mockedBD).removeCompositionFromCollection("song1", "myCollection");
        assertEquals("Успішно", deleteCmd.alertTitle);
        assertTrue(deleteCmd.alertMessage.contains("видалено"));
    }

    @Test
    void testExecute_ExceptionThrown_ShowsErrorAlert() {
        TestableDeleteFromCollection deleteCmd = new TestableDeleteFromCollection("songErr", "collErr");

        CollectionCompositionBD mockedBD = mockedCollectionCompositionBD.constructed().get(0);
        Mockito.when(mockedBD.getCollectionIdByName("collErr")).thenReturn(1);

        Mockito.doThrow(new RuntimeException("DB failure"))
                .when(mockedBD).removeCompositionFromCollection("songErr", "collErr");

        deleteCmd.execute();

        assertEquals("Помилка", deleteCmd.alertTitle);
        assertTrue(deleteCmd.alertMessage.contains("DB failure"));
    }

    @Test
    void testPrintInfo() {
        DeleteFromCollection deleteCmd = new DeleteFromCollection("any", "any");
        String info = deleteCmd.printInfo();
        assertEquals("Видалити композицію з колекції.", info);
    }

    private static class TestableDeleteFromCollection extends DeleteFromCollection {
        String alertTitle;
        String alertMessage;

        public TestableDeleteFromCollection(String compositionName, String collectionName) {
            super(compositionName, collectionName);
        }

        @Override
        protected void showAlert(String title, String message) {
            this.alertTitle = title;
            this.alertMessage = message;
        }
    }
}
