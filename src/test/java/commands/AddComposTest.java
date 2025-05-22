package commands;
import database.CompositionBD;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class AddComposTest {

    @Test
    public void testSuccessfulInsertion() {
        try (MockedStatic<CompositionBD> mockedBD = mockStatic(CompositionBD.class)) {
            mockedBD.when(() -> CompositionBD.insertComposition(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString()))
                    .thenReturn(42);

            int result = CompositionBD.insertComposition("Name", "Style", 200, "Author", "Lyrics", "file://audio.mp3");

            assertEquals(42, result);
            mockedBD.verify(() -> CompositionBD.insertComposition("Name", "Style", 200, "Author", "Lyrics", "file://audio.mp3"));
        }
    }

    @Test
    public void testInsertReturnsMinusOne() {
        try (MockedStatic<CompositionBD> mockedBD = mockStatic(CompositionBD.class)) {
            mockedBD.when(() -> CompositionBD.insertComposition(anyString(), anyString(), anyInt(), anyString(), anyString(), anyString()))
                    .thenReturn(-1);

            int result = CompositionBD.insertComposition("Name", "Style", 200, "Author", "Lyrics", "file://audio.mp3");

            assertEquals(-1, result);
        }
    }
}
