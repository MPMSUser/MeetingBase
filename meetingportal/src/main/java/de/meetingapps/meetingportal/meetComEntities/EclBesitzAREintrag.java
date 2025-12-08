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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;
import java.util.List;

import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;

/**Eigener Aktienregistereintrag - mit zugeordneten Meldungen*/
public class EclBesitzAREintrag implements Serializable {
    private static final long serialVersionUID = -8170882225242428622L;

    public EclAktienregister aktienregisterEintrag = null;

    /**Anmeldungen Aktienregister*/
    public List<EclZugeordneteMeldungNeu> zugeordneteMeldungenListe = null;
    public int liefereAnzZugeordneteMeldungenEigeneAktien() {
        if (zugeordneteMeldungenListe==null) {return 0;}
        return zugeordneteMeldungenListe.size();
    }
    
    /**Anmeldungen - angeforderte Gäste*/
    public List<EclZugeordneteMeldungNeu> zugeordneteMeldungenGaesteListe = null;

    /**Dieser Aktienregistereintrag ist bereits angemeldet=true*/
    public boolean angemeldet = false;

    /**Portal: Dieser Aktienregistereintrag ist bereits angemeldet, und es sind nicht alle Anmeldungen ausgeblendet=true
     * Ist letztendlich der entscheidende Wert, ob die eingeloggte Person Aktionärsrechte hat.*/
    public boolean angemeldeteNichtAusgeblendeteVorhanden = false;

    /**Anzahl der Gastkarten, die von dieser Kennung aus angefordert wurden. Nur relevant,
     * wenn eigene Aktien dieser Kennung (nicht bei Instis)
     */
    public int gastKartenGemeldetEigeneAktien = 0;

    /*++++++Einzeldaten für Anzeige der Aktienregistereinträge Aktionär+++++++*/
    /**Aktienregisternummer*/
    public String aktienregisterNummer = "";
    
    /**Wird nur für ku310-Mandant benötigt*/
    public String liefereAktienregisterNummerku310() {
        if (aktienregisterNummer.length()<8) {return aktienregisterNummer;}
        String vorne=aktienregisterNummer.substring(0,7);
        String hinten=aktienregisterNummer.substring(7);
        return vorne+"-"+hinten;
    }

    /**aktionaerTitelVornameName*/
    public String aktionaerNameKomplett = "";

    /**AktionärOrt*/
    public String aktionaerOrt = "";

    /**Aktien*/
    public String aktionaerStimmenDE = "";
    public String aktionaerStimmenEN = "";

    /**Siehe KonstPortalTexte.IAUSWAHL_GATTUNG_AREINTRAG_* */
    public int textNrVorAktien=1228;
    
    public boolean bestandIstNullBestand() {
        return (aktienregisterEintrag.stueckAktien==(long)0);
    }
    
    public int liefereTextNrGattung() {
        int lGattung=aktienregisterEintrag.gattungId;
        switch (lGattung) {
        case 0:
        case 1:return KonstPortalTexte.ALLGEMEIN_GATTUNG1_BESITZ_VOR_AKTIEN;
        case 2:return KonstPortalTexte.ALLGEMEIN_GATTUNG2_BESITZ_VOR_AKTIEN;
        case 3:return KonstPortalTexte.ALLGEMEIN_GATTUNG3_BESITZ_VOR_AKTIEN;
        case 4:return KonstPortalTexte.ALLGEMEIN_GATTUNG4_BESITZ_VOR_AKTIEN;
        case 5:return KonstPortalTexte.ALLGEMEIN_GATTUNG5_BESITZ_VOR_AKTIEN;
        }
        return KonstPortalTexte.ALLGEMEIN_GATTUNG1_KENNUNG_VOR_AKTIEN;
    }
    
    /*************************Standard getter und setter************************************/


    public List<EclZugeordneteMeldungNeu> getZugeordneteMeldungenListe() {
        return zugeordneteMeldungenListe;
    }

    public void setZugeordneteMeldungenListe(List<EclZugeordneteMeldungNeu> zugeordneteMeldungenListe) {
        this.zugeordneteMeldungenListe = zugeordneteMeldungenListe;
    }

    public int getGastKartenGemeldetEigeneAktien() {
        return gastKartenGemeldetEigeneAktien;
    }

    public void setGastKartenGemeldetEigeneAktien(int gastKartenGemeldetEigeneAktien) {
        this.gastKartenGemeldetEigeneAktien = gastKartenGemeldetEigeneAktien;
    }

    public boolean isAngemeldet() {
        return angemeldet;
    }

    public void setAngemeldet(boolean angemeldet) {
        this.angemeldet = angemeldet;
    }

    public String getAktienregisterNummer() {
        return aktienregisterNummer;
    }

    public void setAktienregisterNummer(String aktienregisterNummer) {
        this.aktienregisterNummer = aktienregisterNummer;
    }

    public String getAktionaerNameKomplett() {
        return aktionaerNameKomplett;
    }

    public void setAktionaerNameKomplett(String aktionaerNameKomplett) {
        this.aktionaerNameKomplett = aktionaerNameKomplett;
    }

    public String getAktionaerOrt() {
        return aktionaerOrt;
    }

    public void setAktionaerOrt(String aktionaerOrt) {
        this.aktionaerOrt = aktionaerOrt;
    }

    public String getAktionaerStimmenDE() {
        return aktionaerStimmenDE;
    }

    public void setAktionaerStimmenDE(String aktionaerStimmenDE) {
        this.aktionaerStimmenDE = aktionaerStimmenDE;
    }

    public String getAktionaerStimmenEN() {
        return aktionaerStimmenEN;
    }

    public void setAktionaerStimmenEN(String aktionaerStimmenEN) {
        this.aktionaerStimmenEN = aktionaerStimmenEN;
    }

    public List<EclZugeordneteMeldungNeu> getZugeordneteMeldungenGaesteListe() {
        return zugeordneteMeldungenGaesteListe;
    }

    public void setZugeordneteMeldungenGaesteListe(List<EclZugeordneteMeldungNeu> zugeordneteMeldungenGaesteListe) {
        this.zugeordneteMeldungenGaesteListe = zugeordneteMeldungenGaesteListe;
    }
    public int getTextNrVorAktien() {
        return textNrVorAktien;
    }
    public void setTextNrVorAktien(int textNrVorAktien) {
        this.textNrVorAktien = textNrVorAktien;
    }

}
