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

import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAppZugeordneteKennungen;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungStatus;
import de.meetingapps.meetingportal.meetComEntities.EclZugeordneteMeldung;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

/**Enthält die Funktionen, die zur Kennungsverwaltung in der App
 * erforderlich sind. D.h die Eingaben bzgl. zugeordneten Kennungen
 * werden in der App gemacht, dann über diese klasse verifiziert bzw.
 * verwaltet, und das Ergebnis (tatsächliche, bereinigte Kennungen)
 * und die zugehörigen - gesammelten / konsoliderten! - Willenserklärungen
 * zurückgeliefert.
 * 
 * Ziel ist es, nach Neuzuordnungen in der App bzw. beim Abgleich in der App
 * möglichst wenig Verarbeitungslogik in der App zu haben. Ist auch die 
 * Voraussetzung dafür, z.B. einen Portalzugang mit mehreren - von 
 * verschiedenen Beständen "unterschiedlicher Personen" - zugeordneten
 * Besitzen anzubieten, z.B. für Banken oder Schutzvereinigungen
 * 
 * 
 * 
 * Gesamtablauf:
 * Die App schickt eine Liste aller zugeordneten Kennungen. Diese kann entweder "initial" sein,
 * oder bereits die Anmeldedetails enthalten (und damit ggf. doppelte Kennungen enthalten).
 * 
 * Nun passieren folgende Schritte:
 * > Gültigkeit der Kennungen überprüfen - ggf. "eliminieren"
 * > Doppelte entfernen
 * > Anmeldedetails löschen (mit Ausnahme der Person, die bei "Sind Sie" bestätigt wurde)
 * > Anmeldedetails füllen (dabei entstehen wieder doppelte Kennungen)
 * 
 * Achtung: bei Neuzuordnung von Kennungen in der App erst mit dieser Funktion die Meldedetails
 * ermitteln lassen, und dann die "neuzugeordneten" Kennungen mit "Sind Sie" abfragen.
 * Das ist aber Zukunftsmusik!
 * TODO #9 App - bereits beim Zuordnen Name abfragen und auf fehlende Vollmacht hinweisen!
 */
public class BlAppKennungsverwaltung {

    /**Ein- und Ausgabeparameter: alle Kennungen, die überprüft
     * werden sollen, bzw. die zusammen in einer App aktiv sind.
     */
    public EclAppZugeordneteKennungen[] zugeordneteKennungenArray = null;

    /**Ausgabeparameter nach holeMeldungsStatus(): alle Kennungen mit
     * zugeordneten Anmeldungen, bzw. restliche felder gefüllt*/
    public EclAppZugeordneteKennungen[] zugeordneteKennungenArrayNeu = null;

    /**Zwischenspeicher zum Aufbau, wird am Schluß in zugeordneteKennungenArrayNeu übertragen*/
    private List<EclAppZugeordneteKennungen> zugeordneteKennungenListeNeu = new ArrayList<EclAppZugeordneteKennungen>();

    /**Hier wird - nach blTeilnehmerLogin.pruefeZugeordneteKennung - der eingelesene
     * Aktienregistersatz zwischengespeichert, um doppelte Datenbankzugriffe
     * zu vermeiden. Achtung - nicht durchgängig gefüllt, sondern nur für die
     * Einträge, bei denen in zugeordneteKennungenArray eine Aktienregister-Kennung
     * enthalten ist
     */
    private EclAktienregister[] lAktienregisterArray = null;

    /**Hier wird - nach blTeilnehmerLogin.pruefeZugeordneteKennung - der eingelesene
     * Aktienregistersatz zwischengespeichert, um doppelte Datenbankzugriffe
     * zu vermeiden. Achtung - nicht durchgängig gefüllt, sondern nur für die
     * Einträge, bei denen in zugeordneteKennungenArray eine PersonNatJur-Kennung
     * enthalten ist
     */
    private EclPersonenNatJur[] lPersonNatJurArray = null;

    DbBundle dbBundle = null;

