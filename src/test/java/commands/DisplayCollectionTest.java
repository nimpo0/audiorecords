package commands;
import composition.Collection;
import composition.Composition;
import javafx.scene.layout.VBox;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testutil.DisplayHelper;
import java.util.Arrays;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

public class DisplayCollectionTest {

    private DisplayCollection displayCollection;

    @BeforeEach
    public void setup() {
        displayCollection = new DisplayCollection() {
            @Override
            public java.util.List<Collection> getAllCollections() {
                return Collections.emptyList();
            }

            @Override
            public VBox createCollectionCard(Collection collection) {
                collection.getCompositions().clear();
                return super.createCollectionCard(collection);
            }
        };
    }

    @Test
    public void testGetDurationText_withValidDuration() {
        String result = displayCollection.getDurationText(5, 125);
        assertEquals("Кількість пісень: 5\nТривалість: 2 хв 5 сек", result);
    }

    @Test
    public void testGetDurationText_withZeroDuration() {
        String result = displayCollection.getDurationText(0, 0);
        assertEquals("Кількість пісень: 0\nТривалість: 0 хв 0 сек", result);
    }

    @Test
    public void testGetDurationText_negativeDuration() {
        String result = displayCollection.getDurationText(2, -1);
        assertEquals("Кількість пісень: 2\nТривалість: невизначено", result);
    }

    @Test
    public void testGetDurationText_withNegativeDuration() {
        String result = displayCollection.getDurationText(3, -1);
        assertEquals("Кількість пісень: 3\nТривалість: невизначено", result);
    }

    @Test
    public void testFormatDuration_returnsCorrectText() {
        Composition c1 = new Composition();
        c1.setDuration(120);

        Composition c2 = new Composition();
        c2.setDuration(180);

        String result = DisplayHelper.formatDuration(Arrays.asList(c1, c2));
        assertEquals("Duration: 300 sec", result);
    }

    @Test
    public void testFormatDuration_emptyList() {
        String result = DisplayHelper.formatDuration(java.util.Collections.emptyList());
        assertEquals("Duration: 0 sec", result);
    }
}
