package me.ialistannen.selfbot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import me.ialistannen.selfbot.util.NicerCmdArgumentNamespace;
import me.ialistannen.selfbot.util.functions.UncheckedConsumer;

/**
 * Starts the bot.
 */
class TokenFetcher {

  static final String TOKEN_ARGUMENT_KEY = "token";
  static final String TOKEN_FILE_ARGUMENT_KEY = "token-file";

  /**
   * Retrieves the token from the given arguments (if possible) and calls the consumer (if
   * successful).
   *
   * @param arguments The {@link NicerCmdArgumentNamespace} to fetch the arguments from
   * @param tokenConsumer The action to do with the token
   */
  static void fetchToken(NicerCmdArgumentNamespace arguments,
      UncheckedConsumer<String> tokenConsumer) {

    if (arguments.hasValueFor(TOKEN_ARGUMENT_KEY)) {
      tokenConsumer.accept(arguments.getString(TOKEN_ARGUMENT_KEY));
    } else if (arguments.hasValueFor(TOKEN_FILE_ARGUMENT_KEY)) {
      tokenConsumer.accept(fetchFromFile(arguments.getString(TOKEN_FILE_ARGUMENT_KEY)));
    } else {
      System.err.printf(
          "Sorry, but I could not find a token. You need to specify either '%s' or '%s'.\n",
          TOKEN_ARGUMENT_KEY, TOKEN_FILE_ARGUMENT_KEY
      );
    }
  }

  private static String fetchFromFile(String string) {
    Path path = Paths.get(string);

    if (Files.notExists(path)) {
      System.err.printf("Token file '%s' not found!\n", path.toAbsolutePath());
      return null;
    }

    try {
      List<String> contents = Files.readAllLines(path, StandardCharsets.UTF_8);

      if (contents.isEmpty()) {
        System.err.println("Sorry, but the file does not contain a token in the first line.");
        return null;
      }

      return contents.get(0);
    } catch (IOException e) {
      System.err.printf("Error reading the token file:\n");
      e.printStackTrace();
    }

    return null;
  }
}
