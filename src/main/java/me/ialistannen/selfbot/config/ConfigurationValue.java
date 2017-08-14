package me.ialistannen.selfbot.config;

import java.util.List;

/**
 * A {@link ConfigurationSection} that is a single value.
 */
public class ConfigurationValue extends AbstractConfigurationSection {

  private Object data;

  /**
   * Empty constructor for serialization.
   *
   * <p><strong><em>Do <u>NOT</u> use this constructor.</em></strong>
   */
  @SuppressWarnings("unused")
  public ConfigurationValue() {
    this(null);
  }

  /**
   * @param value The value for this section
   */
  ConfigurationValue(Object value) {
    this.data = value;
  }

  @Override
  public boolean isValue() {
    return true;
  }

  @Override
  public ConfigurationSection get(String path) {
    return null;
  }

  /**
   * @return The raw value
   */
  public Object getRaw() {
    return data;
  }

  /**
   * @return The value casted to a {@link String}
   * @throws ClassCastException if it was none
   */
  public String getAsString() {
    return (String) getRaw();
  }

  /**
   * @return The value casted to a double
   * @throws ClassCastException if it was none
   */
  public double getAsDouble() {
    return (double) getRaw();
  }

  /**
   * @return The value casted to an int
   * @throws ClassCastException if it was none
   */
  public int getAsInt() {
    return (int) getRaw();
  }

  /**
   * @return The value casted to a long
   * @throws ClassCastException if it was none
   */
  public long getAsLong() {
    return (long) getRaw();
  }

  /**
   * @return The value casted to a list
   * @throws ClassCastException if it was none
   */
  public <T> List<T> getAsList() {
    @SuppressWarnings("unchecked")
    List<T> t = (List<T>) getRaw();
    return t;
  }

  @Override
  public String toString() {
    return "ConfigurationValue["
        + "path='" + getPath() + "'"
        + ", "
        + getRaw()
        + "] ";
  }
}
