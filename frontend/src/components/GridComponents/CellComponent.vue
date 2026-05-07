<script setup>


import { onMounted, ref, useTemplateRef, nextTick, watch, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { useGridStore } from '../../stores/grid'


const gridStore = useGridStore()
const { t } = useI18n()

const props = defineProps({
  data: Object,
  editable: {
    type: Boolean,
    default: true
  }
})

const emit = defineEmits(['update:cell', 'check'])

const editedCell = ref({
      content: null,
      checked: false
  })
const index = ref(-1)

const editInput = useTemplateRef('editInput')
const mode = ref('read') // or 'edit'

onMounted(() => {
  if (props.data) {
    editedCell.value.content = props.data.content ?? ''
    editedCell.value.isChecked = props.data.isChecked ?? false
    index.value = props.data.position ?? -1 
  }
})

const clickCell = () => {
  if (!props.editable) {
    editedCell.value.isChecked = !editedCell.value.isChecked
    emit('check', editedCell.value.isChecked)
  } else {
    mode.value = 'edit'
    // wait vue netxtick
    nextTick(() => {
      editInput.value.focus()
    })
  }
}

const closeEdition = (evt) => {
    if (!evt.currentTarget.contains(evt.relatedTarget)) {
        mode.value = 'read';
        emit('update:cell', editedCell.value)
    }

}

const isChecked = computed(() => {
  return editedCell.value.isChecked
})

// watcher on props.content to update editedCell when content changes from outside
watch(() => props.data, (newdata) => {
  if (newdata) {
    editedCell.value.content = newdata.content ?? ''
    editedCell.value.isChecked = newdata.isChecked ?? false
  }
})



</script>
<template>
  <div :class="'cell-component ' + (isChecked ? 'checked' : '')" @click="clickCell()" @focusout="evt => closeEdition(evt)" >
    <span v-if="mode === 'read'">{{ editedCell.content  ?? index }}</span>
    <input class="content-input" ref="editInput" :name="'cell-content-' + index" v-show="mode === 'edit'" type="text" v-model="editedCell.content"/>
    <input class="check-input" v-show="mode !== 'edit'" type="checkbox" 
    :checked="isChecked" v-model="editedCell.isChecked" @change="$emit('check', editedCell.isChecked)"/>
  </div>
</template>

<style lang="scss">

@use "./../../variables.scss";

.cell-component {
  width: 20%;
  height: 20%;
  padding: .5rem;
  text-align: center;
  border: 1px solid var(--text);
  position: relative;

  input.check-input {
    position: absolute;
    top: 0;
    right: 0;
    width: 1.5rem;
    height: 1.5rem;
  }
}

.cell-component input.content-input {
  width: 100%;
  height: 100%;
  padding: .5rem;
  text-align: center;
}

.theme-round .cell-component {
  border-radius: 50%;
}

.size-3 .cell-component {
  width: 22vh;
  height: 22vh;
}

.size-4 .cell-component {
  width: 17vh;
  height: 17vh;
}

.size-5 .cell-component {
  width: 14vh;
  height: 14vh;
}

@media (max-width: variables.$breakPointSmall) {
  .size-3 .cell-component {
    width: 30vw;
    height: 30vw;
  }

  .size-4 .cell-component {
    width: 22vw;
    height: 22vw;
  }

  .size-5 .cell-component {
    width: 18vw;
    height: 18vw;
  }
}
</style>