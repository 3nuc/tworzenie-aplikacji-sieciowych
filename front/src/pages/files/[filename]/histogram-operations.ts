export const chartify = (rawJson: any[]) => {
  const itemCounts = rawJson.items.map(item => item.itemCount)
  const domain = itemCounts?.[0].map(x=> x.decission) ?? []
  const items = itemCounts.map((x,section) => x.map( (y,index) => ({...y, section, index})))
  const actualChartData = items.reduce((acc, current) => [...acc, ...current], []);
  return {
    actualChartData, 
    domain,
  }
}
