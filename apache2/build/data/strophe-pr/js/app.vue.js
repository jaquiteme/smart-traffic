new Vue({
    el: '#app',
    data: {
        jid: "user1@example.com",
        password: "azerty",
        message: 'Hello Vue!',
        server: '127.0.0.2',
        BOSH_SERVICE: 'http://127.0.0.2:5280/http-bind/',
        connection: null,
        connectionStatus: null
    },
    watch: {
        connectionStatus: function (val, oldVal) {
            console.log('new: %s, old: %s', val, oldVal)
            connectionStatus = val
        },
    },
    methods: {
        login: function (event) {
            if (event) {
                event.preventDefault()
            }
            this.connection = new Strophe.Connection(this.BOSH_SERVICE);
            this.connection.rawInput = this.rawInput;
            this.connection.rawOutput = this.rawOutput;
            this.connection.connect(this.jid, this.password, this.onConnect);
        },
        logout: function (event) {
            if (event) {
                event.preventDefault()
            }
            this.connection.disconnect();
        },
        onConnect: function (status) {
            this.connectionStatus = status
            if (status == Strophe.Status.CONNECTING) {
                console.log('Strophe is connecting.');
            } else if (status == Strophe.Status.CONNFAIL) {
                console.log('Strophe failed to connect.');
            } else if (status == Strophe.Status.DISCONNECTING) {
                console.log('Strophe is disconnecting.');
            } else if (status == Strophe.Status.DISCONNECTED) {
                console.log('Strophe is disconnected.');
            } else if (status == Strophe.Status.CONNECTED) {
                console.log("Statut: " + Strophe.Status.CONNECTED);
                console.log('Strophe is connected.');
                // set presence
                this.connection.send($pres());
                // set handlers
                this.connection.addHandler(onMessage, null, 'message', null, null, null);
                this.connection.addHandler(onSubscriptionRequest, null, "presence", "subscribe");
                this.connection.addHandler(onPresence, null, "presence");
            }
        },
        rawInput: function (data) {
            console.log('RECV: ' + data);
        },
        rawOutput: function (data) {
            console.log('SENT: ' + data);
        }
    }
})