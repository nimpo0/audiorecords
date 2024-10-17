package commands;

import composition.ComposCollection;

public class DisplayAllCompos implements Command {
    private ComposCollection allCompositions;

    public DisplayAllCompos(ComposCollection allCompositions) {
        this.allCompositions = allCompositions;
    }

    @Override
    public void execute() {
        if (allCompositions.isAllEmpty()) {
            System.out.println("There are no compositions.");
        } else {
            String header = String.format("| %-20s | %-15s | %-15s | %-10s | %-30s |%n",
                    "Title", "Style", "Author", "Duration", "Lyrics");
            String separator = "+----------------------+-----------------+-----------------+------------+--------------------------------+";
            System.out.println(separator);
            System.out.print(header);
            System.out.println(separator);
            allCompositions.displayAllCompositions();
        }
    }

    @Override
    public String printInfo() {
        return "Show all compositions.";
    }
}
