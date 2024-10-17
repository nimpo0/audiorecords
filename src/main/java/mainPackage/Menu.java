package mainPackage;

import commands.*;
import composition.ComposCollection;

import java.util.*;

public class Menu {
    private ComposCollection collection;
    private ComposCollection allCompositions;
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
                System.out.println("Exiting the program. Goodbye!");
                break;
            }

            Command command = commandMap.get(choice);
            if (command != null) {
                try {
                    command.execute();
                    System.out.println("Command executed successfully.");
                } catch (Exception e) {
                    System.out.println("Error");
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n\t\t\t\t  === MENU ===");
        System.out.println("==================================================");

        for (int key : commandMap.keySet()) {
            Command command = commandMap.get(key);
            System.out.println("\t" + key + ". " + command.printInfo());
        }

        System.out.println("\t0. Exit");
        System.out.println("==================================================");
        System.out.print("Choose an option (1-10): ");
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
        commandMap.put(9, new SaveToFile(collection));
        commandMap.put(10, new LoadFromFile(collection));
    }

    private int getUserChoice() {
        while (true) {
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice == 0 || commandMap.containsKey(choice)) {
                    return choice;
                } else {
                    System.out.print("Invalid choice. Please enter a number between 0 and 10: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input, please enter a whole: ");
            }
        }
    }
}
