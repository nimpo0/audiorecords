package commands;
import composition.Composition;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class SortingByStyleTest {

    @Test
    void sort_shouldReturnNull_whenListIsEmpty() {
        SortingByStyle sorter = new SortingByStyle();
        List<Composition> emptyList = new ArrayList<>();
        List<Composition> result = sorter.sort(emptyList, "По алфавіту");
        assertNull(result);
    }

    @Test
    void sort_shouldSortAlphabetically_whenSelected() {
        SortingByStyle sorter = new SortingByStyle();
        List<Composition> list = new ArrayList<>();
        list.add(new Composition(1, "B", "Rock", 100, "Author", "Lyrics", "Path"));
        list.add(new Composition(2, "A", "Jazz", 200, "Author", "Lyrics", "Path"));
        list.add(new Composition(3, "C", "Electronic", 300, "Author", "Lyrics", "Path"));

        List<Composition> result = sorter.sort(list, "По алфавіту");

        assertEquals("Electronic", result.get(0).getStyle());
        assertEquals("Jazz", result.get(1).getStyle());
        assertEquals("Rock", result.get(2).getStyle());
    }

    @Test
    void sort_shouldSortInReverseOrder_whenSelected() {
        SortingByStyle sorter = new SortingByStyle();
        List<Composition> list = new ArrayList<>();
        list.add(new Composition(1, "A", "Electronic", 300, "Author", "Lyrics", "Path"));
        list.add(new Composition(2, "B", "Jazz", 200, "Author", "Lyrics", "Path"));
        list.add(new Composition(3, "C", "Rock", 100, "Author", "Lyrics", "Path"));

        List<Composition> result = sorter.sort(list, "У зворотному порядку");

        assertEquals("Rock", result.get(0).getStyle());
        assertEquals("Jazz", result.get(1).getStyle());
        assertEquals("Electronic", result.get(2).getStyle());
    }

    @Test
    void sort_shouldReturnNull_whenSelectedIsNull() {
        SortingByStyle sorter = new SortingByStyle();
        List<Composition> list = new ArrayList<>();
        list.add(new Composition(1, "X", "Pop", 150, "Author", "Lyrics", "Path"));

        List<Composition> result = sorter.sort(list, null);

        assertNull(result);
    }
}
