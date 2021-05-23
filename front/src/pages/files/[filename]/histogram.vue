<script setup>
import { defineProps, ref, onMounted, reactive, computed } from 'vue'
import * as Plot from '@observablehq/plot'
import { useAsyncState } from '@vueuse/core'
import rawData from './histogram-data.json'
import { chartify } from './histogram-operations'
import { getFileInfo, getHistogram } from '~/services'
const histogram = ref(null)
const props = defineProps({
  filename: { type: String, required: true },
})

const form = reactive({
  columnToCheck: null,
  decissionColumn: null,
  amountOfSections: 2,
})

const { state: fileInfo, isReady: isFileReady } = useAsyncState(async() => (await getFileInfo({ fileName: props.filename })).data)
const { state, isReady, execute } = useAsyncState(async() => (await getHistogram({ fileName: props.filename, ...form })).data, null, { immediate: false })

const executeWrapper = async() => {
  await execute()
  const chartified = chartify(state.value)
  const plot = Plot.plot({
    grid: true,
    facet: {
      data: chartified.actualChartData,
      x: 'section',
    },
    marks: [
      Plot.barY(chartified.actualChartData, { x: 'decission', y: 'count', fill: 'decission', title: 'decission' }),
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
      domain: chartified.domain,
    },
    x: {
      axis: null,
      domain: chartified.domain,
    },
  })
  while (histogram.value.firstChild)
    histogram.value.removeChild(histogram.value.firstChild)

  histogram.value.appendChild(plot)
}
</script>

<template>
  <el-form v-loading="!isReady && !isFileReady" label-position="top">
    <el-form-item label="Kolumna decyzyjna" required>
      <el-select v-model="form.decissionColumn">
        <el-option
          v-for="column in fileInfo?.columnNames ?? []"
          :key="column"
          :value="column"
          :label="column"
        />
      </el-select>
    </el-form-item>
    <el-form-item>
      <el-row>
        <el-col :span="8" />
        <el-col :span="8">
          <el-form-item label="Kolumna do sprawdzenia" required>
            <el-select v-model="form.columnToCheck" :disabled="form.decissionColumn === null">
              <el-option
                v-for="column in fileInfo?.columnNames ?? []"
                :key="column"
                :value="column"
                :label="column"
                :disabled="column === form.decissionColumn"
              />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form-item>

    <el-form-item label="Ilość cięć" required>
      <el-input-number v-model="form.amountOfSections" :min="1" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :disabled="form.decissionColumn === null || form.columnToCheck === null" @click="executeWrapper">
        Wyślij
      </el-button>
    </el-form-item>
    <!-- table here -->
  </el-form>

  <div ref="histogram" />
</template>
