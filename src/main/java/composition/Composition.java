package composition;

import java.io.Serial;
import java.io.Serializable;

public class Composition implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String compositionName;
    private String style;
    private String author;
    private int composDuration;
    private String lyrics;

    public Composition(String compositionName, String style, String author, int compositionDuration, String lyrics) {
        this.compositionName = compositionName;
        this.style = style;
        this.author = author;
        this.composDuration = compositionDuration;
        this.lyrics = lyrics;
        System.out.println("Створено композицію: " + compositionName);
    }

    public String getCompositionName() {
        return compositionName;
    }

    public String getStyle() {
        return style;
    }

    public int getComposDuration() {
        return composDuration;
    }

    public String toString() {
        String comp = String.format("| %-20s | %-15s | %-15s | %-10s | %-30s |%n",
                compositionName, style, author, composDuration + " с", lyrics);
        String separator = "+----------------------+-----------------+-----------------+------------+--------------------------------+";
        return comp + separator;
    }
}
