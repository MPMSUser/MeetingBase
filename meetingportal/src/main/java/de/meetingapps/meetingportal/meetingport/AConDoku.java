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
package de.meetingapps.meetingportal.meetingport;

/**Nur zur Dokumentation der Text-Logik auf den einzelnen Seiten! App und Portal
 * 
 * 
  *
 */

public class AConDoku {

    public void allgemeineTexte() {
    }

    /**Allgemeine Texte, die auf verschiedenen Seiten verwendet werden 
     * (35) Logout
     * 
     * TODO _PortalTexte In App noch auzfnehmen. Außerdem zwei Texte, je nachdem ob Datum vorhanden oder nicht 
     * TODO _PortalTexte Auch Variable für "Datum Stand Aktienregister" einführen!
     * (531-0/31) Text vor den Aktionärsdaten ("Sie sind ... eingetragen") / siehe auch nurFuerAppBestandsübersicht
     * (492-0/4) Aktionärsnummer
     * (494-0/6) Aktien - Bestand 
     * 
     * Spezialtext:
     * (550-0/30) Nur App: Für Header: wenn gefüllt, dann wird der enthaltene Text im Kopfbereich anstelle der Standard-Mandanten-Daten angezeigt.
     * 
     *  
     *  
     *  
     *  Spezial-Texte - frei im Portal verwendbar, in App nicht zur Verfügung
     *  (532) ku164
     *  (533) ku164
     *  (534) ku164
     *  (535) ku164
     *  (536) ku164
     *  
     *  (567) Scout 24: Gesamtmarkierungs-Text für Briefwahl
     *  (568) Scout 24: Text zwischen Gesamtmarkierung und Weisung
     *  (569) Scout 24: Text zwischen Gesamtmarkierung und Briefwahl
     * */

    public void nurInternNurApp() {
    }

    /**========================================Nur intern (nur App)====================================================*/

    /**AppAuswahl
     * ==========
     * Nur Testbetrieb - Auswahl der verschiedenen Apps (alte App, Neue App etc.)
     */

    /**========================================Mandantenabhängig======================================================*/

    public void login() {
        aLogin();
    }

    private void aLogin() {
    }

    /**aLogin
     * ======
     * Login-Seite im Portal. Die Text dieser Seite werden in der App auf div. Login-Seiten verwendet (siehe weiter unten)
     * 
     * (1-/) Titel Überschrift
     * (3-15/3) Überschrift
     * (4-15/8) Text, was mit dem Internetservice alles möglich ist
     * 
     * Falls App-Install-buttons angezeigt werden sollen (paramPortal.appInstallButtonsAnzeigen=01):
     * (199) Text, dass auch die App genutzt werden kann
     * (198) Button "ios-App installieren"
     * (197) Button "android-App installieren"
     * 
     * (2-15/9) Text-Hinweis zum Einloggen (App: Manueller Vorgang, d.h. Kennung und Passwort eingeben)
     * (5-15/4) Feldbezeichnung Kennung
     * (6-15/5) Feldbezeichnung Passwort
     * 
     * Falls Passwort-Vergessen-Verfahren aktiv (paramPortal.verfahrenPasswortVergessen!=0):
     * (9-15/10) Text mit Hinweis
     * (10-15/7) Link-Beschriftung Passwort vergessen (App: Button-Beschriftung)
     * 
     * (7-15/11) Ende-Text
     * (8-15/6) Button "Login"
     * 
     * Handhabung in der App:
     * ======================
     * Im folgenden ist die derzeit "recommended" Version empfohlen. Weitere Varianten sind konfigurierbar, aber eher aus
     * historischen Gründen enthalten.
     * Alles in AppBesitzHinzufuegen enthalten.
     * 
     * Startmaske
     * ----------
     * Maske 12: Auswahl, ob gescannt werden soll oder manueller Login durchgeführt werden soll
     * Mandantenunabhängig.
     * 
     * Login über Scanvorgang:
     * -----------------------
     * Maske "Scannen": keine Texte.
     * 
     * Maske 14: Scann-Daten anzeigen und bestätigen, d.h. dann Login durchführen
     * 
     * Maske 13: Passwort nach Scannen ergänzen, wenn im Scan falsches Passwort enthalten ist
     * Hinweis: ist derzeit noch mandantenunabhängig. Siehe ToDos ...
     * 
     * Login manuell durchführen (nach Auswahl des Mandanten):
     * -------------------------------------------------------
     * Maske 17 (falls Mandant "Sonstige" ausgewählt): HV-Code eingeben.
     * Mandantenunabhängig
     * 
     * Maske 15: manuell Login-Daten eingeben (ggf. einschließlich Passwort-Vergessen-Verfahren)
     * 
     * Nun im Detail:
     * Maske 14, Scan-Daten bestätigen: Verwendet Texte von Maske 15 (siehe auch oben aLogin)!
     * (226-15/12) Text: bitte gescannte Daten überprüfen und bestätigen
     * (5-15/4) Feldbezeichnung Kennung
     * (6-15/5) Feldbezeichnung Passwort
     * (8-15/6) Button "Login"
     * (3-15/3) Überschrift
     * (4-15/8) Text, was mit dem Internetservice alles möglich ist
     * (7-15/11) Ende-Text
     * 
     * (549-15/13) "Spezialtext". Ist dieser leer, dann werden vom System die Gesellschaftsdaten standardmäßig angezeigt.
     * Wenn dieser Text gefüllt ist, werden die Standard-Gesellschaftsdaten nicht angezeigt, sondern nur dieser Text
     * 
     * Maske 15, manuell Login-Daten eingeben:
     * (5-15/4) Feldbezeichnung Kennung
     * (6-15/5) Feldbezeichnung Passwort
     * (8-15/6) Button "Login"
     * 
     * Falls Passwort-Vergessen-Verfahren:
     * (10-15/7) Link-Beschriftung Passwort vergessen (App: Button-Beschriftung)
     * 
     * (4-15/8) Text, was mit dem Internetservice alles möglich ist
     * (2-15/9) Text-Hinweis zum Einloggen (App: Manueller Vorgang, d.h. Kennung und Passwort eingeben)
     * 
     * Falls Passwort-Vergessen-Verfahren:
     * (9-15/10) Text mit Hinweis
     * 
     * (7-15/11) Ende-Text
     * 
     */

    public void erstanmeldung() {
        aAnmelden();
        aAnmeldenEK();
    }

    private void aAnmelden() {
    }

    /**aAnmelden - AppAnmelden
     * =======================
     * Erstanmeldung eines Aktionärs
     * 
     * (319-111/999) Titel (nur App)
     * (38-111/1) Überschrift
     * 
     * Falls Null-Bestand (nur App - dann alles andere nicht anzeigen):
     * (320-111/11) Text (aktuell: nur App)
     * 
     * Falls paramPortal.lfdHVPortalErstanmeldungIstMoeglich==1 (Anmeldephase ist aktiv):
     * (39-111/15) Text-Start
     * (537) Text vor Button EK (nicht im Standard)
     * (40-111/2) Button Anmelden mit EK
     * (538) Text vor Button Briefwahl (nicht im Standard)
     * (41-111/3) Button Anmelden mit Briefwahl
     * (539) Text vor Button SRV (nicht im Standard)
     * (42-111/4) Button Anmelden SRV
     * (540) Text vor Button KIAV (nicht im Standard)
     * (321-111/5) Button Anmelden KIAV
     * (43) Button "Einstellungen" (nur Portal)
     * (44-111/13) Text-Ende
     * 
     * Falls paramPortal.lfdHVPortalErstanmeldungIstMoeglich!=1 (Anmeldephase nicht aktiv):
     * 		Falls paramPortal.lfdHVPortalInBetrieb!=1 (HV-Portal-Teil ist überhaupt nicht in Betrieb - nur dauerhafter Teil vorhanden!)
     * 		(45-111/14) Text-Start
     * 		(43) Button "Einstellungen" (nur Portal)
     * 		Falls paramPortal.lfdHVPortalInBetrieb==1 (HV-Portal ist noch in Betrieb, aber keine Anmeldungen mehr möglich)
     * 		(318-111/16/) Text-Start
     * 		(43) Button "Einstellungen" (nur Portal)
     * 
     * (35) Button Abmelden (nur Portal)
     * (322-111/12) Button "Einstellungen/rechtliches" (nur App)
     */

    private void aAnmeldenEK() {
    }

    /**aAnmeldenEK - AppAnmeldenEK
     * ===========================
     * Erstanmeldung eines Aktionärs - Eintrittskartenausstellung, Auswahl welche Eintrittskarte ausgestellt werden soll 
     * 
     * (323-151/999) Titel (nur App)
     * (46-151/1) Überschrift
     * (47-151/12) Text-Start
     * 
     * (Achtung- folgender Mittelteil identische Nummern mit
     * aAnmeldenEK)
     * (324-151/2) Text 
     * (48-151/3) Button Eine EK Selbst
     * 
     * (325-151/4) Text
     * (49-151/5) Button Zwei EK Selbst
     * 
     * (326-151/6) Text
     * (50-151/7) Button EineEK mit Vollmacht
     * 
     * (327-151/8) Text
     * (51-151/9) Button Zwei EK mit oder ohne Bevollmächtigte
     * 
     * (328-151/10) Text
     * (52-151/11) Button Zwei EK für Personengemeinschaft
     * (Mittelteil Ende)
     * 
     * (53-151/13) Text-Ende
     * (54) Button Zurück (nur Portal)
     * (35) Button Abmelden (nur Portal)
     */

