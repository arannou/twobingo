<script setup>
import { useRouter, useRoute } from 'vue-router'
import { onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../../stores/user'
import { useGridStore } from '../../stores/grid'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const gridStore = useGridStore()
const { t } = useI18n()



onMounted(() => {
  gridStore.isUserLoggedIn()
})
</script>

<template>
  <div class="grid-editor">
    
  </div>
</template>

<style></style>