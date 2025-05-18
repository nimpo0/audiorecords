package database;

import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

public class CollectionCompositionBD extends DatabaseManager {

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
            System.err.println("PostgreSQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error retrieving id from " + tableName + ": " + e.getMessage());
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
            System.out.println("Composition or collection not found.");
            return;
        }

        String sql = "INSERT INTO collection_compositions (composition_id, collection_id) VALUES (?, ?)";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, compositionId);
            pstmt.setInt(2, collectionId);
            pstmt.executeUpdate();
            System.out.println("Composition added to collection successfully.");
        } catch (SQLException e) {
            System.err.println("Error adding composition to collection: " + e.getMessage());
        }
    }

    public void removeCompositionFromCollection(String compositionName, String collectionName) {
        int compositionId = getCompositionIdByName(compositionName);
        int collectionId = getCollectionIdByName(collectionName);

        if (compositionId == -1 || collectionId == -1) {
            System.out.println("Composition or collection not found.");
            return;
        }

        String sql = "DELETE FROM collection_compositions WHERE composition_id = ? AND collection_id = ?";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, compositionId);
            pstmt.setInt(2, collectionId);
            pstmt.executeUpdate();
            System.out.println("Composition removed from collection.");
        } catch (SQLException e) {
            System.err.println("Error removing composition from collection: " + e.getMessage());
        }
    }

    public Map<String, List<Integer>> getCompositionsForCollection() {
        Map<String, List<Integer>> map = new HashMap<>();
        String sql = "SELECT c.name AS collection_name, cc.composition_id FROM collection_compositions cc JOIN collections c ON cc.collection_id = c.id";

        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String colName = rs.getString("collection_name");
                int compId = rs.getInt("composition_id");
                map.computeIfAbsent(colName, k -> new ArrayList<>()).add(compId);
            }
        } catch (SQLException e) {
            System.err.println("Error reading collection-composition links: " + e.getMessage());
        }

        return map;
    }
}
