let table ='';
let rows = 8;
let cols = 8;

for (let r = 0; r < rows; r++){
table += '<div>';

for(let c = 0; c < cols; c++){

table += '<div>' + '</div>';

}

table += '</div>';
}

 document.getElementById("playerBoard").innerHTML ='<div>' + table + '</div>'