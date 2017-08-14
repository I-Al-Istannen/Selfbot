package me.ialistannen.selfbot.command.commands;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import me.ialistannen.selfbot.command.Command;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;
import net.sourceforge.plantuml.SourceStringReader;

/**
 * A command to render UML via PlantUml.
 */
public class CommandPlantUml extends Command {

  public CommandPlantUml() {
    super("plantuml");
  }

  @Override
  public void execute(TextChannel channel, Message message, String[] arguments) {
    message.delete().queue();

    if (arguments.length < 1) {
      channel.sendMessage(getUsagePrefix() + "<uml>").queue();
      return;
    }

    String uml = String.join(" ", arguments);
    SourceStringReader reader = new SourceStringReader(uml);

    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    try {
      String description = reader.generateImage(byteArrayOutputStream);
      description = "**Description:**\n" + description;

      Message imageMessage = new MessageBuilder().append(description).build();
      channel.sendFile(byteArrayOutputStream.toByteArray(), "uml.png", imageMessage)
          .queue();
    } catch (IOException e) {
      e.printStackTrace();
      channel.sendMessage(getErrorPrefix() + " Writing the image!").queue();
    }
  }
}
