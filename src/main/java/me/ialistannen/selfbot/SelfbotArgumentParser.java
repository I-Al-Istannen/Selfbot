package me.ialistannen.selfbot;

import java.util.Collections;
import me.ialistannen.selfbot.util.NicerCmdArgumentNamespace;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

/**
 * Parses command line arguments for this selfbot.
 */
class SelfbotArgumentParser {

  /**
   * Parses the arguments or calls {@link System#exit(int)} if an error occurs. Prints an error in
   * that case.
   *
   * @param args The arguments passed to the main method
   * @return The resulting {@link NicerCmdArgumentNamespace}
   */
  static NicerCmdArgumentNamespace parseArgumentsOrExit(String[] args) {
    ArgumentParser argumentParser = createArgumentParser();
    try {
      return new NicerCmdArgumentNamespace(argumentParser.parseArgs(args).getAttrs());
    } catch (ArgumentParserException e) {
      argumentParser.handleError(e);
    }
    System.exit(1);

    // dummy code to please IDEA: exit will NEVER return normally.
    return new NicerCmdArgumentNamespace(Collections.emptyMap());
  }


  private static ArgumentParser createArgumentParser() {
    ArgumentParser parser = ArgumentParsers.newArgumentParser("I Al Istannen's selfbot")
        .defaultHelp(true)
        .description("A small discord selfbot, written by and for I Al Istannen");

    parser.addArgument("-tf", "--" + TokenFetcher.TOKEN_FILE_ARGUMENT_KEY)
        .type(String.class)
        .required(false)
        .nargs(1);

    parser.addArgument("-t", "--" + TokenFetcher.TOKEN_ARGUMENT_KEY)
        .type(String.class)
        .required(false)
        .nargs(1);

    return parser;
  }
}