    public void weitereWillenserklaerung() {
        aStatus();
        aStatus0Bestand();
        aNeueWillenserklaerung();
        aNeueWillenserklaerungEK();
    }

    private void aStatus() {
    }

    /**aStatus / AppStatus
     * ===================
     * 
     * (475-115/999) Titel (nur App)
     * 
     * HV-Portal in Betrieb (paramPortal.lfdHVPortalHVInBetrieb==1):
     * (127-115/5) Überschrift
     * (476-115/22) Start
     * 
     * Falls Anmeldungen für diesen Aktionär vorhanden:
     * (128-115/7) Text Start
     * 		Für alle Anmeldungen:
     * 		Aktionärsdaten (fest vom System)
     * 		(129-115/8) Status angemeldet (immer)
     * 		(130-115/23) Status Präsent (nur, wenn tatsächlich präsent ist)
     * 		(131-115/9) Feldbezeichnung Aktien (Achtung: Wert darf hier nicht als Variable verwendet werden - 
     * 			technisch nicht möglich! Wird vom Programm anschließend - mit Blank getrennt - angezeigt)
     * 		(132-115/10) Button "Weitere Willenserklärung" (so für diese Anmeldung möglich)
     * 		Für alle Willenserklärungen dieser Anmeldung:
     * 			Vom Programm generierte Textzeilen
     * 			Falls keine Willenserklärungen:
     * 				(134,115/11) "Blank" dann Anzahl Aktien (vom Programm vorgegeben) "Blank" (140,115/24) "Blank" (135-115/25)
     * 			(136-115/12) Button Stornieren
     * 			(137-115/13) Button Ändern
     * 			(138-115/14) Button Detailanzeige
     * 			
     * 
     * 
     * HV-Portal nicht in Betrieb (paramPortal.lfdHVPortalHVInBetrieb!=1):
     * 	Je nach paramPortalLfdVorDerHVNachDerHV:
     * 	0: (HV geschlossen während der HV)
     * 		(477-115/1) Überschrift
     * 		(478-115/26) Text
     *  1: (HV geschlossen vor der Anmeldephase)
     * 		(479-115/2) Überschrift
     * 		(480-115/27) Text
     *  2: (HV geschlossen nach der HV)
     * 		(481-115/3) Überschrift
     * 		(482-115/28) Text
     * 
     * Falls keine Briefwahl mehr möglich, aber sonst schon noch was (derzeit nicht im Standard) 
     * (eclZugeordneteMeldungListeM.briefwahlVorhanden==true and eclParameterM.pLfdHVPortalBriefwahlIstMoeglich!=1):
     * (551-115/29/) Text
     * 
     * Immer:
     * (483-115/20) Button "Weitere Funktionen" (falls solche Vorhanden) (nur App) (siehe auch 322 bei aAnmelden)
     * (139) Button Einstellungen (nur Portal)
     * (35) Button Logout
     * (484-115/21) Button "Fertig" (nur App)
     * 
     */

    private void aStatus0Bestand() {
    }

    private void aNeueWillenserklaerung() {
    }

    /**aNeueWillenserklaerung / AppNeueWillenserklaerung
     * =================================================
     * (142-116/999) Titel (nur App)
     * (143-116/1) Überschrift
     * (141-116/7) Text (mit <<<MeldungAktien>>>)
     * (542) Text vor Button EK (nicht im Standard)
     * (144-116/2) Button neue Eintrittskarte
     * (543) Text vor Button Briefwahl (nicht im Standard)
     * (145-116/3) Button Briefwahl
     * (544) Text vor Button SRV (nicht im Standard)
     * (146-116/4) Button Stimmrechtsvertreter
     * (545) Text vor Button KIAV (nicht im Standard)
     * (-116/5) Button KIAV
     * (43) Button "Einstellungen" (nur Portal)
     * (546) Text vor Button Vollmacht an Dritte (nicht im Standard)
     * (-116/6) Button Vollmacht an Dritte
     * 
     * (54) Button Zurück (nur Portal)
     * (35) Button Abmelden (nur Portal)
     */

    private void aNeueWillenserklaerungEK() {
    }

    /**aNeueWillenserklaerungEK / AppNeueWillenserklaerungEK
     * =====================================================
     * (149-151/998) Titel (nur App)
     * (147-151/14) Überschrift
     * (148-151/15) Text-Start (mit <<<MeldungAktien>>>)
     * 
     * (Achtung- folgender Mittelteil identische Nummern mit
     * aAnmeldenEK)
     * (324-151/2) Text 
     * (48-151/3) Button Eine EK Selbst
     * 
     * (325-151/4) Text
     * (49-151/5) Button Zwei EK Selbst
     * 
     * (326-151/6) Text
     * (50-151/7) Button EineEK mit Vollmacht
     * 
     * (327-151/8) Text
     * (51-151/9) Button Zwei EK mit oder ohne Bevollmächtigte
     * 
     * (328-151/10) Text
     * (52-151/11) Button Zwei EK für Personengemeinschaft
     * (Mittelteil Ende)
     * 
     * (474-151/16) Text-Ende
     * (54) Button Zurück (nur Portal)
     * (35) Button Abmelden (nur Portal)
     * 
     */

    public void eintrittskarten() {
        aEintrittskarte();
        aEintrittskarteDetail();
        aEintrittskarteQuittung();
        aEintrittskarteStornieren();
        aEintrittskarteStornierenQuittung();
    }

    private void aEintrittskarte() {
    }

