<script setup>
import { useRouter, useRoute } from 'vue-router'
import { computed, onBeforeMount, ref, onBeforeUnmount } from 'vue'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../../stores/user'
import { useGridStore } from '../../stores/grid'
import CellComponent from './CellComponent.vue'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const gridStore = useGridStore()
const { t } = useI18n()
const mode = ref('create')


const error = ref(null)

const THEMES = ['square','round']
const COLORS = [
  { name: 'Black and white',
    foreground: '#fff',
    background: '#000',
    textColor: '#000000'
  },
  {
    name: 'Green',
    foreground: '#00ff00',
    background: '#aaffaa',
    textColor: '#000000'
  },
  {
    name: 'Blue',
    foreground: '#0000ff',
    background: '#aaaaff',
    textColor: '#000000'
  },
]

const editedGrid = ref({
      name: '',
      description: '',
      size: 3,
      theme: THEMES[0],
      gridColor: COLORS[0],
  })

onBeforeMount(() => {
  if (gridStore.selectedGrid?.id) {
      mode.value = 'edit'
      loadGridData(gridStore.selectedGrid)
    } else {
      if (route.query.gridId) {
        const gridId = parseInt(route.query.gridId)
        gridStore.fetchGridById(gridId).then((gridToEdit) => {
          if (gridToEdit) {
            mode.value = 'edit'

            loadGridData(gridToEdit)
          } else {
            console.warn('No grid found with id from route query:', route.query.gridId)
          }
        })

      }
      mode.value = 'create'
    }
  
})

const loadGridData = (data) => {
      editedGrid.value = { ...data };
      // find color object from COLORS array that matches the gridColor name
      const colorObj = COLORS.find(color => color.name === data.color)
      if (colorObj) {
        editedGrid.value.gridColor = colorObj;
      } else {
        editedGrid.value.gridColor = COLORS[0];
      }
  }


const cellsByPosition = computed(() => {
  if (!editedGrid.value.cells) return {};
  let byPos = {}
  for (let cell of editedGrid.value.cells) {
    byPos[cell.position] = cell.content;
  }
  return byPos;
})

const handleCellUpdate = (cellIndex, cellData) => {
    if (!editedGrid.value) return;
    if (!editedGrid.value.cells) {
        editedGrid.value.cells = [];
        editedGrid.value.cells.push({ content: cellData.content, checked: false, position: cellIndex });
        return;
    }
    const updatedCells = editedGrid.value.cells.map(cell => {
      if (cell.position === cellIndex) {
        cell.content = cellData.content;
      }
      return cell;
    });
    // if cell with position cellIndex doesn't exist, add it
    if (!editedGrid.value.cells.some(cell => cell.position === cellIndex)) {
        editedGrid.value.cells.push({ content: cellData.content, checked: false, position: cellIndex });
    } else {
        editedGrid.value.cells = updatedCells;
    }

}

const validateGrid = () => {
  error.value = null
  if (!editedGrid.value.name) {
    error.value = 'Please enter a grid name'
    return false
  }
  if (editedGrid.value.size < 3 || editedGrid.value.size > 5) {
    error.value = 'Grid size must be between 3 and 5'
    return false
  }
  if (!editedGrid.value.cells) {
    error.value = 'Grid must have at least one cell'
    return false
  }
  return true
}
const submit = () => {
  if (!validateGrid()) {
    return
  }
  if (!editedGrid.value.gridColor) {
    editedGrid.value.gridColor = COLORS[0]
  }
  if (mode.value === 'create') {
    gridStore.createGrid(editedGrid.value).then(() => {
      router.push('/')
    })
  } else if (mode.value === 'edit') {
    gridStore.updateGrid(gridStore.selectedGrid.id, editedGrid.value).then(() => {
      router.push('/')
    })
  }
}
// Before unmount, reset the selected grid in the store to avoid stale data when creating a new grid after editing
onBeforeUnmount(() => {
  gridStore.selectedGrid = null
})
</script>

