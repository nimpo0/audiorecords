package commands;

import composition.ComposCollection;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Scanner;

public class LoadFromFile implements Command {
    private ComposCollection collection;
    private Scanner scanner;

    public LoadFromFile(ComposCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        String filename = "C:\\Users\\Admin\\Desktop\\disk.ser";

        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            ComposCollection loadedCollection = (ComposCollection) in.readObject();
            collection.getCompositions().clear();
            collection.getCompositions().addAll(loadedCollection.getCompositions());
            System.out.println("Збірку успішно завантажено з файлу \"" + filename + "\".");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Помилка при завантаженні файлу.");
        }
    }
    @Override
    public String printInfo() {
        return "Завантажити збірку з диска";
    }
}
