package commands;

import composition.ComposCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class DisplayCollectionTest {

    private ComposCollection collection;
    private DisplayCollection displayCollection;

    @BeforeEach
    public void setUp() {
        collection = mock(ComposCollection.class);
        displayCollection = new DisplayCollection(collection);
    }

    @Test
    public void testExecuteWhenCollectionIsEmpty() {
        when(collection.isEmpty()).thenReturn(true);
        displayCollection.execute();
        verify(collection).isEmpty();
    }

    @Test
    public void testExecuteWhenCollectionHasCompositions() {
        when(collection.isEmpty()).thenReturn(false);
        displayCollection.execute();
        verify(collection).isEmpty();
        verify(collection).displayCompositions();
    }
}
