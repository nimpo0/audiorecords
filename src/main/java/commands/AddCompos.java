package commands;

import database.CompositionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class AddCompos implements Command {
    private static final Logger logger = LogManager.getLogger(AddCompos.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private final Scanner scanner;

    public AddCompos(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        try {
            System.out.println("Enter the composition name:");
            String name = scanner.nextLine();

            System.out.println("Enter the composition style:");
            String style = scanner.nextLine();

            System.out.println("Enter the author's name:");
            String author = scanner.nextLine();

            int duration = getDurationNum();

            System.out.println("Enter the lyrics:");
            String lyrics = scanner.nextLine();

            int newId = CompositionBD.insertComposition(name, style, duration, author, lyrics);

            if (newId != -1) {
                logger.info("Composition '{}' successfully added with ID {}", name, newId);
                System.out.println("Composition successfully added.");
            } else {
                logger.warn("Failed to add composition '{}'", name);
                System.out.println("Failed to add composition.");
            }
        } catch (Exception e) {
            errorLogger.error("Error while adding composition: {}", e.getMessage(), e);
            System.out.println("An error occurred while adding the composition.");
        }
    }

    @Override
    public String printInfo() {
        return "Add a new composition.";
    }

    private int getDurationNum() {
        int dur = -1;
        while (dur <= 0) {
            System.out.println("Enter the composition duration (in seconds):");
            try {
                String input = scanner.nextLine();
                dur = Integer.parseInt(input);
                if (dur <= 0) {
                    System.out.println("Duration must be a positive number. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a whole number.");
                errorLogger.error("Invalid input for duration: '{}'", e.getMessage());
            }
        }
        return dur;
    }
}
