package testCommands;

import commands.DisplayAllCompos;
import composition.ComposCollection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class DisplayAllComposTest {

    private ComposCollection allCompositions;
    private DisplayAllCompos displayAllCompos;

    @BeforeEach
    public void setUp() {
        allCompositions = mock(ComposCollection.class);
        displayAllCompos = new DisplayAllCompos(allCompositions);
    }

    @Test
    public void testExecuteWhenCollectionIsEmpty() {
        when(allCompositions.isAllEmpty()).thenReturn(true);

        displayAllCompos.execute();
        verify(allCompositions).isAllEmpty();
    }

    @Test
    public void testExecuteWhenCollectionHasCompositions() {
        when(allCompositions.isAllEmpty()).thenReturn(false);
        displayAllCompos.execute();

        verify(allCompositions).isAllEmpty();
        verify(allCompositions).displayAllCompositions();
    }
}
