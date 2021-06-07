<script setup>
import { defineProps, ref, computed } from 'vue'
import * as Plot from '@observablehq/plot'
import { useAsyncState } from '@vueuse/core'
import { useFileInfo } from '~/pages/files/logics'
import { getNeighbors } from '~/services'

const props = defineProps(['filename'])
const form = ref({
  decissionColumn: null,
  columns: [],
  findType: 'Euklidean',
  neighbours: 2,
  pointCoordinates: {},
})

const chart = ref(null)

const formFormatted = computed(() => ({
  ...form.value,
  fileName: props.filename,
  columns: form.value.columns.join(',').replaceAll('"', ''),
  pointCoordinates: Object.values(form.value.pointCoordinates).join(','),
  decissionColumn: form.value.decissionColumn?.replaceAll('"', ''),
}))

const { state, isReady } = useFileInfo({ fileName: props.filename, immediate: true })
const columns = computed(() => state?.value?.columnNames ?? [])

const { state: neighborData, execute: executeNeighbors }
  = useAsyncState(
    async() => (await getNeighbors(formFormatted.value)).data,
    null,
    { immediate: false },
  )

const chartified = computed(() => {
  const arrayToPointConverter = (item) => {
    // classes can be numbers but plot lib goes stupid
    // if you mix strings and numbers, so lets do strings
    const assignedClass = String(item[item.length - 1])
    const pointValues = item.slice(0, item.length)
    // cant display more than 2 dimensions due to chart lib limitation
    return { assignedClass, x: pointValues[0] ?? 0, y: pointValues[1] ?? 0, isUserInput: assignedClass === 'userinput' }
  }

  const userPoint = [...Object.values(form.value.pointCoordinates), 'userinput']

    console.log(neighborData.value ?? [])
  return [
    ...(neighborData.value.dataset ?? []),
    userPoint,
  ].map(arrayToPointConverter)
})

const sendNeighbors = async() => {
  await executeNeighbors()
  const plot = Plot.plot({
    grid: true,
    marks: [
      Plot.dot(chartified.value,
        {
          x: 'x',
          y: 'y',
          fill: d => d.isUserInput,
          r: d => d.isUserInput ? 10 : 1,
          title: d => `Class: ${d.assignedClass} - x: ${d.x}, y: ${d.y}`,
        }),
      Plot.text(chartified.value, { x: 'x', y: 'y', text: d => d.isUserInput ? 'USER' : '', dy: 5, fontWeight: 'bold' }),
    ],
    color: {
      scheme: 'accent',
    },
  })
  while (chart.value.firstChild)
    chart.value.removeChild(chart.value.firstChild)

  chart.value.appendChild(plot)
}
</script>

<template>
  <el-form v-loading="!isReady" label-position="top">
    <el-form-item label="Kolumna decyzyjna" required>
      <el-select v-model="form.decissionColumn">
        <el-option
          v-for="column in columns"
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
          <el-form-item label="Kolumny" required>
            <el-select v-model="form.columns" multiple :disabled="form.decissionColumn === null" @update:modelValue="form.pointCoordinates = {}">
              <el-option
                v-for="column in columns"
                :key="column"
                :value="column"
                :label="column"
                :disabled="column === form.decissionColumn"
              />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-card v-if="form.columns.length">
            <el-form label-position="left">
              <el-form-item
                v-for="columnName in form.columns"
                :key="columnName"
                :label="`Koordynat - ${columnName}`"
                required
              >
                <el-input-number
                  :precision="2"
                  :model-value="form.pointCoordinates[columnName] ?? 0"
                  @update:modelValue="newValue => { form.pointCoordinates[columnName] = newValue }"
                />
              </el-form-item>
            </el-form>
          </el-card>
        </el-col>
      </el-row>
    </el-form-item>

    <el-form-item label="Ilość sąsiadów" required>
      <el-input-number v-model="form.neighbours" :min="1" :max="state?.rowCount" />
    </el-form-item>

    <el-form-item label="Metryka" required>
      <el-radio-group v-model="form.findType">
        <el-radio label="Euklidean">
          Euklidesowa
        </el-radio>
        <el-radio label="Chebyshew">
          Czebyszewa
        </el-radio>
        <el-radio label="Mahalanobis">
          Mahalanobisa
        </el-radio>
        <el-radio label="Manhattan">
          Manhattan
        </el-radio>
        />
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="sendNeighbors">
        Wyślij
      </el-button>
    </el-form-item>
    <div ref="chart" />
  </el-form>
</template>
