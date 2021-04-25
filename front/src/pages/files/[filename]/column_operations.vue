<script setup>
import {ref, defineEmit} from 'vue'

const emit = defineEmit(['discretization', 'digitalizaiton', 'normalization'])
const tab = ref('dyskretyzacja')
</script>

<template>
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
</template>
