package org.grammo;

import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;

public class Contents {
   
    public final Index contentsIndex = new Index();

    public Map<String, Object> contentPreamble(String preambleString, String contentPath, String urlBase) {
        Yaml yaml = new Yaml();
        Map<String, Object> preamble = yaml.load(preambleString);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALY);
        LocalDate localeDate = LocalDate.parse(preamble.get("date").toString(), formatter);
        Timestamp timestamp = Timestamp.valueOf(localeDate.atStartOfDay());

        preamble.put("path", contentPath.replace("contents/", urlBase));
        preamble.put("timestamp", timestamp.getTime());

        return preamble;
    }

    public String contentImagePath(String contentPath, String urlBase, String contentsDirectory) {
        File f = new File(contentPath + "pic.png");
        if (f.exists() && !f.isDirectory()) {
            return urlBase + contentPath.replace(contentsDirectory, "") + "pic.png";
        }
        return urlBase + "images/default_meta.png";
    }

    public void updateIndex(Map<String,Object> preamble){
        contentsIndex.addPage(preamble);
    }
    public void exportIndex(String indexPath) {
        Disk.toDisk(indexPath, contentsIndex.toJSON());
    }


}
