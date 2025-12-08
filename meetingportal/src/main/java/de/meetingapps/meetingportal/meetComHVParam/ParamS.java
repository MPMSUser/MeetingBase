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

import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComEntities.EclGeraetKlasseSetZuordnung;
import de.meetingapps.meetingportal.meetComEntities.EclGeraeteSet;
import de.meetingapps.meetingportal.meetComEntities.EclTermine;
import de.meetingapps.meetingportal.meetComEntities.EclUserLogin;

/**Statischer "Container" für die aktuell gültigen Parameter - analog zu EclParamM*/
public class ParamS {
    
//    private static int logDrucken=10;
    
    /**false => Parameter wurden überhaupt noch nicht eingelesen*/
    static boolean paramEingelesen = false;

    public static ParamServer paramServer = null;
    public static EclGeraeteSet eclGeraeteSet = null;
    public static EclGeraetKlasseSetZuordnung eclGeraetKlasseSetZuordnung = null;
    public static ParamGeraet paramGeraet = null;

    public static ClGlobalVar clGlobalVar = null;

    public static EclEmittenten eclEmittent = null;
    public static HVParam param = null;

    public static EclTermine[] terminlisteTechnisch = null;

    public static EclUserLogin eclUserLogin = null;

//    public static void eclUserLoginTestAusgabe(){
//        CaBug.druckeLog(
//            " ParamS.eclUserLogin.pruefe_modul_moduleHVMaster()="+ParamS.eclUserLogin.pruefe_modul_moduleHVMaster(), logDrucken, 10);
//    }

    
    /*************Standard Setters, da Find-Bug Warnung beim direkten Setzen von statischen Klassen gibt****************/
    public static void setParamEingelesen(boolean paramEingelesen) {
        ParamS.paramEingelesen = paramEingelesen;
    }

    public static void setParamServer(ParamServer paramServer) {
        ParamS.paramServer = paramServer;
    }

    public static void setEclGeraeteSet(EclGeraeteSet eclGeraeteSet) {
        ParamS.eclGeraeteSet = eclGeraeteSet;
    }

    public static void setEclGeraetKlasseSetZuordnung(EclGeraetKlasseSetZuordnung eclGeraetKlasseSetZuordnung) {
        ParamS.eclGeraetKlasseSetZuordnung = eclGeraetKlasseSetZuordnung;
    }

    public static void setParamGeraet(ParamGeraet paramGeraet) {
        ParamS.paramGeraet = paramGeraet;
    }

    public static void setClGlobalVar(ClGlobalVar clGlobalVar) {
        ParamS.clGlobalVar = clGlobalVar;
    }

    public static void setEclEmittent(EclEmittenten eclEmittent) {
        ParamS.eclEmittent = eclEmittent;
    }

    public static void setParam(HVParam param) {
        ParamS.param = param;
    }

    public static void setEclUserLogin(EclUserLogin eclUserLogin) {
        ParamS.eclUserLogin = eclUserLogin;
    }

    public static void setTerminlisteTechnisch(EclTermine[] terminlisteTechnisch) {
        ParamS.terminlisteTechnisch = terminlisteTechnisch;
    }

}
