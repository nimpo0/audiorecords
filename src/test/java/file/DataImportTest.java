package file;

import composition.Composition;
import database.CompositionBD;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainPackage.Menu;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DataImportTest {

    private MockedStatic<Menu> menuMockedStatic;
    private MockedStatic<CompositionBD> compositionBDMockedStatic;

    private Menu mockMenu;
    private Stage mockStage;

    @BeforeEach
    public void setup() {
        // Мокаємо Menu.getInstance() та Menu.getPrimaryStage()
        mockMenu = mock(Menu.class);
        mockStage = mock(Stage.class);

        menuMockedStatic = Mockito.mockStatic(Menu.class);
        menuMockedStatic.when(Menu::getInstance).thenReturn(mockMenu);
        menuMockedStatic.when(Menu::getPrimaryStage).thenReturn(mockStage);

        // Мокаємо статичні методи CompositionBD
        compositionBDMockedStatic = Mockito.mockStatic(CompositionBD.class);
    }

    @AfterEach
    public void tearDown() {
        menuMockedStatic.close();
        compositionBDMockedStatic.close();
    }

    @Test
    public void testExecute_successfulImport() throws Exception {
        // Підготовка даних
        File mockFile = mock(File.class);
        Composition c1 = new Composition(1,"Name1", "Style1", 200, "Author1", "Lyrics1", "path1");
        Composition c2 = new Composition(2, "Name2", "Style2", 180, "Author2", "Lyrics2", "path2");

        // Створюємо "шпигуна" над об'єктом DataImport, щоби підмінити методи
        DataImport importCommand = spy(new DataImport());

        // Підміна поведінки методів
        FileChooser mockChooser = mock(FileChooser.class);
        doReturn(mockChooser).when(importCommand).createFileChooser();
        when(mockChooser.showOpenDialog(mockStage)).thenReturn(mockFile);
        doReturn(List.of(c1, c2)).when(importCommand).readCompositionsFromFile(mockFile);

        // Виконання
        importCommand.execute();

        // Перевірка що вставлено
        compositionBDMockedStatic.verify(() -> CompositionBD.insertComposition(
                eq("Name1"), eq("Style1"), eq(200), eq("Author1"), eq("Lyrics1"), eq("path1")));
        compositionBDMockedStatic.verify(() -> CompositionBD.insertComposition(
                eq("Name2"), eq("Style2"), eq(180), eq("Author2"), eq("Lyrics2"), eq("path2")));

        // Перевірка що показано повідомлення
        verify(mockMenu).showAlert(eq("Успіх"), contains("Композиції імпортовано успішно"));
    }

    @Test
    public void testExecute_userCancelsDialog() {
        DataImport importCommand = spy(new DataImport());
        FileChooser mockChooser = mock(FileChooser.class);

        // Повертає null, наче користувач скасував вибір
        doReturn(mockChooser).when(importCommand).createFileChooser();
        when(mockChooser.showOpenDialog(mockStage)).thenReturn(null);

        // Перевірка, що нічого не викликається
        importCommand.execute();

        // Ніякого імпорту чи алертів не має бути
        verify(mockMenu, never()).showAlert(anyString(), anyString());
        compositionBDMockedStatic.verifyNoInteractions();
    }

    @Test
    public void testExecute_fileReadFails_showsError() throws Exception {
        File mockFile = mock(File.class);
        DataImport importCommand = spy(new DataImport());

        FileChooser mockChooser = mock(FileChooser.class);
        doReturn(mockChooser).when(importCommand).createFileChooser();
        when(mockChooser.showOpenDialog(mockStage)).thenReturn(mockFile);

        // readCompositionsFromFile кидає IOException
        doThrow(new java.io.IOException("Failed to read file")).when(importCommand).readCompositionsFromFile(mockFile);

        importCommand.execute();

        // Має зловити помилку і показати алерт
        verify(mockMenu).showAlert(eq("Помилка"), contains("Failed to read file"));
    }

    @Test
    void testCreateFileChooser() {
        DataImport importCommand = new DataImport();
        FileChooser chooser = importCommand.createFileChooser();

        Assertions.assertNotNull(chooser);
        assertEquals("Імпорт композицій з файлу", chooser.getTitle());

        var filters = chooser.getExtensionFilters();
        assertEquals(1, filters.size());
        assertEquals("JSON files", filters.get(0).getDescription());
        Assertions.assertTrue(filters.get(0).getExtensions().contains("*.json"));
    }

    @Test
    void testReadCompositionsFromFile() throws Exception {
        DataImport importCommand = new DataImport();

        File tempFile = File.createTempFile("test_compositions", ".json");
        tempFile.deleteOnExit();

        String json = """
        [
          {
            "id": 1,
            "name": "Test Song",
            "style": "Pop",
            "duration": 200,
            "author": "Author1",
            "lyrics": "Some lyrics",
            "audioPath": "path/to/audio.mp3"
          }
        ]
        """;
        java.nio.file.Files.writeString(tempFile.toPath(), json);

        List<Composition> result = importCommand.readCompositionsFromFile(tempFile);

        assertEquals(1, result.size());
        Composition c = result.get(0);
        assertEquals("Test Song", c.getName());
        assertEquals("Pop", c.getStyle());
        assertEquals(200, c.getDuration());
        assertEquals("Author1", c.getAuthor());
        assertEquals("Some lyrics", c.getLyrics());
        assertEquals("path/to/audio.mp3", c.getAudioPath());
    }

    @Test
    void testPrintInfo() {
        assertEquals("Імпорт композицій з файлу json.", new DataImport().printInfo());
    }
}
