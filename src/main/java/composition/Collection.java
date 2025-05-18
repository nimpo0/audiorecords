package composition;

import java.util.ArrayList;
import java.util.List;

public class Collection {
    private int id;
    private String name;
    private List<Composition> compositions = new ArrayList<>();

    public Collection() {}

    public Collection(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Composition> getCompositions() {
        return compositions;
    }

    public void setCompositions(List<Composition> compositions) {
        this.compositions = compositions;
    }

    @Override
    public String toString() {
        return "Collection{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
