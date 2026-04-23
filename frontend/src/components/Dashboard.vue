<template>
  <div class="dashboard">

    <!-- Status Messages -->
    <div v-if="gridStore.error" class="banner error-message">
      <p>{{ gridStore.error }}</p>
    </div>
    <div v-if="!userStore.currentUser.username" class="banner info-message">
      <p>{{ t('dashboard.connectToCreateGrids') }}</p>
    </div>

    <div v-if="gridStore.loading" class="banner loading-message">
      <p>{{ t('dashboard.loading') }}</p>
    </div>

    <!-- Controls -->
    <div v-if="userStore.currentUser.username" class="button-group">
      <button @click="navigateToCreate" class="primary">
        ➕ {{ t('dashboard.create') }}
      </button>
      <button @click="loadGrids" :disabled="gridStore.loading">
        {{ t('dashboard.refresh') }}
      </button>
    </div>


    <!-- Filters -->
    <div v-if="userStore.currentUser.username" class="filter-section">
      <div>
        <label>
          <input
            type="checkbox"
            v-model="gridStore.filterOwner"
            @change="gridStore.toggleOwnerFilter"
          />
          {{ t('dashboard.ownerOnly') }}
        </label>
      </div>

      <div>
        <label>
          <input
            type="checkbox"
            v-model="gridStore.filterArchived"
            @change="gridStore.toggleArchivedFilter"
          />
          {{ t('dashboard.archivedItems') }}
        </label>
      </div>
    </div>


    <!-- Items List -->
    <div v-if="gridsList.length > 0" class="grid-list">
      <GridCard
        v-for="grid in gridsList"
        :key="grid.id"
        :grid="grid"
      >
      </GridCard>
    </div>

    <div v-else class="no-items">
      <p>{{ t('dashboard.noItems') }}</p>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useGridStore } from '../stores/grid'
import { useUserStore } from '../stores/user'
import { computed, onMounted, ref, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import GridCard from './GridComponents/GridCard.vue'


const router = useRouter()
const gridStore = useGridStore()
const userStore = useUserStore()
const { t } = useI18n()

const navigateToCreate = () => {
  router.push('/grid-creator')
}

const loadGrids = async () => {
  if (userStore.currentUser.username) {
    gridStore.fetchForUser()
  } else {
    console.log('No user logged in, loading grids from localStorage')
    let lsGrids = localStorage.getItem('grids')
    if (lsGrids) {
      try {
        const parsedGrids = JSON.parse(lsGrids)
        gridStore.grids = parsedGrids
      } catch (e) {
        console.error('Failed to parse grids from localStorage:', e)
        gridStore.grids = []
      }
    } else {
      gridStore.grids = []
    }
  }
}


const gridsList = computed(() => {
  return gridStore.grids
})
// Load items on component mount
onMounted(() => {
  loadGrids()
})

// watcher on userStore.currentUser.username to reload grids when user logs in or out
watch(() => userStore.currentUser.username, (newUsername, oldUsername) => {
  loadGrids()
})


</script>

<style scoped lang="scss">

@use "./../variables.scss";

.dashboard {
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}

.item-image {
  width: 60px;
  height: 60px;
  object-fit: cover;
  border-radius: 4px;
  border: 1px solid #ddd;
}

.image-placeholder {
  display: inline-block;
  color: #999;
  font-size: 12px;
}


.filter-section {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.filter-section select {
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
}



.no-items {
  text-align: center;
  padding: 40px;
  border-radius: 4px;
}

.grid-list {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
}

@media (max-width: variables.$breakPointSmall) {
  .grid-list {
    flex-direction: column;
    align-items: center;
  }
}

</style>
