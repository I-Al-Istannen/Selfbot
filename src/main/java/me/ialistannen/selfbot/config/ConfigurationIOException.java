package me.ialistannen.selfbot.config;

/**
 * Indicates an {@link java.io.IOException} was thrown while doing a config operation.
 */
public class ConfigurationIOException extends RuntimeException {

  public ConfigurationIOException(String message, Throwable cause) {
    super(message, cause);
  }
}
