<script setup>
import { useRouter, useRoute } from 'vue-router'
import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../../stores/user'

const router = useRouter()
const route = useRoute()
const store = useUserStore()
const { t } = useI18n()
const isDropdownOpen = ref(false)
const isloggedIn = ref(false)

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

const logout = () => {
    store.logout().then(() => {
        router.push('/')
    })
}

onMounted(() => {
    store.getLoggedUser().then((user) => {
      if (user) {
        isloggedIn.value = true;
      }
    });

})
</script>

<template>
  <div class="user-thumbnail">
    <div class="user-info">
        <img
            v-if="store.currentUser.avatarImagePath"
            :src="'src/assets/avatars/' + store.currentUser.avatarImagePath"
            width="40px"
            height="40px"
            :alt="t('userThumbnail.altProfilePicture')"
            class="profile-picture"
        />
        <span v-else class="profile-placeholder">⚫</span>
        <div class="user-details">
            <p class="user-name" v-if="store.currentUser.username">{{ store.currentUser.username }}</p>
            <p class="user-name" v-else>{{ t('userThumbnail.guest') }}</p>
        </div>
    </div>
    <!-- dropdown -->
    <div class="user-dropdown" >
        <button class="dropdown-toggle" @click="toggleDropdown">{{isDropdownOpen ? '▲' : '▼'}}</button>
        <div class="dropdown-menu" v-show="isDropdownOpen" @blur="isDropdownOpen = false">
            <template v-if="!isloggedIn">
                <a href="#" class="dropdown-item" @click="router.push('/login'); isDropdownOpen = false">{{ t('userThumbnail.login') }}</a>
                <a href="#" class="dropdown-item" @click="router.push('/register'); isDropdownOpen = false">{{ t('userThumbnail.register') }}</a>
            </template>
            <template v-else>
                <a href="#" class="dropdown-item">{{ t('userThumbnail.profile') }}</a>
                <a href="#" class="dropdown-item">{{ t('userThumbnail.settings') }}</a>
                <a href="#" class="dropdown-item" @click="logout()">{{ t('userThumbnail.logout') }}</a>
            </template>
        </div>
    </div>
  </div>
</template>

<style scoped>
.user-dropdown {
  position: relative;
}

.dropdown-menu {
  position: absolute;
  top: 100%;
  right: 0;
  border-radius: 4px;
  min-width: 150px;
}

.dropdown-item {
  display: block;
  padding: 8px 12px;
  text-decoration: none;
  color: var(--text);
  background: var(--bg);
}

.dropdown-item:hover {
  filter: brightness(105%);
  font-weight: bold;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 1rem;
}
.user-thumbnail {
  display: flex;
  align-items: center;
  gap: 1rem;
}
</style>