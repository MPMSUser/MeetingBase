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
var jwt;
var timer;

async function openWebSocket(mode) {
    if (!webSocketconnected) {
        if (mode === 0) {
            jwt = jwtWsToken;
        } else if (mode === 1) {
            jwt = jwtWsTokenMitteilungen;
        }
        if (aktionaer && aktionaer !== '') {
            webSocketconnectedServerUrl = getServerPraefix() + "/meetingportal/" + webSocketName + "?jwt=" + jwt;
            connectToWebsocket();
        }
    }
}

var onOpen = function () {
    console.log('OPENED: Websocket');
    webSocketconnected = true;
    clearTimeout(timer);
};

var onClose = function () {
    console.log('CLOSED: Websocket');
    ws = null;
    reset();
    timer = setTimeout(() => {
        reconnect();
    }, 5000);
};

var onMessage = function (event) {
    decodeSocketCode(event.data);
};

var onError = function (event) {
    console.log("Connection Error: Websocket");
    ws.close();
};

var reset = function () {
    webSocketconnected = false;
};

function decodeSocketCode(code) {
    if (code.startsWith("*WortmeldungAktion")) {
        resetModals();
    }
    switch (code) {
        case "*WortmeldungAktionTS*":
            endFullscreen();
            showModal('socket-modal-wortmeldung-ts', "true");
            break;
        case "*WortmeldungAktionTV*":
            endFullscreen();
            showModal('socket-modal-wortmeldung-tv', "true");
            break;
        case "*WortmeldungAktionRS*":
            endFullscreen();
            showModal('socket-modal-wortmeldung-rs', "true");
            break;
        case "*WortmeldungAktionRV*":
            endFullscreen();
            showModal('socket-modal-wortmeldung-rv', "true");
            break;
        case "*WortmeldungAktionSP*":
            endFullscreen();
            showModal('socket-modal-wortmeldung-sp', "true");
            break;
        case "*WortmeldungAktionTVNOK*":
            endFullscreen();
            showModal('socket-modal-wortmeldung-tv-nok', "true");
            break;
        case "*WortmeldungAktionTestNichtErreicht*":
            endFullscreen();
            showModal('socket-modal-test-nicht-erreicht', "true");
            break;
        case "*WortmeldungAktionRedeNichtErreicht*":
            endFullscreen();
            showModal('socket-modal-rede-nicht-erreicht', "true");
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

function reconnect() {
    if (!webSocketconnected) {
        console.log("Reconnect to websocket");
        connectToWebsocket();
    }
}

function connectToWebsocket() {
    ws = new WebSocket(webSocketconnectedServerUrl);
    ws.onopen = onOpen;
    ws.onclose = onClose;
    ws.onmessage = onMessage;
    ws.onerror = onError;
}