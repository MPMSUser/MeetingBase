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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBank;
import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclAuftrag;
import de.meetingapps.meetingportal.meetComEntities.EclInsti;
import de.meetingapps.meetingportal.meetComEntities.EclInstiEmittentenMitZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclInstiNachrichtEmpfaenger;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclNachricht;
import de.meetingapps.meetingportal.meetComEntities.EclNachrichtAnhang;
import de.meetingapps.meetingportal.meetComEntities.EclNachrichtBasisText;
import de.meetingapps.meetingportal.meetComEntities.EclNachrichtVerwendungsCode;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;
import de.meetingapps.meetingportal.meetComEntities.EclUserNachrichtEmpfaenger;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragArt;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragModul;
import de.meetingapps.meetingportal.meetComKonst.KonstAuftragStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstNachrichtVerwendungsCode;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetComStub.StubRoot;
import jakarta.servlet.http.Part;

/**TODO Stub grundsätzlich vorbereitet, aber nicht umgesetzt!*/
public class BlNachrichten extends StubRoot {

    private int logDrucken = 3;

    private String[] blackList = { "ADE", "ADP", "APP", "ASP", "ASPX", "ASX", "BAS", "BAT", "CER", "CHM", "CMD", "CNT",
            "COM", "CPL", "CRT", "CSH", "DER", "diagcab", "EXE", "FXP", "GADGET", "GRP", "HLP", "HPJ", "HTA", "HTC",
            "INF", "INS", "ISP", "ITS", "JAR", "JNLP", "JS", "JSE", "KSH", "LNK", "MAD", "MAF", "MAG", "MAG", "MAM",
            "MAQ", "MAR", "MAR", "MAS", "MAT", "MAU", "MAV", "MAW", "MCF", "MDA", "MDB", "MDE", "MDT", "MDW", "MDZ",
            "MSC", "MSH", "MSH1", "MSH2", "MSHXML", "MSH1XML", "MSH2XML", "MSI", "MSP", "MST", "MSU", "OPS", "OSD",
            "PCD", "PIF", "PL", "PLG", "PRF", "PRG", "PRINTEREXPORT", "PS1", "PS1XML", "PS2", "PS2XML", "PSC1", "PSC2",
            "PSD1", "PSDM1", "PST", "PY", "PYC", "PYO", "PYW", "PYZ", "PYZW", "REG", "SCF", "SCR", "SCT", "SHB", "SHS",
            "THEME", "TMP", "URL", "VB", "VBE", "VBP", "VBS", "VHD", "VHDX", "VSMACROS", "VSW", "WEBPNP", "WEBSITE",
            "WS", "WSC", "WSF", "WSH", "XBAP", "XLL", "XNK" };

    public BlNachrichten(boolean pIstServer, DbBundle pDbBundle) {
        super(pIstServer, pDbBundle);
    }

    /*************Funktionen zum Vorbereiten und Senden einer Nachricht*******************/

    /*+++++++++++++++++Vorbereiten einer Nachricht+++++++++++++++++++++*/
    /**Alle Verwendungscodes, die (beim manuellen) Senden einer Nachricht verwendet werden dürfen.
     * D.h. für das System reservierte Verwendungscodes sind nicht enthalten*/
    public List<EclNachrichtVerwendungsCode> rcNachrichtVerwendungsCodeList = null;

    /**Alle Basistexte, die zur Verfügung stehen*/
    public List<EclNachrichtBasisText> rcNachrichtBasisTextList = null;

    /**Potentielle Empfänger - Better Orange.
     * Immer gefüllt.
     */
    public List<EclUserNachrichtEmpfaenger> rcEmpfaengerBO = new LinkedList<EclUserNachrichtEmpfaenger>();

    /**Potentielle Empfänger auf Emittentenseite. Nur gefüllt,
     * wenn mandant!=0
     */
    public List<EclUserNachrichtEmpfaenger> rcEmpfaengerEmittent = new LinkedList<EclUserNachrichtEmpfaenger>();

    /**Potentielle Empfänger auf Seiten Dritter (d.h. im Auftrag des Emittenten). Nur gefüllt,
     * wenn mandant!=0
     */
    public List<EclUserNachrichtEmpfaenger> rcEmpfaengerDritte = new LinkedList<EclUserNachrichtEmpfaenger>();

    /**Potentielle Empfänger Insti - alle Instis, ggf. mit Kennzeichen Bestand. Immer gefüllt*/
    public List<EclInstiNachrichtEmpfaenger> rcEmpfaengerInsti = new LinkedList<EclInstiNachrichtEmpfaenger>();

    /**Enthält die Mail-Ident der gesendeten Nachricht*/
    public int rcIdentMail = 0;