    /**aEintrittskarte /AppEintrittskarte
     * ==================================
     * Ausstellen einer neuen Eintrittskarte (sowohl bei Erstanmeldung, als auch als neue Willenserklärung).
     * 
     * Auf dieser Seite wiederholen sich verschiedene Textblöcke öfters, die hier einmal aufgeführt sind:
     * 
     * ...............Bevollmächtigter: nur einer..................
     * (329-161/21) Text vor Bevollmächtigten
     * (75-161/24) Feld Vertreter Name
     * (76-161/27) Feld Vertreter Vorname
     * (77-161/30) Feld Vertreter Ort 
     * (359-161/33) Text nach Bevollmächtigtem
     * 
     * ...............Bevollmächtigter: erster von zwei..................
     * (330-161/22) Text vor Bevollmächtigten
     * (332-161/25) Feld Vertreter Name
     * (334-161/28) Feld Vertreter Vorname
     * (336-161/31) Feld Vertreter Ort 
     * (338-161/34) Text nach Bevollmächtigtem
     * 
     * ...............Bevollmächtigter: zweiter von zwei..................
     * (331-161/23) Text vor Bevollmächtigten
     * (333-161/26) Feld Vertreter Name
     * (335-161/29) Feld Vertreter Vorname
     * (337-161/32) Feld Vertreter Ort 
     * (339-161/35) Text nach Bevollmächtigtem
     * 
     * ..............Versandart: nur eine EK............
     * (78-161/41) Text vor Auswahl
     * (66-161/44) Auswahl: Online-Ausdruck der EK (Portal) bzw. Hinterlegung der EK in der App (App)
     * (67-161/47) Auswahl: Versand Online-Ticket an E-Mail
     * 		(70-161/56) Feld E-Mail-Adresse
     * 		(71-161/65) Feld E-Mail-Adresse bestätigen
     * (68-161/50) Auswahl Versand an Aktienregister-Adresse
     * (69-161/53) Auswahl abweichende Versand-Adresse
     * 		(72-161/59) Felder abweichende Versandadresse
     * 		(73-161/62) Warnhinweis - keine Vollmacht für diese Adresse
     * 
     * ..............Versandart: zwei EK, aber nur 1 gemeinsame Versandart............
     * (361-161/81) Text vor Auswahl
     * (362-161/84) Auswahl: Online-Ausdruck der EK (Portal) bzw. Hinterlegung der EK in der App (App)
     * (363-161/87) Auswahl: Versand Online-Ticket an E-Mail
     * 		(364-161/96) Feld E-Mail-Adresse
     * 		(365-161/105) Feld E-Mail-Adresse bestätigen
     * (366-161/90) Auswahl Versand an Aktienregister-Adresse
     * (367-161/93) Auswahl abweichende Versand-Adresse
     * 		(368-161/99) Felder abweichende Versandadresse
     * 		(369-161/102) Warnhinweis - keine Vollmacht für diese Adresse
     * 
     * .........Versandart: erste EK von zwei........
     * (340-161/42) Text vor Auswahl
     * (342-161/45) Auswahl: Online-Ausdruck der EK (Portal) bzw. Hinterlegung der EK in der App (App)
     * (344-161/48) Auswahl: Versand Online-Ticket an E-Mail
     * 		(346-161/57) Feld E-Mail-Adresse
     * 		(348-161/66) Feld E-Mail-Adresse bestätigen
     * (350-161/51) Auswahl Versand an Aktienregister-Adresse
     * (352-161/54) Auswahl abweichende Versand-Adresse
     * 		(354-161/60) Felder abweichende Versandadresse
     * 		(357-161/63) Warnhinweis - keine Vollmacht für diese Adresse
     * 
     * .........Versandart: zweite EK von zwei........
     * (341-161/43) Text vor Auswahl
     * (343-161/46) Auswahl: Online-Ausdruck der EK (Portal) bzw. Hinterlegung der EK in der App (App)
     * (345-161/49) Auswahl: Versand Online-Ticket an E-Mail
     * 		(347-161/58) Feld E-Mail-Adresse
     * 		(349-161/67) Feld E-Mail-Adresse bestätigen
     * (351-161/52) Auswahl Versand an Aktienregister-Adresse
     * (353-161/55) Auswahl abweichende Versand-Adresse
     * 		(355-161/61) Felder abweichende Versandadresse
     * 		(358-161/64) Warnhinweis - keine Vollmacht für diese Adresse
     * 
     * 
     * 
     * Hier nun der eigentliche Aufbau
     * -------------------------------
     * -------------------------------
     * Gemeinsamer Start-Bereich:
     *(356-161/999) Titel (nur App)
     *
     * ausgewaehlteAktion==1 - eine EK selbst:
     * ---------------------------------------
     * (56-161/1) Überschrift (abhängig von Aktion)
     * (360-161/2) Text - Start (abhängig von Aktion)
     * (61-161/75) Text - Start
     * Block "Versandart: nur eine EK"
     * 
     * ausgewaehlteAktion==2 - 2 EK Selbst für Personengemeinschaft:
     * -------------------------------------------------------------
     * (60-161/71) Überschrift (abhängig von Aktion)
     * (65-161/3) Text - Start (abhängig von Aktion)
     * (siehe oben) Text - Start
     * Block "Versandart: erste EK von zwei"
     * Block "Versandart: zweite EK von zwei"
     * 
     * ausgewaehlteAktion==3 - 1 EK für Bevollmächtigten:
     * -------------------------------------------------------------
     * (58-161/72) Überschrift (abhängig von Aktion)
     * (63-161/4) Text - Start (abhängig von Aktion)
     * (siehe oben) Text - Start
     * Block "Bevollmächtigter: nur einer"
     * Block "Versandart: nur eine EK"
     * 
     * ausgewaehlteAktion==28 - 2EK mit oder ohne Vollmacht
     * -------------------------------------------------------------
     * (59-161/73) Überschrift (abhängig von Aktion)
     * (64-161/5) Text - Start (abhängig von Aktion)
     * (siehe oben) Text - Start
     * Block "Bevollmächtigter: erster von zwei"
     * Block "Versandart: erste EK von zwei"
     * Block "Bevollmächtigter: zweiter von zwei"
     * Block "Versandart: zweite EK von zwei"
     * 
     * ausgewaehlteAktion==30 - 2 EK selbst:
     * ---------------------------------------
     * (57-161/74) Überschrift (abhängig von Aktion)
     * (62-161/6) Text - Start (abhängig von Aktion)
     * (siehe oben) Text - Start
     * Block "Versandart: zwei EK, aber nur 1 gemeinsame Versandart"
     * 
     * 
     * 
     * Gemeinsamer Ende-Bereich
     * (74-161/7) Button "Ausstellen"
     * (54) Button Zurück (nur Portal)
     * (35) Button Abmelden (nur Portal)
     * 
     */

    private void aEintrittskarteQuittung() {
    }

    /**aEintrittskarteQuittung
     * =======================
     * Auf dieser Seite wiederholen sich verschiedene Textblöcke öfters, die hier einmal aufgeführt sind:
     * 
     * ...............Bevollmächtigter: nur einer..................
     * (370-162/31) Text vor Bevollmächtigten
     * (371-162/41) Text nach Bevollmächtigtem
     * 
     * ...............Bevollmächtigter: erster von zwei..................
     * (372-162/32) Text vor Bevollmächtigten
     * (373-162/42) Text nach Bevollmächtigtem
     * 
     * ...............Bevollmächtigter: zweiter von zwei..................
     * (374-162/33) Text vor Bevollmächtigten
     * (375-162/43) Text nach Bevollmächtigtem
     * 
     * ..............Versandart: nur eine EK............
     * (376-162/51) Text: Online-Ausdruck der EK (Portal) bzw. Hinterlegung der EK in der App (App)
     * (377-162/61) Button: EK erneut drucken (nur Portal)
     * 
     * (378-162/71) Text: Versand Online-Ticket an E-Mail <<<MailEK1>>>
     * (379-162/81) Button: EK erneut Mailen
     * 
     * (380-162/91) Text Versand an Aktienregister-Adresse, 
     * 
     * (381-162/101) Text abweichende Versand-Adresse, danach Versandadresse
     * 
     * ..............Versandart: zwei EK, aber nur 1 gemeinsame Versandart............
     * (382-162/52) Text: Online-Ausdruck der EK (Portal) bzw. Hinterlegung der EK in der App (App)
     * (383-162/62) Button: EK1 erneut drucken (nur Portal)
     * (384-162/112) Button: EK2 erneut drucken (nur Portal)
     * 
     * (385-162/72) Text: Versand Online-Ticket an E-Mail <<<MailEK1>>>
     * (386-162/82) Button: EK 1erneut Mailen 
     * (387-162/122) Button: EK 2 erneut Mailen
     * 
     * (388-162/92) Text Versand an Aktienregister-Adresse, 
     * 
     * (389-162/102) Text abweichende Versand-Adresse, danach Versandadresse
     * 
     * .........Versandart: erste EK von zwei........
     * (390-162/53) Text: Online-Ausdruck der EK (Portal) bzw. Hinterlegung der EK in der App (App)
     * (391-162/63) Button: EK erneut drucken (nur Portal)
     * 
     * (392-162/73) Text: Versand Online-Ticket an E-Mail <<<MailEK1>>>
     * (393-162/83) Button: EK erneut Mailen
     * 
     * (394-162/93) Text Versand an Aktienregister-Adresse, 
     * 
     * (395-162/103) Text abweichende Versand-Adresse, danach Versandadresse
     * 
     * .........Versandart: zweite EK von zwei........
     * (396-162/54) Text: Online-Ausdruck der EK (Portal) bzw. Hinterlegung der EK in der App (App)
     * (397-162/64) Button: EK erneut drucken (nur Portal)
     * 
     * (398-162/74) Text: Versand Online-Ticket an E-Mail <<<MailEK2>>>
     * (399-162/84) Button: EK erneut Mailen
     * 
     * (400-162/94) Text Versand an Aktienregister-Adresse, 
     * 
     * (401-162/104) Text abweichende Versand-Adresse, danach Versandadresse
     * 
     * 
     * 
     * Frei werden dann:
     * 82 bis 116
     * 
     * Hier nun der eigentliche Aufbau
     * -------------------------------
     * -------------------------------
     * Gemeinsamer Start-Bereich:
     *(402-162/999) Titel (nur App)
     *
     * ausgewaehlteAktion==1 - eine EK selbst:
     * ---------------------------------------
     * (79-162/1) Überschrift (abhängig von Aktion)
     * (80-162/21) Text - Start Erstanmeldung
     * (541-162/8) Text - Start Zusätzliche Erklärung (also keine Erstanmeldung)
     * (403-162/11) Text - Start (abhängig von Aktion)
     * (81-162/22) Text - Start
     * Block "Versandart: nur eine EK"
     * 
     * ausgewaehlteAktion==2 - 2 EK Selbst für Personengemeinschaft:
     * -------------------------------------------------------------
     * (404-162/2) Überschrift (abhängig von Aktion)
     * (siehe oben) Text - Start Erstanmeldung
     * (siehe oben) Text - Start Zusätzliche Erklärung
     * (405-162/12) Text - Start (abhängig von Aktion)
     * (siehe oben) Text - Start
     * Block "Versandart: erste EK von zwei"
     * Block "Versandart: zweite EK von zwei"
     * 
     * ausgewaehlteAktion==3 - 1 EK für Bevollmächtigten:
     * -------------------------------------------------------------
     * (406-162/3) Überschrift (abhängig von Aktion)
     * (siehe oben) Text - Start Erstanmeldung
     * (siehe oben) Text - Start Zusätzliche Erklärung
     * (407-162/13) Text - Start (abhängig von Aktion)
     * (siehe oben) Text - Start
     * Block "Bevollmächtigter: nur einer"
     * Block "Versandart: nur eine EK"
     * 
     * ausgewaehlteAktion==28 - 2EK mit oder ohne Vollmacht
     * -------------------------------------------------------------
     * (408-162/4) Überschrift (abhängig von Aktion)
     * (siehe oben) Text - Start Erstanmeldung
     * (siehe oben) Text - Start Zusätzliche Erklärung
     * (409-162/14) Text - Start (abhängig von Aktion)
     * (siehe oben) Text - Start
     * Block "Bevollmächtigter: erster von zwei"
     * Block "Versandart: erste EK von zwei"
     * Block "Bevollmächtigter: zweiter von zwei"
     * Block "Versandart: zweite EK von zwei"
     * 
     * ausgewaehlteAktion==30 - 2 EK selbst:
     * ---------------------------------------
     * (410-162/5) Überschrift (abhängig von Aktion)
     * (siehe oben) Text - Start Erstanmeldung
     * (siehe oben) Text - Start Zusätzliche Erklärung
     * (411-162/15) Text - Start (abhängig von Aktion)
     * (siehe oben) Text - Start
     * Block "Versandart: zwei EK, aber nur 1 gemeinsame Versandart"
     * 
     * 
     * 
     * Gemeinsamer Ende-Bereich
     * (34-0/20) Button Weiter 
     * (35) Button Abmelden (nur Portal)
     * 
     * 
     * E-Mail-Texte für Eintrittskarten per Email:
     * (218) Betreff
     * (219) Inhalt
     * 
     * 
     */

