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

import static java.lang.Thread.sleep;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaPasswortErzeugen;
import de.meetingapps.meetingportal.meetComBrM.BrMGenossenschaftCallClient;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComEntities.EclInhaberImportAnmeldedaten;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetAdressenRC;
import de.meetingapps.meetingportal.meetComEntitiesGenossenschaftSys.EgxGetAdressenResult;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterErgaenzung;
import de.meetingapps.meetingportal.meetComKonst.KonstAktienregisterQuelle;
import de.meetingapps.meetingportal.meetComKonst.KonstGruppen;

public class BlAktienregisterImport {
    DbBundle dbBundle = null;
    CaPasswortErzeugen caPasswortErzeugen = null;

    public List<EclStaaten> staaten = null;
    public List<EclAktienregister> aktienregister = null;
    public int mandant = 0;
    public int bestand = 0;

    public BlAktienregisterImport(List<EclStaaten> staaten) {
        this.staaten = staaten;
    }

    public BlAktienregisterImport(List<EclStaaten> staaten, int mandant) {
        this.staaten = staaten;
        this.mandant = mandant;
    }

    public BlAktienregisterImport(List<EclStaaten> staaten, DbBundle lDbBundle) {
        this.staaten = staaten;
        dbBundle = lDbBundle;
    }

    public BlAktienregisterImport(DbBundle pDbBundle) {
        this.dbBundle = pDbBundle;
        this.caPasswortErzeugen = new CaPasswortErzeugen();
    }

    public List<EclAktienregister> registerNew = null;
    public HashMap<String, EclAktienregisterErgaenzung> ergaenzungMap = new HashMap<String, EclAktienregisterErgaenzung>();
    public HashMap<String, String> nummer_vorhanden = new HashMap<String, String>();
    public Set<String> isinSet = new HashSet<>();

    private final String Konst_Legitimationsaktionaer = "Legitimationsaktionär";
    private final String co = "c/o";


