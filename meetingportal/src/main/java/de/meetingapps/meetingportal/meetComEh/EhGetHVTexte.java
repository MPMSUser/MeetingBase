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
package de.meetingapps.meetingportal.meetComEh;

import de.meetingapps.meetingportal.meetComEntities.EclFehler;

/**Return-Klasse für BlMPuffer.getHVTexte*/
public class EhGetHVTexte {
    
    public int tempTexteVersion = 0;
    public EclFehler[] tempFehlerDeutschArray = null;
    public EclFehler[] tempFehlerEnglischArray = null;
    public String[] tempPortalTexteDEArray = null;
    public String[] tempPortalTexteENArray = null;
    public String[] tempPortalTexteAdaptivDEArray = null;
    public String[] tempPortalTexteAdaptivENArray = null;

}
