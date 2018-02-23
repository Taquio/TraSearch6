package com.example.taquio.trasearch6;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * Created by Taquio on 2/23/2018.
 */

public class Spider {
    private Set<String> pagesVisited = new HashSet<String>(); // Links visited that contains the searchWord || source of the Links of video
    private List<String> pagesToVisit = new LinkedList<String>(); // Links found to be visited || Links of video
    private List<String> searchedWord = new LinkedList<String>();
    private Object[] paganation;

    private String nextUrl() {
        String nextUrl;
        do {
            nextUrl = this.pagesToVisit.remove(0);
        }while(this.pagesVisited.contains(nextUrl));
        this.pagesVisited.add(nextUrl);
        return nextUrl;
    }


    public HashMap<Integer, CrawledData> searchEngine(String searchWord) {
        SpiderLeg leg = new SpiderLeg();
        String urlSource = "https://www.youtube.com/results?search_query=";
//		String urlThisWeek = "https://www.youtube.com/results?sp=EgIIAw%253D%253D&search_query=";
//		String urlThisYear = "https://www.youtube.com/results?sp=EgIIBQ%253D%253D&search_query=";
        String url = urlSource + searchWord.replace(" ", "+");
//		String url = urlThisWeek + searchWord.replace(" ", "+");
        while (leg.checkConnection(url)){
            String currentUrl;

            if(this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesVisited.add(url);
            }
            else {
                currentUrl = this.nextUrl();
            }

            if (this.searchedWord.isEmpty()) {
                this.searchedWord.add(searchWord);
            }
            else {
                this.searchedWord.add(searchWord);
            }

            leg.crawl(currentUrl);

            paganation = leg.getnextLinks().toArray();
            int x = 0;
            do{
                System.out.println("Page " + (x+1) + ": " + paganation[x]);
                x++;
            }while(x < paganation.length);
//			leg.crawl(paganation[0].toString());
//			System.out.println("Paganation[0]: " + paganation[0]);

            boolean success = leg.searchForWord(searchWord);
            if(success) {
                System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
                break;
            }
            this.pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));
        return leg.getVideoData();
    }

    public HashMap<String, String> search(String url, String searchWord) {
        SpiderLeg leg = new SpiderLeg();
        while (leg.checkConnection(url)) {
            String currentUrl;

            if(this.pagesToVisit.isEmpty()) {
                currentUrl = url;
                this.pagesVisited.add(url);
            }
            else {
                currentUrl = this.nextUrl();
            }

            if (this.searchedWord.isEmpty()) {
                this.searchedWord.add(searchWord);
            }
            else if (!(this.searchedWord.contains(searchWord))) {
                this.searchedWord.add(searchWord);
            }

            System.out.println("pagesToVisit" + this.pagesToVisit);
            System.out.println("pagesVisited" + this.pagesVisited);
            System.out.println("searchedWord" + this.searchedWord);
            leg.crawl(currentUrl);


            boolean success = leg.searchForWord(searchWord);
            if(success) {
                System.out.println(String.format("**Success** Word %s found at %s", searchWord, currentUrl));
                break;
            }
            this.pagesToVisit.addAll(leg.getLinks());
        }
        System.out.println(String.format("**Done** Visited %s web page(s)", this.pagesVisited.size()));
        return leg.getVideos();
    }

    public Set<String> getPagesVisited() {
        return this.pagesVisited;
    }
    public List<String> getPagesToVisit() {
        return this.pagesToVisit;
    }

    public List<String> getSearchedWord() {
        return this.searchedWord;
    }

}
