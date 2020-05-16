import React from 'react';

export const SpotifyContext = React.createContext({
    accessToken: "",
    clientId: ""
})

export const SpotifyProvider = SpotifyContext.Provider
export const SpotifyConsumer = SpotifyContext.Consumer
export default SpotifyContext
