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

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEntities.EclAbstimmung;

/**In dieser Klasse werden Funktionen gebündelt, die zum Bearbeiten / Neueinfügen von Abstimmungen erforderlich sind*/

public class BlAbstimmungsListeBearbeiten {

    private int logDrucken=10;
    
    /**Wird beim Initialisieren der Klasse als "Arbeitsliste" übergeben*/
    private EclAbstimmung[] listeDerAbstimmungen = null;

    public BlAbstimmungsListeBearbeiten(EclAbstimmung[] pListeDerAbstimmungen) {
        listeDerAbstimmungen = pListeDerAbstimmungen;
    }

    /**Prüft, ob noch ein IdentWeisungssatz verfügbar ist; wenn nein, dann -1, ansonsten
     * Rückgabe des freien Weisungssatzes
     */
    public int liefereFreieIdentWeisungssatz() {
        int freieIdent = -1;
        int freieIdentWeisungssatz[] = new int[200];
        for (int i = 0; i < 200; i++) {
            freieIdentWeisungssatz[i] = -1;
        }
        for (int i = 0; i < listeDerAbstimmungen.length; i++) {
            if (listeDerAbstimmungen[i].identWeisungssatz != -1) {
                freieIdentWeisungssatz[listeDerAbstimmungen[i].identWeisungssatz] = 1;
            }
        }
        for (int i = 1; i < 200; i++) {
            if (freieIdent == -1 && freieIdentWeisungssatz[i] == -1) {
                freieIdent = i;
            }
        }
        return freieIdent;
    }

    /**Liefert die höchste vergebene Position +1 - sprich, zum Anhängen ans Ende*/
    public int liefere_AnzeigePositionIntern_AmEnde() {
        return lieferePositionAmEnde(1);
    }

    /**Liefert die höchste vergebene Position +1 - sprich, zum Anhängen ans Ende*/
    public int liefere_AnzeigePositionExternWeisungen_AmEnde() {
        return lieferePositionAmEnde(2);
    }

    /**Liefert die höchste vergebene Position +1 - sprich, zum Anhängen ans Ende*/
    public int liefere_AnzeigePositionExternWeisungenHV_AmEnde() {
        return lieferePositionAmEnde(8);
    }

    /**Sub-FUnktion für oben, PositionIntern oder etc. abhängig von pAktuelleFunktion*/
    private int lieferePositionAmEnde(int pAktuelleFunktion) {
        int hoechsteGef = 0;
        if (listeDerAbstimmungen == null) {
            return 1;
        }
        int anz = listeDerAbstimmungen.length;
        for (int i = 0; i < anz; i++) {
            switch (pAktuelleFunktion) {
            case 1:
                if (listeDerAbstimmungen[i].anzeigePositionIntern >= hoechsteGef) {
                    hoechsteGef = listeDerAbstimmungen[i].anzeigePositionIntern;
                }
                break;
            case 2:
                if (listeDerAbstimmungen[i].anzeigePositionExternWeisungen >= hoechsteGef) {
                    hoechsteGef = listeDerAbstimmungen[i].anzeigePositionExternWeisungen;
                }
                break;
            //    		case 3:
            //        		if (angezeigteAbstimmungZuAbstimmungsblock[i].position>=neueIdent){
            //        			angezeigteAbstimmungZuAbstimmungsblock[i].position++;
            //        			abstimmungWurdeVeraendert[i]=true;
            //        		}
            //    			break;
            //    		case 4:
            //        		if (angezeigteAbstimmungZuAbstimmungsblock[i].positionAufStimmkarte>=neueIdent){
            //        			angezeigteAbstimmungZuAbstimmungsblock[i].positionAufStimmkarte++;
            //        			abstimmungWurdeVeraendert[i]=true;
            //        		}
            //    			break;
            //       		case 5:
            //        		if (angezeigteAbstimmungZuStimmkarte[i].positionInStimmkarte>=neueIdent){
            //        			angezeigteAbstimmungZuStimmkarte[i].positionInStimmkarte++;
            //        			abstimmungWurdeVeraendert[i]=true;
            //        		}
            //    			break;
            //    		case 7:
            //        		if (angezeigteAbstimmungZuAbstimmungsblock[i].positionInAusdruck>=neueIdent){
            //        			angezeigteAbstimmungZuAbstimmungsblock[i].positionInAusdruck++;
            //        			abstimmungWurdeVeraendert[i]=true;
            //        		}
            //    			break;
            case 8:
                if (listeDerAbstimmungen[i].anzeigePositionExternWeisungenHV >= hoechsteGef) {
                    hoechsteGef = listeDerAbstimmungen[i].anzeigePositionExternWeisungenHV;
                }
                break;
            }
        }
        return hoechsteGef + 1;
    }

