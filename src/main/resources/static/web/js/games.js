fetch("http://localhost:8080/api/games"

    ).then(function (response) {
    return response.json();

}).then((dataFromServer) => {
    console.log(dataFromServer);


})

function showListGames(){

document.getElementById('attendance-data').innerHTML +=
}