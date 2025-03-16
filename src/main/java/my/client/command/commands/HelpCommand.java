package my.client.command.commands;

import my.client.command.Command;
import my.client.command.CommandRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

public class HelpCommand extends Command {

    public HelpCommand() {
        super("help", "Shows all available commands", ".help [command]", "commands", "?");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            // No args, list all commands
            sendMessage(TextFormatting.GRAY + "Available commands:");

            for (Command command : CommandRegistry.getCommands()) {
                sendMessage(TextFormatting.GREEN + "." + command.getName() + TextFormatting.GRAY + " - " +
                        TextFormatting.WHITE + command.getDescription());
            }

            sendMessage(TextFormatting.GRAY + "Use .help [command] for more information about a specific command.");
        } else {
            // Looking for specific command help
            String commandName = args[0].toLowerCase();

            for (Command command : CommandRegistry.getCommands()) {
                if (command.getName().equalsIgnoreCase(commandName) || hasAlias(command, commandName)) {
                    sendMessage(TextFormatting.GREEN + "Command: " + command.getName());
                    sendMessage(TextFormatting.GRAY + "Description: " + TextFormatting.WHITE + command.getDescription());
                    sendMessage(TextFormatting.GRAY + "Syntax: " + TextFormatting.WHITE + command.getSyntax());

                    if (command.getAliases().length > 0) {
                        sendMessage(TextFormatting.GRAY + "Aliases: " + TextFormatting.WHITE +
                                String.join(", ", command.getAliases()));
                    }

                    return;
                }
            }

            sendMessage(TextFormatting.RED + "Command not found: " + commandName);
        }
    }

    private boolean hasAlias(Command command, String alias) {
        for (String commandAlias : command.getAliases()) {
            if (commandAlias.equalsIgnoreCase(alias)) {
                return true;
            }
        }
        return false;
    }

    private void sendMessage(String message) {
        Minecraft.getInstance().player.sendMessage(new StringTextComponent(message), Minecraft.getInstance().player.getUniqueID());
    }
}