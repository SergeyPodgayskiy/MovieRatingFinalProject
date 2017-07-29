package by.epam.movierating.controller.util;

import by.epam.movierating.command.Command;
import org.apache.log4j.Logger;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
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
    private final String COMMANDS_XML_PATH = "/commands.xml";
    private XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    private XMLStreamReader xmlStreamReader;
    private Map<String, Command> commandMap;
    private String tagName;
    private String commandName;
    private Command commandInstance;

    private static final String CHILD_TAG = "command";
    private static final String ROOT_TAG = "commands";

    private CommandParserUtil() {
    }

    public static CommandParserUtil getInstance() {
        return INSTANCE;
    }

    public Map<String, Command> parseXML() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(COMMANDS_XML_PATH);
            xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);
        } catch (XMLStreamException e) {
            logger.error("Error during creating xml stream", e);
        }
        commandMap = getCommandMap();
        return commandMap;
    }

    private Map<String, Command> getCommandMap() {
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
            logger.error("xml stream error", e);
        }
        return commandMap;
    }

    private void startElement() {
        tagName = xmlStreamReader.getLocalName();
        if (isRootElementTag(tagName)) {
            commandMap = new HashMap<>();
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
            commandMap.put(commandName, commandInstance);
        }
    }

    private boolean isChildElementTag(String tagName) {
        return tagName.equals(CHILD_TAG);
    }

    private boolean isRootElementTag(String tagName) {
        return tagName.equals(ROOT_TAG);
    }
}
