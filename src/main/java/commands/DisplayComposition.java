package commands;

import composition.ComposCollection;
import mainPackage.Command;

public class DisplayComposition implements Command {
    private ComposCollection collection;

    public DisplayComposition(ComposCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        collection.displayAllCompositions();
    }
}
