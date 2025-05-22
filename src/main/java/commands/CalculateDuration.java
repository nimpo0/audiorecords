package commands;
import composition.Composition;
import database.CollectionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

public class CalculateDuration {
    private static final Logger logger = LogManager.getLogger(CalculateDuration.class);
    CollectionBD collectionBD = new CollectionBD();

    public int getTotalDuration(String collectionName) {
        if (collectionName == null || collectionName.trim().isEmpty()) {
            logger.warn("Не вказано назву колекції для обчислення тривалості.");
            return -1;
        }

        List<Composition> compositions = collectionBD.getCompositionsForCollection(collectionName);

        if (compositions == null || compositions.isEmpty()) {
            logger.warn("Колекція '{}' порожня або не існує.", collectionName);
            return -1;
        }

        int total = compositions.stream()
                .mapToInt(Composition::getDuration)
                .sum();

        logger.info("Обчислено тривалість колекції '{}': {} сек.", collectionName, total);
        return total;
    }
}
