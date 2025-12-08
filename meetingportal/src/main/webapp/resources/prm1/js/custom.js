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
$(".menue-not-selected").has(".sub-menue>.row.menue-selected").addClass("menue-selected");

/* === Resize === */
function resizeDivQuad(divId) {
    const element = document.getElementById(divId);
    var width = element.offsetWidth;
    element.setAttribute("style", "height:" + width + "px");
}

/* === Before-Unload */

var logoutButtonClicked = false;
var vorherModal = null;

window.addEventListener("beforeunload", function(event) {

    let isLoggedIn = document.querySelector("#inhaltsform\\:schliessenId").value === "1";
    logoutButtonClicked = document.querySelector("#inhaltsform\\:logoutButtonClicked").value === "1";

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
            showModal('loading-modal');
            break;

        case "complete": // After the ajax response is arrived.
            break;

        case "success": // After update of HTML DOM based on ajax response.
            window.scrollTo(0, 0);
            hideModal('loading-modal');
            break;
    }
}

function nachAjaxButtonUmsaetze(data) {

    var status = data.status; //  "begin", "complete" oder "success".

    switch (status) {
        case "begin": // Before the ajax request is sent.
            showModal('loading-modal');
            break;

        case "complete": // After the ajax response is arrived.
            break;

        case "success": // After update of HTML DOM based on ajax response.
            window.scrollTo(0, 0);
            showHideDivUmsaetzeZeitraum();
            hideModal('loading-modal');
            break;
    }
}


function nachAjaxButtonShowModal(data, modal) {

    var status = data.status; //  "begin", "complete" oder "success".

    switch (status) {
        case "begin": // Before the ajax request is sent.
            break;

        case "complete": // After the ajax response is arrived.
            break;

        case "success": // After update of HTML DOM based on ajax response.
            showModal(modal);
            break;
    }
}

function nachAjaxButtonHideModal(data, modal) {

    var status = data.status; //  "begin", "complete" oder "success".

    switch (status) {
        case "begin": // Before the ajax request is sent.
            showModal('loading-modal');
            break;

        case "complete": // After the ajax response is arrived.
            break;

        case "success": // After update of HTML DOM based on ajax response.
            hideModal('loading-modal');

            //hideModal(modal);
            break;
    }
}

function nachAjaxButtonHideModal01(data, modal) {

    var status = data.status; //  "begin", "complete" oder "success".

    switch (status) {
        case "begin": // Before the ajax request is sent.
            showModal('loading-modal');
            break;

        case "complete": // After the ajax response is arrived.
            break;

        case "success": // After update of HTML DOM based on ajax response.
            hideModal('loading-modal');
            hideModal(modal);
            break;
    }
}

/* Führt einen Scroll zum Bereich des Streams aus.Muß nach dem Streamstart aufgerufen werden(anstelle nachAjaxButton) */
function nachAjaxButtonStreamstart(data) {
    const c = document.getElementById('streamform');
    window.scrollTo(0, c.scrollHeight);
}

/* === Start Modal === */
var modal;



function set_info_modal(modal_message_short, modal_message_long, fehler_anzeigen, typ) {

    //console.log(typ);
    //console.log(modal_message_long);

    if (fehler_anzeigen == "true") {
        $('#info-modal-message').html(modal_message_short);
    } else {
        $('#info-modal-message').html(modal_message_long);
    }

    switch (typ) {
        case "0":
            break;
        case "1":
            $('#info-modal-icon').removeClass("fas fa-check-circle success");
            $('#info-modal-icon').removeClass("fas fa-exclamation-circle warning");
            $('#info-modal-icon').removeClass("fas fa-info-circle");
            $('#info-modal-icon').addClass("fas fa-exclamation-circle error");
            break;
        case "2":
            $('#info-modal-icon').removeClass("fas fa-exclamation-circle warning");
            $('#info-modal-icon').removeClass("fas fa-exclamation-circle error");
            $('#info-modal-icon').removeClass("fas fa-info-circle");
            $('#info-modal-icon').addClass("fas fa-check-circle success");
            break;
        case "3":
            $('#info-modal-icon').removeClass("fas fa-check-circle success");
            $('#info-modal-icon').removeClass("fas fa-exclamation-circle error");
            $('#info-modal-icon').removeClass("fas fa-info-circle");
            $('#info-modal-icon').addClass("fas fa-exclamation-circle warning");
            break;
        case "4":
            $('#info-modal-icon').removeClass("fas fa-check-circle success");
            $('#info-modal-icon').removeClass("fas fa-exclamation-circle error");
            $('#info-modal-icon').removeClass("fas fa-exclamation-circle warning");
            $('#info-modal-icon').addClass("fas fa-info-circle");
            break;
        default:
            break;

    }
}

function set_info_modal_and_show(modal_message_short, modal_message_long, fehler_anzeigen, typ) {
    set_info_modal(modal_message_short, modal_message_long, fehler_anzeigen, typ);
    showModal('info-modal');
}

function showModal(modalName) {
    $("#" + modalName).removeClass("d-none");
    $("#" + modalName).addClass("d-block");
}

function hideModal(modalName) {
    $("#" + modalName).removeClass("d-block");
    $("#" + modalName).addClass("d-none");
}

/* === Ende Modal === */

/* === Passwort === */
function showPassword(id, spanClass) {
    var inputId = "inhaltsform:" + id;
    var input = document.getElementById(inputId);
    input.setAttribute("type", "text");
    $(".spanShowPassword" + spanClass).removeClass("d-inline");
    $(".spanShowPassword" + spanClass).addClass("d-none");
    $(".spanHidePassword" + spanClass).removeClass("d-none");
    $(".spanHidePassword" + spanClass).addClass("d-inline");
}

