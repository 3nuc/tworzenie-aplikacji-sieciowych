<script setup lang="ts">
import { computed } from 'vue'
import { useRouter } from 'vue-router'
import { useFileList } from './logics'
import FancyCard from '~/components/FancyCard.vue'
const router = useRouter()

// const { data: files, isFetching, error } = useApi('listOfFiles').get().json<string[]>()
const { state: files, isReady } = useFileList()
const isEmpty = computed(() => files.value?.length === 0)
</script>

<template>
  <div class="inline-flex justify-center flex-col divide-y-2 divide-solid divide-gray-500">
    <FancyCard>
      <h1 class="text-4xl mb-4 ">
        Dostępne pliki
      </h1>
      <div class="flex flex-row">
        <button
          v-for="file in files"
          :key="file"
          class="group text-2xl inline-flex items-center h-18 m-3 p-6 shadow hover:shadow-lg transition-shadow transition-all rounded-lg ring-1 hover:bg-gradient-to-r hover:bg-red-400 hover:ring-purple-400 hover:ring-opacity-35 hover:ring-4 hover:text-white"
          @click="router.push(`/files/${file}`)"
        >
          <vite-icon-carbon-dashboard class="group-hover:animate-bounce self-start mr-2" /> {{ file }}
        </button>
      </div>
    </FancyCard>
    <el-empty v-if="isEmpty">
      <router-link to="/upload">
        <el-link class="text-3xl" type="warning">
          Brak plików. Kliknij aby przejść do wysyłania
        </el-link>
      </router-link>
    </el-empty>
  </div>
</template>
