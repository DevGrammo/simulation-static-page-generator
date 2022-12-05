package org.grammo;

import java.io.FileNotFoundException;
import java.util.Map;

public class Config {
    private final Map<String,Object> config;
    public Config(){
        try {
            config = YAMLConverter.readYAML(Disk.readFile("simulation_config.yaml"));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public String getURLBase(){
        return config.get("url-base").toString();
    }
    public String getContentDirectory(){
        return config.get("content-directory").toString();
    }

    public String getTwitter() {
        if(config.containsKey("twitter")){
            return  config.get("twitter").toString();
        }else{
            return null;
        }
    }
}
