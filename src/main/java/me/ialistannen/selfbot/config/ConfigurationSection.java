package me.ialistannen.selfbot.config;

/**
 * A simple section in a configuration.
 */
public interface ConfigurationSection {

  String PATH_SEPARATOR = ".";

  /**
   * @return True if this is just a value, not a group.
   */
  boolean isValue();

  /**
   * @param path The path, maybe containing the path separator {@value PATH_SEPARATOR}.
   * @return The configuration with the path or null if not found
   */
  ConfigurationSection get(String path);

  /**
   * @return The nane of this section (null if not added to a config)
   */
  String getName();

  /**
   * @return The Path to this {@link ConfigurationSection} (null if not added to a config)
   */
  String getPath();

  /**
   * @return This object as a {@link ConfigurationValue}.
   * @throws ClassCastException if {@link #isValue()} was false
   */
  default ConfigurationValue getAsValue() {
    return (ConfigurationValue) this;
  }

  /**
   * @return This object as a {@link ConfigurationGroup}.
   * @throws ClassCastException if {@link #isValue()} was true
   */
  default ConfigurationGroup getAsGroup() {
    return (ConfigurationGroup) this;
  }
}
