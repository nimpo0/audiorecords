package database;
import composition.Composition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CompositionBDTest {

    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockResult;

    @BeforeEach
    void setup() throws SQLException {
        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockResult = mock(ResultSet.class);
    }

    @Test
    void testInsertComposition_success() throws Exception {
        when(mockConn.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);
        when(mockStmt.getGeneratedKeys()).thenReturn(mockResult);
        when(mockResult.next()).thenReturn(true);
        when(mockResult.getInt(1)).thenReturn(42);

        try (var mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            int id = CompositionBD.insertComposition("Song", "Pop", 200, "Author", "Lyrics", "file.wav");

            assertEquals(42, id);
            verify(mockStmt, times(1)).executeUpdate();
        }
    }

    @Test
    void testInsertComposition_sqlError() throws Exception {
        try (var mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenThrow(new SQLException("DB error"));

            int id = CompositionBD.insertComposition("Bad", "Rock", 100, "A", "L", "file");

            assertEquals(-1, id);
        }
    }

    @Test
    void testDeleteComposition_success() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(1);

        try (var mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            boolean deleted = CompositionBD.deleteComposition("TestSong");

            assertTrue(deleted);
        }
    }

    @Test
    void testDeleteComposition_fail() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeUpdate()).thenReturn(0);

        try (var mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            boolean deleted = CompositionBD.deleteComposition("Unknown");

            assertFalse(deleted);
        }
    }

    @Test
    void testGetAllCompositions() throws Exception {
        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
        when(mockStmt.executeQuery()).thenReturn(mockResult);

        when(mockResult.next()).thenReturn(true, false);
        when(mockResult.getInt("id")).thenReturn(1);
        when(mockResult.getString("name")).thenReturn("TestSong");
        when(mockResult.getString("style")).thenReturn("Jazz");
        when(mockResult.getInt("duration")).thenReturn(120);
        when(mockResult.getString("author")).thenReturn("John");
        when(mockResult.getString("lyrics")).thenReturn("Lyrics");
        when(mockResult.getString("audiopath")).thenReturn("file.mp3");

        try (var mocked = mockStatic(DatabaseManager.class)) {
            mocked.when(DatabaseManager::getConnection).thenReturn(mockConn);

            List<Composition> result = CompositionBD.getAllCompositions();

            assertEquals(1, result.size());
            assertEquals("TestSong", result.get(0).getName());
        }
    }
}
