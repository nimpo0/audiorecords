package commands;

import composition.ComposCollection;
import java.util.Scanner;

public class SaveToFile implements Command {
    private ComposCollection collection;
    private Scanner scanner;

    public SaveToFile(ComposCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
    }

    @Override
    public String printInfo() {
        return "Зберегти збірку на диск";
    }
}
