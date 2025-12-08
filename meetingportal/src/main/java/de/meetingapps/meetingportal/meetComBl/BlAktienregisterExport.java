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

import java.text.NumberFormat;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaDateiWrite;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclStaaten;
import de.meetingapps.meetingportal.meetComHVParam.ParamS;
import de.meetingapps.meetingportal.meetComHVParam.ParamSpezial;

public class BlAktienregisterExport {
    DbBundle dbBundle = null;

    private List<EclStaaten> staaten = null;

    private final EclLoginDaten maLoginDaten = new EclLoginDaten("12345678910", "xabc13*dxabc13*dxabc13*d");

    // TODO Frage nach Parameter?
    private int pwLaenge = 8;
    public Boolean angemeldet = false;

    private final EclAktienregister ma = new EclAktienregister("12345678910", 1, "Link Market Services GmbH", "",
            "Landshuter Allee 10", "80637", "München", 56, 56, "Link Market Services GmbH", 123, 123, "E",
            "Link Market Services GmbH", "Landshuter Allee 10", "80637 München", "");

    public BlAktienregisterExport(DbBundle pDbBundle, List<EclStaaten> staaten) {
        this.dbBundle = pDbBundle;
        this.staaten = staaten;
    }

    public int listeAusgebenVersand(CaDateiWrite caDateiWrite, int selektion, int versandNummer, Boolean maPost) {

        int count = 0;

        ausgabeHeaderVersandlisten(caDateiWrite);

        this.dbBundle.dbJoined.read_aktienregisterVersandliste(selektion, versandNummer);

        for (int i = 0; i < this.dbBundle.dbJoined.anzErgebnisAktienregisterEintrag(); i++) {

            EclLoginDaten lLoginDaten = this.dbBundle.dbJoined.ergebnisLoginDatenPosition(i);
            EclAktienregister lAktienregistereintrag = this.dbBundle.dbJoined.ergebnisAktienregisterEintragPosition(i);

            String passwort = lLoginDaten.passwortInitial;

            printLine(caDateiWrite, lLoginDaten, lAktienregistereintrag, passwort);

            if (i % 1000 == 0)
                System.out.println(i);

            angemeldet = angemeldet ? true : lAktienregistereintrag.personNatJur != 0;

            count++;
        }

        if (selektion == 1 && versandNummer == 1 && maPost) {
            printLine(caDateiWrite, maLoginDaten, ma, maLoginDaten.passwortInitial);
            count += 1;
        }

        return count;
    }

