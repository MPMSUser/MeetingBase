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
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclNachricht;
import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class TNachrichtenSession  implements Serializable {
    private static final long serialVersionUID = 4916654553767378601L;

    /**Alle Nachrichten, die einem Empfänger zugeordnet sind*/
    private List<EclNachricht> empfangeneNachrichten = null;

    
    /**Angezeigte Nachricht*/
    private boolean nachrichtDetailWirdAngezeigt=false;
    private EclNachricht nachrichtDetailNachricht=null;
    
    private int nachrichtDetailMailTextTextNrVor=0;
    private String nachrichtDetailText="";
    private int nachrichtDetailMailTextTextNrNach=0;

    private int nachrichtDetailFunktionsButton1TextNr=0;
    private int nachrichtDetailFunktionsButton2TextNr=0;

    private int anzahlUngeleseneNachrichten=0;
    
    /*******************Standard getter und setter*************************************/
    public List<EclNachricht> getEmpfangeneNachrichten() {
        return empfangeneNachrichten;
    }

    public void setEmpfangeneNachrichten(List<EclNachricht> empfangeneNachrichten) {
        this.empfangeneNachrichten = empfangeneNachrichten;
    }

    public boolean isNachrichtDetailWirdAngezeigt() {
        return nachrichtDetailWirdAngezeigt;
    }

    public void setNachrichtDetailWirdAngezeigt(boolean nachrichtDetailWirdAngezeigt) {
        this.nachrichtDetailWirdAngezeigt = nachrichtDetailWirdAngezeigt;
    }

    public String getNachrichtDetailText() {
        return nachrichtDetailText;
    }

    public void setNachrichtDetailText(String nachrichtDetailText) {
        this.nachrichtDetailText = nachrichtDetailText;
    }

    public int getAnzahlUngeleseneNachrichten() {
        return anzahlUngeleseneNachrichten;
    }

    public void setAnzahlUngeleseneNachrichten(int anzahlUngeleseneNachrichten) {
        this.anzahlUngeleseneNachrichten = anzahlUngeleseneNachrichten;
    }

    public EclNachricht getNachrichtDetailNachricht() {
        return nachrichtDetailNachricht;
    }

    public void setNachrichtDetailNachricht(EclNachricht nachrichtDetailNachricht) {
        this.nachrichtDetailNachricht = nachrichtDetailNachricht;
    }

    public int getNachrichtDetailMailTextTextNrVor() {
        return nachrichtDetailMailTextTextNrVor;
    }

    public void setNachrichtDetailMailTextTextNrVor(int nachrichtDetailMailTextTextNrVor) {
        this.nachrichtDetailMailTextTextNrVor = nachrichtDetailMailTextTextNrVor;
    }

    public int getNachrichtDetailMailTextTextNrNach() {
        return nachrichtDetailMailTextTextNrNach;
    }

    public void setNachrichtDetailMailTextTextNrNach(int nachrichtDetailMailTextTextNrNach) {
        this.nachrichtDetailMailTextTextNrNach = nachrichtDetailMailTextTextNrNach;
    }

    public int getNachrichtDetailFunktionsButton1TextNr() {
        return nachrichtDetailFunktionsButton1TextNr;
    }

    public void setNachrichtDetailFunktionsButton1TextNr(int nachrichtDetailFunktionsButton1TextNr) {
        this.nachrichtDetailFunktionsButton1TextNr = nachrichtDetailFunktionsButton1TextNr;
    }

    public int getNachrichtDetailFunktionsButton2TextNr() {
        return nachrichtDetailFunktionsButton2TextNr;
    }

    public void setNachrichtDetailFunktionsButton2TextNr(int nachrichtDetailFunktionsButton2TextNr) {
        this.nachrichtDetailFunktionsButton2TextNr = nachrichtDetailFunktionsButton2TextNr;
    }
    
    

}
