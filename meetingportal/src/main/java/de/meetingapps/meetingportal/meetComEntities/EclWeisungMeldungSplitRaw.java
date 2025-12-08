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

public class EclWeisungMeldungSplitRaw implements Serializable {
    private static final long serialVersionUID = -5703301261156694943L;

    /**Mandant**/
    public int mandant = 0;

    /**Ident dieser Weisung - Verbindung zu EclWeisungAktionaer!**/
    public int weisungIdent = 0;

    /**Ident der Meldung, zu der die Weisung gehört**/
    public int meldungsIdent = 0;

    /**Ident der Sammelkarte, der die Meldung zugeordnet ist**/
    public int sammelIdent = 0;

    /**Ident der Willenserklärung, die diese Zuordnung ausgelöst hat**/
    public int willenserklaerungIdent = 0;

    /**Einzelne Weisungsabgaben - wie markiert; Länge jeweils 8 Byte long, für 10 Abstimmungseingaben**/
    public long[][] abgabe = new long[200][10];

}