    public void printLine(CaDateiWrite caDateiWrite, EclLoginDaten lLoginDaten,
            EclAktienregister lAktienregistereintrag, String passwort) {

        String name = (lAktienregistereintrag.name1 + " " + lAktienregistereintrag.name2 + " "
                + lAktienregistereintrag.name3 + " " + lAktienregistereintrag.titel + " "
                + lAktienregistereintrag.vorname + " " + lAktienregistereintrag.nachname).trim();

        String email1 = lLoginDaten.eMailFuerVersand;
        if (email1 == null) {
            email1 = "";
        }

        caDateiWrite.newline();
        caDateiWrite.ausgabe(lAktienregistereintrag.aktionaersnummer.substring(0,
                lAktienregistereintrag.aktionaersnummer.length() - 1));
        caDateiWrite.ausgabe(lAktienregistereintrag.aktionaersnummer);
        caDateiWrite.ausgabe(lAktienregistereintrag.nameKomplett);
        if (!lAktienregistereintrag.postfach.equals("")) {
            caDateiWrite.ausgabe("Postfach: " + lAktienregistereintrag.postfach);
            caDateiWrite.ausgabe(lAktienregistereintrag.postleitzahlPostfach + " " + lAktienregistereintrag.ort);
        } else {
            caDateiWrite.ausgabe(lAktienregistereintrag.strasse);
            caDateiWrite.ausgabe(lAktienregistereintrag.postleitzahl + " " + lAktienregistereintrag.ort);
        }
        caDateiWrite.ausgabe(name);
        caDateiWrite.ausgabe(lAktienregistereintrag.ort);
        if (ParamSpezial.ku108(ParamS.clGlobalVar.mandant))
            caDateiWrite.ausgabe(lAktienregistereintrag.adresszeile9);

        caDateiWrite.ausgabe(lAktienregistereintrag.adresszeile1);
        caDateiWrite.ausgabe(lAktienregistereintrag.adresszeile2);
        caDateiWrite.ausgabe(lAktienregistereintrag.adresszeile3);
        caDateiWrite.ausgabe(lAktienregistereintrag.adresszeile4);
        caDateiWrite.ausgabe(lAktienregistereintrag.adresszeile5);
        caDateiWrite.ausgabe(lAktienregistereintrag.adresszeile6);

        final int staatId = lAktienregistereintrag.versandAbweichend == 1 ? lAktienregistereintrag.staatIdVersand
                : lAktienregistereintrag.staatId;

        final EclStaaten land = staaten.stream().filter(e -> e.id == staatId).findFirst().orElse(null);

        caDateiWrite.ausgabe(land != null ? land.nameDE : "");
        if (ParamSpezial.ku108(ParamS.clGlobalVar.mandant)) {
            caDateiWrite.ausgabe(lAktienregistereintrag.adresszeile10);
        } else if (lAktienregistereintrag.anredeId == 1) {
            caDateiWrite.ausgabe("Sehr geehrter Herr " + new StringBuilder(String.valueOf(lAktienregistereintrag.titel))
                    .append(" ").append(lAktienregistereintrag.nachname).toString().trim() + ",");
        } else if (lAktienregistereintrag.anredeId == 2) {
            caDateiWrite.ausgabe("Sehr geehrte Damen und Herren,");
        } else if (lAktienregistereintrag.anredeId == 3) {
            caDateiWrite.ausgabe("Sehr geehrte Frau " + new StringBuilder(String.valueOf(lAktienregistereintrag.titel))
                    .append(" ").append(lAktienregistereintrag.nachname).toString().trim() + ",");
        } else if (lAktienregistereintrag.anredeId == 4) {
            caDateiWrite.ausgabe("Sehr geehrte Aktionärin, sehr geehrter Aktionär,");
        } else {
            caDateiWrite.ausgabe("");
        }

        caDateiWrite.ausgabe(email1);
        caDateiWrite.ausgabe(String.valueOf(NumberFormat.getInstance().format(lAktienregistereintrag.stimmen)));
        if (lAktienregistereintrag.stueckAktien > 1L) {
            caDateiWrite.ausgabe("Aktien");
        } else {
            caDateiWrite.ausgabe("Aktie");
        }
        if (lAktienregistereintrag.besitzart.equals("E")) {
            caDateiWrite.ausgabe("im Eigenbesitz");
        } else {
            caDateiWrite.ausgabe("im Fremdbesitz");
        }
        //        caDateiWrite
        //                .ausgabe(String.valueOf(lAktienregistereintrag.gruppe));
        //        caDateiWrite
        //                .ausgabe(String.valueOf(lAktienregistereintrag.gattungId));

        passwort = (!passwort.isBlank() && passwort.length() != 8) ? passwort.substring(pwLaenge, pwLaenge * 2) : "";

        String linkStart = dbBundle.param.paramPortal.kurzLinkPortal + "?";
        String kennung = "nummer=" + lAktienregistereintrag.aktionaersnummer;
        String pw = passwort.isBlank() ? "" : "&p=" + passwort;

        caDateiWrite.ausgabe(passwort);
        caDateiWrite.ausgabe(linkStart + kennung + pw);
    }

    public void ausgabeHeaderVersandlisten(CaDateiWrite caDateiWrite) {

        if (ParamSpezial.ku302_303(ParamS.clGlobalVar.mandant))
            caDateiWrite.ausgabe("GP-Nr.");
        else
            caDateiWrite.ausgabe("ANR kurz");

        caDateiWrite.ausgabe("ANR lang");
        caDateiWrite.ausgabe("Name sort");
        caDateiWrite.ausgabe("Straße");
        caDateiWrite.ausgabe("Postleitzahl_Ort");
        caDateiWrite.ausgabe("Name");
        caDateiWrite.ausgabe("Ort");
        if (ParamSpezial.ku108(ParamS.clGlobalVar.mandant))
            caDateiWrite.ausgabe("Adresse Anrede");
        caDateiWrite.ausgabe("Adresszeile 1");
        caDateiWrite.ausgabe("Adresszeile 2");
        caDateiWrite.ausgabe("Adresszeile 3");
        caDateiWrite.ausgabe("Adresszeile 4");
        caDateiWrite.ausgabe("Adresszeile 5");
        caDateiWrite.ausgabe("Adresszeile 6");
        caDateiWrite.ausgabe("Land");
        caDateiWrite.ausgabe("Anrede");
        caDateiWrite.ausgabe("Email 1");
        caDateiWrite.ausgabe("Bestand");
        caDateiWrite.ausgabe("Numerus");
        caDateiWrite.ausgabe("Besitzart");
        caDateiWrite.ausgabe("Passwort");
        caDateiWrite.ausgabe("Link");

    }

