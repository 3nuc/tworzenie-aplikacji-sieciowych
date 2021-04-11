import { createFetch } from '@vueuse/core'
export const BASE_URL = 'http://localhost:8082'

export const useApi = createFetch({ baseUrl: BASE_URL })
