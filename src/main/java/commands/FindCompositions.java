package commands;

import composition.ComposCollection;
import composition.Composition;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FindCompositions implements Command {
    private ComposCollection allCompos;
    private Scanner scanner;

    public FindCompositions(ComposCollection allCompos, Scanner scanner) {
        this.allCompos = allCompos;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        if (allCompos.isAllEmpty()) {
            System.out.println("The collection is empty.");
            return;
        }

        int minDuration = getMinDuration();
        int maxDuration = getMaxDuration(minDuration);

        List<Composition> foundCompos = new ArrayList<>();
        for (Composition comp : allCompos.getAllCompositions()) {
            int duration = comp.getComposDuration();
            if (duration >= minDuration && duration <= maxDuration) {
                foundCompos.add(comp);
            }
        }

        if (foundCompos.isEmpty()) {
            System.out.println("No compositions found within the specified duration range.");
        } else {
            System.out.println("Found compositions:");
            System.out.println("+----------------------+-----------------+-----------------+------------+--------------------------------+");
            for (Composition comp : foundCompos) {
                System.out.println(comp);
            }
        }
    }

    @Override
    public String printInfo() {
        return "Find compositions by duration range.";
    }

    private int getMinDuration() {
        int minDur = -1;

        while (minDur < 0) {
            System.out.println("Enter the minimum duration of the composition (in seconds):");
            try {
                String input = scanner.nextLine();
                minDur = Integer.parseInt(input);
                if (minDur < 0) {
                    System.out.println("Duration cannot be negative. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a whole number.");
            }
        }

        return minDur;
    }

    private int getMaxDuration(int minDur) {
        int maxDur = -1;

        while (maxDur < minDur) {
            System.out.println("Enter the maximum duration (in seconds):");
            try {
                String input = scanner.nextLine();
                maxDur = Integer.parseInt(input);
                if (maxDur < minDur) {
                    System.out.println("Maximum duration cannot be less than the minimum. Please try again.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please enter a whole number.");
            }
        }

        return maxDur;
    }
}
