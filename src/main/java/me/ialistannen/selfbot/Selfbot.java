package me.ialistannen.selfbot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.security.auth.login.LoginException;
import me.ialistannen.selfbot.command.CommandHandler;
import me.ialistannen.selfbot.config.SimpleYamlConfig;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * The main class for the selfbot.
 */
public final class Selfbot {

  private static Selfbot instance;

  private SimpleYamlConfig config;

  private Selfbot() throws LoginException, InterruptedException, RateLimitedException {
    instance = this;

    if (Files.exists(getConfigPath())) {
      config = SimpleYamlConfig.loadConfig(getConfigPath());
    } else {
      config = new SimpleYamlConfig();
      config.applyDefaults(
          SimpleYamlConfig.loadConfig(getClass().getResourceAsStream("/resources/config.yml"))
      );

      Path savePath = Paths.get("config.yml").toAbsolutePath();
      config.save(savePath);
      System.out.printf("Please populate your config now (%s)!\n", savePath);
      System.exit(0);
    }

    new JDABuilder(AccountType.CLIENT)
        .setToken(getConfig().get("settings.token").getAsString())
        .addEventListener(new CommandHandler())
        .buildBlocking();
  }

  public Path getConfigPath() {
    return Paths.get("config.yml").toAbsolutePath();
  }

  public SimpleYamlConfig getConfig() {
    return config;
  }

  public static Selfbot getInstance() {
    return instance;
  }

  public static void main(String[] args) throws Exception {
    new Selfbot();
  }
}
