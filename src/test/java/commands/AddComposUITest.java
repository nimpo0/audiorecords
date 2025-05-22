package commands;
import database.CompositionBD;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import mainPackage.Menu;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import testutil.BaseFxTest;
import testutil.TestableAddCompos;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AddComposUITest extends BaseFxTest {

    @Test
    public void testExecuteCreatesUI() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(new BorderPane()));
                Menu dummyMenu = new Menu();
                dummyMenu.setPrimaryStage(stage);
                Menu.instance = dummyMenu;

                TestableAddCompos addCompos = new TestableAddCompos();
                addCompos.execute();

                assertNotNull(stage.getScene());
                assertInstanceOf(BorderPane.class, stage.getScene().getRoot());
            } catch (Exception e) {
                fail("Exception під час виконання: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testSubmitButtonInsertsComposition() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try (MockedStatic<CompositionBD> mockedDB = mockStatic(CompositionBD.class)) {
                mockedDB.when(() -> CompositionBD.insertComposition(
                                anyString(), anyString(), anyInt(), anyString(), anyString(), anyString()))
                        .thenReturn(123);

                TestableAddCompos addCompos = new TestableAddCompos();
                BorderPane layout = addCompos.createLayout();
                VBox form = (VBox) layout.getCenter();

                ((TextField) form.getChildren().get(1)).setText("Пісня");
                ((TextField) form.getChildren().get(2)).setText("Автор");
                ((TextField) form.getChildren().get(3)).setText("Рок");
                ((TextField) form.getChildren().get(4)).setText("180");
                ((TextArea) form.getChildren().get(5)).setText("Слова пісні");
                ((TextField) ((javafx.scene.layout.HBox) form.getChildren().get(6)).getChildren().get(0))
                        .setText("file:/audio.mp3");

                Button submitButton = (Button) form.getChildren().get(7);
                submitButton.fire();

                mockedDB.verify(() -> CompositionBD.insertComposition(
                        eq("Пісня"), eq("Рок"), eq(180), eq("Автор"), eq("Слова пісні"), eq("file:/audio.mp3")
                ));
            } catch (Exception e) {
                fail("Exception: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testSubmitButtonShowsAlertIfFieldsEmpty() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                TestableAddCompos addCompos = new TestableAddCompos();
                BorderPane layout = addCompos.createLayout();
                VBox form = (VBox) layout.getCenter();

                ((TextField) form.getChildren().get(1)).setText("");
                ((TextField) form.getChildren().get(2)).setText("");
                ((TextField) form.getChildren().get(3)).setText("");
                ((TextField) form.getChildren().get(4)).setText("");
                ((TextArea) form.getChildren().get(5)).setText("");
                ((TextField) ((HBox) form.getChildren().get(6)).getChildren().get(0)).setText("");

                Button submitButton = (Button) form.getChildren().get(7);
                submitButton.fire();

                assertEquals("Усі поля мають бути заповнені.", addCompos.lastAlertMessage);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testSubmitButtonShowsAlertIfDurationInvalid() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                TestableAddCompos addCompos = new TestableAddCompos();
                BorderPane layout = addCompos.createLayout();
                VBox form = (VBox) layout.getCenter();

                ((TextField) form.getChildren().get(1)).setText("Пісня");
                ((TextField) form.getChildren().get(2)).setText("Автор");
                ((TextField) form.getChildren().get(3)).setText("Рок");
                ((TextField) form.getChildren().get(4)).setText("-10");
                ((TextArea) form.getChildren().get(5)).setText("Текст");
                ((TextField) ((HBox) form.getChildren().get(6)).getChildren().get(0)).setText("file:/audio.mp3");

                Button submitButton = (Button) form.getChildren().get(7);
                submitButton.fire();

                assertEquals("Тривалість має бути додатнім числом.", addCompos.lastAlertMessage);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testSubmitShowsAlertWhenInsertFails() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try (MockedStatic<CompositionBD> mockedDB = mockStatic(CompositionBD.class)) {
                mockedDB.when(() -> CompositionBD.insertComposition(
                                anyString(), anyString(), anyInt(), anyString(), anyString(), anyString()))
                        .thenReturn(-1);

                TestableAddCompos addCompos = new TestableAddCompos();
                BorderPane layout = addCompos.createLayout();
                VBox form = (VBox) layout.getCenter();

                ((TextField) form.getChildren().get(1)).setText("Пісня");
                ((TextField) form.getChildren().get(2)).setText("Автор");
                ((TextField) form.getChildren().get(3)).setText("Рок");
                ((TextField) form.getChildren().get(4)).setText("120");
                ((TextArea) form.getChildren().get(5)).setText("Текст");
                ((TextField) ((HBox) form.getChildren().get(6)).getChildren().get(0)).setText("file:/audio.mp3");

                Button submitButton = (Button) form.getChildren().get(7);
                submitButton.fire();

                assertEquals("Не вдалося додати композицію.", addCompos.lastAlertMessage);
            } finally {
                latch.countDown();
            }
        });

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }


    @Test
    public void testSubmitShowsAlertWhenNumberFormatException() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try {
                TestableAddCompos addCompos = new TestableAddCompos();
                BorderPane layout = addCompos.createLayout();
                VBox form = (VBox) layout.getCenter();

                ((TextField) form.getChildren().get(1)).setText("Пісня");
                ((TextField) form.getChildren().get(2)).setText("Автор");
                ((TextField) form.getChildren().get(3)).setText("Рок");
                ((TextField) form.getChildren().get(4)).setText("НЕЧИСЛО");
                ((TextArea) form.getChildren().get(5)).setText("Текст");
                ((TextField) ((HBox) form.getChildren().get(6)).getChildren().get(0)).setText("file:/audio.mp3");

                Button submitButton = (Button) form.getChildren().get(7);
                submitButton.fire();

                assertEquals("Некоректне число в полі тривалості.", addCompos.lastAlertMessage);
            } finally {
                latch.countDown();
            }
        });

        assertFalse(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testSubmitShowsAlertWhenExceptionThrown() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        Platform.runLater(() -> {
            try (MockedStatic<CompositionBD> mockedDB = mockStatic(CompositionBD.class)) {
                mockedDB.when(() -> CompositionBD.insertComposition(
                                anyString(), anyString(), anyInt(), anyString(), anyString(), anyString()))
                        .thenThrow(new RuntimeException("DB error"));

                TestableAddCompos addCompos = new TestableAddCompos();
                BorderPane layout = addCompos.createLayout();
                VBox form = (VBox) layout.getCenter();

                ((TextField) form.getChildren().get(1)).setText("Пісня");
                ((TextField) form.getChildren().get(2)).setText("Автор");
                ((TextField) form.getChildren().get(3)).setText("Рок");
                ((TextField) form.getChildren().get(4)).setText("120");
                ((TextArea) form.getChildren().get(5)).setText("Текст");
                ((TextField) ((HBox) form.getChildren().get(6)).getChildren().get(0)).setText("file:/audio.mp3");

                Button submitButton = (Button) form.getChildren().get(7);
                submitButton.fire();

                assertEquals("Виникла помилка при додаванні композиції.", addCompos.lastAlertMessage);
            } finally {
                latch.countDown();
            }
        });

        assertFalse(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testPrintInfo() {
        TestableAddCompos addCompos = new TestableAddCompos();
        String info = addCompos.printInfo();
        assertEquals("Add a new composition.", info);
    }

}
