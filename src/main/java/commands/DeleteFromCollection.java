package commands;

import database.CollectionCompositionBD;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteFromCollection implements Command {
    private static final Logger logger = LogManager.getLogger(DeleteFromCollection.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private final CollectionCompositionBD relationBD = new CollectionCompositionBD();

    protected final String compositionName;
    protected final String collectionName;

    public DeleteFromCollection(String compositionName, String collectionName) {
        this.compositionName = compositionName;
        this.collectionName = collectionName;
    }

    @Override
    public void execute() {
        try {
            int collectionId = relationBD.getCollectionIdByName(collectionName);
            if (collectionId == -1) {
                showAlert("Колекція не знайдена", "Колекція '" + collectionName + "' не існує.");
                return;
            }

            relationBD.removeCompositionFromCollection(compositionName, collectionName);
            logger.info("Композицію '{}' видалено з колекції '{}'.", compositionName, collectionName);
            showAlert("Успішно", "Композицію '" + compositionName + "' видалено з колекції '" + collectionName + "'.");

        } catch (Exception e) {
            errorLogger.error("Помилка при видаленні з колекції: {}", e.getMessage(), e);
            showAlert("Помилка", "Помилка при видаленні: " + e.getMessage());
        }
    }

    @Override
    public String printInfo() {
        return "Видалити композицію з колекції.";
    }

    protected void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
