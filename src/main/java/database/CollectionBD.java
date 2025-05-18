package database;

import composition.Collection;
import composition.Composition;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionBD extends DatabaseManager {

    public void insertCollection(String name) {
        String sql = "INSERT INTO collections (name) VALUES (?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            pstmt.executeUpdate();
            System.out.println("✔ Collection inserted successfully");
        } catch (SQLException e) {
            System.err.println("❌ Error inserting collection: " + e.getMessage());
        }
    }

    public void deleteCollection(String name) {
        String sql = "DELETE FROM collections WHERE name = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, name);
            int affected = pstmt.executeUpdate();
            if (affected > 0) {
                System.out.println("✔ Collection deleted successfully");
            } else {
                System.out.println("⚠ No collection found with that name.");
            }
        } catch (SQLException e) {
            System.err.println("Error deleting collection: " + e.getMessage());
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
            System.err.println("Error reading collections: " + e.getMessage());
        }
        return list;
    }

    public List<Composition> getCompositionsForCollection(String collectionName) {
        List<Composition> compositions = new ArrayList<>();
        String sql = """
            SELECT c.id, c.name, c.style, c.duration, c.author, c.lyrics
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
                            rs.getString("lyrics")
                    );
                    compositions.add(composition);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching compositions for collection '" + collectionName + "': " + e.getMessage());
        }

        return compositions;
    }
}
