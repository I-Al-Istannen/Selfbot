package me.ialistannen.selfbot.command;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import me.ialistannen.selfbot.Selfbot;
import me.ialistannen.selfbot.command.commands.CommandPlantUml;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * The command executor.
 */
public class CommandHandler extends ListenerAdapter {

  private Set<Command> commands;
  private final String prefix;

  public CommandHandler(Selfbot selfbot) {
    commands = new HashSet<>();

    commands.add(new CommandPlantUml());
    prefix = selfbot.getConfig().get("settings.prefix").getAsString();
  }

  @Override
  public void onMessageReceived(MessageReceivedEvent event) {
    if (!event.getAuthor().equals(event.getJDA().getSelfUser())) {
      return;
    }

    String content = event.getMessage().getRawContent();

    if (!content.startsWith(prefix)) {
      return;
    }

    content = content.substring(prefix.length());
    String[] parts = content.split(" ");
    String keyword = parts[0];

    getCommand(keyword)
        .ifPresent(command -> {
              System.out.println("Executing command: " + command.getKeyword());
              String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);

              command.execute(event.getTextChannel(), event.getMessage(), arguments);
            }
        );
  }

  private Optional<Command> getCommand(String keyword) {
    return commands.stream()
        .filter(command -> command.getKeyword().equalsIgnoreCase(keyword))
        .findFirst();
  }
}
