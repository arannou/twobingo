<script setup>
import { useRouter, useRoute } from 'vue-router'
import { onMounted } from 'vue'
import { useI18n } from 'vue-i18n'

import UserThumbnail from './components/UserComponents/UserThumbnail.vue'

const router = useRouter()
const route = useRoute()
const { t, locale } = useI18n()

const languages = [
  { code: 'en-US', label: 'English (US)' },
  { code: 'fr-FR', label: 'Français' },
]

const storeLocale = () => {
  localStorage.setItem('locale', locale.value)
}

const loadLocale = () => {
  const loc = localStorage.getItem('locale')
  if (loc && languages.some(lang => lang.code === loc)) {
    locale.value = loc
  }
}

onMounted(() => {
  loadLocale()
})
</script>

<template>
  <div class="app">
    <header>
      <div class="app-header inverted">
        <h1 @click="router.push('/')">{{ t('app.title') }}</h1>
        <div>

          <select v-model="locale" class="locale-select" name="language-select" @change="storeLocale">
            <option v-for="lang in languages" :key="lang.code" :value="lang.code">
              {{ lang.label }}
            </option>
          </select>
          <UserThumbnail />
        </div>
      </div>
      <!-- <nav>
        <router-link to="/" class="nav-link" :class="{ active: route.path === '/' }">
          titre 1
        </router-link>
        <router-link to="/grid-editor" class="nav-link" :class="{ active: route.path === '/grid-editor' }">
          titre 2
        </router-link>
      </nav> -->
    </header>
    <main>
      <router-view />
    </main>
  </div>
</template>

<style lang="scss">

@use "variables";

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  text-align: center;
  min-height: 100vh;
}

.app {
  display: flex;
  flex-direction: column;
  height: 100vh;
}

header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
}

main {
  flex-grow: 2;
  border-radius: 20px;
  margin-top: -20px;
  background: var(--bg);
}

.app-header {
  padding: 20px;
  padding-bottom: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: none;

  h1 {
    font-size: 32px;
    margin: 0;
    cursor: default;
    color: var(--bg);
  }


}
nav {
  display: flex;
  gap: 20px;

  .nav-link {
    color: white;
    text-decoration: none;
    padding: 10px 20px;
    font-weight: 500;
    transition: background-color 0.3s;
    border-bottom: 2px solid transparent;

    &:hover {
      background-color: rgba(255, 255, 255, 0.1);
    }

    &.active {
      background-color: rgba(255, 255, 255, 0.2);
      border-bottom-color: white;
    }
  }
}

.locale-select {
  padding: 8px 10px;
  border: none;
  background: rgba(255, 255, 255, 0.15);
  color: white;
  min-width: 160px;

  option {
    background: var(--bg);
    color: var(--text);
  }

  ::picker() {
    border: none;
  }
}

.banner {
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}
.error-message {
  background-color: var(--red);

}

.loading-message {
  background-color: var(--yellow);
  color: #08060d;
}

.info-message {
  background-color: var(--info);
}




@media (max-width: variables.$breakPointMedium) {
  header {
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;

    h1 {
      font-size: 24px;
    }

    nav {
      width: 100%;
      gap: 10px;
      display: flex;
    }

    .nav-link {
      flex: 1;
      text-align: center;
    }
  }

}
</style>
