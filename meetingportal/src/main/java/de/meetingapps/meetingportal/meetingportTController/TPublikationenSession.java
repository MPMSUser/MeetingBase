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

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TPublikationenSession implements Serializable {
	private static final long serialVersionUID = -7811155200623185171L;

	/**
	 * 1=per Post 2=per Mail 3=kein Newsletter
	 */
	private String versandartNewsletter = "1";

	/**
	 * Wenn true, dann sind Tickets noch nicht abgearbeitet, d.h. der aktuelle
	 * angezeigte Status beinhaltet diese Verarbeitung ggf. noch nicht ->
	 * Warnhinweis anzeigen
	 * 
	 * Analog "fehlerhaft".
	 * 
	 * Wird gefüllt über BrMPublikationen.pruefeObAenderungenInArbeit
	 *
	 */
	private boolean aenderungenInArbeit = false;
	private boolean aenderungenFehlerhaft = false;

	
    /**aenderungsverfolgung*/
    private String vorAenderungVersandartNewsletter="";

	
	
	/*************** Standard getter und setter **************************/

	public String getVersandartNewsletter() {
		return versandartNewsletter;
	}

	public void setVersandartNewsletter(String versandartNewsletter) {
		if (versandartNewsletter.length() > 1) {
			switch (versandartNewsletter) {
			case "1 - E-Mail":
				versandartNewsletter = "2";
				break;
			case "2 - Post":
				versandartNewsletter = "1";
				break;
			default:
				versandartNewsletter = "2";
				break;
			}
		}
		this.versandartNewsletter = versandartNewsletter;
	}

	public boolean isAenderungenInArbeit() {
		return aenderungenInArbeit;
	}

	public void setAenderungenInArbeit(boolean aenderungenInArbeit) {
		this.aenderungenInArbeit = aenderungenInArbeit;
	}

	public boolean isAenderungenFehlerhaft() {
		return aenderungenFehlerhaft;
	}

	public void setAenderungenFehlerhaft(boolean aenderungenFehlerhaft) {
		this.aenderungenFehlerhaft = aenderungenFehlerhaft;
	}

    public String getVorAenderungVersandartNewsletter() {
        return vorAenderungVersandartNewsletter;
    }

    public void setVorAenderungVersandartNewsletter(String vorAenderungVersandartNewsletter) {
        this.vorAenderungVersandartNewsletter = vorAenderungVersandartNewsletter;
    }

}
