package commands;
import database.CompositionBD;
import javafx.scene.control.Alert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DeleteCompos implements Command {
    private static final Logger logger = LogManager.getLogger(DeleteCompos.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private final String compositionName;

    public DeleteCompos(String compositionName) {
        this.compositionName = compositionName;
    }

    @Override
    public void execute() {
        boolean result;
        try {
            result = CompositionBD.deleteComposition(compositionName);
        } catch (Exception e) {
            errorLogger.error("Помилка при видаленні композиції: {}", e.getMessage(), e);
            showAlert("Помилка", "Сталася помилка під час видалення: " + e.getMessage());
            return;
        }

        if (result) {
            logger.info("Композицію '{}' успішно видалено з бази даних.", compositionName);
            showAlert("Успіх", "Композицію '" + compositionName + "' видалено з бази даних.");
        } else {
            logger.warn("Композицію '{}' не знайдено в базі даних.", compositionName);
            showAlert("Не знайдено", "Композицію не знайдено в базі даних.");
        }
    }

    @Override
    public String printInfo() {
        return "Видалити вказану композицію з бази даних.";
    }

    protected void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
