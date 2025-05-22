package commands;
import composition.Collection;
import composition.Composition;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import mainPackage.Menu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import testutil.BaseFxTest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import static org.junit.jupiter.api.Assertions.*;

public class DisplayCollectionUITest extends BaseFxTest {

    private DisplayCollection displayCollection;

    @BeforeEach
    public void setUp() throws Exception {
        List<Collection> fakeCollections = new ArrayList<>();

        Collection testCollection = new Collection(1, "Test Collection");
        Composition song1 = new Composition();
        song1.setName("Song 1");
        song1.setDuration(120);
        Composition song2 = new Composition();
        song2.setName("Song 2");
        song2.setDuration(180);
        testCollection.addComposition(song1);
        testCollection.addComposition(song2);

        fakeCollections.add(testCollection);

        displayCollection = new DisplayCollection() {
            @Override
            public List<Collection> getAllCollections() {
                return fakeCollections;
            }

            @Override
            public List<Composition> getCompositionsForCollection(String collectionName) {
                for (Collection c : fakeCollections) {
                    if (c.getName().equals(collectionName)) {
                        return c.getCompositions();
                    }
                }
                return List.of();
            }
        };

        runOnFxThreadAndWait(() -> {
            Stage stage = new Stage();
            Menu.setPrimaryStage(stage);
            displayCollection.showAllCollections();
            stage.show();
        });
    }

    @Test
    public void testDisplayCollectionsUI() {
        lookup(node -> node instanceof Label).queryAll().forEach(node -> {
            System.out.println(((Label) node).getText());
        });

        Set<Node> labels = lookup(node -> node instanceof Label).queryAll();

        Label infoLabel = null;
        for (Node node : labels) {
            String text = ((Label) node).getText();
            if (text != null && text.contains("Кількість пісень: 2") && text.contains("Тривалість: невизначено")) {
                infoLabel = (Label) node;
                break;
            }
        }
        assertNotNull(infoLabel, "Інформація про кількість пісень повинна бути відображена");
        assertTrue(infoLabel.getText().contains("2"), "Інформація про кількість пісень повинна бути правильною");
        assertTrue(infoLabel.getText().contains("невизначено"), "Інформація про тривалість повинна бути правильною");

        Button backButton = lookup(node -> node instanceof Button && "⬅ Назад до меню".equals(((Button) node).getText())).queryButton();
        assertNotNull(backButton, "Кнопка назад до меню повинна бути присутня");

        Label collectionLabel = (Label) lookup(node -> node instanceof Label && ((Label) node).getText().contains("Test Collection")).queryLabeled();
        assertNotNull(collectionLabel, "Назва колекції повинна бути відображена");

        List<Button> buttons = lookup(node -> node instanceof Button)
                .queryAll().stream().map(node -> (Button) node)
                .filter(b -> b.getText().equals("Відкрити") || b.getText().equals("🗑 Видалити"))
                .collect(Collectors.toList());

        assertEquals(2, buttons.size(), "Повинні бути кнопки 'Відкрити' і 'Видалити'");
    }

}
