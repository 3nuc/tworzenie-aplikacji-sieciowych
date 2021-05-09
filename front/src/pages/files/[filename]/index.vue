<script setup lang="ts">
import { defineProps, ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useDigitalization, useDiscretizaiton, useFileCsv, useNormalization, useFileInfo } from '~/pages/files/logics'
import FancyCard from '~/components/FancyCard.vue'

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

const router = useRouter()

</script>

<template>
  <div>
    <template v-if="isReady">
      <div class="grid grid-cols-1 ">
        <div class="grid grid-cols-3">
          <div class="col-span-2">
            <FancyCard>
              <div class="text-6xl text-white font-bold">
                {{ filename }}
              </div>
              <div>Liczba kolumn: <span class="text-2xl font-bold">{{ logics.fileInfo.state.value.columnCount }}</span></div>
              <div class="mb-8">
                Liczba rzędów: <span class="text-2xl font-bold">{{ logics.fileInfo.state.value.rowCount }}</span>
              </div>
              <el-button @click="router.push({path: `/files/${props.filename}/find_neighbors/`})">
                Przejdź do wykrywania sąsiadów
              </el-button>
              <el-button @click="router.push({path: `/files/${props.filename}/kmeans`})">Przejdź do k-means</el-button>
            </FancyCard>
          </div>
          <div>
            <el-tabs v-model="tab" tab-position="left">
              <el-tab-pane label="Dyskretyzacja" name="dyskretyzacja">
                <el-form label-position="top" inline>
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
          </div>
        </div>
        <el-divider />
      </div>
      <table v-if="logics.fileCsv.isReady.value" class="table table-auto w-full border-collapse border-solid border-1 rounded-lg">
        <thead>
          <tr>
            <th v-for="columnName,index in logics.fileInfo.state.value.columnNames" v-text="columnName" />
          </tr>
        </thead>
        <tbody>
          <tr v-for="row in logics.fileCsv.state.value">
            <td v-for="cell in Object.values(row)" v-text="cell" />
          </tr>
        </tbody>
      </table>
    </template>
    <div v-else>
      Ładowanie...
    </div>
  </div>
</template>