    public BlAppKennungsverwaltung(DbBundle pDbBundle) {
        dbBundle = pDbBundle;
    }

    /**Prüft alle Kennungen in zugeordneteKennungenListe ab, ob diese noch
     * verwendbar sind. Füllt gleichzeitig lAktienregisterArray und lPersonNatJurArray
     * zur späteren Verwendung (in holeMeldungsStatus)
     */
    public void pruefeObKennungenGueltig() {
        if (zugeordneteKennungenArray == null) {
            return;
        }

        lAktienregisterArray = new EclAktienregister[zugeordneteKennungenArray.length];
        lPersonNatJurArray = new EclPersonenNatJur[zugeordneteKennungenArray.length];

        for (int i = 0; i < zugeordneteKennungenArray.length; i++) {
            BlTeilnehmerLogin blTeilnehmerLogin = new BlTeilnehmerLogin();
            blTeilnehmerLogin.initDB(dbBundle);
            blTeilnehmerLogin.pruefeZugeordneteKennung(zugeordneteKennungenArray[i]);
            switch (zugeordneteKennungenArray[i].anmeldeKennungArt) {
            case 1: {/*Aktienregister*/
                lAktienregisterArray[i] = blTeilnehmerLogin.ergebnisAktienregister;
                break;
            }
            case 3: {/*PersonNatJur*/
                lPersonNatJurArray[i] = blTeilnehmerLogin.ergebnisPersonNatJur;
                break;
            }
            }

        }
    }

    /**"Schmeißt" doppelte Kennungen raus. Vorher pruefeObKennungenGueltig
     * aufrufen, damit bei doppelten ggf. die mit dem ungültigen Passwort
     * rausgeschmissen wird!
     * 
     *  Fälle:
     *  0/0/0 => 0/-3/-3
     *  0/-2/0 => 0/-3/-3
     *  -2/0/0 => -3/0/0 => -3/0/-3
     */
    public void eliminiereDuplettenInKennungen() {
        if (zugeordneteKennungenArray == null) {
            return;
        }
        for (int i = 0; i < zugeordneteKennungenArray.length - 1; i++) {
            /*Vergleichen mit allen anderen Elementen*/
            for (int i1 = i + 1; i1 < zugeordneteKennungenArray.length; i1++) {
                if (zugeordneteKennungenArray[i].kennung.compareTo(zugeordneteKennungenArray[i1].kennung) == 0) {
                    /*Kennungen sind gleich*/
                    if (zugeordneteKennungenArray[i].returnVerarbeitung == 0
                            && zugeordneteKennungenArray[i1].returnVerarbeitung == 0) {
                        /*Bisher beide fehlerfrei*/
                        zugeordneteKennungenArray[i1].returnVerarbeitung = -3; /*"Spätere" auf doppelt setzen, da sonst "mehrere Dupletten" nicht richtig verarbeitet würden*/
                    } else {
                        /*Mindestens eine von beiden hat einen Fehler*/
                        if (zugeordneteKennungenArray[i].returnVerarbeitung == -3
                                || zugeordneteKennungenArray[i1].returnVerarbeitung == -3) {
                            /*Mindestens eine bereits eliminiert - nichts tun, andere bleibt ggf.erhalten*/

                        } else {
                            if (zugeordneteKennungenArray[i].returnVerarbeitung != 0) {
                                /*1. hat Fehler - 2. erhalten*/
                                zugeordneteKennungenArray[i].returnVerarbeitung = -3;
                            } else {
                                zugeordneteKennungenArray[i1].returnVerarbeitung = -3;
                            }
                        }
                    }
                }
            }

        }

    }

