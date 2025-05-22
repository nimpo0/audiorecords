package testutil;
import composition.Composition;
import java.util.List;

public class DisplayHelper {
    public static String formatDuration(List<Composition> compositions) {
        int total = 0;
        for (Composition c : compositions) {
            total += c.getDuration();
        }
        return "Duration: " + total + " sec";
    }
}
