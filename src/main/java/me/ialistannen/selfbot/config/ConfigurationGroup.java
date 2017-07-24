package me.ialistannen.selfbot.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * A {@link ConfigurationSection} that is a group (has sub keys).
 */
public class ConfigurationGroup extends AbstractConfigurationSection {

  private Map<String, AbstractConfigurationSection> children = new HashMap<>();

  @Override
  public boolean isValue() {
    return false;
  }

  /**
   * @return All child keys. Empty list if {@link #isValue()} is true.
   */
  public List<String> getKeys() {
    return children.values().stream()
        .map(AbstractConfigurationSection::getPath)
        .collect(Collectors.toList());
  }

  /**
   * @return All child values. Empty map if {@link #isValue()} is true.
   */
  public Map<String, ConfigurationSection> getValues() {
    Map<String, ConfigurationSection> values = new HashMap<>();

    for (AbstractConfigurationSection section : children.values()) {
      values.put(section.getPath(), section);

      if (section instanceof ConfigurationGroup) {
        values.putAll(section.getAsGroup().getValues());
      }
    }

    return values;
  }

  @Override
  public ConfigurationSection get(String key) {
    if (!key.contains(PATH_SEPARATOR)) {
      return children.get(key);
    }
    String[] parts = key.split(Pattern.quote(PATH_SEPARATOR));

    String childName = parts[0];

    ConfigurationSection value = children.get(childName);

    if (value != null) {
      value = value.get(key.substring(childName.length() + PATH_SEPARATOR.length()));
    }

    return value;
  }

  /**
   * Sets the value for a given key.
   *
   * @param key The key
   * @param value The new value
   */
  public void set(String key, Object value) {
    if (!key.contains(PATH_SEPARATOR)) {
      setOnThis(key, value);
      return;
    }
    String[] parts = key.split(Pattern.quote(PATH_SEPARATOR));

    String childName = parts[0];

    ConfigurationSection child = children.get(childName);

    if (child == null || !(child instanceof ConfigurationGroup)) {
      child = new ConfigurationGroup();
    }

    ConfigurationGroup childAsGroup = child.getAsGroup();
    childAsGroup.set(key.substring(childName.length() + PATH_SEPARATOR.length()), value);

    addChild(childName, childAsGroup);
  }

  private void setOnThis(String key, Object value) {
    if (value instanceof AbstractConfigurationSection) {
      addChild(key, (AbstractConfigurationSection) value);
    } else if (value instanceof ConfigurationSection) {
      throw new IllegalArgumentException(
          "You can not add your own subclasses of ConfigurationSection!"
      );
    } else {
      addChild(key, new ConfigurationValue(value));
    }
  }

  private void addChild(String name, AbstractConfigurationSection section) {
    section.setParent(this, name);
    children.put(name, section);
  }

  @Override
  public String toString() {
    return "Group{"
        + "'" + getPath() + "'"
        + ", "
        + children
        + "}";
  }
}
