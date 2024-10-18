package commands;

import composition.ComposCollection;
import composition.Composition;

import java.util.Comparator;
import java.util.Scanner;

public class SortingByStyle implements Command {
    private ComposCollection collection;
    private Scanner scanner;

    public SortingByStyle(ComposCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (collection.isEmpty()) {
            System.out.println("The collection is empty.");
            return;
        }

        int choice = getUserChoice();

        if (choice == 1) {
            collection.getCompositions().sort(Comparator.comparing(composition -> composition.getStyle()));
            System.out.println("Compositions sorted alphabetically.");
            System.out.println("+----------------------+-----------------+-----------------+------------+--------------------------------+");
        }
        else if (choice == 2) {
            collection.getCompositions().sort(Comparator.comparing(Composition::getStyle).reversed());
            System.out.println("Compositions sorted in reverse alphabetical order.");
            System.out.println("+----------------------+-----------------+-----------------+------------+--------------------------------+");
        }
        else {
            System.out.println("Invalid choice, please try again.");
            return;
        }

        collection.displayCompositions();
    }

    @Override
    public String printInfo() {
        return "Sort compositions by style in collection.";
    }

    private int getUserChoice() {
        int choice = -1;
        while (choice != 1 && choice != 2) {
            System.out.println("Choose sorting order:");
            System.out.println("1. Alphabetically");
            System.out.println("2. In reverse alphabetical order");

            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
                if (choice != 1 && choice != 2) {
                    System.out.println("Invalid input, please enter 1 or 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a whole number.");
            }
        }
        return choice;
    }

}
