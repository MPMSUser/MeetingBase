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
package de.meetingapps.meetingportal.meetingportTFunktionen;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtAuswahl1M;
import de.meetingapps.meetingportal.meetComEclM.EclBesitzGesamtM;
import de.meetingapps.meetingportal.meetComEclM.EclLoginDatenM;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenM;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenMCompBotschaften;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenMCompExterneSeite;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenMCompLoginOben;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenMCompLoginUnten;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenMCompPPortal;
import de.meetingapps.meetingportal.meetComEclM.EclPortalUnterlagenMCompUnterlagen;
import de.meetingapps.meetingportal.meetComEntities.EclBesitzJeKennung;
import de.meetingapps.meetingportal.meetComEntities.EclPortalFunktion;
import de.meetingapps.meetingportal.meetComEntities.EclPortalPhase;
import de.meetingapps.meetingportal.meetComEntities.EclPortalUnterlagen;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalFunktionen;
import de.meetingapps.meetingportal.meetComKonst.KonstPortalUnterlagen;
import de.meetingapps.meetingportal.meetingportTController.TAuswahlSession;
import de.meetingapps.meetingportal.meetingportTController.TSession;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;

/*****************************************************************************
 * Hinweis Test Zeitsteuerung:
 * 1. Parameter:
 * zeit=TTMMJJJJHHMMSS
 * 
 */

/******************************************************************************
 * Ablauf zum Überprüfen von Verfügbarkeit von Funktionen und "Nebenläufigkeit"
 * bei Willenserkläerungen
 * 
 * Ablauf der Überprüfung:
 * 
 * Mit Eröffnung werden Parameter ggf. neu eingelesen:
 * eclDbM.openall()
 * 
 * Füllen der Session-Werte in Abhängigkeit der Login-Daten für Verfügbarkeit der
 * Funktionen:
 *      Für Portalfunktionen (stehen in anschließend in tAuswahlSession):
 *      belegeAngebotenAktivFuerAuswahl()
 *      
 *      Für Willenserklärungen (stehen anschließend in tSession):
 *      belegeAktivFuerWillenserklaerungen()
 *      
 *      Oder kombiniert beides:
 *      belegePortalFunktionenWillenserklaerungenStatusAktiv()
 *      
 *      Oder nur für eine einzige Portalfunktion:
 *      aktiv(portalFunktionNr) bzw. für Unterlagen aktivUnterlagen()
 */

/******************************************************************************
 * Phasen und Aktivierung von Willenserklärungen / Portalfunktionen
 * ================================================================
 * 
 * Willenserklärungen
 * ------------------
 * Sind alle Erklärungen, die ein Aktionär für einen konkreten Bestand abgibt, um den
 * Status des Bestandes zu ändern, also
 * Anmeldung, Zugang (bei Online-Teilnahme), Abgang (bei Online-Teilnahme), EK-Ausstellung,
 * Briefwahl, SRV, ...
 * 
 * 
 * Portalfunktionen
 * ----------------
 * Sind Funktionen, die ein einzelner Teilnehmer im Portal auslösen kann, ohne den Status
 * seines bestands zu ändern, also
 * Fragen, Wortmeldungen, Stream, Unterlagen, ....
 * 
 * 
 * Welche Phase gilt?
 * ------------------
 * > Alle Phasen, die manuell deaktiv sind, werden ignoriert (auch wenn eine Phase gleichzeitig
 * manuell aktiviert wurde)
 * > Vorrangig gilt die erste Phase, die manuell aktiviert wurde. (weitere manuell
 * aktivierte Phasen werden ignoriert)
 * > "Zeitlich" wird die Phase aktiviert, deren Beginn "am kürzesten" vor der aktuellen (Test-) Zeit liegt.
 * > Wird nach den bisherigen Kriterien keine Phase gefunden, wird standardmäßig Phase 1 aktiviert.
 * 
 * Die Pflege der Phasen erfolgt unter Parameter-Portal-App "Phasen".
 * 
 * Die Pflege der Zeitpunkte, zu denen die Phasen aktiviert werden, erfolgt unter HVMaster->Konfiguration->Termine. 
 * Dort entsprechen die Idents 1 bis 20 den Phasen 1 bis 20. Achtung: für die Zeitsteuerung sind nur die Spalten
 * "TT.MM.JJJJ" und "HH:MM:SS" relevant - beide müssen vollständig gefüllt sein.
 *
 *
 * Angeboten, Aktiv, Berechtigt
 * ----------------------------
 * "Angeboten": die jeweilige Willenserklärung/Portalfunktion wird grundsätzlich und überhaupt angeboten.
 * 
 * "Aktiv": die jeweilige Willenserklärung/Portalfunktion ist derzeit aktiv, dh. kann grundsätzlich
 * zum jetzigen Zeitpunkt genutzt werden.
 * 
 * "Berechtigt": der jeweilige Teilnehmer ist berechtigt, die Willenserklärung/Funktion zu nutzen.
 * 
 * Damit ein Teilnehmer eine Willenserklärung/Portalfunktion tatsächlich aktuell nutzen kann, muß diese
 * Angeboten werden, derzeit aktiv sein, und der Teilnehmer muß dafür berechtigt sein.
 * 
 * 
 * Angeboten/Aktiv/Berechtigt für Willenserklärungen
 * -------------------------------------------------
 * "Angeboten" wird in Parameter-Portal-App unter "Erklärungen" gesetzt.
 * "Aktivierung" erfolgt ausschließlich über die Phasen.
 * Ob ein Teilnehmer für eine Willenserklärung "berechtigt" ist, hängt ausschließlich vom aktuellen Status seines
 * Bestandes ab.
 * 
 * Angeboten/Aktiv/Berechtigt für Portalfunktionen
 * -----------------------------------------------
 * "Angeboten" wird in Parameter-Portal-App unter "Portal-Funktionen" gesetzt.
 * 
 * Aktiv wird wie folgt gesetzt:
 * > (4) "Manuell Deaktiv" in Parameter-Portal-App "Portal-Funktionen": dann aktuell deaktiv, egal was sonst eingestellt ist
 * > (3) "Manuell Aktiv" in Parameter-Portal-App "Portal-Funktionen": dann aktuell Aktiv, egal was sonst eingestellt ist
 * > (2) gemäß Zeitsteuerung: Schlägt Phasensteuerung - d.h. Funktion ist aktiv jenachdem was in HVMaster->
 *      Konfiguration->Termine für diese Portalfunktion eingetragen ist.
 *              Dabei werden in den Terminen die Idents ab 121 angezogen. Der erste Termin ist der Beginn-Termin
 *              (bei Streaming die 121), der zweite Termin (bei Streaming also die 122) der Ende-Termin, usw.
 * > (1) gemäß Phasensteuerung: die Portal-Funktion wird aktiv gesetzt, wenn sie in der aktuell aktiven
 *      Phase aktiviert ist.             
 * 
 * Spezialfall Portalfunktion Unterlagen:
 * --------------------------------------
 * Es gibt keine eigene Portalfunktion für Aktivierung / Deaktivierung Unterlagen. Die Portalfunktion "Unterlagen-Anzeigen"
 * wird immer dann angeboten / aktiv, wenn eine der Unterlagen-Gruppen angeboten/aktiv ist. Deshalb muß - auch wenn
 * keine Unterlagen-Gruppen sonst verwendet werden - mindestens immer eine der Unterlagen-Gruppen zur Steuerung
 * verwendet werden.
 * 
 * Spezialfall Unterlagen:
 * -----------------------
 * Bei Unterlagen wird die Aktivierung je Unterlage über den Reiter "Unterlagen" gesteuert - Feld "aktiv (1-7)".
 * 1-5 die Unterlagenaktivierung ist abhängig von der Unterlagen-Gruppe 1 bis 5 (siehe Unterlagen-Gruppen)
 * 6=die Unterlage ist immer aktiv
 * 7=die Unterlage ist immer deaktiv
 * 
 * Die Berechtigung erfolgt entweder über den Reiter Unterlagen (Gast1 etc.) (wenn 
 * Aktivierung NICHT über eine Unterlagengruppe erfolgt),  oder individuell über eine Unterlagen-
 * Gruppe. Für eine Unterlagen-Gruppe kann die Berechtigung je Login-Kennung vergeben werden, nicht jedoch für
 * eine einzelne Unterlage.
 * 
 * Achtung: eine Unterlage kann nur ENTWEDER über eine Unterlagen-Gruppe gesteuert werden, ODER über Gast1, Gast2 etc.
 * im Reiter Unterlagen. Vorrangig gilt dabei die Steuerung über eine Unterlagen-Gruppe.
 * 
 * Spezialfall Unterlagen-Gruppen
 * ------------------------------
 * Warum Unterlagen-Gruppen verwenden?
 * Einzelne Unterlagen können nicht Phasengesteuert aktiviert / deaktiviert werden und auch nicht einzelnen
 * Kennungen explizit zugeordnet werden.
 * 
 * Damit wären folgende Anforderungen ohne Unterlagen-Gruppen nicht oder nur aufwändig manuell - 
 * Aktivierung / Deaktivierung je Dokument - abbildbar:
 * > Fall 1: die Rede des Vorsitzenden als Text-PDF und als Präsentations-PDF sollen erst nach dem Halten
 * der Rede abrufbar sein
 * > Fall 2: den "Presse-Gastkennungen" sollen 5 PDFs am Tag der HV zur Verfügung gestellt werden
 * 
 * Unterlagen-Gruppen können sowohl einzelnen Kennungen als Berechtigung zugeordnet werden, und 
 * sie können auch Phasengesteuert aktiviert / deaktiviert werden.
 * Im obigen Beispiel:
 * > Die beiden Dokumente der Rede des Vorsitzenden werden der Unterlagen-Gruppe 1 zugeordnet. Die
 * Unterlagen-Gruppe 1 wird in den Portal-Funktionen entsprechend zugeordnet. Mit Aktivierung der
 * Unterlagen-Gruppe 1 werden dann beide Dokumente aktiv.
 * > Die obigen Dokumente für die Presse werden der Unterlagen-Gruppe 2 zugeordnet. Die Gastkennungen
 * für die Presse werden die Berechtigung für die Unterlagen-Gruppe 2 zugeordnet. Start der Unterlagen-Gruppe
 * 2 wird auf HV-Tag 8.00 Uhr gesetzt.
  * 
 * Spezialfall Instis
 * ------------------
 * Instis haben immer mindestens den Status "Aktionär angemeldet" - unabhängig davon, ob bereits
 * Bestände zugeordnet sind oder nicht.
 * 
 * 
 * Abhilfe, falls dieser Effekt nicht gewünscht ist: Gastkarte für Instis bereits anlegen und 
 * verschicken, die Insti-Nummer bei der Gastkarte aber erst eintragen, wenn Bestände vorhanden sind.
 * 
 * 
 * Spezialfall Vertreter-Kennungen
 * -------------------------------
 * Vertreter-Kennungen haben immer den Status, den sie laut den ihnen zugeordneten Vollmachten haben.
 * Wenn sie keine zugeordneten Vollmachten haben (z.B. nach Widerruf), dann haben sie den
 * normalen Status eines Gastes.
 * 
 * 
 */