    private void aEintrittskarteDetail() {
    }

    /** aEintrittskarteDetail / AppEintrittskarteDetail
     * ==================================================
     * Anzeige einer bereits ausgestellten Eintrittskarte.
     * 
     * (412-165/999) Titel (App)
     * (117-165/1) Überschrift
     * (118-165/12) Start-Text
     * 
     * Je nach Art der Eintrittskarte:
     * 1 Eintrittskarte Selbst: (119-165/2)
     * 2 Eintrittskarte Vollmacht: (120-165/3)
     * 		Anschließend: Bevollmächtigter
     * 3 Eintrittskarte Gast: (126-165/4)
     * 
     * (121-165/5) Eintrittskartennummer Text + Nummer <<<EkNummer>>> (eclWillenserklaerungStatusM.zutrittsIdent bzw. App.eclWillenserklaerungStatusM.zutrittsIdent)
     * 
     * Je nach Versandart:
     * 1: An Aktienregistereintrag: (122-165/6)
     * 2: An abweichende Versandadresse: (123-165/7), danach die Adresszeilen
     * 3: Online-Druck: (124-165/8)
     * 			(485) Button "Drucken" (nur Portal)
     * 4: Per E-Mail (125-165/9), <<<MailEK1>>>
     * 			(485) Button "Drucken" (nur Portal) 
     * 			(107-165/10) Button "E-Mail"
     * 
     * (34-0/20) Button Weiter
     * (35) Button Abmelden (nur Portal) 
     */

    private void aEintrittskarteStornieren() {
    }

    /**aEintrittskarteStornieren / AppEintrittskarteStornieren
     * =======================================================
     * (413-163/999) Titel App
     * 
     * Falls Gastkarte (16):
     * (414-163/9) Überschrift
     * (416-163/2) Text
     * Generierter Text (EK-Beschreibung)
     * (417-163/5) End-Text
     * 
     * Falls Eintrittskarte Selbst (17):
     * (151-163/1) Überschrift
     * (153-163/3) Text
     * Generierter Text (EK-Beschreibung)
     * (418-163/6) End-Text
     * 
     * Falls Eintrittskarte Bevollmächtigter (18):
     * (152-163/10) Überschrift
     * (154-163/4) Text
     * Generierter Text (EK-Beschreibung)
     * (419-163/7) End-Text
     * 
     * (155-163/8) Button Stornieren
     * (54) Button zurück (nur Portal)
     * (35) Button abmelden (nur Portal)
     * 
     */

    private void aEintrittskarteStornierenQuittung() {
    }

    /**aEintrittskarteStornierenQuittung / AppEintrittskarteStornierenQuittung
     * =======================================================================
     * (420-164/999) Titel App
     * 
     * Falls Gastkarte (16):
     * (421-164/9) Überschrift
     * (422-164/2) Text
     * Generierter Text (EK-Beschreibung)
     * 
     * Falls Eintrittskarte Selbst (17):
     * (156-164/1) Überschrift
     * (158-164/3) Text
     * Generierter Text (EK-Beschreibung)
     * 
     * Falls Eintrittskarte Bevollmächtigter (18):
     * (157-164/10) Überschrift
     * (159-164/4) Text
     * Generierter Text (EK-Beschreibung)
     * 
     * (34-0/20) Button Stornieren
     * (35) Button abmelden (nur Portal)
     * 
     */

    public void weisungBriefwahl() {
        aWeisung();
        aWeisungAendern();
        aWeisungBestaetigen();
        aWeisungQuittung();
    }

    private void aWeisung() {
    }

    /**aWeisung
     * ========
     * (App: 121 auch für aWeisungAendern verwendet! Dort weitere Nummern!)
     * 
     * SRV(4)
     * (100-121/999) Titel (nur App)
     * (160-121/40) Überschrift
     * (162-121/21) Text
     * 
     * Briefwahl (5)
     * (101-121/998) Titel (nur App)
     * (161-121/41) Überschrift
     * (163-121/19) Text
     * 
     * Gesamtmarkierung:
     * (167-121/20) Text zur Gesamtmarkierung
     * (82-121/4) Button Gesamtmarkierung Ja
     * (83-121/5) Button Gesamtmarkierung Nein
     * (84-121/17) Button Gesamtmarkierung Enthaltung
     * (85-121/22) Button Gesamtmarkierung Löschen (anstelle Gesamtmarkierung Enthaltung - wenn Löschbutton!)
     * (102-121/2) Button Gesamtmarkierung im Sinne der Gesellschaft
     * (103-121/3) Button Gesamtmarkierung Gegen Sinne der Gesellschaft
     * 
     * "Weisungstabelle":
     * (547) Überschrift "Nr" (nicht im Standard)
     * (168-0/22) Überschrift "Agenda" (Allgemein)
     * (164-121/6) Überschrift Ja (App: Button Ja)
     * (165-121/7) Überschrift Nein (App: Button Nein)
     * (166-121/8) Überschrift Enthaltung (App: Button Enthaltung)
     * (86-121/9) Button "Löschen" (Einzelweisung)
     * 
     * SRV
     * (88-121/23) Text
     * Briefwahl
     * (89-121/24) Text
     * 
     * Gegenantragstext:
     * (90-121/25) 1: derzeit liegen keine Gegenanträge vor
     * (91-121/26) 2: Gegenanträge finden Sie ausschließlich ...
     * (92-121/27) 3: Gegenanträge können Sie im folgenden unterstützen ...
     * 
     * "Gegenantragstabelle":
     * (548) Überschrift "Nr" (nicht im Standard)
     * (93-0/23) Überschrift "Agenda" (Allgemein)
     * (94-121/13) Überschrift Ja (App: Button Ja)
     * (95-121/14) Überschrift Nein (App: Button Nein)
     * (96-121/15) Überschrift Enthaltung (App: Button Enthaltung)
     * (97-121/16) Button "Löschen" (Einzelweisung)
     * 
     * SRV
     * (98-121/28) Text Schluß
     * Briefwahl
     * (99-121/29) Text Schluß
     * 
     * (34-0/20) Button Weiter (falls Bestätigungsmaske folgt)
     * (455-121/36) Button "Erteilen" SRV (falls Quittungsmaske folgt)
     * (456-121/37) Button "Erteilen" Briefwahl (falls Quittungsmaske folgt)
     * (54) Button Zurück
     * (35) Button Abmelden
     */

    private void aWeisungAendern() {
    };

