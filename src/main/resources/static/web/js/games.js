fetchData();
let player;

if (player){
console.log(player);
document.getElementById("form-logout").style.display = 'block';
          document.getElementById("form-login").style.display = 'none';
          document.getElementById("form-signup").style.display = 'none';
           document.getElementById("createNewGame").style.display = 'block';
}

// GET THE INFO TO SHOW THE LEADERBOARD FOR ALL PLAYERS

fetch("http://localhost:8080/api/leaderboard", {mode: 'cors'}

    ).then(function (response) {
    return response.json();

}).then((dataFromServer) => {
playersData = dataFromServer;

console.log(playersData);
updateLeaderBoard(playersData);
})

function updateLeaderBoard(data){

let rows = "";
let row = "";

var sortable = [];
for (var score in data) {
    sortable.push([score, data[score]]);

}

sortable.sort(function(a, b) {
    if(b[1].total > a[1].total){
 return 1;
    }

    else if(b[1].total < a[1].total){
    return -1;
    }
    else{
   return a[1].numberGames - b[1].numberGames;
    }
});

console.log(sortable);
sortable.forEach(score => {

// console.log(score);
let userName = score[0];
let total = score[1].total;
let wins = score[1].wins;
let ties = score[1].draw;
let losses = score[1].lost;
let numberGames = score[1].numberGames;

row = "<tr><td>" + userName + "</td><td>" + total + "</td><td>" + wins + "</td><td>" + losses + "</td><td>" + ties + "</td><td>" + numberGames + "</td></tr>";
rows += row;

})
$("#leaderBoard").html(rows);
};




document.getElementById("login-submit").addEventListener("click", function(e){
e.preventDefault();
login();
})


function showButtonsIfLoggedIn(){

if (player){
          document.getElementById("form-logout").style.display = 'block';
          document.getElementById("form-login").style.display = 'none';
          document.getElementById("form-signup").style.display = 'none';
           document.getElementById("createNewGame").style.display = 'block';

}
}

// LOGIN FUNCTION

function login() {

let userName= document.getElementById("username");
let password= document.getElementById("password");

     var ourData = {
        userName: userName.value,
        password: password.value
      };
            console.log(ourData);

      console.log(userName.value);
    //  console.log(ourData);

      fetch("/api/login", {
        credentials: "include",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        method: "POST",
        body: getBody(ourData)

      })
        .then(function(data) {

         if(data.ok){
          console.log("Request success: ", data);
                   alert("You are logged in");
                   window.location= "/web/games.html";
                   let playerLoggedIn = document.getElementById("player-logged-in");
                   playerLoggedIn.innerHTML = userName.value;
             //      UserLoggedIn(data);
                   fetchData();
         }else{

         console.log("Something went wrong!")
         }


        })
        .catch(function(error) {
          console.log("Request failure: ", error);
        });

      function getBody(json) {
      console.log(json)
        var body = [];
        for (var key in json) {
          var encKey = encodeURIComponent(key);
          var encVal = encodeURIComponent(json[key]);
          body.push(encKey + "=" + encVal);
        }
        return body.join("&");
      }
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
               window.location.href = "/web/games.html";
               document.getElementById("form-logout").style.display = 'none';
               document.getElementById("player-logged-in").style.display = 'none';
                         document.getElementById("form-login").style.display = 'block';
                         document.getElementById("form-signup").style.display = 'block';

         })
         .catch(function(error) {
           console.log("Request failure: ", error);
         });
     }

    // SIGN UP FUNCTION

document.getElementById("signup-submit").addEventListener("click", function(e){
e.preventDefault();
signUp();
})

    function signUp(){

    let userName= document.getElementsByClassName("username");
    let password= document.getElementsByClassName("password");

       let ourData = {
            userName: userName[0].value,
            password: password[0].value
          };

          document.getElementById("username").value = userName[0].value;
          document.getElementById("password").value = password[0].value;



console.log(ourData);

    fetch("/api/players",{
    method: "POST",
    headers:{
    'Content-Type': 'application/json'
    },
    body:JSON.stringify(ourData)
    } ).then(res=> res.json()).then(data=> {
    console.log(data)
    login();
    });




}



