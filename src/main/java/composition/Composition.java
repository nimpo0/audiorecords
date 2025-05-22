package composition;

public class Composition {
    private int id;
    private String name;
    private String style;
    private int duration;
    private String author;
    private String lyrics;
    private String audioPath;

    public Composition() {}

    public Composition(int id, String name, String style, int duration, String author, String lyrics, String audioPath) {
        this.id = id;
        this.name = name;
        this.style = style;
        this.duration = duration;
        this.author = author;
        this.lyrics = lyrics;
        this.audioPath = audioPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStyle() {
        return style;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAuthor() {
        return author;
    }

    public String getLyrics() {
        return lyrics;
    }

    public String getAudioPath() {
        return audioPath;
    }

    @Override
    public String toString() {
        return "Composition{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", style='" + style + '\'' +
                ", duration=" + duration +
                ", author='" + author + '\'' +
                ", lyrics='" + lyrics + '\'' +
                ", audioPath='" + audioPath + '\'' +
                '}';
    }
}
