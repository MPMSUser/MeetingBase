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
package de.meetingapps.meetingportal.meetComEntitiesku178;

import java.util.ArrayList;

public class EprPostBeteiligungserhoehung {
    
    public String locale;
    public EprApplicant applicant;
    public EprShare share;
    public ArrayList<EprBankAccount> bankAccount;
    public String paymentMethod;
    public boolean confirmPayment;
    public String magazineDelivery;
    public boolean advertisingPermission;
    public String advertisingEvent;
    public String transmissionID;
    public String touchpoint;
    public boolean pdfDontSendEmail;
    public String signingType;
    public EprRecommendBonus recommendBonus;
    public EprIntroducer introducer;
    
    
    /*
     * Standard getter und Setter
     */
    
    public String getMagazineDelivery() {
        return magazineDelivery;
    }
    public void setMagazineDelivery(String magazineDelivery) {
        this.magazineDelivery = magazineDelivery;
    }
    public boolean isAdvertisingPermission() {
        return advertisingPermission;
    }
    public void setAdvertisingPermission(boolean advertisingPermission) {
        this.advertisingPermission = advertisingPermission;
    }
    public String getAdvertisingEvent() {
        return advertisingEvent;
    }
    public void setAdvertisingEvent(String advertisingEvent) {
        this.advertisingEvent = advertisingEvent;
    }
    public String getTransmissionID() {
        return transmissionID;
    }
    public void setTransmissionID(String transmissionID) {
        this.transmissionID = transmissionID;
    }
    public String getTouchpoint() {
        return touchpoint;
    }
    public void setTouchpoint(String touchpoint) {
        this.touchpoint = touchpoint;
    }
    public boolean isPdfDontSendEmail() {
        return pdfDontSendEmail;
    }
    public void setPdfDontSendEmail(boolean pdfDontSendEmail) {
        this.pdfDontSendEmail = pdfDontSendEmail;
    }
    public EprApplicant getApplicant() {
        return applicant;
    }
    public void setApplicant(EprApplicant applicant) {
        this.applicant = applicant;
    }
    public EprShare getShare() {
        return share;
    }
    public void setShare(EprShare share) {
        this.share = share;
    }
    public ArrayList<EprBankAccount> getBankAccount() {
        return bankAccount;
    }
    public void setBankAccount(ArrayList<EprBankAccount> bankAccount) {
        this.bankAccount = bankAccount;
    }
    public String getPaymentMethod() {
        return paymentMethod;
    }
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
    public String getSigningType() {
        return signingType;
    }
    public void setSigningType(String signingType) {
        this.signingType = signingType;
    }
    public String getLocale() {
        return locale;
    }
    public void setLocale(String locale) {
        this.locale = locale;
    }
    public EprRecommendBonus getRecommendBonus() {
        return recommendBonus;
    }
    public void setRecommendBonus(EprRecommendBonus recommendBonus) {
        this.recommendBonus = recommendBonus;
    }
    public EprIntroducer getIntroducer() {
        return introducer;
    }
    public void setIntroducer(EprIntroducer introducer) {
        this.introducer = introducer;
    }
    public boolean isConfirmPayment() {
        return confirmPayment;
    }
    public void setConfirmPayment(boolean confirmPayment) {
        this.confirmPayment = confirmPayment;
    }
    
    

}
