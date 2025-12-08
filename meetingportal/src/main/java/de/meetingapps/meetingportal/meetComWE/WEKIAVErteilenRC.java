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
package de.meetingapps.meetingportal.meetComWE;

public class WEKIAVErteilenRC extends WERootRC {

    /**WillenserkärungsIdent für die durchgeführte Willenserklärung (also z.B. Weisung, EK etc.)
     * Wichtig: wenn 2 EKs ausgestellt werden, dann werden diese Variablen nicht korrekt / vollständig gefüllt - 
     * Verwendung dafür dann nicht sinnvoll!*/
    private int rcWillenserklaerungIdentAusgefuehrt = 0;

    /*Ab hier Standard-Setters und Getters*/

    public int getRcWillenserklaerungIdentAusgefuehrt() {
        return rcWillenserklaerungIdentAusgefuehrt;
    }

    public void setRcWillenserklaerungIdentAusgefuehrt(int rcWillenserklaerungIdentAusgefuehrt) {
        this.rcWillenserklaerungIdentAusgefuehrt = rcWillenserklaerungIdentAusgefuehrt;
    }

}
