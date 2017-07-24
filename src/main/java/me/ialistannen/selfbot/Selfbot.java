package me.ialistannen.selfbot;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.security.auth.login.LoginException;
import me.ialistannen.selfbot.config.SimpleYamlConfig;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

/**
 * The main class for the selfbot.
 */
public final class Selfbot {

  private static Selfbot instance;

  private SimpleYamlConfig config;

  private Selfbot(String token) throws LoginException, InterruptedException, RateLimitedException {
    instance = this;

    if (Files.exists(getConfigPath())) {
      config = SimpleYamlConfig.loadConfig(getConfigPath());
    } else {
      config = new SimpleYamlConfig();
      config.applyDefaults(
          SimpleYamlConfig.loadConfig(getClass().getResourceAsStream("/resources/config.yml"))
      );
    }

    System.out.println(getConfig());

//    new JDABuilder(AccountType.CLIENT)
//        .setToken(token)
//        .addEventListener(new CommandHandler())
//        .buildBlocking();
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
    TokenFetcher.fetchToken(SelfbotArgumentParser.parseArgumentsOrExit(args), Selfbot::new);
  }
}
