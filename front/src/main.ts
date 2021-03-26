import 'windi.css'
import './styles/main.css'
import 'element-plus/lib/theme-chalk/index.css'
import { ViteSSG } from 'vite-ssg'
import generatedRoutes from 'pages-generated'
import { setupLayouts } from 'layouts-generated'
import App from './App.vue'
import ElementPlus from 'element-plus'
import ApexCharts from 'vue3-apexcharts'

const routes = setupLayouts(generatedRoutes)

// https://github.com/antfu/vite-ssg
export const createApp = ViteSSG(
  App,
  { routes },
  (ctx) => {
    // install all modules under `modules/`
    ctx.app.use(ElementPlus)
    ctx.app.use(ApexCharts)
    Object.values(import.meta.globEager('./modules/*.ts')).map(i => i.install?.(ctx))
  },
)
