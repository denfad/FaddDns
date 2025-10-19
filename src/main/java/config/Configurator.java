package config;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.FileInputStream;
import java.io.InputStream;


public class Configurator {

    public static AppConfig loadConfig(String path) {
        Yaml yaml = new Yaml(new Constructor(AppConfig.class, new LoaderOptions()));

        try (InputStream inputStream = new FileInputStream(path)) {
            return yaml.load(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }
}