    public int listeAusgebenVersandku217(CaDateiWrite caDateiWrite, int selektion, int versandNummer) {

        int count = 0;

        caDateiWrite.ausgabe("Firma1");
        caDateiWrite.ausgabe("Akademischer Titel");
        caDateiWrite.ausgabe("Vorname");
        caDateiWrite.ausgabe("Nachname");
        caDateiWrite.ausgabe("Mitgliedsnummer");
        caDateiWrite.ausgabe("Passwort");
        caDateiWrite.ausgabe("Stimmenanzahl_aktuell");
        caDateiWrite.ausgabe("PLZ");
        caDateiWrite.ausgabe("Ort");
        caDateiWrite.ausgabe("Strasse");
        caDateiWrite.ausgabe("E-Mail");
        caDateiWrite.ausgabe("Portal_Link");
        caDateiWrite.ausgabe("Gattung");

        this.dbBundle.dbJoined.read_aktienregisterVersandliste(selektion, versandNummer);

        for (int i = 0; i < this.dbBundle.dbJoined.anzErgebnisAktienregisterEintrag(); i++) {

            EclLoginDaten lLoginDaten = this.dbBundle.dbJoined.ergebnisLoginDatenPosition(i);
            EclAktienregister lAktienregistereintrag = this.dbBundle.dbJoined.ergebnisAktienregisterEintragPosition(i);

            String passwort = lLoginDaten.passwortInitial;

            printLineku217(caDateiWrite, lLoginDaten, lAktienregistereintrag, passwort);

            if (i % 1000 == 0)
                System.out.println(i);

            count++;
        }

        return count;
    }

    private void printLineku217(CaDateiWrite caDateiWrite, EclLoginDaten lLoginDaten,
            EclAktienregister lAktienregistereintrag, String passwort) {

        caDateiWrite.newline();
        caDateiWrite.ausgabe(lAktienregistereintrag.name1);
        caDateiWrite.ausgabe(lAktienregistereintrag.titel);
        caDateiWrite.ausgabe(lAktienregistereintrag.vorname);
        caDateiWrite.ausgabe(lAktienregistereintrag.nachname);

        final String user = lLoginDaten.loginKennung.substring(4, lLoginDaten.loginKennung.length() - 1);
        caDateiWrite.ausgabe(user);

        passwort = (!passwort.isBlank() && passwort.length() != 8) ? passwort.substring(pwLaenge, pwLaenge * 2) : "";
        caDateiWrite.ausgabe(passwort);

        caDateiWrite.ausgabe(String.valueOf(lAktienregistereintrag.stimmen));
        caDateiWrite.ausgabe(lAktienregistereintrag.postleitzahl);
        caDateiWrite.ausgabe(lAktienregistereintrag.ort);
        caDateiWrite.ausgabe(lAktienregistereintrag.strasse);
        caDateiWrite.ausgabe(lAktienregistereintrag.email);

        String linkStart = dbBundle.param.paramPortal.kurzLinkPortal + "?";
        String kennung = "nummer=" + user;
        String pw = passwort.isBlank() ? "" : "&p=" + passwort;

        caDateiWrite.ausgabe(linkStart + kennung + pw);
        caDateiWrite.ausgabe(String.valueOf(lAktienregistereintrag.gattungId));

    }

    public int listeAusgebenVersandku216(CaDateiWrite caDateiWrite, int selektion, int versandNummer) {

        int count = 0;

        caDateiWrite.ausgabe("Mitgliedsnummer");
        caDateiWrite.ausgabe("Passwort");
        caDateiWrite.ausgabe("Vorname");
        caDateiWrite.ausgabe("Nachname");
        caDateiWrite.ausgabe("Strasse");
        caDateiWrite.ausgabe("PLZ");
        caDateiWrite.ausgabe("Ort");
        caDateiWrite.ausgabe("PLZ_Ort");
        caDateiWrite.ausgabe("Gruppe");
        caDateiWrite.ausgabe("Geburts-/Gründungsdatum");

        this.dbBundle.dbJoined.read_aktienregisterVersandliste(selektion, versandNummer);

        for (int i = 0; i < this.dbBundle.dbJoined.anzErgebnisAktienregisterEintrag(); i++) {

            EclLoginDaten lLoginDaten = this.dbBundle.dbJoined.ergebnisLoginDatenPosition(i);
            EclAktienregister lAktienregistereintrag = this.dbBundle.dbJoined.ergebnisAktienregisterEintragPosition(i);

            String passwort = lLoginDaten.passwortInitial;

            printLineku216(caDateiWrite, lLoginDaten, lAktienregistereintrag, passwort);

            if (i % 1000 == 0)
                System.out.println(i);

            count++;
        }

        return count;
    }

