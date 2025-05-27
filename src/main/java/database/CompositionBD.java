package database;
import composition.Composition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompositionBD extends DatabaseManager {
    private static final Logger logger = LogManager.getLogger(CompositionBD.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    public static int insertComposition(String name, String style, int duration, String author, String lyrics, String audiopath) {
        String sql = "INSERT INTO compositions (name, style, duration, author, lyrics, audiopath) VALUES (?, ?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setString(2, style);
            pstmt.setInt(3, duration);
            pstmt.setString(4, author);
            pstmt.setString(5, lyrics);
            pstmt.setString(6, audiopath);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
                logger.info("Композицію '{}' успішно додано до бази, id = {}", name, generatedId);
            } else {
                logger.warn("Композицію '{}' не вдалося додати до бази", name);
            }
        } catch (SQLException e) {
            errorLogger.error("Помилка при додаванні композиції '{}': {}", name, e.getMessage(), e);
        }

        return generatedId;
    }

    public static boolean deleteComposition(String name) {
        String sql = "DELETE FROM compositions WHERE name = ?";
        boolean deleted = false;

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int affected = pstmt.executeUpdate();
            deleted = affected > 0;

            if (deleted) {
                logger.info("Композицію '{}' успішно видалено з бази.", name);
            } else {
                logger.warn("Композицію '{}' не знайдено для видалення.", name);
            }
        } catch (SQLException e) {
            errorLogger.error("Помилка при видаленні композиції '{}': {}", name, e.getMessage(), e);
        }

        return deleted;
    }

    public static List<Composition> getAllCompositions() {
        List<Composition> list = new ArrayList<>();
        String sql = "SELECT id, name, style, duration, author, lyrics, audiopath FROM compositions";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Composition comp = new Composition(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("style"),
                        rs.getInt("duration"),
                        rs.getString("author"),
                        rs.getString("lyrics"),
                        rs.getString("audiopath")
                );
                list.add(comp);
            }
            logger.info("Зчитано {} композицій з бази даних.", list.size());
        } catch (SQLException e) {
            errorLogger.error("Помилка при зчитуванні композицій: {}", e.getMessage(), e);
        }
        return list;
    }
}
