package commands;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CriticalErrorTest {

    @Test
    void testExecute_DoesNotThrow() {
        CriticalError cmd = new CriticalError();
        assertDoesNotThrow(cmd::execute);
    }

    @Test
    void testPrintInfo_ReturnsCorrectString() {
        CriticalError cmd = new CriticalError();
        assertEquals("Симулювати критичну помилку (для перевірки email-сповіщень)", cmd.printInfo());
    }
}
