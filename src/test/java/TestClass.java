import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.grammo.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class TestClass {

    private final Config config = new Config();

    @Test
    public void testReadPreamble() {
        Contents contents = new Contents();
        String path = "testDIR/example/post.md";
        Map<String, Object> preamble;
        try {
            preamble = contents.contentPreamble(Disk.readContentPreamble(path), path, config.getURLBase());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        assertFalse(preamble.isEmpty());
    }

    private boolean isDescription(Element e) {
        String description = "";
        if ("description".equals(e.attr("name"))) {
            description = e.attr("content");
        }
        return !description.isEmpty();
    }

    private boolean isOgDescription(Element e) {
        String description = "";
        if ("og:description".equals(e.attr("property"))) {
            description = e.attr("content");
        }
        return !description.isEmpty();
    }

    private boolean isOgTitle(Element e) {
        String title = "";
        if ("og:title".equals(e.attr("property"))) {
            title = e.attr("content");
        }
        return !title.isEmpty();
    }

    private boolean isOgImage(Element e) {
        String image = "";
        if ("og:image".equals(e.attr("property"))) {
            image = e.attr("content");
        }
        return !image.isEmpty();
    }

    @Test
    public void testMakePost() {
        Map<String, Object> preamble = new HashMap<>();
        preamble.put("description", "description");
        preamble.put("title", "title");
        preamble.put("contentImagePath", "imagePath");
        preamble.put("urlBase", "urlBase");
        preamble.put("twitter", "@luca");
        preamble.put("URI", "uri");

        Document doc = HTMLDoc.makeHtmlPost(preamble);
        assertFalse(doc.title().isEmpty());
        Elements elements = doc.getElementsByTag("meta");
        List<Element> testElements = elements
                .stream()
                .filter(e ->
                        isDescription(e) || isOgTitle(e) || isOgDescription(e) || isOgImage(e))
                .collect(Collectors.toList());
        assertEquals(4, (long) testElements.size());

    }

    @Test
    public void testMakePage() throws IOException {
        SiteBuilder siteBuilder = new SiteBuilder(config);

        String contentPath = "testDIR/example/";

        siteBuilder.buildPage(contentPath);
        File f = new File(contentPath + "index.html");
        assertTrue(f.exists());

    }

    @Test
    public void testIndex() {
        Index index = new Index();
        Contents contents = new Contents();
        String path = "contents/posts/post_A";

        Map<String, Object> preamble1 = contents.contentPreamble("date: '10/11/2022'", path, config.getURLBase());
        Map<String, Object> preamble2 = contents.contentPreamble("date: '05/11/2022'", path, config.getURLBase());
        Map<String, Object> preamble3 = contents.contentPreamble("date: '01/11/2022'", path, config.getURLBase());

        index.addPage(preamble2);
        index.addPage(preamble1);
        index.addPage(preamble3);

        assertTrue(3 <= index.getIndex().size());
        //test order

        Map<String, Object> firstPreamble = null;
        Optional<Map<String, Object>> firstElement = index
                .getIndex()
                .stream()
                .findFirst();
        if (firstElement.isPresent()) {
            firstPreamble = firstElement.get();
        }

        assert firstPreamble != null;
        assertEquals(
                preamble1.get("date").toString(),
                firstPreamble.get("date").toString());

    }

    @Test
    public void testBuildSite() throws IOException {
        SiteBuilder siteBuilder = new SiteBuilder(config);
        siteBuilder.buildSite();
        File f = new File(config.getContentDirectory() + "/data/index.json");
        assertTrue(f.exists());

        ObjectMapper mapper = new ObjectMapper();

        //JSON file to Java object
        List<Map<String, Object>> indexJSON = mapper.readValue(f, new TypeReference<List<Map<String, Object>>>() {
        });
        assertTrue(indexJSON.size() > 0);
    }


    @Test
    public void testConfiguration() {
        Config config = new Config();
        assertFalse(config.getContentDirectory().isEmpty());
        assertFalse(config.getURLBase().isEmpty());

    }

}




