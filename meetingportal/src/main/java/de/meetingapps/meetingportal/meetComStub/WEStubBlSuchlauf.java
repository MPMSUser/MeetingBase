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

import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufDefinition;
import de.meetingapps.meetingportal.meetComEntities.EclSuchlaufErgebnis;
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubBlSuchlauf extends WERoot {

    /**Bisher vergebener Max-Wert = 4*/
    public int stubFunktion = 1;

    public int pVerwendung = 0;
    public String pParameter1 = "";
    public String pParameter2 = "";
    public String pParameter3 = "";
    public String pParameter4 = "";
    public String pParameter5 = "";

    /*Auch: eclSuchlaufDefinition*/
    public EclSuchlaufDefinition pSuchlaufDefinition = null;

    public int pAnzahl = 0;

    public boolean pMitReset = false;

    public List<EclSuchlaufErgebnis> suchlaufErgebnisAlleListe = null;
    public List<EclAktienregister> aktienregisterAlleListe = null;
    public List<EclMeldung> meldungAlleListe = null;

    public int aktienregisterOderMeldungen;
    public int nurAktionaereOderNurVertreterOderAktionaereVertreter;
    public boolean durchsuchenSammelkarten;
    public boolean durchsuchenInSammelkarten;
    public boolean durchsuchenGaeste;
    public boolean auchInaktiveAufnehmen;

    public String suchlaufBegriffe = null;

    /***********Standard Getter und Setter********************/

}
