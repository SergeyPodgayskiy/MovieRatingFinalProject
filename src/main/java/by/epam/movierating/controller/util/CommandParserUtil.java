package by.epam.movierating.controller.util;

import by.epam.movierating.command.Command;
import org.apache.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * @author serge
 *         29.05.2017.
 */
public final class CommandParserUtil {
    private static final Logger logger = Logger.getLogger(CommandParserUtil.class);
    private static final CommandParserUtil INSTANCE = new CommandParserUtil();
    private static final String COMMANDS_XML_PATH = "D:\\Epam\\Final_Project_MovieRating\\src\\main\\resources\\commands.xml";
//    private final String COMMANDS_XML_PATH = "/commands.xml";//todo correct path
    private XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    private XMLStreamReader xmlStreamReader;
    private Map<String, Command> mapOfCommands;
    private String tagName;
    private String commandName;
    private Command commandInstance;

    private CommandParserUtil() {
    }

    public static CommandParserUtil getInstance() {
        return INSTANCE;
    }

    public Map<String, Command> parseXML() {
        try {
            InputStream inputStream = new FileInputStream(COMMANDS_XML_PATH);
            xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);
        } catch (FileNotFoundException | XMLStreamException e) {
            logger.error("Could not find file by path " + COMMANDS_XML_PATH, e);
        }
        mapOfCommands = getMapOfCommands();
        return mapOfCommands;
    }

    private Map<String, Command> getMapOfCommands() {
        try {
            while (xmlStreamReader.hasNext()) {
                int stepType = xmlStreamReader.next();
                switch (stepType) {
                    case XMLStreamConstants.START_ELEMENT:
                        startElement();
                        break;
                    case XMLStreamConstants.CHARACTERS:
                        characters();
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        endElement();
                        break;
                }
            }
        } catch (XMLStreamException e) {
            logger.error("XML Stream error", e);
        }
        return mapOfCommands;
    }

    private void startElement() {
        tagName = xmlStreamReader.getLocalName();
        if (isRootElementTag(tagName)) {
            mapOfCommands = new HashMap<>();
        }
    }

    private void characters() {
        final String commandNameTag = "command-name";
        final String commandClassTag = "command-class";
        String tagValue = xmlStreamReader.getText().trim();
        if (!tagValue.isEmpty()) {
            switch (tagName) {
                case commandNameTag:
                    commandName = tagValue;
                    break;
                case commandClassTag:
                    try {
                        commandInstance = (Command) Class.forName(tagValue).newInstance();
                    } catch (InstantiationException |
                            IllegalAccessException |
                            ClassNotFoundException e) {
                        logger.error(e);
                    }
                    break;
            }
        }
    }

    private void endElement() {
        tagName = xmlStreamReader.getLocalName();
        if (isChildElementTag(tagName)) {
            mapOfCommands.put(commandName, commandInstance);
        }
    }

    private boolean isChildElementTag(String tagName) {
        String childTag = "command";
        return tagName.equals(childTag);
    }

    private boolean isRootElementTag(String tagName) {
        String rootTag = "commands";
        return tagName.equals(rootTag);
    }
}
