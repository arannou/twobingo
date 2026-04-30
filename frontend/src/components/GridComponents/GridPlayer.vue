<script setup>
import { useRouter, useRoute } from 'vue-router'
import { computed, onBeforeMount, ref, onBeforeUnmount } from 'vue'
import { useI18n } from 'vue-i18n'
import { useUserStore } from '../../stores/user'
import { useGridStore } from '../../stores/grid'
import CellComponent from './CellComponent.vue'

import { THEMES, COLORS } from '../../variables'

const router = useRouter()
const route = useRoute()
const userStore = useUserStore()
const gridStore = useGridStore()
const { t } = useI18n()
const mode = ref('create')


const playedGrid = ref({
      name: '',
      description: '',
      size: 3,
      theme: THEMES[0],
      gridColor: COLORS[0],
  })

onBeforeMount(() => {
    console.log((route.params.gridId))
  if (gridStore.selectedGrid?.id) {

      loadGridData(gridStore.selectedGrid)
    } else {
      if (route.params.gridId) {
        const gridId = parseInt(route.params.gridId)
        gridStore.fetchGridById(gridId).then((gridToPlay) => {
          if (gridToPlay) {

            loadGridData(gridToPlay)
          } else {
            console.warn('No grid found with id from route query:', route.query.gridId)
          }
        })

      }

    }
})

const loadGridData = (data) => {
      playedGrid.value = { ...data };
      // find color object from COLORS array that matches the gridColor name
      const colorObj = COLORS.find(color => color.name === data.color)
      if (colorObj) {
        playedGrid.value.gridColor = colorObj;
      } else {
        playedGrid.value.gridColor = COLORS[0];
      }
  }

const cellsByPosition = computed(() => {
  if (!playedGrid.value.cells) return {};
  let byPos = {}
  for (let cell of playedGrid.value.cells) {
    byPos[cell.position] = cell.content;
  }
  return byPos;
})

const onCellCheck = (cellIndex, isChecked) => {
    if (!playedGrid.value) return;
    if (!playedGrid.value.cells) return;
    
    const updatedCells = playedGrid.value.cells.map(cell => {
      if (cell.position === cellIndex) {
        cell.checked = isChecked;
      }
      return cell;
    });

    playedGrid.value.cells = updatedCells;

}

</script>

<template>
  <div class="grid-player">
    <div class="player">
      <h3>{{ playedGrid.name }}</h3>
      <p>{{ playedGrid.description }}</p>
      <div class="grid-preview" :class="'theme-' +playedGrid.theme" >
        <div class="grid-container" :class="'size-' +playedGrid.size + ' color-' + playedGrid.gridColor.name.toLowerCase().replace(/\s/g, '-')">
        <div v-for="n in parseInt(playedGrid.size)*parseInt(playedGrid.size)" :key="'row-' + n" class="grid-cell">
            <CellComponent :content="cellsByPosition[n] ?? ''" :index="n" :editable="false" @check="(isChecked)  => onCellCheck(n, isChecked)"/>
        </div>
        </div>
      </div>
    </div>
    <div class="participants">
      <h4>{{ t('gridPlayer.participants') }}</h4>
      <!-- <ul>
        <li v-for="participant in playedGrid.participants" :key="participant.id">{{ participant.username }}</li>
      </ul>
       <div v-if="playedGrid.participants.length === 0">
        <p>{{ t('gridPlayer.noParticipants') }}</p>
      </div> -->
    </div>
  </div>
</template>

<style lang="scss">

@use "./../../variables.scss";

.grid-player {
  display: flex;
  flex-direction: row;
  gap: 20px;
  height: 100%;
  justify-content: space-between;

  .participants {
    background-color: var(--bg-highlight);
    height: 100%;
    border-top-left-radius: 20px;
    border-bottom-left-radius: 20px;
  }

    .player {
        flex-grow: 2;
    }

}


@media (max-width: variables.$breakPointSmall) {
  .grid-player {
    flex-direction: column;

    .participants {
      border-top-right-radius: 20px;
      border-bottom-left-radius: 0px;
    }
  }
}
</style>