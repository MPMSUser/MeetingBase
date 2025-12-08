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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.IOException;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComAllg.CaTokenUtil;
import de.meetingapps.meetingportal.meetComBl.BlAbstimmungsListe;
import de.meetingapps.meetingportal.meetComBl.BlMitteilungen;
import de.meetingapps.meetingportal.meetComBl.BlTeilnehmerLoginNeu;
import de.meetingapps.meetingportal.meetComBlManaged.BlMAbstimmung;
import de.meetingapps.meetingportal.meetComBlManaged.BlMVirtuelleHV;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungSetM;
import de.meetingapps.meetingportal.meetComEclM.EclAbstimmungenListeM;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclDbM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEh.EhInhaltsHinweise;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilung;
import de.meetingapps.meetingportal.meetComEntities.EclMitteilungBestand;
import de.meetingapps.meetingportal.meetComEntities.EclWortmeldetischProtokoll;
import de.meetingapps.meetingportal.meetComKonst.KonstMitteilungStatus;
import de.meetingapps.meetingportal.meetComKonst.KonstMitteilungViewBereich;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalTexte;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalView;
import de.meetingapps.meetingportal.meetComKonst.KonstWeisungserfassungSicht;
import de.meetingapps.meetingportal.meetingCoreReport.RpBrowserAnzeigen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TBestandDurcharbeiten;
import de.meetingapps.meetingportal.meetingportTFunktionen.TFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPortalFunktionen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TPruefeStartNachOpen;
import de.meetingapps.meetingportal.meetingportTFunktionen.TSessionVerwaltung;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@RequestScoped
@Named
public class TMitteilung {

    private int logDrucken = 10;

    private @Inject EclDbM eclDbM;
    private @Inject EclParamM eclParamM;
    private @Inject EclLoginDatenM eclLoginDatenM;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;
    private @Inject TBestandDurcharbeiten tBestandDurcharbeiten;
    private @Inject BlMVirtuelleHV blMVirtuelleHV;
    private @Inject BlMAbstimmung blMAbstimmung;

    private @Inject TSessionVerwaltung tSessionVerwaltung;
    private @Inject TPortalFunktionen tPortalFunktionen;
    private @Inject TFunktionen tFunktionen;
    private @Inject TPruefeStartNachOpen tPruefeStartNachOpen;
    private @Inject EclAbstimmungenListeM eclAbstimmungenListeM;
    private @Inject EclAbstimmungSetM eclAbstimmungSetM;

    private @Inject TAuswahlSession tAuswahlSession;
    private @Inject TSession tSession;
    private @Inject TMitteilungSession tMitteilungSession;

    /**pFunktion Wert laut KonstPortalFunktionen
     * EclDbM wird in aufrufender Funktion verwaltet*/
    public int initAusAuswahl(int pFunktion) {
        tMitteilungSession.setErstaufrufAusAuswahl(true);
        init(pFunktion);
        tMitteilungSession.setErstaufrufAusAuswahl(false);
        return 1;
    }

    public int init(int pFunktion) {

        protokoll("initNeueMitteilung");

        BlMitteilungen blMitteilungen = new BlMitteilungen(true, eclDbM.getDbBundle(), pFunktion);
        initSmall(pFunktion, blMitteilungen);

        CaBug.druckeLog("blMitteilungen.rcListeAnzeigen" + blMitteilungen.rcListeAnzeigen, logDrucken, 10);

        if (tMitteilungSession.getStellerZulaessig() != 3 && eclLoginDatenM.liefereKennungArt() == 1) {
            /*TODO - hier müßten eigentlich alle zugeordneten anderen Kennungen auch noch überprüft werden*/
            EclAktienregister lEigenerBestand = eclBesitzGesamtM.eigenerBestandAktienregister();
            if (lEigenerBestand.pruefePersonengemeinschaft() && (tMitteilungSession.getStellerZulaessig() & 1) == 0) {
                tMitteilungSession.setAnzuzeigenderBereich(KonstMitteilungViewBereich.personenArtNichtZulaessig);
                tMitteilungSession.setPersonengemeinschaftNichtZulaessig(true);
                return 1;
            }
            if (lEigenerBestand.istJuristischePerson == 1 && (tMitteilungSession.getStellerZulaessig() & 2) == 0) {
                tMitteilungSession.setAnzuzeigenderBereich(KonstMitteilungViewBereich.personenArtNichtZulaessig);
                tMitteilungSession.setJuristischePersonNichtZulaessig(true);
                return 1;
            }
        }

        holeMitteilungenZuLoginKennung(blMitteilungen);

        blMitteilungen.holeRednerListe();
        tMitteilungSession.setRednerListe(blMitteilungen.rcRednerliste);
        tMitteilungSession.setAnzahlRednerListe(blMitteilungen.rcRednerliste.size());

        if (tMitteilungSession.getAnzahlMitteilungenGestelltOhneZurueckgezogen() >= tMitteilungSession
                .getAnzahlJeAktionaer()) {
            tMitteilungSession.setAnzuzeigenderBereich(KonstMitteilungViewBereich.keineWeiterenMitteilungenMoeglich);
            return 1;
        }

        tMitteilungSession.setAnzuzeigenderBereich(KonstMitteilungViewBereich.ausgangsView);

        konferenzStatusEinlesen();

        return 1;
    }

    private void inhaltsHinweiseAnbietenBelegen() {
        if (tMitteilungSession.getArtDerMitteilung() != KonstPortalFunktionen.wortmeldungen
                || eclParamM.getParam().paramPortal.wortmeldungInhaltsHinweiseAktiv != 0) {
            tMitteilungSession.setInhaltsHinweiseAnbieten(true);
        } else {
            tMitteilungSession.setInhaltsHinweiseAnbieten(false);
        }
    }

