package commands;

import composition.Composition;
import database.CompositionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FindCompositions implements Command {
    private static final Logger logger = LogManager.getLogger(FindCompositions.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private Scanner scanner;
    private CompositionBD compositionBD = new CompositionBD();

    public FindCompositions(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        List<Composition> allCompositions = CompositionBD.getAllCompositions();

        if (allCompositions.isEmpty()) {
            System.out.println("There are no compositions in the database.");
            logger.warn("Attempted to search compositions, but the database is empty.");
            return;
        }

        int minDuration = getMinDuration();
        int maxDuration = getMaxDuration(minDuration);

        List<Composition> foundCompos = new ArrayList<>();
        for (Composition comp : allCompositions) {
            int duration = comp.getDuration();
            if (duration >= minDuration && duration <= maxDuration) {
                foundCompos.add(comp);
            }
        }

        if (foundCompos.isEmpty()) {
            System.out.println("No compositions found within the specified duration range.");
            logger.warn("No compositions found for duration range {} to {} seconds.", minDuration, maxDuration);
        } else {
            System.out.println("Found compositions:");
            System.out.println("+----------------------+-----------------+-----------------+------------+--------------------------------+");
            for (Composition comp : foundCompos) {
                System.out.println(comp);
            }
            logger.info("Found {} compositions for duration range {} to {} seconds.", foundCompos.size(), minDuration, maxDuration);
        }
    }

    @Override
    public String printInfo() {
        return "Find compositions by duration range.";
    }

    private int getMinDuration() {
        int minDur = -1;
        while (minDur < 0) {
            System.out.println("Enter the minimum duration of the composition (in seconds):");
            try {
                String input = scanner.nextLine();
                minDur = Integer.parseInt(input);
                if (minDur < 0) {
                    System.out.println("Duration cannot be negative. Please try again.");
                    logger.warn("Invalid minimum duration input: {}", input);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a whole number.");
                errorLogger.error("Invalid input for minimum duration: {}", e.getMessage());
            }
        }
        return minDur;
    }

    private int getMaxDuration(int minDur) {
        int maxDur = -1;
        while (maxDur < minDur) {
            System.out.println("Enter the maximum duration (in seconds):");
            try {
                String input = scanner.nextLine();
                maxDur = Integer.parseInt(input);
                if (maxDur < minDur) {
                    System.out.println("Maximum duration cannot be less than the minimum. Please try again.");
                    logger.warn("Invalid maximum duration input: {}. It cannot be less than the minimum duration of {}.", input, minDur);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a whole number.");
                errorLogger.error("Invalid input for maximum duration: {}", e.getMessage());
            }
        }
        return maxDur;
    }

    public CompositionBD getCompositionBD() {
        return compositionBD;
    }

    public void setCompositionBD(CompositionBD compositionBD) {
        this.compositionBD = compositionBD;
    }
}
