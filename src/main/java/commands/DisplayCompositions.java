package commands;

import composition.ComposCollection;

public class DisplayCompositions implements Command {
    private ComposCollection collection;

    public DisplayCompositions(ComposCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        collection.displayAllCompositions();
    }

    @Override
    public String printInfo() {
        return "Показати всі композиції";
    }
}
