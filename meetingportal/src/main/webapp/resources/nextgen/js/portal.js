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
//setJahr();
//window.addEventListener('resize', setStickyFooter);
window.faces.ajax.addOnError(facesAjaxErrorHandler);

/**
 * Handles key press events.
 */
window.addEventListener('keypress', function (event) {
    var tagName = event.target.tagName;
    var type = event.target.type;
    var targetId = event.target.id;

    if (event.key == 'Enter') {
        if (tagName == 'INPUT' && type == 'text') {
            event.preventDefault();
        }
    }
})

function facesAjaxErrorHandler(errorData) {
    document.getElementById("loading-modal").style.display = "none";
    document.getElementById("loading-modal-error").style.display = "block";
}
