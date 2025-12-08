/*
 *  Copyright 2025 Better Orange IR & HV AG
 *
 *  Licensed under the Meetingbase License (the "License");
 *  Vou may not use this file except in compliance with the License.
 *  You may obtain a copy of the License in the root directory (MEETINGBASE_LICENSE).
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
var logoutButtonClicked = false;

window.addEventListener("beforeunload", function (event) {
  let isLoggedIn =
    document.querySelector("#inhaltsform\\:schliessenId").value === "1";
  logoutButtonClicked =
    document.querySelector("#inhaltsform\\:logoutButtonClicked").value === "1";

  if (isLoggedIn && !logoutButtonClicked) {
    event.returnValue = "Seite verlassen?";
  } else {
    delete event.returnValue;
  }
});

function setLogoutButtonClicked() {
  let inputField = document.querySelector("#inhaltsform\\:logoutButtonClicked");
  inputField.value = "1";
}

function nachAjaxButton(data) {
  var status = data.status; //  "begin", "complete" oder "success".
  switch (status) {
    case "begin": // Before the ajax request is sent.
      showModal("loading-modal");
      break;

    case "complete": // After the ajax response is arrived.
      break;

    case "success": // After update of HTML DOM based on ajax response.
      window.scrollTo(0, 0);
      hideModal("loading-modal");
      setJahr();
      setFixedHeader();
      break;
  }
}

function nachAjaxButtonUpload(data) {
  var status = data.status; //  "begin", "complete" oder "success".

  switch (status) {
    case "begin": // Before the ajax request is sent.
      showModal("loading-modal");
      break;

    case "complete": // After the ajax response is arrived.
      break;

    case "success": // After update of HTML DOM based on ajax response.
      window.scrollTo(0, 0);
      hideModal("loading-modal");
      setJahr();
      setFixedHeader();
      loadUpload();
      break;
  }
}

function nachAjaxButtonMitteilung(data) {
  var status = data.status; //  "begin", "complete" oder "success".

  switch (status) {
    case "begin": // Before the ajax request is sent.
      openWebSocket();
      showModal("loading-modal");
      break;

    case "complete": // After the ajax response is arrived.
      break;

    case "success": // After update of HTML DOM based on ajax response.
      window.scrollTo(0, 0);
      hideModal("loading-modal");
      setJahr();
      setFixedHeader();
      break;
  }
}

function nachAjaxButtonKonferenz(data) {
  var status = data.status; //  "begin", "complete" oder "success".

  switch (status) {
    case "begin": // Before the ajax request is sent.
      showModal("loading-modal");
      break;

    case "complete": // After the ajax response is arrived.
      break;

    case "success": // After update of HTML DOM based on ajax response.
      window.scrollTo(0, 0);
      hideModal("loading-modal");
      setJahr();
      setFixedHeader();
      break;
  }
}

function setFileUpload(
  file,
  erlaubteFormateVideo,
  erlaubteFormateText,
  filesizeMaxVideo,
  filesizeMaxText
) {
  showModal("loading-modal");
  filesizeMaxVideo = filesizeMaxVideo * 1024 * 1024;
  var lblUploadFileName = document.getElementById("lblUploadFileName");
  var filenameArray = file.value.split("\\");
  var filename = filenameArray[filenameArray.length - 1];
  var filetypArray = filename.split(".");
  var filetyp = filetypArray[filetypArray.length - 1].toLowerCase();
  filesize = file.files[0].size;
  lblUploadFileName.innerText = filename;
  if (erlaubteFormateVideo.toLowerCase().includes(filetyp)) {
    if (filesize > filesizeMaxVideo) {
      resetUploadFile(file, "Zu groß Video");
    } else {
      acceptUploadFile();
    }
  } else if (erlaubteFormateText.toLowerCase().includes(filetyp)) {
    if (filesize > filesizeMaxText) {
      resetUploadFile(file, "Zu groß Text");
    } else {
      acceptUploadFile();
    }
  } else {
    resetUploadFile(file, "Falscher Dateityp");
  }
  hideModal("loading-modal");
}

function resetUploadFile(file, message) {
  file.value = "";
  document.getElementById("fileUploadVorAuswahl").style.display = "block";
  document.getElementById("fileUploadNachAuswahl").style.display = "none";
  set_info_modal(message, 0);
  showModal("info-modal");
}

function resetUploadFileInput() {
  document.getElementById(
    "inhaltsform:iIBotschaftenEinreichen:uploadFile"
  ).value = "";
}

function acceptUploadFile() {
  document.getElementById("fileUploadVorAuswahl").style.display = "none";
  document.getElementById("fileUploadNachAuswahl").style.display = "block";
}

/* Führt einen Scroll zum Bereich des Streams aus.Muß nach dem Streamstart aufgerufen werden(anstelle nachAjaxButton) */
function nachAjaxButtonStreamstart(data) {
  const c = document.getElementById("streamform");
  window.scrollTo(0, c.scrollHeight);
}

