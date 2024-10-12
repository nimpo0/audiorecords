package commands;

import composition.ComposCollection;
import composition.Composition;

public class CalculateDuration implements Command {
    private ComposCollection collection;

    public CalculateDuration(ComposCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        if (collection.isEmpty()) {
            System.out.println("Збірка порожня.");
            return;
        }

        int totalDuration = 0;
        for (Composition comp : collection.getCompositions()) {
            totalDuration += comp.getComposDuration();
        }

        int minutes = totalDuration / 60;
        int seconds = totalDuration % 60;

        System.out.println("Загальна тривалість композицій у збірці: " + minutes + " хв " + seconds + " сек.");
    }

    @Override
    public String printInfo() {
        return "Порахувати тривалість збірки";
    }
}