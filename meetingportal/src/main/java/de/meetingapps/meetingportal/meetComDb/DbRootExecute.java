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
import java.sql.Statement;

import de.meetingapps.meetingportal.meetComAllg.CaBug;

public class DbRootExecute {

    private int logDrucken=3;
    
    public ResultSet executeQuery(PreparedStatement pstm1) {
        CaBug.druckeLog("==============================executeQuery erweitert=============================================", logDrucken, 10);
        boolean ausgefuehrt=false;
        int zaehler=0;
        ResultSet ergebnis=null;
        
        while (ausgefuehrt==false) {
            try {
                ergebnis=pstm1.executeQuery();
                ausgefuehrt=true;
            } catch (SQLException e) {
                zaehler++;
                if (zaehler<10) {
                    CaBug.drucke("----------------------------------------------------------------------execute001----------------------------");
                    System.out.println("Statment="+pstm1);
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e1) {
                    CaBug.drucke("..............Wait....................");
                    System.out.println("Statment="+pstm1);
                    e1.printStackTrace();
                }
            }
        }

        return ergebnis;

    }

    public ResultSet executeQuery(Statement pstm1, String sql) {
        CaBug.druckeLog("==============================executeQuery erweitert=============================================", logDrucken, 10);
        boolean ausgefuehrt=false;
        int zaehler=0;
        ResultSet ergebnis=null;

        while (ausgefuehrt==false) {
            try {
                ergebnis=pstm1.executeQuery(sql);
                ausgefuehrt=true;
            } catch (SQLException e) {
                zaehler++;
                if (zaehler<10) {
                    CaBug.drucke("----------------------------------------------------------------------execute001----------------------------");
                    System.out.println("Statment="+pstm1+" Sql="+sql);
                    e.printStackTrace();
                }
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e1) {
                    CaBug.drucke("..............Wait....................");
                    System.out.println("Statment="+pstm1+" Sql="+sql);
                    e1.printStackTrace();
                }
            }
        }

        return ergebnis;

    }


    public int executeUpdate(PreparedStatement pstm1) {
        CaBug.druckeLog("==============================executeUpdate erweitert=============================================", logDrucken, 10);
        //        boolean ausgefuehrt=false;
        int ergebnis=0;

        /**Bei Execute-Update auf die schnelle keine Wiederholung, da unklar ist wie der Umgang mit z.b. Duplicate Key o.ä.
         * Situationen ist
         */
        //        while (ausgefuehrt==false) {
        try {
            ergebnis=pstm1.executeUpdate();
            //                ausgefuehrt=true;
        } catch (SQLException e) {
            CaBug.drucke("----------------------------------------------------------------------execute001----------------------------");
                e.printStackTrace();
            }
//        }
        
        return ergebnis;
        
    }

    public int executeUpdate(Statement pstm1, String sql) {
        CaBug.druckeLog("==============================executeUpdate erweitert=============================================", logDrucken, 10);
//        boolean ausgefuehrt=false;
        int ergebnis=0;
        
        /**Bei Execute-Update auf die schnelle keine Wiederholung, da unklar ist wie der Umgang mit z.b. Duplicate Key o.ä.
         * Situationen ist
         */
//        while (ausgefuehrt==false) {
            try {
                ergebnis=pstm1.executeUpdate(sql);
//                ausgefuehrt=true;
            } catch (SQLException e) {
                CaBug.drucke("----------------------------------------------------------------------execute001----------------------------");
                e.printStackTrace();
            }
//        }
        
        return ergebnis;
        
    }

}
