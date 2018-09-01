package com.spd.BLL;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;

import java.io.IOException;
import java.util.*;

public class YouTubeBL {

    private static YouTube youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {
        @Override
        public void initialize(HttpRequest request) {
        }
    }).setApplicationName("spd").build();

    private String getSongId(Map<String, String> trackNameAuthor) throws IOException {
        YouTube.Search.List search = youtube.search().list("id,snippet");

        String artists = trackNameAuthor.get("artist");
        List<String> artistNames = Arrays.asList(artists.substring(1, artists.length() - 1).split(", "));

        String searchQuery = trackNameAuthor.get("name");

        for(String artistName: artistNames) {
            searchQuery.replace(artistName, "");
        }

        for(String artistName: artistNames) {
            searchQuery += " " + artistName;
        }

        String apiKey = "AIzaSyAUX3ih6yCAw5dQQ6XTWDXx6OIGKuivIYc";
        search.setKey(apiKey);
        search.setQ(searchQuery);

        search.setType("video");

        search.setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)");
        search.setMaxResults((long) 1);

        // Call the API and print results.
        SearchListResponse searchResponse = search.execute();
        List<SearchResult> searchResultList = searchResponse.getItems();
        return filterResult(searchResultList);
    }

    // TODO: add searching if it is remix and for the authors themselves
    private String filterResult(List<SearchResult> searchResultList) {
        return searchResultList.get(0).getId().getVideoId();
    }

    public List<String> getSongsIds(List<Map<String, String>> tracks) {
        List<String> songIds = new ArrayList<>();
        for (Map<String, String> track: tracks) {
            try {
                String songId = getSongId(track);
                songIds.add(songId);
            } catch (IOException e) {
                System.out.println("IOEXCEPTION ON YOUTUBE SIDE");
            }
        }

        return songIds;
    }
}
