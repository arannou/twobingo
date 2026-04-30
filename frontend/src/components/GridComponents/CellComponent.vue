<script setup>


import { onMounted, ref, useTemplateRef, nextTick, watch } from 'vue'
import { useI18n } from 'vue-i18n'
import { useGridStore } from '../../stores/grid'


const gridStore = useGridStore()
const { t } = useI18n()

const props = defineProps({
  index: Number,
  content: String,
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

const editInput = useTemplateRef('editInput')
const mode = ref('read') // or 'edit'

onMounted(() => {
  if (props.content) {
    editedCell.value.content = props.content
  }
})

const clickCell = () => {
  if (!props.editable) {
    editedCell.value.checked = !editedCell.value.checked
    emit('check', editedCell.value.checked)
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

// watcher on props.content to update editedCell when content changes from outside
watch(() => props.content, (newContent) => {
  editedCell.value.content = newContent
})



</script>
<template>
  <div :class="'cell-component ' + (editedCell.checked ? 'checked' : '')" @click="clickCell" @focusout="evt => closeEdition(evt)" >
    <span v-if="mode === 'read'">{{ editedCell.content  ?? props.index }}</span>
    <input class="content-input" ref="editInput" :name="'cell-content-' + props.index" v-show="mode === 'edit'" type="text" v-model="editedCell.content"/>
    <input class="check-input" v-show="mode !== 'edit'" type="checkbox" v-model="editedCell.checked" @change="$emit('check', editedCell.checked)"/>
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