package commands;

import database.CollectionBD;

import java.util.Scanner;

public class CreateCollection implements Command {
    private final Scanner scanner;
    private final CollectionBD collectionBD = new CollectionBD();

    public CreateCollection(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter the name of the new collection: ");
        String collectionName = scanner.nextLine();

        collectionBD.insertCollection(collectionName);
    }

    @Override
    public String printInfo() {
        return "Create a new collection";
    }
}
