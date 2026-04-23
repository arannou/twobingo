<script setup>

import { useRouter } from 'vue-router'
import { onMounted, ref, useTemplateRef, nextTick } from 'vue'
import { useI18n } from 'vue-i18n'
import { useGridStore } from '../../stores/grid'


const router = useRouter()
const gridStore = useGridStore()
const { t } = useI18n()

const props = defineProps({
  grid: Object
})


const copyLink = () => {
    const link = `${window.location.origin}/join/gridId=${props.grid.id}`
    navigator.clipboard.writeText(link)
        .then(() => {
            copySuccessVisible.value = true
            setTimeout(() => {
                copySuccessVisible.value = false
            }, 4000)
        })
        .catch(err => {
            console.error('Failed to copy link:', err)
            copyFailureVisible.value = true
            setTimeout(() => {
                copyFailureVisible.value = false
            }, 4000)
        })
}

const confirmationVisible = ref(false)
const copySuccessVisible = ref(false)
const copyFailureVisible = ref(false)

const onDeleteClick = () => {
    confirmationVisible.value = true
}

const onEditGrid = () => {
  gridStore.selectedGrid = props.grid
  router.push({name: 'GridEditor', query: { 'gridId': props.grid.id }})
}

</script>
<template>
  <div class="grid-card">
    <div class="confirmation confirm-delete" v-if="confirmationVisible">
      <p>{{ t('gridCard.confirmDelete') }}</p>
      <div class="button-group">
        <button class="small" @click="gridStore.deleteGrid(props.grid.id)">{{ t('gridCard.yes') }}</button>
        <button class="small" @click="confirmationVisible = false">{{ t('gridCard.no') }}</button>
      </div>
    </div>
    <div class="confirmation confirm-success" v-if="copySuccessVisible">
      <p>{{ t('gridCard.copySuccess') }}</p>
    </div>
      <div class="confirmation confirm-failure" v-if="copyFailureVisible">
      <p>{{ t('gridCard.copyFailure') }}</p>
    </div>
    <h3>{{ props.grid.name }}</h3>
    <p>{{ props.grid.description }}</p>

    <div class="icon-group">
        <button class="small" @click="router.push(`/play/gridId=${props.grid.id}`)" :title="t('gridCard.play')">⚡▶️▶</button>
        <button class="small" @click="copyLink()" :title="t('gridCard.share')">🔗</button>
        <button class="small" @click="onEditGrid()" :title="t('gridCard.edit')">✏️</button>
        <button class="small" @click="onDeleteClick()" :title="t('gridCard.delete')">🗑️</button>
    </div>
  </div>
</template>

<style lang="scss">
.grid-card {
  border-radius: 5px;
  background-color: var(--bg-highlight);
  padding: 1rem;
  position: relative;

  .icon-group {
    margin-top: 10px;
  }
}

@keyframes fadeConfirmation {
	0% {
		opacity: 1;
	}

	100% {
		opacity: 0;
	}
}

.confirmation {
    position: absolute;
    border-radius: 5px;
    width: 100%;
    height: 100%;
    top: 0;
    right: 0;
    padding: 1rem;

    &.confirm-delete {
      background: #00000083;
    }
    &.confirm-success {
      background: #00ff0083;
      // animate opacity from 1 to 0 over 2 seconds, then hide
      animation: fadeConfirmation 2s ease-in-out 2s 1 normal forwards;
    }
    &.confirm-failure {
      background: #ff000083;
      animation: fadeConfirmation 2s ease-in-out 2s 1 normal forwards;
    }

}

.confirmation .button-group {
  justify-content: center;
}
</style>