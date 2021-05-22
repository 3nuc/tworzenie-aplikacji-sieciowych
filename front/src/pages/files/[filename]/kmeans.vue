<script setup lang="ts">
import { computed, defineProps, ref } from 'vue'
import { useAsyncState } from '@vueuse/core'
import { useFileInfo } from '~/pages/files/logics'
import { getKmeans } from '~/services'

const props = defineProps(['filename'])
const form = ref({
  decissionColumn: null,
  columns: [],
  findType: 'Euklidean',
})

const formFormatted = computed(() => ({
  ...form.value,
  fileName: props.filename,
  columns: form.value.columns.join(',').replaceAll('"', ''),
  decissionColumn: form.value.decissionColumn?.replaceAll('"', ''),
}))

const isSendDisabled = computed(() => form.value.columns.length === 0 || form.value.decissionColumn === null)

const { state, isReady } = useFileInfo({ fileName: props.filename, immediate: true })
const columns = computed(() => state?.value?.columnNames ?? [])

const { state: kmeansData, execute: executeKmeans }
  = useAsyncState(async() => (await getKmeans(formFormatted.value)).data, null, { immediate: false })

const tableData = computed(() => {
  const tableDataRows = kmeansData.value?.dataset.slice(1) ?? []
  const mapRowToObject = (row: string[]) => {
    const obj: Record<string, string> = {}
    tableHeaders.value.forEach((header, index) => {
      obj[header] = row[index]
    })
    return obj
  }
  return tableDataRows.map(mapRowToObject)
})
const tableHeaders = computed(() => kmeansData.value?.dataset.at(0))

const tableRowClassName = ({ row, rowIndex }) => {
  const isCorrect = (kmeansData.value?.properties.correctlyPredictedRowIDS ?? []).includes(rowIndex)
  const isIncorrect = (kmeansData.value?.properties.incorrectlyPredictedRowIDS ?? []).includes(rowIndex)
  console.log(isCorrect, isIncorrect)
  if (isCorrect)
    return 'row-correct'
  else if (isIncorrect)
    return 'row-incorrect'
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
  <el-table :data="tableData" :row-class-name="tableRowClassName">
    <el-table-column v-for="header in tableHeaders" :key="header" :prop="header" :label="header" />
  </el-table>
</template>

<style>
.row-correct {
  color: green;
}
.row-incorrect {
  color: red;
}
</style>
