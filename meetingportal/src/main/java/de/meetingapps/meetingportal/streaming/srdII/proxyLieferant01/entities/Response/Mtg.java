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
package de.meetingapps.meetingportal.streaming.srdII.proxyLieferant01.entities.Response;

import java.util.List;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Mtg {

    @XmlElement(name = "MtgId")
    private String mtgId;

    @XmlElement(name = "IssrMtgId")
    private String issrMtgId;

    @XmlElement(name = "Tp")
    private String tp;

    @XmlElement(name = "Clssfctn")
    private Clssfctn clssfctn;

    @XmlElement(name = "Prtcptn")
    private List<Prtcptn> prtcptn;

    @XmlElement(name = "EntitlmntFxgDt")
    private EntitlmntFxgDt entitlmntFxgDt;

    @XmlElement(name = "RegnSctiesMktDdln")
    private DtOrDtTm regnSctiesMktDdln;

    public String getMtgId() { return mtgId; }
    public void setMtgId(String mtgId) { this.mtgId = mtgId; }

    public String getIssrMtgId() { return issrMtgId; }
    public void setIssrMtgId(String issrMtgId) { this.issrMtgId = issrMtgId; }

    public String getTp() { return tp; }
    public void setTp(String tp) { this.tp = tp; }

    public Clssfctn getClssfctn() { return clssfctn; }
    public void setClssfctn(Clssfctn clssfctn) { this.clssfctn = clssfctn; }

    public List<Prtcptn> getPrtcptn() { return prtcptn; }
    public void setPrtcptn(List<Prtcptn> prtcptn) { this.prtcptn = prtcptn; }

    public EntitlmntFxgDt getEntitlmntFxgDt() { return entitlmntFxgDt; }
    public void setEntitlmntFxgDt(EntitlmntFxgDt entitlmntFxgDt) { this.entitlmntFxgDt = entitlmntFxgDt; }

    public DtOrDtTm getRegnSctiesMktDdln() { return regnSctiesMktDdln; }
    public void setRegnSctiesMktDdln(DtOrDtTm regnSctiesMktDdln) { this.regnSctiesMktDdln = regnSctiesMktDdln; }
}
