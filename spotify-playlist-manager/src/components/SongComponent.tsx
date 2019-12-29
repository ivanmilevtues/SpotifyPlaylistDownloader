import React, { Component } from 'react'
import Song from '../interfaces/Song.interface'

export class SongComponent extends Component<Song> {
    render() {
        const { name, authors } = this.props;
        
        return (
            <div className="song">
                <span className="song-name">{name}</span>
                <span className="authors">{authors}</span>
            </div>
        )
    }
}

export default SongComponent
