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
package de.meetingapps.meetingportal.meetComStub;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;

public class StubRoot {

    protected boolean istServer = false;
    protected DbBundle lDbBundle = null;
    protected WSClient wsClient = null;

    /**DbBundle muß existieren.
     * 	Beim Client: darf aber nicht geöffnet sein!
     * 	Beim Server: muß geöffnet sein!*/
    public StubRoot(boolean pIstServer, DbBundle pDbBundle) {
        istServer = pIstServer;
        lDbBundle = pDbBundle;
        if (!pIstServer) {
            wsClient = new WSClient();
        }
    }

    protected boolean verwendeWebService() {
        if (istServer == true) {
            return false;
        } //Server; immer auf Datenbank zugreifen
        if (ParamS.clGlobalVar.webServicePfadNr == -1) {
            return false;
        } //Kein Web-Service verfügbar => Datenbank
        if (ParamS.clGlobalVar.datenbankPfadNr == -1) {
            return true;
        } //Kein Datenbank -> immer Webservice

        if (ParamS.paramGeraet.dbVorrangig == false) {
            return true;
        }
        return false;

    }

    protected void dbOpen() {
        if (istServer == false) {
            lDbBundle.openAll();
        }
    }

    protected void dbOpenOhneParameterCheck() {
        if (istServer == false) {
            lDbBundle.openAllOhneParameterCheck();
        }
    }

    /*TODO: bei Stubs die Handhabung von openWeitere() untersuchen. Wird z.B. im WAIntern-Aufruf auch schon geöffnet -
     * und hier dann immer auch?
     */
    protected void dbOpenUndWeitere() {
        if (istServer == false) {
            lDbBundle.openAll();
        }
        lDbBundle.openWeitere(); //Immer öffnen, da ja vorher nur lDbBundle erfolgt
    }

    /**Eröffnet nur "Weitere"*/
    protected void dbOpenWeitere() {
        lDbBundle.openWeitere(); //Immer öffnen, da ja vorher nur lDbBundle erfolgt
    }

    /**Erforderlich, wenn die Parameter in den lokalen DbBundle neu übernommen werden sollen*/
    protected void dbOpenMitNew() {
        if (istServer == false) {
            lDbBundle = new DbBundle();
            lDbBundle.openAll();
        }
    }
    
    /**Erforderlich, wenn die Parameter in den lokalen DbBundle neu übernommen werden sollen*/
    protected void dbOpenUndWeitereMitNew() {
        if (istServer == false) {
            lDbBundle = new DbBundle();
            lDbBundle.openAll();
        }
        lDbBundle.openWeitere(); //Immer öffnen, da ja vorher nur lDbBundle erfolgt
    }

    
    protected void dbClose() {
        if (istServer == false) {
            lDbBundle.closeAll();
        }
    }

}
