import React, { Component } from 'react'
import Song from '../interfaces/Song.interface'
import { timingSafeEqual } from 'crypto';

export class SongComponent extends Component<Song> {
    render() {
        const { name, authors, index } = this.props;
        
        return (
            <tr>
                <th scope="row">{index}</th>
                <td>{name}</td>
                <td>{authors.join(", ")}</td>
            </tr>
        )
    }
}

export default SongComponent
