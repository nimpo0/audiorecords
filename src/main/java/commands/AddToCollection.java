package commands;

import composition.ComposCollection;
import composition.Composition;

import java.util.Scanner;

public class AddToCollection implements Command {
    private ComposCollection collection;
    private ComposCollection allCompositions;
    private Scanner scanner;

    public AddToCollection(ComposCollection collection, ComposCollection allCompositions, Scanner scanner) {
        this.collection = collection;
        this.allCompositions = allCompositions;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (allCompositions.isAllEmpty()) {
            System.out.println("No available compositions to add.");
            return;
        }

        System.out.println("Available compositions to add:");
        boolean hasCompos = false;
        for (Composition comp : allCompositions.getAllCompositions()) {
            if (!collection.containsComposition(comp)) {
                System.out.println("- " + comp.getCompositionName());
                hasCompos = true;
            }
        }

        if (!hasCompos) {
            System.out.println("All compositions are already added to the collection.");
            return;
        }

        System.out.println("Enter the name of the composition you want to add to the collection:");
        String name = scanner.nextLine();

        Composition compositionToAdd = allCompositions.findInAllCompositions(name);
        if (compositionToAdd != null) {
            collection.addComposition(compositionToAdd);
            System.out.println("Composition \"" + name + "\" successfully added to the collection.");
        } else {
            System.out.println("Composition not found or it is already in the collection.");
        }
    }

    @Override
    public String printInfo() {
        return "Add a composition to the collection.";
    }
}
