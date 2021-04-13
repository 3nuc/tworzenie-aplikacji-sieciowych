import { api } from '~/logics/api'
export const requestNormalization = (args: {fileName: string; columnName: string; amountOfSections: string}) => api.get('normalization/get', { params: args })

export const requestDiscretization = (args: {fileName: string; columnName: string}) => api.get('discretization/get', { params: { fileName, columnName } })

export const requestDigitalization = (args: { fileName: string; columnName: string }) => api.get('digitalization/get', { params: { fileName, columnName } })

export const getListOfFiles = () => api.get('listOfFiles')

export const getFileInfo = (args: {fileName: string}) => api.get('getFileInfo', { params: args })

export const getFileCsv = (args: {fileName: string}) => api.get('download', { params: args })