<template>
  <div class="grid-editor">
    <div class="form">
      <div class="name-input">
        <label for="grid-name">Grid Name</label>
        <input type="text" name="grid-name" id="grid-name" v-model="editedGrid.name" placeholder="Grid Name" autocomplete="off">
      </div>
      <div class="description-input">
        <label for="grid-description">Description</label>
        <textarea name="grid-description" id="grid-description" v-model="editedGrid.description" placeholder="Grid Description"></textarea>
      </div>
      <div class="size-selection">
        <label for="grid-size">Size</label>
        <select name="grid-size" id="grid-size" v-model="editedGrid.size">
          <option value="3">3x3</option>
          <option value="4">4x4</option>
          <option value="5">5x5</option>
        </select>
      </div>
      <div class="theme-selection">
        <label for="grid-theme">Theme</label>
        <select name="grid-theme" id="grid-theme" v-model="editedGrid.theme">
          <option v-for="theme in THEMES" :value="theme">
            {{ theme }}
          </option>
        </select>
      </div>
      <div class="color-selection">
        <label for="grid-color">Color</label>
        <select name="grid-color" id="grid-color" v-model="editedGrid.gridColor">
          <option v-for="color in COLORS" :value="color">
            {{ color.name }}
          </option>
        </select>
      </div>

      <div class="button-group">
        <button type="submit" class="primary" @click="submit()">Save</button>
        <button type="submit" @click="router.push('/')">Cancel</button>
      </div>
      <div v-if="error" class="banner error-message">{{ error }}</div>

    </div>

    <div class="preview">
      <h3>Preview</h3>
      <div class="grid-preview" :class="'theme-' +editedGrid.theme" >
        <div class="grid-container" :class="'size-' +editedGrid.size + ' color-' + editedGrid.gridColor.name.toLowerCase().replace(/\s/g, '-')">
        <div v-for="n in parseInt(editedGrid.size)*parseInt(editedGrid.size)" :key="'row-' + n" class="grid-cell">
            <CellComponent :content="cellsByPosition[n] ?? ''" :index="n" @update:cell="(cellData)  => handleCellUpdate(n, cellData)"/>
        </div>
        </div>
      </div>
    </div>



  </div>
</template>

<style lang="scss">

@use "./../../variables.scss";

.grid-editor {
  display: flex;
  flex-direction: row;
  gap: 20px;
  height: 100%;

  .form {
    background-color: var(--bg-highlight);
    height: 100%;
    border-top-left-radius: 20px;
    border-bottom-left-radius: 20px;

    label {
      display: block;
      margin-top: 5px;
    }
  }

  .button-group {
    justify-content: center;
  }
}


.grid-preview {
  display: flex;
  flex-direction: column;
  align-items: center;

  &.theme-round .grid-cell {
    border-radius: 50%;
  }
}

.grid-cell {
  font-size: 10px;
  line-height: normal;
  word-break: break-word;
}

.grid-container {
  display: grid;
  gap: 5px;
  padding: 10px;

  &.size-3 {
    grid-template-columns: repeat(3, 1fr);
    grid-template-rows: repeat(3, 1fr);
  }

  &.size-4 {
    grid-template-columns: repeat(4, 1fr);
    grid-template-rows: repeat(4, 1fr);
  }

  &.size-5 {
    grid-template-columns: repeat(5, 1fr);
    grid-template-rows: repeat(5, 1fr);
  }

  &.color-black-and-white .grid-cell {
    background-color: #fff;
    color: #000;
  }

  &.color-green .grid-cell {
    background-color: #aaffaa;
    color: #000;
  }

  &.color-blue .grid-cell {
    background-color: #aaaaff;
    color: #000;
  }
}

@media (max-width: variables.$breakPointSmall) {
  .grid-editor {
    flex-direction: column;

    .form {
      border-top-right-radius: 20px;
      border-bottom-left-radius: 0px;
    }
  }
}
</style>