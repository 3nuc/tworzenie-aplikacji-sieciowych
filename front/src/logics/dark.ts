import { watch, computed } from 'vue'
import { useToggle } from '@vueuse/core'
import { colorSchema } from './store'

export const isDark = computed({
  get() {
    //element plus doesn't support dark mode
    return false
  },
  set() {
    colorSchema.value = 'light'
  },
})

export const toggleDark = useToggle(isDark)

watch(
  isDark,
  v => typeof document !== 'undefined' && document.documentElement.classList.toggle('dark', v),
  { immediate: true },
)
