package commands;

import composition.ComposCollection;
import composition.Composition;

import java.util.Scanner;

public class DeleteFromCollection implements Command {
    private ComposCollection collection;
    private Scanner scanner;

    public DeleteFromCollection(ComposCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (collection.isEmpty()) {
            System.out.println("Колекція порожня.");
            return;
        }

        System.out.println("Введіть назву композиції, яку бажаєте видалити:");
        String name = scanner.nextLine();

        Composition toRemove = collection.findCompositionByName(name);

        if (toRemove != null) {
            collection.deleteComposition(toRemove);
            System.out.println("Композицію \"" + name + "\" успішно видалено з колекції.");
        } else {
            System.out.println("Композицію з назвою \"" + name + "\" не знайдено в колекції.");
        }
    }
}