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
            System.out.println("Колекція порожня. Немає композицій для сортування.");
            return;
        }

        int choice = getUserChoice();

        if (choice == 1) {
            collection.getCompositions().sort(Comparator.comparing(Composition::getStyle));
            System.out.println("Композиції відсортовано за алфавітом.");
        }
        else if (choice == 2) {
            collection.getCompositions().sort(Comparator.comparing(Composition::getStyle).reversed());
            System.out.println("Композиції відсортовано проти алфавіту.");
        }
        else {
            System.out.println("Невірно, будь ласка, спробуйте ще раз.");
            return;
        }

        collection.displayAllCompositions();
    }

    @Override
    public String printInfo() {
        return "Сортувати композиції за стилем";
    }

    private int getUserChoice() {
        int choice = -1;
        while (choice != 1 && choice != 2) {
            System.out.println("Оберіть порядок сортування:");
            System.out.println("1. За алфавітом");
            System.out.println("2. Проти алфавіту");

            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
                if (choice != 1 && choice != 2) {
                    System.out.println("Неправильно, будь ласка, введіть 1 або 2.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неправильно, будь ласка, введіть ціле число.");
            }
        }
        return choice;
    }

}
