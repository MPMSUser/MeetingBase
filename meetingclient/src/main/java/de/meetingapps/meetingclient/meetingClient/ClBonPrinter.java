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
package de.meetingapps.meetingclient.meetingClient;

import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;

/**
 * The Class ClBonPrinter.
 */
public class ClBonPrinter {

    /** The votes. */
    public String votes = "";

    /**
     * Drucken.
     */
    public void drucken() {

        PrintService[] services = PrinterJob.lookupPrintServices();

        String votes1 = "u(3Lx02" + votes + "oPrEsF2=";

        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.update(votes1.getBytes(StandardCharsets.UTF_8));
            String ergebnis = new String(md.digest());
            votes = votes + "Pruefzahl B: " + ergebnis; //Ursprünglich: md.digest()

        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }

        votes = votes + "\n\n\n\n\n\n\n";

        for (PrintService ps : services) {
            if (ps.getName().equals("???LabelPrinter???")) {
                try {
                    print(ps);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (PrinterException e) {
                    e.printStackTrace();
                } catch (PrintException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

    }

    /**
     * Prints the.
     *
     * @param ps the ps
     * @throws PrinterException             the printer exception
     * @throws UnsupportedEncodingException the unsupported encoding exception
     * @throws PrintException               the print exception
     */
    private void print(PrintService ps) throws PrinterException, UnsupportedEncodingException, PrintException {
        DocPrintJob job = ps.createPrintJob();
        InputStream is = new ByteArrayInputStream(votes.getBytes("UTF8"));
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        pras.add(new Copies(1));
        DocFlavor flavor = DocFlavor.INPUT_STREAM.AUTOSENSE;
        Doc doc = new SimpleDoc(is, flavor, null);
        job.print(doc, pras);
    }

}
