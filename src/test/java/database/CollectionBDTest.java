package database;
import composition.Collection;
import composition.Composition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionBDTest {

    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockResult;

    private CollectionBD collectionBD;

    @BeforeEach
    void setup() throws SQLException {
        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockResult = mock(ResultSet.class);

        collectionBD = new CollectionBD();
    }

    @Test
    void testInsertCollection_success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            collectionBD.insertCollection("My Playlist");

            verify(mockStmt, times(1)).executeUpdate();
            verify(mockStmt).setString(1, "My Playlist");
        }
    }

    @Test
    void testInsertCollection_sqlError() {
        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenThrow(new SQLException("Insert fail"));

            assertDoesNotThrow(() -> collectionBD.insertCollection("Fails"));
        }
    }

    @Test
    void testDeleteCollection_success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1); // one row deleted

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            collectionBD.deleteCollection("Old One");

            verify(mockStmt, times(1)).executeUpdate();
        }
    }

    @Test
    void testDeleteCollection_noSuchCollection() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            collectionBD.deleteCollection("Missing");
        }
    }

    @Test
    void testDeleteCollection_sqlError() {
        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenThrow(new SQLException("Delete fail"));

            assertDoesNotThrow(() -> collectionBD.deleteCollection("Error"));
        }
    }

    @Test
    void testGetAllCollections_success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResult);

        when(mockResult.next()).thenReturn(true, false);
        when(mockResult.getInt("id")).thenReturn(1);
        when(mockResult.getString("name")).thenReturn("My Collection");

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<Collection> result = CollectionBD.getAllCollections();

            assertEquals(1, result.size());
            assertEquals("My Collection", result.get(0).getName());
        }
    }

    @Test
    void testGetAllCollections_sqlError() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Query fail"));

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<Collection> result = CollectionBD.getAllCollections();

            assertTrue(result.isEmpty());
        }
    }

    @Test
    void testGetCompositionsForCollection_success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResult);

        when(mockResult.next()).thenReturn(true, false);
        when(mockResult.getInt("id")).thenReturn(1);
        when(mockResult.getString("name")).thenReturn("Song");
        when(mockResult.getString("style")).thenReturn("Pop");
        when(mockResult.getInt("duration")).thenReturn(180);
        when(mockResult.getString("author")).thenReturn("John");
        when(mockResult.getString("lyrics")).thenReturn("Lyrics");
        when(mockResult.getString("audiopath")).thenReturn("file.mp3");

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<Composition> result = collectionBD.getCompositionsForCollection("My Collection");

            assertEquals(1, result.size());
            assertEquals("Song", result.get(0).getName());
        }
    }

    @Test
    void testGetCompositionsForCollection_sqlError() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Fail"));

        try (MockedStatic<DatabaseManager> mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<Composition> result = collectionBD.getCompositionsForCollection("Bad Collection");

            assertTrue(result.isEmpty());
        }
    }
}
