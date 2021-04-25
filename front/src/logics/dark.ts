import { watch, computed } from 'vue'
import { usePreferredDark, useToggle } from '@vueuse/core'
import { colorSchema } from './store'

const preferredDark = usePreferredDark()

export const isDark = computed({
  get() {
    //element plus doesn't support dark mode
    return false
  },
  set(v: boolean) {
    if (v === preferredDark.value)
      colorSchema.value = 'auto'
    else
      colorSchema.value = v ? 'dark' : 'light'
  },
})

export const toggleDark = useToggle(isDark)

watch(
  isDark,
  v => typeof document !== 'undefined' && document.documentElement.classList.toggle('dark', v),
  { immediate: true },
)
