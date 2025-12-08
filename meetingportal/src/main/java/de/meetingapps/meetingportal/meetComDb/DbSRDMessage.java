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
package de.meetingapps.meetingportal.meetComDb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import de.meetingapps.meetingportal.meetComEntities.EclSRDMessage;

public class DbSRDMessage extends DbRoot<EclSRDMessage> {

    public DbSRDMessage(DbBundle pDbBundle) {
        super(pDbBundle);
    }

    //  @formatter:off
    final public String defaultSeev = "seev.001.001.11";
    
    final public String defaultAppHeader = ""
            + "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"
            + "<RequestPayload xmlns=\"urn:iso:std:iso:20022:tech:xsd:iso20022:RequestPayload\">"
            + "<Envelope>" 
            + "<AppHdr>"
            + "    <Fr>"
            + "        <FIId>"
            + "            <FinInstnId>"
            + "                <BICFI>LINKDE00XXX</BICFI>"
            + "            </FinInstnId>"
            + "        </FIId>"
            + "    </Fr>"
            + "    <To>"
            + "        <FIId>"
            + "            <FinInstnId>"
            + "                <BICFI>PRXYGB2LXXX</BICFI>"
            + "            </FinInstnId>"
            + "        </FIId>"
            + "    </To>"
            + "    <BizMsgIdr>$BizMsgIdr</BizMsgIdr>"
            + "    <MsgDefIdr>$MsgDefIdr</MsgDefIdr>"
            + "    <CreDt>$CreDt</CreDt>"
            + "</AppHdr>"
            + "<Document xsi:type=\"xs:string\" xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">";
    
    final private String defaultDate = LocalDate.now().toString();
    
