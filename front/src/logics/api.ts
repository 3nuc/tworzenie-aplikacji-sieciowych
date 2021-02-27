import {useFetch} from '@vueuse/core'
const { BASE_URL } = process.env;

type UseFetchParams= Parameters<typeof useFetch>;
type UseFetchParamsWithoutUrl = [UseFetchParams[1], UseFetchParams[2]];
export const useApi = <TResponse>(...params: UseFetchParamsWithoutUrl) => {
  return useFetch<TResponse>(BASE_URL, ...params)
}

const a = []
