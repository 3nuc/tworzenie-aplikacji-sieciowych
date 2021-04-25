<script setup>
import {defineProps, ref, computed} from 'vue'
import { useFileInfo } from '~/pages/files/logics'
const props = defineProps(['filename'])
const form = ref({
  decissionColumn: null,
  columns: [],
  findType: 'Euklidean',
  neighbours: 2,
})
const formFormatted = computed(() => ({
    ...form.value,
    columns: form.value.columns.join(','),
  }))

const { state, isReady } = useFileInfo({fileName: props.filename, immediate: true})

const columns = computed(() => state?.value?.columnNames ?? [])
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
      <el-col :span="11">
        <el-form-item label="Kolumny">
          <el-select multiple v-model="form.columns">
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
      <el-col v-if="form.columns.length" :span="11">
        <el-card>
          <el-form label-position="left">
            <el-form-item 
              v-for="(columnName,index) in form.columns"
              :label="`${columnName}`" required v-if="form.columns.length">
               <el-input-number />
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
    <el-button type="primary">Wyślij</el-button>
  </el-form-item>
</el-form>
</template>
