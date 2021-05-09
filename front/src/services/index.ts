import { api } from '~/logics/api'
export const requestNormalization = (args: {fileName: string; columnName: string; amountOfSections: string}) => api.get('normalization/get', { params: args })

export const requestDiscretization = (args: {fileName: string; columnName: string}) => api.get('discretization/get', { params: { fileName: args.fileName, columnName: args.columnName } })

export const requestDigitalization = (args: { fileName: string; columnName: string }) => api.get('digitalization/get', { params: { fileName: args.fileName, columnName: args.columnName } })

export const getListOfFiles = () => api.get('listOfFiles')

export const getFileInfo = (args: {fileName: string}) => api.get('getFileInfo', { params: args })

export const getFileCsv = (args: {fileName: string}) => api.get('download', { params: args })

interface NeighborsArgs {
  columns: string
  decissionColumn: string
  pointCoordinates: string
  findType: string
}

// rip consistent naming
export const getNeighbors = (args: NeighborsArgs) =>
  api.get('returnNearestNeighbours', { params: { ...args, returnAllColumns: false } })

export interface KmeansArgs {
  columns: string
  decissionColumn: string
  pointCoordinates: string
  findType: 'Euklidean' | 'Manhattan'
}

export interface ReturnKmeans {
  dataset: any[]
  properties: {
    correctlyPredictedIDS: number[]
    incorrectlyPredictedIDS: number[]
    correctlyPredictedAmount: {hits: number}
    hitsPercentage: {hitsPercentage: number}
  }
}

export const getKmeans = (args: KmeansArgs) =>
  api.get<ReturnKmeans>('predictDecision', { params: { ...args, returnAllColumns: true } })
