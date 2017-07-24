package me.ialistannen.selfbot.util.functions;

import java.util.function.Consumer;

/**
 * A {@link Consumer} that only throws {@link ErrorInUncheckedFunctionException}s, but allows the
 * lambda to throw what it wants.
 */
@FunctionalInterface
public interface UncheckedConsumer<T> extends Consumer<T> {

  /**
   * The abstract function in this consumer. Can throw whatever it wants.
   *
   * @param t The argument
   */
  void acceptArgument(T t) throws Exception;

  @Override
  default void accept(T t) {
    try {
      acceptArgument(t);
    } catch (Exception e) {
      throw new ErrorInUncheckedFunctionException(e);
    }
  }
}
