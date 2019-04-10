/**
 * Framework Base
 * @author: Merjiezo
 * @since: 2018-06-23
 */
export const warn = (errMsg, funcName) => {
  console.warn(
    '[EP Framework Warning!],' +
    'Error Message is ' + errMsg + ', ' +
    'ERROR Find in file ' + funcName
  )
}

export const noop = () => {}

export const rtnOrigin = (data) => { return data }
