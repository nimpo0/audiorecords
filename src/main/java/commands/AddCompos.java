package commands;
import database.CompositionBD;
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
import javafx.stage.FileChooser;
import mainPackage.Menu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.util.Objects;

public class AddCompos implements Command {
    private static final Logger logger = LogManager.getLogger(AddCompos.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    public TextField nameField = new TextField();
    public TextField styleField = new TextField();
    public TextField authorField = new TextField();
    public TextField durationField = new TextField();
    public TextField audioPathField = new TextField();
    public TextArea lyricsArea = new TextArea();

    @Override
    public void execute() {
        Menu.getPrimaryStage().getScene().setRoot(createLayout());
    }

    @Override
    public String printInfo() {
        return "Add a new composition.";
    }

    protected File showAudioFileDialog() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Виберіть аудіофайл");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Аудіофайли", "*.mp3", "*.wav"));
        return fileChooser.showOpenDialog(Menu.getPrimaryStage());
    }

    public BorderPane createLayout() {
        Label title = new Label("Додати нову композицію");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);

        ImageView icon = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResource("/music.png")).toExternalForm())
        );
        icon.setFitWidth(70);
        icon.setFitHeight(70);

        HBox titleBox = new HBox(10, icon, title);
        titleBox.setAlignment(Pos.CENTER);

        nameField.setPromptText("Назва композиції");
        authorField.setPromptText("Ім’я автора");
        styleField.setPromptText("Стиль композиції");
        durationField.setPromptText("Тривалість (у секундах)");

        lyricsArea.setPromptText("Текст пісні");
        lyricsArea.setWrapText(true);
        lyricsArea.setPrefRowCount(4);

        audioPathField.setPromptText("Шлях до аудіофайлу");
        audioPathField.setEditable(false);

        Button browseButton = new Button("🎵 Вибрати аудіо");
        browseButton.setOnAction(e -> {
            File file = showAudioFileDialog();
            if (file != null) {
                audioPathField.setText(file.toURI().toString());
            }
        });

        HBox audioBox = new HBox(10, audioPathField, browseButton);
        audioBox.setAlignment(Pos.CENTER);

        Button submitButton = new Button("Додати композицію");
        submitButton.setStyle("-fx-background-radius: 15; -fx-font-size: 14px; -fx-background-color: #ffffff; -fx-text-fill: #800080;");
        submitButton.setPrefWidth(200);
        submitButton.setPrefHeight(40);

        submitButton.setOnAction(e -> {
            try {
                String name = nameField.getText().trim();
                String style = styleField.getText().trim();
                String author = authorField.getText().trim();
                String durationStr = durationField.getText().trim();
                String lyrics = lyricsArea.getText().trim();
                String audioPath = audioPathField.getText().trim();

                if (name.isEmpty() || style.isEmpty() || author.isEmpty() || durationStr.isEmpty() || audioPath.isEmpty()) {
                    showAlert("Усі поля мають бути заповнені.");
                    return;
                }

                int duration = Integer.parseInt(durationStr);
                if (duration <= 0) {
                    showAlert("Тривалість має бути додатнім числом.");
                    return;
                }

                int newId = CompositionBD.insertComposition(name, style, duration, author, lyrics, audioPath);

                if (newId != -1) {
                    logger.info("Композицію '{}' успішно додано з ID {}", name, newId);
                    showAlert("Композицію успішно додано!");
                } else {
                    showAlert("Не вдалося додати композицію.");
                }
            } catch (NumberFormatException ex) {
                showAlert("Некоректне число в полі тривалості.");
                errorLogger.error("Некоректне значення тривалості: {}", ex.getMessage(), ex);
            } catch (Exception ex) {
                errorLogger.error("Помилка під час додавання композиції: {}", ex.getMessage(), ex);
                showAlert("Виникла помилка при додаванні композиції.");
            }
        });

        Button backButton = new Button("⬅ Назад до меню");
        backButton.setStyle("-fx-background-radius: 15; -fx-font-size: 14px; -fx-background-color: #ffffff; -fx-text-fill: #800080;");
        backButton.setPrefWidth(200);
        backButton.setPrefHeight(40);
        backButton.setOnAction(e -> Menu.getInstance().showMainMenu());

        VBox form = new VBox(15, titleBox, nameField, authorField, styleField, durationField, lyricsArea, audioBox, submitButton, backButton);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(30));
        form.setMaxWidth(500);

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

    protected void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Інформація");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
