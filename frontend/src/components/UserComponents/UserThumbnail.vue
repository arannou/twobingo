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

const toggleDropdown = () => {
  isDropdownOpen.value = !isDropdownOpen.value
}

onMounted(() => {
  store.isUserLoggedIn()
})
</script>

<template>
  <div class="user-thumbnail">
    <div class="user-info">
        <img
            v-if="store.currentUser.avatarImagePath"
            :src="store.currentUser.avatarImagePath"
            :alt="t('userThumbnail.altProfilePicture')"
            class="profile-picture"
        />
        <span v-else class="profile-placeholder">⚫</span>
        <div class="user-details">
            <p class="user-name">{{ store.currentUser.name }}</p>
        </div>
    </div>
    <!-- dropdown -->
    <div class="user-dropdown">
        <button class="dropdown-toggle" @click="toggleDropdown">▼</button>
        <div class="dropdown-menu" v-show="isDropdownOpen">
            <a href="#" class="dropdown-item">{{ t('userThumbnail.profile') }}</a>
            <a href="#" class="dropdown-item">{{ t('userThumbnail.settings') }}</a>
            <a href="#" class="dropdown-item">{{ t('userThumbnail.logout') }}</a>
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
</style>