    /**pMandant = entweder Mandantennummer (damit dann z.B. Empfangs-Adressen Emittent vorgegeben),
     * oder =0 => übergreifend
     *
     * Füllt:
     * rcNachrichtVerwendungsCodeList, rcNachrichtBasisTextList,
     * rcEmpfaengerBO, rcEmpfaengerEmittent, rcEmpfaengerDritte,
     * rcEmpfaengerInstiAlle, rcEmpfaengerInstiMitBestand
     */
    public int senden_datenVorbereiten(int pMandant) {
        dbOpenUndWeitere();

        /*rcNachrichtVerwendungsCodeList*/
        lDbBundle.dbNachrichtVerwendungsCode.read(0);
        rcNachrichtVerwendungsCodeList = erzeugeNachrichtVerwendungsCode();

        /*rcNachrichtBasisTextList*/
        lDbBundle.dbNachrichtBasisText.read(0);
        rcNachrichtBasisTextList = erzeugeNachrichtBasisText();

        /*rcEmpfaengerBO*/
        lDbBundle.dbUserLogin.lese_allZuInsti(-1, 0);
        rcEmpfaengerBO = erzeuge_UserListe();

        /*rcEmpfaengerEmittent*/
        if (pMandant == 0) {
            rcEmpfaengerEmittent = new LinkedList<EclUserNachrichtEmpfaenger>();
        } else {
            lDbBundle.dbUserLogin.lese_allZuInsti(-2, pMandant);
            rcEmpfaengerEmittent = erzeuge_UserListe();
        }

        /*rcEmpfaengerDritte*/
        if (pMandant == 0) {
            rcEmpfaengerDritte = new LinkedList<EclUserNachrichtEmpfaenger>();
        } else {
            lDbBundle.dbUserLogin.lese_allZuInsti(-3, pMandant);
            rcEmpfaengerDritte = erzeuge_UserListe();
        }

        /*rcEmpfaengerInsti*/
        rcEmpfaengerInsti = erzeugeInstiListe(pMandant);

        dbClose();
        return 1;
    }

    private List<EclNachrichtVerwendungsCode> erzeugeNachrichtVerwendungsCode() {
        List<EclNachrichtVerwendungsCode> ergebnisListe = new LinkedList<EclNachrichtVerwendungsCode>();
        for (int i = 0; i < lDbBundle.dbNachrichtVerwendungsCode.anzErgebnis(); i++) {
            ergebnisListe.add(lDbBundle.dbNachrichtVerwendungsCode.ergebnisPosition(i));
        }

        return ergebnisListe;
    }

    private List<EclNachrichtBasisText> erzeugeNachrichtBasisText() {
        List<EclNachrichtBasisText> ergebnisListe = new LinkedList<EclNachrichtBasisText>();
        for (int i = 0; i < lDbBundle.dbNachrichtBasisText.anzErgebnis(); i++) {
            ergebnisListe.add(lDbBundle.dbNachrichtBasisText.ergebnisPosition(i));
        }

        return ergebnisListe;
    }

    private List<EclUserNachrichtEmpfaenger> erzeuge_UserListe() {
        List<EclUserNachrichtEmpfaenger> ergebnisListe = new LinkedList<EclUserNachrichtEmpfaenger>();
        for (int i = 0; i < lDbBundle.dbUserLogin.anzUserLoginGefunden(); i++) {
            EclUserLogin lUserLogin = lDbBundle.dbUserLogin.userLoginGefunden(i);
            if (lUserLogin.pruefe_uportal_mailSenden()) {
                ergebnisListe.add(new EclUserNachrichtEmpfaenger(lUserLogin));
            }
        }
        return ergebnisListe;
    }

    private EclInsti[] tempInsti = null;
    private EclUserLogin[] tempInstiUser = null;
    private EclInstiEmittentenMitZuordnung[] tempInstiEmittentenMitZuordnung = null;

    /**pMandant=0 => alle Instis; .bestand ist gesetzt, wenn irgendeine Bestandszuordnung;
     * pMandant!=0 => alle Instis; .bestand ist gesetzt, wenn Bestandszuordnung zu diesem Mandant
     * @param pMandant
     * @return
     */
    private List<EclInstiNachrichtEmpfaenger> erzeugeInstiListe(int pMandant) {
        CaBug.druckeLog("pMandant=" + pMandant, logDrucken, 10);
        tempInsti = new EclInsti[0];
        lDbBundle.dbInsti.readAlle();
        if (lDbBundle.dbInsti.anzErgebnis() > 0) {
            tempInsti = lDbBundle.dbInsti.ergebnisAlsArray();
        }

        tempInstiUser = new EclUserLogin[0];
        lDbBundle.dbUserLogin.lese_allZuInsti();
        if (lDbBundle.dbUserLogin.anzUserLoginGefunden() > 0) {
            tempInstiUser = lDbBundle.dbUserLogin.userLoginArray;
        }

        tempInstiEmittentenMitZuordnung = new EclInstiEmittentenMitZuordnung[0];
        if (pMandant != 0) {
            lDbBundle.dbInstiEmittentenMitZuordnung.readAllZuMandant(pMandant);
            if (lDbBundle.dbInstiEmittentenMitZuordnung.anzErgebnis() > 0) {
                tempInstiEmittentenMitZuordnung = lDbBundle.dbInstiEmittentenMitZuordnung.ergebnis();
            }
        }

        List<EclInstiNachrichtEmpfaenger> lInstiNachrichtEmpfaengerList = new LinkedList<EclInstiNachrichtEmpfaenger>();
        for (int i = 0; i < tempInsti.length; i++) {
            EclInsti lInsti = tempInsti[i];
            if (pruefeInstiHatEmpfaenger(lInsti)) {
                boolean lHatBestand = false;
                if (pMandant != 0) {
                    lHatBestand = pruefeInstiHatBestand(lInsti, pMandant);
                }
                lInstiNachrichtEmpfaengerList.add(new EclInstiNachrichtEmpfaenger(lInsti, lHatBestand));
            }

        }

        return lInstiNachrichtEmpfaengerList;
    }

