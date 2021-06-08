import { rest } from 'msw'

export const handlers = [
  rest.get('http://localhost:8082/listOfFiles', (_req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.json(['irysy_mocked.csv']),
    )
  }),
]
