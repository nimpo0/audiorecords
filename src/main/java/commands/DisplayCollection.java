package commands;

import composition.Composition;
import database.CollectionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class DisplayCollection implements Command {
    private static final Logger logger = LogManager.getLogger(DisplayCollection.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private final Scanner scanner;
    private final CollectionBD collectionBD = new CollectionBD();

    public DisplayCollection(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Enter the name of the collection to display:");
        String collectionName = scanner.nextLine().trim();

        List<Composition> compositions = collectionBD.getCompositionsForCollection(collectionName);

        if (compositions.isEmpty()) {
            System.out.println("The collection '" + collectionName + "' is empty or does not exist.");
            logger.warn("Attempted to display the collection '{}', but it is empty or does not exist.", collectionName);
            return;
        }

        // Виведення таблиці
        String header = String.format("| %-20s | %-15s | %-15s | %-10s | %-30s |%n",
                "Title", "Style", "Author", "Duration", "Lyrics");
        String separator = "+----------------------+-----------------+-----------------+------------+--------------------------------+";

        System.out.println(separator);
        System.out.print(header);
        System.out.println(separator);

        for (Composition comp : compositions) {
            System.out.printf("| %-20s | %-15s | %-15s | %-10d | %-30s |%n",
                    comp.getName(),
                    comp.getStyle(),
                    comp.getAuthor(),
                    comp.getDuration(),
                    comp.getLyrics());
        }

        System.out.println(separator);
        logger.info("Displayed all compositions in the collection '{}'.", collectionName);
    }

    @Override
    public String printInfo() {
        return "Show all compositions from a selected collection.";
    }
}
