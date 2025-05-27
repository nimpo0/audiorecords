package commands;
import database.CompositionBD;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import static org.junit.jupiter.api.Assertions.*;

class DeleteComposTest {

    private MockedStatic<CompositionBD> mockedCompositionBD;

    @BeforeEach
    void setUp() {
        mockedCompositionBD = Mockito.mockStatic(CompositionBD.class);
    }

    @AfterEach
    void tearDown() {
        mockedCompositionBD.close();
    }

    @Test
    void testExecute_SuccessfulDeletion() {
        mockedCompositionBD.when(() -> CompositionBD.deleteComposition("TestSong")).thenReturn(true);

        TestableDeleteCompos deleteCompos = new TestableDeleteCompos("TestSong");
        deleteCompos.execute();

        mockedCompositionBD.verify(() -> CompositionBD.deleteComposition("TestSong"));
        assertEquals("Успіх", deleteCompos.alertTitle);
        assertTrue(deleteCompos.alertContent.contains("видалено"));
    }

    @Test
    void testExecute_CompositionNotFound() {
        mockedCompositionBD.when(() -> CompositionBD.deleteComposition("MissingSong")).thenReturn(false);

        TestableDeleteCompos deleteCompos = new TestableDeleteCompos("MissingSong");
        deleteCompos.execute();

        mockedCompositionBD.verify(() -> CompositionBD.deleteComposition("MissingSong"));
        assertEquals("Не знайдено", deleteCompos.alertTitle);
    }

    @Test
    void testExecute_ExceptionThrown() {
        mockedCompositionBD.when(() -> CompositionBD.deleteComposition("ErrorSong"))
                .thenThrow(new RuntimeException("DB Error"));

        TestableDeleteCompos deleteCompos = new TestableDeleteCompos("ErrorSong");
        deleteCompos.execute();

        mockedCompositionBD.verify(() -> CompositionBD.deleteComposition("ErrorSong"));
        assertEquals("Помилка", deleteCompos.alertTitle);
        assertTrue(deleteCompos.alertContent.contains("DB Error"));
    }

    @Test
    void testPrintInfo() {
        DeleteCompos deleteCompos = new DeleteCompos("AnySong");
        String info = deleteCompos.printInfo();
        assertEquals("Видалити вказану композицію з бази даних.", info);
    }

    private static class TestableDeleteCompos extends DeleteCompos {
        String alertTitle;
        String alertContent;

        public TestableDeleteCompos(String compositionName) {
            super(compositionName);
        }

        @Override
        protected void showAlert(String title, String content) {
            this.alertTitle = title;
            this.alertContent = content;
        }
    }
}
