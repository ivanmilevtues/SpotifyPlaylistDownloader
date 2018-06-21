package com.spd.controllers;


import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.PlaylistTrack;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeRequest;
import com.wrapper.spotify.requests.authorization.authorization_code.AuthorizationCodeUriRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.playlists.GetPlaylistsTracksRequest;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


// TODO: Finish Autorization and USER ID taking. For the listing of playlists and songs.
@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    private static final String clientId  = "13f8f386136c4b07a08d00545cad2632";
    private static final String clientSecret = "d2a87b56f28c40b98c05a2eb11d01733";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8081/spotify/authorize");

    private SpotifyApi spotifyApi;


    @GetMapping("/auth")
    public String authenticate() throws IOException {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();

        final String url = getAuthorizationURICode();
        return url;
    }


    @GetMapping("/authorize")
    public String authorizeGet(String code) {
        try {
            setAutorizationCodeTokens(code);
            return listPlaylistsForUser().toString();
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
        }
        return "";
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
    public List<String> listPlaylistsForUser() throws IOException, SpotifyWebApiException {

            GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
                    .getListOfCurrentUsersPlaylists()
                    .offset(0).build();

            Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest
                    .execute();

            List<String> playlistNames = Arrays.asList(playlistSimplifiedPaging.getItems())
                    .stream()
                    .map(x -> x.getName())
                    .collect(Collectors.toList());

            return playlistNames;
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
        return uri.toString();
    }

    private void setAutorizationCodeTokens(String authenticationCode) throws IOException, SpotifyWebApiException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(authenticationCode).build();
        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
    }
}
