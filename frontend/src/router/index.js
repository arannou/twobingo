import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../components/Dashboard.vue'
import GridEditor from '../components/GridComponents/GridEditor.vue'
import GridPlayer from '../components/GridComponents/GridPlayer.vue'
import Login from '../components/UserComponents/Login.vue'

const routes = [
    {
        path: '/',
        name: 'Dashboard',
        component: Dashboard,
    },
    {
        path: '/grid-creator',
        name: 'GridCreator',
        component: GridEditor,
    },
    {
        path: '/grid-editor',
        name: 'GridEditor',
        component: GridEditor,
        props: true,
    },
    {
        path: '/play/:gridId',
        name: 'GridPlayer',
        component: GridPlayer,
        props: true,
    },
    {
        path: '/login',
        name: 'Login',
        component: Login,
        props: { mode: 'login' },
    },
    {
        path: '/register',
        name: 'Register',
        component: Login,
        props: { mode: 'register' },
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router
