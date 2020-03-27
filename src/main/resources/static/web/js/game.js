
let parameterFromUrl= getParameterByName("gp");
console.log(parameterFromUrl);

fetch("http://localhost:8080/api/game_view/"+parameterFromUrl

    ).then(function (response) {
    return response.json();

}).then((dataFromServer) => {
    console.log(dataFromServer);
gameView = dataFromServer;

gameInfo(gameView);

printShips(gameView);

})

function gameInfo (data){
let playerInfo1 = data.gamePlayers[0].player.user_name;
let playerInfo2 = data.gamePlayers[1].player.user_name;

if(data.gamePlayers[0].gamePlayer_id == getParameterByName('gp')){
 document.getElementById("game-information").innerHTML = 'Player ' + playerInfo1 + ' (you) vs ' + playerInfo2;
 } else {
 document.getElementById("game-information").innerHTML = 'Player ' + playerInfo2 + ' (you) vs ' + playerInfo1;
 }
 }

        function getParameterByName(name) {
            name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
            var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                results = regex.exec(location.search);
            return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
        }


         function printShips (data) {
       //  let locations = data.ships[0].locations;
         let ship = data.ships;

            $.each(ship, function (index, ship) {
                $.each(ship.locations, function (index, location) {
                    $("#" + location).css("background-color", "green");
                    $("#" + location).html(ship.type);

                })
            })






/*
    var parentDiv = [];
    $("tr > td").each((index, elem) => {
      parentDiv.push(elem.id);
    });

    console.log(parentDiv);


          console.log(locations);


*/



           }