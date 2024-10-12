package mainPackage;

import commands.*;
import composition.ComposCollection;

import java.util.*;

public class Menu {
    private ComposCollection collection;
    private Scanner scanner;
    private Map<Integer, Command> commandMap;

    public Menu() {
        this.collection = new ComposCollection();
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

        List<Integer> keys = new ArrayList<>(commandMap.keySet());

        for (Integer key : keys) {
            Command command = commandMap.get(key);
            System.out.println("\t" + key + ". " + command.printInfo());
        }

        System.out.println("\t0. Вихід");
        System.out.println("==================================================");
        System.out.print("Оберіть опцію (1-8): ");
    }

    private void initializeCommands() {
        commandMap.put(1, new AddToCollection(collection, scanner));
        commandMap.put(2, new DeleteFromCollection(collection, scanner));
        commandMap.put(3, new DisplayCompositions(collection));
        commandMap.put(4, new CalculateDuration(collection));
        commandMap.put(5, new SortingByStyle(collection, scanner));
        commandMap.put(6, new FindCompositions(collection, scanner));
        commandMap.put(7, new SaveToFile(collection, scanner));
        commandMap.put(8, new LoadFromFile(collection, scanner));
    }

    private int getUserChoice() {
        while (true) {
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice == 0 || commandMap.containsKey(choice)) {
                    return choice;
                } else {
                    System.out.print("Неправильний вибір. Введіть число, яке відповідає опціям меню (0-8): ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Некоректне введення. Будь ласка, введіть ціле число: ");
            }
        }
    }
}
