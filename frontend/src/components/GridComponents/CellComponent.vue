<script setup>


import { onMounted, ref, useTemplateRef, nextTick } from 'vue'
import { useI18n } from 'vue-i18n'
import { useGridStore } from '../../stores/grid'


const gridStore = useGridStore()
const { t } = useI18n()

const props = defineProps({
  cellId: String,
  gridId: String,
  index: Number
})

const emit = defineEmits(['update:cell'])

const editedCell = ref({
      content: null,
      checked: false
  })

const editInput = useTemplateRef('editInput')
const mode = ref('read') // or 'edit'

onMounted(() => {
  if (props.gridId && props.cellId) {
    gridStore.loadCell(props.gridId, props.cellId)
  }
})

const editCell = () => {
  mode.value = 'edit'
  console.log(editInput.value)
  // wait vue netxtick
  nextTick(() => {
    editInput.value.focus()
  })
}

const closeEdition = (evt) => {
    if (!evt.currentTarget.contains(evt.relatedTarget)) {
        mode.value = 'read';
        emit('update:cell', editedCell.value)
    }

}



</script>
<template>
  <div class="cell-component" @click="editCell" @focusout="evt => closeEdition(evt)">
    <span v-if="mode === 'read'">{{ editedCell.content  ?? props.index }}</span>
    <input ref="editInput" v-show="mode === 'edit'" type="text" v-model="editedCell.content"/>
  </div>
</template>

<style>
.cell-component {
  width: 5rem;
  height: 5rem;
  padding: .5rem;
  text-align: center;
}

.cell-component input {
  width: 100%;
  height: 100%;
  padding: .5rem;
  text-align: center;
}
</style>