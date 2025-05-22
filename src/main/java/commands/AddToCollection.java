package commands;
import composition.Composition;
import database.CollectionBD;
import database.CollectionCompositionBD;
import database.CompositionBD;
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
import java.util.List;
import java.util.Optional;

public class AddToCollection implements Command {
    private static final Logger logger = LogManager.getLogger(AddToCollection.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private CollectionBD collectionBD = new CollectionBD();
    private CollectionCompositionBD relationBD = new CollectionCompositionBD();

    private final String collectionName;

    public AddToCollection(String collectionName) {
        this.collectionName = collectionName;
    }

    public AddToCollection(String collectionName, CollectionBD collectionBD, CollectionCompositionBD relationBD) {
        this.collectionName = collectionName;
        this.collectionBD = collectionBD;
        this.relationBD = relationBD;
    }

    @Override
    public void execute() {
        try {
            int collectionId = relationBD.getCollectionIdByName(collectionName);
            if (collectionId == -1) {
                showStyledMessage("Колекція не знайдена", "Колекції з назвою '" + collectionName + "' не існує.");
                errorLogger.error("Collection '{}' not found.", collectionName);
                return;
            }

            List<Composition> allCompositions = CompositionBD.getAllCompositions();
            List<Composition> currentInCollection = collectionBD.getCompositionsForCollection(collectionName);

            List<Composition> available = allCompositions.stream()
                    .filter(c -> currentInCollection.stream().noneMatch(col -> col.getName().equalsIgnoreCase(c.getName())))
                    .toList();

            if (available.isEmpty()) {
                showStyledMessage("Немає доступних композицій", "Усі композиції вже містяться в цій колекції.");
                return;
            }

            List<String> availableNames = available.stream().map(Composition::getName).toList();

            Optional<String> compositionChoice = showChoiceDialog(
                    "Додати композицію",
                    "Виберіть композицію для додавання до колекції '" + collectionName + "':",
                    availableNames
            );

            if (compositionChoice.isEmpty()) return;
            String compositionName = compositionChoice.get().trim();

            if (currentInCollection.stream().anyMatch(c -> c.getName().equalsIgnoreCase(compositionName))) {
                showStyledMessage("Композиція вже додана", "Ця композиція вже міститься в обраній колекції.");
                return;
            }

            relationBD.addCompositionToCollection(compositionName, collectionName);
            logger.info("Composition '{}' added to collection '{}'.", compositionName, collectionName);
            showStyledMessage("Успішно", "Композиція '" + compositionName + "' додана до колекції '" + collectionName + "'.");

        } catch (Exception e) {
            errorLogger.error("Error while adding to collection: {}", e.getMessage(), e);
            showStyledMessage("Помилка", "Сталася помилка: " + e.getMessage());
        }
    }

    @Override
    public String printInfo() {
        return "Add a composition to a specific collection.";
    }

    protected Optional<String> showChoiceDialog(String title, String header, List<String> choices) {
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.setTitle(title);

        Label headerLabel = new Label(header);
        headerLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold;");
        headerLabel.setWrapText(true);

        ChoiceBox<String> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(choices);
        choiceBox.setValue(choices.get(0));

        Button okButton = new Button("ОК");
        okButton.setStyle("-fx-background-radius: 15; -fx-background-color: white; -fx-text-fill: purple;");
        final boolean[] confirmed = {false};

        okButton.setOnAction(e -> {
            if (choiceBox.getValue() != null) {
                confirmed[0] = true;
                dialogStage.close();
            }
        });

        VBox vbox = new VBox(20, headerLabel, choiceBox, okButton);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(25));
        vbox.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.MEDIUMPURPLE),
                        new Stop(1, Color.HOTPINK)),
                new CornerRadii(15), Insets.EMPTY
        )));

        Scene scene = new Scene(vbox, 400, 200);
        dialogStage.setScene(scene);
        dialogStage.showAndWait();

        return confirmed[0] ? Optional.of(choiceBox.getValue()) : Optional.empty();
    }

    protected void showStyledMessage(String title, String content) {
        Stage msgStage = new Stage();
        msgStage.initModality(Modality.APPLICATION_MODAL);
        msgStage.setTitle(title);

        Label messageLabel = new Label(content);
        messageLabel.setStyle("-fx-text-fill: white; -fx-font-size: 16px;");
        messageLabel.setWrapText(true);

        Button okButton = new Button("ОК");
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

        Scene scene = new Scene(vbox, 420, 200);
        msgStage.setScene(scene);
        msgStage.showAndWait();
    }


}
