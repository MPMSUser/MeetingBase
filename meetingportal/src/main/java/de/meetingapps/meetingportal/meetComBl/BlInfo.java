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
package de.meetingapps.meetingportal.meetComBl;

import de.meetingapps.meetingportal.meetComDb.DbBundle;
import de.meetingapps.meetingportal.meetComEntities.EclInfo;

public class BlInfo {
    
    private DbBundle lDbBundle=null;
    
    public BlInfo(DbBundle pDbBundle) {
        lDbBundle=pDbBundle;
    }
    
    
    public EclInfo liefereNachrichtFuerVersamnmlungsleiter() {
        
        lDbBundle.dbInfo.read(1);
        if (lDbBundle.dbInfo.anzErgebnis()==0) {
            EclInfo lInfo=new EclInfo();
            lInfo.infoIdent=1;
            lInfo.infoKlasse=1;
            lInfo.empfaenger=1;
            lInfo.infoText="";
            lDbBundle.dbInfo.insert(lInfo);
            return lInfo;
        }
        
        return lDbBundle.dbInfo.ergebnisPosition(0);
    }
    
    public int schreibeNachrichtFuerVersammlungsleiter(EclInfo pInfo){
        int rc=lDbBundle.dbInfo.insert(pInfo);
        if (rc==1) {
            return 1;
        }
        rc=lDbBundle.dbInfo.update(pInfo);
        
        return rc;
    }
    
    
}