    /**Init-Funktion ausgelagert, da diese für Test-Mail-Versendung benötigt wird.
     * BlMitteilungen muß mit new erzeugt worden sein.*/
    public int initSmall(int pFunktion, BlMitteilungen blMitteilungen) {
        tMitteilungSession.setArtDerMitteilung(pFunktion);

        blMitteilungen.paramBelegen();
        tMitteilungSession.setStellerAbfragen(blMitteilungen.rcStellerAbfragen);
        tMitteilungSession.setNameAbfragen(blMitteilungen.rcNameAbfragen);
        tMitteilungSession.setKontaktdatenAbfragen(blMitteilungen.rcKontaktdatenAbfragen);
        tMitteilungSession.setKontaktdatenTelefonAbfragen(blMitteilungen.rcKontaktdatenTelefonAbfragen);
        tMitteilungSession.setKontaktdatenEMailVorschlagen(blMitteilungen.rcKontaktdatenEMailVorschlagen);
        tMitteilungSession.setKurztextAbfragen(blMitteilungen.rcKurztextAbfragen);
        tMitteilungSession.setTopListeAnbieten(blMitteilungen.rcTopListeAnbieten);
        tMitteilungSession.setSkIstZuTopListe(blMitteilungen.rcSkIstZuTopListe);
        tMitteilungSession.setLangtextAbfragen(blMitteilungen.rcLangtextAbfragen);
        tMitteilungSession.setLangtextUndDateiNurAlternativ(blMitteilungen.rcLangtextUndDateiNurAlternativ);
        tMitteilungSession.setZurueckziehenMoeglich(blMitteilungen.rcZurueckziehenMoeglich);
        tMitteilungSession.setLaenge(blMitteilungen.rcLaenge);
        tMitteilungSession.setAnzahlJeAktionaer(blMitteilungen.rcAnzahlJeAktionaer);
        tMitteilungSession.setStellerZulaessig(blMitteilungen.rcStellerZulaessig);

        tMitteilungSession.setInhaltsHinweiseTextDE(blMitteilungen.rcInhaltsHinweiseTextDE);
        tMitteilungSession.setInhaltsHinweiseTextEN(blMitteilungen.rcInhaltsHinweiseTextEN);
        tMitteilungSession.setInhaltsHinweiseAktiv(blMitteilungen.rcInhaltsHinweiseAktiv);
        tMitteilungSession.setInhaltsHinweiseVorhandenundAktiv(blMitteilungen.rcInhaltsHinweiseVorhandenundAktiv);
        tMitteilungSession.setInhaltsHinweiseListe(blMitteilungen.rcInhaltsHinweiseListe);

        tMitteilungSession.setListeAnzeigen(blMitteilungen.rcListeAnzeigen);
        tMitteilungSession.setMailBeiEingang(blMitteilungen.rcMailBeiEingang);
        tMitteilungSession.setMailVerteiler1(blMitteilungen.rcMailVerteiler1);
        tMitteilungSession.setMailVerteiler2(blMitteilungen.rcMailVerteiler2);
        tMitteilungSession.setMailVerteiler3(blMitteilungen.rcMailVerteiler3);

        tMitteilungSession.setHinweisGelesen(blMitteilungen.rcHinweisGelesen);
        tMitteilungSession.setDateiHochladenMoeglich(blMitteilungen.rcDateiHochladenMoeglich);
        tMitteilungSession.setVideoDateiMoeglich(blMitteilungen.rcVideoDateiMoeglich);
        tMitteilungSession.setVideoZusatz(blMitteilungen.rcVideoZusatz);
        tMitteilungSession.setVideoFormate(blMitteilungen.rcVideoFormate);
        tMitteilungSession.setVideoLaenge(blMitteilungen.rcVideoLaenge);
        tMitteilungSession.setTextDateiMoeglich(blMitteilungen.rcTextDateiMoeglich);
        tMitteilungSession.setTextDateiZusatz(blMitteilungen.rcTextDateiZusatz);
        tMitteilungSession.setTextFormate(blMitteilungen.rcTextFormate);
        tMitteilungSession.setTextLaenge(blMitteilungen.rcTextLaenge);

        if (tMitteilungSession.isErstaufrufAusAuswahl()) {
            if (blMitteilungen.rcHinweisVorbelegenMit == 1) {
                tMitteilungSession.setHinweisBestaetigt(true);
            } else {
                tMitteilungSession.setHinweisBestaetigt(false);
            }
        }

        boolean aktiv = pruefeObAktiv();

        CaBug.druckeLog("A", logDrucken, 10);

        switch (pFunktion) {
        case KonstPortalFunktionen.fragen:
            CaBug.druckeLog("Switch Fragen", logDrucken, 10);
            tMitteilungSession.setTextNrStarttextAktiv("769");
            tMitteilungSession.setTextNrStarttextInaktiv("770");

            tMitteilungSession.setTextNrAusgangsViewStarttext("771");
            tMitteilungSession.setTextNrAusgangsViewButtonStart("772");
            tMitteilungSession.setTextNrAusgangsViewEndetext("773");

            tMitteilungSession.setTextNrPersonBestaetigenStarttext("777");
            tMitteilungSession.setTextNrPersonBestaetigenTextVorName("778");
            tMitteilungSession.setTextNrPersonBestaetigenTextNachName("779");
            tMitteilungSession.setTextNrPersonBestaetigenJaButton("1257");
            tMitteilungSession.setTextNrPersonBestaetigenNeinButton("1258");

            tMitteilungSession.setTextNrPersonNichtBestaetigt("788");

            tMitteilungSession.setTextNrPersonengemeinschaftNichtZulaessig("1259");
            tMitteilungSession.setTextNrJuristischePersonNichtZulaessig("1260");

            tMitteilungSession.setTextNrMitteilungStarttext("780");
            tMitteilungSession.setTextNrMitteilungNameAbfragen("1261");
            tMitteilungSession.setTextNrMitteilungKontaktAbfragen("1262");
            tMitteilungSession.setTextNrMitteilungKontaktTelefonAbfragen("2067");
            tMitteilungSession.setTextNrMitteilungHinweisGelesen("1787");
            tMitteilungSession.setTextNrVorTOPAuswahl("1890");
            tMitteilungSession.setTextNrNachTOPAuswahl("1891");
            tMitteilungSession.setTextNrVorInhaltsHinweise("2074");
            tMitteilungSession.setTextNrMitteilungVorKurzText("783");
            tMitteilungSession.setTextNrMitteilungVorLangText("784");
            tMitteilungSession.setTextNrMitteilungLangTextVorZaehler("793");
            tMitteilungSession.setTextNrMitteilungLangTextNachZaehler("785");

            tMitteilungSession.setTextNrStarttextHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrNochKeineDateiAusgewaehltHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrButtonDateiAuswaehlenHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrVorDateiNameHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrNachDateiNameHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrEndetextHochladenBereich("790"); //nur als Dummy

            tMitteilungSession.setTextNrMitteilungButtonAbschicken("786");
            tMitteilungSession.setTextNrMitteilungEndetext("787");

            tMitteilungSession.setTextNrKeineWeiterenMitteilungen("789");

            tMitteilungSession.setTextNrEndetextAktiv("790");

            tMitteilungSession.setTextNrListeStarttext("790"); //nur als Dummy
            tMitteilungSession.setTextNrListeButtonAktualisieren("790"); //nur als Dummy
            tMitteilungSession.setTextNrListeEndetext("790"); //nur als Dummy

            tMitteilungSession.setTextNrListeMitteilungenStarttext("791");
            tMitteilungSession.setTextNrListeMitteilungenButtonZurueckziehen("1263");
            tMitteilungSession.setTextNrListeMitteilungenEndetext("792");

            tMitteilungSession.setTextNrMailBetreff("1264"); //Default-Wert
            tMitteilungSession.setTextNrMailBetreffErteilt("1264");
            tMitteilungSession.setTextNrMailBetreffZurueckgezogen("1265");
            tMitteilungSession.setTextNrMailLfdIdent("1266");
            tMitteilungSession.setTextNrMailKennung("1267");
            tMitteilungSession.setTextNrMailSteller("1268");
            tMitteilungSession.setTextNrMailNameAbgefragt("1269");
            tMitteilungSession.setTextNrMailKontaktdaten("1270");
            tMitteilungSession
                    .setTextNrMailKontaktdatenTelefon(KonstPortalTexte.MITTEILUNG_FRAGEN_MAIL_KONTAKTDATENTELEFON);
            tMitteilungSession.setTextNrMailAktien("1271");
            tMitteilungSession.setTextNrMailKurztext("1272");
            tMitteilungSession.setTextNrMailLangtext("1273");
            tMitteilungSession.setTextNrMailZeitpunktErteilt("1274");
            tMitteilungSession.setTextNrMailZurueckgezogen("1275");
            tMitteilungSession.setTextNrMailZeitpuntZurueckgezogen("1276");
            tMitteilungSession.setTextNrMailHinweisBestaetigt("1921");
            break;
        case KonstPortalFunktionen.wortmeldungen:
            tMitteilungSession.setTextNrStarttextAktiv("1131");
            tMitteilungSession.setTextNrStarttextInaktiv("1132");

            tMitteilungSession.setTextNrAusgangsViewStarttext("1133");
            tMitteilungSession.setTextNrAusgangsViewButtonStart("1134");
            tMitteilungSession.setTextNrAusgangsViewEndetext("1135");

            tMitteilungSession.setTextNrPersonBestaetigenStarttext("1139");
            tMitteilungSession.setTextNrPersonBestaetigenTextVorName("1140");
            tMitteilungSession.setTextNrPersonBestaetigenTextNachName("1141");
            tMitteilungSession.setTextNrPersonBestaetigenJaButton("1279");
            tMitteilungSession.setTextNrPersonBestaetigenNeinButton("1280");

            tMitteilungSession.setTextNrPersonNichtBestaetigt("1151");

            tMitteilungSession.setTextNrPersonengemeinschaftNichtZulaessig("1281");
            tMitteilungSession.setTextNrJuristischePersonNichtZulaessig("1282");

            tMitteilungSession.setTextNrMitteilungStarttext("1142");
            tMitteilungSession.setTextNrMitteilungNameAbfragen("1166");
            tMitteilungSession.setTextNrMitteilungKontaktAbfragen("1167");
            tMitteilungSession.setTextNrMitteilungKontaktTelefonAbfragen("2068");
            tMitteilungSession.setTextNrMitteilungHinweisGelesen("1788");
            tMitteilungSession.setTextNrVorTOPAuswahl("1892");
            tMitteilungSession.setTextNrNachTOPAuswahl("1893");
            tMitteilungSession.setTextNrVorInhaltsHinweise("2075");
            tMitteilungSession.setTextNrMitteilungVorKurzText("1145");
            tMitteilungSession.setTextNrMitteilungVorLangText("1146");
            tMitteilungSession.setTextNrMitteilungLangTextVorZaehler("1147");
            tMitteilungSession.setTextNrMitteilungLangTextNachZaehler("1148");

            tMitteilungSession.setTextNrStarttextHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrNochKeineDateiAusgewaehltHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrButtonDateiAuswaehlenHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrVorDateiNameHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrNachDateiNameHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrEndetextHochladenBereich("790"); //nur als Dummy

            tMitteilungSession.setTextNrMitteilungButtonAbschicken("1149");
            tMitteilungSession.setTextNrMitteilungEndetext("1150");

            tMitteilungSession.setTextNrKeineWeiterenMitteilungen("1152");

            tMitteilungSession.setTextNrEndetextAktiv("1153");

            tMitteilungSession.setTextNrListeStarttext("1154");
            tMitteilungSession.setTextNrListeButtonAktualisieren("1155");
            tMitteilungSession.setTextNrListeEndetext("1156");

            tMitteilungSession.setTextNrListeMitteilungenStarttext("1157");
            tMitteilungSession.setTextNrListeMitteilungenButtonZurueckziehen("1195");
            tMitteilungSession.setTextNrListeMitteilungenEndetext("1165");

            tMitteilungSession.setTextNrMailBetreff("1283");//Default-Wert
            tMitteilungSession.setTextNrMailBetreffErteilt("1283");
            tMitteilungSession.setTextNrMailBetreffZurueckgezogen("1284");
            tMitteilungSession.setTextNrMailLfdIdent("1285");
            tMitteilungSession.setTextNrMailKennung("1286");
            tMitteilungSession.setTextNrMailSteller("1287");
            tMitteilungSession.setTextNrMailNameAbgefragt("1288");
            tMitteilungSession.setTextNrMailKontaktdaten("1289");
            tMitteilungSession.setTextNrMailKontaktdatenTelefon(
                    KonstPortalTexte.MITTEILUNG_WORTMELDUNGEN_MAIL_KONTAKTDATENTELEFON);
            tMitteilungSession.setTextNrMailAktien("1290");
            tMitteilungSession.setTextNrMailKurztext("1291");
            tMitteilungSession.setTextNrMailLangtext("1292");
            tMitteilungSession.setTextNrMailZeitpunktErteilt("1293");
            tMitteilungSession.setTextNrMailZurueckgezogen("1294");
            tMitteilungSession.setTextNrMailZeitpuntZurueckgezogen("1295");
            tMitteilungSession.setTextNrMailHinweisBestaetigt("1922");
            break;
        case KonstPortalFunktionen.widersprueche:
            tMitteilungSession.setTextNrStarttextAktiv("809");
            tMitteilungSession.setTextNrStarttextInaktiv("810");

            tMitteilungSession.setTextNrAusgangsViewStarttext("811");
            tMitteilungSession.setTextNrAusgangsViewButtonStart("812");
            tMitteilungSession.setTextNrAusgangsViewEndetext("813");

            tMitteilungSession.setTextNrPersonBestaetigenStarttext("817");
            tMitteilungSession.setTextNrPersonBestaetigenTextVorName("818");
            tMitteilungSession.setTextNrPersonBestaetigenTextNachName("819");
            tMitteilungSession.setTextNrPersonBestaetigenJaButton("1304");
            tMitteilungSession.setTextNrPersonBestaetigenNeinButton("1305");

            tMitteilungSession.setTextNrPersonNichtBestaetigt("828");

            tMitteilungSession.setTextNrPersonengemeinschaftNichtZulaessig("1306");
            tMitteilungSession.setTextNrJuristischePersonNichtZulaessig("1307");

            tMitteilungSession.setTextNrMitteilungStarttext("820");
            tMitteilungSession.setTextNrMitteilungNameAbfragen("1308");
            tMitteilungSession.setTextNrMitteilungKontaktAbfragen("1309");
            tMitteilungSession.setTextNrMitteilungKontaktTelefonAbfragen("2069");
            tMitteilungSession.setTextNrMitteilungHinweisGelesen("1789");
            tMitteilungSession.setTextNrVorTOPAuswahl("1894");
            tMitteilungSession.setTextNrNachTOPAuswahl("1895");
            tMitteilungSession.setTextNrVorInhaltsHinweise("2076");
            tMitteilungSession.setTextNrMitteilungVorKurzText("1310");
            tMitteilungSession.setTextNrMitteilungVorLangText("824");
            tMitteilungSession.setTextNrMitteilungLangTextVorZaehler("833");
            tMitteilungSession.setTextNrMitteilungLangTextNachZaehler("825");

            tMitteilungSession.setTextNrStarttextHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrNochKeineDateiAusgewaehltHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrButtonDateiAuswaehlenHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrVorDateiNameHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrNachDateiNameHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrEndetextHochladenBereich("790"); //nur als Dummy

            tMitteilungSession.setTextNrMitteilungButtonAbschicken("826");
            tMitteilungSession.setTextNrMitteilungEndetext("827");

            tMitteilungSession.setTextNrKeineWeiterenMitteilungen("829");

            tMitteilungSession.setTextNrEndetextAktiv("830");

            tMitteilungSession.setTextNrListeStarttext("");
            tMitteilungSession.setTextNrListeButtonAktualisieren("");
            tMitteilungSession.setTextNrListeEndetext("");

            tMitteilungSession.setTextNrListeMitteilungenStarttext("831");
            tMitteilungSession.setTextNrListeMitteilungenButtonZurueckziehen("1311");
            tMitteilungSession.setTextNrListeMitteilungenEndetext("832");

            tMitteilungSession.setTextNrMailBetreff("1312");//Default-Wert
            tMitteilungSession.setTextNrMailBetreffErteilt("1312");
            tMitteilungSession.setTextNrMailBetreffZurueckgezogen("1313");
            tMitteilungSession.setTextNrMailLfdIdent("1314");
            tMitteilungSession.setTextNrMailKennung("1315");
            tMitteilungSession.setTextNrMailSteller("1316");
            tMitteilungSession.setTextNrMailNameAbgefragt("1317");
            tMitteilungSession.setTextNrMailKontaktdaten("1318");
            tMitteilungSession.setTextNrMailKontaktdatenTelefon(
                    KonstPortalTexte.MITTEILUNG_WIDERSPRUECHE_MAIL_KONTAKTDATENTELEFON);
            tMitteilungSession.setTextNrMailAktien("1319");
            tMitteilungSession.setTextNrMailKurztext("1320");
            tMitteilungSession.setTextNrMailLangtext("1321");
            tMitteilungSession.setTextNrMailZeitpunktErteilt("1322");
            tMitteilungSession.setTextNrMailZurueckgezogen("1323");
            tMitteilungSession.setTextNrMailZeitpuntZurueckgezogen("1324");
            tMitteilungSession.setTextNrMailHinweisBestaetigt("1923");
            break;
        case KonstPortalFunktionen.antraege:
            tMitteilungSession.setTextNrStarttextAktiv("1325");
            tMitteilungSession.setTextNrStarttextInaktiv("1326");

            tMitteilungSession.setTextNrAusgangsViewStarttext("1327");
            tMitteilungSession.setTextNrAusgangsViewButtonStart("1328");
            tMitteilungSession.setTextNrAusgangsViewEndetext("1329");

            tMitteilungSession.setTextNrPersonBestaetigenStarttext("1330");
            tMitteilungSession.setTextNrPersonBestaetigenTextVorName("1331");
            tMitteilungSession.setTextNrPersonBestaetigenTextNachName("1332");
            tMitteilungSession.setTextNrPersonBestaetigenJaButton("1333");
            tMitteilungSession.setTextNrPersonBestaetigenNeinButton("1334");

            tMitteilungSession.setTextNrPersonNichtBestaetigt("1335");

            tMitteilungSession.setTextNrPersonengemeinschaftNichtZulaessig("1336");
            tMitteilungSession.setTextNrJuristischePersonNichtZulaessig("1337");

            tMitteilungSession.setTextNrMitteilungStarttext("1338");
            tMitteilungSession.setTextNrMitteilungNameAbfragen("1339");
            tMitteilungSession.setTextNrMitteilungKontaktAbfragen("1340");
            tMitteilungSession.setTextNrMitteilungKontaktTelefonAbfragen("2070");
            tMitteilungSession.setTextNrMitteilungHinweisGelesen("1790");
            tMitteilungSession.setTextNrVorTOPAuswahl("1896");
            tMitteilungSession.setTextNrNachTOPAuswahl("1897");
            tMitteilungSession.setTextNrVorInhaltsHinweise("2077");
            tMitteilungSession.setTextNrMitteilungVorKurzText("1341");
            tMitteilungSession.setTextNrMitteilungVorLangText("1342");
            tMitteilungSession.setTextNrMitteilungLangTextVorZaehler("1343");
            tMitteilungSession.setTextNrMitteilungLangTextNachZaehler("1344");

            tMitteilungSession.setTextNrStarttextHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrNochKeineDateiAusgewaehltHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrButtonDateiAuswaehlenHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrVorDateiNameHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrNachDateiNameHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrEndetextHochladenBereich("790"); //nur als Dummy

            tMitteilungSession.setTextNrMitteilungButtonAbschicken("1345");
            tMitteilungSession.setTextNrMitteilungEndetext("1346");

            tMitteilungSession.setTextNrKeineWeiterenMitteilungen("1347");

            tMitteilungSession.setTextNrEndetextAktiv("1348");

            tMitteilungSession.setTextNrListeStarttext("");
            tMitteilungSession.setTextNrListeButtonAktualisieren("");
            tMitteilungSession.setTextNrListeEndetext("");

            tMitteilungSession.setTextNrListeMitteilungenStarttext("1349");
            tMitteilungSession.setTextNrListeMitteilungenButtonZurueckziehen("1350");
            tMitteilungSession.setTextNrListeMitteilungenEndetext("1351");

            tMitteilungSession.setTextNrMailBetreff("1352");//Default-Wert
            tMitteilungSession.setTextNrMailBetreffErteilt("1352");
            tMitteilungSession.setTextNrMailBetreffZurueckgezogen("1353");
            tMitteilungSession.setTextNrMailLfdIdent("1354");
            tMitteilungSession.setTextNrMailKennung("1355");
            tMitteilungSession.setTextNrMailSteller("1356");
            tMitteilungSession.setTextNrMailNameAbgefragt("1357");
            tMitteilungSession.setTextNrMailKontaktdaten("1358");
            tMitteilungSession
                    .setTextNrMailKontaktdatenTelefon(KonstPortalTexte.MITTEILUNG_ANTRAEGE_MAIL_KONTAKTDATENTELEFON);
            tMitteilungSession.setTextNrMailAktien("1359");
            tMitteilungSession.setTextNrMailKurztext("1360");
            tMitteilungSession.setTextNrMailLangtext("1361");
            tMitteilungSession.setTextNrMailZeitpunktErteilt("1362");
            tMitteilungSession.setTextNrMailZurueckgezogen("1363");
            tMitteilungSession.setTextNrMailZeitpuntZurueckgezogen("1364");
            tMitteilungSession.setTextNrMailHinweisBestaetigt("1924");
            break;
        case KonstPortalFunktionen.sonstigeMitteilungen:
            tMitteilungSession.setTextNrStarttextAktiv("1365");
            tMitteilungSession.setTextNrStarttextInaktiv("1366");

            tMitteilungSession.setTextNrAusgangsViewStarttext("1367");
            tMitteilungSession.setTextNrAusgangsViewButtonStart("1368");
            tMitteilungSession.setTextNrAusgangsViewEndetext("1369");

            tMitteilungSession.setTextNrPersonBestaetigenStarttext("1370");
            tMitteilungSession.setTextNrPersonBestaetigenTextVorName("1371");
            tMitteilungSession.setTextNrPersonBestaetigenTextNachName("1372");
            tMitteilungSession.setTextNrPersonBestaetigenJaButton("1373");
            tMitteilungSession.setTextNrPersonBestaetigenNeinButton("1374");

            tMitteilungSession.setTextNrPersonNichtBestaetigt("1375");

            tMitteilungSession.setTextNrPersonengemeinschaftNichtZulaessig("1376");
            tMitteilungSession.setTextNrJuristischePersonNichtZulaessig("1377");

            tMitteilungSession.setTextNrMitteilungStarttext("1378");
            tMitteilungSession.setTextNrMitteilungNameAbfragen("1379");
            tMitteilungSession.setTextNrMitteilungKontaktAbfragen("1380");
            tMitteilungSession.setTextNrMitteilungKontaktTelefonAbfragen("2071");
            tMitteilungSession.setTextNrMitteilungHinweisGelesen("1791");
            tMitteilungSession.setTextNrVorTOPAuswahl("1898");
            tMitteilungSession.setTextNrNachTOPAuswahl("1899");
            tMitteilungSession.setTextNrVorInhaltsHinweise("2078");
            tMitteilungSession.setTextNrMitteilungVorKurzText("1381");
            tMitteilungSession.setTextNrMitteilungVorLangText("1382");
            tMitteilungSession.setTextNrMitteilungLangTextVorZaehler("1383");
            tMitteilungSession.setTextNrMitteilungLangTextNachZaehler("1384");

            tMitteilungSession.setTextNrStarttextHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrNochKeineDateiAusgewaehltHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrButtonDateiAuswaehlenHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrVorDateiNameHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrNachDateiNameHochladenBereich("790"); //nur als Dummy
            tMitteilungSession.setTextNrEndetextHochladenBereich("790"); //nur als Dummy

            tMitteilungSession.setTextNrMitteilungButtonAbschicken("1385");
            tMitteilungSession.setTextNrMitteilungEndetext("1386");

            tMitteilungSession.setTextNrKeineWeiterenMitteilungen("1387");

            tMitteilungSession.setTextNrEndetextAktiv("1388");

            tMitteilungSession.setTextNrListeStarttext("");
            tMitteilungSession.setTextNrListeButtonAktualisieren("");
            tMitteilungSession.setTextNrListeEndetext("");

            tMitteilungSession.setTextNrListeMitteilungenStarttext("1389");
            tMitteilungSession.setTextNrListeMitteilungenButtonZurueckziehen("1390");
            tMitteilungSession.setTextNrListeMitteilungenEndetext("1391");

            tMitteilungSession.setTextNrMailBetreff("1392");//Default-Wert
            tMitteilungSession.setTextNrMailBetreffErteilt("1392");
            tMitteilungSession.setTextNrMailBetreffZurueckgezogen("1393");
            tMitteilungSession.setTextNrMailLfdIdent("1394");
            tMitteilungSession.setTextNrMailKennung("1395");
            tMitteilungSession.setTextNrMailSteller("1396");
            tMitteilungSession.setTextNrMailNameAbgefragt("1397");
            tMitteilungSession.setTextNrMailKontaktdaten("1398");
            tMitteilungSession.setTextNrMailKontaktdatenTelefon(
                    KonstPortalTexte.MITTEILUNG_SONSTIGEMITTEILUNGEN_MAIL_KONTAKTDATENTELEFON);
            tMitteilungSession.setTextNrMailAktien("1399");
            tMitteilungSession.setTextNrMailKurztext("1400");
            tMitteilungSession.setTextNrMailLangtext("1401");
            tMitteilungSession.setTextNrMailZeitpunktErteilt("1402");
            tMitteilungSession.setTextNrMailZurueckgezogen("1403");
            tMitteilungSession.setTextNrMailZeitpuntZurueckgezogen("1404");
            tMitteilungSession.setTextNrMailHinweisBestaetigt("1925");
            aktiv = tAuswahlSession.isSonstigeMitteilungenAktiv();
            break;
        case KonstPortalFunktionen.botschaftenEinreichen:
            CaBug.druckeLog("Switch BotschaftenEinreichen", logDrucken, 10);
            tMitteilungSession.setTextNrStarttextAktiv("1792");
            tMitteilungSession.setTextNrStarttextInaktiv("1793");

            tMitteilungSession.setTextNrAusgangsViewStarttext("1794");
            tMitteilungSession.setTextNrAusgangsViewButtonStart("1795");
            tMitteilungSession.setTextNrAusgangsViewEndetext("1796");

            tMitteilungSession.setTextNrPersonBestaetigenStarttext("1797");
            tMitteilungSession.setTextNrPersonBestaetigenTextVorName("1798");
            tMitteilungSession.setTextNrPersonBestaetigenTextNachName("1799");
            tMitteilungSession.setTextNrPersonBestaetigenJaButton("1800");
            tMitteilungSession.setTextNrPersonBestaetigenNeinButton("1801");

            tMitteilungSession.setTextNrPersonNichtBestaetigt("1802");

            tMitteilungSession.setTextNrPersonengemeinschaftNichtZulaessig("1803");
            tMitteilungSession.setTextNrJuristischePersonNichtZulaessig("1804");

            tMitteilungSession.setTextNrMitteilungStarttext("1805");
            tMitteilungSession.setTextNrMitteilungNameAbfragen("1806");
            tMitteilungSession.setTextNrMitteilungKontaktAbfragen("1807");
            tMitteilungSession.setTextNrMitteilungKontaktTelefonAbfragen("2072");
            tMitteilungSession.setTextNrMitteilungHinweisGelesen("1808");
            tMitteilungSession.setTextNrVorTOPAuswahl("1900");
            tMitteilungSession.setTextNrNachTOPAuswahl("1901");
            tMitteilungSession.setTextNrVorInhaltsHinweise("2079");
            tMitteilungSession.setTextNrMitteilungVorKurzText("1809");
            tMitteilungSession.setTextNrMitteilungVorLangText("1810");
            tMitteilungSession.setTextNrMitteilungLangTextVorZaehler("1811");
            tMitteilungSession.setTextNrMitteilungLangTextNachZaehler("1812");

            if (eclParamM.getParam().paramPortal.botschaftenLangtextAbfragen == 1) {

                tMitteilungSession.setTextNrStarttextHochladenBereich("1813");
            } else {
                tMitteilungSession.setTextNrStarttextHochladenBereich("1814");
            }
            tMitteilungSession.setTextNrNochKeineDateiAusgewaehltHochladenBereich("1927");
            tMitteilungSession.setTextNrButtonDateiAuswaehlenHochladenBereich("1916");
            tMitteilungSession.setTextNrVorDateiNameHochladenBereich("1928");
            tMitteilungSession.setTextNrNachDateiNameHochladenBereich("1929");
            tMitteilungSession.setTextNrEndetextHochladenBereich("1918");

            tMitteilungSession.setTextNrMitteilungButtonAbschicken("1815");
            tMitteilungSession.setTextNrMitteilungEndetext("1816");

            tMitteilungSession.setTextNrKeineWeiterenMitteilungen("1817");

            tMitteilungSession.setTextNrEndetextAktiv("1818");

            tMitteilungSession.setTextNrListeStarttext("");
            tMitteilungSession.setTextNrListeButtonAktualisieren("");
            tMitteilungSession.setTextNrListeEndetext("");

            tMitteilungSession.setTextNrListeMitteilungenStarttext("1819");
            tMitteilungSession.setTextNrListeMitteilungenButtonZurueckziehen("1820");
            tMitteilungSession.setTextNrListeMitteilungenEndetext("1821");

            tMitteilungSession.setTextNrMailBetreff("1822"); //Default-Wert
            tMitteilungSession.setTextNrMailBetreffErteilt("1822");
            tMitteilungSession.setTextNrMailBetreffZurueckgezogen("1823");
            tMitteilungSession.setTextNrMailLfdIdent("1824");
            tMitteilungSession.setTextNrMailKennung("1825");
            tMitteilungSession.setTextNrMailSteller("1826");
            tMitteilungSession.setTextNrMailNameAbgefragt("1827");
            tMitteilungSession.setTextNrMailKontaktdaten("1828");
            tMitteilungSession.setTextNrMailKontaktdatenTelefon(
                    KonstPortalTexte.MITTEILUNG_BOTSCHAFTENEINREICEHN_MAIL_KONTAKTDATENTELEFON);
            tMitteilungSession.setTextNrMailAktien("1829");
            tMitteilungSession.setTextNrMailKurztext("1830");
            tMitteilungSession.setTextNrMailLangtext("1831");
            tMitteilungSession.setTextNrMailZeitpunktErteilt("1832");
            tMitteilungSession.setTextNrMailZurueckgezogen("1833");
            tMitteilungSession.setTextNrMailZeitpuntZurueckgezogen("1834");
            tMitteilungSession.setTextNrMailHinweisBestaetigt("1926");
            break;
        case KonstPortalFunktionen.rueckfragen:
            CaBug.druckeLog("Switch Rückfragen", logDrucken, 10);
            tMitteilungSession.setTextNrStarttextAktiv("1842");
            tMitteilungSession.setTextNrStarttextInaktiv("1843");

            tMitteilungSession.setTextNrAusgangsViewStarttext("1844");
            tMitteilungSession.setTextNrAusgangsViewButtonStart("1845");
            tMitteilungSession.setTextNrAusgangsViewEndetext("1846");

            tMitteilungSession.setTextNrPersonBestaetigenStarttext("1847");
            tMitteilungSession.setTextNrPersonBestaetigenTextVorName("1848");
            tMitteilungSession.setTextNrPersonBestaetigenTextNachName("1849");
            tMitteilungSession.setTextNrPersonBestaetigenJaButton("1850");
            tMitteilungSession.setTextNrPersonBestaetigenNeinButton("1851");

            tMitteilungSession.setTextNrPersonNichtBestaetigt("1852");

            tMitteilungSession.setTextNrPersonengemeinschaftNichtZulaessig("1853");
            tMitteilungSession.setTextNrJuristischePersonNichtZulaessig("1854");

            tMitteilungSession.setTextNrMitteilungStarttext("1855");
            tMitteilungSession.setTextNrMitteilungNameAbfragen("1856");
            tMitteilungSession.setTextNrMitteilungKontaktAbfragen("1857");
            tMitteilungSession.setTextNrMitteilungKontaktTelefonAbfragen("2073");
            tMitteilungSession.setTextNrMitteilungHinweisGelesen("1858");
            tMitteilungSession.setTextNrVorTOPAuswahl("1902");
            tMitteilungSession.setTextNrNachTOPAuswahl("1903");
            tMitteilungSession.setTextNrVorInhaltsHinweise("2080");
            tMitteilungSession.setTextNrMitteilungVorKurzText("1859");
            tMitteilungSession.setTextNrMitteilungVorLangText("1860");
            tMitteilungSession.setTextNrMitteilungLangTextVorZaehler("1861");
            tMitteilungSession.setTextNrMitteilungLangTextNachZaehler("1862");
            tMitteilungSession.setTextNrStarttextHochladenBereich("1863");
            tMitteilungSession.setTextNrMitteilungButtonAbschicken("1865");
            tMitteilungSession.setTextNrMitteilungEndetext("1866");

            tMitteilungSession.setTextNrKeineWeiterenMitteilungen("1867");

            tMitteilungSession.setTextNrEndetextAktiv("1868");

            tMitteilungSession.setTextNrListeStarttext("");
            tMitteilungSession.setTextNrListeButtonAktualisieren("");
            tMitteilungSession.setTextNrListeEndetext("");

            tMitteilungSession.setTextNrListeMitteilungenStarttext("1869");
            tMitteilungSession.setTextNrListeMitteilungenButtonZurueckziehen("1870");
            tMitteilungSession.setTextNrListeMitteilungenEndetext("1871");

            tMitteilungSession.setTextNrMailBetreff("1872"); //Default-Wert
            tMitteilungSession.setTextNrMailBetreffErteilt("1872");
            tMitteilungSession.setTextNrMailBetreffZurueckgezogen("1873");
            tMitteilungSession.setTextNrMailLfdIdent("1874");
            tMitteilungSession.setTextNrMailKennung("1875");
            tMitteilungSession.setTextNrMailSteller("1876");
            tMitteilungSession.setTextNrMailNameAbgefragt("1877");
            tMitteilungSession.setTextNrMailKontaktdaten("1878");
            tMitteilungSession
                    .setTextNrMailKontaktdatenTelefon(KonstPortalTexte.MITTEILUNG_RUECKFRAGEN_MAIL_KONTAKTDATENTELEFON);
            tMitteilungSession.setTextNrMailAktien("1879");
            tMitteilungSession.setTextNrMailKurztext("1880");
            tMitteilungSession.setTextNrMailLangtext("1881");
            tMitteilungSession.setTextNrMailZeitpunktErteilt("1882");
            tMitteilungSession.setTextNrMailZurueckgezogen("1883");
            tMitteilungSession.setTextNrMailZeitpuntZurueckgezogen("1884");
            tMitteilungSession.setTextNrMailHinweisBestaetigt("1921");
            break;
        default:
            CaBug.drucke("001 - falsche Portalfunktion pFunktion=" + pFunktion);
        }

        if (blMitteilungen.rcTopListeAnbieten > 0) {
            /*TOP_Liste wird verwendet, deshalb Initialisieren*/
            int[] gattungen = new int[6];
            for (int i = 1; i <= 5; i++) {
                gattungen[i] = 1;
            }
            blMAbstimmung.leseWeisungsliste(gattungen, blMitteilungen.rcSkIstZuTopListe,
                    KonstWeisungserfassungSicht.portalWeisungserfassung);
        }

        tMitteilungSession.initNaechsteFrage();
        tMitteilungSession.setFunktionIstAktiv(aktiv);

        inhaltsHinweiseAnbietenBelegen();

        return 1;
    }

