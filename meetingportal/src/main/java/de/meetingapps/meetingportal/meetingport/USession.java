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
package de.meetingapps.meetingportal.meetingport;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;

@SessionScoped
@Named
public class USession implements Serializable {
    private static final long serialVersionUID = 9099396139571462572L;

    private String fehlermeldung;

    /**true => das Mandanten-Auswahl-Menü wird nicht aufgerufen*/
    private boolean nurMandant = false;

    /**Reset Variablen für neuen Dialogschritt*/
    public void clearRequest() {
        clearFehlermeldung();
    }

    public void clearFehlermeldung() {
        fehlermeldung = "";

    }

    /**Reset Variablen für die komplette Session (d.h. neuer Login)*/
    public void clearSession() {
        clearFehlermeldung();
    }

    /**Quasi statische Variable
     * 1=uTemplateB.xhtml und uLogin
     * 2=uTemplateLLogin.xhtml und libetLogin
     * 3=uINCHeaderL. und lLogin (LMS)*/

    private String aktuellesTemplate = "uTemplateB.xhtml";
    private int portalVariante = 1;

    public void aktiviereGovVal() {
        aktuellesTemplate = "uIncHeaderL.xhtml";
        portalVariante = 2;
    }

    public void aktiviereBO() {
        aktuellesTemplate = "uIncHeaderB.xhtml";
        portalVariante = 1;
    }

    public void aktiviereLMS() {
        aktuellesTemplate = "uIncHeaderL.xhtml";
        portalVariante = 3;
    }

    /*************Standard-Getter und Setter**************************/

    public String getFehlermeldung() {
        return fehlermeldung;
    }

    public void setFehlermeldung(String fehlermeldung) {
        this.fehlermeldung = fehlermeldung;
    }

    public String getAktuellesTemplate() {
//        System.out.println("*********************************getAktuellesTemplate="+aktuellesTemplate);
        return aktuellesTemplate;
    }

    public void setAktuellesTemplate(String aktuellesTemplate) {
        this.aktuellesTemplate = aktuellesTemplate;
    }

    public int getPortalVariante() {
        return portalVariante;
    }

    public void setPortalVariante(int portalVariante) {
        this.portalVariante = portalVariante;
    }

    public boolean isNurMandant() {
        return nurMandant;
    }

    public void setNurMandant(boolean nurMandant) {
        this.nurMandant = nurMandant;
    }

}