    /**aWeisungAendern
     * ===============
     * (App: 121 auch für aWeisung verwendet! Dort weitere Nummern!)
     * bis 39
     * SRV(10)
     * (447-121/997) Titel (nur App)
     * (193-121/1) Überschrift
     * (449-121/30) Text
     * 
     * Briefwahl (11)
     * (448-121/996) Titel (nur App)
     * (194-121/18) Überschrift
     * (450-121/31) Text
     * 
     * Gesamtmarkierung:
     * (167-121/20) Text zur Gesamtmarkierung
     * (82-121/4) Button Gesamtmarkierung Ja
     * (83-121/5) Button Gesamtmarkierung Nein
     * (84-121/17) Button Gesamtmarkierung Enthaltung
     * (85-121/22) Button Gesamtmarkierung Löschen (anstelle Gesamtmarkierung Enthaltung - wenn Löschbutton!)
     * (102-121/2) Button Gesamtmarkierung im Sinne der Gesellschaft
     * (103-121/3) Button Gesamtmarkierung Gegen Sinne der Gesellschaft
     * 
     * "Weisungstabelle" (identisch zu aWeisung):
     * (547) Überschrift "Nr" (nicht im Standard)
     * (168-0/22) Überschrift "Agenda" (Allgemein)
     * (164-121/6) Überschrift Ja (App: Button Ja)
     * (165-121/7) Überschrift Nein (App: Button Nein)
     * (166-121/8) Überschrift Enthaltung (App: Button Enthaltung)
     * (86-121/9) Button "Löschen" (Einzelweisung)
     * 
     * SRV
     * (451-121/32) Text
     * Briefwahl
     * (452-121/33) Text
     * 
     * Gegenantragstext (identisch zu aWeisung):
     * (90-121/25) 1: derzeit liegen keine Gegenanträge vor
     * (91-121/26) 2: Gegenanträge finden Sie ausschließlich ...
     * (92-121/27) 3: Gegenanträge können Sie im folgenden unterstützen ...
     * 
     * "Gegenantragstabelle" (identisch zu aWeisung):
     * (548) Überschrift "Nr" (nicht im Standard)
     * (93-0/23) Überschrift "Agenda" (Allgemein)
     * (94-121/13) Überschrift Ja (App: Button Ja)
     * (95-121/14) Überschrift Nein (App: Button Nein)
     * (96-121/15) Überschrift Enthaltung (App: Button Enthaltung)
     * (97-121/16) Button "Löschen" (Einzelweisung)
     * 
     * SRV
     * (453-121/34) Text Schluß
     * Briefwahl
     * (454-121/35) Text Schluß
     * 
     * (34-0/20) Button Weiter (falls Bestätigungsmaske folgt)
     * (457-121/38) Button "Erteilen" SRV (falls Quittungsmaske folgt)
     * (458-121/39) Button "Erteilen" Briefwahl (falls Quittungsmaske folgt)
     * (54) Button Zurück
     * (35) Button Abmelden
     */

    private void aWeisungBestaetigen() {
    };

    /**aWeisungBestaetigen
     * ===================
     * (auch verwendet für KIAV, Aktion 12 - derzeit nicht implementiert)
     * (App: nur noch Texte für Seite 123 erforderlich)
     * 
     * Ersterteilung SRV (4)
     * (169-123/1) Überschrift
     * (173-123/2) Text
     * (431-123/999) Titel
     * Ersterteilung Briefwahl (5)
     * (170-123/3) Überschrift
     * (111-123/4) Text
     * (432-123/998) Titel
     * Ändern SRV (10)
     * (171-123/5) Überschrift
     * (112-123/6) Text
     * (433-123/997) Titel
     * Ändern Briefwahl (11)
     * (172-123/7) Überschrift
     * (113-123/8) Text
     * (434-123/996) Titel
     * 
     * "Weisungstabelle" (wie aWeisungen):
     * (168-0/22) Überschrift "Agenda" (Allgemein)
     * (547) Überschrift "Nr" (nicht im Standard)
     * (104-0/24) "Ja" (Allgemein)
     * (105-0/25) "Nein" (Allgemein)
     * (106-0/26) "Enthaltung" (Allgemein)
     * 
     * Ersterteilung SRV (4)
     * (423-123/9) Text vor Gegenanträgen
     * Ersterteilung Briefwahl (5)
     * (424-123/10) Text vor Gegenanträgen
     * Ändern SRV (10)
     * (425-123/11) Text vor Gegenanträgen
     * Ändern Briefwahl (11)
     * (426-123/12) Text vor Gegenanträgen
     * 
     * "Gegenantragstabelle":
     * (548) Überschrift "Nr" (nicht im Standard)
     * (93-0/23) Überschrift "Gegenanträge" (Allgemein)
     * (108-0/27) "Ja" (Allgemein)
     * (109-0/28) "Nein" (Allgemein)
     * (110-0/29) "Enthaltung" (Allgemein)
     * 
     * Ersterteilung SRV (4)
     * (427-123/13) Text Ende
     * Ersterteilung Briefwahl (5)
     * (428-123/14) Text Ende
     * Ändern SRV (10)
     * (429-123/15) Text Ende
     * Ändern Briefwahl (11)
     * (430-123/16) Text Ende
     * 
     * Ersterteilung SRV (4)
     * (174-123/17) Button Erteilen
     * Ersterteilung Briefwahl (5)
     * (114-123/18) Button Erteilen
     * Ändern SRV (10)
     * (175-123/19) Button Speichern
     * Ändern Briefwahl (11)
     * (115-123/20) Button Speichern
     * 
     * Immer Buttons (nur Portal)
     * (54) Button Zurück
     * (35) Button Abmelden
     * 
     * 
     */

    private void aWeisungQuittung() {
    };

    /**aWeisungQuittung
     * ===================
     * (auch verwendet für KIAV, Aktion 12 - derzeit nicht implementiert)
     * (App: nur noch Texte für Seite 125 erforderlich)
     * 
     * Ersterteilung SRV (4)
     * (176-125/1) Überschrift
     * (180-125/2) Text
     * (435-125/999) Titel
     * Ersterteilung Briefwahl (5)
     * (177-125/3) Überschrift
     * (181-125/4) Text
     * (436-125/998) Titel
     * Ändern SRV (10)
     * (178-125/5) Überschrift
     * (182-125/6) Text
     * (437-125/997) Titel
     * Ändern Briefwahl (11)
     * (179-125/7) Überschrift
     * (183-125/8) Text
     * (438-125/996) Titel
     * 
     * "Weisungstabelle":
     * (547) Überschrift "Nr" (nicht im Standard)
     * (168-0/22) Überschrift "Agenda" (Allgemein)
     * (104-0/24) "Ja" (Allgemein)
     * (105-0/25) "Nein" (Allgemein)
     * (106-0/26) "Enthaltung" (Allgemein)
     * 
     * Ersterteilung SRV (4)
     * (439-125/9) Text vor Gegenanträgen
     * Ersterteilung Briefwahl (5)
     * (440-125/10) Text vor Gegenanträgen
     * Ändern SRV (10)
     * (441-125/11) Text vor Gegenanträgen
     * Ändern Briefwahl (11)
     * (442-125/12) Text vor Gegenanträgen
     * 
     * "Gegenantragstabelle":
     * (548) Überschrift "Nr" (nicht im Standard)
     * (93-0/23) Überschrift "Gegenanträge" (Allgemein)
     * (108-0/27) "Ja" (Allgemein)
     * (109-0/28) "Nein" (Allgemein)
     * (110-0/29) "Enthaltung" (Allgemein)
     * 
     * Ersterteilung SRV (4)
     * (443-125/13) Text Ende
     * Ersterteilung Briefwahl (5)
     * (444-125/14) Text Ende
     * Ändern SRV (10)
     * (445-125/15) Text Ende
     * Ändern Briefwahl (11)
     * (446-125/16) Text Ende
     * 
     * Immer Buttons (nur Portal)
     * (34-0/20) Weiter
     * (54) Button Zurück
     * (35) Button Abmelden
     * 
     * 
     */

    /**aWeisungStornieren
     * ===================
     * (auch verwendet für KIAV, Aktion 15 - derzeit nicht implementiert)
     * (App: nur noch Texte für Seite 136 erforderlich)
     * 
     * Storno SRV (13)
     * (184-136/1) Überschrift
     * (186-136/2) Text
     * (459-136/999) Titel
     * Storno Briefwahl (14)
     * (185-136/3) Überschrift
     * (187-136/4) Text
     * (460-136/998) Titel
     * 
     * "Weisungstabelle":
     * (547) Überschrift "Nr" (nicht im Standard)
     * (168-0/22) Überschrift "Agenda" (Allgemein)
     * (104-0/24) "Ja" (Allgemein)
     * (105-0/25) "Nein" (Allgemein)
     * (106-0/26) "Enthaltung" (Allgemein)
     * 
     * Storno SRV (13)
     * (461-136/9) Text vor Gegenanträgen
     * Storno Briefwahl (14)
     * (462-136/10) Text vor Gegenanträgen
     * 
     * "Gegenantragstabelle":
     * (548) Überschrift "Nr" (nicht im Standard)
     * (93-0/23) Überschrift "Gegenanträge" (Allgemein)
     * (108-0/27) "Ja" (Allgemein)
     * (109-0/28) "Nein" (Allgemein)
     * (110-0/29) "Enthaltung" (Allgemein)
     * 
     * Storno SRV (13)
     * (463-136/13) Text Ende
     * Storno Briefwahl (14)
     * (464-136/14) Text Ende
     * 
     * Storno SRV (13)
     * (465-136/17) Button Erteilen
     * Storno Briefwahl (14)
     * (466-136/18) Button Erteilen
     *
     * Immer Buttons (nur Portal)
     * (54) Button Zurück
     * (35) Button Abmelden
     * 
     * 
     */

    /**aWeisungStornierenQuittung
     * ==========================
     * Storno SRV (13)
     * (189-139/1) Überschrift
     * (191-139/3) Text
     * (467-139/999) Titel
     * 
     * Storno Briefwahl (14)
     * (190-139/2) Überschrift
     * (192-139/4) Text
     * (468-139/998) Titel
     * 
     * (34-0/20) Button Weiter
     * (35) Button Abmelden
     * 
     */

