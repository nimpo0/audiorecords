package commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CriticalError implements Command {
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    @Override
    public void execute() {
        try {
            throw new RuntimeException("This is a test critical error for email notification.");
        } catch (RuntimeException e) {
            errorLogger.error("Critical error occurred: {}", e.getMessage(), e);
        }
    }

    @Override
    public String printInfo() {
        return "Trigger critical error (for email notifications)";
    }
}
