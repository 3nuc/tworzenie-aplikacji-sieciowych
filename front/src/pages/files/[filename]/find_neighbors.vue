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
    columns: form.value.columns.join(','),
    pointCoordinates: Object.values(form.value.pointCoordinates).join(',')
  }))

const { state, isReady } = useFileInfo({fileName: props.filename, immediate: true});

const columns = computed(() => state?.value?.columnNames ?? [])

const { state: neighborData, execute: executeNeighbors } = useAsyncState(async () => (await getNeighbors(formFormatted.value).data), null, {immediate: false})

const sendNeighbors = () => {
  executeNeighbors();
}

const chartOptions = {
  chart: {id: 'vuechart-example'},
  xaxis: { categories: [1991, 1992, 1993, 1994, 1995, 1996, 1997, 1998] },
}
const series = [ { name: "series-1", data: [30, 40, 35, 50, 49, 60, 70, 91], }, ];

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
<apex-chart :options="chartOptions" :series="series"></apex-chart>
</el-form>
</template>
