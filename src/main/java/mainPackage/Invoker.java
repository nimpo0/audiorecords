package mainPackage;

public class Invoker {
    public void executeCommand(Command command) {
        if (command != null) {
            command.execute();
        } else {
            System.out.println("Команда не визначена.");
        }
    }
}
