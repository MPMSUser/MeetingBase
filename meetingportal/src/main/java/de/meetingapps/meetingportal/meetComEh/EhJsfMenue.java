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

import java.io.Serializable;

public class EhJsfMenue  implements Serializable{
    private static final long serialVersionUID = -4844263135797711578L;
    
    public String bezeichnung="";
    public int funktion=0;
    
    
    public EhJsfMenue(String pBezeichnung, int pFunktion) {
        bezeichnung=pBezeichnung;
        funktion=pFunktion;
    }
    
    
    /***************************Standard getter und setter*****************************************************/
    public String getBezeichnung() {
        return bezeichnung;
    }
    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }
    public int getFunktion() {
        return funktion;
    }
    public void setFunktion(int funktion) {
        this.funktion = funktion;
    }
    
    
    
}
