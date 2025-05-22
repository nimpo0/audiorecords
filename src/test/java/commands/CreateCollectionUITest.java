package commands;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import testutil.BaseFxTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class CreateCollectionUITest extends BaseFxTest {

    private final CreateCollection createCollection = new CreateCollection();

    @Override
    public void start(Stage stage) {
        BorderPane layout = createCollection.createLayout();
        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void shouldDisplayUIElements() {
        TextField field = lookup(".text-field").query();
        assertEquals("Назва колекції", field.getPromptText());
        verifyThat("Створити", hasText("Створити"));
        verifyThat("⬅ Назад до меню", hasText("⬅ Назад до меню"));
    }

    @Test
    void shouldShowAlertIfNameIsEmpty() {
        clickOn("Створити");
        waitForFxEvents();

        verifyThat("Введіть назву колекції.", node -> node.isVisible());
        clickOn("OK");
    }

    @Test
    void shouldShowSuccessAlertAfterValidCreation() {
        clickOn(".text-field").write("Тестова колекція");
        clickOn("Створити");
        waitForFxEvents();

        verifyThat("Колекцію 'Тестова колекція' успішно створено.", node -> node.isVisible());
        clickOn("OK");
    }
}
