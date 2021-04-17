var Traffic = {
    template: `
    <div class="traffic">
        <ul id="sub-sidebar">
            <li v-for="item in sub_navigation">
                <small> {{ item.name }} </small>
                <ul>
                    <router-link
                        v-for="link in item.links"
                        key="link.to"
                        tag="li"
                        :to="link.to"
                        replace
                        exact
                    >
                    <a>{{ link.name }}</a>
                    </router-link>
                </ul>
            </li>
        </ul>
        <router-view></router-view>
    </div>
`,
    data() {
        return {
            sub_navigation:
                [
                    {
                        name: 'Alertes', links: [
                            { to: '/traffic/alerts/real-time', name: 'Temps réel' },
                            { to: '/traffic/alerts/history', name: 'Historiques' },
                        ]
                    },
                ]

        }
    }
}