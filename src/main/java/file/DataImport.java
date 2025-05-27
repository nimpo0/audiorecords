package file;
import commands.Command;
import composition.Composition;
import database.CompositionBD;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import mainPackage.Menu;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class DataImport implements Command {
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    @Override
    public void execute() {
        Stage stage = Menu.getPrimaryStage();
        FileChooser fileChooser = createFileChooser();
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            try {
                List<Composition> compositions = readCompositionsFromFile(file);
                for (Composition c : compositions) {
                    CompositionBD.insertComposition(
                            c.getName(),
                            c.getStyle(),
                            c.getDuration(),
                            c.getAuthor(),
                            c.getLyrics(),
                            c.getAudioPath()
                    );
                }
                Menu.getInstance().showAlert("Успіх", "Композиції імпортовано успішно!");
            } catch (IOException e) {
                Menu.getInstance().showAlert("Помилка", "Помилка імпорту: " + e.getMessage());
                errorLogger.error("Помилка імпорту: {}", e.getMessage(), e);
            }
        }
    }

    protected FileChooser createFileChooser() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Імпорт композицій з файлу");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON files", "*.json"));
        return chooser;
    }

    protected List<Composition> readCompositionsFromFile(File file) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, new TypeReference<>() {});
    }

    @Override
    public String printInfo() {
        return "Імпорт композицій з файлу json.";
    }
}
