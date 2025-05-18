package mainPackage;

import commands.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Menu {
    private static final Logger logger = LogManager.getLogger(Menu.class);
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    private final Scanner scanner;
    private final Map<Integer, Command> commandMap;

    public Menu() {
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
                logger.info("Exiting the program.");
                break;
            }

            Command command = commandMap.get(choice);
            if (command != null) {
                try {
                    command.execute();
                    System.out.println("Command executed successfully.");
                } catch (Exception e) {
                    System.out.println("Error executing command.");
                    errorLogger.error("Error executing command: {}", e.getMessage());
                }
            } else {
                System.out.println("Invalid choice. Please try again.");
                errorLogger.error("Invalid choice");
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
        System.out.print("Choose an option (0-" + commandMap.size() + "): ");
    }

    private void initializeCommands() {
        commandMap.put(1, new AddCompos(scanner));
        commandMap.put(2, new DisplayAllCompos());
        commandMap.put(3, new AddToCollection(scanner));
        commandMap.put(4, new DeleteFromCollection(scanner));
        commandMap.put(5, new DisplayCollection(scanner));
        commandMap.put(6, new CalculateDuration());
        commandMap.put(7, new SortingByStyle(scanner));
        commandMap.put(8, new FindCompositions(scanner));
        commandMap.put(9, new CriticalError());
        commandMap.put(10, new CreateCollection(scanner));
        commandMap.put(11, new DeleteCollection(scanner));
        commandMap.put(12, new DeleteCompos(scanner));
    }

    private int getUserChoice() {
        while (true) {
            String input = scanner.nextLine();
            try {
                int choice = Integer.parseInt(input);
                if (choice == 0 || commandMap.containsKey(choice)) {
                    return choice;
                } else {
                    System.out.print("Invalid choice. Please try again: ");
                    logger.warn("Invalid menu choice: {}", input);
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid input, please enter a number: ");
                errorLogger.error("Invalid input, not a number: {}", e.getMessage());
            }
        }
    }
}
