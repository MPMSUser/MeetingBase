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
var errorOccured = false;
var errorMsg = "";
var modalAfterError = "";
var trueAfterError = false;
var viewNummer = 0;

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

/* --- Initialize Popovers --- */
function initializePopovers() {
  var popoverTriggerList = [].slice.call(
    document.querySelectorAll('[data-bs-toggle="popover"]')
  );
  var popoverList = popoverTriggerList.map(function (popoverTriggerEl) {
    return new bootstrap.Popover(popoverTriggerEl);
  });
}

/*--- Set Lang-Popover ---*/
function setLangPopover() {
  $(document).ready(function () {
    var options = {
      html: true,
      content: $('[data-name="popover-lang-content"]'),
    };
    var popover_lang = document.getElementById("popover-lang-btn");
    var popover = new bootstrap.Popover(popover_lang, options);
  });
}

function setLangPopover01() {
  $(document).ready(function () {
    var options = {
      html: true,
      content: $('[data-name="popover-lang-content-01"]'),
    };
    var popover_lang = document.getElementById("popover-lang-btn-01");
    var popover = new bootstrap.Popover(popover_lang, options);
  });
}

/*--- Hide Popover ---*/
function hideLangPopover() {
  $("#popover-lang-btn").popover("hide");
  $("#popover-lang-btn-01").popover("hide");
}

/* --- Modales --- */
function showModal(id, static) {
  if (static === "true" || static === true) {
    $("#" + id).modal({ backdrop: "static", keyboard: false });
  }
  $("#" + id).modal("show");
}

function hideModal(id) {
  $("#" + id).modal("hide");
}

function hideAllModalsByClass(className) {
  $(className).modal("hide");
}

function hideAllModals() {
  $(".modal").modal("hide");
}

function okBtnModals() {
  location.reload();
}

function set_info_modal_response(modal_message) {
  $("#info-modal-response-message").text(modal_message);
}

function hideAndCheckModal(modalId) {
  hideModal(modalId);
  if (viewNummer == 411) {
    showModal('mainInhaltModal');
  }
}

/* === Funktionen nach Button oder Links === */

// Nach Sprach Link
function nach_sprach_link(data) {
  var status = data.status; //  "begin", "complete" oder "success".

  switch (status) {
    case "begin": // Before the ajax request is sent.
      hideLangPopover();
      break;

    case "complete": // After the ajax response is arrived.
      break;

    case "success":
      initializePopovers();
      setLangPopover();
      setLangPopover01();
      break;
  }
}

function nachAjaxButtonLogin(
  data,
  modalShow,
  modalHide,
  moadlShowId,
  moadlHideId,
  static
) {
  var status = data.status; //  "begin", "complete" oder "success".

  switch (status) {
    case "begin":
      hideAllModals();
      showLoading();
      if (modalHide === "true" || modalHide === true) {
        hideModal(moadlHideId);
      }
      break;

    case "complete": // After the ajax response is arrived.
      break;

    case "success":
      hideLoading();
      if (errorOccured) {
        set_info_modal_response(errorMsg);
        showModal("infoModalResponse", true);
        errorOccured = false;
        errorMsg = "";
      } else {
        if ((modalShow === "true" || modalShow === true) &&
          !data.responseText.includes(
            "***VIEW***VIEW***VIEW***VIEW103VIEW***VIEW***VIEW***VIEW***"
          ) &&
          !data.responseText.includes(
            "***VIEW***VIEW***VIEW***VIEW101VIEW***VIEW***VIEW***VIEW***"
          )
        ) {
          showModal(moadlShowId, static);
        }
      }
      break;
  }
}

function nachAjaxButton(
  data,
  modalShow,
  modalHide,
  moadlShowId,
  moadlHideId,
  static
) {
  var status = data.status; //  "begin", "complete" oder "success".

  switch (status) {
    case "begin":
      hideAllModals();
      showLoading();
      if (modalHide === "true" || modalHide === true) {
        hideModal(moadlHideId);
      }
      break;

    case "complete": // After the ajax response is arrived.
      break;

    case "success":
      hideLoading();
      if (errorOccured) {

        set_info_modal_response(errorMsg);
        showModal("infoModalResponse", true);
        errorOccured = false;
        errorMsg = "";

        if ((modalShow === "true" || modalShow === true) &&
          !data.responseText.includes(
            "***VIEW***VIEW***VIEW***VIEW103VIEW***VIEW***VIEW***VIEW***"
          ) &&
          !data.responseText.includes(
            "***VIEW***VIEW***VIEW***VIEW101VIEW***VIEW***VIEW***VIEW***"
          )
        ) {
          modalAfterError = moadlShowId;
          trueAfterError = static;
        }

      } else {
        if ((modalShow === "true" || modalShow === true) &&
          !data.responseText.includes(
            "***VIEW***VIEW***VIEW***VIEW103VIEW***VIEW***VIEW***VIEW***"
          ) &&
          !data.responseText.includes(
            "***VIEW***VIEW***VIEW***VIEW101VIEW***VIEW***VIEW***VIEW***"
          )
        ) {
          showModal(moadlShowId, static);
        }
      }
      break;
  }
}

function nachAjaxButtonKonferenz(data,
  modalShow,
  modalHide,
  moadlShowId,
  moadlHideId,
  static) {
  var status = data.status; //  "begin", "complete" oder "success".

  switch (status) {
    case "begin":
      hideAllModals();
      showLoading();
      if (modalHide === "true" || modalHide === true) {
        hideModal(moadlHideId);
      }
      break;

    case "complete": // After the ajax response is arrived.
      break;

    case "success":
      hideLoading();
      if (errorOccured) {

        set_info_modal_response(errorMsg);
        showModal("infoModalResponse", true);
        errorOccured = false;
        errorMsg = "";


        if (modalShow === "true" || modalShow === true) {
          modalAfterError = moadlShowId;
          trueAfterError = static;
        }

      } else {
        if (modalShow === "true" || modalShow === true) {
          showModal(moadlShowId, static);
        }
      }
      window.scrollTo(0, 0);
      break;
  }
}

// Start aktuelle Jahreszahl
function setJahr() {
  var aktuellesJahr = new Date().getFullYear();
  var copyrightText = document.getElementById("copyright").innerHTML;
  var copyrightTextNeu = copyrightText.replace("jahr", aktuellesJahr);
  document.getElementById("copyright").innerHTML = copyrightTextNeu;
}

/* === Video === */
function endFullscreen() {
  if (document.exitFullscreen) {
    document.exitFullscreen();
  } else if (document.webkitExitFullscreen) {
    document.webkitExitFullscreen();
  } else if (document.mozCancelFullScreen) {
    document.mozCancelFullScreen();
  } else if (document.msExitFullscreen) {
    document.msExitFullscreen();
  }
}

// Hide Cookie Hint
function hide_cookie_hint() {
  var cookieHint = document.getElementById("cookieHint");
  cookieHint.style.display = "none";
}

function checkModalAfter() {
  if (modalAfterError !== "") {
    showModal(modalAfterError, trueAfterError);
  }
}

// Zeichen für Textarea zählen
function setVerbleibendeZeichen(event) {
  var textarea = event.target;
  var eingabe = textarea.value;
  var laenge = eingabe.length;
  document.getElementById("span-verbleibende-Zeichen").innerHTML = laenge;
}

function showLoading() {
  modal = document.getElementById('loading-modal');
  modal.style.display = "block";
}

function hideLoading() {
  modal = document.getElementById('loading-modal');
  modal.style.display = "none";
}

function hideHeader() {
  header = document.getElementById("header");
  header.style.height = "auto";
}