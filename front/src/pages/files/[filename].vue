<script setup lang="ts">
import { defineProps, ref, computed, reactive, onMounted } from 'vue'
import { useDigitalization, useDiscretizaiton, useFileCsv, useNormalization, useFileInfo } from '~/pages/files/logics'

const props = defineProps({
  filename: { type: String, required: true },
})
const logics = {
  digitalization: useDigitalization(),
  discretization: useDiscretizaiton(),
  normalization: useNormalization(),
  fileInfo: useFileInfo({ fileName: props.filename, immediate: true }),
  fileCsv: useFileCsv({ fileName: props.filename, immediate: true }),
}

onMounted(() => {
  loadForm()
})

const sendDigitalization = async() => {
  await logics.digitalization.execute()
  loadForm()
}

const sendDiscretization = async() => {
  await logics.discretization.execute()
  loadForm()
}

const sendNormalization = async() => {
  await logics.normalization.execute()
  loadForm()
}

const loadForm = () => {
  logics.fileInfo.execute()
  logics.fileCsv.execute()
}

const isReady = computed(() => logics.fileCsv.isReady.value && logics.fileInfo.isReady.value)

const tab = ref('dyskretyzacja')

const columnOptions = computed(() => {
  return logics.fileInfo.columnNames?.map(columnName => ({ label: columnName, value: columnName })) ?? []
})

</script>

<template>
  <div>
    <template v-if="isReady">
      <div class="grid grid-cols-1">
        <div class="text-6xl text-white font-bold bg-white bg-gradient-to-r from-purple-400 via-pink-500 to-red-500 p-4 mb-4">
          {{ filename }}
        </div>
        <div>
          <div class="grid grid-rows-2 grid-cols-2">
            <div>Liczba kolumn: <span class="text-2xl font-bold">{{ logics.fileInfo.state.value.columnCount }}</span></div>
            <div>Liczba rzędów: <span class="text-2xl font-bold">{{ logics.fileInfo.state.value.rowCount }}</span></div>
          </div>
        </div>
        <el-divider />
        <el-tabs v-model="tab" class="text-white mt-6 mb-6" tab-position="left">
          <el-tab-pane label="Dyskretyzacja" name="dyskretyzacja">
            <el-form label-position="top">
              <el-form-item label="Kolumna">
                <el-select v-model="logics.discretization.form.columnName">
                  <el-option v-for="option in columnOptions" :key="option.value" :value="option.value" :label="option.label" />
                </el-select>
              </el-form-item>
              <el-form-item label="Ilość sekcji">
                <el-input-number v-model="logics.discretization.form.amountOfSections" />
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
                <el-select v-model="logics.normalization.form.columnName">
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
                <el-select v-model="logics.digitalization.form.columnName">
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
      <el-table v-if="logics.fileCsv.isReady.value" :data="logics.fileCsv.state.value" stripe border height="1000">
        <el-table-column v-for="columnName,index in logics.fileInfo.state.value.columnNames" :key="index" :prop="columnName" :label="columnName" sortable />
      </el-table>
    </template>
    <div v-else>
      Ładowanie...
    </div>
  </div>
</template>
