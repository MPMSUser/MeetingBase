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

/**Enthält Testfunktionen an zentraler Stelle, zum leichten Ein-/Ausschalten*/
public class CaTest {

    /**Wartet pSekunden, und gibt vor und nach dem Schlafen pAusgabe aus*/
    public static void sleep(int pSekunden, String pAusgabe) {
        System.out.println("Vor Sleep "+pAusgabe);
        try {
            Thread.sleep(pSekunden*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Nach Sleep "+pAusgabe);

    }
    
    
}
