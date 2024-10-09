package composition;

public class Composition{
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
        return "Композиція - '" + compositionName + '\'' +
                " | стиль - '" + style + '\'' +
                " | автор - '" + author + '\'' +
                " | триваліть - " + composDuration +
                " | текст композиції - '" + lyrics + '\'' +
                " |";
    }
}

