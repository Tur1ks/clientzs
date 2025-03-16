package my.client.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandRegistry {
    private static final List<Command> commands = new ArrayList<>();
    private static final String PREFIX = ".";

    public static void register(Command command) {
        commands.add(command);
    }

    public static void init() {
        // Register all commands here
        register(new my.client.command.commands.HelpCommand());
        // Add more commands as you create them
        // register(new ExampleCommand());
    }

    public static List<Command> getCommands() {
        return commands;
    }

    public static boolean executeCommand(String message) {
        if (!message.startsWith(PREFIX)) {
            return false;
        }

        String noPrefix = message.substring(PREFIX.length());
        String[] args = noPrefix.split(" ");
        String commandName = args[0].toLowerCase();
        String[] commandArgs = Arrays.copyOfRange(args, 1, args.length);

        for (Command command : commands) {
            if (command.getName().equalsIgnoreCase(commandName) || hasAlias(command, commandName)) {
                command.execute(commandArgs);
                return true;
            }
        }

        return false;
    }

    private static boolean hasAlias(Command command, String alias) {
        for (String commandAlias : command.getAliases()) {
            if (commandAlias.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }
}