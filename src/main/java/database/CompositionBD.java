package database;

import composition.Composition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompositionBD extends DatabaseManager {

    public static int insertComposition(String name, String style, int duration, String author, String lyrics) {
        String sql = "INSERT INTO compositions (name, style, duration, author, lyrics) VALUES (?, ?, ?, ?, ?)";
        int generatedId = -1;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setString(2, style);
            pstmt.setInt(3, duration);
            pstmt.setString(4, author);
            pstmt.setString(5, lyrics);

            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    generatedId = rs.getInt(1);
                }
            }
            System.out.println("✔ Composition inserted successfully");
        } catch (SQLException e) {
            System.err.println("Error inserting composition: " + e.getMessage());
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
        } catch (SQLException e) {
            System.err.println("Error deleting composition: " + e.getMessage());
        }

        return deleted;
    }

    public static List<Composition> getAllCompositions() {
        List<Composition> list = new ArrayList<>();
        String sql = "SELECT id, name, style, duration, author, lyrics FROM compositions";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Composition comp = new Composition(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("style"),
                        rs.getInt("duration"),
                        rs.getString("author"),
                        rs.getString("lyrics")
                );
                list.add(comp);
            }
        } catch (SQLException e) {
            System.err.println("Error reading compositions: " + e.getMessage());
        }
        return list;
    }

    public static Composition getCompositionById(int id) {
        String sql = "SELECT * FROM compositions WHERE id = ?";
        Composition composition = null;

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                composition = new Composition(
                        id,
                        rs.getString("name"),
                        rs.getString("style"),
                        rs.getInt("duration"),
                        rs.getString("author"),
                        rs.getString("lyrics")
                );
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving composition by ID: " + e.getMessage());
        }

        return composition;
    }
}
