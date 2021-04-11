<template>
  <div>
    <div v-if="isFetching">
      Ładowanie...
    </div>
    <template v-else>
    <div class="grid grid-cols-1">
      <div class="text-6xl font-bold bg-white bg-gradient-to-r from-purple-400 via-pink-500 to-red-500 p-2 mb-4">{{filename}}</div>
      <div>
        <div class="grid grid-rows-2 grid-cols-2">
          <div>Liczba wierszy: <span class="text-2xl font-bold">{{file.columnCount}}</span></div>
          <div>Liczba rzędów: <span class="text-2xl font-bold">{{file.rowCount}}</span></div>
        </div>
      </div>
      <el-tabs v-model="tab" class="text-white" tab-position="left">
        <el-tab-pane label="Dyskretyzacja" name="dyskretyzacja">
          <el-form>
            <el-form-item label="Kolumna">
              <el-select v-model="discretizationForm.columnName">
                <el-option v-for="option in columnOptions" :key="option.value" :value="option.value" :label="option.label" />
              </el-select>
            </el-form-item>
            <el-form-item label="Ilość sekcji">
              <el-input-number v-model="discretizationForm.amountOfSections"/>
            </el-form-item>
            <el-form-item>
              <el-button @click="sendDiscretization">Wyślij</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="Normalizacja" name="normalizacja">
          <el-form>
            <el-form-item label="Kolumna">
              <el-select v-model="normalizationForm.columnName">
                <el-option v-for="option in columnOptions" :key="option.value" :value="option.value" :label="option.label" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="sendNormalization">Wyślij</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        <el-tab-pane label="Digitalizacja" name="digitalizacja">
          <el-form>
            <el-form-item label="Kolumna">
              <el-select v-model="digitalizationForm.columnName">
                <el-option v-for="option in columnOptions" :key="option.value" :value="option.value" :label="option.label" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button @click="sendDigitalization">Wyślij</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        </el-tabs>
        </div>
    </template>
    <el-table :data="csvData" v-if="csvData !== null">
      <el-table-column prop="0"/>
    </el-table>
  </div>
</template>
<script setup lang="ts">
import { defineProps, ref, computed, reactive, onMounted } from 'vue'
import { useApi, BASE_URL } from '~/logics/api'
import redaxios from 'redaxios'

const props = defineProps({
  filename: String
})


const discretizationForm = reactive({
  fileName: props.filename,
  columnName: null,
  amountOfSections: 1
})

const sendDiscretization = () => {
  redaxios.get(`${BASE_URL}/discretization/get`, {params: discretizationForm})
}

const normalizationForm = reactive({
  fileName: props.filename,
  columnName: null,
})
const sendNormalization = () => {

  redaxios.get(`${BASE_URL}/normalization/get`, {params: normalizationForm})
}

const digitalizationForm = reactive({
  fileName: props.filename,
  columnName: null,
})
const sendDigitalization = () => {
  redaxios.get(`${BASE_URL}/digitalization/get`, {params: digitalizationForm})
}


const csvData = ref(null);
const csvHeaders = ref(null);
const getCsv = async () => {
   const rawCsv = await redaxios.get(`${BASE_URL}/download?fileName=${props.filename}`);
   console.log(rawCsv)
   const rawRows = rawCsv.split('\n');
   const [headers,...rows] = rawRows.map(row => row.split(','));

   csvHeaders.value = headers;
   csvData.value = rows;
}

onMounted(() => {
  getCsv();
})

const { data: file, isFetching } = useApi(`getFileInfo?fileName=${props.filename}`).get().json()

const tab = ref('dyskretyzacja')

const columnOptions = computed(() => {
 return file.value?.columnNames.map(columnName => ({label: columnName, value: columnName})) ?? [];
})

</script>
