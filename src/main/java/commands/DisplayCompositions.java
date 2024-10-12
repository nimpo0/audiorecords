package commands;

import composition.ComposCollection;

public class DisplayCompositions implements Command {
    private ComposCollection collection;

    public DisplayCompositions(ComposCollection collection) {
        this.collection = collection;
    }

    @Override
    public void execute() {
        if (collection.isEmpty()) {
            System.out.println("Колекція порожня.");
        }
        else {
            String header = String.format("| %-20s | %-15s | %-15s | %-10s | %-30s |%n",
                    "Назва", "Стиль", "Автор", "Тривалість", "Текст");
            String separator = "+----------------------+-----------------+-----------------+------------+--------------------------------+";
            System.out.println(separator);
            System.out.print(header);
            System.out.println(separator);
            collection.displayAllCompositions();
        }
    }

    @Override
    public String printInfo() {
        return "Показати всі композиції";
    }
}
