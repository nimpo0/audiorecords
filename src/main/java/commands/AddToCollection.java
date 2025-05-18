package commands;

import database.CollectionBD;
import database.CollectionCompositionBD;
import database.CompositionBD;
import composition.Composition;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class AddToCollection implements Command {
    private static final Logger logger = LogManager.getLogger(AddToCollection.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private final Scanner scanner;
    private final CollectionBD collectionBD = new CollectionBD();
    private final CompositionBD compositionBD = new CompositionBD();
    private final CollectionCompositionBD relationBD = new CollectionCompositionBD();

    public AddToCollection(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Enter the name of the collection you want to add to:");
        String collectionName = scanner.nextLine().trim();

        int collectionId = relationBD.getCollectionIdByName(collectionName);
        if (collectionId == -1) {
            System.out.println("Collection not found.");
            errorLogger.error("Collection '{}' not found.", collectionName);
            return;
        }

        List<Composition> allCompositions = CompositionBD.getAllCompositions();
        List<Composition> compositionsInCollection = collectionBD.getCompositionsForCollection(String.valueOf(collectionId));

        System.out.println("Available compositions to add:");
        boolean hasAvailable = false;
        for (Composition comp : allCompositions) {
            if (compositionsInCollection.stream().noneMatch(c -> c.getName().equalsIgnoreCase(comp.getName()))) {
                System.out.println("- " + comp.getName());
                hasAvailable = true;
            }
        }

        if (!hasAvailable) {
            System.out.println("All compositions are already in the collection.");
            return;
        }

        System.out.println("Enter the name of the composition to add:");
        String compositionName = scanner.nextLine().trim();

        int compositionId = relationBD.getCompositionIdByName(compositionName);
        if (compositionId == -1) {
            System.out.println("Composition not found.");
            errorLogger.error("Composition '{}' not found.", compositionName);
            return;
        }

        if (compositionsInCollection.stream().anyMatch(c -> c.getName().equalsIgnoreCase(compositionName))) {
            System.out.println("Composition is already in the collection.");
            return;
        }

        relationBD.addCompositionToCollection(compositionName, collectionName);
        System.out.println("Composition \"" + compositionName + "\" added to collection \"" + collectionName + "\".");
        logger.info("Composition '{}' added to collection '{}'.", compositionName, collectionName);
    }

    @Override
    public String printInfo() {
        return "Add a composition to a specific collection.";
    }

    public CompositionBD getCompositionBD() {
        return compositionBD;
    }
}
