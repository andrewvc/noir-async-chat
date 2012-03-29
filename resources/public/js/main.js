window.log = function(){
  log.history = log.history || [];   // store logs to an array for reference
  log.history.push(arguments);
  if(this.console){
    console.log( Array.prototype.slice.call(arguments) );
  }
};

$(function() {
  var sock = window.roomSocket = new WebSocket("ws://localhost:3000/room");

  function createMessage (mtype, data) {
    return JSON.stringify({mtype: mtype, data: data}); 
  }

  function sendMessage (mtype, data) {
    var msg = createMessage(mtype, data)
    log("ws-tx", msg);
    return sock.send(msg);
  }

  var lastErrorEl = $("#chatroom-last-error");
  function showError (html) {
    lastErrorEl.html(html);
    lastErrorEl.show();
  }

  function hideError () {
    lastErrorEl.hide();
  }

  function showChat (msg) {
    var user = msg.handle;
    if (!user) { return; }
    var text = msg.text
    var formatted = "<div class='chat-msg'>" + 
                      "<span class='handle'>" + msg.handle + "</span>" +
                      " > " + 
                      "<span class='text'>" + msg.text + "</span>" +
                    "</div>";
                    
    $('#chatroom-messages').append(formatted);
  }

  sock.onopen = function (e) {
    $('#chatroom-controls').slideDown();
  }

  sock.onerror = function (e) {
    alert("Websocket had an error!");
  }

  sock.onmessage = function (e) {
    log("ws-rx", e.data);

    var msg = JSON.parse(e.data);

    if (msg.mtype === "chat") {
      showChat(msg.data);
    } else if (msg.mtype === "handle-set-fail") {
      showError("Handle in use. Please pick another handle");
    } else if (msg.mtype === "handle-set-succ") {
      $('#set-handle-form').slideUp();
      $('#chat-message-form').slideDown();
    } else if (msg.mtype === "all-handles") {
      $('#chatroom-handles').text(
        _.reduce(msg.data, function (m, h) {
          return (m + ", " + h);
        }), ""
      );
    }
  };

  $('#set-handle-form').submit(function (e) {
    e.preventDefault();
    hideError();
    var handle = $('#handle').val();
    sendMessage("set-handle", handle);
  });
  
  var textEl = $('#message-text');
  $('#chat-message-form').submit(function (e) {
    e.preventDefault();
    hideError();
    var text = textEl.val();
    sendMessage("chat", text);
    textEl.val("")
  });
});
