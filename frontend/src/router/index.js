import { createRouter, createWebHistory } from 'vue-router'
import Dashboard from '../components/Dashboard.vue'
import GridEditor from '../components/GridComponents/GridEditor.vue'

const routes = [
    {
        path: '/',
        name: 'Dashboard',
        component: Dashboard,
    },
    {
        path: '/grid-editor',
        name: 'GridEditor',
        component: GridEditor,
    },
]

const router = createRouter({
    history: createWebHistory(),
    routes,
})

export default router
