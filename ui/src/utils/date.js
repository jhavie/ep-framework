/**
 * 日期工具类封装方法
 * @author: Merjiezo
 * @since: 2017-05-26
 */

export const previousDate = function (date, format) {
  let timeStep = isNaN( + date)
    ? new Date().getTime()
    : new Date().getTime() - 1000 * 3600 * 24 * date
  return fecha.format(timeStep, format || 'YYYY-MM-DD')
}

export const previousRangeDate  = function (date, format) {
  return previousDate(date, format) + ',' + fecha.format(new Date(), format || 'YYYY-MM-DD')
}

//date1, date2为字符串YYYY-MM-DD
export const phaseDate = function (date1, date2) {
  let date1s = date1 ? new Date(date1.split("-")[0], date1.split("-")[1]-1, date1.split("-")[2]) : new Date()
  let date2s = date2 ? new Date(date2.split("-")[0], date2.split("-")[1]-1, date2.split("-")[2]) : new Date()
  let phase = date1s.getTime() - date2s.getTime()
  if(phase < 0) {
    phase = -phase
  }
  return phase / (1000 * 3600 * 24)
}
