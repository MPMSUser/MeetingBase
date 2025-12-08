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

import java.io.Serializable;

public class ParamWillenserklaerungen  implements Serializable {
    private static final long serialVersionUID = -683012870861365601L;

    /****************** Für Vollmachten / Vollmachtsstorno *******************/
    /**
     * Für Storno von Vollmachten: =true => auch "nicht direkte" Nachfolger dürfen storniert werden =false => nur
     * direkte Nachfolger drüfen storniert werden
     */
    /*TODO Parameter vorhanden aber nicht funktionsfähig*/
    public boolean paramVollmacht_VorgaengerDarfAlleNachfolgerStornieren = false;

    /**
     * Für Storno von Vollmachten: =true => auch "nicht direkte" Nachfolger dürfen storniert werden =false => nur
     * direkte Nachfolger drüfen storniert werden
     */
    /*TODO Parameter vorhanden aber nicht funktionsfähig*/
    public boolean paramVollmacht_AktionaerDarfAlleNachfolgerStornieren = false;

    /**
     * Für Storno von Vollmachten: =true => Bevollmächtigter darf alle anderen Bevollmächtigten stornieren - auch wenn
     * sie keine Nachfolger von ihm sind =false => Bevollmächtigter darf nur "nachfolgende" (d.h. direkt oder indirekt
     * von ihm erteilte" Vollmachten stornieren
     */
    /*TODO Parameter vorhanden aber nicht funktionsfähig*/
    public boolean paramVollmacht_BevollmaechtigterDarfAllesStornieren = false;

    
}
