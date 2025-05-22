package commands;

import database.CompositionBD;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import testutil.BaseFxTest;

import static org.mockito.Mockito.*;

public class DeleteComposUITest extends BaseFxTest {

    @Test
    void testOriginalShowAlert_DeleteCompos() throws InterruptedException {
        DeleteCompos command = new DeleteCompos("SomeSong");
        runOnFxThreadAndWait(() -> command.showAlert("Тест", "Це тестовий алерт"));
    }

    @Test
    void testExecute_CompositionDeletedSuccessfully_ShowsSuccessAlert() throws InterruptedException {
        try (MockedStatic<CompositionBD> mockStatic = mockStatic(CompositionBD.class)) {
            mockStatic.when(() -> CompositionBD.deleteComposition("Song1")).thenReturn(true);

            DeleteCompos command = new DeleteCompos("Song1") {
                @Override
                protected void showAlert(String title, String content) {
                    try {
                        runOnFxThreadAndWait(() -> {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.OK);
                            alert.setTitle(title);
                            alert.setHeaderText(null);
                            alert.showAndWait();
                        });
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            };

            command.execute();

            mockStatic.verify(() -> CompositionBD.deleteComposition("Song1"));
        }
    }
}
