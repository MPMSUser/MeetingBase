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
package de.meetingapps.meetingportal.meetComEclM;

import java.io.Serializable;

import de.meetingapps.meetingportal.meetComBlManaged.BlMPuffer;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

/**In dieser Klasse sind die Portaltexte für die Sprache, die aktuell im
 * Portal vewendet werden, für die aktuelle Session hinterlegt.
 */
@SessionScoped
@Named
public class EclPortalTexteMTest implements Serializable {
    private static final long serialVersionUID = -9193340261867497015L;

    @Inject
    BlMPuffer blMPuffer;

    private String porTexte[] = null;

    public EclPortalTexteMTest() {
        porTexte = new String[5];
        porTexte[1] = "Arrayinhalt 1";
        porTexte[2] = "Arrayinhalt 2";

    }

    public String holeText(int textNr) {
        //		if (blMPuffer.getPortalTexteDEArray()==null){System.out.println("blmPuffer ist null");}
        //		else{
        //			int laenge=blMPuffer.getPortalTexteDEArray().length;
        //			System.out.println("blmPuffer Länge="+laenge);
        //			for (int i=0;i<laenge;i++){
        //				System.out.println(i+"="+blMPuffer.getPortalTexteDEArray()[i]);
        //			}
        //		}
        //		String ergebnisText=blMPuffer.getPortalTexteDEArray()[textNr];
        //		System.out.println("ErgebnisText "+textNr+" = "+ergebnisText);
        //		return ergebnisText;
        return "";
    }

    /*************Standard Getter/Setter********************/

    public String[] getPorTexte() {
        return porTexte;
    }

    public void setPorTexte(String[] porTexte) {
        this.porTexte = porTexte;
    }

}
