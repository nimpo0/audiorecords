package commands;
import composition.Composition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Comparator;
import java.util.List;

public class SortingByStyle {
    private static final Logger logger = LogManager.getLogger(SortingByStyle.class);

    public List<Composition> sort(List<Composition> compositions) {
        if (compositions.isEmpty()) {
            showStyledMessage("У базі даних немає композицій.");
            return null;
        }

        String selected = showStyledChoiceDialog();
        return sort(compositions, selected);
    }

    public List<Composition> sort(List<Composition> compositions, String selected) {
        if (compositions == null || compositions.isEmpty()) {
            return null;
        }
        if (selected == null) return null;

        if (selected.equals("По алфавіту")) {
            compositions.sort(Comparator.comparing(Composition::getStyle));
            logger.info("Сортування за стилем по алфавіту.");
        } else {
            compositions.sort(Comparator.comparing(Composition::getStyle).reversed());
            logger.info("Сортування за стилем у зворотному порядку.");
        }

        return compositions;
    }

    protected void showStyledMessage(String message) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Повідомлення");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        messageLabel.setWrapText(true);

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-text-fill: purple; -fx-font-size: 14px;");
        okButton.setOnAction(e -> dialogStage.close());

        VBox vbox = new VBox(20, messageLabel, okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        vbox.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.MEDIUMPURPLE),
                        new Stop(1, Color.HOTPINK)),
                new CornerRadii(15), Insets.EMPTY
        )));

        Scene scene = new Scene(vbox, 400, 200);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();
    }

    protected String showStyledChoiceDialog() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Сортування за стилем");

        Label promptLabel = new Label("Оберіть порядок сортування:");
        promptLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        ComboBox<String> comboBox = new ComboBox<>();
        comboBox.getItems().addAll("По алфавіту", "У зворотному порядку");
        comboBox.setValue("По алфавіту");

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-text-fill: purple;");
        final String[] result = {null};
        okButton.setOnAction(e -> {
            result[0] = comboBox.getValue();
            dialogStage.close();
        });

        VBox vbox = new VBox(15, promptLabel, comboBox, okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        vbox.setBackground(new Background(new BackgroundFill(
                new LinearGradient(1, 0, 0, 1, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.PLUM),
                        new Stop(1, Color.MEDIUMPURPLE)),
                new CornerRadii(15), Insets.EMPTY
        )));

        Scene scene = new Scene(vbox, 400, 200);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return result[0];
    }
}
