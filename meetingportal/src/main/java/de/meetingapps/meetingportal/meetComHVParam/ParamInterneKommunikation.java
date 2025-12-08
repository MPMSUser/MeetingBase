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

public class ParamInterneKommunikation {
    
    /**Hinweis: auf die Arrays wird mit den entsprechenden Feldern in ParamS.clGlobalVar verwiesen*/
    
    
    /**Range der alten Arrays. Wird beim Umsetzen in liefereOffsetNeu benötigt*/
    public static final int nummerAltVon=0;
    public static final int nummerAltBis=15;
    
    /**Range der Arrays*/
    public static final int nummerVon=0;
    public static final int nummerBis=8;
    
    
    public static final int freieNummer=3;
    public static final int freieOnlineNummer=8;
    
    public static final int defaultOffsetFuerAuswahl=2; 
  
    
    // @formatter:off

    private static int[] lfdNrNeu= {
            0,
            -1,
            1,
            -1,
            -1,
            -1, //5
            2,
            3,
            -1,
            -1,
            4, //10
            5,
            6,
            -1,
            7,
            8
    };
    
//    private static int[] lfdNrNeu= {
//            0,
//            1,
//            2,
//            3,
//            4,
//            5,
//            6,
//            7,
//            8,
//            9,
//            10,
//            11,
//            12,
//            13,
//            14,
//            15
//    };

    
    
    /**Setzt alte Offsets (von 0 bis 15) auf die neuen Offsets in den Arrays um
     * Liefert -1, wenn pAlteNummer nicht im zulässigen Range*/
    public static int liefereOffsetNeu(int pAlteNummer) {
        if (pAlteNummer>nummerAltBis || pAlteNummer<nummerAltVon) {
            return -1;
        }
        return lfdNrNeu[pAlteNummer];
      
    }
    
    

    public static String[] auswahlTexte= {
            "P1",
            "-",
            "Lokal",
            "-",
            "-",
            "P2",
            "P3",
            "Archiv",
            "-"
    };

//    public static String[] auswahlTexte= {
//            "P1",
//            "-",
//            "-",
//            "-",
//            "-",
//            "-",
//            "Lokal",
//            "-",
//            "-",
//            "-",
//            "-",
//            "P2",
//            "P3",
//            "-",
//            "Archiv",
//            "-"
//    };

    public static String[] webServicePfadZurAuswahlTest={
            "Produktionsserver 1 (HTTPS)",
            "Testserver (nur im Firmennetz)",
            "LocalHost",
            "IP-Adresse",
            "HV-Server", //10
            "Produktionsserver 2 (HTTPS)",
            "Produktionsserver 3 (HTTPS)",
            "HV-Archiv (nur im Firmennetz)",
            "IP-Adresse Online Server" //15
    };



    
    public static String[] webServiceInfo={
            "Produktion 1",
            "Test",
            "Local",
            "IP-Adresse",
            "HV",
            "Produktion 2",
            "Produktion 3",
            "HV-Archiv",
            "IP-Adresse Online Server"
   };

    
    
    public static String[] webServicePfadZurAuswahl={
            "Ergaenzen"
    };

    

    

    /**Nur benötigt für Test-Client von Online-Redakteur*/
    public static String[] webServicePfadZurAuswahlExtern={
            "Ergaenzen"
    };

   
    

    public static String[] webServiceSSLZertifikat={
            "",
            "",
            "",
            ""
 
    };

    
    

    
    public static String[] webServiceTrustStorePassword={
            "",
            "",
            "",
            ""
    };

    
    
    
    public static int liefereDatenbankPfadNr(int pWebServicePfadNr) {
        switch (pWebServicePfadNr) {
        case 0:
            /*Prod 1*/return 0;
        case 1:
            /*Test*/return 1;
        case 2:
            /*Localhost*/return 2;
        case 4:
            /*HV*/return 4;
        case 5:
            /*Prod 2*/return 5;
        case 6:
            /*Prod 3*/return 6;
        case 7:
            /*HV-Archiv*/return 7;
        case 8:
            /*IP-Adresse Online Server*/return 8;
           
         }
        return -1;
       
    }

 

    public static String[] datenbankPfadZurAuswahlText={
            "Produktion 1",
            "Test-Server",
            "lokale Datenbank",
            "IP-Adresse",
            "HV Server",
            "Produktion 2",
            "Produktion 3",
            "HV-Archiv neu",
            "IP-Adresse Online Server"
   };

  
    public static String[] datenbankPfadZurAuswahl={
            ""
    };


    // @formatter:on

}
