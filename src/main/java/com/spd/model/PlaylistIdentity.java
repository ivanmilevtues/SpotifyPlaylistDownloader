package com.spd.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlaylistIdentity {
    private final String playlistId;
    private final String userId;
    private final String playlistName;

    public PlaylistIdentity(String playlistId, String userId, String playlistName) {
        this.playlistId = playlistId;
        this.userId = userId;
        this.playlistName = playlistName;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public String getUserId() {
        return userId;
    }

    public Map<String, String> toMap() {
        Map<String, String> result = new HashMap<>();
        result.put("userId", userId);
        result.put("playlistId", playlistId);
        result.put("playlistName", playlistName);

        return result;
    }
}
