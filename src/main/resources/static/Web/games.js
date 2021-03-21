window.onload = function() {
  request();
};

function request(){
$.get( "http://localhost:8080/api/games", function() {
  //alert( "success" );
})
  .done(function(response) {
    var OL = document.getElementById("OL");
    //$("#summaryOL").append( '<li>' + txt + '</li>' );
    //var json = JSON.parse(response);
    var len = response.length;
    for(var i=0; i<len; i++){
     newLI = document.createElement("li");
     newText = document.createTextNode(response[i].created);
               newLI.appendChild(newText);
        for(var j=0; j<response[i].gamePlayers.length; j++){
            newLI.innerHTML += response[i].gamePlayers[j].player.mail;
        }

          OL.append(newLI);
}
  });
  }

