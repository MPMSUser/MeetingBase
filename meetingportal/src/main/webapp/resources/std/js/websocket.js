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
var ws = null;
var webSocketconnected = false;
var webSocketconnectedServerUrl;
var webSocketName = "wortmeldungSocket";
var aktionaer = '';
var jwtWsToken = '';
var jwtWsTokenMitteilungen = '';
var serverPraefix = '';

var jwtPull = '';
var pullEndpoint = serverPraefix + "/api/v1/wortmeldung/checkStatus";

async function openWebSocket(mode) {
    if (!webSocketconnected) {
        var jwt;
        if (mode === 0) {
            jwt = jwtWsToken;
        } else if (mode === 1) {
            jwt = jwtWsTokenMitteilungen;
        }
        jwtPull = jwt;
        if (aktionaer && aktionaer !== '') {
            webSocketconnectedServerUrl = getServerPraefix() + "/meetingportal/" + webSocketName + "?jwt=" + jwt;
            ws = new WebSocket(webSocketconnectedServerUrl);
            ws.onopen = onOpen;
            ws.onclose = onClose;
            ws.onmessage = onMessage;
            ws.onerror = onError;
        }
    }
}

var onOpen = function () {
    console.log('OPENED: ' + webSocketconnectedServerUrl);
    webSocketconnected = true;
};

var onClose = function () {
    console.log('CLOSED: ' + webSocketconnectedServerUrl);
    ws = null;
    reset();
};

var onMessage = function (event) {
    decodeSocketCode(event.data);
};

var onError = function (event) {
    console.log("Connection Error: " + webSocketconnectedServerUrl);
};

var reset = function () {
    webSocketconnected = false;
};

function decodeSocketCode(code) {
    console.log(code);
    if (code.startsWith("*WortmeldungAktion")) {
        resetModals();
    }
    if (code.startsWith("*Ping*::")) {
        setNewJwt();
    }
    switch (code) {
        case "*WortmeldungAktionTS*":
            showModal('socket-modal-wortmeldung-ts');
            break;
        case "*WortmeldungAktionTV*":
            showModal('socket-modal-wortmeldung-tv');
            break;
        case "*WortmeldungAktionRS*":
            showModal('socket-modal-wortmeldung-rs');
            break;
        case "*WortmeldungAktionRV*":
            showModal('socket-modal-wortmeldung-rv');
            break;
        case "*WortmeldungAktionSP*":
            showModal('socket-modal-wortmeldung-sp');
            break;
        case "*WortmeldungAktionTVNOK*":
            showModal('socket-modal-wortmeldung-tv-nok');
            break;
        case "*WortmeldungAktionTestNichtErreicht*":
            showModal('socket-modal-test-nicht-erreicht');
            break;
        case "*WortmeldungAktionRedeNichtErreicht*":
            showModal('socket-modal-rede-nicht-erreicht');
            break;
        case "*Ping*":
            ws.send('*Pong*');
            break;
        default:
            break;
    }
}

function getServerPraefix() {
    serverPraefix = serverPraefix.replace('http', 'ws');
    serverPraefix = serverPraefix.replace('https', 'wss');
    return serverPraefix;
}

function resetModals() {
    hideModal('socket-modal-wortmeldung-ts');
    hideModal('socket-modal-wortmeldung-tv');
    hideModal('socket-modal-wortmeldung-rs');
    hideModal('socket-modal-wortmeldung-rv');
    hideModal('socket-modal-wortmeldung-sp');
    hideModal('socket-modal-wortmeldung-tv-nok');
    hideModal('socket-modal-test-nicht-erreicht');
    hideModal('socket-modal-rede-nicht-erreicht');
}

function setNewJwt(code) {
    jwtWsToken = code.replace("*Ping*::", "");
    console.log(jwtWsToken);
}

function pullstatus() {
    
    const xhttp = new XMLHttpRequest();
    xhttp.open("GET", pullEndpoint + "/" + jwtPull);
    xhttp.send();
}