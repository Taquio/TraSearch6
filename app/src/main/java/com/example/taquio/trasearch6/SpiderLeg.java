package com.example.taquio.trasearch6;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Taquio on 2/23/2018.
 */

public class SpiderLeg {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.1 (KHTML, like Gecko) Chrome/13.0.782.112 Safari/535.1";
//	private static final String USER_AGENT = "Googlebot/2.1 (+http://www.google.com/bot.html)";

    private HashMap<String, String> video = new HashMap<String, String>();
    private HashMap<Integer, CrawledData> video2 = new HashMap<Integer, CrawledData>();

    private List<String> links = new LinkedList<String>();
    private List<String> linksTitle = new LinkedList<String>();
    private List<String> nextLinks = new LinkedList<String>();
//	private Object[] pagination;

    private List<CrawledData> videoList = new LinkedList<CrawledData>();
    private Document htmlDocument;

    public boolean checkConnection(String url) {
        try {
//			Connection connection = Jsoup.connect(url).userAgent(USER_AGENT).timeout(10000);
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;

            if (!connection.response().contentType().contains("text/html")) {
                return false;
            }

            if (connection.response().statusCode() == 200) {
                return true;
            }

            return true;
        }
        catch(IOException ioe) {
            System.out.println(ioe);
            return false;
        }
    }

    public boolean crawl(String url) {
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if (this.checkConnection(url)) {
//                System.out.println("\n**Visiting** Received web page at " + url);

                Elements linksOnPage = htmlDocument.select("h3 > a[href]");
//                System.out.println("Found (" + linksOnPage.size() + ") links");

                int linkIndex = 0;
                for(Element link : linksOnPage) {
//					String tempTitle = link.attr("title");
//					System.out.println("The Title is" + tempTitle);
                    this.links.add(link.absUrl("href"));

                    CrawledData videoData = new CrawledData();
                    videoData.setTitle(link.attr("title"));
                    videoData.setUrl(link.absUrl("href"));

//					String tempLinks = link.absUrl("href");
//					System.out.println("Links " + tempLinks);
                    this.linksTitle.add(link.attr("title"));
                    this.videoList.add(videoData);
                    this.video.put(link.attr("title"), link.absUrl("href"));

                    this.video2.put(linkIndex, videoData);
                    linkIndex++;
                }// Crawler for the Links and Title then Saved to Video HashMap

//
//				for(Integer index: video2.keySet()){
//					Integer key = index;
//					CrawledData value = video2.get(key);
//					System.out.println("Key in video2: " + key + "\nValue in video2 Title: " + value.getTitle() + "\nValue in video2 Url: " + value.getUrl());
//				}
//
                Elements nextPageLink = htmlDocument.select("a[href].vve-check");
//                System.out.println("Found (" + nextPageLink.size() + ") next page links");
                for(Element nextLink: nextPageLink) {
                    this.nextLinks.add(nextLink.absUrl("href"));
                }// Crawler for the Next Page Links

//				int pageIndex = 0;
//				pagination = nextLinks.toArray();
//				do {
////					Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
////					Document htmlDocument = connection.get();
////					this.htmlDocument = htmlDocument;
////					System.out.println("Next Link: " + pagination[pageIndex]);
//					crawlPaganation(pagination[pageIndex].toString());
//					pageIndex++;
//				}while(pageIndex < pagination.length);
//				crawlPaganation(pagination[pageIndex].toString());
//
//				connection = Jsoup.connect(pagination[pageIndex].toString()).userAgent(USER_AGENT);
//				htmlDocument = connection.get();
//				this.htmlDocument = htmlDocument;

//				System.out.println("Video.isEmpty?" + this.video.isEmpty() + " Size: " + this.video.size());
//				for(String name: video.keySet()) {
//					String key = name.toString();
//					String value = video.get(name).toString();
//					System.out.println("Key: " + key + " Link: " + value);
//				}
                return true;
            }
            else {
//                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
        }
        catch(IOException ioe) {
//            System.out.println("Error in out HTTP request " + ioe);
            return false;
        }
    }

    public boolean crawlPaganation(String url){
        try {
            Connection connection = Jsoup.connect(url).userAgent(USER_AGENT);
            Document htmlDocument = connection.get();
            this.htmlDocument = htmlDocument;
            if (this.checkConnection(url)) {
//                System.out.println("\n**Visiting** Received web page at " + url);

                Elements linksOnPage = htmlDocument.select("h3 > a[href]");
//                System.out.println("Found (" + linksOnPage.size() + ") links");

                int linkIndex = 0;
                for(Element link : linksOnPage) {
//					String tempTitle = link.attr("title");
//					System.out.println("The Title is" + tempTitle);
                    this.links.add(link.absUrl("href"));

                    CrawledData videoData = new CrawledData();
                    videoData.setTitle(link.attr("title"));
                    videoData.setUrl(link.absUrl("href"));

//					String tempLinks = link.absUrl("href");
//					System.out.println("Links " + tempLinks);
                    this.linksTitle.add(link.attr("title"));
                    this.videoList.add(videoData);
                    this.video.put(link.attr("title"), link.absUrl("href"));

                    this.video2.put(linkIndex, videoData);
                    linkIndex++;
                }// Crawler for the Links and Title then Saved to Video HashMap

                return true;
            }
            else {
//                System.out.println("**Failure** Retrieved something other than HTML");
                return false;
            }
        }
        catch(IOException ioe) {
//            System.out.println("Error in out HTTP request " + ioe);
            return false;
        }
    }


    public boolean searchForWord(String searchWord){
        if (this.htmlDocument == null) {
//            System.out.println("Error! Call crawl() before performing analysis on the document");
            return false;
        }

//        System.out.println("Searching for the word " + searchWord + "...");
        String bodyText = this.htmlDocument.body().text();
        return bodyText.toLowerCase().contains(searchWord.toLowerCase());
    }


    public List<String> getLinks(){
        return this.links;
    }

    public List<String> getLinksTitle(){
        return this.linksTitle;
    }

    public List<String> getnextLinks(){
        return this.nextLinks;
    }

    public HashMap<String, String> getVideos(){
        return this.video;
    }

    public HashMap<Integer, CrawledData> getVideoData() {
        return this.video2;
    }
}
