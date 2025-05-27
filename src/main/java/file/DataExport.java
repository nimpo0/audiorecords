package file;
import commands.Command;
import composition.Composition;
import database.CompositionBD;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainPackage.Menu;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataExport implements Command {
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    @Override
    public void execute() {
        Stage stage = Menu.getPrimaryStage();
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {
            try {
                List<Composition> compositions = CompositionBD.getAllCompositions();
                writeCompositionsToFile(file, compositions);
                Menu.getInstance().showAlert("Успіх", "Композиції експортовано успішно!");
            } catch (IOException e) {
                Menu.getInstance().showAlert("Помилка", "Помилка експорту: " + e.getMessage());
                errorLogger.error("Помилка експорту: {}", e.getMessage(), e);
            }
        }
    }

    protected FileChooser createFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Експорт композицій у файл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        return fileChooser;
    }

    protected void writeCompositionsToFile(File file, List<Composition> compositions) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.writeValue(file, compositions);
    }

    @Override
    public String printInfo() {
        return "Експорт всіх композицій у файл json.";
    }
}
