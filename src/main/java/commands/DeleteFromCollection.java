package commands;

import database.CollectionBD;
import database.CollectionCompositionBD;
import composition.Composition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class DeleteFromCollection implements Command {
    private static final Logger logger = LogManager.getLogger(DeleteFromCollection.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private final Scanner scanner;
    private final CollectionBD collectionBD = new CollectionBD();
    private final CollectionCompositionBD relationBD = new CollectionCompositionBD();

    public DeleteFromCollection(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Enter the name of the collection:");
        String collectionName = scanner.nextLine().trim();
        int collectionId = relationBD.getCollectionIdByName(collectionName);

        if (collectionId == -1) {
            System.out.println("Collection not found.");
            return;
        }

        List<Composition> compositions = collectionBD.getCompositionsForCollection(collectionName);
        if (compositions.isEmpty()) {
            System.out.println("The collection is empty.");
            return;
        }

        System.out.println("Enter the name of the composition to delete:");
        String compositionName = scanner.nextLine().trim();

        boolean found = compositions.stream().anyMatch(c -> c.getName().equalsIgnoreCase(compositionName));
        if (!found) {
            System.out.println("Composition not found in the collection.");
            errorLogger.warn("Attempted to delete non-existent composition '{}' from '{}'.", compositionName, collectionName);
            return;
        }

        relationBD.removeCompositionFromCollection(compositionName, collectionName);
        System.out.println("✔ Composition \"" + compositionName + "\" removed from collection \"" + collectionName + "\".");
        logger.info("Composition '{}' removed from collection '{}'.", compositionName, collectionName);
    }

    @Override
    public String printInfo() {
        return "Delete a composition from a selected collection.";
    }
}
