import 'windi.css'
import './styles/main.css'
import 'element-plus/lib/theme-chalk/index.css'
//import { ViteSSG } from 'vite-ssg'
import generatedRoutes from 'pages-generated'
import { setupLayouts } from 'layouts-generated'
import { createApp } from 'vue'
import { createHead } from '@vueuse/head'
import { createRouter, createWebHashHistory } from 'vue-router'
import { createI18n } from 'vue-i18n'
import App from './App.vue'
import ElementPlus from 'element-plus'
import locale from 'element-plus/lib/locale/lang/pl'
import { UserModule } from '~/types'
import VueApexChart from 'vue3-apexcharts'

const routes = setupLayouts(generatedRoutes)

const router = new createRouter({
  history: createWebHashHistory(),
  routes
})

const messages = Object.fromEntries(
  Object.entries(
    import.meta.globEager('../../locales/*.json'))
    .map(([key, value]) => {
      return [key.slice(14, -5), value.default]
    }),
)

const i18n = createI18n({
  legacy: false,
  locale: 'en',
  messages,
})
console.log(JSON.stringify(VueApexChart))
const head = createHead();
// https://github.com/antfu/vite-ssg
export const app = createApp(App);
app.use(router)
app.use(ElementPlus, { locale })
app.use(head)
app.use(i18n)
app.use(VueApexChart)

app.mount('#app')
