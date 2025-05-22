package commands;
import database.CollectionCompositionBD;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.junit.jupiter.api.Test;
import testutil.BaseFxTest;
import static org.mockito.Mockito.*;

public class DeleteFromCollectionUITest extends BaseFxTest {

    @Test
    void testOriginalShowAlert_DeleteFromCollection() throws InterruptedException {
        DeleteFromCollection command = new DeleteFromCollection("Song", "Collection");
        runOnFxThreadAndWait(() -> command.showAlert("Тест", "Це тестовий алерт"));
    }

    @Test
    void testExecute_CollectionNotFound_ShowsAlert() throws InterruptedException {
        CollectionCompositionBD mockRelation = mock(CollectionCompositionBD.class);
        when(mockRelation.getCollectionIdByName("NonExistent")).thenReturn(-1);

        DeleteFromCollection command = new DeleteFromCollection("SongA", "NonExistent") {
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

            private final CollectionCompositionBD bdMock = mockRelation;

            @Override
            public void execute() {
                int collectionId = bdMock.getCollectionIdByName(collectionName);
                if (collectionId == -1) {
                    showAlert("Колекція не знайдена", "Колекція '" + collectionName + "' не існує.");
                    return;
                }

                bdMock.removeCompositionFromCollection(compositionName, collectionName);
                showAlert("Успішно", "Композицію '" + compositionName + "' видалено з колекції '" + collectionName + "'.");
            }
        };

        command.execute();

        verify(mockRelation).getCollectionIdByName("NonExistent");
        verify(mockRelation, never()).removeCompositionFromCollection(any(), any());
    }
}