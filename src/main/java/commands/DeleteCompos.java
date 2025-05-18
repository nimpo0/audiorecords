package commands;

import database.CompositionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class DeleteCompos implements Command {
    private static final Logger logger = LogManager.getLogger(DeleteCompos.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private final Scanner scanner;

    public DeleteCompos(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter the name of the composition to delete: ");
        String nameToDelete = scanner.nextLine();

        try {
            boolean deleted = CompositionBD.deleteComposition(nameToDelete);

            if (deleted) {
                logger.info("Composition '{}' successfully deleted from the database.", nameToDelete);
                System.out.println("Composition successfully deleted.");
            } else {
                logger.warn("Composition '{}' not found in the database.", nameToDelete);
                System.out.println("Composition not found in database.");
            }
        } catch (Exception e) {
            errorLogger.error("Error while deleting composition '{}': {}", nameToDelete, e.getMessage(), e);
            System.out.println("Error occurred while deleting composition. Check logs for more info.");
        }
    }

    @Override
    public String printInfo() {
        return "Delete a composition by name.";
    }
}
