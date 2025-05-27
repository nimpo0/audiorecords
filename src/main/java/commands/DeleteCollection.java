package commands;
import database.CollectionBD;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteCollection implements Command {
    private static final Logger logger = LogManager.getLogger(DeleteCollection.class);

    private final CollectionBD collectionBD = new CollectionBD();
    protected final String collectionName;

    public DeleteCollection(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public void execute() {
        if (collectionName == null || collectionName.trim().isEmpty()) return;

        collectionBD.deleteCollection(collectionName);
        logger.info("Колекцію '{}' успішно видалено з бази даних.", collectionName);
        showMessage("🗑 Колекцію '" + collectionName + "' видалено.");
        new DisplayCollection().execute();
    }

    @Override
    public String printInfo() {
        return "Видалити колекцію.";
    }

    protected void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Результат");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.show();
    }
}
