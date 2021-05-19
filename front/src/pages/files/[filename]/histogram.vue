<script setup>
import { defineProps, ref, onMounted } from 'vue'
import * as Plot from '@observablehq/plot'
import rawData from './histogram-data.json'
import { chartify } from './histogram-operations'
const histogram = ref(null)
const props = defineProps({
  filename: { type: String, required: true },
})
const { actualChartData, domain } = chartify(rawData)
const plot = Plot.plot({
  facet: {
    data: actualChartData,
    x: 'section',
  },
  marks: [
    Plot.barY(actualChartData, { x: 'decission', y: 'count', fill: 'decission' }),
    Plot.ruleY([0]),
  ],
  color: {
    scheme: 'spectral',
    domain,
  },
  x: {
    axis: null,
    domain,
  },
})
onMounted(() => {
  histogram.value.appendChild(plot)
})
</script>

<template>
  <div ref="histogram" />
  {{ actualChartData }}
</template>
