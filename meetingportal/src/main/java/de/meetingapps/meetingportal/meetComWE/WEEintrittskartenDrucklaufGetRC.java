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
package de.meetingapps.meetingportal.meetComWE;

import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJurVersandadresse;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;

public class WEEintrittskartenDrucklaufGetRC extends WERootRC {

    private int drucklauf = 0;

    /**Anzahl der Sätze, die tatsächlich in den Arrays enthalten sind, also ohne die durch
     * Selektion herausgefallenen Null-Einträge*/
    private int anzahlSaetzeTatsaechlich = 0;

    private String ersterAktionaer = "";
    private int offsetErsterAktionaer = -1;
    private String letzterAktionaer = "";
    private int offsetLetzterAktionaer = -1;

    private EclAktienregister ergebnisArrayAktienregister[] = null;
    @Deprecated
    private EclAktienregisterLoginDaten ergebnisArrayAktienregisterLoginDaten[] = null;
    private EclLoginDaten ergebnisArrayLoginDaten[]=null;
    private EclLoginDaten ergebnisArrayLoginDaten1[]=null;
    private EclMeldung ergebnisArrayMeldung[] = null;
    private EclPersonenNatJur ergebnisArrayPersonenNatJur[] = null;
    private EclPersonenNatJurVersandadresse ergebnisArrayPersonenNatJurVersandadresse[] = null;
    private EclWillenserklaerung ergebnisArrayWillenserklaerung[] = null;
    private EclWillenserklaerungZusatz ergebnisArrayWillenserklaerungZusatz[] = null;
    private EclAnrede ergebnisArrayAnrede[] = null;
    private EclAnrede ergebnisArrayAnredeVersand[] = null;
    private EclStaaten ergebnisArrayStaaten[] = null;
    private EclStaaten ergebnisArrayStaaten1[] = null;
    private EclPersonenNatJur ergebnisArrayPersonenNatJur1[] = null;

    /*******************StandardGetter und Setter********************************/

    public EclMeldung[] getErgebnisArrayMeldung() {
        return ergebnisArrayMeldung;
    }

    public void setErgebnisArrayMeldung(EclMeldung[] ergebnisArrayMeldung) {
        this.ergebnisArrayMeldung = ergebnisArrayMeldung;
    }

    public EclPersonenNatJur[] getErgebnisArrayPersonenNatJur() {
        return ergebnisArrayPersonenNatJur;
    }

    public void setErgebnisArrayPersonenNatJur(EclPersonenNatJur[] ergebnisArrayPersonenNatJur) {
        this.ergebnisArrayPersonenNatJur = ergebnisArrayPersonenNatJur;
    }

    public EclWillenserklaerung[] getErgebnisArrayWillenserklaerung() {
        return ergebnisArrayWillenserklaerung;
    }

    public void setErgebnisArrayWillenserklaerung(EclWillenserklaerung[] ergebnisArrayWillenserklaerung) {
        this.ergebnisArrayWillenserklaerung = ergebnisArrayWillenserklaerung;
    }

    public EclWillenserklaerungZusatz[] getErgebnisArrayWillenserklaerungZusatz() {
        return ergebnisArrayWillenserklaerungZusatz;
    }

    public void setErgebnisArrayWillenserklaerungZusatz(
            EclWillenserklaerungZusatz[] ergebnisArrayWillenserklaerungZusatz) {
        this.ergebnisArrayWillenserklaerungZusatz = ergebnisArrayWillenserklaerungZusatz;
    }

    public EclPersonenNatJurVersandadresse[] getErgebnisArrayPersonenNatJurVersandadresse() {
        return ergebnisArrayPersonenNatJurVersandadresse;
    }

    public void setErgebnisArrayPersonenNatJurVersandadresse(
            EclPersonenNatJurVersandadresse[] ergebnisArrayPersonenNatJurVersandadresse) {
        this.ergebnisArrayPersonenNatJurVersandadresse = ergebnisArrayPersonenNatJurVersandadresse;
    }

    public EclAnrede[] getErgebnisArrayAnrede() {
        return ergebnisArrayAnrede;
    }

    public void setErgebnisArrayAnrede(EclAnrede[] ergebnisArrayAnrede) {
        this.ergebnisArrayAnrede = ergebnisArrayAnrede;
    }

