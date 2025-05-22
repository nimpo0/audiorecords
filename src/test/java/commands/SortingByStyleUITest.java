package commands;
import composition.Composition;
import javafx.application.Platform;
import org.junit.jupiter.api.Test;
import testutil.BaseFxTest;
import java.util.ArrayList;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class SortingByStyleUITest extends BaseFxTest {

    private SortingByStyle sorter = new SortingByStyle();

    @Test
    void shouldDisplayEmptyDatabaseMessage() throws Exception {
        Platform.runLater(() -> sorter.sort(new ArrayList<>()));
        Thread.sleep(1000);
        waitForFxEvents();

        verifyThat("У базі даних немає композицій.", node -> node.isVisible());
        clickOn("OK");
    }

    @Test
    void shouldDisplayChoiceDialogAndSelectOption() throws Exception {
        ArrayList<Composition> mockList = new ArrayList<>();
        mockList.add(new Composition(1, "Test", "Рок", 200, "Автор", "Текст", "Шлях"));

        Platform.runLater(() -> sorter.sort(mockList));
        Thread.sleep(1000);
        waitForFxEvents();

        verifyThat("Оберіть порядок сортування:", node -> node.isVisible());

        clickOn(".combo-box");
        clickOn("У зворотному порядку");
        clickOn("OK");
    }

}