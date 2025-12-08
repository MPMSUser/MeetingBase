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

import de.meetingapps.meetingportal.meetComAllg.CaBug;

public class KonstPortalFunktionen {
    static int logDrucken=3;
    /**Hinweis: im Kommentar=Bit für Berechtigung bei Gästen und Instis, sowie Offset im Array EclPortalFunktion.
     * Für ChatInsti, ChatVip und monitoring Berechtigung auch für "normale" Aktionäre.
     * 
     * Berechtigung=Long-Wert, = 8x8 bit = 64 Berechtigungen.
     * 
     * Wert in KonstTerminen:
     * 	Anfang = x*2+119
     * 	Ende= x*2+120
     * */
    public final static int stream = 1; //2
    public final static int fragen = 2; //4
    public final static int wortmeldungen = 3;//8
    public final static int widersprueche = 4; //16
    public final static int antraege = 5; //32
    public final static int sonstigeMitteilungen = 6; //64
    public final static int chat = 7; //128
    public final static int chatInsti = 8; //256
    public final static int chatVIP = 9; //512
    public final static int unterlagenGruppe1 = 10; //1024
    public final static int unterlagenGruppe2 = 11; //2048
    public final static int unterlagenGruppe3 = 12; //4096
    public final static int unterlagenGruppe4 = 13; //8192
    public final static int unterlagenGruppe5 = 14; //16384
    public final static int teilnehmerverzeichnis = 15; //32768
    public final static int abstimmungsergebnisse = 16; //65536
    public final static int onlineteilnahme = 17; //131072
    public final static int botschaftenEinreichen=18; //262144      155
    public final static int botschaftenAnzeige=19; //524288
    public final static int rueckfragen=20; //1048576


    /**Nicht als Phase vorgesehen, aber in Funktions-Pflege und als Platzhalter für Berechtigungsbit*/
    public final static int empfangFragen = 31; //2147483648
    public final static int empfangWortmeldungen = 32; //4294967296
    public final static int empfangWiderspruch = 33; //8589934592
    public final static int empfangAntraege = 34; //17179869184
    public final static int empfangSonstigeMitteilungen = 35; //34359738368
    
    public final static int monitoring = 36; //68719476736


    /*Nur als Platzhalter für Berechtigungsbit*/
    public final static int preview=50; //Berechtigungsbit rechnet ab hier Excel falsch! Deshalb hier eliminiert
    
    public final static int gast1 = 51; // Normaler Gast, nur Streaming
    
    public final static int gast2 = 52; // Insti - z.B. für ChatInsti. Insti hat überlicherweise immer die selben Rechte wie Aktionär. Möglicherweise können aber Zusatzrechte erforderliche sein (Unterlagen ...)
    public final static int gast3 = 53; // Presse
    public final static int gast4 = 54; // Wirtschaftsprüfer
    public final static int gast5 = 55; // Notar
    public final static int gast6 = 56; // HV-Personal
    public final static int gast7 = 57; // 
    public final static int gast8 = 58; //
    
    /**Karte hat - ausschließlich - Zutritt zum Rahmenprogramm (Veranstaltungsmanagement, ku178)*/
    public final static int gast9 = 59; //

    /**Gast 10 gilt zukünftig für: ist einfach Gast, bzw. ist präsenter Gast*/
    public final static int gast10 = 60;//


    /*Nur als Platzhalter für sonstige Funktionen*/
    public final static int einstellungen=71;
    public final static int unterlagen=72;
    public final static int gaeste=73;
    
