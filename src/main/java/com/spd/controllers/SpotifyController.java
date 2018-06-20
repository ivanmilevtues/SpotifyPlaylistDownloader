package com.spd.controllers;


import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;
import org.apache.http.HttpConnection;
import org.glassfish.jersey.server.Uri;
import org.springframework.web.bind.annotation.*;
import org.springframework.ws.transport.http.HttpUrlConnection;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;


// TODO: Finish Autorization and USER ID taking. For the listing of playlists and songs.
@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    private static final String clientId  = "13f8f386136c4b07a08d00545cad2632";
    private static final String clientSecret = "d2a87b56f28c40b98c05a2eb11d01733";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8081/spotify/authorize");

    private SpotifyApi spotifyApi;


    @GetMapping("/auth")
    public String authenticate() throws IOException, SpotifyWebApiException {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();

        final String code = getAuthorizationURICode();
        return code;
//        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(code).build();
//        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
//
//        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
//        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
//
//
//        System.out.println("Expires in: " + authorizationCodeCredentials.getExpiresIn());
    }


    @GetMapping("/authorize")
    public void authorizeGet() {
        System.out.println("GET AUTHORIZE");
    }

    @PostMapping("/authorize")
    public void authorizePost() {
        System.out.println("POST AUTHORIZE");
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


    private String getAuthorizationURICode() throws IOException {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state("x4xkmn9pu3j6ukrs8n")
                .scope("user-read-birthdate,user-read-email")
                .show_dialog(true)
                .build();

        URI uri = authorizationCodeUriRequest.execute();

        HttpURLConnection con = (HttpURLConnection) uri.toURL().openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + uri.toString());
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println(response.toString());
        System.out.println(uri.toString());
        return response.toString();
    }
}
