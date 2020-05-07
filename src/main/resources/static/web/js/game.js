let tableShip_tds = document.querySelectorAll("#table_ship td");
addChangeOrientation();
clickOnSalvoCell();
let salvoArray = [];
opacityImgSalvo();
  var sunkNumHost = [];
    var sunkNumEnem = [];



document.getElementById('sendShips').addEventListener("click", function(){
addShipLocationsToBackEnd(parameterFromUrl);
})


document.getElementById('sendSalvoes').addEventListener("click", function(){
addSalvoLocationsToBackEnd(parameterFromUrl);
})





  let globalObject= [

{shipType: "CARRIER",
shipLocations: []
},

{shipType: "BATTLESHIP",
shipLocations: []
},

{shipType: "SUBMARINE",
shipLocations: []
},

{shipType: "DESTROYER",
shipLocations: []
},

{shipType: "PATROL_BOAT",
shipLocations: []
} ]

   function addShipLocationsToBackEnd(gamePlayerId){

    fetch("/api/games/players/" + gamePlayerId + "/ships"
,{
    method: "POST",
    headers:{
    'Content-Type': 'application/json'
    },
    body:JSON.stringify(globalObject)
    } ).then(res=> res.json()).then(data=> console.log(data))

 function getBody(json) {
   var body = [];
   for (var key in json) {
   var encKey = encodeURIComponent(key);
   var encVal = encodeURIComponent(json[key]);
   body.push(encKey + "=" + encVal);
 }
  return body.join("&");
}

}


function addSalvoLocationsToBackEnd(gamePlayerId){

    fetch("/api/games/players/" + gamePlayerId + "/salvoes"
,{  credentials: "include",
    method: "POST",
    headers:{
    'Content-Type': 'application/json'
    },
    body:JSON.stringify(salvoArray)
    } ).then(res=> res.json()).then(data=> console.log(data))

 function getBody(json) {
   var body = [];
   for (var key in json) {
   var encKey = encodeURIComponent(key);
   var encVal = encodeURIComponent(json[key]);
   body.push(encKey + "=" + encVal);
 }
  return body.join("&");
}

}

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
gameHistory(gameView);
ShipHasSunk_HideImage(gameView);


if (gameView.salvoes[0].length != 0){

printSalvoes(gameView);

printOpponentSalvoes(gameView);

}
})

//////////////////////////////////GAME HISTORY - HITS AND SUNK /////////////////////////////////////////////

function ShipHasSunk_HideImage(data){

let hitsOnOpponent;
let hitsOnCurrentPlayer;
let sunkShipOpponent;
let sunkShipCurrentPlayer;
let gamePlayerId;

for (let i= 0; i < data.gameHistory.length; i++){
console.log(data.gameHistory[i]);


for (let j= 0; j < data.gameHistory[i].shipStatus.length; j++){
gamePlayerId = data.gameHistory[i].gamePlayerId;
console.log(data.gameHistory[i].shipStatus[j]);

if(data.gameHistory[i].shipStatus[j].sunk == true){

let ShipSunkType_upperCase = data.gameHistory[i].shipStatus[j].type;
console.log(parameterFromUrl, data.gameHistory[i].shipStatus[j].type);
console.log(ShipSunkType_upperCase.toLowerCase());

if (gamePlayerId != parameterFromUrl){

sunkShipOpponent = '<td>' + ShipSunkType_upperCase + '</td>';
document.getElementById("tbody_sunk").innerHTML += '<tr>' +  sunkShipOpponent + '</tr>';


}

else{
document.getElementById(ShipSunkType_upperCase.toLowerCase()).style.visibility ="hidden";


}
}


}

}
}


