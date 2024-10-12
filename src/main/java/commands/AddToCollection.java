package commands;

import composition.ComposCollection;
import composition.Composition;

import java.util.Scanner;

public class AddToCollection implements Command {
    private ComposCollection collection;
    private Scanner scanner;

    public AddToCollection(ComposCollection collection, Scanner scanner) {
        this.collection = collection;
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
        collection.addComposition(newComposition);

        System.out.println("Композицію успішно додано до збірки.");
    }

    @Override
    public String printInfo() {
        return "Додати нову композицію в збірку";
    }

    private int getDurationNum(){
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
                System.out.println("Невірний формат числа. Спробуйте ще раз.");
            }
        }
        return dur;
    }
}
