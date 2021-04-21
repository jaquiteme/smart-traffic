var Home = {
	template: `<div class="row justify-content-md-center">
    <div class="col-md-6">
      <div>Login</div>
      <form class="row g-3">
        <div class="col-12 form-group">
          <label class="form-label" for="jid">Username (JID):</label>
          <input class="form-control" type="text" :value="jid" id="jid" />
        </div>
        <div class="col-12 form-group">
          <label class="form-label" for="password">Password:</label>
          <input
            class="form-control"
            type="password"
            :value="password"
            id="password"
          />
        </div>
        <div class="col-12">
          <button
            v-if="connectionStatus != 5"
            class="btn btn-primary mb-3"
            @click="login($event)"
          >
            connect
          </button>
          <button
            v-if="connectionStatus == 5"
            class="btn btn-danger mb-3"
            @click="logout($event)"
          >
            disconnect
          </button>
        </div>
      </form>
    </div>
  </div>`,
	data() {
		return {
			jid: "central01@example.com",
			password: "Centr@l0!",
			credentials: {
				jid: "central01@example.com",
				password: "Centr@l0!"
			},
			pubsub_server: 'pubsub.example.com',
			server: "127.0.0.2",
			BOSH_SERVICE: "http://127.0.0.2:5280/http-bind/",
			connection: null,
			connectionStatus: null,
			subscribed: false,
		}
	},
	watch: {
		connectionStatus: function (val) {
			connectionStatus = val;
			if (val === 5) {
				this.$router.push("/traffic");
			}
		},

	},
	computed: {
		getMessages: function () {
			return this.messages;
		},

		getXmppConnectingStatus: function () {
			return this.connectionStatus
		}
	},
	methods: {
		login: function (event) {
			if (event) {
				event.preventDefault();
			}

			this.connection = new Strophe.Connection(this.BOSH_SERVICE);
			this.connection.rawInput = this.rawInput;
			this.connection.rawOutput = this.rawOutput;
			this.connection.connect(this.jid, this.password, this.onConnect);

			store
				.dispatch("ASSIGN_XMPP_SRV_CONNECTION", this.connection)
				.then(() => {
					console.log("Just wait..")
				})
				.catch(err => {
					console.log(err);
				});

		},

		logout: function (event) {
			if (event) {
				event.preventDefault();
			}
			this.connection.disconnect();
		},

		sleep: function (ms) {
			return new Promise(resolve => setTimeout(resolve, ms));
		},

		onConnect: function (status) {
			this.connectionStatus = status;
			if (status == Strophe.Status.CONNECTING) {
				console.log("Strophe is connecting.");
			} else if (status == Strophe.Status.CONNFAIL) {
				console.log("Strophe failed to connect.");
			} else if (status == Strophe.Status.DISCONNECTING) {
				console.log("Strophe is disconnecting.");
			} else if (status == Strophe.Status.DISCONNECTED) {
				console.log("Strophe is disconnected.");
			} else if (status == Strophe.Status.CONNECTED) {
				console.log("Statut: " + Strophe.Status.CONNECTED);
				console.log("Strophe is connected.");

				/*this.connection.pubsub.subscribe(
					this.jid,
					'pubsub.example.com',
					'denm_events',
					[],
					this.on_event,
					this.on_subscribe
				);*/

				// set presence
				this.connection.send($pres());
				// set handlers
				this.connection.addHandler(
					onMessage,
					null,
					"message",
					null,
					null,
					null
				);

				this.connection.addHandler(
					onSubscriptionRequest,
					null,
					"presence",
					"subscribe"
				);

				this.connection.addHandler(onPresence, null, "presence");

			}
		},

		rawInput: function (data) {
			console.log("RECV: " + data);
		},

		rawOutput: function (data) {
			console.log("SENT: " + data);
		},

		/*on_subscribe: function (sub) {
			this.subscribed = true;
			console.log("Now awaiting messages...");

			return true;
		},*/

		/*on_event: function (message) {
			if (!this.subscribed) {
				return true;
			}

			var server = "^" + this.pubsub_server.replace(/\./g, "\\.");
			var re = new RegExp(server);
			console.log(message.innerHTML)
			// this.messages.push({ id: message.id, message: message.textContent })
			store.dispatch('NEW_INCOMMING_ALERT', this.formatEventXmlToJson(message.innerHTML))

			/*if ($(message).attr('from').match(re) && $(message).attr('type') == 'headline') {
				var _data = $(message).children('event')
					.children('items')
					.children('item')
					.children('entry').text();

				if (_data) {
					console.log(_data)
				}
			}*/
			//return true
		/*},*/

		/*formatEventXmlToJson: function (xmppEvent) {
			let xmlDoc = new DOMParser().parseFromString(xmppEvent, 'application/xml');
			let id = xmlDoc.getElementsByTagName('item')[0].getAttribute('id')
			let eventType = xmlDoc.getElementsByTagName('item')[0].getElementsByTagName('alert')[0].getAttribute('type')
			let content = xmlDoc.getElementsByTagName('item')[0].getElementsByTagName('alert')[0].getElementsByTagName('content')[0].innerHTML
			let created_at = xmlDoc.getElementsByTagName('item')[0].getElementsByTagName('alert')[0].getElementsByTagName('created_at')[0].innerHTML

			let type = (eventType !== 'accident') ? 'alert alert-warning' : 'alert alert-danger'
			let currentTimeMillis = new Date().getTime()

			return { id: id, title: 'Signal', type: type, time: parseInt(created_at), message: content }
		},*/

	},
};
