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
package de.meetingapps.meetingportal.meetComKonst;

/*Werte sind die Bits in hinweisWeitere*/
public class KonstHinweisWeitere {
    
    public static final int PERM_PORTAL_HINWEIS1 = 1;
    public static final int PERM_PORTAL_HINWEIS2 = 2;
    public static final int WEISUNG_QUITTUNG_JN=4;
    public static final int QUITTUNG_PER_EMAIL_BEI_ALLEN_WILLENSERKLAERUNGEN=8;
    
    public static final int NAECHSTER_FREIER= 16;
    
    /**Hier wird die Summe alle bisherigen Bits vergeben*/
    public static final int MAX_WERT=15;

    /**"Leert" das Bit, das in hinweisWeitereBestaetigt übergeben wird, d.h. es 
     * setzt dies auf 0.
     *  
     * Nur für WEISUNG_QUITTUNG_JN implementiert*/
    public static int leereBit(int hinweisIdent, int hinweisWeitereBestaetigt) {
        switch (hinweisIdent) {
        case WEISUNG_QUITTUNG_JN: return (hinweisWeitereBestaetigt & 11); 
        }
        
        return hinweisWeitereBestaetigt;
    }
    
    
    
}
