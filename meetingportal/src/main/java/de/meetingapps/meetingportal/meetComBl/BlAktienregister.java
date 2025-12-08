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
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortVerschluesseln;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportAnmeldedaten;
import de.meetingapps.meetingportal.meetComEntities.EclIsin;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstGruppen;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAktienregister;
import de.meetingapps.meetingportal.meetComStub.WEStubBlAktienregisterRC;
import de.meetingapps.meetingportal.meetComWE.WELoginVerify;

public class BlAktienregister extends StubRoot {

    public BlAktienregister(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    public List<EclAktienregister> registerListe = null;

    public List<EclAktienregister> registerListeInsert = new ArrayList<>();
    public List<EclAktienregister> registerListeUpdate = new ArrayList<>();

    public List<EclAktienregister> registerInsertPart = new ArrayList<>();
    public List<EclAktienregister> registerUpdatePart = new ArrayList<>();

    public List<EclAktienregister> historieListe = new ArrayList<>();
    public List<EclAktienregister> historiePart = new ArrayList<>();

    public List<EclLoginDaten> loginListeInsert = new ArrayList<>();
    public List<EclLoginDaten> loginInsertPart = new ArrayList<>();

    public List<EclStaaten> staaten = null;
    public List<EclIsin> isinList = null;

    public Boolean inhaber = false;
    public EclAktienregister eintrag = null;
    public String currentAction = "";
    public String currentCount = "";
    public long registerStimmen = 0;
    public long registerStueckAktien = 0;

    public long[] bestand = { 0, 0 };
    public int meldebestand = 0;
    public int count = 0;
    public int pSammelIdent = 0;
    public int zeroShareholder = 0;

    /**
     * Aenderungen ohne 0-Baestende
     */
    public int change = 0;
    public int stockChange = 0;
    public int addressChange = 0;
    public int fullChange = 0;

    public Boolean pKarteWurdeVorreserviert = false;
    public List<EclInhaberImportAnmeldedaten> importList = null;

    public int listenLaden() {

        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(1);
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            staaten = stubRC.staaten;
            registerListe = stubRC.registerListe;
            bestand = stubRC.bestand;
            isinList = stubRC.isinList;

            return stubRC.getRc();

        } else {

            dbOpenUndWeitere();

            lDbBundle.dbStaaten.readAll(0);
            staaten = lDbBundle.dbStaaten.ergebnis();
            registerListe = lDbBundle.dbAktienregister.readAll();
            bestand = lDbBundle.dbAktienregister.leseGesamtbestand();
            lDbBundle.dbIsin.readAll();
            isinList = lDbBundle.dbIsin.ergebnis();

            final int maxIdent = lDbBundle.dbAktienregister.readHighestIdent();
            lDbBundle.dbBasis.resetInterneIdentAktienregisterEintrag(maxIdent);

            dbClose();

            return 1;
        }
    }

