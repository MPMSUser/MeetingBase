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

import de.meetingapps.meetingportal.meetComAllg.CaBug;

public class ParamSpezial {

    private static int logDrucken=3;
    
//    public static boolean ku178SepaIstAktiv() {
//        return false;
//    }
    
   

    public static boolean ku287(int pMandant) {
        if (pMandant==287) {return true;}
        return false;
    }

    
    public static boolean ku310(int pMandant) {
        if (pMandant==310) {return true;}
        return false;
    }
    
    
    public static boolean ku302_303(int pMandant) {
        if (pMandant == 303) { /**Holding*/
            return true;
        }
        if (pMandant == 302) { /*AG*/
            return true;
        }
        return false;

    }

    public static boolean ku303mitVerbaenden(int pMandant) {
        CaBug.druckeLog("pMandant=" + pMandant, logDrucken, 10);
        if (pMandant == 303) { /**Holding*/
            return true;
        }
        return false;
    }

    public static boolean ku302ohneVerbaenden(int pMandant) {
        if (pMandant == 302) { /*AG*/
            return true;
        }
        return false;
    }

    /**
     * bereits prametrisiert: ParamBasis.eintrittskarteNeuVergeben auf true setzen
     */
    public static boolean ku178(int pMandant) {
        if (pMandant == 178) {
            return true;
        }
//        if (pMandant == 878) {
//            return true;
//        }
        return false;
    }

    public static boolean ku216(int pMandant) {
        if (pMandant == 216) {
            return true;
        }
        return false;
    }

    public static boolean ku243(int pMandant) {
        if (pMandant == 243) {
            return true;
        }
        return false;
    }

    public static boolean ku219(int pMandant) {
        if (pMandant == 219) {
            return true;
        }
        if (pMandant == 225) {
            return true;
        }
        if (pMandant == 226) {
            return true;
        }
        return false;
    }

    public static boolean ku217(int pMandant) {
        if (pMandant == 217 || pMandant==265) {
            return true;
        }
        return false;
    }

    public static boolean ku111(int pMandant) {
        if (pMandant == 111) {
            return true;
        }
        return false;
    }

    public static boolean ku097(int pMandant) {
//        if (pMandant == 97) {
//            return true;
//        }
        return false;
    }

    public static boolean ku108(int pMandant) {
        if (pMandant == 108) {
            return true;
        }
        return false;
    }

    public static boolean ku254(int pMandant) {
        if (pMandant == 254) {
            return true;
        }
        return false;
    }

    public static boolean ku036(int pMandant) {
        if (pMandant == 36) {
            return true;
        }
        return false;
    }

    public static boolean ku168(int pMandant) {
        if (pMandant == 168) {
            return true;
        }
        return false;
    }

    public static boolean ku152(int pMandant) {
        if (pMandant == 152) {
            return true;
        }
        return false;
    }

    public static boolean ku110(int pMandant) {
        if (pMandant == 110) {
            return true;
        }
        return false;
    }

    public static boolean ku250(int pMandant) {
        if (pMandant == 250) {
            return true;
        }
        return false;
    }

    public static boolean ku252(int pMandant) {
        if (pMandant == 252) {
            return true;
        }
        return false;
    }

    public static boolean ku244(int pMandant) {
        if (pMandant == 244) {
            return true;
        }
        return false;
    }

     public static boolean ku236(int pMandant) {
        if (pMandant == 236) {
            return true;
        }
        return false;
    }

    public static boolean ku083(int pMandant) {
        if (pMandant == 83) {
            return true;
        }
        return false;
    }

    public static boolean ku234(int pMandant) {
        if (pMandant == 234) {
            return true;
        }
        return false;
    }

    public static boolean ku116(int pMandant) {
        return pMandant == 116;
    }
    
    public static boolean ku128(int pMandant) {
        return pMandant == 128;
    }
    
    public static boolean ku164(int pMandant) {
        return pMandant == 164;
    }
}
