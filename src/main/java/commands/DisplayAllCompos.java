package commands;

import composition.ComposCollection;

public class DisplayAllCompos implements Command {
    private ComposCollection allCompositions;

    public DisplayAllCompos(ComposCollection allCompositions) {
        this.allCompositions = allCompositions;
    }

    @Override
    public void execute() {
        if (allCompositions.isAllEmpty()) {
            System.out.println("Немає ніяких композицій.");
        } else {
            String header = String.format("| %-20s | %-15s | %-15s | %-10s | %-30s |%n",
                    "Назва", "Стиль", "Автор", "Тривалість", "Текст");
            String separator = "+----------------------+-----------------+-----------------+------------+--------------------------------+";
            System.out.println(separator);
            System.out.print(header);
            System.out.println(separator);

            // Виклик методу для виведення всіх композицій із колекції
            allCompositions.displayAllCompositions();
        }
    }

    @Override
    public String printInfo() {
        return "Показати всі композиції";
    }
}
