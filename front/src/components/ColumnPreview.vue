<script setup lang="ts">
import { defineProps, defineEmit, computed } from 'vue'
import type {PropType} from 'vue';

const props = defineProps({
  items: {type: Array as PropType<string[]>, required: true},
  modelValue: {type: Boolean, default: false},
  maxValue: {type: [String, Number]},
  minValue: {type: [String, Number]},
})
const emit = defineEmit(['update:modelValue'])
const itemsAsObjects = computed(() => props.items.map(value => ({value})))
</script>

<template>
<el-dialog :modelValue="modelValue" @update:modelValue="emit('update:modelValue', $event)">
  <template #title>
    <div class="text-6xl text-white font-bold bg-white bg-gradient-to-r from-purple-400 via-pink-500 to-red-500 p-4 mb-4">
      <slot name="title"/>
    </div>
  </template>

  <div class="grid grid-rows-2 grid-cols-2">
    <div>Min: <span class="text-2xl font-bold">{{minValue}}</span></div>
    <div>Max: <span class="text-2xl font-bold">{{maxValue}}</span></div>
  </div>
  <el-table :data="itemsAsObjects" style="width: 100%;">
    <el-table-column prop="value" label="Wartości" width="150"/>
  </el-table>
</el-dialog>
</template>
