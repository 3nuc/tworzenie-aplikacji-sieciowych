import { reactive } from 'vue'
import { useAsyncState } from '@vueuse/core'
import { requestDiscretization, requestDigitalization, requestNormalization, getListOfFiles, getFileCsv, getFileInfo } from '~/services'

export const useDiscretizaiton = () => {
  const form = reactive({
    fileName: null,
    columnName: null,
    amountOfSections: 1,
  })

  const asyncState = useAsyncState(
    async() => await (requestDiscretization(form)).data,
    null,
    { immediate: false },
  )

  return {
    form,
    ...asyncState,
  }
}

export const useNormalization = () => {
  const form = reactive({
    fileName: null,
    columnName: null,
  })

  const asyncState = useAsyncState(
    async() => (await requestNormalization(form)).data,
    null,
    { immediate: false },
  )

  return {
    form,
    ...asyncState,
  }
}

export const useDigitalization = () => {
  const form = reactive({
    fileName: null,
    columnName: null,
  })

  const asyncState = useAsyncState(
    async() => (await requestDigitalization(form)).data,
    null,
    { immediate: false },
  )

  return {
    form,
    ...asyncState,
  }
}

export const useFileInfo = (args: { fileName: string; immediate: boolean }) => {
  const asyncState = useAsyncState(
    async() => (await getFileInfo({fileName: args.fileName})).data,
    null,
    { immediate: args.immediate },
  )

  return { ...asyncState }
}

export const useFileList = () => {
  const asyncState = useAsyncState(
    async() => (await getListOfFiles()).data,
    null,
  )

  return {
    ...asyncState,
  }
}

export const useFileCsv = (args: { fileName: string; immediate: boolean }) => {
  const formatCsv = async() => {
    const { data: rawCsv } = await getFileCsv({ fileName: args.fileName })
    const rawRows = rawCsv.split('\n')
    const [headers, ...rows] = rawRows.map(row => row.split(','))

    const objs = rows.map(row => row.map((value, index) => [headers[index], value]))
    return objs.map(row => Object.fromEntries(row))
  }
  const asyncState = useAsyncState(
    formatCsv,
    null,
    { immediate: args.immediate },
  )

  return {
    ...asyncState,
  }
}