    public int insertRegister() {

        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(2);
            stub.registerInsertPart = registerInsertPart;
            stub.loginInsertPart = loginInsertPart;
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            return stubRC.getRc();

        } else {

            dbOpen();

            setIdents();

            CaBug.druckeInfo(registerInsertPart.size() + " neue Datensaetze");
            lDbBundle.dbAktienregister.insertAktienregisterListe(registerInsertPart);

            CaBug.druckeInfo(loginInsertPart.size() + " neue Logindaten");
            lDbBundle.dbLoginDaten.insertAufbereitungListe(loginInsertPart);

            dbClose();

            return 1;

        }
    }


    public int updateRegister() {

        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(4);
            stub.registerUpdatePart = registerUpdatePart;
            stub.historiePart = historiePart;
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            count = stubRC.count;

            return stubRC.getRc();

        } else {

            dbOpen();

            CaBug.druckeInfo(registerUpdatePart.size() + " Datensaetze aktualisieren");
            count = lDbBundle.dbAktienregister.updateAktienregisterListe(registerUpdatePart);

            CaBug.druckeInfo(historiePart.size() + " Historien ergaenzen");
            lDbBundle.dbAktienregister.insertHistorieListe(historiePart);

            dbClose();

            return 1;

        }
    }

    public int readAktienregisterBestand() {

        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(5);
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            bestand = stubRC.bestand;

            return stubRC.getRc();

        } else {

            dbOpen();

            bestand = lDbBundle.dbAktienregister.leseGesamtbestand();

            dbClose();

            return 1;

        }
    }

    //  Fuer InhaberAktien abgleich
    public int readMeldeBestand() {

        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(6);
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            meldebestand = stubRC.meldebestand;

            return stubRC.getRc();

        } else {

            dbOpen();

            meldebestand = lDbBundle.dbMeldungen.checkAktienanzahl();

            dbClose();

            return 1;

        }
    }

    public int insertDatensatz(EclAktienregister insert, Boolean bearer) {

        eintrag = insert;
        inhaber = bearer;

        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(7);
            stub.eintrag = eintrag;
            stub.inhaber = inhaber;
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            eintrag = stubRC.eintrag;

            return stubRC.getRc();

        } else {

            dbOpen();

            BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
            lWillenserklaerung.setzeDbBundle(lDbBundle);

            if (eintrag.getAktionaersnummer().isBlank() && inhaber) {
                eintrag.setAktionaersnummer(String.valueOf(lWillenserklaerung
                        .getNeueKarteAuto(KonstKartenklasse.eintrittskartennummer, eintrag.getGattungId(), false)));
                pKarteWurdeVorreserviert = true;
            }

            EclLoginDaten login = erstelleLoginDaten(eintrag);

            int rc = 0;
            if (lDbBundle.dbAktienregister.insertCheck(eintrag)) {
                if (lDbBundle.dbAktienregister.insert(eintrag) == 1) {
                    login.aktienregisterIdent = eintrag.aktienregisterIdent;
                    rc = lDbBundle.dbLoginDaten.insert(login);
                    lDbBundle.dbAktienregister.insertHistorieListe(List.of(eintrag));

                    createRegistration(eintrag);
                }
            }
            dbClose();
            return rc;
        }
    }

    public int updateDatensatz(EclAktienregister eintrag) {

        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(8);
            stub.eintrag = eintrag;
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            return stubRC.getRc();

        } else {

            dbOpen();

            int rc = lDbBundle.dbAktienregister.updateNew(eintrag);
            if (rc == 1)
                lDbBundle.dbAktienregister.insertHistorieListe(List.of(eintrag));

            dbClose();

            return rc;
        }
    }

    public int searchRegister(String search, String sort, int offset, int limit) {
        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(9);
            stub.suche = search;
            stub.sort = sort;
            stub.offset = offset;
            stub.limit = limit;
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            count = stubRC.count;
            registerListe = stubRC.registerListe;

            return stubRC.getRc();

        } else {

            dbOpen();

            registerListe = lDbBundle.dbAktienregister.readSearchOffset(search, sort, offset, limit);
            count = lDbBundle.dbAktienregister.count;

            dbClose();

            return 1;
        }
    }

    public int readAktienregisterIsin() {

        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(10);
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            isinList = stubRC.isinList;

            return stubRC.getRc();

        } else {

            dbOpen();

            isinList = lDbBundle.dbAktienregister.readIsinDistinct();

            dbClose();

            return 1;
        }
    }

    public int sperreAktionaer(EclAktienregister eintrag) {

        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(11);
            stub.eintrag = eintrag;
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();

            return stubRC.getRc();

        } else {

            dbOpen();

            eintrag.stimmen = 0;
            eintrag.stueckAktien = 0;
            int rc = lDbBundle.dbAktienregister.updateNew(eintrag);

            dbClose();

            return rc;
        }
    }
    
    public int resetPasswort(String filter) {

        if (verwendeWebService()) {

            WEStubBlAktienregister stub = new WEStubBlAktienregister(12);
            stub.filter = filter;
            WEStubBlAktienregisterRC stubRC = verifyLogin(stub);

            if (stubRC.getRc() < 1)
                return stubRC.getRc();
            
            count = stubRC.count;

            return stubRC.getRc();

        } else {

            dbOpen();

            lDbBundle.dbLoginDaten.read_all();

            List<EclLoginDaten> list = List.of(lDbBundle.dbLoginDaten.ergebnis());

            Predicate<EclLoginDaten> predicate = switch (filter) {
            case "Alle" -> e -> e.aktienregisterIdent != 0;
            case "Leere" -> e -> e.aktienregisterIdent != 0 && e.passwortInitial.isBlank();
            case "Post" -> e -> e.aktienregisterIdent != 0 && !(e.eVersandRegistriert() && e.emailBestaetigt == 1);
            case "E-Mail" -> e -> e.aktienregisterIdent != 0 && (e.eVersandRegistriert() && e.emailBestaetigt == 1);
            default -> null;
            };

            if (predicate == null)
                return 0;

            final List<EclLoginDaten> tmpList = list.stream().filter(predicate).toList();

            if (tmpList.size() == 0)
                return 0;

            CaPasswortErzeugen caPasswortErzeugen = new CaPasswortErzeugen();
            final int pwLaenge = 8;
            count = tmpList.size();

            for (var login : tmpList) {

                final String passwort = caPasswortErzeugen.generatePWInitial(pwLaenge, 2, 2, true);
                final String passwortVerschluesselt = CaPasswortVerschluesseln.verschluesselnAusInitialPW(passwort,
                        pwLaenge);

                login.passwortInitial = passwort;
                login.passwortVerschluesselt = passwortVerschluesselt;
                login.eigenesPasswort = 0;

                lDbBundle.dbLoginDaten.update(login);

            }

            dbClose();

            return 1;
        }
    }

    public void setIdents() {

        lDbBundle.dbBasis.getInterneIdentAktienregisterEintragReserviereMehrere(registerInsertPart.size());

        int i = 0;

        for (EclAktienregister pData : registerInsertPart) {
            int ident = lDbBundle.dbBasis.getInterneIdentAktienregisterEintragNext();

            pData.aktienregisterIdent = ident;
            loginInsertPart.get(i++).aktienregisterIdent = ident;
        }
    }


    public List<EclAktienregister> compareUnchanged(List<EclAktienregister> registerNeu) {

        Set<EclAktienregister> set = new HashSet<EclAktienregister>(registerNeu);
        Set<EclAktienregister> regSet = new HashSet<EclAktienregister>(registerListe);

        int count = 0;
        int x = 0;
        
        if (set.size() != registerNeu.size() || regSet.size() != registerListe.size()) {

            HashSet<EclAktienregister> uniqueItems = new HashSet<>();
            
            List<EclAktienregister> duplicatesNeu = registerNeu.stream().filter(n -> !uniqueItems.add(n)).toList();
            CaBug.druckeInfo("registerNeu: " + set.size() + " / " + registerNeu.size());
            duplicatesNeu.forEach(System.out::println);

            uniqueItems.clear();

            List<EclAktienregister> duplicatesListe  = registerListe.stream().filter(n -> !uniqueItems.add(n)).toList();
            CaBug.druckeInfo("registerListe: " + set.size() + " / " + registerListe.size());
            duplicatesListe.forEach(System.out::println);

            return null;
        } else if (!registerListe.isEmpty()) {

            for (EclAktienregister lAktienregister : registerListe) {

                if (set.contains(lAktienregister)) {
                    // ku1003
                    // if (set.contains(lAktienregister) || lAktienregister.aktionaersnummer.startsWith("00002300") || lAktienregister.herkunftIdent != 0) {

                    regSet.remove(lAktienregister);
                    set.remove(lAktienregister);
                    x++;

                }
                count++;
                if (count % 10000 == 0 && count > 0)
                    System.out.println(count);
            }

            System.out.println("Es sind " + x + " Datensätze unverändert");
            registerNeu = new ArrayList<>(set);
            registerListe = new ArrayList<>(regSet);

            if (!registerNeu.isEmpty())
                registerNeu.sort(Comparator.comparing(EclAktienregister::getAktionaersnummer));

        }
        System.out.println("registerAlt: " + registerListe.size());
        System.out.println("registerNeu: " + registerNeu.size());
        return registerNeu;
    }

    public void compareChanged(List<EclAktienregister> registerNeu, Boolean insertOnly) {

        final long timeStart = System.currentTimeMillis();

        int i = 0;

        for (EclAktienregister newEintrag : registerNeu) {

            if (!registerListe.isEmpty()) {

                EclAktienregister oldEintrag = registerListe.stream()
                        .filter(e -> newEintrag.aktionaersnummer.equals(e.aktionaersnummer)).findAny().orElse(null);

                if (oldEintrag != null) {
                    if (!insertOnly && !newEintrag.equals(oldEintrag)) {

                        // Update
                        newEintrag.aktienregisterIdent = oldEintrag.aktienregisterIdent;
                        newEintrag.datenUpdate = ++oldEintrag.datenUpdate;
                        newEintrag.personNatJur = oldEintrag.personNatJur;
                        // Bei Update gleiche Versandnummer wie vorher verwenden
                        newEintrag.versandNummer = oldEintrag.versandNummer;

                        if (newEintrag.stimmen != oldEintrag.stimmen
                                || newEintrag.stueckAktien != oldEintrag.stueckAktien) {
                            newEintrag.bestandGeaendert = true;
                        }

                        newEintrag.personNatJurGeaendert = checkPersonUpdate(newEintrag, oldEintrag);

                        if (newEintrag.bestandGeaendert && newEintrag.personNatJurGeaendert) {
                            fullChange++;
                        } else if (newEintrag.bestandGeaendert) {
                            stockChange++;
                        } else if (newEintrag.personNatJurGeaendert) {
                            addressChange++;
                        }

                        if (i < 10) {

                            CaBug.druckeInfo("Alt: " + oldEintrag.toString());
                            CaBug.druckeInfo("Neu: " + newEintrag.toString());

                            i++;
                        }

                        historieListe.add(oldEintrag);
                        registerListeUpdate.add(newEintrag);

                        if (registerListe.size() % 2000 == 0)
                            CaBug.druckeInfo("Register: " + registerListe.size());
                    }
                    registerListe.remove(oldEintrag);
                } else {
                    // Insert
                    createInsertData(newEintrag);
                }
            } else {
                // Insert
                createInsertData(newEintrag);
            }
        }

        final long timeEnd = System.currentTimeMillis();
        final float millisek = (float) (timeEnd - timeStart);

        final String print = millisek > 60000.0 ? new String((millisek / 1000.0 / 60.0) + " Minuten")
                : new String((millisek / 1000.0) + " Sekunden");
        CaBug.druckeInfo("Fertig!");
        CaBug.druckeInfo("Dauer des Vergleiches: " + print);
    }

    public void compareNull(Boolean insertOnly) {

        if (!insertOnly) {
            // Finde alle neuen 0-Bestaende
            final List<EclAktienregister> toNull = registerListe.stream()
                    .filter(e -> e.stimmen != 0 && e.stueckAktien != 0).collect(Collectors.toList());

            CaBug.druckeInfo("Null Bestände: " + toNull.size());
            historieListe.addAll(toNull);

            zeroShareholder = toNull.size();

            for (EclAktienregister lAktienregister : toNull) {

                // System.out.println(lAktienregister.toString());

                lAktienregister.stimmen = 0L;
                lAktienregister.stueckAktien = 0L;
                lAktienregister.datenUpdate = ++lAktienregister.datenUpdate;
                lAktienregister.bestandGeaendert = true;

                registerListeUpdate.add(lAktienregister);
            }
            change = registerListeUpdate.size();
        } else {
            CaBug.druckeInfo("Nullbestände werden nicht bearbeitet!");
        }
    }

    /*
     * Erstelle Anmeldung + EK
     */
    private void createRegistration(EclAktienregister lAktienregister) {

        String aktionaersnummer = lAktienregister.aktionaersnummer;

        BlWillenserklaerung lWillenserklaerung = new BlWillenserklaerung();
        lWillenserklaerung.pEclAktienregisterEintrag = lAktienregister;
        lWillenserklaerung.pAktienAnmelden = -1;
        lWillenserklaerung.pAnzahlAnmeldungen = 1;

        lWillenserklaerung.anmeldungAusAktienregister(lDbBundle);

        lAktienregister.personNatJur = lWillenserklaerung.pPersonNatJurFuerAnmeldungVerwenden;

        BlWillenserklaerung ekWillenserklaerung = new BlWillenserklaerung();
        ekWillenserklaerung.pErteiltAufWeg = 51;
        ekWillenserklaerung.piMeldungsIdentAktionaer = lWillenserklaerung.rcMeldungen[0];
        ekWillenserklaerung.pFolgeFuerWillenserklaerungIdent = lWillenserklaerung.rcWillenserklaerungIdent;
        ekWillenserklaerung.pWillenserklaerungGeberIdent = -1; /* Aktionär */

        /* Versandart */
        ekWillenserklaerung.pVersandartEK = 1;
        ekWillenserklaerung.pZutrittsIdent.zutrittsIdent = aktionaersnummer;
        ekWillenserklaerung.pZutrittsIdent.zutrittsIdentNeben = "00";

        ekWillenserklaerung.neueZutrittsIdentZuMeldung(lDbBundle, pKarteWurdeVorreserviert);

        /* Weisung */
        if (pSammelIdent != 0) {

            System.out.println("Weisung - " + pSammelIdent + " - " + ekWillenserklaerung.piMeldungsIdentAktionaer);
            BlInhaberImportInfo weisung = new BlInhaberImportInfo();

            EclInhaberImportAnmeldedaten anm = importList.stream()
                    .filter(e -> String.valueOf(e.getReferenzEKNr()).equals(lAktienregister.getAktionaersnummer()))
                    .findFirst().orElse(null);

            weisung.initialisierungsschritte(pSammelIdent, lDbBundle);
            weisung.verarbeitenEinesSatzes(lAktienregister.getGattungId(), anm.abgabeToString(),
                    ekWillenserklaerung.piMeldungsIdentAktionaer);

        }

        if (ekWillenserklaerung.rcIstZulaessig == false) {
            System.out.println("Fehler 002 bei " + aktionaersnummer + "=" + ekWillenserklaerung.rcGrundFuerUnzulaessig);
        }
    }

    private void createInsertData(EclAktienregister lAktienregister) {

        // Insert
        registerListeInsert.add(lAktienregister);
        loginListeInsert.add(erstelleLoginDaten(lAktienregister));

    }

    /*
     * LoginDaten erstellen und ggf. Paramter vorbelegen
     */
    private EclLoginDaten erstelleLoginDaten(EclAktienregister lAktienregister) {

        EclLoginDaten loginDaten = new EclLoginDaten();

        int pwLaenge = 8;

        CaPasswortErzeugen caPasswortErzeugen = new CaPasswortErzeugen();

        loginDaten.loginKennung = lAktienregister.aktionaersnummer;
        // 1 = Aktienregister
        loginDaten.kennungArt = KonstLoginKennungArt.aktienregister;
        loginDaten.passwortInitial = caPasswortErzeugen.generatePWInitial(pwLaenge, 2, 2, true);
        loginDaten.passwortVerschluesselt = CaPasswortVerschluesseln
                .verschluesselnAusInitialPW(loginDaten.passwortInitial, pwLaenge);

        if (ParamSpezial.ku164(lDbBundle.param.mandant)) {
            loginDaten.eVersandRegistrierung = 99;
            lAktienregister.email = "PV"; // PV = Postversand
        }

        /*
         * ku217 - 265 auch ku217
         */
        if (ParamSpezial.ku217(lDbBundle.param.mandant) || lDbBundle.param.mandant == 265
                || lDbBundle.param.mandant == 276 || lDbBundle.param.mandant == 154
                || ParamSpezial.ku302_303(lDbBundle.param.mandant)) {

            if (!lAktienregister.email.isBlank()) {
                loginDaten.eVersandRegistrierung = 99;
                loginDaten.emailBestaetigt = 1;
                loginDaten.eMailFuerVersand = lAktienregister.email;
            }
        }

        /*
         * Anpassungen fuer ku178 und ku243
         */
        if (ParamSpezial.ku178(lDbBundle.param.mandant) || ParamSpezial.ku243(lDbBundle.param.mandant)) {

            // loginDaten.anmeldenUnzulaessig = (lAktienregister.gruppe == KonstGruppen.minderjaehrigesEinzelmitglied) ? 1 : 0;
            loginDaten.dauerhafteRegistrierungUnzulaessig = (lAktienregister.gruppe == KonstGruppen.eheleuteGbR
                    || lAktienregister.gruppe == KonstGruppen.einzelmitglied) ? 0 : 1;

            if (ParamSpezial.ku243(lDbBundle.param.mandant)) {
                if (!lAktienregister.emailVersand.isBlank()) {
                    loginDaten.eVersandRegistrierung = 99;
                    loginDaten.emailBestaetigt = 1;
                    loginDaten.eMailFuerVersand = lAktienregister.emailVersand;
                }
            }
            if (ParamSpezial.ku178(lDbBundle.param.mandant)) {
                /*Bei ku178: ab März 2024: Standard Elektronischen Einladungsversand immer auf Ja stellen,
                 * wenn zulässig*/
                if (loginDaten.dauerhafteRegistrierungUnzulaessig == 0) {
                    loginDaten.eVersandRegistrierung = 99;
                    if (!lAktienregister.emailVersand.isEmpty()) {
                        loginDaten.eMailFuerVersand = lAktienregister.email;
                        loginDaten.emailBestaetigt = 1;
                    }
                }
            }

            if (!lAktienregister.aktionaersnummer.matches("\\d+0$")) {

                loginDaten.anmeldenUnzulaessig = 1;

                // System.out.println(lAktienregister.aktionaersnummer);

                final String tmpAnr = lAktienregister.aktionaersnummer.substring(0,
                        lAktienregister.aktionaersnummer.length() - 1) + "0";

                // System.out.println(tmpAnr);

                final EclLoginDaten tmpLogin = loginListeInsert.stream().filter(e -> e.loginKennung.equals(tmpAnr))
                        .findFirst().orElse(null);

                if (tmpLogin != null) {

                    loginDaten.passwortInitial = tmpLogin.passwortInitial;
                    loginDaten.passwortVerschluesselt = tmpLogin.passwortVerschluesselt;

                } else {

                    CaBug.druckeInfo("Read LoginDaten");

                    dbOpen();

                    int anz = lDbBundle.dbLoginDaten.read_loginKennung(tmpAnr);
                    if (anz > 0) {
                        loginDaten.passwortInitial = lDbBundle.dbLoginDaten.ergebnisPosition(0).passwortInitial;
                        loginDaten.passwortVerschluesselt = lDbBundle.dbLoginDaten
                                .ergebnisPosition(0).passwortVerschluesselt;
                        loginDaten.hinweisWeitereBestaetigt = lDbBundle.dbLoginDaten
                                .ergebnisPosition(0).hinweisWeitereBestaetigt;
                    } else {
                        CaBug.drucke("FEHLER!");
                    }
                    dbClose();
                }
            }
        }
        return loginDaten;
    }

    private WEStubBlAktienregisterRC verifyLogin(WEStubBlAktienregister stub) {

        WELoginVerify weLoginVerify = new WELoginVerify();
        stub.setWeLoginVerify(weLoginVerify);

        return wsClient.stubBlAktienregister(stub);
    }

    public void setParameter(WEStubBlAktienregister stub) {

        registerInsertPart = stub.registerInsertPart;
        loginInsertPart = stub.loginInsertPart;
        historiePart = stub.historiePart;

        registerUpdatePart = stub.registerUpdatePart;
        pSammelIdent = stub.pSammelIdent;
        importList = stub.importList;
    }

    /**
     * Check ob eingetragener Aktionaer veraendert wurde
     * 
     * @param newEintrag neuer Datensatz
     * @param oldEintrag alter Datensatz
     */
    public static Boolean checkPersonUpdate(EclAktienregister newEintrag, EclAktienregister oldEintrag) {

        if (!newEintrag.strasse.equals(oldEintrag.strasse)) {
            return true;
        } else if (!newEintrag.postleitzahl.equals(oldEintrag.postleitzahl)) {
            return true;
        } else if (!newEintrag.ort.equals(oldEintrag.ort)) {
            return true;
        } else if (!newEintrag.vorname.equals(oldEintrag.vorname)) {
            return true;
        } else if (!newEintrag.nachname.equals(oldEintrag.nachname)) {
            return true;
        } else if (!newEintrag.postfach.equals(oldEintrag.postfach)) {
            return true;
        } else if (!newEintrag.postleitzahlPostfach.equals(oldEintrag.postleitzahlPostfach)) {
            return true;
        } else if (newEintrag.staatId != oldEintrag.staatId) {
            return true;
        } else if (newEintrag.anredeId != oldEintrag.anredeId) {
            return true;
        } else if (!newEintrag.titel.equals(oldEintrag.titel)) {
            return true;
        }
        return false;
    }
}
