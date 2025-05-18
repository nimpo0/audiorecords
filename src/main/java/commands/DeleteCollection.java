package commands;

import database.CollectionBD;

import java.util.Scanner;

public class DeleteCollection implements Command {
    private final Scanner scanner;
    private final CollectionBD collectionBD = new CollectionBD();

    public DeleteCollection(Scanner scanner) {
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.print("Enter the name of the collection to delete: ");
        String collectionName = scanner.nextLine();

        collectionBD.deleteCollection(collectionName);
    }

    @Override
    public String printInfo() {
        return "Delete a collection";
    }
}
