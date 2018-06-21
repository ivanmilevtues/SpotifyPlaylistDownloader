package com.spd.controllers;


import com.spd.model.PlaylistIdentity;
import com.spd.model.PlaylistSongs;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


// TODO: Finish Autorization and USER ID taking. For the listing of playlists and songs.
@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    private static final String clientId  = "13f8f386136c4b07a08d00545cad2632";
    private static final String clientSecret = "d2a87b56f28c40b98c05a2eb11d01733";
    private static final URI redirectUri = SpotifyHttpManager.makeUri("http://localhost:8081/spotify/authorize");

    private static SpotifyApi spotifyApi;


    @GetMapping("/auth")
    public String authenticate() throws IOException {
        spotifyApi = new SpotifyApi.Builder()
                .setClientId(clientId)
                .setClientSecret(clientSecret)
                .setRedirectUri(redirectUri)
                .build();

        return getAuthorizationURI();
    }


    @GetMapping("/authorize")
    public void authorizeResponse(HttpServletResponse response, String code) throws IOException {
        try {
            setAutorizationCodeTokens(code);
        } catch (SpotifyWebApiException e) {
            e.printStackTrace();
        }
        response.sendRedirect("/authorized");
    }

    @GetMapping("/playlist")
    public List<Map<String, String>> getPlaylist(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "playlistId") String playlistId) throws IOException, SpotifyWebApiException {
        GetPlaylistsTracksRequest getPlaylistsTracksRequest = spotifyApi
                .getPlaylistsTracks(userId, playlistId)
                .offset(0)
                .build();

        Paging<PlaylistTrack> playlistTrackPaging = getPlaylistsTracksRequest.execute();
        PlaylistSongs playlistSongs = new PlaylistSongs(playlistTrackPaging);
        return playlistSongs.toList();

    }

    @GetMapping("/playlists")
    public List<Map<String, String>> listPlaylistsForUser() throws IOException, SpotifyWebApiException {

            GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest = spotifyApi
                    .getListOfCurrentUsersPlaylists()
                    .offset(0).build();

            Paging<PlaylistSimplified> playlistSimplifiedPaging = getListOfCurrentUsersPlaylistsRequest
                    .execute();

            List<Map<String, String>> playlistDetails = Arrays.asList(playlistSimplifiedPaging.getItems())
                    .stream()
                    .map(x -> new PlaylistIdentity(x.getId(), x.getOwner().getId(), x.getName()).toMap())
                    .collect(Collectors.toList());

            return playlistDetails;
    }


    private String getAuthorizationURI() {
        AuthorizationCodeUriRequest authorizationCodeUriRequest = spotifyApi.authorizationCodeUri()
                .state("x4xkmn9pu3j6ukrs8n")
                .scope("user-read-birthdate,user-read-email")
                .show_dialog(true)
                .build();

        URI uri = authorizationCodeUriRequest.execute();
        return uri.toString();
    }

    private void setAutorizationCodeTokens(String authenticationCode) throws IOException, SpotifyWebApiException {
        AuthorizationCodeRequest authorizationCodeRequest = spotifyApi.authorizationCode(authenticationCode).build();
        AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRequest.execute();
        spotifyApi.setRefreshToken(authorizationCodeCredentials.getRefreshToken());
        spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
    }
}
