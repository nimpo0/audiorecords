package commands;

import composition.ComposCollection;
import composition.Composition;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FindCompositions implements Command {
    private ComposCollection collection;
    private Scanner scanner;

    public FindCompositions(ComposCollection collection, Scanner scanner) {
        this.collection = collection;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (collection.isEmpty()) {
            System.out.println("Збірка порожня.");
            return;
        }

        int minDuration = getMinDuration();
        int maxDuration = getMaxDuration(minDuration);

        List<Composition> foundCompos = new ArrayList<>();
        for (Composition comp : collection.getCompositions()) {
            int duration = comp.getComposDuration();
            if (duration >= minDuration && duration <= maxDuration) {
                foundCompos.add(comp);
            }
        }

        if (foundCompos.isEmpty()) {
            System.out.println("Композицій з тривалістю в заданому діапазоні не знайдено.");
        } else {
            System.out.println("Знайдені композиції:");
            for (Composition comp : foundCompos) {
                System.out.println(comp);
            }
        }
    }

    @Override
    public String printInfo() {
        return "Знайти композиції за діапазоном тривалості";
    }

    private int getMinDuration() {
        int minDur = -1;

        while (minDur < 0) {
            System.out.println("Введіть мінімальну тривалість композиції (у секундах):");
            try {
                String input = scanner.nextLine();
                minDur = Integer.parseInt(input);
                if (minDur < 0) {
                    System.out.println("Тривалість не може бути від'ємною. Спробуйте ще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неправильно, будь ласка, введіть ціле число.");
            }
        }

        return minDur;
    }

    private int getMaxDuration(int minDur) {
        int maxDur = -1;

        while (maxDur < minDur) {
            System.out.println("Введіть максимальну тривалість (у секундах):");
            try {
                String input = scanner.nextLine();
                maxDur = Integer.parseInt(input);
                if (maxDur < minDur) {
                    System.out.println("Максимальна тривалість не може бути меншою за мінімальну. Спробуйте ще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неправильно, будь ласка, введіть ціле число.");
            }
        }

        return maxDur;
    }
}
