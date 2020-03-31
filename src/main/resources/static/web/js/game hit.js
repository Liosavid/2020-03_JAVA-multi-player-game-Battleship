
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

printSalvoes(gameView);

printOpponentSalvoes(gameView);
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
                    $("#table_ship ." + location).css("background-color", "green");
                    $("#table_ship ." + location).html(ship.type);

                })
            })
 }
function printSalvoes (data) {
        console.log("HOLA GONZALO");
        console.log(parameterFromUrl);
         let salvo = data.salvoes[0];
         if(salvo[0].gamePlayerId != parameterFromUrl){
            salvo = data.salvoes[1];
         }

            $.each(salvo, function (index, salvo) {

                $.each(salvo.locations, function (index, location) {
                    $("#table_salvo ." + location).css("background-color", "yellow");
                    $("#table_salvo ." + location).html('Turn ' + salvo.turn);

                })
            })
 }

function printOpponentSalvoes (data) {
        console.log("HOLA lio");
        console.log(data.salvoes[0]);
          console.log("HOLA liooooo");
        console.log(parameterFromUrl);
         let salvo = data.salvoes[0];
         if(salvo[0].gamePlayerId == parameterFromUrl){
            salvo = data.salvoes[1];
         }

            $.each(salvo, function (index, salvo) {

                $.each(salvo.locations, function (index, locationSalvo) {
         //       if(salvo[0].location == salvo[1].location){
         console.log(locationSalvo);

          let ship = data.ships;

                     $.each(ship, function (index, ship) {
                         $.each(ship.locations, function (index, location){
         if( location == locationSalvo){


                    $("#table_ship ." + locationSalvo).css({"background-color": "red", "font-size": "200%"});
                    $("#table_ship ." + locationSalvo).html('HIT');
} else{
console.log("prout");
}


        })
        })
//}
                })

            })
 }
