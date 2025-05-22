package mainPackage;
import commands.Command;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenuTest extends ApplicationTest {

    private Menu menu;

    @Override
    public void start(Stage stage) {
        menu = new Menu();
        menu.start(stage);
    }

    @Test
    void testGetInstanceReturnsMenuInstance() {
        Menu instance = Menu.getInstance();
        assertNotNull(instance, "Menu.getInstance() should not be null after start");
        assertSame(instance, menu, "Menu.getInstance() should return the same instance started");
    }

    @Test
    void testInitializeCommands() {
        menu.initializeCommands();
        assertNotNull(menu.commandMap.get("addCompos"), "addCompos command should be initialized");
        assertNotNull(menu.commandMap.get("displayAll"), "displayAll command should be initialized");
        assertNotNull(menu.commandMap.get("createCollection"), "createCollection command should be initialized");
        assertNotNull(menu.commandMap.get("displayCollection"), "displayCollection command should be initialized");
        assertNotNull(menu.commandMap.get("criticalError"), "criticalError command should be initialized");
    }

    @Test
    void testButtonClickExecutesCommand() {
        Command mockCommand = mock(Command.class);
        String commandKey = "mockKey";
        menu.commandMap.put(commandKey, mockCommand);

        Button btn = menu.createStyledButton("Test Button", commandKey);

        javafx.application.Platform.runLater(() -> {
            if (menu.getPrimaryStage() != null && menu.getPrimaryStage().getScene() != null) {
                ((javafx.scene.layout.Pane) menu.getPrimaryStage().getScene().getRoot()).getChildren().add(btn);
            }
        });

        sleep(200);
        clickOn(btn);
        verify(mockCommand, times(1)).execute();
    }
}
