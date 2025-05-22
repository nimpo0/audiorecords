package commands;
import composition.Composition;
import database.CollectionBD;
import database.CollectionCompositionBD;
import database.CompositionBD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddToCollectionTest {

    private AddToCollection addToCollection;
    private String collectionName = "MyCollection";

    static class TestableAddToCollection extends AddToCollection {
        String lastMessageTitle = null;
        String lastMessageContent = null;
        List<String> lastChoices = null;
        String choiceToReturn = null;

        public TestableAddToCollection(String collectionName, CollectionBD collectionBD, CollectionCompositionBD relationBD) {
            super(collectionName, collectionBD, relationBD);
        }

        @Override
        protected Optional<String> showChoiceDialog(String title, String header, List<String> choices) {
            this.lastChoices = choices;
            return Optional.ofNullable(choiceToReturn);
        }

        @Override
        protected void showStyledMessage(String title, String content) {
            this.lastMessageTitle = title;
            this.lastMessageContent = content;
        }
    }

    private CollectionBD mockCollectionBD;
    private CollectionCompositionBD mockRelationBD;

    @BeforeEach
    void setup() {
        mockCollectionBD = mock(CollectionBD.class);
        mockRelationBD = mock(CollectionCompositionBD.class);
        addToCollection = new TestableAddToCollection(collectionName, mockCollectionBD, mockRelationBD);
    }

    @Test
    void testCollectionNotFound() {
        when(mockRelationBD.getCollectionIdByName(collectionName)).thenReturn(-1);

        addToCollection.execute();

        TestableAddToCollection testable = (TestableAddToCollection) addToCollection;
        assertEquals("Колекція не знайдена", testable.lastMessageTitle);
        assertTrue(testable.lastMessageContent.contains(collectionName));
    }

    @Test
    void testNoAvailableCompositions() {
        when(mockRelationBD.getCollectionIdByName(collectionName)).thenReturn(1);

        List<Composition> allComps = List.of(new Composition(1,"Song1", "Rock", 180, "Author", "Lyrics", "path"));
        List<Composition> currentComps = List.of(new Composition(2,"Song1", "Rock", 180, "Author", "Lyrics", "path"));

        try (MockedStatic<CompositionBD> mockedCompositionBD = Mockito.mockStatic(CompositionBD.class)) {
            mockedCompositionBD.when(CompositionBD::getAllCompositions).thenReturn(allComps);
            when(mockCollectionBD.getCompositionsForCollection(collectionName)).thenReturn(currentComps);

            addToCollection.execute();

            TestableAddToCollection testable = (TestableAddToCollection) addToCollection;
            assertEquals("Немає доступних композицій", testable.lastMessageTitle);
        }
    }

    @Test
    void testUserCancelsChoiceDialog() {
        when(mockRelationBD.getCollectionIdByName(collectionName)).thenReturn(1);

        List<Composition> allComps = List.of(new Composition(2,"Song1", "Rock", 180, "Author", "Lyrics", "path"));
        List<Composition> currentComps = List.of();

        try (MockedStatic<CompositionBD> mockedCompositionBD = Mockito.mockStatic(CompositionBD.class)) {
            mockedCompositionBD.when(CompositionBD::getAllCompositions).thenReturn(allComps);
            when(mockCollectionBD.getCompositionsForCollection(collectionName)).thenReturn(currentComps);

            TestableAddToCollection testable = (TestableAddToCollection) addToCollection;
            testable.choiceToReturn = null;

            addToCollection.execute();

            assertNull(testable.lastMessageTitle);
        }
    }

    @Test
    void testCompositionAlreadyAdded() {
        when(mockRelationBD.getCollectionIdByName(collectionName)).thenReturn(1);

        List<Composition> allComps = List.of(
                new Composition(4, "Song1", "Rock", 180, "Author", "Lyrics", "path"),
                new Composition(5, "Song2", "Pop", 200, "Author2", "Lyrics2", "path2")
        );

        List<Composition> currentComps = List.of(
                new Composition(4, "Song1", "Rock", 180, "Author", "Lyrics", "path")
        );

        try (MockedStatic<CompositionBD> mockedCompositionBD = Mockito.mockStatic(CompositionBD.class)) {
            mockedCompositionBD.when(CompositionBD::getAllCompositions).thenReturn(allComps);
            when(mockCollectionBD.getCompositionsForCollection(collectionName)).thenReturn(currentComps);

            TestableAddToCollection testable = (TestableAddToCollection) addToCollection;
            testable.choiceToReturn = "Song1";

            addToCollection.execute();
            assertEquals("Композиція вже додана", testable.lastMessageTitle);
        }
    }

    @Test
    void testExceptionDuringExecution() {
        when(mockRelationBD.getCollectionIdByName(collectionName)).thenReturn(1);
        List<Composition> allComps = List.of(new Composition(1,"Song", "Rock", 180, "Author", "Lyrics", "path"));
        when(mockCollectionBD.getCompositionsForCollection(collectionName)).thenReturn(List.of());

        try (MockedStatic<CompositionBD> mockedCompositionBD = Mockito.mockStatic(CompositionBD.class)) {
            mockedCompositionBD.when(CompositionBD::getAllCompositions).thenReturn(allComps);

            TestableAddToCollection testable = (TestableAddToCollection) addToCollection;
            testable.choiceToReturn = "Song";

            doThrow(new RuntimeException("DB error")).when(mockRelationBD).addCompositionToCollection("Song", collectionName);

            addToCollection.execute();
            assertEquals("Помилка", testable.lastMessageTitle);
            assertTrue(testable.lastMessageContent.contains("DB error"));
        }
    }


    @Test
    void testSuccessfulAdd() {
        when(mockRelationBD.getCollectionIdByName(collectionName)).thenReturn(1);

        List<Composition> allComps = List.of(
                new Composition(6,"Song1", "Rock", 180, "Author", "Lyrics", "path"),
                new Composition(7,"Song2", "Pop", 200, "Author2", "Lyrics2", "path2")
        );
        List<Composition> currentComps = List.of(new Composition(6,"Song1", "Rock", 180, "Author", "Lyrics", "path"));

        try (MockedStatic<CompositionBD> mockedCompositionBD = Mockito.mockStatic(CompositionBD.class)) {
            mockedCompositionBD.when(CompositionBD::getAllCompositions).thenReturn(allComps);
            when(mockCollectionBD.getCompositionsForCollection(collectionName)).thenReturn(currentComps);

            TestableAddToCollection testable = (TestableAddToCollection) addToCollection;
            testable.choiceToReturn = "Song2";

            addToCollection.execute();
            verify(mockRelationBD).addCompositionToCollection("Song2", collectionName);

            assertEquals("Успішно", testable.lastMessageTitle);
            assertTrue(testable.lastMessageContent.contains("Song2"));
        }

    }
}
