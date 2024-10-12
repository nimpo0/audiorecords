package commands;

import composition.ComposCollection;
import composition.Composition;

import java.util.Scanner;

public class AddCompos implements Command {
    private ComposCollection allCompos;
    private Scanner scanner;

    public AddCompos(ComposCollection allCollection, Scanner scanner) {
        this.allCompos = allCollection;
        this.scanner = scanner;
    }

    @Override
    public void execute() {
        System.out.println("Введіть назву композиції:");
        String name = scanner.nextLine();

        System.out.println("Введіть стиль композиції:");
        String style = scanner.nextLine();

        System.out.println("Введіть ім'я автора:");
        String author = scanner.nextLine();

        int duration = getDurationNum();

        System.out.println("Введіть текст пісні:");
        String lyrics = scanner.nextLine();

        Composition newComposition = new Composition(name, style, author, duration, lyrics);
        allCompos.addToAllCompositions(newComposition);
        System.out.println("Композицію успішно додано.");
    }

    @Override
    public String printInfo() {
        return "Додати нову композицію.";
    }

    private int getDurationNum() {
        int dur = -1;
        while (dur <= 0) {
            System.out.println("Введіть тривалість композиції (у секундах):");
            try {
                String input = scanner.nextLine();
                dur = Integer.parseInt(input);
                if (dur <= 0) {
                    System.out.println("Тривалість повинна бути додатним числом. Спробуйте ще раз.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Неправильно, будь ласка, введіть ціле число.");
            }
        }
        return dur;
    }
}