function gameHistory(data){

let gameHistory = data.gameHistory;
let hitsOnOpponent = data.hits;

console.log(gameHistory);
console.log(hitsOnOpponent);

console.log(data);

for (let i= 0; i < data.hitsOnOpponent.length; i++){

let hitsOnOpponent;
let hitsOnCurrentPlayer;
let sunkShipOpponent;
let sunkShipCurrentPlayer;
let gamePlayerId;
let turnNumber;
//let rows;

turnNumber = '<td>' + (i+1) +'</td>';
console.log(turnNumber);

console.log(data.hitsOnOpponent[i]);
 hitsOnOpponent = '<td>' +  data.hitsOnOpponent[i] +'</td>';
console.log(data.hitsOnCurrentPlayer[i]);

hitsOnCurrentPlayer = '<td>' +  data.hitsOnCurrentPlayer[i] +'</td>';
console.log(data.gameHistory[i]);

/*
for (let j= 0; j < data.gameHistory[i].shipStatus.length; j++){
gamePlayerId = data.gameHistory[i].gamePlayerId;
console.log(data.gameHistory[i].gamePlayerId[j]);

if (data.gameHistory[i].shipStatus[j].sunk == true){

if( gamePlayerId == parameterFromUrl){

console.log(parameterFromUrl, data.gameHistory[i].shipStatus[j].type);
sunkShipCurrentPlayer = '<td>' +  data.gameHistory[i].shipStatus[j].type +'</td>';

} else {
console.log(data.gameHistory[i].shipStatus[j].type);
sunkShipOpponent = '<td>' +  data.gameHistory[i].shipStatus[j].type +'</td>';
}

} else {
console.log("empty cell");
sunkShipCurrentPlayer = '<td>' +'</td>';
sunkShipOpponent = '<td>' +'</td>';
}

}

*/
console.log(turnNumber, hitsOnCurrentPlayer,sunkShipCurrentPlayer, hitsOnOpponent, sunkShipOpponent);
document.getElementById("table_history").innerHTML += '<tr>' + turnNumber + hitsOnCurrentPlayer + hitsOnOpponent +'</tr>' ;

}

}

function turnNumber (data){
for (let i=0; i < data.salvoes[0].length; i++){
return i
}
}


////////////////////////////////////////////////////////////////////////////////////////////////////

function gameInfo (data){

let playerInfo1 = data.gamePlayers[0].player.user_name;

if (data.gamePlayers.length > 1){

let playerInfo2 = data.gamePlayers[1].player.user_name;

if(data.gamePlayers[0].gamePlayer_id == getParameterByName('gp')){
 document.getElementById("game-information").innerHTML = 'Player ' + playerInfo1 + ' (you) vs ' + playerInfo2;
 } else {
 document.getElementById("game-information").innerHTML = 'Player ' + playerInfo2 + ' (you) vs ' + playerInfo1;
 }
 }

 else {

  document.getElementById("game-information").innerHTML = 'Player ' + playerInfo1 + ' (you) vs ' + "Waiting for opponent";


 }
  }

function getParameterByName(name) {
  name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
  var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
  results = regex.exec(location.search);
  return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
 }

function printShips (data) {
  let ship = data.ships;
  $.each(ship, function (index, ship) {
  $.each(ship.locations, function (index, location) {
  $("#table_ship ." + location).css("background-color", '#00FF00');
  $("#table_ship ." + location).html(ship.type);

                })
            })
 }
function onDragOver(event) {
  event.preventDefault();
}


 function onDragStart(event) {
 console.log(event)
   event
     .dataTransfer
     .setData('text/plain', event.target.id);

     let shipPosition = globalObject[Number(event.target.dataset.position)].shipLocations;

       for (let i= 0; i< shipPosition.length; i++){

            let pos = shipPosition[i];
           // console.log(document.querySelector("#table_ship ." + pos));
     document.querySelector("#table_ship ." + pos).style.background="";
            }

  globalObject[Number(event.target.dataset.position)].shipLocations = [];
 }

function onDrop(event) {
  console.log(event);
  const id = event
     .dataTransfer
     .getData('text');

   const draggableElement = document.getElementById(id);
   const dropzone = event.target;
   console.log(dropzone);
   console.log(event.target.className);
   let placedBoat = event.target.className.split("");

       console.log(draggableElement.dataset.size);

// loop where you placed the ship to add more cells depending on the boat size


addImageAndColor (draggableElement, placedBoat, dropzone)

   event
     .dataTransfer
     .clearData();
 }

