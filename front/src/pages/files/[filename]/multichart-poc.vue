<script setup>
import { onMounted, ref } from 'vue'
import * as Plot from '@observablehq/plot'
import rawData from './correlation-data.json'

const chartData = rawData.items
  .map(
    item => ({ ...item, correlationValue: parseFloat(item.correlationValue, 10) }),
  )
const chart = ref(null)
const plot = Plot.plot({
  height: 640,
  width: 640,
  padding: 0.05,
  grid: true,
  x: {
    axis: 'top',
    label: '',
  },
  y: {
    label: '',
    tickRotate: -90,
  },
  color: {
    scheme: 'PiYG',
  },
  marks: [
    Plot.cell(chartData, {
      x: 'row',
      y: 'col',
      fill: 'correlationValue',
    }),
    Plot.cell(chartData, {
      x: 'row',
      y: 'col',
      fill: 'correlationValue',
    }),
    Plot.text(chartData, {
      x: 'row',
      y: 'col',
      text: datum => datum.correlationValue.toFixed(2),
    }),
  ],
  style: {
    background: 'white',
    color: 'white',
  },
})
onMounted(() => {
  chart.value.appendChild(plot)
})
</script>
<template>
  <div ref="chart" />
</template>
