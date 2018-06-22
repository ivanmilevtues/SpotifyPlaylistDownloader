(function() {
    $.ajax({
        method: "GET",
        url: "/spotify/playlists",
        success: function (data) {
            console.log(data);
            populateTable(data)
        }
    });
})();

function populateTable(data) {
    for (var i = 0; i < data.length; i++) {
        $("#playlists-table tbody:last-child").append(generateRow(data[i]))
    }
}


function generateRow(playlistInfo) {
    var tblRowTemplate = "<tr><td>" + playlistInfo.playlistName + "</td>" +
        '<td data-playlist-id='+ playlistInfo.playlistId +' data-user-id=' + playlistInfo.userId + ' onclick="playlistDetails(this)">' +
        '<i class=\"fas fa-chevron-circle-down\"></i></td>' +
        '<td data-playlist-id='+ playlistInfo.playlistId +' data-user-id=' + playlistInfo.userId + ' onclick="downloadPlaylist(this)">' +
        '<i class=\"fas fa-download\"></i></td></tr>';
    return tblRowTemplate;
}


function playlistDetails(element) {
    console.log(element)
}

function downloadPlaylist(element) {
    console.log(element)
}