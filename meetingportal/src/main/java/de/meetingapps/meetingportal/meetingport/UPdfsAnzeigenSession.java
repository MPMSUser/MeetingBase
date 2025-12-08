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
public class UPdfsAnzeigenSession implements Serializable {
    private static final long serialVersionUID = -2088335939432845849L;
    private String[] dateienPDF=null;
    private String[] dateienAbstimmung=null;
    private String[] dateienTeilnahme=null;
    
    /**********************Standard getter und setter**************************************/
    public String[] getDateienPDF() {
        return dateienPDF;
    }
    public void setDateienPDF(String[] dateienPDF) {
        this.dateienPDF = dateienPDF;
    }
    public String[] getDateienAbstimmung() {
        return dateienAbstimmung;
    }
    public void setDateienAbstimmung(String[] dateienAbstimmung) {
        this.dateienAbstimmung = dateienAbstimmung;
    }
    public String[] getDateienTeilnahme() {
        return dateienTeilnahme;
    }
    public void setDateienTeilnahme(String[] dateienTeilnahme) {
        this.dateienTeilnahme = dateienTeilnahme;
    }
    
    
    
    
}
