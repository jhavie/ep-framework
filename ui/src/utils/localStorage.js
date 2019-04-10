export default {
    setLocalStorage (key, value, exdays){
        var curTime = new Date().getTime();
        localStorage.setItem(key,JSON.stringify({data: value, time: curTime, exdays: exdays}))
    },

    getLocalStorage (key) {
        var value = localStorage.getItem(key);
        if(value===null){ //不存在
            return "";
        }
        var data = JSON.parse(value).data
        var time = JSON.parse(value).time
        var exdays = JSON.parse(value).exdays
        if(new Date().getTime() - time > exdays * 24 * 60 * 60 * 1000){  //过期
            return "";
        }
        return data;
    },

    delLocalStorage (key) {
        localStorage.removeItem(key);
    },
    
    clearLocalStorage () {   //全部清空
        localStorage.clear();
    },
}