    /*
    private void pruefenObFertig() {
        String ergebnis = "";
        Future<String> futureRC = tMitteilungSession.getFutureRC();
        try {
            ergebnis = futureRC.get(15, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
        }
        if (!ergebnis.isEmpty()) {
            CaBug.druckeLog("ergebnis=" + ergebnis, logDrucken, 10);
            tMitteilungSession.setStehtZurVerfuegung(true);
            tMitteilungSession.setAnzeigeText(ergebnis);
        }
    }*/

    private void holeMitteilungenZuLoginKennung(BlMitteilungen blMitteilungen) {

        int gattungen[] = { 0, 1, 1, 1, 1, 1 };
        BlAbstimmungsListe blAbstimmungsliste = new BlAbstimmungsListe();
        blAbstimmungsliste.leseWeisungsliste(eclAbstimmungSetM.getAbstimmungSet(), gattungen,
                tMitteilungSession.getSkIstZuTopListe(), KonstWeisungserfassungSicht.portalWeisungserfassung, false);

        blMitteilungen.holeMitteilungenZuLoginKennung(eclLoginDatenM.getEclLoginDaten().ident,
                eclAbstimmungSetM.getAbstimmungSet(), tMitteilungSession.getSkIstZuTopListe());
        tMitteilungSession.setMitteilungenGestelltListe(blMitteilungen.rcMitteilungenZuLoginKennung);
        tMitteilungSession.setAnzahlMitteilungenGestellt(blMitteilungen.rcMitteilungenZuLoginKennung.size());
        tMitteilungSession.setAnzahlMitteilungenGestelltOhneZurueckgezogen(
                blMitteilungen.rcAnzahlMitteilungenGestelltOhneZureuckgezogenZuLoginKennung);
        tMitteilungSession.setAnzahlMitteilungenOffenPfandbrief(
                blMitteilungen.rcAnzahlMitteilungenOffenPfandbrief);
    }

