package composition;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompositionTest {

    @Test
    void testEmptyConstructor() {
        Composition composition = new Composition();
        assertNull(composition.getName());
        assertNull(composition.getStyle());
        assertEquals(0, composition.getDuration());
        assertNull(composition.getAuthor());
        assertNull(composition.getLyrics());
        assertNull(composition.getAudioPath());
    }

    @Test
    void testCompositionFields() {
        Composition composition = new Composition(1, "Title", "Jazz", 200, "Composer", "Lyrics", "/music/audio.mp3");

        assertEquals("Title", composition.getName());
        assertEquals("Jazz", composition.getStyle());
        assertEquals(200, composition.getDuration());
        assertEquals("Composer", composition.getAuthor());
        assertEquals("Lyrics", composition.getLyrics());
        assertEquals("/music/audio.mp3", composition.getAudioPath());
    }

    @Test
    void testToString() {
        Composition composition = new Composition(1, "Title", "Jazz", 200, "Composer", "Lyrics", "/music/audio.mp3");
        String expected = "Composition{id=1, name='Title', style='Jazz', duration=200, author='Composer', lyrics='Lyrics', audioPath='/music/audio.mp3'}";
        assertEquals(expected, composition.toString());
    }
}