    final private String defaultDocument = "" 
            + "<Document xmlns=\"urn:iso:std:iso:20022:tech:xsd:seev.001.001.11\">"
            + "    <MtgNtfctn>"
            + "        <NtfctnGnlInf>"
            + "            <NtfctnTp>NEWM</NtfctnTp>"
            + "            <NtfctnSts>"
            + "                <EvtCmpltnsSts>COMP</EvtCmpltnsSts>"
            + "                <EvtConfSts>CONF</EvtConfSts>"
            + "            </NtfctnSts>"
            + "            <ShrhldrRghtsDrctvInd>false</ShrhldrRghtsDrctvInd>"
            + "            <ConfOfHldgReqrd>false</ConfOfHldgReqrd>"
            + "        </NtfctnGnlInf>"
            + "        <Mtg>"
            + "            <MtgId></MtgId>"
            + "            <IssrMtgId></IssrMtgId>"
            + "            <Tp>GMET</Tp>"
            + "            <AnncmntDt>"
            + "                <Dt>" + defaultDate + "</Dt>"
            + "            </AnncmntDt>"
            + "            <Prtcptn>"
            + "                <PrtcptnMtd>"
            + "                    <Cd>PHYS</Cd>"
            + "                </PrtcptnMtd>"
            + "                <IssrDdlnForVtng>"
            + "                    <DtOrDtTm>"
            + "                        <DtTm>" + defaultDate + "T23:00:00.000Z</DtTm>"
            + "                    </DtOrDtTm>"
            + "                </IssrDdlnForVtng>"
            + "            </Prtcptn>"
            + "            <Prtcptn>"
            + "                <PrtcptnMtd>"
            + "                    <Cd>PRXY</Cd>"
            + "                </PrtcptnMtd>"
            + "                <IssrDdlnForVtng>"
            + "                    <DtOrDtTm>"
            + "                        <DtTm>" + defaultDate + "T23:00:00.000Z</DtTm>"
            + "                    </DtOrDtTm>"
            + "                </IssrDdlnForVtng>"
            + "            </Prtcptn>"
            + "            <Prtcptn>"
            + "                <PrtcptnMtd>"
            + "                    <Cd>EVOT</Cd>"
            + "                </PrtcptnMtd>"
            + "                <IssrDdlnForVtng>"
            + "                    <DtOrDtTm>"
            + "                        <DtTm>" + defaultDate + "T23:00:00.000Z</DtTm>"
            + "                    </DtOrDtTm>"
            + "                </IssrDdlnForVtng>"
            + "            </Prtcptn>"
            + "            <Attndnc>"
            + "                <ConfMktDdln>"
            + "                    <DtOrDtTm>"
            + "                        <DtTm>" + defaultDate + "T23:00:00.000Z</DtTm>"
            + "                    </DtOrDtTm>"
            + "                </ConfMktDdln>"
            + "            </Attndnc>"
            + "            <AddtlDcmnttnURLAdr></AddtlDcmnttnURLAdr>"
            + "            <AddtlPrcdrDtls>"
            + "                <AddtlRght>"
            + "                    <Cd>RSPS</Cd>"
            + "                </AddtlRght>"
            + "                <AddtlRghtInfURLAdr></AddtlRghtInfURLAdr>"
            + "                <AddtlRghtMktDdln>"
            + "                    <DtOrDtTm>"
            + "                        <DtTm>" + defaultDate + "T23:00:00.000Z</DtTm>"
            + "                    </DtOrDtTm>"
            + "                </AddtlRghtMktDdln>"
            + "            </AddtlPrcdrDtls>"
            + "            <PrxyChc>"
            + "                <Prxy>"
            + "                    <MktDdln>"
            + "                        <DtOrDtTm>"
            + "                            <DtTm>" + defaultDate + "T17:00:00.000Z</DtTm>"
            + "                        </DtOrDtTm>"
            + "                    </MktDdln>"
            + "                    <AuthrsdPrxy>"
            + "                        <PrxyTp>NEPR</PrxyTp>"
            + "                        <PrsnDtls>"
            + "                            <PrssgndPrxy>"
            + "                                <NtrlPrsn>"
            + "                                    <NmAndAdr>"
            + "                                        <FrstNm></FrstNm>"
            + "                                        <Srnm></Srnm>"
            + "                                    </NmAndAdr>"
            + "                                    <Id>"
            + "                                        <Id></Id>"
            + "                                    </Id>"
            + "                                </NtrlPrsn>"
            + "                            </PrssgndPrxy>"
            + "                        </PrsnDtls>"
            + "                    </AuthrsdPrxy>"
            + "                    <AuthrsdPrxy>"
            + "                        <PrxyTp>DISC</PrxyTp>"
            + "                    </AuthrsdPrxy>"
            + "                </Prxy>"
            + "            </PrxyChc>"
            + "            <SctiesBlckgPrdEndDt>"
            + "                <DtCd>"
            + "                    <Cd>RDTE</Cd>"
            + "                </DtCd>"
            + "            </SctiesBlckgPrdEndDt>"
            + "            <EntitlmntFxgDt>"
            + "                <Dt>"
            + "                    <Dt>" + defaultDate + "</Dt>"
            + "                </Dt>"
            + "            </EntitlmntFxgDt>"
            + "        </Mtg>"
            + "        <MtgDtls>"
            + "            <DtAndTm>"
            + "                <DtOrDtTm>"
            + "                    <DtTm>" + defaultDate + "T09:00:00.000Z</DtTm>"
            + "                </DtOrDtTm>"
            + "            </DtAndTm>"
            + "            <DtSts>CNFR</DtSts>"
            + "            <Lctn>"
            + "                <Adr>"
            + "                    <AdrTp>ADDR</AdrTp>"
            + "                    <AdrLine></AdrLine>"
            + "                    <StrtNm></StrtNm>"
            + "                    <BldgNb></BldgNb>"
            + "                    <PstCd></PstCd>"
            + "                    <TwnNm></TwnNm>"
            + "                    <Ctry></Ctry>"
            + "                </Adr>"
            + "            </Lctn>"
            + "        </MtgDtls>"
            + "        <Issr>"
            + "            <Id>"
            + "                <NmAndAdr>"
            + "                    <Nm></Nm>"
            + "                    <Adr>"
            + "                        <AdrTp>ADDR</AdrTp>"
            + "                        <StrtNm></StrtNm>"
            + "                        <BldgNb></BldgNb>"
            + "                        <PstCd></PstCd>"
            + "                        <TwnNm></TwnNm>"
            + "                        <Ctry></Ctry>"
            + "                    </Adr>"
            + "                </NmAndAdr>"
            + "            </Id>"
            + "        </Issr>"
            + "        <IssrAgt>"
            + "            <Id>"
            + "                <NmAndAdr>"
            + "                    <Nm>Link Market Services GmbH</Nm>"
            + "                </NmAndAdr>"
            + "            </Id>"
            + "            <Role>PRIN</Role>"
            + "        </IssrAgt>"
            + "        <Scty>"
            + "            <FinInstrmId>"
            + "                <ISIN></ISIN>"
            + "            </FinInstrmId>"
            + "        </Scty>"
            + "        <Vote>"
            + "            <PrtlVoteAllwd>false</PrtlVoteAllwd>"
            + "            <SpltVoteAllwd>false</SpltVoteAllwd>"
            + "            <VoteMktDdln>"
            + "                <DtOrDtTm>"
            + "                    <DtTm>" + defaultDate + "T23:00:00.000Z</DtTm>"
            + "                </DtOrDtTm>"
            + "            </VoteMktDdln>"
            + "            <VoteMthds>"
            + "                <VoteThrghNtwk>"
            + "                    <VoteChanl>VOCI</VoteChanl>"
            + "                </VoteThrghNtwk>"
            + "            </VoteMthds>"
            + "            <BnfclOwnrDsclsr>true</BnfclOwnrDsclsr>"
            + "        </Vote>"
            + "    </MtgNtfctn>"
            + "</Document>";

