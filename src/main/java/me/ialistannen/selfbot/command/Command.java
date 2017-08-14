package me.ialistannen.selfbot.command;

import java.util.Objects;
import me.ialistannen.selfbot.Selfbot;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * A command.
 */
public abstract class Command {

  private String keyword;

  protected Command(String keywordConfigKey) {
    this.keyword = Selfbot.getInstance()
        .getConfig()
        .get("settings.command.keywords." + keywordConfigKey)
        .getAsString();
  }


  public String getKeyword() {
    return keyword;
  }

  /**
   * @return The prefix for the usage message
   */
  protected String getUsagePrefix() {
    String prefix = Selfbot.getInstance().getConfig().get("settings.prefix").getAsString();

    return String.format("**Usage:** `%s%s` ", prefix, getKeyword());
  }

  /**
   * @return The prefix for the error message
   */
  protected String getErrorPrefix() {
    return "**Error:** ";
  }

  /**
   * @param channel The channel the message was sent in
   * @param message The message
   * @param arguments The Arguments for you
   */
  public abstract void execute(TextChannel channel, Message message, String[] arguments);

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Command command = (Command) o;
    return Objects.equals(getKeyword(), command.getKeyword());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getKeyword());
  }

  @Override
  public String toString() {
    return "Command{"
        + "keyword='" + keyword + '\''
        + '}';
  }
}