    public void doMitteilungStart() {
        int lView = 0;
        try {

            lView = ermittleViewNr();
            if (!tSessionVerwaltung.pruefeStart(lView)) {
                return;
            }
            protokoll("doMitteilungStart");
            if (tMitteilungSession.getAnzahlJeAktionaer() <= tMitteilungSession
                    .getAnzahlMitteilungenGestelltOhneZurueckgezogen()) {
                tMitteilungSession
                        .setAnzuzeigenderBereich(KonstMitteilungViewBereich.keineWeiterenMitteilungenMoeglich);
                tSessionVerwaltung.setzeEnde(lView);
                return;
            }
//            if (ParamSpezial.pfandbriefbank(eclParamM.getParam().mandant)) {
//                if (tMitteilungSession
//                        .getAnzahlMitteilungenOffenPfandbrief() >=1 && tMitteilungSession.getArtDerMitteilung()==KonstPortalFunktionen.wortmeldungen ) {
            if (tMitteilungSession.getArtDerMitteilung() == KonstPortalFunktionen.wortmeldungen && 
                    eclParamM.getParam().paramPortal.wortmeldungNurEineOffeneZulaessig == 1 &&
                    tMitteilungSession.getAnzahlMitteilungenOffenPfandbrief() >=1) {
                tMitteilungSession.setAnzuzeigenderBereich(KonstMitteilungViewBereich.keineWeiterenMitteilungenMoeglich);
                tSessionVerwaltung.setzeEnde(lView);
                return;

            }
            if (tMitteilungSession.isPersonenDatenBereitsAbgefragt() == false
                    && tMitteilungSession.getStellerAbfragen() == 1) {
                /*Als nächstes Prozess die Bestätigung des Mitteilungsstellers durchlaufen*/
                tMitteilungSession.setAnzuzeigenderBereich(KonstMitteilungViewBereich.personBestaetigen);
                tSessionVerwaltung.setzeEnde(lView);
                return;
            }

            switch (tMitteilungSession.getArtDerMitteilung()) {
            case KonstPortalFunktionen.fragen:
                tMitteilungSession.setKontaktdatenEingabe(tMitteilungSession.getKontaktDatenEingabeFragen());
                tMitteilungSession
                        .setKontaktdatenTelefonEingabe(tMitteilungSession.getKontaktDatenTelefonEingabeFragen());
                tMitteilungSession.setNameEingabe(tMitteilungSession.getNameEingabeFragen());
                break;
            case KonstPortalFunktionen.wortmeldungen:
                tMitteilungSession.setKontaktdatenEingabe(tMitteilungSession.getKontaktDatenEingabeWortmeldungen());
                tMitteilungSession
                        .setKontaktdatenTelefonEingabe(tMitteilungSession.getKontaktDatenTelefonEingabeWortmeldungen());
                tMitteilungSession.setNameEingabe(tMitteilungSession.getNameEingabeWortmeldungen());
                CaBug.druckeLog("tMitteilungSession.getKontaktDatenEingabeWortmeldungen()="
                        + tMitteilungSession.getKontaktDatenEingabeWortmeldungen(), logDrucken, 10);
                break;
            case KonstPortalFunktionen.widersprueche:
                tMitteilungSession.setKontaktdatenEingabe(tMitteilungSession.getKontaktDatenEingabeWidersprueche());
                tMitteilungSession
                        .setKontaktdatenTelefonEingabe(tMitteilungSession.getKontaktDatenTelefonEingabeWidersprueche());
                tMitteilungSession.setNameEingabe(tMitteilungSession.getNameEingabeWidersprueche());
                break;
            case KonstPortalFunktionen.antraege:
                tMitteilungSession.setKontaktdatenEingabe(tMitteilungSession.getKontaktDatenEingabeAntraege());
                tMitteilungSession
                        .setKontaktdatenTelefonEingabe(tMitteilungSession.getKontaktDatenTelefonEingabeAntraege());
                tMitteilungSession.setNameEingabe(tMitteilungSession.getNameEingabeAntraege());
                break;
            case KonstPortalFunktionen.sonstigeMitteilungen:
                tMitteilungSession
                        .setKontaktdatenEingabe(tMitteilungSession.getKontaktDatenEingabeSonstigeMitteilungen());
                tMitteilungSession.setKontaktdatenTelefonEingabe(
                        tMitteilungSession.getKontaktDatenTelefonEingabeSonstigeMitteilungen());
                tMitteilungSession.setNameEingabe(tMitteilungSession.getNameEingabeSonstigeMitteilungen());
                break;
            case KonstPortalFunktionen.botschaftenEinreichen:
                tMitteilungSession
                        .setKontaktdatenEingabe(tMitteilungSession.getKontaktDatenEingabeBotschaftenEinreichen());
                tMitteilungSession.setKontaktdatenTelefonEingabe(
                        tMitteilungSession.getKontaktDatenTelefonEingabeBotschaftenEinreichen());
                tMitteilungSession.setNameEingabe(tMitteilungSession.getNameEingabeBotschaftenEinreichen());
                break;
            case KonstPortalFunktionen.rueckfragen:
                tMitteilungSession.setKontaktdatenEingabe(tMitteilungSession.getKontaktDatenEingabeRueckfragen());
                tMitteilungSession
                        .setKontaktdatenTelefonEingabe(tMitteilungSession.getKontaktDatenTelefonEingabeRueckfragen());
                tMitteilungSession.setNameEingabe(tMitteilungSession.getNameEingabeRueckfragen());
                break;
            }

            /**Falls Parameter aktiviert, und Kontaktdaten nicht bereits eingegeben, dann ggf. die hinterlegte E-Mail-Adresse
             * aus LoginKennung eintragen
             */
            CaBug.druckeLog("MitteilungSession.getKontaktdatenEMailVorschlagen()="
                    + tMitteilungSession.getKontaktdatenEMailVorschlagen(), logDrucken, 10);
            CaBug.druckeLog("tMitteilungSession.getKontaktdatenEingabe().isEmpty()=<"
                    + tMitteilungSession.getKontaktdatenEingabe().isEmpty() + ">", logDrucken, 10);
            CaBug.druckeLog("eclLoginDatenM.getEclLoginDaten().eMailFuerVersand="
                    + eclLoginDatenM.getEclLoginDaten().eMailFuerVersand, logDrucken, 10);
            if (tMitteilungSession.getKontaktdatenEMailVorschlagen() > 0
                    && tMitteilungSession.getKontaktdatenEingabe().isEmpty()) {
                if (!eclLoginDatenM.getEclLoginDaten().eMailFuerVersand.isEmpty()) {
                    tMitteilungSession.setKontaktdatenEingabe(eclLoginDatenM.getEclLoginDaten().eMailFuerVersand);
                }
            }
            CaBug.druckeLog(
                    "1 tMitteilungSession.getKontaktdatenEingabe=" + tMitteilungSession.getKontaktdatenEingabe(),
                    logDrucken, 10);

            if (tMitteilungSession.getNameEingabe().trim().isEmpty()) {
                /*Name vorbelegen*/
                String hName = eclLoginDatenM.getTitelVornameName();
                tMitteilungSession.setNameEingabe(hName);
            }

            /*Neue MitteilungFrage stellen*/
            tMitteilungSession.initNaechsteFrage();
            tMitteilungSession.setAnzuzeigenderBereich(KonstMitteilungViewBereich.mitteilungEingeben);
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

        tSessionVerwaltung.setzeEnde(lView);
        return;
    }

    public void doPersonBestaetigenJa() {
        int lView = 0;
        try {
            lView = ermittleViewNr();
            if (!tSessionVerwaltung.pruefeStart(lView)) {
                return;
            }
            protokoll("doMitteilungBestaetigenPersonJa");
            tMitteilungSession.setAnzuzeigenderBereich(KonstMitteilungViewBereich.mitteilungEingeben);
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

        tSessionVerwaltung.setzeEnde(lView);
        return;
    }

    public void doPersonBestaetigenNein() {
        int lView = 0;
        try {
            lView = ermittleViewNr();
            if (!tSessionVerwaltung.pruefeStart(lView)) {
                return;
            }
            protokoll("doMitteilungBestaetigenPersonNein");
            tMitteilungSession
                    .setAnzuzeigenderBereich(KonstMitteilungViewBereich.mitteilungNichtMoeglichDaPersonNichtBestaetigt);
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

        tSessionVerwaltung.setzeEnde(lView);
        return;

    }

    public void doMitteilungAbschickenButton() {
        try {
            abschickenUeberButtonOderUpload = 1;
            doMitteilungAbschicken();
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }
    }

    public void doMitteilungAbschickenUpload() {
        try {
            abschickenUeberButtonOderUpload = 2;
            doMitteilungAbschicken();
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }
    }

    private int abschickenUeberButtonOderUpload = 1;

    public void doMitteilungAbschicken() {
        CaBug.druckeLog("doMitteilungAbschicken - Start", logDrucken, 10);
        int lView = 0;
        lView = ermittleViewNr();
        if (!tSessionVerwaltung.pruefeStart(lView)) {
            return;
        }
        protokoll("doMitteilungAbschicken");

        CaBug.druckeLog("doMitteilungAbschicken - A", logDrucken, 10);

        if (!tMitteilungSession.getUploadFilenameOriginalHidden().trim().isEmpty()) {
            tMitteilungSession.setFileWurdeAusgewaehlt(true);
        }

        CaBug.druckeLog("doMitteilungAbschicken - B", logDrucken, 10);

        EclMitteilung lMitteilung = new EclMitteilung();

        EclLoginDaten lLoginDaten = eclLoginDatenM.getEclLoginDaten();
        lMitteilung.loginIdent = lLoginDaten.ident;

        /*TODO*/
        //        lMitteilung.anzahlAktionaereZumZeitpunktDerMitteilung=
        lMitteilung.anzahlAktienZumZeitpunktderMitteilung = eclBesitzGesamtM.getAktienInsgesamtAngemeldet();
        for (int i = 1; i <= 5; i++) {
            lMitteilung.gattungen[i] = eclBesitzGesamtM.getGattungen()[i - 1];
        }
        lMitteilung.identString = lLoginDaten.loginKennung;
        lMitteilung.nameVornameOrtKennung = CaString
                .trunc(eclLoginDatenM.getTitelVornameName() + ", " + eclLoginDatenM.getOrt(), 100);
        lMitteilung.nameVornameOrt = tMitteilungSession.getNameEingabe().trim();
        lMitteilung.kontaktDaten = tMitteilungSession.getKontaktdatenEingabe().trim();
        lMitteilung.kontaktDatenTelefon = tMitteilungSession.getKontaktdatenTelefonEingabe().trim();

        lMitteilung.mitteilungKurztext = tMitteilungSession.getKurzTextEingabe().trim();
        lMitteilung.mitteilungLangtext = tMitteilungSession.getLangTextEingabe().trim();

        lMitteilung.artDerMitteilung = KonstPortalFunktionen.portalFunktionZuView(lView);
        if (lMitteilung.artDerMitteilung == KonstPortalFunktionen.rueckfragen) {
            lMitteilung.artDerMitteilung = KonstPortalFunktionen.fragen;
        }
        protokoll(lMitteilung.identString + " " + lMitteilung.nameVornameOrt + " " + lMitteilung.kontaktDaten);

        if (tMitteilungSession.getHinweisGelesen() > 0) {
            if (tMitteilungSession.isHinweisBestaetigt()) {
                lMitteilung.hinweisWurdeBestaetigt = 1;
            } else {
                lMitteilung.hinweisWurdeBestaetigt = 0;
                if (tMitteilungSession.getHinweisGelesen() == 2) {
                    tSession.trageFehlerEin(CaFehler.afMitteilungHinweisBestaetigen);
                    trageFehlerBotschaftEin();
                    tSessionVerwaltung.setzeEnde(lView);
                    return;
                }
            }
        }

        if (lMitteilung.nameVornameOrt.isEmpty() && tMitteilungSession.getNameAbfragen() == 2) {
            tSession.trageFehlerEin(CaFehler.afWortmeldungWortmelderFehlt);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }
        if (lMitteilung.nameVornameOrt.length() > 100) {
            tSession.trageFehlerEin(CaFehler.afWortmeldungWortmelderZuLang);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }
        if (lMitteilung.kontaktDaten.isEmpty() && tMitteilungSession.getKontaktdatenAbfragen() == 2) {
            tSession.trageFehlerEin(CaFehler.afWortmeldungMailFehlt);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }
        if (lMitteilung.kontaktDatenTelefon.isEmpty() && tMitteilungSession.getKontaktdatenTelefonAbfragen() == 2) {
            tSession.trageFehlerEin(CaFehler.afWortmeldungTelefonNrFehlt);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }
        if (lMitteilung.kontaktDaten.length() > 100) {
            tSession.trageFehlerEin(CaFehler.afWortmeldungMailZuLang);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }
        if (lMitteilung.kontaktDatenTelefon.isEmpty() && tMitteilungSession.getKontaktdatenTelefonAbfragen() == 2) {
            tSession.trageFehlerEin(CaFehler.afWortmeldungTelefonNrFehlt);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }
        if (lMitteilung.kontaktDatenTelefon.length() > 100) {
            tSession.trageFehlerEin(CaFehler.afWortmeldungTelNrZuLang);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }

        String topListe = ""; //Für spätere Ausgabe im Mail
        if (tMitteilungSession.getTopListeAnbieten() > 0) {
            /*Topliste zusammensetzen und überprüfen, falls zwingend*/
            int gef = 0;
            List<EclAbstimmungM> abstimmungenListeM = eclAbstimmungenListeM.getAbstimmungenListeM();
            for (int i = 0; i < abstimmungenListeM.size(); i++) {
                EclAbstimmungM lAbstimmungM = abstimmungenListeM.get(i);
                if (lAbstimmungM.isMarkiert()) {
                    lMitteilung.mitteilungZuTop[lAbstimmungM.getIdentWeisungssatz()] = 1;
                    if (gef != 0) {
                        topListe += "; ";
                    }
                    topListe += lAbstimmungM.getNummer();
                    if (!lAbstimmungM.getNummerindex().isEmpty()) {
                        topListe += " " + lAbstimmungM.getNummerindex();
                    }
                    gef = 1;
                }
            }
            abstimmungenListeM = eclAbstimmungenListeM.getGegenantraegeListeM();
            for (int i = 0; i < abstimmungenListeM.size(); i++) {
                EclAbstimmungM lAbstimmungM = abstimmungenListeM.get(i);
                if (lAbstimmungM.isMarkiert()) {
                    lMitteilung.mitteilungZuTop[lAbstimmungM.getIdentWeisungssatz()] = 1;
                    if (gef != 0) {
                        topListe += "; ";
                    }
                    topListe += lAbstimmungM.getNummer();
                    if (!lAbstimmungM.getNummerindex().isEmpty()) {
                        topListe += " " + lAbstimmungM.getNummerindex();
                    }
                    gef = 1;
                }
            }
            if (gef == 0 && tMitteilungSession.getTopListeAnbieten() == 2) {
                tSession.trageFehlerEin(CaFehler.afWortmeldungTopAuswahlFehlt);
                trageFehlerBotschaftEin();
                tSessionVerwaltung.setzeEnde(lView);
                return;
            }
            lMitteilung.zuTOPListe = topListe;
        }

        boolean inhaltsHinweisAusgewaehlt = false;

        if (tMitteilungSession.isInhaltsHinweiseVorhandenundAktiv()) {
            List<EhInhaltsHinweise> lInhaltsHinweiseListe = tMitteilungSession.getInhaltsHinweiseListe();
            for (EhInhaltsHinweise lInhaltsHinweis : lInhaltsHinweiseListe) {
                if (lInhaltsHinweis.selektiert) {
                    inhaltsHinweisAusgewaehlt = true;
                }
                lMitteilung.inhaltsHinweis[lInhaltsHinweis.lfdNummer] = lInhaltsHinweis.selektiert;
            }
        }

        if (lMitteilung.mitteilungKurztext.length() > 100) {
            tSession.trageFehlerEin(CaFehler.afWortmeldungTopZuLang/*afWortmeldungTelefonNrFehlt*/);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }
        if (lMitteilung.mitteilungKurztext.isEmpty() && tMitteilungSession.getKurztextAbfragen() == 2) {
            tSession.trageFehlerEin(CaFehler.afWortmeldungTopFehlt);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }
        if (lMitteilung.mitteilungLangtext.isEmpty() && tMitteilungSession.getLangtextAbfragen() == 2) {
            tSession.trageFehlerEin(CaFehler.afFrageTextFehlt/*afWortmeldungTelefonNrFehlt*/);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }
        if (lMitteilung.mitteilungLangtext.length() > tMitteilungSession.getLaenge()) {
            tSession.trageFehlerEin(CaFehler.afWortmeldungTextZuLang/*afWortmeldungTelefonNrFehlt*/);
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }

        boolean ladeDateiHochAusfuehren = false;

        if ((tMitteilungSession.getLangtextAbfragen() == 0
                || (tMitteilungSession.getLangtextAbfragen() == 1 && lMitteilung.mitteilungLangtext.isEmpty()))
                && tMitteilungSession.isDateiHochladenMoeglich()
                && (tMitteilungSession.liefereAuswahlLangtextOderDateiAnzeigen() == false
                        || (tMitteilungSession.liefereAuswahlLangtextOderDateiAnzeigen() == true
                                && tMitteilungSession.getAuswahlLangtextOderDatei() == 2))) {
            if (!tMitteilungSession.isFileWurdeAusgewaehlt()) {
                tSession.trageFehlerEin(CaFehler.afDateiUploadKeineDateiAusgewaehlt);
                trageFehlerBotschaftEin();
                tSessionVerwaltung.setzeEnde(lView);
                return;
            }
        }

        if (tMitteilungSession.liefereAuswahlLangtextOderDateiAnzeigen() == true
                && tMitteilungSession.getAuswahlLangtextOderDatei() == 1 && lMitteilung.mitteilungLangtext.isEmpty()) {
            switch (ermittleViewNr()) {
            case KonstPortalView.RUECKFRAGEN:
            case KonstPortalView.FRAGEN:
                tSession.trageFehlerEin(CaFehler.afFrageTextFehlt/*afWortmeldungTelefonNrFehlt*/);
                break;
            case KonstPortalView.WORTMELDUNGEN:
                tSession.trageFehlerEin(CaFehler.afWortmeldungTextFehlt/*afWortmeldungTelefonNrFehlt*/);
                break;
            case KonstPortalView.WIDERSPRUECHE:
                tSession.trageFehlerEin(CaFehler.afWiderspruchTextFehlt/*afWortmeldungTelefonNrFehlt*/);
                break;
            case KonstPortalView.ANTRAEGE:
                tSession.trageFehlerEin(CaFehler.afAntragTextFehlt/*afWortmeldungTelefonNrFehlt*/);
                break;
            case KonstPortalView.SONSTIGEMITTEILUNGEN:
                tSession.trageFehlerEin(CaFehler.afSonstigeMitteilungTextFehlt/*afWortmeldungTelefonNrFehlt*/);
                break;
            case KonstPortalView.BOTSCHAFTEN_EINREICHEN:
                tSession.trageFehlerEin(CaFehler.afBotschaftenEinreichenTextFehlt/*afWortmeldungTelefonNrFehlt*/);
                break;
            }
            trageFehlerBotschaftEin();
            tSessionVerwaltung.setzeEnde(lView);
            return;
        }

        if (tMitteilungSession.isDateiHochladenMoeglich() && tMitteilungSession.isFileWurdeAusgewaehlt()) {
            CaBug.druckeLog("doMitteilungAbschicken - C1", logDrucken, 10);
            if (fileUploadValidFile() == false) {
                trageFehlerBotschaftEin();
                return;
            }
            CaBug.druckeLog("doMitteilungAbschicken - C2", logDrucken, 10);

            ladeDateiHochAusfuehren = true;

        }
        try {

            eclDbM.openAll();
            eclDbM.openWeitere();

            if (!lMitteilung.mitteilungLangtext.isEmpty()) {
                lMitteilung.botschaftsart = (lMitteilung.botschaftsart | 1);
            }
            int lPortalFunktion = KonstPortalFunktionen.portalFunktionZuView(lView);
            boolean brc = tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(lPortalFunktion, false);
            if (brc == false) {
                eclDbM.closeAll();
                trageFehlerBotschaftEin();
                return;
            }

            /**Inhaltshinweise prüfen, wenn zwangsweise*/
            inhaltsHinweiseAnbietenBelegen();
            if (tMitteilungSession.getArtDerMitteilung() == KonstPortalFunktionen.wortmeldungen) {
                if (eclParamM.getParam().paramPortal.wortmeldungInhaltsHinweiseAktiv == 2) {
                    if (inhaltsHinweisAusgewaehlt == false) {
                        eclDbM.closeAll();
                        tSession.trageFehlerEin(CaFehler.afInhaltsHinweiseZwingend);
                        trageFehlerBotschaftEin();
                        tSessionVerwaltung.setzeEnde(lView);
                        return;
                    }

                }
            }

            /**Ggf. Datei-Botschaft hochladen vorbereiten*/
            if (ladeDateiHochAusfuehren) {
                CaBug.druckeLog("doMitteilungAbschicken - D1", logDrucken, 10);
                doFileUploadVorbereiten();
                CaBug.druckeLog("doMitteilungAbschicken - D2", logDrucken, 10);
                lMitteilung.dateiname = CaString.trunc(rcExternerFilename, 199);
                lMitteilung.internerDateiname = rcInternerFilename;
                lMitteilung.internerDateizusatz = rcInternerFilenameZusatz;
                if (rcZusatzIstVideo == true) {
                    lMitteilung.botschaftsart = (lMitteilung.botschaftsart | 2);
                    lMitteilung.interneVerarbeitungLaufend = 1;
                } else {
                    lMitteilung.botschaftsart = (lMitteilung.botschaftsart | 4);
                    lMitteilung.interneVerarbeitungLaufend = 2;
                }
            } else {
                lMitteilung.interneVerarbeitungLaufend = 2;
            }

            BlMitteilungen blMitteilungen = new BlMitteilungen(true, eclDbM.getDbBundle(),
                    tMitteilungSession.getArtDerMitteilung());
            blMitteilungen.neueMitteilungSpeichern(lMitteilung);

            init(tMitteilungSession.getArtDerMitteilung());

            tBestandDurcharbeiten.erzeugenARNummerMeldung();
            /*Aktuell gehaltene Bestände speichern*/
            for (int i = 0; i < tBestandDurcharbeiten.getRcAktienregisternummer().size(); i++) {
                EclMitteilungBestand lMitteilungBestand = new EclMitteilungBestand();
                lMitteilungBestand.artMitteilung = lMitteilung.artDerMitteilung;
                lMitteilungBestand.identMitteilung = lMitteilung.mitteilungIdent;
                lMitteilungBestand.kennungSteller = lMitteilung.identString;
                lMitteilungBestand.aktionaersnummer = tBestandDurcharbeiten.getRcAktienregisternummer().get(i);
                lMitteilungBestand.meldeIdent = tBestandDurcharbeiten.getRcMeldungsIdent().get(i);
                eclDbM.getDbBundle().dbMitteilungBestand.insert(lMitteilungBestand);
            }

            lMitteilung = blMitteilungen.rcNeueMitteilung;
            tMitteilungSession.setTextNrMailBetreff(tMitteilungSession.getTextNrMailBetreffErteilt());
            blMVirtuelleHV.schickeMailFuerMitteilung(lMitteilung);

            //        /**Ggf. Datei-Botschaft hochladen vorbereiten*/
            //        if (ladeDateiHochAusfuehren) {
            //            CaBug.druckeLog("doMitteilungAbschicken - E1", logDrucken, 10);
            //            doFileUpload();
            //            lMitteilung.dateiname = CaString.trunc(rcExternerFilename, 199);
            //            lMitteilung.internerDateiname = rcInternerFilename;
            //            lMitteilung.internerDateizusatz = rcInternerFilenameZusatz;
            //            lMitteilung.interneVerarbeitungLaufend=1;
            //            CaBug.druckeLog("doMitteilungAbschicken - E2", logDrucken, 10);
            //        }

            /*Nun noch Werte für Folge-Mitteilungen setzen (Kontaktdaten, Name bestätigt)*/
            if (tMitteilungSession.getStellerAbfragen() == 1) {
                tMitteilungSession.setPersonenDatenBereitsAbgefragt(true);
            }
            switch (tMitteilungSession.getArtDerMitteilung()) {
            case KonstPortalFunktionen.fragen:
                tMitteilungSession.setKontaktDatenEingabeFragen(lMitteilung.kontaktDaten);
                tMitteilungSession.setKontaktDatenTelefonEingabeFragen(lMitteilung.kontaktDatenTelefon);
                tMitteilungSession.setNameEingabeFragen(lMitteilung.nameVornameOrt);
                break;
            case KonstPortalFunktionen.wortmeldungen:
                CaBug.druckeLog(
                        "Zurückspeichern Kontaktdaten Wortmeldung lMitteilung.kontaktDaten=" + lMitteilung.kontaktDaten,
                        logDrucken, 10);
                tMitteilungSession.setKontaktDatenEingabeWortmeldungen(lMitteilung.kontaktDaten);
                tMitteilungSession.setKontaktDatenTelefonEingabeWortmeldungen(lMitteilung.kontaktDatenTelefon);
                tMitteilungSession.setNameEingabeWortmeldungen(lMitteilung.nameVornameOrt);
                break;
            case KonstPortalFunktionen.widersprueche:
                tMitteilungSession.setKontaktDatenEingabeWidersprueche(lMitteilung.kontaktDaten);
                tMitteilungSession.setKontaktDatenTelefonEingabeWidersprueche(lMitteilung.kontaktDatenTelefon);
                tMitteilungSession.setNameEingabeWidersprueche(lMitteilung.nameVornameOrt);
                break;
            case KonstPortalFunktionen.antraege:
                tMitteilungSession.setKontaktDatenEingabeAntraege(lMitteilung.kontaktDaten);
                tMitteilungSession.setKontaktDatenTelefonEingabeAntraege(lMitteilung.kontaktDatenTelefon);
                tMitteilungSession.setNameEingabeAntraege(lMitteilung.nameVornameOrt);
                break;
            case KonstPortalFunktionen.sonstigeMitteilungen:
                tMitteilungSession.setKontaktDatenEingabeSonstigeMitteilungen(lMitteilung.kontaktDaten);
                tMitteilungSession.setKontaktDatenTelefonEingabeSonstigeMitteilungen(lMitteilung.kontaktDatenTelefon);
                tMitteilungSession.setNameEingabeSonstigeMitteilungen(lMitteilung.nameVornameOrt);
                break;
            case KonstPortalFunktionen.botschaftenEinreichen:
                tMitteilungSession.setKontaktDatenEingabeBotschaftenEinreichen(lMitteilung.kontaktDaten);
                tMitteilungSession.setKontaktDatenTelefonEingabeBotschaftenEinreichen(lMitteilung.kontaktDatenTelefon);
                tMitteilungSession.setNameEingabeBotschaftenEinreichen(lMitteilung.nameVornameOrt);
                break;
            case KonstPortalFunktionen.rueckfragen:
                tMitteilungSession.setKontaktDatenEingabeRueckfragen(lMitteilung.kontaktDaten);
                tMitteilungSession.setKontaktDatenTelefonEingabeRueckfragen(lMitteilung.kontaktDatenTelefon);
                tMitteilungSession.setNameEingabeRueckfragen(lMitteilung.nameVornameOrt);
                break;
            }

            eclDbM.closeAll();//bis hierhin offenlassen, da blMVirtuelleHV eclDbM wg. Parameter benötigt

            generateJwtWsToken();
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

        tSessionVerwaltung.setzeEnde(ermittleViewNr());

    }

    public void doAktualisierenRednerListe() {
        int lView = 0;
        try {
            lView = ermittleViewNr();
            if (!tSessionVerwaltung.pruefeStart(lView)) {
                return;
            }
            eclDbM.openAll();
            eclDbM.openWeitere();

            BlMitteilungen blMitteilungen = new BlMitteilungen(true, eclDbM.getDbBundle(),
                    tMitteilungSession.getArtDerMitteilung());
            blMitteilungen.paramBelegen();

            holeMitteilungenZuLoginKennung(blMitteilungen);

            blMitteilungen.holeRednerListe();
            tMitteilungSession.setRednerListe(blMitteilungen.rcRednerliste);
            tMitteilungSession.setAnzahlRednerListe(blMitteilungen.rcRednerliste.size());

            eclDbM.closeAll();
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }
        tSessionVerwaltung.setzeEnde(lView);
    }

    public void doZurueckziehenMitteilung(EclMitteilung pMitteilung) {
        try {
            int lView = ermittleViewNr();
            if (!tSessionVerwaltung.pruefeStart(lView)) {
                return;
            }
            protokoll("doZurueckziehenMitteilung");
            eclDbM.openAll();
            eclDbM.openWeitere();
            /*int lPortalFunktion = */KonstPortalFunktionen.portalFunktionZuView(lView);
            /**AAAAA Zurückziehen ist jetzt definitiv immer möglich, egal ob aktiv oder deaktiv*/
            //        boolean brc = tPruefeStartNachOpen.pruefeNachOpenPortalFunktion(lPortalFunktion, false);
            //        if (brc == false) {
            //            eclDbM.closeAll();
            //            return;
            //        }

            int artDerMitteilung = tMitteilungSession.getArtDerMitteilung();
            eclDbM.getDbBundle().dbMitteilung.setzeFunktion(artDerMitteilung);
            eclDbM.getDbBundle().dbMitteilung.read(pMitteilung.mitteilungIdent);
            EclMitteilung mitteilungErneutGelesen = eclDbM.getDbBundle().dbMitteilung.ergebnisPosition(0);

            if ((KonstMitteilungStatus.zurueckziehenMoeglich(mitteilungErneutGelesen.status) == false
                    && artDerMitteilung != KonstPortalFunktionen.wortmeldungen)
                    || (eclParamM.getParam().paramPortal.wortmeldetischStatusArray[KonstMitteilungStatus
                            .liefereNurStatus(mitteilungErneutGelesen.status)].zurueckziehenMoeglichDurchTeilnehmer != 1
                            && artDerMitteilung == KonstPortalFunktionen.wortmeldungen)) {
                init(tMitteilungSession.getArtDerMitteilung());
                eclDbM.closeAll();
                tSession.trageFehlerEin(CaFehler.afZurueckziehenDerzeitNichtMoeglich);
                tSessionVerwaltung.setzeEnde(lView);
                return;
            }
            long versionErneutGelesen = mitteilungErneutGelesen.db_version;

            int rc = eclDbM.getDbBundle().dbMitteilung.update_statusZurueckgezogen(pMitteilung.mitteilungIdent,
                    CaDatumZeit.DatumZeitStringFuerDatenbank(), versionErneutGelesen);

            if (rc < 1) {
                eclDbM.closeAll();
                tSession.trageFehlerEin(CaFehler.afZurueckziehenDerzeitNichtMoeglich);
                tSessionVerwaltung.setzeEnde(lView);
                return;
            }

            tBestandDurcharbeiten.erzeugenARNummerMeldung();

            eclDbM.getDbBundle().dbMitteilung.read(pMitteilung.mitteilungIdent);
            EclMitteilung lMitteilung = eclDbM.getDbBundle().dbMitteilung.ergebnisPosition(0);
            tMitteilungSession.setTextNrMailBetreff(tMitteilungSession.getTextNrMailBetreffZurueckgezogen());
            blMVirtuelleHV.schickeMailFuerMitteilung(lMitteilung);

            init(tMitteilungSession.getArtDerMitteilung());

            eclDbM.closeAll();
            tSessionVerwaltung.setzeEnde(lView);
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

    }

    /**
     * Start Methoden füe FileUpload
     */

    private String rcInternerFilename = "";
    private String rcInternerFilenameZusatz = "";
    private String rcExternerFilename = "";
    /**true => Videodatei, false=Textdatei*/
    private boolean rcZusatzIstVideo = false;

    /**Vorbereitung - Belegung von rc*Filename**/
    private void doFileUploadVorbereiten() {
        try {
            rcZusatzIstVideo = false;
            CaBug.druckeLog("doFileUploadVorbereiten - Start", logDrucken, 10);
            rcExternerFilename = tMitteilungSession.getUploadFilenameOriginalHidden();
            CaBug.druckeLog("doFileUploadVorbereiten - 1", logDrucken, 10);

            rcInternerFilename = tMitteilungSession.getUploadFilenameInternHidden();
            int posPunkt = rcExternerFilename.lastIndexOf(".");
            if (posPunkt < 1) {
                CaBug.drucke("001");
            }
            rcInternerFilenameZusatz = rcExternerFilename.substring(posPunkt + 1);

            int anz = eclParamM.getParam().paramPortal.botschaftenVideoZusatz.length;
            for (int i = 0; i < anz; i++) {
                String vergleichsZusatz = eclParamM.getParam().paramPortal.botschaftenVideoZusatz[i];
                if (!vergleichsZusatz.isEmpty()) {
                    if (rcInternerFilenameZusatz.compareToIgnoreCase(vergleichsZusatz) == 0) {
                        rcZusatzIstVideo = true;
                    }
                }
            }
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }
    }

    /**Ergebnis: interner Filename, unter dem die Datei abgespeichert wurde, und Zusatz werden in
     * rcInternerFilename und rcInternerFilenameZusatz abgelegt*/
    //    private void doFileUpload() {
    //        CaBug.druckeLog("doFileUpload - Start", logDrucken, 10);
    //        String dateiPfad = eclParamM.getParamGeraet().lwPfadGrossdokumente + "\\" + eclParamM.getMandantPfad();
    //        CaBug.druckeLog("dateiPfad="+dateiPfad, logDrucken, 10);
    //        CaBug.druckeLog("doFileUpload - 1a", logDrucken, 10);
    //        File fDateiPfad = new File(dateiPfad);
    //        fDateiPfad.mkdirs();
    //        CaBug.druckeLog("doFileUpload - 1b", logDrucken, 10);
    //
    //        File file = new File(dateiPfad, rcInternerFilename + "_ORIG" + "." + rcInternerFilenameZusatz);
    //
    //        //        File file = new File("D:\\\\Meeting_Root\\meetingoutput\\" + eclParamM.getMandantPfad(), rcInternerFilename+"_00"+"."+rcInternerFilenameZusatz);
    //
    //        CaBug.druckeLog("doFileUpload - 2", logDrucken, 10);
    //        try (InputStream input = tMitteilungSession.getUploadFile().getInputStream()) {
    //            CaBug.druckeLog("doFileUpload - 3a", logDrucken, 10);
    //            Files.copy(input, file.toPath());
    //            CaBug.druckeLog("doFileUpload - 3b", logDrucken, 10);
    //           tMitteilungSession.setUploadFileName("");
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //        }
    //    }
    //    
    public void testUpload() {
        CaBug.druckeLog(tMitteilungSession.getUploadFilenameOriginalHidden(), logDrucken, 10);
        CaBug.druckeLog(tMitteilungSession.getUploadFilenameInternHidden(), logDrucken, 10);
        CaBug.druckeLog(String.valueOf(tMitteilungSession.getUploadFilesizeHidden()), logDrucken, 10);
        tMitteilungSession.setUploadFileErrorHidden("Error");
    }

    //    private String getFileNameFromPart(Part part) {
    //        CaBug.druckeLog("getFileNameFromPart - Start", logDrucken, 10);
    //       final String partHeader = part.getHeader("content-disposition");
    //        for (String content : partHeader.split(";")) {
    //            if (content.trim().startsWith("filename")) {
    //                String fileName = content.substring(content.indexOf('=') + 1).trim().replace("\"", "");
    //                CaBug.druckeLog("getFileNameFromPart - 1", logDrucken, 10);
    //                return fileName;
    //            }
    //        }
    //        return null;
    //    }

    //    private void resetFileUpload() {
    //        tMitteilungSession.setFileWurdeAusgewaehlt(false);
    //        tMitteilungSession.setUploadFileName("");
    //        tMitteilungSession.setUploadFileTyp(0);
    //    }

    private Boolean fileUploadValidFile() {
        CaBug.druckeLog("fileUploadValidFile - Start", logDrucken, 10);
        String uploadFilenameOriginalHidden = tMitteilungSession.getUploadFilenameOriginalHidden();
        CaBug.druckeLog("fileUploadValidFile - 1", logDrucken, 10);

        long filesize = tMitteilungSession.getUploadFilesizeHidden();
        long erlaubteUploadGroesseVideo = tMitteilungSession.getVideoLaenge() * 1024L * 1024L;
        long erlaubteUploadGroesseText = tMitteilungSession.getTextLaenge() * 1L;

        try {

            String extension = uploadFilenameOriginalHidden.split("\\.")[1].toLowerCase();

            if (filesize <= 0) {
                tSession.trageFehlerEin(CaFehler.afDateiUploadDateiLeer);
                tSessionVerwaltung.setzeEnde();
                return false;
            } else {
                if (tMitteilungSession.getErlaubteUploadFormateVideoJS().contains(extension)) {
                    if (filesize > erlaubteUploadGroesseVideo) {
                        tSession.trageFehlerEin(CaFehler.afDateiUpLoadVideodateiZuGross);
                        tSessionVerwaltung.setzeEnde();
                        return false;
                    } else {
                        tMitteilungSession.setUploadFileTyp(1);
                    }
                } else if (tMitteilungSession.getErlaubteUploadFormateTextJS().contains(extension)) {
                    if (filesize > erlaubteUploadGroesseText) {
                        tSession.trageFehlerEin(CaFehler.afDateiUpLoadTextdateiZuGross);
                        tSessionVerwaltung.setzeEnde();
                        return false;
                    } else {
                        tMitteilungSession.setUploadFileTyp(0);
                    }
                } else {
                    tSession.trageFehlerEin(CaFehler.afDateiUploadFalschesDateiformat);
                    tSessionVerwaltung.setzeEnde();
                    CaBug.druckeLog("Fehler - afDateiUploadFalschesDateiformat", logDrucken, 10);
                    return false;
                }
                CaBug.druckeLog("extension=" + extension, logDrucken, 10);
                CaBug.druckeLog("tMitteilungSession.getErlaubteUploadFormateVideoJS()="
                        + tMitteilungSession.getErlaubteUploadFormateVideoJS(), logDrucken, 10);
                CaBug.druckeLog("fileUploadValidFile - 2", logDrucken, 10);
            }

            return true;

        } catch (

        Exception ex) {
            CaBug.drucke("Datei upload: " + ex.toString());
            System.out.println(ex.getMessage());
            ex.printStackTrace();
            eclDbM.closeAll();
            return false;
        }
    }

    //    /**
    //     * Erforderlich, wenn gegen den Content-Type geprüft werden soll
    //     * 
    //     */
    //
    //    private String replaceFormateVideo(String erlaubteUploadFormateVideo) {
    //
    //        erlaubteUploadFormateVideo = erlaubteUploadFormateVideo.replace("MP4", "video/mp4");
    //        erlaubteUploadFormateVideo = erlaubteUploadFormateVideo.replace("AVI-Typ1", "video/x-msvideo");
    //        erlaubteUploadFormateVideo = erlaubteUploadFormateVideo.replace("AVI-Typ2", "video/avi");
    //        erlaubteUploadFormateVideo = erlaubteUploadFormateVideo.replace("MOV", "video/quicktime");
    //        erlaubteUploadFormateVideo = erlaubteUploadFormateVideo.replace("MPEG", "video/mpegmp4");
    //        erlaubteUploadFormateVideo = erlaubteUploadFormateVideo.replace("WMV", "video/x-ms-wmv");
    //
    //        return erlaubteUploadFormateVideo;
    //
    //    }
    //
    //    private String replaceFormateText(String erlaubteUploadFormateText) {
    //
    //        erlaubteUploadFormateText = erlaubteUploadFormateText.replace("TXT", "text/plain");
    //        erlaubteUploadFormateText = erlaubteUploadFormateText.replace("PDF", "application/pdf");
    //        erlaubteUploadFormateText = erlaubteUploadFormateText.replace("DOCX", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
    //        erlaubteUploadFormateText = erlaubteUploadFormateText.replace("PPTX", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
    //        erlaubteUploadFormateText = erlaubteUploadFormateText.replace("XLSX", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    //        erlaubteUploadFormateText = erlaubteUploadFormateText.replace("ODT", "application/vnd.oasis.opendocument.text");
    //
    //        return erlaubteUploadFormateText;
    //
    //    }

    /**
     * Ende Methoden füe FileUpload
     */

    public void doZurueck() {
        protokoll("doZurueckUnmittelbarNachAufruf");
        int lView = ermittleViewNr();
        if (!tSessionVerwaltung.pruefeStart(lView)) {
            return;
        }
        protokoll("doZurueck");
        try {
            eclDbM.openAll();
            tPortalFunktionen.belegeAnzahlFragenGestellt(eclDbM.getDbBundle());
            tPortalFunktionen.belegePortalFunktionenWillenserklaerungenStatusAktiv();
            eclDbM.closeAll();
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }
        tSessionVerwaltung.setzeEnde(tFunktionen.waehleAuswahlNachPraesenzfunktion());
        return;
    }

    private void protokoll(String pText) {
        CaBug.druckeInfo(">>>>TMitteilung<<<< " + pText + " Mandant=" + eclParamM.getClGlobalVar().mandant + " Login="
                + eclLoginDatenM.getAnmeldeKennungFuerAnzeige());
    }

    private int ermittleViewNr() {
        int lView = 0;
        switch (tMitteilungSession.getArtDerMitteilung()) {
        case KonstPortalFunktionen.fragen:
            lView = KonstPortalView.FRAGEN;
            break;
        case KonstPortalFunktionen.wortmeldungen:
            lView = KonstPortalView.WORTMELDUNGEN;
            break;
        case KonstPortalFunktionen.widersprueche:
            lView = KonstPortalView.WIDERSPRUECHE;
            break;
        case KonstPortalFunktionen.antraege:
            lView = KonstPortalView.ANTRAEGE;
            break;
        case KonstPortalFunktionen.sonstigeMitteilungen:
            lView = KonstPortalView.SONSTIGEMITTEILUNGEN;
            break;
        case KonstPortalFunktionen.botschaftenEinreichen:
            lView = KonstPortalView.BOTSCHAFTEN_EINREICHEN;
            break;
        case KonstPortalFunktionen.rueckfragen:
            lView = KonstPortalView.RUECKFRAGEN;
            break;
        }
        return lView;

    }

    private void trageFehlerBotschaftEin() {
        if (/*pruefeObDateiBotschaft()*/
        /*tMitteilungSession.isDateiHochladenMoeglich() && 
        (tMitteilungSession.getAuswahlLangtextOderDatei()==2 || tMitteilungSession.liefereAuswahlLangtextOderDateiAnzeigen()==false)
        */
        abschickenUeberButtonOderUpload == 2) {
            String lFehlertext = tSession.getFehlerMeldung();
            if (lFehlertext.trim().isEmpty()) {
                lFehlertext = "error";
            }
            tMitteilungSession.setUploadFileErrorHidden(lFehlertext);
            tSession.setFehlerMeldung("");

        }
    }

    //    /**liefert true, wenn Datei-Botschaften-Upload möglich*/
    //    private boolean pruefeObDateiBotschaft() {
    //        if (tMitteilungSession.getArtDerMitteilung() != KonstPortalFunktionen.botschaftenEinreichen) {
    //            return false;
    //        }
    //        if (tMitteilungSession.isDateiHochladenMoeglich()) {
    //            return true;
    //        }
    //        return false;
    //    }

    private boolean pruefeObAktiv() {
        int lFunktion = tMitteilungSession.getArtDerMitteilung();
        switch (lFunktion) {
        case KonstPortalFunktionen.fragen:
            return tAuswahlSession.isFragenAktiv();
        case KonstPortalFunktionen.wortmeldungen:
            return tAuswahlSession.isWortmeldungenAktiv();
        case KonstPortalFunktionen.widersprueche:
            return tAuswahlSession.isWiderspruecheAktiv();
        case KonstPortalFunktionen.antraege:
            return tAuswahlSession.isAntraegeAktiv();
        case KonstPortalFunktionen.sonstigeMitteilungen:
            return tAuswahlSession.isSonstigeMitteilungenAktiv();
        case KonstPortalFunktionen.botschaftenEinreichen:
            return tAuswahlSession.isBotschaftenEinreichenAktiv();
        case KonstPortalFunktionen.rueckfragen:
            return tAuswahlSession.isRueckfragenAktiv();
        }
        return false;
    }

    /**=============================================================Konferenzing====================*/
    /**Anzeige des Info-PDFs*/

    public void doWortmeldungInfoAnzeigenDE() {
        doWortmeldungInfoAnzeigen("DE");
    }

    public void doWortmeldungInfoAnzeigenEN() {
        doWortmeldungInfoAnzeigen("EN");
    }

    private void doWortmeldungInfoAnzeigen(String pSprache) {
        int lView = ermittleViewNr();

        if (!tSessionVerwaltung.pruefeStart(lView)) {
            return;
        }

        String pDateiname = "WortmeldeInfo";

        String pfadUndDateiname = eclParamM.getParamGeraet().lwPfadGrossdokumente + "\\" + eclParamM.getMandantPfad()
                + "\\" + pDateiname;
        pfadUndDateiname = pfadUndDateiname + pSprache;

        pfadUndDateiname = pfadUndDateiname + ".pdf";

        RpBrowserAnzeigen rpBrowserAnzeigen = new RpBrowserAnzeigen();
        rpBrowserAnzeigen.zeigen(pfadUndDateiname);

        tSessionVerwaltung.setzeEnde(lView);
        return;

    }

    /**Neueinlesen und Belegen der Konferenz-Status-Werte aus EclUserLogin.
     * Open/Close in einlesender Funktion*/
    private void konferenzStatusEinlesen() {
        BlTeilnehmerLoginNeu blTeilnehmerLogin = new BlTeilnehmerLoginNeu();
        blTeilnehmerLogin.initDB(eclDbM.getDbBundle());
        EclLoginDaten lLoginDaten = blTeilnehmerLogin.holeAktuelleLogiNDaten(eclLoginDatenM.getEclLoginDaten().ident);

        CaBug.druckeLog("lLoginDaten.konferenzTestDurchgefuehrt=" + lLoginDaten.konferenzTestDurchgefuehrt, logDrucken,
                10);
        tMitteilungSession.setKonferenzTestDurchgefuehrt(lLoginDaten.konferenzTestDurchgefuehrt);

        tMitteilungSession.setKonferenzTestRaum((lLoginDaten.konferenzTestAblauf & 255));
        if (tMitteilungSession.getKonferenzTestAblaufAlt() == lLoginDaten.konferenzTestAblauf) {
            tMitteilungSession.setKonferenzTestAblauf(0);
        } else {
            tMitteilungSession.setKonferenzTestAblauf(lLoginDaten.konferenzTestAblauf);
            tMitteilungSession.setKonferenzTestAblaufAlt(lLoginDaten.konferenzTestAblauf);
        }

        tMitteilungSession.setKonferenzSprechenRaum((lLoginDaten.konferenzSprechen & 255));
        if (tMitteilungSession.getKonferenzSprechenAlt() == lLoginDaten.konferenzSprechen) {
            tMitteilungSession.setKonferenzSprechen(0);
        } else {
            tMitteilungSession.setKonferenzSprechen(lLoginDaten.konferenzSprechen);
            tMitteilungSession.setKonferenzSprechenAlt(lLoginDaten.konferenzSprechen);
        }
    }

    public void doKonferenzStatusAktualisieren() {
        int lView = ermittleViewNr();
        if (!tSessionVerwaltung.pruefeStart(lView)) {
            return;
        }

        try {

            eclDbM.openAll();
            eclDbM.openWeitere();

            //       konferenzStatusEinlesen();#
            init(tMitteilungSession.getArtDerMitteilung());
            /*
            BlMitteilungen blMitteilungen = new BlMitteilungen(true, eclDbM.getDbBundle(), tMitteilungSession.getArtDerMitteilung());
            
            holeMitteilungenZuLoginKennung(blMitteilungen);
            
            blMitteilungen.holeRednerListe();
            tMitteilungSession.setRednerListe(blMitteilungen.rcRednerliste);
            tMitteilungSession.setAnzahlRednerListe(blMitteilungen.rcRednerliste.size());
            */

            eclDbM.closeAll();

        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

        tSessionVerwaltung.setzeEnde(ermittleViewNr());

    }

    public void stoppeKonferenz() {
        if (!tSessionVerwaltung.pruefeStartOhneMaskenpruefung()) {
            return;
        }
        // add your Convernce Calls here
        tSessionVerwaltung.setzeEnde(tSession.getRueckkehrZuMenue());
        return;
    }

    public void doKonferenzTestStarten() {
        if (!tSessionVerwaltung.pruefeStartOhneMaskenpruefung()) {
            return;
        }
        ladeStatusAusDatenbank();
        if (eclParamM.getParam().paramPortal.konfBackupAktiv == 1) {
            // add your Convernce Calls here
        } else {
            // add your Convernce Calls here
        }
        schreibeProtokoll(false, 102);
        tMitteilungSession.setKonferenzTestAblauf(0);
    }

    public void doKonferenzInRederaumGehen() {
        tMitteilungSession.setKonferenzSprechen(0);
        schreibeProtokoll(false, 105);
        // tStream.stoppeStreamWennKonferenz();
    }

    public void doKonferenzRedeBeginnen() {
        ladeStatusAusDatenbank();
        tMitteilungSession.setKonferenzSprechen(0);
        schreibeProtokoll(false, 107);
    }

    public void doKonferenzRederaumVerlassen() throws IOException {
        // add your Convernce Calls here
    }

    private void ladeStatusAusDatenbank() {
        try {
            eclDbM.openAll();
            eclDbM.openWeitere();

            konferenzStatusEinlesen();

            eclDbM.closeAll();
        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }
    }

    private void schreibeProtokoll(boolean pBereitsOffen, int pSonstigeAktion) {
        try {
            if (pBereitsOffen == false) {
                eclDbM.openAll();
            }

            EclWortmeldetischProtokoll lWortmeldetischProtokoll = new EclWortmeldetischProtokoll();
            lWortmeldetischProtokoll.identWortmeldung = eclLoginDatenM.getEclLoginDaten().ident;
            lWortmeldetischProtokoll.datumZeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
            lWortmeldetischProtokoll.sonstigeAktion = pSonstigeAktion;
            eclDbM.getDbBundle().dbWortmeldetischProtokoll.insert(lWortmeldetischProtokoll);

            if (pBereitsOffen == false) {
                eclDbM.closeAll();
            }

        } catch (Exception e) {
            CaBug.drucke("Exception");
            System.out.println(e.getMessage());
            e.printStackTrace();
            eclDbM.closeAll();
        }

    }

    private void generateJwtWsToken() {
        CaTokenUtil caTokenUtil = new CaTokenUtil();
        String jwt = caTokenUtil.generateToken(eclLoginDatenM.getEclLoginDaten().loginKennung,
                eclParamM.getParam().mandant);
        tSession.setJwtWsToken(jwt);
        tMitteilungSession.setConnectToWebSocket(true);
    }

}
