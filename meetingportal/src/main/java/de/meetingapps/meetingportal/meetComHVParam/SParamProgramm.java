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

public class SParamProgramm {



    static public final String programmVersion = "2025.MASTER.05.04";

   
    /**ZUr Handhabung: üblicherweise sind programmVersionServerAufClient und programmVersionClientAufServer leer, d.h.
     * wenn Client und Server-Version sind gleich, dann paßsts, sonst nicht.
     * 
     * Variante 1: Server wird upgedatet. Client soll weiterarbeiten können.
     * In diesem Fall Wird die Server-Version hochgezählt, und in programmVersionClientAufServer
     * wird noch die alte Programmversion (=noch jetztige Client-Version) eingetragen.
     * 
     * Variante 2: Client wird upgedatet. Server soll weiterarbeiten können.
     * In diesem Fall Wird die Client-Version hochgezählt, und in programmVersionServerAufClient
     * wird noch die alte Programmversion (=noch jetztige Server-Version) eingetragen.

     */
    
    /**Alle die _anderen_Versionen des Servers, mit denen der Client zusammenarbeiten kann. D.h. der Client
     * arbeitet mit diesen -zusätzlichen- Serverfunktionen zusammen*/
    static public final String[] programmVersionServerAufClient={  /*"2023.MASTER.08.14", "2023.MASTER.07.02" /*"2023.MASTER.07.02"*/};
 
    /**Alle die _anderen_Versionen des Clients, mit denen der Server zusammenarbeiten kann. D.h. der Server arbeit
     * mit diesen - zusätzlichen - Clientfunktionen zusammen
     */
    static public final String[] programmVersionClientAufServer={ /*"2023.MASTER.08.14"/*, "2022.MASTER.07.06" */};

    static public final int anzahlGattungen = 5;
    static public final int anzahlMaxIdentInAppIdent = 30;

    static public final String db_passwort = "todo";
    static public final String db_kennung = "todo";
    
    
    /***********************Für Source-Code-Parameter**********************/
    
    /**sourcePfad - wird den packages vorangestellt. Endet mit / 
     * SParamProgramm.sourcePfad
     * */
    static public final String sourcePfad="/de/meetingapps/";
    
    
}
