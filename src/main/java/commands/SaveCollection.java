package commands;

import composition.ComposCollection;
import java.util.Scanner;

public class SaveCollection implements Command {
    private ComposCollection collection;
    private Scanner scanner;

    public SaveCollection(ComposCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
    }
}
