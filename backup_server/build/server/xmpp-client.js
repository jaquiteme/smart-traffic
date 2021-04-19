//[REQUIREMENTS]
const strophe = require("node-strophe").Strophe;
const Strophe = strophe.Strophe;

//[CONFIGURATION]
var server = 'bosh.example.com'
const jid = "backup01@example.com"
const password = "B@ckup!0"

// Set-up the connection
var BOSH_SERVICE = 'https://' + server + '/http-bind';
var connection = new Strophe.Connection(BOSH_SERVICE);

// Log XMPP
connection.rawInput = connection.rawOutput = console.log;

// Connect
connection.connect(jid, password);
