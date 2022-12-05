package org.grammo;

import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class SiteBuilder {

    private final Config config;

    public SiteBuilder(Config config) {
        this.config = config;
    }

    private final Contents contents = new Contents();

    public void buildPage(String content) throws IOException {
        String contentPath = content + "post.md";
        String pagePath = content + "index.html";


        String URI = config.getURLBase() + content.replace( config.getContentDirectory() + "/", "");

        Map<String, Object> preamble = contents.contentPreamble(Disk.readContentPreamble(contentPath), contentPath, config.getURLBase());
        preamble.put("urlBase", config.getURLBase());
        preamble.put("contentImagePath", contents.contentImagePath(contentPath, config.getURLBase(), config.getContentDirectory()));
        preamble.put("twitter", config.getTwitter());
        preamble.put("URI", URI);
        // make html post and write the file to disk
        Document doc = HTMLDoc.makeHtmlPost(preamble);
        Disk.toDisk(pagePath, doc.toString());
        contents.updateIndex(preamble);
    }

    public void buildSite() throws IOException {
        String postDirectory = config.getContentDirectory() + "/posts/";
        File postDir = new File(postDirectory);
        String[] directories = postDir.list((current, name) -> new File(current, name).isDirectory());

        assert directories != null;

        Arrays.stream(directories)
                .parallel()
                .forEach(dir -> {
                    String contentPath = postDirectory + dir + "/";
                    try {
                        buildPage(contentPath);
                    } catch(Exception e) {
                        System.out.println("Unable to create page at: " + contentPath);
                    }
                });

        contents.exportIndex(config.getContentDirectory() + "/data/index.json");


    }
}