    public EclStaaten[] getErgebnisArrayStaaten() {
        return ergebnisArrayStaaten;
    }

    public void setErgebnisArrayStaaten(EclStaaten[] ergebnisArrayStaaten) {
        this.ergebnisArrayStaaten = ergebnisArrayStaaten;
    }

    public EclStaaten[] getErgebnisArrayStaaten1() {
        return ergebnisArrayStaaten1;
    }

    public void setErgebnisArrayStaaten1(EclStaaten[] ergebnisArrayStaaten1) {
        this.ergebnisArrayStaaten1 = ergebnisArrayStaaten1;
    }

    public int getDrucklauf() {
        return drucklauf;
    }

    public void setDrucklauf(int drucklauf) {
        this.drucklauf = drucklauf;
    }

    public EclPersonenNatJur[] getErgebnisArrayPersonenNatJur1() {
        return ergebnisArrayPersonenNatJur1;
    }

    public void setErgebnisArrayPersonenNatJur1(EclPersonenNatJur[] ergebnisArrayPersonenNatJur1) {
        this.ergebnisArrayPersonenNatJur1 = ergebnisArrayPersonenNatJur1;
    }

    public EclAnrede[] getErgebnisArrayAnredeVersand() {
        return ergebnisArrayAnredeVersand;
    }

    public void setErgebnisArrayAnredeVersand(EclAnrede[] ergebnisArrayAnredeVersand) {
        this.ergebnisArrayAnredeVersand = ergebnisArrayAnredeVersand;
    }

    public EclAktienregister[] getErgebnisArrayAktienregister() {
        return ergebnisArrayAktienregister;
    }

    public void setErgebnisArrayAktienregister(EclAktienregister[] ergebnisArrayAktienregister) {
        this.ergebnisArrayAktienregister = ergebnisArrayAktienregister;
    }

    @Deprecated
    public EclAktienregisterLoginDaten[] getErgebnisArrayAktienregisterLoginDaten() {
        return ergebnisArrayAktienregisterLoginDaten;
    }

    @Deprecated
    public void setErgebnisArrayAktienregisterLoginDaten(
            EclAktienregisterLoginDaten[] ergebnisArrayAktienregisterLoginDaten) {
        this.ergebnisArrayAktienregisterLoginDaten = ergebnisArrayAktienregisterLoginDaten;
    }

    public String getErsterAktionaer() {
        return ersterAktionaer;
    }

    public void setErsterAktionaer(String ersterAktionaer) {
        this.ersterAktionaer = ersterAktionaer;
    }

    public String getLetzterAktionaer() {
        return letzterAktionaer;
    }

    public void setLetzterAktionaer(String letzterAktionaer) {
        this.letzterAktionaer = letzterAktionaer;
    }

    public int getOffsetErsterAktionaer() {
        return offsetErsterAktionaer;
    }

    public void setOffsetErsterAktionaer(int offsetErsterAktionaer) {
        this.offsetErsterAktionaer = offsetErsterAktionaer;
    }

    public int getOffsetLetzterAktionaer() {
        return offsetLetzterAktionaer;
    }

    public void setOffsetLetzterAktionaer(int offsetLetzterAktionaer) {
        this.offsetLetzterAktionaer = offsetLetzterAktionaer;
    }

    public int getAnzahlSaetzeTatsaechlich() {
        return anzahlSaetzeTatsaechlich;
    }

    public void setAnzahlSaetzeTatsaechlich(int anzahlSaetzeTatsaechlich) {
        this.anzahlSaetzeTatsaechlich = anzahlSaetzeTatsaechlich;
    }

    public EclLoginDaten[] getErgebnisArrayLoginDaten() {
        return ergebnisArrayLoginDaten;
    }

    public void setErgebnisArrayLoginDaten(EclLoginDaten[] ergebnisArrayLoginDaten) {
        this.ergebnisArrayLoginDaten = ergebnisArrayLoginDaten;
    }

    public EclLoginDaten[] getErgebnisArrayLoginDaten1() {
        return ergebnisArrayLoginDaten1;
    }

    public void setErgebnisArrayLoginDaten1(EclLoginDaten[] ergebnisArrayLoginDaten1) {
        this.ergebnisArrayLoginDaten1 = ergebnisArrayLoginDaten1;
    }

}
