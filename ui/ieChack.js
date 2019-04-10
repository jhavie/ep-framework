;(function (window, document, getBrowserInfo, undefined) {
  'use strict';
  var browserInfo = getBrowserInfo(window.navigator.userAgent),
    ver = parseInt(browserInfo["version"], 10) || 0;
  if ((browserInfo["msie"] && ver < 9) ||
    (browserInfo["chrome"] && ver < 20) ||
    (browserInfo["firefox"] && ver < 20) ||
    (browserInfo["safari"] && ver < 5)) {
    location.href = BASE_URL + "dist/ie/index.html"
  }
}(window, document, function (userAgent) {
  var browser = {},
  matched = (function (ua) {
    var match = /(chrome)[ \/]([\w.]+)/.exec(ua) ||
      ///(webkit)[ \/]([\w.]+)/.exec( ua ) ||
      ///version[\/][\d.]+.*(safari)[ \/]([\w.]+)/.exec(ua) ||
      /version[\/]([\d.]+).*(safari)[ \/][\w.]+/.exec(ua) ||
      /(msie) ([\w.]+)/.exec(ua) ||
      /(firefox)[\/]([\w.]+)/.exec(ua) ||
      /(opera)(?:.*version|)[ \/]([\w.]+)/.exec(ua) ||
      ua.indexOf("compatible") < 0 && /(mozilla)(?:.*? rv:([\w.]+)|)/.exec(ua) || [];
    if (match[2] === "safari") { 
      return {
        browser: match[2] || "",
        version: match[1] || "0"
      };
    };
    return {
      browser: match[1] || "",
      version: match[2] || "0"
    };
  })(userAgent.toLowerCase());
  if (matched.browser) {
    browser[matched.browser] = true;
    browser.version = matched.version;
  }
  return browser;
}));