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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;

import org.primefaces.model.StreamedContent;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
@Deprecated
public class AControllerPdfshowSession implements Serializable {
    private static final long serialVersionUID = -1072228811792773733L;

    private String ident = "";

    private String key = "";

    private boolean anzeigen = false;

    private StreamedContent pdfZumAnzeigen = null;

    /*************Standard setter/getter********************************************/

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean isAnzeigen() {
        return anzeigen;
    }

    public void setAnzeigen(boolean anzeigen) {
        this.anzeigen = anzeigen;
    }

    public StreamedContent getPdfZumAnzeigen() {
        return pdfZumAnzeigen;
    }

    public void setPdfZumAnzeigen(StreamedContent pdfZumAnzeigen) {
        this.pdfZumAnzeigen = pdfZumAnzeigen;
    }

}
