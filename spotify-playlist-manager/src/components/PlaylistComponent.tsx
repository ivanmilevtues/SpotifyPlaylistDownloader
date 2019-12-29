import React, { Component } from 'react'
import Playlist from '../interfaces/Playlist.interface'
import Song from '../interfaces/Song.interface'
import * as $ from "jquery"
import SpotifyContext from '../SpotifyContext'
import SongComponent from './SongComponent'


export class PlaylistComponent extends Component<Playlist, {showSongs: boolean}> {

    static contextType = SpotifyContext;
    
    constructor(props: Playlist) {
        super(props);
        this.state = {
            showSongs: false
        }

        this.showSongs = this.showSongs.bind(this);
    }

    showSongs(): void {
        this.setState({
            showSongs: !this.state.showSongs
        })
    }
    
    getSongs(): Song[] {
        let songs: Song[] = []
        $.ajax({
            url: "https://api.spotify.com/v1/playlists/"+ this.props.id +"/tracks?fields=items(track(artists,name,href,album(name,href)))",
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
        const {name, numberOfSongs, songs} = this.props;
        return (
            <div className="playlist" onClick={this.showSongs}>
                <h2>
                    <span className="playlist-name">{name}</span>
                    <span className="playlist-number-of-songs">{numberOfSongs}</span>
                    { this.state.showSongs && this.getSongs().map(
                        (song, index) => <SongComponent key={index} name={song.name} authors={song.authors}/>) }
                </h2>
            </div>
        )
    }
}

export default PlaylistComponent
