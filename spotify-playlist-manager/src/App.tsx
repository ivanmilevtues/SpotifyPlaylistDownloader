import React, { Component } from 'react';
import './App.css';
import PlaylistList from './components/PlaylistList';
import { SpotifyProvider } from './SpotifyContext';
import 'bootstrap/dist/css/bootstrap.css';

export const authEndpoint = 'https://accounts.spotify.com/authorize';
const clientId = "c661971b488046f392141b97cbe9c07e";
const redirectUri = "http://localhost:3000";
const scopes = [
  "user-read-currently-playing",
  "user-read-playback-state",
  "playlist-read-collaborative",
  "playlist-read-private"
]

const accessToken = window.location.hash.substring(1).split("&")
  .filter(function(item: string) {
        let parts: Array<string> = item.split("=");
        return parts[0] === "access_token";
      })
  .map((item:string) => item.split("=")[1])[0];

window.location.hash = "";

class App extends Component {
  token: string;

  constructor(props: any) {
    super(props);
    this.token = accessToken
  }

  render() {
    let toBeRendered = <a className="btn btn--loginApp-link"
        href={`${authEndpoint}?client_id=${clientId}&redirect_uri=${redirectUri}&scope=${scopes.join("%20")}&response_type=token&show_dialog=true`}>
        Login to Spotify
      </a>
    if (this.token) {
      toBeRendered = <PlaylistList access_token={this.token} />
    }
    return (
      <SpotifyProvider value={{accessToken: this.token, clientId:clientId}}>
        {console.log("Token = " + this.token)}
        <div className="App">
          <img src="https://icon2.cleanpng.com/20180827/zii/kisspng-logo-product-design-brand-green-kenzie-amp-apos-s-corner-spotify-logo-from-html-c-5b846bbccb6741.0860103115354049888332.jpg" className="App-logo" alt="logo" />
          {toBeRendered}
        </div>
      </SpotifyProvider>
    );
  }
}

export default App;