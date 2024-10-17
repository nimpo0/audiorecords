package commands;

import composition.ComposCollection;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SaveToFile implements Command {
    private ComposCollection collection;

    public SaveToFile(ComposCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        String filename = "C:\\Users\\Admin\\Desktop\\disk.ser";

        try (FileOutputStream fileOut = new FileOutputStream(filename);
             ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(collection);
            System.out.println("Collection successfully saved to file \"" + filename + "\".");
        } catch (IOException e) {
            System.out.println("Error while saving the file.");
        }
    }

    @Override
    public String printInfo() {
        return "Save the collection to disk.";
    }
}

