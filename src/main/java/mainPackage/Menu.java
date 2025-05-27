package mainPackage;
import commands.*;
import file.DataExport;
import file.DataImport;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Menu extends Application {
    final Map<String, Command> commandMap = new HashMap<>();
    private static Stage primaryStage;
    public static Menu instance;

    public static Menu getInstance() {
        return instance;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage stage) {
        primaryStage = stage;
    }

    @Override
    public void start(Stage stage) {
        instance = this;
        this.primaryStage = stage;
        initializeCommands();
        showWelcomeScreen();
    }

    private void showWelcomeScreen() {
        BorderPane welcomePane = new BorderPane();
        welcomePane.setPadding(new Insets(50));

        BackgroundFill backgroundFill = new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.MEDIUMPURPLE),
                        new Stop(1, Color.HOTPINK)),
                CornerRadii.EMPTY, Insets.EMPTY);
        welcomePane.setBackground(new Background(backgroundFill));

        Text welcomeText = new Text("🎶 Welcome to Music Collection!");
        welcomeText.setFont(Font.font("Arial", 24));
        welcomeText.setFill(Color.WHITE);
        welcomeText.setWrappingWidth(400);
        welcomeText.setTextAlignment(javafx.scene.text.TextAlignment.CENTER);

        Button startButton = new Button("🚀 Start");
        startButton.setStyle("-fx-font-size: 16px; -fx-background-radius: 20; -fx-background-color: white; -fx-text-fill: purple;");
        startButton.setOnAction(e -> showMainMenu());

        VBox centerBox = new VBox(20, welcomeText, startButton);
        centerBox.setAlignment(Pos.CENTER);

        welcomePane.setCenter(centerBox);

        Scene scene = new Scene(welcomePane, 700, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Music Collection");
        primaryStage.show();
    }

    public void showMainMenu() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(20));
        grid.setHgap(15);
        grid.setVgap(15);
        grid.setAlignment(Pos.CENTER);

        String[][] buttons = {
                {"➕ Додати композицію", "addCompos"},
                {"📄 Показати всі композиції", "displayAll"},
                {"🆕 Створити колекцію", "createCollection"},
                {"📁 Переглянути колекцію", "displayCollection"},
                {"📤 Імпортувати з JSON", "importJson"},
                {"📥 Експортувати у JSON", "exportJson"},
                {"💥 Критична помилка", "criticalError"}
        };

        int row = 0, col = 0;
        for (String[] b : buttons) {
            Button btn = createStyledButton(b[0], b[1]);
            grid.add(btn, col, row);
            col++;
            if (col == 2) {
                col = 0;
                row++;
            }
        }

        BorderPane root = new BorderPane();
        root.setCenter(grid);
        root.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 1, 1, 0, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#F19CBB")),
                        new Stop(1, Color.web("#D8BFD8"))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        ImageView disk = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResource("/brokendisk.png")).toExternalForm())
        );
        disk.setFitWidth(180);
        disk.setFitHeight(100);

        RotateTransition rotate = new RotateTransition(Duration.seconds(5), disk);
        rotate.setByAngle(360);
        rotate.setCycleCount(RotateTransition.INDEFINITE);
        rotate.setInterpolator(Interpolator.LINEAR);
        rotate.play();

        Text rotatingText = new Text("🎵 Керуйте своєю музичною колекцією!");
        rotatingText.setFont(Font.font("Arial", 18));
        rotatingText.setFill(Color.DARKMAGENTA);

        VBox topBox = new VBox(10, disk, rotatingText);
        topBox.setAlignment(Pos.CENTER);
        topBox.setPadding(new Insets(20));

        root.setTop(topBox);

        Scene scene = new Scene(root, 700, 600);
        primaryStage.setScene(scene);
    }

    Button createStyledButton(String text, String commandKey) {
        Button button = new Button(text);
        button.setPrefWidth(260);
        button.setPrefHeight(60);
        button.setStyle("-fx-background-radius: 15; -fx-font-size: 14px; -fx-background-color: #ffffff; -fx-text-fill: #800080;");
        button.setOnAction(e -> {
            Command command = commandMap.get(commandKey);
            if (command != null) {
                try {
                    command.execute();
                } catch (Exception ex) {
                    showAlert("Помилка", "Помилка при виконанні команди: " + ex.getMessage());
                }
            } else {
                showAlert("Помилка", "Команда не знайдена.");
            }
        });
        return button;
    }

    void initializeCommands() {
        commandMap.put("displayAll", new DisplayAllCompos());
        commandMap.put("criticalError", new CriticalError());
        commandMap.put("addCompos", new AddCompos());
        commandMap.put("displayCollection", new DisplayCollection());
        commandMap.put("createCollection", new CreateCollection());
        commandMap.put("importJson", new DataImport());
        commandMap.put("exportJson", new DataExport());
    }

    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
