import javax.xml.stream.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class GameFileIO {

    public static String byteToStringPlayer(byte player) {
        return (player == Constants.EMPTY ? "_" : (player == Constants.PLAYER_X ? "X" : "O"));
    }

    public static byte stringPlayerToByte(String player) {
        return (player.equals("_") ? Constants.EMPTY : (player.equals("X") ? Constants.PLAYER_X : Constants.PLAYER_O));
    }

    public static void saveGame(GameSave save, String fileName) throws IOException, XMLStreamException {
        Files.deleteIfExists(Path.of(fileName));
        OutputStream outputStream = new FileOutputStream(fileName);

        XMLStreamWriter out = XMLOutputFactory.newInstance().createXMLStreamWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        out.writeStartDocument();
        out.writeStartElement("game");
        out.writeStartElement("currentPlayer");
        out.writeCharacters(byteToStringPlayer(save.getCurrentPlayer()));
        out.writeEndElement();
        out.writeStartElement("gameMode");
        out.writeCharacters(String.valueOf(save.getGameMode()));
        out.writeEndElement();
        if(save.getGameMode() == Constants.MAN_VS_AI_MODE) {
            out.writeStartElement("aiMode");
            out.writeCharacters(String.valueOf(save.getAiMode()));
            out.writeEndElement();
            out.writeStartElement("playerSymbol");
            out.writeCharacters(String.valueOf(save.getPlayerSymbol()));
            out.writeEndElement();
        }

        out.writeStartElement("field");
        for (int i = 0; i < Constants.FIELD_SIZE; i++) {
            out.writeStartElement("row");
            out.writeAttribute("n", String.valueOf(i));
            for (int j = 0; j < Constants.FIELD_SIZE; j++) {
                out.writeStartElement("column");
                out.writeAttribute("n", String.valueOf(j));
                out.writeCharacters(byteToStringPlayer(save.getField()[i][j]));
                out.writeEndElement();
            }
            out.writeEndElement();
        }
        out.writeEndElement();
        out.writeEndElement();
        out.writeEndDocument();
        out.close();
        outputStream.close();
    }

    public static GameSave loadGame(String fileName) throws IOException, XMLStreamException {
        GameSave save = new GameSave();
        XMLInputFactory inputFactory = XMLInputFactory.newInstance();
        FileInputStream ins = new FileInputStream(fileName);
        XMLStreamReader reader = inputFactory.createXMLStreamReader(ins);
        int event = reader.getEventType();
        boolean currentPlayerTag = false;
        boolean columnTag = false;
        boolean gameModeTag = false;
        boolean aiModeTag = false;
        boolean playerSymbolTag = false;
        int row = 0, column = 0;
        while (true) {
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    if(reader.getLocalName().equals("currentPlayer")) {
                        currentPlayerTag = true;
                    } else if(reader.getLocalName().equals("row")) {
                        row = Integer.parseInt(reader.getAttributeValue(0));
                    } else if(reader.getLocalName().equals("column")) {
                        column = Integer.parseInt(reader.getAttributeValue(0));
                        columnTag = true;
                    } else if(reader.getLocalName().equals("column")) {
                        gameModeTag = true;
                    } else if(reader.getLocalName().equals("aiMode")) {
                        aiModeTag = true;
                    } else if(reader.getLocalName().equals("playerSymbol")) {
                        playerSymbolTag = true;
                    }
                    break;
                case XMLStreamConstants.CHARACTERS:
                    if(currentPlayerTag) {
                        save.setCurrentPlayer(stringPlayerToByte(reader.getText()));
                        currentPlayerTag = false;
                    }
                    else if(columnTag) {
                        save.getField()[row][column] = stringPlayerToByte(reader.getText());
                        columnTag = false;
                    }
                    else if(gameModeTag) {
                        save.setGameMode(Byte.parseByte(reader.getText()));
                        gameModeTag = false;
                    }
                    else if(aiModeTag) {
                        save.setGameMode(Byte.parseByte(reader.getText()));
                        aiModeTag = false;
                    }
                    else if(playerSymbolTag) {
                        save.setGameMode(Byte.parseByte(reader.getText()));
                        playerSymbolTag = false;
                    }
                    break;
            }
            if(!reader.hasNext()) {
                break;
            }
            event = reader.next();
        }
        reader.close();
        ins.close();
        return save;
    }

}
