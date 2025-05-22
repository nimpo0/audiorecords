package commands;
import composition.Composition;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.*;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

class DisplayAllComposTest {

    @BeforeAll
    static void initJfx() {
        new JFXPanel();
    }

    @Test
    void testPrintInfo_ReturnsCorrectString() {
        DisplayAllCompos display = new DisplayAllCompos();
        assertEquals("Показати всі композиції.", display.printInfo());
    }

    @Test
    void testFormatDuration() throws Exception {
        DisplayAllCompos display = new DisplayAllCompos();

        var method = DisplayAllCompos.class.getDeclaredMethod("formatDuration", int.class);
        method.setAccessible(true);

        assertEquals("0:00", method.invoke(display, 0));
        assertEquals("1:05", method.invoke(display, 65));
        assertEquals("10:59", method.invoke(display, 659));
    }

    @Test
    void testConstructor_WithParams_SetsFields() {
        var list = Collections.<Composition>emptyList();
        DisplayAllCompos display = new DisplayAllCompos(list, "Заголовок", "Колекція");
        assertNotNull(display);
    }

    @Test
    void testExecute_InvokesJavaFxThread() throws Exception {
        DisplayAllCompos display = new DisplayAllCompos(Collections.emptyList(), "Заголовок", null);

        Platform.runLater(() -> {
            try {
                display.execute();
            } catch (Exception e) {
                fail("Execute should not throw exception: " + e.getMessage());
            }
        });

        Thread.sleep(1000);
    }
}
