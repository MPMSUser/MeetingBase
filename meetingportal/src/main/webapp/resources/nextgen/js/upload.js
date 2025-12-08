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
var internerDateiname;
var internerDateiPfad;
var resumable;
var uploadFile;

function getInternerDateiname(callback) {
    $.get("/meetingportal/api/v1/upload/getInternerDateiname", function (data, status) {
        internerDateiname = data.split("*#*")[0];
        internerDateiPfad = data.split("*#*")[1];
        callback();
    });
}

function setResumable() {
    resumable = new Resumable({
        target: '/meetingportal/api/v1/upload/uploadVideo',
        chunkSize: 1 * 1024 * 1024,
        simultaneousUploads: 4,
        throttleProgressCallbacks: 1,
        method: "octet",
        testChunks: false,
        fileType: fileTypesArray,
        maxFiles: fileAnzahlMax,
        maxFileSize: fileSizeMax,
        query: {
            internerDateiname: internerDateiname,
            internerDateiPfad: internerDateiPfad
        }
    });
    if (!resumable.support) {
        $('.resumable-error').show();
    } else {
        $('.resumable-drop').show();
        resumable.assignDrop($('.resumable-drop')[0]);
        resumable.assignBrowse($('.resumable-browse')[0]);
        // Handle file add event
        resumable.on('fileAdded', function (file) {
            uploadFile = file;
            document.getElementById('inhaltsform:iIBotschaftenEinreichen:uploadFilenameOriginalHidden').value = file.fileName;
            document.getElementById('inhaltsform:iIBotschaftenEinreichen:uploadFilenameInternHidden').value = internerDateiname;
            document.getElementById('inhaltsform:iIBotschaftenEinreichen:uploadFilesizeHidden').value = file.size;
            document.getElementById('inhaltsform:iIBotschaftenEinreichen:btnMitteilungAbschickenVideo').click();
        });
        resumable.on('pause', function () { });
        resumable.on('complete', function () {
            // Hide pause/resume when the upload has completed
            $('.resumable-progress .progress-resume-link, .resumable-progress .progress-pause-link').hide();
        });
        resumable.on('fileSuccess', function (file, message) {
            $('.progress-bar').css({ width: '0' });
            // Reflect that the file upload has completed
            $('.resumable-file-' + file.uniqueIdentifier + ' .resumable-file-progress').html('<i class="fas fa-check-circle success">');
        });
        resumable.on('fileError', function (file, message) {
            $('.progress-bar').css({ width: '0' });
            // Reflect that the file upload has resulted in error
            $('.resumable-file-' + file.uniqueIdentifier + ' .resumable-file-progress').html('<i class="fas fa-exclamation-circle error">');
            $('.resumable-datei-label').text(file.fileName + ": " + message);
        });
        resumable.on('fileProgress', function (file) {
            // Handle progress for both the file and the overall upload
            $('.resumable-file-' + file.uniqueIdentifier + ' .resumable-file-progress').html(Math.floor(file.progress() * 100) + '%');
            $('.resumable-datei-label').text(file.fileName + ": " + Math.floor(file.progress() * 100) + '%');
            $('.progress-bar').css({ width: Math.floor(resumable.progress() * 100) + '%' });
        });
    }
}

function loadUpload() {
    getInternerDateiname(setResumable);

}

function setUploadFilenameIntern(original, intern) {
    var extension = original.split('.').pop();
    intern = intern + "_ORIG." + extension;
    return intern;
}

function nachAjaxButtonCheckFileUpload(data,
    modalShow,
    modalHide,
    moadlShowId,
    moadlHideId,
    static) {

    var status = data.status; //  "begin", "complete" oder "success".

    switch (status) {
        case "begin":
            hideAllModals();
            if (modalHide === "true" || modalHide === true) {
                hideModal(moadlHideId);
            }
            break;

        case "complete": // After the ajax response is arrived.
            break;

        case "success": // After update of HTML DOM based on ajax response.
            var error = "";
            var errorInput = !!document.getElementById('inhaltsform:iIBotschaftenEinreichen:uploadFileErrorHidden');
            if (errorInput) {
                error = document.getElementById('inhaltsform:iIBotschaftenEinreichen:uploadFileErrorHidden').value;
            } else {
                error = "error";
            }
            if (error === "") {
                if (uploadFile !== null) {
                    // Show progress bar
                    $('.resumable-progress, .resumable-list').show();
                    // Add the file to the list
                    $('.resumable-list').append('<li class="resumable-file-' + uploadFile.uniqueIdentifier + '"><span class="resumable-file-name"></span> <span class="resumable-file-progress"></span>');
                    $('.resumable-file-' + uploadFile.uniqueIdentifier + ' .resumable-file-name').html(uploadFile.fileName);
                    // Actually start the upload
                    resumable.upload();
                }
            } else if (error === "error") {
                errorOccured = true;
                resumable.cancel();
            } else {
                errorOccured = true;
                resumable.cancel();
            }
            if (errorOccured) {
                set_info_modal_response(error);
                showModal("infoModalResponse", true);
                errorOccured = false;
                errorMsg = "";
                modalAfterError = moadlShowId;
                trueAfterError = true;
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