<script setup>
import { computed, defineProps, onMounted, ref } from 'vue'
import * as Plot from '@observablehq/plot'
import { useAsyncState } from '@vueuse/core'
import rawData from './correlation-data.json'
import { getCorrelationTable } from '~/services'

const props = defineProps(['filename'])
const { state, isReady, execute } = useAsyncState(async() => (await getCorrelationTable({ fileName: props.filename })).data, null, { immediate: false })

const chart = ref(null)
const chartData = computed(() => state.value.items
  .map(item => ({ ...item, correlationValue: parseFloat(item.correlationValue, 10) }),
  ))

onMounted(async() => {
  await execute()
  const plot = Plot.plot({
    width: 640,
    height: 640,
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
      Plot.cell(chartData.value, {
        x: 'row',
        y: 'col',
        fill: 'correlationValue',
      }),
      Plot.text(chartData.value, {
        x: 'row',
        y: 'col',
        text: datum => datum.correlationValue.toFixed(2),
        fontWeight: 'bold',
      }),
    ],
    style: {
      background: 'white',
      color: 'black',
    },
  })
  chart.value.appendChild(plot)
})
</script>
<template>
  <div ref="chart" />
</template>
