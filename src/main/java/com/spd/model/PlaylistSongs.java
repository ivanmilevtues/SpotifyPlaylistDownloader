package com.spd.model;

import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;

public class PlaylistSongs {
    private Paging<PlaylistTrack> playlistTrackPaging;

    public PlaylistSongs(Paging<PlaylistTrack> playlistTrackPaging) {
        this.playlistTrackPaging = playlistTrackPaging;
    }

    public Paging<PlaylistTrack> getPlaylistTrackPaging() {
        return playlistTrackPaging;
    }

    public List<Map<String, String>> toList() {
        List<PlaylistTrack> playlistTracks = Arrays.asList(playlistTrackPaging.getItems());

        return playlistTracks.stream()
                .map(this::trackToMap)
                .collect(Collectors.toList());
    }

    public List<String> getSongNames() {
        List<PlaylistTrack> playlistTracks = Arrays.asList(playlistTrackPaging.getItems());

        return playlistTracks.stream()
                .map(s -> s.getTrack().getName())
                .collect(Collectors.toList());
    }

    private Map<String, String> trackToMap(PlaylistTrack playlistTrack) {
        Map<String, String> song = new HashMap<>();

        List<ArtistSimplified> artists = Arrays.asList(playlistTrack.getTrack().getArtists());
        List<String> artistNames = artists.stream().map(a -> a.getName()).collect(Collectors.toList());

        song.put("name", playlistTrack.getTrack().getName());
        song.put("artist", artistNames.toString());
        return song;
    }

}
