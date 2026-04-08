import { createApp } from 'vue'
import { createPinia } from 'pinia'
import { createI18n } from 'vue-i18n'
import router from './router'
import './style.css'
import App from './App.vue'
import enUS from './locales/en-US.json'
import frFR from './locales/fr-FR.json'

const i18n = createI18n({
    legacy: false,
    locale: 'en-US',
    messages: {
        'en-US': enUS,
        'fr-FR': frFR,
    },
})

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(i18n)
app.mount('#app')
