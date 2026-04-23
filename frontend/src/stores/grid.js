import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { twobingoApi } from '../services/api';

export const useGridStore = defineStore('grid', () => {
    // State
    const grids = ref([]);
    const selectedGrid = ref(null);
    const loading = ref(false);
    const error = ref(null);

    const updateCellContent = (cellIndex, content) => {

    };

    // const categories = computed(() => {
    //     const cats = new Set(items.value.map((item) => item.category).filter(Boolean));
    //     return Array.from(cats).sort();
    // });

    // const suppliers = computed(() => {
    //     const sups = new Set(items.value.map((item) => item.supplier).filter(Boolean));
    //     return Array.from(sups).sort();
    // });

    // Actions
    const fetchForUser = async () => {
        loading.value = true;
        error.value = null;

        try {
            const gridsMap = await twobingoApi.getGridsForUser();
            grids.value = gridsMap.grids || [];
        } catch (err) {
            error.value = err.message;
            console.error('Error fetching grids:', err);
        } finally {
            loading.value = false;
        }
    };


    const fetchGridById = async (id) => {
        loading.value = true;
        error.value = null;
        try {
            selectedGrid.value = await twobingoApi.getGridById(id);
            return selectedGrid.value;
        } catch (err) {
            error.value = err.message;
            console.error('Error fetching grid:', err);
        } finally {
            loading.value = false;
        }
    };


    const createGrid = async (gridData) => {
        loading.value = true;
        error.value = null;
        try {
            const toCreate = { ...gridData, color: gridData.gridColor.name };
            delete toCreate.gridColor;
            const newGrid = await twobingoApi.createGrid(toCreate);
            grids.value.push(newGrid);
            return newGrid;
        } catch (err) {
            error.value = err.message;
            console.error('Error creating grid:', err);
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const updateGrid = async (id, gridData) => {
        loading.value = true;
        error.value = null;
        try {
            const toUpdate = { ...gridData, color: gridData.gridColor.name };
            delete toUpdate.gridColor;
            const updatedGrid = await twobingoApi.updateGrid(id, toUpdate);
            const index = grids.value.findIndex((grid) => grid.id === id);
            if (index !== -1) {
                grids.value[index] = updatedGrid;
            }
            if (selectedGrid.value && selectedGrid.value.id === id) {
                selectedGrid.value = updatedGrid;
            }
            return updatedGrid;
        } catch (err) {
            error.value = err.message;
            console.error('Error updating grid:', err);
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const deleteGrid = async (id) => {
        loading.value = true;
        error.value = null;
        try {
            await twobingoApi.deleteGrid(id);
            grids.value = grids.value.filter((grid) => grid.id !== id);
            if (selectedGrid.value && selectedGrid.value.id === id) {
                selectedGrid.value = null;
            }
            return true;
        } catch (err) {
            error.value = err.message;
            console.error('Error deleting grid:', err);
            throw err;
        } finally {
            loading.value = false;
        }
    };



    return {
        // State
        grids,
        selectedGrid,
        loading,
        error,

        // Computed

        // Actions
        updateCellContent,

        fetchForUser,
        fetchGridById,

        createGrid,
        updateGrid,
        deleteGrid,

    };
});
