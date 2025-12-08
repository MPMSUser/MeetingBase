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

import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComAllg.CaDatumZeit;
import de.meetingapps.meetingportal.meetComAllg.CaFehler;
import de.meetingapps.meetingportal.meetComAllg.CaString;
import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclAktienregister;
import de.meetingapps.meetingportal.meetComEntities.EclLoginDaten;
import de.meetingapps.meetingportal.meetComEntities.EclMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclMeldungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclPersonenNatJur;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkarten;
import de.meetingapps.meetingportal.meetComEntities.EclStimmkartenSecond;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldung;
import de.meetingapps.meetingportal.meetComEntities.EclWeisungMeldungRaw;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklVollmachtenAnDritte;
import de.meetingapps.meetingportal.meetComEntities.EclWillensErklZuordnungZuSammelkarte;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerung;
import de.meetingapps.meetingportal.meetComEntities.EclWillenserklaerungZusatz;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittsIdent;
import de.meetingapps.meetingportal.meetComEntities.EclZutrittskarten;
import de.meetingapps.meetingportal.meetComKonst.KonstKartenklasse;
import de.meetingapps.meetingportal.meetComKonst.KonstLoginKennungArt;
import de.meetingapps.meetingportal.meetComKonst.KonstSkIst;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;
import de.meetingapps.meetingportal.meetComKonst.KonstStimmkarteIstGesperrt;
import de.meetingapps.meetingportal.meetComKonst.KonstWillenserklaerung;

/***************Vorrangiger Hinweis - Delay**************************************************
 * TODO _Delay
 * TODO _App Delay
 * Derzeit alle Delay-Funktionen in dieser Klasse deaktiv, da nicht auf aktuellem Stand!
 * => wenn S-Nr, oder E-Nr, oder Meldung bereits delayed, wird dies ignoriert
 * => Parameter zum Delay dürfen nicht verwendet werden.
 *
 * Für Vollmacht an Dritte, Anmeldungsveränderungen, SRV/KIAV Erteilung und Widerruf und Veränderung
 * gilt aktuell: diese sind manuell zu synchronisieren. Was auch kein Problem ist, solange diese
 * Funktionen dem Aktionär nicht direkt über die App zugänglich sind.
 */


/*******************************************************************************************
 * TODOLANG - Generell bei Willenserklaerungen:
 *
 * Anzahl der Willenserklärungen begrenzen, um Mißbrauch zu verhindern (getrennt nach
 * Präsenzänderungen und "Anmeldeänderungen" wie z.B. Vollmachten
 *
 * Neues Feld: Datum/Uhrzeit Erteilung der Willenserklärung (d.h. nicht Systemzeit, sondern
 * wird mit geliefert)
 *
 * Delay:derzeit wird nicht abgeprüft, ob die Meldung selbst auf "Delayed" steht. Dies wird
 * erfolgen in dem Moment, wo die Willenserklärungen (auch z.B. Vollmachten erteilen) auch in
 * meldung abgespeichert werden. => zukünftig auch abprüfen, ob meldung delayed ist.
 *
 * Willenserklärungen wie z.B. EK, SK, Vollmacht etc. direkt (je nach Umständen) auch in meldungen
 * eintragen (siehe auch Punkt Delay: weiter oben).
 *
 * High-Level-Funktionen:
 * > Speichern noch unbekannter Vollmachten / Vollmachtstornos als Pending (d.h. vor
 * den eigentlichen VollmachtDritte-Low-Level-Funktionen die PersonNatJur anlegen)
 *
 */

/*******************************************************************************************
 * Wichtige Verarbeitungshinweise
 *
 * > Durch Aufruf von _pruefen oder auch verarbeiten werden möglicherweise die pi*
 * 		-Variablen verändert! Dementsprechend müssen im Hauptdialog diese Parameter nach
 * 		_pruefen und vor der eigentlichen Verarbeitung neu gefüllt werden, um den selben Effekt
 * 		zu bekommen.
 ********************************************************************************************/

/*******************************************************************************************
 *Allgemeine Hinweise zur Logik der Willenserklärungen
 *******************************************************************************************
 *
 ****************************************Low Level und Grundsätze***************************
 *
 *Die folgenden Funktionen sind als "Low-Level-Funktionen" zu verstehen - d.h. Abbildung
 *von Willenserklärungen ohne Veränderungen des Meldebestandes.
 *D.h. konkret: für einen Anmeldesatz kann aktuell nur eine Person agieren.
 *
 *zusätzliche Funktionen (z.B. Vollmacht eines Anwesenden wird storniert, dieser wird zum Gast)
 *können mit diesen Low-Level-Funktionen nicht abgebildet werden - dies wird (für Selbstbedienung)
 *in weiteren High-Level-Funktionen abgebildet (z.B. automatische Ausstellung einer neuen Gastkarte).
 *
 *
 *
 ***********************************Grundsätzlicher Ablauf:************************************
 *
 *Voraussetzung für die Aufrufe von Schritt 1 bis Schritt 3:
 *	> Nummernformen (für ZutrittsIdent, Stimmkarte, StimmkarteSecond) sind bereits geprüft und formatiert
 *	  gemäß vorgegebener Form
 *
 *
 *
 *Schritt 1 (optional): Abfragen der aktuell gültigen "Willenserklärungslage"
 *	Dieser Schritt kann auch unabhängig von den nächsten Schritten durchgeführt werden, z.B.
 *	zur Anzeige des Status in einem Portal
 *
 *Schritt 2 ("halboptional"): Prüfen, ob die gewünschte Willenserklärung durchführbar ist
 *	Rückgabe: > ggf. Ablehnungsgrund
 *			  > anzuzeigende Warnungen / Fehlermeldungen / Korrekturen
 *			  > anzuzeigende VIP-Meldungen (deshalb nur "halboptional" - da diese vor
 *				einer evtl. durchzuführenden Buchung angezeigt werden sollen
 *
 *Schritt 3: Einbuchen der Willenserklärung
 *	Achtung: auch wenn Schritt 2 "durchführbar" signalisiert hat, geht Schritt 3 möglicherweise
 *	schief - nämlich wenn sich die "Willenserklärungslage" des betreffenden Aktionärs mittlerweile
 *	geändert hat. Dementsprechend analog zu Schritt 2 Rückgabe.
 *	Rückgabe: > ggf. Ablehnungsgrund
 *			  > anzuzeigende Warnungen / Fehlermeldungen / Korrekturen
 *
 *
 *
 *
 ***********************************************Details:*****************************************************
 *
 *Zu Schritt 2 + 3, Prüfung:
 *	Es wird grundsätzlich geprüft:
 *	> Ist die Willenserklärung überhaupt zulässig?
 *	> Ist die Willenserklärung zulässig unter Berücksichtigung Pending-Pool?
 *	> Welche weiteren Willenserkärungen (Stornierungen!) sind erforderlich, um die Willenserklärung
 *		annehmen zu können? (z.B. Stornierung einer Vollmacht vor Selbstzugang)
 *	> Welche weiteren Willenserklärungen wären optional sinnvoll?
 *		(z.B.: bei Selbstzugang Stornierung einer Vollmacht/Weisung an SRV)
 *
 * Zu Schritt 3: Aktualisierung Willenserklärungslage:
 * >Insbesondere bei "negativen" Willenserklärungen (Abgängen, Stornierungen) wird überprüft, inwieweit
 * 	andere Willenserklärungen wieder aufleben (z.B. noch vorhandene Vollmacht/Weisung an SRV)
 * >Dies muß aber auch generell überprüft werden, da nicht zwangsläufig die letzte abgegebene Willenserklärung
 * 	auch die für die HV erstmal geltende Willenserklärung ist (Bsp: Vollmacht/Weisung an SRV erteilt, dann
 * 	kommt zusätzlich Vollmacht an Dritte - je nach Parametrisierung bleibt Vollmacht/Weisung an SRV die
 * 	aktuell gültige)
 *
 *
 *
 *
 * ********************************************Gast-Logik**************************************************
 *
 * Gästekarten:
 * 	Sind grundsätzlich einfach zu handhaben - eine Person pro Anmeldung, und zwar die eingetragene. Keine Vollmachten,
 *  keine Sammelkarten, nix.
 *
 * Aktionäre:
 * 	Grundsätzlich kann zu jedem Aktionär auch noch ein- oder mehrere "zugeordnete Gästekarten" existieren.
 * 	Diese entstehen z.B., wenn der Aktionär die HV besucht, dann Vollmacht erteilt und selbst als Gast
 * 	anwesend bleibt.
 *
 * 	Eine "Aktionärs Zutritts/Stimmkarten-Ident" kann deshalb einem Aktionär und optional (maximal) einem Gast
 * 	zugeordnet sein - sie gehört damit letztendlich einer "Person" (die i.d.R. immer auch Zugriff auf die Aktionärsdaten
 *  hat - ausnahme z.B. widerrufene Vollmachten), die damit entweder als Aktionär oder als Gast agieren kann.
 *
 * 	Zugehörige Usecases z.B.:
 * 		> Ein Bevollmächtigter oder ein Aktionär gehen als Gast zu, um bestehende Vollmacht an SRV oder Briefwahl
 *   		aufrechtzuerhalten.
 *   	> Ein Aktionär ist präsent, gibt Vollmacht/Weisung z.B. an SRV, bleibt aber selbst als Gast präsent
 *
 *
 ***********************************Begriffsbestimmung Delay, Pending, Mischen**************************
 *Delay:
 *> Zum "Einfrieren" der Präsenz für den Zeitraum der Abstimmungen
 *> Buchungen werden weiter entgegengenommen, auf Gültigkeit geprüft, und "delayed" verbucht
 *> Die "Delayed-Buchungen" sind in einer gültigen, durchführbaren Reihenfolge und können deshalb
 *	_immer_ aufgelöst werden
 *
 *Pending:
 *> Für Backoffice-Erfassung gedacht, bei der nicht sicher ist dass die Willenserklärungen in
 *	der Reihenfolge der Erteilung erfaßt werden.
 *> Es werden auch Buchungen entgegengenommen, die zum jetzigen Zeitpunkt nicht möglich sind
 *> Beim "Pending-Auflösen" versucht das System, die nicht-verarbeitbaren Buchungen in eine
 *	sinnvolle Reihenfolge zu bringen. Manueller Eingriff ist möglich.
 *> Theoretisch auch im Front-Office einsetzbar, allerdings nicht sinnvoll (denn dort sollten
 *	"nichtbuchbare Buchungen" sofort geklärt werden, da sie immer durch eine Fehlbedienung
 *	(mehrfaches Erfassen der selben Erklärung; vergessen einer Erfassung) oder durch ein
 *	Fehlverhalten des Aktionärs (nicht-vorzeigen von Zutritts-/Abgangs-Dokumenten) verursacht
 *	werden und nur in Zusammenarbeit mit dem Aktionär geklärt werden können.
 *
 *Mischen:
 *> Die Willenserklärungen werden zum Zeitpunkt der Erteilung im IT-System erfaßt (dezentral),
 *	ohne in der zentralen Datenbank abgespeichert zu werden.
 *> Anhand des Zeitstempels können die Willenserklärungen dann (theoretisch) zusammengemischt werden.
 *> Theoretisch deshalb, weil "Fehlverhalten" (beim Erfassen, oder des Aktionärs) auftreten kann
 *	und dementsprechend zu Pending-Buchungen führen kann.
 *
 *Derzeit ist Mischen noch nicht implementiert, bzw. noch nicht in der Planung. Realisierbar letztendlich
 *über Buchung als Pending ohne Versuch eines Abgleichs, und separater Misch-/Synchronisations-Funktion.
 *
 *
 **************************************Delay-Logik******************************************************
 ******fachliche Gedanken zur High-Level-Verwendung****
 * Stufe 1: Aktionäre, die zu Beginn der Abstimmung präsent waren und damit abstimmen
 * konnten, dürfen keine Willenserklärung mehr abgegeben die einer solchen Abstimmung
 * widersprechen könnten (bzw. das Teilnehmerverzeichnis "negativ" verändern könnte).
 * D.h. konkret:
 * > kein Abgang mehr (sonst könnte er abstimmen, ist aber nicht mehr in der Präsenz)
 * > keine Vollmacht an Dritte mehr mit Abgang (wg. Gleichbehandlung auch nicht an
 *   natürliche Person),
 * 		Vollmacht an Dritte durch Übergabe Stimmblock wäre möglich. Aber: Vollmacht
 * 		ohne Weitergabe des Stimmblocks (z.B. Elektronisch, oder an KI/AV) und vorher
 * 		hat er noch selbst abgestimmt!
 * > Analog: keine V/W an SRV, keine Briefwahl
 *
 * Stufe 2: Alles wird delayed
 * Dient idealerweise nur dazu, die Präsenz "einzufrieren" bis zur Feststellung der Präsenz
 * unmittelbar zum Ende der Abstimmung.
 * Aber die Praxis zeigt:
 * > Nach Ende der Abstimmung und vor Feststellung der Präsenz müssen möglicherweise noch
 * 		Vollmachten / Weisungen eingelesen werden (insbesondere falls die Abgabemöglichkeit bis
 * 		zum Ende der Abstimmung kommen sollte
 * > Man hätte möglicherweise die Präsenz auch nach der Präsenzfeststellung noch bis zum
 * 		Ende der Abstimmungsauswertung gerne "eingefroren", um möglicherweise noch
 * 		nicht-anwesende (Groß-) Aktinäre präsent setzen zu können und dennoch einen
 * 		Präsenzstand zum Ende der Abstimmung zu erhalten (insbesondere beim
 * 		Subtraktionsverfahren).
 *
 *  Schwierig wird das nun, wenn es mehrere Abstimmungblöcke gibt, die nahtlos ineinander
 *  laufen.
 *  Lösung für diesen Fall: Peding Stufe 1 bleibt bis zum Ende der allerletzten Abstimmung aktiv.
 *  > Dann gibt es möglicherweise Ungenauigkeiten in den Präsenzen zwischen den Abstimmungen
 *  > Allerdings sind diese beim Additionsverfahren eh irrelevant
 *  > Beim Subtraktionsverfahren ergibt es möglicherweise zu viele Ja-Stimmen, d.h. es ist
 *  	zuGunsten der Gesellschaft
 *  		=> Beim Subtraktionsverfahren müssen - falls absolute Genauigkeit erwünscht - klare
 *  			Taktungen eingehalten werden, sobald es mehrere Abstimmungsvorgänge gibt.
 *
 *
 * *****Gedanken zur Wirkung auf den Aktionär - bei Abgabe der Willenserklärung ****
 *
 * Grundsätzlich: sobald eine Willenserklärung delayed ist, werden auch die Folge-
 * Willenserklärungen delayed.
 *
 * Für "normale Schalter"-Aktivitäten problemlos handhabbar.
 *
 * Wie aber für elektronische Selbstbedienung bei Online-Teilnahme oder Terminals oder
 * Vollmacht über Smartphone?
 * Bsp. 1:
 * 1.) Smartphone-User gibt VOllmacht an KI/AV während Abstimmung => wird delayed
 * 2.) Smartphone-User will nun doch selbst abstimmen
 * 3.) Smarthone-User widerruft deshalb nun die Vollmacht
 * Bsp. 2:
 * 1.) Online-Teilnehmer geht während Abstimmung ab
 * 2.) Online-Teilnehmer (möglicherweise auch ein anderer) geht während der
 * 		Abstimmung wieder zu
 * Bsp. 3:
 * 1.) Präsenz-Teilnehmer geht während Abstimmung ab
 * 2.) Online-Teilnehmer geht während Abstimmung zu
 *
 * Bsp. 4:
 * 1.) Zugang mit Widerruf einer Vollmacht (z.B. an SRV, KI/AV, aber auch Dritte)
 * 		(bei Delay des Vollmachtswiderrufs)
 *
 * Allerdings auch folgender Ansatz möglich:
 * > Grundsätzlich wird die Teilnahme an der Abstimmung für den selben Aktionär
 * 	sowohl durch unterschiedliche Personen als auch für unterschiedliche Medien
 * 	einkalkuliert (ist möglicherweise eh nicht auszuschließen, z.B. bei Hybrid-HV
 * 	oder eben bei der elektronischen Urne)
 * > Für diesen Fall können auch einige der obigen Fälle Präsenzmäßig zugelassen
 * 	werden, es müssen dann eben Widersprüche bei der Abstimmung aufgelöst werden,
 * 	z.B.: "Papiereinwurf zählt immer vorrangig, ansonsten die zuletzt eingegangene"
 *
 * ****Gedanken zur Wirkung auf den Aktionär - Teilnehmerverzeichnis ****
 * Im Teilnehmerverzeichnis wird immer der Präsenzstatus _ohne_ Durchführung der
 * Pending-Buchungen angezeigt.
 * Hintergrund:
 * > Technisch: sonst müßte technisch das gesamte Sammelkartenwerk sowohl für
 * 		Pending als auch für Nicht-Pending geführt werden!
 * > Fachlich: wenn ein Papierteilnehmerverzeichnis erstellt wird, muß das ja
 * 		zur festgestellten Präsenz passen und damit nur die non-delayed-Buchungen
 * 		enthalten
 * Lösungsansatz für elektronisches Teilnehmerverzeichnis: Kennzeichnung eines
 * Aktionärs "es liegen noch weitere, noch nicht berücksichtigte Willenserklärungen vor"
 *
 * **********Gedanken zur Handhabung von "Idents" (Zutritts, Stimmkarten)****************
 * Bsp:
 * 	Ein Bevollmächtigter ist im Sammel mit StimmkartenIdent V_S und ZutrittsIdent V_Z. Er
 * 	nimmt an der Abstimmung teil.
 *  Während Pending aktiv ist (also möglicherweise auch erst nach der Abstimmung) kommt nun
 *  Aktionär selbst und widerruft die Vollmacht er erhält A_S und A_Z. Das bedeutet:
 *  > Bevollmächtigter wird zum Gast
 *  > V_S und V_Z verweisen nur noch auf Gast-Meldung
 *  D.h.:
 *  > Wenn Bevollmächtigter V nun mit V_S die HV verläßt, darf sich das nur auf die Gastmeldung
 *  	auswirken
 *  Die Auswertung seiner während der Abstimmung abgegebene Stimmkarten V_S muß noch zählen.
 *  (Achtung - Auswirkung: sowohl V_S als auch A_S können nun für die Abstimmung abgegeben werden!
 *  Entsprechende Behandlung bei der Abstimmung erfoderlich!)
 *
 *
 *==> auch bei Idents muß zumindest die Zuordnung pending und nicht-pending speichern!
 *
 *  Spielchen geht weiter (immer noch selbes Pending aktiv):
 *  > Aktionär geht nun, gibt Vollmacht wieder an V. Seine wird inaktiv. D.h. die beiden Pendings
 *  heben sich auf.
 *  Allerdings Grundvoraussetzung: Eine Ident darf nicht zwischen verschiedenen Meldungen_der_Klasse_Aktionär
 *  hin-und-her-gewechselt werden.
 *
 ***********************************=> technische Lösung sieht wie folgt aus:***************************************
 *
 *?????????????Grundsätzliche Frage - muß wirklich Delayed-Buchungen neutralisiert werden?????????????????
 *Wenn immer:
 *> "Delayed-Status" (falls vorhanden) ist Sicht zum Aktionär, für neue abgegebene Willenserklärung (außer Abstimmung).
 *> Nicht-Delayed-Status ist für Abstimmung gültig
 *> Für Idents gilt: sowohl delayed als auch normal-Status verwendbar - je nach Anwendungsfall!.
 *
 *
 *> Willenserklärungen können je Art delayed werden
 *
 *> Wurde für einen Teilnehmer eine "delayed" Willenserklärung abgegeben, so sind
 *	standardmäßig alle anderen folgenden Willenserklärungen auch erst mal delayed
 *	(z.B. bei Online-Teilnahme dann ein entsprechender Hinweis)
 *
 *> Es können jedoch parametergesteuert Ausnahmen zugelassen werden, bzw. "sich
 *	aufhebende Willenserklärungen" zugelassen werden.
 *	Diese sind konkret aktuell realisiert:
 *	>> Abgang / Wiederzugang mit selber Ident möglich
 *		(Ist z.B. bei einer ganz normalen HV sinnvoll)
 *	>> Abgang / Wiederzugang mit unterschiedlicher Ident möglich
 *		(Ist bei einer HV sinnvoll, bei der eh unterschiedliche Abstimmungsmedien
 *		eingesetzt werden)
 *	>> Vollmacht/Briefwahl (falls Delayed) / Widerruf der selben
 *
 *	(Achtung: folgender Ablauf wird NICHT berücksichtigt , da sinnlos; denn wenn
 *	delayed-V/W widerrufbar sind, dann sind sicher auch delay Abgang-Zugang aufhebbar!)
 *		>> V/Briefwahl "grundsätzlich nicht delayed", aber:
 *				> Aktionär geht ab (wird delayed)
 *				> Aktionär kommt (wird damit auch delayed)
 *				> Aktionär gibt V/W (wird damit auch delayed)
 *				> Aktionär kann V/W widerrufen (damit wieder "delayter Zugang"
 *
 *Noch ein Hinweis:
 *Werden beide Willenserklärugnen "bewußt delayed" (also delay sowohl für Abgang
 *als auch für Zugang gewählt), dann können sich diese dennoc ebenfalls aufheben.
 *
 */
/*******************Aktueller Ablauf der Delay-Prüfung*********************************************
 *
 *"Technischer" Eingabewert:
 *ptForceNonDelay==false -> normale Abprüfung ob delayed oder nicht
 *ptForceNonDelay==true -> es wird "normal" gebucht, auch wenn Willenserklärung delayed sein müßte
 *		wird verwendet z.B. zum Auflösen der Delayed-Buchungen
 *
 *Returnwert:
 *rcDelayed == false -> Willenserklärung wurde normal gebucht
 *rcDelayed == true -> Willenserklärung wurd delayed gebucht.
 *
 *Interne Variable:
 *weiterverarbeitungDelayed =1 => meldung - oder eine im Zusammenhang mit der aktuellen Willenserklärung vorgehende
 *	willenserklärung - ist bereits delayed, und diese Willenserklärung muß deshalb delayed gebucht werden.
 *
 *Interner Ablauf:
 ***evtlEinlesenMeldung():***
 *
 *> in evtEinlesenMeldung wird weiterVerarbeitungDelayed auf 1 gesetzt, wenn die Stimm(Second)/Zutrittsident,
 *	über die identifiziert wurde, das Delay-Kennzeichen hat.
 *		> es wird jedoch nicht überprüft, ob die meldung selbst delayed ist, da dies für Gast
 *	 		und Aktionär abweichend sein kann und in dieser Funktion nicht klar ist, welche der beiden
 * 			weiterverarbeitet ist.
 *
 *> falls ptForceIdentifiaktionsIstMeldungsIdent==true, dann wird in evtlEinlesenMeldung() die Identifikation immer
 *	über meldungsident durchgeführt (=auflösung einer Delayed-Willenserklärung)
 *
 ***prepareWillenserklaerung***
 *> Die Willenserklärung wird NICHT automatisch ggf. delayed gesetzt, da es ja WIllenserklärungen gibt für die
 *	- selbst bei bereits delayed-Willesnerklärungen - kein delayed gesetzt wird, z.B. neue ZutrittsIdent!
 *
 ***einlesenVollmachtenAnDritte***
 *> Es werden grundsätzlich immer auch alle delayed-Vollmachten berücksichtigt, außer ptForceNonDelay==true (
 *	d.h. nicht beim Auflösen von Delayed Willenserklärungen)
 *> Falls bereits eine "vollmachtDritte"-Willenserklärung vorliegt, die delayed ist, dann wird
 *	weiterverarbeitungDelayed auf 1 gesetzt.
 *
 *
 ***_pruefe***
 *> Am Ende von _pruefe ist rcDelayed gesetzt (auch unter Berücksichtigung von ptForceNonDelay - und aufgrund diesem
 *	rcDelayed wird dann auch in der eigentlichen Funktion das Delay-Kennzeichen gesetzt.
 *	- ist weiterverarbeitundDelayed==1?
 *	- ptForcedNonDelay?
 *	- Willenserklärung selbst muß delayed werden gemäß Einstellungen?
 *		lDbBundle.dbBasis.willenserklaerungIstDelayed(EnWillenserklaerung.FreigebenGesperrteZutrittsIdent)
 *
 ***Eigentliches Setzen***
 *> in meldung wird delayed nur upgedated, wenn auch die Felder in meldung upgedatet werden
 */

public class BlWillenserklaerung {

    private int logDrucken = 3;

    /*************************Ab hier: Eingabeparameter-Identifikation (pi)*******************************************
     */

    /**Meldung Aktionär - falls bereits eingelesen. Falls null, dann wird Meldung hier selbstständig eingelesen
     * und steht anschließend in diesem Parameter zur Verfügung*/
    public EclMeldung piEclMeldungAktionaer = null;

    public int piMeldungsIdentAktionaer = 0;

    /**Meldung Gast - falls bereits eingelesen. Falls null, dann wird Meldung hier selbstständig eingelesen
     * und steht anschließend in diesem Parameter zur Verfügung*/
    public EclMeldung piEclMeldungGast = null;

    public int piMeldungsIdentGast = 0;

    public EclZutrittsIdent piZutrittsIdent = new EclZutrittsIdent();

    /**Klasse - als Ergänzung zu pZutrittsIdent. Muß nicht geliefert werden, wenn ZutrittsIdent klassenübergreifend
     * eindeutig ist
     */
    public int piKlasse = -1; /* 0=Gast und 1=Aktionär sind zulässige Werte*/

    public String piStimmkarte = "";
    public String piStimmkarteSecond = "";

    /************Ab hier: Eingabeparameter (p) *********************************************************/
    /**Erteilt auf welchem Weg - siehe EclWillenserklaerung.erteiltAufWeg*/
    public int pErteiltAufWeg = 0;

    /**Zeitpunkt, zu dem die Willenserklärung als "erteilt" gilt. Wenn =="", dann wird die aktuelle
     * Systemzeit genommen (z.B. für Portalfunktionen sinnvoll). Ansonsten
     * 19-stelliger String in der Form YYYY.MM.DD HH:MM:SS
     */
    public String pErteiltZeitpunkt = "";

    /**Die Willenserklärung soll als Folge-Willenserklärung für diese Willenserklärung gespeichert werden.
     * Anwendungsfall: mehrere, in einer Aktion gegebene Detailwillenserklärungen (z.B. Eintrittskarte für
     * Bevollmächtigten)
     *
     * Achtung: die Verkettungen können "mehrfach abhängig sein", als Beispiel:
     * Willenserklärung "Anmeldung":	ident = 101
     * Willenserklärung "EK 1":			ident = 102	Folge für 101
     * Willenserklärung "Vollmacht":	ident = 103	Folge für 102
     * Willenserklärung "EK 2":			ident = 104 Folge für 101
     * Willenserklärung "Vollmacht":	ident = 105	Folge für 104
     *
     * D.h. beim Einlesen/Verarbeiten eines "Blockes" muß ggf. rekursiv vorgegangen werden.
     */
    public int pFolgeFuerWillenserklaerungIdent = 0;

    /**Verweis auf Willenserklärung - z.B. Scan-Datei - max. Länge 100*/
    public String pQuelle = "";

    /************Ab hier: technische Eingabeparameter (pt) - nur intern zu verwenden!*****************/

    /**Mit ==true kann erzwungen werden, dass diese Willenserklärung immer "nondelayed" gebucht wird, selbst
     * wenn sie gemäß globalen Einstellungen auf delayed gesetzt würde.
     *
     * Achtung Achtung! Falsche Verwendung kann zum kompletten Zusammenbruch der Delay-Logik führen!
     *
     * Implementiert wurde dieser Parameter, um "delayed-Buchungen", die sich aufheben, aufzulösen, auch
     * wenn der Delayed-Modus noch aktiv ist.
     *
     * D.h. verwendung ist ausschließlich innerhalb BlWillenserklaerung vorgesehen!
     */
    public boolean ptForceNonDelay = false;

    /**Technischer Parameter - nur intern zu verwenden - zum Speichern von Verweisen auf
     * andere Willenserklärungen, z.B. für Delayed-Auflösung, Pending-Auflösung, Storno.
     * Werden 1:1 in die entsprechenden Felder der willenserklärung übertragen.
     */
    public int ptVerweisart = 0;
    /**Technischer Parameter - nur intern zu verwenden - zum Speichern von Verweisen auf
     * andere Willenserklärungen, z.B. für Delayed-Auflösung, Pending-Auflösung, Storno.
     * Werden 1:1 in die entsprechenden Felder der willenserklärung übertragen.
     */
    public int ptVerweisAufWillenserklaerung = 0;

    /** Falls ==true, dann wird keine Identifikationsüberprüfung durchgeführt, sondern immer
     * davon ausgegangen, dass meldungsIdent bzw. meldungsIdentGast gefüllt sind, die restlichen
     * identifikation-Feld (pi) werden nicht angefaßt, sondern später 1:1 übernommen.
     * ptUebernahmeIdentifikation wird dabei dann als identifkationDurch gesetzt.
     *
     * Wird verwendet, um delayed/Pending-Willenserklärungen entsprechend aufzulösen
     */
    public boolean ptForceIdentifikationIstMeldungsIdent = false;
    /**siehe ptForceIdentifikationMeldungsIdent
     * Falls ==true, dann wird keine Identifikationsüberprüfung durchgeführt, sondern immer
     * davon ausgegangen, dass meldungsIdent bzw. meldungsIdentGast gefüllt sind, die restlichen
     * identifikation-Feld (pi) werden nicht angefaßt, sondern später 1:1 übernommen.
     * ptUebernahmeIdentifikation wird dabei dann als identifkationDurch gesetzt.
     *
     * Wird verwendet, um delayed/Pending-Willenserklärungen entsprechend aufzulösen
     */
    public int ptUebernehmeIdentifikation = -1;

    /******************Ab hier: die folgenden Eingabeparameter (p) werden abhängig von der jeweiligen Willenserkläaerung verarbeitet*********/

    /**Siehe EclWillenserklaerung.willenserklaerungGeberIdent.
     * Ident dessen, der die Willenserklärung abgibt. Durchgängige Verwendung noch unklar. In jedem Fall jedoch zu verwenden bei:
     * > Vollmacht an Dritte - der Vollmachtsgeber
     * > Briefwahl, Vollmacht an KIAV - der Vollmachtsgeber
     *
     * Ident=	Verweis auf tbl_vertreter-Ident, falls von einem Vertreter ausgehend
     * Ident=   -1, falls von Aktionär selbst ausgehend
     * Ident=	0, falls undefiniert.
     *
     */
    public int pWillenserklaerungGeberIdent;

    /** Zuordnung einer neuen Zutritts-Identifikation. 
     * Wenn leer, dann wird die EK-Nummer automatisch vergeben.
     * Wenn gefüllt, muß sie nicht dem erforderlichen Format entsprechend - die Aufbereitung
     * erfolgt innerhalb neueZutrittsIdentZuMeldung.aaaaaaaaaaaaaaaaaaaaaaaaaa
     * Nach der Zuordnung enthält diese die vergebene Ident*/
    public EclZutrittsIdent pZutrittsIdent = new EclZutrittsIdent();

    /**Stimmkarte - Alternative (falls leer bei entsprechender Funktion): automatisches Füllen, z.B. für Online-Teilnahme, Smartphones etc.*/
    public String pStimmkarte = "";

    /**StimmkarteSecond - Alternative (falls leer bei entsprechender Funktion): automatisches Füllen, z.B. für Online-Teilnahme, Smartphones etc.*/
    public String pStimmkarteSecond = "";

    /*********für: Vollmacht an Dritte*************/

    /**Person, für Vollmacht an Dritte*/
    public EclPersonenNatJur pEclPersonenNatJur = null;

    /********für: Sammelkartenzuordnung************/

    /**meldungsId für die zugeordnete Sammelkarte**/
    public int pAufnehmendeSammelkarteIdent;

    /**Für Erteilen und ändern: Hier ist nach den Sammelkarten-Prüfe-Aktionen die aufnehmende Sammelkarte abgelegt*/
    public EclMeldung pEclMeldungSammelkarte = null;

    /**Abgegebene Weisung (uninterpretiert)**/
    public EclWeisungMeldungRaw pEclWeisungMeldungRaw = null;

    /**Bei Neuerteilung: Abgegebene Weisung (interpretiert)
     * Bei Ändern:  .weisungsIdent = Verweis auf bisherige, zu ändernde Weisung
     * 				.willenserklaerungIdent = Verweis auf (alte) Willenserklärung, mit der die
     * 				 zu ändernde Weisung erteilt wurde
     * **/
    public EclWeisungMeldung pEclWeisungMeldung = null;

    /**Für Stornieren: optional - wenn null dann werden alle Zuordnungen zur Sammelkarte (in pAufnehmendeSammelkarteIdent
     * storniert, wenn !=null dann nur die Zuordnung, die durch pEclMeldungSammelkarte.willenserklaerungIdent spezifiziert wird*/
    public EclMeldungZuSammelkarte pEclMeldungZuSammelkarte = null;

    /*****************Für: anmeldungAusAktienregister***********************/
    /**AKtienregistereintrag, aus dem heraus die Anmeldung generiert werden soll*/
    public EclAktienregister pEclAktienregisterEintrag = null;

    /**Anzahl der Aktien, die angemeldet werden sollen. =-1 => Rest / alles*/
    public long pAktienAnmelden = -1;

    /**Soll Anmeldung "Fix" erfolgen?*/
    public boolean pAnmeldungFix = false;

    /**Wenn pAnmeldungFix, und pAnmeldungFixRest==true, dann wird ggf. auch kleinerer
     * Wert angemeldet
     */
    public boolean pAnmeldungFixRest=false;
    
    /**Anzahl Anmeldungen, auf die der Bestand aufgesplittet werden soll*/
    public int pAnzahlAnmeldungen = 1;

    /**Nr der PersonNatJur, die für Anmeldung verwendet werden soll.
     * 0 => eine neue PersonNatJur anlegen
     */
    public int pPersonNatJurFuerAnmeldungVerwenden = 0;

    /******************Für: anmeldungGast (denkbar auch für andere Anmeldungen mit direkter Eingabe*****/
    public EclMeldung pEclMeldungNeu = null;
    
    public long pBerechtigungsWert=0;

    /*******************Optionale Parameter für Eintrittskartenausstellung (zutrittsIdent)*************/

    /**0 = keinerlei Weiterverarbeitung erforderlich (alles bereits vom Programm adhoc erledigt
     * 1 = Aufnahme in Sammelbatch an Adresse im Aktienregister - bei nächstem Drucklauf ausdrucken und versenden
     * 2 = Aufnahme in Sammelbatch an Versandadresse - bei nächstem Drucklauf ausdrucken und versenden
     * 3 = Online-Ausdruck (im Portal) erfolgt
     * 4 = Versand per Email (im Portal) erfolgt
     * 5 = automatische Aufnahme in App
     * 6 = selbe Versandadresse wie zeitgleich ausgestellte Eintrittskarte (nur Gastkarte!)
     */
    public int pVersandartEK;
    public String pVersandadresse1 = "";
    public String pVersandadresse2 = "";
    public String pVersandadresse3 = "";
    public String pVersandadresse4 = "";
    public String pVersandadresse5 = "";

    /**Versand an Emailadresse*/
    public String pEmailAdresseEK = "";

    /**************Ab hier: noch unsortiert!***************************/

    /** Stimmkartennummer - nur wenn "verwenden"=1.
     * Z.B. bei "Tausch 1:1" verwenden auf 0 lassen
     * */
    public int pStimmkarteVerwenden = 0;
    /**0 = Stimmkartennummer steht in pStimmkarte; ansonsten zulässig aktuell 1 = automatische Nummernvergabe aus
     * "Online"-Nummernkreis; erweiterbar auf weitere Nummernkreise
     */
    public int pStimmkarteAutomatischVergeben = 0;

    /** Alternative Stimmkartennummer - z.B. für Televoter o.ä. - nur wenn "verwenden"=1
     * */
    public int pStimmkarteSecondVerwenden = 0;
    /**0 = Stimmkartennummer steht in pStimmkarte; ansonsten zulässig aktuell 1 = automatische Nummernvergabe aus
     * "Online"-Nummernkreis; erweiterbar auf weitere Nummernkreise
     */
    public int pStimmkarteSecondAutomatischVergeben = 0;

    /*********************************Ab hier: Rückgabewerte (rc)***********************************/

    /** "Grundsätzlicher" Returncode - ist die abgegebene Willenserklärung aktuell überhaupt durchführbar?*/
    public boolean rcIstZulaessig = true;
    /** Grund, falls rcIstZulaessig=false
     *
     * Standardmäßige Fehlermeldungen:
     * 	> Durch Identifikation - siehe evtlEinlesenMeldung()
     *  > undefiniert => i.d.R. technischer Fehler
     *
     * Individuelle Fehlermeldungen siehe jeweilige Willenserklärung
     * */
    public int rcGrundFuerUnzulaessig = 0;

    /**Warnung - falls rcIstZulaessig=true. =true, dann steht in rcGrundFuerWarnung eine Erläuterung*/
    public boolean rcWarnung = false;
    /**Grund für rcWarnung=true*/
    public int rcGrundFuerWarnung = 0;

    /** Returncode, = true => die Willenserklärung ist zwar möglich (rcIstZulaessig=true), wird aber "delayed"*/
    public boolean rcDelayed = false;
    /** Returncode, = true => alle bisher delayed Willenserklärungen können aufgelöst werden, die
     * Meldung hat anschließend keine "Delayed"-Willenserklärungen mehr
     */
    /*LANGTODO - rausnehmen. Nur noch historisch in alten Funktionen vorhanden*/
    public boolean rcDelayedAufloesen = false;

    /**Returncode, = true => die Willenserklärung ist nicht möglich (rcIstZulaessig=false),
     * wird aber im Pending Pool eingetragen*/
    public boolean rcPending = false;

    /**Returncode, = true => es liegen Pending-Buchungen vor, deren Auflösung möglicherweise im Zusammenhang
     * mit der gerade erteilten Buchung möglich wären
     * Bsp.: es liegen Pending-Vollmachten vor, und es wird ein Vollmachtenwiderruf gebucht.
     */
    public boolean rcWarnungPendingLiegtVor = false;

    /**Array mit den dem Aktionär piMeldungsIdentAktionaer zugeordneten Vollmachten. Sowohl
     * Gültige als auch Widerrufene
     */
    public EclWillensErklVollmachtenAnDritte[] rcVollmachtenAnDritte = null;
    /**Array mit den dem Aktionär piMeldungsIdentAktionaer zugeordneten Vollmachten. Sowohl
     * Gültige als auch Widerrufene. - Anzahl im Array
     */
    public int rcVollmachtenAnDritteAnzahl = -1;

    /**Array mit den dem Aktionär piMeldungsIdentAktionaer zugeordneten Sammelkarten. Sowohl
     * Gültige als auch Widerrufene und geänderte
     */
    public EclWillensErklZuordnungZuSammelkarte[] rcZuordnungZuSammelkarte = null;
    /**Array mit den dem Aktionär piMeldungsIdentAktionaer zugeordneten Sammelkarten. Sowohl
     * Gültige als auch Widerrufene und geänderte. - Anzahl im Array
     */
    public int rcZuordnungZuSammelkarteAnzahl = -1;

    /**Array mit erzeugten Meldungen, für anmeldungAusAktienregister*/
    public int[] rcMeldungen = null;
    public EclMeldung[] rcEclMeldungen=null;

    /**Bei Anlegen von Gästen (anmeldungGast) und Bevollmächtigten (vollmachtAnDritte):
     * hier wird der zum Bevollmächtigten / Gast passende rcLoginDaten-Satz geliefert*/
    public EclLoginDaten rcLoginDaten = null;

    /**Hier wird die Ident der vergebenen Willenserklärung zurückgegeben. Diese ident kann verwendet werden,
     * um Folge-Willenserklärungen zu speichern (z.B.: ZutrittsIdent ausgestellt und zugleich Vollmacht erteilt =
     * EK auf diesen Bevollmächtigten ausgestellt)
     * Siehe auch Eingabeparameter pFolgeFuerWillenserklaerungIdent
     */
    public int rcWillenserklaerungIdent = 0;

    /**Anzahl Meldungen, die vorhanden sind / storniert werden könnten )anmeldungenAusAktienregisterStornieren_pruefe)*/
    public int rcAnzahlMeldungen = 0;

    /********Ab hier: rc noch nicht final***************/
    /** Returncode, inwiefern vor (mit) Durchführung der Willenserklärungen Stornierungen erforderlich sind,
     * bzw. automatisch durchgeführt wurden*/
    public boolean rcStornierungenErforderlich = false;
    /**Willenserklärungen, die vor der Durchführung zu stornieren sind, bzw. mit der Durchführung storniert
     * wurden*/
    public EclWillenserklaerung rcStornoWillenserklaerungen[] = null;

    /*LANGTODO VIPKZ*/
    /*LANGTODO Verknüpfte Karten*/

    /****************************Private Variablen innerhalb dieser Klasse**********************************/
    private DbBundle lDbBundle = null;

    /**Gibt an, was "übergeben" wurde - wird ggf in _pruefe gesetzt
     * =0 => es wurde nur ein "echter" Gast übergeben
     * =1 => es wurde ein Aktionär (ggf. auch mit paralleler Gast-Meldung) übergeben*/
    private int klasse = 0;

    /**Gibt an, über welche Eingangs-Parameter (pi) die Meldung identifiziert wurde.
     * Werte siehe identifikationDurch bei EclWillenserklärung
     */
    private int identifikationDurch = 0;

    /**Für interne Funktionen. Wird z.B. verwendet, um Präsenzfunktionen (=1) oder
     * Stimmkartenfunktionen (Auswertung, Erfassung; ==0) verarbeitet werden soll:
     * ==0 => es wird auf der Basis "Nicht-Delayed"-Willenserklärungen verarbeitet
     * ==1 => es wird auf der Basis "Delayed-Berücksichtigen"-Willenserklärungen verarbeitet
     */
    //	private int verarbeitungDelayed=0;

    /**Gibt wieder, ob in einer Vorfunktion bereits Delayed-Sachen gefunden wurden, d.h.
     * weitere Verarbeitung muß auch auf Basis delayed erfolgen.
     * ==1 => weitere Verarbeitung muß delayed erfolgen, da bereits bisherige Basis delayed ist
     * ==0 => weitere Verarbeitung kann auch nicht-delayed erfolgen, das wird ers in der Folgeverarbeitung entschieden*/
    private int weiterverarbeitungDelayed = 0;

    /**Willenserklärungstatus - für anmeldungenAusAktienregisterStornieren*/
    private BlWillenserklaerungStatus blWillenserklaerungStatus = null;
    private BlWillenserklaerungStatus blV2WillenserklaerungStatus = null;
    /**Die PersonNatJur, auf die die Anmeldung erfolgt ist - für anmeldungenAusAktienregisterStornieren*/
    private int anmeldungPersNatJur;

    
    /*******************Transaktionsverwaltung für Willenserklärungen****************/
    private void willenserklaerungSperren(DbBundle pDbBundle) {
        pDbBundle.dbSperre.setzeSperre(1);
    }

    private void willenserklaerungFreigeben(DbBundle pDbBundle) {
        pDbBundle.dbSperre.beendeSperre();
    }
    
    private void abbruchBeiException(DbBundle pDbBundle, Exception e) {
        willenserklaerungFreigeben(pDbBundle);
        pDbBundle.closeAll();
        e.printStackTrace();
    }

    private void willenserklaerungOhneSammelkarteSperren(DbBundle pDbBundle) {
        pDbBundle.dbSperre.setzeSperre(1);
    }

    private void willenserklaerungOhneSammelkarteFreigeben(DbBundle pDbBundle) {
        pDbBundle.dbSperre.beendeSperre();
    }
    
    private void abbruchOhneSammelkarteBeiException(DbBundle pDbBundle, Exception e) {
        willenserklaerungFreigeben(pDbBundle);
        pDbBundle.closeAll();
        e.printStackTrace();
    }

    
    /******************liefereGattung**************************************************************************
     * falls piEclMeldungAktionaer gefüllt, dann entsprechende Gattung, ansonsten 0
     */
    private int liefereGattung() {
        int lGattung = 0;
        if (piEclMeldungAktionaer != null && piEclMeldungAktionaer.klasse == 1) {
            lGattung = piEclMeldungAktionaer.gattung;
            if (lGattung == 0) {
                lGattung = 1;
            }
        }
        return lGattung;
    }

    /******************liefereKlasse**************************************************************************
     * falls piEclMeldungAktionaer gefüllt, dann entsprechende Klasse, ansonsten 0
     */
    private int liefereKlasse() {
        int lKlasse = 0;
        if (piEclMeldungAktionaer != null) {
            lKlasse = piEclMeldungAktionaer.klasse;
        } else {
            lKlasse = 0;
        }
        return lKlasse;
    }

    /************negiereWillenserklaerung********************************************************************/
    /**Liefert storno-Willenserklärung zu einer Willenserklärung*/
    private int negiereWillenserklaerung(int pWillenserklaerung) {
        int nWillenserklaerung = KonstWillenserklaerung.undefiniert;

        switch (pWillenserklaerung) {
        case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
        case KonstWillenserklaerung.aendernWeisungAnSRV: {
            nWillenserklaerung = KonstWillenserklaerung.widerrufVollmachtUndWeisungAnSRV;
            break;
        }
        case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
        case KonstWillenserklaerung.aendernWeisungAnKIAV: {
            nWillenserklaerung = KonstWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV;
            break;
        }
        case KonstWillenserklaerung.dauervollmachtAnKIAV: {
            nWillenserklaerung = KonstWillenserklaerung.widerrufDauervollmachtAnKIAV;
            break;
        }
        case KonstWillenserklaerung.briefwahl:
        case KonstWillenserklaerung.aendernBriefwahl: {
            nWillenserklaerung = KonstWillenserklaerung.widerrufBriefwahl;
            break;
        }
        case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
        case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte: {
            nWillenserklaerung = KonstWillenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte;
            break;
        }
        }

        return nWillenserklaerung;
    }

    /************checkZutrittsIdentDelayed*******************************************************************/
    /**Checken, ob für EclZutrittskarten bereits Delayed vorhanden     */
    private int checkZutrittsIdentDelayed(EclZutrittskarten zutrittskarte) {

        if (0 == 0) {
            return 0;
        } /*TODO _Delay deaktiviert*/

        if (/*verarbeitungDelayed==0 || */zutrittskarte.delayedVorhanden == 0) {
            return 0;
        }

        weiterverarbeitungDelayed = 1;

        return 1;

    }

    /************checkStimmkarteDelayed*****************************************************************************/
    /**Checken, ob für EclStimmkarten bereits delayed vorhanden   */
    private int checkStimmkarteDelayed(EclStimmkarten stimmkarte) {

        if (0 == 0) {
            return 0;
        } /*TODO _Delay deaktiviert*/

        if (/*verarbeitungDelayed==0 || */stimmkarte.delayedVorhanden == 0) {
            return 0;
        }

        weiterverarbeitungDelayed = 1;

        return 1;

    }

    /************checkStimmkarteSecondDelayed******************************************************************/
    /**Checken, ob für EclStimmkartenSecond bereits delayed vorhanden     */
    private int checkStimmkarteSecondDelayed(EclStimmkartenSecond stimmkarteSecond) {

        if (0 == 0) {
            return 0;
        } /*TODO _Delay deaktiviert*/

        if (/*verarbeitungDelayed==0 ||*/ stimmkarteSecond.delayedVorhanden == 0) {
            return 0;
        }

        weiterverarbeitungDelayed = 1;

        return 1;

    }

    /*****************readPiMeldungsIdents*********************************************************************/
    /**piMeldungsIdentAktionaer und piMeldungsIdentGast einlesen, falls !=0*/
    private boolean readPiMeldungsIdents() {
        EclMeldung lMeldung = new EclMeldung();
        EclMeldung lMeldungGast = new EclMeldung();
        if (piMeldungsIdentAktionaer != 0) {/*Aktionär einlesen*/
            /*Hinweis: bei piKlasse==0 eigentlich unlogisch - solche ZutrittsIdent dürfen aber auch
             * keine meldungsIdentAKtionaer eingetragen haben => keine Sonderbehandlung erforderlich,
             * da diese Bedingung eh nicht zutrifft
             */

            lMeldung.meldungsIdent = piMeldungsIdentAktionaer;

            int erg = lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
            if (erg < 1) { /*Satz nicht vorhanden*/
                CaBug.drucke("BlWillenserklaerung.readPiMeldungsIdents - 001");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungsIdentNichtVorhanden;
                return false;
            }
            if (lDbBundle.dbMeldungen.meldungenArray[0].klasse == 0) {
                CaBug.drucke("BlWillenserklaerung.readPiMeldungsIdents - 002");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungAktionaerIstKeinAktionaer;
                return false;
            }
            this.piEclMeldungAktionaer = lDbBundle.dbMeldungen.meldungenArray[0];
            CaBug.druckeLog("piEclMeldungAktionaer neu eingelesen erg="+erg+" ", logDrucken, 10);
            CaBug.druckeLog("nach NeuEinlesen piEclMeldungAktionaer.willenserklaerung="+piEclMeldungAktionaer.willenserklaerung+" piEclMeldungAktionaer.meldungsIdent="+piEclMeldungAktionaer.meldungsIdent, logDrucken, 10);

        }

        if (piMeldungsIdentGast != 0) {/*Gast einlesen*/
            lMeldungGast.meldungsIdent = piMeldungsIdentGast;
            int erg = lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldungGast);
            if (erg < 1) { /*Satz nicht vorhanden*/
                CaBug.drucke("BlWillenserklaerung.readPiMeldungsIdents - 003");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungsIdentNichtVorhanden;
                return false;
            }
            if (lDbBundle.dbMeldungen.meldungenArray[0].klasse == 1) {
                CaBug.drucke("BlWillenserklaerung.readPiMeldungsIdents - 004");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungGastIstKeinGast;
                return false;
            }
            this.piEclMeldungGast = lDbBundle.dbMeldungen.meldungenArray[0];
        }

        return true;
    }

    /*************************************setzeDbBundle****************************************************
     * Wird benötigt, wenn einzelne Funktionen separat aufgerufen werden, bei denen nicht vorher DbBundle
     * übergeben wurde.
     * Also konkret aufrufen vor:
     * > einlesenVollmachtenAnDritte()
     */
    public void setzeDbBundle(DbBundle pDbBundle) {
        lDbBundle = pDbBundle;
        return;
    }

    /**********************************einlesenVollmachtenAnDritte*********************************************/
    /**Liest die Vollmachten an Dritte ein, die aktuell zum Aktionär in piMeldungsIdentAktionaer vorliegen.
     * Es werden sowohl widerrufene als auch gültige eingelesen.
     *
     * Delay:
     * > wenn ptForceNonDelay==true, dann werden nur die "nicht-Delayed"-Erklärungen berücksichtigt,
     *   ansonsten auch die Delayed-Erklärungen.
     * > Wenn Delayed-Erklärung vorhanden, dann wird weiterverarbeitungDelayed auf 1 gesetzt
     *
     * Falls Pendings vorliegen, wird rcWarnungPendingLiegtVor auf true gesetzt
     */
    public int einlesenVollmachtenAnDritte() {
        int i, gef;
        EclMeldung lMeldung = new EclMeldung();
        EclWillenserklaerung willenserklaerung;

        lMeldung.meldungsIdent = piMeldungsIdentAktionaer;

        lDbBundle.dbWillenserklaerung.leseZuMeldung(lMeldung);

        /*Anzahl erteilter Vollmachten an Dritte ermitteln*/
        rcVollmachtenAnDritteAnzahl = 0;
        for (i = 0; i < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i++) {
            willenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i);
            if (willenserklaerung.pending == 1) {
                rcWarnungPendingLiegtVor = true;
            }
            if (willenserklaerung.delayed == 1 && ptForceNonDelay == false) {
                /*weiterverarbeitungDelayed=1;*/ /*TODO _Delay deaktiviert*/}
            if (willenserklaerung.delayed != 2 && (ptForceNonDelay == false || willenserklaerung.delayed == 0)
                    && willenserklaerung.pending == 0) {
                /*Hinweis zum Pending: gemäß Verarbeitungsregeln (siehe allgemeine Hinweise) erfolgt
                 * Pending-Auflösung nicht automatisch. D.h. auch bei einem späteren Erteilen einer Vollmacht
                 * werden Pending-Stornos, die dadurch wirksam werden würden, nicht automatisch durchgeführt.
                 * Dementsprechend werden diese nicht in diese Liste aufgenommen.*/

                /*Bereits aufgelöste Delayed-Willenserklärungen sowie Pendings werden nicht mehr berücksichtigt*/
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.vollmachtAnDritte) {
                    rcVollmachtenAnDritteAnzahl++;
                }
            }
        }

        //		System.out.println("rcVollmachtenAnDritteAnzahl="+rcVollmachtenAnDritteAnzahl);

        /*Nun die Willenserklärungen durcharbeiten und im Array konsolidiert ablegen*/
        gef = 0;
        if (rcVollmachtenAnDritteAnzahl > 0) {
            rcVollmachtenAnDritte = new EclWillensErklVollmachtenAnDritte[rcVollmachtenAnDritteAnzahl];

            for (i = 0; i < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i++) {
                willenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i);
                if (willenserklaerung.delayed != 2) { /*Bereits aufgelöste Delayed-Willenserklärungen werden nicht mehr berücksichtigt*/

                    if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.vollmachtAnDritte) {
                        /*Vollmacht verarbeiten - d.h. neu in Array speichern*/
                        rcVollmachtenAnDritte[gef] = new EclWillensErklVollmachtenAnDritte();
                        rcVollmachtenAnDritte[gef].willenserklaerungErteilt = willenserklaerung;
                        int rc;
                        rc = lDbBundle.dbPersonenNatJur.read(willenserklaerung.bevollmaechtigterDritterIdent);
                        //System.out.println("Ident="+willenserklaerung.bevollmaechtigterDritterIdent);
                        if (rc < 1) {
                            CaBug.drucke("BlWillenserklaerung.einlesenVollmachtenAnDritte 001");
                        }
                        rcVollmachtenAnDritte[gef].bevollmaechtigtePerson = lDbBundle.dbPersonenNatJur
                                .PersonNatJurGefunden(0);
                        gef++;

                    } else {
                        if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufVollmachtAnDritte) {
                            /*Hinweis: ob delayed oder nicht, ist zu diesem Zeitpunkt egal.*/

                            /*Vollmacht-Widerruf verarbeiten, d.h. die zugehörige Vollmacht im Array suchen und dort Widerruf-Daten eintragen
                             * Achtung - Zuordnung erfolgt ausschließlich über willenserklaerung.verweisAufWillenserklaerung - d.h. dieses Feld muß immer gefüllt sein*/
                            int i1, gef1 = -1;
                            for (i1 = 0; i1 < gef; i1++) {
                                if (rcVollmachtenAnDritte[i1].willenserklaerungErteilt.willenserklaerungIdent == willenserklaerung.verweisAufWillenserklaerung) {
                                    rcVollmachtenAnDritte[i1].willenserklaerungStorniert = willenserklaerung;
                                    rcVollmachtenAnDritte[i1].wurdeStorniert = true;
                                    gef1 = i1;
                                }
                            }
                            if (gef1 == -1) {
                                /*Hinweis: dies kann tatsächlich auftreten, nämlich wenn nicht ein direkter Nachfolger
                                 * widerrufen wurde. In diesem Fall werden zwei Widerrufs-Willenserklärungen generiert:
                                 * Einmal Storno vom Stornierer zum Bevollmächtigten - dazu gibt es keine Vollmachtswillenserklärung!
                                 * und einmal als Folge-Willenserklärung die zu der tatsächlichen Willenserklärung zum bevollmächtigten,
                                 * die storniert wurde, paßt.
                                 */
                                /*CaBug.drucke("BlWillenserklaerung.einlesenVollmachtenAnDritte 002");*/}

                        }
                    }
                }

            }
        }
        return 0;
    }

    /**********************************einlesenZuordnungZuSammelkarten*********************************************/
    /**Liest die Zuordnungen an Sammelkarten ein, die aktuell zum Aktionär in piMeldungsIdentAktionaer vorliegen.
     * Es werden sowohl widerrufene, geänderte als auch gültige eingelesen.
     *
     * Delay:
     * > wenn ptForceNonDelay==true, dann werden nur die "nicht-Delayed"-Erklärungen berücksichtigt,
     *   ansonsten auch die Delayed-Erklärungen.
     * > Wenn Delayed-Erklärung vorhanden, dann wird weiterverarbeitungDelayed auf 1 gesetzt
     *
     * Falls Pendings vorliegen, wird rcWarnungPendingLiegtVor auf true gesetzt
     */
    public int einlesenZuordnungZuSammelkarten() {
        int i, gef;
        EclMeldung lMeldung = new EclMeldung();
        EclWillenserklaerung willenserklaerung;
        lMeldung.meldungsIdent = piMeldungsIdentAktionaer;

        lDbBundle.dbWillenserklaerung.leseZuMeldung(lMeldung);

        /*Anzahl erteilter Zuordnungen an Sammelkarte ermitteln - aktive und inaktive*/
        rcZuordnungZuSammelkarteAnzahl = 0;
        CaBug.druckeLog("lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden()="
                + lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(), logDrucken, 10);
        for (i = 0; i < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i++) {
            willenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i);
            if (willenserklaerung.pending == 1) {
                rcWarnungPendingLiegtVor = true;
            }
            if (willenserklaerung.delayed == 1 && ptForceNonDelay == false) {
                /*weiterverarbeitungDelayed=1;*/} /*TODO _Delay deaktiviert*/
            if (willenserklaerung.delayed != 2 && (ptForceNonDelay == false || willenserklaerung.delayed == 0)
                    && willenserklaerung.pending == 0) {
                /*Hinweis zum Pending: gemäß Verarbeitungsregeln (siehe allgemeine Hinweise) erfolgt
                 * Pending-Auflösung nicht automatisch. D.h. auch bei einem späteren Erteilen einer Vollmacht
                 * werden Pending-Stornos, die dadurch wirksam werden würden, nicht automatisch durchgeführt.
                 * Dementsprechend werden diese nicht in diese Liste aufgenommen.*/

                /*Bereits aufgelöste Delayed-Willenserklärungen sowie Pendings werden nicht mehr berücksichtigt*/
                //				if (EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.VollmachtUndWeisungAnSRV ||
                //						EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernWeisungAnSRV ||
                //						EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.VollmachtUndWeisungAnKIAV ||
                //						EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernWeisungAnKIAV ||
                //						EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.DauervollmachtAnKIAV ||
                //						EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.Briefwahl ||
                //						EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernBriefwahl ||
                //						EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.OrganisatorischMitWeisungInSammelkarte ||
                //						EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernWeisungOrganisatorischInSammelkarte
                if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.vollmachtUndWeisungAnSRV
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernWeisungAnSRV
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.vollmachtUndWeisungAnKIAV
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernWeisungAnKIAV
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.dauervollmachtAnKIAV
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.briefwahl
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernBriefwahl
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte
                        || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte) {
                    rcZuordnungZuSammelkarteAnzahl++;
                }
            }
        }

        //		System.out.println("rcVollmachtenAnDritteAnzahl="+rcVollmachtenAnDritteAnzahl);

        /*Nun die Willenserklärungen durcharbeiten und im Array konsolidiert ablegen*/
        gef = 0;
        if (rcZuordnungZuSammelkarteAnzahl > 0) {
            rcZuordnungZuSammelkarte = new EclWillensErklZuordnungZuSammelkarte[rcZuordnungZuSammelkarteAnzahl];

            for (i = 0; i < lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i++) {

                willenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i);
                if (willenserklaerung.delayed != 2) { /*Bereits aufgelöste Delayed-Willenserklärungen werden nicht mehr berücksichtigt*/

                    if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.vollmachtUndWeisungAnSRV
                            || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernWeisungAnSRV
                            || willenserklaerung.willenserklaerung == KonstWillenserklaerung.vollmachtUndWeisungAnKIAV
                            || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernWeisungAnKIAV
                            || willenserklaerung.willenserklaerung == KonstWillenserklaerung.dauervollmachtAnKIAV
                            || willenserklaerung.willenserklaerung == KonstWillenserklaerung.briefwahl
                            || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernBriefwahl
                            || willenserklaerung.willenserklaerung == KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte
                            || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte) {
                        //						if (EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.VollmachtUndWeisungAnSRV ||
                        //								EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernWeisungAnSRV ||
                        //								EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.VollmachtUndWeisungAnKIAV ||
                        //								EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernWeisungAnKIAV ||
                        //								EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.DauervollmachtAnKIAV ||
                        //								EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.Briefwahl ||
                        //								EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernBriefwahl ||
                        //								EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.OrganisatorischMitWeisungInSammelkarte ||
                        //								EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernWeisungOrganisatorischInSammelkarte
                        //								){
                        /*Vollmacht verarbeiten - d.h. neu in Array speichern*/

                        rcZuordnungZuSammelkarte[gef] = new EclWillensErklZuordnungZuSammelkarte();
                        rcZuordnungZuSammelkarte[gef].willenserklaerungErteilt = willenserklaerung;

                        int rc;
                        rc = lDbBundle.dbMeldungZuSammelkarte
                                .leseZuWillenserklaerung(willenserklaerung.willenserklaerungIdent);
                        //System.out.println("Ident="+willenserklaerung.bevollmaechtigterDritterIdent);
                        if (rc < 1) {
                            CaBug.drucke("BlWillenserklaerung.einlesenZuordnungZuSammelkarten 001");
                        }
                        rcZuordnungZuSammelkarte[gef].meldungZuSammelkarte = lDbBundle.dbMeldungZuSammelkarte
                                .meldungZuSammelkarteGefunden(0);
                        rcZuordnungZuSammelkarte[gef].zugeordneteSammelkarteIdent = rcZuordnungZuSammelkarte[gef].meldungZuSammelkarte.sammelIdent;
                        rcZuordnungZuSammelkarte[gef].aktiv = rcZuordnungZuSammelkarte[gef].meldungZuSammelkarte.aktiv;

                        gef++;
                        /*Falls Änderung, dann in geändertem Satz Verweis eintragen*/
                        if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernWeisungAnSRV
                                || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernWeisungAnKIAV
                                || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernBriefwahl
                                || willenserklaerung.willenserklaerung == KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte) {
                            //							if (
                            //									EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernWeisungAnSRV ||
                            //									EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernWeisungAnKIAV ||
                            //									EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernBriefwahl ||
                            //									EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.AendernWeisungOrganisatorischInSammelkarte
                            //									){

                            int i1;
                            for (i1 = 0; i1 < gef; i1++) {
                                if (rcZuordnungZuSammelkarte[i1].willenserklaerungErteilt.willenserklaerungIdent == willenserklaerung.verweisAufWillenserklaerung) {
                                    rcZuordnungZuSammelkarte[i1].wurdeGeaendert = true;
                                }

                            }

                        }
                    } else {
                        if (willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufVollmachtUndWeisungAnSRV
                                || willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV
                                || willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufDauervollmachtAnKIAV
                                || willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufBriefwahl
                                || willenserklaerung.willenserklaerung == KonstWillenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte) {
                            //							if (
                            //									EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.WiderrufVollmachtUndWeisungAnSRV ||
                            //									EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.WiderrufVollmachtUndWeisungAnKIAV ||
                            //									EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.WiderrufDauervollmachtAnKIAV ||
                            //									EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.WiderrufBriefwahl ||
                            //									EnWillenserklaerung.fromClWillenserklaerung(willenserklaerung)==EnWillenserklaerung.WiderrufOrganisatorischMitWeisungInSammelkarte
                            //									){
                            /*Hinweis: ob delayed oder nicht, ist zu diesem Zeitpunkt egal.*/

                            /*Vollmacht-Widerruf verarbeiten, d.h. die zugehörige Vollmacht im Array suchen und dort Widerruf-Daten eintragen
                             * Achtung - Zuordnung erfolgt ausschließlich über willenserklaerung.verweisAufWillenserklaerung - d.h. dieses Feld muß immer gefüllt sein*/
                            int i1, gef1 = -1;
                            for (i1 = 0; i1 < gef; i1++) {
                                if (rcZuordnungZuSammelkarte[i1].willenserklaerungErteilt.willenserklaerungIdent == willenserklaerung.verweisAufWillenserklaerung) {
                                    rcZuordnungZuSammelkarte[i1].willenserklaerungStorniert = willenserklaerung;
                                    rcZuordnungZuSammelkarte[i1].wurdeStorniert = true;
                                    gef1 = i1;
                                }
                            }
                            if (gef1 == -1) {
                                CaBug.drucke("BlWillenserklaerung.einlesenZuordnungZuSammelkarten 002");
                            }

                        }
                    }
                }

            }
        }

        return 0;
    }

    /**************************************evtlEinlesenMeldung()***********************************************/
    /** Prüfen, ob piEclMeldung* eingelesen ist. Wenn nicht, dann anhand der Parameter (in dieser Prio) einlesen:
     * > piMeldungsIdent*
     * > piZutrittsIdent und piKlasse
     * > piZutrittsIdent (Klasse wird automatisch ermittelt)
     * > piStimmkarte, falls pStimmkarteVerwenden==1
     * > piStimmkarteSecond, falls pStimmkarteSecondVerwenden==1
     *
     * Nach dem Prüfen ist in jedem Fall belegt (falls Identifizierung über Parameter möglich):
     * > piEclMeldungAktionaer und/oder piEclMeldungGast
     * > piMeldungsIdentAktionaer und/oder piMeldungsIdentGast
     * > Falls bei ZutrittsIdent oder StimmkartenIdent das Delayed-Kennzeichen gesetzt, dann zusätzlich auch:
     * 		> weiterverarbeitungDelayed=1
     * 		> Hinweis: es wird jedoch nicht überprüft, ob die meldung selbst delayed ist, da dies für Gast
     * 			und Aktionär abweichend sein kann und in dieser Funktion nicht klar ist, welche der beiden
     * 			weiterverarbeitet ist.
     *
     * Zusätzlich ggf. belegt:
     * > lDbBundle.dbZutrittskarten.zutrittskartenGefunden(0), falls piZutrittsIdent gefüllt war
     *
     * Hinweis: Es wird nicht überprüft, ob die Meldungen möglicherweise gesperrt  sind!
     * Lediglich bei ZutrittsIdent und Stimmkarten wird dies überprüft und als Fehlermeldung
     * zurückgegeben.
     */
    private void evtlEinlesenMeldung() {
        /*Last Error = 31*/

        EclMeldung lMeldung = new EclMeldung();
        EclMeldung lMeldungGast = new EclMeldung();

        if (this.ptForceIdentifikationIstMeldungsIdent == true) {

            /*Für "rekursiven" Aufruf (z.B. Nachbuchen von Pending, Delayed) wird
             * Identifikation komplett übernommen, d.h. nur meldung anhand von
             * piMeldungsIdent eingelesen - alle andere pi-Parameter bleiben
             * unverändert erhalten
             *
             * Dies bedeutet aber auch, das Gültigkeitsprüfungen von zutrittsIdent / Stimmkarten
             * nicht mehr durchgeführt werden bei dieser Art Buchung - was auch richtig ist (siehe
             * Beschreibung sperrenZutrittsIdent - Delay)
             *
             * In diesem Fall gilt auch: Ausgangspunkt ist immer das "nicht-Delayed", da ja die
             * Delayed-Übertragungn auf diese Basis übertragen werden.
             * */
            this.identifikationDurch = this.ptUebernehmeIdentifikation;
            if (piMeldungsIdentAktionaer != 0) {
                lMeldung.meldungsIdent = piMeldungsIdentAktionaer;
                int erg = lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
                if (erg < 1) { /*Satz nicht vorhanden*/
                    CaBug.drucke("BlWillenserklaerung.evtlEinlesenMeldung - 001");
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungsIdentNichtVorhanden;
                    return;
                }
                if (lMeldung.klasse == 0) {
                    CaBug.drucke("BlWillenserklaerung.evtlEinlesenMeldung - 002");
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungAktionaerIstKeinAktionaer;
                    return;
                }
                this.piEclMeldungAktionaer = lDbBundle.dbMeldungen.meldungenArray[0];
            }
            if (piMeldungsIdentGast != 0) {
                lMeldungGast.meldungsIdent = piMeldungsIdentGast;
                int erg = lDbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldungGast);
                if (erg < 1) { /*Satz nicht vorhanden*/
                    CaBug.drucke("BlWillenserklaerung.evtlEinlesenMeldung - 003");
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungsIdentNichtVorhanden;
                    return;
                }
                if (lMeldungGast.klasse == 1) {
                    CaBug.drucke("BlWillenserklaerung.evtlEinlesenMeldung - 004");
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungGastIstKeinGast;
                    return;
                }
                this.piEclMeldungGast = lDbBundle.dbMeldungen.meldungenArray[0];

            }
            //			if (piZutrittsIdent.zutrittsIdent!=""){
            if (piZutrittsIdent.zutrittsIdent.compareTo("") != 0) {
                if (piKlasse == 1) {/*Aktionär*/
                    lDbBundle.dbZutrittskarten.readAktionaer(piZutrittsIdent, 1);
                } else {
                    lDbBundle.dbZutrittskarten.readGast(piZutrittsIdent, 1);
                }

            }
            return;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;

        if (piEclMeldungAktionaer != null || piEclMeldungGast != null) {
            /***************Meldung ist bereits eingelesen ***************
             * keine weitere Aktion erforderlich, außer piMeldungsIdent*
             * füllen und checken, ob Meldung nicht gesperrt ist*/
            if (piEclMeldungAktionaer != null) {
                if (piEclMeldungAktionaer.klasse == 0) {
                    CaBug.drucke("BlWillenserklaerung.evtlEinlesenMeldung - 005");
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungAktionaerIstKeinAktionaer;
                    return;
                }
                this.piMeldungsIdentAktionaer = piEclMeldungAktionaer.meldungsIdent;
            }
            if (piEclMeldungGast != null) {
                if (piEclMeldungGast.klasse == 1) {
                    CaBug.drucke("BlWillenserklaerung.evtlEinlesenMeldung - 006");
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungGastIstKeinGast;
                    return;
                }
                this.piMeldungsIdentGast = piEclMeldungGast.meldungsIdent;
            }
            /*Nun sicherheitshalber nochmal einlesen, da möglicherweise verändert durch mittlerweile andere Transaktion!*/
            if (!readPiMeldungsIdents()) {
                return;
            }
            identifikationDurch = 0;
            return;
        }

        if (piMeldungsIdentAktionaer != 0 || piMeldungsIdentGast != 0) {

            /***************************Anhand piMeldungsIdent einlesen***********************/
            if (!readPiMeldungsIdents()) {
                return;
            }
            identifikationDurch = 1;
            return;
        }

        //		if (piKlasse!=-1 && piZutrittsIdent.zutrittsIdent!=""){
        if (piKlasse != -1 && piZutrittsIdent.zutrittsIdent.compareTo("") != 0) {
            /**************************Anhand Klasse und pZutrittsIdent einlesen****************************/
            klasse = piKlasse;

            if (piKlasse == 1) {/*Aktionär*/
                lDbBundle.dbZutrittskarten.readAktionaer(piZutrittsIdent, 1);
            } else {
                lDbBundle.dbZutrittskarten.readGast(piZutrittsIdent, 1);
            }

            if (lDbBundle.dbZutrittskarten.anzErgebnis() < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmZutrittsIdentNichtVorhanden;
                return;
            }

            if ((checkZutrittsIdentDelayed(lDbBundle.dbZutrittskarten.ergebnisPosition(0)) == 0
                    && lDbBundle.dbZutrittskarten.ergebnisPosition(0).zutrittsIdentWurdeGesperrt())
                    || (checkZutrittsIdentDelayed(lDbBundle.dbZutrittskarten.ergebnisPosition(0)) == 1
                            && lDbBundle.dbZutrittskarten.ergebnisPosition(0).zutrittsIdentWurdeGesperrt_Delayed())) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmZutrittsIdentGesperrt;
                return;
            }

            piMeldungsIdentAktionaer = lDbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentAktionaer;
            piMeldungsIdentGast = lDbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentGast;
            /*TODO Ggf. Delay aus Delayter Zutrittskarte setzen!*/

            if (!readPiMeldungsIdents()) {
                return;
            }
            identifikationDurch = 2;
            return;
        }

        //		if (piKlasse==-1 && piZutrittsIdent.zutrittsIdent!=""){
        if (piKlasse == -1 && piZutrittsIdent.zutrittsIdent.compareTo("") != 0) {
            /****************** Nur anhand ZutrittsIdent einlesen - Klasse unbekannt.**************************
             * Ist möglicherweise bei überschneidenden Nummernkreisen
             * für die beiden Klassen nicht eindeutig => Fehlermeldung*/

            lDbBundle.dbZutrittskarten.read_alleVersionen(piZutrittsIdent);
            if (lDbBundle.dbZutrittskarten.anzErgebnis() < 1) { /*Überhaupt keine vorhanden*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmZutrittsIdentNichtVorhanden;
                return;
            }
            int i;
            int gefGastKlasse = -1;
            int gefAktionaerKlasse = -1;
            int gefGastKlasseGesperrt = 0;
            int gefAktionaerKlasseGesperrt = 0;

            for (i = 0; i < lDbBundle.dbZutrittskarten.anzErgebnis(); i++) {
                if (lDbBundle.dbZutrittskarten.ergebnisPosition(
                        i).zutrittsIdentVers == 0) {/*"Versionierte" als gesperrte werden nicht berücksichtigt!*/
                    if (lDbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentKlasse == 1) {
                        gefAktionaerKlasse = i;
                        if (checkZutrittsIdentDelayed(lDbBundle.dbZutrittskarten.ergebnisPosition(0)) == 0) {
                            gefAktionaerKlasseGesperrt = lDbBundle.dbZutrittskarten
                                    .ergebnisPosition(i).zutrittsIdentIstGesperrt;
                        } else {
                            gefAktionaerKlasseGesperrt = lDbBundle.dbZutrittskarten
                                    .ergebnisPosition(i).zutrittsIdentIstGesperrt_Delayed;
                        }
                        gefAktionaerKlasseGesperrt = lDbBundle.dbZutrittskarten
                                .ergebnisPosition(i).zutrittsIdentIstGesperrt;
                    }
                    if (lDbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentKlasse == 0) {
                        gefGastKlasse = i;
                        if (checkZutrittsIdentDelayed(lDbBundle.dbZutrittskarten.ergebnisPosition(0)) == 0) {
                            gefGastKlasseGesperrt = lDbBundle.dbZutrittskarten
                                    .ergebnisPosition(i).zutrittsIdentIstGesperrt;
                        } else {
                            gefGastKlasseGesperrt = lDbBundle.dbZutrittskarten
                                    .ergebnisPosition(i).zutrittsIdentIstGesperrt_Delayed;

                        }
                    }
                }
            }
            if (gefGastKlasse == -1 && gefAktionaerKlasse == -1) {
                /*FÜr keine Klasse gültige ZutrittsIdent vorhanden => Nur versionierte vorhanden*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmZutrittsIdentGesperrt;
                return;
            }

            if (gefGastKlasse != -1 && gefGastKlasseGesperrt == 0 && gefAktionaerKlasse != -1
                    && gefAktionaerKlasseGesperrt == 0) {
                /*Für beide Klassen sind nicht-stornierte ZutrittsIdent gespeichert => nicht eindeutig*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmZutrittsIdentNichtEindeutig;
                return;
            }
            if ((gefGastKlasse == -1 && gefAktionaerKlasseGesperrt == KonstStimmkarteIstGesperrt.gesperrt) /*Gast nicht gefunden, und Aktionär gesperrt*/
                    || (gefAktionaerKlasse == -1
                            && gefGastKlasseGesperrt == KonstStimmkarteIstGesperrt.gesperrt)) /*Aktionär nicht gefunden, und Gast gesperrt*/
            {/*Nur ZutrittsIdent für eine Klasse gefunden, und die ist storniert*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmZutrittsIdentGesperrt;
                return;
            }

            /*Hier ist sichergestellt: eindeutige ZutrittsIdent gefunden*/
            //			int gef=0;
            if (gefAktionaerKlasse != -1) { /*Aktionärs-ZutrittsIdent gefunden*/
                piKlasse = 1;
                klasse = 1;/*gef=gefAktionaerKlasse;*/
                lDbBundle.dbZutrittskarten.readAktionaer(piZutrittsIdent, 1);

            } else { /*Gast-ZutrittsIdent gefunden*/
                piKlasse = 0;
                klasse = 0;/*gef=gefGastKlasse;*/
                lDbBundle.dbZutrittskarten.readGast(piZutrittsIdent, 1);
            }

            /*Die betreffende ZutrittsIdent ist nun immer in [0] - da ja neu eingelesen wurde!*/
            piMeldungsIdentAktionaer = lDbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentAktionaer;
            piMeldungsIdentGast = lDbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentGast;
            if (checkZutrittsIdentDelayed(lDbBundle.dbZutrittskarten.ergebnisPosition(0)) == 0) {
            } else {
                /*weiterverarbeitungDelayed=1;*/ /*TODO _Delay deaktiviert*/
            }
            if (!readPiMeldungsIdents()) {
                return;
            }
            identifikationDurch = 3;
            return;
        }

        //		if (piStimmkarte!=""){
        if (piStimmkarte.compareTo("") != 0) {
            /**************************Anhand Stimmkarte einlesen*****************************/
            lDbBundle.dbStimmkarten.read(piStimmkarte);

            if (lDbBundle.dbStimmkarten.anzErgebnis() < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteNichtVorhanden;
                return;
            }

            if ((checkStimmkarteDelayed(lDbBundle.dbStimmkarten.ergebnisPosition(0)) == 0
                    && lDbBundle.dbStimmkarten.ergebnisPosition(0).stimmkarteIstGesperrt == 1)
                    || (checkStimmkarteDelayed(lDbBundle.dbStimmkarten.ergebnisPosition(0)) == 1
                            && lDbBundle.dbStimmkarten.ergebnisPosition(0).stimmkarteIstGesperrt_Delayed == 1)) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteGesperrt;
                return;
            }

            piMeldungsIdentAktionaer = lDbBundle.dbStimmkarten.ergebnisPosition(0).meldungsIdentAktionaer;
            piMeldungsIdentGast = lDbBundle.dbStimmkarten.ergebnisPosition(0).meldungsIdentGast;
            if (checkStimmkarteDelayed(lDbBundle.dbStimmkarten.ergebnisPosition(0)) == 0) {
            } else {
                /*weiterverarbeitungDelayed=1;*/ /*TODO _Delay deaktiviert*/
            }

            if (!readPiMeldungsIdents()) {
                return;
            }
            identifikationDurch = 4;
            return;
        }

        //		if (piStimmkarteSecond!=""){
        if (piStimmkarteSecond.compareTo("") != 0) {
            /**************************Anhand Stimmkarte einlesen*****************************/
            lDbBundle.dbStimmkartenSecond.read(piStimmkarteSecond);

            if (lDbBundle.dbStimmkartenSecond.anzErgebnis() < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteSecondNichtVorhanden;
                return;
            }

            if ((checkStimmkarteSecondDelayed(lDbBundle.dbStimmkartenSecond.ergebnisPosition(0)) == 0
                    && lDbBundle.dbStimmkartenSecond.ergebnisPosition(0).stimmkarteSecondIstGesperrt == 1)
                    || (checkStimmkarteSecondDelayed(lDbBundle.dbStimmkartenSecond.ergebnisPosition(0)) == 1
                            && lDbBundle.dbStimmkartenSecond
                                    .ergebnisPosition(0).stimmkarteSecondIstGesperrt_Delayed == 1)) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteSecondGesperrt;
                return;
            }

            piMeldungsIdentAktionaer = lDbBundle.dbStimmkartenSecond.ergebnisPosition(0).meldungsIdentAktionaer;
            piMeldungsIdentGast = lDbBundle.dbStimmkartenSecond.ergebnisPosition(0).meldungsIdentGast;
            if (checkStimmkarteSecondDelayed(lDbBundle.dbStimmkartenSecond.ergebnisPosition(0)) == 0) {
            } else {
                /*weiterverarbeitungDelayed=1;*/ /*TODO _Delay deaktiviert*/
            }

            if (!readPiMeldungsIdents()) {
                return;
            }
            identifikationDurch = 5;
            return;
        }

        CaBug.drucke("BlWillenserklaerung.evtlEinlesenMeldung - 031");
        this.rcIstZulaessig = false;
        this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungNichHinreichendSpezifiziert;
        return;
    }

    /**************************************************prepareWillenserklaerung*****************************/
    /**Willenserklaerung mit willensart erzeugen und mit Standard-Werten belegen
     *
     * */

    private EclWillenserklaerung preparedWillenserklaerung = null;
    private EclWillenserklaerungZusatz preparedWillenserklaerungZusatz = null;

    private void prepareWillenserklaerung(int willensart) {
        preparedWillenserklaerung = new EclWillenserklaerung();
        preparedWillenserklaerungZusatz = new EclWillenserklaerungZusatz();
        preparedWillenserklaerung.willenserklaerung = willensart;

        preparedWillenserklaerung.meldungsIdent = piMeldungsIdentAktionaer;
        preparedWillenserklaerung.meldungsIdentGast = piMeldungsIdentGast;

        preparedWillenserklaerung.identifikationDurch = identifikationDurch;
        preparedWillenserklaerung.identifikationKlasse = klasse;
        preparedWillenserklaerung.identifikationZutrittsIdent = piZutrittsIdent.zutrittsIdent;
        preparedWillenserklaerung.identifikationZutrittsIdentNeben = piZutrittsIdent.zutrittsIdentNeben;
        preparedWillenserklaerung.identifikationStimmkarte = piStimmkarte;
        preparedWillenserklaerung.identifikationStimmkarteSecond = piStimmkarteSecond;

        if (piEclMeldungAktionaer != null) {
            preparedWillenserklaerung.stimmen = piEclMeldungAktionaer.stimmen;
            preparedWillenserklaerung.aktien = piEclMeldungAktionaer.stueckAktien;
        }

        preparedWillenserklaerung.erteiltAufWeg = pErteiltAufWeg;
        if (!this.pErteiltZeitpunkt.isEmpty()) {
            preparedWillenserklaerung.veraenderungszeit = this.pErteiltZeitpunkt;
        } else {
            preparedWillenserklaerung.veraenderungszeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
        }

        preparedWillenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;

        preparedWillenserklaerung.verweisart = ptVerweisart;
        preparedWillenserklaerung.verweisAufWillenserklaerung = ptVerweisAufWillenserklaerung;

        preparedWillenserklaerung.folgeBuchungFuerIdent = pFolgeFuerWillenserklaerungIdent;

        preparedWillenserklaerungZusatz.quelle = pQuelle;

        /*Hinweis: wird ggf. von den einzelnen "Nutzern" nach Aufruf von prepareWillenserklaerung
         * nochmal überschrieben - siehe Beschreibung "mehrfach abhängig" bei pFolgeFuerWillenserklaerungIdent
         */
        return;
    }

    /************Für zweite Willenserklärung - sicherheitshalber unabhängig!**********/
    private EclWillenserklaerung preparedWillenserklaerung2 = null;
    private EclWillenserklaerungZusatz preparedWillenserklaerungZusatz2 = null;

    private void prepareWillenserklaerung2(int willensart) {
        preparedWillenserklaerung2 = new EclWillenserklaerung();
        preparedWillenserklaerungZusatz2 = new EclWillenserklaerungZusatz();
        preparedWillenserklaerung2.willenserklaerung = willensart;

        preparedWillenserklaerung2.meldungsIdent = piMeldungsIdentAktionaer;
        preparedWillenserklaerung2.meldungsIdentGast = piMeldungsIdentGast;

        preparedWillenserklaerung2.identifikationDurch = identifikationDurch;
        preparedWillenserklaerung2.identifikationKlasse = klasse;
        preparedWillenserklaerung2.identifikationZutrittsIdent = piZutrittsIdent.zutrittsIdent;
        preparedWillenserklaerung2.identifikationZutrittsIdentNeben = piZutrittsIdent.zutrittsIdentNeben;
        preparedWillenserklaerung2.identifikationStimmkarte = piStimmkarte;
        preparedWillenserklaerung2.identifikationStimmkarteSecond = piStimmkarteSecond;

        if (piEclMeldungAktionaer != null) {
            preparedWillenserklaerung2.stimmen = piEclMeldungAktionaer.stimmen;
            preparedWillenserklaerung2.aktien = piEclMeldungAktionaer.stueckAktien;
        }

        preparedWillenserklaerung2.erteiltAufWeg = pErteiltAufWeg;
        if (!this.pErteiltZeitpunkt.isEmpty()) {
            preparedWillenserklaerung2.veraenderungszeit = this.pErteiltZeitpunkt;
        } else {
            preparedWillenserklaerung2.veraenderungszeit = CaDatumZeit.DatumZeitStringFuerDatenbank();
        }

        preparedWillenserklaerung2.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;

        preparedWillenserklaerung2.verweisart = ptVerweisart;
        preparedWillenserklaerung2.verweisAufWillenserklaerung = ptVerweisAufWillenserklaerung;

        preparedWillenserklaerung2.folgeBuchungFuerIdent = pFolgeFuerWillenserklaerungIdent;

        preparedWillenserklaerungZusatz2.quelle = pQuelle;

        /*Hinweis: wird ggf. von den einzelnen "Nutzern" nach Aufruf von prepareWillenserklaerung
         * nochmal überschrieben - siehe Beschreibung "mehrfach abhängig" bei pFolgeFuerWillenserklaerungIdent
         */
        return;
    }

    /************************anmeldungAusAktienregister*************************************/
    /**Für den Aktienregistereintrag in pEclAktienregisterEintrag wird eine Anmeldung durchgeführt
     * Eingabeparameter:
     * pEclAktienregisterEintrag = Aktienregistereintrag, für den Anmeldung durchgeführt werden soll
     * pAktienAnmelden = Aktienzahl, die angemeldet werden sollen (-1 = "Rest", bzw. "alles")
     * pAnmeldungFix = Anmeldebestand soll "Fix" erfolgen (d.h. keine Erhöhung, falls sich Aktienregisterbestand erhöht)
     * pAnzahlAnmeldungen = Anzahl Anmeldungen, auf denen der anzumeldende Bestand aufgesplittet werden soll
     */
    /***************************************************************************************/
    private long anmeldungAusAktienregister_AktienAnmelden; /*Aktien, die letztendlich angemeldet werden; wird in _pruefe
                                                            ermittelt*/

    public void anmeldungAusAktienregister_pruefe(DbBundle dbBundle) {

        this.lDbBundle = dbBundle;

        int erg;
        long aktienAngemeldet = 0;
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.aktionaersnummer = pEclAktienregisterEintrag.aktionaersnummer;
        erg = dbBundle.dbMeldungen.leseZuAktionaersnummer(lMeldung);

        /*Ist Aktienregistereintrag bereits angemeldet?
         * Falls ja: bestehende Anmeldungen in rcMeldungen zurückliefern*/
        if (erg > 0) {

            /*Noch nicht angemeldeter Bestand vorhanden?*/
            int i;
            for (i = 0; i < dbBundle.dbMeldungen.meldungenArray.length; i++) {
                aktienAngemeldet += dbBundle.dbMeldungen.meldungenArray[i].stueckAktien;
            }
            if (aktienAngemeldet >= pEclAktienregisterEintrag.stueckAktien) {/*Bereits alles angemeldet - kein Anmeldung mehr möglich*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmGesamterBestandBereitsAngemeldet; 
                return;
            }
        } else {
        aktienAngemeldet = 0;
        }

        if (pAktienAnmelden != -1) { /*Festen Bestand anmelden*/
            if (pAnmeldungFixRest==false) {
                if (aktienAngemeldet
                        + pAktienAnmelden > pEclAktienregisterEintrag.stueckAktien) { /*Nicht mehr genügend nichtangemeldete Aktien*/
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = CaFehler.pmNichtAngemeldeterBestandZuKlein;
                    return;
                }
            }
            else {
                long aktienAnmeldenNeu=
                        pEclAktienregisterEintrag.stueckAktien - aktienAngemeldet;
                if (aktienAnmeldenNeu<=0) {
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = CaFehler.pmGesamterBestandBereitsAngemeldet; 
                    return;
                }
                pAktienAnmelden=aktienAnmeldenNeu;
            }
            if (pAktienAnmelden < pAnzahlAnmeldungen) { /*Nicht genügend Aktien nichtangemeldet für Split*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmNichtGenuegendAktienFuerSplit;
                return;
            }
            anmeldungAusAktienregister_AktienAnmelden = pAktienAnmelden;
        } else {/*Restbestand anmelden*/
            if (pAnzahlAnmeldungen > pEclAktienregisterEintrag.stueckAktien - aktienAngemeldet) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmNichtGenuegendAktienFuerSplit;
                return;
            }
            anmeldungAusAktienregister_AktienAnmelden = pEclAktienregisterEintrag.stueckAktien - aktienAngemeldet;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;

    }

    public void anmeldungAusAktienregister(DbBundle dbBundle) {

        try {

        int i;
        
        /*Prüfen aufrufen*/
        willenserklaerungSperren(dbBundle);

        anmeldungAusAktienregister_pruefe(dbBundle);
        
        
        
        
//        System.out.println("----------------Wait-----------------------");
//        try {
//            Thread.sleep(20000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("----------------Wait-----------------------");

        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        if (pEclAktienregisterEintrag.personNatJur != 0) {
            pPersonNatJurFuerAnmeldungVerwenden = pEclAktienregisterEintrag.personNatJur;
        }
        String landesCode = "";
        dbBundle.dbStaaten.readId(pEclAktienregisterEintrag.staatId);
        if (dbBundle.dbStaaten.anzErgebnis() > 0) {
            landesCode = dbBundle.dbStaaten.ergebnisPosition(0).code;
        }

        /*Aktienbestand verteilen*/
        long[] aktienProAnmeldung = new long[pAnzahlAnmeldungen];
        long aktienProEinzelneAnmeldung = anmeldungAusAktienregister_AktienAnmelden / pAnzahlAnmeldungen;
        long aktienrest = anmeldungAusAktienregister_AktienAnmelden; /*Noch nicht verteilte Aktien - am Schluß für letzte Anmeldung*/

        for (i = 0; i < pAnzahlAnmeldungen - 1; i++) {
            aktienProAnmeldung[i] = aktienProEinzelneAnmeldung;
            aktienrest -= aktienProEinzelneAnmeldung;
        }
        aktienProAnmeldung[pAnzahlAnmeldungen - 1] = aktienrest;

        /*Anmeldebestand in meldungen (mit Anhängsel PersonenNatJur) erzeugen*/
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungAktiv = 1;
        lMeldung.klasse = 1;
        lMeldung.aktionaersnummer = pEclAktienregisterEintrag.aktionaersnummer;
        lMeldung.aktienregisterIdent = pEclAktienregisterEintrag.aktienregisterIdent;
        lMeldung.meldungstyp = 1;
        lMeldung.gattung = pEclAktienregisterEintrag.getGattungId();
        CaBug.druckeLog("lMeldung.gattung=" + lMeldung.gattung, logDrucken, 10);
        lMeldung.besitzart = pEclAktienregisterEintrag.besitzart;
        lMeldung.stimmausschluss = pEclAktienregisterEintrag.stimmausschluss;
        lMeldung.anrede = pEclAktienregisterEintrag.anredeId;
        lMeldung.titel = pEclAktienregisterEintrag.titel;
        if (pEclAktienregisterEintrag.istJuristischePerson == 1) {
            lMeldung.name = CaString.trunc(pEclAktienregisterEintrag.name1 + " " + pEclAktienregisterEintrag.name2 + " "
                    + pEclAktienregisterEintrag.name3, 80).trim();
            lMeldung.vorname = "";

        } else {/*Natürliche Person*/
            lMeldung.name = CaString.trunc(pEclAktienregisterEintrag.nachname, 80);
            lMeldung.vorname = CaString.trunc(pEclAktienregisterEintrag.vorname, 80);
        }
        lMeldung.land = landesCode;
        lMeldung.ort = pEclAktienregisterEintrag.ort;
        if (!pEclAktienregisterEintrag.postleitzahlPostfach.isEmpty()
                && pEclAktienregisterEintrag.postleitzahlPostfach.compareTo("0") != 0) {
            /*Postfach*/
            lMeldung.plz = pEclAktienregisterEintrag.postleitzahlPostfach;
            lMeldung.strasse = "Postfach " + pEclAktienregisterEintrag.postfach;
        } else {/*Normale Adresse*/
            lMeldung.strasse = pEclAktienregisterEintrag.strasse;
            lMeldung.plz = pEclAktienregisterEintrag.postleitzahl;
        }

        /*Kompletter Block lahmgelegt, da Versandadresse immer aus der aufbereiteten Versandadresse im Aktienregister genommen wird!*/
        //		if (pEclAktienregisterEintrag.versandAbweichend!=0){/*Es gibt abweichende Versandadresse - abspeichern*/
        //			EclPersonenNatJurVersandadresse lPersonenNatJurVersandadresse=new EclPersonenNatJurVersandadresse();
        //			lPersonenNatJurVersandadresse.versandAbweichend=pEclAktienregisterEintrag.versandAbweichend;
        //			lPersonenNatJurVersandadresse.anredeIdVersand=pEclAktienregisterEintrag.anredeIdVersand;
        //			lPersonenNatJurVersandadresse.titelVersand=pEclAktienregisterEintrag.titelVersand;
        //			lPersonenNatJurVersandadresse.name3Versand=pEclAktienregisterEintrag.name3Versand;
        //			lPersonenNatJurVersandadresse.name2Versand=pEclAktienregisterEintrag.name2Versand;
        //
        //			lPersonenNatJurVersandadresse.nameVersand=pEclAktienregisterEintrag.nachnameVersand;
        //			lPersonenNatJurVersandadresse.vornameVersand=pEclAktienregisterEintrag.vornameVersand;
        //			lPersonenNatJurVersandadresse.ortVersand=pEclAktienregisterEintrag.ortVersand;
        //			lPersonenNatJurVersandadresse.staatIdVersand=pEclAktienregisterEintrag.staatIdVersand;
        //
        //			if (!pEclAktienregisterEintrag.postleitzahlPostfachVersand.isEmpty() && pEclAktienregisterEintrag.postleitzahlPostfachVersand.compareTo("0")!=0){
        //				/*Postfach*/
        //				lPersonenNatJurVersandadresse.postleitzahlVersand=pEclAktienregisterEintrag.postleitzahlPostfachVersand;
        //				lPersonenNatJurVersandadresse.strasseVersand="Postfach "+pEclAktienregisterEintrag.postfachVersand;
        //			}
        //			else{/*Normale Adresse*/
        //				lPersonenNatJurVersandadresse.strasseVersand=pEclAktienregisterEintrag.strasseVersand;
        //				lPersonenNatJurVersandadresse.postleitzahlVersand=pEclAktienregisterEintrag.postleitzahlVersand;
        //			}
        //
        //
        ////			lPersonenNatJurVersandadresse.strasseVersand=pEclAktienregisterEintrag.strasseVersand;
        //			dbBundle.dbPersonenNatJurVersandadresse.insert(lPersonenNatJurVersandadresse);
        //			lMeldung.identVersandadresse=lPersonenNatJurVersandadresse.ident;
        //		}
        //		else{
        //			lMeldung.identVersandadresse=0;
        //		}

        lMeldung.loginKennung = lMeldung.aktionaersnummer;

        BlKennung lkennung = new BlKennung(dbBundle);
        lkennung.neueKennungUndOeffentlicheID(lMeldung.name, lMeldung.vorname);
        lMeldung.oeffentlicheID = lkennung.ergebnisOeffentlicheID;
        lMeldung.personenNatJurIdent = this.pPersonNatJurFuerAnmeldungVerwenden;
        /*fehlt: mailadresse, kommunikationssprache*/

        int hauptwillenserklaerung = 0;
        rcMeldungen = new int[pAnzahlAnmeldungen];
        rcEclMeldungen=new EclMeldung[pAnzahlAnmeldungen];
        
        for (i = 0; i < pAnzahlAnmeldungen; i++) {
            lMeldung.stueckAktien = aktienProAnmeldung[i];
            lMeldung.stimmen = aktienProAnmeldung[i];
            if (pAnmeldungFix == true) {
                lMeldung.fixAnmeldung = 1;
            } else {
                lMeldung.fixAnmeldung = 0;
            }
            /*Hinweis: PersonenNatJur ist am Anfang 0, wird neuvergeben, und bleibt für Folge
             * inserts erhalten => alle Meldungen verweisen auf selbe PersonenNatJur
             */
            lMeldung.meldungsIdent = 0;
            /*Muß auf 0 gesetzt werden, da sonst keine neue meldungsIdent beim Einfügen verwendet wird!!*/
            /*erg=*/dbBundle.dbMeldungen.insert(lMeldung);

            pPersonNatJurFuerAnmeldungVerwenden = lMeldung.personenNatJurIdent;
            rcMeldungen[i] = lMeldung.meldungsIdent;
            rcEclMeldungen[i]=new EclMeldung();
            lMeldung.copyTo(rcEclMeldungen[i]);

            /*Zu jeder Anmeldung auch eine Willenserklärung speichern*/
            prepareWillenserklaerung(KonstWillenserklaerung.anmeldungAusAktienregister);
            EclWillenserklaerung willenserklaerung = this.preparedWillenserklaerung;
            EclWillenserklaerungZusatz willenserklaerungZusatz = this.preparedWillenserklaerungZusatz;

            willenserklaerung.meldungsIdent = lMeldung.meldungsIdent;
            willenserklaerungZusatz.aktienregisterIdent = pEclAktienregisterEintrag.aktienregisterIdent;
            willenserklaerungZusatz.aktienAnmelden = pAktienAnmelden;
            if (pAnmeldungFix == true) {
                willenserklaerungZusatz.anmeldungFix = 1;
            } else {
                willenserklaerungZusatz.anmeldungFix = 0;
            }
            willenserklaerungZusatz.anzahlAnmeldungen = pAnzahlAnmeldungen;
            if (i > 0) {
                willenserklaerung.folgeBuchungFuerIdent = hauptwillenserklaerung;
            }
            /*erg=*/dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
            if (i == 0) {
                hauptwillenserklaerung = willenserklaerung.willenserklaerungIdent;
                rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;
            }

            //			/*Letzte Willenserklärung in lMeldung updaten*/
            //			lMeldung.letzteWillenserklaerungIdent=willenserklaerung.willenserklaerungIdent;
            int erg = dbBundle.dbMeldungen.update(lMeldung, false); //Ohne update personNatJur
            if (erg < 1) {
                CaBug.drucke("001");
            }

        }
        dbBundle.dbAktienregister.updatePersonNatJur(pEclAktienregisterEintrag,
                pPersonNatJurFuerAnmeldungVerwenden);

        willenserklaerungFreigeben(dbBundle);
        } catch (Exception e) {
            abbruchBeiException(dbBundle, e);
        }

    }

    /******************************anmeldungenAusAktienregisterStornieren***********************/
    /**Für den Aktienregistereintrag in pEclAktienregisterEintrag wird geprüft, ob Anmeldungen (für Aktien)
     * vorhanden sind, und ob diese storniert werden können (d.h. keine bzw. nur stornierte Willenserklärungen
     * für die Anmeldungen vorliegen).
     *
     * Liegt (mindestens) eine Split-Anmeldung vor, wird ebenfalls "nicht möglich" zurückgeliefert.
     *
     * Verwendung: z.B. eine durch eine Bestellung "zwei EK" erzeugte Split-Anmeldung wieder zusammenführen
     *
     * Eingabeparameter:
     * 	pEclAktienregisterEintrag = Aktienregistereintrag, für den Stornierung durchgeführt werden soll
     *
     * Rückgabeparameter:
     *  rcAnzahlMeldungen
     *
     * Belegt nach dieser Funktion:
     * 	willenserklaerungStatus: zugeordneteMeldungenArray
     * 	int anmeldungPersNatJur
     ****************************************************************************************/
    public void anmeldungenAusAktienregisterStornieren_pruefe(DbBundle dbBundle) {
        int i;
        int fixangemeldet = 0;
        this.lDbBundle = dbBundle;

        blWillenserklaerungStatus = new BlWillenserklaerungStatus(lDbBundle);
        blWillenserklaerungStatus
                .leseMeldungenZuAktienregisterIdent(this.pEclAktienregisterEintrag.aktienregisterIdent);

        if (blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray == null
                || blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length == 0) {
            /*Dann noch keine Anmeldungen vorhanden!*/
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmKeineAnmeldungVorhanden;
            return;
        }
        rcAnzahlMeldungen = 0;
        for (i = 0; i < blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length; i++) {
            if (blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].klasse == 1) {
                rcAnzahlMeldungen++;
                anmeldungPersNatJur = blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].personNatJurIdent;
                if (blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].fixAnmeldung == true) {
                    fixangemeldet++;
                }
            }
        }

        if (fixangemeldet > 0) {
            /*Dann Fixanmeldung vorhanden!*/
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmFixAnmeldungVorhanden;
            return;
        }

        /*Nun Willenserklärungen zu Meldungen einlesen - denn dann sind anschließend die entsprechenden
         * Werte (AnzKIAV etc.) je Anmeldung gefüllt*/
        blWillenserklaerungStatus.leseMeldungenEigeneGastkartenZuPersonNatJur(anmeldungPersNatJur);
        blWillenserklaerungStatus.leseMeldungenBevollmaechtigtZuPersonNatJur(anmeldungPersNatJur);
        blWillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(anmeldungPersNatJur);

        /*Nun prüfen, ob alle "Aktien-Anmeldungen" auch ohne Willenserklärungen sind (bzw. alle Willenserklärungen storniert sind*/
        for (i = 0; i < blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length; i++) {
            if (blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].klasse == 1) {

                //				System.out.println("anzKIAVSRV="+blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzKIAVSRV);
                //				System.out.println("anzVollmachtenDritte="+blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzVollmachtenDritte);
                //				System.out.println("anzZutrittsIdentSelbst="+blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentSelbst);
                //				System.out.println("anzZutrittsIdentVollmacht="+blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentVollmacht);

                if (blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzKIAVSRV != 0
                        || blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzVollmachtenDritte != 0
                        || blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentSelbst != 0
                        || blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentVollmacht != 0) {
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = CaFehler.pmWillenserklaerungenVorhanden;
                    return;
                }
            }
        }
        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;
    }

    public void anmeldungenAusAktienregisterStornieren(DbBundle dbBundle) {
        try {
        int i, i1, i2, /*erg,*/ gef;

        willenserklaerungSperren(dbBundle);

        anmeldungenAusAktienregisterStornieren_pruefe(dbBundle);

        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }
        int hauptwillenserklaerung = 0;
        for (i = 0; i < blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length; i++) {
            if (blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].klasse == 1) {
                int lMeldungsIdent = blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent;

                /*Storno-Anmeldungs-Willenserklärung anhängen*/
                prepareWillenserklaerung(KonstWillenserklaerung.stornoAnmeldungAusAktienregister);
                EclWillenserklaerung willenserklaerung = this.preparedWillenserklaerung;
                EclWillenserklaerungZusatz willenserklaerungZusatz = this.preparedWillenserklaerungZusatz;

                willenserklaerung.meldungsIdent = lMeldungsIdent;
                willenserklaerungZusatz.aktienregisterIdent = pEclAktienregisterEintrag.aktienregisterIdent;
                if (i > 0) {
                    willenserklaerung.folgeBuchungFuerIdent = hauptwillenserklaerung;
                }
                /*Info: willenserklaerungZusatz wird momentan noch als leer gespeichert. Geplant ist hier jedoch
                 * demnächst dann noch Begründung zu füllen
                 */
                /*erg=*/dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
                if (i == 0) {
                    hauptwillenserklaerung = willenserklaerung.willenserklaerungIdent;
                    rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;
                }

                /*Anmeldungs-Willenserklärung-Zusatz auf "storniert" setzen*/

                EclMeldung eclMeldung = new EclMeldung();
                eclMeldung.meldungsIdent = lMeldungsIdent;
                dbBundle.dbWillenserklaerungZusatz.leseZuMeldung(eclMeldung);
                dbBundle.dbWillenserklaerung.leseZuMeldung(eclMeldung);

                for (i1 = 0; i1 < dbBundle.dbWillenserklaerungZusatz.willenserklaerungArray.length; i1++) {
                    EclWillenserklaerungZusatz lWillenserklaerungZusatz = dbBundle.dbWillenserklaerungZusatz.willenserklaerungArray[i1];
                    if (lWillenserklaerungZusatz.willenserklaerung == KonstWillenserklaerung.anmeldungAusAktienregister) {
                        lWillenserklaerungZusatz.anmeldungIstStorniert = 1;
                        gef = 0;
                        for (i2 = 0; i2 < dbBundle.dbWillenserklaerung.willenserklaerungArray.length; i2++) {
                            EclWillenserklaerung lWillenserklaerung = dbBundle.dbWillenserklaerung.willenserklaerungArray[i2];
                            if (lWillenserklaerung.willenserklaerungIdent == lWillenserklaerungZusatz.willenserklaerungIdent) {
                                dbBundle.dbWillenserklaerung.updateMitZusatz(lWillenserklaerung,
                                        lWillenserklaerungZusatz);
                                gef++;
                            }

                        }
                        if (gef != 1) {
                            CaBug.drucke("BlWillenserklaerung.anmeldungenAusAktienregisterStornieren - 001");
                            this.rcIstZulaessig = false;
                            this.rcGrundFuerUnzulaessig = -1;
                            willenserklaerungFreigeben(dbBundle);
                            return;
                        }

                    }

                }

                /*Satz in dbMeldung auf "inaktiv" setzen*/
                EclMeldung lMeldung = new EclMeldung();
                lMeldung.meldungsIdent = lMeldungsIdent;
                dbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
                lMeldung = dbBundle.dbMeldungen.meldungenArray[0];
                lMeldung.meldungAktiv = 2;
                //				lMeldung.letzteWillenserklaerungIdent=hauptwillenserklaerung;
                dbBundle.dbMeldungen.update(lMeldung);

            }
        }
        willenserklaerungFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

    }

    /******************************anmeldungStornieren***********************
     * Identifikation über (pi):
     * > meldeIdent
     *
     * Für die Meldung wird geprüft, ob nicht-stornierte Willenserklärungen
     * vorliegen.
     * Falls nein: anmeldung wird storniert.
     *
     ****************************************************************************************/
    public void anmeldungStornieren_pruefe(DbBundle dbBundle) {
        int meldungaktionaer /*,meldunggast*/; /*=1 => Zuordnung zu dieser Klasse*/
        int i;
        int gef = -1;

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        if (piMeldungsIdentAktionaer != 0) {
            meldungaktionaer = 1;
        } else {
            meldungaktionaer = 0;
        }
        //		if (piMeldungsIdentGast!=0){meldunggast=1;}else{meldunggast=0;}
        if (/*meldunggast==0 &&*/ meldungaktionaer == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungNichHinreichendSpezifiziert;
            return;
        }

        pEclAktienregisterEintrag = new EclAktienregister();
        pEclAktienregisterEintrag.aktionaersnummer = this.piEclMeldungAktionaer.aktionaersnummer;

        lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(pEclAktienregisterEintrag);
        pEclAktienregisterEintrag = lDbBundle.dbAktienregister.ergebnisPosition(0);

        blV2WillenserklaerungStatus = new BlWillenserklaerungStatus(lDbBundle);
        blV2WillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(pEclAktienregisterEintrag.aktienregisterIdent);

        if (blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray == null
                || blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length == 0) {
            /*Dann noch keine Anmeldungen vorhanden!*/
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmKeineAnmeldungVorhanden;
            return;
        }

        /*Ab hier neu eingefügt - scheint irgendwann verschwunden zu sein????*/
        rcAnzahlMeldungen = 0;
        for (i = 0; i < blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length; i++) {
            if (blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].klasse == 1) {
                rcAnzahlMeldungen++;
                anmeldungPersNatJur = blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].personNatJurIdent;
            }
        }
        /*Bis hier neu eingefügt*/

        /*Nun Willenserklärungen zu Meldungen einlesen - denn dann sind anschließend die entsprechenden
         * Werte (AnzKIAV etc.) je Anmeldung gefüllt*/
        blV2WillenserklaerungStatus.leseMeldungenEigeneGastkartenZuPersonNatJur(anmeldungPersNatJur);
        blV2WillenserklaerungStatus.leseMeldungenBevollmaechtigtZuPersonNatJur(anmeldungPersNatJur);
        blV2WillenserklaerungStatus.ergaenzeZugeordneteMeldungenUmWillenserklaerungen(anmeldungPersNatJur);

        /*Nun prüfen, ob die Anmeldung auch ohne Willenserklärungen ist (bzw. alle Willenserklärungen storniert sind*/
        for (i = 0; i < blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length; i++) {
            if (blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].meldungsIdent == piMeldungsIdentAktionaer) {

                //				System.out.println("anzKIAVSRV="+blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzKIAVSRV);
                //				System.out.println("anzVollmachtenDritte="+blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzVollmachtenDritte);
                //				System.out.println("anzZutrittsIdentSelbst="+blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentSelbst);
                //				System.out.println("anzZutrittsIdentVollmacht="+blWillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentVollmacht);
                gef = i;
                if (blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzKIAVSRV != 0
                        || blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzVollmachtenDritte != 0
                        || blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentSelbst != 0
                        || blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].anzZutrittsIdentVollmacht != 0) {
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = CaFehler.pmWillenserklaerungenVorhanden;
                    return;
                }
            }
        }
        if (gef == -1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmKeineAnmeldungVorhanden;
            return;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;
    }

    public void anmeldungStornieren(DbBundle dbBundle) {
        try {
       int i1, i2, gef;

        willenserklaerungSperren(dbBundle);

        anmeldungStornieren_pruefe(dbBundle);

        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        int lMeldungsIdent = piMeldungsIdentAktionaer;

        /*Storno-Anmeldungs-Willenserklärung anhängen*/
        prepareWillenserklaerung(KonstWillenserklaerung.stornoAnmeldungAusAktienregister);
        EclWillenserklaerung willenserklaerung = this.preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = this.preparedWillenserklaerungZusatz;

        willenserklaerung.meldungsIdent = lMeldungsIdent;
        willenserklaerungZusatz.aktienregisterIdent = pEclAktienregisterEintrag.aktienregisterIdent;
        /*Info: willenserklaerungZusatz wird momentan noch als leer gespeichert. Geplant ist hier jedoch
         * demnächst dann noch Begründung zu füllen
         */
        dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        /*Anmeldungs-Willenserklärung-Zusatz auf "storniert" setzen*/

        EclMeldung eclMeldung = new EclMeldung();
        eclMeldung.meldungsIdent = lMeldungsIdent;
        dbBundle.dbWillenserklaerungZusatz.leseZuMeldung(eclMeldung);
        dbBundle.dbWillenserklaerung.leseZuMeldung(eclMeldung);

        for (i1 = 0; i1 < dbBundle.dbWillenserklaerungZusatz.willenserklaerungArray.length; i1++) {
            EclWillenserklaerungZusatz lWillenserklaerungZusatz = dbBundle.dbWillenserklaerungZusatz.willenserklaerungArray[i1];
            if (lWillenserklaerungZusatz.willenserklaerung == KonstWillenserklaerung.anmeldungAusAktienregister) {
                lWillenserklaerungZusatz.anmeldungIstStorniert = 1;
                gef = 0;
                for (i2 = 0; i2 < dbBundle.dbWillenserklaerung.willenserklaerungArray.length; i2++) {
                    EclWillenserklaerung lWillenserklaerung = dbBundle.dbWillenserklaerung.willenserklaerungArray[i2];
                    if (lWillenserklaerung.willenserklaerungIdent == lWillenserklaerungZusatz.willenserklaerungIdent) {
                        dbBundle.dbWillenserklaerung.updateMitZusatz(lWillenserklaerung, lWillenserklaerungZusatz);
                        gef++;
                    }

                }
                if (gef != 1) {
                    CaBug.drucke("BlWillenserklaerung.anmeldungenAusAktienregisterStornieren - 001");
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = -1;
                    willenserklaerungFreigeben(dbBundle);
                    return;
                }

            }

        }

        /*Satz in dbMeldung auf "inaktiv" setzen*/
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = lMeldungsIdent;
        dbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        lMeldung = dbBundle.dbMeldungen.meldungenArray[0];
        //		lMeldung.letzteWillenserklaerungIdent=rcWillenserklaerungIdent;
        lMeldung.meldungAktiv = 2;
        dbBundle.dbMeldungen.update(lMeldung);
        willenserklaerungFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

    }

    /*****************Interne Routine: Änderung einer bestehenden Aktienzahl*************************************/
    private void anmeldungAendernBestand(DbBundle dbBundle, long pNeuerBestand, EclWillenserklaerung pWillenserklaerung,
            EclWillenserklaerungZusatz pWillenserklaerungZusatz, boolean pFix) {

        int lMeldungsIdent = pWillenserklaerungZusatz.meldungsIdent;
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = lMeldungsIdent;
        dbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        lMeldung = dbBundle.dbMeldungen.meldungenArray[0];
        long lAlterBestand = lMeldung.stueckAktien;

        lMeldung.stueckAktien = pNeuerBestand;
        lMeldung.stimmen = pNeuerBestand;

        dbBundle.dbMeldungen.update(lMeldung);

        if (pFix) {
            pWillenserklaerungZusatz.anmeldungFix = 1;
            pWillenserklaerungZusatz.aktienAnmelden = pNeuerBestand;
        } else {
            pWillenserklaerungZusatz.aktienAnmelden = -1;

        }
        dbBundle.dbWillenserklaerung.updateMitZusatz(pWillenserklaerung, pWillenserklaerungZusatz);

        veraendernAktienbestandAktuelleWillenserklaerung(dbBundle, lMeldung, lAlterBestand, pNeuerBestand);

    }

    /******************************anmeldungFixAendern***********************
     * Identifikation über (pi):
     * > meldeIdent
     *
     * Die Anmeldung wird auf die übergebene Aktienzahl geändert (soweit nicht Aktienregisterbestand überschritten wird),
     * und die Anmeldeart auf Fix geändert.
     *
     ****************************************************************************************/
    public void anmeldungFixAendern_pruefe(DbBundle dbBundle) {
        int meldungaktionaer /*,meldunggast*/; /*=1 => Zuordnung zu dieser Klasse*/
        int i;
        long diffAktien = 0; /* + > wird erhöht, - > wird erniedrigt*/
        long istAktien = 0;
        long darfAktien = 0;

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        if (piMeldungsIdentAktionaer != 0) {
            meldungaktionaer = 1;
        } else {
            meldungaktionaer = 0;
        }
        //		if (piMeldungsIdentGast!=0){meldunggast=1;}else{meldunggast=0;}
        if (/*meldunggast==0 &&*/ meldungaktionaer == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungNichHinreichendSpezifiziert;
            return;
        }

        diffAktien = this.pAktienAnmelden - this.piEclMeldungAktionaer.stimmen;

        pEclAktienregisterEintrag = new EclAktienregister();
        pEclAktienregisterEintrag.aktionaersnummer = this.piEclMeldungAktionaer.aktionaersnummer;

        lDbBundle.dbAktienregister.leseZuAktienregisterEintrag(pEclAktienregisterEintrag);
        pEclAktienregisterEintrag = lDbBundle.dbAktienregister.ergebnisPosition(0);
        darfAktien = pEclAktienregisterEintrag.stimmen;

        blV2WillenserklaerungStatus = new BlWillenserklaerungStatus(lDbBundle);
        blV2WillenserklaerungStatus.leseMeldungenZuAktienregisterIdent(pEclAktienregisterEintrag.aktienregisterIdent);

        if (blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray == null
                || blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length == 0) {
            /*Dann noch keine Anmeldungen vorhanden!*/
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmKeineAnmeldungVorhanden;
            return;
        }

        /*Nun prüfen, ob Aktienanzahl noch frei ist*/
        istAktien = 0;
        for (i = 0; i < blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray.length; i++) {
            istAktien += blV2WillenserklaerungStatus.zugeordneteMeldungenEigeneAktienArray[i].aktionaerStimmen;
        }
        if (istAktien + diffAktien > darfAktien) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmNichtAngemeldeterBestandZuKlein;
            return;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;
    }

    public void anmeldungFixAendern(DbBundle dbBundle) {
        try {
        int i1, i2, gef;

        willenserklaerungSperren(dbBundle);

        anmeldungFixAendern_pruefe(dbBundle);
        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        //		int lMeldungsIdent=piMeldungsIdentAktionaer;

        //		EclMeldung lMeldung=new EclMeldung();
        //		lMeldung.meldungsIdent=lMeldungsIdent;
        //		dbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        //		lMeldung=dbBundle.dbMeldungen.meldungenArray[0];
        //
        //		lMeldung.stueckAktien=pAktienAnmelden;
        //		lMeldung.stimmen=pAktienAnmelden;
        //
        //		dbBundle.dbMeldungen.update(lMeldung);

        /*Anmeldungs-Willenserklärung-Zusatz auf "fix" und veränderte Aktienzahl setzen*/

        EclMeldung eclMeldung = new EclMeldung();
        eclMeldung.meldungsIdent = piMeldungsIdentAktionaer;
        dbBundle.dbWillenserklaerungZusatz.leseZuMeldung(eclMeldung);
        dbBundle.dbWillenserklaerung.leseZuMeldung(eclMeldung);

        for (i1 = 0; i1 < dbBundle.dbWillenserklaerungZusatz.willenserklaerungArray.length; i1++) {
            EclWillenserklaerungZusatz lWillenserklaerungZusatz = dbBundle.dbWillenserklaerungZusatz.willenserklaerungArray[i1];
            if (lWillenserklaerungZusatz.willenserklaerung == KonstWillenserklaerung.anmeldungAusAktienregister) {
                //				lWillenserklaerungZusatz.anmeldungFix=1;
                //				lWillenserklaerungZusatz.aktienAnmelden=pAktienAnmelden;
                gef = 0;
                for (i2 = 0; i2 < dbBundle.dbWillenserklaerung.willenserklaerungArray.length; i2++) {
                    EclWillenserklaerung lWillenserklaerung = dbBundle.dbWillenserklaerung.willenserklaerungArray[i2];
                    if (lWillenserklaerung.willenserklaerungIdent == lWillenserklaerungZusatz.willenserklaerungIdent) {
                        anmeldungAendernBestand(dbBundle, pAktienAnmelden, lWillenserklaerung, lWillenserklaerungZusatz,
                                true);
                        gef++;
                    }

                }
                if (gef != 1) {
                    CaBug.drucke("BlWillenserklaerung.anmeldungFixAendern - 001");
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = -1;
                    willenserklaerungFreigeben(dbBundle);
                    return;
                }

            }

        }
        willenserklaerungFreigeben(dbBundle);
        } catch (Exception e) {
            abbruchBeiException(dbBundle, e);
        }


    }

    /****************************Reduzieren / Erhöhen Aktienbestand im Aktienregister*************************************************
     * Eingabeparameter:
     * > pAktienAnmelden: Neuer Aktienbestand
     * > pEclAktienregisterEintrag: Aktienregistereintrag, der verändert wird (und zwar VOR der Veränderung des Bestandes!)
     *
     * Rückgabewerte von rcGrundFuerUnzulaessig
     * 	pmKeineAnmeldungenVorhanden
     *
     * */
    public void anmeldungAendernAktienbestandRegister(DbBundle dbBundle) {
        try {
        int i, i2, gef;
        int anzMeldungen = 0;
        List<Integer> meldungenIdent = new LinkedList<Integer>();
        List<EclWillenserklaerungZusatz> willenserklaerungZusatzListe = new LinkedList<EclWillenserklaerungZusatz>();
        int anzMeldungenFix = 0;

        willenserklaerungSperren(dbBundle);

        /*Willenserklärungen der Anmeldungen einlesen*/
        dbBundle.dbWillenserklaerungZusatz
                .leseZuAktienregisterIdentOhneStorno(pEclAktienregisterEintrag.aktienregisterIdent);

        /*Liegen überhaupt Anmeldungen vor?*/
        if (dbBundle.dbWillenserklaerungZusatz.anzWillenserklaerungGefunden() == 0) {
            /*Keine Willenserklärungen (oder nur stornierte) => keine Ausführung*/
            rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmKeineAnmeldungenVorhanden;
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        for (i = 0; i < dbBundle.dbWillenserklaerungZusatz.anzWillenserklaerungGefunden(); i++) {
            EclWillenserklaerungZusatz lEclWillenserklaerungZusatz = dbBundle.dbWillenserklaerungZusatz
                    .willenserklaerungGefunden(i);
            if (lEclWillenserklaerungZusatz.willenserklaerung == KonstWillenserklaerung.anmeldungAusAktienregister) {

                /*Veränderungstatsache als Willenserklärung speichern*/
                prepareWillenserklaerung(KonstWillenserklaerung.veraenderungAktienbestandAktienregister);
                EclWillenserklaerung willenserklaerung = this.preparedWillenserklaerung;
                willenserklaerung.meldungsIdent = lEclWillenserklaerungZusatz.meldungsIdent;
                EclWillenserklaerungZusatz willenserklaerungZusatz = this.preparedWillenserklaerungZusatz;
                willenserklaerungZusatz.aktienregisterIdent = pEclAktienregisterEintrag.aktienregisterIdent;

                anzMeldungen++;
                meldungenIdent.add(lEclWillenserklaerungZusatz.meldungsIdent);
                willenserklaerungZusatzListe.add(lEclWillenserklaerungZusatz);

                if (lEclWillenserklaerungZusatz.anmeldungFix == 1) {
                    anzMeldungenFix++;
                }
            }
        }

        /*Sind Fix-Bestände angemeldet?*/
        if (anzMeldungenFix > 0) {
            rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmFixAnmeldungenVorhanden;
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        /*Sind mehr als 2 Bestände angemeldet?*/
        if (anzMeldungen > 2) {
            rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmMehrAls2AnmeldungenVorhanden;
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        /*Aktienbestand verteilen*/
        long[] aktienProAnmeldung = new long[anzMeldungen];
        long aktienProEinzelneAnmeldung = pAktienAnmelden / anzMeldungen;
        long aktienrest = pAktienAnmelden; /*Noch nicht verteilte Aktien - am Schluß für letzte Anmeldung*/

        for (i = 0; i < anzMeldungen - 1; i++) {
            aktienProAnmeldung[i] = aktienProEinzelneAnmeldung;
            aktienrest -= aktienProEinzelneAnmeldung;
        }
        aktienProAnmeldung[anzMeldungen - 1] = aktienrest;

        for (i = 0; i < meldungenIdent.size(); i++) {

            EclMeldung eclMeldung = new EclMeldung();
            eclMeldung.meldungsIdent = meldungenIdent.get(i);
            dbBundle.dbWillenserklaerung.leseZuMeldung(eclMeldung);

            gef = 0;
            for (i2 = 0; i2 < dbBundle.dbWillenserklaerung.willenserklaerungArray.length; i2++) {
                EclWillenserklaerung lWillenserklaerung = dbBundle.dbWillenserklaerung.willenserklaerungArray[i2];
                if (lWillenserklaerung.willenserklaerungIdent == willenserklaerungZusatzListe
                        .get(i).willenserklaerungIdent) {
                    anmeldungAendernBestand(dbBundle, aktienProAnmeldung[i], lWillenserklaerung,
                            willenserklaerungZusatzListe.get(i), false);
                    gef++;
                }

            }
            if (gef != 1) {
                CaBug.drucke("BlWillenserklaerung.anmeldungAendernAktienbestandRegister - 001");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = -1;
                willenserklaerungFreigeben(dbBundle);
                return;
            }

        }
        willenserklaerungFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }
    }

    /************************anmeldungGast**********************************************/
    /**Für den Gast, übergeben in pEclMeldungNeu wird eine Anmeldung als reiner Gast durchgeführt.
     * Eingabeparameter:
     * pEclMeldungNeu = Daten, mit denen der Gast angemeldet werden soll
     */

    /*TODO ACHTUNG WICHTIGSTER HINWEIS!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * Funktioniert auch, wenn anstelle der Gastdatenfelder nur personNatJur gefüllt wird in meldung. Wird derzeit genutzt, um in der App
     * automatisch eine weitere Gastkarte auszustellen. Dies führt aber dann zu unerwünschten Effekten (Aktionär kann keine Gastkarte für
     * fremden mehr ausstellen; und im Portal "komische Anzeige" (diese Gastkarte wird dann zweimal aufgeführt)
     */

    /***************************************************************************************/
    public void anmeldungGast_pruefe(DbBundle dbBundle) {

        this.lDbBundle = dbBundle;

        /*Derzeit sind keine Gründe bekannt, warum ein Gast nicht angemeldet werden könnte.
         * Future möglicherweise Abprüfung von Parametern o.ä. (Gäste überhaupt zulässig;
         * Anzahl Gäste pro Aktionär begrenzt, oder ähnliches)
         */

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;

    }

    public void anmeldungGast(DbBundle dbBundle) {
        //		int erg;

        try {

        willenserklaerungSperren(dbBundle);

        /*Prüfen aufrufen*/
        anmeldungGast_pruefe(dbBundle);

        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        /*Anmeldebestand in meldungen (mit Anhängsel PersonenNatJur) erzeugen*/
        pEclMeldungNeu.meldungAktiv = 1;
        pEclMeldungNeu.klasse = 0;
        pEclMeldungNeu.meldungstyp = 0;

        BlKennung lkennung = new BlKennung(dbBundle);
        lkennung.neueKennungUndOeffentlicheID(pEclMeldungNeu.name, pEclMeldungNeu.vertreterVorname);
        pEclMeldungNeu.oeffentlicheID = lkennung.ergebnisOeffentlicheID;
        pEclMeldungNeu.loginKennung = lkennung.ergebnisKennung;

        rcMeldungen = new int[1];
        rcEclMeldungen=new EclMeldung[1];

        /*erg=*/dbBundle.dbMeldungen.insert(pEclMeldungNeu);
        rcMeldungen[0] = pEclMeldungNeu.meldungsIdent;
        rcEclMeldungen[0]=pEclMeldungNeu;

        /*Zur Anmeldung auch eine Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.anmeldungGast);
        EclWillenserklaerung willenserklaerung = this.preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = this.preparedWillenserklaerungZusatz;

        /*TODO #5 Gäste - hier vermutlich Inkonsistenz - funktioniert aber anscheinden - überprüfen!*/
        //		willenserklaerung.meldungsIdent=pEclMeldungNeu.meldungsIdent; das war früher!!!
        willenserklaerung.meldungsIdentGast = pEclMeldungNeu.meldungsIdent; //das wurde so korrigiert - funktioniert das jetzt noch?
        willenserklaerungZusatz.meldungsIdentGast = pEclMeldungNeu.meldungsIdent;
        willenserklaerungZusatz.anzahlAnmeldungen = 1;
        if (pEclAktienregisterEintrag != null) { /*Dann Verknüpfung zum Aktienregister herstellen*/
            willenserklaerungZusatz.aktienregisterIdent = pEclAktienregisterEintrag.aktienregisterIdent;
        }
        /*erg=*/dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        /*Nun zur Anmeldung den Login-Satz speichern*/
        EclLoginDaten lLoginDaten = new EclLoginDaten();
        lLoginDaten.loginKennung = lkennung.ergebnisKennung;
        lLoginDaten.kennungArt = KonstLoginKennungArt.personenNatJur;
        lLoginDaten.personenNatJurIdent = pEclMeldungNeu.personenNatJurIdent;
        lLoginDaten.meldeIdent = pEclMeldungNeu.meldungsIdent;
        lLoginDaten.passwortVerschluesselt = lkennung.ergebnisPasswortInitialVerschluesselt;
        lLoginDaten.passwortInitial = lkennung.ergebnisPasswortInitialFuerDatenbank;
        lLoginDaten.eMailFuerVersand = pEclMeldungNeu.mailadresse;
        lLoginDaten.berechtigungPortal=pBerechtigungsWert;
        
        if (!pEclMeldungNeu.mailadresse.isEmpty()) {
            lLoginDaten.emailBestaetigt = 1;
        }
        dbBundle.dbLoginDaten.insert(lLoginDaten);
        rcLoginDaten = lLoginDaten;

        willenserklaerungFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

    }

    
 
    
    /************** Autom. Vergabe neuer ZutrittsIdents (EK, SK, GK), sowie Prüfung manuell vergebener****
     * Falls nurPruefen==true, dann wird nur geprüft ob Vergabe prinzipiell möglich ist.
     *
     * Voraussetzung: piEclMeldungAktionaer ist gefüllt,
     * */
    private int getNeueKarteAuto(int pKartenklasse, boolean nurPruefen) {
        int lGattung = liefereGattung();
        return getNeueKarteAuto(pKartenklasse, lGattung, nurPruefen);
    }

    
    /************** Autom. Vergabe neuer ZutrittsIdents (EK, SK, GK), sowie Prüfung manuell vergebener****
     * Falls nurPruefen==true, dann wird nur geprüft ob Vergabe prinzipiell möglich ist.
     *
      * Voraussetzung: DbBundle wurde geöffnet und z.B. mit setzeDbBundle an BlWIllenserklaerung übergeben.
     * */
    
    public int getNeueKarteAuto(int pKartenklasse, int lGattung, boolean nurPruefen) {
        int erg = -1;

        /*Prüfen, ob Alpha-Kreis, dann keine automatische Nummernvergabe möglich*/
        if (!lDbBundle.param.paramNummernkreise.istNumerisch[pKartenklasse]
                || pKartenklasse == KonstKartenklasse.stimmkartennummerSecond) {
            CaBug.drucke("001");
            return CaFehler.pfAutomatischeVergabeXyNichtMoeglich;
        }

        switch (pKartenklasse) {
        case KonstKartenklasse.eintrittskartennummer:
            if (nurPruefen) {
                erg = lDbBundle.dbBasis.getEintrittskartennummerPruefe(lGattung);
            } else {
                erg = lDbBundle.dbBasis.getEintrittskartennummer(lGattung);
            }
            if (erg < 1) {
                return erg;
            }

            if (erg > lDbBundle.param.paramNummernkreise.bisSubEintrittskartennummer[lGattung][2]) {
                CaBug.drucke("002 lGattung=" + lGattung + " erg=" + erg
                        + " lDbBundle.param.paramNummernkreise.bisSubEintrittskartennummer[lGattung][2]="
                        + lDbBundle.param.paramNummernkreise.bisSubEintrittskartennummer[lGattung][2]);
                return CaFehler.pfAutomatischeVergabeXyNichtMoeglich;
            }
            break;
        case KonstKartenklasse.gastkartennummer:
            if (nurPruefen) {
                erg = lDbBundle.dbBasis.getGastkartennummerPruefe();
            } else {
                erg = lDbBundle.dbBasis.getGastkartennummer();
            }
            if (erg < 1) {
                return erg;
            }
            if (erg > lDbBundle.param.paramNummernkreise.bisKartennummerAuto[KonstKartenklasse.gastkartennummer]) {
                CaBug.drucke("003");
                return CaFehler.pfAutomatischeVergabeXyNichtMoeglich;
            }
            break;
        case KonstKartenklasse.stimmkartennummer:
            if (nurPruefen) {
                erg = lDbBundle.dbBasis.getStimmkartennummerPruefe(lGattung);
            } else {
                erg = lDbBundle.dbBasis.getStimmkartennummer(lGattung);
            }
            if (erg < 1) {
                return erg;
            }
            if (erg > lDbBundle.param.paramNummernkreise.bisSubStimmkartennummer[lGattung][5]) {
                CaBug.drucke("004");
                return CaFehler.pfAutomatischeVergabeXyNichtMoeglich;
            }
            break;
        }
        return erg;
    }

    private int pruefeKartenNummer(int pKartenklasse, String pKartennummer, String pNebennummer, boolean pKarteWurdeVorreserviert) {
        int lGattung = 0;
        boolean istSammelkarte = false;
        if (pKartenklasse != KonstKartenklasse.gastkartennummer) {
            lGattung = liefereGattung();
            if (this.piEclMeldungAktionaer.meldungstyp == 2) {
                istSammelkarte = true;
            }
        }

        /* Falls Leer-Feld Übergeben wurde
         * => Vorbedingung: automatische Vergabe wurde vor diesem Check durchgeführt
         * */
        if (pKartennummer.isEmpty()) {
            return (CaFehler.pfFormatXyUnzulaessig);
        }

        /*Falls Alpha zulässig: Maximale Länge überschritten?*/
        if (!lDbBundle.param.paramNummernkreise.istNumerisch[pKartenklasse]) {
            if (pKartennummer.length() > lDbBundle.param.paramNummernkreise.laengeKartennummer[pKartenklasse]) {
                return (CaFehler.pfFormatXyUnzulaessig);
            } else {
                return 1;
            }
        } else { //Falls numerisch
            /*Nur Zahlen enthalten?*/
            int nrZutrittsIdent = 0;
            try {
                nrZutrittsIdent = Integer.parseInt(pKartennummer);
            } catch (Exception e1) {
                return (CaFehler.pfFormatXyUnzulaessig);
            }
            if (nrZutrittsIdent == 0) {
                return CaFehler.pfXyNichtImZulaessigenNummernkreis;
            }
            /*Im Nummernkreis von - bis enthalten? */
            switch (pKartenklasse) {
            case KonstKartenklasse.eintrittskartennummer:
                boolean gefSammel = false; //True => Nummer liegt in einem zulässigen Bereich*/
                if (istSammelkarte) {
                    if (lDbBundle.param.paramNummernkreise.vonSammelkartennummer != 0
                            && lDbBundle.param.paramNummernkreise.bisSammelkartennummer != 0) { //im Sammelkreis
                        if (nrZutrittsIdent >= lDbBundle.param.paramNummernkreise.vonSammelkartennummer
                                && nrZutrittsIdent <= lDbBundle.param.paramNummernkreise.bisSammelkartennummer) {
                            gefSammel = true;
                        }
                    }
                    //					for (int i=1;i<=5;i++){
                    if (nrZutrittsIdent >= lDbBundle.param.paramNummernkreise.vonSubEintrittskartennummer[lGattung][1]
                            && nrZutrittsIdent <= lDbBundle.param.paramNummernkreise.bisSubEintrittskartennummer[lGattung][1]) {
                        gefSammel = true;
                    }
                    //					}
                    if (gefSammel == false) {
                        return CaFehler.pfXyNichtImZulaessigenNummernkreis;
                    }
                } else {
                    if (pKarteWurdeVorreserviert==false) {
                        /*In manuellem Nummernkreis?*/
                        if (nrZutrittsIdent > lDbBundle.param.paramNummernkreise.bisSubEintrittskartennummer[lGattung][1]
                                || nrZutrittsIdent < lDbBundle.param.paramNummernkreise.vonSubEintrittskartennummer[lGattung][1]) {
                            return CaFehler.pfXyNichtImZulaessigenNummernkreis;
                        }
                    }
                    else {
                        /*In auto-Nummernkreis?*/
                        if (nrZutrittsIdent > lDbBundle.param.paramNummernkreise.bisSubEintrittskartennummer[lGattung][2]
                                || nrZutrittsIdent < lDbBundle.param.paramNummernkreise.vonSubEintrittskartennummer[lGattung][2]) {
                            return CaFehler.pfXyNichtImZulaessigenNummernkreis;
                        }
                    }
                }
                break;
            case KonstKartenklasse.gastkartennummer:
                if (nrZutrittsIdent > lDbBundle.param.paramNummernkreise.bisKartennummerManuell[KonstKartenklasse.gastkartennummer]
                        || nrZutrittsIdent < lDbBundle.param.paramNummernkreise.vonKartennummerManuell[KonstKartenklasse.gastkartennummer]) {
                    return CaFehler.pfXyNichtImZulaessigenNummernkreis;
                }
                break;
            case KonstKartenklasse.stimmkartennummer:
                boolean gef = false;
                for (int i = 1; i < 4; i++) {
                    if (nrZutrittsIdent >= lDbBundle.param.paramNummernkreise.vonSubEintrittskartennummer[lGattung][i]
                            && nrZutrittsIdent <= lDbBundle.param.paramNummernkreise.bisSubEintrittskartennummer[lGattung][i]) {
                        gef = true;
                    }
                }
                if (!gef) {
                    return CaFehler.pfXyNichtImZulaessigenNummernkreis;
                }
                break;
            }
        }

        return 1;
    }

    /***********************neueZutrittsIdentZuMeldung_pruefe*******************************/
    /** Vergibt eine neue ZutrittsIdent und ordnet diese der Meldung zu.
     *
     * Varianten:
     * > Wenn nur meldungGast gefüllt, dann aus Gastnummernkreis und Klasse gast
     * > Ansonsten aus Aktionärsnummernkreis und klasse Aktionär
     *
     * Identifikation über (pi):
     * > meldeIdent und/oder meldeIdentGast gefüllt
     *
     * Eingabeparamter (p)
     * > pZutrittsIdent (falls leer, dann automatische Vergabe); (zugehörige Klasse wird
     * 		automatisch bestimmt - siehe oben)
     *
     * Gedanken zum Delay:
     * > Kein Delay erforderlich (möglich), da nicht Abstimmungsrelevant.
     * > Allerdings:
     * 		Eine ZutrittsIdent neu zu vergeben, die zwar gesperrt und versioniert aber eben
     * 		delayed-gesperrt und/oder delayed-versioniert ist, ist nicht möglich!
     * 		Dies ist auch grundsätzlich gut und richtig so, denn eine ZutrittsIdent, die während
     * 		der Abstimmung noch (richtigerweise) verwendet werden konnte, darf nicht gleichzeitig
     * 		während der Auswertung schon wieder anderweitig bekannt sein - sonst wird die
     * 		Sache sehr problematisch.
     * 		Ist auch ein "Müh"-Fall (versehentlich gesperrt, und muß wieder aufleben) - dieser
     * 		muß dann eben organisatorisch nach dem Delayed erfolgen, bzw. es muß eine separate
     * 		Funktion geben - Stornierung der Sperrung einer EK.
     */
    public void neueZutrittsIdentZuMeldung_pruefe(DbBundle dbBundle, boolean pKarteWurdeVorreserviert) {
        int meldungaktionaer, meldunggast; /*=1 => Zuordnung zu dieser Klasse*/
        int erg = 0;

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        if (piMeldungsIdentAktionaer != 0) {
            meldungaktionaer = 1;
        } else {
            meldungaktionaer = 0;
        }
        if (piMeldungsIdentGast != 0) {
            meldunggast = 1;
        } else {
            meldunggast = 0;
        }
        if (meldunggast == 0 && meldungaktionaer == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungNichHinreichendSpezifiziert;
            return;
        }

        /*Wenn nur Gast zugeordnet wird, dann ZutrittsIdent aus Gast-Kreis vergeben,
         * sonst aus Aktionärs-Kreis
         */
        if (meldungaktionaer == 0) {
            klasse = 0;
        } else {
            klasse = 1;
        }

        if (this.pZutrittsIdent.zutrittsIdent.isEmpty()) { /*Prüfen, ob neue ZutrittsIdent automatisch vergebbar*/
            switch (klasse) {
            case 1:
                erg = getNeueKarteAuto(KonstKartenklasse.eintrittskartennummer, true);
                break;
            case 0:
                erg = getNeueKarteAuto(KonstKartenklasse.gastkartennummer, true);
                break;
            }

            if (erg < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                return;
            }
        } else {/*Prüfen, ob übergebene ZutrittsIdent dem Format entspricht und noch nicht vergeben ist*/

            /*Format zulässig?*/
            if (pZutrittsIdent.zutrittsIdentNeben.isEmpty()) {
                pZutrittsIdent.zutrittsIdentNeben = "00";
            }
            switch (klasse) {
            case 1:
                erg = pruefeKartenNummer(KonstKartenklasse.eintrittskartennummer, this.pZutrittsIdent.zutrittsIdent,
                        this.pZutrittsIdent.zutrittsIdentNeben, pKarteWurdeVorreserviert);
                break;
            case 0:
                erg = pruefeKartenNummer(KonstKartenklasse.gastkartennummer, this.pZutrittsIdent.zutrittsIdent,
                        this.pZutrittsIdent.zutrittsIdentNeben, pKarteWurdeVorreserviert);
                break;
            }
            if (erg < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                return;
            }
            /*Schon vorhanden?*/
            dbBundle.dbZutrittskarten.read_alleVersionen(this.pZutrittsIdent);
            int i;
            int gef = 0;
            for (i = 0; i < dbBundle.dbZutrittskarten.anzErgebnis(); i++) {
                if (dbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentKlasse == klasse
                        && dbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentVers == 0) {
                    if (dbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentWurdeGesperrt()) {
                        if (gef == 0) {
                            gef = 2;
                        }
                    } else {
                        gef = 1;
                    }
                }
            }
            if (gef == 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmZutrittsIdentBereitsVorhanden;
                return;
            }
            if (gef == 2) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmZutrittsIdentIstStorniert;
                return;
            }
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;
    }

    /********************neueZutrittsIdentZuMeldung*********************************************/
    public void neueZutrittsIdentZuMeldung(DbBundle dbBundle) {
        neueZutrittsIdentZuMeldung (dbBundle, false);
    }
    
    /**pKarteWurdeVorreserviert muß standardmäßig auf false stehen.
     * Nur wenn vorher eine EK aus dem Autonummernkreis mit getNeueKarteAuto reserviert wurde, und diese als zu vergebende EK-Nummer
     * mit übergeben wurde, darf/muß dieser WQert auf false stehen.
     */
    public void neueZutrittsIdentZuMeldung(DbBundle dbBundle, boolean pKarteWurdeVorreserviert) {
        try {
        int erg = 0;
        willenserklaerungSperren(dbBundle);

        this.neueZutrittsIdentZuMeldung_pruefe(dbBundle, pKarteWurdeVorreserviert);
        if (this.rcIstZulaessig == false) {
            CaBug.druckeLog("nicht zulässig nach neueZutrittsIdentZuMeldung_pruefe this.rcGrundFuerUnzulaessig="
                    + this.rcGrundFuerUnzulaessig, logDrucken, 10);
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        if (this.pZutrittsIdent.zutrittsIdent.isEmpty()) { /*neue ZutrittsIdent automatisch vergebenr*/
            BlZutrittsIdent blZutrittsIdent = new BlZutrittsIdent(dbBundle);
            int meldungsIdent = 0;
            if (piMeldungsIdentAktionaer != 0) {
                meldungsIdent = piMeldungsIdentAktionaer;
            } else {
                meldungsIdent = piMeldungsIdentGast;
            }

            int lGattung = liefereGattung();

            erg = blZutrittsIdent.neueZutrittsIdentZuMeldung(meldungsIdent, liefereKlasse(), lGattung);
            if (erg < 1) {
                CaBug.drucke("001 nicht zulässig nach blZutrittsIdent.neueZutrittsIdentZuMeldung");
                CaBug.drucke("lGattung=" + lGattung);
                CaBug.drucke("liefereKlasse()=" + liefereKlasse());

                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                return;
            }
            this.pZutrittsIdent.zutrittsIdent = blZutrittsIdent.rEclZutrittsIdent.zutrittsIdent;
            this.pZutrittsIdent.zutrittsIdentNeben = blZutrittsIdent.rEclZutrittsIdent.zutrittsIdentNeben;
        } else {
            if (pZutrittsIdent.zutrittsIdentNeben.isEmpty()) {
                pZutrittsIdent.zutrittsIdentNeben = "00";
            }
        }

        /*ZutrittsIdent in db speichern*/
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        String hZutrittsIdent = blNummernformen.formatiereEKNr(pZutrittsIdent.zutrittsIdent);
        pZutrittsIdent.zutrittsIdent = hZutrittsIdent;

        EclZutrittskarten zutrittskarten = new EclZutrittskarten();
        zutrittskarten.zutrittsIdent = pZutrittsIdent.zutrittsIdent;
        zutrittskarten.zutrittsIdentNeben = pZutrittsIdent.zutrittsIdentNeben;
        zutrittskarten.zutrittsIdentKlasse = liefereKlasse();
        //		zutrittskarten.meldungsIdentGast=piMeldungsIdentGast;		In V2
        //		zutrittskarten.meldungsIdentAktionaer=piMeldungsIdentAktionaer;		in V2
        if (piMeldungsIdentGast != 0) {
            zutrittskarten.gueltigeKlasse = 0;
            zutrittskarten.gueltigeKlasse_Delayed = 0;
        } else {
            zutrittskarten.gueltigeKlasse = 1;
            zutrittskarten.gueltigeKlasse_Delayed = 1;
        }
        zutrittskarten.meldungsIdentGast = piMeldungsIdentGast;
        zutrittskarten.meldungsIdentAktionaer = piMeldungsIdentAktionaer;
        erg = dbBundle.dbZutrittskarten.insert(zutrittskarten);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.neueZutrittsIdentZuMeldung);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        willenserklaerung.zutrittsIdent = pZutrittsIdent.zutrittsIdent;
        willenserklaerung.zutrittsIdentNeben = pZutrittsIdent.zutrittsIdentNeben;
        willenserklaerungZusatz.versandartEK = pVersandartEK;
        willenserklaerungZusatz.versandadresse1 = pVersandadresse1;
        willenserklaerungZusatz.versandadresse2 = pVersandadresse2;
        willenserklaerungZusatz.versandadresse3 = pVersandadresse3;
        willenserklaerungZusatz.versandadresse4 = pVersandadresse4;
        willenserklaerungZusatz.versandadresse5 = pVersandadresse5;
        willenserklaerungZusatz.emailAdresseEK = pEmailAdresseEK;

        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.neueZutrittsIdentZuMeldung - 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        /*ZutrittsIdent in Meldungssatz eintragen*/
        EclMeldung lMeldung = new EclMeldung();
        if (piMeldungsIdentAktionaer != 0) {
            lMeldung.meldungsIdent = piMeldungsIdentAktionaer;
        } else {
            lMeldung.meldungsIdent = piMeldungsIdentGast;
        }

        dbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        if (dbBundle.dbMeldungen.meldungenArray.length != 1) {
            CaBug.drucke("BlWillenserklaerung.zuZutrittsIdentNeuesDokument 001");
        }

        lMeldung = dbBundle.dbMeldungen.meldungenArray[0];
        lMeldung.zutrittsIdent = pZutrittsIdent.zutrittsIdent;
        dbBundle.dbMeldungen.update(lMeldung);

        this.rcIstZulaessig = true;
        willenserklaerungFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }
    }

    /********************neueZutrittsIdentZuMeldungV2*********************************************/
    /*Hinweis: da V2 weniger gesetzt wird als bei der anderen Version,
     * und die andere Version bei den "wichtigeren" Logiken eingesetzt wird,
     * wurde das Deprecated jetzt umgelegt.
     */
    @Deprecated
    public void neueZutrittsIdentZuMeldungV2(DbBundle dbBundle) {
        try {
        int erg = 0;
        
        willenserklaerungSperren(dbBundle);

        this.neueZutrittsIdentZuMeldung_pruefe(dbBundle, false);
        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        if (this.pZutrittsIdent.zutrittsIdent.isEmpty()) { /*neue ZutrittsIdent automatisch vergebenr*/
            BlZutrittsIdent blZutrittsIdent = new BlZutrittsIdent(dbBundle);
            int meldungsIdent = 0;
            if (piMeldungsIdentAktionaer != 0) {
                meldungsIdent = piMeldungsIdentAktionaer;
            } else {
                meldungsIdent = piMeldungsIdentGast;
            }

            int lGattung = liefereGattung();

            erg = blZutrittsIdent.neueZutrittsIdentZuMeldung(meldungsIdent, liefereKlasse(), lGattung);
            if (erg < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                return;
            }
            this.pZutrittsIdent.zutrittsIdent = blZutrittsIdent.rEclZutrittsIdent.zutrittsIdent;
            this.pZutrittsIdent.zutrittsIdentNeben = blZutrittsIdent.rEclZutrittsIdent.zutrittsIdentNeben;
        } else {
            if (pZutrittsIdent.zutrittsIdentNeben.isEmpty()) {
                pZutrittsIdent.zutrittsIdentNeben = "00";
            }
        }

        /*ZutrittsIdent in db speichern*/
        BlNummernformen blNummernformen = new BlNummernformen(lDbBundle);
        String hZutrittsIdent = blNummernformen.formatiereEKNr(pZutrittsIdent.zutrittsIdent);
        pZutrittsIdent.zutrittsIdent = hZutrittsIdent;

        EclZutrittskarten zutrittskarten = new EclZutrittskarten();
        zutrittskarten.zutrittsIdent = pZutrittsIdent.zutrittsIdent;
        zutrittskarten.zutrittsIdentNeben = pZutrittsIdent.zutrittsIdentNeben;
        zutrittskarten.zutrittsIdentKlasse = liefereKlasse();
        zutrittskarten.meldungsIdentGast = piMeldungsIdentGast;
        zutrittskarten.meldungsIdentAktionaer = piMeldungsIdentAktionaer;
        if (piMeldungsIdentGast != 0) {
            zutrittskarten.gueltigeKlasse = 0;
            zutrittskarten.gueltigeKlasse_Delayed = 0;
        } else {
            zutrittskarten.gueltigeKlasse = 1;
            zutrittskarten.gueltigeKlasse_Delayed = 1;
        }
        //		zutrittskarten.meldungsIdentGast=piMeldungsIdentGast;    fehlt in V2
        //		zutrittskarten.meldungsIdentAktionaer=piMeldungsIdentAktionaer;		fehlt in V2
        erg = dbBundle.dbZutrittskarten.insert(zutrittskarten);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.neueZutrittsIdentZuMeldung);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        willenserklaerung.zutrittsIdent = pZutrittsIdent.zutrittsIdent;
        willenserklaerung.zutrittsIdentNeben = pZutrittsIdent.zutrittsIdentNeben;
        //		willenserklaerungZusatz.versandartEK=pVersandartEK;  fehlt in V2 - warum?
        willenserklaerungZusatz.versandadresse1 = pVersandadresse1;
        willenserklaerungZusatz.versandadresse2 = pVersandadresse2;
        willenserklaerungZusatz.versandadresse3 = pVersandadresse3;
        willenserklaerungZusatz.versandadresse4 = pVersandadresse4;
        willenserklaerungZusatz.versandadresse5 = pVersandadresse5;
        willenserklaerungZusatz.emailAdresseEK = pEmailAdresseEK;

        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.neueZutrittsIdentZuMeldung - 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        /*ZutrittsIdent in Meldungssatz eintragen*/
        EclMeldung lMeldung = new EclMeldung();
        if (piMeldungsIdentAktionaer != 0) {
            lMeldung.meldungsIdent = piMeldungsIdentAktionaer;
        } else {
            lMeldung.meldungsIdent = piMeldungsIdentGast;
        }

        dbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        if (dbBundle.dbMeldungen.meldungenArray.length != 1) {
            CaBug.drucke("BlWillenserklaerung.zuZutrittsIdentNeuesDokument 001");
        }

        lMeldung = dbBundle.dbMeldungen.meldungenArray[0];
        lMeldung.zutrittsIdent = pZutrittsIdent.zutrittsIdent;
        dbBundle.dbMeldungen.update(lMeldung);

        this.rcIstZulaessig = true;
        willenserklaerungFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }
    }

    /**********************************sperrenZutrittsIdent_pruefe***************************/
    /** Sperren einer ZutrittsIdent.
     *
     * Eingabeparameter:
     * > piZutrittsIdent und piKlasse
     *
     * Fehlermeldungen:
     * > pfXyNichtVorhanden
     * > pfdUnbekannterFehler (falls Klasse falsch angegeben)
     * > zutrittsIdentIstGesperrt (ZutrittsIdent ist bereits gesperrt)
     *
     * Handhabungshinweise:
     * > falls die ZutrittsIdent "in Verwendung" ist, kann sie "eigentlich" nicht gesperrt werden.
     * 		Was heißt "in Verwendung"?: eine (der möglicherweise zwei) zugeordneten meldungen
     * 			ist anwesend, und zutrittsIdent ist dort als aktuelle eingetragen.
     *
     *Aber: es gibt DREI Gründe, eine ZutrittsIdent zu sperren:
     *a) Weitere Handlungsweisen einer (auch anwesenden) Person auf der HV sollen unterbunden werden
     *b) Eine Person hat ihre ZutrittsIdent (möglicherweise bereits benutzt) verloren, erhält eine neue,
     *und will die alte ungültig machen (im Sinne einer Ersatzkarte)
     *c) im Vorfeld der HV wurde eine ZutrittsIdent ausgestellt, die - z.B. im Rahmen einer Anmeldestornierung -
     *gesperrt werden soll
     *
     *zu b) und c): sind damit vom Effekt quasi gleich - und diese Stornierungen müssen auch möglich sein,
     *wenn die ZutrittsIdent aktuell in Verwendung ist!
     *zu a): das ist keine Sperren der ZutrittsIdent - sondern eine "Umordnung". Z.B. ein Bevollmächtigter, der
     *auf der HV ist, und dem die Vollmacht entzogen wird, muß ja als Gast weiter präsent bleiben. D.h. dieser
     *Fall wird nicht über "ZutrittsIdent sperren" abgewickelt, sondern die ZutrittsIdent verweis dann nur noch
     *auf eine Gastmeldung.
     *
     *Fazit: ein Sperren ist immer möglich, auch wenn die Karte präsent ist.
     *
     *Zum Delay: Da eine Sperre möglicherweise nach der Abstimmung erfolgt, die Abstimmung selbst aber noch gelten
     *soll, und das Einlesen / Auswerten dann erst nach der Sperre erfolgen würde, muß ein Delay implementiert werden.
     *
     *Herausforderung: bei Teilnahme über Smartphone, Online o.ä. muß der Teilnehmer-Status "umgekickt" werden.
     *Dies kann jedoch erst im dortigen Zusammenhang relaisiert werden.
     *LANGTODO Online-Teilnahme / Smartphone-Teilnahme
     */
    public void sperrenZutrittsIdent_pruefe(DbBundle dbBundle) {

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        EclZutrittskarten zutrittsKarte = null;
        zutrittsKarte = dbBundle.dbZutrittskarten.ergebnisPosition(0); /*Nur zur Schreiberleichterung :-) */

        /*Überprüfen, ob bereits gesperrt*/
        if (((ptForceNonDelay == true || zutrittsKarte.delayedVorhanden == 0 || 1 == 1 /*TODO _Delay deaktiviert*/)
                && zutrittsKarte.zutrittsIdentWurdeGesperrt())
                || (ptForceNonDelay == false && zutrittsKarte.delayedVorhanden == 1
                        && zutrittsKarte.zutrittsIdentWurdeGesperrt_Delayed())) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmZutrittsIdentGesperrt;
            return;
        }

        /*Überprüfen, ob Delayed wird*/
        if ((lDbBundle.willenserklaerungIstDelayed(
                KonstWillenserklaerung.sperrenZutrittsIdent) /*Dann ist die Willenserklärung an sich delayed*/
                || zutrittsKarte.delayedVorhanden == 1 /*Dann ist ZutrittsIdent selbst schon delayed*/
                || weiterverarbeitungDelayed == 1 /*Dann wurde in der Vorverarbeitung festgestellt, dass delayed werden muß*/)
                && ptForceNonDelay == false && 1 == 2 /*TODO _Delay deaktiviert*/
        ) {
            this.rcDelayed = true;
        } else { /* Nun die einzelnen zugeordneten Meldungen überprüfen.*/
            /*Tja, das muß aber nach derzeitigen Überlegungen nicht erfolgen.Ob Willenserklärungen der einzelnen
             * Meldungen delayed sind oder nicht, ist wurscht (zum Nachbuchen der Delayed-Meldungen sind ja die
             * Zutritts/Stimmkarten-Idents nicht mehr erforderlich).
             */
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;

    }

    public void sperrenZutrittsIdent(DbBundle dbBundle) {
        try {
        int erg = 0;
        
        willenserklaerungSperren(dbBundle);

        sperrenZutrittsIdent_pruefe(dbBundle);

        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        EclZutrittskarten zutrittsKarte = null;
        zutrittsKarte = dbBundle.dbZutrittskarten.ergebnisPosition(0); /*Nur zur Schreiberleichterung :-) */

        zutrittsKarte.zutrittsIdentIstGesperrt = KonstStimmkarteIstGesperrt.gesperrt;
        if (this.rcDelayed == false) {
            zutrittsKarte.zutrittsIdentIstGesperrt_Delayed = KonstStimmkarteIstGesperrt.gesperrt;
        }
        erg = dbBundle.dbZutrittskarten.update(zutrittsKarte);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungFreigeben(dbBundle);
            return;
        } else {
            this.rcIstZulaessig = true;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.sperrenZutrittsIdent);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        if (this.rcDelayed == true) {
            willenserklaerung.delayed = 1;
        }
        willenserklaerung.zutrittsIdent = zutrittsKarte.zutrittsIdent;
        willenserklaerung.zutrittsIdentNeben = zutrittsKarte.zutrittsIdentNeben;
        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.sperrenZutrittsIdent 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        /*Nun bei willenserklaerungZusatz bei der ursprünglichen Willenserklärung das Storniert-Kennzeichen setzen*/
        if (zutrittsKarte.meldungsIdentAktionaer != 0) { /*Aktionär*/
            EclMeldung lMeldung = new EclMeldung();
            lMeldung.meldungsIdent = zutrittsKarte.meldungsIdentAktionaer;
            dbBundle.dbWillenserklaerung.leseZuMeldung(lMeldung);
            int i, gef = 0;
            EclWillenserklaerung lEclWillenserklaerung = null;
            EclWillenserklaerungZusatz lEclWillenserklaerungZusatz = null;
            int willenserklaerungsIdent = 0;
            for (i = 0; i < dbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i++) {
                if (dbBundle.dbWillenserklaerung.willenserklaerungArray[i].willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung) {
                    if (dbBundle.dbWillenserklaerung.willenserklaerungArray[i].zutrittsIdent
                            .compareTo(zutrittsKarte.zutrittsIdent) == 0
                            && dbBundle.dbWillenserklaerung.willenserklaerungArray[i].zutrittsIdentNeben
                                    .compareTo(zutrittsKarte.zutrittsIdentNeben) == 0) {
                        willenserklaerungsIdent = dbBundle.dbWillenserklaerung.willenserklaerungArray[i].willenserklaerungIdent;
                        gef++;
                        lEclWillenserklaerung = dbBundle.dbWillenserklaerung.willenserklaerungArray[i];
                    }
                }
            }
            if (gef != 1) {
                CaBug.drucke("BlWillenserklaerung.sperrenZutrittsIdent 002");
                //				System.out.println("gef="+gef);
                //				System.out.println("dbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden()="+dbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden());
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = -1;
                willenserklaerungFreigeben(dbBundle);
                return;
            }
            dbBundle.dbWillenserklaerungZusatz.leseZuIdent(willenserklaerungsIdent);
            lEclWillenserklaerungZusatz = dbBundle.dbWillenserklaerungZusatz.willenserklaerungGefunden(0);
            lEclWillenserklaerungZusatz.anmeldungIstStorniert = 1;
            dbBundle.dbWillenserklaerung.updateMitZusatz(lEclWillenserklaerung, lEclWillenserklaerungZusatz);
        }
        if (zutrittsKarte.meldungsIdentGast != 0) { /*Gast*/
            EclMeldung lMeldung = new EclMeldung();
            lMeldung.meldungsIdent = zutrittsKarte.meldungsIdentGast;
            dbBundle.dbWillenserklaerung.leseZuMeldungGast(lMeldung);
            int i, gef = 0;
            EclWillenserklaerung lEclWillenserklaerung = null;
            EclWillenserklaerungZusatz lEclWillenserklaerungZusatz = null;
            int willenserklaerungsIdent = 0;
            for (i = 0; i < dbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden(); i++) {
                if (dbBundle.dbWillenserklaerung.willenserklaerungArray[i].willenserklaerung == KonstWillenserklaerung.neueZutrittsIdentZuMeldung) {
                    if (dbBundle.dbWillenserklaerung.willenserklaerungArray[i].zutrittsIdent
                            .compareTo(zutrittsKarte.zutrittsIdent) == 0
                            && dbBundle.dbWillenserklaerung.willenserklaerungArray[i].zutrittsIdentNeben
                                    .compareTo(zutrittsKarte.zutrittsIdentNeben) == 0) {
                        willenserklaerungsIdent = dbBundle.dbWillenserklaerung.willenserklaerungArray[i].willenserklaerungIdent;
                        gef++;
                        lEclWillenserklaerung = dbBundle.dbWillenserklaerung.willenserklaerungArray[i];
                    }
                }
            }
            if (gef != 1) {
                CaBug.drucke("BlWillenserklaerung.sperrenZutrittsIdent 003");
                CaBug.drucke("gef=" + gef);
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = -1;
                willenserklaerungFreigeben(dbBundle);
                return;
            }
            dbBundle.dbWillenserklaerungZusatz.leseZuIdent(willenserklaerungsIdent);
            lEclWillenserklaerungZusatz = dbBundle.dbWillenserklaerungZusatz.willenserklaerungGefunden(0);
            lEclWillenserklaerungZusatz.anmeldungIstStorniert = 1;
            dbBundle.dbWillenserklaerung.updateMitZusatz(lEclWillenserklaerung, lEclWillenserklaerungZusatz);
        }

        /*ZutrittsIdent in Meldungssatz löschen - ggf neue Eintragen*/
        EclMeldung lMeldung = new EclMeldung();
        if (piMeldungsIdentAktionaer != 0) {
            lMeldung.meldungsIdent = piMeldungsIdentAktionaer;
        } else {
            lMeldung.meldungsIdent = piMeldungsIdentGast;
        }

        dbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        if (dbBundle.dbMeldungen.meldungenArray.length != 1) {
            CaBug.drucke("BlWillenserklaerung.zuZutrittsIdentNeuesDokument 001");
        }

        lMeldung = dbBundle.dbMeldungen.meldungenArray[0];
        lMeldung.zutrittsIdent = "";

        dbBundle.dbJoined.read_willenserklaerungArtZuAktionaer(lMeldung.meldungsIdent,
                /*EnWillenserklaerung.NeueZutrittsIdentZuMeldung*/KonstWillenserklaerung.neueZutrittsIdentZuMeldung, 1);
        int i;
        for (i = 0; i < dbBundle.dbJoined.anzErgebnisWillenserklaerung(); i++) {
            lMeldung.zutrittsIdent = dbBundle.dbJoined.ergebnisWillenserklaerungPosition(i).zutrittsIdent;
        }

        dbBundle.dbMeldungen.update(lMeldung);

        willenserklaerungFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

    }

    /***********************************freigebenGesperrteZutrittsIdent************************************************/
    /**Freigeben einer gesperrten ZutrittsIdent
     *
     * Eingabeparameter:
     * > piZutrittsIdent und piKlasse
     *
     * Zum Delay:
     * > Grundsätzlich kein Delay dieser Funktion erforderlich.
     * > Allerdings ist möglicherweise schon das Sperren einer ZutrittsIdent Delayed, in diesem Fall muß dann auch
     * 		das Freigeben delayed werden.
     * => Delay erforderlich.
     *
     * Achtung: beim Neuvergeben einer ZutrittsIdent wird nur die nicht-delayete Versionierung überprüft! Sieht
     * Beschreibung dort!
     *
     */
    public void freigebenGesperrteZutrittsIdent_pruefe(DbBundle dbBundle) {
        int erg = 0;

        try {
        willenserklaerungOhneSammelkarteSperren(dbBundle);
        
        this.lDbBundle = dbBundle;

        EclZutrittskarten zutrittsKarte = null; /*Nur zur Schreiberleichterung :-) */

        /*Hinweis: für diese Willenserklärung kann evtlEinlesenMeldung nicht verwendet werden, da diese
         * abbricht, wenn eine ZutrittsIdent gesperrt ist
         */

        if (this.piKlasse == -1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        klasse = piKlasse;
        if (this.piKlasse == 0) {
            erg = dbBundle.dbZutrittskarten.readZutrittsIdentGast(this.piZutrittsIdent);
        }
        if (this.piKlasse == 1) {
            erg = dbBundle.dbZutrittskarten.readZutrittsIdentAktionaer(this.piZutrittsIdent);
        }
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfXyNichtVorhanden;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
           return;
        }

        zutrittsKarte = dbBundle.dbZutrittskarten.ergebnisPosition(0);

        if ((ptForceNonDelay == false && zutrittsKarte.delayedVorhanden == 1
                && zutrittsKarte.zutrittsIdentIstGesperrt_Delayed == KonstStimmkarteIstGesperrt.aktiv)
                || ((zutrittsKarte.delayedVorhanden == 0 || 1 == 1 /*TODO _Delay deaktiviert*/
                        || ptForceNonDelay == true) && zutrittsKarte.zutrittsIdentIstGesperrt == KonstStimmkarteIstGesperrt.aktiv)) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmZutrittsIdentNichtGesperrt;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
           return;
        }

        /*Überprüfen, ob Delayed wird*/
        if (ptForceNonDelay == false && ((lDbBundle.willenserklaerungIstDelayed(
                KonstWillenserklaerung.freigebenGesperrteZutrittsIdent) /*Dann ist die Willenserklärung an sich delayed*/
                || zutrittsKarte.delayedVorhanden == 1 /*Dann ist ZutrittsIdent selbst schon delayed*/)
                && 1 == 2 /*TODO _Delay deaktiviert*/)) {
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
           this.rcDelayed = true;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }
        return;

    }

    public void freigebenGesperrteZutrittsIdent(DbBundle dbBundle) {
        int erg = 0;
        int hoechsteVersion = 0;

        try {
        willenserklaerungOhneSammelkarteSperren(dbBundle);

        freigebenGesperrteZutrittsIdent_pruefe(dbBundle);
        if (this.rcIstZulaessig == false) {
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
           return;
        }

        /*Einlesen aller Sätze mit ZutrittsIdent (egal welche Klasse)*/
        dbBundle.dbZutrittskarten.read_alleVersionen(this.piZutrittsIdent);

        /*Ermitteln aus allen Sätzen, die zur Klasse gehören: höchste Versionsnummer*/
        int i;
        for (i = 0; i < dbBundle.dbZutrittskarten.anzErgebnis(); i++) {
            if (dbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentKlasse == this.piKlasse) {
                if (dbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentVers > hoechsteVersion) {
                    hoechsteVersion = dbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentVers;
                }
                if (dbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentVers_Delayed > hoechsteVersion) {
                    hoechsteVersion = dbBundle.dbZutrittskarten.ergebnisPosition(i).zutrittsIdentVers_Delayed;
                }

            }
        }
        hoechsteVersion++;

        /*gültigen Datensatz mit der ZutrittsIdent und entsprechenden Klasse einlesen*/
        if (this.piKlasse == 0) {
            erg = dbBundle.dbZutrittskarten.readZutrittsIdentGast(this.piZutrittsIdent);
        } else {
            erg = dbBundle.dbZutrittskarten.readZutrittsIdentAktionaer(this.piZutrittsIdent);
        }
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.freigebenGesperrteZutrittsIdent 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        EclZutrittskarten zutrittsKarte = null; /*Nur zur Schreiberleichterung :-) */
        zutrittsKarte = dbBundle.dbZutrittskarten.ergebnisPosition(0);

        /*Versionsnummer updaten*/
        if (this.rcDelayed == false) {
            zutrittsKarte.zutrittsIdentVers = hoechsteVersion;
        } else {
            zutrittsKarte.zutrittsIdentVers_Delayed = hoechsteVersion;
            if (zutrittsKarte.delayedVorhanden == 0) { /*Delayed noch nicht vorhanden*/
                zutrittsKarte.meldungsIdentAktionaer_Delayed = zutrittsKarte.meldungsIdentAktionaer;
                zutrittsKarte.meldungsIdentGast_Delayed = zutrittsKarte.meldungsIdentGast;
                zutrittsKarte.zutrittsIdentIstGesperrt_Delayed = zutrittsKarte.zutrittsIdentIstGesperrt;
                zutrittsKarte.delayedVorhanden = 1;
            }
        }

        erg = dbBundle.dbZutrittskarten.update(zutrittsKarte);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.freigebenGesperrteZutrittsIdent);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        willenserklaerung.zutrittsIdent = piZutrittsIdent.zutrittsIdent;
        willenserklaerung.zutrittsIdentNeben = piZutrittsIdent.zutrittsIdentNeben;
        if (this.rcDelayed == true) {
            willenserklaerung.delayed = 1;
        }
        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.freigebenGesperrteZutrittsIdent 003");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        this.rcIstZulaessig = true;
        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

    }

    /***********************zuZutrittsIdentNeuesDokument_pruefe*******************************/
    /** Erzeugt ein neues Dokument zu einer bestehende ZutrittsIdent aufgrund der bestehenden
     * ggf. veränderten Meldedaten.
     *
     * Identifikation über (pi):
     * > meldeIdent und/oder meldeIdentGast gefüllt
     *
     * Eingabeparamter (p)
     * > pZutrittsIdent
     *
     */
    public void zuZutrittsIdentNeuesDokument_pruefe(DbBundle dbBundle) {
        int meldungaktionaer, meldunggast; /*=1 => Zuordnung zu dieser Klasse*/
        //		int erg=0;

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        if (piMeldungsIdentAktionaer != 0) {
            meldungaktionaer = 1;
        } else {
            meldungaktionaer = 0;
        }
        if (piMeldungsIdentGast != 0) {
            meldunggast = 1;
        } else {
            meldunggast = 0;
        }
        if (meldunggast == 0 && meldungaktionaer == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungNichHinreichendSpezifiziert;
            return;
        }

        if (meldungaktionaer == 0) {
            klasse = 0;
        } else {
            klasse = 1;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;
    }

    /********************zuZutrittsIdentNeuesDokument*********************************************/
    public void zuZutrittsIdentNeuesDokument(DbBundle dbBundle) {
        try {
        int erg = 0;
        willenserklaerungSperren(dbBundle);

        this.zuZutrittsIdentNeuesDokument_pruefe(dbBundle);
        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.zuZutrittsIdentNeuesDokument);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        willenserklaerung.zutrittsIdent = pZutrittsIdent.zutrittsIdent;
        willenserklaerung.zutrittsIdentNeben = pZutrittsIdent.zutrittsIdentNeben;
        if (pZutrittsIdent.zutrittsIdent.isEmpty()) {
            CaBug.drucke("BlWillenserklaerung.zuZutrittsIdentNeuesDokument - 003");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        willenserklaerungZusatz.versandartEK = pVersandartEK;
        willenserklaerungZusatz.versandadresse1 = pVersandadresse1;
        willenserklaerungZusatz.versandadresse2 = pVersandadresse2;
        willenserklaerungZusatz.versandadresse3 = pVersandadresse3;
        willenserklaerungZusatz.versandadresse4 = pVersandadresse4;
        willenserklaerungZusatz.versandadresse5 = pVersandadresse5;
        willenserklaerungZusatz.emailAdresseEK = pEmailAdresseEK;

        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.zuZutrittsIdentNeuesDokument - 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        this.rcIstZulaessig = true;
        willenserklaerungFreigeben(dbBundle);
        } catch (Exception e) {
            abbruchBeiException(dbBundle, e);
        }
    }

    /***********************neueStimmkarteZuMeldung_pruefe*******************************/
    /** Vergibt eine neue Stimmkarte und ordnet diese der Meldung zu.
     *
     * Identifikation über (pi):
     * > meldeIdent und/oder meldeIdentGast gefüllt
     *
     * Eingabeparamter (p)
     * > pStimmkarte (falls leer, dann automatische Vergabe aus virtuellem Kreis);
     *
     * Gedanken zum Delay:
     * > Kein Delay erforderlich (möglich), da nicht Abstimmungsrelevant.
     * > Allerdings:
     * 		Eine Stimmkarte neu zu vergeben, die zwar gesperrt und versioniert aber eben
     * 		delayed-gesperrt und/oder delayed-versioniert ist, ist nicht möglich!
     * 		Dies ist auch grundsätzlich gut und richtig so, denn eine Stimmkarte, die während
     * 		der Abstimmung noch (richtigerweise) verwendet werden konnte, darf nicht gleichzeitig
     * 		während der Auswertung schon wieder anderweitig bekannt sein - sonst wird die
     * 		Sache sehr problematisch.
     * 		Ist auch ein "Müh"-Fall (versehentlich gesperrt, und muß wieder aufleben) - dieser
     * 		muß dann eben organisatorisch nach dem Delayed erfolgen, bzw. es muß eine separate
     * 		Funktion geben - Stornierung der Sperrung einer Stimmkarte.
     */
    public void neueStimmkarteZuMeldung_pruefe(DbBundle dbBundle) {
        int meldungaktionaer, meldunggast; /*=1 => Zuordnung zu dieser Klasse*/
        int erg = 0;

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        if (piMeldungsIdentAktionaer != 0) {
            meldungaktionaer = 1;
        } else {
            meldungaktionaer = 0;
        }
        if (piMeldungsIdentGast != 0) {
            meldunggast = 1;
        } else {
            meldunggast = 0;
        }
        if (meldunggast == 0 && meldungaktionaer == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungNichHinreichendSpezifiziert;
            return;
        }

        if (this.pStimmkarte.isEmpty()) { /*Prüfen, ob neue ZutrittsIdent automatisch vergebbar*/
            erg = getNeueKarteAuto(KonstKartenklasse.stimmkartennummer, true);
            if (erg < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                return;
            }

        } else {/*Prüfen, ob übergebene Stimmkarte dem Format entspricht und noch nicht vergeben ist*/
            /*Format zulässig?*/
            erg = pruefeKartenNummer(KonstKartenklasse.stimmkartennummer, this.pStimmkarte, "", false);
            if (erg < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                return;
            }
            /*Schon vorhanden?*/
            dbBundle.dbStimmkarten.read_alleVersionen(this.pStimmkarte);
            int i;
            int gef = 0;
            for (i = 0; i < dbBundle.dbStimmkarten.anzErgebnis(); i++) {
                if (dbBundle.dbStimmkarten.ergebnisPosition(i).stimmkarteVers == 0) {
                    if (dbBundle.dbStimmkarten.ergebnisPosition(i).stimmkarteIstGesperrt == 1) {
                        if (gef == 0) {
                            gef = 2;
                        }
                    } else {
                        gef = 1;
                    }
                }
            }
            if (gef == 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteBereitsVorhanden;
                return;
            }
            if (gef == 2) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteGesperrt;
                return;
            }
        }
        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;
    }

    /********************neueStimmkarteZuMeldung*********************************************/
    public void neueStimmkarteZuMeldung(DbBundle dbBundle) {
        int erg = 0;
        try {
        willenserklaerungOhneSammelkarteSperren(dbBundle);
        this.neueStimmkarteZuMeldung_pruefe(dbBundle);
        if (this.rcIstZulaessig == false) {
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        if (this.pStimmkarte.isEmpty()) { /*neue Stimmkarte - virtuell automatisch vergebenr*/
//            int lGattung = liefereGattung();
            erg = getNeueKarteAuto(liefereKlasse(), false);
            if (erg < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                willenserklaerungOhneSammelkarteFreigeben(dbBundle);
                return;
            }
            this.pStimmkarte = Integer.toString(erg);

        }

        /*Stimmkarte in db speichern*/
        EclStimmkarten stimmkarten = new EclStimmkarten();
        stimmkarten.stimmkarte = pStimmkarte;
        stimmkarten.meldungsIdentGast = piMeldungsIdentGast;
        stimmkarten.meldungsIdentAktionaer = piMeldungsIdentAktionaer;
        stimmkarten.gueltigeKlasse = 1;
        stimmkarten.gueltigeKlasse_Delayed = 1;
        erg = dbBundle.dbStimmkarten.insert(stimmkarten);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.neueStimmkarteZuMeldung);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;

        willenserklaerung.stimmkarte1 = pStimmkarte;

        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.neueStimmkarteZuMeldung - 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        this.rcIstZulaessig = true;
        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }
    }

    /**********************************sperrenStimmkarte_pruefe***************************/
    /** Sperren einer Stimmkarte.
     *
     * Eingabeparameter:
     * > piStimmkarte
     *
     * Fehlermeldungen:
     * > pfXyNichtVorhanden
     * > pmStimmkarteGesperrt (Stimmkarte ist bereits gesperrt)
     *
     * Handhabungshinweise:
     * > falls die Stimmkarte "in Verwendung" ist, kann sie "eigentlich" nicht gesperrt werden.
     * 		Was heißt "in Verwendung"?: eine (der möglicherweise zwei) zugeordneten meldungen
     * 			ist anwesend, und Stimmkarte ist dort als aktuelle eingetragen.
     *
     *Aber: es gibt verschiedene GRÜNDE, eine Stimmkarte zu sperren:
     *a) Weitere Handlungsweisen einer (auch anwesenden) Person auf der HV sollen unterbunden werden
     *b) Eine Person hat ihre Stimmkarte (möglicherweise bereits benutzt) verloren, erhält eine neue,
     *und will die alte ungültig machen (im Sinne einer ErsatzStimmkarte)
     *
     *zu b):  diese Stornierungen müssen auch möglich sein,
     *wenn die Stimmkarte aktuell in Verwendung ist!
     *zu a): das ist keine Sperren der Stimmkarte - sondern eine "Umordnung". Z.B. ein Bevollmächtigter, der
     *auf der HV ist, und dem die Vollmacht entzogen wird, muß ja als Gast weiter präsent bleiben. D.h. dieser
     *Fall wird nicht über "Stimmkarte sperren" abgewickelt, sondern die Stimmkarte verweist dann nur noch
     *auf eine Gastmeldung.
     *
     *Fazit: ein Sperren ist immer möglich, auch wenn die Karte präsent ist.
     *
     *Zum Delay: Da eine Sperre möglicherweise nach der Abstimmung erfolgt, die Abstimmung selbst aber noch gelten
     *soll, und das Einlesen / Auswerten dann erst nach der Sperre erfolgen würde, muß ein Delay implementiert werden.
     *
     *Herausforderung: bei Teilnahme über Smartphone, Online o.ä. muß der Teilnehmer-Status "umgekickt" werden.
     *Dies kann jedoch erst im dortigen Zusammenhang relaisiert werden.
     *LANGTODO Online-Teilnahme / Smartphone-Teilnahme
     */
    public void sperrenStimmkarte_pruefe(DbBundle dbBundle) {

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        EclStimmkarten stimmkarte = null;
        stimmkarte = dbBundle.dbStimmkarten.ergebnisPosition(0); /*Nur zur Schreiberleichterung :-) */

        /*Überprüfen, ob bereits gesperrt*/
        if (((ptForceNonDelay == true || stimmkarte.delayedVorhanden == 0 || 1 == 1 /*TODO _Delay deaktiviert*/)
                && stimmkarte.stimmkarteIstGesperrt == 1)
                || (ptForceNonDelay == false && stimmkarte.delayedVorhanden == 1
                        && stimmkarte.stimmkarteIstGesperrt_Delayed == 1)) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteGesperrt;
            return;
        }

        /*Überprüfen, ob Delayed wird*/
        if ((lDbBundle.willenserklaerungIstDelayed(
                KonstWillenserklaerung.sperrenStimmkarte) /*Dann ist die Willenserklärung an sich delayed*/
                || stimmkarte.delayedVorhanden == 1 /*Dann ist ZutrittsIdent selbst schon delayed*/)
                && ptForceNonDelay == false && 1 == 2 /*TODO _Delay deaktiviert*/
        ) {
            this.rcDelayed = true;
        } else { /* Nun die einzelnen zugeordneten Meldungen überprüfen.*/
            /*Tja, das muß aber nach derzeitigen Überlegungen nicht erfolgen.Ob Willenserklärungen der einzelnen
             * Meldungen delayed sind oder nicht, ist wurscht (zum Nachbuchen der Delayed-Meldungen sind ja die
             * Zutritts/Stimmkarten-Idents nicht mehr erforderlich).
             */
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;
    }

    public void sperrenStimmkarte(DbBundle dbBundle) {
        int erg = 0;
        try {
        willenserklaerungOhneSammelkarteSperren(dbBundle);

        sperrenStimmkarte_pruefe(dbBundle);

        if (this.rcIstZulaessig == false) {
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        EclStimmkarten stimmkarte = null;
        stimmkarte = dbBundle.dbStimmkarten.ergebnisPosition(0); /*Nur zur Schreiberleichterung :-) */

        if (this.rcDelayed == true) {
            if (stimmkarte.delayedVorhanden == 0) {
                /*Bisher noch nicht delayed - deshalb die relevanten Felder ins Delayed übertragen*/
                willenserklaerungOhneSammelkarteFreigeben(dbBundle);
                stimmkarte.delayedVorhanden = 1;
            }
            stimmkarte.stimmkarteIstGesperrt_Delayed = 1;
        } else {
            stimmkarte.stimmkarteIstGesperrt = 1;
        }
        erg = dbBundle.dbStimmkarten.update(stimmkarte);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        } else {
            this.rcIstZulaessig = true;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.sperrenStimmkarte);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;

        if (this.rcDelayed == true) {
            willenserklaerung.delayed = 1;
        }
        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.sperrenStimmkarte 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

    }

    /***********************************freigebenGesperrteStimmkarte************************************************/
    /**Freigeben einer gesperrten Stimmkarte
     *
     * Eingabeparameter:
     * > piStimmkarte
     *
     * Zum Delay:
     * > Grundsätzlich kein Delay dieser Funktion erforderlich.
     * > Allerdings ist möglicherweise schon das Sperren einer Stimmkarte Delayed, in diesem Fall muß dann auch
     * 		das Freigeben delayed werden.
     * => Delay erforderlich.
     *
     * Achtung: beim Neuvergeben einer Stimmkarte wird nur die nicht-delayete Versionierung überprüft! Sieht
     * Beschreibung dort!
     *
     */
    public void freigebenGesperrteStimmkarte_pruefe(DbBundle dbBundle) {
        int erg = 0;

        this.lDbBundle = dbBundle;

        EclStimmkarten stimmkarte = null; /*Nur zur Schreiberleichterung :-) */

        /*Hinweis: für diese Willenserklärung kann evtlEinlesenMeldung nicht verwendet werden, da diese
         * abbricht, wenn eine Stimmkarte gesperrt ist
         */

        erg = dbBundle.dbStimmkarten.read(this.piStimmkarte);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfXyNichtVorhanden;
            return;
        }

        stimmkarte = dbBundle.dbStimmkarten.ergebnisPosition(0);

        if ((ptForceNonDelay == false && stimmkarte.delayedVorhanden == 1
                && stimmkarte.stimmkarteIstGesperrt_Delayed == 0)
                || ((stimmkarte.delayedVorhanden == 0 || 1 == 1 /*TODO _Delay deaktiviert*/ || ptForceNonDelay == true)
                        && stimmkarte.stimmkarteIstGesperrt == 0)) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteNichtGesperrt;
            return;
        }

        /*Überprüfen, ob Delayed wird*/
        if (ptForceNonDelay == false && (lDbBundle.willenserklaerungIstDelayed(
                KonstWillenserklaerung.freigebenGesperrteStimmkarte) /*Dann ist die Willenserklärung an sich delayed*/
                || stimmkarte.delayedVorhanden == 1 /*Dann ist stimmkarte selbst schon delayed*/)
                && 1 == 2 /*TODO _Delay deaktiviert*/
        ) {
            this.rcDelayed = true;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;

    }

    public void freigebenGesperrteStimmkarte(DbBundle dbBundle) {
        int erg = 0;
        int hoechsteVersion = 0;

        try {
        willenserklaerungOhneSammelkarteSperren(dbBundle);

        freigebenGesperrteStimmkarte_pruefe(dbBundle);
        if (this.rcIstZulaessig == false) {
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        /*Einlesen aller Sätze mit Stimmkarte*/
        dbBundle.dbStimmkarten.read_alleVersionen(this.piStimmkarte);

        /*Ermitteln aus allen Sätzen, die zur Klasse gehören: höchste Versionsnummer*/
        int i;
        for (i = 0; i < dbBundle.dbStimmkarten.anzErgebnis(); i++) {
            if (dbBundle.dbStimmkarten.ergebnisPosition(i).stimmkarteVers > hoechsteVersion) {
                hoechsteVersion = dbBundle.dbStimmkarten.ergebnisPosition(i).stimmkarteVers;
            }
            if (dbBundle.dbStimmkarten.ergebnisPosition(i).stimmkarteVers_Delayed > hoechsteVersion) {
                hoechsteVersion = dbBundle.dbStimmkarten.ergebnisPosition(i).stimmkarteVers_Delayed;
            }

        }
        hoechsteVersion++;

        /*gültigen Datensatz mit der Stimmkarte einlesen*/
        erg = dbBundle.dbStimmkarten.read(this.piStimmkarte);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.freigebenGesperrteStimmkarte 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        EclStimmkarten stimmkarte = null; /*Nur zur Schreiberleichterung :-) */
        stimmkarte = dbBundle.dbStimmkarten.ergebnisPosition(0);

        /*Versionsnummer updaten*/
        if (this.rcDelayed == false) {
            stimmkarte.stimmkarteVers = hoechsteVersion;
        } else {
            stimmkarte.stimmkarteVers_Delayed = hoechsteVersion;
            if (stimmkarte.delayedVorhanden == 0) { /*Delayed noch nicht vorhanden*/
                stimmkarte.meldungsIdentAktionaer_Delayed = stimmkarte.meldungsIdentAktionaer;
                stimmkarte.meldungsIdentGast_Delayed = stimmkarte.meldungsIdentGast;
                stimmkarte.stimmkarteIstGesperrt_Delayed = stimmkarte.stimmkarteIstGesperrt;
                stimmkarte.delayedVorhanden = 1;
            }
        }

        erg = dbBundle.dbStimmkarten.update(stimmkarte);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.freigebenGesperrteStimmkarte);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        willenserklaerung.stimmkarte1 = piStimmkarte;
        if (this.rcDelayed == true) {
            willenserklaerung.delayed = 1;
        }
        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.freigebenGesperrteStimmkarte 003");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        this.rcIstZulaessig = true;
        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

    }

    /***********************neueStimmkarteSecondZuMeldung_pruefe*******************************/
    /** Vergibt eine neue StimmkarteSecond und ordnet diese der Meldung zu.
     *
     * Identifikation über (pi):
     * > meldeIdent und/oder meldeIdentGast gefüllt
     *
     * Eingabeparamter (p)
     * > pStimmkarteSecond (falls leer, dann automatische Vergabe aus virtuellem Kreis);
     *
     * Gedanken zum Delay:
     * > Kein Delay erforderlich (möglich), da nicht Abstimmungsrelevant.
     * > Allerdings:
     * 		Eine StimmkarteSecond neu zu vergeben, die zwar gesperrt und versioniert aber eben
     * 		delayed-gesperrt und/oder delayed-versioniert ist, ist nicht möglich!
     * 		Dies ist auch grundsätzlich gut und richtig so, denn eine StimmkarteSecond, die während
     * 		der Abstimmung noch (richtigerweise) verwendet werden konnte, darf nicht gleichzeitig
     * 		während der Auswertung schon wieder anderweitig bekannt sein - sonst wird die
     * 		Sache sehr problematisch.
     * 		Ist auch ein "Müh"-Fall (versehentlich gesperrt, und muß wieder aufleben) - dieser
     * 		muß dann eben organisatorisch nach dem Delayed erfolgen, bzw. es muß eine separate
     * 		Funktion geben - Stornierung der Sperrung einer StimmkarteSecond.
     */
    public void neueStimmkarteSecondZuMeldung_pruefe(DbBundle dbBundle) {
        int meldungaktionaer, meldunggast; /*=1 => Zuordnung zu dieser Klasse*/
        int erg = 0;

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        if (piMeldungsIdentAktionaer != 0) {
            meldungaktionaer = 1;
        } else {
            meldungaktionaer = 0;
        }
        if (piMeldungsIdentGast != 0) {
            meldunggast = 1;
        } else {
            meldunggast = 0;
        }
        if (meldunggast == 0 && meldungaktionaer == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmMeldungNichHinreichendSpezifiziert;
            return;
        }

        if (this.pStimmkarteSecond.isEmpty()) { /*Prüfen, ob neue ZutrittsIdent automatisch vergebbar*/
            erg = getNeueKarteAuto(KonstKartenklasse.stimmkartennummerSecond, true);
            if (erg < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                return;
            }

        } else {/*Prüfen, ob übergebene StimmkarteSecond dem Format entspricht und noch nicht vergeben ist*/
            /*Format zulässig?*/
            erg = pruefeKartenNummer(KonstKartenklasse.stimmkartennummerSecond, this.pStimmkarteSecond, "", false);
            if (erg < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                return;
            }
            /*Schon vorhanden?*/
            dbBundle.dbStimmkartenSecond.read_alleVersionen(this.pStimmkarteSecond);
            int i;
            int gef = 0;
            for (i = 0; i < dbBundle.dbStimmkartenSecond.anzErgebnis(); i++) {
                if (dbBundle.dbStimmkartenSecond.ergebnisPosition(i).stimmkarteSecondVers == 0) {
                    if (dbBundle.dbStimmkartenSecond.ergebnisPosition(i).stimmkarteSecondIstGesperrt == 1) {
                        if (gef == 0) {
                            gef = 2;
                        }
                    } else {
                        gef = 1;
                    }
                }
            }
            if (gef == 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteSecondBereitsVorhanden;
                return;
            }
            if (gef == 2) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteSecondGesperrt;
                return;
            }
        }
        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;
    }

    /********************neueStimmkarteSecondZuMeldung********************************************
     * this.pStimmkarteSecond muß zwingend belegt sein - keine automatische Nummernvergabe bei StimmkarteSecond!
     * Generell: pStimmkarteSecond ist immer Alpha möglich.
     * */
    public void __neueStimmkarteSecondZuMeldung(DbBundle dbBundle) {
        int erg = 0;
        try {
        willenserklaerungOhneSammelkarteSperren(dbBundle);
        this.neueStimmkarteSecondZuMeldung_pruefe(dbBundle);
        if (this.rcIstZulaessig == false) {
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        if (this.pStimmkarteSecond.isEmpty()) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfAutomatischeVergabeXyNichtMoeglich;
            return;
        }

        /*StimmkarteSecond in db speichern*/
        EclStimmkartenSecond stimmkartenSecond = new EclStimmkartenSecond();
        stimmkartenSecond.stimmkarteSecond = pStimmkarteSecond;
        stimmkartenSecond.meldungsIdentGast = piMeldungsIdentGast;
        stimmkartenSecond.meldungsIdentAktionaer = piMeldungsIdentAktionaer;
        stimmkartenSecond.gueltigeKlasse = 1;
        stimmkartenSecond.gueltigeKlasse_Delayed = 1;
        erg = dbBundle.dbStimmkartenSecond.insert(stimmkartenSecond);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.neueStimmkarteSecondZuMeldung);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        willenserklaerung.stimmkarteSecond = pStimmkarteSecond;

        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.neueStimmkarteSecondZuMeldung - 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        this.rcIstZulaessig = true;
        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }
    }

    /**********************************sperrenStimmkarteSecond_pruefe***************************/
    /** Sperren einer StimmkarteSecond.
     *
     * Eingabeparameter:
     * > piStimmkarteSecond
     *
     * Fehlermeldungen:
     * > pfXyNichtVorhanden
     * > pmStimmkarteSecondGesperrt (StimmkarteSecond ist bereits gesperrt)
     *
     * Handhabungshinweise:
     * > falls die StimmkarteSecond "in Verwendung" ist, kann sie "eigentlich" nicht gesperrt werden.
     * 		Was heißt "in Verwendung"?: eine (der möglicherweise zwei) zugeordneten meldungen
     * 			ist anwesend, und StimmkarteSecond ist dort als aktuelle eingetragen.
     *
     *Aber: es gibt verschiedene GRÜNDE, eine StimmkarteSecond zu sperren:
     *a) Weitere Handlungsweisen einer (auch anwesenden) Person auf der HV sollen unterbunden werden
     *b) Eine Person hat ihre StimmkarteSecond (möglicherweise bereits benutzt) verloren, erhält eine neue,
     *und will die alte ungültig machen (im Sinne einer ErsatzStimmkarteSecond)
     *
     *zu b):  diese Stornierungen müssen auch möglich sein,
     *wenn die StimmkarteSecond aktuell in Verwendung ist!
     *zu a): das ist keine Sperren der StimmkarteSecond - sondern eine "Umordnung". Z.B. ein Bevollmächtigter, der
     *auf der HV ist, und dem die Vollmacht entzogen wird, muß ja als Gast weiter präsent bleiben. D.h. dieser
     *Fall wird nicht über "StimmkarteSecond sperren" abgewickelt, sondern die StimmkarteSecond verweist dann nur noch
     *auf eine Gastmeldung.
     *
     *Fazit: ein Sperren ist immer möglich, auch wenn die Karte präsent ist.
     *
     *Zum Delay: Da eine Sperre möglicherweise nach der Abstimmung erfolgt, die Abstimmung selbst aber noch gelten
     *soll, und das Einlesen / Auswerten dann erst nach der Sperre erfolgen würde, muß ein Delay implementiert werden.
     *
     *Herausforderung: bei Teilnahme über Smartphone, Online o.ä. muß der Teilnehmer-Status "umgekickt" werden.
     *Dies kann jedoch erst im dortigen Zusammenhang relaisiert werden.
     *LANGTODO Online-Teilnahme / Smartphone-Teilnahme
     */
    public void sperrenStimmkarteSecond_pruefe(DbBundle dbBundle) {

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        EclStimmkartenSecond stimmkarteSecond = null;
        stimmkarteSecond = dbBundle.dbStimmkartenSecond.ergebnisPosition(0); /*Nur zur Schreiberleichterung :-) */

        /*Überprüfen, ob bereits gesperrt*/
        if (((ptForceNonDelay == true || stimmkarteSecond.delayedVorhanden == 0 || 1 == 1 /*TODO _Delay deaktiviert*/)
                && stimmkarteSecond.stimmkarteSecondIstGesperrt == 1)
                || (ptForceNonDelay == false && stimmkarteSecond.delayedVorhanden == 1
                        && stimmkarteSecond.stimmkarteSecondIstGesperrt_Delayed == 1)) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteSecondGesperrt;
            return;
        }

        /*Überprüfen, ob Delayed wird*/
        if ((lDbBundle.willenserklaerungIstDelayed(
                KonstWillenserklaerung.sperrenStimmkarteSecond) /*Dann ist die Willenserklärung an sich delayed*/
                || stimmkarteSecond.delayedVorhanden == 1 /*Dann ist ZutrittsIdent selbst schon delayed*/)
                && ptForceNonDelay == false && 1 == 2 /*TODO _Delay deaktiviert*/
        ) {
            this.rcDelayed = true;
        } else { /* Nun die einzelnen zugeordneten Meldungen überprüfen.*/
            /*Tja, das muß aber nach derzeitigen Überlegungen nicht erfolgen.Ob Willenserklärungen der einzelnen
             * Meldungen delayed sind oder nicht, ist wurscht (zum Nachbuchen der Delayed-Meldungen sind ja die
             * Zutritts/StimmkartenSecond-Idents nicht mehr erforderlich).
             */
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;
    }

    public void sperrenStimmkarteSecond(DbBundle dbBundle) {
        int erg = 0;
        try {
        willenserklaerungOhneSammelkarteSperren(dbBundle);

        sperrenStimmkarteSecond_pruefe(dbBundle);

        if (this.rcIstZulaessig == false) {
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        EclStimmkartenSecond stimmkarteSecond = null;
        stimmkarteSecond = dbBundle.dbStimmkartenSecond.ergebnisPosition(0); /*Nur zur Schreiberleichterung :-) */

        if (this.rcDelayed == true) {
            if (stimmkarteSecond.delayedVorhanden == 0) {
                /*Bisher noch nicht delayed - deshalb die relevanten Felder ins Delayed übertragen*/
                stimmkarteSecond.meldungsIdentAktionaer_Delayed = stimmkarteSecond.meldungsIdentAktionaer;
                stimmkarteSecond.meldungsIdentGast_Delayed = stimmkarteSecond.meldungsIdentGast;
                stimmkarteSecond.delayedVorhanden = 1;
            }
            stimmkarteSecond.stimmkarteSecondIstGesperrt_Delayed = 1;
        } else {
            stimmkarteSecond.stimmkarteSecondIstGesperrt = 1;
        }
        erg = dbBundle.dbStimmkartenSecond.update(stimmkarteSecond);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        } else {
            this.rcIstZulaessig = true;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.sperrenStimmkarteSecond);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        if (this.rcDelayed == true) {
            willenserklaerung.delayed = 1;
        }
        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.sperrenStimmkarteSecond 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

    }

    /***********************************freigebenGesperrteStimmkarteSecond************************************************/
    /**Freigeben einer gesperrten StimmkarteSecond
     *
     * Eingabeparameter:
     * > piStimmkarteSecond
     *
     * Zum Delay:
     * > Grundsätzlich kein Delay dieser Funktion erforderlich.
     * > Allerdings ist möglicherweise schon das Sperren einer StimmkarteSecond Delayed, in diesem Fall muß dann auch
     * 		das Freigeben delayed werden.
     * => Delay erforderlich.
     *
     * Achtung: beim Neuvergeben einer StimmkarteSecond wird nur die nicht-delayete Versionierung überprüft! Sieht
     * Beschreibung dort!
     *
     */
    public void freigebenGesperrteStimmkarteSecond_pruefe(DbBundle dbBundle) {
        int erg = 0;

        this.lDbBundle = dbBundle;

        EclStimmkartenSecond stimmkarteSecond = null; /*Nur zur Schreiberleichterung :-) */

        /*Hinweis: für diese Willenserklärung kann evtlEinlesenMeldung nicht verwendet werden, da diese
         * abbricht, wenn eine StimmkarteSecond gesperrt ist
         */

        erg = dbBundle.dbStimmkartenSecond.read(this.piStimmkarteSecond);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfXyNichtVorhanden;
            return;
        }

        stimmkarteSecond = dbBundle.dbStimmkartenSecond.ergebnisPosition(0);

        if ((ptForceNonDelay == false && stimmkarteSecond.delayedVorhanden == 1
                && stimmkarteSecond.stimmkarteSecondIstGesperrt_Delayed == 0)
                || ((stimmkarteSecond.delayedVorhanden == 0 || 1 == 1 /*TODO _Delay deaktiviert*/
                        || ptForceNonDelay == true) && stimmkarteSecond.stimmkarteSecondIstGesperrt == 0)) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmStimmkarteSecondNichtGesperrt;
            return;
        }

        /*Überprüfen, ob Delayed wird*/
        if (ptForceNonDelay == false && 1 == 2 /*TODO _Delay deaktiviert*/
                && (lDbBundle.willenserklaerungIstDelayed(
                        KonstWillenserklaerung.freigebenGesperrteStimmkarteSecond) /*Dann ist die Willenserklärung an sich delayed*/
                        || stimmkarteSecond.delayedVorhanden == 1 /*Dann ist stimmkarteSecond selbst schon delayed*/)) {
            this.rcDelayed = true;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;

    }

    public void freigebenGesperrteStimmkarteSecond(DbBundle dbBundle) {
        int erg = 0;
        int hoechsteVersion = 0;

        try {
        willenserklaerungOhneSammelkarteSperren(dbBundle);

        freigebenGesperrteStimmkarteSecond_pruefe(dbBundle);
        if (this.rcIstZulaessig == false) {
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        /*Einlesen aller Sätze mit StimmkarteSecond*/
        dbBundle.dbStimmkartenSecond.read_alleVersionen(this.piStimmkarteSecond);

        /*Ermitteln aus allen Sätzen, die zur Klasse gehören: höchste Versionsnummer*/
        int i;
        for (i = 0; i < dbBundle.dbStimmkartenSecond.anzErgebnis(); i++) {
            if (dbBundle.dbStimmkartenSecond.ergebnisPosition(i).stimmkarteSecondVers > hoechsteVersion) {
                hoechsteVersion = dbBundle.dbStimmkartenSecond.ergebnisPosition(i).stimmkarteSecondVers;
            }
            if (dbBundle.dbStimmkartenSecond.ergebnisPosition(i).stimmkarteSecondVers_Delayed > hoechsteVersion) {
                hoechsteVersion = dbBundle.dbStimmkartenSecond.ergebnisPosition(i).stimmkarteSecondVers_Delayed;
            }

        }
        hoechsteVersion++;

        /*gültigen Datensatz mit der StimmkarteSecond einlesen*/
        erg = dbBundle.dbStimmkartenSecond.read(this.piStimmkarteSecond);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.freigebenGesperrteStimmkarteSecond 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
           return;
        }

        EclStimmkartenSecond stimmkarteSecond = null; /*Nur zur Schreiberleichterung :-) */
        stimmkarteSecond = dbBundle.dbStimmkartenSecond.ergebnisPosition(0);

        /*Versionsnummer updaten*/
        if (this.rcDelayed == false) {
            stimmkarteSecond.stimmkarteSecondVers = hoechsteVersion;
        } else {
            stimmkarteSecond.stimmkarteSecondVers_Delayed = hoechsteVersion;
            if (stimmkarteSecond.delayedVorhanden == 0) { /*Delayed noch nicht vorhanden*/
                stimmkarteSecond.meldungsIdentAktionaer_Delayed = stimmkarteSecond.meldungsIdentAktionaer;
                stimmkarteSecond.meldungsIdentGast_Delayed = stimmkarteSecond.meldungsIdentGast;
                stimmkarteSecond.stimmkarteSecondIstGesperrt_Delayed = stimmkarteSecond.stimmkarteSecondIstGesperrt;
                stimmkarteSecond.delayedVorhanden = 1;
            }
        }

        erg = dbBundle.dbStimmkartenSecond.update(stimmkarteSecond);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.freigebenGesperrteStimmkarteSecond);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;

        willenserklaerung.stimmkarteSecond = piStimmkarteSecond;
        if (this.rcDelayed == true) {
            willenserklaerung.delayed = 1;
        }
        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.freigebenGesperrteStimmkarteSecond 003");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        this.rcIstZulaessig = true;
        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

    }

    /***********************************Vollmachten an Dritten*******************************************************************/
    /*
     * Wichtige Hinweise:
     *
     * I Es kann Zyklen geben: in den Personen, aber nicht in den Willenserklärungsverweisen!
     * 		(1)A=>B (2)B=>C, (3)C=>A
     * 		Beim Storno von A wird A dann zweimal gefunden. (3) ist bereits möglicherweise storniert, wenn (1)
     * 		und folgende (und damit wieder (3) storniert wird.
     *
     * II Ein Bevollmächtigter kann mehrere Personen bevollmächtigen - in den Personen, aber nicht in den
     * 	 Willenserklärungsverweisen!
     * 		(1)A->B (2)B->C
     * 		(3)A->D (4)D->E
     * 		Bsp. Storno C durch A: Gibt es einen Weg von A nach C?
     *
     * III Ein Bevollmächtigter kann von mehreren Personen bevollmächtigt werden!
     * 		(1)A->B (2)B->C
     * 		(3)A->D (3)D->C
     *
     * IIII Damit komplexe Situationen möglich:
     * 		(1)A->B (2)B->D (3)D->E
     * 		(4)A->C (5)C->F (6)F->E
     * 			    (7)B->C
     * 		Auftrag: B storniert E => Sowohl (3) als auch (6) werden storniert, da es Wege gibt.
     *	D.h.: nur "es gibt einen Weg" reicht nicht aus - es muß auch überprüft werden, ob es nicht einen
     *	weiteren Weg vom Aktionär zu E gibt, der nicht über B geht! Dann:
     *		D->E Wird von B storniert, da kein Weg existiert der um "B herumgeht"
     *		F->E wird von B nicht storniert, da es noch einen zweiten Weg gibt.
     *
     *Lösungsansatz LA1 (Verworfen): eine Vollmacht kann nur an eine Person erteilt werden, die noch nicht
     *Bevollmächtigter ist.
     *Damit können die Fälle I, III, und damit auch IIII nicht auftreten.
     *
     *D.h. es bleibt nur noch Fall II - gibt es einen Weg von Vollmachtsstornierer zum Bevollmächtigten, dann
     *kann storniert werden.
     *
     * Aber: was macht man mit Vollmachten per Mail? Läßt sich nicht ausschließen, dass zwei Bevollmächtigte
     * die selbe Person Bevollmächtigen. Und leider auch keine Zyklen ausschließbar.
     *
     * Lösungsansatz LA2: eine (entferntere Folge)-Vollmacht kann nur dann storniert werden,
     * LA2A: wenn es zu diesem Bevollmächtigten nicht einen weiteren Weg ohne den "Stornierer" gibt oder
     * LA2B: wenn der Parameter "ein Bevollmächtigter darf alle anderen stornieren" aktiv ist
     * D.h. im Fall IIII:
     * LA2A: B kann E nicht stornieren. C kann E nicht stornieren. Aber jeder seinen direkten, also z.B.
     * 		C kann F stornieren, und B kann D stornieren, und damit verschwinden dann auch die VOllmachten an E.
     *
     * 		D.h. aber: jeder kann seine direkt gegebene Vollmacht (Willenserklärung) widerrufen. Ob dann
     * 		die Sub-Vollmachten ebenfalls widerrufen werden, hängt davon ab, ob es noch einen weiteren Weg
     * 		gibt ohne die gerade wiederrufene Vollmacht.
     * LA2B: Bkann E stornieren, dann wird aber sowohl (3) D=>E als auch (6)F->E storniert!
     *
     *
     * => Lösungsansatz LA2 ist wie folgt:
     * a) Eine direkt gegebene Vollmacht kann immer widerrufen werden
     * 			> Folgevollmachten dieser widerrufenen Vollmacht werden nur dann widerrufen, wenn es keinen
     * 				anderen Weg zu diesen gibt ohne über den widerrufenen Vertreter
     * b) Eine "entfernte untergeordnete" Vollmacht kann (auch bei Parameterstellung) nur widerrufen werden,
     * 		wenn es dorthin nicht einen anderen Weg ohne dem Widerrufer gibt
     * c) Bei "Parameter alles kann widerrufen werden" (oder durch den Aktionär selbst) kann auch eine
     * 		"entfernte untergeordnete Vollmacht"
     * 		immer widerrufen werden - es werden dann alle Vollmachten auf diese Vollmacht widerrufen. Egal
     * 		auf welchem Weg.
     * d) FolgeBevollmächtigte dieser widerrufenen Vollmacht werden nur dann widerrufen, wenn es keinen
     * 				anderen Weg zu diesen gibt ohne über den widerrufenen Vertreter
     *
     *
     *  Überlegungen zur Pending-Handhabung
     *  -----------------------------------
     *  F1)Vollmachten, bei denen der Vollmachtgeber unbekannt ist, landen im Pending.
     *  F2)	 Untervollmachten einer solchen Vollmacht landen im Pending.
     *
     *  F3) Stornos, bei denen der stornierte Bevollmächtigte unbekannt ist, landen im Pending.
     *  F4) Stornos, bei denen der Stornierende unbekannt ist, landen im Pending.
     *
     *  Fallbeispiel:
     *
     *  1 Vollmacht B->C => Pending
     *  2 Storno    D->C => Pending
     *  3 Vollmacht -1->C wird aktiv
     *  4 Vollmacht -1->D wird aktiv
     *  5 Vollmacht -1->B wird aktiv
     *
     *  Wie nun das gewünschte Ergebnis ist, kommt auf die tatsächlich gewünschte Reihenfolge an:
     *  Reihenfolge V1: 3, 4, 5, 2, 1
     *  -1->C
     *  -1->D
     *  -1->B
     *   D->C Storno, d.h. C ist storniert
     *   B->C d.h. C ist nicht mehr storniert
     *  Reihenfolge V2: 3, 4, 5, 1, 2
     *  -1->C
     *  -1->D
     *  -1->B
     *   B->C d.h. C ist vorhanden
     *   D->C Strono, d.h. C ist storniert
     *
     *   Möglicherweise Parameterabhängig noch einschränkbar (z.B. wenn nur direkt erteilte Vollmachten storniert
     *   werden können), aber unsicher.
     *   Fazit: Pending-Auflösung nur manuell / als Sonderschalterfunktion.
     */
    /****************************************************************************************************************************/

    /******************************"Durcharbeiten" / "Überprüfen" der Vollmachtsketten*******************************************/
    /**Grundsätzliches Verfahren:
     * > es wird rekursiv vorgegangen, bis "Ziel" gefunden wurde
     * > dabei wird EclWillensErklVollmachtenAnDritte.merker verwendet, um Schleifen aufzudecken:
     * 		> "ebene" wird mit jedem Rekursionsaufruf um 1 erhöht
     * 		> .merker wird mit ebene vorbelegt; sobald ein Element erreicht wurde, das > 0 ist => dieses Element nicht mehr berücksichtigen
     * 		> Nach dem Return alle merker > Ebene wieder auf 0 setzen
     */

    private int ebene = 0;

    /** von: kann 0 oder -1 sein - dann werden alle Vollmachten behandelt, die von 0 oder -1 ausgehen (0 und -1 gelten als "root")
     * ohne: -1 => es wird nach irgendeinem Weg gesucht; ansonsten: es wird nach einem Weg gesucht, der nicht über diesen Bevollmächtigten geht
     * */
    private boolean rekursivGibtEsWegVonZu(int von, int zu, int ohne) {
        int i, i1;
        boolean ergebnis = false;
//        int hilfsebene = ebene; /*aktuelle Ebene*/

        //		System.out.println("rekursiv von "+von+" zu "+zu+" ohne "+ohne);
        for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
            //			System.out.println("i="+i+"merker="+rcVollmachtenAnDritte[i].merker+" wurdeStorniert="+rcVollmachtenAnDritte[i].merker+
            //					"von= "+von+" ohne="+ohne+
            //					" GeberIdent="+rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent+
            //					"bevollmaechtigterDritterIdent="+rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent
            //					);
            if (rcVollmachtenAnDritte[i].merker == 0 /*Ansonsten wurde diese Vollmacht in diesem Durchlauf schon bearbeitet - Kreis!*/
                    && rcVollmachtenAnDritte[i].wurdeStorniert == false /*Ansonsten stornierte Vollmacht*/
                    && ((von != 0 && von != -1
                            && rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent == von)
                            || ((von == 0 || von == -1)
                                    && (rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent == 0
                                            || rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent == -1)))
                    && (ohne == -1
                            || rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent != ohne)) {
                //				System.out.println("Im If");
                if (rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent == zu) {
                    //					System.out.println("True 1");
                    return true; /*Ziel gefunden!*/
                }
                /*Nun rekursiver Aufruf mit zu als von :-)*/
                rcVollmachtenAnDritte[i].merker = ebene;
                ebene++;
                ergebnis = rekursivGibtEsWegVonZu(
                        rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent, zu, ohne);
                ebene--;
                if (ergebnis == true) {/*Weg gefunden*/
                    //					System.out.println("True 2");
                    return true;
                }
                /*Merker bereinigen!*/
                for (i1 = 0; i1 < rcVollmachtenAnDritteAnzahl; i1++) {
                    if (rcVollmachtenAnDritte[i1].merker > ebene) {
                        rcVollmachtenAnDritte[i1].merker = 0;
                    }
                }

            }
        }

        return false;
    }

    /**gibtEsWegVonZu - Bestimmen, ob es von Vollmachtsgeber zu Vollmachtsnehmer einen Weg gibt
     * Aufruf nur für >0 zulässig - denn von 0/-1 gibt es sicher immer einen Weg
     */
    private boolean gibtEsWegVonZu(int von, int zu) {
        int i;

        /*Initialisierungsarbeiten*/
        ebene = 0;
        for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
            rcVollmachtenAnDritte[i].merker = 0;
        }
        ebene++;
        return rekursivGibtEsWegVonZu(von, zu, -1);
    }

    /**gibtEsWegVonBasisZuOhne - Gibt es einen (weiteren) Weg von der Basis zu dem Bevollmächtigten, ohne über den Vollmachtsgeber zu gehen*/

    private boolean gibtEsWegVonBasisZuOhne(int zu, int ohne) {
        int i;
        //		System.out.println("von Basis zu "+zu+" ohne "+ohne);
        /*Initialisierungsarbeiten*/
        ebene = 0;
        for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
            rcVollmachtenAnDritte[i].merker = 0;
        }
        ebene++;
        return rekursivGibtEsWegVonZu(0, zu, ohne);
    }

    /**istDirekterNachfolger - ist "zu" direkter Nachfolger von "von"*/
    private boolean istDirekterNachfolger(int von, int zu) {
        int i;
        boolean gef = false;

        for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
            if (rcVollmachtenAnDritte[i].wurdeStorniert == false) {
                if (rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent == zu
                        && rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent == von) {
                    gef = true;
                }
            }
        }

        return gef;
    }

    /**auchAlsNichtDirekterNachfolgerVorhanden - "von" hat auch eine Vollmacht bekommen, die nicht
     * direkt von "zu" erteilt wurde */
    private boolean auchAlsNichtDirekterNachfolgerVorhanden(int von, int zu) {
        int i;
        boolean gef = false;

        for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
            if (rcVollmachtenAnDritte[i].wurdeStorniert == false) {
                if (rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent == zu
                        && rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent != von) {
                    gef = true;
                }
            }
        }

        return gef;
    }

    /*********************pruefenObVollmachtgeberVollmachtHat*************************************
     * Prüfen, ob Vollmachtgeber (in pWillenserklaerungGeberIdent) eine gültige Vollmacht besitzt.
     * Wird auch verwendet bei Vollmacht und Briefwahl anderer Art.
     * Voraussetzung: einlesenVollmachtenAnDritte() wurde vorher aufgerufen.
     *
     * Bei externem Aufruf (von außerhalb): bitte this.rcIstZulaessig vorher auf true setzen!
     */
    public void pruefenObVollmachtgeberVollmachtHat() {
        /*Prüfen, ob Vollmachtgeber eine gültige Vollmacht besitzt*/
        if (pWillenserklaerungGeberIdent > 0) { /*Ansonsten kein "Geber" spezifiziert, oder Aktionär ist selbst Vollmachtgeber*/
            int i;

            /*Programmierhinweis:
             * Vollmachtgebender kann mehrere Vollmachten gegeben haben!
             * Bevollmächtigter kann von mehreren verschiedenen Personen Bevollmächtigt sein!
             * => der Vollmachtgeber kann sowohl mit storno als auch ohne Storno seiner Bevollmächtigung vorhanden sein.
             * Für Fehlermeldung entsprechend ermitteln, welche Fälle vorhanden sind
             * */
            boolean gefOhneStorno = false; /*Der Vollmachtgeber wurde gefunden, und seine Vollmacht ist noch nicht storniert*/
            boolean gefMitStorno = false; /*Der Vollmachtgeber wurde gefunden, aber mit einer stornierten Vollmacht*/
            for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
                if (rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent == pWillenserklaerungGeberIdent) {/*Der Vollmachtsgeber wurde gefunden*/
                    if (rcVollmachtenAnDritte[i].wurdeStorniert == true) {/*Vollmachtgeber mittlerweile widerrufen*/
                        gefMitStorno = true;
                    } else {
                        gefOhneStorno = true;
                    }
                }
            }
            if (gefMitStorno == false
                    && gefOhneStorno == false) {/*Vollmachtsgeber hat keine Vollmacht von diesem Aktionär*/
                if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                    this.rcPending = true;
                }
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmVollmachtGeberHatKeineVollmacht;
                return;
            }
            if (gefMitStorno == true && gefOhneStorno == false) {/*Vollmachtgeber mittlerweile widerrufen*/
                if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                    this.rcPending = true;
                }
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmVollmachtGeberVollmachtIstWiderrufen;
                return;
            }
        }

    }

    /***********************************vollmachtAnDritte************************************************************************/
    /**Zuordnen einer Vollmacht an Dritte
     *
     * Eingabeparameter:
     * > pi*
     * > pEclPersonenNatJur
     * 		- falls nur ident gefüllt, dann Zuordnung einer bereits bestehenden Vollmacht - alle anderen Felder von pEclPersonenNatJur
     * 		  werden ignoriert.
     * 		- falls ident nicht gefüllt, dann wird eine neue Person angelegt, mit den entsprechenden Feldern aus pEclPersonenNatJur
     * > pWillenserklaerungGeberIdent
     * 		- wenn der Geber nicht der Aktionär selbst ist (d.h. -1, oder aber 0-undefiniert), dann muß der Geber
     * 			ein gültiger Bevollmächtigter sein
     *
     * Voraussetzungen:
     * > Vollmachten können nur Aktionärsmeldungen zugeordnet werden, nicht Gästen.
     * 		(einem Gast kann allerdings eine bestehende Vollmachts-PersonNatJur als "Gast selbst" zugeordnet werden, z.B. wenn
     * 		einem anwesenden Bevollmächtigten die Vollmacht entzogen wird)
     * > Ansonsten keine Voraussetzung - eine Vollmacht an Dritte kann grundsätzlich immer übermittelt und gespeichert werden.
     *
     * LANGTODO: Offener Punkt: Einschränkung im Portal - herkömmlicher Weg
     * > Lösung (falls erforderlich): über übergeordnete Funktion - in einer Transaktion abprüfen, welche Willenserklärungen bereits erhalten sind,
     * 		und erst dann ggf. Buchen oder ablehnen
     * > Äh, sollte auf jeden Fall implementiert werden - zum Vermeiden von Mißbrauch bzw. "Flooden" über das Portal!
     *
     *
     * Zum Delay:
     * > Grundsätzlich auf den ersten Blick kein Delay dieser Funktion erforderlich. Allerdings ist ein Delay des
     *	Widerrufs erforderlich - und dementsprechend müssen auch spätere Vollmachtserteilungen delayed werden (sonst
     *	gibts falsche Ergebnisse/Stornierungen beim Delay-Auflösen des Widerrufs!
     *
     * Zum Pending:
     * > diese Low-Level-Funktion läßt als Vollmachtsgeber nur einen Verweis auf eine PersonNatJur zu. Soll also
     * als Pending eine Vollmacht von einer noch nicht existierenden PersonNatJur gespeichert werden, so muß dies über
     * die High-Level-Funktion erfolgen: erst Bevollmächtigten als PersonNatJur speichern, und dann die Vollmacht als Pending.
     */
    public void vollmachtAnDritte_pruefe(DbBundle dbBundle) {

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        /*Prüfen, ob ein Aktionär referenziert wurde*/
        if (piMeldungsIdentAktionaer == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmVollmachtNurFuerAktionaerMoeglich;
            return;
        }

        einlesenVollmachtenAnDritte();

        /*Prüfen, ob Vollmachtgeber eine gültige Vollmacht besitzt*/
        pruefenObVollmachtgeberVollmachtHat();
        if (this.rcIstZulaessig == false) {
            return;
        }

        /*Prüfen, ob Bevollmächtigte Person schon vorhanden (falls Referenz eingetragen)*/
        if (pEclPersonenNatJur.ident != 0) {
            if (lDbBundle.dbPersonenNatJur.read(pEclPersonenNatJur.ident) < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmBevollmaechtigterNichtVorhanden;
                return;

            }
            ;

        }

        /*Prüfen ob delayed. Überlegung: peding-Buchungen werden nicht delayed, daher nicht vorher erforderlich*/
        if ((lDbBundle.willenserklaerungIstDelayed(
                KonstWillenserklaerung.vollmachtAnDritte) /*Dann ist die Willenserklärung an sich delayed*/
                || weiterverarbeitungDelayed == 1 /*Dann ist vorher was delayed*/) && ptForceNonDelay == false) {
            this.rcDelayed = true;
        }

        return;
    }

    public void vollmachtAnDritte(DbBundle dbBundle) {

        try {

        int erg = 0;

        this.rcIstZulaessig = true;

        willenserklaerungSperren(dbBundle);

        vollmachtAnDritte_pruefe(dbBundle);

        if (this.rcIstZulaessig == false && rcPending == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        if (pEclPersonenNatJur.ident == 0) {
            /*Neue Person speichern*/
            BlKennung lkennung = new BlKennung(dbBundle);
            lkennung.neueKennungUndOeffentlicheID(pEclPersonenNatJur.name, pEclPersonenNatJur.vorname);
            pEclPersonenNatJur.loginKennung = lkennung.ergebnisKennung;
            pEclPersonenNatJur.oeffentlicheID = lkennung.ergebnisOeffentlicheID;
            erg = dbBundle.dbPersonenNatJur.insert(pEclPersonenNatJur);
            if (erg < 1) {
                CaBug.drucke("002");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                willenserklaerungFreigeben(dbBundle);
                return;
            }

            /*Login-Kennungssatz zu neuer Person speichern*/
            EclLoginDaten lLoginDaten = new EclLoginDaten();
            lLoginDaten.loginKennung = lkennung.ergebnisKennung;
            lLoginDaten.kennungArt = KonstLoginKennungArt.personenNatJur;
            lLoginDaten.personenNatJurIdent = pEclPersonenNatJur.ident;
            lLoginDaten.passwortVerschluesselt = lkennung.ergebnisPasswortInitialVerschluesselt;
            lLoginDaten.passwortInitial = lkennung.ergebnisPasswortInitialFuerDatenbank;
            erg=dbBundle.dbLoginDaten.insert(lLoginDaten);
            rcLoginDaten = lLoginDaten;
            if (erg < 1) {
                CaBug.drucke("003");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                willenserklaerungFreigeben(dbBundle);
                return;
            }
        } else {
            erg = dbBundle.dbLoginDaten.read_personNatJurIdent(pEclPersonenNatJur.ident);
            if (erg < 1) {
                /**TODO Fehlersituation wird wegen Vereinsvollmacht ignoriert*/
//                CaBug.drucke("007 pEclPersonenNatJur.ident="+pEclPersonenNatJur.ident);
//                this.rcIstZulaessig = false;
//                this.rcGrundFuerUnzulaessig = erg;
//                dbBundle.dbBasis.rollbackTransaction();
//                dbBundle.dbBasis.endTransaction();
//                return;
                rcLoginDaten=null;
            }
            else {
                rcLoginDaten = dbBundle.dbLoginDaten.ergebnisPosition(0);
            }
        }

        /*Willenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.vollmachtAnDritte);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        if (this.rcDelayed == true) {
            willenserklaerung.delayed = 1;
        }

        /*Restliche Felder der Willenserklärung füllen und speichern*/
        willenserklaerung.bevollmaechtigterDritterIdent = pEclPersonenNatJur.ident;
        willenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
        if (this.rcPending == true) {/*Willenserklärung nur als Pending speichern*/
            willenserklaerung.pending = 1;
        }

        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

        /*Vertreter in Meldungssatz eintragen*/
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = piMeldungsIdentAktionaer;

        dbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        if (dbBundle.dbMeldungen.meldungenArray.length != 1) {
            CaBug.drucke("004");
        }

        lMeldung = dbBundle.dbMeldungen.meldungenArray[0];
        if (lMeldung.statusPraesenz == 0) {
            /*Eintrag des Vertreters im Meldesatz nur dann, wenn diese Meldung noch nie präsent war. Ansonsten würde ja ein
             * bereits über die Präsenz "fixierter" Vertreter überschrieben werden.*/

            /*Zum Delayed: da an dieser Stelle ein Eintrag nur dann erfolgt, wenn der Meldungssatz noch nie in irgendeiner Form
             * präsent war, kann auch kein Delay für diesen Satz vorliegen
             */

            lMeldung.vertreterName = pEclPersonenNatJur.name;
            lMeldung.vertreterName_Delayed = pEclPersonenNatJur.name;
            lMeldung.vertreterVorname = pEclPersonenNatJur.vorname;
            lMeldung.vertreterVorname_Delayed = pEclPersonenNatJur.vorname;
            lMeldung.vertreterOrt = pEclPersonenNatJur.ort;
            lMeldung.vertreterOrt_Delayed = pEclPersonenNatJur.ort;
            lMeldung.vertreterIdent = pEclPersonenNatJur.ident;
            lMeldung.vertreterIdent_Delayed = pEclPersonenNatJur.ident;
            dbBundle.dbMeldungen.update(lMeldung);
        }

        willenserklaerungFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

        return;
    }

    /***In den nachfolgenden Variablen werden die Erkenntnisse aus widerrufVollmachtAnDritte_pruefen gespeichert
     * zur weiteren Verwendung in widerrufVollmachtAnDritte
     */
    /**==true => Der zu stornierende Bevollmächtigte existiert auch noch mit anderen Vollmachtswegen - d.h. er wird
     * nicht vollständig storniert, sondern nur die direkt vom Stornierer erteilte Vollmacht*/
    private boolean vad_weitereVollmachtenFuerBevollmaechtigtenVorhanden = false;

    /***********************************widerrufVollmachtAnDritte************************************************/
    /**Widerrufen einer Vollmacht an Dritte
     *
     * Eingabeparameter:
     * > pi*
     * > pEclPersonenNatJur
     * 		- falls nur ident gefüllt, dann Zuordnung einer bereits bestehenden Vollmacht - alle anderen Felder von pEclPersonenNatJur
     * 		  werden ignoriert.
     * 		- falls ident nicht gefüllt, dann wird eine neue Person angelegt, mit den entsprechenden Feldern aus pEclPersonenNatJur
     * > pWillenserklaerungGeberIdent
     * 		- wenn der Geber nicht der Aktionär selbst ist (d.h. -1, oder aber 0-undefiniert), dann muß der Geber
     * 			ein gültiger Bevollmächtigter sein
     *
     * Voraussetzungen:
     * > Meldung darf mit diesem Vertreter nicht präsent sein.
     * 		Wie ist mit Präsenter Meldung zu verfahren? => High-Level-Funktion:
     * 		Meldung auf Abwesend / bisherigen Bevollmächtigten auf Gast / Vollmacht stornieren etc.
     * > Wer darf was stornieren?
     * 		- Immer:
     * 			 - pWillenserklaerungsGeberIdent=0 (undefiniert) darf alles stornieren
     * 			 - Vorgänger direkten Nachfolger
     * 		- je nach Parameter:
     * 			- Vorgänger alle Nachfolger (auch übersprungen)
     * 				paramVollmacht_VorgaengerDarfAlleNachfolgerStornieren
     * 			- Aktionär alle Bevollmächtigten (auch übersprungen)
     * 				paramVollmacht_AktionaerDarfAlleNachfolgerStornieren
     * 			- Bevollmächtigter alle anderen Bevollmächtigten (in anderen Linien, aber nicht die
     * 				direkt über ihm!
     * 				paramVollmacht_BevollmaechtigterDarfAllesStornieren
     *
     * Zum Delay:
     * > Delay ist erforderlich und wird unterstützt.
     *
     * Zum Pending:
     * > diese Low-Level-Funktion läßt als Vollmachtsstornier und als zu stornierendem Bevollmächtigten
     * nur jeweils einen Verweis auf eine PersonNatJur zu. Soll also
     * als Pending eine Stornierung von einer noch nicht existierenden PersonNatJur
     * bzw. einer Vollmacht einer noch nicht existierenden PersonNatJur gespeichert werden, so muß dies über
     * die High-Level-Funktion erfolgen: erst Stornierer/Bevollmächtigten als PersonNatJur speichern,
     * und dann die Vollmacht als Pending.
     *
     *
     */
    public void widerrufVollmachtAnDritte_pruefe(DbBundle dbBundle) {
        int i;
        boolean gefBevollmaechtigter = false;
        boolean gefBevollmaechtigterStorno = false;
        this.lDbBundle = dbBundle;
        CaBug.druckeLog("A", logDrucken, 10);

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            CaBug.druckeLog("B", logDrucken, 10);
            return;
        }

        /*Prüfen, ob ein Aktionär referenziert wurde*/
        if (piMeldungsIdentAktionaer == 0) {
            CaBug.druckeLog("C", logDrucken, 10);
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmVollmachtNurFuerAktionaerMoeglich;
            return;
        }
        CaBug.druckeLog("D", logDrucken, 10);

        einlesenVollmachtenAnDritte();
        CaBug.druckeLog("E", logDrucken, 10);
        einlesenZuordnungZuSammelkarten();
        CaBug.druckeLog("F", logDrucken, 10);

        /*Überprüfen: gehört spezifizierte Vollmacht zu den Vollmachten - ist sie noch gültig?*/
        for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
            if (rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent == pEclPersonenNatJur.ident) {
                if (rcVollmachtenAnDritte[i].wurdeStorniert == true) {/*Vollmacht bereits widerrufen*/
                    gefBevollmaechtigterStorno = true;
                } else {
                    gefBevollmaechtigter = true;
                }
            }
        }
        if (gefBevollmaechtigter == false && gefBevollmaechtigterStorno == false) {/*Bevollmächtigter Nicht Vorhanden*/
            CaBug.druckeLog("G", logDrucken, 10);
            if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                this.rcPending = true;
            }
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmBevollmaechtigterNichtVorhanden;
            return;
        }
        if (gefBevollmaechtigterStorno == true && gefBevollmaechtigter == false) {/*Vollmacht bereits widerrufen*/
            CaBug.druckeLog("H", logDrucken, 10);
            if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                this.rcPending = true;
            }
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmVollmachtBereitsWiderrufen;
            return;
        }

        CaBug.druckeLog("I", logDrucken, 10);

        /*Überprüfen: ist Vollmacht gerade präsent?*/
        /*LANGTODO - Präsenz überprüfen derzeit noch nicht implementiert - erst machen wenn Zu-/Abgang etc.*/

        /*Prüfen, ob Vollmachtgeber eine gültige Vollmacht besitzt*/
        if (pWillenserklaerungGeberIdent > 0) { /*Ansonsten kein "Geber" spezifiziert, oder Aktionär ist selbst Vollmachtgeber*/
            CaBug.druckeLog("J", logDrucken, 10);
            einlesenVollmachtenAnDritte();

            /*Programmierhinweis:
             * Vollmachtgebender kann mehrere Vollmachten gegeben haben!
             * Bevollmächtigter kann von mehreren verschiedenen Personen Bevollmächtigt sein!
             * => der Vollmachtgeber kann sowohl mit storno als auch ohne Storno seiner Bevollmächtigung vorhanden sein.
             * Für Fehlermeldung entsprechend ermitteln, welche Fälle vorhanden sind
             * */
            boolean gefOhneStorno = false; /*Der Vollmachtgeber wurde gefunden, und seine Vollmacht ist noch nicht storniert*/
            boolean gefMitStorno = false; /*Der Vollmachtgeber wurde gefunden, aber mit einer stornierten Vollmacht*/
            for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
                if (rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent == pWillenserklaerungGeberIdent) {/*Der Vollmachtsgeber wurde gefunden*/
                    if (rcVollmachtenAnDritte[i].wurdeStorniert == true) {/*Vollmachtgeber mittlerweile widerrufen*/
                        gefMitStorno = true;
                    } else {
                        gefOhneStorno = true;
                    }
                }
            }
            if (gefMitStorno == false
                    && gefOhneStorno == false) {/*Vollmachtsgeber hat keine Vollmacht von diesem Aktionär*/
                CaBug.druckeLog("K", logDrucken, 10);
                if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                    this.rcPending = true;
                }
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmVollmachtGeberHatKeineVollmacht;
                return;
            }
            if (gefMitStorno == true && gefOhneStorno == false) {/*Vollmachtgeber mittlerweile widerrufen*/
                CaBug.druckeLog("L", logDrucken, 10);
                if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                    this.rcPending = true;
                }
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmVollmachtGeberVollmachtIstWiderrufen;
                return;
            }
        }

        /*Prüfen ob delayed. Überlegung: peding-Buchungen werden nicht delayed, daher nicht vorher erforderlich*/
        if ((lDbBundle.willenserklaerungIstDelayed(
                KonstWillenserklaerung.widerrufVollmachtAnDritte) /*Dann ist die Willenserklärung an sich delayed*/
                || weiterverarbeitungDelayed == 1 /*Dann ist vorher was delayed*/) && ptForceNonDelay == false) {
            this.rcDelayed = true;
        }

        /*Überprüfen: darf der Vollmachtgebende genau diese Vollmacht widerrufen?*/
        CaBug.druckeLog("M", logDrucken, 10);

        if (pWillenserklaerungGeberIdent == 0) {
            /*Vollmachtgeber undefiniert - darf alles widerrufen*/
            CaBug.druckeLog("N", logDrucken, 10);
            this.rcIstZulaessig = true;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            return;
        }

        if (pWillenserklaerungGeberIdent == -1) {
            CaBug.druckeLog("O", logDrucken, 10);
            /*Aktionär*/
            if (lDbBundle.param.paramWillenserklaerungen.paramVollmacht_AktionaerDarfAlleNachfolgerStornieren == true) {
                /*lt. Parameter darf Aktionär alles*/

                this.rcIstZulaessig = true;
                this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                return;
            }

            if (istDirekterNachfolger(-1, pEclPersonenNatJur.ident)) {
                /*Bevollmächtigter ist direkter Nachfolger von Aktionär - d.h. er darf storniert werden -
                 * aber prüfen, ob Bevollmächtigter auch noch von einem anderen Bevollmächtigt wurde -
                 * diese würde dann nämlich erhalten bleiben*/
                this.rcIstZulaessig = true;
                this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                if (auchAlsNichtDirekterNachfolgerVorhanden(-1, pEclPersonenNatJur.ident)) {
                    this.rcWarnung = true;
                    this.rcGrundFuerWarnung = CaFehler.pmWeitereVollmachtFuerBevollmaechtigtemVorhanden;
                    vad_weitereVollmachtenFuerBevollmaechtigtenVorhanden = true;
                }
                return;
            }
            /*Bevollmächtigter ist nicht direkter Nachfolger von Aktionär, und Aktionär darf nicht alles stornieren*/
            if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                this.rcPending = true;
            }
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmAktionaerDarfNurDirektenNachfolgerWiderrufen;
            return;
        }

        /*Hier: "Stornierer" ist selbst Bevollmächtiger, und definiert*/

        if (istDirekterNachfolger(pWillenserklaerungGeberIdent, pEclPersonenNatJur.ident)) {
            CaBug.druckeLog("Q", logDrucken, 10);
            /*Direkter Nachfolger - kann in jedem Fall storniert werden - aber prüfen, ob noch zweiter Weg zum
             * Bevollmächtigten vorhanden - dann Warnung*/
            this.rcIstZulaessig = true;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            if (gibtEsWegVonBasisZuOhne(pEclPersonenNatJur.ident, pWillenserklaerungGeberIdent)) {
                this.rcWarnung = true;
                this.rcGrundFuerWarnung = CaFehler.pmWeitereVollmachtFuerBevollmaechtigtemVorhanden;
                vad_weitereVollmachtenFuerBevollmaechtigtenVorhanden = true;
            }
            return;
        }
        CaBug.druckeLog("R", logDrucken, 10);

        if (gibtEsWegVonZu(pWillenserklaerungGeberIdent, pEclPersonenNatJur.ident)) {
            CaBug.druckeLog("S", logDrucken, 10);

            /*Ist Nachfolger, aber nicht direkter => nur stornierbar, wenn Parameter*/
            if (lDbBundle.param.paramWillenserklaerungen.paramVollmacht_VorgaengerDarfAlleNachfolgerStornieren == false) {
                if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                    this.rcPending = true;
                }
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmBevollmaechtigterDarfNurDirektenNachfolgerWiderrufen;
                return;
            }

            /*Lt. Parameter auch nicht-direkte Nachfolger stornierbar - aber checken, ob noch anderer Weg - in diesem
             * Fall dann nicht stornierbar! Ausnahme: Bevollmächtigter darf ALLES andere stornieren*/
            if (gibtEsWegVonBasisZuOhne(pEclPersonenNatJur.ident, pWillenserklaerungGeberIdent)
                    && lDbBundle.param.paramWillenserklaerungen.paramVollmacht_BevollmaechtigterDarfAllesStornieren == false) {
                if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                    this.rcPending = true;
                }
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmWeitereVollmachtFuerBevollmaechtigtemVorhanden;
                return;
            }

            /*Vollmacht ist von "Stornier" abhängig, kein alternativer Weg oder: alternativer Weg aber Bevollmächtigter darf alles stornieren => Stornierbar*/
            this.rcIstZulaessig = true;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            return;

        }
        CaBug.druckeLog("T", logDrucken, 10);

        /*Bevollmächtigter ist nicht vom Stornierer abhängig - nur stornierbar bei entsprechender Parameterstellung*/
        if (lDbBundle.param.paramWillenserklaerungen.paramVollmacht_BevollmaechtigterDarfAllesStornieren == true) {
            /*Parameter - alles widerrufbar. Vollmacht des Vollmachtgebers darf aber nicht (alleine) von dem
             * zu widerrufenden Bevollmächtigten abhängig sein -d.h. es muß Weg vom Aktionär zum "Stornier"
             * ohne dem "Stornierten" geben*/
            if (gibtEsWegVonBasisZuOhne(pWillenserklaerungGeberIdent, pEclPersonenNatJur.ident) == false) {
                /*Stornierer ist vom Bevollmächtigten abhängig - kann nicht storniert werden*/
                if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                    this.rcPending = true;
                }
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmEigenerVollmachtsgeberKannNichtWiderrufenWerden;
                return;
            }
            CaBug.druckeLog("U", logDrucken, 10);
            this.rcIstZulaessig = true;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            return;
        } else {
            /*Nicht stornierbar - nicht abhängig, und Parameter läßt was anderes nicht zu*/
            if (lDbBundle.pendingIstMoeglich(KonstWillenserklaerung.vollmachtAnDritte)) {
                this.rcPending = true;
            }
            this.rcIstZulaessig = false;
            CaBug.druckeLog("V", logDrucken, 10);
            this.rcGrundFuerUnzulaessig = CaFehler.pmVollmachtNichtVonVollmachtgeberAbhaenig;
            return;
        }
    }

    /**Stornieren aller Vollmachten des zu stornierenden Bevollmächtigten sowie dessen
     * Untervollmachten, so sie nicht erreichbar sind
     *
     *Übergabeparameter:
     * 		ident des Vertreters, dessen Vollmachten storniert werden sollen
     * 		willenserklaerungIdent, zu deren Folge die nachfolgenden Stornierungen gebucht werden sollen
     **/
    private void rekursivStornoBevollmaechtigten(int bevollmaechtigter, int folgeBuchungFuerIdent) {

        //		System.out.println("rekursivStornoBevollmaechtigten bevollmaechtigter="+bevollmaechtigter+" folgeBuchungFuerIdent="+folgeBuchungFuerIdent);
        EclWillenserklaerung willenserklaerungFolge = null;
        EclWillenserklaerungZusatz willenserklaerungFolgeZusatz = null;
        int i;
        int erg = 0;

        /*Alle Vollmachten auf den zu stornierenden Bevollmächtigten suchen und stornieren*/
        for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
            if (rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent == bevollmaechtigter
                    && rcVollmachtenAnDritte[i].wurdeStorniert == false) {
                /*Für diesen Satz einen Widerruf eintragen*/
                prepareWillenserklaerung(KonstWillenserklaerung.widerrufVollmachtAnDritte);
                willenserklaerungFolge = preparedWillenserklaerung;
                willenserklaerungFolgeZusatz = preparedWillenserklaerungZusatz;
                willenserklaerungFolge.verweisart = 4;
                willenserklaerungFolge.folgeBuchungFuerIdent = folgeBuchungFuerIdent;
                willenserklaerungFolge.verweisAufWillenserklaerung = rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungIdent;
                willenserklaerungFolge.willenserklaerungGeberIdent = rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent;
                /*alt: pWillenserklaerungGeberIdent;*/
                willenserklaerungFolge.bevollmaechtigterDritterIdent = rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent;
                /*alt: pEclPersonenNatJur.ident;*/
                if (this.rcDelayed == true) {
                    willenserklaerungFolge.delayed = 1;
                }
                erg = lDbBundle.dbWillenserklaerung.insert(willenserklaerungFolge, willenserklaerungFolgeZusatz);
                if (erg < 1) {
                    CaBug.drucke("BlWillenserklaerung.widerrufVollmachtAnDritte 004");
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = erg;
                    return;
                }
                lDbBundle.dbWillenserklaerung.update_setzeStorno(
                        rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungIdent, 1, 1);
                /*Satz selbst als storniert kennzeichnen*/
                rcVollmachtenAnDritte[i].wurdeStorniert = true;

            }
        }

        /*Alle VOllmachten finden, die der stornierte Bevollmächtigte erteilt hat.
         * Wenn diese Bevollmächtigten nicht auf einem anderne Weg erreicht werden können, dann auch diese
         * stornieren (durch rekursiven Aufruf dieser Funktion)
         */

        /*Umgang mit Schleifen?
         * Es wird nur aufgerufen für Vollmachten, die zu löschen sind, und die nicht auf anderen Weg erreichbar
         * sind, und die noch nicht storniert sind.
         * Bsp:
         * A => B => C => D => C
         * - Storno ab B: C ist beim zweiten Aufruf storniert, d.h. Schleife durchbrochen
         * - Strono von C: C nach D ist bereits storniert
         * - Storno von D: D=>C wg. direktem Nachfolger wird storniert. Schleife bei C dann durchbrochen,
         * 		weil auf anderem Weg erreichbar
         */
        for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
            if (rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent == bevollmaechtigter
                    && rcVollmachtenAnDritte[i].wurdeStorniert == false) {
                //				System.out.println("Im If i ="+i);
                //				System.out.println("willenserklaerungGeberIdent="+rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent+
                //						" bevollmaechtigterDritterIdent="+rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent);
                if (gibtEsWegVonBasisZuOhne(
                        rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent,
                        bevollmaechtigter) == false) {/*Gibt es anderen Weg?*/
                    /*Falls es keinen anderen Weg gibt: Rekursiv löschen*/
                    //					System.out.println("im If - If");
                    setzeVertreterAufReload(rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent);
                    rekursivStornoBevollmaechtigten(
                            rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent,
                            folgeBuchungFuerIdent);
                } else {
                    //					System.out.println("im If - else");
                    /*Es gibt noch einen anderen Weg - deshalb nur die direkt erteilte Vollmacht vom Gesamt-stornierten
                     * Vollmachtsgeber aus löschen
                     */
                    /*Für diesen Satz einen Widerruf eintragen*/
                    prepareWillenserklaerung(KonstWillenserklaerung.widerrufVollmachtAnDritte);
                    willenserklaerungFolge = preparedWillenserklaerung;
                    willenserklaerungFolgeZusatz = preparedWillenserklaerungZusatz;
                    willenserklaerungFolge.verweisart = 4;
                    willenserklaerungFolge.folgeBuchungFuerIdent = folgeBuchungFuerIdent;
                    willenserklaerungFolge.verweisAufWillenserklaerung = rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungIdent;
                    willenserklaerungFolge.willenserklaerungGeberIdent = rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent;
                    /*alt: pWillenserklaerungGeberIdent;*/
                    willenserklaerungFolge.bevollmaechtigterDritterIdent = rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent;
                    /*alt: pEclPersonenNatJur.ident;*/
                    if (this.rcDelayed == true) {
                        willenserklaerungFolge.delayed = 1;
                    }
                    //					System.out.println("1");
                    erg = lDbBundle.dbWillenserklaerung.insert(willenserklaerungFolge, preparedWillenserklaerungZusatz);
                    //					System.out.println("2");
                    if (erg < 1) {
                        CaBug.drucke("BlWillenserklaerung.widerrufVollmachtAnDritte 005");
                        this.rcIstZulaessig = false;
                        this.rcGrundFuerUnzulaessig = erg;
                        return;
                    }
                    /*Satz selbst als storniert kennzeichnen*/
                    rcVollmachtenAnDritte[i].wurdeStorniert = true;

                }

            }
        }

        /*Stornieren aller Sammelkarten-Vollmachten, die von diesem Bevollmächtigten ausgehen*/
        widerrufVollmachtUndWeisungVonPerson(lDbBundle, bevollmaechtigter, folgeBuchungFuerIdent);
    }

    private void korrigiereVollmachtAnDritteInMeldung(DbBundle dbBundle) {
        /*Nun Vollmacht an Dritte in Eclmeldung korrigieren*/
        //		System.out.println("A Storno - korrigieren");
        this.einlesenVollmachtenAnDritte();
        EclMeldung lMeldung = new EclMeldung();
        lMeldung.meldungsIdent = piMeldungsIdentAktionaer;
        dbBundle.dbMeldungen.leseZuMeldungsIdent(lMeldung);
        if (dbBundle.dbMeldungen.meldungenArray.length != 1) {
            CaBug.drucke("BlWillenserklaerung.vollmachtAnDritte 001");
        }
        lMeldung = dbBundle.dbMeldungen.meldungenArray[0];
        lMeldung.vertreterName = "";
        lMeldung.vertreterName_Delayed = "";
        lMeldung.vertreterVorname = "";
        lMeldung.vertreterVorname_Delayed = "";
        lMeldung.vertreterOrt = "";
        lMeldung.vertreterOrt_Delayed = "";
        lMeldung.vertreterIdent = 0;
        lMeldung.vertreterIdent_Delayed = 0;
        //		System.out.println("B Storno - korrigieren");

        if (rcVollmachtenAnDritte != null && rcVollmachtenAnDritte.length > 0) {
            int i1;
            int hoechsteWillenserklaerung = 0;
            for (i1 = 0; i1 < rcVollmachtenAnDritte.length; i1++) {
                if (rcVollmachtenAnDritte[i1].willenserklaerungErteilt.willenserklaerungIdent > hoechsteWillenserklaerung) {
                    if (rcVollmachtenAnDritte[i1].wurdeStorniert == false) {
                        lMeldung.vertreterName = rcVollmachtenAnDritte[i1].bevollmaechtigtePerson.name;
                        lMeldung.vertreterVorname = rcVollmachtenAnDritte[i1].bevollmaechtigtePerson.vorname;
                        lMeldung.vertreterOrt = rcVollmachtenAnDritte[i1].bevollmaechtigtePerson.ort;
                        lMeldung.vertreterIdent = rcVollmachtenAnDritte[i1].bevollmaechtigtePerson.ident;
                        hoechsteWillenserklaerung = rcVollmachtenAnDritte[i1].willenserklaerungErteilt.willenserklaerungIdent;
                    }
                }

            }
        }
        dbBundle.dbMeldungen.update(lMeldung);

        //		System.out.println("C Storno - korrigieren");

    }

    private void setzeVertreterAufReload(int pPersonIdent) {
        CaBug.druckeLog("pPersonIdent="+pPersonIdent, logDrucken, 3);
        int rc=lDbBundle.dbPersonenNatJur.read(pPersonIdent);
        if (rc==0) {
            CaBug.drucke("001");
            return;
        }
        String kennung=lDbBundle.dbPersonenNatJur.personenNatJurArray[0].loginKennung;
        if (kennung.isEmpty()) {
            CaBug.drucke("002");
            return;
        }
        lDbBundle.dbLoginDaten.update_letzterLoginAufServerNegativ(kennung);
    }
    
    
    
    
    public void widerrufVollmachtAnDritte(DbBundle dbBundle) {
        
        
 
        try {
       
        int i, gef = -1, erg = 0;

        CaBug.druckeLog("A", logDrucken, 10);

        EclWillenserklaerung willenserklaerung = null;
        EclWillenserklaerungZusatz willenserklaerungZusatz = null;

        willenserklaerungSperren(dbBundle);

        this.widerrufVollmachtAnDritte_pruefe(dbBundle);
        CaBug.druckeLog("B", logDrucken, 10);

        if (this.rcIstZulaessig == false && rcPending == true) {
            CaBug.druckeLog("C", logDrucken, 10);
            /*Einfach Storno als Pending speichern - keine weiteren Aktionen!*/
            prepareWillenserklaerung(KonstWillenserklaerung.widerrufVollmachtAnDritte);
            willenserklaerung = preparedWillenserklaerung;
            willenserklaerungZusatz = preparedWillenserklaerungZusatz;

            willenserklaerung.verweisart = 0;
            willenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
            willenserklaerung.bevollmaechtigterDritterIdent = pEclPersonenNatJur.ident;
            willenserklaerung.pending = 1;
            erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
            if (erg < 1) {
                CaBug.drucke("BlWillenserklaerung.widerrufVollmachtAnDritte 003");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                willenserklaerungFreigeben(dbBundle);
                return;
            }
            rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

            willenserklaerungFreigeben(dbBundle);
            return;
        }

        if (this.rcIstZulaessig == false) {
            CaBug.druckeLog("D", logDrucken, 10);
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        CaBug.druckeLog("E", logDrucken, 10);

        /*Einfachster Fall: für den Bevollmächtigten sind unterschiedliche Vollmachten vorhanden - es wird nur
         * die vom Stornierer direkt erteilte Vollmacht storniert. Keine Folgevollmachten-Stornierung etc.
         */
        if (vad_weitereVollmachtenFuerBevollmaechtigtenVorhanden == true) {
            /*zu stornierende Willenserklärung raussuchen*/
            for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
                if (rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent == pEclPersonenNatJur.ident
                        && rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent == pWillenserklaerungGeberIdent
                        && rcVollmachtenAnDritte[i].wurdeStorniert == false) {
                    gef = i;
                }
            }
            if (gef == -1) {
                CaBug.drucke("BlWillenserklaerung.widerrufVollmachtAnDritte 001");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                willenserklaerungFreigeben(dbBundle);
                return;
            }

            /*Neue Willenserklärung mit Verweis auf zu stornierende erzeugen*/
            prepareWillenserklaerung(KonstWillenserklaerung.widerrufVollmachtAnDritte);
            willenserklaerung = preparedWillenserklaerung;
            willenserklaerungZusatz = preparedWillenserklaerungZusatz;

            if (this.rcDelayed == true) {
                willenserklaerung.delayed = 1;
            }
            willenserklaerung.verweisart = 4;
            willenserklaerung.verweisAufWillenserklaerung = rcVollmachtenAnDritte[gef].willenserklaerungErteilt.willenserklaerungIdent;
            willenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
            willenserklaerung.bevollmaechtigterDritterIdent = pEclPersonenNatJur.ident;
            erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
            if (erg < 1) {
                CaBug.drucke("BlWillenserklaerung.widerrufVollmachtAnDritte 002");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                willenserklaerungFreigeben(dbBundle);
                return;
            }
            erg = dbBundle.dbWillenserklaerung.update_setzeStorno(
                    rcVollmachtenAnDritte[gef].willenserklaerungErteilt.willenserklaerungIdent, 1, 1);
            rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

            rcVollmachtenAnDritte[gef].wurdeStorniert = true;/*An dieser Stelle eigentlich unnötig, da keine Folgeverarbeitung mehr*/

            /*Nun Vollmacht an Dritte in Eclmeldung korrigieren*/
            korrigiereVollmachtAnDritteInMeldung(dbBundle);

            willenserklaerungFreigeben(dbBundle);
            return;
        }

        CaBug.druckeLog("F", logDrucken, 10);

        /*Es werden alle vorhandenen Vollmachten an den Bevollmächtigten storniert (egal von wem),
         * und deshalb alle Folgevollmachten ebenfalls.
         */

        /*"Basis-Willenserklärung" erzeugen und speichern*/
        willenserklaerung = null;
        prepareWillenserklaerung(KonstWillenserklaerung.widerrufVollmachtAnDritte);
        willenserklaerung = preparedWillenserklaerung;
        willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        if (this.rcDelayed == true) {
            willenserklaerung.delayed = 1;
        }
        willenserklaerung.verweisart = 4;
        willenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
        willenserklaerung.bevollmaechtigterDritterIdent = pEclPersonenNatJur.ident;

        for (i = 0; i < rcVollmachtenAnDritteAnzahl; i++) {
            if (rcVollmachtenAnDritte[i].willenserklaerungErteilt.bevollmaechtigterDritterIdent == pEclPersonenNatJur.ident
                    && rcVollmachtenAnDritte[i].willenserklaerungErteilt.willenserklaerungGeberIdent == pWillenserklaerungGeberIdent
                    && rcVollmachtenAnDritte[i].wurdeStorniert == false) {
                gef = i;
            }
        }
        if (gef != -1) {
            /*Checken: gibt es eine direkte Vollmacht vom Stornierer an den Bevollmächtigten? Denn dann
             * wird die Stornierungs-Willenserklärung auf diese verweisen.
             */
            willenserklaerung.verweisAufWillenserklaerung = rcVollmachtenAnDritte[gef].willenserklaerungErteilt.willenserklaerungIdent;
            rcVollmachtenAnDritte[gef].wurdeStorniert = true;
        } else {
            /*Ansonsten: die eigentliche Stornierungs-Willenserklärung dient nur als "Basis" - sie verweist nicht
             * direkt auf eine Vollmacht.
             */
            /*Also keinen Verweis eintragen!*/
        }
        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.widerrufVollmachtAnDritte 003");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;
        CaBug.druckeLog("G", logDrucken, 10);

        setzeVertreterAufReload(pEclPersonenNatJur.ident);
        if (gef != -1) {
            dbBundle.dbWillenserklaerung.update_setzeStorno(
                    rcVollmachtenAnDritte[gef].willenserklaerungErteilt.willenserklaerungIdent, 1, 1);
        }

        /*Stornieren aller Vollmachten des zu stornierenden Bevollmächtigten sowie dessen
         * Untervollmachten, so sie nicht erreichbar sind*/

        /*Übergabeparameter:
         * 		ident des Vertreters, dessen Vollmachten storniert werden sollen
         * 		willenserklaerungIdent, zu deren Folge die nachfolgenden Stornierungen gebucht werden sollen
         * */
        rekursivStornoBevollmaechtigten(pEclPersonenNatJur.ident, willenserklaerung.willenserklaerungIdent);
        CaBug.druckeLog("H", logDrucken, 10);

        /*Nun Vollmacht an Dritte in Eclmeldung korrigieren*/
        korrigiereVollmachtAnDritteInMeldung(dbBundle);
        CaBug.druckeLog("I", logDrucken, 10);

        willenserklaerungFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }
        return;

    }

    /******************************Sammelkarten - SRV, KI/AV, etc.***************************************************
     *
     * Für die unterschiedlichen Willenserklärungen gibt es unterschiedliche Funktionen. Diese laufen jedoch
     * letztendlich in eine gemeinsame "allumfassende" Funktion ein.
     *
     * Eingabeparameter:
     * > Wie üblich - Meldung
     * > pAufnehmendeSammelkarteIdent - zwingend auf dieser Ebene erforderlich (z.B. können ja für Vollmacht/Weisung SRV
     * 		unterschiedliche Sammelkarten - eine für Papier, eine für Internet - angelegt sein. D.h. ggf. die Festlegung
     * 		der Sammelkartennr muß in High-Level-Funktionen erledigt werden)
     * > ggf. EclWeisungMeldung (siehe Hinweise) - Weisungsstring
     *
     * Hinweise:
     * > falls EclWeisungMeldung==null, dann nur Vollmacht, keine Weisung (bei Briefwahl z.B. unzulässig)
     * > Die Weisungen selbst müssen vorher auf Regeln überprüft und interpretiert werden. Insbesondere auch
     * 	alle Gesamtmarkierungen (z.B. gemäßEmpfehlungen) so aufgelöst werden, dass sie in den Detailweisungen
     * 	gespeichert sind.
     * 	TODOLANG: Was allerdings noch - wegen Transaktionssicherheit - gemacht werden muß innerhalb dieser
     * 	Funktion: Übertragen der aktuellen Empfehlungen. Wird aber erst programmiert, wenn Weisungsinterpretation
     *  gemacht wird.
     *
     * Grundsätzliche, maximale Struktur:
     *
     * Überprüfen ob möglich:
     * > Grundsätzlich ist Abgabe der Willenserklärungen dieser Art jederzeit möglich.
     * > Selbst zweimal die selbe Willenserklärung vom selben Geber (also z.B. zweimal Vollmacht an SRV durch Aktionär)
     * 		ist zulässig - z.B. wenn zwei mal schriftliche Weisung eingeht.
     * > D.h. die Überprüfung ob möglich beschränkt sich darauf, ob die bei der Willenserklärung mit angegebene
     * 		Sammelkarte die abgegebene Willenserklärung akzeptiert (z.B. zutreffende Weisungen)
     *
     *  Verarbeitungsschritte (nach Prüfung):
     *  > Ermitteln, ob Zuordnung aktiv wird oder inaktiv
     *
     *  > Zuordnung Sammelkarte <-> MeldungsIdent eintragen (mit Kennzeichen "aktiv" oder "inaktiv")
     *  > Falls aktiv: Summen Aktien etc. in Sammelkarte verändern
     *
     *  > Weisung für MeldungsIdent eintragen - Normal oder Split (mit Kennzeichen "aktiv" oder "inaktiv")
     *  > Falls aktiv: Summe Weisungen in Sammelkarte verändern
     *
     *  > Willenserklärung speichern
     ****************************************************************************************************************/

    /** Sub-Funktion für Aktivieren/Deaktivieren: Austragen / Deaktivieren /Verändern Aktienbestand der aktuell eingetragenen
     * Willenserkärung
     */

    private boolean veraendernAktienbestandAktuelleWillenserklaerung(DbBundle pDbBundle, EclMeldung pMeldung,
            long pBestandAlt, long pBestandNeu) {
        int i, rc;

        /*****Bei Aktionär: Zuordnung zu Sammelkarte auf "inaktiv" seten - in EclMeldungZuSammelkarte (Aktionär)****
         * Dabei auch die bisher gültige Weisung des Aktionärs suchen, um diese dann in der Sammelkarte "abzuziehen"*/

        if (pMeldung.willenserklaerung == 0) {/*Aktionär ist aktuell keiner Sammelkarte zugeordnet - nichts tun*/
            return true;
        }

        //		System.out.println("Meldung verändern in Sammelkarte: "+pMeldung.aktionaersnummer);

        pDbBundle.dbMeldungZuSammelkarte.leseZuWillenserklaerung(pMeldung.willenserklaerungIdent);
        if (pDbBundle.dbMeldungZuSammelkarte.anzMeldungZuSammelkarteGefunden() != 1) {
            CaBug.drucke("BlWillenserklaerung.veraendernAktienbestandAktuelleWillenserklaerung 001");
        }
        /*Hier: im 0-ten-Satz in dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden steht die bisher aktive Zuordnung*/
        int alterWeisungssatz = pDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(0).weisungIdent;

        int alteSammelkarteIdent = pMeldung.meldungEnthaltenInSammelkarte;
        EclMeldung alteSammelkarteEcl = new EclMeldung();

        /*Sammelkarte Summen bereinigen - in EclMeldung (Sammelkarte), zu korrigieren sind pBestandAlt, pBestandNeu*/
        alteSammelkarteEcl.meldungsIdent = alteSammelkarteIdent;
        pDbBundle.dbMeldungen.leseZuMeldungsIdent(alteSammelkarteEcl);
        if (pDbBundle.dbMeldungen.meldungenArray.length != 1) {
            CaBug.drucke("BlWillenserklaerung.veraendernAktienbestandAktuelleWillenserklaerung 002");
        }

        alteSammelkarteEcl = pDbBundle.dbMeldungen.meldungenArray[0];
        alteSammelkarteEcl.stueckAktien = alteSammelkarteEcl.stueckAktien - pBestandAlt + pBestandNeu;
        alteSammelkarteEcl.stimmen = alteSammelkarteEcl.stimmen - pBestandAlt + pBestandNeu;

        rc = pDbBundle.dbMeldungen.update(alteSammelkarteEcl);
        if (rc != 1) {
            CaBug.drucke("BlWillenserklaerung.veraendernAktienbestandAktuelleWillenserklaerung 005");
        }

        /*Sammelkarte Summen bereinigen - in EclWeisungMeldung (Split) (Sammelkarte)*/
        pDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(alteSammelkarteIdent, false);
        if (pDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() != 1) {
            CaBug.drucke("BlWillenserklaerung.veraendernAktienbestandAktuelleWillenserklaerung 006");
        }
        EclWeisungMeldung sammelWeisungMeldung = pDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);

        pDbBundle.dbWeisungMeldung.leseZuWeisungIdent(alterWeisungssatz,
                false); /*"alten" Weisungssatz des Aktionärs einlesen*/
        if (pDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() != 1) {
            CaBug.drucke("BlWillenserklaerung.veraendernAktienbestandAktuelleWillenserklaerung 003");
        }
        EclWeisungMeldung aktionaerWeisungMeldung = pDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);

        if (aktionaerWeisungMeldung.weisungSplit == 0) { /*Kein Weisungssplit bei Aktionär*/
            for (i = 0; i < 200; i++) {
                int stimmart = aktionaerWeisungMeldung.abgabe[i];
                if (stimmart == KonstStimmart.gegenantragWirdUnterstuetzt) {
                    stimmart = KonstStimmart.ja;
                }
                if (sammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[i]==0) {
                    sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][stimmart] = sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][stimmart]
                            - pBestandAlt + pBestandNeu;
                }
            }
        } else {/*Weisungssplit bei Aktionär vorhanden*/
            /*TODO #9 Weisungssplit bei Aktionär funktioniert nicht - ist auch aktuell an anderer Stelle nicht immer implementiert!*/
            //			for (i=0;i<200;i++){
            //				for (i1=0;i1<10;i1++){
            //					sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][i1]-=aktionaerWeisungMeldung.weisungMeldungSplit.abgabe[i][i1];
            //				}
            //			}
        }
        rc = pDbBundle.dbWeisungMeldung.update(sammelWeisungMeldung, null, false);
        if (rc != 1) {
            CaBug.drucke("BlWillenserklaerung.veraendernAktienbestandAktuelleWillenserklaerung 004");
        }

        return true;
    }

    private boolean deaktivierenAktuelleWillenserklaerung(boolean pPraesenzKorrigieren) {
        int i, i1, rc;

        /*****Bei Aktionär: Zuordnung zu Sammelkarte auf "inaktiv" seten - in EclMeldungZuSammelkarte (Aktionär)****
         * Dabei auch die bisher gültige Weisung des Aktionärs suchen, um diese dann in der Sammelkarte "abzuziehen"*/

        lDbBundle.dbMeldungZuSammelkarte.leseZuWillenserklaerung(piEclMeldungAktionaer.willenserklaerungIdent);
        if (lDbBundle.dbMeldungZuSammelkarte.anzMeldungZuSammelkarteGefunden() != 1) {
            CaBug.drucke("BlWillenserklaerung.deaktivierenAktuelleWillenserklaerung 001");
        }
        if (lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(0).aktiv == 1) {
            /*Nur aktive "inaktiv" setzen. Es kann nämlich sein, dass aktiv (z.B. wg. Storno oder Änderung)
             * bereits auf -1 oder -2 steht!
             */
            lDbBundle.dbMeldungZuSammelkarte.updateAktivKZ(
                    lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(0).willenserklaerungIdent, 0);
        }
        /*Hier: im 0-ten-Satz in dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden steht die bisher aktive Zuordnung*/
        int alterWeisungssatz = lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(0).weisungIdent;

        int alteSammelkarteIdent = piEclMeldungAktionaer.meldungEnthaltenInSammelkarte;
        EclMeldung alteSammelkarteEcl = new EclMeldung();

        /*Sammelkarte Summen bereinigen - in EclMeldung (Sammelkarte), abzuziehen sind die Weisungen aus aktionaerAlteWeisungMeldung*/
        alteSammelkarteEcl.meldungsIdent = alteSammelkarteIdent;
        lDbBundle.dbMeldungen.leseZuMeldungsIdent(alteSammelkarteEcl);
        if (lDbBundle.dbMeldungen.meldungenArray.length != 1) {
            CaBug.drucke("BlWillenserklaerung.deaktivierenAktuelleWillenserklaerung 002");
        }

        alteSammelkarteEcl = lDbBundle.dbMeldungen.meldungenArray[0];
        alteSammelkarteEcl.stueckAktien -= piEclMeldungAktionaer.stueckAktien;
        alteSammelkarteEcl.stimmen -= piEclMeldungAktionaer.stimmen;

        lDbBundle.dbMeldungen.update(alteSammelkarteEcl);

        /*Sammelkarte Summen bereinigen - in EclWeisungMeldung (Split) (Sammelkarte)*/
        lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(alteSammelkarteIdent, false);
        if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() != 1) {
            CaBug.drucke("BlWillenserklaerung.deaktivierenAktuelleWillenserklaerung 002");
        }
        EclWeisungMeldung sammelWeisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);

        lDbBundle.dbWeisungMeldung.leseZuWeisungIdent(alterWeisungssatz,
                false); /*"alten" Weisungssatz des Aktionärs einlesen*/
        if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() != 1) {
            CaBug.drucke("BlWillenserklaerung.deaktivierenAktuelleWillenserklaerung 003");
        }
        EclWeisungMeldung aktionaerWeisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
        if (aktionaerWeisungMeldung.aktiv == 1) { /*Kann mittlerweile auch storniert oder geändert sein*/
            aktionaerWeisungMeldung.aktiv = 0;
            lDbBundle.dbWeisungMeldung.update(aktionaerWeisungMeldung, null, false);
        }

        if (aktionaerWeisungMeldung.weisungSplit == 0) { /*Kein Weisungssplit bei Aktionär*/
            for (i = 0; i < 200; i++) {
                int stimmart = aktionaerWeisungMeldung.abgabe[i];
                if (stimmart == KonstStimmart.gegenantragWirdUnterstuetzt) {
                    stimmart = KonstStimmart.ja;
                }
                if (sammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[i]==0) {
                    sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][stimmart] -= piEclMeldungAktionaer.stimmen;
                }
            }
        } else {/*Weisungssplit bei Aktionär vorhanden*/
            for (i = 0; i < 200; i++) {
                for (i1 = 0; i1 < 10; i1++) {
                    if (sammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[i]==0) {
                        sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][i1] -= aktionaerWeisungMeldung.weisungMeldungSplit.abgabe[i][i1];
                    }
                }
            }
        }
        rc = lDbBundle.dbWeisungMeldung.update(sammelWeisungMeldung, null, false);
        if (rc != 1) {
            CaBug.drucke("BlWillenserklaerung.deaktivierenAktuelleWillenserklaerung 004");
        }

        /*Bei Aktionär "in Sammelkarte" setzen - in EclMeldung (Aktionär)*/
        piEclMeldungAktionaer.willenserklaerung = 0;
        piEclMeldungAktionaer.willenserklaerungIdent = 0;
        piEclMeldungAktionaer.meldungEnthaltenInSammelkarte = 0;
        piEclMeldungAktionaer.meldungEnthaltenInSammelkarteArt = 0;
        piEclMeldungAktionaer.veraenderungszeit = "";
        piEclMeldungAktionaer.erteiltAufWeg = 0;
        piEclMeldungAktionaer.weisungVorhanden = 0;
        piEclMeldungAktionaer.meldungstyp = 1;
        if (piEclMeldungAktionaer.vertreterIdent == alteSammelkarteEcl.vertreterIdent) {
            /*In Aktionär war bereits Sammelkartenvertreter eingetragen. Diesen jetzt aus Meldesatz wieder austragen*/
            piEclMeldungAktionaer.vertreterIdent = 0;
            piEclMeldungAktionaer.vertreterIdent_Delayed = 0;
            piEclMeldungAktionaer.vertreterName = "";
            piEclMeldungAktionaer.vertreterName_Delayed = "";
            piEclMeldungAktionaer.vertreterVorname = "";
            piEclMeldungAktionaer.vertreterVorname_Delayed = "";
            piEclMeldungAktionaer.vertreterOrt = "";
            piEclMeldungAktionaer.vertreterOrt_Delayed = "";
        }

        /*****************Nun ggf. Präsenzkennzeichen sowie Präsenzsummen korrigieren ("Abgang")************************/

        if (alteSammelkarteEcl.statusPraesenz == 1 && pPraesenzKorrigieren) {
            /*Verfahrensbeschreibung:
             * Nur, wenn Sammelkarte präsent ist (also nie bei Briefwahl!).
             *
             * > Präsenzkennzeichen bei Einzelkarte verändern (nur, wenn dies gesetzt ist - sprich nicht bei "ohne Offenlegung"
             * > Präsenzsumme korrigieren
             * > Für Teilnehmerverzeichnis / Protokollierung:
             * 		> Willenserklärung AbgangAusSRV, AbgangAusOrga, AbgangAusDauervollmacht, AbgangAusKIAV
             * 			für den Einzelaktionär erzeugen
             *
             */
            /*Daten für Präsenz sicherheitshalber einlesen (wird für den "stand-Alone-Aufruf" benötigt*/
            BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
            lBlPraesenzlistenNummer.leseAktuelleNummernOhneUpdate();/*TODO Verändert auf ohne Update*/

            BlPraesenzProtokoll lBlPraesenzProtokoll = new BlPraesenzProtokoll(lDbBundle);
            lBlPraesenzProtokoll.leseProtokollNr();

            /*Präsenzkennzeichen in Einzelkarte korrigieren*/
            if (piEclMeldungAktionaer.statusPraesenz == 4) {
                piEclMeldungAktionaer.statusPraesenz = 0;
                piEclMeldungAktionaer.statusPraesenz_Delayed = 0;
                /*TODO #9 nochmal genauer untersuchen. Aktuell Überlegung: wenn jemand in einer Sammelkarte war, und herausgelöscht wurde,
                 * dann braucht er hinterher auf jeden Falle einen neuen Stimmblock, neuen Vertreter etc. - d.h. Behandeln wie Erstzugang
                 */
            }

            /*Präsenzsummen*/
            BlPraesenzSummen lBlPraesenzSummen = new BlPraesenzSummen(lDbBundle);
            lBlPraesenzSummen.abgang(piEclMeldungAktionaer.stueckAktien, piEclMeldungAktionaer.gattung, 0);

            /*Willenserklärung für Teilnehmerverzeichnis erzeugen*/
            switch (alteSammelkarteEcl.skIst) {
            case KonstSkIst.kiav:
                prepareWillenserklaerung2(KonstWillenserklaerung.abgangAusKIAV);
                break;
            case KonstSkIst.srv:
                prepareWillenserklaerung2(KonstWillenserklaerung.abgangAusSRV);
                break;
            case KonstSkIst.organisatorisch:
                prepareWillenserklaerung2(KonstWillenserklaerung.abgangAusOrga);
                break;
            case KonstSkIst.dauervollmacht:
                prepareWillenserklaerung2(KonstWillenserklaerung.abgangAusDauervollmacht);
                break;
            }
            if (!piEclMeldungAktionaer.zutrittsIdent.isEmpty()) {
                preparedWillenserklaerung2.zutrittsIdent = piEclMeldungAktionaer.zutrittsIdent;
                preparedWillenserklaerung2.identifikationKlasse = 1;
            } else {
                preparedWillenserklaerung2.zutrittsIdent = alteSammelkarteEcl.zutrittsIdent;
                preparedWillenserklaerung2.identifikationKlasse = 1;
            }
            preparedWillenserklaerung2.stimmkarte1 = alteSammelkarteEcl.stimmkarte;

            preparedWillenserklaerung2.stimmkarteSecond = alteSammelkarteEcl.stimmkarteSecond;

            preparedWillenserklaerung2.willenserklaerungGeberIdent = alteSammelkarteEcl.vertreterIdent;
            preparedWillenserklaerung2.bevollmaechtigterDritterIdent = alteSammelkarteEcl.vertreterIdent;
            preparedWillenserklaerung2.identMeldungZuSammelkarte = alteSammelkarteEcl.meldungsIdent;

            rc = lDbBundle.dbWillenserklaerung.insert(preparedWillenserklaerung2, preparedWillenserklaerungZusatz2);
        }

        lDbBundle.dbMeldungen.update(piEclMeldungAktionaer);

        return true;
    }

    /** Für alle Einträge in Sammelkarten: stellt fest, inwieweit die aktuell abgegebene Willenserklärung/Sammelkarte
     * gemäß Vorrangigkeitsregeln als "aktiv" beim Aktionär eingetragen werden soll, und führt dies dann ggf.
     * auch durch. Bei Durchführung wird eine ggf. bereits eingetragene Willenserklärung/Sammelkarte auf
     * "inaktiv" gesetzt.
     *
     * @return
     * false => die Willenserklärung wurde nicht aktiv, beim Aktionär bleibt alles beim alten
     * true => die Willenserklärung wurde aktiv gesetzt
     */
    private boolean pruefenUndSetzenAktivAktuelleWillenserklaerung(EclWillenserklaerung pNeueWillenserklaerung,
            EclMeldungZuSammelkarte pMeldungZuSammelkarte, boolean pPraesenzKorrigieren) {

        int i, i1, rc;

//        System.out.println("Vor Sleep");
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("Nach Sleep");

        
        boolean neueWillenserklaerungEintragen = false;
        boolean alteWillenserklaerungAustragen = false;
        //		System.out.println("BlWillenserklaerung pruefenUndSetzenAktivAktuelleWillenserklaerung A");
        if (piEclMeldungAktionaer.willenserklaerung != 0) {
            CaBug.druckeLog("piEclMeldungAktionaer.willenserklaerung!=0", logDrucken, 10);
            /******Bei Aktionär ist bereits eine Sammel-Willenserklärung eingetragen***/

            /************Nun prüfen, ob neue Willenserklärung "vorrangiger" ist**************/
            switch (lDbBundle.clGlobalVar.sammelkartenVorrangVerfahren) {
            case 1: {/*immer zuletzt eingegangene Willenserklärung zählt*/

                if (1 == 1 || piEclMeldungAktionaer.veraenderungszeit
                        .compareTo(pNeueWillenserklaerung.veraenderungszeit) <= 0) {
                    /*TODO #9 Hier muß das 1==1 wieder raus. Aktuel rein, weil Uhrzeitvergleich nicht konsistent durchdacht. Vom
                     * Anmeldetool kommt ja z.B. 9.00 Uhr , wird auf HV verwendet, und das ist dann älter als das was bisher schon
                     * über andere Tools gelaufen ist!
                     */
                    neueWillenserklaerungEintragen = true;
                    alteWillenserklaerungAustragen = true;
                }
                break;
            }
            case 2: {/*Internet-Willenserklärungen schlagen alles andere, ansonsten zählt zuletzt eingegangene*/
                if ((piEclMeldungAktionaer.erteiltAufWeg == 21
                        /*Portal*/ || piEclMeldungAktionaer.erteiltAufWeg == 22/*App*/)
                        && (pNeueWillenserklaerung.erteiltAufWeg != 21
                                && pNeueWillenserklaerung.erteiltAufWeg != 22)) {/*Aktive Willenserklärung ist über Portal oder App erteilt worden => bleibt aktiv*/
                    neueWillenserklaerungEintragen = false;
                    alteWillenserklaerungAustragen = false;
                } else {/*Gleicher Weg => letzt eingegangene zählt*/
                    if (piEclMeldungAktionaer.veraenderungszeit
                            .compareTo(pNeueWillenserklaerung.veraenderungszeit) <= 0) {
                        neueWillenserklaerungEintragen = true;
                        alteWillenserklaerungAustragen = true;
                    } else {
                        neueWillenserklaerungEintragen = false;
                        alteWillenserklaerungAustragen = false;
                    }

                }
                break;
            }
            }
        } else {/*****Noch keine "Sammel"-Willenserklärung beim Aktionär eintragen - aktuelle Willenserklärung eintragen***/
            CaBug.druckeLog("piEclMeldungAktionaer.willenserklaerung==0", logDrucken, 10);
            neueWillenserklaerungEintragen = true;
            alteWillenserklaerungAustragen = false;
        }

        CaBug.druckeLog("neueWillenserklaerungEintragen="+neueWillenserklaerungEintragen+" alteWillenserklaerungAustragen="+alteWillenserklaerungAustragen, logDrucken, 10);
        
        if (alteWillenserklaerungAustragen == true) {/*********Alte Willenserklärung deaktivieren************/

            deaktivierenAktuelleWillenserklaerung(pPraesenzKorrigieren);

            //			/*****Bei Aktionär: Zuordnung zu Sammelkarte auf "inaktiv" seten - in EclMeldungZuSammelkarte (Aktionär)****
            //			 * Dabei auch die bisher gültige Weisung des Aktionärs suchen, um diese dann in der Sammelkarte "abzuziehen"*/
            //
            //			lDbBundle.dbMeldungZuSammelkarte.leseZuMeldung(piEclMeldungAktionaer);
            //			if (lDbBundle.dbMeldungZuSammelkarte.anzMeldungZuSammelkarteGefunden()<1){
            //				CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 005");
            //			}
            //			gef=0; /*Hier wird der "alte" Satz gespeichert, um die alte Weisung dann weiterverarbeiten (=austragen) zu können*/
            //			anzgef=0; /*Hier wird gespeichert, wieviele "alte zugeordnete aktive" Sätze gefunden wurden. Wenn !=1, dann ist in der Datenbank
            //						bereits ein Zuordenfehler!*/
            //			for (i=0;i<lDbBundle.dbMeldungZuSammelkarte.anzMeldungZuSammelkarteGefunden();i++){
            //				/*ALLE aktiven Verbinduneng zu Sammelkarten können auf deaktiv gesetzt werden, da maximal immer nur eine aktiv ist.*/
            //				if (lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(i).aktiv==1){
            //					gef=i;anzgef++;
            //					lDbBundle.dbMeldungZuSammelkarte.updateAktivKZ(lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(i).willenserklaerungIdent, 0);
            //				}
            //				if (anzgef!=1){
            //					CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 005");
            //				}
            //			}
            //			/*Hier: im gef-ten-Satz in dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden steht die bisher aktive Zuordnung*/
            //			int alterWeisungssatz=lDbBundle.dbMeldungZuSammelkarte.meldungZuSammelkarteGefunden(gef).weisungIdent;
            //
            //			int alteSammelkarteIdent=piEclMeldungAktionaer.meldungEnthaltenInSammelkarte;
            //			EclMeldung alteSammelkarteEcl=new EclMeldung();
            //
            //			/*Sammelkarte Summen bereinigen - in EclMeldung (Sammelkarte), abzuziehen sind die Weisungen aus aktionaerAlteWeisungMeldung*/
            //			alteSammelkarteEcl.meldungsIdent=alteSammelkarteIdent;
            //			lDbBundle.dbMeldungen.leseZuMeldungsIdent(alteSammelkarteEcl);
            //			if (lDbBundle.dbMeldungen.meldungenArray.length!=1){
            //				CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 001");
            //			}
            //
            //			alteSammelkarteEcl=lDbBundle.dbMeldungen.meldungenArray[0];
            //			alteSammelkarteEcl.stueckAktien-=piEclMeldungAktionaer.stueckAktien;
            //			alteSammelkarteEcl.stimmen-=piEclMeldungAktionaer.stimmen;
            //
            //			lDbBundle.dbMeldungen.update(alteSammelkarteEcl);
            //
            //			/*Sammelkarte Summen bereinigen - in EclWeisungMeldung (Split) (Sammelkarte)*/
            //			lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(alteSammelkarteIdent, false);
            //			if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden()!=1){
            //				CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 002");}
            //			EclWeisungMeldung sammelWeisungMeldung=lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
            //
            //			lDbBundle.dbWeisungMeldung.leseZuWeisungIdent(alterWeisungssatz, false); /*"alten" Weisungssatz des Aktionärs einlesen*/
            //			if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden()!=1){
            //				CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 003");}
            //			EclWeisungMeldung aktionaerWeisungMeldung=lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
            //			aktionaerWeisungMeldung.aktiv=0;
            //			lDbBundle.dbWeisungMeldung.update(aktionaerWeisungMeldung, null, false);
            //
            //			if (aktionaerWeisungMeldung.weisungSplit==0){ /*Kein Weisungssplit bei Aktionär*/
            //				for (i=0;i<200;i++){
            //					sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][aktionaerWeisungMeldung.abgabe[i]]-=piEclMeldungAktionaer.stimmen;
            //				}
            //			}
            //			else{/*Weisungssplit bei Aktionär vorhanden*/
            //				for (i=0;i<200;i++){
            //					for (i1=0;i1<10;i1++){
            //						sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][i1]-=aktionaerWeisungMeldung.weisungMeldungSplit.abgabe[i][i1];
            //					}
            //				}
            //			}
            //			rc=lDbBundle.dbWeisungMeldung.update(sammelWeisungMeldung, null, false);
            //			if (rc!=1){
            //				CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 004");
            //			}
            //
            //			/*Bei Aktionär "in Sammelkarte" setzen - in EclMeldung (Aktionär)*/
            //			piEclMeldungAktionaer.willenserklaerung=0;
            //			piEclMeldungAktionaer.willenserklaerungIdent=0;
            //			piEclMeldungAktionaer.meldungEnthaltenInSammelkarte=0;
            //			piEclMeldungAktionaer.meldungEnthaltenInSammelkarteArt=0;
            //			piEclMeldungAktionaer.veraenderungszeit="";
            //			piEclMeldungAktionaer.erteiltAufWeg=0;
            //			piEclMeldungAktionaer.weisungVorhanden=0;
            //			piEclMeldungAktionaer.meldungstyp=0;

        }

        if (neueWillenserklaerungEintragen == true) {/**********Neue Willenserklärung als aktiv eintragen***********/
            //			System.out.println("BlWillenserklaerung pruefenUndSetzenAktivAktuelleWillenserklaerung B");

            int neueSammelkarteIdent = pMeldungZuSammelkarte.sammelIdent;
            EclMeldung neueSammelkarteEcl = new EclMeldung();

            /*Sammelkarte Summen bereinigen - in EclMeldung (Sammelkarte)*/
            neueSammelkarteEcl.meldungsIdent = neueSammelkarteIdent;
            lDbBundle.dbMeldungen.leseZuMeldungsIdent(neueSammelkarteEcl);
            if (lDbBundle.dbMeldungen.meldungenArray.length != 1) {
                CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 006");
            }

            neueSammelkarteEcl = lDbBundle.dbMeldungen.meldungenArray[0];
            neueSammelkarteEcl.stueckAktien += piEclMeldungAktionaer.stueckAktien;
            neueSammelkarteEcl.stimmen += piEclMeldungAktionaer.stimmen;

            //			System.out.println("BlWillenserklaerung pruefenUndSetzenAktivAktuelleWillenserklaerung B1");
            lDbBundle.dbMeldungen.update(neueSammelkarteEcl);
            //			System.out.println("BlWillenserklaerung pruefenUndSetzenAktivAktuelleWillenserklaerung B2");

            /*Sammelkarte Summen bereinigen - in EclWeisungMeldung (Split) (Sammelkarte)*/
            lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(neueSammelkarteIdent, false);
            if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() != 1) {
                CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 007");
            }
            EclWeisungMeldung sammelWeisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);

            /*Weisung zu aktueller Willenserklärung einlesen*/
            lDbBundle.dbWeisungMeldung.leseZuWillenserklaerungIdent(pNeueWillenserklaerung.willenserklaerungIdent,
                    false);
            if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden() != 1) {
                CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 008");
            }
            EclWeisungMeldung aktionaerWeisungMeldung = lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
            aktionaerWeisungMeldung.aktiv = 1;
            lDbBundle.dbWeisungMeldung.update(aktionaerWeisungMeldung, null, false);

            if (aktionaerWeisungMeldung.weisungSplit == 0) { /*Kein Weisungssplit bei Aktionär*/
                for (i = 0; i < 200; i++) {
                    int stimmart = aktionaerWeisungMeldung.abgabe[i];
                    if (stimmart == KonstStimmart.gegenantragWirdUnterstuetzt) {
                        stimmart = KonstStimmart.ja;
                    }
                    if (sammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[i]==0) {
                        sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][stimmart] += piEclMeldungAktionaer.stimmen;
                    }
                }
            } else {/*Weisungssplit bei Aktionär vorhanden*/
                for (i = 0; i < 200; i++) {
                    for (i1 = 0; i1 < 10; i1++) {
                        if (sammelWeisungMeldung.weisungMeldungSplit.nichtBerechnen[i]==0) {
                            sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][i1] += aktionaerWeisungMeldung.weisungMeldungSplit.abgabe[i][i1];
                        }
                    }
                }
            }
            rc = lDbBundle.dbWeisungMeldung.update(sammelWeisungMeldung, null, false);
            if (rc != 1) {
                CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 009");
            }

            /*Bei Aktionär "in Sammelkarte" setzen - in EclMeldung (Aktionär)*/
            piEclMeldungAktionaer.willenserklaerung = pNeueWillenserklaerung.willenserklaerung;
            piEclMeldungAktionaer.willenserklaerungIdent = pNeueWillenserklaerung.willenserklaerungIdent;
            piEclMeldungAktionaer.meldungEnthaltenInSammelkarte = neueSammelkarteIdent;
            switch (pNeueWillenserklaerung.willenserklaerung) {
            case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
            case KonstWillenserklaerung.aendernWeisungAnSRV: {
                piEclMeldungAktionaer.meldungEnthaltenInSammelkarteArt = 2;
                break;
            }
            case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
            case KonstWillenserklaerung.aendernWeisungAnKIAV: {
                piEclMeldungAktionaer.meldungEnthaltenInSammelkarteArt = 1;
                break;
            }
            case KonstWillenserklaerung.briefwahl:
            case KonstWillenserklaerung.aendernBriefwahl: {
                piEclMeldungAktionaer.meldungEnthaltenInSammelkarteArt = 4;
                break;
            }
            case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
            case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte: {
                piEclMeldungAktionaer.meldungEnthaltenInSammelkarteArt = 3;
                break;
            }
            case KonstWillenserklaerung.dauervollmachtAnKIAV: {
                piEclMeldungAktionaer.meldungEnthaltenInSammelkarteArt = 5;
                break;
            }
            default:
                break;
            }
            piEclMeldungAktionaer.veraenderungszeit = pNeueWillenserklaerung.veraenderungszeit;
            piEclMeldungAktionaer.erteiltAufWeg = pNeueWillenserklaerung.erteiltAufWeg;
            piEclMeldungAktionaer.weisungVorhanden = 1;
            piEclMeldungAktionaer.meldungstyp = 3;

            /*****************Nun ggf. Präsenzkennzeichen sowie Präsenzsummen korrigieren ("Zugang")************************/
            //			System.out.println("BlWillenserklaerung statusPraesenz="+neueSammelkarteEcl.statusPraesenz);
            if (neueSammelkarteEcl.statusPraesenz == 1 && pPraesenzKorrigieren) {
                /*Verfahrensbeschreibung:
                 * Nur, wenn Sammelkarte präsent ist (also nie bei Briefwahl!).
                 *
                 * > Präsenzkennzeichen bei Einzelkarte verändern (nur, wenn dies gesetzt ist - sprich nicht bei "ohne Offenlegung"
                 * > Präsenzsumme korrigieren
                 * > Für Teilnehmerverzeichnis / Protokollierung:
                 * 		> Willenserklärung ZugangInSRV, ZugangInOrga, ZugangInDauervollmacht, ZugangInKIAV
                 * 			für den Einzelaktionär erzeugen
                 * 		oder analog WechselIn .... (falls Aktionär bereits präsent ist)
                 *
                 */
                /*Daten für Präsenz sicherheitshalber einlesen (wird für den "stand-Alone-Aufruf" benötigt*/
                /*TODO #9 Achtung: Wechsel einer präsenten Meldung in Briefwahl ist derzeit nich möglich,
                 * wird aber auch nicht abgefangen! Vorsicht! Vorsicht! Vorsicht!
                 */
                BlPraesenzlistenNummer lBlPraesenzlistenNummer = new BlPraesenzlistenNummer(lDbBundle);
                lBlPraesenzlistenNummer.leseAktuelleNummernOhneUpdate();/*TODO Verändert auf ohne Update*/

                BlPraesenzProtokoll lBlPraesenzProtokoll = new BlPraesenzProtokoll(lDbBundle);
                lBlPraesenzProtokoll.leseProtokollNr();

                /*Präsenzkennzeichen in Einzelkarte korrigieren - aber nur bei Offenlegung!*/
                int alterPraesenzstatus = piEclMeldungAktionaer.statusPraesenz;
                int vorherAnwesend = 0;
                if (alterPraesenzstatus == 1 || alterPraesenzstatus == 4) {
                    vorherAnwesend = 1;
                }

                if (neueSammelkarteEcl.liefereOffenlegungTatsaechlich(lDbBundle) == 1) {
                    piEclMeldungAktionaer.statusPraesenz = 4;
                    piEclMeldungAktionaer.statusPraesenz_Delayed = 4;

                }

                /*Präsenzsummen*/

                if (vorherAnwesend == 0) {
                    BlPraesenzSummen lBlPraesenzSummen = new BlPraesenzSummen(lDbBundle);
                    lBlPraesenzSummen.zugang(piEclMeldungAktionaer.stueckAktien, piEclMeldungAktionaer.gattung, 0);
                }

                /*Willenserklärung für Teilnehmerverzeichnis erzeugen*/
                //				System.out.println("BlWillenserklaerung skIst="+neueSammelkarteEcl.skIst);
                switch (neueSammelkarteEcl.skIst) {
                case KonstSkIst.kiav:
                    if (vorherAnwesend == 0) {
                        prepareWillenserklaerung2(KonstWillenserklaerung.zugangInKIAV);
                    } else {
                        prepareWillenserklaerung2(KonstWillenserklaerung.wechselInKIAV);
                    }
                    break;
                case KonstSkIst.srv:
                    if (vorherAnwesend == 0) {
                        prepareWillenserklaerung2(KonstWillenserklaerung.zugangInSRV);
                    } else {
                        prepareWillenserklaerung2(KonstWillenserklaerung.wechselInSRV);
                    }
                    break;
                case KonstSkIst.organisatorisch:
                    if (vorherAnwesend == 0) {
                        prepareWillenserklaerung2(KonstWillenserklaerung.zugangInOrga);
                    } else {
                        prepareWillenserklaerung2(KonstWillenserklaerung.wechselInOrga);
                    }
                    break;
                case KonstSkIst.dauervollmacht:
                    if (vorherAnwesend == 0) {
                        prepareWillenserklaerung2(KonstWillenserklaerung.zugangInDauervollmacht);
                    } else {
                        prepareWillenserklaerung2(KonstWillenserklaerung.wechselInDauervollmacht);
                    }
                    break;
                }
                if (!piEclMeldungAktionaer.zutrittsIdent.isEmpty()) {
                    preparedWillenserklaerung2.zutrittsIdent = piEclMeldungAktionaer.zutrittsIdent;
                    preparedWillenserklaerung2.identifikationKlasse = 1;
                } else {
                    preparedWillenserklaerung2.zutrittsIdent = neueSammelkarteEcl.zutrittsIdent;
                    preparedWillenserklaerung2.identifikationKlasse = 1;
                }
                preparedWillenserklaerung2.stimmkarte1 = neueSammelkarteEcl.stimmkarte;

                preparedWillenserklaerung2.stimmkarteSecond = neueSammelkarteEcl.stimmkarteSecond;

                preparedWillenserklaerung2.willenserklaerungGeberIdent = neueSammelkarteEcl.vertreterIdent;
                preparedWillenserklaerung2.bevollmaechtigterDritterIdent = neueSammelkarteEcl.vertreterIdent;
                preparedWillenserklaerung2.identMeldungZuSammelkarte = neueSammelkarteEcl.meldungsIdent;

                rc = lDbBundle.dbWillenserklaerung.insert(preparedWillenserklaerung2, preparedWillenserklaerungZusatz2);
            }

            //			System.out.println("BlWillenserklaerung pruefenUndSetzenAktivAktuelleWillenserklaerung B4");
            //			System.out.println("Aktionär="+piEclMeldungAktionaer.meldungsIdent);
            //			System.out.println("Aktionär="+piEclMeldungAktionaer.db_version);
            CaBug.druckeLog("updateDbMeldungen piEclMeldungAktionaer.willenserklaerung="+piEclMeldungAktionaer.willenserklaerung+" piEclMeldungAktionaer.meldungsIdent="+piEclMeldungAktionaer.meldungsIdent, logDrucken, 10);
            int rc1=lDbBundle.dbMeldungen.update(piEclMeldungAktionaer);
            CaBug.druckeLog("rc1="+rc1, logDrucken, 10);
            //			System.out.println("BlWillenserklaerung pruefenUndSetzenAktivAktuelleWillenserklaerung B5");

            /*Bei Aktionär: Zuordnung zu Sammelkarte auf "inaktiv" seten - in EclMeldungZuSammelkarte (Aktionär)*/
            lDbBundle.dbMeldungZuSammelkarte.updateAktivKZ(pNeueWillenserklaerung.willenserklaerungIdent, 1);
            //			System.out.println("BlWillenserklaerung pruefenUndSetzenAktivAktuelleWillenserklaerung C");

        }

        return false;
    }

    /**Deaktiviert die übergebene Willenserklärung aus der Sammelkarte
     *
     * @return
     * false => die Willenserklärung wurde nicht aktiv, beim Aktionär bleibt alles beim alten
     * true => die Willenserklärung wurde aktiv gesetzt
     */
    private boolean pruefenUndSetzenDeaktivWillenserklaerung(EclMeldungZuSammelkarte pMeldungZuSammelkarte, boolean pPraesenzKorrigieren) {

        //		System.out.println("BlWillenserklaerung pruefenUndSetzenDeaktivWillenserklaerung A");
        boolean alteWillenserklaerungAustragen = false;

        if (piEclMeldungAktionaer.willenserklaerung != 0) {/******Bei Aktionär ist bereits eine Sammel-Willenserklärung eingetragen***/
            //			System.out.println("BlWillenserklaerung pruefenUndSetzenDeaktivWillenserklaerung B");

            /************Nun prüfen, ob alte Willenserklärung "aktiv" ist**************/
            if (piEclMeldungAktionaer.willenserklaerungIdent == pMeldungZuSammelkarte.willenserklaerungIdent) {
                alteWillenserklaerungAustragen = true;
                //				System.out.println("BlWillenserklaerung pruefenUndSetzenDeaktivWillenserklaerung C");

            }
        }

        if (alteWillenserklaerungAustragen == true) {/*********Alte Willenserklärung deaktivieren************/
            deaktivierenAktuelleWillenserklaerung(pPraesenzKorrigieren);
        }

        /*TODO nun checken, ob alte Willenserklärung wieder auflebt!*/
        //		if (neueWillenserklaerungEintragen==true){/**********Neue Willenserklärung als aktiv eintragen***********/
        //
        //			int neueSammelkarteIdent=pMeldungZuSammelkarte.sammelIdent;
        //			EclMeldung neueSammelkarteEcl=new EclMeldung();
        //
        //			/*Sammelkarte Summen bereinigen - in EclMeldung (Sammelkarte)*/
        //			neueSammelkarteEcl.meldungsIdent=neueSammelkarteIdent;
        //			lDbBundle.dbMeldungen.leseZuMeldungsIdent(neueSammelkarteEcl);
        //			if (lDbBundle.dbMeldungen.meldungenArray.length!=1){
        //				CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 006");
        //			}
        //
        //			neueSammelkarteEcl=lDbBundle.dbMeldungen.meldungenArray[0];
        //			neueSammelkarteEcl.stueckAktien+=piEclMeldungAktionaer.stueckAktien;
        //			neueSammelkarteEcl.stimmen+=piEclMeldungAktionaer.stimmen;
        //
        //			lDbBundle.dbMeldungen.update(neueSammelkarteEcl);
        //
        //
        //			/*Sammelkarte Summen bereinigen - in EclWeisungMeldung (Split) (Sammelkarte)*/
        //			lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(neueSammelkarteIdent, false);
        //			if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden()!=1){
        //				CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 007");}
        //			EclWeisungMeldung sammelWeisungMeldung=lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
        //
        //			lDbBundle.dbWeisungMeldung.leseZuMeldungsIdent(piEclMeldungAktionaer.meldungsIdent, false);
        //			if (lDbBundle.dbWeisungMeldung.anzWeisungMeldungGefunden()!=1){
        //				CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 008");}
        //			EclWeisungMeldung aktionaerWeisungMeldung=lDbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
        //			aktionaerWeisungMeldung.aktiv=1;
        //			lDbBundle.dbWeisungMeldung.update(aktionaerWeisungMeldung, null, false);
        //
        //
        //			if (aktionaerWeisungMeldung.weisungSplit==0){ /*Kein Weisungssplit bei Aktionär*/
        //				for (i=0;i<200;i++){
        //					sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][aktionaerWeisungMeldung.abgabe[i]]+=piEclMeldungAktionaer.stimmen;
        //				}
        //			}
        //			else{/*Weisungssplit bei Aktionär vorhanden*/
        //				for (i=0;i<200;i++){
        //					for (i1=0;i1<10;i1++){
        //						sammelWeisungMeldung.weisungMeldungSplit.abgabe[i][i1]+=aktionaerWeisungMeldung.weisungMeldungSplit.abgabe[i][i1];
        //					}
        //				}
        //			}
        //			rc=lDbBundle.dbWeisungMeldung.update(sammelWeisungMeldung, null, false);
        //			if (rc!=1){
        //				CaBug.drucke("BlWillenserklaerung.pruefenUndSetzenAktivAktuelleWillenserklaerung 009");
        //			}
        //
        //			/*Bei Aktionär "in Sammelkarte" setzen - in EclMeldung (Aktionär)*/
        //			piEclMeldungAktionaer.willenserklaerung=pNeueWillenserklaerung.willenserklaerung;
        //			piEclMeldungAktionaer.willenserklaerungIdent=pNeueWillenserklaerung.willenserklaerungIdent;
        //			piEclMeldungAktionaer.meldungEnthaltenInSammelkarte=neueSammelkarteIdent;
        //			switch (EnWillenserklaerung.fromInt(pNeueWillenserklaerung.willenserklaerung)){
        //			case VollmachtUndWeisungAnSRV:{
        //				piEclMeldungAktionaer.meldungEnthaltenInSammelkarteArt=2;
        //				break;}
        //			case VollmachtUndWeisungAnKIAV:{
        //				piEclMeldungAktionaer.meldungEnthaltenInSammelkarteArt=1;
        //				break;}
        //			case Briefwahl:{
        //				piEclMeldungAktionaer.meldungEnthaltenInSammelkarteArt=4;
        //				break;}
        //			default: break;
        //			}
        //			piEclMeldungAktionaer.veraenderungszeit=pNeueWillenserklaerung.veraenderungszeit;
        //			piEclMeldungAktionaer.erteiltAufWeg=pNeueWillenserklaerung.erteiltAufWeg;
        //			piEclMeldungAktionaer.weisungVorhanden=1;
        //			piEclMeldungAktionaer.meldungstyp=3;
        //
        //			/*Bei Aktionär: Zuordnung zu Sammelkarte auf "inaktiv" seten - in EclMeldungZuSammelkarte (Aktionär)*/
        //			lDbBundle.dbMeldungZuSammelkarte.updateAktivKZ(pNeueWillenserklaerung.willenserklaerungIdent, 1);
        //		}
        //

        return false;
    }

    /*????????????????*/

    /**Allgemeine Funktion**/
    /**Übergabe:
     * > Willenserklärung mit Eingaben gemäß üblicher Eingabeparameter. Funktion sucht dann, ob je nach Willenserklärung die
     * 		richtigen Parameter übergeben wurden.
     *
     */

    private void vollmachtUndWeisungAnXY_pruefe(DbBundle dbBundle, int willenserklaerung) {

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        /*Prüfen, ob ein Aktionär referenziert wurde*/
        if (piMeldungsIdentAktionaer == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmVollmachtNurFuerAktionaerMoeglich;
            return;
        }

        einlesenVollmachtenAnDritte();

        /*Prüfen, ob Vollmachtgeber eine gültige Vollmacht besitzt*/
        pruefenObVollmachtgeberVollmachtHat();
        if (this.rcIstZulaessig == false) {
            return;
        }

        /*Prüfen, ob referenzierte Sammelkarte vorhanden*/
        if (pAufnehmendeSammelkarteIdent == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmSammelkarteUnzulaessig;
            return;
        }
        pEclMeldungSammelkarte = new EclMeldung();
        pEclMeldungSammelkarte.meldungsIdent = pAufnehmendeSammelkarteIdent;
        int erg = lDbBundle.dbMeldungen.leseZuMeldungsIdent(pEclMeldungSammelkarte);
        lDbBundle.dbMeldungen.meldungenArray[0].copyTo(pEclMeldungSammelkarte);
        if (erg < 0) { /*Sammelkarte nicht vorhanden*/
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmSammelkarteUnzulaessig;
            return;
        }
        if (pEclMeldungSammelkarte.meldungAktiv != 1) { /*Sammelkarte nicht aktiv*/
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmSammelkarteUnzulaessig;
            return;
        }
        if (pEclMeldungSammelkarte.klasse != 1) { /*ist keine aktienrechtliche Anmeldung*/
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmSammelkarteUnzulaessig;
            return;
        }
        if (pEclMeldungSammelkarte.meldungstyp != 2) { /*ist keine Sammelkarte*/
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmSammelkarteUnzulaessig;
            return;
        }
        //		System.out.println("BlWillenserklaerung 001: pEclMeldungSammelkarte.skIst"+ pEclMeldungSammelkarte.skIst+" willenserklaerung="+willenserklaerung);

        /*Prüfen, ob Gattung von Aktionär und Gattung von Sammelkarte übereinstimmen*/
        //		System.out.println("pEclMeldungSammelkarte.liefereGattung()="+pEclMeldungSammelkarte.liefereGattung()+" piEclMeldungAktionaer.liefereGattung()="+piEclMeldungAktionaer.liefereGattung());
        if (pEclMeldungSammelkarte.liefereGattung() != piEclMeldungAktionaer.liefereGattung()) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmSammelkarteFalscheGattung;
            return;
        }

        /*Prüfen, ob präsenter Aktionär in nicht-präsente Sammelkarte gebucht werden soll*/
        if (piEclMeldungAktionaer.statusPraesenz == 1 && pEclMeldungSammelkarte.statusPraesenz != 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmSammelkarteNichtPraesentAberAktionaer;
            return;
        }

        /*Prüfen, ob referenzierte Sammelkarte zu Willenserklärung paßt*/
        if ((pEclMeldungSammelkarte.skIst == 1 && willenserklaerung != KonstWillenserklaerung.vollmachtUndWeisungAnKIAV
                && willenserklaerung != KonstWillenserklaerung.aendernWeisungAnKIAV)
                || (pEclMeldungSammelkarte.skIst == 2
                        && willenserklaerung != KonstWillenserklaerung.vollmachtUndWeisungAnSRV
                        && willenserklaerung != KonstWillenserklaerung.aendernWeisungAnSRV)
                || (pEclMeldungSammelkarte.skIst == 3
                        && willenserklaerung != KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte
                        && willenserklaerung != KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte)
                || (pEclMeldungSammelkarte.skIst == 4 && willenserklaerung != KonstWillenserklaerung.briefwahl
                        && willenserklaerung != KonstWillenserklaerung.aendernBriefwahl)
                || (pEclMeldungSammelkarte.skIst == 5
                        && willenserklaerung != KonstWillenserklaerung.dauervollmachtAnKIAV)) {
            CaBug.druckeLog("BlWillenserklaerung 001: pEclMeldungSammelkarte.skIst" + pEclMeldungSammelkarte.skIst
                    + " willenserklaerung=" + willenserklaerung, logDrucken, 10);
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmWillenserklaerungZuDieserSammelkarteUnzulaessig;
            return;
        }

        /* > Art der Weisung*/
        if (pEclWeisungMeldung == null) { /*Keine Weisung vorhanden in der Willenserklärung*/
            if ((pEclMeldungSammelkarte.skWeisungsartZulaessig
                    & 1) == 1) { /*Wenn Weisung möglich, dann muß auch eine gespeichert werden*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmWeisungsartZuDieserSammelkarteUnzulaessig;
                return;
            }
        } else {

            /*Ausführlicher 1. Teil nur für Debuggen!*/
            //						if ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 2) !=2 && pEclWeisungMeldung.hatFreieWeisungen()){System.out.println("blWillenserklaerung - Debug - 2");}
            //						if ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 4) !=4 && pEclWeisungMeldung.hatDedizierteWeisungen()){System.out.println("blWillenserklaerung - Debug - 4");}
            //						if ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 8) !=8 && pEclWeisungMeldung.hatWeisungenGemEigenerEmpfehlungFix()){System.out.println("blWillenserklaerung - Debug - 8");}
            //						if ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 16) !=16 && pEclWeisungMeldung.hatWeisungenGemEigenerEmpfehlungFlexBestehend()){System.out.println("blWillenserklaerung - Debug - 16");}
            //						if ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 32) !=32 && pEclWeisungMeldung.hatWeisungenGemEigenerEmpfehlungFlexNeu()){System.out.println("blWillenserklaerung - Debug - 32");}
            //						if ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 64) !=64 && pEclWeisungMeldung.hatWeisungenGemFremderEmpfehlungFix()){System.out.println("blWillenserklaerung - Debug - 64");}
            //						if ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 128) !=128 && pEclWeisungMeldung.hatWeisungenGemFremderEmpfehlungFlexBestehend()){System.out.println("blWillenserklaerung - Debug - 128");}
            //						if ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 256) !=256 && pEclWeisungMeldung.hatWeisungenGemFremderEmpfehlungFlexNeu()){System.out.println("blWillenserklaerung - Debug - 256");}

            if (((pEclMeldungSammelkarte.skWeisungsartZulaessig & 2) != 2 && pEclWeisungMeldung.hatFreieWeisungen())
                    || ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 4) != 4
                            && pEclWeisungMeldung.hatDedizierteWeisungen())
                    || ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 8) != 8
                            && pEclWeisungMeldung.hatWeisungenGemEigenerEmpfehlungFix())
                    || ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 16) != 16
                            && pEclWeisungMeldung.hatWeisungenGemEigenerEmpfehlungFlexBestehend())
                    || ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 32) != 32
                            && pEclWeisungMeldung.hatWeisungenGemEigenerEmpfehlungFlexNeu())
                    || ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 64) != 64
                            && pEclWeisungMeldung.hatWeisungenGemFremderEmpfehlungFix())
                    || ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 128) != 128
                            && pEclWeisungMeldung.hatWeisungenGemFremderEmpfehlungFlexBestehend())
                    || ((pEclMeldungSammelkarte.skWeisungsartZulaessig & 256) != 256
                            && pEclWeisungMeldung.hatWeisungenGemFremderEmpfehlungFlexNeu())

            ) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmWeisungsartZuDieserSammelkarteUnzulaessig;
                return;
            }
        }

        /*Prüfen, ob Weisungsinterpretation vorhanden*/
        if (pEclWeisungMeldung == null || pEclWeisungMeldung.gemaessEigenemAbstimmungsVorschlagIdent != 0) {
            lDbBundle.dbAbstimmungsVorschlag.leseZuSammelIdent(pAufnehmendeSammelkarteIdent);
            if (lDbBundle.dbAbstimmungsVorschlag.anzAbstimmungsVorschlagGefunden() == 0) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmKeinEigenerAbstimmungsvorschlagVorhanden;
                return;
            }
        }
        if (pEclWeisungMeldung.gemaessFremdenAbstimmungsVorschlagIdent != 0) {
            lDbBundle.dbAbstimmungsVorschlag.lese(Math.abs(pEclWeisungMeldung.gemaessFremdenAbstimmungsVorschlagIdent));
            if (lDbBundle.dbAbstimmungsVorschlag.anzAbstimmungsVorschlagGefunden() == 0) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmKeinEigenerAbstimmungsvorschlagVorhanden;
                return;
            }

        }

        /*Prüfen ob delayed. Überlegung: peding-Buchungen werden nicht delayed, daher nicht vorher erforderlich*/
        if ((lDbBundle
                .willenserklaerungIstDelayed(willenserklaerung) /*Dann ist die Willenserklärung an sich delayed*/
                || weiterverarbeitungDelayed == 1 /*Dann ist vorher was delayed*/) && ptForceNonDelay == false) {
            this.rcDelayed = true;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
        return;

    }

    private void vollmachtUndWeisungAnXY(DbBundle dbBundle, int plWillenserklaerung) {

        try {
        int erg;

        /*Hinweis: sehr viel doppelt in aendernWeisungAnXY!*/

        /*Idents der Sätze, die eingefügt wurden*/
        int lIdentWeisungMeldung = 0, lIdentWillenserklaerung = 0;

        EclWillenserklaerung lWillenserklaerung = null;
        EclWillenserklaerungZusatz lWillenserklaerungZusatz = null;
        EclMeldungZuSammelkarte lEclMeldungZuSammelkarte = null;
        
        willenserklaerungSperren(dbBundle);
         
        this.vollmachtUndWeisungAnXY_pruefe(dbBundle, plWillenserklaerung);


        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        /*======================"Inaktiv" eintragen===================================*/

        /*************Willenserklärung erzeugen************/
        prepareWillenserklaerung(plWillenserklaerung);
        lWillenserklaerung = preparedWillenserklaerung;
        lWillenserklaerungZusatz = preparedWillenserklaerungZusatz;
        if (this.rcDelayed == true) {
            lWillenserklaerung.delayed = 1;
        }

        /*Restliche Felder der Willenserklärung füllen und speichern*/
        lWillenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
        if (this.rcPending == true) {/*Willenserklärung nur als Pending speichern*/
            lWillenserklaerung.pending = 1;
        }
        /*Später noch setzen - aktuell noch nicht bekannt:
         * > identMeldungZuSammelkarte
         */

        erg = dbBundle.dbWillenserklaerung.insert(lWillenserklaerung, lWillenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.vollmachtUndWeisungAnXY - 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
            willenserklaerungFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = lWillenserklaerung.willenserklaerungIdent;

        lIdentWillenserklaerung = lWillenserklaerung.willenserklaerungIdent;

        /*******Zuordnung Sammelkarte <-> Aktionär speichern*******/
        lEclMeldungZuSammelkarte = new EclMeldungZuSammelkarte();
        lEclMeldungZuSammelkarte.meldungsIdent = piMeldungsIdentAktionaer;
        lEclMeldungZuSammelkarte.sammelIdent = pAufnehmendeSammelkarteIdent;
        lEclMeldungZuSammelkarte.willenserklaerungIdent = lIdentWillenserklaerung;
        lEclMeldungZuSammelkarte.aktiv = 0;
        lDbBundle.dbMeldungZuSammelkarte.insert(lEclMeldungZuSammelkarte);

        /*Später noch setzen - aktuell noch nicht bekannt:
         * > willenserklaerungIdent
         * > weisungIdent
         */

        if (pEclWeisungMeldung != null) {
            /**********Weisungen bei Aktionär speichern************************/
            /*Felder in EclWeisungMeldung des Aktionärs füllen*/
            /*meldungsIdent*/
            pEclWeisungMeldung.meldungsIdent = piMeldungsIdentAktionaer;

            /*sammelIdent*/
            pEclWeisungMeldung.sammelIdent = pAufnehmendeSammelkarteIdent;

            /*skIst*/
            switch (plWillenserklaerung) {
            case KonstWillenserklaerung.briefwahl:
                pEclWeisungMeldung.skIst = KonstSkIst.briefwahl;
                break;
            case KonstWillenserklaerung.vollmachtUndWeisungAnSRV:
                pEclWeisungMeldung.skIst = KonstSkIst.srv;
                break;
            case KonstWillenserklaerung.vollmachtUndWeisungAnKIAV:
                pEclWeisungMeldung.skIst = KonstSkIst.kiav;
                break;
            case KonstWillenserklaerung.dauervollmachtAnKIAV:
                pEclWeisungMeldung.skIst = KonstSkIst.dauervollmacht;
                break;
            case KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte:
                pEclWeisungMeldung.skIst = KonstSkIst.organisatorisch;
                break;
            }

            /*weisungSplit*/
            if (pEclWeisungMeldung.hatSplitWeisung()) {
                pEclWeisungMeldung.weisungSplit = 1;
            }

            /*istWeisungBriefwahl*/
            if (plWillenserklaerung == KonstWillenserklaerung.briefwahl) {
                pEclWeisungMeldung.istWeisungBriefwahl = 2;
            } else {
                pEclWeisungMeldung.istWeisungBriefwahl = 1;
            }

            pEclWeisungMeldung.willenserklaerungIdent = lIdentWillenserklaerung;
            pEclWeisungMeldung.aktiv = 0;

            erg = lDbBundle.dbWeisungMeldung.insert(pEclWeisungMeldung, pEclWeisungMeldungRaw);
            lIdentWeisungMeldung = pEclWeisungMeldung.weisungIdent;
        }

        
        /******Nun sind alle Querverweise bekannt******/
        /*von Willenserklärung nachholen: identMeldungZuSammelkarte*/
        lWillenserklaerung.identMeldungZuSammelkarte = pAufnehmendeSammelkarteIdent;
        lDbBundle.dbWillenserklaerung.update(lWillenserklaerung);
        /*von Zuordnung Sammelkarte <-> Aktioär: willenserklaerungIdent und ggf. weisungident*/
        lEclMeldungZuSammelkarte.willenserklaerungIdent = lIdentWillenserklaerung;
        if (pEclWeisungMeldung != null) {
            lEclMeldungZuSammelkarte.weisungIdent = lIdentWeisungMeldung;
        }
        lDbBundle.dbMeldungZuSammelkarte.update(lEclMeldungZuSammelkarte);

        pruefenUndSetzenAktivAktuelleWillenserklaerung(lWillenserklaerung, lEclMeldungZuSammelkarte, true);
        
        willenserklaerungFreigeben(dbBundle);
        } catch (Exception e) {
            abbruchBeiException(dbBundle, e);
        }
        return;
    }

    /***********************Weisung ändern (in Sammelkarte)*********************************************
     * Alle Eingabeparamter müssen gefüllt sein analog zu "neue Vollmacht/Weisung zu Sammelkarte".
     * Zusätzlich muß in pEclWeisungMeldung eingetragen sein:
     * > .weisungIdent = Ident (=Verweis) aufbisherige Weisung (also die, die geändert wird)
     * > .willenserklaerungIdent = ("alte") Willenserklärung, mit der die bisherige Weisung erzeugt (oder zuletzt geändert)
     * 		worden ist.
     * Diese beiden Felder sollten verfügbar sein, wenn die zu ändernde Weisung eingelesen wurde (in den dortigen Feldern).
     *
     * ???In MeldungZuSammelkarte-Ident muß die BISHERIGE Weisung eingetragen werden. ???? Dieser Kommentar ist unklar, konnte nicht
     * nachvollzogen werden!?????
     *
     * Ablauf:
     * Wie bei vollmachtUndWeisung, zusätzlich:
     * 	> Überprüfen, ob die bestehende Weisung mittlerweile schon verändert wurde
     *
     * Die bestehende Weisung und Sammelkartenzuordnung wird auf "geändert" gesetzt. Die veränderte Weisung
     * und Sammelzuordnung wird neu gespeichert.
     */
    private void aendernWeisungAnXY_pruefe(DbBundle dbBundle, int willenserklaerung) {
        int alteWeisung = 0;
        alteWeisung = this.pEclWeisungMeldung.weisungIdent;
        if (alteWeisung == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmAlteWeisungNichtReferenziert;
            return;
        }

        /*Prüfen, ob alte Weisung mittlerweile verändert wurde*/
        if (dbBundle.dbWeisungMeldung.leseAktivKZ(alteWeisung) < 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmAlteWeisungBereitsVeraendert;
            return;
        }

        vollmachtUndWeisungAnXY_pruefe(dbBundle, willenserklaerung);

        if (this.rcIstZulaessig == false) {
            return;
        }

    }

    private void aendernWeisungAnXY(DbBundle dbBundle, int plWillenserklaerung) {
        
        try {

        int erg;

        /*Hinweis: sehr viel doppelt in vollmachtUndWeisungAnXY!*/
        /*Idents der Sätze, die eingefügt wurden*/
        int lIdentWeisungMeldung = 0, lIdentWillenserklaerung = 0;
        int altesAktiv = 0;
        int alteWeisung = 0;
        int alteWillenserklaerung = 0;

        alteWeisung = this.pEclWeisungMeldung.weisungIdent;
        alteWillenserklaerung = this.pEclWeisungMeldung.willenserklaerungIdent;

        EclWillenserklaerung lWillenserklaerung = null;
        EclWillenserklaerungZusatz lWillenserklaerungZusatz = null;
        EclMeldungZuSammelkarte lEclMeldungZuSammelkarte = null;

        willenserklaerungSperren(dbBundle);
        
        this.aendernWeisungAnXY_pruefe(dbBundle, plWillenserklaerung);

        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
           return;
        }

        altesAktiv = lDbBundle.dbWeisungMeldung.leseAktivKZ(alteWeisung);

        /*************Willenserklärung erzeugen************/
        prepareWillenserklaerung(plWillenserklaerung);
        lWillenserklaerung = preparedWillenserklaerung;
        lWillenserklaerungZusatz = preparedWillenserklaerungZusatz;

        if (this.rcDelayed == true) {
            lWillenserklaerung.delayed = 1;
        }

        /*Restliche Felder der Willenserklärung füllen und speichern*/
        lWillenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
        if (this.rcPending == true) {/*Willenserklärung nur als Pending speichern*/
            lWillenserklaerung.pending = 1;
        }

        lWillenserklaerung.verweisAufWillenserklaerung = alteWillenserklaerung;

        /*Später noch setzen - aktuell noch nicht bekannt:
         * > identMeldungZuSammelkarte
         */

        erg = dbBundle.dbWillenserklaerung.insert(lWillenserklaerung, lWillenserklaerungZusatz);
        if (erg < 1) {
            CaBug.drucke("BlWillenserklaerung.vollmachtUndWeisungAnXY - 001");
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = lIdentWillenserklaerung;
            willenserklaerungFreigeben(dbBundle);
            return;
        }
        rcWillenserklaerungIdent = lWillenserklaerung.willenserklaerungIdent;
        lIdentWillenserklaerung = lWillenserklaerung.willenserklaerungIdent;

        /*******Zuordnung Sammelkarte <-> Aktionär speichern*******/
        lEclMeldungZuSammelkarte = new EclMeldungZuSammelkarte();
        lEclMeldungZuSammelkarte.meldungsIdent = piMeldungsIdentAktionaer;
        lEclMeldungZuSammelkarte.sammelIdent = pAufnehmendeSammelkarteIdent;
        lEclMeldungZuSammelkarte.aktiv = altesAktiv;
        lEclMeldungZuSammelkarte.willenserklaerungIdent = lIdentWillenserklaerung;
        lDbBundle.dbMeldungZuSammelkarte.insert(lEclMeldungZuSammelkarte);

        /*Später noch setzen - aktuell noch nicht bekannt:
         * > weisungIdent
         */

        if (pEclWeisungMeldung != null) {
            /**********Weisungen bei Aktionär speichern************************/
            /*Felder in EclWeisungMeldung des Aktionärs füllen*/
            /*meldungsIdent*/
            pEclWeisungMeldung.meldungsIdent = piMeldungsIdentAktionaer;

            /*sammelIdent*/
            pEclWeisungMeldung.sammelIdent = pAufnehmendeSammelkarteIdent;

            /*skIst*/
            switch (plWillenserklaerung) {
            case KonstWillenserklaerung.aendernBriefwahl:
                pEclWeisungMeldung.skIst = KonstSkIst.briefwahl;
                break;
            case KonstWillenserklaerung.aendernWeisungAnSRV:
                pEclWeisungMeldung.skIst = KonstSkIst.srv;
                break;
            case KonstWillenserklaerung.aendernWeisungAnKIAV:
                pEclWeisungMeldung.skIst = KonstSkIst.kiav;
                break;
            case KonstWillenserklaerung.aendernWeisungDauervollmachtAnKIAV:
                pEclWeisungMeldung.skIst = KonstSkIst.dauervollmacht;
                break;
            case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte:
                pEclWeisungMeldung.skIst = KonstSkIst.organisatorisch;
                break;
            }

            /*weisungSplit*/
            if (pEclWeisungMeldung.hatSplitWeisung()) {
                pEclWeisungMeldung.weisungSplit = 1;
            }

            /*istWeisungBriefwahl*/
            if (plWillenserklaerung == KonstWillenserklaerung.briefwahl) {
                pEclWeisungMeldung.istWeisungBriefwahl = 2;
            } else {
                pEclWeisungMeldung.istWeisungBriefwahl = 1;
            }

            pEclWeisungMeldung.willenserklaerungIdent = lIdentWillenserklaerung;
            pEclWeisungMeldung.aktiv = altesAktiv;

            erg = lDbBundle.dbWeisungMeldung.insert(pEclWeisungMeldung, pEclWeisungMeldungRaw);
            lIdentWeisungMeldung = pEclWeisungMeldung.weisungIdent;
        }

        /******Nun sind alle Querverweise bekannt******/
        /*von Willenserklärung nachholen: identMeldungZuSammelkarte*/
        lWillenserklaerung.identMeldungZuSammelkarte = pAufnehmendeSammelkarteIdent;
        lDbBundle.dbWillenserklaerung.update(lWillenserklaerung);

        /*von Zuordnung Sammelkarte <-> Aktioär: willenserklaerungIdent und ggf. weisungident*/
        if (pEclWeisungMeldung != null) {
            lEclMeldungZuSammelkarte.weisungIdent = lIdentWeisungMeldung;
        }
        lDbBundle.dbMeldungZuSammelkarte.update(lEclMeldungZuSammelkarte);

        /*Aktiv in alter Weisung und Zuordnung auf -2 (geändert) setzen)*/
        int neuAktiv = -2;
        lDbBundle.dbWeisungMeldung.updateAktivKZ(alteWeisung, neuAktiv);
        lDbBundle.dbMeldungZuSammelkarte.updateAktivKZ(alteWillenserklaerung, neuAktiv);

        pruefenUndSetzenAktivAktuelleWillenserklaerung(lWillenserklaerung, lEclMeldungZuSammelkarte, false);

        if (altesAktiv == 1) {
            /***********Aktivieren****************************/

            /*Sammelkarte Summen bereinigen - in meldung*/

            /*Sammelkarte Summen bereinigen - in Weisungssatz*/

            /*Bei Aktionär "in Sammelkarte" setzen*/

            /*TODO _Sammelkarten: Änderungen konsolidieren - hier ist offensichtlich was noch nicht fertig*/
        }
        willenserklaerungFreigeben(lDbBundle);
        } catch (Exception e) {
            abbruchBeiException(lDbBundle, e);
        }
        return;

    }

    /**********************************Widerruf VollmachtUndWeisung********************************************************
     * Eingabeparameter wie üblich.
     *
     * Angabe der zu widerrufenden Willenserklärungen:
     *
     * > Eingabeparameter-Identifikation müssen immer gefüllt sein (wie üblich, zusätzlich entweder a oder b oder c)
     * > pWillenserklaerungGeberIdent muß immer gefüllt sein (daraus wird abgeleitet, ob und ggf. welche
     * 		VollmachtUndWeisung storniert werden soll)
     * 		= 0 => alle Willenserklärungen, betreffend dieser Meldung und des Sammelkartentyps, werden storniert
     *			Anwendungsfall z.B.: persönliches Erscheinen, und zwangsweise alle anderen Sammelkarteneinträge eines Typs sollen
     * 			storniert werden (aber nicht unbedingt andere Vollmachten/Dritte)
     * 		> 0 => alle Willenserklärungen (je nach Parameter direkt oder indirekt erreichbar) dieses
     * 			Typs werden storniert
     * > optional: pAufnehmendeSammelkarteIdent !=0 => Nur die Verbindungen dieser Art zu dieser
     * 			Sammelkarte werden storniert
     * > optional: <pEclMeldungZuSammelkarte ungleich Null> <==> noch überprüfen, ob das auch optional ist! >>>>
     * 		 und pEclMeldungZuSammelkarte.willenserklaerungIdent gefüllt:
     * 		nur diese eine Verbindung
     * 		wird storniert - vorausgesetzt die Parameter lassen zu, dass pWillenserklaerungGeberIdent dies
     * 		darf. In diesem Fall muß auch pAufnehmendeSammelkarteIdent gefüllt sein.
     *
     * Zur Entwicklung:
     * > Die wirklichen Stornofunktionen sind in Subroutinen, die die Ein- und Ausgabestruktur von
     * 		BlWillenserklaerung nicht verwendet, sondern über Aufruf-Parameter versorgt wird. Diese Subroutinen
     * 		werden dann auch beim StornoVollmachtDritte verwendet, wenn rekursiv durch die Stornierung
     * 		einer Vollmacht an Dritte auch Vollmachten an Sammelkarten storniert werden müssen.
     * 	Aufrufparamter sind dann meldungsIdent, pWillenserklaerunggeberIdent,
     * 	pAufnahmendeSammelkarteIdent, weisungsident.
     * Möglicherweise macht es Sinn, dies noch weiter in verschiedene Subroutinen zu Zerlegen?
     */
    private void widerrufVollmachtUndWeisungAnXY_pruefe(DbBundle dbBundle, int willenserklaerung) {
        int i;
        boolean gefSammelkartenZuordnung = false; /*True => es wurden generell Zuordnungen gefunden*/
        boolean gefSammelkartenZuordnungIstGueltig = false; /*True => es wurde eine aktive Zuordnung gefunden*/
        boolean gefSammelkartenZuordnungStornierbarVonGeber = false; /*True => es wurde eine Vollmacht gefunden, die vom Geber storniert werden darf*/
        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        /*Prüfen, ob ein Aktionär referenziert wurde*/
        if (piMeldungsIdentAktionaer == 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmVollmachtNurFuerAktionaerMoeglich;
            return;
        }

        einlesenZuordnungZuSammelkarten();

        einlesenVollmachtenAnDritte();

        /*Alle Sammelkartenzuordnungen durchgehen.
         * Dabei merker setzen. -1 => nicht zu stornieren; +1 => wird storniert*/
        CaBug.druckeLog("rcZuordnungZuSammelkarteAnzahl=" + rcZuordnungZuSammelkarteAnzahl, logDrucken, 10);
        for (i = 0; i < rcZuordnungZuSammelkarteAnzahl; i++) {
            CaBug.druckeLog("Start von Schleife i", logDrucken, 10);
            this.rcZuordnungZuSammelkarte[i].merker = -1;

            if (pEclMeldungZuSammelkarte == null /*Keine bestimmte Willenserklärung spezifiziert*/
                    || pEclMeldungZuSammelkarte.willenserklaerungIdent == rcZuordnungZuSammelkarte[i].meldungZuSammelkarte.willenserklaerungIdent /*Willenserklärung passt zur spezifizierten*/
            ) {
                CaBug.druckeLog("In If 1 drin", logDrucken, 10);
                if (negiereWillenserklaerung(
                        rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerung) == willenserklaerung) {
                    CaBug.druckeLog("In If 2 drin", logDrucken, 10);
                    /*Gefundene Sammelkartenzuordnung paßt zum Willenserklärungstyp*/
                    if (pAufnehmendeSammelkarteIdent == rcZuordnungZuSammelkarte[i].zugeordneteSammelkarteIdent) {
                        CaBug.druckeLog("In If 3 drin", logDrucken, 10);
                        gefSammelkartenZuordnung = true;
                        if (this.rcZuordnungZuSammelkarte[i].aktiv >= 0) {/*Zuordnung sonst nicht mehr gültig, d.h. widerrufen oder geändert*/
                            gefSammelkartenZuordnungIstGueltig = true;
                            if (pWillenserklaerungGeberIdent == 0 /*"Undefiniert" darf alles stornieren*/
                                    || (pWillenserklaerungGeberIdent == rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerungGeberIdent) /*Geber darf eigene Erklärung immer stornieren*/
                                    || (pWillenserklaerungGeberIdent == -1
                                            && lDbBundle.param.paramWillenserklaerungen.paramVollmacht_AktionaerDarfAlleNachfolgerStornieren) /*Aktionär selbst darf - bei entsprechender Parameterstellung - alles stornieren*/
                                    || (pWillenserklaerungGeberIdent > 0
                                            && lDbBundle.param.paramWillenserklaerungen.paramVollmacht_BevollmaechtigterDarfAllesStornieren) /*Bevollmächtigter darf alles - bei entsprechender Parameterstellung*/
                                    || (gibtEsWegVonZu(pWillenserklaerungGeberIdent,
                                            rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerungGeberIdent)
                                            && lDbBundle.param.paramWillenserklaerungen.paramVollmacht_VorgaengerDarfAlleNachfolgerStornieren) /*Es gibt Weg, und abhängige dürfen storniert werden*/
                            ) {
                                gefSammelkartenZuordnungStornierbarVonGeber = true;
                                /*Überprüfen: ist Vollmacht gerade präsent?*/
                                /*LANGTODO - Präsenz überprüfen derzeit noch nicht implementiert - erst machen wenn Zu-/Abgang etc.*/
                                this.rcZuordnungZuSammelkarte[i].merker = 1;
                            }

                        }
                    }
                }
            }

        }

        if (gefSammelkartenZuordnung == false) {/*Zuordnung Nicht Vorhanden*/
            if (lDbBundle.pendingIstMoeglich(willenserklaerung)) {
                this.rcPending = true;
            }
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmZuordnungZuSammelkarteNichtVorhanden;
            return;
        }
        if (gefSammelkartenZuordnungIstGueltig == false) {/*Zuordnung nicht mehr gültig*/
            if (lDbBundle.pendingIstMoeglich(willenserklaerung)) {
                this.rcPending = true;
            }
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmZuordnungSammelkarteNichtMehrGueltig;
            return;
        }

        if (gefSammelkartenZuordnungStornierbarVonGeber == false) { /*Darf nicht vom Geber storniert werden*/
            if (lDbBundle.pendingIstMoeglich(willenserklaerung)) {
                this.rcPending = true;
            }
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmZuordnungSammelkarteNichtVonDieserPersonStornierbar;
            return;

        }

        /*Prüfen ob delayed. Überlegung: pending-Buchungen werden nicht delayed, daher nicht vorher erforderlich*/
        if ((lDbBundle
                .willenserklaerungIstDelayed(willenserklaerung) /*Dann ist die Willenserklärung an sich delayed*/
                || weiterverarbeitungDelayed == 1 /*Dann ist vorher was delayed*/) && ptForceNonDelay == false) {
            this.rcDelayed = true;
        }

    }

    private void widerrufVollmachtUndWeisungAnXY(DbBundle dbBundle, int pWillenserklaerung) {

        try {

        int erg, i, anzstorno;
        int fuehrendeWillenserklaerungIdent = 0;

        EclWillenserklaerung willenserklaerung = null;
        EclWillenserklaerungZusatz willenserklaerungZusatz = null;

        willenserklaerungSperren(dbBundle);
        
        this.widerrufVollmachtUndWeisungAnXY_pruefe(dbBundle, pWillenserklaerung);

        if (this.rcIstZulaessig == false && rcPending == true) {
            /*Einfach Storno als Pending speichern - keine weiteren Aktionen!*/
            prepareWillenserklaerung(pWillenserklaerung);
            willenserklaerung = preparedWillenserklaerung;
            willenserklaerungZusatz = preparedWillenserklaerungZusatz;
            willenserklaerung.verweisart = 0;
            willenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
            if (pEclMeldungZuSammelkarte != null) {
                willenserklaerung.verweisAufWillenserklaerung = pEclMeldungZuSammelkarte.willenserklaerungIdent;
            }
            willenserklaerung.bevollmaechtigterDritterIdent = pAufnehmendeSammelkarteIdent;
            willenserklaerung.pending = 1;
            erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
            if (erg < 1) {
                CaBug.drucke("BlWillenserklaerung.widerrufVollmachtUndWeisungAnXY 003");
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                willenserklaerungFreigeben(dbBundle);
                return;
            }
            rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;

            willenserklaerungFreigeben(dbBundle);
            return;
        }

        if (this.rcIstZulaessig == false) {
            willenserklaerungFreigeben(dbBundle);
            return;
        }

        anzstorno = 0; /*Anzahl der zu stornierenden VollmachtAnSammelkarten*/
        for (i = 0; i < rcZuordnungZuSammelkarteAnzahl; i++) {
            if (rcZuordnungZuSammelkarte[i].merker == 1) {
                anzstorno++;

                /*Neue Willenserklärung mit Verweis auf zu stornierende erzeugen*/
                prepareWillenserklaerung(pWillenserklaerung);
                willenserklaerung = preparedWillenserklaerung;
                willenserklaerungZusatz = preparedWillenserklaerungZusatz;

                if (this.rcDelayed == true) {
                    willenserklaerung.delayed = 1;
                }
                if (anzstorno == 1) { /*Dann erste Stornierung*/
                } else { /*Folgestornierung - d.h. als Folgesatz kennzeichnen*/
                    willenserklaerung.folgeBuchungFuerIdent = fuehrendeWillenserklaerungIdent;
                }

                willenserklaerung.verweisart = 4;
                willenserklaerung.verweisAufWillenserklaerung = rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerungIdent;
                willenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
                if (pEclMeldungZuSammelkarte != null) {
                    willenserklaerung.verweisAufWillenserklaerung = pEclMeldungZuSammelkarte.willenserklaerungIdent;
                }
                willenserklaerung.bevollmaechtigterDritterIdent = pAufnehmendeSammelkarteIdent;
                erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
                if (erg < 1) {
                    CaBug.drucke("BlWillenserklaerung.widerrufVollmachtAnDritte 002");
                    this.rcIstZulaessig = false;
                    this.rcGrundFuerUnzulaessig = erg;
                    willenserklaerungFreigeben(dbBundle);
                    return;
                }

                if (anzstorno == 1) { /*Dann erste Stornierung - Nr. der Willenserklärung für evtl. Folgestornierungen merken*/
                    fuehrendeWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;
                    rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;
                }

                /*Sammelkarten-Zuordnung "Schnellzugriffe" updaten  - in Datenbank zu EclMeldungZuSammelkarte, EclWeisungMeldung*/
                dbBundle.dbMeldungZuSammelkarte
                        .updateAktivKZ(rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerungIdent, -1);
                if (rcZuordnungZuSammelkarte[i].meldungZuSammelkarte.weisungIdent != 0) {
                    dbBundle.dbWeisungMeldung
                            .updateAktivKZ(rcZuordnungZuSammelkarte[i].meldungZuSammelkarte.weisungIdent, -1);
                }

                /*Falls Zuordnung aktiv, dann Sammelkarte korrigieren*/
                pruefenUndSetzenDeaktivWillenserklaerung(rcZuordnungZuSammelkarte[i].meldungZuSammelkarte, true);
            }

        }
        willenserklaerungFreigeben(dbBundle);
        } catch (Exception e) {
            abbruchBeiException(dbBundle, e);
        }

    }

    /**Widerruf alle Vollmachten und Weisungen, die direkt von einer bestimmten Person person erteilt wurden.
     * Wird verwendet beim Stornieren von Bevollmächtigten, um die von dem zu stornierenden Bevollmächtigten erteilten
     * VollmachtWeisungen auch zu stornieren.
     *
     * Nur innerhalb von Transaktion!
     * meldung etc. muß eingelesen sein.
     * Ebenso ist der Aufruf von einlesenZuordnungZuSammelkarten() in der verwendenden Funktion durchzuführen
     *
     * folgebuchungFuer: hier muss die "ursprüngliche" Willenserklärung angegeben werden, wegen der diese
     * "Folgestornierungen" erfolgen.
     *
     * Vorgehen:
     * > die bestehende Willenserklärung wird nahezu unverändert kopiert in eine neue Willenserklärung.
     * 		Verändert werden: personNatJurGeber, und ggf. Willenserklärungsart (von "Änderung" auf "Erteilung")
     * > die bestehende Weisung wird unverändert kopiert (mit Ausnahme der Verweisfelder)
     * > Sammelkartensummen und Präsenzsummen brauchen nicht verändert werden
     * > Dann die alte Willenserklärung als storniert markieren.
     */
    private void widerrufVollmachtUndWeisungVonPerson(DbBundle dbBundle, int person, int folgebuchungFuer) {

        int i, erg;
        EclWillenserklaerung willenserklaerung = null;
        EclWillenserklaerungZusatz willenserklaerungZusatz = null;
        int willenserklaerungArt = KonstWillenserklaerung.undefiniert;
        int willenserklaerungArtZuStornieren;

        for (i = 0; i < rcZuordnungZuSammelkarteAnzahl; i++) {

            if (this.rcZuordnungZuSammelkarte[i].aktiv >= 0) {/*Zuordnung sonst nicht mehr gültig, d.h. widerrufen oder geändert*/
                if (person == rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerungGeberIdent) {

                    /*"Alte" Willenserklärung einlesen*/
                    dbBundle.dbWillenserklaerung
                            .leseZuIdent(rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerungIdent);
                    EclWillenserklaerung neueWillenserklaerung = dbBundle.dbWillenserklaerung
                            .willenserklaerungGefunden(0);
                    dbBundle.dbWillenserklaerungZusatz
                            .leseZuIdent(rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerungIdent);
                    EclWillenserklaerungZusatz neueWillenserklaerungZusatz = null;
                    if (dbBundle.dbWillenserklaerungZusatz.anzWillenserklaerungGefunden() > 0) {
                        neueWillenserklaerungZusatz = dbBundle.dbWillenserklaerungZusatz.willenserklaerungGefunden(0);
                    }
                    int alteWillenserklaerungIdent = neueWillenserklaerung.willenserklaerungIdent;
                    /*Alte Willenserklärung korrigieren*/
                    neueWillenserklaerung.willenserklaerungIdent = 0;
                    int neueWillenserklaerungArt = neueWillenserklaerung.willenserklaerung;
                    switch (neueWillenserklaerungArt) {
                    case KonstWillenserklaerung.aendernWeisungAnSRV:
                        neueWillenserklaerungArt = KonstWillenserklaerung.vollmachtUndWeisungAnSRV;
                        break;
                    case KonstWillenserklaerung.aendernWeisungAnKIAV:
                        neueWillenserklaerungArt = KonstWillenserklaerung.vollmachtUndWeisungAnKIAV;
                        break;
                    case KonstWillenserklaerung.aendernBriefwahl:
                        neueWillenserklaerungArt = KonstWillenserklaerung.briefwahl;
                        break;
                    case KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte:
                        neueWillenserklaerungArt = KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte;
                        break;
                    case KonstWillenserklaerung.aendernWeisungDauervollmachtAnKIAV:
                        neueWillenserklaerungArt = KonstWillenserklaerung.dauervollmachtAnKIAV;
                        break;
                    }
                    neueWillenserklaerung.willenserklaerung = neueWillenserklaerungArt;
                    neueWillenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
                    if (neueWillenserklaerungZusatz != null) {
                        neueWillenserklaerungZusatz.willenserklaerungIdent = 0;
                        neueWillenserklaerungZusatz.willenserklaerung = neueWillenserklaerungArt;
                    }
                    /*Korrigierte Willenserklärung als neue Willenserklärung speichern - muß später noch korrigiert werden wg. Weisungsident*/
                    erg = dbBundle.dbWillenserklaerung.insert(neueWillenserklaerung, neueWillenserklaerungZusatz);
                    if (erg < 1) {
                        CaBug.drucke("001");
                        this.rcIstZulaessig = false;
                        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                        return;
                    }
                    int neueWillenserklaerungIdent = neueWillenserklaerung.willenserklaerungIdent;

                    /*Alte Weisungen einlesen*/
                    erg = dbBundle.dbWeisungMeldung.leseZuWillenserklaerungIdent(alteWillenserklaerungIdent, true);
                    if (erg < 1) {
                        CaBug.drucke("002");
                        this.rcIstZulaessig = false;
                        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                         return;
                    }
                    EclWeisungMeldung neueWeisungMeldung = dbBundle.dbWeisungMeldung.weisungMeldungGefunden(0);
                    EclWeisungMeldungRaw neueWeisungMeldungRaw = dbBundle.dbWeisungMeldung.weisungMeldungRawGefunden(0);
                    /*Alte Weisung zu neuer Willenserklärungsident speichern*/
                    neueWeisungMeldung.willenserklaerungIdent = neueWillenserklaerungIdent;
                    erg = dbBundle.dbWeisungMeldung.insert(neueWeisungMeldung, neueWeisungMeldungRaw);
                    if (erg < 1) {
                        CaBug.drucke("003");
                        this.rcIstZulaessig = false;
                        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                        return;
                    }
                    int neueWeisungMeldungIdent = neueWeisungMeldung.weisungIdent;

                    /******Nun sind alle Querverweise bekannt******/
                    /*von Willenserklärung nachholen: identMeldungZuSammelkarte*/
                    neueWillenserklaerung.identMeldungZuSammelkarte = pAufnehmendeSammelkarteIdent;
                    erg=lDbBundle.dbWillenserklaerung.update(neueWillenserklaerung);
                    if (erg < 1) {
                        CaBug.drucke("004");
                        this.rcIstZulaessig = false;
                        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                        return;
                    }

                    /*von Zuordnung Sammelkarte <-> Aktionär: willenserklaerungIdent und weisungident*/
                    erg = dbBundle.dbMeldungZuSammelkarte.leseZuWillenserklaerung(alteWillenserklaerungIdent);
                    if (erg < 1) {
                        CaBug.drucke("005");
                        this.rcIstZulaessig = false;
                        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                        return;
                    }
                    EclMeldungZuSammelkarte neueMeldungZuSammelkarte = dbBundle.dbMeldungZuSammelkarte
                            .meldungZuSammelkarteGefunden(0);
                    neueMeldungZuSammelkarte.willenserklaerungIdent = neueWillenserklaerungIdent;
                    neueMeldungZuSammelkarte.weisungIdent = neueWeisungMeldungIdent;
                    lDbBundle.dbMeldungZuSammelkarte.insert(neueMeldungZuSammelkarte);

                    /*Nun noch bei Meldung neue Idents eintragen*/
                    dbBundle.dbMeldungen.leseZuIdent(neueWillenserklaerung.meldungsIdent);
                    if (dbBundle.dbMeldungen.anzErgebnis() < 1) {
                        CaBug.drucke("006");
                        this.rcIstZulaessig = false;
                        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                        return;
                    }
                    EclMeldung lMeldung = dbBundle.dbMeldungen.meldungenArray[0];
                    lMeldung.willenserklaerungIdent = neueWillenserklaerungIdent;

                    willenserklaerungArtZuStornieren = rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerung;
                    willenserklaerungArt = negiereWillenserklaerung(willenserklaerungArtZuStornieren);

                    /*Neue Willenserklärung mit Verweis auf zu stornierende erzeugen*/
                    prepareWillenserklaerung(willenserklaerungArt);
                    willenserklaerung = preparedWillenserklaerung;
                    willenserklaerungZusatz = preparedWillenserklaerungZusatz;

                    if (this.rcDelayed == true) {
                        willenserklaerung.delayed = 1;
                    }
                    willenserklaerung.folgeBuchungFuerIdent = folgebuchungFuer;

                    willenserklaerung.verweisart = 4;
                    willenserklaerung.verweisAufWillenserklaerung = rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerungIdent;
                    willenserklaerung.willenserklaerungGeberIdent = pWillenserklaerungGeberIdent;
                    erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
                    if (erg < 1) {
                        CaBug.drucke("BlWillenserklaerung.widerrufVollmachtUndWeisungVonPerson 007");
                        this.rcIstZulaessig = false;
                        this.rcGrundFuerUnzulaessig = erg;
                        return;
                    }

                    /*Sammelkarten-Zuordnung "Schnellzugriffe" updaten  - in Datenbank zu EclMeldungZuSammelkarte, EclWeisungMeldung*/
                    dbBundle.dbMeldungZuSammelkarte.updateAktivKZ(
                            rcZuordnungZuSammelkarte[i].willenserklaerungErteilt.willenserklaerungIdent, -1);
                    dbBundle.dbWeisungMeldung
                            .updateAktivKZ(rcZuordnungZuSammelkarte[i].meldungZuSammelkarte.weisungIdent, -1);

                }

            }
        }

    }

    /********************Ab hier einzelne Willenserklärungen nach außen verfügbar*********************/
    /*vollmachtUndWeisungAnSRV*/
    public void vollmachtUndWeisungAnSRV_pruefe(DbBundle dbBundle) {
        vollmachtUndWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.vollmachtUndWeisungAnSRV);
    }

    public void vollmachtUndWeisungAnSRV(DbBundle dbBundle) {
        vollmachtUndWeisungAnXY(dbBundle, KonstWillenserklaerung.vollmachtUndWeisungAnSRV);
    }

    public void aendernWeisungAnSRV_pruefe(DbBundle dbBundle) {
        aendernWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.aendernWeisungAnSRV);
    }

    public void aendernWeisungAnSRV(DbBundle dbBundle) {
        aendernWeisungAnXY(dbBundle, KonstWillenserklaerung.aendernWeisungAnSRV);
    }

    public void widerrufVollmachtUndWeisungAnSRV_pruefe(DbBundle dbBundle) {
        widerrufVollmachtUndWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.widerrufVollmachtUndWeisungAnSRV);
    }

    public void widerrufVollmachtUndWeisungAnSRV(DbBundle dbBundle) {
        widerrufVollmachtUndWeisungAnXY(dbBundle, KonstWillenserklaerung.widerrufVollmachtUndWeisungAnSRV);
    }

    /*vollmachtUndWeisungAnKIAV*/
    public void vollmachtUndWeisungAnKIAV_pruefe(DbBundle dbBundle) {
        vollmachtUndWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.vollmachtUndWeisungAnKIAV);
    }

    public void vollmachtUndWeisungAnKIAV(DbBundle dbBundle) {
        vollmachtUndWeisungAnXY(dbBundle, KonstWillenserklaerung.vollmachtUndWeisungAnKIAV);
    }

    public void aendernWeisungAnKIAV_pruefe(DbBundle dbBundle) {
        aendernWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.aendernWeisungAnKIAV);
    }

    public void aendernWeisungAnKIAV(DbBundle dbBundle) {
        aendernWeisungAnXY(dbBundle, KonstWillenserklaerung.aendernWeisungAnKIAV);
    }

    public void widerrufVollmachtUndWeisungAnKIAV_pruefe(DbBundle dbBundle) {
        widerrufVollmachtUndWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV);
    }

    public void widerrufVollmachtUndWeisungAnKIAV(DbBundle dbBundle) {
        widerrufVollmachtUndWeisungAnXY(dbBundle, KonstWillenserklaerung.widerrufVollmachtUndWeisungAnKIAV);
    }

    /*dauervollmachtAnKIAV*/
    public void dauervollmachtAnKIAV_pruefe(DbBundle dbBundle) {
        vollmachtUndWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.dauervollmachtAnKIAV);
    }

    public void dauervollmachtAnKIAV(DbBundle dbBundle) {
        vollmachtUndWeisungAnXY(dbBundle, KonstWillenserklaerung.dauervollmachtAnKIAV);
    }

    public void aendernWeisungDauervollmachtAnKIAV_pruefe(DbBundle dbBundle) {
        aendernWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte);
    }

    public void aendernWeisungDauervollmachtAnKIAV(DbBundle dbBundle) {
        aendernWeisungAnXY(dbBundle, KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte);
    }

    public void widerrufDauervollmachtAnKIAV_pruefe(DbBundle dbBundle) {
        widerrufVollmachtUndWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.widerrufDauervollmachtAnKIAV);
    }

    public void widerrufDauervollmachtAnKIAV(DbBundle dbBundle) {
        widerrufVollmachtUndWeisungAnXY(dbBundle, KonstWillenserklaerung.widerrufDauervollmachtAnKIAV);
    }

    /*organisatorisch*/
    public void organisatorischMitWeisungInSammelkarte_pruefe(DbBundle dbBundle) {
        vollmachtUndWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte);
    }

    public void organisatorischMitWeisungInSammelkarte(DbBundle dbBundle) {
        vollmachtUndWeisungAnXY(dbBundle, KonstWillenserklaerung.organisatorischMitWeisungInSammelkarte);
    }

    public void aendernWeisungOrganisatorischInSammelkarte_pruefe(DbBundle dbBundle) {
        aendernWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte);
    }

    public void aendernWeisungOrganisatorischInSammelkarte(DbBundle dbBundle) {
        aendernWeisungAnXY(dbBundle, KonstWillenserklaerung.aendernWeisungOrganisatorischInSammelkarte);
    }

    public void widerrufOrganisatorischMitWeisungInSammelkarte_pruefe(DbBundle dbBundle) {
        widerrufVollmachtUndWeisungAnXY_pruefe(dbBundle,
                KonstWillenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte);
    }

    public void widerrufOrganisatorischMitWeisungInSammelkarte(DbBundle dbBundle) {
        widerrufVollmachtUndWeisungAnXY(dbBundle,
                KonstWillenserklaerung.widerrufOrganisatorischMitWeisungInSammelkarte);
    }

    /*briefwahl*/
    public void briefwahl_pruefe(DbBundle dbBundle) {
        vollmachtUndWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.briefwahl);
    }

    public void briefwahl(DbBundle dbBundle) {
        vollmachtUndWeisungAnXY(dbBundle, KonstWillenserklaerung.briefwahl);
    }

    public void aendernBriefwahl_pruefe(DbBundle dbBundle) {
        aendernWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.aendernBriefwahl);
    }

    public void aendernBriefwahl(DbBundle dbBundle) {
        aendernWeisungAnXY(dbBundle, KonstWillenserklaerung.aendernBriefwahl);
    }

    public void widerrufBriefwahl_pruefe(DbBundle dbBundle) {
        widerrufVollmachtUndWeisungAnXY_pruefe(dbBundle, KonstWillenserklaerung.widerrufBriefwahl);
    }

    public void widerrufBriefwahl(DbBundle dbBundle) {
        widerrufVollmachtUndWeisungAnXY(dbBundle, KonstWillenserklaerung.widerrufBriefwahl);
    }

    /*LANGTODO????????????????
     *
     *
     *LANGTODO
     *
     *
     *LANGTODO
     * */

    /*TODO #9 ab hier alles nicht mehr überarbeitet!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *
     */
    /******************************************zugangGast_pruefe****************************************************/
    /** Zugang eines Gastes. Als ZutrittsIdent kann sowohl eine Gast-ZutrittsIdent, als auch eine
     * Aktionärs-ZutrittsIdent der auch eine Gastmeldung zugeordnet ist, dienen.
     *
     * Mögliche Identifikation:
     * > piEclMeldung eingelesen (mit Klasse 0 oder Klasse 1 - aber keine Sammelkarte)
     * > piMeldungsIdent
     * > piZutrittsIdent, piKlasse=0 oder 1
     * > piZutrittsIdent
     *
     */
    public void zugangGast_pruefe(DbBundle dbBundle) {

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        /*Prüfen, ob über Identifikation eine GastMeldung erreicht wurde*/
        if (piMeldungsIdentGast == 0) {

        }
        /*LANGTODO???????????????????????????????????????*/

        /*Nun prüfen, ob wirklich "ein echter Gast" eingelesen wurde.
         * Ist erforderlich für die Kombination "piZutrittsIdent ohne Klasse"*/
        if (piKlasse != 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmKeinGastGefunden;
            return;
        }

        /*LANGTODO Hinweis für normalem Aktionär: berücksichtigen, ob in Sammelkarte*/

        /*Präsenzstatus ermitteln - falls bereits präsent kein Zugang möglich*/
        if (piEclMeldungGast.delayedVorhanden == 1 /*Bereits Delayed vorhanden => Aufsetzen auf Delayed-Präsenzstatus*/
                && ptForceNonDelay == false /* Falls ForceNonDelay=true, dann wird immer auf "normalem" Präsenzstatus aufgesetzt*/
        ) {
            if (piEclMeldungGast.statusPraesenz_Delayed == 1) {/*Falls Pending zulässig => Pending-Eintrag ermöglichen*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmBereitsAnwesend;
                if (lDbBundle.pendingIstZulaessig()) {
                    this.rcPending = true;
                }
                return;
            }
            /*Willenserklärung kann grundsätzlich durchgeführt werden - allerdings liegt bereits delayed-WIllenserklärung vor,
             * d.h. diese Willenserklärung wird auch delayed. Außer: es entsteht eine zulässige Kombi, die die Willenserklärung aufhebt*/
            lDbBundle.dbWillenserklaerung.leseZuMeldungGast(this.piEclMeldungGast);
            int anzWillen = lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden();
            if (anzWillen == 0) {
                /*Hier ist eigentlich was falsch - denn wenn delayed vorhanden, dann muß auch mindestens eine Willenserklärung da sein!
                 *
                 */
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                return;
            }

            /*Willenserklärungen nur zu berücksichtigen:
             * >> Nicht stornierte
             * >> Keine Stornos selbst
             * >> Keine Pendings
             * >> Abgänge / Zugänge
             */
            /*Letzte Präsenz-Änderungs-Willenserklärung suchen*/
            int i;
            int gefLetztePassendePraesenzAenderung = 0;
            int gefAndererDelayed = 0; /*Es gibt eine andere Willenserklärung, die nicht aufgelöst werden kann, und die "delayed" ist
                                       /*LANGTODO !!!!!!!!!!!!!!!!!!Storno-Willenserklaerungen fehlen noch!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!*/
            EclWillenserklaerung tempWillenserklaerung = null;
            for (i = anzWillen; i >= 1; i--) {
                tempWillenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i - 1);
                if (tempWillenserklaerung.pending == 0) {
                    if (tempWillenserklaerung.willenserklaerung == KonstWillenserklaerung.zugangGast) {
                        /*Darf nicht möglich sein => technischer Fehler!*/
                        this.rcIstZulaessig = false;
                        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                        return;
                    }
                    if (tempWillenserklaerung.willenserklaerung == KonstWillenserklaerung.abgangGast) {
                        /*Vorhergehende Willenserklärung ist grundsätzlich passend. Nun aber klären, ob Auflösung auch zulässig gemäß Parameterstellen*/
                        if (lDbBundle.delayAufloesenAbZuVerschiedeneIdent()) {
                            /*Dann Zu-/Abgang immer gemeinsam aufloesen*/
                            gefLetztePassendePraesenzAenderung = i;
                        } else {
                            if (lDbBundle.delayAufloesenAbZuGleicheIdent()) {
                                if (tempWillenserklaerung.identifikationDurch == this.identifikationDurch) {
                                    if (this.identifikationDurch == 0 /*piEclMeldung - braucht nicht verglichen zu werden*/
                                            || this.identifikationDurch == 1 /*MeldungsIdent muß auch gleich sein*/
                                            || ((this.identifikationDurch == 2 || this.identifikationDurch == 3)
                                                    && tempWillenserklaerung.zutrittsIdent == this.piZutrittsIdent.zutrittsIdent)
                                    /*Weitere Idents bei Gästen derzeit nicht zulässig*/
                                    ) {

                                    }
                                }
                                /*Nur auflösen, wenn gleiche Ident!*/
                                gefLetztePassendePraesenzAenderung = i;
                            }
                        }
                        break;
                    }
                    /*Wenn hier ist, dann war dies eine nicht-zu berücksichtigende Willenserklärung, weiterlesen!*/
                }
            }
            if (gefLetztePassendePraesenzAenderung == 0) {
                /*Das darf nicht sein ...!*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                return;
            }
            /*Falls diese Abgang und Delayed, dann prinzipiell "Aufheben" möglich.
             * Ansonsten technischer fehler (Präsenzkennzeichen müßte dann ein anderes sein!)*/

            /*In diesem Fall: suchen, ob es andere "delayed" Meldungen gibt, denn dann ist
             * komplettes auflösen nicht möglich
             */
            for (i = anzWillen; i >= 1; i--) {
                if (i != gefLetztePassendePraesenzAenderung) {
                    tempWillenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i - 1);
                    if (tempWillenserklaerung.delayed == 1) {
                        gefAndererDelayed = i;
                    }
                }
            }
            if (gefAndererDelayed != 0 /*Anderer Delay gefunden - deshalb kein Auflösen möglich*/
                    || gefLetztePassendePraesenzAenderung == 0 /*Keine passende Präsenzänderung gefunden - deshalb kein Auflösen möglich*/
            ) {
                this.rcDelayed = true;
            } else {
                this.rcDelayedAufloesen = true;
            }

        } else {/*Kein Delayed vorhanden => Aufsetzen auf normalem Präsenzstatus*/
            if (piEclMeldungGast.statusPraesenz == 1) {/*Falls Pending zulässig => Pending-Eintrag ermöglichen*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pmBereitsAnwesend;
                if (lDbBundle.pendingIstZulaessig()) {
                    this.rcPending = true;
                }
                return;
            }
        }

        if (lDbBundle.willenserklaerungIstDelayed(KonstWillenserklaerung.zugangGast)
                && this.rcDelayedAufloesen == true) {
            /*Zugang wird delayed, auch wenn vorher noch keine delayed-Willenserklärung abgegeben wurde.
             * Dies erfolgt allerdings nur, wenn sich diese Willenserklärung nicht mit einer vorhergehenden aufhebt*/
            this.rcDelayed = true;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;

        return;
    }

    /** Zugang eines Gastes - nur mit einer "echten" Gastmeldung und einer "echten" GastzutrittsIdent möglich.
     *
     * Mögliche Identifikation:
     * > piEclMeldungGast eingelesen (mit Klasse 0)
     * > piMeldungsIdentGast
     * > piZutrittsIdent, piKlasse=0
     * > piZutrittsIdent
     *
     * Hinweis: ein Meldesatz, der zwar auch eine Gastmeldung ist, aber zu einem Aktionär gehört (d.h. also einem
     * "parallelen" Gastsatz zu einem Aktionärssatz kann mit dieser Funktion nicht abgebildet werden - dies wäre
     * dann "zugangAktionaerAlsGast".
     * @param dbBundle
     */
    public void zugangGast(DbBundle dbBundle) {

        int todoPending = 0; /*=1 => Buchung wird nur als Pending durchgeführt*/

        try {
        willenserklaerungOhneSammelkarteSperren(dbBundle);

        this.lDbBundle = dbBundle;
        zugangGast_pruefe(dbBundle);
        if (this.rcIstZulaessig == false
                && (this.rcGrundFuerUnzulaessig != CaFehler.pmBereitsAnwesend || this.rcPending == false)) {
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
           return;
        }

        /*Meldung einlesen*/
        int erg;
        EclMeldung meldung = new EclMeldung();
        meldung.meldungsIdent = piMeldungsIdentGast;
        erg = dbBundle.dbMeldungen.leseZuMeldungsIdent(meldung);
        lDbBundle.dbMeldungen.meldungenArray[0].copyTo(meldung);
        erg = dbBundle.dbWillenserklaerung.leseZuMeldungGast(meldung);

        if (this.ptForceNonDelay == true) {
            /*Dann nur ganz stinknormales Buchen der Willenserklärung - keine Überprüfung und ähnliches!*/
            this.rcDelayedAufloesen = false;
            this.rcDelayed = false;
        }

        /*Was muß getan werden?
         * rcPending=true => Buchung nur als Pending-Buchung ausführen
         * rcDelayedAufloesen=true => Buchung als normal durchführen, aber Delayed-Buchungen aufloesen
         * rcDelayed=true => Buchung wird als delayed durcgeführt, keine Auflösung
         * */

        /*Erster Schritt: ggf. Bisher vorhandene Delayed-Willenserklärungen auflösen/"Durchbuchen";*/
        if (this.rcDelayedAufloesen == true) {
            int anzWillen = lDbBundle.dbWillenserklaerung.anzWillenserklaerungGefunden();
            if (anzWillen == 0) {
                /*Hier ist eigentlich was falsch - denn wenn delayed vorhanden, dann muß auch mindestens eine Willenserklärung da sein!*/
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                willenserklaerungOhneSammelkarteFreigeben(dbBundle);
                return;
            }
            int i;
            EclWillenserklaerung tempWillenserklaerung = null;
            BlWillenserklaerung blWillenserklaerung = null;
            for (i = 1; i <= anzWillen; i++) {
                tempWillenserklaerung = lDbBundle.dbWillenserklaerung.willenserklaerungGefunden(i - 1);
                if (tempWillenserklaerung.delayed == 1) {

                    blWillenserklaerung = new BlWillenserklaerung();
                    /***********Felder für jede Willenserklärung füllen*******/

                    /*Technik - Eingabeparameter für "Delayed-Nachbuchung" setzen*/
                    blWillenserklaerung.ptForceNonDelay = true;
                    blWillenserklaerung.ptVerweisart = 2; /*Delayed-Nachbuchung*/
                    blWillenserklaerung.ptVerweisAufWillenserklaerung = tempWillenserklaerung.willenserklaerungIdent;

                    /*Für Identifikation*/
                    blWillenserklaerung.ptForceIdentifikationIstMeldungsIdent = true;
                    blWillenserklaerung.ptUebernehmeIdentifikation = tempWillenserklaerung.identifikationDurch;

                    blWillenserklaerung.piEclMeldungAktionaer = null;
                    blWillenserklaerung.piMeldungsIdentAktionaer = tempWillenserklaerung.meldungsIdent;
                    blWillenserklaerung.piEclMeldungGast = null;
                    blWillenserklaerung.piMeldungsIdentGast = tempWillenserklaerung.meldungsIdentGast;

                    blWillenserklaerung.piZutrittsIdent.zutrittsIdent = tempWillenserklaerung.identifikationZutrittsIdent;
                    blWillenserklaerung.piZutrittsIdent.zutrittsIdentNeben = tempWillenserklaerung.identifikationZutrittsIdentNeben;
                    blWillenserklaerung.piKlasse = tempWillenserklaerung.identifikationKlasse;
                    blWillenserklaerung.piStimmkarte = tempWillenserklaerung.identifikationStimmkarte;
                    blWillenserklaerung.piStimmkarteSecond = tempWillenserklaerung.identifikationStimmkarteSecond;

                    blWillenserklaerung.pErteiltAufWeg = tempWillenserklaerung.erteiltAufWeg;

                    /*Felder für jede Willenserklärung füllen*/
                    switch (tempWillenserklaerung.willenserklaerung) {
                    case KonstWillenserklaerung.zugangGast: {
                        blWillenserklaerung.zugangGast(lDbBundle);
                        break;
                    }
                    case KonstWillenserklaerung.abgangGast: {
                        blWillenserklaerung.abgangGast(lDbBundle);
                        break;
                    }
                    default: {
                        /*Darf nicht vorkommen*/
                        this.rcIstZulaessig = false;
                        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;
                        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
                        return;
                    }
                    }
                    /*Willenserklaerung als "Delayed verarbeitet" kennzeichnen und zurückspeichern*/
                    tempWillenserklaerung.delayed = 2;
                    lDbBundle.dbWillenserklaerung.update(tempWillenserklaerung);
                }
            }
            /*meldung wurde durch obige Verbuchungen in Datenbank verändert - vor weiterer Verarbeitung
             * deshalb neu einlesen, sonst kommt "von anderem Benutzer verändert"
             */
            meldung = new EclMeldung();
            meldung.meldungsIdent = piMeldungsIdentGast;
            erg = dbBundle.dbMeldungen.leseZuMeldungsIdent(meldung);
            lDbBundle.dbMeldungen.meldungenArray[0].copyTo(meldung);
            
        }

        /***************Felder in Meldung updaten*****************************/
        /*statusPraesenz etal*/
        if (rcPending == false) {/*Wenn Pending-Buchen, dann hat diese keine Auswirkung auf den eigentlichen Status in Meldung*/
            if (meldung.delayedVorhanden == 1) { /*Meldung hat bereits delayed-Willenserklärungen, d.h. Ausgangsbasis
                                                 ist das delayed Präsenzkennzeichen*/
                if (rcDelayedAufloesen == false) {/*Delay-Status fortschreiben*/
                    meldung.delayedVorhanden = 1;
                    meldung.statusPraesenz_Delayed = 1;
                    meldung.statusWarPraesenz_Delayed = 1;
                } else { /*Bisherige Delayed Buchungen wurden bereits vorhin aufgelöst, und "normaler
                         Präsenzstatus entsprechend fortgeschrieben. Nun nur noch Delayed-Präsenzstatus zurücksetzen*/
                    meldung.delayedVorhanden = 0;
                    meldung.statusPraesenz_Delayed = 0;
                    meldung.statusWarPraesenz_Delayed = 0;
                    /*Nun noch die "normalen Präsenzstati" aktualisieren*/
                    meldung.statusPraesenz = 1;
                    meldung.statusWarPraesenz = 1;
                    if (!piZutrittsIdent.zutrittsIdent.isEmpty()) {
                        meldung.zutrittsIdent = piZutrittsIdent.zutrittsIdent;
                    }
                }
            } else {/*Meldung hat keine delayed-Willenserklärungen, d.h. Ausgangsbasis ist das normale Präsenzkennzeichen*/
                if (rcDelayed == true) { /*Diese Willenserklärung ist delayed, d.h. normales Präsenzkennzeichen bleibt unverändert*/
                    meldung.delayedVorhanden = 1;
                    meldung.statusPraesenz_Delayed = 1;
                    meldung.statusWarPraesenz_Delayed = 1;
                } else {/*Diese Willenserklärung ist auch nicht delayed, d.h. normales Präsenzkennzeichen wird verändert*/
                    meldung.statusPraesenz = 1;
                    meldung.statusWarPraesenz = 1;
                    if (!piZutrittsIdent.zutrittsIdent.isEmpty()) {
                        meldung.zutrittsIdent = piZutrittsIdent.zutrittsIdent;
                    }
                }
            }

        }

        /*Meldung speichern*/
        erg = dbBundle.dbMeldungen.update(meldung);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        /*dbWillenserklärung speichern*/
        prepareWillenserklaerung(KonstWillenserklaerung.zugangGast);
        EclWillenserklaerung willenserklaerung = preparedWillenserklaerung;
        EclWillenserklaerungZusatz willenserklaerungZusatz = preparedWillenserklaerungZusatz;
        if (this.rcPending) {
            willenserklaerung.pending = 1;
        }
        if (this.rcDelayed) {
            willenserklaerung.delayed = 1;
        }

        erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        this.rcIstZulaessig = true;
        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }

        return;

    }

    /** Abgang eines Gastes - nur mit einer "echten" Gastmeldung und einer "echten" GastzutrittsIdent möglich.
     *
     * Mögliche Identifikation:
     * > piEclMeldungGast eingelesen (mit Klasse 0)
     * > piMeldungsIdentGast
     * > piZutrittsIdent, piKlasse=0
     * > piZutrittsIdent
     *
     * Hinweis: ein Meldesatz, der zwar auch eine Gastmeldung ist, aber zu einem Aktionär gehört (d.h. also einem
     * "parallelen" Gastsatz zu einem Aktionärssatz kann mit dieser Funktion nicht abgebildet werden - dies wäre
     * dann "zugangAktionaerAlsGast".
     * @param dbBundle
     */
    public void abgangGast_pruefe(DbBundle dbBundle) {

        this.lDbBundle = dbBundle;

        this.evtlEinlesenMeldung();
        if (this.rcIstZulaessig == false) {
            return;
        }

        /*Nun prüfen, ob wirklich "ein echter Gast" eingelesen wurde.
         * Ist erforderlich für die Kombination "piZutrittsIdent ohne Klasse"*/
        if (piKlasse != 0) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmKeinGastGefunden;
            return;
        }

        /*Präsenzstatus ermitteln - falls bereits präsent kein Zugang möglich*/
        if (piEclMeldungGast.statusPraesenz != 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = CaFehler.pmNochNichtAnwesend;
            return;
        }
        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;

        return;
    }

    /** Abgang eines Gastes - nur mit einer "echten" Gastmeldung und einer "echten" GastzutrittsIdent möglich.
     *
     * Mögliche Identifikation:
     * > piEclMeldungGast eingelesen (mit Klasse 0)
     * > piMeldungsIdentGast
     * > piZutrittsIdent, piKlasse=0
     * > piZutrittsIdent
     *
     * Hinweis: ein Meldesatz, der zwar auch eine Gastmeldung ist, aber zu einem Aktionär gehört (d.h. also einem
     * "parallelen" Gastsatz zu einem Aktionärssatz kann mit dieser Funktion nicht abgebildet werden - dies wäre
     * dann "zugangAktionaerAlsGast".
     * @param dbBundle
     */
    public void abgangGast(DbBundle dbBundle) {

        try {
        willenserklaerungOhneSammelkarteSperren(dbBundle);

        this.lDbBundle = dbBundle;
        abgangGast_pruefe(dbBundle);
        if (this.rcIstZulaessig == false && this.rcGrundFuerUnzulaessig != CaFehler.pmNochNichtAnwesend) {
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
           return;
        }

        /*Meldung einlesen*/
        int erg;
        EclMeldung meldung = new EclMeldung();
        meldung.meldungsIdent = piMeldungsIdentGast;
        erg = dbBundle.dbMeldungen.leseZuMeldungsIdent(meldung);
        lDbBundle.dbMeldungen.meldungenArray[0].copyTo(meldung);
        if (erg < 1) {
            this.rcIstZulaessig = false;
            this.rcGrundFuerUnzulaessig = erg;
            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
            return;
        }

        if (this.rcGrundFuerUnzulaessig == CaFehler.pmNochNichtAnwesend) {
            if (0 == 1) {/*Prüfen, ob bereits delayed-Willenserklärungen für diese Meldung vorliegen*/
                /*TODO _Gast: abgangGast - wird überhaupt die ganze Funktion noch verwendet? Hier scheint irgendwas noch nicht fertig zu sein - siehe die ganzen folgenden Statements*/

                /*Falls ja => Prüfen, ob gemäß delayed-Willenserklärungen die abgegebene
                 * Willenserklärung doch passen würde
                 */

                /*Wenn ja, dann auch diese Willenserklärung als Delayed speichern*/
            }

            /*Wenn hier, dann:
             * 1) Es liegen entweder keine Delayed-Willenserklärungen für diese Meldung vor, oder
             * 2) Die aktuelle Willenserklärung paßt auch nicht gemäß Delayed-Historie
             */
            if (0 == 1) {/*Prüfen, ob Pending-Pool aktiv*/
            } else { /*Pending Pool ist auch nicht aktiv => Willenserklärung ist definitiv nicht zulässig*/
                return;
            }

        }

        if (0 == 1) {/*Willenserklärung ist aktuell delayed*/
        } else {
            /*Präsenzstatus aktualisieren - Präsenzkennzeichen und verwendete ZutrittsIdent (falls vorhanden!)
             * in meldung übertragen; warPraesent setzen*/
            meldung.statusPraesenz = 2;
            meldung.statusWarPraesenz = 1;
            if (!piZutrittsIdent.zutrittsIdent.isEmpty()) {
                meldung.zutrittsIdent = piZutrittsIdent.zutrittsIdent;
            }

            /*Meldung speichern*/
            erg = dbBundle.dbMeldungen.update(meldung);
            if (erg < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                willenserklaerungOhneSammelkarteFreigeben(dbBundle);
                return;
            }

            /*Willenserklärung speichern*/
            EclWillenserklaerung willenserklaerung = new EclWillenserklaerung();
            EclWillenserklaerungZusatz willenserklaerungZusatz = new EclWillenserklaerungZusatz();

            willenserklaerung.meldungsIdentGast = dbBundle.dbZutrittskarten.ergebnisPosition(0).meldungsIdentGast;
            willenserklaerung.willenserklaerung = KonstWillenserklaerung.abgangGast;
            willenserklaerung.identifikationDurch = this.identifikationDurch;
            willenserklaerung.zutrittsIdent = piZutrittsIdent.zutrittsIdent;
            willenserklaerung.zutrittsIdentNeben = piZutrittsIdent.zutrittsIdentNeben;
            erg = dbBundle.dbWillenserklaerung.insert(willenserklaerung, willenserklaerungZusatz);
            if (erg < 1) {
                this.rcIstZulaessig = false;
                this.rcGrundFuerUnzulaessig = erg;
                willenserklaerungOhneSammelkarteFreigeben(dbBundle);
                return;
            }
            this.rcWillenserklaerungIdent = willenserklaerung.willenserklaerungIdent;
        }

        this.rcIstZulaessig = true;
        this.rcGrundFuerUnzulaessig = CaFehler.pfdUnbekannterFehler;

        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
    } catch (Exception e) {
        abbruchBeiException(dbBundle, e);
    }
    }

//    public int zzugang(DbBundle dbBundle, EclMeldung meldung) {
//
//        try {
//        willenserklaerungOhneSammelkarteSperren(dbBundle);
//
//        /* meldung sichern für den Fall des Abbruchs!*/
//        EclMeldung meldungKopie = new EclMeldung();
//        meldung.copyTo(meldungKopie);
//
//        if (dbBundle.dbBasis == null) {
//            System.err.println("vmcdbBasis nicht initialisiert");
//        }
//        if (dbBundle.dbMeldungen == null) {
//            System.err.println("vmcdbMeldungen nicht initialisiert");
//        }
//        if (dbBundle.dbWillenserklaerung == null) {
//            System.err.println("vmcdbPraesenzveraenderung nicht initialisiert");
//        }
//
//        /*Prüfen, ob meldung noch gültig ist (oder stornierte Meldung oder deaktivierte
//         * ZutrittsIdent ist*/
//        if (EnMeldungAktiv.fromClMeldung(meldung) == EnMeldungAktiv.MeldungIstStorniert) {
//            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
//            return (CaFehler.pmMeldungIstStorniert);
//        }
//        if (EnMeldungAktiv.fromClMeldung(meldung) == EnMeldungAktiv.ZutrittsIdentIstStorniert) {
//            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
//            return (CaFehler.pmZutrittsIdentIstStorniert);
//        }
//
//        /*Prüfen, ob meldung bereits präsent*/
//        if (EnAzStatusPraesenz.fromClMeldung(meldung) == EnAzStatusPraesenz.Anwesend
//                || EnAzStatusPraesenz.fromClMeldung(meldung) == EnAzStatusPraesenz.AnwesendAlsGast) {
//            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
//            return (CaFehler.pmBereitsAnwesend); /* Ist bereits anwesend */
//        }
//
//        /*Gast oder Aktionär?*/
//        int gast = 0;
//        if (meldung.klasse == 1) {
//            gast = 1;
//        }
//
//        /*Erstzugang oder Wiederzugang?*/
//        int warpraesent = 0;
//        if (EnAzStatusPraesenz.fromClMeldung(meldung) == EnAzStatusPraesenz.WarAnwesend) {
//            warpraesent = 1;
//        } /*Wenn als Aktionär anwesend gewesen, dann gilt das auch für Gast*/
//        if (EnAzStatusPraesenz.fromClMeldung(meldung) == EnAzStatusPraesenz.WarAnwesendAlsGast && gast == 1) {
//            warpraesent = 1;
//        } /*Wenn als Gast anwesend gewesen, gilt das für Aktionär als Erstzugang*/
//
//        /*Zukünftiger Präsenzstatus in meldung*/
//        EnAzStatusPraesenz pStatus;
//        if (gast == 0) {
//            pStatus = EnAzStatusPraesenz.Anwesend;
//        } else {
//            pStatus = EnAzStatusPraesenz.AnwesendAlsGast;
//        }
//        meldung.statusPraesenz = EnAzStatusPraesenz.toClMeldung(pStatus);
//        meldung.statusWarPraesenzGast = EnAzStatusPraesenz.toClMeldung(EnAzStatusPraesenz.WarAnwesendAlsGast);
//
//        /*Veraenderung festlegen*/
//        int pVeraenderung;
//        if (warpraesent == 1) {
//            if (gast == 0) {
//                pVeraenderung = KonstWillenserklaerung.wiederzugang;
//            } else {
//                pVeraenderung = KonstWillenserklaerung.wiederzugangAlsGast;
//            }
//        } else {
//            if (gast == 0) {
//                pVeraenderung = KonstWillenserklaerung.wiederzugang;
//            } else {
//                pVeraenderung = KonstWillenserklaerung.wiederzugangAlsGast;
//            }
//        }
//
//        /* Meldung eintragen */
//        int erg = dbBundle.dbMeldungen.updateStatusPraesenz(meldung);
//        if (erg < 1) {
//            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
//            meldungKopie.copyTo(meldung);
//            return (erg);
//        }
//
//        /*in McdbPraesenzveraenderung eintragen*/
//        EclWillenserklaerung praesenzver = new EclWillenserklaerung();
//        EclWillenserklaerungZusatz praesenzverZusatz = new EclWillenserklaerungZusatz();
//        praesenzver.meldungsIdent = meldung.meldungsIdent;
//        praesenzver.willenserklaerung = pVeraenderung;
//
//        dbBundle.dbWillenserklaerung.insert(praesenzver, praesenzverZusatz);
//        rcWillenserklaerungIdent = praesenzver.willenserklaerungIdent;
//
//        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
//    } catch (Exception e) {
//        abbruchBeiException(dbBundle, e);
//    }
//
//        return (1);
//
//    }

//    public int aabgang(DbBundle dbBundle, EclMeldung meldung) {
//
//        try {
//        willenserklaerungOhneSammelkarteSperren(dbBundle);
//
//        /* meldung sichern für den Fall des Abbruchs!*/
//        EclMeldung meldungKopie = new EclMeldung();
//        meldung.copyTo(meldungKopie);
//
//        if (dbBundle.dbBasis == null) {
//            System.err.println("vmcdbBasis nicht initialisiert");
//        }
//        if (dbBundle.dbMeldungen == null) {
//            System.err.println("vmcdbMeldungen nicht initialisiert");
//        }
//        if (dbBundle.dbWillenserklaerung == null) {
//            System.err.println("vmcdbPraesenzveraenderung nicht initialisiert");
//        }
//
//        /*Prüfen, ob meldung noch gültig ist (oder stornierte Meldung oder deaktivierte
//         * ZutrittsIdent ist*/
//        if (EnMeldungAktiv.fromClMeldung(meldung) == EnMeldungAktiv.MeldungIstStorniert) {
//            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
//            return (CaFehler.pmMeldungIstStorniert);
//        }
//        if (EnMeldungAktiv.fromClMeldung(meldung) == EnMeldungAktiv.ZutrittsIdentIstStorniert) {
//            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
//            return (CaFehler.pmZutrittsIdentIstStorniert);
//        }
//
//        /*Prüfen, ob meldung noch nicht anwesend*/
//        if (EnAzStatusPraesenz.fromClMeldung(meldung) != EnAzStatusPraesenz.Anwesend
//                && EnAzStatusPraesenz.fromClMeldung(meldung) != EnAzStatusPraesenz.AnwesendAlsGast) {
//            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
//            return (CaFehler.pmNochNichtAnwesend); /* Ist nicht anwesend */
//        }
//
//        /*Gast oder Aktionär?*/
//        int gast = 0;
//        if (meldung.klasse == 1) {
//            gast = 1;
//        }
//
//        /*Zukünftiger Präsenzstatus in meldung*/
//        EnAzStatusPraesenz pStatus;
//        if (gast == 0) {
//            pStatus = EnAzStatusPraesenz.WarAnwesend;
//        } else {
//            pStatus = EnAzStatusPraesenz.WarAnwesendAlsGast;
//        }
//        meldung.statusPraesenz = EnAzStatusPraesenz.toClMeldung(pStatus);
//        meldung.statusWarPraesenzGast = EnAzStatusPraesenz.toClMeldung(EnAzStatusPraesenz.WarAnwesendAlsGast);
//
//        /*Veraenderung festlegen*/
//        int pVeraenderung;
//        if (gast == 0) {
//            pVeraenderung = KonstWillenserklaerung.abgang;
//        } else {
//            pVeraenderung = KonstWillenserklaerung.abgangAlsGast;
//        }
//
//        /* Meldung eintragen */
//        int erg = dbBundle.dbMeldungen.updateStatusPraesenz(meldung);
//        if (erg < 1) {
//            willenserklaerungOhneSammelkarteFreigeben(dbBundle);
//            meldungKopie.copyTo(meldung);
//            return (erg);
//        }
//
//        /*in McdbPraesenzveraenderung eintragen*/
//        EclWillenserklaerung praesenzver = new EclWillenserklaerung();
//        EclWillenserklaerungZusatz praesenzverZusatz = new EclWillenserklaerungZusatz();
//        praesenzver.meldungsIdent = meldung.meldungsIdent;
//        praesenzver.willenserklaerung = pVeraenderung;
//
//        dbBundle.dbWillenserklaerung.insert(praesenzver, praesenzverZusatz);
//        rcWillenserklaerungIdent = praesenzver.willenserklaerungIdent;
//
//        willenserklaerungOhneSammelkarteFreigeben(dbBundle);
//    } catch (Exception e) {
//        abbruchBeiException(dbBundle, e);
//    }
//
//        return (1);
//
//    }

    /**Speicher - zur zukünftigen Verwendung**/

    //	/**Einlesen über tbl_willenserklaerung aller Vollmachten an Dritte (auch die Widerrufenen!).
    //	 * Ein Entity aus tbl_vertreter kann dabei mehrfach gefunden werden - z.B. einmal für Erteilung einer
    //	 * Vollmacht an Dritte, einmal für den Widerruf. Theoretisch auch wenn mehrfach an den selben erteilt wurde*/
    //	public int leseZuMeldung(EclMeldung meldung) {
    //		int anzInArray=0;
    //		try {
    //
    //			String sql="SELECT ve.*, wi.willenserklaerungIdent, ve from tbl_vertreter ve "
    //					+ "INNER JOIN tbl_willenserklaerung wi on ve.ident=wi.bevollmaechtigterDritterIdent"
    //					+ "where "+
    //					"wi.mandant=? AND "+
    //					"wi.meldungsIdent=? ORDER BY wi.willenserklaerungIdent;";
    //
    //			PreparedStatement pstm1=verbindung.prepareStatement(sql,ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
    //			pstm1.setInt(1, ClGlobalVar.mandant);
    //			pstm1.setInt(2, meldung.meldungsIdent);
    //
    //			ResultSet ergebnis=pstm1.executeQuery();
    //			ergebnis.last();
    //			anzInArray=ergebnis.getRow();
    //			ergebnis.beforeFirst();
    //
    //			willenserklaerungArray=new EclWillenserklaerung[anzInArray];
    //
    //			int i=0;
    //			while (ergebnis.next()==true){
    //
    //				willenserklaerungArray[i]=this.decodeErgebnis(ergebnis);
    //				i++;
    //
    //			}
    //
    //		} catch (Exception e){
    //			CaBug.drucke("leseZuMeldung.leseZuMeldung 001");
    //			System.err.println(" "+e.getMessage());
    //		}
    //
    //
    //		return (anzInArray);
    //
    //
    //	}

}
