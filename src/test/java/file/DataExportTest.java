package file;
import composition.Composition;
import database.CompositionBD;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainPackage.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.io.File;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DataExportTest {

    private File mockFile;

    @BeforeEach
    void setUp() {
        mockFile = new File("mock.json");
    }

    @Test
    void testExecute_successfulExport() throws IOException {
        DataExport export = new DataExport() {
            @Override
            protected FileChooser createFileChooser() {
                FileChooser fileChooser = mock(FileChooser.class);
                when(fileChooser.showSaveDialog(any())).thenReturn(mockFile);
                return fileChooser;
            }

            @Override
            protected void writeCompositionsToFile(File file, List<Composition> compositions) throws IOException {
            }
        };

        try (
                MockedStatic<Menu> menuMock = mockStatic(Menu.class);
                MockedStatic<CompositionBD> compMock = mockStatic(CompositionBD.class)
        ) {
            Menu mockMenu = mock(Menu.class);
            menuMock.when(Menu::getPrimaryStage).thenReturn(mock(Stage.class));
            menuMock.when(Menu::getInstance).thenReturn(mockMenu);

            List<Composition> comps = List.of(new Composition(1, "A", "Rock", 100, "B", "Lyrics", "audio.mp3"));
            compMock.when(CompositionBD::getAllCompositions).thenReturn(comps);

            export.execute();

            verify(mockMenu).showAlert("Успіх", "Композиції експортовано успішно!");
        }
    }

    @Test
    void testExecute_withIOException() throws IOException {
        DataExport export = new DataExport() {
            @Override
            protected FileChooser createFileChooser() {
                FileChooser chooser = mock(FileChooser.class);
                when(chooser.showSaveDialog(any())).thenReturn(mockFile);
                return chooser;
            }

            @Override
            protected void writeCompositionsToFile(File file, List<Composition> compositions) throws IOException {
                throw new IOException("Simulated IO failure");
            }
        };

        try (
                MockedStatic<Menu> menuMock = mockStatic(Menu.class);
                MockedStatic<CompositionBD> compMock = mockStatic(CompositionBD.class)
        ) {
            Menu mockMenu = mock(Menu.class);
            menuMock.when(Menu::getPrimaryStage).thenReturn(mock(Stage.class));
            menuMock.when(Menu::getInstance).thenReturn(mockMenu);

            compMock.when(CompositionBD::getAllCompositions).thenReturn(List.of(
                    new Composition(1, "A", "Rock", 100, "B", "Lyrics", "audio.mp3")
            ));

            export.execute();

            verify(mockMenu).showAlert(eq("Помилка"), contains("Simulated IO failure"));
        }
    }

    @Test
    void testCreateFileChooser() {
        DataExport export = new DataExport();
        FileChooser chooser = export.createFileChooser();

        assertNotNull(chooser);
        assertEquals("Експорт композицій у файл", chooser.getTitle());

        List<FileChooser.ExtensionFilter> filters = chooser.getExtensionFilters();
        assertEquals(1, filters.size());
        assertEquals("JSON files", filters.get(0).getDescription());
        assertTrue(filters.get(0).getExtensions().contains("*.json"));
    }


    @Test
    void testWriteCompositionsToFile() throws IOException {
        DataExport export = new DataExport();

        File tempFile = File.createTempFile("test_compositions", ".json");
        tempFile.deleteOnExit();

        List<Composition> comps = List.of(
                new Composition(1, "Test Song", "Pop", 200, "Author", "Lyrics", "audio.mp3")
        );

        export.writeCompositionsToFile(tempFile, comps);

        // Перевіримо, що файл не пустий
        assertTrue(tempFile.length() > 0);

        // (опціонально) перевірити, що вміст має назву композиції
        String content = java.nio.file.Files.readString(tempFile.toPath());
        assertTrue(content.contains("Test Song"));
    }

    @Test
    void testPrintInfo() {
        assertEquals("Експорт всіх композицій у файл json.", new DataExport().printInfo());
    }
}