    private void printLineku216(CaDateiWrite caDateiWrite, EclLoginDaten lLoginDaten,
            EclAktienregister lAktienregistereintrag, String passwort) {

        caDateiWrite.newline();
        caDateiWrite.ausgabe(lAktienregistereintrag.aktionaersnummer.substring(0,
                lAktienregistereintrag.aktionaersnummer.length() - 1));
        caDateiWrite.ausgabe(
                (!passwort.isBlank() && passwort.length() != 8) ? passwort.substring(pwLaenge, pwLaenge * 2) : "");
        caDateiWrite.ausgabe(lAktienregistereintrag.vorname);
        caDateiWrite.ausgabe(lAktienregistereintrag.nachname);
        caDateiWrite.ausgabe(lAktienregistereintrag.strasse);
        caDateiWrite.ausgabe(lAktienregistereintrag.postleitzahl);
        caDateiWrite.ausgabe(lAktienregistereintrag.ort);
        caDateiWrite.ausgabe(lAktienregistereintrag.postleitzahl + " " + lAktienregistereintrag.ort);
        caDateiWrite.ausgabe(String.valueOf(lAktienregistereintrag.gruppe));
        caDateiWrite.ausgabe(lAktienregistereintrag.adresszeile10);

    }

    public int listeAusgebenexportFormat1001(CaDateiWrite caDateiWrite, int selektion, int versandNummer) {

        int count = 0;

        caDateiWrite.ausgabe("id");
        caDateiWrite.ausgabe("Aktionaersnummervariante");
        caDateiWrite.ausgabe("ISIN");
        caDateiWrite.ausgabe("Anrede");
        caDateiWrite.ausgabe("Nachname");
        caDateiWrite.ausgabe("Vorname");
        caDateiWrite.ausgabe("Titel");
        caDateiWrite.ausgabe("Strasse");
        caDateiWrite.ausgabe("Postfach");
        caDateiWrite.ausgabe("Postleitzahl");
        caDateiWrite.ausgabe("Ort");
        caDateiWrite.ausgabe("Staat");
        caDateiWrite.ausgabe("EMail");
        caDateiWrite.ausgabe("Personenart");
        caDateiWrite.ausgabe("LA_Anrede");
        caDateiWrite.ausgabe("LA_Nachname");
        caDateiWrite.ausgabe("LA_Vorname");
        caDateiWrite.ausgabe("LA_Titel");
        caDateiWrite.ausgabe("LA_Strasse");
        caDateiWrite.ausgabe("LA_Postfach");
        caDateiWrite.ausgabe("LA_Postleitzahl");
        caDateiWrite.ausgabe("LA_Ort");
        caDateiWrite.ausgabe("LA_Staat");
        caDateiWrite.ausgabe("LA_EMail");
        caDateiWrite.ausgabe("LA_Personenart");
        caDateiWrite.ausgabe("Besitzart");
        caDateiWrite.ausgabe("Aktienbestand_Eigenbesitz");
        caDateiWrite.ausgabe("Aktienbestand_Fremdbesitz");
        caDateiWrite.ausgabe("VIP");
        caDateiWrite.ausgabe("Internetcode");
        caDateiWrite.ausgabe("Aktionaersnummer");

        this.dbBundle.dbJoined.read_aktienregisterVersandliste(selektion, versandNummer);

        for (int i = 0; i < this.dbBundle.dbJoined.anzErgebnisAktienregisterEintrag(); i++) {

            EclLoginDaten lLoginDaten = this.dbBundle.dbJoined.ergebnisLoginDatenPosition(i);
            EclAktienregister lAktienregistereintrag = this.dbBundle.dbJoined.ergebnisAktienregisterEintragPosition(i);

            String passwort = lLoginDaten.passwortInitial;

            printLineexportFormat1001(i, caDateiWrite, lLoginDaten, lAktienregistereintrag, passwort);

            if (i % 1000 == 0)
                System.out.println(i);

            count++;
        }
        return count;
    }

