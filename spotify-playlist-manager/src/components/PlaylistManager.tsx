import React, { Component } from 'react'
import PlaylistComponent from './PlaylistComponent'
import * as $ from "jquery"
import Playlist from '../interfaces/Playlist.interface'
import SpotifyContext from '../SpotifyContext'
import Song from '../interfaces/Song.interface'
import SongComponent from "./SongComponent"

export class PlaylistManager extends Component<{}, { showSongs: boolean, activePlaylistId: string}> {
    static contextType = SpotifyContext

    constructor(props: any) {
        super(props)
        this.state = {
            showSongs: false,
            activePlaylistId: ""
        }

        this.showSongs = this.showSongs.bind(this);
    }

    private showSongs(playlistId: string): void {
        this.setState({
            showSongs: !this.state.showSongs,
            activePlaylistId: playlistId
        })
    }

    private getPlaylists(): Playlist[] {
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

    private getSongs(): Song[] {
        let songs: Song[] = []
        $.ajax({
            url: "https://api.spotify.com/v1/playlists/" + this.state.activePlaylistId + "/tracks?fields=items(track(artists,name,href,album(name,href)))",
            type: "GET",
            beforeSend: (xhr) => {
                xhr.setRequestHeader("Authorization", "Bearer " + this.context.accessToken);
            },
            success: (data) => {
                data.items.forEach((song: any) => {
                    let track: any = song.track;
                    songs.push({
                        name: track.name,
                        authors: track.artists.map((artist: any) => artist.name)
                    })
                });
            },
            error: (data) => {
                console.error(data)
            },
            async: false
        })
        return songs
    }

    render() {
        let playlists = this.getPlaylists();
        return (
            <div className="container">
                <div className="row">
                    <div className="col-lg-3 col-md-3">
                        <table className="table table-hover table-dark">
                            <thead>
                                <tr>
                                    <th>Playlists</th>
                                </tr>
                            </thead>
                            <tbody>
                                {playlists.map((item) =>
                                    <PlaylistComponent key={item.name} name={item.name} numberOfSongs={item.numberOfSongs} id={item.id} showSongs={this.showSongs}/>)}
                            </tbody>
                        </table>
                    </div>
                    <div className="col-lg-9 col-md-3">
                        {this.state.showSongs &&
                            <table className="table table-striped table-hover">
                                <thead>
                                    <tr>
                                        <th scope="col">#</th>
                                        <th scope="col">Song name</th>
                                        <th scope="col">Authors</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {this.getSongs().map(
                                        (song, index) => <SongComponent key={index} index={index} name={song.name} authors={song.authors}/>)}
                                </tbody>
                            </table>
                        }
                    </div>
                </div>
            </div>
        )
    }
}

export default PlaylistManager
