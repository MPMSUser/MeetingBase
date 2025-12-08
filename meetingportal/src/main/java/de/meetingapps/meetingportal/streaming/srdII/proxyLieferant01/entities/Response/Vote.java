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

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Vote {

    @XmlElement(name = "PrtlVoteAllwd")
    private boolean prtlVoteAllwd;

    @XmlElement(name = "SpltVoteAllwd")
    private boolean spltVoteAllwd;

    @XmlElement(name = "VoteDdln")
    private DtOrDtTm voteDdln;

    @XmlElement(name = "VoteMktDdln")
    private DtOrDtTm voteMktDdln;

    @XmlElement(name = "BnfclOwnrDsclsr")
    private boolean bnfclOwnrDsclsr;

    @XmlElement(name = "IncntivPrm")
    private IncntivPrm incntivPrm;

    // Getter and Setter
    public boolean isPrtlVoteAllwd() { return prtlVoteAllwd; }
    public void setPrtlVoteAllwd(boolean prtlVoteAllwd) { this.prtlVoteAllwd = prtlVoteAllwd; }

    public boolean isSpltVoteAllwd() { return spltVoteAllwd; }
    public void setSpltVoteAllwd(boolean spltVoteAllwd) { this.spltVoteAllwd = spltVoteAllwd; }

    public DtOrDtTm getVoteDdln() { return voteDdln; }
    public void setVoteDdln(DtOrDtTm voteDdln) { this.voteDdln = voteDdln; }

    public DtOrDtTm getVoteMktDdln() { return voteMktDdln; }
    public void setVoteMktDdln(DtOrDtTm voteMktDdln) { this.voteMktDdln = voteMktDdln; }

    public boolean isBnfclOwnrDsclsr() { return bnfclOwnrDsclsr; }
    public void setBnfclOwnrDsclsr(boolean bnfclOwnrDsclsr) { this.bnfclOwnrDsclsr = bnfclOwnrDsclsr; }

    public IncntivPrm getIncntivPrm() { return incntivPrm; }
    public void setIncntivPrm(IncntivPrm incntivPrm) { this.incntivPrm = incntivPrm; }
}