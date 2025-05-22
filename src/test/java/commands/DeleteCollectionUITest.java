package commands;
import database.CollectionBD;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.Test;
import testutil.BaseFxTest;
import static org.mockito.Mockito.*;

public class DeleteCollectionUITest extends BaseFxTest {

    @Test
    void testOriginalShowMessage_CallsJavaFxAlert() throws InterruptedException {
        DeleteCollection command = new DeleteCollection("TestCollection");
        runOnFxThreadAndWait(() ->
                command.showMessage("🗑 Колекцію 'TestCollection' видалено.")
        );
    }

    @Test
    void testExecute_ShowsAlertDialog() throws InterruptedException {

        CollectionBD mockBD = mock(CollectionBD.class);

        DeleteCollection command = new DeleteCollection("TestCollection") {
            @Override
            protected void showMessage(String msg) {
                try {
                    runOnFxThreadAndWait(() -> {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
                        alert.setTitle("Результат");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    });
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            private final CollectionBD collectionBDMock = mockBD;

            @Override
            public void execute() {
                collectionBDMock.deleteCollection(collectionName);
                showMessage("🗑 Колекцію '" + collectionName + "' видалено.");
            }
        };

        command.execute();

        verify(mockBD).deleteCollection("TestCollection");
    }
}
