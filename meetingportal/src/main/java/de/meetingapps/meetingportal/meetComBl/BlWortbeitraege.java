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


/*Diese Klasse ist erst mal eine "Dummy-Klasse", um das Konzept aufzunehmen :-)
 * 
 * Wortmeldung
 * ===========
 * > Überprüfen, was alles mit eingegeben werden soll. Z.B.: "Wortmeldeantrag enthält Antrag / Widerspruch o.ä."
 * > Kontaktdaten, z.B. Telefon, für den Fall dass Schwierigkeiten beim Test
 * > Beim Eingehen einer Wortmeldung: "Push" an User-Gruppe "Wortmeldetisch"
 * 
 * "Wortmeldetisch" (im uLogin):
 * =============================
 * > Grundsätzlich Usability überarbeiten
 * > Neue Funktionalitäten:
 * >> Reihenfolge der Wortmeldungen veränderbar
 * >> Statusse konfigurierbar
 * 
 * > Update-Nachricht bei Eingang einer neuen Wortmeldung (siehe Push-Meldung beim Wortmeldetisch)
 * > Bei Statusveränderungen Push an "Wortmeldeverarbeitung"
 * 
 * "Status-Anzeige" Versammlungsleitung (gehört zur Gruppe "Wortmeldeverarbeitung"
 * ===============================================================================
 * > automatischer Update
 * > Evtl. Anzeige aufwerten, konfigurierbar machen 
 * >> was soll dort alles angezeigt werden?
 * >> evtl. verschiedene Status-Anzeigen - Versammlungsleitung, Regie, ...
 * 
 * "Ablauf Redetest"
 * =================
 * > Grundsätzliche Fragen / Herausforderungen:
 * >> wie erfährt Aktionär, dass er zum Testen aufgefordert wird?
 * >> Parallel zum Test ist kein Videoschauen zulässig. Das muß auch technisch abgeschaltet werden. Evtl. am Generalprobentest einen Testablauf für Redner anbieten?
 * 
 * Ablauf damit:
 * > Push an Teilnehmer "Bitte kommen Sie zum Redetest. Ihr Videostream wird parallel abgeschaltet, wenn sie auf weiter klicken".
 * > Timer - wenn Teilnehmer innerhalb von 1 Minute nicht geantwortet, dann "auf Wiederholung setzen"
 * > Wenn Teilnehmer auf ok clickt:
 * >> Deaktivierung Stream
 * >> Redetest
 * > Wenn Redetest beendet: Stream wieder starten.
 * 
 * "Ablauf Rededurchführung"
 * =========================
 * > Vorankündigungs-Push (Bestätigung der Bereitschaft)
 * > Bei Redestart analog "Ablauf Redetest"
 * >> Abweichungen: automatischer Status-Update "Redet / Redebereit"
 * 
 * Grundsätzliche Fragen
 * =====================
 * > Mehrere Konferenz-Räume, um parallel Tests durchzuführen, bzw. parallel Redner "vorzuhalten" und dann live in den Stream zuzuschalten
 * > neuer Status bei Teilnehmer: Redetest erfolgreich
 * > Ersatzmechanismus ohne Websockets
 * > Versatz: Achtung, keine Versammlungsleitende Maßnahmen kurz vor Redner!
 * > Konferenzraum für alle Redner einer Wortmelderunde. Vor einer Wortmelderunde 2 Minuten Pause wegen Versatz. Oder 2 Minuten Warten vor jedem Redner
 * 
 * 
 * 
 */


public class BlWortbeitraege {

}
