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

public class KonstTermine {

    /*Hinweis: bei weiteren technischen Terminen bitte unbedingt BvDatenbank.initialisereTermine anpassen!!*/

    public final static int unbekannt = 0;
    public final static int veroeffentlichung = 1;
    public final static int einberufung = 2;
    public final static int endeTOErweiterung = 3;
    public final static int endeGegenantraege = 4;
    public final static int recordDate = 5;
    public final static int letzterAnmeldetag = 6;
    public final static int hvTag = 7;
    
    /**Achtung - so wie es aussieht, wird das nicht verwendet. In der Emittenten-Oberfläche wird
     * nämlich für den Eintrag des Beginns der HV auch den Termin hvTag verwendet (durch Tests am 26.02.2025 nachgewiesen)
     */
    public final static int hvTagBeginnHV=8;
    public final static int hvTagEinlass=9;

    public final static int portalPhase1 = 101;
    public final static int portalPhase2 = 102;
    public final static int portalPhase3 = 103;
    public final static int portalPhase4 = 104;
    public final static int portalPhase5 = 105;
    public final static int portalPhase6 = 106;
    public final static int portalPhase7 = 107;
    public final static int portalPhase8 = 108;
    public final static int portalPhase9 = 109;
    public final static int portalPhase10 = 110;
    public final static int portalPhase11 = 111;
    public final static int portalPhase12 = 112;
    public final static int portalPhase13 = 113;
    public final static int portalPhase14 = 114;
    public final static int portalPhase15 = 115;
    public final static int portalPhase16 = 116;
    public final static int portalPhase17 = 117;
    public final static int portalPhase18 = 118;
    public final static int portalPhase19 = 119;
    public final static int portalPhase20 = 120;

    /**Werte sind verbunden mit den Werten in KonstPortalFunktionen!*/
    public final static int stream = 121;
    public final static int stream_Ende = 122;
    public final static int fragen = 123;
    public final static int fragen_Ende = 124;
    public final static int wortmeldungen = 125;
    public final static int wortmeldungen_Ende = 126;
    public final static int widersprueche = 127;
    public final static int widersprueche_Ende = 128;
    public final static int antraege = 129;
    public final static int antraege_Ende = 130;
    public final static int sonstigeMitteilungen = 131;
    public final static int sonstigeMitteilungen_Ende = 132;
    public final static int chat = 133;
    public final static int chat_Ende = 134;
    public final static int chatInsti = 135;
    public final static int chatInsti_Ende = 136;
    public final static int chatVIP = 137;
    public final static int chatVIP_Ende = 138;
    public final static int unterlagenGruppe1 = 139;
    public final static int unterlagenGruppe1_Ende = 140;
    public final static int unterlagenGruppe2 = 141;
    public final static int unterlagenGruppe2_Ende = 142;
    public final static int unterlagenGruppe3 = 143;
    public final static int unterlagenGruppe3_Ende = 144;
    public final static int unterlagenGruppe4 = 145;
    public final static int unterlagenGruppe4_Ende = 146;
    public final static int unterlagenGruppe5 = 147;
    public final static int unterlagenGruppe5_Ende = 148;
    public final static int teilnehmerverzeichnis = 149;
    public final static int teilnehmerverzeichnis_Ende = 150;
    public final static int abstimmungsergebnisse = 151;
    public final static int abstimmungsergebnisse_Ende = 152;
    public final static int onlineteilnahme = 153;
    public final static int onlineteilnahme_Ende = 154;
    public final static int botschaften_einreichen = 155;
    public final static int botschaften_einreichen_Ende = 156;
    public final static int botschaften = 157;
    public final static int botschaften_Ende = 158;
    public final static int rueckfragen=159;
    public final static int rueckfragenEnde=160;

    public final static int hotline = 201;
    public final static int hotline_Ende = 202;

    static public String getText(int nr) {
        switch (nr) {
        case 0: {
            return "";
        }
        case 1: {
            return "Veröffentlichung Wertpapier-Mitteilung";
        }
        case 2: {
            return "Einberufung";
        }
        case 3: {
            return "Spätester Eingang Minderheitsverlangen zur Erweiterung der TO";
        }
        case 4: {
            return "Ende Gegenantragsfrist";
        }
        case 5: {
            return "Record Date";
        }
        case 6: {
            return "Letzter Anmeldetag";
        }
        case 7: {
            return "HV-Tag (Beginn HV)";
        }
        case 8: {
            return "nicht verwendet";
//            return "HV-Tag Beginn HV";
        }
        case 9: {
            return "HV-Tag (Einlass)";
        }

        case 101:
        case 102:
        case 103:
        case 104:
        case 105:
        case 106:
        case 107:
        case 108:
        case 109:
        case 110:
        case 111:
        case 112:
        case 113:
        case 114:
        case 115:
        case 116:
        case 117:
        case 118:
        case 119:
        case 120: {
            return "Portal Phase " + Integer.toString(nr - 100);
        }

        case 121:
            return "Übertragung der Versammlung";
        case 122:
            return "Übertragung der Versammlung Ende";
        case 123:
            return "Fragen";
        case 124:
            return "Fragen Ende";
        case 125:
            return "Wortmeldungen";
        case 126:
            return "Wortmeldungen Ende";
        case 127:
            return "Widersprüche";
        case 128:
            return "Widersprüche Ende";
        case 129:
            return "Anträge";
        case 130:
            return "Anträge Ende";
        case 131:
            return "Sonstige Mitteilungen";
        case 132:
            return "Sonstige Mitteilungen Ende";
        case 133:
            return "Chat";
        case 134:
            return "Chat Ende";
        case 135:
            return "Chat Institutionelle";
        case 136:
            return "Chat Institutionelle Ende";
        case 137:
            return "Chat VIP";
        case 138:
            return "Chat VIP Ende";
        case 139:
            return "Unterlagen Gruppe 1";
        case 140:
            return "Unterlagen Gruppe 1 Ende";
        case 141:
            return "Unterlagen Gruppe 2";
        case 142:
            return "Unterlagen Gruppe 2 Ende";
        case 143:
            return "Unterlagen Gruppe 3";
        case 144:
            return "Unterlagen Gruppe 3 Ende";
        case 145:
            return "Unterlagen Gruppe 4";
        case 146:
            return "Unterlagen Gruppe 4 Ende";
        case 147:
            return "Unterlagen Gruppe 5";
        case 148:
            return "Unterlagen Gruppe 5 Ende";
        case 149:
            return "Teilnehmerverzeichnis";
        case 150:
            return "Teilnehmerverzeichnis 5 Ende";
        case 151:
            return "Abstimmungsergebnisse";
        case 152:
            return "Abstimmungsergebnisse Ende";
        case 153:
            return "Online-Teilnahme";
        case 154:
            return "Online-Teilnahme Ende";
        case 155:
            return "Botschaften einreichen";
        case 156:
            return "Botschaften einreichen Ende";
        case botschaften:
            return "Botschaften";
        case botschaften_Ende:
            return "Botschaften Ende";
        case rueckfragen:
            return "Rückfragen";
        case rueckfragenEnde:
            return "Rückfragen Ende";

        case 201: {
            return "Hotline";
        }
        case 202: {
            return "Hotline Ende";
        }
        }

        return "";
    }
}
