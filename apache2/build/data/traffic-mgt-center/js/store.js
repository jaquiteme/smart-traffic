// import { BOSH_SERVICE } from './app.config.js';

const store = new Vuex.Store({
    state: {
        connection: null,
        connectionStatus: null,
        subscribed: false,
        alerts: [],
        navigation: [
            { to: '/dashboard', name: 'Dashboard', icon: 'mdi mdi-view-dashboard-outline' },
            { to: '/traffic', name: 'Traffic', icon: 'mdi mdi-traffic-cone' },
            { to: '/data', name: 'Données', icon: 'mdi mdi-database' },
        ],
        sub_navigation: {
            "nav-traffic": [
                { to: '/nav-traffic/alerts', name: 'Alerts', icon: 'mdi mdi-traffic-cone' },
                { to: '/nav-traffic//messages', name: 'Road messages', icon: 'mdi mdi-traffic-cone' },
            ]
        },
        active_sub_nav: []
    },
    getters: {

    },
    actions: {
        ASSIGN_XMPP_SRV_CONNECTION: ({ commit }, payload) => {
            commit('CONNECTED_XMPP_USER', payload)
        },
        DECO_XMPP_SRV_CONNECTION: ({ commit }) => {
            commit('DISCONNECT_TO_XMPP')
        },
        NEW_INCOMMING_ALERT: ({ commit }, payload) => {
            commit('ADD_INCOMMING_ALERT', payload)
        },
        ADD_SUB_NAV_LINKS: ({ commit }, payload) => {
            commit('SET_SUB_ACTIVE_LINKS', payload)
        }
    },
    mutations: {
        CONNECTED_XMPP_USER: (state, connection) => {
            state.connection = connection
        },
        DISCONNECT_TO_XMPP: (state) => {
            state.connection.disconnect()
            state.connection = null
        },
        ADD_INCOMMING_ALERT: (state, alert) => {
            state.alerts.unshift(alert)
        },
        SET_SUB_ACTIVE_LINKS: (state, key) => {
            console.log(key)
            if (key.length !== 0)
                state.active_sub_nav = state.sub_navigation[key]
            else
                state.active_sub_nav = []
        }
    }
})

