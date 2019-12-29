import React, { Component } from 'react'
import PlaylistComponent from './PlaylistComponent'
import * as $ from "jquery"
import Playlist from '../interfaces/Playlist.interface'
import SpotifyContext from '../SpotifyContext'

export class PlaylistList extends Component<{
    access_token: string,
}> {
    static contextType = SpotifyContext
    
    takePlaylists(): Playlist[] {
        let playlistComponents: Playlist[] = []
        const accessToken = this.context.accessToken;
        $.ajax({
            url: "https://api.spotify.com/v1/me/playlists?limit=50&offset=0",
            type: "GET",
            beforeSend: (xhr) => {
                xhr.setRequestHeader("Authorization", "Bearer " + accessToken);
            },
            success: (data) => {
                data.items.forEach((playlist: any) => {
                    playlistComponents.push(
                        {
                            name: playlist.name,
                            numberOfSongs: playlist.tracks.total,
                            id: playlist.id
                        })
                    });
            },
            async: false
        })
        return playlistComponents;
    }

    render() {
        let playlists = this.takePlaylists();
        return (
            <div className="playlists-container">
                {playlists.map((item) =>  <PlaylistComponent key={item.name} name={item.name} numberOfSongs={item.numberOfSongs} id={item.id}/>)}
            </div>
        )
    }
}

export default PlaylistList
