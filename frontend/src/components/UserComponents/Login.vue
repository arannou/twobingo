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
    userStore.error = null; // Clear any previous errors
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
    <div class="form">
        <template v-if="mode === 'login'">
            <a href="#" @click="router.push('/register')">{{ t('login.noAccount') }}</a>
            <input v-model="username" type="text" :placeholder="t('login.username')" />
            <input v-model="password" type="password" :placeholder="t('login.password')" @keydown.enter="login()" />
            <div class="button-group">
                <button class="primary" @click="login()">{{ t('login.login') }}</button>
                <button @click="router.push('/')">{{ t('login.cancel') }}</button>
            </div>
        </template>
        <template v-else>
            <a href="#" @click="router.push('/login')">{{ t('login.haveAccount') }}</a>
            <input v-model="username" name="username" type="text" :placeholder="t('login.username')" />
            <input v-model="password" name="password" type="password" :placeholder="t('login.password')" @keydown.enter="register()"/>
            <input v-model="repeatedPassword" name="repeatedPassword" type="password" :placeholder="t('login.repeatPassword')" />

            <div class="button-group">
                <button class="primary" @click="register()">{{ t('login.register') }}</button>
                <button @click="router.push('/')">{{ t('login.cancel') }}</button>
            </div>
            
        </template>
        <div v-if="userStore.error" class="banner error-message">{{ userStore.error }}</div>
    </div>

  </div>
</template>

<style lang="scss">

@use "./../../variables.scss";

.login-view .form {
    display: flex;
    flex-direction: column;
    gap: 1rem;
    padding: 2rem 4rem;

    width: 50%;
    margin: auto;
    border-radius: 20px;
    background-color: var(--bg-highlight);

    button {
        padding: 0.5rem 1rem;
    }

    .button-group {
        justify-content: center;
    }
}

@media (max-width:  variables.$breakPointMedium) {
    .login-view .form {
        width: 100%;
        padding: 1rem;
    }
}
</style>