package composition;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ComposCollection implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private List<Composition> compositions;
    private List<Composition> allCompositions;

    public ComposCollection() {
        this.compositions = new ArrayList<>();
        this.allCompositions = new ArrayList<>();
    }

    public List<Composition> getCompositions() {
        return compositions;
    }

    public List<Composition> getAllCompositions() {
        return allCompositions;
    }

    public void addComposition(Composition composition) {
        compositions.add(composition);
    }

    public void addToAllCompositions(Composition composition) {
        allCompositions.add(composition);
    }

    public void deleteComposition(Composition composition) {
        compositions.remove(composition);
    }

    public Composition findCompositionByName(String name) {
        for (Composition comp : compositions) {
            if (comp.getCompositionName().equals(name)) {
                return comp;
            }
        }
        return null;
    }

    public Composition findInAllCompositions(String name) {
        for (Composition comp : allCompositions) {
            if (comp.getCompositionName().equals(name)) {
                return comp;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return compositions.isEmpty();
    }

    public boolean isAllEmpty() {
        return allCompositions.isEmpty();
    }

    public boolean containsComposition(Composition composition) {
        return !compositions.contains(composition);
    }

    public void displayCompositions() {
        if (compositions.isEmpty()) {
            System.out.println("Збірка порожня.");
        } else {
            for (Composition comp : compositions) {
                System.out.println(comp);
            }
        }
    }

    public void displayAllCompositions() {
        if (allCompositions.isEmpty()) {
            System.out.println("Немає доступних композицій.");
        } else {
            for (Composition comp : allCompositions) {
                System.out.println(comp);
            }
        }
    }
}
