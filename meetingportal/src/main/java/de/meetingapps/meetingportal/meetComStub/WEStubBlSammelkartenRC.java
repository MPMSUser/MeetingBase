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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclKIAVFuerVollmachtDritte;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungMitAktionaersWeisung;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclVertreter;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComWE.WERootRC;

public class WEStubBlSammelkartenRC extends WERootRC {

    public LinkedList<EclKIAVFuerVollmachtDritte> eclKIAVFuerVollmachtDritte = null;

    public EclMeldung[] rcSammelMeldung = null;
    public EclZutrittskarten[][] rcZutrittskartenArray = null;
    public EclStimmkarten[][] rcStimmkartenArray = null;
    public EclWillensErklVollmachtenAnDritte[][] rcWillensErklVollmachtenAnDritteArray = null;

    public EclWeisungMeldung rcWeisungenSammelkopf = null;
    public EclWeisungMeldung rcAktionaersSummen = null;

    public List<EclMeldungMitAktionaersWeisung> aktionaereAktiv = null;
    public List<EclMeldungMitAktionaersWeisung> aktionaereInaktiv = null;
    public List<EclMeldungMitAktionaersWeisung> aktionaereWiderrufen = null;
    public List<EclMeldungMitAktionaersWeisung> aktionaereGeaendert = null;

    public List<EclVertreter> rcVertreterListe = null;

    public String rcEintrittskarte = "";

    public EclMeldung[][] rcSammelkartenFuerWeisungserfassung = null;
    public int[] rcSammelkartenFuerWeisungserfassungAnzahl = null;
    public int[] rcSammelkartenFuerWeisungserfassungMeldeIdentFuerStandard = null;
    public int[] rcSammelkartenFuerWeisungserfassungAusgewaehlteMeldeIdent = null;
    public int rcSammelkartenFuerWeisungAktuellerWegInternetOderPapierOderHV = 0;

}
