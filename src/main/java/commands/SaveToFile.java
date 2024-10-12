package commands;

import composition.ComposCollection;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Scanner;

public class SaveToFile implements Command {
    private ComposCollection collection;
    private Scanner scanner;

    public SaveToFile(ComposCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        String filename = "C:\\Users\\Admin\\Desktop\\disk.ser";

        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(collection);
            System.out.println("Збірку успішно збережено у файл \"" + filename + "\".");
        } catch (IOException e) {
            System.out.println("Помилка при збереженні файлу.");
        }
    }

    @Override
    public String printInfo() {
        return "Зберегти збірку на диск";
    }
}
