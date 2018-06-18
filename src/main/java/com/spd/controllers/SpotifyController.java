package com.spd.controllers;


import com.wrapper.spotify.SpotifyApi;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;




// TODO: Look at https://github.com/thelinmichael/spotify-web-api-java/blob/master/examples/data/albums/GetAlbumExample.java
@RestController
@RequestMapping("/spotify")
public class SpotifyController {

    @GetMapping("/auth")
    public void authenticate() throws URISyntaxException{
        URI redirectUri = new URI("/home");
        SpotifyApi spotifyApi = new SpotifyApi.Builder()
                .setClientId("myId")
                .setClientSecret("someSecret")
                .setRedirectUri(redirectUri)
                .build();
    }
}
