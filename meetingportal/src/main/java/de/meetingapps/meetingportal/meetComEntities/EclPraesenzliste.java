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

import de.meetingapps.meetingportal.meetComDb.DbBundle;

/** wird erzeugt durch dbjoined_readPraesenzliste*/
public class EclPraesenzliste {

    public int mandant;
    public long db_version;

    /**Für Präsenzliste (normal):
     * ==0 => wird aktuell in die Liste nicht gedruckt (z.B. wg. Offenlegung, doppelten Zu-/Abgängen o.ä.
     * 
     * Für Teilnehmerverzeichnis zu bestimmten Zeitpunkt:
     * */
    public int drucken = 1;

    public int willenserklaerungIdent = 0;

    public int meldeIdentAktionaer = 0;

    /**meldungstyp aus EclMeldung.
     * Achtung - trügerisch. Für einen Einzelaktionär gilt nämlich: der Meldungstyp kann mittlerweile (d.h. seit der tatsächlichen Präsenzbuchung)
     * von "in Sammelkarte" zu "Einzelaktionär" oder umgekehrt gewechselt haben!
     * D.h.: ob ein Aktionär zum "Präsenzbuchungszeitpunkt" in Sammelkarte war oder nicht, kann nicht hierüber ermittelt werden.
     * Sondern dafür muß sammelkartenIdent verwendet werden (wenn !=0, dann war die Präsenzbuchung zu diesem zeitpunkt in einer 
     * Sammelkarte).
     */
    public int meldungstyp = 0;

    /**Falls Aktionär selbst eine Sammelkarte ist, dann gibt dieses kennzeichen wieder ob mit oder ohne Offenlegung*/
    public int meldungSkOffenlegung = 0; //fuelleMeldungOffenlegungTatsaechlich
    /**falls Meldungstyp=2 Sammelkarte: (siehe: IntMeldungInSammelkarte)
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht*/
    public int meldungSkIst = 0;

    /**Aus Willenserklärung (Identifikation)*/
    public String zutrittsIdent = "";
    /**Aus Willenserklärung (Identifikation)*/
    public String stimmkarte = "";
    /**Aus Meldung (abweichend von zutrittsIdent, wenn Aktionär in Sammelkart!)*/
    public String zutrittsIdentMeldung = "";

    public String aktionaerName = "";
    public String aktionaerVorname = "";
    public String aktionaerOrt = "";
    public String aktionaersnummer = "";

    public long aktien = 0;
    public long stimmen = 0;
    public String besitzartKuerzel = "";
    public int gattung = 0;

    public int willenserklaerung = 0;
    public String veraenderungszeit = "";

    public int vertreterIdent = 0;
    public String vertreterName = "";
    public String vertreterVorname = "";
    public String vertreterOrt = "";

    /**Falls Aktionär in Sammelkartenenthalten ist, und es sich um eine Einbuchung/Ausbuchung in/aus Sammelkarte handelt,
     * für "nicht Offenlegung"
     */
    public int sammelkartenIdent = 0;
    public String sammelkartenName = "";
    public String sammelkartenVorname = "";
    public String sammelkartenOrt = "";
    public int skOffenlegung = 0;//fuelleOffenlegungTatsaechlich
    /**falls Meldungstyp=2 Sammelkarte: (siehe: IntMeldungInSammelkarte)
     *1=KIAV
     *2=SRV
     *3=organisatorisch
     *4=Briefwahl
     *5=Dauervollmacht*/
    public int skIst = 0;

    /**Zum Sortieren: je nach Offenlegung Aktionärsname oder Sammelkartenname*/
    public String sortierName = "";

    /**Werte auch Parameterstellugn aus, falls skOffenlegung==0*/
    //	public int liefereMeldungOffenlegungTatsaechlich(DbBundle pDbBundle) {
    /**Aufruf erst, nachdem meldungSkIst gefüllt ist!*/
    public void fuelleMeldungOffenlegungTatsaechlich(DbBundle pDbBundle, int pMeldungSkOffenlegung) {
        int hOffenlegung = pMeldungSkOffenlegung, hParameter = 0;
        if (hOffenlegung == 0) {
            switch (meldungSkIst) {
            case 1:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungKIAV;
                break;
            case 2:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungSRV;
                break;
            case 3:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungOrga;
                break;
            case 5:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungDauer;
                break;
            default:
                hParameter = 1;
                break;
            }
            if (hParameter == 1) {
                hOffenlegung = 1;
            } else {
                hOffenlegung = -1;
            }

        }
        meldungSkOffenlegung = hOffenlegung;
    }

    /**Werte auch Parameterstellugn aus, falls skOffenlegung==0*/
    public void fuelleOffenlegungTatsaechlich(DbBundle pDbBundle, int pSkOffenlegung) {
        int hOffenlegung = pSkOffenlegung, hParameter = 0;
        if (hOffenlegung == 0) {
            switch (skIst) {
            case 1:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungKIAV;
                break;
            case 2:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungSRV;
                break;
            case 3:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungOrga;
                break;
            case 5:
                hParameter = pDbBundle.param.paramAkkreditierung.skOffenlegungDauer;
                break;
            default:
                hParameter = 1;
                break;
            }
            if (hParameter == 1) {
                hOffenlegung = 1;
            } else {
                hOffenlegung = -1;
            }

        }
        skOffenlegung = hOffenlegung;
    }

}
