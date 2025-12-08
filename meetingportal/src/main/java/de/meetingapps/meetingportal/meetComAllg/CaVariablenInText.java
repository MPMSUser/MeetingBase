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
package de.meetingapps.meetingportal.meetComAllg;

public class CaVariablenInText {

//    /**NL wird nicht ersetzt, da dies möglicherweise für Mail etc. unterschiedlich aufbereitet werden muß!*/
//    public static String ersetze(String pQuelle, String[] pVariablen, String[] pWerte) {
//        String neuerText = pQuelle;
//
//        for (int i = 0; i < pVariablen.length; i++) {
//            neuerText = ersetzeVariable(neuerText, pVariablen[i], pWerte[i]);
//        }
//
//        return neuerText;
//    }

//    private static String ersetzeVariable(String pQuelle, String pVariable, String pWert) {
//        String neuerText = pQuelle;
//        //		System.out.println("pVariablen="+pVariable);
//        int pos = neuerText.indexOf("<<<" + pVariable + ">>>");
//        while (pos != -1) {
//            neuerText = neuerText.substring(0, pos) + pWert + neuerText.substring(pos + pVariable.length() + 6);
//            //        	System.out.println("neuerText="+neuerText);
//            pos = neuerText.indexOf("<<<" + pVariable + ">>>");
//        }
//        return neuerText;
//    }

    /**Bereitet Mail-Text auf für einfügen in HTML-Seite. D.h.:
     * ' ' => %20
     * <<<NL>>> => %0D%0A
     */
    public static String aufbereiteFuerHTML(String pQuelle) {
        String neuerText = pQuelle;

        /*Als erstes unzulässige Zeichen rausschmeißen*/
        neuerText = ersetzeSpeziellesZeichenFuerMail(neuerText, '%', "");
        neuerText = ersetzeSpeziellesZeichenFuerMail(neuerText, '&', "%26");
        neuerText = ersetzeSpeziellesZeichenFuerMail(neuerText, ',', "%2C");
        neuerText = ersetzeSpeziellesZeichenFuerMail(neuerText, '.', "%2E");
        neuerText = ersetzeSpeziellesZeichenFuerMail(neuerText, ';', "%3B");
        neuerText = ersetzeSpeziellesZeichenFuerMail(neuerText, '?', "%3F");

        neuerText = CaString.ersetzeString(neuerText, "<br />", "%0D%0A");

        /*Zum Schluß unzulässige verbleibende Zeichen rausschmeißen*/
        neuerText = ersetzeSpeziellesZeichenFuerMail(neuerText, '<', "");
        neuerText = ersetzeSpeziellesZeichenFuerMail(neuerText, '>', "");

        return neuerText;
    }

    private static String ersetzeSpeziellesZeichenFuerMail(String pQuelle, char pZeichen, String pZiel) {
        String neuerText = pQuelle;

        int pos = neuerText.indexOf(pZeichen);
        while (pos != -1) {
            neuerText = neuerText.substring(0, pos) + pZiel + neuerText.substring(pos + 1);
            pos = neuerText.indexOf(pZeichen);
        }

        return neuerText;
    }

    /**Bereitet Mail-Text für normales Mail D.h.:
     * 
     * <<<NL>>> => \n
     */
    public static String aufbereiteFuerNormalesMail(String pQuelle) {
        String neuerText = pQuelle;

        neuerText = CaString.ersetzeString(neuerText, "<br />", "\n");

        return neuerText;
    }

}
