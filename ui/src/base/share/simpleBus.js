/**
 * Simple Event Bus
 * @author: Merjiezo
 * @since: 2018-07-02
 */
class Bus {

  constructor () {
    this._on = {}
  }

  trigger (evtName, ...args) {
    let evtArr = this._on[evtName]
    if (Array.isArray(evtArr)) {
      evtArr.forEach(func => {
        func.apply(this, args)
      })
    }
  }

  on (evtName, fn) {
    if (undefined === this._on[evtName]) {
      this._on[evtName] = []
    }
    let evtArr = this._on[evtName]
    if (evtArr.indexOf(fn) === -1) {
      evtArr.push(fn)
    }
  }

  off (evtName, fn) {
    if (fn) {
      let evtArr = this._on[evtName]
      let index = evtArr && evtArr.indexOf(fn)
      if (index >= 0) { evtArr.splice(index) }
    } else {
      delete(this._on[evtName])
    }
  }

}

export default Bus
