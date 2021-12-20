import jssc.SerialPortException;

public class Main {

    public static void main(String[] args) {
        try (Game game = new Game(args[0])) {
            game.startGame();
        } catch (SerialPortException e) {
            System.out.println(e.getExceptionType());
        }
    }

}