function cleanColor (arrayLocationsShipSelected){
        for (let j= 0; j < arrayLocationsShipSelected.length; j++){
          console.log(arrayLocationsShipSelected[j]);
           document.querySelector("#table_ship ." + arrayLocationsShipSelected[j]).style.background="";
}
}

function addChangeOrientation () {
 Array.from(document.querySelectorAll("img")).forEach(img => {
 img.addEventListener("click", function(event){

let ship = event.target;
console.log(ship.dataset.orientation);
ship.dataset.orientation == "H" ? ship.dataset.orientation = "V" : ship.dataset.orientation = "H";
console.log(ship.dataset.orientation);
             console.log("works", event.target);
             addImageAndColor(event.target, event.target.parentElement.className.split(""), event.target.parentElement);
         //    moveShipVertical (event.target)
             console.log(globalObject);
      });

 })
}


 function addImageAndColor (draggableElement, placedBoat, dropzone){
  let arrayLocationsShipSelected = globalObject[Number(draggableElement.dataset.position)].shipLocations;
  let allowDrawingShip = true;
  cleanColor (arrayLocationsShipSelected);

  if (draggableElement.dataset.orientation == "H"){
  //allowDrawingShip = true;
    for (let i= 0; i< draggableElement.dataset.size; i++){
             console.log((Number(placedBoat[1]) + i));
             console.log((Number(placedBoat[1]) + i) + Number(draggableElement.dataset.size));
             let pos = placedBoat[0] + (Number(placedBoat[1]) + i);
console.log(pos);
             console.log(((Number(placedBoat[1]) + Number(draggableElement.dataset.size) < 9)
                                   && document.querySelector("#table_ship ." + pos).style.background !="green"));

          if ((Number(placedBoat[1]) + Number(draggableElement.dataset.size) < 10)
          && document.querySelector("#table_ship ." + pos).style.background !="green"){
            console.log(document.querySelector("#table_ship ." + pos));
       //     document.querySelector("#table_ship ." + pos).style.background="green";
        //    arrayLocationsShipSelected.push(pos);
       //     dropzone.appendChild(draggableElement);
            console.log(arrayLocationsShipSelected);
       //     moveShipVertical (draggableElement);

          }else{
          allowDrawingShip = false;
            console.log(" HORIZONTAL Not enough space for your ship here!");
            break;
}
  console.log(globalObject);

}
} else {

for (let i= 0; i< draggableElement.dataset.size; i++){
             console.log((Number(placedBoat[1]) + i));
             console.log((Number(placedBoat[1]) + i) + Number(draggableElement.dataset.size));
let pos = (String.fromCharCode(placedBoat[0].charCodeAt(0) + i) + (placedBoat[1]) );

console.log(placedBoat[0].charCodeAt(0) + Number(draggableElement.dataset.size));

          if (((placedBoat[0].charCodeAt(0) + Number(draggableElement.dataset.size)) < 74)
          && document.querySelector("#table_ship ." + pos).style.background !="green"){
            console.log(document.querySelector("#table_ship ." + pos));
       //     document.querySelector("#table_ship ." + pos).style.background="green";
        //    arrayLocationsShipSelected.push(pos);
            console.log(arrayLocationsShipSelected);
       //     moveShipVertical (draggableElement);
          }else{
            allowDrawingShip = false;
            console.log(" VERTICAL Not enough space for your ship here!");
            break;
}
}
}

if (allowDrawingShip){
  dropzone.appendChild(draggableElement);
  moveShipVertical (draggableElement);
}
else{
draggableElement.dataset.orientation == "H" ? draggableElement.dataset.orientation = "V" : draggableElement.dataset.orientation = "H";
  moveShipVertical (draggableElement);

}

}

