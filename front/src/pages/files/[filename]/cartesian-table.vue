<script setup>
import { useAsyncState } from '@vueuse/core'
import * as Plot from '@observablehq/plot'
import { computed, defineProps, onMounted, reactive, ref } from 'vue'
import { getCartesianTable, getFileInfo } from '~/services'

const props = defineProps({
  filename: { type: String, required: true },
})
const chart = ref(null)

const form = reactive({
  decissionClass: null,
})

const { state, execute } = useAsyncState(
  async() => (await getCartesianTable({ fileName: props.filename, decissionClass: form.decissionClass })).data, null, { immediate: false },
)

const { state: fileInfo, isReady: isFileReady } = useAsyncState(async() => (await getFileInfo({ fileName: props.filename })).data)

const chartified = computed(() => {
  console.log(state.value?.graphs.flatMap(graph => graph.items))
  return state.value?.graphs.flatMap(graph => graph.items) ?? []
})

const executeWrapper = async() => {
  await execute()
  const plot = Plot.plot({
    grid: true,
    facet: {
      x: 'row',
    },
    marks: [
      Plot.dot(chartified.value, { x: 'row', y: 'col', fill: 'val' }),
      /*
      Plot.text(chartified.actualChartData, {
        x: 'decission',
        y: 'count',
        text: 'decission',
        rotate: 90,
        textAnchor: 'start',
        fontSize: 3,
        dx: "1em"
      }),
      */
      // Plot.ruleY([0]),
    ],
    color: {
      scheme: 'spectral',
    },
  })
  while (chart.value.firstChild)
    chart.value.removeChild(chart.value.firstChild)

  chart.value.appendChild(plot)
}
</script>
<template>
  <el-form label-position="top">
    <el-form-item label="Kolumna decyzyjna" required>
      <el-select v-model="form.decissionClass">
        <el-option
          v-for="column in fileInfo?.columnNames ?? []"
          :key="column"
          :value="column"
          :label="column"
        />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :disabled="form.decissionClass === null" @click="executeWrapper">
        Wyślij
      </el-button>
    </el-form-item>
    <div ref="chart" />
  </el-form>
  {{ chartified }}
</template>
