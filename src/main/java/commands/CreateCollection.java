package commands;
import database.CollectionBD;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import mainPackage.Menu;
import java.util.Objects;

public class CreateCollection implements Command {

    private final CollectionBD collectionBD;
    private TextField collectionNameField;

    public CreateCollection() {
        this.collectionBD = new CollectionBD();
    }

    @Override
    public void execute() {
        BorderPane layout = createLayout();
        Menu.getPrimaryStage().getScene().setRoot(layout);
    }

    @Override
    public String printInfo() {
        return "Створити нову колекцію.";
    }

    public void handleCollectionCreation(String collectionName) {
        String name = collectionName.trim();
        if (name.isEmpty()) {
            showAlert("Введіть назву колекції.");
            return;
        }

        collectionBD.insertCollection(name);
        showAlert("Колекцію '" + name + "' успішно створено.");
        if (collectionNameField != null) {
            collectionNameField.clear();
        }
    }

    public BorderPane createLayout() {
        Label title = new Label("Створити нову колекцію");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);

        ImageView icon = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResource("/girl3.png")).toExternalForm())
        );
        icon.setFitWidth(120);
        icon.setFitHeight(140);

        VBox titleBox = new VBox(10, icon, title);
        titleBox.setAlignment(Pos.CENTER);

        collectionNameField = new TextField();
        collectionNameField.setPromptText("Назва колекції");
        collectionNameField.setMaxWidth(300);

        Button createButton = new Button("Створити");
        createButton.setStyle("-fx-background-radius: 15; -fx-font-size: 14px; -fx-background-color: #ffffff; -fx-text-fill: #800080;");
        createButton.setPrefWidth(150);
        createButton.setPrefHeight(40);
        createButton.setOnAction(e -> handleCollectionCreation(collectionNameField.getText()));

        Button backButton = new Button("⬅ Назад до меню");
        backButton.setStyle("-fx-background-radius: 15; -fx-font-size: 14px; -fx-background-color: #ffffff; -fx-text-fill: #800080;");
        backButton.setPrefWidth(150);
        backButton.setPrefHeight(40);
        backButton.setOnAction(e -> Menu.getInstance().showMainMenu());

        VBox form = new VBox(20, titleBox, collectionNameField, createButton, backButton);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(30));
        form.setMaxWidth(400);

        BorderPane layout = new BorderPane();
        layout.setCenter(form);
        layout.setPadding(new Insets(20));
        layout.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 1, 1, 0, true,
                        javafx.scene.paint.CycleMethod.NO_CYCLE,
                        new javafx.scene.paint.Stop(0, Color.LAVENDERBLUSH),
                        new javafx.scene.paint.Stop(1, Color.web("#916DB4"))),
                new CornerRadii(10),
                Insets.EMPTY)));

        return layout;
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Результат");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