    public List<EclAktienregister> aufbereitenInhaberImport(List<EclInhaberImportAnmeldedaten> list) {

        registerNew = new ArrayList<>();

        for (EclInhaberImportAnmeldedaten data : list) {

            EclAktienregister lAktienregisterEintrag = new EclAktienregister();

            lAktienregisterEintrag.setMandant(dbBundle.param.mandant);
            lAktienregisterEintrag.setAktionaersnummer(String.valueOf(data.getReferenzEKNr()));

            int stimmen = data.getStueck();
            if (data.getSatzKzf().equals("U") || data.getSatzKzf().equals("S"))
                stimmen = 0;

            lAktienregisterEintrag.setStueckAktien(stimmen);
            lAktienregisterEintrag.setStimmen(stimmen);

            lAktienregisterEintrag.setBesitzart(data.getBesitzMm());

            lAktienregisterEintrag.setAnredeId(helFindeAnredeInhaberImport(data.getAnredeKzf()));

            lAktienregisterEintrag.setIstJuristischePerson(lAktienregisterEintrag.getAnredeId() == 2 ? 1 : 0);

            lAktienregisterEintrag.setIstPersonengemeinschaft(data.getAnredeKzf() == 8 ? 1 : 0);

            if (lAktienregisterEintrag.getIstJuristischePerson() == 1) {
                lAktienregisterEintrag
                        .setName1(data.getNachname().isBlank() ? data.getAnmeldung() : data.getNachname());
                lAktienregisterEintrag.setName2(data.getVorname());
            } else {
                lAktienregisterEintrag.setTitel(
                        (data.getAkadGrad() + " " + data.getAdelstitel1() + " " + data.getAdelstitel2()).trim());
                lAktienregisterEintrag
                        .setNachname(data.getNachname().isBlank() ? data.getAnmeldung() : data.getNachname());
                lAktienregisterEintrag.setVorname(data.getVorname());
            }

            lAktienregisterEintrag.setNameKomplett(nameKomplett(lAktienregisterEintrag));
            lAktienregisterEintrag.setStrasse(data.getStrasse());
            lAktienregisterEintrag.setPostleitzahl(data.getPlz());
            lAktienregisterEintrag.setOrt(data.getOrt());
            lAktienregisterEintrag.setStaatId(helFindeStaatIdNeu(data.getLandKzf()));

            lAktienregisterEintrag.setGattungId(data.getGattungId());
            lAktienregisterEintrag.setIsin(data.getIsin());

            lAktienregisterEintrag
                    .setVersandAbweichend(!(data.getEmpfKzf().equals("0") || data.getEmpfKzf().equals("")) ? 1 : 0);

            if (lAktienregisterEintrag.getVersandAbweichend() != 0) {

                lAktienregisterEintrag
                        .setAnredeIdVersand((data.getEmpfAnrede().equals("4") || data.getEmpfAnrede().equals("9") ? 2
                                : (data.getEmpfAnrede().equals("5") || data.getEmpfAnrede().equals("8")
                                        || data.getEmpfAnrede().equals("0")) ? 0
                                                : data.getEmpfAnrede().equals("2") ? 3 : 1));

                final int jur = lAktienregisterEintrag.getAnredeIdVersand() == 2 ? 1 : 0;

                if (jur == 1) {
                    lAktienregisterEintrag.setName1Versand(data.getEmpfNachname());
                    lAktienregisterEintrag.setName2Versand(data.getEmpfVorname());
                } else {
                    lAktienregisterEintrag.setNachnameVersand(data.getEmpfNachname());
                    lAktienregisterEintrag.setVornameVersand(data.getEmpfVorname());
                }

                lAktienregisterEintrag.setStrasseVersand(data.getEmpfStrasse());
                lAktienregisterEintrag.setPostleitzahlVersand(data.getEmpfPLZ());
                lAktienregisterEintrag.setOrtVersand(data.getEmpfOrt());
                lAktienregisterEintrag.setStaatIdVersand(helFindeStaatIdNeu(data.getEmpfLandKzf()));

            }

            helAdresszeilenAufbereiten(lAktienregisterEintrag, false);

            lAktienregisterEintrag.setHerkunftQuelle(KonstAktienregisterQuelle.InhaberImport_IMPORT);
            lAktienregisterEintrag.setHerkunftIdent(data.getIdent());

            bestand += lAktienregisterEintrag.stimmen;
            registerNew.add(lAktienregisterEintrag);

        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenSeminar(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        for (String zeile : list) {
            //  Id  Anrede  Vorname Nachname    Unternehmen Straße  Postleitzahl    Ort Telefon E-Mail  Teilnahme   x

            if (!zeile.startsWith("Id")) {

                EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();
                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerku178(zeileSplit[0].trim());

                lAktienregister.anredeId = zeileSplit[1].trim().equals("Herr") ? 1 : 3;

                lAktienregister.vorname = zeileSplit[2].trim();
                lAktienregister.nachname = zeileSplit[3].trim();

                if (zeileSplit[4].trim().isBlank()) {
                    lAktienregister.name1 = (lAktienregister.vorname + " " + lAktienregister.nachname).trim();
                } else {
                    lAktienregister.name1 = zeileSplit[4].trim();

                    lAktienregisterErgaenzung.ergaenzungLangString[24] = lAktienregister.nachname.trim();
                    lAktienregisterErgaenzung.ergaenzungLangString[25] = lAktienregister.vorname.trim();
                }

                lAktienregister.name1 = zeileSplit[4].trim().isBlank()
                        ? (lAktienregister.vorname + " " + lAktienregister.nachname).trim()
                        : zeileSplit[4].trim();

                lAktienregister.strasse = zeileSplit[5].trim();
                lAktienregister.postleitzahl = zeileSplit[6].trim();
                lAktienregister.ort = zeileSplit[7].trim();

                lAktienregister.stimmen = 1;
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                lAktienregister.gattungId = 1;
                lAktienregister.gruppe = 1;
                lAktienregister.istJuristischePerson = 1;

                lAktienregister.email = zeileSplit[9].trim();

                lAktienregister.nameKomplett = lAktienregister.name1;

                lAktienregister.besitzart = "E";
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                lAktienregisterErgaenzung.mandant = lAktienregister.mandant;
                lAktienregisterErgaenzung.ergaenzungLangString[26] = lAktienregister.ort;

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);

                ergaenzungMap.put(lAktienregister.aktionaersnummer, lAktienregisterErgaenzung);
            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenarfuehrer005(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        // ISIN steht in der 1. Zeile
        isinSet.add(list.get(0).substring(2, 14));
        System.out.println(Arrays.toString(isinSet.toArray()));

        list.remove(0);

        for (String zeile : list) {

            if (zeile.length() > 700) {

                EclAktienregister lAktienregister = new EclAktienregister();

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerNeu(zeile.substring(3, 14).trim(),
                        zeile.substring(554, 555).trim());
                lAktienregister.anredeId = helFindeAnrede(zeile.substring(24, 27).trim());
                lAktienregister.istJuristischePerson = zeile.substring(553, 554).trim().equals("J") ? 1
                        : helCheckJurPerson(zeile.substring(24, 27).trim());

                if (lAktienregister.istJuristischePerson == 1) {
                    if (zeile.substring(65, 103).trim().contains(Konst_Legitimationsaktionaer)) {
                        lAktienregister.name1 = zeile.substring(27, 65).trim();
                        lAktienregister.name2 = zeile.substring(65, 103).trim();
                    } else {
                        lAktienregister.name1 = (zeile.substring(27, 65).trim() + " " + zeile.substring(65, 103).trim())
                                .trim();
                    }
                    lAktienregister.name3 = zeile.substring(103, 141).trim();
                } else {
                    lAktienregister.titel = zeile.substring(103, 141).trim();
                    lAktienregister.nachname = zeile.substring(27, 65).trim();
                    lAktienregister.vorname = zeile.substring(65, 103).trim();
                }

                lAktienregister.strasse = zeile.substring(609, 647).trim();
                lAktienregister.postfach = zeile.substring(647, 657).trim();
                lAktienregister.postleitzahl = zeile.substring(657, 667).trim();
                lAktienregister.postleitzahlPostfach = zeile.substring(667, 677).trim();
                lAktienregister.ort = zeile.substring(677, 715).trim();
                lAktienregister.staatId = helFindeStaatIdNeu(zeile.substring(715, 717).trim());

                lAktienregister.nameKomplett = nameKomplett(lAktienregister);

                lAktienregister.versandAbweichend = zeile.substring(141, 142).trim().equals("0") ? 0 : 1;

                if (lAktienregister.versandAbweichend == 1) {

                    lAktienregister.anredeIdVersand = lAktienregister.anredeId;
                    lAktienregister.name1Versand = lAktienregister.name1;
                    lAktienregister.name2Versand = lAktienregister.name2;
                    lAktienregister.name3Versand = lAktienregister.name3;
                    lAktienregister.titelVersand = lAktienregister.titel;
                    lAktienregister.nachnameVersand = lAktienregister.nachname;
                    lAktienregister.vornameVersand = lAktienregister.vorname;
                    checkZusatz(lAktienregister, zeile.substring(142, 180).trim(), zeile.substring(180, 218).trim(),
                            zeile.substring(218, 256).trim());

                    lAktienregister.strasseVersand = zeile.substring(370, 408).trim();
                    lAktienregister.postfachVersand = zeile.substring(408, 418).trim();
                    lAktienregister.postleitzahlVersand = zeile.substring(418, 428).trim();
                    lAktienregister.postleitzahlPostfachVersand = zeile.substring(428, 438).trim();
                    lAktienregister.ortVersand = zeile.substring(438, 476).trim();
                    lAktienregister.staatIdVersand = helFindeStaatIdNeu(zeile.substring(478, 480).trim());

                }

                lAktienregister.email = zeile.substring(480, 550).trim();
                lAktienregister.istPersonengemeinschaft = helCheckPersonengemeinschaft(zeile.substring(24, 27).trim());
                lAktienregister.gattungId = 1;
                lAktienregister.stueckAktien = (Long.parseLong(zeile.substring(555, 573).trim()) / 10000L);
                lAktienregister.stimmen = (Long.parseLong(zeile.substring(555, 573).trim()) / 10000L);
                lAktienregister.besitzart = zeile.substring(554, 555).trim();
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                helAdresszeilenAufbereiten(lAktienregister, false);

                if (lAktienregister.besitzart.equals("B")) {

                    System.out.println("Besitzart: B!!!");

                    EclAktienregister tmpAktienregister = EclAktienregister.copyEntity(lAktienregister);

                    lAktienregister.besitzart = "E";
                    lAktienregister.stueckAktien = (Long.parseLong(zeile.substring(573, 591).trim()) / 10000L);
                    lAktienregister.stimmen = (Long.parseLong(zeile.substring(573, 591).trim()) / 10000L);

                    tmpAktienregister.besitzart = "F";
                    tmpAktienregister.aktionaersnummer = helAktionaersnummerNeu(zeile.substring(3, 14).trim(), "F");
                    tmpAktienregister.stueckAktien = (Long.parseLong(zeile.substring(591, 609).trim()) / 10000L);
                    tmpAktienregister.stimmen = (Long.parseLong(zeile.substring(591, 609).trim()) / 10000L);

                    bestand += tmpAktienregister.stimmen;
                    registerNew.add(tmpAktienregister);

                }
                bestand += lAktienregister.stimmen;

                registerNew.add(lAktienregister);
            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenarfuehrer003Neu(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();
        List<String> anrList = new ArrayList<>();

        for (String zeile : list) {
            if ((!zeile.contains("Registerdownload")) && (!zeile.contains("Mandant"))
                    && (!zeile.contains("Emittent"))) {

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerNeu(zeileSplit[1].trim(), zeileSplit[45].trim());
                lAktienregister.anredeId = helFindeAnrede(zeileSplit[5].trim());
                lAktienregister.titel = helCheckTitel(zeileSplit[5].trim(), zeileSplit[4].trim());
                lAktienregister.name3 = helCheckName3(zeileSplit[5].trim(), zeileSplit[4].trim());
                lAktienregister.nachname = helCheckNachname(zeileSplit[5].trim(), zeileSplit[2].trim());
                lAktienregister.name1 = helCheckName1(zeileSplit[5].trim(), zeileSplit[2].trim());
                lAktienregister.vorname = helCheckVorname(zeileSplit[5].trim(), zeileSplit[3].trim());
                lAktienregister.name2 = helCheckName2(zeileSplit[5].trim(), zeileSplit[3].trim());
                lAktienregister.strasse = zeileSplit[17].trim();
                lAktienregister.postfach = zeileSplit[19].trim();
                lAktienregister.postleitzahl = helErstellePlz(zeileSplit[16].trim(), zeileSplit[20].trim());
                lAktienregister.postleitzahlPostfach = helErstellePlz(zeileSplit[18].trim(), zeileSplit[20].trim());
                lAktienregister.ort = zeileSplit[15].trim();
                lAktienregister.staatId = helFindeStaatIdNeu(zeileSplit[20].trim());
                lAktienregister.email = zeileSplit[21].trim();
                lAktienregister.anredeIdVersand = helFindeAnrede(zeileSplit[26].trim());
                lAktienregister.titelVersand = helCheckTitel(zeileSplit[26].trim(), zeileSplit[25].trim());
                lAktienregister.name3Versand = helCheckName3(zeileSplit[26].trim(), zeileSplit[25].trim());
                lAktienregister.nachnameVersand = helCheckNachname(zeileSplit[26].trim(), zeileSplit[23].trim());
                lAktienregister.name1Versand = helCheckName1(zeileSplit[26].trim(), zeileSplit[23].trim());
                lAktienregister.vornameVersand = helCheckVorname(zeileSplit[26].trim(), zeileSplit[24].trim());
                lAktienregister.name2Versand = helCheckName2(zeileSplit[26].trim(), zeileSplit[24].trim());
                lAktienregister.strasseVersand = zeileSplit[30].trim();
                lAktienregister.postfachVersand = zeileSplit[32].trim();
                lAktienregister.postleitzahlVersand = helErstellePlz(zeileSplit[29].trim(), zeileSplit[33].trim());
                lAktienregister.postleitzahlPostfachVersand = helErstellePlz(zeileSplit[31].trim(),
                        zeileSplit[33].trim());
                lAktienregister.ortVersand = zeileSplit[28].trim();
                lAktienregister.staatIdVersand = helFindeStaatIdNeu(zeileSplit[33].trim());
                lAktienregister.emailVersand = zeileSplit[34].trim();
                lAktienregister.istPersonengemeinschaft = helCheckPersonengemeinschaft(zeileSplit[5].trim());
                lAktienregister.istJuristischePerson = helCheckJurPerson(zeileSplit[5].trim());
                lAktienregister.versandAbweichend = Integer.parseInt(zeileSplit[22].trim());

                lAktienregister.nameKomplett = nameKomplett(lAktienregister);

                lAktienregister.isin = helIsin(zeileSplit[43].trim());
                lAktienregister.besitzart = zeileSplit[45].trim();
                lAktienregister.stueckAktien = Long.parseLong(zeileSplit[48].replace(".", "").trim());
                lAktienregister.stimmen = lAktienregister.stueckAktien;

                lAktienregister.gattungId = 1;

                if (ParamSpezial.ku116(ParamS.clGlobalVar.mandant)) {
                    lAktienregister.gattungId = 2;
                    if (lAktienregister.aktionaersnummer.equals("20050020531")) {
                        lAktienregister.gattungId = 1;
                    }
                }

                helAdresszeilenAufbereiten(lAktienregister, false);

                lAktienregister.englischerVersand = helCheckEnglischerVersand(zeileSplit[35].trim());
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                anrList.add(lAktienregister.aktionaersnummer);

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);
            }
        }

        checkDuplicates(anrList);

        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku168(List<String> list, String versandNummer) throws Exception {
        // 0Aktionärsnummer;1Name;2Aktien;3Strasse;4PLZ;5Ort;6Land

        registerNew = new ArrayList<>();

        for (String zeile : list) {

            if (!zeile.startsWith("Aktionärsnr")) {

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerNeu(zeileSplit[0].trim(), "E");
                lAktienregister.name1 = zeileSplit[1].trim();

                if (lAktienregister.name1.length() > 75)
                    System.out.println("name!" + lAktienregister.name1);

                lAktienregister.strasse = zeileSplit[3].trim();
                lAktienregister.postleitzahl = zeileSplit[4].trim();
                lAktienregister.ort = zeileSplit[5].trim();
                lAktienregister.staatId = helFindeStaatByName(zeileSplit[6].trim());

                lAktienregister.nameKomplett = lAktienregister.name1;

                lAktienregister.stueckAktien = Long.parseLong(zeileSplit[2].replace(".", "").trim());
                lAktienregister.stimmen = lAktienregister.stueckAktien;
                lAktienregister.besitzart = "E";

                lAktienregister.gattungId = 1;
                lAktienregister.istJuristischePerson = 1;

                lAktienregister.adresszeile1 = lAktienregister.name1;
                lAktienregister.adresszeile2 = lAktienregister.strasse;
                lAktienregister.adresszeile3 = lAktienregister.postleitzahl + " " + lAktienregister.ort;
                lAktienregister.adresszeile4 = lAktienregister.staatId != 56 ? zeileSplit[6].trim() : "";
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);

            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenarfuehrer001Neu(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        for (String zeile : list) {

            EclAktienregister lAktienregister = new EclAktienregister();

            lAktienregister.mandant = ParamS.clGlobalVar.mandant;
            lAktienregister.aktionaersnummer = zeile.substring(3, 13).trim();
            lAktienregister.anredeId = helFindeAnrede(zeile.substring(205, 206).trim());
            lAktienregister.titel = helCheckTitel(zeile.substring(205, 206).trim(), zeile.substring(83, 118).trim());
            lAktienregister.name3 = helCheckName3(zeile.substring(205, 206).trim(), zeile.substring(83, 118).trim());
            lAktienregister.nachname = helCheckNachname(zeile.substring(205, 206).trim(),
                    zeile.substring(13, 48).trim());
            lAktienregister.name1 = helCheckName1(zeile.substring(205, 206).trim(), zeile.substring(13, 48).trim());
            lAktienregister.vorname = helCheckVorname(zeile.substring(205, 206).trim(), zeile.substring(48, 83).trim());
            lAktienregister.name2 = helCheckName2(zeile.substring(205, 206).trim(), zeile.substring(48, 83).trim());
            lAktienregister.strasse = zeile.substring(532, 567).trim();
            lAktienregister.postfach = zeile.substring(577, 587).trim();
            lAktienregister.postleitzahl = zeile.substring(567, 577).trim();
            lAktienregister.postleitzahlPostfach = zeile.substring(577, 587).trim();
            lAktienregister.ort = zeile.substring(597, 632).trim();
            lAktienregister.staatId = helFindeStaatIdNeu(zeile.substring(632, 634).trim());
            lAktienregister.email = zeile.substring(241, 496).trim();

            lAktienregister.anredeIdVersand = 0;
            lAktienregister.titelVersand = "";
            lAktienregister.nachnameVersand = "";
            lAktienregister.vornameVersand = "";

            lAktienregister.name3Versand = zeile.substring(704, 739).trim();
            lAktienregister.name1Versand = zeile.substring(669, 704).trim();
            lAktienregister.name2Versand = zeile.substring(634, 669).trim();

            lAktienregister.strasseVersand = zeile.substring(739, 774).trim();
            lAktienregister.postfachVersand = zeile.substring(784, 794).trim();
            lAktienregister.postleitzahlVersand = zeile.substring(774, 784).trim();
            lAktienregister.postleitzahlPostfachVersand = zeile.substring(794, 804).trim();
            lAktienregister.ortVersand = zeile.substring(804, 839).trim();
            lAktienregister.staatIdVersand = helFindeStaatIdNeu(zeile.substring(839, 841).trim());
            lAktienregister.emailVersand = "";
            lAktienregister.istPersonengemeinschaft = helCheckPersonengemeinschaft(zeile.substring(205, 206).trim());
            lAktienregister.istJuristischePerson = helCheckJurPerson(zeile.substring(205, 206).trim());
            lAktienregister.nameKomplett = nameKomplett(lAktienregister);
            lAktienregister.versandAbweichend = 0;
            lAktienregister.gattungId = 1;
            lAktienregister.stueckAktien = Long.parseLong(zeile.substring(508, 520).trim());
            lAktienregister.stimmen = Long.parseLong(zeile.substring(520, 532).trim());
            if (!zeile.substring(203, 204).trim().equals("")) {
                lAktienregister.besitzart = "F";
                lAktienregister.aktionaersnummer += "1";
            } else {
                lAktienregister.besitzart = "E";
                lAktienregister.aktionaersnummer += "0";
            }
            lAktienregister.adresszeile1 = zeile.substring(841, 876).trim();
            lAktienregister.adresszeile2 = zeile.substring(876, 911).trim();
            lAktienregister.adresszeile3 = zeile.substring(911, 946).trim();
            lAktienregister.adresszeile4 = zeile.substring(946, 981).trim();
            lAktienregister.adresszeile5 = zeile.substring(981, 1016).trim();
            lAktienregister.adresszeile6 = zeile.substring(1016, 1051).trim();
            lAktienregister.adresszeile7 = zeile.substring(1051, 1086).trim();
            lAktienregister.adresszeile8 = zeile.substring(1086, 1121).trim();
            lAktienregister.adresszeile9 = zeile.substring(1121, 1156).trim();
            lAktienregister.adresszeile10 = zeile.substring(1156, 1191).trim();
            lAktienregister.englischerVersand = helCheckEnglischerVersand(zeile.substring(188, 189).trim());
            lAktienregister.versandNummer = Integer.parseInt(versandNummer);

            bestand += lAktienregister.stimmen;
            registerNew.add(lAktienregister);

        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenarfuehrer003KSNeu(List<String> list, String versandNummer) throws Exception {
        List<String> anrList = new ArrayList<>();
        registerNew = new ArrayList<>();

        for (String zeile : list) {
            if ((!zeile.contains("Registerdownload")) && (!zeile.contains("Mandant"))
                    && (!zeile.contains("Emittent"))) {

                String[] zeileSplit = zeile.split(";");

                EclAktienregister lAktienregister = new EclAktienregister();

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerNeu(zeileSplit[1].trim(), zeileSplit[45].trim());
                lAktienregister.anredeId = helFindeAnrede(zeileSplit[5].trim());
                lAktienregister.titel = helCheckTitel(zeileSplit[5].trim(), zeileSplit[4].trim());
                lAktienregister.name3 = helCheckName3(zeileSplit[5].trim(), zeileSplit[4].trim());
                lAktienregister.nachname = helCheckNachname(zeileSplit[5].trim(), zeileSplit[2].trim());
                lAktienregister.name1 = helCheckName1(zeileSplit[5].trim(), zeileSplit[2].trim());
                lAktienregister.vorname = helCheckVorname(zeileSplit[5].trim(), zeileSplit[3].trim());
                lAktienregister.name2 = helCheckName2(zeileSplit[5].trim(), zeileSplit[3].trim());
                lAktienregister.strasse = zeileSplit[17].trim();
                lAktienregister.postfach = zeileSplit[19].trim();
                lAktienregister.postleitzahl = helErstellePlz(zeileSplit[16].trim(), zeileSplit[16].trim());
                lAktienregister.postleitzahlPostfach = helErstellePlz(zeileSplit[18].trim(), zeileSplit[16].trim());
                lAktienregister.ort = zeileSplit[15].trim();
                lAktienregister.staatId = helFindeStaatIdNeu(zeileSplit[20].trim());
                // Partnerfond
                // lAktienregister.staatId = helFindeStaatByName(zeileSplit[16].trim());

                // Wird wenn neuer Aktionär mit 'PV' beschriftet
                lAktienregister.email = "";
                lAktienregister.anredeIdVersand = helFindeAnrede(zeileSplit[26].trim());
                lAktienregister.titelVersand = helCheckTitel(zeileSplit[26].trim(), zeileSplit[25].trim());
                lAktienregister.name3Versand = helCheckName3(zeileSplit[26].trim(), zeileSplit[25].trim());

                lAktienregister.nachnameVersand = helCheckNachname(zeileSplit[26].trim(), zeileSplit[23].trim());
                lAktienregister.name1Versand = helCheckName1(zeileSplit[26].trim(), zeileSplit[23].trim());
                lAktienregister.vornameVersand = helCheckVorname(zeileSplit[26].trim(), zeileSplit[24].trim());
                lAktienregister.name2Versand = helCheckName2(zeileSplit[26].trim(), zeileSplit[24].trim());
                lAktienregister.strasseVersand = zeileSplit[30].trim();
                lAktienregister.postfachVersand = zeileSplit[32].trim();
                lAktienregister.postleitzahlVersand = helErstellePlz(zeileSplit[29].trim(), zeileSplit[39].trim());
                lAktienregister.postleitzahlPostfachVersand = helErstellePlz(zeileSplit[31].trim(),
                        zeileSplit[39].trim());
                lAktienregister.ortVersand = zeileSplit[28].trim();
                lAktienregister.staatIdVersand = helFindeStaatIdNeu(zeileSplit[33].trim());
                // Partnerfond
                // lAktienregister.staatIdVersand = lAktienregister.staatId;
                lAktienregister.emailVersand = zeileSplit[34].trim();
                lAktienregister.istPersonengemeinschaft = helCheckPersonengemeinschaft(zeileSplit[5].trim());
                lAktienregister.istJuristischePerson = helCheckJurPerson(zeileSplit[5].trim());
                lAktienregister.nameKomplett = nameKomplett(lAktienregister);
                lAktienregister.versandAbweichend = Integer.parseInt(zeileSplit[22].trim());
                lAktienregister.gattungId = 1;
                lAktienregister.stueckAktien = Long.parseLong(zeileSplit[48].replace(".", "").trim());
                lAktienregister.stimmen = lAktienregister.stueckAktien;
                lAktienregister.besitzart = zeileSplit[45].trim();
                lAktienregister.stimmausschluss = "";

                helAdresszeilenAufbereiten(lAktienregister, false);

                lAktienregister.englischerVersand = helCheckEnglischerVersand(zeileSplit[35].trim()); // 30
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                anrList.add(lAktienregister.aktionaersnummer);

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);

                if (lAktienregister.stimmen != lAktienregister.stueckAktien)
                    System.out.println("Bestand: " + (lAktienregister.stimmen != lAktienregister.stueckAktien));

            }
        }

        checkDuplicates(anrList);
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenarfuehrer002(List<String> list, String versandNummer) throws Exception {
        registerNew = new ArrayList<>();
        List<String> anrList = new ArrayList<>();

        for (String zeile : list) {
            if ((!zeile.contains("Registerdownload")) && (!zeile.contains("Mandant"))
                    && (!zeile.contains("Emittent"))) {

                String[] zeileSplit = zeile.split(";");

                EclAktienregister lAktienregister = new EclAktienregister();

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummer(zeileSplit[2].trim(), zeileSplit[12].trim());
                lAktienregister.anredeId = helFindeAnredeBM(zeileSplit[9].trim());
                lAktienregister.titel = zeileSplit[3].trim();
                lAktienregister.name3 = helCheckName3BM(zeileSplit[9].trim(), zeileSplit[6].trim());
                lAktienregister.nachname = helCheckNachnameBM(zeileSplit[9].trim(), zeileSplit[4].trim());
                lAktienregister.name1 = helCheckName1BM(zeileSplit[9].trim(), zeileSplit[4].trim());
                lAktienregister.vorname = helCheckVornameBM(zeileSplit[9].trim(), zeileSplit[5].trim());
                lAktienregister.name2 = helCheckName2BM(zeileSplit[9].trim(), zeileSplit[5].trim());
                lAktienregister.strasse = zeileSplit[15].trim();
                lAktienregister.postfach = zeileSplit[16].trim().trim();
                lAktienregister.postleitzahl = helErstellePlzBM(zeileSplit[17].trim(), zeileSplit[20].trim());
                lAktienregister.postleitzahlPostfach = helErstellePlzBM(zeileSplit[18].trim(), zeileSplit[20].trim());
                lAktienregister.ort = zeileSplit[19].trim();
                lAktienregister.staatId = helFindeStaatIdNeu(zeileSplit[37].trim());
                lAktienregister.email = zeileSplit[21].trim();
                lAktienregister.anredeIdVersand = helFindeAnredeBM(zeileSplit[23].trim());
                lAktienregister.titelVersand = zeileSplit[24].trim();
                lAktienregister.name3Versand = helCheckName3BM(zeileSplit[23].trim(), zeileSplit[27].trim());
                lAktienregister.nachnameVersand = helCheckNachnameBM(zeileSplit[23].trim(), zeileSplit[25].trim());
                lAktienregister.name1Versand = helCheckName1BM(zeileSplit[23].trim(), zeileSplit[25].trim());
                lAktienregister.vornameVersand = helCheckVornameBM(zeileSplit[23].trim(), zeileSplit[26].trim());
                lAktienregister.name2Versand = helCheckName2BM(zeileSplit[23].trim(), zeileSplit[26].trim());
                lAktienregister.strasseVersand = zeileSplit[29].trim();
                lAktienregister.postfachVersand = zeileSplit[30].trim().trim();
                lAktienregister.postleitzahlVersand = helErstellePlzBM(zeileSplit[31].trim(), zeileSplit[34].trim());
                lAktienregister.postleitzahlPostfachVersand = helErstellePlzBM(zeileSplit[32].trim(),
                        zeileSplit[34].trim());
                lAktienregister.ortVersand = zeileSplit[33].trim();
                lAktienregister.staatIdVersand = helFindeStaatIdNeu(zeileSplit[38].trim());
                lAktienregister.emailVersand = zeileSplit[35].trim();
                lAktienregister.istPersonengemeinschaft = helCheckPersonengemeinschaftBM(zeileSplit[9].trim());
                lAktienregister.istJuristischePerson = helCheckJurPersonBM(zeileSplit[8].trim());
                lAktienregister.nameKomplett = nameKomplett(lAktienregister);
                lAktienregister.versandAbweichend = helVersandAbweichendBM(zeileSplit[22].trim());
                lAktienregister.gattungId = 1;
                lAktienregister.stueckAktien = Long.parseLong(zeileSplit[13].trim());
                lAktienregister.stimmen = Long.parseLong(zeileSplit[13].trim());
                lAktienregister.besitzart = zeileSplit[12].trim().substring(0, 1);
                lAktienregister.isin = helIsin(zeileSplit[11].trim());
                lAktienregister.stimmausschluss = "";

                if (ParamSpezial.ku128(ParamS.clGlobalVar.mandant)) {
                    if (lAktienregister.isin.equals("DE0006858533")) {
                        lAktienregister.gattungId = 2;
                    }
                    lAktienregister.aktionaersnummer = helAktionaersnummer(zeileSplit[2].trim(),
                            lAktienregister.gattungId == 2 ? "F" : "E");
                } else if (ParamSpezial.ku287(ParamS.clGlobalVar.mandant)) {

                    EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();
                    ergaenzungMap.put(lAktienregister.aktionaersnummer, lAktienregisterErgaenzung);
                    lAktienregister.gruppe = 1;
                    lAktienregister.emailVersand = zeileSplit[41].trim();
                }

                final String staatNameVersand = helFindeStaatNameNeu(lAktienregister.staatIdVersand);

                lAktienregister = checkName(lAktienregister);

                final List<String> adresszeilen = helAdresszeilenAufbereitenBM(lAktienregister.anredeId,
                        lAktienregister.anredeIdVersand, zeileSplit[22].trim(), lAktienregister.titel,
                        lAktienregister.vorname, lAktienregister.nachname, lAktienregister.name1, lAktienregister.name2,
                        lAktienregister.name3, lAktienregister.titelVersand, lAktienregister.vornameVersand,
                        lAktienregister.nachnameVersand, lAktienregister.name1Versand, lAktienregister.name2Versand,
                        lAktienregister.name3Versand, lAktienregister.strasseVersand, lAktienregister.postfachVersand,
                        lAktienregister.postleitzahlVersand, lAktienregister.postleitzahlPostfachVersand,
                        lAktienregister.ortVersand, zeileSplit[28].trim(), staatNameVersand,
                        lAktienregister.nameKomplett);

                lAktienregister.adresszeile1 = adresszeilen.get(0).trim();
                lAktienregister.adresszeile2 = adresszeilen.get(1).trim();
                lAktienregister.adresszeile3 = adresszeilen.get(2).trim();
                lAktienregister.adresszeile4 = adresszeilen.get(3).trim();
                lAktienregister.adresszeile5 = adresszeilen.get(4).trim();
                lAktienregister.adresszeile6 = adresszeilen.get(5).trim();

                lAktienregister.englischerVersand = helCheckEnglischerVersand(zeileSplit[10].trim());
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                bestand += lAktienregister.stimmen;

                anrList.add(lAktienregister.aktionaersnummer);
                registerNew.add(lAktienregister);
            }
        }

        checkDuplicates(anrList);

        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku178(List<String> list, String versandNummer) throws Exception {
        registerNew = new ArrayList<>();

        BrMGenossenschaftCallClient req = new BrMGenossenschaftCallClient();

        Boolean eol = false;

        List<EgxGetAdressenResult> mDaten = new ArrayList<>();

        int page = 0;

        do {
            EgxGetAdressenRC rc = null;

            int versuch = 0;

            while (rc == null) {

                rc = req.doGetRequestAdressen(page + "");

                if (rc == null) {
                    if (versuch < 2) {

                        versuch++;
                        CaBug.out("Verbindung unterbrochen - Warte 5 Sekunden - Versuch: " + versuch);
                        sleep(5000);

                    } else {
                        CaBug.out("Verbindung nicht möglich");
                        return null;
                    }
                }
            }

            CaBug.out("Page: " + page++);

            mDaten.addAll(rc.getResult());
            eol = !rc.get_links().getNext().isBlank();

        } while (eol);

        mDaten.size();

        for (EgxGetAdressenResult data : mDaten) {

            EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();

            EclAktienregister lAktienregister = new EclAktienregister();

            lAktienregister.mandant = ParamS.clGlobalVar.mandant;
            lAktienregister.aktionaersnummer = helAktionaersnummerku178(String.valueOf(data.getMitgliedsnummer()));
            lAktienregister.anredeId = helFindeAnredeku178(data.getAnrede());
            lAktienregister.nachname = helNachnameku178(data.getNachname(), data.getGruppierung());
            lAktienregister.nachname += helName1ku178(data.getNachname(), data.getGruppierung());
            if (lAktienregister.nachname.length() > 80) {
                lAktienregister.nachname = lAktienregister.nachname.substring(0, 80).trim();
            }
            lAktienregister.vorname = data.getVorname();
            lAktienregister.strasse = data.getStrasse();
            lAktienregister.postleitzahl = helPLZku178(data.getPlz(), data.getLand());
            lAktienregister.ort = data.getOrt();
            lAktienregister.staatId = helFindeStaatByName(data.getLand());
            lAktienregister.anredeIdVersand = helFindeAnredeku178(data.getP_anrede());
            lAktienregister.nachnameVersand = data.getP_nachname();
            if (lAktienregister.nachnameVersand.length() > 80) {
                lAktienregister.nachnameVersand = lAktienregister.nachnameVersand.substring(0, 80).trim();
            }
            lAktienregister.vornameVersand = data.getP_vorname();
            lAktienregister.strasseVersand = data.getP_strasse();
            lAktienregister.postleitzahlVersand = helPLZku178(data.getP_plz(), data.getP_land());
            lAktienregister.ortVersand = data.getP_ort();
            lAktienregister.staatIdVersand = helFindeStaatByName(data.getP_land());
            lAktienregister.nameKomplett = helCheckNameKomplettku178(lAktienregister.vorname, lAktienregister.nachname,
                    lAktienregister.name1);
            // lAktienregister.versandAbweichend = helVersandAbweichendku178(data.);
            lAktienregister.stueckAktien = 1;
            lAktienregister.stimmen = 1;
            lAktienregister.besitzart = "E";
            lAktienregister.stimmausschluss = "";
            lAktienregister.gattungId = 1;
            lAktienregister.gruppe = helGruppe(data.getGruppierung());

            final List<String> adresszeilen = helAdresszeilenAufbereitenku178(lAktienregister.vornameVersand,
                    lAktienregister.nachnameVersand, lAktienregister.strasseVersand,
                    lAktienregister.postleitzahlVersand, lAktienregister.ortVersand,
                    helFindeStaatNameNeu(lAktienregister.staatIdVersand), data.getP_adresszusatz());

            lAktienregister.adresszeile1 = adresszeilen.get(0).trim();
            lAktienregister.adresszeile2 = adresszeilen.get(1).trim();
            lAktienregister.adresszeile3 = adresszeilen.get(2).trim();
            lAktienregister.adresszeile4 = adresszeilen.get(3).trim();
            lAktienregister.adresszeile5 = adresszeilen.get(4).trim();
            lAktienregister.adresszeile6 = adresszeilen.get(5).trim();

            lAktienregister.versandNummer = Integer.parseInt(versandNummer);

            lAktienregisterErgaenzung.mandant = lAktienregister.mandant;
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_GeburtsdatumMitglied] = CaDatumZeit
                    .datumJJJJ_MM_TTzuNormal(data.getGeburtsdatum_01());
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_GeburtsdatumEhegatte] = CaDatumZeit
                    .datumJJJJ_MM_TTzuNormal(data.getGeburtsdatum_02());
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Name] = lAktienregister.name1
                    + lAktienregister.nachname;
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Vorname] = lAktienregister.vorname;
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_StrasseHausnummer] = lAktienregister.strasse;
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_PLZ] = lAktienregister.postleitzahl;
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Ort] = lAktienregister.ort;
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Gruppe] = String
                    .valueOf(lAktienregister.gruppe);
            lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Bundesland] = data
                    .getBundesland();

            bestand += lAktienregister.stimmen;
            registerNew.add(lAktienregister);

            ergaenzungMap.put(lAktienregister.aktionaersnummer, lAktienregisterErgaenzung);

        }
        return registerNew;

        /*
         * Nach Import SQL-Statement ausfuehren: SELECT a.aktienregisterIdent,
         * a.loginKennung, a.passwortInitial, a.anmeldenUnzulaessig,
         * a.dauerhafteRegistrierungUnzulaessig, b.gruppe FROM
         * db_mc178j2020ap.tbl_logindaten a LEFT JOIN tbl_aktienregister b ON
         * a.aktienregisterIdent = b.aktienregisterIdent WHERE a.anmeldenUnzulaessig = 1
         * and b.aktionaersnummer like '%0'; Einzellfaelle aendern anmledungUnzulaesssig
         * = 0
         */
    }

    // ku1008
    public List<EclAktienregister> aufbereitenku1008(List<String> list, String versandNummer) throws Exception {
        // #;Nachname;Vorname;Titel;Strasse;PLZ;Ort;Wohnland;Stimmen
        // 0 bei ANR 1 bis 9 ergänzen - damit alle ANR gleich lang werden

        registerNew = new ArrayList<>();

        for (String zeile : list) {

            if (!zeile.startsWith("#")) {

                final String anr = "2400";

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerNeu(anr + zeileSplit[0].trim(), "E");
                lAktienregister.anredeId = zeileSplit[2].isBlank() ? 2 : 0;

                if (lAktienregister.anredeId == 2) {

                    lAktienregister.name1 = zeileSplit[1].trim();

                } else {

                    lAktienregister.nachname = zeileSplit[1].trim();
                    lAktienregister.vorname = zeileSplit[2].trim();
                    lAktienregister.titel = zeileSplit[3].trim();

                }

                lAktienregister.gattungId = 1;
                lAktienregister.strasse = zeileSplit[4].trim();
                lAktienregister.postleitzahl = helErstellePlz(zeileSplit[5].trim(), zeileSplit[7].trim());
                lAktienregister.ort = zeileSplit[6].trim();
                lAktienregister.staatId = helFindeStaatIdNeu(zeileSplit[7].trim());
                lAktienregister.stimmen = Long.parseLong(zeileSplit[8].trim());
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                lAktienregister.nameKomplett = nameKomplett(lAktienregister);
                lAktienregister.besitzart = "E";

                helAdresszeilenAufbereiten(lAktienregister, false);

                lAktienregister.versandNummer = Integer.parseInt(versandNummer);
                bestand += lAktienregister.stimmen;

                registerNew.add(lAktienregister);

            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku1005(List<String> list, String versandNummer) throws Exception {
        // Lfd;Anrede;Vorname;Nachname;Geb;Straße;PLZ;Ort;Stamm;Vorzug
        // 

        registerNew = new ArrayList<>();

        for (String zeile : list) {

            if (!zeile.startsWith("Lfd")) {

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                String anr = zeileSplit[0].trim();
                anr = anr.length() == 1 ? "00" + anr : "0" + anr;

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerNeu(anr, "E");
                lAktienregister.anredeId = zeileSplit[1].contains("Firma") ? 2 : 0;

                if (lAktienregister.anredeId == 2) {

                    lAktienregister.name1 = zeileSplit[3].trim();
                    lAktienregister.istJuristischePerson = 1;

                } else {

                    lAktienregister.vorname = zeileSplit[2].trim();
                    lAktienregister.nachname = zeileSplit[3].trim();

                }

                lAktienregister.gattungId = 1;
                lAktienregister.strasse = zeileSplit[5].trim();
                lAktienregister.postleitzahl = zeileSplit[6].trim();
                lAktienregister.ort = zeileSplit[7].trim();
                lAktienregister.staatId = 0;
                lAktienregister.stimmen = !zeileSplit[8].isBlank() ? Long.parseLong(zeileSplit[8].trim()) : 0;
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                lAktienregister.nameKomplett = nameKomplett(lAktienregister);
                lAktienregister.besitzart = "E";

                helAdresszeilenAufbereiten(lAktienregister, false);

                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                if (!zeileSplit[9].isBlank()) {

                    EclAktienregister tmpAktienregister = EclAktienregister.copyEntity(lAktienregister);

                    tmpAktienregister.aktionaersnummer = helAktionaersnummerNeu("5" + anr, "E");
                    tmpAktienregister.gattungId = 2;
                    tmpAktienregister.stimmen = !zeileSplit[9].isBlank() ? Long.parseLong(zeileSplit[9].trim()) : 0;
                    tmpAktienregister.stueckAktien = tmpAktienregister.stimmen;

                    if (tmpAktienregister.stimmen != 0) {
                        bestand += tmpAktienregister.stimmen;
                        registerNew.add(tmpAktienregister);
                    }
                }

                if (lAktienregister.stimmen != 0) {
                    bestand += lAktienregister.stimmen;
                    registerNew.add(lAktienregister);
                }
            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku243(List<String> list, String versandNummer) throws Exception {
        registerNew = new ArrayList<>();

        for (String zeile : list) {

            if (!zeile.startsWith("Mitgliedsnummer")) {

                EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerku178(zeileSplit[0].trim());
                lAktienregister.anredeId = helFindeAnredeku243(zeileSplit[1].trim());
                lAktienregister.nachname = helNachnameku178(zeileSplit[3].trim(), zeileSplit[11].trim());
                lAktienregister.nachname += helName1ku178(zeileSplit[3].trim(), zeileSplit[11].trim());
                if (lAktienregister.nachname.length() > 80) {
                    lAktienregister.nachname = lAktienregister.nachname.substring(0, 80);
                }
                lAktienregister.vorname = zeileSplit[2].trim();
                lAktienregister.strasse = zeileSplit[4].trim();
                lAktienregister.postleitzahl = helPLZku178(zeileSplit[6].trim(), zeileSplit[8].trim());
                lAktienregister.ort = zeileSplit[7].trim();
                lAktienregister.staatId = helFindeStaatByName(zeileSplit[8].trim());
                lAktienregister.anredeIdVersand = helFindeAnredeku243(zeileSplit[13].trim());
                lAktienregister.nachnameVersand = zeileSplit[15].trim();
                if (lAktienregister.nachnameVersand.length() > 80) {
                    lAktienregister.nachnameVersand = lAktienregister.nachnameVersand.substring(0, 80);
                }
                lAktienregister.vornameVersand = zeileSplit[14].trim().isBlank() ? "" : zeileSplit[14].trim();
                lAktienregister.strasseVersand = zeileSplit[16].trim();
                lAktienregister.postleitzahlVersand = helPLZku178(zeileSplit[18].trim(), zeileSplit[20].trim());
                lAktienregister.ortVersand = zeileSplit[19].trim();
                lAktienregister.staatIdVersand = helFindeStaatByName(zeileSplit[20].trim());
                lAktienregister.nameKomplett = helCheckNameKomplettku178(lAktienregister.vorname,
                        lAktienregister.nachname, lAktienregister.name1);
                lAktienregister.versandAbweichend = helVersandAbweichendku178(zeileSplit[12].trim());
                lAktienregister.stueckAktien = 1;
                lAktienregister.stimmen = 1;
                lAktienregister.besitzart = "E";
                lAktienregister.stimmausschluss = "";
                lAktienregister.gattungId = zeileSplit[23].trim().equals("Ordentliches Mitglied") ? 1
                        : zeileSplit[23].trim().equals("Investierendes Mitglied") ? 2 : 0;
                lAktienregister.gruppe = helGruppe(zeileSplit[11].trim().toLowerCase());

                lAktienregister.emailVersand = zeileSplit[21].trim().equals("1") ? zeileSplit[22].trim() : "";

                final List<String> adresszeilen = helAdresszeilenAufbereitenku178(lAktienregister.vornameVersand,
                        lAktienregister.nachnameVersand, lAktienregister.strasseVersand,
                        lAktienregister.postleitzahlVersand, lAktienregister.ortVersand,
                        helFindeStaatNameNeu(lAktienregister.staatIdVersand), zeileSplit[17].trim());

                lAktienregister.adresszeile1 = adresszeilen.get(0).trim();
                lAktienregister.adresszeile2 = adresszeilen.get(1).trim();
                lAktienregister.adresszeile3 = adresszeilen.get(2).trim();
                lAktienregister.adresszeile4 = adresszeilen.get(3).trim();
                lAktienregister.adresszeile5 = adresszeilen.get(4).trim();
                lAktienregister.adresszeile6 = adresszeilen.get(5).trim();

                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                lAktienregisterErgaenzung.mandant = lAktienregister.mandant;
                lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_GeburtsdatumMitglied] = zeileSplit[9]
                        .trim();
                lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Name] = lAktienregister.name1
                        + lAktienregister.nachname;
                lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Vorname] = lAktienregister.vorname;
                lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_StrasseHausnummer] = lAktienregister.strasse;
                lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_PLZ] = lAktienregister.postleitzahl;
                lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Ort] = lAktienregister.ort;
                lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_Gruppe] = String
                        .valueOf(lAktienregister.gruppe);

                registerNew.add(lAktienregister);

                ergaenzungMap.put(lAktienregister.aktionaersnummer, lAktienregisterErgaenzung);

            }
        }

        return registerNew;

    }

    public List<EclAktienregister> aufbereitenKu219(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        //            int count = 0;

        for (String zeile : list) {
            //                System.out.println("Line: " + ++count);

            if (!zeile.startsWith("Mitglied")) {

                EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerKu219(zeileSplit[0].trim());
                //              Firma
                lAktienregister.name1 = zeileSplit[6].trim();
                lAktienregister.nameKomplett = lAktienregister.name1;
                lAktienregister.name2 = zeileSplit[5].trim();
                lAktienregister.titel = zeileSplit[2].trim();
                lAktienregister.nachname = zeileSplit[4].trim();
                lAktienregister.strasse = zeileSplit[7].trim();
                lAktienregister.postleitzahl = helPLZku178(zeileSplit[8].trim(), zeileSplit[10].trim());
                lAktienregister.ort = zeileSplit[9].trim();
                lAktienregister.staatId = helFindeStaatByName(zeileSplit[10].trim());
                lAktienregister.staatIdVersand = lAktienregister.staatId;
                lAktienregister.anredeId = helFindeAnredeKu219(zeileSplit[1].trim());
                lAktienregister.anredeIdVersand = lAktienregister.anredeId;

                lAktienregister.istJuristischePerson = 1;

                lAktienregister.gattungId = 1;
                lAktienregister.stueckAktien = 1;
                lAktienregister.stimmen = 1;
                lAktienregister.besitzart = "E";
                lAktienregister.stimmausschluss = "";
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                lAktienregister.gruppe = 1;

                lAktienregisterErgaenzung.mandant = lAktienregister.mandant;
                //                      Nachname
                lAktienregisterErgaenzung.ergaenzungLangString[24] = zeileSplit[4].trim();
                //                      Titel + Vorname
                lAktienregisterErgaenzung.ergaenzungLangString[25] = zeileSplit[2].isBlank() ? zeileSplit[3].trim()
                        : zeileSplit[2].trim() + " " + zeileSplit[3].trim();
                //                      Ort wenn vorhanden
                lAktienregisterErgaenzung.ergaenzungLangString[26] = zeileSplit[9].trim();

                List<String> adresszeilen = new ArrayList<>();

                adresszeilen.add(lAktienregister.name1);
                adresszeilen.add(lAktienregister.name2);
                adresszeilen.add((lAktienregisterErgaenzung.ergaenzungLangString[25] + " "
                        + lAktienregisterErgaenzung.ergaenzungLangString[24]).trim());
                adresszeilen.add(lAktienregister.strasse);
                adresszeilen.add(lAktienregister.postleitzahl + " " + lAktienregister.ort);
                adresszeilen.add(lAktienregister.staatId != 56 ? helFindeStaatNameNeu(lAktienregister.staatId) : "");

                adresszeilen = adresszeilen.stream().filter(e -> !e.isBlank()).collect(Collectors.toList());

                final int size = adresszeilen.size();

                lAktienregister.adresszeile1 = size > 0 ? adresszeilen.get(0) : "";
                lAktienregister.adresszeile2 = size > 1 ? adresszeilen.get(1) : "";
                lAktienregister.adresszeile3 = size > 2 ? adresszeilen.get(2) : "";
                lAktienregister.adresszeile4 = size > 3 ? adresszeilen.get(3) : "";
                lAktienregister.adresszeile5 = size > 4 ? adresszeilen.get(4) : "";
                lAktienregister.adresszeile6 = size > 5 ? adresszeilen.get(5) : "";

                bestand += lAktienregister.stimmen;

                registerNew.add(lAktienregister);

                ergaenzungMap.put(lAktienregister.aktionaersnummer, lAktienregisterErgaenzung);
            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku217(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        for (String zeile : list) {
            //          System.out.println("Line: " + ++count);

            if (list.indexOf(zeile) != 0) {

                EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();
                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;

                lAktienregister.aktionaersnummer = zeileSplit[3].trim().replace("-", "");

                if (zeileSplit[5].trim().contains("Stimmberechtigt")) {
                    lAktienregister.gattungId = 1;
                } else if (zeileSplit[5].trim().contains("Gast")) {
                    lAktienregister.gattungId = 2;
                } else {
                    lAktienregister.gattungId = 4;
                }

                lAktienregister.anredeId = 1;

                lAktienregister.titel = zeileSplit[9].trim();
                lAktienregister.vorname = zeileSplit[10].trim();
                lAktienregister.nachname = zeileSplit[11].trim();

                lAktienregister.stimmen = Long.parseLong(lAktienregister.gattungId == 1 ? zeileSplit[12].trim() : "1");
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                lAktienregister.name1 = zeileSplit[14].trim();
                lAktienregister.email = zeileSplit[15].trim();
                lAktienregister.postleitzahl = zeileSplit[16].trim();
                lAktienregister.ort = zeileSplit[17].trim();

                lAktienregister.nameKomplett = lAktienregister.name1;

                lAktienregister.besitzart = "E";
                lAktienregister.gruppe = 1;
                lAktienregister.istJuristischePerson = 1;
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                lAktienregisterErgaenzung.mandant = lAktienregister.mandant;
                lAktienregisterErgaenzung.ergaenzungLangString[24] = lAktienregister.nachname;
                lAktienregisterErgaenzung.ergaenzungLangString[25] = (lAktienregister.titel + " "
                        + lAktienregister.vorname).trim();
                lAktienregisterErgaenzung.ergaenzungLangString[26] = lAktienregister.ort;

                if (lAktienregister.gattungId == 1)
                    bestand += lAktienregister.stimmen;

                registerNew.add(lAktienregister);

                ergaenzungMap.put(lAktienregister.aktionaersnummer, lAktienregisterErgaenzung);
            }
        }
        return registerNew;
    }

    /*
     * 
     */
    public List<EclAktienregister> aufbereitenku217_2(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        for (String zeile : list) {
            //          System.out.println("Line: " + ++count);

            if (!zeile.startsWith("#")) {

                System.out.println(zeile);

                EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;

                lAktienregister.anredeId = 0;
                lAktienregister.name1 = zeileSplit[2].trim();
                lAktienregister.stimmen = Long.parseLong(zeileSplit[4].trim());
                lAktienregister.stueckAktien = lAktienregister.stimmen;
                lAktienregister.aktionaersnummer = helAktionaersnummerku178(zeileSplit[5].trim().replace(".", ""));
                lAktienregister.titel = zeileSplit[7].trim();
                lAktienregister.vorname = zeileSplit[9].trim();
                lAktienregister.nachname = (zeileSplit[8].trim() + " " + zeileSplit[10].trim()).trim();
                //                lAktienregister.strasse = zeileSplit[11].trim();
                //                lAktienregister.postleitzahl = zeileSplit[12].trim();
                lAktienregister.ort = zeileSplit[13].trim();
                lAktienregister.staatId = 56;
                lAktienregister.email = zeileSplit[15].trim();

                lAktienregister.gattungId = 1;
                lAktienregister.gruppe = 1;
                lAktienregister.istJuristischePerson = 1;

                lAktienregister.nameKomplett = lAktienregister.name1;

                lAktienregister.besitzart = "E";
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                lAktienregisterErgaenzung.mandant = lAktienregister.mandant;
                lAktienregisterErgaenzung.ergaenzungLangString[24] = lAktienregister.nachname.trim();
                lAktienregisterErgaenzung.ergaenzungLangString[25] = lAktienregister.vorname.trim();
                lAktienregisterErgaenzung.ergaenzungLangString[26] = lAktienregister.ort;

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);

                ergaenzungMap.put(lAktienregister.aktionaersnummer, lAktienregisterErgaenzung);
            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenKu1006(List<String> list, String versandnummer) throws Exception {

        registerNew = new ArrayList<>();

        for (String zeile : list) {
            if (!zeile.startsWith("Partner")) {

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerNeu(zeileSplit[0].trim(), "E");
                lAktienregister.stimmen = Integer.parseInt(zeileSplit[1].trim());
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                int le1 = zeileSplit[3].trim().length();
                int le2 = zeileSplit[4].trim().length();
                int le3 = zeileSplit[5].trim().length();

                if (le1 + le2 + le3 < 65)
                    lAktienregister.nachname = (zeileSplit[3].trim() + " " + zeileSplit[4].trim() + " "
                            + zeileSplit[5].trim()).trim();
                else if (le1 + le2 < 65) {
                    lAktienregister.nachname = (zeileSplit[3].trim() + " " + zeileSplit[4].trim()).trim();
                    lAktienregister.vorname = zeileSplit[5].trim();
                } else {
                    lAktienregister.nachname = zeileSplit[3].trim();
                    if (le2 + le3 < 65)
                        lAktienregister.vorname = (zeileSplit[4].trim() + " " + zeileSplit[5].trim()).trim();
                    else {
                        lAktienregister.vorname = zeileSplit[4].trim();
                        lAktienregister.name3 = zeileSplit[5].trim();
                    }
                }

                lAktienregister.nameKomplett = (lAktienregister.nachname + " " + lAktienregister.vorname + " "
                        + lAktienregister.name3).trim();

                lAktienregister.zusatz = zeileSplit[6].trim();
                lAktienregister.strasse = zeileSplit[7].trim();
                lAktienregister.postleitzahl = helErstellePlz(zeileSplit[9].trim(),
                        zeileSplit[8].trim().isBlank() ? "DE" : zeileSplit[8].trim());
                lAktienregister.ort = zeileSplit[10].trim();
                lAktienregister.staatId = helFindeStaatIdNeu(
                        zeileSplit[8].trim().isBlank() ? "DE" : zeileSplit[8].trim());

                lAktienregister.email = zeileSplit[13].trim();

                lAktienregister.gattungId = 1;
                lAktienregister.besitzart = "E";
                lAktienregister.versandNummer = Integer.parseInt(versandnummer);

                List<String> adresszeilen = new ArrayList<>();

                adresszeilen.add(zeileSplit[2].trim());
                adresszeilen.add(lAktienregister.nachname);
                adresszeilen.add(lAktienregister.vorname);
                adresszeilen.add(lAktienregister.name3);
                adresszeilen.add(lAktienregister.zusatz);
                adresszeilen.add(lAktienregister.strasse);
                adresszeilen.add((lAktienregister.postleitzahl + " " + lAktienregister.ort).trim());
                adresszeilen.add(helFindeStaatNameNeu(lAktienregister.getStaatId()));

                adresszeilen = adresszeilen.stream().filter(e -> !e.isBlank()).collect(Collectors.toList());

                while (adresszeilen.size() < 7)
                    adresszeilen.add("");

                lAktienregister.adresszeile1 = adresszeilen.get(0);
                lAktienregister.adresszeile2 = adresszeilen.get(1);
                lAktienregister.adresszeile3 = adresszeilen.get(2);
                lAktienregister.adresszeile4 = adresszeilen.get(3);
                lAktienregister.adresszeile5 = adresszeilen.get(4);
                lAktienregister.adresszeile6 = adresszeilen.get(5);
                lAktienregister.adresszeile7 = adresszeilen.get(6);

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);
            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku1001(List<String> list, String versandnummer) throws Exception {

        registerNew = new ArrayList<>();

        int anr = 8000;

        for (String zeile : list) {
            if (!zeile.startsWith("Account")) {

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = String.valueOf(anr++);
                lAktienregister.stimmen = Integer.parseInt(zeileSplit[8].trim().replaceAll("\\D+", ""));
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                lAktienregister.nachname = zeileSplit[0].trim() + " - " + zeileSplit[1].trim();
                lAktienregister.vorname = zeileSplit[2].trim();

                lAktienregister.nameKomplett = (lAktienregister.nachname + " " + lAktienregister.vorname).trim();

                lAktienregister.strasse = zeileSplit[3].trim();
                lAktienregister.ort = (zeileSplit[4].trim() + " " + zeileSplit[5].trim()).trim();

                lAktienregister.staatId = 1;
                lAktienregister.gattungId = 2;

                lAktienregister.adresszeile1 = (lAktienregister.vorname + " " + lAktienregister.nachname).trim();
                lAktienregister.adresszeile2 = lAktienregister.strasse;
                lAktienregister.adresszeile3 = lAktienregister.ort;
                lAktienregister.adresszeile4 = zeileSplit[6].trim();

                lAktienregister.adresszeile9 = Integer.parseInt(zeileSplit[7].trim().replaceAll("\\D+", "")) + "";
                lAktienregister.adresszeile10 = zeileSplit[0].trim();

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);
            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku302_303(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        System.out.println(list.size());

        for (String zeile : list) {
            if (!zeile.startsWith("ANR")) {

                EclAktienregister lAktienregister = new EclAktienregister();

                System.out.println(zeile);

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerNeu(
                        String.valueOf(Integer.parseInt(zeileSplit[0].trim())), "E");

                lAktienregister.adresszeile10 = String.valueOf(Integer.parseInt(zeileSplit[1].trim()));

                lAktienregister.anredeId = helFindeAnrede(zeileSplit[2].trim());

                if (lAktienregister.anredeId == 2) {

                    lAktienregister.name1 = zeileSplit[6].replaceAll("\"", "").trim();
                    lAktienregister.istJuristischePerson = 1;
                    lAktienregister.nameKomplett = lAktienregister.name1;

                } else {

                    lAktienregister.titel = zeileSplit[3].trim();
                    lAktienregister.vorname = zeileSplit[5].trim();
                    lAktienregister.nachname = (zeileSplit[4].trim() + " " + zeileSplit[6].trim()).trim();
                    lAktienregister.nameKomplett = nameKomplett(lAktienregister);

                }

                lAktienregister.zusatz = zeileSplit[7].trim();
                lAktienregister.strasse = zeileSplit[8].trim().startsWith("Nr.")
                        ? (zeileSplit[11].trim() + " " + zeileSplit[8].trim()).trim()
                        : zeileSplit[8].trim();

                lAktienregister.postleitzahl = helErstellePlz(zeileSplit[9].trim(), zeileSplit[12].trim());
                lAktienregister.ort = zeileSplit[10].trim();
                lAktienregister.staatId = helFindeStaatIdNeu(zeileSplit[12].trim());

                lAktienregister.stimmen = Integer.parseInt(zeileSplit[13].trim());
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                lAktienregister.email = zeileSplit[16].trim();

                lAktienregister.staatIdVersand = lAktienregister.staatId;

                lAktienregister.besitzart = "E";
                lAktienregister.gattungId = 1;
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                List<String> adresszeilen = new ArrayList<>();

                adresszeilen.add(lAktienregister.nameKomplett);
                adresszeilen.add(lAktienregister.zusatz);
                adresszeilen.add(lAktienregister.strasse);
                adresszeilen.add((lAktienregister.postleitzahl + " " + lAktienregister.ort).trim());
                adresszeilen.add(helFindeStaatNameNeu(lAktienregister.staatId));

                adresszeilen = adresszeilen.stream().filter(e -> !e.isBlank()).collect(Collectors.toList());

                final int size = adresszeilen.size();

                lAktienregister.adresszeile1 = size > 0 ? adresszeilen.get(0) : "";
                lAktienregister.adresszeile2 = size > 1 ? adresszeilen.get(1) : "";
                lAktienregister.adresszeile3 = size > 2 ? adresszeilen.get(2) : "";
                lAktienregister.adresszeile4 = size > 3 ? adresszeilen.get(3) : "";
                lAktienregister.adresszeile5 = size > 4 ? adresszeilen.get(4) : "";
                lAktienregister.adresszeile6 = size > 5 ? adresszeilen.get(5) : "";

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);

            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku1009(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        int anr = 1;

        final String jahr = ParamS.eclEmittent.bezeichnungKurz
                .substring(ParamS.eclEmittent.bezeichnungKurz.length() - 4).trim();

        for (String zeile : list) {
            if (!zeile.startsWith("A1-Nummer")) {

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;

                lAktienregister.zusatz = zeileSplit[0].trim();

                if (lAktienregister.zusatz.equals("A1 05681"))
                    lAktienregister.aktionaersnummer = helAktionaersnummerNeu("12317" + jahr, "E");
                else
                    lAktienregister.aktionaersnummer = helAktionaersnummerNeu(anr + jahr, "E");

                lAktienregister.zusatz = zeileSplit[0].trim();

                lAktienregister.nachname = zeileSplit[1].trim();
                lAktienregister.ort = zeileSplit[3].trim();
                lAktienregister.postleitzahl = helErstellePlz(zeileSplit[4].trim(), zeileSplit[6].trim());
                lAktienregister.strasse = zeileSplit[5].trim();
                lAktienregister.staatId = helFindeStaatIdNeu(zeileSplit[6].trim());

                lAktienregister.stimmen = Integer.parseInt(zeileSplit[7].trim().replaceAll("\\D+", ""));
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                lAktienregister.nachname = zeileSplit[1].trim();
                lAktienregister.vorname = zeileSplit[2].trim();

                lAktienregister.nameKomplett = lAktienregister.nachname;

                lAktienregister.gattungId = 1;
                lAktienregister.besitzart = "E";
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                List<String> adresszeilen = new ArrayList<>();

                adresszeilen.add(lAktienregister.nachname);
                adresszeilen.add(zeileSplit[2].trim());
                adresszeilen.add(lAktienregister.strasse);
                adresszeilen.add((lAktienregister.postleitzahl + " " + lAktienregister.ort).trim());
                adresszeilen.add(helFindeStaatNameNeu(lAktienregister.staatId));

                adresszeilen = adresszeilen.stream().filter(e -> !e.isBlank()).collect(Collectors.toList());

                while (adresszeilen.size() < 5)
                    adresszeilen.add("");

                lAktienregister.adresszeile1 = adresszeilen.get(0);
                lAktienregister.adresszeile2 = adresszeilen.get(1);
                lAktienregister.adresszeile3 = adresszeilen.get(2);
                lAktienregister.adresszeile4 = adresszeilen.get(3);
                lAktienregister.adresszeile5 = adresszeilen.get(4);

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);

                anr++;
            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku1002(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        for (String zeile : list) {
            if (!zeile.startsWith("Anzahl")) {

                EclAktienregister lAktienregister = new EclAktienregister();

                System.out.println(zeile);

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerNeu(zeileSplit[1], "E");

                lAktienregister.stimmen = Integer.parseInt(zeileSplit[0].trim());
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                lAktienregister.anredeId = anrIdku108(zeileSplit[4].trim());

                if (lAktienregister.anredeId == 2) {
                    lAktienregister.name1 = zeileSplit[2].trim();
                    lAktienregister.name2 = zeileSplit[3].trim();
                    lAktienregister.nameKomplett = nameKomplett(lAktienregister);
                } else {
                    lAktienregister.titel = zeileSplit[5].trim();
                    lAktienregister.vorname = zeileSplit[6].trim();
                    lAktienregister.nachname = zeileSplit[7].trim();
                    lAktienregister.zusatz = zeileSplit[2].trim();
                    lAktienregister.nameKomplett = nameKomplett(lAktienregister);
                }

                lAktienregister.staatId = helFindeStaatByName(zeileSplit[11].trim());

                lAktienregister.strasse = zeileSplit[8].trim();
                lAktienregister.postleitzahl = zeileSplit[9].trim();
                lAktienregister.ort = zeileSplit[10].trim();

                lAktienregister.istJuristischePerson = lAktienregister.anredeId == 2 ? 1 : 0;
                lAktienregister.besitzart = "E";
                lAktienregister.stimmausschluss = "";
                lAktienregister.gattungId = 1;
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                helAdresszeilenAufbereiten(lAktienregister, false);

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);

            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku1004(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        System.out.println(list.size());

        for (String zeile : list) {
            if (!zeile.startsWith("EK")) {

                EclAktienregister lAktienregister = new EclAktienregister();

                String[] zeileSplit = zeile.split(";");

                int ek = Integer.parseInt(zeileSplit[0].trim());

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = helAktionaersnummerNeu(zeileSplit[0].trim(), "E");

                lAktienregister.adresszeile10 = String.valueOf(Integer.parseInt(zeileSplit[1].trim()));

                lAktienregister.anredeId = 0;
                lAktienregister.nachname = (zeileSplit[5].trim() + " " + zeileSplit[2].trim()).trim();
                lAktienregister.nachname = zeileSplit[2].trim();
                lAktienregister.vorname = zeileSplit[3].trim();
                lAktienregister.titel = zeileSplit[4].trim();
                lAktienregister.zusatz = zeileSplit[10].trim();
                lAktienregister.postleitzahl = zeileSplit[11].trim();
                lAktienregister.ort = zeileSplit[12].trim();
                lAktienregister.strasse = zeileSplit[13].trim();
                lAktienregister.staatId = helFindeStaatIdNeu(zeileSplit[14].trim());

                lAktienregister.stimmen = 1;
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                lAktienregister.besitzart = "E";
                lAktienregister.gattungId = ek < 5000 ? 1 : 2;
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                helAdresszeilenAufbereiten(lAktienregister, false);

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);

            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku108(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        for (String zeile : list) {
            if (!zeile.startsWith("Kunden")) {

                EclAktienregister lAktienregister = new EclAktienregister();

                System.out.println(zeile);

                String[] zeileSplit = zeile.split(";");

                lAktienregister.mandant = ParamS.clGlobalVar.mandant;
                lAktienregister.aktionaersnummer = String.valueOf(Integer.parseInt(zeileSplit[0].trim()));

                lAktienregister.stimmen = Integer.parseInt(zeileSplit[1].trim().replaceAll("\\D+", ""));
                lAktienregister.stueckAktien = lAktienregister.stimmen;

                lAktienregister.anredeId = anrIdku108(zeileSplit[2].trim());

                if (lAktienregister.anredeId == 2) {
                    lAktienregister.name1 = zeileSplit[3].trim();
                    lAktienregister.name2 = zeileSplit[4].trim();
                    lAktienregister.name3 = zeileSplit[5].trim();
                } else {
                    lAktienregister.nachname = zeileSplit[3].trim();
                    lAktienregister.vorname = zeileSplit[4].trim();
                    lAktienregister.name3 = zeileSplit[5].trim();
                }

                lAktienregister.staatId = staatku108(zeileSplit[9].trim());

                lAktienregister.strasse = zeileSplit[6].trim();
                lAktienregister.postleitzahl = lAktienregister.staatId == 0 ? "" : zeileSplit[7].trim();
                lAktienregister.ort = lAktienregister.postleitzahl.isBlank()
                        ? (zeileSplit[7].trim() + " " + zeileSplit[8].trim()).trim()
                        : zeileSplit[8].trim();

                lAktienregister.adresszeile9 = zeileSplit[2].trim();
                lAktienregister.adresszeile10 = (zeileSplit[10].trim() + " " + zeileSplit[11].trim()).trim();

                lAktienregister.nameKomplett = zeileSplit[3].trim()
                        + (zeileSplit[4].trim().isBlank() ? "" : ", " + zeileSplit[4].trim());
                lAktienregister.istJuristischePerson = lAktienregister.anredeId == 2 ? 1 : 0;
                lAktienregister.besitzart = "E";
                lAktienregister.stimmausschluss = "";
                lAktienregister.gattungId = 2;
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                List<String> adresszeilen = new ArrayList<>();

                adresszeilen.add(zeileSplit[3].trim());
                adresszeilen.add(zeileSplit[4].trim());
                adresszeilen.add(zeileSplit[5].trim());
                adresszeilen.add(lAktienregister.strasse);
                adresszeilen.add((lAktienregister.postleitzahl + " " + lAktienregister.ort).trim());
                adresszeilen.add(zeileSplit[9].trim());

                adresszeilen = adresszeilen.stream().filter(e -> !e.isBlank()).collect(Collectors.toList());

                final int size = adresszeilen.size();

                lAktienregister.adresszeile1 = size > 0 ? adresszeilen.get(0) : "";
                lAktienregister.adresszeile2 = size > 1 ? adresszeilen.get(1) : "";
                lAktienregister.adresszeile3 = size > 2 ? adresszeilen.get(2) : "";
                lAktienregister.adresszeile4 = size > 3 ? adresszeilen.get(3) : "";
                lAktienregister.adresszeile5 = size > 4 ? adresszeilen.get(4) : "";
                lAktienregister.adresszeile6 = size > 5 ? adresszeilen.get(5) : "";

                bestand += lAktienregister.stimmen;
                registerNew.add(lAktienregister);

            }
        }
        return registerNew;
    }

    private int staatku108(String str) {

        if (str.isBlank())
            return 56;
        else {
            return 0;
        }
    }

    private int anrIdku108(String str) {

        if (str.contains("Frau") && str.contains("Herr"))
            return 4;
        else if (str.contains("Frau"))
            return 3;
        else if (str.contains("Herr"))
            return 1;
        else
            return 2;
    }

    /*
     * Start ku216
     */
    public List<EclAktienregister> aufbereitenku216_alt(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();

        for (String zeile : list) {

            EclAktienregister lAktienregister = new EclAktienregister();

            String[] zeileSplit = zeile.split(";");

            lAktienregister.mandant = ParamS.clGlobalVar.mandant;
            lAktienregister.aktionaersnummer = helAktionaersnummerku216(zeileSplit[1].trim());
            lAktienregister.anredeId = 1;
            String[] nameArray = helNameku216(zeileSplit[3].trim());
            lAktienregister.name1 = nameArray[0];
            lAktienregister.name2 = nameArray[1];
            lAktienregister.name3 = nameArray[2];
            lAktienregister.email = zeileSplit[2].trim();
            lAktienregister.nameKomplett = zeileSplit[3].trim();
            lAktienregister.gattungId = 1;
            lAktienregister.stueckAktien = 1;
            lAktienregister.stimmen = 1;
            lAktienregister.besitzart = "E";
            lAktienregister.gruppe = 1;
            lAktienregister.versandNummer = Integer.parseInt(versandNummer);

            registerNew.add(lAktienregister);

            if (list.indexOf(zeile) == 0) {
                System.out.println(zeileSplit[3].trim());
                System.out.println(zeile);
                System.out.println(Arrays.toString(zeileSplit));
                System.out.println(lAktienregister.toString());
            }
        }
        return registerNew;
    }

    public List<EclAktienregister> aufbereitenku216_2022(List<String> list, String versandNummer) throws Exception {

        registerNew = new ArrayList<>();
        List<String> anrList = new ArrayList<>();
        ergaenzungMap.clear();

        int i = 1;

        for (String zeile : list) {

            if (!zeile.startsWith("Mitglied")) {

                String[] zeileSplit = zeile.split(";");

                if (i % 5000 == 0)
                    System.out.println(i);

                final String anr = helAktionaersnummerku216(zeileSplit[0].trim());

                anrList.add(anr);

                EclAktienregisterErgaenzung lAktienregisterErgaenzung = new EclAktienregisterErgaenzung();

                EclAktienregister lAktienregister = new EclAktienregister();

                lAktienregister.mandant = 216;
                lAktienregister.aktionaersnummer = anr;
                lAktienregister.vorname = zeileSplit[2].trim();
                lAktienregister.nachname = zeileSplit[3].trim();
                lAktienregister.name1 = zeileSplit[3].trim();
                lAktienregister.strasse = zeileSplit[4].trim();
                lAktienregister.postleitzahl = zeileSplit[6].trim();
                lAktienregister.ort = zeileSplit[7].trim();
                lAktienregister.email = zeileSplit[14].trim();

                lAktienregister.nameKomplett = nameKomplett(lAktienregister);

                lAktienregister.adresszeile10 = zeileSplit[14].trim();

                int gruppe = 0;

                if (zeileSplit[13].trim().toLowerCase().equals("juristische person"))
                    gruppe = 6;
                else if (zeileSplit[8].trim().toLowerCase().equals("minderjährig"))
                    gruppe = 3;
                else if (zeileSplit[10].trim().toLowerCase().equals("nachlass"))
                    gruppe = 5;
                else if (zeileSplit[11].trim().toLowerCase().equals("unter betreuung"))
                    gruppe = 7;
                else
                    gruppe = 1;

                lAktienregister.gruppe = gruppe;

                if (gruppe == 6) {

                    lAktienregister.name1 = zeileSplit[2].trim();
                    lAktienregister.name2 = zeileSplit[3].trim();

                }

                lAktienregister.gattungId = 1;
                lAktienregister.stueckAktien = 1;
                lAktienregister.stimmen = 1;
                lAktienregister.besitzart = "E";
                lAktienregister.versandNummer = Integer.parseInt(versandNummer);

                lAktienregisterErgaenzung.mandant = lAktienregister.mandant;
                lAktienregisterErgaenzung.ergaenzungLangString[KonstAktienregisterErgaenzung.ku178_GeburtsdatumMitglied] = zeileSplit[14]
                        .trim();

                registerNew.add(lAktienregister);

                ergaenzungMap.put(lAktienregister.aktionaersnummer, lAktienregisterErgaenzung);

            }
            i++;
        }

        deleteDuplicates(anrList);

        return registerNew;

    }

    public String helAktionaersnummerku216(String nummer) {
        nummer += "0";
        while (nummer.length() < 11) {
            nummer = "0" + nummer;
        }
        return nummer;
    }

    public String[] helNameku216(String name) {
        String[] nameArray = new String[3];
        String name1 = "";
        String name2 = "";
        String name3 = "";
        if (name.length() <= 80) {
            name1 = name;
        } else if (name.length() > 80 && name.length() <= 160) {
            name1 = name.substring(0, 80);
            name2 = name.substring(80, name.length());
        } else {
            name1 = name.substring(0, 80);
            name2 = name.substring(80, 80);
            name3 = name.substring(160, name.length());
        }
        nameArray[0] = name1;
        nameArray[1] = name2;
        nameArray[2] = name3;
        return nameArray;
    }

    /*
     * Ende ku216
     */

    /*
     * Anfang ku217
     */

    public String[] helNameku217(String name) {
        String[] nameArray = new String[2];
        String name1 = "";
        String name2 = "";
        if (name.length() <= 80) {
            name1 = name;
        } else {
            name1 = name.substring(0, 80);
            name2 = name.substring(80, name.length());
        }
        nameArray[0] = name1;
        nameArray[1] = name2;
        return nameArray;
    }

    /*
     * Ende ku217
     */

    private void checkDuplicates(List<String> anrList) {

        Set<String> duplicates = findDuplicateBySetAdd(anrList);

        if (duplicates.size() > 0) {

            System.out.println("Es wurden " + duplicates.size() + " doppelte Aktionärsnummern gefunden");

            int count = 0;

            for (String data : duplicates) {

                List<EclAktienregister> tmpList = registerNew.stream().filter(e -> e.aktionaersnummer.equals(data))
                        .collect(Collectors.toList());

                count += tmpList.size();

                if (tmpList.size() > 1) {

                    Comparator<EclAktienregister> shareComparator = Comparator
                            .comparing(EclAktienregister::getStueckAktien);
                    Collections.sort(tmpList, shareComparator);

                    long sum = tmpList.stream().mapToLong(EclAktienregister::getStueckAktien).sum();

                    registerNew.get(registerNew.indexOf(tmpList.get(0))).stueckAktien = sum;
                    registerNew.get(registerNew.indexOf(tmpList.get(0))).stimmen = sum;

                    for (int i = 1; i < tmpList.size(); i++) {

                        registerNew.remove(tmpList.get(i));

                    }
                }
            }
            System.out.println("Es wurden " + count + " auf " + duplicates.size() + " zusammengefasst");
        }
    }

    private void deleteDuplicates(List<String> anrList) {

        Set<String> duplicates = findDuplicateBySetAdd(anrList);

        if (duplicates.size() > 0) {

            System.out.println("Es wurden " + duplicates.size() + " doppelte Aktionärsnummern gefunden");

            int count = 0;

            int x = 0;

            for (String data : duplicates) {

                if (x % 300 == 0)
                    System.out.println(x);

                List<EclAktienregister> tmpList = registerNew.stream().filter(e -> e.aktionaersnummer.equals(data))
                        .collect(Collectors.toList());

                count += tmpList.size();

                if (tmpList.size() > 1) {
                    for (int i = 1; i < tmpList.size(); i++)
                        registerNew.remove(tmpList.get(i));
                }
                x++;
            }
            System.out.println("Es wurden " + count + " auf " + duplicates.size() + " zusammengefasst");
        }
    }

    public <T> Set<T> findDuplicateBySetAdd(List<T> list) {
        Set<T> items = new HashSet<>();
        return list.stream().filter(n -> !items.add(n)).collect(Collectors.toSet());
    }

    /*
     * arfuehrer002 Importcheck, gelegentlich zu lange Felder
     */
    private EclAktienregister checkName(EclAktienregister lAktienregister) {

        lAktienregister.nachname = lAktienregister.nachname.length() > 80 ? lAktienregister.nachname.substring(0, 80)
                : lAktienregister.nachname;
        lAktienregister.vorname = lAktienregister.vorname.length() > 80 ? lAktienregister.vorname.substring(0, 80)
                : lAktienregister.vorname;
        lAktienregister.nachnameVersand = lAktienregister.nachnameVersand.length() > 80
                ? lAktienregister.nachnameVersand.substring(0, 80)
                : lAktienregister.nachnameVersand;
        lAktienregister.vornameVersand = lAktienregister.vornameVersand.length() > 80
                ? lAktienregister.vornameVersand.substring(0, 80)
                : lAktienregister.vornameVersand;

        return lAktienregister;
    }

    public String helCheckStrasseBM(String strasse) {
        String str = "";
        if (!str.contains("Postfach")) {
            str = strasse;
        }
        return str;
    }

    public String helCheckPostfachBM(String postfach) {
        String pf = "";
        if (postfach.contains("Postfach")) {
            pf = postfach.replace("Postfach", "");
            pf = pf.replace("Nachlass-Team", "");
        }
        return pf;
    }

    public int helFindeAnrede(String anrede) {
        int anredeId = 0;
        if ((anrede.equals("1")) || (anrede.equals("001"))) {
            anredeId = 1;
        } else if ((anrede.equals("2")) || (anrede.equals("002"))) {
            anredeId = 3;
        } else if ((anrede.equals("9")) || (anrede.equals("009"))) {
            anredeId = 2;
        } else if ((anrede.equals("8")) || (anrede.equals("008"))) {
            anredeId = 4;
        }
        return anredeId;
    }

    public int helFindeAnredeBM(String anrede) {
        int anredeId = 0;
        if (anrede.equals("Herr")) {
            anredeId = 1;
        } else if (anrede.equals("Frau")) {
            anredeId = 3;
        } else if (anrede.equals("Juristische Person")) {
            anredeId = 2;
        } else if (anrede.equals("Personengemeinschaft")) {
            anredeId = 4;
        }
        return anredeId;
    }

    public String helFindeAnredeById(int anredeId) {
        String anrede = "";
        if (anredeId == 1)
            anrede = "Herr";
        else if (anredeId == 2)
            anrede = ""; // Firma
        else if (anredeId == 3)
            anrede = "Frau";
        return anrede;
    }

    private int helFindeAnredeInhaberImport(int id) {

        switch (id) {
        case 1:
            return 1;
        case 2:
            return 3;
        case 4:
            return 2;
        case 8:
            return 4;
        case 9:
            return 2;
        default:
            return 0;
        }
    }

    public int helFindeAnredeKu219(String anrede) {
        int anredeId = 2;
        if (anrede.contains("Herr")) {
            anredeId = 1;
        } else if (anrede.contains("Frau")) {
            anredeId = 3;
        }
        return anredeId;
    }

    private int helFindeStaatIdNeu(String laendercode) {

        EclStaaten staat = staaten.stream().filter(e -> e.code.equals(laendercode)).findAny().orElse(null);

        return staat == null ? 1 : staat.id;
    }

    private int helFindeStaatByName(String land) {

        if (land.isBlank())
            return 56;

        EclStaaten staat = staaten.stream().filter(e -> e.nameDE.toLowerCase().contains(land.toLowerCase())).findFirst()
                .orElse(null);

        return staat == null ? 1 : staat.id;
    }

    private String helFindeStaatNameNeu(int id) {

        if (id == 56 || id == 1)
            return "";

        EclStaaten staat = staaten.stream().filter(e -> e.id == id).findAny().orElse(null);

        return staat == null ? "" : staat.nameDE;

    }

    public String helCheckTitel(String anrede, String titel_name3) {
        String titel = "";
        if ((!anrede.equals("9")) && (!anrede.equals("009"))) {
            titel = titel_name3;
        }
        return titel;
    }

    public String helCheckName3(String anrede, String titel_name3) {
        String name3 = "";
        if ((anrede.equals("9")) || (anrede.equals("009"))) {
            name3 = titel_name3;
        }
        return name3;
    }

    public String helCheckName3BM(String anrede, String nz_name3) {
        String name3 = "";
        if (!anrede.equals("Juristische Person")) {
            name3 = nz_name3;
        }
        return name3;
    }

    public String helCheckNachname(String anrede, String nachname_name1) {
        String nachname = "";
        if ((!anrede.equals("9")) && (!anrede.equals("009"))) {
            nachname = nachname_name1;
        }
        return nachname;
    }

    public String helCheckName1(String anrede, String nachname_name1) {
        String name1 = "";
        if ((anrede.equals("9")) || (anrede.equals("009"))) {
            name1 = nachname_name1;
        }
        return name1;
    }

    public String helCheckNachnameBM(String anrede, String nachname_name1) {
        String nachname = "";
        if (!anrede.equals("Juristische Person")) {
            nachname = nachname_name1;
        }
        return nachname;
    }

    public String helCheckName1BM(String anrede, String nachname_name1) {
        String name1 = "";
        if (anrede.equals("Juristische Person")) {
            name1 = nachname_name1;
        }
        return name1;
    }

    public String helCheckVorname(String anrede, String vorname_name2) {
        String vorname = "";
        if ((!anrede.equals("9")) && (!anrede.equals("009"))) {
            vorname = vorname_name2;
        }
        return vorname;
    }

    public String helCheckName2(String anrede, String vorname_name2) {
        String name2 = "";
        if ((anrede.equals("9")) || (anrede.equals("009"))) {
            name2 = vorname_name2;
        }
        return name2;
    }

    public String helCheckVornameBM(String anrede, String vorname_name2) {
        String vorname = "";
        if (!anrede.equals("Juristische Person")) {
            vorname = vorname_name2;
        }
        return vorname;
    }

    public String helCheckName2BM(String anrede, String vorname_name2) {
        String name2 = "";
        if (anrede.equals("Juristische Person")) {
            name2 = vorname_name2;
        }
        return name2;
    }

    /**
     * Im aktuellen Registereintrag muss das Feld "istJuristischePerson" vorher gesetzt sein!
     * 
     * @param eintrag Aktueller Registereintrag
     * @return NameKomplett
     */
    public static String nameKomplett(EclAktienregister eintrag) {

        return eintrag.istJuristischePerson == 1
                ? (eintrag.name1 + " " + eintrag.name2 + " " + eintrag.name3).replaceAll("\\s+", " ").trim()
                : (eintrag.titel + " " + eintrag.vorname + " " + eintrag.nachname).replaceAll("\\s+", " ").trim();

    }

    private void checkZusatz(EclAktienregister eintrag, String str1, String str2, String str3) {
        if (eintrag.istJuristischePerson == 1) {
            String zusatz = (str1.trim() + " " + str2.trim() + " " + str3.trim()).trim();

            if (!zusatz.equals(eintrag.nameKomplett) && zusatz.length() < 80)
                eintrag.zusatzVersand = zusatz;

        } else {
            String zusatz = (str3.trim() + " " + str2.trim() + " " + str1.trim()).trim();

            if (!zusatz.equals(eintrag.nameKomplett) && zusatz.length() < 80)
                eintrag.zusatzVersand = zusatz;
        }
    }

    public int helCheckEnglischerVersand(String laendercode) {
        int englischerVersand = 0;
        if ((!laendercode.equals("DE")) && (!laendercode.equals("AT")) && (!laendercode.equals("D"))
                && (!laendercode.equals("Deutschland")) && (!laendercode.equals("Österreich"))) {
            englischerVersand = 1;
        }
        return englischerVersand;
    }

    public int helCheckPersonengemeinschaft(String anrede) {
        int personengemeinschaft = 0;
        if ((anrede.equals("8")) || (anrede.equals("008"))) {
            personengemeinschaft = 1;
        }
        return personengemeinschaft;
    }

    public int helCheckPersonengemeinschaftBM(String anrede) {
        int personengemeinschaft = 0;
        if (anrede.equals("Personengemeinschaft")) {
            personengemeinschaft = 1;
        }
        return personengemeinschaft;
    }

    public int helCheckJurPerson(String anrede) {
        int jurPerson = 0;
        if ((anrede.equals("9")) || (anrede.equals("009"))) {
            jurPerson = 1;
        }
        return jurPerson;
    }

    public int helCheckJurPersonBM(String person) {
        int jurPerson = 0;
        if (person.equals("Juristische Person")) {
            jurPerson = 1;
        }
        return jurPerson;
    }

    public int helVersandAbweichendBM(String versand) {
        int versandAbweichend = 0;
        if ((versand.equals("Versandadresse")) || (versand.equals("Vollmachtsadresse"))) {
            versandAbweichend = 1;
        }
        return versandAbweichend;
    }

    public void helAdresszeilenAufbereiten(EclAktienregister lAktienregister, Boolean anrede) {

        List<String> adresszeilen = new ArrayList<>();

        final Boolean versandAbweichend = lAktienregister.versandAbweichend != 0;

        if (versandAbweichend) {

            if (lAktienregister.istJuristischePerson == 0) {
                if (anrede) {
                    adresszeilen.add(helFindeAnredeById(lAktienregister.anredeIdVersand));
                }

                final String name = (lAktienregister.nachnameVersand + " " + lAktienregister.vornameVersand)
                        .toLowerCase();

                if (name.toLowerCase().contains(co)) {
                    adresszeilen.add(
                            name.startsWith(co) ? lAktienregister.vornameVersand : lAktienregister.nachnameVersand);
                    adresszeilen.add(
                            name.startsWith(co) ? lAktienregister.nachnameVersand : lAktienregister.vornameVersand);
                } else {
                    adresszeilen.add((lAktienregister.titelVersand + " " + lAktienregister.vornameVersand + " "
                            + lAktienregister.nachnameVersand).replaceAll("\\s+", " ").trim());
                }
            } else {

                if (lAktienregister.name2.contains(Konst_Legitimationsaktionaer)) {
                    adresszeilen.add(lAktienregister.name1Versand);
                    adresszeilen.add(lAktienregister.name2Versand);
                } else {
                    adresszeilen.add(lAktienregister.name1Versand);
                    adresszeilen.add((lAktienregister.name2Versand + " " + lAktienregister.name3Versand)
                            .replaceAll("\\s+", " ").trim());
                }
            }

            adresszeilen.add(lAktienregister.zusatzVersand);
            adresszeilen.add(lAktienregister.postfachVersand.isBlank() ? lAktienregister.strasseVersand
                    : "Postfach: " + lAktienregister.postfachVersand);

            final String plz = lAktienregister.postfachVersand.isBlank() ? lAktienregister.postleitzahlVersand
                    : lAktienregister.postleitzahlPostfachVersand.isBlank() ? lAktienregister.postleitzahlVersand
                            : lAktienregister.postleitzahlPostfachVersand;

            final String land = helFindeStaatNameNeu(lAktienregister.getStaatIdVersand());

            if (land.isBlank()) {
                adresszeilen.add((plz + " " + lAktienregister.ortVersand));
            } else {
                adresszeilen.add(plz.isBlank() ? lAktienregister.ortVersand
                        : lAktienregister.ortVersand.contains(plz) ? lAktienregister.ortVersand
                                : (plz + " " + lAktienregister.ortVersand).trim());
            }

            adresszeilen.add(land);

        } else {

            if (lAktienregister.istJuristischePerson == 0) {
                if (anrede) {
                    adresszeilen.add(helFindeAnredeById(lAktienregister.anredeId));
                }

                final String name = (lAktienregister.nachname + " " + lAktienregister.vorname).toLowerCase();

                if (name.toLowerCase().contains(co)) {
                    adresszeilen.add(name.startsWith(co) ? lAktienregister.vorname : lAktienregister.nachname);
                    adresszeilen.add(name.startsWith(co) ? lAktienregister.nachname : lAktienregister.vorname);
                } else {
                    adresszeilen.add(
                            (lAktienregister.titel + " " + lAktienregister.vorname + " " + lAktienregister.nachname)
                                    .replaceAll("\\s+", " ").trim());
                }
            } else {

                if (lAktienregister.name2.contains(Konst_Legitimationsaktionaer)) {
                    adresszeilen.add(lAktienregister.name1);
                    adresszeilen.add(lAktienregister.name2);
                } else {
                    adresszeilen.add(lAktienregister.name1);
                    adresszeilen
                            .add((lAktienregister.name2 + " " + lAktienregister.name3).replaceAll("\\s+", " ").trim());
                }
            }

            adresszeilen.add(lAktienregister.zusatz);
            adresszeilen.add(lAktienregister.postfach.isBlank() ? lAktienregister.strasse
                    : lAktienregister.postfach.toLowerCase().contains("postfach") ? lAktienregister.postfach
                            : ("Postfach: " + lAktienregister.postfach));

            final String plz = lAktienregister.postfach.isBlank() ? lAktienregister.postleitzahl
                    : lAktienregister.postleitzahlPostfach.isBlank() ? lAktienregister.postleitzahl
                            : lAktienregister.postleitzahlPostfach;

            final String land = helFindeStaatNameNeu(lAktienregister.getStaatId());

            if (land.isBlank()) {
                adresszeilen.add((plz + " " + lAktienregister.ort));
            } else {
                adresszeilen.add(plz.isBlank() ? lAktienregister.ort
                        : lAktienregister.ort.contains(plz) ? lAktienregister.ort
                                : (plz + " " + lAktienregister.ort).trim());
            }

            adresszeilen.add(land);

        }

        adresszeilen = adresszeilen.stream().filter(e -> !e.isBlank()).collect(Collectors.toList());

        while (adresszeilen.size() < 7)
            adresszeilen.add("");

        lAktienregister.adresszeile1 = adresszeilen.get(0);
        lAktienregister.adresszeile2 = adresszeilen.get(1);
        lAktienregister.adresszeile3 = adresszeilen.get(2);
        lAktienregister.adresszeile4 = adresszeilen.get(3);
        lAktienregister.adresszeile5 = adresszeilen.get(4);
        lAktienregister.adresszeile6 = adresszeilen.get(5);
        lAktienregister.adresszeile7 = adresszeilen.get(6);

    }

 
    public ArrayList<String> helAdresszeilenAufbereitenBM(int anrede, int anredeVersand, String adressart, String titel,
            String vorname, String nachname, String name1, String name2, String name3, String titelVersand,
            String vornameVersand, String nachnameVersand, String name1Versand, String name2Versand,
            String name3Versand, String strasse, String postfach, String postleitzahl, String postleitzahlPostfach,
            String ort, String zusatz, String staat, String nameKomplett) {
        ArrayList<String> adresszeilen = new ArrayList<String>();

        String zeile1 = "";
        String zeile2 = "";
        String zeile3 = "";
        String zeile4 = "";
        String zeile5 = "";
        String zeile6 = "";

        String postleitzahlVersand = postleitzahl;
        String strasseVersand = strasse;
        String staatVersand = staat;
        if (!postleitzahlPostfach.equals("")) {
            postleitzahlVersand = postleitzahlPostfach;
        }
        if ((strasse.equals("")) && (!postfach.equals(""))) {
            strasseVersand = postfach.toLowerCase().contains("postfach") ? postfach : "Postfach: " + postfach;
        }
        if (staat.equals("Deutschland")) {
            staatVersand = "";
        }

        if (anredeVersand == 0)
            anredeVersand = anrede;

        if ((anredeVersand == 1) || (anredeVersand == 3) || (anredeVersand == 4)) {
            if (adressart.equals("Meldeadresse")) {
                zeile1 = titelVersand + " " + vornameVersand + " " + nachnameVersand;
                if (zusatz.equals("")) {
                    zeile2 = strasseVersand;
                    zeile3 = postleitzahlVersand + " " + ort;
                    zeile4 = staatVersand;
                } else {
                    zeile2 = zusatz;
                    zeile3 = strasseVersand;
                    zeile4 = postleitzahlVersand + " " + ort;
                    zeile5 = staatVersand;
                }
            } else if (adressart.equals("Versandadresse")) {
                String zeileTemp1 = titel + " " + vorname + " " + nachname;
                String zeileTemp2 = titelVersand + " " + vornameVersand + " " + nachnameVersand;

                if (zeileTemp1.trim().equals(zeileTemp2.trim())) {
                    zeile1 = zeileTemp1;
                    if (zusatz.equals("")) {
                        zeile2 = strasseVersand;
                        zeile3 = postleitzahlVersand + " " + ort;
                        zeile4 = staatVersand;
                    } else {
                        zeile2 = zusatz;
                        zeile3 = strasseVersand;
                        zeile4 = postleitzahlVersand + " " + ort;
                        zeile5 = staatVersand;
                    }
                } else {
                    zeile1 = zeileTemp1;
                    zeile2 = zeileTemp2;
                    if (zusatz.equals("")) {
                        zeile3 = strasseVersand;
                        zeile4 = postleitzahlVersand + " " + ort;
                        zeile5 = staatVersand;
                    } else {
                        zeile3 = zusatz;
                        zeile4 = strasseVersand;
                        zeile5 = postleitzahlVersand + " " + ort;
                        zeile6 = staatVersand;
                    }
                }
            } else if (adressart.equals("Vollmachtsadresse")) {
                zeile1 = titel + " " + vorname + " " + nachname;
                zeile2 = (nachnameVersand + " " + vornameVersand).trim();
                if (!zeile2.equals("")) {
                    if (zusatz.equals("")) {
                        zeile3 = strasseVersand;
                        zeile4 = postleitzahlVersand + " " + ort;
                        zeile5 = staatVersand;
                    } else {
                        zeile3 = zusatz;
                        zeile4 = strasseVersand;
                        zeile5 = postleitzahlVersand + " " + ort;
                        zeile6 = staatVersand;
                    }
                } else {
                    if (zusatz.equals("")) {
                        zeile2 = strasseVersand;
                        zeile3 = postleitzahlVersand + " " + ort;
                        zeile4 = staatVersand;
                    } else {
                        zeile2 = zusatz;
                        zeile3 = strasseVersand;
                        zeile4 = postleitzahlVersand + " " + ort;
                        zeile5 = staatVersand;
                    }
                }
            }
        } else if (anredeVersand == 2) {
            if (adressart.equals("Vollmachtsadresse") || adressart.equals("Versandadresse")) {

                if ((anrede == 1) || (anrede == 3) || (anrede == 4)) {
                    zeile1 = titel + " " + vorname + " " + nachname;
                } else {
                    zeile1 = name1 + " " + name2 + " " + name3;
                }

                if ((!name1Versand.equals("")) && (!name2Versand.equals("")) && (!name3Versand.equals(""))) {
                    zeile2 = name1Versand + " " + name2Versand;
                    zeile3 = name3Versand;
                    zeile4 = strasseVersand;
                    zeile5 = postleitzahlVersand + " " + ort;
                    zeile6 = staatVersand;
                } else if ((!name1Versand.equals("")) && (name2Versand.equals("")) && (!name3Versand.equals(""))) {
                    zeile2 = name1Versand;
                    zeile3 = name3Versand;
                    zeile4 = strasseVersand;
                    zeile5 = postleitzahlVersand + " " + ort;
                    zeile6 = staatVersand;
                } else if ((!name1Versand.equals("")) && (name2Versand.equals("")) && (name3Versand.equals(""))) {
                    zeile2 = name1Versand;
                    zeile3 = strasseVersand;
                    zeile4 = postleitzahlVersand + " " + ort;
                    zeile5 = staatVersand;
                } else if ((!name1Versand.equals("")) && (!name2Versand.equals("")) && (name3Versand.equals(""))) {
                    zeile2 = name1Versand + " " + name2Versand;
                    zeile3 = strasseVersand;
                    zeile4 = postleitzahlVersand + " " + ort;
                    zeile5 = staatVersand;
                } else {
                    zeile2 = strasseVersand;
                    zeile3 = postleitzahlVersand + " " + ort;
                    zeile4 = staatVersand;
                }
            } else if ((!name1Versand.equals("")) && (!name2Versand.equals("")) && (!name3Versand.equals(""))) {
                zeile1 = name1Versand;
                zeile2 = name2Versand;
                zeile3 = name3Versand;
                zeile4 = strasseVersand;
                zeile5 = postleitzahlVersand + " " + ort;
                zeile6 = staatVersand;
            } else if ((!name1Versand.equals("")) && (name2Versand.equals("")) && (!name3Versand.equals(""))) {
                zeile1 = name1Versand;
                zeile2 = name3Versand;
                zeile3 = strasseVersand;
                zeile4 = postleitzahlVersand + " " + ort;
                zeile5 = staatVersand;
            } else if ((!name1Versand.equals("")) && (name2Versand.equals("")) && (name3Versand.equals(""))) {
                zeile1 = name1Versand;
                zeile2 = strasseVersand;
                zeile3 = postleitzahlVersand + " " + ort;
                zeile4 = staatVersand;
            } else if ((!name1Versand.equals("")) && (!name2Versand.equals("")) && (name3Versand.equals(""))) {
                zeile1 = name1Versand;
                zeile2 = name2Versand;
                zeile3 = strasseVersand;
                zeile4 = postleitzahlVersand + " " + ort;
                zeile5 = staatVersand;
            }
        }

        if (zeile1.isBlank()) {
            zeile1 = nameKomplett;
        }

        adresszeilen.add(zeile1);
        adresszeilen.add(zeile2);
        adresszeilen.add(zeile3);
        adresszeilen.add(zeile4);
        adresszeilen.add(zeile5);
        adresszeilen.add(zeile6);

        return adresszeilen;
    }

    public String helAktionaersnummer(String nummer, String besitzmerkmal) {
        while (nummer.indexOf("0") == 0) {
            nummer = nummer.substring(1, nummer.length());
        }
        if ((besitzmerkmal.equals("F")) || (besitzmerkmal.equals("1")) || (besitzmerkmal.equals("Fremdbesitz"))) {
            nummer = nummer + "1";
        } else {
            nummer = nummer + "0";
        }

        while (nummer.length() < 11) {
            nummer = "0" + nummer;
        }
        return nummer;
    }

    /*
     * ku178 Anfang
     */

    public int helGruppe(String gruppierung) {
        int g = 0;

        switch (gruppierung.toLowerCase()) {
        case "einzelmitglied":
            g = KonstGruppen.einzelmitglied;
            break;
        case "eheleute gbr":
            g = KonstGruppen.eheleuteGbR;
            break;
        case "minderjähriges einzelmitglied":
            g = KonstGruppen.minderjaehrigesEinzelmitglied;
            break;
        case "eheleute gesamthandsgemeinschaft":
            g = KonstGruppen.eheleuteGesamthans;
            break;
        case "erbengemeinschaft":
            g = KonstGruppen.erbengemeinschaft;
            break;
        case "firmen und sonstige":
            g = KonstGruppen.firmen;
            break;
        default:
            break;
        }

        return g;
    }

    public int helFindeAnredeku178(String anrede) {
        int anredeId = 0;
        if (anrede.equals("Herr")) {
            anredeId = 1;
        } else if (anrede.equals("Frau")) {
            anredeId = 3;
        } else {
            anredeId = 4;
        }
        return anredeId;
    }

    public int helFindeAnredeku243(String anrede) {
        int anredeId = 0;
        if (anrede.equals("Herr")) {
            anredeId = 1;
        } else if (anrede.equals("Frau")) {
            anredeId = 3;
        } else {
            anredeId = 2;
        }
        return anredeId;
    }

    public String helPLZku178(String plz, String land) {
        if (land.equals("") || land.equals("Deutschland")) {
            while (plz.length() < 5) {
                plz = "0" + plz;
            }
        }
        return plz;
    }

    /*
     * private int helFindeStaatku178(String laendercode) throws SQLException {
     * 
     * if (laendercode.equals("")) { laendercode = "Deutschland"; }
     * 
     * laendercode = laendercode.toLowerCase(); laendercode =
     * Character.toUpperCase(laendercode.charAt(0)) + laendercode.substring(1,
     * laendercode.length());
     * 
     * return EclAktienregisterListen.staatenName.indexOf(laendercode) + 1; }
     */

    public String helCheckNameKomplettku178(String vorname, String nachname, String name1) {
        String nameKomplett = "";
        if (name1.equals("")) {
            nameKomplett = (nachname + " " + vorname).trim();
        } else {
            nameKomplett = (name1).trim();
        }
        return nameKomplett;
    }

    public int helAnmledenUnzulaessig(String gruppierung) {
        if (gruppierung.equals("Minderjähriges Einzelmitglied")) {
            return 1;
        } else {
            return 0;
        }
    }

    public int helDauerhafteRegistrierungUnzulaessig(String gruppierung) {
        if (gruppierung.equals("Eheleute GbR") || gruppierung.equals("Einzelmitglied")) {
            return 0;
        } else {
            return 1;
        }
    }

    public String helNachnameku178(String nachname, String gruppierung) {
        String n = "";
        switch (gruppierung) {
        case "Einzelmitglied":
            n = nachname;
            break;
        case "Minderjähriges Einzelmitglied":
            n = nachname;
            break;
        default:
            break;
        }

        return n;
    }

    public String helName1ku178(String name1, String gruppierung) {
        String n = name1;
        switch (gruppierung) {
        case "Einzelmitglied":
            n = "";
            break;
        case "Minderjähriges Einzelmitglied":
            n = "";
            break;
        default:
            break;
        }

        return n;
    }

    public int helVersandAbweichendku178(String versand) {
        int versandAbweichend = 0;
        if (versand.equals("Postempfänger")) {
            versandAbweichend = 1;
        }
        return versandAbweichend;
    }

    public String helAktionaersnummerku178(String nummer) {
        if (nummer_vorhanden.containsKey(nummer)) {
            String anhang = nummer_vorhanden.get(nummer);
            switch (anhang) {
            case "0":
                anhang = "1";
                break;
            case "1":
                anhang = "2";
                break;
            case "2":
                anhang = "3";
                break;
            case "3":
                anhang = "4";
                break;
            case "4":
                anhang = "5";
                break;
            case "5":
                anhang = "6";
                break;
            case "6":
                anhang = "7";
                break;
            case "7":
                anhang = "8";
                break;
            case "8":
                anhang = "9";
                break;
            case "9":
                anhang = "A";
                break;
            case "A":
                anhang = "B";
                break;
            case "B":
                anhang = "C";
                break;
            case "C":
                anhang = "D";
                break;
            case "D":
                anhang = "E";
                break;
            case "E":
                anhang = "F";
                break;
            case "F":
                anhang = "G";
                break;
            case "G":
                anhang = "H";
                break;
            case "H":
                anhang = "I";
                break;
            case "I":
                anhang = "J";
                break;
            case "J":
                anhang = "K";
                break;
            case "K":
                anhang = "L";
                break;
            default:
                break;
            }
            nummer_vorhanden.replace(nummer, anhang);
            nummer += anhang;
        } else {
            nummer_vorhanden.put(nummer, "0");
            nummer += "0";
        }

        while (nummer.length() < 11) {
            nummer = "0" + nummer;
        }

        return nummer;
    }

    public String helAktionaersnummerKu219(String nummer) {
        if (nummer_vorhanden.containsKey(nummer)) {
            String anhang = nummer_vorhanden.get(nummer);
            switch (anhang) {
            case "0":
                anhang = "1";
                break;
            case "1":
                anhang = "2";
                break;
            case "2":
                anhang = "3";
                break;
            case "3":
                anhang = "4";
                break;
            case "4":
                anhang = "5";
                break;
            case "5":
                anhang = "6";
                break;
            case "6":
                anhang = "7";
                break;
            case "7":
                anhang = "8";
                break;
            case "8":
                anhang = "9";
                break;
            case "9":
                anhang = "A";
                break;
            case "A":
                anhang = "B";
                break;
            case "B":
                anhang = "C";
                break;
            case "C":
                anhang = "D";
                break;
            case "D":
                anhang = "E";
                break;
            case "E":
                anhang = "F";
                break;
            case "F":
                anhang = "G";
                break;
            case "G":
                anhang = "H";
                break;
            case "H":
                anhang = "I";
                break;
            case "I":
                anhang = "J";
                break;
            case "J":
                anhang = "K";
                break;
            case "K":
                anhang = "L";
                break;
            default:
                break;
            }
            nummer_vorhanden.replace(nummer, anhang);
            nummer += anhang;
        } else {
            nummer_vorhanden.put(nummer, "0");
            nummer += "0";
        }

        nummer += "0";

        while (nummer.length() < 11) {
            nummer = "0" + nummer;
        }

        return nummer;
    }

    public ArrayList<String> helAdresszeilenAufbereitenku178(String vorname, String nachname, String strasse,
            String postleitzahl, String ort, String staat, String adresszusatz) {

        ArrayList<String> adresszeilen = new ArrayList<String>();

        String zeile1 = "";
        String zeile2 = "";
        String zeile3 = "";
        String zeile4 = "";
        String zeile5 = "";
        String zeile6 = "";

        if (staat.equals("Deutschland")) {
            staat = "";
        } else {
            staat = staat.toUpperCase();
        }

        zeile1 = (vorname + " " + nachname).trim();
        if (adresszusatz.equals("")) {
            zeile2 = (strasse).trim();
            zeile3 = (postleitzahl + " " + ort).trim();
            zeile4 = staat;
        } else {
            zeile2 = adresszusatz;
            zeile3 = (strasse).trim();
            zeile4 = (postleitzahl + " " + ort).trim();
            zeile5 = staat;
        }

        adresszeilen.add(zeile1);
        adresszeilen.add(zeile2);
        adresszeilen.add(zeile3);
        adresszeilen.add(zeile4);
        adresszeilen.add(zeile5);
        adresszeilen.add(zeile6);

        return adresszeilen;
    }

    /*
     * ku178 Ende
     */

    public String helAktionaersnummerNeu(String nummer, String besitzmerkmal) {
        while (nummer.indexOf("0") == 0) {
            nummer = nummer.substring(1, nummer.length());
        }
        if ((besitzmerkmal.equals("F")) || (besitzmerkmal.equals("1")) || (besitzmerkmal.equals("Fremdbesitz"))) {
            nummer = nummer + "1";
        } else {
            nummer = nummer + "0";
        }

        while (nummer.length() < 11)
            nummer = "0" + nummer;

        return nummer;
    }

    public String helErstellePlz(String postleitzahl, String laendercode) {
        if ((laendercode.equals("DE") || laendercode.equals("Deutschland")) && (postleitzahl.length() < 5)
                && (postleitzahl.length() > 0)) {
            postleitzahl = "0" + postleitzahl;
        }
        return postleitzahl;
    }

    public String helErstellePlzBM(String postleitzahl, String laendercode) {
        String plz = "";
        if ((laendercode.equals("Deutschland")) && (postleitzahl.length() < 5) && (postleitzahl.length() > 0)) {
            plz = "0" + postleitzahl;
        } else {
            plz = postleitzahl;
        }
        return plz;
    }

    private String helIsin(String isin) {
        if (isin.length() == 12) {
            isinSet.add(isin);
            return isin;
        }
        return "";
    }

}