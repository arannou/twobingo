import { defineStore } from 'pinia';
import { ref, computed } from 'vue';
import { twobingoApi } from '../services/api';

export const useGridStore = defineStore('grid', () => {
    // State
    const grids = ref([]);
    const selectedGrid = ref(null);
    const loading = ref(false);
    const error = ref(null);



    // const categories = computed(() => {
    //     const cats = new Set(items.value.map((item) => item.category).filter(Boolean));
    //     return Array.from(cats).sort();
    // });

    // const suppliers = computed(() => {
    //     const sups = new Set(items.value.map((item) => item.supplier).filter(Boolean));
    //     return Array.from(sups).sort();
    // });

    // Actions
    const fetchAll = async () => {
        loading.value = true;
        error.value = null;
        try {
            items.value = await twobingoApi.getAllItems();
        } catch (err) {
            error.value = err.message;
            console.error('Error fetching items:', err);
        } finally {
            loading.value = false;
        }
    };


    const fetchItemById = async (id) => {
        loading.value = true;
        error.value = null;
        try {
            selectedItem.value = await inventoryApi.getItemById(id);
        } catch (err) {
            error.value = err.message;
            console.error('Error fetching item:', err);
        } finally {
            loading.value = false;
        }
    };


    const createItem = async (itemData) => {
        loading.value = true;
        error.value = null;
        try {
            const newItem = await inventoryApi.createItem(itemData);
            items.value.push(newItem);
            return newItem;
        } catch (err) {
            error.value = err.message;
            console.error('Error creating item:', err);
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const updateItem = async (id, itemData) => {
        loading.value = true;
        error.value = null;
        try {
            const updatedItem = await inventoryApi.updateItem(id, itemData);
            const index = items.value.findIndex((item) => item.id === id);
            if (index !== -1) {
                items.value[index] = updatedItem;
            }
            if (selectedItem.value && selectedItem.value.id === id) {
                selectedItem.value = updatedItem;
            }
            return updatedItem;
        } catch (err) {
            error.value = err.message;
            console.error('Error updating item:', err);
            throw err;
        } finally {
            loading.value = false;
        }
    };

    const deleteItem = async (id) => {
        loading.value = true;
        error.value = null;
        try {
            await inventoryApi.deleteItem(id);
            items.value = items.value.filter((item) => item.id !== id);
            if (selectedItem.value && selectedItem.value.id === id) {
                selectedItem.value = null;
            }
            return true;
        } catch (err) {
            error.value = err.message;
            console.error('Error deleting item:', err);
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
        fetchAll,
        fetchItemById,

        createItem,
        updateItem,
        deleteItem,

    };
});
