'use strict';

!(function () {

  if (document.readyState === "complete" || (document.readyState !== "loading" && !document.documentElement.doScroll)) {
    main();
  } else {
    document.addEventListener("DOMContentLoaded", main);
  }

  function main() {
    // ...
  }

}());
