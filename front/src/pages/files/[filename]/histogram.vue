<script setup>
import { defineProps, ref, onMounted } from 'vue'
import * as Plot from '@observablehq/plot'
import rawData from './histogram-data.json'
import { chartify } from './histogram-operations'
const histogram = ref(null)
const props = defineProps({
  filename: { type: String, required: true },
})
const chartableData = chartify(rawData)
onMounted(() => {
  const plot = Plot.plot({
    facet: {
      data: chartableData,
      x: 'section',
    },
    marks: [
      Plot.barY(chartableData, { x: 'decission', y: 'count', fill: 'decission' }),
      Plot.ruleY([0]),
    ],
    color: {
      scheme: 'spectral',
      domain: ['setosa', 'versicolor', 'virginica'],
    },
    x: {
      axis: null,
      domain: ['setosa', 'versicolor', 'virginica'],
    },
  })
  histogram.value.appendChild(plot)
})
</script>

<template>
  <div ref="histogram" />
  {{chartableData}}
</template>
