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
}

const redrawChart = (x, y) => {
  if ([x, y].includes(null)) return

  const dataForGraph = state.value?.graphs.find((graph) => {
    const { rowName, colName } = graph.properties
    console.log(x, y, rowName, colName, rowName === x && colName === y)
    return rowName === x && colName === y
  })?.items ?? []
  const plot = Plot.plot({
    grid: true,
    width: 1080,
    height: 1080,
    marks: [
      Plot.dot(dataForGraph, {
        x: 'row',
        y: 'col',
        fill: 'val',
        r: 10,
        title: d => `${d.val}, x - ${d.row}, y - ${d.col}`,
      }),

    ],
    color: {
      scheme: 'tableau10',
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
    <el-radio-group v-model="col_permutation.x" :disabled="state===null" class="mr-5" @change="(col_permutation.x, col_permutation.y)">
      <el-radio-button v-for="column in columns" :label="column">
        {{ column }}
      </el-radio-button>
    </el-radio-group>

    <el-radio-group v-model="col_permutation.y" :disabled="state===null" @change="redrawChart(col_permutation.x, col_permutation.y)">
      <el-radio-button v-for="column in columns" :label="column">
        {{ column }}
      </el-radio-button>
    </el-radio-group>
  </div>
</template>
