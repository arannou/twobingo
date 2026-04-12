<script setup>


import { onMounted, ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { useGridStore } from '../../stores/grid'


const gridStore = useGridStore()
const { t } = useI18n()

const props = defineProps({
  cellId: String,
  gridId: String,
  index: Number
})

const editedCell = ref({
      content: null,
      checked: false
  })

onMounted(() => {
  console.log('CellComponent mounted with props:', props)
  if (props.gridId && props.cellId) {
    gridStore.loadCell(props.gridId, props.cellId)
  }
})



</script>
<template>
  <div class="cell-component">
    {{ editedCell.content  ?? props.index }}
</div>
</template>

<style>
.cell-component {
    width: 3rem;
    height: 3rem;
  padding: .5rem;
  text-align: center;
}
</style>