/**Managen und aufbereiten von Portalfunktionen - in Abhängigkeiten von Parametern und eingeloggten Usern*/
@RequestScoped
public class TPortalFunktionen {

    private int logDrucken = 3;

    private @Inject EclParamM eclParamM;
    private @Inject EclLoginDatenM eclLoginDatenM;

    private @Inject TSession tSession;
    private @Inject TAuswahlSession tAuswahlSession;

    private @Inject EclBesitzGesamtAuswahl1M eclBesitzGesamtAuswahl1M;
    private @Inject EclBesitzGesamtM eclBesitzGesamtM;

    /**Wird einmal je Situation gesetzt (über initWerte)*/
    private long lBerechtigung = 0;
    private long zeitstamp = 0;

    private EclPortalPhase aktuellePortalPhase = null;
    private int aktuellePortalPhaseNr = 0;

    /*******************Grundsätzliche einheitliche Überprüfung von Aktionärs-/Gast-Eigenschaft************/
    private boolean pruefeObAktionaer() {
        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 1) {
            /*Sonderablauf: ist selbst Mitglied, oder es existieren erhaltene Vollmachten (kann ein Bevollmächtigter oder ein Mitglied sein)*/
            if (eclBesitzGesamtAuswahl1M.isEigenerAREintragVorhanden() || eclBesitzGesamtAuswahl1M.isErhalteneVollmachtenVorhanden()) {
                return true;
            }
        } else {
            if (eclBesitzGesamtM.isAngemeldeteVorhanden() || eclBesitzGesamtM.isNichtAngemeldeteVorhanden()) {
                return true;
            }
        }
        return false;
    }

    private boolean pruefeObAngemeldeterAktionaer() {
        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 1) {
            /*Sonderablauf: ist selbst Mitglied, oder es existieren erhaltene Vollmachten (kann ein Bevollmächtigter oder ein Mitglied sein)*/
            if ( /*Eigener Bestand vorhanden, und dieser ist auch angemeldet*/
            (eclBesitzGesamtAuswahl1M.isEigenerAREintragVorhanden() && eclBesitzGesamtAuswahl1M.isAngemeldet())
                    /*Vollmachten sind ja auch automatisch immer angemeldet*/
                    || eclBesitzGesamtAuswahl1M.isErhalteneVollmachtenVorhanden()) {
                return true;
            }
        } else {
            CaBug.druckeLog("Checke angemeldeter Aktionär normaler Ablauf eclBesitzGesamtM.isAngemeldeteVorhanden()=" + eclBesitzGesamtM.isAngemeldeteVorhanden(), logDrucken, 10);
            if (eclBesitzGesamtM.isAngemeldeteNichtAusgeblendeteVorhanden()) {
                return true;
            }
        }
        
        if (eclBesitzGesamtM.isInstiKennungZugeordnet()) {
            return true;
        }
        return false;
    }

    private boolean pruefeObOnlineTeilnehmenderAktionaer() {
        if (eclParamM.getParam().paramPortal.varianteDialogablauf == 1) {
            if (eclBesitzGesamtAuswahl1M.liefereEigenePraesentVorhanden() || eclBesitzGesamtAuswahl1M.liefereVertretenePraesentVorhanden()
                    || eclBesitzGesamtAuswahl1M.liefereGeerbtePraesentVorhanden()) {
                return true;
            }
        }
        if (eclParamM.getParam().paramPortal.varianteDialogablauf==0) {
            if (eclBesitzGesamtM.isKennungIstOnlinePraesent()) {
                return true;
            }
        }
        return false;
    }

    private boolean pruefeObGast() {
        if (/*Kennung ist eine "Nicht-Aktionärskennung", also Insti oder Gast oder bevollmächtigter*/
        eclLoginDatenM.liefereKennungArt() == KonstLoginKennungArt.personenNatJur ||
        /*Kennung ist Aktionärskennung, die angemeldet ist aber nur ausgeblendete besitzt*/
                (eclLoginDatenM.liefereKennungArt() == KonstLoginKennungArt.aktienregister && eclBesitzGesamtM.isAngemeldeteVorhanden()
                        && eclBesitzGesamtM.isAngemeldeteNichtAusgeblendeteVorhanden() == false)) {
            return true;
        }
        return false;
    }

    private boolean pruefeObOnlineTeilnehmenderGast() {
        if (eclBesitzGesamtAuswahl1M.isGastPraesent()) {
            return true;
        }
        return false;
    }

    private boolean pruefeGastN(boolean pPortalUnterlagenBerechtigtGastN, int pPortalFunktion) {
        if (pPortalUnterlagenBerechtigtGastN) {
            long berechtigungsBit = KonstPortalFunktionen.berechtigungsbit(pPortalFunktion);
            if ((berechtigungsBit & lBerechtigung) == berechtigungsBit) {
                return true;
            }
    
        }
        return false;
    }

    private boolean pruefeOnlineTeilnehmenderGastN(boolean pPortalUnterlagenBerechtigtGastN, int pPortalFunktion) {
        if (pPortalUnterlagenBerechtigtGastN) {
            long berechtigungsBit = KonstPortalFunktionen.berechtigungsbit(pPortalFunktion);
            if ((berechtigungsBit & lBerechtigung) == berechtigungsBit) {
                if (pruefeObOnlineTeilnehmenderGast()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**************************Für Funktionen in KonstPortalFunktionen***********/

    private void initZeit() {
        zeitstamp = CaDatumZeit.zeitStempelMS() + tSession.getDifZeit();
    }

    /**Vorbelegen der Werte, die für die anderen Sub-Funktionen benötigt werden:
     * lBerechtigung : mit oder-verknüpfte Berechtigung aller zugeordneten Kennungen
     * zeitstamp
     * */
    private void initWerte() {
        lBerechtigung = eclLoginDatenM.getEclLoginDaten().berechtigungPortal;

        /*Nun ggf. weitere zugeordnete Kennungen mit verknüpfen*/
        if (eclBesitzGesamtM.isWeitereKennungen()) {
            List<EclBesitzJeKennung> besitzJeKennungListe = eclBesitzGesamtM.getBesitzJeKennungListe();
            if (besitzJeKennungListe != null) {
                for (EclBesitzJeKennung iEclBesitzJeKennung : besitzJeKennungListe) {
                    lBerechtigung = lBerechtigung | iEclBesitzJeKennung.berechtigungPortal;
                }
            }

        }
        initZeit();
    }

    private void initPhase() {
        long beginnZeitstamp = 0;
        int gefPhase = 1;
        int manuellAktiv = 0;
        for (int i = 1; i <= 20; i++) {
            EclPortalPhase lPortalPhase = eclParamM.getParam().paramPortal.eclPortalPhase[i];
            if (lPortalPhase.manuellDeaktiv == false && manuellAktiv == 0) {
                if (lPortalPhase.manuellAktiv) {
                    gefPhase = i;
                    manuellAktiv = i;
                } else {
                    if (lPortalPhase.offenVon > beginnZeitstamp && lPortalPhase.offenVon <= zeitstamp) {
                        gefPhase = i;
                        beginnZeitstamp = lPortalPhase.offenVon;
                    }
                }
            }
        }
        aktuellePortalPhase = eclParamM.getParam().paramPortal.eclPortalPhase[gefPhase];
        aktuellePortalPhaseNr = gefPhase;
        CaBug.druckeLog("aktive Phase Nr=" + aktuellePortalPhaseNr, logDrucken, 10);
    }

    /**pAnzeigebereich siehe KonstPortalUnterlagen*/
    private boolean berechtigtFuerPreview(int pAnzeigebereich) {
        switch (pAnzeigebereich) {
        case KonstPortalUnterlagen.ANZEIGEN_UNTERLAGEN:
        case KonstPortalUnterlagen.ANZEIGEN_BOTSCHAFTEN:
            return KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.preview, lBerechtigung);
        case KonstPortalUnterlagen.ANZEIGEN_LOGIN_OBEN:
        case KonstPortalUnterlagen.ANZEIGEN_LOGIN_UNTEN:
        case KonstPortalUnterlagen.ANZEIGEN_EXTERNE_SEITE:
            if (tSession.getTestModus().equals("1") || tSession.getTestModus().equals("2")) {
                return true;
            }
        }
        
        return false;
    }
    private boolean angebotenUB(int pPortalFunktionNr) {
        if (KonstPortalFunktionen.monitoring == pPortalFunktionNr) {
            /*Monitoring immer zulässig*/
            return true;
        }
        if (KonstPortalFunktionen.gaeste==pPortalFunktionNr) {
            /*Gästefunktion wird separat verwaltet*/
            return true;
        }
        if (KonstPortalFunktionen.einstellungen==pPortalFunktionNr) {
            return false;
        }
        if (pPortalFunktionNr > 40) {
            CaBug.drucke("001 pPortalFunktionNr="+pPortalFunktionNr);
            return false;
        }
        return eclParamM.getParam().paramPortal.eclPortalFunktion[pPortalFunktionNr].wirdAngeboten;
    }

    private boolean aktivUB(int pPortalFunktionNr) {
        if (pPortalFunktionNr == KonstPortalFunktionen.monitoring) {
            /**Monitoring ist immer aktiv - unabhängig von Phase etc.*/
            return true;
        }
        EclPortalFunktion lPortalFunktion = eclParamM.getParam().paramPortal.eclPortalFunktion[pPortalFunktionNr];
        
        /*Manuelle Steuerung*/
        CaBug.druckeLog("Pruefe Manuell " + KonstPortalFunktionen.getText(pPortalFunktionNr), logDrucken, 10);
        if (lPortalFunktion.aktivVorrangig()) {
            CaBug.druckeLog("Manuell aktiviert", logDrucken, 10);
            return true;
        }
        if (lPortalFunktion.inaktivVorrangig()) {
            CaBug.druckeLog("Manuell deaktiviert", logDrucken, 10);
            return false;
        }
    
        /*Steuerung lt. Zeit*/
    
        /*Steuerung laut Funktion*/
        if (lPortalFunktion.aktivLtTermin()) {
            CaBug.druckeLog("Pruefe Zeit lt. Funktion", logDrucken, 10);
            /*Von*/
            CaBug.druckeLog("zeitstamp=" + zeitstamp + " lPortalFunktion.offenVon=" + lPortalFunktion.offenVon + " lPortalFunktion.offenBis=" + lPortalFunktion.offenBis, logDrucken, 10);
            if (lPortalFunktion.offenVon != 0) {
                if (zeitstamp < lPortalFunktion.offenVon) {
                    return false;
                }
            }
            if (lPortalFunktion.offenBis != 0) {
                if (zeitstamp > lPortalFunktion.offenBis) {
                    return false;
                }
            }
            return true;
        }
    
        /*Steuerung laut Phase*/
        CaBug.druckeLog("Pruefe lt. Phase", logDrucken, 10);
        CaBug.druckeLog("Aktive Phase=" + aktuellePortalPhaseNr, logDrucken, 10);
        //@formatter:off
        switch (pPortalFunktionNr) {
        case KonstPortalFunktionen.stream:
            if (aktuellePortalPhase.lfdHVStreamIstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.fragen:
            if (aktuellePortalPhase.lfdHVFragenIstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.rueckfragen:
            if (aktuellePortalPhase.lfdHVRueckfragenIstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.wortmeldungen:
            if (aktuellePortalPhase.lfdHVWortmeldungenIstMoeglich) {return true;}
             break;
        case KonstPortalFunktionen.widersprueche:
            if (aktuellePortalPhase.lfdHVWiderspruecheIstMoeglich) {return true;}
             break;
        case KonstPortalFunktionen.antraege:
            if (aktuellePortalPhase.lfdHVAntraegeIstMoeglich) {return true;}
             break;
        case KonstPortalFunktionen.sonstigeMitteilungen:
            if (aktuellePortalPhase.lfdHVSonstigeMitteilungenIstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.botschaftenEinreichen:
            if (aktuellePortalPhase.lfdHVBotschaftenEinreichenIstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.botschaftenAnzeige:
            if (aktuellePortalPhase.lfdHVBotschaftenIstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.chat:
            if (aktuellePortalPhase.lfdHVChatIstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.chatInsti:
            if (aktuellePortalPhase.lfdHVChatIstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.chatVIP:
            if (aktuellePortalPhase.lfdHVChatIstMoeglich) {return true;}
             break;
        case KonstPortalFunktionen.unterlagenGruppe1:
            if (aktuellePortalPhase.lfdHVUnterlagenGruppe1IstMoeglich) {return true;}
             break;
        case KonstPortalFunktionen.unterlagenGruppe2:
            if (aktuellePortalPhase.lfdHVUnterlagenGruppe2IstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.unterlagenGruppe3:
            if (aktuellePortalPhase.lfdHVUnterlagenGruppe3IstMoeglich) {return true;}
             break;
        case KonstPortalFunktionen.unterlagenGruppe4:
            if (aktuellePortalPhase.lfdHVUnterlagenGruppe4IstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.unterlagenGruppe5:
            if (aktuellePortalPhase.lfdHVUnterlagenGruppe5IstMoeglich) {return true;}
            break;
        case KonstPortalFunktionen.teilnehmerverzeichnis:
            if (aktuellePortalPhase.lfdHVTeilnehmerverzIstMoeglich) {return true;}
           break;
        case KonstPortalFunktionen.abstimmungsergebnisse:
            if (aktuellePortalPhase.lfdHVAbstimmungsergIstMoeglich) {return true;}
             break;
        case KonstPortalFunktionen.onlineteilnahme:
            if (aktuellePortalPhase.lfdHVAbstimmungsergIstMoeglich) {return true;}
             break;
       }
        //@formatter:on
    
        return false;
    }

    private boolean berechtigt(int pPortalFunktionNr) {
        /*Direkt berechtigt - über eclLoginDaten.berechtigungPortal?*/
        long berechtigungsBit = KonstPortalFunktionen.berechtigungsbit(pPortalFunktionNr);
        CaBug.druckeLog(KonstPortalFunktionen.getText(pPortalFunktionNr) + "berechtigungsBit=" + berechtigungsBit + " lBerechtigung=" + lBerechtigung, logDrucken, 10);
        if (((long) berechtigungsBit & (long) lBerechtigung) == berechtigungsBit) {
            CaBug.druckeLog("Berechtigung wg. Berechtigungsbit", logDrucken, 10);
            return true;
        }

        //@formatter:off

         EclPortalFunktion lPortalFunktion = eclParamM.getParam().paramPortal.eclPortalFunktion[pPortalFunktionNr];
        /*Berechtigung - über Aktionärsstatus?*/
        if (lPortalFunktion.berechtigtAktionaer) {
            CaBug.druckeLog("Checke 'nur Aktionär reicht'", logDrucken, 10);
            if (pruefeObAktionaer()) {return true;}
        }
        if (lPortalFunktion.berechtigtAngemeldeterAktionaer) {
            CaBug.druckeLog("Checke angemeldeter Aktionär", logDrucken, 10);
            if (pruefeObAngemeldeterAktionaer()) {return true;}
        }
        CaBug.druckeLog("lPortalFunktion.berechtigtOnlineTeilnahmeAktionaer="+lPortalFunktion.berechtigtOnlineTeilnahmeAktionaer, logDrucken, 10);
        if (lPortalFunktion.berechtigtOnlineTeilnahmeAktionaer) {
            CaBug.druckeLog("pruefeObOnlineTeilnehmenderAktionaer()="+pruefeObOnlineTeilnehmenderAktionaer(), logDrucken, 10);
            if (pruefeObOnlineTeilnehmenderAktionaer()) {return true;}
       }

        /*Berechtigung - über Gastgruppe?*/
        if (pruefeGastN(lPortalFunktion.berechtigtGast1, KonstPortalFunktionen.gast1)) { return true; }
        if (pruefeGastN(lPortalFunktion.berechtigtGast2, KonstPortalFunktionen.gast2)) { return true; }
        if (pruefeGastN(lPortalFunktion.berechtigtGast3, KonstPortalFunktionen.gast3)) { return true; }
        if (pruefeGastN(lPortalFunktion.berechtigtGast4, KonstPortalFunktionen.gast4)) { return true; }
        if (pruefeGastN(lPortalFunktion.berechtigtGast5, KonstPortalFunktionen.gast5)) { return true; }
        if (pruefeGastN(lPortalFunktion.berechtigtGast6, KonstPortalFunktionen.gast6)) { return true; }
        if (pruefeGastN(lPortalFunktion.berechtigtGast7, KonstPortalFunktionen.gast7)) { return true; }
        if (pruefeGastN(lPortalFunktion.berechtigtGast8, KonstPortalFunktionen.gast8)) { return true; }
        if (pruefeGastN(lPortalFunktion.berechtigtGast9, KonstPortalFunktionen.gast9)) { return true; }
        
        /**Gast 10 muß nicht explizit berechtigt werden, sondern wird anhand von Gasteigenschaften gewertet*/
        if (lPortalFunktion.berechtigtGast10) {
            if (pruefeObGast()) {
                return true;
            }
        }

        /*Berechtigung - über Gastgruppe und Online-Gast-Präsent?*/
        if (pruefeOnlineTeilnehmenderGastN(lPortalFunktion.berechtigtGastOnlineTeilnahmer1, KonstPortalFunktionen.gast1)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(lPortalFunktion.berechtigtGastOnlineTeilnahmer2, KonstPortalFunktionen.gast2)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(lPortalFunktion.berechtigtGastOnlineTeilnahmer3, KonstPortalFunktionen.gast3)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(lPortalFunktion.berechtigtGastOnlineTeilnahmer4, KonstPortalFunktionen.gast4)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(lPortalFunktion.berechtigtGastOnlineTeilnahmer5, KonstPortalFunktionen.gast5)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(lPortalFunktion.berechtigtGastOnlineTeilnahmer6, KonstPortalFunktionen.gast6)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(lPortalFunktion.berechtigtGastOnlineTeilnahmer7, KonstPortalFunktionen.gast7)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(lPortalFunktion.berechtigtGastOnlineTeilnahmer8, KonstPortalFunktionen.gast8)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(lPortalFunktion.berechtigtGastOnlineTeilnahmer9, KonstPortalFunktionen.gast9)) { return true; }
        /**Gast 10 muß nicht explizit berechtigt werden, sondern wird anhand von Gasteigenschaften gewertet*/
        if (lPortalFunktion.berechtigtGastOnlineTeilnahmer10) {
            if (pruefeObOnlineTeilnehmenderGast()) {
                return true;
            }
        }
        //@formatter:on

        return false;
    }

    /**Vor Aufruf dieser Funktion sollte belegeAnzahLFragenGestellt aufgerufen werden.
     * Muß aufgerufen werden, wenn Rückfragen zulässig.
     */
    public void belegeAngebotenAktivFuerAuswahl() {
        initWerte();
        initPhase();
        belegeAngebotenAktivFuerAuswahlAusfuehren();
    }

    /**Voraussetzung: initWerte und initPhase bereits ausgeführt*/
    private void belegeAngebotenAktivFuerAuswahlAusfuehren() {
        for (int i = 1; i <= 40; i++) {
            if (i <= 20 || i >= 31) {
                belegeAngebotenAktivFuerAuswahlAusfuehrenFuerEinzelnePortalFunktion(i);
            }
        }
        ergaenzeUnterlagen();
    }

    private void belegeAngebotenAktivFuerAuswahlAusfuehrenFuerEinzelnePortalFunktion(int i) {
        boolean lAngebotenUB = false;
        boolean lBerechtigt = false;
        boolean lAngeboten = false;
        boolean lAktivUB = false;
        boolean lAktiv = false;

        lAngebotenUB = angebotenUB(i);
        if (lAngebotenUB) {
            lBerechtigt = berechtigt(i);
            lAngeboten = lAngebotenUB & lBerechtigt;
            lAktivUB = aktivUB(i);
            lAktiv = lAngebotenUB & lBerechtigt & lAktivUB;
            if (logDrucken == 10) {
                CaBug.druckeLog(KonstPortalFunktionen.getText(i) + " i=" + i + " lAngebotenUB=" + lAngebotenUB + " lBerechtigt=" + lBerechtigt + " lAngeboten=" + lAngeboten + " lAktivUB="
                        + lAktivUB + " lAktiv=" + lAktiv, logDrucken, 10);
            }
        }
        CaBug.druckeLog("i="+i, logDrucken, 10);
        switch (i) {
        case KonstPortalFunktionen.stream:
            tAuswahlSession.setStreamAngebotenUB(lAngebotenUB);
            tAuswahlSession.setStreamBerechtigt(lBerechtigt);
            tAuswahlSession.setStreamAngeboten(lAngeboten);
            tAuswahlSession.setStreamAktivUB(lAktivUB);
            tAuswahlSession.setStreamAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.fragen:
            tAuswahlSession.setFragenAngebotenUB(lAngebotenUB);
            tAuswahlSession.setFragenBerechtigt(lBerechtigt);
            tAuswahlSession.setFragenAngeboten(lAngeboten);
            tAuswahlSession.setFragenAktivUB(lAktivUB);
            tAuswahlSession.setFragenAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.rueckfragen:
            tAuswahlSession.setRueckfragenAngebotenUB(lAngebotenUB);
            /*Sondersituation!*/
            tAuswahlSession.setRueckfragenBerechtigtWennGestellt(lBerechtigt);
            tAuswahlSession.setRueckfragenAngeboten(lAngeboten);
            tAuswahlSession.setRueckfragenAktivUB(lAktivUB);
            tAuswahlSession.setRueckfragenAktiv(lAktiv);
            CaBug.druckeLog("rueckfragen A", logDrucken, 10);
            if (lBerechtigt && eclParamM.getParam().paramPortal.fragenRueckfragenErmoeglichen==1) {
                CaBug.druckeLog("rueckfragen B", logDrucken, 10);
                if (tAuswahlSession.isFragenWurdenGestellt()) {
                    CaBug.druckeLog("rueckfragen C", logDrucken, 10);
                    tAuswahlSession.setRueckfragenBerechtigt(true);
                }
                else {
                    CaBug.druckeLog("rueckfragen D", logDrucken, 10);
                   tAuswahlSession.setRueckfragenBerechtigt(false);
                }
            }
            else {
                CaBug.druckeLog("rueckfragen E", logDrucken, 10);
                tAuswahlSession.setRueckfragenBerechtigt(false);
            }
            break;
        case KonstPortalFunktionen.wortmeldungen:
            tAuswahlSession.setWortmeldungenAngebotenUB(lAngebotenUB);
            tAuswahlSession.setWortmeldungenBerechtigt(lBerechtigt);
            tAuswahlSession.setWortmeldungenAngeboten(lAngeboten);
            tAuswahlSession.setWortmeldungenAktivUB(lAktivUB);
            tAuswahlSession.setWortmeldungenAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.widersprueche:
            tAuswahlSession.setWiderspruecheAngebotenUB(lAngebotenUB);
            tAuswahlSession.setWiderspruecheBerechtigt(lBerechtigt);
            tAuswahlSession.setWiderspruecheAngeboten(lAngeboten);
            tAuswahlSession.setWiderspruecheAktivUB(lAktivUB);
            tAuswahlSession.setWiderspruecheAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.antraege:
            tAuswahlSession.setAntraegeAngebotenUB(lAngebotenUB);
            tAuswahlSession.setAntraegeBerechtigt(lBerechtigt);
            tAuswahlSession.setAntraegeAngeboten(lAngeboten);
            tAuswahlSession.setAntraegeAktivUB(lAktivUB);
            tAuswahlSession.setAntraegeAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.sonstigeMitteilungen:
            tAuswahlSession.setSonstigeMitteilungenAngebotenUB(lAngebotenUB);
            tAuswahlSession.setSonstigeMitteilungenBerechtigt(lBerechtigt);
            tAuswahlSession.setSonstigeMitteilungenAngeboten(lAngeboten);
            tAuswahlSession.setSonstigeMitteilungenAktivUB(lAktivUB);
            tAuswahlSession.setSonstigeMitteilungenAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.chat:
            tAuswahlSession.setChatAngebotenUB(lAngebotenUB);
            tAuswahlSession.setChatBerechtigt(lBerechtigt);
            tAuswahlSession.setChatAngeboten(lAngeboten);
            tAuswahlSession.setChatAktivUB(lAktivUB);
            tAuswahlSession.setChatAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.chatInsti:
            tAuswahlSession.setChatInstiAngebotenUB(lAngebotenUB);
            tAuswahlSession.setChatInstiBerechtigt(lBerechtigt);
            tAuswahlSession.setChatInstiAngeboten(lAngeboten);
            tAuswahlSession.setChatInstiAktivUB(lAktivUB);
            tAuswahlSession.setChatInstiAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.chatVIP:
            tAuswahlSession.setChatVIPAngebotenUB(lAngebotenUB);
            tAuswahlSession.setChatVIPBerechtigt(lBerechtigt);
            tAuswahlSession.setChatVIPAngeboten(lAngeboten);
            tAuswahlSession.setChatVIPAktivUB(lAktivUB);
            tAuswahlSession.setChatVIPAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.unterlagenGruppe1:
            tAuswahlSession.setUnterlagenGruppe1AngebotenUB(lAngebotenUB);
            tAuswahlSession.setUnterlagenGruppe1Berechtigt(lBerechtigt);
            tAuswahlSession.setUnterlagenGruppe1Angeboten(lAngeboten);
            tAuswahlSession.setUnterlagenGruppe1AktivUB(lAktivUB);
            tAuswahlSession.setUnterlagenGruppe1Aktiv(lAktiv);
            break;
        case KonstPortalFunktionen.unterlagenGruppe2:
            tAuswahlSession.setUnterlagenGruppe2AngebotenUB(lAngebotenUB);
            tAuswahlSession.setUnterlagenGruppe2Berechtigt(lBerechtigt);
            tAuswahlSession.setUnterlagenGruppe2Angeboten(lAngeboten);
            tAuswahlSession.setUnterlagenGruppe2AktivUB(lAktivUB);
            tAuswahlSession.setUnterlagenGruppe2Aktiv(lAktiv);
            break;
        case KonstPortalFunktionen.unterlagenGruppe3:
            tAuswahlSession.setUnterlagenGruppe3AngebotenUB(lAngebotenUB);
            tAuswahlSession.setUnterlagenGruppe3Berechtigt(lBerechtigt);
            tAuswahlSession.setUnterlagenGruppe3Angeboten(lAngeboten);
            tAuswahlSession.setUnterlagenGruppe3AktivUB(lAktivUB);
            tAuswahlSession.setUnterlagenGruppe3Aktiv(lAktiv);
            break;
        case KonstPortalFunktionen.unterlagenGruppe4:
            tAuswahlSession.setUnterlagenGruppe4AngebotenUB(lAngebotenUB);
            tAuswahlSession.setUnterlagenGruppe4Berechtigt(lBerechtigt);
            tAuswahlSession.setUnterlagenGruppe4Angeboten(lAngeboten);
            tAuswahlSession.setUnterlagenGruppe4AktivUB(lAktivUB);
            tAuswahlSession.setUnterlagenGruppe4Aktiv(lAktiv);
            break;
        case KonstPortalFunktionen.unterlagenGruppe5:
            tAuswahlSession.setUnterlagenGruppe5AngebotenUB(lAngebotenUB);
            tAuswahlSession.setUnterlagenGruppe5Berechtigt(lBerechtigt);
            tAuswahlSession.setUnterlagenGruppe5Angeboten(lAngeboten);
            tAuswahlSession.setUnterlagenGruppe5AktivUB(lAktivUB);
            tAuswahlSession.setUnterlagenGruppe5Aktiv(lAktiv);
            break;
        case KonstPortalFunktionen.botschaftenAnzeige:
            tAuswahlSession.setBotschaftenAnzeigeAngebotenUB(lAngebotenUB);
            tAuswahlSession.setBotschaftenAnzeigeBerechtigt(lBerechtigt);
            tAuswahlSession.setBotschaftenAnzeigeAngeboten(lAngeboten);
            tAuswahlSession.setBotschaftenAnzeigeAktivUB(lAktivUB);
            tAuswahlSession.setBotschaftenAnzeigeAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.botschaftenEinreichen:
            tAuswahlSession.setBotschaftenEinreichenAngebotenUB(lAngebotenUB);
            tAuswahlSession.setBotschaftenEinreichenBerechtigt(lBerechtigt);
            tAuswahlSession.setBotschaftenEinreichenAngeboten(lAngeboten);
            tAuswahlSession.setBotschaftenEinreichenAktivUB(lAktivUB);
            tAuswahlSession.setBotschaftenEinreichenAktiv(lAktiv);
            /*Sonderfall*/
            if (eclParamM.getParam().paramPortal.botschaftenVoranmeldungErforderlich==1) {
                if (KonstPortalFunktionen.darfFunktion(KonstPortalFunktionen.botschaftenEinreichen, lBerechtigung)) {
                    tAuswahlSession.setBotschaftenEinreichenTatsaechlichBerechtigt(true);
                }
                else {
                    tAuswahlSession.setBotschaftenEinreichenTatsaechlichBerechtigt(false);
                }
            }
            else {
                tAuswahlSession.setBotschaftenEinreichenTatsaechlichBerechtigt(true);
            }
            break;
        case KonstPortalFunktionen.teilnehmerverzeichnis:
            tAuswahlSession.setTeilnehmerverzeichnisAngebotenUB(lAngebotenUB);
            tAuswahlSession.setTeilnehmerverzeichnisBerechtigt(lBerechtigt);
            tAuswahlSession.setTeilnehmerverzeichnisAngeboten(lAngeboten);
            tAuswahlSession.setTeilnehmerverzeichnisAktivUB(lAktivUB);
            tAuswahlSession.setTeilnehmerverzeichnisAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.abstimmungsergebnisse:
            tAuswahlSession.setAbstimmungsergebnisseAngebotenUB(lAngebotenUB);
            tAuswahlSession.setAbstimmungsergebnisseBerechtigt(lBerechtigt);
            tAuswahlSession.setAbstimmungsergebnisseAngeboten(lAngeboten);
            tAuswahlSession.setAbstimmungsergebnisseAktivUB(lAktivUB);
            tAuswahlSession.setAbstimmungsergebnisseAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.onlineteilnahme:
            tAuswahlSession.setOnlineteilnahmeAngebotenUB(lAngebotenUB);
            tAuswahlSession.setOnlineteilnahmeBerechtigt(lBerechtigt);
            tAuswahlSession.setOnlineteilnahmeAngeboten(lAngeboten);
            tAuswahlSession.setOnlineteilnahmeAktivUB(lAktivUB);
            tAuswahlSession.setOnlineteilnahmeAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.empfangFragen:
            tAuswahlSession.setEmpfangFragenAngebotenUB(lAngebotenUB);
            tAuswahlSession.setEmpfangFragenBerechtigt(lBerechtigt);
            tAuswahlSession.setEmpfangFragenAngeboten(lAngeboten);
            tAuswahlSession.setEmpfangFragenAktivUB(lAktivUB);
            tAuswahlSession.setEmpfangFragenAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.empfangWortmeldungen:
            tAuswahlSession.setEmpfangWortmeldungenAngebotenUB(lAngebotenUB);
            tAuswahlSession.setEmpfangWortmeldungenBerechtigt(lBerechtigt);
            tAuswahlSession.setEmpfangWortmeldungenAngeboten(lAngeboten);
            tAuswahlSession.setEmpfangWortmeldungenAktivUB(lAktivUB);
            tAuswahlSession.setEmpfangWortmeldungenAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.empfangWiderspruch:
            tAuswahlSession.setEmpfangWiderspruchAngebotenUB(lAngebotenUB);
            tAuswahlSession.setEmpfangWiderspruchBerechtigt(lBerechtigt);
            tAuswahlSession.setEmpfangWiderspruchAngeboten(lAngeboten);
            tAuswahlSession.setEmpfangWiderspruchAktivUB(lAktivUB);
            tAuswahlSession.setEmpfangWiderspruchAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.empfangAntraege:
            tAuswahlSession.setEmpfangAntraegeAngebotenUB(lAngebotenUB);
            tAuswahlSession.setEmpfangAntraegeBerechtigt(lBerechtigt);
            tAuswahlSession.setEmpfangAntraegeAngeboten(lAngeboten);
            tAuswahlSession.setEmpfangAntraegeAktivUB(lAktivUB);
            tAuswahlSession.setEmpfangAntraegeAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.empfangSonstigeMitteilungen:
            tAuswahlSession.setEmpfangSonstigeMitteilungenAngebotenUB(lAngebotenUB);
            tAuswahlSession.setEmpfangSonstigeMitteilungenBerechtigt(lBerechtigt);
            tAuswahlSession.setEmpfangSonstigeMitteilungenAngeboten(lAngeboten);
            tAuswahlSession.setEmpfangSonstigeMitteilungenAktivUB(lAktivUB);
            tAuswahlSession.setEmpfangSonstigeMitteilungenAktiv(lAktiv);
            break;
        case KonstPortalFunktionen.monitoring:
            tAuswahlSession.setMonitoringAngebotenUB(lAngebotenUB);
            tAuswahlSession.setMonitoringBerechtigt(lBerechtigt);
            tAuswahlSession.setMonitoringAngeboten(lAngeboten);
            tAuswahlSession.setMonitoringAktivUB(lAktivUB);
            tAuswahlSession.setMonitoringAktiv(lAktiv);
            break;
        }
    }

    private void ergaenzeUnterlagen() {
        //@formatter:off
        tAuswahlSession.setUnterlagenAngebotenUB(tAuswahlSession.isUnterlagenGruppe1AngebotenUB() || tAuswahlSession.isUnterlagenGruppe2AngebotenUB() ||tAuswahlSession.isUnterlagenGruppe3AngebotenUB() ||tAuswahlSession.isUnterlagenGruppe4AngebotenUB() ||tAuswahlSession.isUnterlagenGruppe5AngebotenUB());
        tAuswahlSession.setUnterlagenBerechtigt(tAuswahlSession.isUnterlagenGruppe1Berechtigt() || tAuswahlSession.isUnterlagenGruppe2Berechtigt() ||tAuswahlSession.isUnterlagenGruppe3Berechtigt() ||tAuswahlSession.isUnterlagenGruppe4Berechtigt() ||tAuswahlSession.isUnterlagenGruppe5Berechtigt());
        tAuswahlSession.setUnterlagenAngeboten(tAuswahlSession.isUnterlagenGruppe1Angeboten() || tAuswahlSession.isUnterlagenGruppe2Angeboten() ||tAuswahlSession.isUnterlagenGruppe3Angeboten() ||tAuswahlSession.isUnterlagenGruppe4Angeboten() ||tAuswahlSession.isUnterlagenGruppe5Angeboten());
        tAuswahlSession.setUnterlagenAktivUB(tAuswahlSession.isUnterlagenGruppe1AktivUB() || tAuswahlSession.isUnterlagenGruppe2AktivUB() ||tAuswahlSession.isUnterlagenGruppe3AktivUB() ||tAuswahlSession.isUnterlagenGruppe4AktivUB() ||tAuswahlSession.isUnterlagenGruppe5AktivUB());
        tAuswahlSession.setUnterlagenAktiv(tAuswahlSession.isUnterlagenGruppe1Aktiv() || tAuswahlSession.isUnterlagenGruppe2Aktiv() ||tAuswahlSession.isUnterlagenGruppe3Aktiv() ||tAuswahlSession.isUnterlagenGruppe4Aktiv() ||tAuswahlSession.isUnterlagenGruppe5Aktiv());
        //@formatter:on

    }
    /**Nur für Aufruf von außen gedacht, nach bereiteVorPruefungEinzelnePortalFunktion, 
     * da sonst Werte jedesmal initiiert werden.
     * 
     * Nur gedacht, wenn eine einzelne Funktion überprüft werden soll (und deshalb nicht
     * gesamte tAuswahlSession-Werte erzeugt werden müssen)
     * */
    public boolean angebotenUndBerechtigtPortalFunktion(int pPortalFunktionNr) {
        CaBug.druckeLog("pPortalFunktionNr=" + pPortalFunktionNr, logDrucken, 10);
        if (pPortalFunktionNr==KonstPortalFunktionen.unterlagen) {
            return (
              (
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe1) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe2) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe3) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe4) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe5)
              )
              &&
              (
                      berechtigt(KonstPortalFunktionen.unterlagenGruppe1) ||
                      berechtigt(KonstPortalFunktionen.unterlagenGruppe2) ||
                      berechtigt(KonstPortalFunktionen.unterlagenGruppe3) ||
                      berechtigt(KonstPortalFunktionen.unterlagenGruppe4) ||
                      berechtigt(KonstPortalFunktionen.unterlagenGruppe5)
              )
                  );   
        }
        else {
            return (angebotenUB(pPortalFunktionNr) && berechtigt(pPortalFunktionNr));
        }
    }

    /**Nur für Aufruf von außen gedacht, nach bereiteVorPruefungEinzelnePortalFunktion, 
     * da sonst Werte jedesmal initiiert werden.
     * 
     * Nur gedacht, wenn eine einzelne Funktion überprüft werden soll (und deshalb nicht
     * gesamte tAuswahlSession-Werte erzeugt werden müssen)
     * */
    public boolean aktivEinzelnePortalFunktion(int pPortalFunktionNr) {
        if (pPortalFunktionNr==KonstPortalFunktionen.unterlagen) {
            return (
              (
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe1) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe2) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe3) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe4) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe5)
              )
              &&
              (
                      berechtigt(KonstPortalFunktionen.unterlagenGruppe1) ||
                      berechtigt(KonstPortalFunktionen.unterlagenGruppe2) ||
                      berechtigt(KonstPortalFunktionen.unterlagenGruppe3) ||
                      berechtigt(KonstPortalFunktionen.unterlagenGruppe4) ||
                      berechtigt(KonstPortalFunktionen.unterlagenGruppe5)
              )
              &&
              (
                      aktivUB(KonstPortalFunktionen.unterlagenGruppe1) ||
                      aktivUB(KonstPortalFunktionen.unterlagenGruppe2) ||
                      aktivUB(KonstPortalFunktionen.unterlagenGruppe3) ||
                      aktivUB(KonstPortalFunktionen.unterlagenGruppe4) ||
                      aktivUB(KonstPortalFunktionen.unterlagenGruppe5)
              )
                    );   
                }
                else {
            return angebotenUB(pPortalFunktionNr) && berechtigt(pPortalFunktionNr) && aktivUB(pPortalFunktionNr);
        }
    }

    /**Nur für Aufruf von außen gedacht, nach bereiteVorPruefungEinzelnePortalFunktion, 
     * da sonst Werte jedesmal initiiert werden.
     * 
     * Nur gedacht, wenn eine einzelne Funktion überprüft werden soll (und deshalb nicht
     * gesamte tAuswahlSession-Werte erzeugt werden müssen)
     * */
    public boolean aktivUBEinzelnePortalFunktion(int pPortalFunktionNr) {
        if (pPortalFunktionNr==KonstPortalFunktionen.unterlagen) {
            return (
              (
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe1) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe2) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe3) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe4) ||
                      angebotenUB(KonstPortalFunktionen.unterlagenGruppe5)
              )
              &&
              (
                      aktivUB(KonstPortalFunktionen.unterlagenGruppe1) ||
                      aktivUB(KonstPortalFunktionen.unterlagenGruppe2) ||
                      aktivUB(KonstPortalFunktionen.unterlagenGruppe3) ||
                      aktivUB(KonstPortalFunktionen.unterlagenGruppe4) ||
                      aktivUB(KonstPortalFunktionen.unterlagenGruppe5)
              )
            );   
        }
        else {
            return angebotenUB(pPortalFunktionNr) && aktivUB(pPortalFunktionNr);
        }
    }

    /**Muß aufgerufen werden, bevor pruefeEinzelnePortalFunktion* aufgerufen wird (außer es wird
     * belegePortalFunktionenWillenserklaerungenStatusAktiv oder vergleichbare Funktion mit Initialisierung
     * stattdessen aufgerufen).
     * 
     * Anschließend sind - nur für diese Portalfunktion - die Werte in aAuswahlSession belegt.
     * angebotenUndBerechtigtPortalFunktion, aktivEinzelnePortalFunktion, aktivUBEinzelnePortalFunktion können
     * anschließend für ALLE portal-Funktionen aufgerufen werden.*/
    public void bereiteVorPruefungEinzelnePortalFunktion(int pPortalFunktionNr) {
        initWerte();
        initPhase();
        if (pPortalFunktionNr==KonstPortalFunktionen.gaeste) {
            return;
        }
        if (pPortalFunktionNr==KonstPortalFunktionen.unterlagen) {
            belegeAngebotenAktivFuerAuswahlAusfuehrenFuerEinzelnePortalFunktion(KonstPortalFunktionen.unterlagenGruppe1);
            belegeAngebotenAktivFuerAuswahlAusfuehrenFuerEinzelnePortalFunktion(KonstPortalFunktionen.unterlagenGruppe2);
            belegeAngebotenAktivFuerAuswahlAusfuehrenFuerEinzelnePortalFunktion(KonstPortalFunktionen.unterlagenGruppe3);
            belegeAngebotenAktivFuerAuswahlAusfuehrenFuerEinzelnePortalFunktion(KonstPortalFunktionen.unterlagenGruppe4);
            belegeAngebotenAktivFuerAuswahlAusfuehrenFuerEinzelnePortalFunktion(KonstPortalFunktionen.unterlagenGruppe5);
            ergaenzeUnterlagen();
        }
        else {
            belegeAngebotenAktivFuerAuswahlAusfuehrenFuerEinzelnePortalFunktion(pPortalFunktionNr);
        }
    }
    
    /**
     * Wie "aktiv()", nur für Unterlagen - da hier sonst 5 Prüfungen
     * hintereinander stattfinden müssen*/
    public boolean aktivUnterlagen() {
        initWerte();
        initPhase();
        if (angebotenUB(KonstPortalFunktionen.unterlagenGruppe1) && berechtigt(KonstPortalFunktionen.unterlagenGruppe1) && aktivUB(KonstPortalFunktionen.unterlagenGruppe1))
            return true;
        if (angebotenUB(KonstPortalFunktionen.unterlagenGruppe2) && berechtigt(KonstPortalFunktionen.unterlagenGruppe2) && aktivUB(KonstPortalFunktionen.unterlagenGruppe2))
            return true;
        if (angebotenUB(KonstPortalFunktionen.unterlagenGruppe3) && berechtigt(KonstPortalFunktionen.unterlagenGruppe3) && aktivUB(KonstPortalFunktionen.unterlagenGruppe3))
            return true;
        if (angebotenUB(KonstPortalFunktionen.unterlagenGruppe4) && berechtigt(KonstPortalFunktionen.unterlagenGruppe4) && aktivUB(KonstPortalFunktionen.unterlagenGruppe4))
            return true;
        if (angebotenUB(KonstPortalFunktionen.unterlagenGruppe5) && berechtigt(KonstPortalFunktionen.unterlagenGruppe5) && aktivUB(KonstPortalFunktionen.unterlagenGruppe5))
            return true;
        return false;
    }

    /**
     * pUnterlagenSubBereich wird nur verwendet, wenn pAnzeigeseite=KonstPortalUnterlagen.ANZEIGEN_PPORTAL
     */
    private boolean pruefenObEintragenUnterlagen(EclPortalUnterlagen pPortalUnterlagen, int pAnzeigeseite, int pUnterlagenSubBereich) {
        CaBug.druckeLog("pPortalUnterlagen.ident="+pPortalUnterlagen.ident, logDrucken, 10);
        /*Prüfen, ob Unterlage auf Anzeigeseite angezeigt werden soll*/
        switch (pAnzeigeseite) {
        case KonstPortalUnterlagen.ANZEIGEN_LOGIN_UNTENODEROBEN:
            if (pPortalUnterlagen.reihenfolgeLoginOben==0 && pPortalUnterlagen.reihenfolgeLoginUnten==0) {return false;}
            break;
        case KonstPortalUnterlagen.ANZEIGEN_LOGIN_OBEN:
            if (pPortalUnterlagen.reihenfolgeLoginOben==0) {return false;}
            break;
        case KonstPortalUnterlagen.ANZEIGEN_LOGIN_UNTEN:
            if (pPortalUnterlagen.reihenfolgeLoginUnten==0) {return false;}
            break;
        case KonstPortalUnterlagen.ANZEIGEN_EXTERNE_SEITE:
            if (pPortalUnterlagen.reihenfolgeExterneSeite==0) {return false;}
            break;
        case KonstPortalUnterlagen.ANZEIGEN_UNTERLAGEN:
            if (pPortalUnterlagen.reihenfolgeUnterlagen==0) {return false;}
            break;
        case KonstPortalUnterlagen.ANZEIGEN_BOTSCHAFTEN:
            if (pPortalUnterlagen.reihenfolgeBotschaften==0) {return false;}
            break;
        case KonstPortalUnterlagen.ANZEIGEN_PPORTAL:
            if (/*pUnterlagenSubbereich ist ungleich 0, 100, 200 etc. - dann muß die Menue-Nr. übereinstimmen*/
                    (pPortalUnterlagen.unterlagenbereichMenueNr!=pUnterlagenSubBereich
                && pUnterlagenSubBereich % 100!=0)
                    ||
                    /*pUnterlagenSubbereich ist gleich 0, 100, 200 etc. - dann muß Menue-Nr. im folgenden 100er Bereich sein*/
                    (pUnterlagenSubBereich % 100==0 &&
                    (pPortalUnterlagen.unterlagenbereichMenueNr<pUnterlagenSubBereich
                            || pPortalUnterlagen.unterlagenbereichMenueNr>=pUnterlagenSubBereich+100
                            )
                            )
                    ) {return false;}
            break;
        }
        CaBug.druckeLog("A", logDrucken, 10);
        /*Prüfen, ob Preview aktiv und berechtigt*/
        if (!berechtigtFuerPreview(pAnzeigeseite)) {
            switch (pAnzeigeseite) {
            case KonstPortalUnterlagen.ANZEIGEN_LOGIN_UNTENODEROBEN:
            case KonstPortalUnterlagen.ANZEIGEN_LOGIN_OBEN:
            case KonstPortalUnterlagen.ANZEIGEN_LOGIN_UNTEN:
                if (pPortalUnterlagen.previewLogin==1) {return false;}
                break;
            case KonstPortalUnterlagen.ANZEIGEN_EXTERNE_SEITE:
                if (pPortalUnterlagen.previewExterneSeite==1) {return false;}
                break;
            case KonstPortalUnterlagen.ANZEIGEN_UNTERLAGEN:
            case KonstPortalUnterlagen.ANZEIGEN_BOTSCHAFTEN:
                if (pPortalUnterlagen.previewIntern==1) {return false;}
                break;
            case KonstPortalUnterlagen.ANZEIGEN_PPORTAL:
                if (pPortalUnterlagen.previewUnterlagenbereich==1) {return false;}
                break;
             }
        }
        CaBug.druckeLog("B", logDrucken, 10);

        /*Prüfen, ob aktiv (und damit berechtigt) lt. Unterlagengruppe*/
        int aktivLtGruppe = pPortalUnterlagen.aktivLtGruppe();
        //@formatter:off
        if (KonstPortalUnterlagen.pruefeObOeffentlicheAnzeige(pAnzeigeseite)==false && KonstPortalUnterlagen.pruefeObMitgliederportalAnzeige(pAnzeigeseite)==false) {
            switch (aktivLtGruppe) {
            case 1:return tAuswahlSession.isUnterlagenGruppe1Angeboten();
            case 2:return tAuswahlSession.isUnterlagenGruppe2Angeboten();
            case 3:return tAuswahlSession.isUnterlagenGruppe3Angeboten();
            case 4:return tAuswahlSession.isUnterlagenGruppe4Angeboten();
            case 5:return tAuswahlSession.isUnterlagenGruppe5Angeboten();
            default:/*0, dann individuell überprüfen*/
            }
        }

        /*Manuell deaktiviert*/
        if (pPortalUnterlagen.inaktivVorrangig()) {
            CaBug.druckeLog("Manuell deaktiviert", logDrucken, 10);
            return false;
        }

        CaBug.druckeLog("C", logDrucken, 10);

        /*Hier: Manuell aktiviert. Falls öffentlich oder Mitgliederportal, dann zulässig, sonst überprüfen der Berechtigung*/
        if (KonstPortalUnterlagen.pruefeObOeffentlicheAnzeige(pAnzeigeseite) || KonstPortalUnterlagen.pruefeObMitgliederportalAnzeige(pAnzeigeseite)) {
            return true;
        }
        CaBug.druckeLog("D", logDrucken, 10);
        
        /*Berechtigung - über Aktionärsstatus?*/
        if (pPortalUnterlagen.berechtigtAktionaer) {
            if (pruefeObAktionaer()) { return true;}
        }
        if (pPortalUnterlagen.berechtigtAngemeldeterAktionaer) {
            if (pruefeObAngemeldeterAktionaer()) {return true; }
        }
        if (pPortalUnterlagen.berechtigtOnlineTeilnahmeAktionaer) {
            if (pruefeObOnlineTeilnehmenderAktionaer()) {return true;}
        }
        CaBug.druckeLog("E", logDrucken, 10);

        /*Berechtigung - über Gastgruppe?*/
        if (pruefeGastN(pPortalUnterlagen.berechtigtGast1, KonstPortalFunktionen.gast1)) { return true; }
        if (pruefeGastN(pPortalUnterlagen.berechtigtGast2, KonstPortalFunktionen.gast2)) { return true; }
        if (pruefeGastN(pPortalUnterlagen.berechtigtGast3, KonstPortalFunktionen.gast3)) { return true; }
        if (pruefeGastN(pPortalUnterlagen.berechtigtGast4, KonstPortalFunktionen.gast4)) { return true; }
        if (pruefeGastN(pPortalUnterlagen.berechtigtGast5, KonstPortalFunktionen.gast5)) { return true; }
        if (pruefeGastN(pPortalUnterlagen.berechtigtGast6, KonstPortalFunktionen.gast6)) { return true; }
        if (pruefeGastN(pPortalUnterlagen.berechtigtGast7, KonstPortalFunktionen.gast7)) { return true; }
        if (pruefeGastN(pPortalUnterlagen.berechtigtGast8, KonstPortalFunktionen.gast8)) { return true; }
        if (pruefeGastN(pPortalUnterlagen.berechtigtGast9, KonstPortalFunktionen.gast9)) { return true; }
        if (pPortalUnterlagen.berechtigtGast10) {
            if (pruefeObGast()) {return true;}
        }

        if (pruefeOnlineTeilnehmenderGastN(pPortalUnterlagen.berechtigtGastOnlineTeilnahmer1, KonstPortalFunktionen.gast1)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(pPortalUnterlagen.berechtigtGastOnlineTeilnahmer2, KonstPortalFunktionen.gast2)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(pPortalUnterlagen.berechtigtGastOnlineTeilnahmer3, KonstPortalFunktionen.gast3)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(pPortalUnterlagen.berechtigtGastOnlineTeilnahmer4, KonstPortalFunktionen.gast4)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(pPortalUnterlagen.berechtigtGastOnlineTeilnahmer5, KonstPortalFunktionen.gast5)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(pPortalUnterlagen.berechtigtGastOnlineTeilnahmer6, KonstPortalFunktionen.gast6)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(pPortalUnterlagen.berechtigtGastOnlineTeilnahmer7, KonstPortalFunktionen.gast7)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(pPortalUnterlagen.berechtigtGastOnlineTeilnahmer8, KonstPortalFunktionen.gast8)) { return true; }
        if (pruefeOnlineTeilnehmenderGastN(pPortalUnterlagen.berechtigtGastOnlineTeilnahmer9, KonstPortalFunktionen.gast9)) { return true; }
        if (pPortalUnterlagen.berechtigtGastOnlineTeilnahmer10) {
            if (pruefeObOnlineTeilnehmenderGast()) {return true;}
        }
        CaBug.druckeLog("F", logDrucken, 10);

        //@formatter:on

        return false;
    }

    /**pAnzeigeSeite lt. KonstPortalUnterlagen.ANZEIGEN_
     * 
     * pUnterlagenSubBereich wird nur verwendet, wenn pUnterlagenbereich=KonstPortalUnterlagen.ANZEIGEN_PPORTAL
     * In diesem Fall wird dann funktionscodeSub des ausgewählten Menüeintrages hierfür verwendet.
     * */
    public List<EclPortalUnterlagenM> erzeugeUnterlagenliste(int pAnzeigeseite, int pUnterlagenSubBereich) {
        CaBug.druckeLog("pAnzeigeseite="+pAnzeigeseite, logDrucken, 10);
        if (!KonstPortalUnterlagen.pruefeObOeffentlicheAnzeige(pAnzeigeseite)) {
            initWerte();
        }
        else {
            initZeit();
        }

        List<EclPortalUnterlagenM> portalUnterlagenMListe = new LinkedList<EclPortalUnterlagenM>();
        List<EclPortalUnterlagen> portalUnterlagenListe = eclParamM.getParam().paramPortal.eclPortalUnterlagen;

        for (int i = 0; i < portalUnterlagenListe.size(); i++) {
            EclPortalUnterlagen lPortalUnterlage=portalUnterlagenListe.get(i);
            if (pruefenObEintragenUnterlagen(lPortalUnterlage, pAnzeigeseite, pUnterlagenSubBereich)) {
                
                EclPortalUnterlagenM neuePortalUnterlagenM=null;
                
                if (lPortalUnterlage.art==KonstPortalUnterlagen.ART_VERWEIS) {
                    /**Wird gefüllt, wenn bei der Aufbereitung ein Fehler aufgetreten ist*/
                    String lFehlertext="";
                    int verweisAufUnterlage=-1;
                    int zielUnterlageOffset=-1;
                    EclPortalUnterlagen lPortalUnterlageZiel=null;
                    if (CaString.isNummern(lPortalUnterlage.dateiname)==true) {
                        verweisAufUnterlage=Integer.parseInt(lPortalUnterlage.dateiname);
                        if (verweisAufUnterlage>=0) {
                            for (int i1=0;i1<portalUnterlagenListe.size(); i1++) {
                                if (portalUnterlagenListe.get(i1).ident==verweisAufUnterlage) {
                                    zielUnterlageOffset=i1;
                                }
                            }
                            if (zielUnterlageOffset>=0) {
                                if (portalUnterlagenListe.get(zielUnterlageOffset).art!=KonstPortalUnterlagen.ART_VERWEIS) {
                                    lPortalUnterlageZiel=portalUnterlagenListe.get(zielUnterlageOffset);
                                }
                                else {
                                    lFehlertext="Unzulässiger Verweis - Ziel ist wieder ein Verweis";
                                }
                            }
                            else {
                                lFehlertext="Unzulässiger Verweis - Ziel nicht gefunden";
                            }
                        }
                        else {
                            lFehlertext="Unzulässiger Verweis - falsche Zahl";
                        }
                                
                    }
                    else {
                        lFehlertext="Unzulässiger Verweis - keine Ziffern";
                    }
                    
                    
                    if (lFehlertext.isEmpty()) {
                        /*Kein Fehler aufgetreten*/
                        neuePortalUnterlagenM = new EclPortalUnterlagenM(lPortalUnterlage, lPortalUnterlageZiel);
                    }
                    else {
                        /*Fehler aufgetreten - diesen vorrangig anzeigen*/
                        lFehlertext="UnterlageIdent="+Integer.toString(lPortalUnterlage.ident)+" "+lFehlertext;
                        if (zielUnterlageOffset!=-1) {
                            lFehlertext+=" Ziel-Ident="+Integer.toString(zielUnterlageOffset);
                        }
                        lPortalUnterlage.art=KonstPortalUnterlagen.ART_TEXT;
                        lPortalUnterlage.bezeichnungDE=lFehlertext;
                        lPortalUnterlage.bezeichnungEN=lFehlertext;
                        neuePortalUnterlagenM = new EclPortalUnterlagenM(lPortalUnterlage);
                    }
                }
                else {
                    neuePortalUnterlagenM = new EclPortalUnterlagenM(lPortalUnterlage);
                }
                portalUnterlagenMListe.add(neuePortalUnterlagenM);
            }
        }

        /*Nun noch sortieren*/
        Comparator <EclPortalUnterlagenM> comp=null;
        switch (pAnzeigeseite) {
        case KonstPortalUnterlagen.ANZEIGEN_LOGIN_UNTENODEROBEN:
        case KonstPortalUnterlagen.ANZEIGEN_LOGIN_OBEN:
            comp=new EclPortalUnterlagenMCompLoginOben();
            break;
        case KonstPortalUnterlagen.ANZEIGEN_LOGIN_UNTEN:
            comp=new EclPortalUnterlagenMCompLoginUnten();
            break;
        case KonstPortalUnterlagen.ANZEIGEN_EXTERNE_SEITE:
            comp=new EclPortalUnterlagenMCompExterneSeite();
            break;
        case KonstPortalUnterlagen.ANZEIGEN_UNTERLAGEN:
            comp=new EclPortalUnterlagenMCompUnterlagen();
            break;
        case KonstPortalUnterlagen.ANZEIGEN_BOTSCHAFTEN:
            comp=new EclPortalUnterlagenMCompBotschaften();
            break;
        case KonstPortalUnterlagen.ANZEIGEN_PPORTAL:
            comp=new EclPortalUnterlagenMCompPPortal();
            break;
       }
        
        Collections.sort(portalUnterlagenMListe, comp);

        return portalUnterlagenMListe;

    }

    /*******************Für Willenserklärungs-relevante Funktionen****************
     * Werden in tSession gespeichert*/
    public void belegeAktivFuerWillenserklaerungen() {
        initZeit();
        initPhase();
        belegeAktivFuerWillenserklaerungenAusfuehren();
    }

    /**Voraussetzung: initWerte und initPhase bereits ausgeführt*/
    private void belegeAktivFuerWillenserklaerungenAusfuehren() {
        tSession.setGewinnspielAktiv(aktuellePortalPhase.gewinnspielAktiv);
        tSession.setVorDerHVNachDerHV(aktuellePortalPhase.lfdVorDerHVNachDerHV);

        tSession.setHvPortalInBetrieb(aktuellePortalPhase.lfdHVPortalInBetrieb);
        if (aktuellePortalPhase.lfdHVPortalInBetrieb == false) {
            tSession.setPortalErstanmeldungIstMoeglich(false);
            tSession.setPortalEKIstMoeglich(false);
            tSession.setPortalSRVIstMoeglich(false);
            tSession.setPortalBriefwahlIstMoeglich(false);
            tSession.setPortalKIAVIstMoeglich(false);
            tSession.setPortalVollmachtDritteIstMoeglich(false);
        } else {
            tSession.setPortalErstanmeldungIstMoeglich(aktuellePortalPhase.lfdHVPortalErstanmeldungIstMoeglich & (eclParamM.liefereEKAngeboten() == 1 || eclParamM.getAnmeldenOhneWeitereWK()==1));
            tSession.setPortalEKIstMoeglich(aktuellePortalPhase.lfdHVPortalEKIstMoeglich);
            tSession.setPortalSRVIstMoeglich(aktuellePortalPhase.lfdHVPortalSRVIstMoeglich & (eclParamM.getParam().paramPortal.srvAngeboten == 1));
            tSession.setPortalBriefwahlIstMoeglich(aktuellePortalPhase.lfdHVPortalBriefwahlIstMoeglich & (eclParamM.getParam().paramPortal.briefwahlAngeboten == 1));
            tSession.setPortalKIAVIstMoeglich(aktuellePortalPhase.lfdHVPortalKIAVIstMoeglich & (eclParamM.getParam().paramPortal.vollmachtKIAVAngeboten == 1));
            tSession.setPortalVollmachtDritteIstMoeglich(aktuellePortalPhase.lfdHVPortalVollmachtDritteIstMoeglich & (eclParamM.getParam().paramPortal.vollmachtDritteAngeboten == 1));

        }

        tSession.setAktuellePortalPhaseNr(aktuellePortalPhaseNr);
    }

    /*******************Globale Funktion - muß jedesmal aufgerufen werden, bevor überprüft wird ob eine Portalfunktion / Willenserklärung noch zur Verfügung steht******
     * Unterlagen im Detail (sprich je Unterlage) werden dabei nicht geprüft - das muß separat gemacht werden*/
    public void belegePortalFunktionenWillenserklaerungenStatusAktiv() {
        CaBug.druckeLog("belegePortalFunktionenWillenserklaerungenStatusAktiv", logDrucken, 3);

        initWerte();
        initPhase();
        belegeAngebotenAktivFuerAuswahlAusfuehren();
        belegeAktivFuerWillenserklaerungenAusfuehren();
    }

    public EclPortalPhase getAktuellePortalPhase() {
        return aktuellePortalPhase;
    }

    /**
     * Liest ein, wieviel Fragen von der aktuellen Kennung gestellt wurden.
     * Muß aufgerufen werden, bevor angeboten/berechtigt für Portalfunktionen
     * belegt wird, da dort für Rückfragen diese Info benötigt wird.
     * Datenbankzugriff erfolgt hier nur, wenn Parameter "Rückfragen möglich"
     * gestellt ist.
     * 
     * Benötigt offene dbWeitere.
     * 
     * Da diese Methode als einzige in dieser Klasse pDbBundle benötigt,
     * wird dies hier als Parameter übergeben und nicht als Bean-Injection,
     * um sicherzustellen dass auch tatsächlich pDbBundle geöffnet ist.
     */
    public void belegeAnzahlFragenGestellt(DbBundle pDbBundle) {
        if (eclParamM.getParam().paramPortal.fragenRueckfragenErmoeglichen==1) {
            pDbBundle.dbMitteilung.setzeFunktion(KonstPortalFunktionen.fragen);
            int anz=pDbBundle.dbMitteilung.readAll_loginIdent(eclLoginDatenM.getEclLoginDaten().ident);
            if (anz>0) {
                tAuswahlSession.setFragenWurdenGestellt(true);
            }
            else {
                tAuswahlSession.setFragenWurdenGestellt(false);
            }
        }
        else {
            tAuswahlSession.setFragenWurdenGestellt(false);
        }
        
    }
}
