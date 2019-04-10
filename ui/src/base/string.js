/**
 * 扩展String
 * @author: Merjiezo
 * @since: 2017-05-27
 */
;(function() {
    'use strict';
    var fn = String.prototype;
    fn.interceptString = function(length) {
        if (this.length > length) {
            return this.substring(0, length) + '...';
        }
        return this;
    },
    fn.toArray = function(tag) {
        if (this.indexOf(tag) != -1) {
            return this.split(tag);
        } else {
            if (this !== '') {
                return [this.toString];
            } else {
                return [];
            }
        }
    },
    fn.toNumber = function() {
        return this.replace(/\D/g, '');
    },
    fn.toCN = function() {
        var regEx = /[^\u4e00-\u9fa5\uf900-\ufa2d]/g;
        return this.replace(regEx, '');
    },
    fn.startWith = function(part) {
        return this.substring(0, part.length) == part;
    },
    fn.endWith = function(part) {
        return this.substring(this.length - part.length) == part;
    },
    fn.replaceAll = function(s1, s2) {
        var regEx = new RegExp(s1, 'gm');
        return this.replace(regEx, s2);
    }
    fn.isEmpty = function() {
        return this === ''
    }
})();
