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
            System.out.println("Немає доступних композицій для додавання.");
            return;
        }

        System.out.println("Доступні композиції для додавання:");
        boolean hasCompos = false;
        for (Composition comp : allCompositions.getAllCompositions()) {
            if (collection.containsComposition(comp)) {
                System.out.println("- " + comp.getCompositionName());
                hasCompos = true;
            }
        }

        if (!hasCompos) {
            System.out.println("Всі композиції вже додані до збірки.");
            return;
        }

        System.out.println("Введіть назву композиції, яку бажаєте додати до збірки:");
        String name = scanner.nextLine();

        Composition compositionToAdd = allCompositions.findInAllCompositions(name);
        if (compositionToAdd != null && collection.containsComposition(compositionToAdd)) {
            collection.addComposition(compositionToAdd);
            System.out.println("Композицію \"" + name + "\" успішно додано до збірки.");
        } else {
            System.out.println("Композицію не знайдено або вона вже є у збірці.");
        }

    }

    @Override
    public String printInfo() {
        return "Додати композицію до збірки";
    }
}
