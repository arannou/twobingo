<script setup>
import { useRouter, useRoute } from 'vue-router'
import { ref, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../../stores/user'
import { useGridStore } from '../../stores/grid'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
// const gridStore = useGridStore()
const { t } = useI18n()

const isloggedIn = ref(false)

const username = ref('')
const password = ref('')
const repeatedPassword = ref('')

// props
const props = defineProps({
    mode: {
        type: String,
        default: 'login'
    }
})


const login = () => {
    userStore.login(username.value, password.value).then(() => {
        if (!userStore.error) {
            router.push('/')
        }
    })
}

const register = () => {
    userStore.register(username.value, password.value, repeatedPassword.value).then(() => {
    if (!userStore.error) {
        router.push('/')
    }
    })
}
onMounted(() => {

    userStore.getLoggedUser().then((user) => {
        if (user) {
            isloggedIn.value = true;
            router.push('/'); // Redirect to home page if already logged in

        }
    });
    
})
</script>

<template>
  <div class="login-view">
    <div clas="mode">
        <nav>
            <button @click="router.push('/login')">{{ t('login.login') }}</button>
            <button @click="router.push('/register')">{{ t('login.register') }}</button>
        </nav>
    </div>
    <div class="form">
        <template v-if="mode === 'login'">
            <input v-model="username" type="text" :placeholder="t('login.username')" />
            <input v-model="password" type="password" :placeholder="t('login.password')" />
            <button @click="login()">{{ t('login.login') }}</button>
        </template>
        <template v-else>
            <input v-model="username" type="text" :placeholder="t('login.username')" />
            <input v-model="password" type="password" :placeholder="t('login.password')" />
            <input v-model="repeatedPassword" type="password" :placeholder="t('login.repeatPassword')" />
            <button @click="register()">{{ t('login.register') }}</button>
        </template>
        <div v-if="userStore.error" class="error">{{ userStore.error }}</div>
        <div class="button-group">
            <button @click="router.push('/')">{{ t('login.backToHome') }}</button>
        </div>
    </div>

  </div>
</template>

<style></style>