    public void printLineexportFormat1001(int count, CaDateiWrite caDateiWrite, EclLoginDaten lLoginDaten,
            EclAktienregister eintrag, String passwort) {

        caDateiWrite.newline();
        caDateiWrite.ausgabe("" + count + 1);
        caDateiWrite.ausgabe(eintrag.getAktionaersnummer());
        caDateiWrite.ausgabe(eintrag.getIsin());

        printData(caDateiWrite, eintrag);

        if (eintrag.versandAbweichend == 1) {
            caDateiWrite.ausgabe(convertAnredeexportFormat1001(eintrag.getAnredeIdVersand()) + "");
            if (eintrag.getAnredeIdVersand() == 2) {
                caDateiWrite.ausgabe((eintrag.name1 + " " + eintrag.name2 + " " + eintrag.name3).trim());
                caDateiWrite.ausgabe("");
                caDateiWrite.ausgabe("");
            } else {
                caDateiWrite.ausgabe(eintrag.getNachnameVersand());
                caDateiWrite.ausgabe(eintrag.getVornameVersand());
                caDateiWrite.ausgabe(eintrag.getTitelVersand());
            }

            caDateiWrite.ausgabe(eintrag.getStrasseVersand());
            caDateiWrite.ausgabe(eintrag.getPostfachVersand());
            caDateiWrite.ausgabe(eintrag.getPostleitzahlVersand());
            caDateiWrite.ausgabe(eintrag.getOrtVersand());

            final EclStaaten landVersand = staaten.stream().filter(e -> e.id == eintrag.getStaatIdVersand()).findFirst()
                    .orElse(null);
            caDateiWrite.ausgabe(landVersand.getCode());
            caDateiWrite.ausgabe("");
            caDateiWrite.ausgabe((eintrag.getAnredeIdVersand() == 2 ? 3 : 2) + "");

        } else {
            printData(caDateiWrite, eintrag);
        }

        caDateiWrite.ausgabe((eintrag.getBesitzart().equals("E") ? 2 : 3) + "");
        caDateiWrite.ausgabe((eintrag.getBesitzart().equals("E") ? eintrag.getStimmen() : "") + "");
        caDateiWrite.ausgabe((eintrag.getBesitzart().equals("E") ? "" : eintrag.getStimmen()) + "");
        caDateiWrite.ausgabe("");

        passwort = (!passwort.isBlank() && passwort.length() != 8) ? passwort.substring(pwLaenge, pwLaenge * 2) : "";
        caDateiWrite.ausgabe(passwort);
        caDateiWrite.ausgabe(eintrag.getAktionaersnummer());
    }

    private void printData(CaDateiWrite caDateiWrite, EclAktienregister eintrag) {

        caDateiWrite.ausgabe(convertAnredeexportFormat1001(eintrag.getAnredeId()) + "");

        if (eintrag.istJuristischePerson == 1) {
            caDateiWrite.ausgabe(eintrag.getNameKomplett());
            caDateiWrite.ausgabe("");
            caDateiWrite.ausgabe("");
        } else {
            caDateiWrite.ausgabe(eintrag.getNachname());
            caDateiWrite.ausgabe(eintrag.getVorname());
            caDateiWrite.ausgabe(eintrag.getTitel());
        }

        caDateiWrite.ausgabe(eintrag.getStrasse());
        caDateiWrite.ausgabe(eintrag.getPostfach());
        caDateiWrite.ausgabe(eintrag.getPostleitzahl());
        caDateiWrite.ausgabe(eintrag.getOrt());

        final EclStaaten land = staaten.stream().filter(e -> e.id == eintrag.staatId).findFirst().orElse(null);
        caDateiWrite.ausgabe(land.getCode());
        caDateiWrite.ausgabe("");
        caDateiWrite.ausgabe((eintrag.istJuristischePerson == 1 ? 3 : 2) + "");

    }

    public static String createAnrede(int anredeId, String name) {

        return switch (anredeId) {
        case 1 -> "";
        case 2 -> "";
        case 3 -> "";
        case 4 -> "";
        default -> "Sehr geehrte Damen und Herren";
        };

    }

    public static int convertAnredeexportFormat1001(int anredeId) {
        return switch (anredeId) {
        case 1 -> 1;
        case 2 -> 9;
        case 3 -> 2;
        case 4 -> 8;
        default -> 0;
        };
    }

}
