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

onMounted(() => {
  console.log(props.grid.id)
})

const copyLink = () => {
    const link = `${window.location.origin}/join/gridId=${props.grid.id}`
    navigator.clipboard.writeText(link)
        .then(() => {
            alert(t('gridCard.linkCopied'))
        })
        .catch(err => {
            console.error('Failed to copy link:', err)
            alert(t('gridCard.linkCopyFailed'))
        })
}

const confirmationVisible = ref(false)

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
    <div class="confirmation" v-if="confirmationVisible">
      <p>{{ t('gridCard.confirmDelete') }}</p>
      <div class="button-group">
        <button class="small" @click="gridStore.deleteGrid(props.grid.id)">{{ t('gridCard.yes') }}</button>
        <button class="small" @click="confirmationVisible = false">{{ t('gridCard.no') }}</button>
      </div>
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

.confirmation {
    position: absolute;
    background: #00000083;
    width: 100%;
    height: 100%;
    top: 0;
    right: 0;
    padding: 1rem;
}

.confirmation .button-group {
  justify-content: center;
}
</style>