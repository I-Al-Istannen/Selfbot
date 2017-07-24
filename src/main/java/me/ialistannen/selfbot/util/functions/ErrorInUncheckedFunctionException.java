package me.ialistannen.selfbot.util.functions;

/**
 * A {@link RuntimeException} to indicate that some {@code Unchecked*} has thrown some exception.
 */
public class ErrorInUncheckedFunctionException extends RuntimeException {

  public ErrorInUncheckedFunctionException(Throwable cause) {
    super(cause);
  }
}