function hidePassword(id, spanClass) {
    var inputId = "inhaltsform:" + id;
    var input = document.getElementById(inputId);
    input.setAttribute("type", "password");
    $(".spanShowPassword" + spanClass).removeClass("d-none");
    $(".spanShowPassword" + spanClass).addClass("d-inline");
    $(".spanHidePassword" + spanClass).removeClass("d-inline");
    $(".spanHidePassword" + spanClass).addClass("d-none");
}

function showPassword0(id, spanClass) {
    var inputId = "inhaltsform:passwort0:" + id;
    var input = document.getElementById(inputId);
    input.setAttribute("type", "text");
    $(".spanShowPassword" + spanClass).removeClass("d-inline");
    $(".spanShowPassword" + spanClass).addClass("d-none");
    $(".spanHidePassword" + spanClass).removeClass("d-none");
    $(".spanHidePassword" + spanClass).addClass("d-inline");
}

function hidePassword0(id, spanClass) {
    var inputId = "inhaltsform:passwort0:" + id;
    var input = document.getElementById(inputId);
    input.setAttribute("type", "password");
    $(".spanShowPassword" + spanClass).removeClass("d-none");
    $(".spanShowPassword" + spanClass).addClass("d-inline");
    $(".spanHidePassword" + spanClass).removeClass("d-inline");
    $(".spanHidePassword" + spanClass).addClass("d-none");
}

//var sessionID = "#{eclTeilnehmerLoginM.anmeldeKennungAufbereitet}";
//var mandant = "#{eclParamM.mandantString}";


function setStickyFooter() {

    var heightFooter = document.getElementById('footer-bereich').offsetHeight;
    var inhalt = document.getElementById('inhalt');
    inhalt.style.paddingBottom = heightFooter + 50 + "px";
}

// Zeichen für Textarea zählen
function setVerbleibendeZeichen(event) {
    var textarea = event.target;
    var eingabe = textarea.value;
    var laenge = eingabe.length;
    document.getElementById('span-verbleibende-Zeichen').innerHTML = laenge;
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
            break;
    }
}

// Hide Cookie Hint
function hide_cookie_hint() {
    var cookieHint = document.getElementById('cookieHint');
    cookieHint.style.display = "none";
}

// Start mobile Menü
var mobile_nav_active = 0;

function mobile_menu() {
    if (mobile_nav_active === 0) {
        $('#mobile-nav-content').removeClass('d-none');
        mobile_nav_active = 1;
    } else {
        $('#mobile-nav-content').addClass('d-none');
        mobile_nav_active = 0;
    }
}

function hide_mobile_menu() {
    if (mobile_nav_active === 1) {
        $('#mobile-nav-content').addClass('d-none');
        mobile_nav_active = 0;
    }
}

function show_mobile_submenu(target) {
    var css_aktuell = $("#" + target + " .sub-menue").css("display");
    if (css_aktuell == "block") {
        $("#" + target + ">.row>.menue-not-selected>.col-7").css("text-decoration", "none");
        $("#" + target + " .sub-menue").css("display", "none");
    } else {
        $("#" + target + ">.row>.menue-not-selected>.col-7").css("text-decoration", "underline");
        $("#" + target + " .sub-menue").css("display", "block");
    }
}
// Ende mobile Menü

// Start Bankdaten

function checkBankdaten(data) {
    var status = data.status; //  "begin", "complete" oder "success".

    switch (status) {
        case "begin": // Before the ajax request is sent.
            break;

        case "complete": // After the ajax response is arrived.
            break;

        case "success": // After update of HTML DOM based on ajax response.
            checkBankdaten0();
            break;
    }
}

function checkBankdaten0() {
    var bankname = $('#txtAendBankname').val().trim();
    var bic = $('#txtAendBankBic').val().trim();

    if (bankname === '') {
        $('#txtAendBankname').prop('disabled', false);
    } else {
        $('#txtAendBankname').prop('disabled', true);
    }

    if (bic === '') {
        $('#txtAendBankBic').prop('disabled', false);
    } else {
        $('#txtAendBankBic').prop('disabled', true);
    }
}

// Umsätze Zeitraum

function showHideDivUmsaetzeZeitraum() {

    var checkedZeitraum = document.getElementById('inhaltsform:auswahlUmsatzzeitraum:0').checked;
    var x = window.matchMedia("(max-width: 768px)");

    if (checkedZeitraum) {
        if (x.matches) {
            $('#divUmsaetzeZeitraumMobil').css("display", "block");
        } else {
            $('#divUmsaetzeZeitraum').css("display", "block");
        }
    } else {
        if (x.matches) {
            $('#divUmsaetzeZeitraumMobil').css("display", "none");
        } else {
            $('#divUmsaetzeZeitraum').css("display", "none");
        }
    }
}

// Umsätze Drucken

function druckeUmsaetze() {
    var divContents = document.getElementById("umsaetze-druckbereich").innerHTML;
    var a = window.open('', '', 'height=500, width=500');
    a.document.write('<html>');
    a.document.write('<head>');
    a.document.write('<link rel="stylesheet" href="../jakarta.faces.resource/css/ku178_print.css.xhtml?ln=prm1&v=1.2.1">');
    a.document.write('</head>');
    a.document.write('<body>');
    a.document.write(divContents);
    a.document.write('</body></html>');
    a.document.close();
    a.focus();
}

// Start CO2Rechner


// Ende CO2Rechner

// Start Beteiligungserhoehung