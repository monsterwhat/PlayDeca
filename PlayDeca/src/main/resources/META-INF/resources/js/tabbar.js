/**
 * Opens a tab and hides other tabs.
 * @param {Event} evt - The event object.
 * @param {string} tabName - The name of the tab to open.
 */

function openTab(evt, tabName) {
  // Get all elements with the class "content-tab"
  var i, x, tablinks;
  x = document.getElementsByClassName("content-tab");
  
  // Hide all elements with the class "content-tab"
  for (i = 0; i < x.length; i++) {
    x[i].style.display = "none";
  }
  
  // Get all elements with the class "tab"
  tablinks = document.getElementsByClassName("tab");
  
  // Remove the "is-active" class from all elements with the class "tab"
  for (i = 0; i < x.length; i++) {
    tablinks[i].className = tablinks[i].className.replace(" is-active", "");
  }
  
  // Display the element with the id equal to the value of tabName
  document.getElementById(tabName).style.display = "block";
  
  // Add the "is-active" class to the clicked element
  evt.currentTarget.className += " is-active";
}