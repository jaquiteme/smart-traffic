var server = '127.0.0.2';
var BOSH_SERVICE = 'http://127.0.0.2:5280/http-bind/';
var connection = null;

function onConnect(status) {
    if (status == Strophe.Status.CONNECTING) {
        log('Strophe is connecting.');
    } else if (status == Strophe.Status.CONNFAIL) {
        log('Strophe failed to connect.');
        $('#connect').get(0).value = 'connect';
    } else if (status == Strophe.Status.DISCONNECTING) {
        log('Strophe is disconnecting.');
    } else if (status == Strophe.Status.DISCONNECTED) {
        log('Strophe is disconnected.');
        $('#connect').get(0).value = 'connect';
    } else if (status == Strophe.Status.CONNECTED) {
        log('Strophe is connected.');
        $('#to').get(0).value = connection.jid; // full JID
        // set presence
        connection.send($pres());
        // set handlers
        connection.addHandler(onMessage, null, 'message', null, null, null);
        connection.addHandler(onSubscriptionRequest, null, "presence", "subscribe");
        connection.addHandler(onPresence, null, "presence");

        listRooms();
    }
}

function rawInput(data) {
    console.log('RECV: ' + data);
}

function rawOutput(data) {
    console.log('SENT: ' + data);
}

$(document).ready(function () {

    $('#jid').get(0).value = "user1@example.com";
    $('#pass').get(0).value = "azerty";

    $('#connect').bind('click', function () {
        var url = BOSH_SERVICE;
        connection = new Strophe.Connection(url);
        connection.rawInput = rawInput;
        connection.rawOutput = rawOutput;
        var button = $('#connect').get(0);
        if (button.value == 'connect') {
            button.value = 'disconnect';
            connection.connect($('#jid').get(0).value, $('#pass').get(0).value, onConnect);
        } else {
            button.value = 'connect';
            connection.disconnect();
        }
    });
});