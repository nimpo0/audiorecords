package testutil;
import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.testfx.framework.junit5.ApplicationTest;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public abstract class BaseFxTest extends ApplicationTest {

    private static boolean fxInitialized = false;

    @BeforeAll
    static void initToolkit() throws Exception {
        if (!fxInitialized) {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.startup(latch::countDown);
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("JavaFX toolkit didn't start in time");
            }
            fxInitialized = true;
        }
    }

    protected void runOnFxThreadAndWait(Runnable runnable) throws InterruptedException {
        if (Platform.isFxApplicationThread()) {
            runnable.run();
        } else {
            CountDownLatch latch = new CountDownLatch(1);
            Platform.runLater(() -> {
                runnable.run();
                latch.countDown();
            });
            if (!latch.await(5, TimeUnit.SECONDS)) {
                throw new RuntimeException("Timeout on JavaFX thread");
            }
        }
    }
}