    public void bestaetigenEtc() {
        aBestaetigen();
        aBestaetigen2();
        aBestaetigt();
        aBestaetigt2();
        aPasswortVergessen();
        aPasswortVergessenQuittung();
        aPWZurueck();
        aPWZurueckApp();
        aPWZurueckAppQuittung();
    }

    /**===================================Div. Bestätigungsseiten============================================================================*/
    private void aBestaetigen() {
    }

    /**aBestaetigen
     * ============
     * Bestätigung der hinterlegten Email-Adresse 1.
     * Nur Portal - bei der App ist dies in aEinstellungen/aRegistrierung integriert (über Code-Eingabe).
     * 
     * (212) Überschrift
     * (213) Text
     * (214) Button E-Mail-Bestätigen
     * 
     * Hinweis: Email-Texte für Bestätigung E-Mail-Adresse 1:
     * (Achtung: muß für Portal und App abweichen - App enthält nur Code, Portal Link) 
     * Aus Portal angefordert:
     * (220) Text
     * (221) Inhalt 
     * Aus App angefordert:
     * (282) Betreff
     * (283) Text
     * 
     * Wichtiger Hinweis zu den Mail-Texten: einfach Zeilenumbruch mit Enter einfügen. \n und <<<NL>>> etc. NICHT verwenden!
     */

    private void aBestaetigen2() {
    }

    /**aBestaetigen2
     * ============
     * Bestätigung der hinterlegten Email-Adresse 2 (also der zweiten E-Mail-Adresse).
     * Nur Portal - bei der App ist dies in aEinstellungen/aRegistrierung integriert (über Code-Eingabe).
     * Derzeit wird keine zweite E-Mail-Adresse angeboten, also Seite derzeit nicht relevant.
     * 
     */

    private void aBestaetigt() {
    }

    /**aBestaetigt
     * ===========
     * Quittung der Bestätigung der hinterlegten Email-Adresse 1.
     * Nur Portal - bei der App ist dies in aEinstellungen/aRegistrierung integriert (über Code-Eingabe).
     * 
     * (215) Überschrift
     * (216) Text
     * 
     */

    private void aBestaetigt2() {
    }

    /**aBestaetigt2
     * ===========
     * Quittung der Bestätigung der hinterlegten Email-Adresse 2.
     * Nur Portal - bei der App ist dies in aEinstellungen/aRegistrierung integriert (über Code-Eingabe).
     * Derzeit wird keine zweite E-Mail-Adresse angeboten, also Seite derzeit nicht relevant.
     * 
     */

    private void aPasswortVergessen() {
    }

    /**aPasswortVergessen / AppPasswortVergessen
     * =========================================
     * Hinweis: App-Seite enthält auch einen Button zum Eingeben des Rücksetzcodes in AppPWZuruek
     * 
     * Nur App:
     * 243-103/999: Fenstertitel
     * 
     * 207-103/1: Überschrift
     * 
     * Bereich nur in der App (nur, falls E-Mail-Versand-Verfahren aktiv):
     * 241-103/8: Button (falls Sie bereits einen Code erhalten haben)
     * 242-103/9: Feld: Code ??????????Gibts nicht mehr?
     * 
     * Text (alternativ):
     * 231-103/10: Text (gar nichts außer Aktionärsnummer einzugeben) - Variante 1
     * 208-103/2: Text (nur E-Mail-Eingabe) - Variante 2
     * 233-103/11: Text (nur Strasse/PLZ-Eingabe) - Variante 3
     * 232-103/12: Text (falls Auswahl möglich) - Variante 4
     * 
     * 
     * AuswahL:
     * 229-103/13: Email eingeben
     * 230-103/14: Ort/Strasse eingeben
     * 
     * 209-103/5: Feld Aktionärsnummer
     * Je nach Auswahl:
     * 210-103/6: Feld E-Mail
     * Oder:
     * 227-103/15: Feld Strasse
     * 228-103/16: Feld Ort
     * 
     * 211-103/4: Button Absenden
     * 557 Button zum Login - nur Portal
     */

    private void aPasswortVergessenQuittung() {
    }

    /**aPasswortVergessenQuittung
     * ==========================
     * (in App als Dialogfenster in AppPasswortVergessen enthalten!)
     * 
     * 237 Überschrift (nur in Portal)
     * Texte für Quittung:
     * Variante 1: Email verschickt, 238-103/7
     * Variante 2: per Post: 239-103/17
     * 
     * 557 Button Zum Login - nur Portal
     * 
     * Texte für Email:
     * Von Portal aus: Betreff=222, Text=223
     * Von App aus: Betreff=235, Text=236
     */

    private void aPWZurueck() {
    }

    /**Seitenaufbau für aPWZurueck (AppPWZurueck)
    * ==========================
    * 244-16/999 Titel (nur App)
    * 242-16/1 Überschrift
    * 250-16/2 Start-Text
    * 251-16/8 Feld Code (nur App)
    * 245-16/3 Feld Aktionärsnummer
    * 246-16/4 Feld neues Passwort
    * 247-16/5 Feld neues Passwort bestätigen
    * 249-16/6 Ende-Text
    * 248-16/7 Button "Passwort Speichern"
    * 252- / Button "Abbrechen" (nur Portal)
    * 253-16/9 Quittungs-Text (nur App)
    * 
    * 
    */

    private void aPWZurueckApp() {
    }

    /**Seitenaufbau für aPWZurueckApp - Passwortbestätigung in App auch über Portal
    * ==========================
    * 558 Überschrift
    * 559 Start-Text
    * 560 Feld Aktionärsnummer
    * 561 Feld neues Passwort
    * 562 Feld neues Passwort bestätigen
    * 563 Ende-Text
    * 564 Button "Passwort Speichern"
    * 
    */

    private void aPWZurueckAppQuittung() {
    }

    /**Seitenaufbau für aPWZurueckAppQuittung - Passwortbestätigung in App auch über Portal
    * ==========================
    * 565 Überschrift
    * 566 Start-Text
    * 
    */

    public void aRegistrierung_aEinstellungen() {
        aEinstellungen();
        aRegistrierung();
    }

    private void aEinstellungen() {
    }

    private void aRegistrierung() {
    }

