package commands;
import composition.Composition;
import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.Test;
import testutil.BaseFxTest;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;
import static org.testfx.api.FxAssert.verifyThat;
import static org.testfx.matcher.control.LabeledMatchers.hasText;
import static org.testfx.util.WaitForAsyncUtils.waitForFxEvents;

public class FindCompositionsUITest extends BaseFxTest {

    private FindCompositions finder = new FindCompositions();

    @Test
    void shouldShowMessageIfDatabaseIsEmpty() throws Exception {
        Platform.runLater(() -> finder.findInRange(new ArrayList<>()));
        Thread.sleep(1000);
        waitForFxEvents();

        verifyThat("У базі немає композицій.", hasText("У базі немає композицій."));
        clickOn("OK");
    }

    @Test
    void shouldShowValidationErrorOnWrongInput() throws Exception {
        List<Composition> compositions = List.of(
                new Composition(1, "Test", "Рок", 200, "Автор", "Текст", "Шлях")
        );

        Platform.runLater(() -> finder.findInRange(compositions));
        Thread.sleep(1000);
        waitForFxEvents();

        verifyThat("Введіть діапазон тривалості (сек):", hasText("Введіть діапазон тривалості (сек):"));

        var fieldsSet = lookup(".text-field").queryAllAs(TextField.class);
        List<TextField> fields = fieldsSet.stream().collect(Collectors.toList());

        clickOn(fields.get(0)).write("abc");
        clickOn(fields.get(1)).write("xyz");
        clickOn("OK");

        Thread.sleep(500);
        waitForFxEvents();

        verifyThat("Введіть лише цілі числа.", hasText("Введіть лише цілі числа."));
        clickOn("OK");
    }

    @Test
    void shouldShowValidationErrorIfMinGreaterThanMax() throws Exception {
        List<Composition> compositions = List.of(
                new Composition(1, "Test", "Рок", 200, "Автор", "Текст", "Шлях")
        );

        Platform.runLater(() -> finder.findInRange(compositions));
        Thread.sleep(1000);
        waitForFxEvents();

        var fieldsSet = lookup(".text-field").queryAllAs(TextField.class);
        List<TextField> fields = fieldsSet.stream().collect(Collectors.toList());

        clickOn(fields.get(0)).write("300");
        clickOn(fields.get(1)).write("100");
        clickOn("OK");

        Thread.sleep(500);
        waitForFxEvents();

        verifyThat("Мінімум не може бути більшим за максимум.", hasText("Мінімум не може бути більшим за максимум."));
        clickOn("OK");
    }

    @Test
    void shouldShowMessageIfNoCompositionsFound() throws Exception {
        List<Composition> compositions = List.of(
                new Composition(1, "Test", "Рок", 90, "Автор", "Текст", "Шлях")
        );

        Platform.runLater(() -> finder.findInRange(compositions));
        Thread.sleep(1000);
        waitForFxEvents();

        var fieldsSet = lookup(".text-field").queryAllAs(TextField.class);
        List<TextField> fields = fieldsSet.stream().collect(Collectors.toList());

        clickOn(fields.get(0)).write("200");
        clickOn(fields.get(1)).write("300");
        clickOn("OK");

        Thread.sleep(500);
        waitForFxEvents();

        verifyThat("Композицій у заданому діапазоні не знайдено.", hasText("Композицій у заданому діапазоні не знайдено."));
        clickOn("OK");
    }

    @Test
    void shouldFindCompositionsInRange() throws Exception {
        List<Composition> compositions = List.of(
                new Composition(1, "Test1", "Рок", 120, "Автор", "Текст", "Шлях"),
                new Composition(2, "Test2", "Поп", 180, "Автор", "Текст", "Шлях")
        );

        final List<Composition>[] found = new List[1];
        Platform.runLater(() -> found[0] = finder.findInRange(compositions));
        Thread.sleep(1000);
        waitForFxEvents();

        var fieldsSet = lookup(".text-field").queryAllAs(TextField.class);
        List<TextField> fields = fieldsSet.stream().collect(Collectors.toList());

        clickOn(fields.get(0)).write("100");
        clickOn(fields.get(1)).write("150");
        clickOn("OK");

        Thread.sleep(500);
        waitForFxEvents();

        assertNotNull(found[0]);
        assertEquals(1, found[0].size());
        assertEquals("Test1", found[0].get(0).getName());
    }
}
