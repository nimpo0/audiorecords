package mainPackage;

import commands.*;
import composition.ComposCollection;
import composition.Composition;

import java.util.*;

public class Menu {
    private ComposCollection collection; // Збірка
    private ComposCollection allCompositions; // Всі композиції
    private Scanner scanner;
    private Map<Integer, Command> commandMap;

    public Menu() {
        this.collection = new ComposCollection();
        this.allCompositions = new ComposCollection();
        this.scanner = new Scanner(System.in);
        this.commandMap = new HashMap<>();
        initializeCommands();
    }

    public void start() {

        while (true) {
            printMenu();
            int choice = getUserChoice();

            if (choice == 0) {
                break;
            }

            Command command = commandMap.get(choice);
            if (command != null) {
                command.execute();
            } else {
                System.out.println("Неправильний вибір. Спробуйте ще раз.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n\t\t\t\t  === МЕНЮ ===");
        System.out.println("==================================================");

        for (Map.Entry<Integer, Command> entry : commandMap.entrySet()) {
            System.out.println("\t" + entry.getKey() + ". " + entry.getValue().printInfo());
        }

        System.out.println("\t0. Вихід");
        System.out.println("==================================================");
        System.out.print("Оберіть опцію (1-10): ");
    }

    private void initializeCommands() {
        commandMap.put(1, new AddCompos(allCompositions, scanner));
        commandMap.put(2, new DisplayAllCompos(allCompositions));
        commandMap.put(3, new AddToCollection(collection, allCompositions, scanner));
        commandMap.put(4, new DeleteFromCollection(collection, scanner));
        commandMap.put(5, new DisplayCollection(collection));
        commandMap.put(6, new CalculateDuration(collection));
        commandMap.put(7, new SortingByStyle(collection, scanner));
        commandMap.put(8, new FindCompositions(collection, scanner));
        commandMap.put(9, new SaveToFile(collection, scanner));
        commandMap.put(10, new LoadFromFile(collection, scanner));
    }

    private int getUserChoice() {
        while (true) {
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice == 0 || commandMap.containsKey(choice)) {
                    return choice;
                } else {
                    System.out.print("Неправильний вибір. Введіть число від 0 до 10: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Неправильно, будь ласка, введіть ціле число: ");
            }
        }
    }
}
