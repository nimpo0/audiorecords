package commands;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CriticalError implements Command {
    private static final Logger errorLogger = LogManager.getLogger("ErrorLogger");

    @Override
    public void execute() {
        try {
            throw new RuntimeException("Це тестова критична помилка для надсилання повідомлення на пошту.");
        } catch (RuntimeException e) {
            errorLogger.error("Виникла критична помилка: {}", e.getMessage(), e);
        }
    }

    @Override
    public String printInfo() {
        return "Симулювати критичну помилку (для перевірки email-сповіщень)";
    }
}