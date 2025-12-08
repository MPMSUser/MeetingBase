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

public class EprApplicant {

    public int membershipID;
    public boolean isCompany;
    public boolean isMember;
    public String companyname;
    public String name;
    public String forename;
    public String salutation;
    public String birthday;
    public EprAddress address;
    public EprContact contact;
    public String idnr;
    
    /*
     * Standard getter und Setter
     */
    
    public int getMembershipID() {
        return membershipID;
    }
    public void setMembershipID(int membershipID) {
        this.membershipID = membershipID;
    }
    public EprContact getContact() {
        return contact;
    }
    public void setContact(EprContact contact) {
        this.contact = contact;
    }
    public EprAddress getAddress() {
        return address;
    }
    public void setAddress(EprAddress address) {
        this.address = address;
    }
    public String getSalutation() {
        return salutation;
    }
    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }
    public String getForename() {
        return forename;
    }
    public void setForename(String forename) {
        this.forename = forename;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBirthday() {
        return birthday;
    }
    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
    public String getIdnr() {
        return idnr;
    }
    public void setIdnr(String idnr) {
        this.idnr = idnr;
    }
    public boolean isCompany() {
        return isCompany;
    }
    public void setCompany(boolean isCompany) {
        this.isCompany = isCompany;
    }
    public boolean isMember() {
        return isMember;
    }
    public void setMember(boolean isMember) {
        this.isMember = isMember;
    }
    public String getCompanyname() {
        return companyname;
    }
    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }
    
    
}
