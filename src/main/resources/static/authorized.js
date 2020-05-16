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
        '<td data-playlist-id='+ playlistInfo.playlistId +' data-user-id=' + playlistInfo.userId +
        ' onclick="playlistDetails(this)">' +
        '<i class=\"fas fa-chevron-circle-down\"></i></td>' +
        '<td data-playlist-id='+ playlistInfo.playlistId +' data-user-id=' + playlistInfo.userId +
        ' onclick="downloadPlaylist(this)">' +
        '<i class=\"fas fa-download\"></i></td></tr>';
    return tblRowTemplate;
}


function playlistDetails(element) {
    console.log(element)
}

function downloadPlaylist(element) {
    var userId = $(element).attr("data-user-id");
    var playlistId = $(element).attr("data-playlist-id");
    console.log(playlistId, userId);

    $.ajax({
        method: "POST",
        data: {userId: userId, playlistId: playlistId},
        url: "/spotify/download",
        success: function (data) {
            for(var i = 0; i < data.length; i++) {
                downloadSong(data[i])
            }
        }
    });
}

function downloadSong(songId) {
    var url = "https://baixaryoutube.net/@api/json/mp3/"+ songId

    $.get(url, function (data) {
        console.log(data)
        var downloadUrl = data['vidInfo'][2]['dloadUrl'];
        console.log(downloadUrl);
        startDownload(downloadUrl);
    });
}

function startDownload(url) {
    var win = window.open(url, '_blank');
    win.focus();
}

