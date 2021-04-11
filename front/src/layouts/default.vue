<script setup lang="ts">
import {useRouter} from 'vue-router'

const router = useRouter()
</script>
<template>
  <el-menu mode="horizontal" @select="(_index, _indexPath, attrs) => router.push(attrs.route)">
    <el-menu-item route="/" index="0">
      <div class="bg-white text-white bg-gradient-to-r from-purple-400 via-pink-500 to-red-500">
        <vite-icon-carbon-chemistry class="text-white-500 w-12 h-12"/>
      </div>
    </el-menu-item>
    <el-menu-item route="/files" index="1">
       <vite-icon-carbon-list/>
       Lista plików
    </el-menu-item>
    <el-menu-item route="/upload" index="2">
      <vite-icon-carbon-upload/> Upload
    </el-menu-item>
  </el-menu>
  <main class="px-4 py-10 text-center text-gray-700 dark:text-gray-200">
    <router-view />
    <Footer />
    <div class="mt-5 mx-auto text-center opacity-25 text-sm">
    </div>
  </main>
</template>
