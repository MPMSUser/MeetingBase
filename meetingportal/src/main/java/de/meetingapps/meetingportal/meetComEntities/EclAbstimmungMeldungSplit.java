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

import de.meetingapps.meetingportal.meetComKonst.KonstStimmart;

public class EclAbstimmungMeldungSplit {
    /**Mandant**/
    public int mandant = 0;

    /**Ident der Meldung, zu der die Weisung gehört**/
    public int meldungsIdent = 0;

    /**1 bis 5. Wird aus dem Meldungssatz übernommen, um bei der Abstimmungsauswertung schneller addieren zu können*/
    /*XXX In Datenbank noch einbauen!, dann Default auf 0 setzen*/
    public int gattung = 1;

    /**Einzelne Weisungsabgaben - wie markiert; Länge jeweils 8 Byte long, für 10 Abstimmungseingaben**/
    public long[][] abgabe = new long[200][10];

    public EclAbstimmungMeldungSplit() {
        int i, i1;
        for (i = 0; i < 200; i++) {
            for (i1 = 0; i1 < 10; i1++) {
                abgabe[i][i1] = 0;
            }
        }
    }

    public boolean pruefeObNurStimmausschluss(int pPos) {
        boolean stimmausschlussgefunden=false;
        boolean sonstigeWeisunggefunden=false;
        for (int i1=0;i1<10;i1++){
            if (abgabe[pPos][i1]!=0) {
                if (i1==KonstStimmart.stimmausschluss) {
                    stimmausschlussgefunden=true;
                }
                else {
                    sonstigeWeisunggefunden=true;
                }
            }
            
        }
        if (stimmausschlussgefunden && sonstigeWeisunggefunden==false) {
            return true;
        }
        return false;
    }
    
    
}
