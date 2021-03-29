$(function () {
  loadData();
});

function getParameterByName(name) {
  var match = RegExp('[?&]' + name + '=([^&]*)').exec(window.location.search);
  return match && decodeURIComponent(match[1].replace(/\+/g, ' '));
}

function loadData() {
  $.get('/api/game_view/' + getParameterByName('gp'))
    .done(function (data) {
      var playerInfo;
      if (data.gamePlayers[0].id == getParameterByName('gp'))
        playerInfo = [data.gamePlayers[0].player, data.gamePlayers[1].player];
      else
        playerInfo = [data.gamePlayers[1].player, data.gamePlayers[0].player];

      $('#playerInfo').text(playerInfo[0].mail + '(you) vs ' + playerInfo[1].mail);

      data.Ships.forEach(function (shipPiece) {
        shipPiece.location.forEach(function (shipLocation) {
          if(isHit(shipLocation,data.Salvos,playerInfo[0].id)  !=  0){
            $('#B_' + shipLocation).addClass('ship-piece-hited');
            $('#B_' + shipLocation).text(isHit(shipLocation,data.Salvos,playerInfo[0].id));
          }

          else
            $('#B_' + shipLocation).addClass('ship-piece');
        });
      });
      data.Salvos.forEach(function (salvo) {
        if (playerInfo[0].id === salvo.player) {
          salvo.location.forEach(function (location) {
            $('#S_' + location).addClass('salvo-piece');
            $('#S_' + location).text(salvo.turn);

          });
        } else {
          salvo.location.forEach(function (location) {
            $('#B_' + location).addClass('salvo');
          });
        }
      });
    })
    .fail(function (jqXHR, textStatus) {
      alert('Failed: ' + textStatus);
    });
}

function isHit(shipLocation,Salvos,playerId) {
  var turn = 0;
  Salvos.forEach(function (salvo) {
    if(salvo.player != playerId)
      salvo.location.forEach(function (location) {
        if(shipLocation === location)
          turn = salvo.turn;
      });
  });
  return turn;
}