    /**Für alle zugeordneten Kennungen in zugeordneteKennungenListe: ergänze Anmeldestatus.
     * 
     * -------------------------------------------
     * Gedanken Zu "Mehrfachauftretungen:
     * Gleiche PersonNatJur bzw. AktienregisterIdent etc. wurden / müssen vorher eliminiert werden.
     * 
     * Fall 1: Aktienregistereintrag - eigener Besitz:
     * > EK auf Selbst (nur eine) - EK eindeutig.
     * > auf Bevollmächtigte - wird hier nicht ausgewiesen
     * > zugeordnete Gastkarten auf diese Ident --> diese könnten auch direkt eingescannt werden? 
     * 		Nein - da bei Zuordnung von Gastkarten zu bestehenden PersonNatJur keine neuen 
     * 		Zugangsdaten vergeben werden
     * > Bevollmächtigt zugeordnete - diese könnten auch direkt eingescannt werden?
     * 		Nein - da bei Vollmachten an bestehenden PersonNatJur keine neuen 
     * 		Zugangsdaten vergeben werden
     * 
     * Fall 2: PersonNatJur
     * > Gastkarte - ok
     * > Eigene EK (bei Inhaberaktien!) - wird gelöst über Zugangsdaten zu Aktionär und ausgestellter Eigener EK
     * > EK auf PersonNatJur als Bevollmächtigten
     * 		Hier können Dupletten auftreten! Aktionär stellt EK auf Sich aus (EK A), und bevollmächtigt sich (EK B), 
     * 		und bevollmächtigt damit wieder sich (EK C) und scannt alle 2 zusätzlichen Karten ein.
     * 
     * TODO #APP - Beim Frontoffice abfangen, dass die App mehrere verschiedene EKs anzeigt!
     * 
     * Fazit: keine Bereinigung durchführen!
     * Vorgehen beim "Auswählen" und Willenserklärungen bearbeiten: Kennung verwenden, und
     * schlimmstenfalls alle gehörigen Bearbeiten - besser nur den ausgewählten Besitz
     * 
     *
     */
    public void holeMeldungsStatus() {
        if (zugeordneteKennungenArray == null) {
            zugeordneteKennungenArrayNeu = null;
            return;
        }

        for (int i = 0; i < zugeordneteKennungenArray.length; i++) {

            EclAppZugeordneteKennungen appZugeordneteKennung = zugeordneteKennungenArray[i];

            /*Anmeldungsdaten zurücksetzen*/
            leereMeldungsTeilAusKennung(appZugeordneteKennung);

            if (appZugeordneteKennung.returnVerarbeitung >= 0) {

                BlWillenserklaerungStatus lWillenserklaerungStatus = new BlWillenserklaerungStatus(dbBundle);

                /*Meldungen überprüfen und ggf. hinzufügen*/
                switch (appZugeordneteKennung.anmeldeKennungArt) {
                case 1: {/*Aktienregister*/
                    lWillenserklaerungStatus
                            .leseMeldungenZuAktienregisterIdent(appZugeordneteKennung.aktienregisterIdent);
                    appZugeordneteKennung.personNatJurIdent = lWillenserklaerungStatus.aktienregisterPersonNatJurIdent; /*Kann auch 0 sein, z.B. wenn keine Meldung*/
                    if (appZugeordneteKennung.personNatJurIdent != 0) {
                        lWillenserklaerungStatus
                                .leseMeldungenEigeneGastkartenZuPersonNatJur(appZugeordneteKennung.personNatJurIdent);
                        lWillenserklaerungStatus
                                .leseMeldungenBevollmaechtigtZuPersonNatJur(appZugeordneteKennung.personNatJurIdent);
                        lWillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(
                                appZugeordneteKennung.personNatJurIdent);
                    }

                    /*Eigene Anmeldungen durchrennen*/
                    if (lAktienregisterArray[i].stimmen == 0) {/*0-Bestand*/
                        leereMeldungsTeilAusKennung(appZugeordneteKennung);
                        appZugeordneteKennung.vertretungsArt = 32;
                        appZugeordneteKennung.aktionaer = lAktienregisterArray[i].nameKomplett + ", "
                                + lAktienregisterArray[i].ort;
                        addZugeordneteKennungZuNeu(appZugeordneteKennung);
                    } else {
                        int eigenerBestandAngemeldet = 0;
                        /*Im folgenden if war ursprünglich ||. Das macht aber keinen Sinn - denn wenn die erste Bedingung nicht erfüllt ist, d.h. =null, dann crashed die zweite Bedingung...*/
                        if (lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray != null
                                && lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length != 0) {
                            for (int i1 = 0; i1 < lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length; i1++) {
                                EclZugeordneteMeldung zugeordneteMeldung = lWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i1];
                                if (zugeordneteMeldung.klasse == 1) {/*Ansonsten Gast - verteilte Gäste werden hier nicht separat angezeigt*/
                                    leereMeldungsTeilAusKennung(appZugeordneteKennung);
                                    appZugeordneteKennung.aktionaer = lAktienregisterArray[i].nameKomplett + ", "
                                            + lAktienregisterArray[i].ort;
                                    appZugeordneteKennung.stimmen = zugeordneteMeldung.stueckAktien; /*Geändert am 26.01.2019 - vorher wurde auch bei Meldungen immer der Aktienregistereintrag eingezeigt*/
                                    /*lAktienregisterArray[i].stimmen;*/
                                    appZugeordneteKennung.vertretungsArt = 1; /*Auf jeden Fall angemeldet*/
                                    appZugeordneteKennung.gattungNummer = lAktienregisterArray[i].getGattungId();
                                    appZugeordneteKennung.gattungBezeichnung = dbBundle.param.paramBasis
                                            .getGattungBezeichnung(lAktienregisterArray[i].getGattungId());

                                    /*Anmeldeausprägungen bestimmen und Eintragen*/
                                    if (zugeordneteMeldung.anzVollmachtenDritte > 0
                                            || zugeordneteMeldung.anzZutrittsIdentVollmacht > 0) {
                                        appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                | 4);
                                        appZugeordneteKennung.delegationArt = (appZugeordneteKennung.delegationArt | 1);
                                    }
                                    if (zugeordneteMeldung.anzSRV > 0) {
                                        appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                | 4);
                                        appZugeordneteKennung.delegationArt = (appZugeordneteKennung.delegationArt | 2);
                                    }
                                    if (zugeordneteMeldung.anzKIAV > 0) {
                                        appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                | 4);
                                        appZugeordneteKennung.delegationArt = (appZugeordneteKennung.delegationArt | 4);
                                    }
                                    if (zugeordneteMeldung.anzBriefwahl > 0) {
                                        appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                | 4);
                                        appZugeordneteKennung.delegationArt = (appZugeordneteKennung.delegationArt | 8);
                                    }

                                    /*Nun Willenserklärungen durchrennen, um ggf. Eintrittskarten "Selbst" zufüllen*/
                                    if (zugeordneteMeldung.anzZutrittsIdentSelbst > 0) {
                                        appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                | 2);
                                        for (int i3 = 0; i3 < zugeordneteMeldung.zugeordneteWillenserklaerungenList
                                                .size(); i3++) {
                                            EclWillenserklaerungStatus zugeordneteWillenserklaerungStatus = zugeordneteMeldung.zugeordneteWillenserklaerungenList
                                                    .get(i3);
                                            if (!zugeordneteWillenserklaerungStatus.storniert) {
                                                if (zugeordneteWillenserklaerungStatus.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung) {
                                                    appZugeordneteKennung.zutrittsIdent = zugeordneteWillenserklaerungStatus.zutrittsIdent;
                                                    appZugeordneteKennung.zutrittsIdentNeben = zugeordneteWillenserklaerungStatus.zutrittsIdentNeben;
                                                }
                                            }
                                        }
                                    }

                                    /*Anmeldung in Ergebnis eintragen*/
                                    addZugeordneteKennungZuNeu(appZugeordneteKennung);
                                    eigenerBestandAngemeldet = 1;
                                }
                            }
                        }
                        if (eigenerBestandAngemeldet == 0) {
                            leereMeldungsTeilAusKennung(appZugeordneteKennung);
                            appZugeordneteKennung.vertretungsArt = 0;
                            appZugeordneteKennung.aktionaer = lAktienregisterArray[i].nameKomplett + ", "
                                    + lAktienregisterArray[i].ort;
                            appZugeordneteKennung.stimmen = lAktienregisterArray[i].stimmen;
                            addZugeordneteKennungZuNeu(appZugeordneteKennung);
                        }
                    }
                    break;
                }
                case 3: {/*PersonNatJur*/
                    /**TODO #Reengineering - leseMeldungenEigeneAktienZuPersonNatJur eigentlich unnötig - was sind das für Fälle?*/
                    lWillenserklaerungStatus
                            .leseMeldungenEigeneAktienZuPersonNatJur(appZugeordneteKennung.aktienregisterIdent);

                    lWillenserklaerungStatus
                            .leseMeldungenEigeneGastkartenZuPersonNatJur(appZugeordneteKennung.personNatJurIdent);
                    lWillenserklaerungStatus
                            .leseMeldungenBevollmaechtigtZuPersonNatJur(appZugeordneteKennung.personNatJurIdent);
                    lWillenserklaerungStatus
                            .ergaenzeZugeordneteMeldungenUmWillenserklaerungen(appZugeordneteKennung.personNatJurIdent);

                    break;
                }
                }

                /*Nun erhaltene Vollmachten und Eigene Gastkarten - identisch für Aktienregister und PersonNatJur*/
                int eigeneZutrittsIdentAngemeldet = 0; /*für Kennung nur mit PersonNatJur - zum Feststellen zum Schluß,
                                                       dass keine eigene karte zur Verfügung steht*/

                /*Eigene erhaltene Vollmachten durchrennen*/
                if (lWillenserklaerungStatus.zugeordneteMeldungenBevollmaechtigtArray != null
                        && lWillenserklaerungStatus.zugeordneteMeldungenBevollmaechtigtArray.length != 0) {
                    for (int i1 = 0; i1 < lWillenserklaerungStatus.zugeordneteMeldungenBevollmaechtigtArray.length; i1++) {
                        EclZugeordneteMeldung zugeordneteMeldung = lWillenserklaerungStatus.zugeordneteMeldungenBevollmaechtigtArray[i1];
                        leereMeldungsTeilAusKennung(appZugeordneteKennung);
                        appZugeordneteKennung.aktionaer = zugeordneteMeldung.aktionaerNameVornameTitel + ", "
                                + zugeordneteMeldung.aktionaerOrt;
                        appZugeordneteKennung.stimmen = zugeordneteMeldung.aktionaerStimmen;
                        appZugeordneteKennung.vertretungsArt = 1; /*Auf jeden Fall angemeldet*/
                        appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                | 64); /*Erhaltene Vollmacht*/
                        /*Beim Durchrennen rausfinden: KIAV/Briefwahl/Vollmacht von dieser PersonNatJur weiter erteilt (delegiert)
                         * 								EK*/
                        for (int i3 = 0; i3 < zugeordneteMeldung.zugeordneteWillenserklaerungenList.size(); i3++) {
                            EclWillenserklaerungStatus zugeordneteWillenserklaerungStatus = zugeordneteMeldung.zugeordneteWillenserklaerungenList
                                    .get(i3);
                            if (!zugeordneteWillenserklaerungStatus.storniert) {
                                if (zugeordneteWillenserklaerungStatus.willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung_VollmachtAnDritte) {
                                    if (zugeordneteWillenserklaerungStatus.bevollmaechtigterDritterIdent == appZugeordneteKennung.personNatJurIdent) {
                                        appZugeordneteKennung.zutrittsIdent = zugeordneteWillenserklaerungStatus.zutrittsIdent;
                                        appZugeordneteKennung.zutrittsIdentNeben = zugeordneteWillenserklaerungStatus.zutrittsIdentNeben;
                                    }
                                } else {
                                    if (zugeordneteWillenserklaerungStatus.willenserklaerungGeberIdent == appZugeordneteKennung.personNatJurIdent) {
                                        switch (zugeordneteWillenserklaerungStatus.willenserklaerung) {
                                        case KonstWillenserklaerung.vollmachtAnDritte: {
                                            appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                    | 4);
                                            appZugeordneteKennung.delegationArt = (appZugeordneteKennung.delegationArt
                                                    | 1);
                                            break;
                                        }
                                        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV: {
                                            appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                    | 4);
                                            appZugeordneteKennung.delegationArt = (appZugeordneteKennung.delegationArt
                                                    | 2);
                                            break;
                                        }
                                        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV: {
                                            appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                    | 4);
                                            appZugeordneteKennung.delegationArt = (appZugeordneteKennung.delegationArt
                                                    | 4);
                                            break;
                                        }
                                        case KonstWillenserklaerung.dauervollmachtAnKIAV: {
                                            appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                    | 4);
                                            appZugeordneteKennung.delegationArt = (appZugeordneteKennung.delegationArt
                                                    | 4);
                                            break;
                                        }
                                        case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte: {
                                            appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                    | 4);
                                            appZugeordneteKennung.delegationArt = (appZugeordneteKennung.delegationArt
                                                    | 4);
                                            break;
                                        }
                                        case KonstWillenserklaerung.briefwahl: {
                                            appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                                    | 4);
                                            appZugeordneteKennung.delegationArt = (appZugeordneteKennung.delegationArt
                                                    | 8);
                                            break;
                                        }
                                        }

                                    }
                                }
                            }
                        }
                        eigeneZutrittsIdentAngemeldet = 1;
                        addZugeordneteKennungZuNeu(appZugeordneteKennung);
                    }
                }

                /*Eigene Gastkarten durchrennen*/
                if (lWillenserklaerungStatus.zugeordneteMeldungenEigeneGastkartenArray != null
                        && lWillenserklaerungStatus.zugeordneteMeldungenEigeneGastkartenArray.length != 0) {
                    for (int i1 = 0; i1 < lWillenserklaerungStatus.zugeordneteMeldungenEigeneGastkartenArray.length; i1++) {
                        EclZugeordneteMeldung zugeordneteMeldung = lWillenserklaerungStatus.zugeordneteMeldungenEigeneGastkartenArray[i1];
                        leereMeldungsTeilAusKennung(appZugeordneteKennung);
                        appZugeordneteKennung.aktionaer = zugeordneteMeldung.aktionaerNameVornameTitel + ", "
                                + zugeordneteMeldung.aktionaerOrt;
                        appZugeordneteKennung.stimmen = 0;
                        appZugeordneteKennung.vertretungsArt = 1; /*Auf jeden Fall angemeldet*/
                        appZugeordneteKennung.vertretungsArt = (appZugeordneteKennung.vertretungsArt
                                | 16); /*Eigene Gastkarte*/
                        for (int i3 = 0; i3 < zugeordneteMeldung.zugeordneteWillenserklaerungenList.size(); i3++) {
                            EclWillenserklaerungStatus zugeordneteWillenserklaerungStatus = zugeordneteMeldung.zugeordneteWillenserklaerungenList
                                    .get(i3);
                            if (!zugeordneteWillenserklaerungStatus.storniert) {
                                if (zugeordneteWillenserklaerungStatus.willenserklaerung == KonstWillenserklaerung.anmeldungGast) {
                                    appZugeordneteKennung.zutrittsIdent = zugeordneteWillenserklaerungStatus.zutrittsIdent;
                                    appZugeordneteKennung.zutrittsIdentNeben = zugeordneteWillenserklaerungStatus.zutrittsIdentNeben;
                                }
                            }
                        }
                        eigeneZutrittsIdentAngemeldet = 1;
                        addZugeordneteKennungZuNeu(appZugeordneteKennung);
                    }
                }

                /*Falls keine ZutrittsIdent gefunden, bei PersonNatJur "Leer-Satz" anhängen*/
                if (eigeneZutrittsIdentAngemeldet == 0 && appZugeordneteKennung.anmeldeKennungArt == 3) {
                    leereMeldungsTeilAusKennung(appZugeordneteKennung);
                    appZugeordneteKennung.vertretungsArt = 0;
                    /*zu appZugeordneteKennung.aktionaer: Wurde ja nicht überschrieben - müßte deshalb die PersonNatJur Name enthalten*/
                    appZugeordneteKennung.stimmen = lAktienregisterArray[i].stimmen;
                    addZugeordneteKennungZuNeu(appZugeordneteKennung);
                }

            } else {
                /*Kennung nicht mehr gültig - einfach anhängen ohne Meldedaten*/
                zugeordneteKennungenListeNeu.add(appZugeordneteKennung);
            }

        }
        /*Nun noch in zugeordneteKennungenArrayNeu übertragen*/
        zugeordneteKennungenArrayNeu = new EclAppZugeordneteKennungen[zugeordneteKennungenListeNeu.size()];
        for (int i = 0; i < zugeordneteKennungenListeNeu.size(); i++) {
            zugeordneteKennungenArrayNeu[i] = zugeordneteKennungenListeNeu.get(i);
        }
    }

    private void addZugeordneteKennungZuNeu(EclAppZugeordneteKennungen pZugeordneteKennung) {
        EclAppZugeordneteKennungen lZugeordneteKennungNeu = new EclAppZugeordneteKennungen();
        lZugeordneteKennungNeu.interneIdent = 0; /*Muß in der App dann neu vergeben werden*/
        lZugeordneteKennungNeu.mandant = pZugeordneteKennung.mandant;
        lZugeordneteKennungNeu.anmeldeKennungArt = pZugeordneteKennung.anmeldeKennungArt;
        lZugeordneteKennungNeu.kennung = pZugeordneteKennung.kennung;
        lZugeordneteKennungNeu.passwort = pZugeordneteKennung.passwort;
        lZugeordneteKennungNeu.aktienregisterIdent = pZugeordneteKennung.aktienregisterIdent;
        lZugeordneteKennungNeu.personNatJurIdent = pZugeordneteKennung.personNatJurIdent;
        lZugeordneteKennungNeu.vertretungsArt = pZugeordneteKennung.vertretungsArt;
        lZugeordneteKennungNeu.delegationArt = pZugeordneteKennung.delegationArt;
        lZugeordneteKennungNeu.aktionaer = pZugeordneteKennung.aktionaer;
        lZugeordneteKennungNeu.stimmen = pZugeordneteKennung.stimmen;
        lZugeordneteKennungNeu.gattungNummer = pZugeordneteKennung.gattungNummer;
        lZugeordneteKennungNeu.gattungBezeichnung = pZugeordneteKennung.gattungBezeichnung;
        lZugeordneteKennungNeu.zutrittsIdent = pZugeordneteKennung.zutrittsIdent;
        lZugeordneteKennungNeu.zutrittsIdentNeben = pZugeordneteKennung.zutrittsIdentNeben;
        lZugeordneteKennungNeu.stimmkartenIdent = pZugeordneteKennung.stimmkartenIdent;
        lZugeordneteKennungNeu.letzterPraesenzstatus = pZugeordneteKennung.letzterPraesenzstatus;
        lZugeordneteKennungNeu.returnVerarbeitung = pZugeordneteKennung.returnVerarbeitung;

        zugeordneteKennungenListeNeu.add(lZugeordneteKennungNeu);
    }

    public void leereMeldungsTeilAusKennung(EclAppZugeordneteKennungen pZugeordneteKennung) {
        pZugeordneteKennung.vertretungsArt = 0;
        pZugeordneteKennung.delegationArt = 0;
        pZugeordneteKennung.aktionaer = "";
        pZugeordneteKennung.stimmen = 0;
        pZugeordneteKennung.gattungNummer = 0;
        pZugeordneteKennung.gattungBezeichnung = "";
        pZugeordneteKennung.zutrittsIdent = "";
        pZugeordneteKennung.zutrittsIdentNeben = "";
        pZugeordneteKennung.stimmkartenIdent = "";
        pZugeordneteKennung.letzterPraesenzstatus = 0;
    }
}
