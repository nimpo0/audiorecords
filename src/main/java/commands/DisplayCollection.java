package commands;
import composition.Collection;
import composition.Composition;
import database.CollectionBD;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import mainPackage.Menu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Objects;

public class DisplayCollection implements Command {
    private static final Logger logger = LogManager.getLogger(DisplayCollection.class);
    private final CollectionBD collectionBD = new CollectionBD();

    @Override
    public void execute() {
        showAllCollections();
    }

    private void showAllCollections() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        root.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 1, 1, 0, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#F19CBB")),
                        new Stop(1, Color.web("#D8BFD8"))),
                new CornerRadii(10),
                Insets.EMPTY)));

        Label header = new Label("🎵 Плейлисти");
        header.setFont(Font.font("Arial", 28));
        header.setTextFill(Color.DARKMAGENTA);

        ImageView icon = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResource("/girl.png")).toExternalForm())
        );
        icon.setFitWidth(80);
        icon.setFitHeight(100);

        HBox headerBox = new HBox(10, header, icon);
        headerBox.setAlignment(Pos.CENTER);

        root.getChildren().add(headerBox);

        GridPane grid = new GridPane();
        grid.setHgap(20);
        grid.setVgap(20);
        grid.setAlignment(Pos.CENTER);

        List<Collection> allCollections = CollectionBD.getAllCollections();
        CalculateDuration calculator = new CalculateDuration();

        int col = 0, row = 0;

        for (Collection collection : allCollections) {
            String collectionName = collection.getName();
            List<Composition> compositions = collectionBD.getCompositionsForCollection(collectionName);
            int count = compositions.size();
            int duration = calculator.getTotalDuration(collectionName);

            VBox card = new VBox(8);
            card.setPadding(new Insets(15));
            card.setAlignment(Pos.CENTER_LEFT);
            card.setPrefWidth(300);
            card.setBackground(new Background(new BackgroundFill(
                    new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                            new Stop(0, Color.LAVENDERBLUSH),
                            new Stop(1, Color.MEDIUMPURPLE)),
                    new CornerRadii(15), Insets.EMPTY)));
            card.setBorder(new Border(new BorderStroke(Color.web("#8E4585"), BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(2))));

            Label name = new Label("📁 " + collectionName);
            name.setFont(Font.font("Arial", 18));
            name.setTextFill(Color.web("#800080"));

            String durationText = duration >= 0
                    ? String.format("Кількість пісень: %d\nТривалість: %d хв %d сек", count, duration / 60, duration % 60)
                    : String.format("Кількість пісень: %d\nТривалість: невизначено", count);

            Label info = new Label(durationText);
            info.setFont(Font.font("Arial", 14));
            info.setTextFill(Color.web("#800080"));

            Button openBtn = new Button("Відкрити");
            openBtn.setStyle("-fx-background-radius: 15; -fx-font-size: 13px; -fx-background-color: white; -fx-text-fill: #800080;");
            openBtn.setOnAction(e -> {
                new DisplayAllCompos(compositions, "🎼 Композиції з '" + collectionName + "'", collectionName).execute();
            });

            Button deleteBtn = new Button("🗑 Видалити");
            deleteBtn.setStyle("-fx-background-radius: 15; -fx-font-size: 13px; -fx-background-color: white; -fx-text-fill: #4B0082;");
            deleteBtn.setOnAction(e -> new DeleteCollection(collectionName).execute());

            card.getChildren().addAll(name, info, openBtn, deleteBtn);

            grid.add(card, col, row);

            col++;
            if (col > 1) {
                col = 0;
                row++;
            }
        }

        root.getChildren().add(grid);

        Button backButton = new Button("⬅ Назад до меню");
        backButton.setStyle("-fx-background-radius: 15; -fx-font-size: 14px; -fx-background-color: white; -fx-text-fill: #800080;");
        backButton.setOnAction(e -> Menu.getInstance().showMainMenu());

        root.getChildren().add(backButton);

        ScrollPane scrollPane = new ScrollPane(root);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 1, 1, 0, true,
                        CycleMethod.NO_CYCLE,
                        new Stop(0, Color.LAVENDERBLUSH),
                        new Stop(1, Color.MEDIUMPURPLE)),
                CornerRadii.EMPTY,
                Insets.EMPTY)));

        Scene scene = new Scene(scrollPane, 700, 600);
        Menu.getPrimaryStage().setScene(scene);
        logger.info("Сторінка всіх плейлистів відображена.");
    }

    @Override
    public String printInfo() {
        return "Переглянути всі плейлисти та композиції в них.";
    }

    private void showMessage(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Повідомлення");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
