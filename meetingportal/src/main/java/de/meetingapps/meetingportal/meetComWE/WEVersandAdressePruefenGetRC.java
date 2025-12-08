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
import de.meetingapps.meetingportal.meetComEntities.EclAnrede;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJurVersandadresse;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;

public class WEVersandAdressePruefenGetRC extends WERootRC {

    private EclAktienregister ergebnisArrayAktienregister[] = null;
    private EclMeldung ergebnisArrayMeldung[] = null;
    private EclPersonenNatJur ergebnisArrayPersonenNatJur[] = null;
    private EclPersonenNatJurVersandadresse ergebnisArrayPersonenNatJurVersandadresse[] = null;
    private EclWillenserklaerung ergebnisArrayWillenserklaerung[] = null;
    private EclWillenserklaerungZusatz ergebnisArrayWillenserklaerungZusatz[] = null;
    private EclAnrede ergebnisArrayAnrede[] = null;
    private EclAnrede ergebnisArrayAnredeVersand[] = null;
    private EclStaaten ergebnisArrayStaaten[] = null;
    private EclStaaten ergebnisArrayStaaten1[] = null;

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

}
