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

public class EprBankAccount {
    
    public boolean sepaDirectDebitMandate;
    public boolean payments;
    public EprDepositor depositor;
    public String iban;
    public String bic;
    public String bank;
    
    
    /*
     * Standard getter und Setter
     */
    
    public boolean isSepaDirectDebitMandate() {
        return sepaDirectDebitMandate;
    }
    public void setSepaDirectDebitMandate(boolean sepaDirectDebitMandate) {
        this.sepaDirectDebitMandate = sepaDirectDebitMandate;
    }
    public boolean isPayments() {
        return payments;
    }
    public void setPayments(boolean payments) {
        this.payments = payments;
    }
    public EprDepositor getDepositor() {
        return depositor;
    }
    public void setDepositor(EprDepositor depositor) {
        this.depositor = depositor;
    }
    public String getIban() {
        return iban;
    }
    public void setIban(String iban) {
        this.iban = iban;
    }
    public String getBic() {
        return bic;
    }
    public void setBic(String bic) {
        this.bic = bic;
    }
    public String getBank() {
        return bank;
    }
    public void setBank(String bank) {
        this.bank = bank;
    }
    
    

}
