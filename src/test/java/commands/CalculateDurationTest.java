package commands;
import composition.Composition;
import database.CollectionBD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CalculateDurationTest {

    private CalculateDuration calculateDuration;
    private CollectionBD mockCollectionBD;

    @BeforeEach
    void setUp() {
        mockCollectionBD = mock(CollectionBD.class);
        calculateDuration = new CalculateDuration() {
            {
                this.collectionBD = mockCollectionBD;
            }
        };
    }

    @Test
    void shouldReturnMinusOneIfCollectionNameIsNull() {
        int result = calculateDuration.getTotalDuration(null);
        assertEquals(-1, result);
    }

    @Test
    void shouldReturnMinusOneIfCollectionNameIsEmpty() {
        int result = calculateDuration.getTotalDuration("   ");
        assertEquals(-1, result);
    }

    @Test
    void shouldReturnMinusOneIfCollectionDoesNotExistOrEmpty() {
        when(mockCollectionBD.getCompositionsForCollection("MyCollection")).thenReturn(null);
        int resultNull = calculateDuration.getTotalDuration("MyCollection");
        assertEquals(-1, resultNull);

        when(mockCollectionBD.getCompositionsForCollection("MyCollection")).thenReturn(List.of());
        int resultEmpty = calculateDuration.getTotalDuration("MyCollection");
        assertEquals(-1, resultEmpty);
    }

    @Test
    void shouldReturnSumOfDurations() {
        List<Composition> compositions = List.of(
                new Composition(1, "Song1", "Pop", 120, "Author1", "Text1", "Path1"),
                new Composition(2, "Song2", "Rock", 180, "Author2", "Text2", "Path2"),
                new Composition(3, "Song3", "Jazz", 240, "Author3", "Text3", "Path3")
        );

        when(mockCollectionBD.getCompositionsForCollection("MyCollection")).thenReturn(compositions);

        int result = calculateDuration.getTotalDuration("MyCollection");
        assertEquals(120 + 180 + 240, result);
    }
}
