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
package de.meetingapps.meetingportal.meetingportTController;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEclM.EclVeranstaltungM;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltung;
import de.meetingapps.meetingportal.meetComEntities.EclVeranstaltungenMenue;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;


@SessionScoped
@Named
public class TVeranstaltungenSession implements Serializable {
	private static final long serialVersionUID = -1072228811792773733L;

	private List<EclVeranstaltungenMenue> veranstaltungenFuerMenueListe=null;

	public boolean liefereVeranstaltungenVorhanden() {
	    if (veranstaltungenFuerMenueListe==null || veranstaltungenFuerMenueListe.size()==0) {
	        return false;
	    }
	    return true;
	}
	
	private int subFunktion=0;
	
	private EclVeranstaltungenMenue ausgewaehlteVeranstaltungMenue=null;
	
	/*??????*/
	
	private boolean istAngemeldet=false;
    private boolean istAbgemeldet=false;

	private String anzPersonen="0";
	
	private EclVeranstaltungM zurVeranstaltungAngemeldet=null;
	private List<EclVeranstaltungM> zuVeranstaltungenAngemeldetListe=null;
	
    private List<EclVeranstaltungM> veranstaltungen=null;
	
	private String ausgewaehlt="";
	
	private String mailAdresse="";
	
	
	public void setZuVeranstaltungenAngemeldetListe(List<EclVeranstaltung> angemeldetZuVeranstaltungenListe) {
	    zuVeranstaltungenAngemeldetListe=new LinkedList<EclVeranstaltungM>();
	    if (angemeldetZuVeranstaltungenListe!=null) {
	        for (EclVeranstaltung iVeranstaltung:angemeldetZuVeranstaltungenListe) {
	            EclVeranstaltungM lVeranstaltungM=new EclVeranstaltungM(iVeranstaltung);
	            zuVeranstaltungenAngemeldetListe.add(lVeranstaltungM);
	        }
	    }
	}
	
	/*******************Standard getter und setter***************************/

	
	public boolean isIstAngemeldet() {
		return istAngemeldet;
	}

	public void setIstAngemeldet(boolean istAngemeldet) {
		this.istAngemeldet = istAngemeldet;
	}

	public String getAnzPersonen() {
		return anzPersonen;
	}

	public void setAnzPersonen(String anzPersonen) {
		this.anzPersonen = anzPersonen;
	}

	public EclVeranstaltungM getZurVeranstaltungAngemeldet() {
		return zurVeranstaltungAngemeldet;
	}

	public void setZurVeranstaltungAngemeldet(EclVeranstaltungM zurVeranstaltungAngemeldet) {
		this.zurVeranstaltungAngemeldet = zurVeranstaltungAngemeldet;
	}

	public List<EclVeranstaltungM> getVeranstaltungen() {
		return veranstaltungen;
	}

	public void setVeranstaltungen(List<EclVeranstaltungM> veranstaltungen) {
		this.veranstaltungen = veranstaltungen;
	}

	public String getAusgewaehlt() {
		return ausgewaehlt;
	}

	public void setAusgewaehlt(String ausgewaehlt) {
		this.ausgewaehlt = ausgewaehlt;
	}

    public String getMailAdresse() {
        return mailAdresse;
    }

    public void setMailAdresse(String mailAdresse) {
        this.mailAdresse = mailAdresse;
    }

    public boolean isIstAbgemeldet() {
        return istAbgemeldet;
    }

    public void setIstAbgemeldet(boolean istAbgemeldet) {
        this.istAbgemeldet = istAbgemeldet;
    }

    public List<EclVeranstaltungM> getZuVeranstaltungenAngemeldetListe() {
        return zuVeranstaltungenAngemeldetListe;
    }

    public void setZuVeranstaltungenAngemeldetList(List<EclVeranstaltungM> zuVeranstaltungenAngemeldetListe) {
        this.zuVeranstaltungenAngemeldetListe = zuVeranstaltungenAngemeldetListe;
    }

    public List<EclVeranstaltungenMenue> getVeranstaltungenFuerMenueListe() {
        return veranstaltungenFuerMenueListe;
    }

    public void setVeranstaltungenFuerMenueListe(List<EclVeranstaltungenMenue> veranstaltungenFuerMenueListe) {
        this.veranstaltungenFuerMenueListe = veranstaltungenFuerMenueListe;
    }

    public int getSubFunktion() {
        return subFunktion;
    }

    public void setSubFunktion(int subFunktion) {
        this.subFunktion = subFunktion;
    }

    public EclVeranstaltungenMenue getAusgewaehlteVeranstaltungMenue() {
        return ausgewaehlteVeranstaltungMenue;
    }

    public void setAusgewaehlteVeranstaltungMenue(EclVeranstaltungenMenue ausgewaehlteVeranstaltungMenue) {
        this.ausgewaehlteVeranstaltungMenue = ausgewaehlteVeranstaltungMenue;
    }


	


}
