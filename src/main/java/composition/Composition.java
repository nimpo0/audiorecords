package composition;

public class Composition {
    private String compositionName;
    private String style;
    private String author;
    private int compositionDuration;
    private String lyrics;

    public Composition(String compositionName, String style, String author, int compositionDuration, String lyrics) {
        this.compositionName = compositionName;
        this.style = style;
        this.author = author;
        this.compositionDuration = compositionDuration;
        this.lyrics = lyrics;
    }

    public String getCompositionName() {
        return compositionName;
    }

    public String getStyle() {
        return style;
    }

    public String getAuthor() {
        return author;
    }

    public int getCompositionDuration() {
        return compositionDuration;
    }

    public String getLyrics() {
        return lyrics;
    }

    public void setCompositionName(String compositionName) {
        this.compositionName = compositionName;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setCompositionDuration(int compositionDuration) {
        this.compositionDuration = compositionDuration;
    }

    public void setLyrics(String lyrics) {
        this.lyrics = lyrics;
    }

    public String toString() {
        return "Composition{" +
                "compositionName='" + compositionName + '\'' +
                ", style='" + style + '\'' +
                ", author='" + author + '\'' +
                ", compositionDuration=" + compositionDuration +
                ", lyrics='" + lyrics + '\'' +
                '}';
    }
}