    private boolean pruefeInstiHatEmpfaenger(EclInsti pInsti) {
        for (int i = 0; i < tempInstiUser.length; i++) {
            EclUserLogin lUserLogin = tempInstiUser[i];
            if (lUserLogin.gehoertZuInsti == pInsti.ident) {
                if (lUserLogin.pruefe_uportal_mailEmpfangen()) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean pruefeInstiHatBestand(EclInsti pInsti, int pMandant) {
        for (int i = 0; i < tempInstiEmittentenMitZuordnung.length; i++) {
            if (tempInstiEmittentenMitZuordnung[i].mandant == pMandant
                    && tempInstiEmittentenMitZuordnung[i].identInsti == pInsti.ident) {
                return true;
            }
        }
        return false;
    }

    public int neueMailIdent() {
        int lNeueMailIdent = 0;
        lNeueMailIdent = lDbBundle.dbBasis.getInterneIdentNachrichtIdentMail();
        return lNeueMailIdent;
    }

    public EclNachrichtAnhang erzeugeAnhang(Part pDateiAnhang, String pBeschreibung, int pMailIdent) {
        dbOpenUndWeitere();

        EclNachrichtAnhang lNachrichtAnhang = new EclNachrichtAnhang();
        try {
            String fileName = pDateiAnhang.getSubmittedFileName();

            boolean brc = pruefeObFileNameZulaessig(fileName);
            if (brc == false) {
                lNachrichtAnhang.beschreibung = "Unzulässiger Dateiname";
                lNachrichtAnhang.ident = -1;
                dbClose();
                return lNachrichtAnhang;
            }

            String folder = liefereFolder();
            lNachrichtAnhang.beschreibung = pBeschreibung;
            lNachrichtAnhang.dateiname = fileName;
            lNachrichtAnhang.identMail = pMailIdent;

            lDbBundle.dbNachrichtAnhang.insert(lNachrichtAnhang);
            int nachrichtAnhangIdent = lNachrichtAnhang.ident;

            InputStream input = pDateiAnhang.getInputStream();
            Files.copy(input, new File(folder, liefereDateinameDisk(nachrichtAnhangIdent, fileName)).toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dbClose();
        return lNachrichtAnhang;
    }

    private boolean pruefeObFileNameZulaessig(String pDateiname) {
        String lDateiname = pDateiname.toUpperCase();
        for (int i = 0; i < blackList.length; i++) {
            String hBlackListElement = "." + blackList[i].toUpperCase();
            if (lDateiname.endsWith(hBlackListElement)) {
                return false;
            }
        }
        return true;
    }

    public List<String> rcMailEmpfaenger = null;
    public List<String> rcMailBetreff = null;
    public List<String> rcMailInhalt = null;

    /**Füllt:
     * rcMailEmpfaenger, rcMailBetreff, rcMailInhalt
     */
    public int neueNachrichtSenden(List<Integer> pEmpfaengerListe, List<Integer> pEmpfaengerInstiListe,
            int pVerwendungsCode, boolean pVollenTextInEMail, boolean pDateiAnhangVorhanden, int pMailIdent,
            String pBetreff, String pInhalt, int pMandant, int pAntwortAufNachricht, int pAbsenderIdent,
            boolean pMailInhaltVollstaendig, String pParameter1, String pParameter2, String pParameter3,
            String pParameter4, String pParameter5) {

        rcMailEmpfaenger = new LinkedList<String>();
        rcMailBetreff = new LinkedList<String>();
        rcMailInhalt = new LinkedList<String>();

        dbOpenUndWeitere();

        String mailBetreff = "";
        String mailInhalt = "";
        lDbBundle.dbNachrichtVerwendungsCode.read(KonstNachrichtVerwendungsCode.emailText);
        if (lDbBundle.dbNachrichtVerwendungsCode.anzErgebnis() != 0) {
            int basisIdent = lDbBundle.dbNachrichtVerwendungsCode.ergebnisPosition(0).identNachrichtBasisText;
            lDbBundle.dbNachrichtBasisText.read(basisIdent);
            mailBetreff = lDbBundle.dbNachrichtBasisText.ergebnisPosition(0).betreff;
            mailInhalt = lDbBundle.dbNachrichtBasisText.ergebnisPosition(0).mailText;
        }

        ergaenzeEmpfaengerListeUmInsti(pEmpfaengerListe, pEmpfaengerInstiListe, pMandant);

        /*Ggf. neue MailIdent vergeben*/
        if (pMailIdent == 0) {
            pMailIdent = neueMailIdent();
        }
        rcIdentMail = pMailIdent;

        String sendezeitpunkt = CaDatumZeit.DatumZeitStringFuerDatenbank();
        for (int i = 0; i < pEmpfaengerListe.size(); i++) {
            EclNachricht neueNachricht = new EclNachricht();
            neueNachricht.identMail = pMailIdent;

            if (pMandant != 0) {
                neueNachricht.mandant = pMandant;
                neueNachricht.hvJahr = lDbBundle.clGlobalVar.hvJahr;
                neueNachricht.hvNummer = lDbBundle.clGlobalVar.hvNummer;
                neueNachricht.dbArt = lDbBundle.clGlobalVar.datenbereich;
            }
            neueNachricht.antwortZuMailIdent = pAntwortAufNachricht;
            neueNachricht.userIdAbsender = pAbsenderIdent;
            neueNachricht.userIdEmpfaenger = pEmpfaengerListe.get(i);

            neueNachricht.sendezeitpunkt = sendezeitpunkt;

            if (pMailInhaltVollstaendig) {
                neueNachricht.mailTextAuchInEmailAuffuehren = 1;
            }
            if (pDateiAnhangVorhanden) {
                neueNachricht.anlagenSindVorhanden = 1;
            }
            neueNachricht.verwendungsCode = pVerwendungsCode;
            neueNachricht.betreff = pBetreff;
            neueNachricht.mailText = pInhalt;

            neueNachricht.parameter1 = pParameter1;
            neueNachricht.parameter2 = pParameter2;
            neueNachricht.parameter3 = pParameter3;
            neueNachricht.parameter4 = pParameter4;
            neueNachricht.parameter5 = pParameter5;

            lDbBundle.dbNachricht.insert(neueNachricht);

            lDbBundle.dbUserLogin.leseZuIdent(neueNachricht.userIdEmpfaenger);
            String empfaengerMail = lDbBundle.dbUserLogin.userLoginArray[0].email;
            if (!empfaengerMail.isEmpty()) {
                rcMailEmpfaenger.add(empfaengerMail);
                if (pMailInhaltVollstaendig) {
                    rcMailBetreff.add(neueNachricht.betreff);
                    rcMailInhalt.add(neueNachricht.mailText);
                } else {
                    rcMailBetreff.add(mailBetreff);
                    rcMailInhalt.add(mailInhalt);
                }
            }

        }
        dbClose();

        return 1;
    }

    private void ergaenzeEmpfaengerListeUmInsti(List<Integer> pEmpfaengerListe, List<Integer> pEmpfaengerInstiListe,
            int pMandant) {
        if (pEmpfaengerInstiListe == null) {
            return;
        }
        for (int i = 0; i < pEmpfaengerInstiListe.size(); i++) {
            CaBug.druckeLog("pEmpfaengerInstiListe.get(i)" + pEmpfaengerInstiListe.get(i),
                    logDrucken, 10);
            int instIdent = pEmpfaengerInstiListe.get(i);
            lDbBundle.dbUserLogin.lese_allZuInsti(instIdent, 0);
            for (int i1 = 0; i1 < lDbBundle.dbUserLogin.anzUserLoginGefunden(); i1++) {
                pEmpfaengerListe.add(lDbBundle.dbUserLogin.userLoginArray[i1].userLoginIdent);
                CaBug.druckeLog(
                        "lDbBundle.dbUserLogin.userLoginArray[i1].userLoginIdent"
                                + lDbBundle.dbUserLogin.userLoginArray[i1].userLoginIdent,
                        logDrucken, 10);
            }
        }
    };

    /*******************************Funktionen zum Anzeigen empfangener Nachrichten**************************/



    /**Detailsicht einer Nachricht*/
    public EclNachricht rcNachrichtDetail = null;
    public List<EclNachrichtAnhang> rcNachrichtAnhangArray = null;
    public EclUserNachrichtEmpfaenger[] rcAlleEmpfaengerArray = null;



    /**Besetzt rcNachrichtDetail, rcNachrichtAnhangList, rcAlleEmpfaengerArray*/
    public int leseDetailsZuNachricht(int pIdent, int pDbServerIdent) {
        dbOpenUndWeitere();

        /*Nachricht selbst*/
        lDbBundle.dbNachricht.read(pIdent, pDbServerIdent);
        rcNachrichtDetail = lDbBundle.dbNachricht.ergebnisPosition(0);

        /*Sender*/
        lDbBundle.dbUserLogin.leseZuIdent(rcNachrichtDetail.userIdAbsender);
        rcNachrichtDetail.senderName = lDbBundle.dbUserLogin.userLoginArray[0].name;

        /*Alle Anhänge*/
        lDbBundle.dbNachrichtAnhang.read_IdentMail(rcNachrichtDetail.identMail, rcNachrichtDetail.dbServerIdent);
        rcNachrichtAnhangArray = lDbBundle.dbNachrichtAnhang.ergebnis();

        /*Alle Empfänger*/
        lDbBundle.dbNachricht.readZuIdentMail(rcNachrichtDetail.identMail, pDbServerIdent);
        List<EclNachricht> lNachrichtArray = lDbBundle.dbNachricht.ergebnis();
        rcAlleEmpfaengerArray = new EclUserNachrichtEmpfaenger[lNachrichtArray.size()];
        for (int i = 0; i < lNachrichtArray.size(); i++) {
            int userIdEmpfaenger = lNachrichtArray.get(i).userIdEmpfaenger;
            lDbBundle.dbUserLogin.leseZuIdent(userIdEmpfaenger);
            rcAlleEmpfaengerArray[i] = new EclUserNachrichtEmpfaenger(lDbBundle.dbUserLogin.userLoginArray[0]);
        }

        dbClose();
        return 1;
    }

    /*********************Nachrichten verändern*******************************/
    public int nachrichtEinblendenBeimEmpfaenger(int pIdent, int pDbServerIdent) {
        dbOpenUndWeitere();
        lDbBundle.dbNachricht.setzeEinblenden(pIdent, pDbServerIdent);
        dbClose();
        return 1;
    }

    public int nachrichtAusblendenBeimEmpfaenger(EclNachricht pEclNachricht) {
        int rc=0;
        dbOpenUndWeitere();
        if (pEclNachricht.generierteNachricht==false) {
            /*Nachricht - direkt in Nachricht auf gelesen setzen*/
            rc=lDbBundle.dbNachricht.setzeAusblenden(pEclNachricht.ident, pEclNachricht.dbServerIdent);
            if (rc<1) {CaBug.drucke("001");}
        }
        else {
            /*Generierte Auftrag - je nach Modul nun setzen*/
            EclAuftrag lAuftrag=pEclNachricht.zugehoerigerAuftrag;
            switch (lAuftrag.gehoertZuModul){
                case KonstAuftragModul.ANBINDUNG_AKTIENREGISTER:
                    lDbBundle.dbAuftrag.mandantenabhaengig=true;
                    rc=lDbBundle.dbAuftrag.setzeStatusAuftragsArtGeloescht(lAuftrag.ident, lAuftrag.dbServerIdent);
                    if (rc<1) {CaBug.drucke("002");}
                    break;
                case KonstAuftragModul.KONTAKT_FORMULAR:
                    lDbBundle.dbAuftrag.mandantenabhaengig=false;
                    rc=lDbBundle.dbAuftrag.setzeStartAnzeigeGeloescht(lAuftrag.ident, lAuftrag.dbServerIdent);
                    if (rc<1) {CaBug.drucke("003");}
                    lDbBundle.dbAuftrag.mandantenabhaengig=true;
                    break;
            }
         }

        dbClose();
        return 1;
    }


    public int nachrichtAusblendenBeimEmpfaenger(int pIdent, int pDbServerIdent) {
        dbOpenUndWeitere();
        lDbBundle.dbNachricht.setzeAusblenden(pIdent, pDbServerIdent);
        dbClose();
        return 1;
    }

    public int nachrichtSetzeBearbeitetBeimEmpfaenger(int pIdent, int pDbServerIdent) {
        dbOpenUndWeitere();
        lDbBundle.dbNachricht.setzeBearbeitet(pIdent, pDbServerIdent);
        dbClose();
        return 1;
    }

    public int nachrichtSetzeUnbearbeitetBeimEmpfaenger(int pIdent, int pDbServerIdent) {
        dbOpenUndWeitere();
        lDbBundle.dbNachricht.setzeUnbearbeitet(pIdent, pDbServerIdent);
        dbClose();
        return 1;
    }

    public int nachrichtSetzeGelesenBeimEmpfaenger(EclNachricht pEclNachricht) {
        int rc=0;
        dbOpenUndWeitere();
        if (pEclNachricht.generierteNachricht==false) {
            /*Nachricht - direkt in Nachricht auf gelesen setzen*/
            rc=lDbBundle.dbNachricht.setzeGelesenBeimEmpfaenger(pEclNachricht.ident, pEclNachricht.dbServerIdent);
            if (rc<1) {CaBug.drucke("001");}
        }
        else {
            /*Generierte Auftrag - je nach Modul nun setzen*/
            EclAuftrag lAuftrag=pEclNachricht.zugehoerigerAuftrag;
            switch (lAuftrag.gehoertZuModul){
                case KonstAuftragModul.ANBINDUNG_AKTIENREGISTER:
                    lDbBundle.dbAuftrag.mandantenabhaengig=true;
                    rc=lDbBundle.dbAuftrag.setzeStatusAuftragsArtGelesen(lAuftrag.ident, lAuftrag.dbServerIdent);
                    if (rc<1) {CaBug.drucke("002");}
                    break;
                case KonstAuftragModul.KONTAKT_FORMULAR:
                    lDbBundle.dbAuftrag.mandantenabhaengig=false;
                    rc=lDbBundle.dbAuftrag.setzeStartAnzeigeGelesen(lAuftrag.ident, lAuftrag.dbServerIdent);
                    if (rc<1) {CaBug.drucke("003");}
                    lDbBundle.dbAuftrag.mandantenabhaengig=true;
                    break;
            }
         }

        dbClose();
        return 1;
    }



    /**Liefert vollständigen Pfad mit Dateinahme zum Anzeigen eines Anhangs*/
    public String lieferePfadDateiname(int pIdent, String pDateiname) {
        return liefereFolder() + liefereDateinameDisk(pIdent, pDateiname);
    }

    /********************Übergreifende Hilfsfunktionen**************************/
    private String liefereFolder() {
        return lDbBundle.clGlobalVar.lwPfadAllgemein + "\\meetingnachrichtenanhang\\";
    }

    private String liefereDateinameDisk(int pIdent, String pDateiname) {
        return CaString.fuelleLinksNull(Integer.toString(pIdent), 6) + "_" + pDateiname;

    }


    /********************************************Allgemeine Mail-Funktionen**************************************************/

    /**Alle Nachrichten, die einem Empfänger zugeordnet sind*/
    public List<EclNachricht> rcEmpfangeneNachrichtenList =  new LinkedList<EclNachricht>();
    public int rcEmpfangeneNachrichtenAnzahlUngelesen=0;

    /**Alle Nachrichten, die einem Sender zugeordnet sind*/
    public List<EclNachricht> rcGesendeteNachrichtenList = new LinkedList<EclNachricht>();

    /**Besetzt rcEmpfangeneNachrichtenList. Open erfolgt hier je nach Stub-übergabe*/
    public int leseAlleEmpfangenenNachrichten(int pUserIdEmpfaenger) {
        dbOpenUndWeitere();
        int rc=leseAlleEmpfangenenNachrichtenOhneOpen(pUserIdEmpfaenger);
        dbClose();
        return rc;
    }

    private int leseAlleEmpfangenenNachrichtenOhneOpen(int pUserIdEmpfaenger) {
        rcEmpfangeneNachrichtenList=new LinkedList<EclNachricht>();
        if (lDbBundle.param.mandant!=0) {
            lDbBundle.dbNachricht.readZuEmpfaenger(pUserIdEmpfaenger);
            rcEmpfangeneNachrichtenList = lDbBundle.dbNachricht.ergebnis();
            for (int i = 0; i < lDbBundle.dbNachricht.anzErgebnis(); i++) {
                EclNachricht lNachricht = rcEmpfangeneNachrichtenList.get(i);
                lNachricht.senderName=holeNameZuSenderEmpfaengerIdent(lNachricht.userIdAbsender);
            }
        }
        if (pUserIdEmpfaenger>0) {
            /*bei Nicht-Aktionären auch die mandantenübergreifenden Mails einlesen*/
            lDbBundle.dbNachricht.mandantenabhaengig=false;
            lDbBundle.dbNachricht.readZuEmpfaenger(pUserIdEmpfaenger);
            for (int i = 0; i < lDbBundle.dbNachricht.anzErgebnis(); i++) {
                EclNachricht lNachricht =  lDbBundle.dbNachricht.ergebnisPosition(i);
                lNachricht.senderName=holeNameZuSenderEmpfaengerIdent(lNachricht.userIdAbsender);
                rcEmpfangeneNachrichtenList.add(lNachricht);
            }

            lDbBundle.dbNachricht.mandantenabhaengig=true;
        }
//        CaBug.druckeLog("rcEmpfangeneNachrichtenList==null"+(rcEmpfangeneNachrichtenList==null), logDrucken, 10);
        return 1;
    }

    /**Besetzt rcGesendeteNachrichtenList*/
    public int leseAlleGesendetenNachrichten(int pUserIdSender) {
        dbOpenUndWeitere();
        rcGesendeteNachrichtenList=new LinkedList<EclNachricht>();
        if (lDbBundle.param.mandant!=0) {
            lDbBundle.dbNachricht.readZuSender(pUserIdSender);
            rcGesendeteNachrichtenList = lDbBundle.dbNachricht.ergebnis();
            for (int i = 0; i < lDbBundle.dbNachricht.anzErgebnis(); i++) {
                EclNachricht lNachricht = rcGesendeteNachrichtenList.get(i);
                lNachricht.empfaengerName = holeNameZuSenderEmpfaengerIdent(lNachricht.userIdEmpfaenger);
            }
        }
        if (pUserIdSender>0) {
            /*bei Nicht-Aktionären auch die mandantenübergreifenden Mails einlesen*/
            lDbBundle.dbNachricht.mandantenabhaengig=false;
            lDbBundle.dbNachricht.readZuEmpfaenger(pUserIdSender);
            for (int i = 0; i < lDbBundle.dbNachricht.anzErgebnis(); i++) {
                EclNachricht lNachricht = lDbBundle.dbNachricht.ergebnisPosition(i);
                lNachricht.senderName=holeNameZuSenderEmpfaengerIdent(lNachricht.userIdAbsender);
                rcGesendeteNachrichtenList.add(lNachricht);
            }

            lDbBundle.dbNachricht.mandantenabhaengig=true;
        }
        dbClose();
        return 1;
    }



    /**Felder, die von holeNameZuSenderEmpfaengerIdent geliefert werden.
     * TODO: sollten nicht bei jeder Nachricht neu eingelesen werden ....
     */
    private boolean senderEmpfaengerIstAktionaer=false;
    private String senderEmpfaengerName="";
    private String senderEmpfaengerKennung="";
    /**Holt je nach pIdent den Namen des Senders/Empfängers*/
    private String holeNameZuSenderEmpfaengerIdent(int pIdent) {
        if (pIdent>0) {
            lDbBundle.dbUserLogin.leseZuIdent(pIdent);
            return (lDbBundle.dbUserLogin.userLoginArray[0].name);
        }
        if (pIdent==0){
            return ("System");
        }

        /*An dieser Stelle gilt: pIdent<0*/
        int erg=lDbBundle.dbLoginDaten.read_ident(pIdent*(-1));
        EclLoginDaten eclLoginDaten=lDbBundle.dbLoginDaten.ergebnisPosition(0);
        switch (eclLoginDaten.kennungArt) {
        case KonstLoginKennungArt.aktienregister:
            erg = lDbBundle.dbAktienregister.leseZuAktienregisterIdent(eclLoginDaten.aktienregisterIdent);
            if (erg <= 0) {
                CaBug.drucke("001");
                return ("");
            }
            EclAktienregister eclAktienregister = lDbBundle.dbAktienregister.ergebnisPosition(0);
            senderEmpfaengerIstAktionaer=true;
            senderEmpfaengerName=eclAktienregister.liefereNameKommaTitelVorname();
            senderEmpfaengerKennung=eclAktienregister.aktionaersnummer;
            return (senderEmpfaengerName);
        case KonstLoginKennungArt.personenNatJur:
            erg = lDbBundle.dbPersonenNatJur.read(eclLoginDaten.personenNatJurIdent);
            if (erg <= 0) {
                CaBug.drucke("002");
                return "";
            }
            EclPersonenNatJur eclPersonenNatJur = lDbBundle.dbPersonenNatJur.PersonNatJurGefunden(0);
            senderEmpfaengerIstAktionaer=false;
            senderEmpfaengerName=eclPersonenNatJur.liefereNameKommaTitelVorname();
            senderEmpfaengerKennung=eclLoginDaten.loginKennung;
            return eclPersonenNatJur.liefereNameKommaTitelVorname();
        }
        CaBug.drucke("003");
        return "";

    }

    /*********************************Funktionen für Aktionär**************************************************/
    /**Besetzt rcEmpfangeneNachrichtenList, rcEmpfangeneNachrichtenAnzahlUngelesen*/
    public int leseNachrichtenUndAuftraegeFuerAktionaer(int pUserIdEmpfaenger) {
        dbOpenUndWeitere();
        /*Lese "normale" empfangene Nachrichten ein*/
        int rc=leseAlleEmpfangenenNachrichten(pUserIdEmpfaenger);
        if (rc<1) {
            dbClose();
            return rc;
        }

        /*Ergänze "normale" Nachrichten um System-generierte Nachrichten aus Aufträgen - mandantenabhängig*/
        lDbBundle.dbAuftrag.read_allBenutzer(pUserIdEmpfaenger);
        int anz=lDbBundle.dbAuftrag.anzErgebnis();
        for (int i=0;i<anz;i++) {
            EclAuftrag lAuftrag=lDbBundle.dbAuftrag.ergebnisPosition(i);
            switch (lAuftrag.gehoertZuModul) {
            case KonstAuftragModul.ANBINDUNG_AKTIENREGISTER:
                ergaenzeNachrichtUmAuftrag_AnbindungAktienregister(lAuftrag);
                break;
            }
        }

        /*Ergänze "normale" Nachrichten um System-generierte Nachrichten aus Aufträgen - mandanten-un-abhängig*/
        lDbBundle.dbAuftrag.mandantenabhaengig=false;
        lDbBundle.dbAuftrag.read_allBenutzer(pUserIdEmpfaenger);
        anz=lDbBundle.dbAuftrag.anzErgebnis();
        CaBug.druckeLog("Anzahl mandantenunabhängiger Aufträge="+anz, logDrucken, 10);
        for (int i=0;i<anz;i++) {
            EclAuftrag lAuftrag=lDbBundle.dbAuftrag.ergebnisPosition(i);
            switch (lAuftrag.gehoertZuModul) {
            case KonstAuftragModul.KONTAKT_FORMULAR:
                ergaenzeNachrichtUmAuftrag_Kontaktformular(lAuftrag);
                break;
            }
        }
        lDbBundle.dbAuftrag.mandantenabhaengig=true;

        /*Belege rcEmpfangeneNachrichtenAnzahlUngelesen*/
        rcEmpfangeneNachrichtenAnzahlUngelesen=0;
        for (EclNachricht iEclNachricht: rcEmpfangeneNachrichtenList) {
            if (iEclNachricht.anzeigeBeimEmpfaengerGelesen==0) {
                rcEmpfangeneNachrichtenAnzahlUngelesen++;
            }
        }

        /*Sortiere Nachrichten nach Datum*/
        sortiereNachDatumAbsteigend(rcEmpfangeneNachrichtenList);

        dbClose();
        return 1;
    }

    private int ergaenzeNachrichtUmAuftrag_AnbindungAktienregister(EclAuftrag pEclAuftrag) {
        if (pEclAuftrag.status==KonstAuftragStatus.ERLEDIGT) {
            //Auftrag final erledigt
            return 1;
        }

        if (pEclAuftrag.statusAuftragsArtGeloescht==1) {
            /*Auftrag zwar noch nicht erledigt, aber Mail für letzte Statusänderung wurde
             * vom Benutzer als gelöscht markiert
             */
            return 1;
        }

        EclNachricht neueNachricht=new EclNachricht();
        neueNachricht.senderName=holeNameZuSenderEmpfaengerIdent(0);
        neueNachricht.empfaengerName=holeNameZuSenderEmpfaengerIdent(pEclAuftrag.userIdAuftraggeber);
        neueNachricht.anzeigeBeimEmpfaengerGelesen=pEclAuftrag.statusAuftragsArtGelesen;
        CaBug.druckeLog("pEclAuftrag.statusAuftragsArtGelesen="+pEclAuftrag.statusAuftragsArtGelesen, logDrucken, 10);
        neueNachricht.generierteNachricht=true;
        neueNachricht.sendezeitpunkt=pEclAuftrag.zeitLetzterStatuswechsel;

        neueNachricht.zugehoerigerAuftrag=pEclAuftrag;

        neueNachricht.betreffTextNrVor=KonstPortalTexte.mailBetreffAuftragsart(pEclAuftrag.auftragsArt);
        neueNachricht.betreffTextNrNach=KonstPortalTexte.mailBetreffAuftragStatusAuftragsArt(pEclAuftrag.statusAuftragsArt);
        neueNachricht.mailTextTextNrVor=KonstPortalTexte.mailTextAuftragsart(pEclAuftrag.auftragsArt);
        neueNachricht.mailTextTextNrNach=KonstPortalTexte.mailTextAuftragStatusAuftragsArt(pEclAuftrag.statusAuftragsArt);
        neueNachricht.mailTextNr=KonstPortalTexte.mailTextNrBasis(pEclAuftrag.auftragsArt, pEclAuftrag.parameterTextLang[5]);
        neueNachricht.funktionsButton1TextNr=0;//1556;
        neueNachricht.funktionsButton2TextNr=0;//1557;

        switch (pEclAuftrag.auftragsArt) {
        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_NEWSLETTER:
            switch (pEclAuftrag.parameterInt[0]) {
            case 1:neueNachricht.mailText="Versand per Post";break;
            case 2:neueNachricht.mailText="Versand per E-Mail";break;
            case 3:neueNachricht.mailText="Kein Newsletter";break;
            }
            break;

        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_ANSCHRIFT:
            neueNachricht.mailText="";
            for (int i=0;i<5;i++) {
                if (!pEclAuftrag.parameterTextLang[i].isEmpty()) {
                    neueNachricht.mailText+=pEclAuftrag.parameterTextLang[i]+"; ";
                }
            }
            break;

        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_BANKVERBINDUNG:
            neueNachricht.mailText="";
            for (int i=0;i<4;i++) {
                if (!pEclAuftrag.parameterTextLang[i].isEmpty()) {
                    if (i!=2) {
                        neueNachricht.mailText+=pEclAuftrag.parameterTextLang[i]+"; ";
                    }
                    else {
                        neueNachricht.mailText+=CaBank.ibanFuerAnzeige(pEclAuftrag.parameterTextLang[i])+"; ";
                    }
                }
            }
            break;

        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON1:
        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON2:
        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON3:
        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_TELEFON4:
        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_EMAIL:
        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_GEBURTSDATUM:
        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_AENDERN_MEINEDATEN_STEUERID:
            neueNachricht.mailText=pEclAuftrag.parameterTextLang[0].trim();
            if (neueNachricht.mailText.isEmpty()) {
                neueNachricht.mailText="ohne Angabe";
            }
            break;

        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_ANTRAG_BETEILIGUNGSERHOEHUNG_ZEICHNUNG_OHNE_AUFFUELLBETRAG:
        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_ANTRAG_BETEILIGUNGSERHOEHUNG_ZEICHNUNG_UND_AUFFUELLBETRAG:
        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_ANTRAG_BETEILIGUNGSERHOEHUNG_OHNE_ZEICHNUNG_MIT_AUFFUELLBETRAG:
        case KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_ANTRAG_BETEILIGUNGSERHOEHUNG_ZEICHNUNG_OHNE_AUFFUELLBETRAG_TEXTFORM:
            List<String> lVariablen=new LinkedList<String>();
            List<String> lInhalt=new LinkedList<String>();
//          @formatter:off
            lVariablen.add("Erhoehungsbetrag");lInhalt.add(pEclAuftrag.parameterTextLang[0]); //pErhoehungsbetrag
            lVariablen.add("Auffuellbetrag");lInhalt.add(pEclAuftrag.parameterTextLang[1]); //pAuffuellbetrag
            lVariablen.add("WeitereAnteile");lInhalt.add(pEclAuftrag.parameterTextLang[2]); //pWeitereAnteile

            String hMitgliedsnummer=senderEmpfaengerKennung;
            if (senderEmpfaengerIstAktionaer) {
                hMitgliedsnummer=BlNummernformBasis.aufbereitenKennungFuerExtern(hMitgliedsnummer, lDbBundle);
            }
            lVariablen.add("Aktionaersnummer");lInhalt.add(hMitgliedsnummer);
            lVariablen.add("Aktionaersname");lInhalt.add(senderEmpfaengerName);

            /*Bei dieser Auftragsart wird neueNachricht.mailTextNr verwendet, mit anschließender Füllung dieser Variablen*/
            neueNachricht.betreffTextNrNach=0;
            neueNachricht.mailTextTextNrNach=0; //Vorsorglich erst mal hier wieder auf 0 setzen, da noch kein Status vorgesehen
            neueNachricht.variablenListe=lVariablen;
            neueNachricht.inhaltListe=lInhalt;
//          @formatter:on

            CaBug.druckeLog(String.valueOf(neueNachricht.mailTextNr), 10, 10);
            
            break;
        }

        if (pEclAuftrag.auftragsArt!=KonstAuftragArt.ANBINDUNG_AKTIENREGISTER_ANTRAG_BETEILIGUNGSERHOEHUNG_ZEICHNUNG_OHNE_AUFFUELLBETRAG) {
            rcEmpfangeneNachrichtenList.add(neueNachricht);
        }
        return 1;
    }

    private int ergaenzeNachrichtUmAuftrag_Kontaktformular(EclAuftrag pEclAuftrag) {
        CaBug.druckeLog("Start", logDrucken, 10);
        if (pEclAuftrag.status==KonstAuftragStatus.ERLEDIGT) {
            //Auftrag final erledigt
            return 1;
        }

        if (pEclAuftrag.startAnzeigeGeloescht==1) {
            /*Auftrag zwar noch nicht erledigt, aber Mail für letzte Statusänderung wurde
             * vom Benutzer als gelöscht markiert
             */
            return 1;
        }
        CaBug.druckeLog("Nachricht füllen Start", logDrucken, 10);

        EclNachricht neueNachricht=new EclNachricht();
        neueNachricht.senderName=holeNameZuSenderEmpfaengerIdent(0);
        neueNachricht.empfaengerName=holeNameZuSenderEmpfaengerIdent(pEclAuftrag.userIdAuftraggeber);
        neueNachricht.anzeigeBeimEmpfaengerGelesen=pEclAuftrag.startAnzeigeGelesen;
        CaBug.druckeLog("pEclAuftrag.statusAuftragsArtGelesen="+pEclAuftrag.statusAuftragsArtGelesen, logDrucken, 10);
        neueNachricht.generierteNachricht=true;
        neueNachricht.sendezeitpunkt=pEclAuftrag.zeitStart;

        neueNachricht.zugehoerigerAuftrag=pEclAuftrag;

        neueNachricht.betreffTextNrVor=1559;
        neueNachricht.betreff=pEclAuftrag.parameterTextLang[1];
        neueNachricht.betreffTextNrNach=1560;

        neueNachricht.mailTextTextNrVor=1561;
        neueNachricht.mailText=pEclAuftrag.freitextBeschreibung;
        neueNachricht.mailTextTextNrNach=1562;

        neueNachricht.funktionsButton1TextNr=0;
        neueNachricht.funktionsButton2TextNr=0;


        rcEmpfangeneNachrichtenList.add(neueNachricht);
        return 1;
    }

    /***********************Sortierfunktionen für Nachrichten****************************************/
//    private void sortiereNachDatumAufsteigend(List<EclNachricht> pNachrichtenListe){
//        Comparator<EclNachricht> nachDatumAbsteigend=(t1, t2)-> t1.sendezeitpunkt.compareTo(t2.sendezeitpunkt);
//        pNachrichtenListe.sort(nachDatumAbsteigend);
//    }

    private void sortiereNachDatumAbsteigend(List<EclNachricht> pNachrichtenListe){
        Comparator<EclNachricht> nachDatumAbsteigend=(t1, t2)-> t2.sendezeitpunkt.compareTo(t1.sendezeitpunkt);
        pNachrichtenListe.sort(nachDatumAbsteigend);
    }

}
