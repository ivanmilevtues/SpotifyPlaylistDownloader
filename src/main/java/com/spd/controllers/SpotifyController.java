package com.spd.controllers;


import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;


// TODO: Finish Autorization and USER ID taking. For the listing of playlists and songs.
@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    private static final String clientId  = "13f8f386136c4b07a08d00545cad2632";
    private static final String clientSecret = "d2a87b56f28c40b98c05a2eb11d01733";

    private SpotifyApi spotifyApi;
    private ClientCredentialsRequest clientCredentialsRequest;
    private ClientCredentials clientCredentials;

    public SpotifyController() throws URISyntaxException{
    }


    @GetMapping("/auth")
    public void authenticate() throws URISyntaxException{
        try {
            spotifyApi = new SpotifyApi.Builder()
                    .setClientId(clientSecret)
                    .setClientSecret(clientId)
                    .build();

            clientCredentialsRequest = spotifyApi.clientCredentials().build();
            clientCredentials = clientCredentialsRequest.execute();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    @GetMapping("/playlist")
    public List<String> getPlaylist(@RequestParam(value = "userId") String userId,
                                    @RequestParam(value = "playlistId") String playlistId) {
        GetPlaylistsTracksRequest getPlaylistRequest = spotifyApi.getPlaylistsTracks(userId, playlistId)
                .fields("description")
                .offset(0)
                .build();
        try {
            Paging<PlaylistTrack> playlistTrackPaging = getPlaylistRequest.execute();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error " + e.getMessage());
        }

        throw new NotImplementedException();
    }

    @GetMapping("/playlists")
    public List<String> listPlaylistsForUser() {

        try {
            GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
                    .getListOfCurrentUsersPlaylists()
                    .offset(0).build();

            Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest
                    .execute();
        } catch (IOException | SpotifyWebApiException e) {
            System.out.println("Error " + e.getMessage());
        }
        throw new NotImplementedException();
    }
}
