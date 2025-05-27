package database;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;

public class CollectionCompositionBD extends DatabaseManager {
    private static final Logger logger = LogManager.getLogger(CollectionCompositionBD.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private int getIdByName(String tableName, String name) {
        String sql = "SELECT id FROM " + tableName + " WHERE name = ?";
        int id = -1;

        try {
            Class.forName("org.postgresql.Driver");

            try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, name);
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    id = rs.getInt("id");
                }

            }
        } catch (ClassNotFoundException e) {
            errorLogger.error("Драйвер PostgreSQL не знайдено: {}", e.getMessage(), e);
        } catch (SQLException e) {
            errorLogger.error("Помилка при отриманні id з таблиці '{}': {}", tableName, e.getMessage(), e);
        }

        return id;
    }

    public int getCompositionIdByName(String compositionName) {
        return getIdByName("compositions", compositionName);
    }

    public int getCollectionIdByName(String collectionName) {
        return getIdByName("collections", collectionName);
    }

    public void addCompositionToCollection(String compositionName, String collectionName) {
        int compositionId = getCompositionIdByName(compositionName);
        int collectionId = getCollectionIdByName(collectionName);

        if (compositionId == -1 || collectionId == -1) {
            logger.warn("Композицію або колекцію не знайдено ({} / {}).", compositionName, collectionName);
            return;
        }

        String sql = "INSERT INTO collection_compositions (composition_id, collection_id) VALUES (?, ?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, compositionId);
            pstmt.setInt(2, collectionId);
            pstmt.executeUpdate();
            logger.info("Композицію '{}' успішно додано до колекції '{}'.", compositionName, collectionName);
        } catch (SQLException e) {
            errorLogger.error("Помилка при додаванні композиції до колекції: {}", e.getMessage(), e);
        }
    }

    public void removeCompositionFromCollection(String compositionName, String collectionName) {
        int compositionId = getCompositionIdByName(compositionName);
        int collectionId = getCollectionIdByName(collectionName);

        if (compositionId == -1 || collectionId == -1) {
            logger.warn("Композицію або колекцію не знайдено для видалення ({} / {}).", compositionName, collectionName);
            return;
        }

        String sql = "DELETE FROM collection_compositions WHERE composition_id = ? AND collection_id = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, compositionId);
            pstmt.setInt(2, collectionId);
            pstmt.executeUpdate();
            logger.info("Композицію '{}' видалено з колекції '{}'.", compositionName, collectionName);
        } catch (SQLException e) {
            errorLogger.error("Помилка при видаленні композиції з колекції: {}", e.getMessage(), e);
        }
    }
}
