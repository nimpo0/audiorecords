package commands;

import composition.ComposCollection;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LoadFromFile implements Command {
    private ComposCollection collection;

    public LoadFromFile(ComposCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        String filename = "C:\\Users\\Admin\\Desktop\\disk.ser";

        try (FileInputStream fileIn = new FileInputStream(filename);
             ObjectInputStream in = new ObjectInputStream(fileIn)) {
            ComposCollection loadedCollection = (ComposCollection) in.readObject();
            collection.getCompositions().addAll(loadedCollection.getCompositions());
            System.out.println("Collection successfully loaded from file \"" + filename + "\".");
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error while loading the file.");
        }
    }


    @Override
    public String printInfo() {
        return "Load the collection from disk.";
    }
}
