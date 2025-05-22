package commands;
import database.CollectionBD;
import javafx.scene.control.Alert;

public class DeleteCollection implements Command {
    private final CollectionBD collectionBD = new CollectionBD();
    protected final String collectionName;

    public DeleteCollection(String collectionName) {
        this.collectionName = collectionName;
    }

    @Override
    public void execute() {
        if (collectionName == null || collectionName.trim().isEmpty()) return;

        collectionBD.deleteCollection(collectionName);
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
