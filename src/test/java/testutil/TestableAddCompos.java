package testutil;
import commands.AddCompos;

public class TestableAddCompos extends AddCompos {
    public String lastAlertMessage = null;

    public TestableAddCompos() {
    }

    @Override
    protected void showAlert(String message) {
        lastAlertMessage = message;
    }
}
