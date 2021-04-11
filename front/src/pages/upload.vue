<script setup lang="ts">
import { BASE_URL } from '~/logics/api'
import { ref } from 'vue'
const uploadAction = `${BASE_URL}/upload`
const uploaded = ref(false)
const error = ref(false)

const onSuccess = () => {
  uploaded.value = true
}
const onError = (err) => {
  if(err.status === 406) {
    error.value = 'Plik o tej nazwie istnieje już w bazie'
  } else error.value = err;

}
</script>

<template>
  <el-upload drag :action="uploadAction" accept=".csv" :on-success="onSuccess" :on-error="onError" v-if="!error">
    <div class="text-grey-100 hover:text-blue-500 animate-pulse">
      <div class="text-2xl">Przeciągnij plik lub kliknij</div>
      <div>
        <vite-icon-carbon-upload class="h-24 w-24 mt-5" />
      </div>
    </div>
  </el-upload>
  <div v-if="uploaded" class="text-green-500">
    <vite-icon-carbon-checkmark /> Wysłano pomyślnie
  </div>
  <template v-if="error">
    <div class="text-red-500 text-3xl">Wystąpił błąd. {{error}} </div> 
    <div class="mt-8">
      <el-button type="primary" @click="error = false">Spróbuj ponownie</el-button>
    </div>
  </template>
</template>
