import Song from './Song.interface'

export default interface Playlist {
    id: string,
    name: string,
    numberOfSongs: number,
    showSongs?: (id: string) => void,
}