    /**=====================================Informationen zu aRegistrierung / aEinstellungen==================================================
     * App: AppRegistrierung (beinhaltet aRegistrierung und aEinstellungen).
     * 
     *  aRegistrierung: wird aufgerufen nach i.d.R. erstmaligem Login des Aktionärs, sowie immer dann wenn E-Mail-Adresse noch nicht
     *  bestätigt wurde.
     *  
     *  aEinstellungen: wird aus dem Menü heraus aufgerufen. Beinhaltet grundsätzlich selbst Funktionalität wie aRegistrierung, nur nicht
     *  das Bestätigen der Hinweise.
     * 
     * Bereich 1: Anzeige immer (wenn Seite angezeigt wird) 
     * (284-101/999) nur App: Titel (aRegistrierung)
     * (285-101/998) nur App: Titel (aEinstellung)
     * 
     * (23-101/1) Überschrift (aRegistrierung)
     * (36-101/29) Überschrift (aEinstellungen)
     * (24-101/2) einleitender Text
     * 
     * Bereich 2: Anzeige von Hinweise / Meldungen (wenn aDlgVariablen.anzeigeMeldung)
     * (25-101/23) Meldungstext 1 - eigentliche Meldung (E-Mail 1 noch nicht bestätigt)
     * (286-101/24) Meldungstext 2 - eigentliche Meldung (E-Mail 2 noch nicht bestätigt - derzeit nicht implementiert)
     * (281-101/31) Meldungstext- was ist zu tun 
     * 
     * Bereich 3: Kommunikationssprache (abhängig von paramPortal.kommunikationsspracheAuswahl)
     * (254-101/33) Text-Start
     * Auswahlmöglichkeit eclTeilnehmerLoginM.aktienregisterZusatzM.kommunikationssprache:
     * (255-101/34) Text für Auswahl Deutsch
     * (256-101/35) Text für Auswahl Englisch
     * 
     * Bereich 4: Zustimmung zu elektronischem Einladungsversand (abhängig von paramPortal.registrierungFuerEmailVersandMoeglich)
     * (27-101/28) Text-Start
     * (28-101/7) Text zu Checkbox "Ja ich will" (eclTeilnehmerLoginM.aktienregisterZusatzM.eMailRegistrierungAnzeige)
     * (257-101/3) Text-Ende
     * Wenn paramPortal.gewinnspielAktiv:
     * 	(287-101/4) Gewinnspiel-Text
     * 	(258-101/6) Text: Link für Teilnahmedingungen anzeigen (abhängig von paramPortal.separateTeilnahmebedingungenFuerGewinnspiel) (bei App: Button für Teilnahmebedingungen anzeigen)
     * 
     * (nicht mehr verwendet: xxx-101/5)
     * 
     * Bereich 5: Publikationen - nur Hinweis auf Fundort (abhängig von paramPortal.publikationenAnbieten)
     * (259-101/36) Text (Links ggf. einbetten in Text!)
     * 
     * Bereich 6: Kontaktdetails (abhängig von paramPortal.kontaktDetailsAnbieten)
     * (260-101/37) Text-Start
     * (261-101/38) Text Feld Telefon (privat) (eclTeilnehmerLoginM.aktienregisterZusatzM.kontaktTelefonPrivat)
     * (262-101/39) Text Feld Telefon (geschäftlich) (eclTeilnehmerLoginM.aktienregisterZusatzM.kontaktTelefonGeschaeftlich)
     * (263-101/40) Text Mobil (eclTeilnehmerLoginM.aktienregisterZusatzM.kontaktTelefonMobil)
     * (264-101/41) Text Fax (eclTeilnehmerLoginM.aktienregisterZusatzM.kontaktTelefonFax)
     * 
     * Bereich 7: Hinweis auf Adressänderung über Depotbank (wird angezeigt, wenn Text nicht leer)
     * (265-101/42) Text
     * 
     * 
     * Bereich 8: Eigenes Passwort (abghängig von paramPortal.dauerhaftesPasswortMoeglich=1 oder 2)
     * (266-101/43) Text-Start
     * Falls aDlgVariablen.passwortBereitsVergeben=false:
     * 		(267-101/11) Checkbox "Ja ich will vergeben" (Checkbox-Wert in aDlgVariablen.ausgewaehltVergabeEigenesPasswort)
     * sonst (aDlgVariablen.passwortBereitsVergeben=true):
     * 		(279-101/44) Checkbox "Ich habe vergeben" (Checkbox-Wert in aDlgVariablen.ausgewaehltVergabeEigenesPasswort)
     * 		(278-101/45) Checkbox "Ja ich will Ändern" (Checkbox-Wert in aDlgVariablen.neuesPasswort), Anzeige nur wenn Passwort-Vergabe gewählt
     * Immer:
     * Falls Checkbox 267 oder (Checkbox 278 und Checkbox 279) ausgewählt:
     * (268-101/46) Text Mitte
     * (269-101/47) Feld "Neues Passwort" (eclTeilnehmerLoginM.aktienregisterZusatzM.neuesPasswort)
     * (270-101/48) Feld "Neues Passwort wiederholen" (eclTeilnehmerLoginM.aktienregisterZusatzM.neuesPasswortBestaetigung)
     * (271-101/49) Text Ende
     * 
     * Bereich 9: E-Mail-Adresse (anbieten, wenn paramPortal.registrierungFuerEmailVersandMoeglich!=0 ||
     * 			paramPortal.dauerhaftesPasswortMoeglich!=0
     * (272-101/50) Text-Start
     * (29-101/9) Feldbezeichnung zur Eingabe E-Mail-Adresse (eclTeilnehmerLoginM.aktienregisterZusatzM.eMailFuerVersand)
     * (30-101/10) Feldbezeichnung zur Wiederholung E-Mail-Adresse (eclTeilnehmerLoginM.aktienregisterZusatzM.eMailFuerVersandBestaetigen)
     * (273-101/51) Text-Ende
     * Wenn aDlgVariablen.emailbestaetigen:
     * 	(37-101/52) Button "Bestätigungs-Mail erneut versenden" (nur Einstellungen)
     * 	nur bei App:
     * 	(290-101/55) Feld "Bestätigungs-Code eingeben"
     * 
     * Bereich 10: Hinweise/Bestätigungen (wenn 10b oder 10c angezeigt wird)
     * (274-101/16) Gesamt-Einleitender Text
     * 
     * Bereich 10a: Separate Datenschutzerklärungen
     * Wenn aDlgVariablen.anzeigeHinweisDatenschutzerklaerung (wird angezeigt, wenn einer der Hinweise zu bestätigen ist, und gleichzeitig
     * 	paramPortal.separateDatenschutzerklaerung==1):
     * 	(288-101/53) Einleitender Text
     * 	(289-101/19) Text: Link für Hinweise Datenschutz anzeigen (App: Button zum Anzeigen) 
     * 
     * Bereich 10b: Hinweis HV-Portal bestätigen (abhängig von aDlgVariablen.anzeigeHinweisHVPortalBestaetigen)
     * (31-101/17) Einleitender Text
     * (275-101/56) Text: Link für Hinweise HV Portal anzeigen (App: Button zum Anzeigen)
     * (32-101/25) Checkbox-Text "Ich habe gelesen" (eclTeilnehmerLoginM.aktienregisterZusatzM.hinweisHVPortalBestaetigtAnzeige)
     * 
     * Bereich 10c: Hinweis Aktionärs-Portal bestätigen (abhängig von aDlgVariablen.anzeigeHinweisAktionaersPortalBestaetigen)
     * (276-101/54) Einleitender Text
     * (277-101/20) Text: Link für Hinweise Aktionaersportal anzeigen (App: Button zum Anzeigen)
     * (33-101/21) Checkbox-Text "Ich habe gelesen" (eclTeilnehmerLoginM.aktienregisterZusatzM.hinweisAktionaersPortalBestaetigtAnzeige)
     * 
     * (34-0/20) Button Weiter
     * (35-/) Button Abbrechen (Nur Portal)
     */

    public void hinweisFenster() {
        aDatenschutz();
        aNutzungshinweise();
        aKontakt();
        aImpressum();
    }

    private void aDatenschutz() {
    }

    private void aNutzungshinweise() {
    }

    private void aKontakt() {
    }

    private void aImpressum() {
    }

    /**============================================ "Hinweis-Fenster"=============================================
     * 
     * Grundsätzlich sind folgende Hinweisfenster vorgesehen:
     * 
     * > Teilnahmebedingungen zum Gewinnspiel (falls paramPortal.gewinnspielAktiv und zusätzlich paramPortal.separateTeilnahmebedingungenFuerGewinnspiel)
     * Verwendbar im Zusammenhang mit dem elektronischen Einladungsversand.
     * Kein separater Bestätigungsbutton vorhanden
     * App: AInfoMandant
     * Portal: aTeilnahme.xhtml
     * (291-913/999) Titel (nur App)
     * (292-913/1) Überschrift
     * (293-913/2) Text
     * (294-913/3) Button Schließen (App: Fertig)
     * 
     * > Datenschutz-Bestimmungen (paramPortal.separateDatenschutzerklaerung)
     * Separate Hinweise, für den Datenschutz gedacht.
     * Kein separater Bestätigungsbutton vorhanden.
     * App:  AInfoMandant
     * Portal: aDatenschutz.xhtml
     * (295-912/999) Titel (nur App)
     * (296-912/1) Überschrift
     * (297-912/2) Text
     * (298-912/3) Button Schließen (App: Fertig)
    * 
     * > Hinweise HV-Portal (paramPortal.bestaetigungSeparateHVPortalHinweiseErforderlich)
     * Zu bestätigen
     * App:  AInfoMandant
     * Portal: aNutzungshinweise.xhtml
     * (299-911/999) Titel (nur App)
     * (12-911/1) Überschrift
     * (13-911/2) Text
     * (14-911/3) Button Schließen (App: Fertig)
     * 
     * > Hinweise Aktionärs-Portal (paramPortal.bestaetigungSeparateAktionaersPortalHinweiseErforderlich)
     * Zu bestätigen
     * App: AInfoMandant
     * PortaL: aNutzungshinweisePortal.xhtml
     * (300-919/999) Titel (nur App)
     * (301-919/1) Überschrift
     * (302-919/2) Text
     * (303-919/3) Button Schließen (App: Fertig)
     * 
     * > Kontakt/Hilfe (paramPortal.kontaktFenster)
     * App: AInfoMandant
     * Portal: aKontakt.xhtml
     * (304-918/999) Titel (nur App)
     * (15-918/1) Überschrift
     * (16-918/2) Text
     * (17-918/3) Button Schließen (App: Fertig)
     * 
     * > emittentenspezifisches Impressum (paramPortal.impressumEmittent)
     * App: aInfoMandant
     * Portal: aImpressum.xhtml
     * (552-917/999) Titel (nur App)
     * (553-917/1) Überschrift
     * (554-917/2) Text
     * (555-917/3) Button Schließen (App: Fertig)
     * 
     * 
     * 
     * Im Portal sind die einzelnen Seiten aufrufbar wie folgt:
     * > Variante 1:
     * Sie die <a href="aNutzungshinweise.xhtml" target="blank">Datenschutz- und Nutzungshinweise</a> sorgfältig
     * 
     * 
     * Für die Fenster sind grundsätzlich folgende Texte vorgesehen:
     * 
     * (-/) Titel (nur App)
     * (-/) Überschrift
     * (-/) Text
     * (-/) Button Schließen (nur Portal)
     * 
     * 
     * Aufrufe in Portal
     * =================
     * (200) Allgemeine Footer-Zeile, ggf. mit Aufruflinks
     * (202) Kontakt (für Link in Footer z.B.)
     * 
     * Auswahl in App
     * ==============
     * 
     * 
     * Aufruf des Auswahlfensters AWeitereFunktionen (AppWeitereFunktionenMandant)
     * (305-/) ???? wohl nicht verwendet
     * (322-111/12) (aAnmelden)
     * (483-115/20) (aStatus)
     * 
     * Auswahlfenster für div. Info-Seite / Einstellungen: Seite 55
     * (aWeitereFunktionen)
     * (306-55/999) Titel (nur App)
     * (307-55/1) Überschrift
     * (308-55/9) Anfangs-Text
     * (309-55/7): Einstellungen (abhängig von App.paramPortal.registrierungFuerEmailVersandMoeglich == 1 || App.paramPortal.dauerhaftesPasswortMoeglich == 1)
     * (310-55/8): Startseite für den jeweiligen Mandant (abhängig von App.paramPortal.anzeigeStartseite & 16) == 16 )
     * (311-55/5): Separate Datenschutzerklärung (App.paramPortal.separateDatenschutzerklaerung == 1)
     * (312-55/10): Kontakt/Hilfe (App.paramPortal.kontaktFenster == 1)
     * (313-55/4): Hinweise Aktionärsportal (App.paramPortal.bestaetigungSeparateAktionaersPortalHinweiseErforderlich == 1)
     * (314-55/11): Hinweise HV (App.paramPortal.bestaetigungSeparateHVPortalHinweiseErforderlich == 1)
     * (315-55/6): Gewinnspiel - separate Bedingungen (App.paramPortal.gewinnspielAktiv==1 && App.paramPortal.separateTeilnahmebedingungenFuerGewinnspiel == 1)
     * (556-55/12): Impressum-Emittent (App.paramPortal.impressumEmittent)
     * 
     * (316-55/2): Ende-Text
     * (317-55/3): Weiter-Button
     * 
     * 
     */

