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
package de.meetingapps.meetingportal.meetComBlManaged;

/**User-Management für tLogin.
 * 
 * Verarbeitung von gleichzeitigem Login, sowie Zu-/Abgang bei Online-Teilnahme
 * ============================================================================
 * 
 * 1.a) Beim Aufruf der Portal-Seite aus neuer Browser-Session heraus, ohne Mandanten-Parameter:
 * > KonstPortalView.fehlerLinkAufruf (=0, also standardmäßig gesetzt)
 * 1.b) Beim Aufruf der Portal-Seite aus bestehender Browser-Session heraus, ohne Mandanten-Parameter:
 * > Es wird  die bereits existierende JSF-Session übernommen.
 * 		Aber: SocketSession wird dadurch getrennt und neu aufgebaut, d.h. ein bereits eingeloggter User wird "disabled".
 * 		D.h. auch die Session-ID des Users muß disabled werden. D.h. beim nächsten Button-Druck -> Fehlerseite
 * 1.c) Beim Aufruf der Portal-Seite mit korrektem, vollständigen Link mit funktionierendem Mandanten:
 * > Mandanten-Nummer (HV-Jahr) etc. wird gesetzt (TSessionVerwaltung.setMandant)
 * 		> Falls Mandant aktiv, dann:
 * 			> Daten für Login-Maske "clearen"
 * 			> KonstPortalView.login
 * 		> Falls Mandant nicht aktiv, dann
 * 			> KonstPortalView.fehlerPortalNichtAktiv
 * > JSF Session-ID wird in Sockets eingetragen (beim Initialisieren der Socket-Verbindung)
 * 		> Zuordnungen socketToJsf, jsfToSocket
 * 		> Falls jsfToSocket bereits eine Verbindung enthält (Öffnen des Portals in zwei Browser-Tabs!), 
 * 			dann Session Sperren  (aber User nicht austragen! Erfolgt erst beim Logout noch benötigt!) 
 * 
 * 2.) Login
 * Prüfen, ob bereits gültiger Login vorliegt (eingetragene Servernummer !=0).
 * Wenn ja:
 * > Login für diesen Server temporär sperren (Servernummer=99990)
 * > Wenn bei gleichem Server, dann User aus der Session austragen (Hinweis: kann nur andere JSF-Session haben, da bei selbser JSF-Session
 * 				beide Socket-Sessions schon gesperrt werden und deshalb neuer Login gar nicht mehr möglich ist).
 * 				Ist möglicherweise schon ausgetragen worden, wenn durch Socket-Trennung deaktiviert!
 * > Ggf. Präsenz-Abmeldungen durchführen
 * > Login wieder freigeben,l dabei aktuellen Server eintragen
 * (Hinweis: Server-Nummer-Update immer über Spezialfunktion, die nur dann funktioniert wenn nicht gerade von anderem verändert - Fehler abfangen, Login derzeit nicht möglich
 * wg. Aktion von anderem Arbeitsplatz aus, in einigen Minuten wieder probieren)
 * 
 * 
 * 101) Logout (auch in doBrowserSchliessen)
 * > Nur durchführen, wenn Session noch aktiv ist. D.h.:
 * 		> ob Session gesperrt ist, ist irrelevant
 * 		> Server-Nummer muß noch gleich sein
 * 		> User muß noch unter dieser HTTP-Session eingetragen werden.
 * > Achtung: auch wenn ServerNummer nicht gleich ist, muß der User ggf. aus der HTTP-Session ausgetragen werden - falls der Eintrag noch existiert, der User aber mittlerweile
 * 		auf einem anderen Server eingeloggt wurde. In diesem Fall darf aber natürlich kein Präsenz-Abgang gebucht werden!
 * 102) Trennen einer Web-Socket-Verbindung
 * > JSF Session ermitteln
 * > Anzahl JSF Sessions verringern
 * > Wenn Anzahl JSF-Sessions =0, dann JSF-Session komplett austragen (d.h. erst dann wird die Sperre dieser JSF-Session, die
 * 		ggf. durch Doppelt-Aufruf in zwei Browser-Tabs entstanden ist, wieder aufgehoben)
 * > Achtung: "User wird aus Socket-Liste ausgetragen" - darf hier NICHT stattfinden - da anschließend noch doBrowserSchliessen aufgerufen wird und dort der User nur ausgeloggt wird,
 * 		wenn User noch aktiv ist.
 * 
 * 
 * Prüfen bei jedem Buttondruck:
 * > Ist die JSF-Session gesperrt? (immer! Da sonst möglicherweise durch eine zweite Browser-Tabb ein Parametersatz
 * 		eines anderen Mandanten geladen wurde, und deshalb Funktionen (anderer Mandant wird ja noch angezeig!) aufgerufen werden können 
 * 		die für diesen neuen Mandanten gar nicht zulässig wären
 * > Ist der bei der User-Kennung gespeicherte Server identisch mit dem aktuellen Server? (braucht nur gemacht zu werden, wenn
 * 		der Button-Druck einen Datenzugriff auslöst) (wenn nein, dann ist Login auf anderem Server erfolgt)
 * > Ist der zu dieser JSF-Session gehörende User noch aktiv? (wenn nein, dann ist neuer Login in neuer Session erfolgt; muß immer neue Session sein,
 * 		da sonst Parallel-Session gewesen wäre, und dann sowieso die Session gesperrt worden wäre)
 *
 *
 * Sperren
 * =======
 * Über letzterLoginAufServer=100000 wird eine Loginkennung temporär gesperrt (und später wieder freigegeben.
 * zeitstempel regelt, wann die Sperre automatisch verfällt.
 * Sicherstellen, wenn parallel Login-Versuch gemacht wird:
 * 		Update immer auf bisherige letzterLoginAufServer / zeitstempel - dadurch quasi unmöglich dass zwei Updates parallel durchgehen.
 *
 * Spezialfälle
 * ============
 * User ist eingeloggt. Im selben Browser-Tabb wird dann Link neu aufgerufen, ohne dass User aktiv ausgeloggt wird
 * ---------------------------------------------------------------------------------------------------------------
 * Es kommt "Seite verlassen?". Wenn ja, dann wird alter User ausgeloggt. Alles gut.
 * 
 * Serverneustart
 * --------------
 * a) Präsenzstatus: Es bleiben alle präsent. Ausloggen/Einloggen erfolgt erst, wenn sich User neu anmeldet.
 * b) Sperrkennzeichen in tbl (Server=9999): wird gelöst durch zusätzlichen Timestamp - Sperrkennzeichen verliert Wirkung nach x Minuten (derzeit: 5 Minuten).
 * 
 * "Kabel wird getrennt"
 * ---------------------
 * a)
 * Dabei bleiben alle Login-Daten (einschließlich Präsenz) erhalten.
 * Beim nächsten Login dann gelöscht.
 * b) 
 * Falls gerade beim User das Sperrkennzeichen in der Tbl gesetzt, (was eigentlich nicht passieren kann, da ja Serverprozess auch bei getrenntem Kabel weiterläuft!),
 * dann ist erneuter Login nach Zeit-Ablauf des Sperrkennzeichens möglich. 
 */
public class BlMUserManagement {

}
