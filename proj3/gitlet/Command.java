package gitlet;

import java.io.*;

public class Command implements Serializable {
    public Command() {
        File command = new File(".gitlet/command");
        if (command.exists()) {
            Command obj;
            try {
                ObjectInputStream in =
                        new ObjectInputStream(new FileInputStream(command));
                obj = (Command) in.readObject();
                in.close();

            } catch (IOException | ClassNotFoundException exception) {
                System.out.println(exception);
            }
        }
    }
}
