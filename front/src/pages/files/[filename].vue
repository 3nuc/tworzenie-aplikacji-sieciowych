<template>
  <div>
    <div v-if="isFetching">
      Ładowanie...
    </div>
    <template v-else>
      <div class="grid grid-cols-1">
        <div class="text-6xl text-white font-bold bg-white bg-gradient-to-r from-purple-400 via-pink-500 to-red-500 p-4 mb-4">
          {{ filename }}
        </div>
        <div>
          <div class="grid grid-rows-2 grid-cols-2">
            <div>Liczba kolumn: <span class="text-2xl font-bold">{{ file?.columnCount }}</span></div>
            <div>Liczba rzędów: <span class="text-2xl font-bold">{{ file?.rowCount }}</span></div>
          </div>
        </div>
        <el-divider />
        <el-tabs v-model="tab" class="text-white mt-6 mb-6" tab-position="left">
          <el-tab-pane label="Dyskretyzacja" name="dyskretyzacja">
            <el-form label-position="top">
              <el-form-item label="Kolumna">
                <el-select v-model="discretizationForm.columnName">
                  <el-option v-for="option in columnOptions" :key="option.value" :value="option.value" :label="option.label" />
                </el-select>
              </el-form-item>
              <el-form-item label="Ilość sekcji">
                <el-input-number v-model="discretizationForm.amountOfSections" />
              </el-form-item>
              <el-form-item>
                <el-button type="success" @click="sendDiscretization">
                  Wyślij
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="Normalizacja" name="normalizacja">
            <el-form label-position="top">
              <el-form-item label="Kolumna">
                <el-select v-model="normalizationForm.columnName">
                  <el-option v-for="option in columnOptions" :key="option.value" :value="option.value" :label="option.label" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="success" @click="sendNormalization">
                  Wyślij
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="Digitalizacja" name="digitalizacja">
            <el-form label-position="top">
              <el-form-item label="Kolumna">
                <el-select v-model="digitalizationForm.columnName">
                  <el-option v-for="option in columnOptions" :key="option.value" :value="option.value" :label="option.label" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="success" @click="sendDigitalization">
                  Wyślij
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
        <el-divider />
      </div>
      <el-table v-if="csvData !== null" :data="csvData" stripe border height="1000">
        <el-table-column v-for="columnName,index in file.columnNames" :prop="columnName" :label="columnName" :key="index" sortable />
      </el-table>
    </template>
  </div>
</template>
<script setup lang="ts">
import { defineProps, ref, computed, reactive, onMounted } from 'vue'
import redaxios from 'redaxios'
import { useApi, BASE_URL } from '~/logics/api'

const props = defineProps({
  filename: String,
})

const discretizationForm = reactive({
  fileName: props.filename,
  columnName: null,
  amountOfSections: 1,
})

const sendDiscretization = async() => {
  await redaxios.get(`${BASE_URL}/discretization/get`, { params: discretizationForm })
  await sharedExecuteTasks()
}

const normalizationForm = reactive({
  fileName: props.filename,
  columnName: null,
})
const sendNormalization = async() => {
  await redaxios.get(`${BASE_URL}/normalization/get`, { params: normalizationForm })
  await sharedExecuteTasks()
}

const digitalizationForm = reactive({
  fileName: props.filename,
  columnName: null,
})
const sendDigitalization = async() => {
  await redaxios.get(`${BASE_URL}/digitalization/get`, { params: digitalizationForm })
  await sharedExecuteTasks()
}

const csvData = ref(null)
const csvHeaders = ref(null)
const getCsv = async() => {
  const { data: rawCsv } = await redaxios.get(`${BASE_URL}/download?fileName=irysy.csv`)
  const rawRows = rawCsv.split('\n')
  const [headers, ...rows] = rawRows.map(row => row.split(','))

  const objs = rows.map(row => row.map((value, index) => [headers[index], value]))
  const arrayOfObjs = objs.map(row => Object.fromEntries(row))

  csvData.value = arrayOfObjs
}

onMounted(() => {
  getCsv()
})

const { data: file, isFetching, execute } = useApi(`getFileInfo?fileName=${props.filename}`).get().json()

const sharedExecuteTasks = async() => {
  await execute()
  await getCsv()
}

const tab = ref('dyskretyzacja')

const columnOptions = computed(() => {
  return file.value?.columnNames.map(columnName => ({ label: columnName, value: columnName })) ?? []
})

</script>
