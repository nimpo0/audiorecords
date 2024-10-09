package commands;

import composition.ComposCollection;

public class SortingByStyle implements Command {
    private ComposCollection collection;

    public SortingByStyle(ComposCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
    }
}
