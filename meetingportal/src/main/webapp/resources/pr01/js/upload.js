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
var r;
var uploadFile;
var mode;

function getInternerDateiname(callback) {
    $.get("/meetingportal/api/v1/upload/getInternerDateinameku178Vollmachten", function (data, status) {
        internerDateiname = data.split("*#*")[0];
        console.log(internerDateiname);
        internerDateiPfad = data.split("*#*")[1];
        callback();
    });
}

function setResumable() {
    r = new Resumable({
        target: '/meetingportal/api/v1/upload/uploadVollmacht',
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
            internerDateiPfad: internerDateiPfad,
        }
    });
    if (!r.support) {
        $('.resumable-error').show();
    } else {
        $('.resumable-drop').show();
        r.assignDrop($('.resumable-drop')[0]);
        r.assignBrowse($('.resumable-browse')[0]);
        // Handle file add event
        r.on('fileAdded', function (file) {
            if(validateFile(file)) {
            uploadFile = file;
            document.getElementById('inhaltsform:uploadFilenameOriginalHidden').value = file.fileName;
            document.getElementById('inhaltsform:uploadFilenameInternHidden').value = internerDateiname;
            document.getElementById('inhaltsform:uploadFilesizeHidden').value = file.size;
            if (mode == 0) {
                document.getElementById('inhaltsform:btnVollmachtAbschickenUpload').click();
            } else {
                document.getElementById('inhaltsform:btnAbschickenUploadBriefwahl').click();
            }
        } else {
            r.cancel();
            set_info_modal("Bitte wählen Sie einen korrekten Dateityp.", 0);
            showModal('info-modal');
        }
        });
        r.on('pause', function () { });
        r.on('complete', function () {
            hideModal('upload-modal');
            // Hide pause/resume when the upload has completed
            $('.resumable-progress .progress-resume-link, .resumable-progress .progress-pause-link').hide();
        });
        r.on('fileSuccess', function (file, message) {
            $('#upload-modal-icon').addClass("fas fa-check-circle success");
            $('#upload-modal-icon').removeClass("fas fa-exclamation-circle error");
            $('.progress-bar').css({ width: '0' });
            hideModal('upload-modal');
            // Reflect that the file upload has completed
            $('.resumable-file-' + file.uniqueIdentifier + ' .resumable-file-progress').html('<i class="fas fa-check-circle success">');
        });
        r.on('fileError', function (file, message) {
            $('#upload-modal-icon').removeClass("fas fa-check-circle success");
            $('#upload-modal-icon').addClass("fas fa-exclamation-circle error");
            $('.progress-bar').css({ width: '0' });
            hideModal('upload-modal');
            // Reflect that the file upload has resulted in error
            $('.resumable-file-' + file.uniqueIdentifier + ' .resumable-file-progress').html('<i class="fas fa-exclamation-circle error">');
            $('.resumable-datei-label').text(file.fileName + ": " + message);
        });
        r.on('fileProgress', function (file) {
            // Handle progress for both the file and the overall upload
            $('.resumable-file-' + file.uniqueIdentifier + ' .resumable-file-progress').html(Math.floor(file.progress() * 100) + '%');
            $('.resumable-datei-label').text(file.fileName + ": " + Math.floor(file.progress() * 100) + '%');
            $('.progress-bar').css({ width: Math.floor(r.progress() * 100) + '%' });
        });
    }
}

function loadUpload() {
    console.log("loadUpload");
    getInternerDateiname(setResumable);

}

function setUploadFilenameIntern(original, intern) {
    var extension = original.split('.').pop();
    intern = intern + "_ORIG." + extension;
    return intern;
}

function nachAjaxButtonCheckFileUpload(data) {

    var status = data.status; //  "begin", "complete" oder "success".

    switch (status) {
        case "begin": // Before the ajax request is sent.
            showModal('loading-modal');
            break;

        case "complete": // After the ajax response is arrived.
            hideModal('loading-modal');
            break;

        case "success": // After update of HTML DOM based on ajax response.
            var error = "";
            var errorInput = !!document.getElementById('inhaltsform:uploadFileErrorHidden');
            if (errorInput) {
                error = document.getElementById('inhaltsform:uploadFileErrorHidden').value;
            } else {
                error = "error";
            }
            hideModal('loading-modal');
            if (error === "") {
                if (uploadFile !== null) {
                    showModal('upload-modal');
                    $('#upload-modal-icon').removeClass("fas fa-check-circle success");
                    $('#upload-modal-icon').removeClass("fas fa-exclamation-circle error");
                    // Show progress pabr
                    $('.resumable-progress, .resumable-list').show();
                    // Add the file to the list
                    $('.resumable-list').append('<li class="resumable-file-' + uploadFile.uniqueIdentifier + '"><span class="resumable-file-name"></span> <span class="resumable-file-progress"></span>');
                    $('.resumable-file-' + uploadFile.uniqueIdentifier + ' .resumable-file-name').html(uploadFile.fileName);
                    // Actually start the upload
                    r.upload();
                }
            } else if (error === "error") {
                r.cancel();
            } else {
                r.cancel();
                set_info_modal(error, 0);
                showModal('info-modal');
            }
            break;
    }
    
}



function validateFile(file) {
    // Erlaubte Endungen
    const allowedExtensions = [
        '.pdf',  // PDF Document
        '.jpg', '.jpeg', // JPEG Images
        '.tif', '.tiff', // TIFF Images
        '.png'   // PNG Images
    ];
    
    const fileName = file.fileName.toLowerCase();
    const extension = fileName.slice(fileName.lastIndexOf('.'));
    
    return allowedExtensions.includes(extension);
}
