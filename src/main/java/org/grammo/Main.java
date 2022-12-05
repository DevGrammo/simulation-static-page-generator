package org.grammo;

import java.io.IOException;

public class Main {
    public static void main(String[] a) {
        System.out.println("Welcome to grammo page builder! \nBuilding site ...");
        Config config = new Config();
        SiteBuilder siteBuilder = new SiteBuilder(config);
        try {
            siteBuilder.buildSite();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("... done.");
    }
}
