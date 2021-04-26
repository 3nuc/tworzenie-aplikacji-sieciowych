<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useFileList } from './logics'
const router = useRouter()

// const { data: files, isFetching, error } = useApi('listOfFiles').get().json<string[]>()
const { state: files, isReady } = useFileList()
const isEmpty = computed(() => files.value?.length === 0)
</script>

<template>
  <div class="grid grid-cols-3">
    <div class="col-start-2">
      <div class="text-4xl mb-8 text-left color-gray-2">
        Lista plików:
      </div>
      <el-skeleton :loading="!isReady">
        <template #template>
          <el-card>
            <template #header>
              <div class="text-4xl">
                Proszę czekać...
              </div>
            </template>
          </el-card>
        </template>
        <el-card v-for="file in files" :key="file">
          <template #header>
            <div class="flex justify-between">
              <span class="text-4xl sm:mr-5">
                {{ file }}
              </span>
              <el-button @click="router.push(`/files/${file}`)">
                Otwórz
              </el-button>
            </div>
          </template>
        </el-card>
      </el-skeleton>
      <el-empty v-if="isEmpty">
        <router-link to="/upload">
          <el-link class="text-3xl" type="warning">
            Brak plików. Kliknij aby przejść do wysyłania
          </el-link>
        </router-link>
      </el-empty>
    </div>
  </div>
</template>
