fetch("http://localhost:8080/api/games"

    ).then(function (response) {
    return response.json();

}).then((dataFromServer) => {
    console.log(dataFromServer);
games = dataFromServer;

   // console.log(games);



showListGames(games);


})

function showListGames(data){

let listGamePlayers = data.map(game => '<li>' + 'Game number ' + game.game_id + ' ' + new Date(game.game_creation_date).toLocaleString() + '</li>');
console.log(listGamePlayers);
listGamePlayers.map(gp => console.log(gp));

document.getElementById("game-list").innerHTML = listGamePlayers;


// let gameplayersGonzalo = data.map(game => game.gamePlayers.map(gp => gp))
// console.log("Hola");
// console.log(gameplayersGonzalo)
// gameplayersGonzalo.map(gp => console.log(gp))



}
   //  let htmlListGames = data.map(function (games) {

 //   for (let i = 0; i < games.length; i++) {
// ('<li>' + 'Game number ' + games[i].game_id + ' ' + new Date(games[i].game_creation_date).toLocaleString() + ' ' + games[i].gamePlayers.map(function(p) { return p.player.user_name}).join(',')  + '</li>');
//     }).join('');
    //  document.getElementById("game-list").innerHTML = htmlListGames;




