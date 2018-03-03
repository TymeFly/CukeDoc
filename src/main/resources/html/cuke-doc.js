"use strict";

  window.onscroll = function() {
    document.getElementById("scrollUp").style.display =
        (document.body.scrollTop > 40 || document.documentElement.scrollTop > 40) ? "block" : "none";
  }

  function showPanel(table, panel, show) {
    let panelRows = document.getElementsByClassName(panel);
    let panelIndex = panelRows.length;

    while (panelIndex-- != 0) {
      let classNames = panelRows[panelIndex].className;
      let enable = (classNames.indexOf(show) !== -1);
      panelRows[panelIndex].style.display = enable ? '' : 'none';
    }

    let tabs = document.getElementsByClassName(table);
    let tabIndex = tabs.length;

    while (tabIndex-- != 0) {
      let classNames = tabs[tabIndex].className;
      let enable = (classNames.indexOf(show) !== -1);

      if (enable) {
        tabs[tabIndex].className = classNames + " activeTab"
      } else {
        tabs[tabIndex].className = classNames.replace("activeTab", "").trim();
      }
    }
  }
