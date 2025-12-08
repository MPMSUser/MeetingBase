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
package de.meetingapps.meetingportal.meetComHVParam;

import java.io.Serializable;

public class ParamGaesteModul implements Serializable {
    private static final long serialVersionUID = 9141722345603210663L;

    /**Steuert, welche Mails bei Ausstellung einer Gastkarte verschickt werden
     * (vorausgesetzt, eine Mail-Adresse ist eingegeben)
     * Werte mit "OR" verknüpft.
     *  0 / 1 => Mail mit Gastkarte als Anhang wird verschickt (automatisch)
     *  2 => Mail als "Bestätigung der Ausstellung" wird verschickt, ohne Anhang (automatisch)
     *  4 => Mail als "Bestätigung der Ausstellung" wird auf der Quittungsseite bereitgestellt (aber nicht automatisch verschickt)
     */
    public int mailVerschickenGK = 1; //tfMailVerschickenGK

    /**bccAdresse1, an die die Emails verschickt werden; Maximale Länge 2x40 Zeichen.
     * Ist in parameterLocal gespeichert*/
    public String bccAdresse1 = "";//tfBccAdresse1

    /**bccAdresse2, an die die Emails verschickt werden; Maximale Länge 2x40 Zeichen.
     * Ist in parameterLocal gespeichert*/
    public String bccAdresse2 = "";//tfBccAdresse2

    /**Felder anbieten?
     * 0 = nicht anbieten, 1= anbieten, 2=zwingend auszufüllen
     */
    public int feldAnredeVerwenden = 2;//tfFeldAnredeVerwenden
    public int feldTitelVerwenden = 1;//tfFeldTitelVerwenden
    public int feldAdelstitelVerwenden = 0;//tfFeldAdelstitelVerwenden
    public int feldNameVerwenden = 2;//tfFeldNameVerwenden
    public int feldVornameVerwenden = 1;//tfFeldVornameVerwenden
    public int feldZuHaendenVerwenden = 1;//tfFeldZuHaendenVerwenden
    public int feldZusatz1Verwenden = 0;//tfFeldZusatz1Verwenden
    public int feldZusatz2Verwenden = 0;//tfFeldZusatz2Verwenden
    public int feldStrasseVerwenden = 1;//tfFeldStrasseVerwenden
    public int feldLandVerwenden = 1;//tfFeldLandVerwenden
    public int feldPLZVerwenden = 1;//tfFeldPLZVerwenden
    public int feldOrtVerwenden = 2;//tfFeldOrtVerwenden
    public int feldMailadresseVerwenden = 1;//tfFeldMailadresseVerwenden
    public int feldKommunikationsspracheVerwenden = 0;//tfFeldKommunikationsspracheVerwenden
    public int feldGruppeVerwenden = 0;//tfFeldGruppeVerwenden
    public int feldAusstellungsgrundVerwenden = 1;//tfFeldAusstellungsgrundVerwenden
    public int feldVipVerwenden = 0;//tfFeldVipVerwenden

    /*AAAAA neuer Parameter Portal Logik Gastkarten*/
    public int feldAnredeVerwendenPortal = 2;//tfFeldAnredeVerwendenPortal 327
    public int feldTitelVerwendenPortal = 1;//tfFeldTitelVerwendenPortal 328
    public int feldAdelstitelVerwendenPortal = 0;//tfFeldAdelstitelVerwendenPortal 329
    public int feldNameVerwendenPortal = 2;//tfFeldNameVerwendenPortal 330
    public int feldVornameVerwendenPortal = 1;//tfFeldVornameVerwendenPortal 331 
    public int feldZuHaendenVerwendenPortal = 1;//tfFeldZuHaendenVerwendenPortal 332
    public int feldZusatz1VerwendenPortal = 0;//tfFeldZusatz1VerwendenPortal 333
    public int feldZusatz2VerwendenPortal = 0;//tfFeldZusatz2VerwendenPortal 334
    public int feldStrasseVerwendenPortal = 1;//tfFeldStrasseVerwendenPortal 335
    public int feldLandVerwendenPortal = 1;//tfFeldLandVerwendenPortal 336
    public int feldPLZVerwendenPortal = 1;//tfFeldPLZVerwendenPortal 337
    public int feldOrtVerwendenPortal = 2;//tfFeldOrtVerwendenPortal 338
    public int feldMailadresseVerwendenPortal = 1;//tfFeldMailadresseVerwendenPortal 339
    public int feldKommunikationsspracheVerwendenPortal = 0;//tfFeldKommunikationsspracheVerwendenPortal 340

    /**Parametrisierbare Feldbezeichnungen*/
    public String feldZuHaendenBezeichnung = "Firma";//tfFeldZuHaendenBezeichnung
    public String feldZusatz1Bezeichnung = "zu Händen";//tfFeldZusatz1Bezeichnung
    public String feldZusatz2Bezeichnung = "Zusatz 2";//tfFeldZusatz2Bezeichnung

    /**Anzubietende Buttons beim Anlegen der neuen Gastkarte - Button "Speichern"*/
    public int buttonSpeichernAnzeigen = 1; // tfButtonSpeichernAnzeigen//ku164: false
    /**Anzubietende Buttons beim Anlegen der neuen Gastkarte - Button "Speichern" umd Sofortdrucken*/
    public int buttonSpeichernDruckenAnzeigen = 1; // tfButtonSpeichernDruckenAnzeigen//ku164: false

}
