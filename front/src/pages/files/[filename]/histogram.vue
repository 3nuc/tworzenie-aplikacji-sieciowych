<script setup>
import { defineProps, ref, onMounted } from 'vue'
import * as Plot from '@observablehq/plot'
import rawData from './histogram-data.json'
import { chartify } from './histogram-operations'
const histogram = ref(null)
const props = defineProps({
  filename: { type: String, required: true },
})
const { actualChartData, domain } = chartify(rawData)

const plot = Plot.plot({
  facet: {
    data: actualChartData,
    x: 'section',
  },
  marks: [
    Plot.barY(actualChartData, { x: 'decission', y: 'count', fill: 'decission' }),
    Plot.ruleY([0]),
  ],
  color: {
    scheme: 'spectral',
    domain,
  },
  x: {
    axis: null,
    domain,
  },
})
onMounted(() => {
  histogram.value.appendChild(plot)
})
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
            <el-select v-model="form.columns" multiple :disabled="form.decissionColumn === null">
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
      </el-row>
    </el-form-item>

    <el-form-item label="Metryka" required>
      <el-radio-group v-model="form.findType">
        <el-radio label="Euklidean">
          Euklidesowa
        </el-radio>
        <el-radio label="Manhattan">
          Manhattan
        </el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" :disabled="isSendDisabled" @click="executeKmeans">
        Wyślij
      </el-button>
    </el-form-item>
    <!-- table here -->
  </el-form>

  <div ref="histogram" />
</template>
