package org.grammo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class HTMLDoc {

    private static void setMeta(Document doc,
                                String siteName,
                                String title,
                                String description,
                                String picture,
                                String twitter,
                                String URI) {
        doc.head().appendElement("meta").attr("name", "description").attr("content", description);
        doc.head().appendElement("meta").attr("property", "og:image").attr("content", picture);
        doc.head().appendElement("meta").attr("property", "og:title").attr("content", title);
        doc.head().appendElement("meta").attr("property", "og:description").attr("content", description);
        doc.head().appendElement("meta").attr("property", "og:type").attr("content", "simulation post");
        doc.head().appendElement("meta").attr("property", "og:site_name").attr("content", siteName);
        doc.head().appendElement("meta").attr("property", "og:url").attr("content", URI);
        if(!twitter.isEmpty()){
            doc.head().appendElement("meta").attr("name", "twitter:card").attr("content", "summary_large_image");
            doc.head().appendElement("meta").attr("name", "twitter:site").attr("content", twitter);
            doc.head().appendElement("meta").attr("name", "twitter:title").attr("content", title);
            doc.head().appendElement("meta").attr("name", "twitter:description").attr("content", description);
        }


    }

    private static void setScriptURI(Document doc, String urlBase) {
        Elements elements = doc.getElementsByTag("script");
        for (Element element : elements) {
            element.attr("src");
            element.attr("src", urlBase + "scripts/" + element.attr("src"));
        }
    }

    public static Document makeHtmlPost(Map<String, Object> preamble) {
        Document htmlFile = null;
        try {
            htmlFile = Jsoup.parse(new File("./template/html_base.html"), "ISO-8859-1");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // right

        // set document title
        assert htmlFile != null;
        htmlFile.title(preamble.get("title").toString());
        // set meta data
        setMeta(htmlFile,
                preamble.get("urlBase").toString(),
                preamble.get("title").toString(),
                preamble.get("description").toString(),
                preamble.get("contentImagePath").toString(),
                preamble.get("twitter").toString(),
                preamble.get("URI").toString());


        setScriptURI(htmlFile,
                preamble.get("urlBase").toString());
        // set scripts
        return htmlFile;
    }

}
