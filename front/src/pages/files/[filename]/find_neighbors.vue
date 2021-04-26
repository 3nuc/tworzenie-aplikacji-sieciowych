<script setup>
import {defineProps, ref, computed} from 'vue'
import {useAsyncState} from '@vueuse/core'
import { useFileInfo } from '~/pages/files/logics'
import { getNeighbors } from '~/services'
import ApexChart from 'vue3-apexcharts'

const props = defineProps(['filename'])

const form = ref({
  decissionColumn: null,
  columns: [],
  findType: 'Euklidean',
  neighbours: 2,
  pointCoordinates: {}
})

const formFormatted = computed(() => ({
    ...form.value,
    fileName: props.filename,
    columns: form.value.columns.join(',').replaceAll('"', ''),
    pointCoordinates: Object.values(form.value.pointCoordinates).join(','),
    decissionColumn: form.value.decissionColumn?.replaceAll('"', '')
  }))

const { state, isReady } = useFileInfo({fileName: props.filename, immediate: true});

const columns = computed(() => state?.value?.columnNames ?? [])

const { state: neighborData, execute: executeNeighbors } = useAsyncState(async () => {const req = await getNeighbors(formFormatted.value); console.log(req); return req.data}, null, {immediate: false})

const sendNeighbors = async() => {
  await executeNeighbors();
  console.log(neighborData.value)
  correctSeries.value = recalculateSeries();
}

const recalculateSeries = () => {
  if(neighborData.value === null) return null;
  const seriesNames = [...new Set(neighborData.value.map(item => item.at(-1)))];
  const seriesObjects = seriesNames.map(name => ({name, data: []}));

  console.log(seriesNames, seriesObjects)

  neighborData.value.forEach((item) => {
    const coords = item.slice(0, item.length-1)
    const seriesName = item.at(-1)
    const seriesBelongedTo = seriesObjects.find(({name}) => name === seriesName);
    seriesBelongedTo.data.push(coords)
  })
  seriesObjects.push({name: 'Podany przez użytkownika', data: [formFormatted.value.pointCoordinates.split(',')]})
  return seriesObjects;
}

const options = {
  chart: { type: 'scatter', height: 300, zoom: {enabled: true, type: 'xy'}}, 
  xaxis: {
    tickAmount: 10,
  },
  yaxis: {
    tickAmount: 7
  } 
}

const correctSeries = ref([{
            name: "SAMPLE A",
            data: [
            [16.4, 5.4], [21.7, 2], [25.4, 3], [19, 2], [10.9, 1], [13.6, 3.2], [10.9, 7.4], [10.9, 0], [10.9, 8.2], [16.4, 0], [16.4, 1.8], [13.6, 0.3], [13.6, 0], [29.9, 0], [27.1, 2.3], [16.4, 0], [13.6, 3.7], [10.9, 5.2], [16.4, 6.5], [10.9, 0], [24.5, 7.1], [10.9, 0], [8.1, 4.7], [19, 0], [21.7, 1.8], [27.1, 0], [24.5, 0], [27.1, 0], [29.9, 1.5], [27.1, 0.8], [22.1, 2]]
          },{
            name: "SAMPLE B",
            data: [
            [36.4, 13.4], [1.7, 11], [5.4, 8], [9, 17], [1.9, 4], [3.6, 12.2], [1.9, 14.4], [1.9, 9], [1.9, 13.2], [1.4, 7], [6.4, 8.8], [3.6, 4.3], [1.6, 10], [9.9, 2], [7.1, 15], [1.4, 0], [3.6, 13.7], [1.9, 15.2], [6.4, 16.5], [0.9, 10], [4.5, 17.1], [10.9, 10], [0.1, 14.7], [9, 10], [12.7, 11.8], [2.1, 10], [2.5, 10], [27.1, 10], [2.9, 11.5], [7.1, 10.8], [2.1, 12]]
          },{
            name: "SAMPLE C",
            data: [
            [21.7, 3], [23.6, 3.5], [24.6, 3], [29.9, 3], [21.7, 20], [23, 2], [10.9, 3], [28, 4], [27.1, 0.3], [16.4, 4], [13.6, 0], [19, 5], [22.4, 3], [24.5, 3], [32.6, 3], [27.1, 4], [29.6, 6], [31.6, 8], [21.6, 5], [20.9, 4], [22.4, 0], [32.6, 10.3], [29.7, 20.8], [24.5, 0.8], [21.4, 0], [21.7, 6.9], [28.6, 7.7], [15.4, 0], [18.1, 0], [33.4, 0], [16.4, 0]]
          }]);
</script>

<template>
<el-form label-position="top" v-loading="!isReady">
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
      <el-col :span="8"/>
      <el-col :span="8">
        <el-form-item label="Kolumny" required>
          <el-select multiple v-model="form.columns" :disabled="form.decissionColumn === null" @update:modelValue="form.pointCoordinates = {}">
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
              :key="columnName"
              v-for="columnName in form.columns"
              :label="`Koordynat - ${columnName}`" required >
              <el-input-number 
               :precision="2" 
               :modelValue="form.pointCoordinates[columnName] ?? 0" 
               @update:modelValue="newValue => { form.pointCoordinates[columnName] = newValue }"
              />
            </el-form-item>  
          </el-form>
        </el-card>
      </el-col>
    </el-row>
  </el-form-item>

  <el-form-item label="Ilość sąsiadów" required>
    <el-input-number v-model="form.neighbours" :min="1" :max="state?.rowCount"/>
  </el-form-item> 

  <el-form-item label="Metryka" required>
    <el-radio-group v-model="form.findType">
      <el-radio label="Euklidean">Euklidesowa</el-radio>
      <el-radio label="Chebyshew">Czebyszewa</el-radio>
      <el-radio label="Mahalanobis">Mahalanobisa</el-radio>
      <el-radio label="Manhattan">Manhattan</el-radio>
      />
    </el-radio-group>
  </el-form-item> 
  <el-form-item>
    <el-button type="primary" @click="sendNeighbors">Wyślij</el-button>
  </el-form-item>
  <apex-chart type="scatter" width="500" :options="options" :series="correctSeries"/>
</el-form>
</template>
