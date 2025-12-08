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
package de.meetingapps.meetingportal.meetComStub;

import de.meetingapps.meetingportal.meetComEntities.EclEmittenten;
import de.meetingapps.meetingportal.meetComHVParam.HVParam;
import de.meetingapps.meetingportal.meetComWE.WERoot;

public class WEStubParameter extends WERoot {

    /**Bisher vergebener Max-Wert = 5*/
    public int stubFunktion = -1;

    public HVParam pHVParam = null;
    public EclEmittenten pEmittent=null;
    
    public int pMandant;
    public int pHvJahr;
    public String pHvNummer;
    public String pDatenbereich;

    
    
}
