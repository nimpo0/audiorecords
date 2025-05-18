package commands;

import composition.Composition;
import database.CollectionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Scanner;

public class CalculateDuration implements Command {
    private static final Logger logger = LogManager.getLogger(CalculateDuration.class);
    private CollectionBD collectionBD = new CollectionBD();

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the name of the collection:");
        String collectionName = scanner.nextLine();

        List<Composition> compositions = collectionBD.getCompositionsForCollection(collectionName);

        if (compositions.isEmpty()) {
            System.out.println("The collection '" + collectionName + "' is empty or does not exist.");
            logger.warn("Attempted to calculate duration, but the collection '{}' is empty or does not exist.", collectionName);
            return;
        }

        int totalDuration = compositions.stream()
                .mapToInt(Composition::getDuration)
                .sum();

        int minutes = totalDuration / 60;
        int seconds = totalDuration % 60;

        System.out.println("Total duration in the collection '" + collectionName + "': " + minutes + " min " + seconds + " sec.");
        logger.info("Total duration in collection '{}' calculated: {} min {} sec.", collectionName, minutes, seconds);
    }

    @Override
    public String printInfo() {
        return "Calculate the total duration of the selected collection.";
    }
}
