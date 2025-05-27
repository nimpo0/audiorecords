package database;
import composition.Collection;
import composition.Composition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionBD extends DatabaseManager {
    private static final Logger logger = LogManager.getLogger(CollectionBD.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    public void insertCollection(String name) {
        String sql = "INSERT INTO collections (name) VALUES (?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            logger.info("Колекцію '{}' успішно додано до бази даних.", name);
        } catch (SQLException e) {
            errorLogger.error("Помилка під час додавання колекції '{}': {}", name, e.getMessage(), e);
        }
    }

    public void deleteCollection(String name) {
        String sql = "DELETE FROM collections WHERE name = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                logger.info("Колекцію '{}' успішно видалено з бази даних.", name);
            } else {
                logger.warn("Колекцію '{}' не знайдено для видалення.", name);
            }
        } catch (SQLException e) {
            errorLogger.error("Помилка під час видалення колекції '{}': {}", name, e.getMessage(), e);
        }
    }

    public static List<Collection> getAllCollections() {
        List<Collection> list = new ArrayList<>();
        String sql = "SELECT id, name FROM collections";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Collection col = new Collection(
                        rs.getInt("id"),
                        rs.getString("name")
                );
                list.add(col);
            }
        } catch (SQLException e) {
            Logger errorLogger = LogManager.getLogger("ErrorLogger");
            errorLogger.error("Помилка під час зчитування колекцій: {}", e.getMessage(), e);
        }
        return list;
    }

    public List<Composition> getCompositionsForCollection(String collectionName) {
        List<Composition> compositions = new ArrayList<>();
        String sql = """
            SELECT c.id, c.name, c.style, c.duration, c.author, c.lyrics, c.audiopath
            FROM compositions c
            JOIN collection_compositions cc ON c.id = cc.composition_id
            JOIN collections col ON col.id = cc.collection_id
            WHERE col.name = ?""";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, collectionName);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Composition composition = new Composition(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("style"),
                            rs.getInt("duration"),
                            rs.getString("author"),
                            rs.getString("lyrics"),
                            rs.getString("audiopath")
                    );
                    compositions.add(composition);
                }
            }
            logger.info("Знайдено {} композицій у колекції '{}'.", compositions.size(), collectionName);
        } catch (SQLException e) {
            errorLogger.error("Помилка при отриманні композицій для колекції '{}': {}", collectionName, e.getMessage(), e);
        }

        return compositions;
    }
}
