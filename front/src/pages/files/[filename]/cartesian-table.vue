<script setup>
import { useAsyncState } from '@vueuse/core'
import * as Plot from '@observablehq/plot'
import { computed, defineProps, onMounted, reactive, ref } from 'vue'
import { getCartesianTable, getFileInfo } from '~/services'

const props = defineProps({
  filename: { type: String, required: true },
})
const chart = ref(null)
const col_permutation = reactive({
  x: null,
  y: null,
})

const form = reactive({
  decissionClass: null,
})

const { state, execute } = useAsyncState(
  async() => (await getCartesianTable({ fileName: props.filename, decissionClass: form.decissionClass })).data, null, { immediate: false },
)

const { state: fileInfo, isReady: isFileReady } = useAsyncState(async() => (await getFileInfo({ fileName: props.filename })).data)

const columns = computed(() => fileInfo.value?.columnNames ?? [])

const executeWrapper = async() => {
  await execute()
  redrawChart()
}

const redrawChart = (x, y) => {
  const dataForGraph = state.value?.graphs.find(graph => graph[0])
  const plot = Plot.plot({
    grid: true,
    facet: {
      x: 'row',
    },
    marks: [
      Plot.dot(chartified.value, { x: 'row', y: 'col', fill: 'val' }),
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
          v-for="column in columns"
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
  <div class="flex flex-col gap-10">
    <el-radio-group class="mr-5" v-model="col_permutation.x" @change="redrawChart">
      <el-radio-button v-for="column in columns" :label="column">{{column}}</el-radio-button>
    </el-radio-group>

    <el-radio-group v-model="col_permutation.y" @change="redrawChart">
      <el-radio-button v-for="column in columns" :label="column">{{column}}</el-radio-button>
    </el-radio-group>
  </div>
</template>
