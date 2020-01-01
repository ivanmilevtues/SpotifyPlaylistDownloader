import React, { Component } from 'react'
import Playlist from '../interfaces/Playlist.interface'
import SpotifyContext from '../SpotifyContext'


export class PlaylistComponent extends Component<Playlist> {

    static contextType = SpotifyContext;

    constructor(props: Playlist) {
        super(props);
    }

    render() {
        const {id, name, numberOfSongs, showSongs} = this.props;
        return (
            <tr onClick={(event) => showSongs(id)}>
                <td className="playlist-name">{name}
                <span className="playlist-number-of-songs badge badge-secondary">{numberOfSongs}</span>
                </td>
            </tr>
        )
    }
}

export default PlaylistComponent
