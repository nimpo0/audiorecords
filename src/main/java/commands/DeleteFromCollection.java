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
            System.out.println("The collection is empty.");
            return;
        }

        System.out.println("Enter the name of the composition you want to delete:");
        String name = scanner.nextLine();

        Composition delete = collection.findCompositionByName(name);

        if (delete != null) {
            collection.deleteComposition(delete);
            System.out.println("Composition \"" + name + "\" successfully deleted from the collection.");
        } else {
            System.out.println("Composition \"" + name + "\" not found in the collection.");
        }
    }

    @Override
    public String printInfo() {
        return "Delete a composition.";
    }
}
