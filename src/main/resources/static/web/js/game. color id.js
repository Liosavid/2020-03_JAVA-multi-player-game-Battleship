
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
         let locations = data.ships[0].locations;
     //   let children = document.getElementById('parent').children[1].children[2].cells;
        let children = document.getElementById('parent').children[1];
console.log(children);

for (let j=0; j < children.length; j++){
console.log(children.cells[j].id);

         for(i=0; i < locations.length; i++){
  //       console.log(locations[i]);

/*
  if (children[j].cells[j].id == locations[i]){
 console.log(locations[i]);
   }
         else {
         console.log("non");}

         */
         }
         }
         }