function moveShipVertical (draggableElement) {

console.log(draggableElement.parentElement.className);

let arrayLocationsShipSelected = globalObject[Number(draggableElement.dataset.position)].shipLocations;
  cleanColor(arrayLocationsShipSelected);
  let arrayLocationsShipSelectedSplit = draggableElement.parentElement.className.split("");

  arrayLocationsShipSelected = [];

  for (let j= 0; j < draggableElement.dataset.size; j++){

  if (draggableElement.dataset.orientation == "V") {
    let pos = (String.fromCharCode(arrayLocationsShipSelectedSplit[0].charCodeAt(0) + j) + (arrayLocationsShipSelectedSplit[1]) );
    document.querySelector("#table_ship ." + pos).style.background="green";
    arrayLocationsShipSelected.push(pos);

    } else{
     let pos = arrayLocationsShipSelectedSplit[0] + (Number(arrayLocationsShipSelectedSplit[1]) + j);
     document.querySelector("#table_ship ." + pos).style.background="green";
      arrayLocationsShipSelected.push(pos);
    }

}

globalObject[Number(draggableElement.dataset.position)].shipLocations = arrayLocationsShipSelected;
 console.log(globalObject);
}







function printSalvoes (data) {
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
  let ship = data.ships;
  console.log(data.salvoes[0]);
  console.log(parameterFromUrl);
  let salvo = data.salvoes[0];
  if(salvo[0].gamePlayerId == parameterFromUrl){
    salvo = data.salvoes[1];
}
  console.log(salvo);
  $.each(salvo, function (index, salvo) {
  $.each(salvo.locations, function (index, locationSalvo_Opponent) {
  var firstDiv = $("#table_ship ." + locationSalvo_Opponent);
  console.log(locationSalvo_Opponent);
  console.log(firstDiv.css('background-color'))

  if (firstDiv.css('background-color') ==='rgb(0, 255, 0)'){
    var element = document.getElementsByClassName(locationSalvo_Opponent);
      element[0].classList.add("backgroundRed"); //[0] for left board.
      element[0].textContent = "HIT";

}else{
$("#table_ship ." + locationSalvo_Opponent).css({"background-color": "orange"})
                    $("#table_ship ." + locationSalvo_Opponent).html('Turn ' + salvo.turn);
}
})
})
}



// LOGOUT FUNCTION

document.getElementById("logout-submit").addEventListener("click", function(e){
e.preventDefault();
logout();
})

 function logout() {

       fetch("/api/logout", {
         method: "POST"
       })
         .then(function(data) {
           console.log("Logout success: ", data);
               alert("You are logged out");
                location.href = "/web/games.html";


         })
         .catch(function(error) {
           console.log("Request failure: ", error);
         });
     }



function clickOnSalvoCell () {
  Array.from(document.querySelectorAll("#table_salvo td")).forEach(td => {
  td.addEventListener("click", function(event){
  let salvoCell = event.target.className;
  let imageSalvo = '<img src="./images/salvo.png"/>';

  if (!td.classList.contains("salvoAdded")){
    document.querySelector("#table_salvo ." + salvoCell).innerHTML += '<img src="./images/salvo.png"/>';
    document.querySelector("#table_salvo ." + salvoCell).classList.add("salvoAdded");
    opacityImgSalvo();
    console.log(salvoArray);

    if(salvoArray.length> 4){
      alert("You can only add 5 salvoes for each turn!");
      td.innerHTML = '';
      console.log(salvoArray);

    }else{
    salvoArray.push(salvoCell);
    console.log(salvoArray);
    }

  }else{
  td.innerHTML = '';
  td.classList.remove("salvoAdded");
  salvoArray.pop();
  console.log(salvoArray);
  opacityImgSalvo();
  }
  });
  })
}


function opacityImgSalvo(){
  if (salvoArray.length > 4){
    document.getElementById("salvoImg").classList.add("opacity");

  }else{
  document.getElementById("salvoImg").classList.remove("opacity");
  }
}


