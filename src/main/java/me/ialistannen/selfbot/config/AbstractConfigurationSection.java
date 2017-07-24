package me.ialistannen.selfbot.config;

/**
 * A small helper class for the {@link ConfigurationSection}
 */
abstract class AbstractConfigurationSection implements ConfigurationSection {

  private transient AbstractConfigurationSection parent;
  private transient String name;

  /**
   * @return The Path for this section
   */
  public String getPath() {
    if (parent == null) {
      return name == null ? "" : name;
    }
    if(parent.getPath().isEmpty()) {
      return name;
    }
    return parent.getPath() + PATH_SEPARATOR + name;
  }

  @Override
  public String getName() {
    return name;
  }

  void setParent(AbstractConfigurationSection parent, String name) {
    this.parent = parent;
    this.name = name;
  }
}
