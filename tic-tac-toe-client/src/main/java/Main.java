import jssc.SerialPortException;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try (Game game = new Game(args[0])) {
            game.start();
        } catch (SerialPortException e) {
            System.out.println(e.getExceptionType());
        } catch (XMLStreamException | IOException e) {
            e.printStackTrace();
        }
    }

}
