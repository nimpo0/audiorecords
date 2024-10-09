package composition;

import java.util.ArrayList;
import java.util.List;

public class ComposCollection {
    private List<Composition> compositions;

    public ComposCollection() {
        this.compositions = new ArrayList<>();
    }

    public List<Composition> getCompositions() {
        return compositions;
    }

    public void addComposition(Composition composition) {
        compositions.add(composition);
    }

    public void deleteComposition(Composition composition) {
        compositions.remove(composition);
    }

    public Composition findCompositionByName(String name) {
        for (Composition comp : compositions) {
            if (comp.getCompositionName().equalsIgnoreCase(name)) {
                return comp;
            }
        }
        return null;
    }

    public boolean isEmpty() {
        return compositions.isEmpty();
    }

    public void displayAllCompositions() {
        if (compositions.isEmpty()) {
            System.out.println("Колекція порожня.");
        } else {
            System.out.println("Список композицій у збірці:");
            for (Composition comp : compositions) {
                System.out.println(comp);
            }
        }
    }
}