    @Override
    String getCreateString() {
       return "CREATE TABLE IF NOT EXISTS " + getSchema() + getTableName()+" ( "
                + "`id` int(11) AUTO_INCREMENT NOT NULL, "
                + "`seev` varchar(15) DEFAULT NULL, "
                + "`isin` varchar(12) DEFAULT NULL, "
                + "`appHeader` TEXT NOT NULL, "
                + "`document` TEXT NOT NULL, "
                + "`meetingId` varchar(35) DEFAULT NULL, "
                + "`issuerMeetingId` varchar(35) DEFAULT NULL, "
                + "`mtgDtAndTm` varchar(24) DEFAULT NULL, "
                + "`type` varchar(4) DEFAULT NULL, "
                + "`messageId` varchar(35) DEFAULT NULL, "
                + "`prevMessageId` varchar(35) DEFAULT NULL, "
                + "`responseMessage` TEXT DEFAULT NULL, "
                + "`sent_at` TIMESTAMP DEFAULT NULL, "
                + "`updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP, "
                + "PRIMARY KEY (`id`))";
    //  @formatter:on
    }

    @Override
    String getSchema() {
        return dbBundle.getSchemaMandant();
    }

    @Override
    String getTableName() {
        return "tbl_srd_message";
    }

    private EclSRDMessage createData(ResultSet rs) throws SQLException {
        return new EclSRDMessage(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7), rs.getString(8),
                rs.getString(9), rs.getString(10), rs.getString(11), rs.getString(12), rs.getTimestamp(13), rs.getTimestamp(14));
    }

    @Override
    public int insert(EclSRDMessage value) {

        int erg = -1;

        final String sql = "INSERT INTO " + getSchema() + getTableName()
                + "(seev, isin, appHeader, document, meetingId, issuerMeetingId, mtgDtAndTm, type, messageId, prevMessageId, responseMessage, sent_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(1, value.getSeev());
            ps.setString(2, value.getIsin());
            ps.setString(3, value.getAppHeader());
            ps.setString(4, value.getDocument());
            ps.setString(5, value.getMeetingId());
            ps.setString(6, value.getIssuerMeetingId());
            ps.setString(7, value.getMtgDtAndTime());
            ps.setString(8, value.getType());
            ps.setString(9, value.getMessageId());
            ps.setString(10, value.getPrevMessageId());
            ps.setString(11, value.getResponseMessage());
            ps.setTimestamp(12, value.getSent_at());

            erg = executeUpdate(ps);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return erg;
    }

    public int insertDefault(String isin) {
        return insert(new EclSRDMessage(0, defaultSeev, isin, defaultAppHeader.replaceAll("(?<=>)(\\s+)(?=<)", ""),
                defaultDocument.replaceAll("(?<=>)(\\s+)(?=<)", ""), null, null, null, null, null, null, null, null, null));
    }

    public int update(EclSRDMessage value) {

        int erg = -1;

        final String sql = "UPDATE " + getSchema() + getTableName()
                + " SET seev=?, isin=?, appHeader=?, document=?, meetingId=?, issuerMeetingId=?, mtgDtAndTm=?, type=?, messageId=?, prevMessageId=?, responseMessage=?, sent_at=? WHERE id=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            ps.setString(1, value.getSeev());
            ps.setString(2, value.getIsin());
            ps.setString(3, value.getAppHeader());
            ps.setString(4, value.getDocument());
            ps.setString(5, value.getMeetingId());
            ps.setString(6, value.getIssuerMeetingId());
            ps.setString(7, value.getMtgDtAndTime());
            ps.setString(8, value.getType());
            ps.setString(9, value.getMessageId());
            ps.setString(10, value.getPrevMessageId());
            ps.setString(11, value.getResponseMessage());
            ps.setTimestamp(12, value.getSent_at());
            ps.setInt(13, value.getId());

            erg = executeUpdate(ps);

        } catch (SQLException e) {
            e.printStackTrace();
            return erg;
        }
        return erg;
    }

    public List<EclSRDMessage> readAll() {

        final List<EclSRDMessage> list = new ArrayList<>();
        final String sql = "SELECT * FROM " + getSchema() + getTableName();

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {

            try (ResultSet rs = executeQuery(ps)) {
                while (rs.next()) {
                    list.add(createData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    
    public EclSRDMessage readById(int id) {
        final String sql = "SELECT * FROM " + getSchema() + getTableName() + " WHERE id=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {
            
            ps.setInt(1, id);

            try (ResultSet rs = executeQuery(ps)) {
                if (rs.next()) {
                    return createData(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public List<EclSRDMessage> readByIsin(String isin) {
        
        final List<EclSRDMessage> list = new ArrayList<>();
        final String sql = "SELECT * FROM " + getSchema() + getTableName() + " WHERE isin=?";

        try (PreparedStatement ps = verbindung.prepareStatement(sql)) {
            
            ps.setString(1, isin);

            try (ResultSet rs = executeQuery(ps)) {
                while (rs.next()) {
                    list.add(createData(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    void resetInterneIdent(int pHoechsteIdent) {
    }

    @Override
    String getFeldFuerInterneIdent() {
        return "";
    }

    @Override
    int getAnzFelder() {
        return 0;
    }

    @Override
    EclSRDMessage decodeErgebnis(ResultSet pErgebnis) {
        return null;
    }

    @Override
    void fuellePreparedStatementKomplett(PreparedStatement pPStm, int pOffset, EclSRDMessage pEcl) {
    }

}
