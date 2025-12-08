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
package de.meetingapps.meetingportal.meetComEntities;

import java.io.Serializable;

/**Beschreibung der Service-Desk-Sets*/
public class EclServiceDeskSet  implements Serializable {
    private static final long serialVersionUID = -8608780041805172851L;

    public int ident=0;
    
    /**LEN=200*/
    public String kurzBeschreibung="";
    
    /**LEN=10000*/
    public String langBeschreibung="";
    

    /**Hinweis zu den Berechtigungen:
     * 
     * Ein Set enthält alle Workflow-Elemente für Bankenschalter und Serviceschalter.
     * 
     * Für jede dieser beiden "Schalter" gibt es 2 Berechtigungsstufen:
     * > Standard - Standardfälle, z.B. auch über Hilfspersonal abdeckbar
     * > Erweitert - Alle Fälle, auch Sonderfälle, i.d.R. für Fachpersonal
     * 
     * Erweitert enthält dabei automatisch auch alle Element der Berechtigung Standard.
     * 
     * Die Berechtigungen beziehen sich dabei auf das Ausführen von Aktionen (Das Suchergebnis
     * kann ja mit jeder Berechtigung entstehen ...).
     * 
     * Sind mit einer anderen Berechtigung weitere Aktionen vorhanden, erscheint der Hinweis:
     * "Falls nicht ausreichend, bitte Rücksprache" - falls höhere Berechtigung am Servicedesk vorhanden
     * "Falls nicht ausreichend, bitte Bankenschalter" - falls mit Servicedesk gearbeitet wird, aber
     * höhere Berechtigung für Bankenschalter.
     * "Falls nicht ausreichend, bitte Servicedesk" - Bankenschalter => Servicedesk.
     * 
     * Grundsätzlich dürfen einer Kennung auch alle Berechtigungen zugeordnet werden - damit ergibt
     * sich dann der allumfassende Schalter für den Consultant :-). 
     */
}
