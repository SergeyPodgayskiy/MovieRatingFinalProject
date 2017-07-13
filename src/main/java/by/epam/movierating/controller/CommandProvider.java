package by.epam.movierating.controller;

import by.epam.movierating.command.Command;
import by.epam.movierating.command.constant.ParameterName;
import by.epam.movierating.controller.util.CommandParserUtil;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * @author serge
 *         08.05.2017.
 */
public class CommandProvider {
    private static final Logger logger = Logger.getLogger(CommandProvider.class);
    private final static CommandProvider INSTANCE = new CommandProvider();
    private Map<String, Command> commandRepository;

    private CommandProvider() {
        commandRepository = CommandParserUtil.getInstance().parseXML();
    }

    public static CommandProvider getInstance() {
        return INSTANCE;
    }

    public Command getCommand(String commandName) {
        Command command = null;
        try {
            if (commandName != null) {
                command = commandRepository.get(commandName);
            } else {
                command = commandRepository.get(ParameterName.SHOW_WELCOME_PAGE);
            }
        } catch (IllegalArgumentException | NullPointerException e) {
            logger.error(e); //todo process NullPointer(Write something for client)
        }
        return command;
    }
}
