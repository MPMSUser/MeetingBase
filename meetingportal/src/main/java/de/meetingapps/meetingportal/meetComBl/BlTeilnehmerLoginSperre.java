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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlTeilnehmerLoginSperre;
import de.meetingapps.meetingportal.meetComStub.WEStubBlTeilnehmerLoginSperreRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

/**Sperr- und Freigabe-Funktionen für Teilnehmer (Aktionäre und Gäste)*/
public class BlTeilnehmerLoginSperre extends StubRoot {

    
    public BlTeilnehmerLoginSperre(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /**1*/
    public int alle0BestaendeSperren() {
        if (verwendeWebService()) {
            WEStubBlTeilnehmerLoginSperre weStubBlTeilnehmerLoginSperre = new WEStubBlTeilnehmerLoginSperre();
            weStubBlTeilnehmerLoginSperre.stubFunktion = 1;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlTeilnehmerLoginSperre.setWeLoginVerify(weLoginVerify);

            WEStubBlTeilnehmerLoginSperreRC weStubBlTeilnehmerLoginSperreRC = wsClient.stubBlTeilnehmerLoginSperre(weStubBlTeilnehmerLoginSperre);

            if (weStubBlTeilnehmerLoginSperreRC.rc < 1) {
                return weStubBlTeilnehmerLoginSperreRC.rc;
            }

            return weStubBlTeilnehmerLoginSperreRC.rc;
        }
        dbOpen();
        lDbBundle.dbAktienregister.lese0Bestand();
        for (int i=0;i<lDbBundle.dbAktienregister.anzErgebnis();i++) {
            String hKennung=lDbBundle.dbAktienregister.ergebnisPosition(i).aktionaersnummer;
            setzeKennungSperrKZ(hKennung, 1);
        }
        
        dbClose();
        return 1;
    }
    
    /**2*/
    public int freigebenKennung(String pKennung) {
        if (verwendeWebService()) {
            WEStubBlTeilnehmerLoginSperre weStubBlTeilnehmerLoginSperre = new WEStubBlTeilnehmerLoginSperre();
            weStubBlTeilnehmerLoginSperre.stubFunktion = 2;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlTeilnehmerLoginSperre.setWeLoginVerify(weLoginVerify);
            weStubBlTeilnehmerLoginSperre.pKennung=pKennung;
            WEStubBlTeilnehmerLoginSperreRC weStubBlTeilnehmerLoginSperreRC = wsClient.stubBlTeilnehmerLoginSperre(weStubBlTeilnehmerLoginSperre);

            if (weStubBlTeilnehmerLoginSperreRC.rc < 1) {
                return weStubBlTeilnehmerLoginSperreRC.rc;
            }

            return weStubBlTeilnehmerLoginSperreRC.rc;
        }
        dbOpen();
        setzeKennungSperrKZ(pKennung, 0);
        dbClose();
        return 1;
    }
    
    /**3*/
    public int sperrenKennung(String pKennung) {
        if (verwendeWebService()) {
            WEStubBlTeilnehmerLoginSperre weStubBlTeilnehmerLoginSperre = new WEStubBlTeilnehmerLoginSperre();
            weStubBlTeilnehmerLoginSperre.stubFunktion = 3;

            WELoginVerify weLoginVerify = new WELoginVerify();
            weStubBlTeilnehmerLoginSperre.setWeLoginVerify(weLoginVerify);
            weStubBlTeilnehmerLoginSperre.pKennung=pKennung;
            WEStubBlTeilnehmerLoginSperreRC weStubBlTeilnehmerLoginSperreRC = wsClient.stubBlTeilnehmerLoginSperre(weStubBlTeilnehmerLoginSperre);

            if (weStubBlTeilnehmerLoginSperreRC.rc < 1) {
                return weStubBlTeilnehmerLoginSperreRC.rc;
            }

            return weStubBlTeilnehmerLoginSperreRC.rc;
        }
        dbOpen();
        setzeKennungSperrKZ(pKennung, 1);
        dbClose();
        return 1;
    }
    
    private int setzeKennungSperrKZ(String pKennung, int sperrKZ) {
        int rc=0;
        while (rc!=1) {/*Für den Fall, dass parallel gerade von anderem user upgedated wird*/
            lDbBundle.dbLoginDaten.read_loginKennung(pKennung);
            EclLoginDaten lLoginDaten=lDbBundle.dbLoginDaten.ergebnisPosition(0);
            lLoginDaten.anmeldenUnzulaessig=sperrKZ;
            rc=lDbBundle.dbLoginDaten.update(lLoginDaten);
        }
        
        return 1;
    }
}