function fetchData () {

fetch("/api/games"

    ).then(function (response) {
    return response.json();

}).then((dataFromServer) => {
gamesData = dataFromServer;

player = dataFromServer.player;

console.log(gamesData);
updateGamesBoard(gamesData);
console.log(gamesData.player);

showButtonsIfLoggedIn(gamesData.player);


})

}

function updateGamesBoard(data){

let listGames;

for (let i=0; i < data.games.length; i++){
console.log(data.games[i].gamePlayers);

if (data.games[i].gamePlayers.length == 2){
console.log("players number equal 2");

listGames = data.games.map(game => '<tr><td>' + new Date(game.game_creation_date).toLocaleString() + '</td>' + game.gamePlayers.map(gp => '<td>' + gp.player.user_name  + '</td>').join('')  + '<td>' + addButton(game.gamePlayers, game.game_id) +'</td>' +'</tr>');

//listGames.map(gp => console.log(gp));

}

else if (data.games[i].gamePlayers.length < 2){
console.log("players less than 2");
listGames = data.games.map(game => '<tr><td>' + new Date(game.game_creation_date).toLocaleString() + '</td>' + game.gamePlayers.map(gp => '<td>' + gp.player.user_name  + '</td>').join('') +'<td>' + addButton(game.gamePlayers, game.game_id) +'</td>' +'</tr>');

// + '<td>' + "Waiting for opponent" + '</td>'
//listGames.map(gp => console.log(gp));

}



 }


//row = "<tr><td>" + userName + "</td><td>" + total + "</td><td>" + wins + "</td><td>" + losses + "</td><td>" + ties + "</td><td>" + numberGames + "</td></tr>";
//rows += row;

document.getElementById("gameBoard").innerHTML = listGames.join('');

joinGameClick();



};


function addButton (gameplayers, gameId) {

if (player) {

return gameplayers.map(gpl => {

if(gpl.player.user_id == player.user_id && gameplayers.length == 2){

 console.log("REJOIN");
 console.log(gpl.gamePlayer_id);

 return ('<button type="button" class="btn btn-warning">' + '<a href='+ `/web/game.html?gp=${gpl.gamePlayer_id}` + '>' + "REJOIN" +'</a>' +'</button>' );

 }

 else if(gpl.player.user_id == player.user_id && gameplayers.length < 2){

  console.log("REJOIN");
  console.log(gpl.gamePlayer_id);

  return ("Waiting for opponent" + '<td>'+ '<button type="button" class="btn btn-warning">' + '<a href='+ `/web/game.html?gp=${gpl.gamePlayer_id}` + '>' + "REJOIN" +'</a>' +'</button>' + '</td>');

  }

else if (gameplayers.length < 2){
console.log("JOIN");

return ("Waiting for opponent" + '<td>'+ '<button type="button" id=" '+gameId+' "class="link_joinGame btn btn-success" >' + "JOIN" +'</button>' + '</td>');}
}
).join('')
}

else{

return ("");
}


}

// CREATE NEW GAME



document.getElementById("createNewGame").addEventListener("click", function(e){
e.preventDefault();
createGame();
})

function createGame(){
fetch("/api/games", {credentials: "include", method:"POST"}

    ).then(function (response) {
    return response.json();

}).then((dataFromServer) => {
 location.href = "/web/game.html?gp=" + dataFromServer.GamePlayerID;

})
}

//function UserLoggedIn(player) {
//    return (player != null) ? true : false;
//}

function joinGameClick(){
Array.from(document.getElementsByClassName("link_joinGame")).forEach(jg => {
jg.addEventListener("click", function(event){
   joinGame(event.target.id);
   })
}
)
}



function joinGame(gameId) {
console.log(gameId);
fetch("/api/game/" + gameId + "/players", {credentials: "include", method:"POST"}

    ).then(function (response) {
    return response.json();

}).then((dataFromServer) => {
 location.href = "/web/game.html?gp=" + dataFromServer.newGamePlayerID;

})


}
