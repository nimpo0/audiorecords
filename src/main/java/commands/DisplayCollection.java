package commands;

import composition.ComposCollection;

public class DisplayCollection implements Command {
    private ComposCollection collection;

    public DisplayCollection(ComposCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        if (collection.isEmpty()) {
            System.out.println("The collection is empty.");
        }
        else {
            String header = String.format("| %-20s | %-15s | %-15s | %-10s | %-30s |%n",
                    "Title", "Style", "Author", "Duration", "Lyrics");
            String separator = "+----------------------+-----------------+-----------------+------------+--------------------------------+";
            System.out.println(separator);
            System.out.print(header);
            System.out.println(separator);
            collection.displayCompositions();
        }
    }

    @Override
    public String printInfo() {
        return "Show all compositions from the collection.";
    }
}
