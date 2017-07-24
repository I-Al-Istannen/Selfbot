package me.ialistannen.selfbot.util;

import java.util.Map;
import net.sourceforge.argparse4j.inf.Namespace;

/**
 *
 */
public class NicerCmdArgumentNamespace extends Namespace {

  /**
   * Construct this object using given {@code attrs}.
   *
   * @param attrs The attributes
   */
  public NicerCmdArgumentNamespace(Map<String, Object> attrs) {
    super(attrs);
  }

  /**
   * @param key The key to check
   * @return True if this {@link Namespace} has a value for the given key (defaults count!)
   */
  public boolean hasValueFor(String key) {
    return getAttrs().get(key) != null;
  }
}
