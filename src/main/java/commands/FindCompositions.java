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
import java.util.ArrayList;
import java.util.List;

public class FindCompositions {
    private static final Logger logger = LogManager.getLogger(FindCompositions.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    public List<Composition> findInRange(List<Composition> allCompositions) {
        if (allCompositions.isEmpty()) {
            showStyledMessage("У базі немає композицій.");
            return null;
        }

        int[] range = showDurationInputDialog();
        if (range == null) return null;

        int minDuration = range[0];
        int maxDuration = range[1];

        List<Composition> found = new ArrayList<>();
        for (Composition c : allCompositions) {
            int d = c.getDuration();
            if (d >= minDuration && d <= maxDuration) {
                found.add(c);
            }
        }

        if (found.isEmpty()) {
            showStyledMessage("Композицій у заданому діапазоні не знайдено.");
            logger.warn("Пошук: не знайдено композицій між {} і {} сек.", minDuration, maxDuration);
            return null;
        }

        logger.info("Знайдено {} композицій ({} - {} сек).", found.size(), minDuration, maxDuration);
        return found;
    }

    protected int[] showDurationInputDialog() {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle("Фільтр за тривалістю");

        Label label = new Label("Введіть діапазон тривалості (сек):");
        label.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");

        TextField minField = new TextField();
        minField.setPromptText("Мінімум");
        minField.setMaxWidth(120);

        TextField maxField = new TextField();
        maxField.setPromptText("Максимум");
        maxField.setMaxWidth(120);

        HBox inputBox = new HBox(15, minField, maxField);
        inputBox.setAlignment(Pos.CENTER);

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-text-fill: purple;");
        final int[][] result = {null};

        okButton.setOnAction(e -> {
            try {
                int min = Integer.parseInt(minField.getText());
                int max = Integer.parseInt(maxField.getText());
                if (min > max) {
                    showStyledMessage("Мінімум не може бути більшим за максимум.");
                } else if (min < 0) {
                    showStyledMessage("Значення не можуть бути від’ємними.");
                } else {
                    result[0] = new int[]{min, max};
                    dialogStage.close();
                }
            } catch (NumberFormatException ex) {
                errorLogger.error("Невірне введення тривалості: {}", ex.getMessage());
                showStyledMessage("Введіть лише цілі числа.");
            }
        });

        VBox vbox = new VBox(20, label, inputBox, okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(30));
        vbox.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.MEDIUMPURPLE),
                        new Stop(1, Color.HOTPINK)),
                new CornerRadii(15), Insets.EMPTY
        )));

        Scene scene = new Scene(vbox, 420, 220);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return result[0];
    }

    void showStyledMessage(String message) {
        Stage msgStage = new Stage();
        msgStage.initModality(Modality.APPLICATION_MODAL);
        msgStage.setTitle("Повідомлення");

        Label messageLabel = new Label(message);
        messageLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        messageLabel.setWrapText(true);

        Button okButton = new Button("OK");
        okButton.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-text-fill: purple;");
        okButton.setOnAction(e -> msgStage.close());

        VBox vbox = new VBox(20, messageLabel, okButton);
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
        msgStage.setScene(scene);
        msgStage.showAndWait();
    }
}