    public void sonstiges() {
        aDlgAbbruch();
        aDlgFehler();
        aFehlerAllgemein();
    }

    private void aDlgAbbruch() {
    };

    /**aDlgAbbruch
     * ===========
     * Wird angezeigt, wenn ganz offensichtlich irgendwas "parallel" gelaufen ist, d.h. z.B. wenn sich ein Aktionär zweimal
     * gleichzeitig anmeldet, und in zwei Browsern unterschiedliche Willenserklärungen parallel abgibt.
     * Derzeit nur im Portal.
     * In der App noch nicht so ganz klar, was da passiert ....
     * 
     * (469) Überschrift
     * (470) Text
     * (34) Button Weiter (Zurück in die Status-Maske des eingeloggten Aktionärs
     * (35) Button Logout
     */

    private void aDlgFehler() {
    };

    /**aDlgFehler
     * ==========
     * Wird angezeigt, wenn z.B. mit den Browser-Tasten navigiert wurde. Oder sonstwas in der Navigation schief
     * gegangen ist (z.B. Verwendung eines illegalen Links).
     * In der App nicht vorhanden.
     * 
     * (204) Überschrift
     * (205) Text
     * (206) Button Schließen
     * 
     */

    private void aFehlerAllgemein() {
    }

    /**aFehlerAllgemein
     * ================
     * Seite, die bei einem internen - nicht vorhersehbaren - Programmfehler
     * angezeigt wird.
     * Bei App nicht vorhanden.
     * 
     * (471) Überschrift
     * (472) Text
     * (473) Button Schließen
     */

    public void nurFuerApp() {
    }

    /**Spezielle Texte/Seiten, die nur in der App vorkommen
     * ====================================================
     * Bestandsübersicht
     * -----------------
     * (486-11/8) HV: Servicekarte anzeigen
     * (487-11/5) HV: Digitale Eintrittskarte anzeigen
     * (488-11/6) Willenserklärungen erteilen oder verändern
     * (489-11/7) Fetter Warnhinweis: Für diese HV ist kein Bestand angemeldet!
     * (490-11/9) "Alte-HV-Funktionen" - Button Text muß für Realbetrieb leer sein!
     * (491-11/999) Titel (nur Mandantenunabhängig!!!)
     * 
     * HV-Karte/Service-Code Übersicht anzeigen (AppHVUebersicht)
     * ----------------------------------------
     * Seite 301
     * (528-301/999) Titel
     * (506-301/2) Button Akkreditierungscode anzeigen
     * (507-301/17) Button Servicecode anzeigen
     * 
     * (508-301/1) Überschrift und Text - Eintrittskarte vorhanden
     * (509-301/16) Überschrift und Text - nur Servicecode
     * 
     * (510-301/3) Text- Eintrittskarte
     * (511-301/19) Text-Servicekarte, Teilnahme grundsätzlich möglich
     * (512-301/18) Text-Servicekarte, keine Teilnahme möglich
     * 
     * Hinweistexte (alternativ)
     * (513-301/4) Alles ok, direkt rein ohne Sonderschalter
     * 
     * oder
     * (514-301/5) Starttext: Bitte beachten Sie ....:
     * Dann für alle aufgetretenen Fälle (mehrfach möglich, falls mehrere Fälle!):
     * (515-301/6) Angemeldet, aber wird nicht vertreten
     * (516-301/7) Angemeldet, ist delegiert
     * (517-301/8) Nullbestand
     * (518-301/9) Nicht angemeldet
     * Dann:
     * (519-301/10) Endtext
     * 
     * 
     * Danach Anzeige der Bestände, jeweils mit Voraustext:
     * (520-301/11) Diese Bestände können direkt akkreditiert werden (EK vorhanden)
     * (521-301/12) Diese Bestände sind angemeldet, aber delegiert
     * (522-301/13) Angemeldet, aber nicht vertreten
     * (523-301/14) Nicht angemeldet
     * (524-301/15) Nullbestände
     *  
     * 
     * Akkreditierungsseite (Code anzeige) (AppHVAkkreditierungsanzeige)
     * -------------------------------------------------
     * Seite 302
     * (529-302/999) Titel Akkreditierungscode
     * (530-302/998) Titel Servicevode)
     * (525-302/1) Einleitender Text (ggf. einschl. Überschrift) für Eintrittskarte
     * (526-302/3) Einleitender Text (ggf. einschl. Überschrift) für Servicecode
     * Danach Code anzeige
     * (527-302/2) Button "Weiter"
     * (
     * 
     *  
     * Übergreifende Texte
     * -------------------
     * (492-0/4) Aktionärsnummer (Feld)
     * Alternativ:
     * (493-0/5) Kennung
     * 
     * (494-0/6) Aktien (Feld)
     * (495-0/8) Dieser Bestand ist nicht uzr HV angemeldet und wird derzeit nicht vertreten
     * (496-0/10) Ist sich Nullbestand
     * (497-0/7) Passwort nicht mehr gültig
     * (498-0/12) EK Selbst
     * (499-0/13) EK Bevollmächtigt
     * (500-0/14) Vollmacht an Dritte
     * (501-0/15) Delegiert: Vollmacht an Dritte
     * (502-0/16) Delegiert: SRV
     * (503-0/17) Delegiert: KIAV
     * (504-0/18) Delegiert: Briefwahl
     * (505-0/19) "Unsicher"
     * 
     *
     * Nur Vision:
     * (-0/9) Kein bestand zugeordnet
     * (-0/11) Gastkarte
    
     * 
     */

    public void kundenInfos() {
        kundenInfosKpS();
    }

    private void kundenInfosKpS() {
    }
    /**K Plus S
     * ========
     * 
     * Phasen:
     * lfdHVPortalInBetrieb muß auch noch während der HV "in betrieb" sein!
     * 
     * Phasen 1, 2: vor Einladungsversand
     * Phasen 3: Anmeldephase
     * Phase 4: keine Anmeldung mehr, sonst alles
     * 
     * Phasen 5: wie 4, aber; Briefwahl nicht mehr möglich - bereits erteilte Briefwahlen auch nicht mehr stornierbar
     * Text:
     * Sie haben Ihre Stimmen bereits per Briefwahl abgegeben und können Ihre Briefwahlstimmen nur durch persönliche Teilnahme an der Hauptversammlung und dortige Abgabe einer Erklärung in Textform widerrufen.
     * You have already cast your votes by postal voting. Your votes can only be revoked by attending the Annual General Meeting in person and issuing a declaration in text form there.  
     * 
     * Phase 6: gar nichts mehr möglich - während der HV, aber es wird noch aStatus Willenserklärungen angezeigt
     * lfdHVPortalInBetrieb noch auf 1!
     * 
     * Phase 7: nach der HV, lfdHVPortalInBetrieb auf 2
     * 
     */

}

/*TODO _App: Kommunikationsfehler-Text noch parametrisieren*/
/*TODO _App: Maske 13, Passwort nach Scannen ergänzen, noch mandantenabhängig machen*/
/*TODO _Portal: Klären, was bei Null-Bestand passiert*/
/*TODO _PortalTexte Text: Fußzeile*/
/*TODO Noch folgende Variablen für Portal und App einfügen:
 * Datum Umschreibestopp
 * Ort, Straße Einlass, Beginn, Veranstaltungsraum HV
 */
