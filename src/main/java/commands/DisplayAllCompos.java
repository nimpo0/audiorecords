package commands;

import composition.Composition;
import database.CompositionBD;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class DisplayAllCompos implements Command {
    private static final Logger logger = LogManager.getLogger(DisplayAllCompos.class);

    @Override
    public void execute() {
        List<Composition> compositions = CompositionBD.getAllCompositions();

        if (compositions.isEmpty()) {
            System.out.println("There are no compositions.");
            logger.warn("No compositions available to display.");
            return;
        }

        String header = String.format("| %-20s | %-15s | %-15s | %-10s | %-30s |%n",
                "Title", "Style", "Author", "Duration", "Lyrics");
        String separator = "+----------------------+-----------------+-----------------+------------+--------------------------------+";

        System.out.println(separator);
        System.out.print(header);
        System.out.println(separator);

        for (Composition c : compositions) {
            System.out.printf("| %-20s | %-15s | %-15s | %-10d | %-30s |%n",
                    c.getName(),
                    c.getStyle(),
                    c.getAuthor(),
                    c.getDuration(),
                    c.getLyrics().length() > 30 ? c.getLyrics().substring(0, 27) + "..." : c.getLyrics());
        }

        System.out.println(separator);
        logger.info("Displayed all compositions.");
    }

    @Override
    public String printInfo() {
        return "Show all compositions.";
    }
}
