package database;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import java.sql.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollectionCompositionBDTest {

    private CollectionCompositionBD collectionCompositionBD;
    private Connection mockConn;
    private PreparedStatement mockStmt;
    private ResultSet mockResult;

    @BeforeEach
    void setup() throws SQLException {
        collectionCompositionBD = spy(new CollectionCompositionBD());
        mockConn = mock(Connection.class);
        mockStmt = mock(PreparedStatement.class);
        mockResult = mock(ResultSet.class);
    }

    @Test
    void testGetCompositionIdByName_found() throws Exception {
        try (MockedStatic<DriverManager> driverManagerStatic = mockStatic(DriverManager.class)) {
            driverManagerStatic.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockResult);
            when(mockResult.next()).thenReturn(true);
            when(mockResult.getInt("id")).thenReturn(7);

            int id = collectionCompositionBD.getCompositionIdByName("SomeName");

            assertEquals(7, id);
            verify(mockStmt).setString(1, "SomeName");
        }
    }

    @Test
    void testGetCompositionIdByName_notFound() throws Exception {
        try (MockedStatic<DriverManager> driverManagerStatic = mockStatic(DriverManager.class)) {
            driverManagerStatic.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockResult);
            when(mockResult.next()).thenReturn(false);

            int id = collectionCompositionBD.getCompositionIdByName("NoSuchName");

            assertEquals(-1, id);
        }
    }

    @Test
    void testGetCollectionIdByName_found() throws Exception {
        try (MockedStatic<DriverManager> driverManagerStatic = mockStatic(DriverManager.class)) {
            driverManagerStatic.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockResult);
            when(mockResult.next()).thenReturn(true);
            when(mockResult.getInt("id")).thenReturn(11);

            int id = collectionCompositionBD.getCollectionIdByName("CollectionName");

            assertEquals(11, id);
        }
    }

    @Test
    void testGetCollectionIdByName_notFound() throws Exception {
        try (MockedStatic<DriverManager> driverManagerStatic = mockStatic(DriverManager.class)) {
            driverManagerStatic.when(() -> DriverManager.getConnection(anyString(), anyString(), anyString()))
                    .thenReturn(mockConn);
            when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);
            when(mockStmt.executeQuery()).thenReturn(mockResult);
            when(mockResult.next()).thenReturn(false);

            int id = collectionCompositionBD.getCollectionIdByName("Missing");

            assertEquals(-1, id);
        }
    }

    @Test
    void testAddCompositionToCollection_success() throws Exception {
        doReturn(1).when(collectionCompositionBD).getCompositionIdByName("CompName");
        doReturn(2).when(collectionCompositionBD).getCollectionIdByName("ColName");

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

        try (MockedStatic<DatabaseManager> dbStatic = mockStatic(DatabaseManager.class)) {
            dbStatic.when(DatabaseManager::getConnection).thenReturn(mockConn);

            collectionCompositionBD.addCompositionToCollection("CompName", "ColName");

            verify(mockStmt).setInt(1, 1);
            verify(mockStmt).setInt(2, 2);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testAddCompositionToCollection_notFound() {
        doReturn(-1).when(collectionCompositionBD).getCompositionIdByName("BadComp");
        doReturn(3).when(collectionCompositionBD).getCollectionIdByName("ColName");

        collectionCompositionBD.addCompositionToCollection("BadComp", "ColName");
    }

    @Test
    void testAddCompositionToCollection_sqlException() throws Exception {
        doReturn(1).when(collectionCompositionBD).getCompositionIdByName("Comp");
        doReturn(2).when(collectionCompositionBD).getCollectionIdByName("Col");

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Insert fail"));

        try (MockedStatic<DatabaseManager> dbStatic = mockStatic(DatabaseManager.class)) {
            dbStatic.when(DatabaseManager::getConnection).thenReturn(mockConn);

            collectionCompositionBD.addCompositionToCollection("Comp", "Col");
        }
    }

    @Test
    void testRemoveCompositionFromCollection_success() throws Exception {
        doReturn(5).when(collectionCompositionBD).getCompositionIdByName("CompName");
        doReturn(6).when(collectionCompositionBD).getCollectionIdByName("ColName");

        when(mockConn.prepareStatement(anyString())).thenReturn(mockStmt);

        try (MockedStatic<DatabaseManager> dbStatic = mockStatic(DatabaseManager.class)) {
            dbStatic.when(DatabaseManager::getConnection).thenReturn(mockConn);

            collectionCompositionBD.removeCompositionFromCollection("CompName", "ColName");

            verify(mockStmt).setInt(1, 5);
            verify(mockStmt).setInt(2, 6);
            verify(mockStmt).executeUpdate();
        }
    }

    @Test
    void testRemoveCompositionFromCollection_notFound() {
        doReturn(5).when(collectionCompositionBD).getCompositionIdByName("CompName");
        doReturn(-1).when(collectionCompositionBD).getCollectionIdByName("BadCol");

        collectionCompositionBD.removeCompositionFromCollection("CompName", "BadCol");
    }

    @Test
    void testRemoveCompositionFromCollection_sqlException() throws Exception {
        doReturn(5).when(collectionCompositionBD).getCompositionIdByName("CompName");
        doReturn(6).when(collectionCompositionBD).getCollectionIdByName("ColName");

        when(mockConn.prepareStatement(anyString())).thenThrow(new SQLException("Delete fail"));

        try (MockedStatic<DatabaseManager> dbStatic = mockStatic(DatabaseManager.class)) {
            dbStatic.when(DatabaseManager::getConnection).thenReturn(mockConn);

            collectionCompositionBD.removeCompositionFromCollection("CompName", "ColName");
        }
    }
}
