export const chartify = (rawJson: any[]) => {
  const itemCounts = rawJson.items.map(item => item.itemCount)
  const items = itemCounts.map((x,section) => x.map( (y,index) => ({...y, section, index})))
  return items.reduce((acc, current) => [...acc, ...current], [])
}
