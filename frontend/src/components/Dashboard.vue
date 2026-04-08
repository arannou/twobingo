<template>
  <div class="dashboard">

    <!-- Status Messages -->
    <div v-if="store.error" class="error-message">
      <p>{{ store.error }}</p>
      <!-- <button @click="store.clearError">Dismiss</button> -->
    </div>

    <div v-if="store.loading" class="loading-message">
      <p>loadingmessage</p>
    </div>

    <!-- Controls -->
    <div class="controls">
      <button @click="navigateToCreate" class="primary">
        ➕ create
      </button>
      <button @click="store.fetchAll" :disabled="store.loading">
        refresh
      </button>
    </div>


    <!-- Filters -->
    <div class="filter-section">
      <!-- <div>
        <label>
          <input
            type="checkbox"
            v-model="store.filterOwner"
            @change="store.toggleOwnerFilter"
          />
          Owner Only
        </label>
      </div>

      <div>
        <label>
          <input
            type="checkbox"
            v-model="store.filterArchived"
            @change="store.toggleArchivedFilter"
          />
          Archived Items
        </label>
      </div> -->
    </div>


    <!-- Items List -->
    <div v-if="store.grids.length > 0" class="grid-list">

    </div>

    <div v-else class="no-items">
      <p>No item found</p>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useGridStore } from '../stores/grid'
import { onMounted } from 'vue'
import { useI18n } from 'vue-i18n'


const router = useRouter()
const store = useGridStore()
const { t } = useI18n()




const navigateToCreate = () => {
  router.push('/create-item')
}

// Load items on component mount
onMounted(() => {
  store.fetchAll()
})
</script>

<style scoped>
.dashboard {
  margin: 0 auto;
  padding: 20px;
  font-family: Arial, sans-serif;
}


.error-message {
  background-color: #fee;
  border: 1px solid #f00;
  color: #f00;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.error-message button {
  background-color: #f00;
  color: white;
  border: none;
  padding: 5px 10px;
  border-radius: 3px;
  cursor: pointer;
  margin-top: 10px;
}

.loading-message {
  background-color: #ffe;
  border: 1px solid #ff0;
  color: #ff0;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.controls {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
  flex-wrap: wrap;
}

.controls button {
  background-color: var(--secondary);
  color: white;
  border: none;
  padding: 10px 15px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.controls button:hover:not(:disabled) {
  filter: brightness(110%);
}

.controls button:disabled {
  filter: brightness(80%);
  cursor: not-allowed;
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

.search-section {
  margin-bottom: 20px;
}

.search-section input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
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

.stats {
  display: flex;
  gap: 30px;
  margin-bottom: 20px;
  padding: 15px;
  background-color: var(--bg-highlight);
  border-radius: 4px;
}

.stats p {
  margin: 0;
}

.items-list {
  overflow-x: auto;
}

table {
  width: 100%;
  border-collapse: collapse;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  border-radius: 4px;
  overflow: hidden;
}

thead {
  font-weight: bold;
}

th,
td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: var(--secondary);
  color: white;
  font-weight: bold;
}
tr {
    background-color: var(--bg-highlight);
}
tr:hover {
  filter: brightness(105%);
}

tr:last-child td {
  border-bottom: none;
}

.low-stock {
  color: #f00;
  font-weight: bold;
}

.no-items {
  text-align: center;
  padding: 40px;
  border-radius: 4px;
}
</style>
