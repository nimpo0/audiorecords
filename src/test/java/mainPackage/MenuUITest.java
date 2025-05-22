package mainPackage;
import commands.Command;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;
import javafx.stage.Stage;
import testutil.BaseFxTest;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.assertions.api.Assertions.assertThat;
import static org.testfx.util.NodeQueryUtils.hasText;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class MenuUITest extends BaseFxTest {
    private Menu menu;

    @Override
    public void start(Stage stage) {
        menu = new Menu();
        menu.start(stage);
    }

    @Test
    void shouldShowWelcomeScreen() {
        verifyThat("🚀 Start", hasText("🚀 Start"));
        verifyThat("🎶 Welcome to Music Collection!", node -> node.isVisible());
    }

    @Test
    void shouldOpenMainMenuOnStartClick() {
        clickOn("🚀 Start");
        waitForFxEvents();

        verifyThat("➕ Додати композицію", hasText("➕ Додати композицію"));
        verifyThat("🆕 Створити колекцію", hasText("🆕 Створити колекцію"));
    }

    @Test
    public void welcomeScreenShouldContainWelcomeTextAndStartButton() {
        waitForFxEvents();

        Text welcomeText = lookup(".text").query();
        String welcomeStr = welcomeText.getText();

        Button startButton = lookup(".button").queryButton();
        String startBtnText = startButton.getText();
        String startBtnTextNoEmoji = startBtnText.replaceAll("[^\\p{L}\\p{N}\\s,.!]", "");
        assertThat(startBtnTextNoEmoji).isEqualTo(" Start");

        clickOn(startButton);
        waitForFxEvents();

        Button anyMainButton = lookup(b -> b instanceof Button && ((Button) b).getText().contains("композицію")).query();
        assertThat(anyMainButton).isNotNull();
    }

    @Test
    void testCreateStyledButton_executesCommandOnClick() {
        Command mockCommand = mock(Command.class);
        String commandKey = "mockCommand";
        menu.commandMap.put(commandKey, mockCommand);

        Button button = menu.createStyledButton("Test Button", commandKey);
        javafx.application.Platform.runLater(() -> {
            if (menu.getPrimaryStage() != null && menu.getPrimaryStage().getScene() != null) {
                ((javafx.scene.layout.Pane) menu.getPrimaryStage().getScene().getRoot()).getChildren().add(button);
            }
        });
        waitForFxEvents();
        clickOn(button);
        verify(mockCommand, times(1)).execute();
    }

    @Test
    void testCreateStyledButton_showsAlertWhenCommandNotFound() {
        String missingCommandKey = "noSuchCommand";
        Button button = menu.createStyledButton("Missing Command Button", missingCommandKey);

        javafx.application.Platform.runLater(() -> {
            if (menu.getPrimaryStage() != null && menu.getPrimaryStage().getScene() != null) {
                ((javafx.scene.layout.Pane) menu.getPrimaryStage().getScene().getRoot()).getChildren().add(button);
            }
        });
        waitForFxEvents();
        clickOn(button);
        waitForFxEvents();

        verifyThat("Команда не знайдена.", hasText("Команда не знайдена."));
        clickOn("OK");
    }

    @Test
    void testCreateStyledButton_showsAlertOnCommandException() {
        Command failingCommand = mock(Command.class);
        doThrow(new RuntimeException("fail")).when(failingCommand).execute();

        String commandKey = "failingCommand";
        menu.commandMap.put(commandKey, failingCommand);

        Button button = menu.createStyledButton("Failing Command", commandKey);

        javafx.application.Platform.runLater(() -> {
            if (menu.getPrimaryStage() != null && menu.getPrimaryStage().getScene() != null) {
                ((javafx.scene.layout.Pane) menu.getPrimaryStage().getScene().getRoot()).getChildren().add(button);
            }
        });
        waitForFxEvents();

        clickOn(button);
        waitForFxEvents();

        verifyThat("Помилка при виконанні команди: fail", hasText("Помилка при виконанні команди: fail"));
        clickOn("OK");
    }
}