/* === Start Modal === */
var modal;
var modal_nachricht;
var modal_typ;

function showModal(modalName) {
  modal = document.getElementById(modalName);
  modal.style.display = "block";
}

function hideModal(modalName) {
  modal = document.getElementById(modalName);
  modal.style.display = "none";
}

function set_info_modal(modal_message, typ) {
  $("#info-modal-message").text(modal_message);

  switch (typ) {
    case 0:
      $("#info-modal-icon").removeClass("fas fa-check-circle success");
      $("#info-modal-icon").addClass("fas fa-exclamation-circle error");
      break;
    case 1:
      $("#info-modal-icon").removeClass("fas fa-exclamation-circle error");
      $("#info-modal-icon").addClass("fas fa-check-circle success");
      break;
    case 2:
      break;
  }
}

/* === Ende Modal === */

/* === Passwort === */
function showPassword() {
  var input = document.getElementById("inhaltsform:passwort");
  input.setAttribute("type", "text");
  document.getElementById("spanShowPassword").classList.remove("d-inline");
  document.getElementById("spanShowPassword").classList.add("d-none");
  document.getElementById("spanHidePassword").classList.remove("d-none");
  document.getElementById("spanHidePassword").classList.add("d-inline");
}

function hidePassword() {
  var input = document.getElementById("inhaltsform:passwort");
  input.setAttribute("type", "password");
  document.getElementById("spanShowPassword").classList.remove("d-none");
  document.getElementById("spanShowPassword").classList.add("d-inline");
  document.getElementById("spanHidePassword").classList.remove("d-inline");
  document.getElementById("spanHidePassword").classList.add("d-none");
}

function setStickyFooter() {
  var heightFooter = document.getElementById("footer-bereich").offsetHeight;
  var inhalt = document.getElementById("inhalt");
  inhalt.style.paddingBottom = heightFooter + 50 + "px";
}

var fixHeader = 0;

function setFixedHeader() {
  var widthWindow = window.innerWidth;
  var inhalt = document.getElementById("inhalt");
  if (widthWindow > 768) {
    if (fixHeader == 1) {
      var heightHeader = document.getElementById("header-bereich").offsetHeight;
      inhalt.style.paddingTop = heightHeader + 50 + "px";
    }
  } else {
    inhalt.style.paddingTop = 0 + "px";
  }
}

// Start aktuelle Jahreszahl
function setJahr() {
  var aktuellesJahr = new Date().getFullYear();
  var copyrightText = document.getElementById("copyright").innerHTML;
  var copyrightTextNeu = copyrightText.replace("jahr", aktuellesJahr);
  document.getElementById("copyright").innerHTML = copyrightTextNeu;
}
// Ende aktuelle Jahreszahl

// Zeichen für Textarea zählen
function setVerbleibendeZeichen(event) {
  var textarea = event.target;
  var eingabe = textarea.value;
  var laenge = eingabe.length;
  document.getElementById("span-verbleibende-Zeichen").innerHTML = laenge;
}

// Nach Sprach Link
function nach_sprach_link(data) {
  var status = data.status; //  "begin", "complete" oder "success".

  switch (status) {
    case "begin": // Before the ajax request is sent.
      break;

    case "complete": // After the ajax response is arrived.
      break;

    case "success": // After update of HTML DOM based on ajax response.
      setJahr();
      break;
  }
}

// Nach Test Btn
function nach_test_btn(data) {
  var status = data.status; //  "begin", "complete" oder "success".

  switch (status) {
    case "begin": // Before the ajax request is sent.
      break;

    case "complete": // After the ajax response is arrived.
      break;

    case "success": // After update of HTML DOM based on ajax response.
      setJahr();
      break;
  }
}

// Hide Cookie Hint
function hide_cookie_hint() {
  var cookieHint = document.getElementById("cookieHint");
  cookieHint.style.display = "none";
}
