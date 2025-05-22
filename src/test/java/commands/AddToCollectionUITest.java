package commands;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import testutil.BaseFxTest;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class AddToCollectionUITest extends BaseFxTest {

    private TestableAddToCollection testable;

    static class TestableAddToCollection extends AddToCollection {
        private Optional<String> lastChoice = Optional.empty();

        public TestableAddToCollection(String collectionName) {
            super(collectionName);
        }

        public Optional<String> callShowChoiceDialog(List<String> choices) {
            lastChoice = showChoiceDialog("Вибір стилю", "Оберіть стиль:", choices);
            return lastChoice;
        }

        public void callShowStyledMessage(String title, String content) {
            showStyledMessage(title, content);
        }

        public Optional<String> getLastChoice() {
            return lastChoice;
        }
    }

    @BeforeEach
    void setUp() {
        testable = new TestableAddToCollection("TestCollection");
    }

    @Test
    void testShowChoiceDialog(FxRobot robot) throws Exception {
        List<String> options = List.of("Jazz", "Rock", "Pop");

        Platform.runLater(() -> testable.callShowChoiceDialog(options));

        robot.sleep(500);

        Window dialogWindow = listWindows()
                .stream()
                .filter(Window::isShowing)
                .filter(w -> w instanceof Stage && ((Stage) w).getTitle().contains("Вибір стилю"))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Діалог не знайдено"));

        robot.targetWindow(dialogWindow);

        robot.clickOn(".choice-box");
        robot.clickOn("Rock");
        robot.clickOn("ОК");

        for (int i = 0; i < 20; i++) {
            if (!dialogWindow.isShowing()) break;
            Thread.sleep(100);
        }

        assertFalse(dialogWindow.isShowing(), "Діалог все ще відкритий");

        Optional<String> selected = testable.getLastChoice();
        assertTrue(selected.isPresent(), "Очікуємо вибір");
        assertEquals("Rock", selected.get());
    }

    @Test
    void testShowStyledMessage(FxRobot robot) throws Exception {
        String title = "Повідомлення";
        String message = "Це тестове повідомлення";

        runOnFxThreadAndWait(() -> {
            Platform.runLater(() -> testable.callShowStyledMessage(title, message));
        });

        robot.sleep(1000);

        Label label = robot.lookup((Label l) -> l.getText().equals(message)).query();
        assertNotNull(label, "Повідомлення не знайдено на сцені");

        robot.clickOn("ОК");
    }
}
