package org.grammo;

import org.yaml.snakeyaml.Yaml;

import java.util.Map;

public class YAMLConverter {
    public static Map<String, Object> readYAML(String yamlString)  {
        Yaml yaml = new Yaml();
        return yaml.load(yamlString);
    }

}
