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
package de.meetingapps.meetingportal.meetComHVParam;

/********************Grundsätzlicher Parameterablauf*********************************************
 * 
 * 
 * Mandantennummer und Parameter - JSF
 * ===================================
 * 
 * Parameter sind:
 * Session (werden beim eclDbM.openall gefüllt):
 * > EclParamM (aktuell, zu erweitern). Bündel für:
 * 		ClGlobalVar
 * 		HVParam (einschließlich ClParameter - alt - ClParameter nicht mehr verwenden)
 * 		ParamServer
 * 		EclGeraeteSet
 * 		EclGerateSetKlasseSetZuordnung
 * 		ParamGeraet
 * 		EclEmittenten
 * > EclFehlerM
 * 
 * Nicht mehr verwendet / gefüllt werden:
 * > EclParameterM (historisch, einschließlich lfd)
 * > EclGlobalVarM (enthält insbesondere mandant!)
 * 
 * Initialisierung - Aktionärsportal und Aktionärsapp
 * --------------------------------------------------
 * Mandant wird über Parameter im Aufruf-Link an das Programm übergeben, z.B.:
 * http://127.0.0.1:8080/betterport/M054/aLogin.xhtml?mandant=054&sprache=DE&test=1
 * 
 * Auf der ersten (Start)JSF-Maske wird mandant an XControllerAllgemein übergeben:
 * 		<f:metadata>
 *			<f:viewParam name="mandant" value="#{xControllerAllgemein.mandant}" />
 *			<f:viewParam name="sprache" value="#{aLanguage.sprache}" />
 *			<f:viewParam name="test" value="#{aDlgVariablen.test}" />
 *		</f:metadata>
 * 
 * Im Setter von XControllerAllgemein.mandant wird:
 * > mandant an eclParamM.ClGlobalVar.mandant übergeben
 * > Über EclDbM.openall ggf. die erforderlichen Puffer gefüllt (ohne Mandant)
 * > Überprüft, ob für diesen Mandant ein Portal aktiv ist.
 * > Jahr, HV-Nummer, Datenbereich aus dem aktuell aktiven "Mandanten-Jahr" belegt.
 * > Über EclDBM.openall (nochmals) die erforderlichen Puffer gefüllt (mit Mandant) und damit auch
 * 		die obigen Session-Beans der Parameter gefüllt, außerdem die Parameter in EclDbM.dbBundle für diese
 * 		Session eingetragen.
 * 
 * 
 * 
 * 
 * "Tiefere" Schichten
 * -------------------
 * Damit die Parameter und globalen Variablen auch in tieferer Business-Logik (ohne Injects ...) zur
 * Verfügung stehen, werden
 * in EclDbM beim openall die erforderlichen Daten aus dem Puffer geholt und an DbBundle gehängt - über dieses DbBundle können dann auch andere Klassen
 * auf diesen Satz zugreifen.
 * 
 * 
 * 
 * 
 * Mandantennummer und Parameter - JavaFx Clients (und Business-Logik)
 * ===================================================================
 * Parameter werden dauerhaft gespeichert in Static-Klassen:
 * > ParamS (analog zu EclParamM, enthält auch clGlobalVar)
 * > FehlerS (analog zu EclFehlerM)
 * 
 * Innerhalb der JavaFX-Oberflächen kann auf diese direkt zugegriffen werden, allerdings
 * keinesfalls in "tieferen Businesslogiken!" (static in EE-Komponenten geht schief!!!)
 * 
 * Zur Verwendung in tieferen Businesslogiken: 
 * Hierzu wird ein DbBundle erzeugt, und als Parameter an die tieferen Businesslogiken übergeben.
 * Erzeugen genügt! DbBundle lDbBundle=new DbBundle();
 * Open/Close ist nicht erforderlich!
 * Beim Erzeugen werden die Statik-Klassen in lDbBundle übernommen, und stehen (über die Parameterübergabe
 * von lDbBundle) so auch in tieferen Schichten zur Verfügung.
 * 
 * Initialisieren
 * --------------
 * Für EE-Modul: Erfolgt in EclDbM und BlmFuellePuffer/BlmPuffer
 * Sonst: 
 * > Erfolgt in DbBundle bzw. in BlFuellePufferAusDBAufClient (in meetComBl)
 * 
 * 
 * 
 * Tables, die inhaltlich in leere Datenbank eingespielt werden müssen, damit Programm
 * überhaupt korrekt startet:
 * (in ExpGlobalStandard - Export enthalten)
 * 
 * tbl_berechtigungentexte
 * 
 * tbl_parameterserver
 * tbl_geraeteset
 * tbl_geraeteklasse
 * tbl_geraeteParameter
 * tbl_geraetklassesetzuordnung
 * 
 * tbl_userlogin
 * > einschließlich User 9999 und 9998 (manuell eingetragen) für Portal- und App-Aktionärsuser
 */

public class Dok_GlobalVar_Parameter {

}
