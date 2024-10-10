package commands;

import composition.ComposCollection;
import java.util.Scanner;

public class LoadFromFile implements Command {
    private ComposCollection collection;
    private Scanner scanner;

    public LoadFromFile(ComposCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
    }

    @Override
    public String printInfo() {
        return "Завантажити збірку з диска";
    }
}
