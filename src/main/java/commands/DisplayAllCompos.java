package commands;
import composition.Composition;
import database.CompositionBD;
import database.CollectionBD;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import javafx.scene.text.FontWeight;
import mainPackage.Menu;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Objects;

public class DisplayAllCompos implements Command {
    private static final Logger logger = LogManager.getLogger(DisplayAllCompos.class);
    private List<Composition> compositions;
    private final String headerText;
    private String collectionName;
    private final CollectionBD collectionBD = new CollectionBD();
    private MediaPlayer activePlayer = null;

    public DisplayAllCompos() {
        this.compositions = CompositionBD.getAllCompositions();
        this.headerText = "🎼 Всі композиції";
    }

    public DisplayAllCompos(List<Composition> compositions, String headerText, String collectionName) {
        this.compositions = compositions;
        this.headerText = headerText;
        this.collectionName = collectionName;
    }

    @Override
    public void execute() {
        if (collectionName == null) {
            this.compositions = CompositionBD.getAllCompositions();
        }

        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        root.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 1, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#F19CBB")),
                        new Stop(1, Color.web("#D8BFD8"))),
                new CornerRadii(10), Insets.EMPTY)));

        Label header = new Label(headerText);
        header.setFont(Font.font("Arial", 24));
        header.setTextFill(Color.DARKMAGENTA);

        ImageView disk = new ImageView(
                new Image(Objects.requireNonNull(getClass().getResource("/disks.png")).toExternalForm())
        );
        disk.setFitWidth(100);
        disk.setFitHeight(100);
        HBox headerBox = new HBox(10, header, disk);
        headerBox.setAlignment(Pos.CENTER);

        HBox controlBox = new HBox(15);
        controlBox.setAlignment(Pos.CENTER);

        Button sortButton = new Button("🎧 Сортувати за стилем");
        sortButton.setStyle("-fx-background-radius: 15; -fx-font-size: 13px; -fx-background-color: white; -fx-text-fill: #800080;");
        sortButton.setOnAction(e -> {
            SortingByStyle sorter = new SortingByStyle();
            List<Composition> sorted = sorter.sort(compositions);
            if (sorted != null) {
                new DisplayAllCompos(sorted, "🎧 Відсортовано за стилем", collectionName).execute();
            }
        });

        Button findButton = new Button("🔍 Знайти за тривалістю");
        findButton.setStyle("-fx-background-radius: 15; -fx-font-size: 13px; -fx-background-color: white; -fx-text-fill: #800080;");
        findButton.setOnAction(e -> {
            FindCompositions finder = new FindCompositions();
            List<Composition> filtered = finder.findInRange(compositions);
            if (filtered != null) {
                new DisplayAllCompos(filtered, "🔍 Знайдені композиції", collectionName).execute();
            }
        });

        controlBox.getChildren().addAll(sortButton, findButton);

        if (collectionName != null) {
            Button addButton = new Button("➕ Додати до колекції");
            addButton.setStyle("-fx-background-radius: 15; -fx-font-size: 13px; -fx-background-color: white; -fx-text-fill: #800080;");
            addButton.setOnAction(e -> {
                new AddToCollection(collectionName).execute();
                new DisplayAllCompos(collectionBD.getCompositionsForCollection(collectionName),
                        headerText, collectionName).execute();
            });

            controlBox.getChildren().add(addButton);
        }

        VBox listBox = new VBox(15);
        listBox.setAlignment(Pos.CENTER);
        if (compositions.isEmpty()) {
            Label emptyLabel = new Label("Список композицій порожній.");
            emptyLabel.setFont(Font.font("Arial", 16));
            emptyLabel.setTextFill(Color.DARKMAGENTA);
            listBox.getChildren().add(emptyLabel);
        } else {
            for (Composition c : compositions) {
                VBox card = createCompositionCard(c);
                listBox.getChildren().add(card);
            }
        }

        Button backButton = new Button("⬅ Назад");
        backButton.setStyle("-fx-background-radius: 15; -fx-font-size: 14px; -fx-background-color: white; -fx-text-fill: #800080;");
        backButton.setOnAction(e -> {
            if (collectionName != null) {
                new DisplayCollection().execute();
            } else {
                Menu.getInstance().showMainMenu();
            }
        });

        root.getChildren().addAll(headerBox, controlBox, listBox, backButton);

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
        logger.info("Показано композиції.");
    }

    private VBox createCompositionCard(Composition c) {
        VBox card = new VBox(8);
        card.setPadding(new Insets(15));
        card.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.LAVENDERBLUSH),
                        new Stop(1, Color.web("#acaae3"))),
                new CornerRadii(15), Insets.EMPTY)));
        card.setBorder(new Border(new BorderStroke(Color.web("#8E4585"), BorderStrokeStyle.SOLID, new CornerRadii(15), new BorderWidths(2))));
        card.setMaxWidth(320);
        card.setMinHeight(180);
        card.setPrefHeight(Region.USE_COMPUTED_SIZE);

        Label name = new Label(c.getName());
        name.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        name.setTextFill(Color.web("#800080"));
        name.setWrapText(true);

        Label author = new Label(c.getAuthor());
        author.setFont(Font.font("Arial", 16));
        author.setTextFill(Color.web("#800080"));

        Label style = new Label(c.getStyle());
        style.setFont(Font.font("Arial", 14));
        style.setTextFill(Color.web("#800080"));

        Label lyrics = new Label(c.getLyrics().isEmpty() ? "–" :
                (c.getLyrics().length() > 100 ? c.getLyrics().substring(0, 97) + "..." : c.getLyrics()));
        lyrics.setFont(Font.font("Arial", 12));
        lyrics.setTextFill(Color.web("#800080"));
        lyrics.setWrapText(true);
        lyrics.setMaxWidth(220);

        Label duration = new Label(formatDuration(c.getDuration()));
        duration.setFont(Font.font("Arial", 12));
        duration.setTextFill(Color.web("#800080"));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox lyricsAndDuration = new HBox(lyrics, spacer, duration);
        lyricsAndDuration.setAlignment(Pos.TOP_LEFT);

        Button playButton = new Button();
        playButton.setStyle("-fx-background-radius: 20; -fx-font-size: 14px; -fx-background-color: white; -fx-text-fill: #4B0082;");

        if (activePlayer != null && activePlayer.getStatus() == MediaPlayer.Status.PLAYING && activePlayer.getMedia().getSource().equals(c.getAudioPath())) {
            playButton.setText("⏸");
        } else {
            playButton.setText("▶");
        }

        playButton.setOnAction(e -> {
            try {
                if (activePlayer != null && activePlayer.getStatus() == MediaPlayer.Status.PLAYING
                        && activePlayer.getMedia().getSource().equals(c.getAudioPath())) {
                    activePlayer.pause();
                    playButton.setText("▶");
                } else {
                    if (activePlayer != null) {
                        activePlayer.stop();
                        activePlayer.dispose();
                    }

                    Media media = new Media(c.getAudioPath());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    mediaPlayer.setOnEndOfMedia(() -> playButton.setText("▶"));
                    mediaPlayer.play();
                    activePlayer = mediaPlayer;
                    playButton.setText("⏸");
                }
            } catch (Exception ex) {
                showMessage("Помилка при відтворенні аудіо.");
            }
        });

        Button deleteButton = new Button();
        deleteButton.setText(collectionName != null ? "🗑 Видалити з колекції" : "🗑 Видалити");
        deleteButton.setStyle("-fx-background-radius: 12; -fx-font-size: 12px; -fx-background-color: white; -fx-text-fill: #8B0000;");
        deleteButton.setOnAction(e -> {
            if (collectionName != null) {
                new DeleteFromCollection(c.getName(), collectionName).execute();
                new DisplayAllCompos(collectionBD.getCompositionsForCollection(collectionName), headerText, collectionName).execute();
            } else {
                new DeleteCompos(c.getName()).execute();
                new DisplayAllCompos().execute();
            }
        });

        HBox bottomBox = new HBox(playButton, new Region(), deleteButton);
        HBox.setHgrow(bottomBox.getChildren().get(1), Priority.ALWAYS);
        bottomBox.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(name, author, style, lyricsAndDuration, bottomBox);
        return card;
    }

    private String formatDuration(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%d:%02d", minutes, seconds);
    }

    @Override
    public String printInfo() {
        return "Показати всі композиції.";
    }

    private void showMessage(String msg) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("Повідомлення");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
