package me.ialistannen.selfbot.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;
import org.yaml.snakeyaml.introspector.PropertyUtils;
import org.yaml.snakeyaml.representer.Representer;

/**
 * A simple YAML configuration.
 */
public class SimpleYamlConfig {

  private static final Logger LOGGER = Logger.getLogger("SimpleYamlConfig");
  private static final Yaml yaml = createYaml();

  private ConfigurationGroup rootGroup;

  /**
   * Creates an empty configuration.
   */
  public SimpleYamlConfig() {
    this(new ConfigurationGroup());
  }

  /**
   * Creates a configuration with the given root.
   *
   * @param rootGroup The root group.
   */
  private SimpleYamlConfig(ConfigurationGroup rootGroup) {
    this.rootGroup = rootGroup;
  }

  /**
   * Sets the values of this config to the default, if they are not yet set.
   *
   * @param defaults The default values
   */
  public void applyDefaults(SimpleYamlConfig defaults) {
    Map<String, ConfigurationSection> defaultValues = defaults.getRootGroup().getValues();

    for (Entry<String, ConfigurationSection> entry : defaultValues.entrySet()) {
      // only set raw values, as we might overwrite the hierarchies otherwise
      if (!entry.getValue().isValue()) {
        continue;
      }
      if (!contains(entry.getKey())) {
        set(entry.getKey(), entry.getValue());
      }
    }
  }

  /**
   * @return The root {@link ConfigurationGroup}, used to store all other.
   */
  public ConfigurationGroup getRootGroup() {
    return rootGroup;
  }

  /**
   * Returns a {@link ConfigurationValue} from the config.
   *
   * @param path The path to the value
   * @return The {@link ConfigurationValue}, or null if it does not exist or is not a value.
   */
  public ConfigurationValue get(String path) {
    ConfigurationSection section = rootGroup.get(path);

    if (section.isValue()) {
      return section.getAsValue();
    }

    return null;
  }

  /**
   * @param path The path to the object
   * @param value The value to set it to
   */
  public void set(String path, Object value) {
    rootGroup.set(path, value);
  }

  /**
   * @param path The path to the value
   * @return True if this config contains the value (it is not null)
   */
  public boolean contains(String path) {
    return rootGroup.get(path) != null;
  }

  /**
   * @param path The {@link Path} to the file
   * @throws ConfigurationIOException if an {@link IOException} occurs
   */
  public void save(Path path) {
    String yamlDump = yaml.dump(rootGroup);

    System.out.println(yamlDump);

    try {
      Files.write(
          path, Collections.singletonList(yamlDump), StandardCharsets.UTF_8,
          StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE
      );
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Error saving the config", e);

      throw new ConfigurationIOException("Error saving the configuration", e);
    }
  }

  @Override
  public String toString() {
    return "SimpleYamlConfig{"
        + "rootGroup=" + rootGroup
        + '}';
  }

  /**
   * Loads the config from the given path.
   *
   * @param path The {@link Path} to load from
   * @return The loaded config
   * @throws ConfigurationIOException if an {@link IOException} occurs
   */
  public static SimpleYamlConfig loadConfig(Path path) {
    try {
      return loadConfig(Files.newInputStream(path, StandardOpenOption.READ));
    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Error loading the config at '" + path.toAbsolutePath() + "'", e);

      throw new ConfigurationIOException("Error loading the configuration", e);
    }
  }


  /**
   * Loads the config from the given path.
   *
   * @param inputStream The {@link InputStream} to load from
   * @return The loaded config
   * @throws ConfigurationIOException if an {@link IOException} occurs
   */
  public static SimpleYamlConfig loadConfig(InputStream inputStream) {
    try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader)) {

      Object loadedObject = yaml.load(reader);

      if (!(loadedObject instanceof ConfigurationGroup)) {
        throw new IllegalArgumentException("Root is not of type 'ConfigurationGroup'!");
      }

      return new SimpleYamlConfig((ConfigurationGroup) loadedObject);

    } catch (IOException e) {
      LOGGER.log(Level.WARNING, "Error loading a config", e);

      throw new ConfigurationIOException("Error loading the configuration", e);
    }
  }

  private static Yaml createYaml() {
    PropertyUtils propertyUtils = new PropertyUtils();

    // I do not like JavaBeans for this, I would need to expose way too much
    propertyUtils.setAllowReadOnlyProperties(true);
    propertyUtils.setBeanAccess(BeanAccess.FIELD);

    Representer representer = new Representer();
    representer.setPropertyUtils(propertyUtils);

    return new Yaml(representer);
  }
}
