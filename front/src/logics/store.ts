import { Ref } from 'vue'
import { useStorage } from '@vueuse/core'

export const colorSchema = useStorage('color-schema', 'light') as Ref<'auto' | 'dark' | 'light'>