    /*************Einfügen einer Abstimmung an bestimmter Position innerhalb der Weisungssortierung*************************/
    /**pFunktion:
     * =1 anzeigePositionIntern
     * =2 anzeigePositionExternWeisungen
     * =8 anzeigePositionExternWeisungenHV
     */
    public int verschiebenPositionAbstimmungen(EclAbstimmung pZuVerschiebendeAbstimmung, int pZielPos, int pFunktion,
            boolean[] abstimmungWurdeVeraendert) {
        einfuegenNeueIdentAbstimmungen(pZielPos, pFunktion, abstimmungWurdeVeraendert);

        int altePos = 0;

        switch (pFunktion) {
        case 1:
            altePos = pZuVerschiebendeAbstimmung.anzeigePositionIntern;
            pZuVerschiebendeAbstimmung.anzeigePositionIntern = pZielPos;
            break;
        case 2:
            altePos = pZuVerschiebendeAbstimmung.anzeigePositionExternWeisungen;
            pZuVerschiebendeAbstimmung.anzeigePositionExternWeisungen = pZielPos;
            break;
        case 8:
            altePos = pZuVerschiebendeAbstimmung.anzeigePositionExternWeisungenHV;
            pZuVerschiebendeAbstimmung.anzeigePositionExternWeisungenHV = pZielPos;
            break;
        }

        loescheAlteIdentAbstimmungen(altePos, pFunktion, abstimmungWurdeVeraendert);

        return 1;
    }

    /**Die neue Position neueIdent wird eingefügt - alle >= dieser Position 
     * werden nach hinten verschoben
     */
    private void einfuegenNeueIdentAbstimmungen(int neueIdent, int pFunktion, boolean[] abstimmungWurdeVeraendert) {
        CaBug.druckeLog("neueIdent="+neueIdent, logDrucken, 10);
        CaBug.druckeLog("pFunktion="+pFunktion, logDrucken, 10);
       
        int anz = listeDerAbstimmungen.length;
        CaBug.druckeLog("anz="+anz, logDrucken, 10);
        
        for (int i = 0; i < anz; i++) {
            switch (pFunktion) {
            case 1:
                CaBug.druckeLog("listeDerAbstimmungen[i].anzeigePositionIntern="+listeDerAbstimmungen[i].anzeigePositionIntern, logDrucken, 10);
                if (listeDerAbstimmungen[i].anzeigePositionIntern >= neueIdent) {
                    listeDerAbstimmungen[i].anzeigePositionIntern++;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            case 2:
                if (listeDerAbstimmungen[i].anzeigePositionExternWeisungen >= neueIdent) {
                    listeDerAbstimmungen[i].anzeigePositionExternWeisungen++;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            case 8:
                if (listeDerAbstimmungen[i].anzeigePositionExternWeisungenHV >= neueIdent) {
                    listeDerAbstimmungen[i].anzeigePositionExternWeisungenHV++;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            }
        }

    }

    /**Löschen der alten Position - aber nur, wenn nicht doppelt vorhanden*/
    private void loescheAlteIdentAbstimmungen(int altePos, int pFunktion, boolean[] abstimmungWurdeVeraendert) {
        int anz = listeDerAbstimmungen.length;

        /*Prüfen, ob alte Ident mehrfach vorhanden ist. Falls ja, dann Lücke nicht schließen!*/
        int gef = 0;
        for (int i = 0; i < anz; i++) {
            switch (pFunktion) {
            case 1:
                if (listeDerAbstimmungen[i].anzeigePositionIntern == altePos) {
                    gef++;
                }
                break;
            case 2:
                if (listeDerAbstimmungen[i].anzeigePositionExternWeisungen == altePos) {
                    gef++;
                }
                break;
            case 8:
                if (listeDerAbstimmungen[i].anzeigePositionExternWeisungenHV == altePos) {
                    gef++;
                }
                break;
            }
        }
        if (gef > 0) {
            return;
        }

        /*Falls alteIdent 0, dann Lücke nicht schließen (0 möglichst nicht verwenden)*/
        if (altePos == 0) {
            return;
        }
        /*Nun Lücke füllen*/
        for (int i = 0; i < anz; i++) {
            switch (pFunktion) {
            case 1:
                if (listeDerAbstimmungen[i].anzeigePositionIntern > altePos) {
                    listeDerAbstimmungen[i].anzeigePositionIntern--;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            case 2:
                if (listeDerAbstimmungen[i].anzeigePositionExternWeisungen > altePos) {
                    listeDerAbstimmungen[i].anzeigePositionExternWeisungen--;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            case 8:
                if (listeDerAbstimmungen[i].anzeigePositionExternWeisungenHV > altePos) {
                    listeDerAbstimmungen[i].anzeigePositionExternWeisungenHV--;
                    abstimmungWurdeVeraendert[i] = true;
                }
                break;
            }
        }

    }
}
