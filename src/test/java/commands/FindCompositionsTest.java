package commands;
import composition.Composition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FindCompositionsTest {

    private FindCompositions finder;

    @BeforeEach
    void setUp() {
        finder = Mockito.spy(new FindCompositions() {
            @Override
            protected int[] showDurationInputDialog() {
                return new int[]{100, 300};
            }

            @Override
            protected void showStyledMessage(String message) {
            }
        });
    }

    @Test
    void findInRange_shouldReturnNull_whenListIsEmpty() {
        List<Composition> emptyList = new ArrayList<>();
        List<Composition> result = finder.findInRange(emptyList);
        assertNull(result);
        verify(finder).showStyledMessage("У базі немає композицій.");
    }

    @Test
    void findInRange_shouldReturnNull_whenInputIsCancelled() {
        FindCompositions cancellingFinder = Mockito.spy(new FindCompositions() {
            @Override
            protected int[] showDurationInputDialog() {
                return null;
            }

            @Override
            protected void showStyledMessage(String message) {
            }
        });

        List<Composition> list = List.of(new Composition(1, "Test", "Rock", 150, "Author", "Lyrics", "Path"));
        List<Composition> result = cancellingFinder.findInRange(list);
        assertNull(result);
    }

    @Test
    void findInRange_shouldReturnMatchingCompositions() {
        List<Composition> list = List.of(
                new Composition(1, "C1", "Rock", 90, "Author", "Lyrics", "Path"),
                new Composition(2, "C2", "Jazz", 150, "Author", "Lyrics", "Path"),
                new Composition(3, "C3", "Pop", 310, "Author", "Lyrics", "Path")
        );

        List<Composition> result = finder.findInRange(list);
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Jazz", result.get(0).getStyle());
    }

    @Test
    void findInRange_shouldReturnNull_ifNoMatchesFound() {
        List<Composition> list = List.of(
                new Composition(1, "C1", "Rock", 50, "Author", "Lyrics", "Path"),
                new Composition(2, "C2", "Jazz", 60, "Author", "Lyrics", "Path")
        );

        List<Composition> result = finder.findInRange(list);
        assertNull(result);
        verify(finder).showStyledMessage("Композицій у заданому діапазоні не знайдено.");
    }
}