    /*Achtung- maximaler Wert 60!
     * 
     * Wert für maximale Berechtigung: 
     */

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "";
        }
        case 1: {
            return "Stream";
        }
        case 2: {
            return "Fragen";
        }
        case 3: {
            return "Wortmeldungen";
        }
        case 4: {
            return "Widersprüche";
        }
        case 5: {
            return "Antraege";
        }
        case 6: {
            return "Sonstige Mitteilungen";
        }
        case 7: {
            return "Chat";
        }
        case 8:
            return "Chat Institutionelle";
        case 9:
            return "Chat VIP";
        case 10:
            return "Unterlagen Gruppe 1";
        case 11:
            return "Unterlagen Gruppe 2";
        case 12:
            return "Unterlagen Gruppe 3";
        case 13:
            return "Unterlagen Gruppe 4";
        case 14:
            return "Unterlagen Gruppe 5";
        case 15:
            return "Teilnehmerverzeichnis";
        case 16:
            return "Abstimmungsergebnisse";
        case 17:
            return "Online-Teilnahme";
        case 18:
            return "Botschaft einreichen";
        case botschaftenAnzeige:
            return "Botschaft";
        case rueckfragen:
            return "Rückfragen";

        case 31:
            return "Empfang Fragen";
        case 32:
            return "Empfang Wortmeldungen";
        case 33:
            return "Empfang Widersprüche";
        case 34:
            return "Empfang Anträge";
        case 35:
            return "Empfang sonstige Mitteilungen";
        case 36:
            return "Monitoring";
            
            
        case 50:
            return "Preview";

        case 51:
            return "Gast 1";
        case 52:
            return "Gast 2";
        case 53:
            return "Gast 3";
        case 54:
            return "Gast 4";
        case 55:
            return "Gast 5";
        case 56:
            return "Gast 6";
        case 57:
            return "Gast 7";
        case 58:
            return "Gast 8";
        case 59:
            return "Gast 9";
        case 60:
            return "Gast 10";
        case 71:
            return "Einstellungen";
        case 73:
            return "Gäste";
        }

        return "";
    }

    static public int portalFunktionZuView(int pView) {
        switch (pView) {
        case KonstPortalView.EINSTELLUNGEN:return einstellungen; 
        case KonstPortalView.MONITORING:return monitoring; 
        case KonstPortalView.STREAM_START:return stream; 
        case KonstPortalView.UNTERLAGEN:return unterlagen; 
        case KonstPortalView.FRAGEN:return fragen; 
        case KonstPortalView.WORTMELDUNGEN:return wortmeldungen; 
        case KonstPortalView.WIDERSPRUECHE:return widersprueche; 
        case KonstPortalView.TEILNEHMERVERZEICHNIS:return teilnehmerverzeichnis; 
        case KonstPortalView.ANTRAEGE:return antraege; 
        case KonstPortalView.SONSTIGEMITTEILUNGEN:return sonstigeMitteilungen; 
        case KonstPortalView.ABSTIMMUNGSERGEBNISSE:return abstimmungsergebnisse; 
        case KonstPortalView.BOTSCHAFTEN_EINREICHEN:return botschaftenEinreichen; 
        case KonstPortalView.BOTSCHAFTEN_ANZEIGEN:return botschaftenAnzeige; 
        case KonstPortalView.RUECKFRAGEN:return rueckfragen; 
        case KonstPortalView.GASTKARTE_UEBERSICHT:
        case KonstPortalView.GASTKARTE_EINGABE:
            return gaeste;
        }
        return -1;
    }
    
    static public long berechtigungsbit(int nr) {
        return (long) Math.pow(2, nr);
    }
    
    static public boolean darfFunktion(int nr, long berechtigungsWert) {
        CaBug.druckeLog("nr="+nr+" berechtigungsWert="+berechtigungsWert+" berechtigungsbit(nr)="+berechtigungsbit(nr), logDrucken, 10);
        return ((berechtigungsWert & berechtigungsbit(nr))!=0);
        
    }
    
    static public long setzeBerechtigung(long pBerechtigungsWert, int nr, boolean pSetzen) {
        long neuerBerechtigungsWert=pBerechtigungsWert;
        if (pSetzen) {
            neuerBerechtigungsWert=pBerechtigungsWert | berechtigungsbit(nr);
        }
        return neuerBerechtigungsWert;
    }
    
    static public boolean benoetigtHVPortal(int nr) {
        switch (nr) {
        case einstellungen:return false;
        case monitoring:return false;
        }
        return true;
    }
    
    static public boolean aktivPruefungMoeglich(int nr) {
        if (nr==einstellungen) {return false;}
        if (nr==gaeste) {return false;}
        return true;
    }
}
