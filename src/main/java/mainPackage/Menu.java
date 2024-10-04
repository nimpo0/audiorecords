package mainPackage;

import commands.*;
import composition.ComposCollection;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private ComposCollection collection;
    private Invoker invoker;
    private Scanner scanner;
    private Map<Integer, Command> commandMap;

    public Menu() {
        this.collection = new ComposCollection();
        this.invoker = new Invoker();
        this.scanner = new Scanner(System.in);
        this.commandMap = new HashMap<>();
        initializeCommands();
    }

    // Метод для запуску меню
    public void start() {
        while (true) {
            System.out.println("\n\t=== МЕНЮ ===");
            System.out.println("\t1. Додати нову композицію в збірку");
            System.out.println("\t2. Видалити композицію");
            System.out.println("\t3. Показати всі композиції");
            System.out.println("\t4. Порахувати тривалість збірки");
            System.out.println("\t5. Сортувати композиції за стилем");
            System.out.println("\t6. Знайти композиції за діапазоном тривалості");
            System.out.println("\t7. Зберегти збірку на диск");
            System.out.println("\t8. Завантажити збірку з диска");
            System.out.println("\t9. Вихід");
            System.out.print("Оберіть опцію (1-9): ");

            int choice = getUserChoice();

            if (choice == 9) {
                break;
            }

            Command command = commandMap.get(choice);
            if (command != null) {
                invoker.executeCommand(command);
            } else {
                System.out.println("Неправильний вибір. Спробуйте ще раз.");
            }
        }
    }

    private void initializeCommands() {
        commandMap.put(1, new AddToCollection(collection, scanner));
        commandMap.put(2, new DeleteFromCollection(collection, scanner));
        commandMap.put(3, new DisplayComposition(collection));
        commandMap.put(4, new CalculateDuration(collection));
        commandMap.put(5, new SortingByStyle(collection));
        commandMap.put(6, new FindComposition(collection, scanner));
        commandMap.put(7, new SaveCollectionCommand(collection, scanner));
        commandMap.put(8, new LoadCollectionCommand(collection, scanner));
    }

    private int getUserChoice() {
        int choice = -1;
        while (true) {
            String input = scanner.nextLine();
            try {
                choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 9) {
                    break;
                } else {
                    System.out.print("Неправильний вибір. Введіть число від 1 до 9: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Неправильний вибір. Введіть число від 1 до 9: ");
            }
        }
        return choice;
    }
}
