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
package de.meetingapps.meetingportal.meetComBa;

import java.io.File;

import de.meetingapps.meetingportal.meetComAllg.CaBug;
import de.meetingapps.meetingportal.meetComEclM.EclParamM;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.annotation.Resource;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;

@RequestScoped
@Named
public class BaMailM {

    private int logDrucken = 10;

    @Inject
    EclParamM eclParamM;

    @Resource(name = "java:/meetingport")
    private Session session1;

    @Resource(name = "java:/meetingport2")
    private Session session2;

    @Resource(name = "java:/meetingport3")
    private Session session3;

    @Resource(name = "java:/meetingport4")
    private Session session4;

    @Resource(name = "java:/meetingport5")
    private Session session5;

    public int senden(String empfaenger, String betreff, String inhalt) {

        CaBug.druckeLog("eclParamM.getParam().paramPortal.absendeMailAdresse="
                + eclParamM.getParam().paramPortal.absendeMailAdresse, logDrucken, 3);
        CaBug.druckeLog("empfaenger=" + empfaenger, logDrucken, 3);
        CaBug.druckeLog("betreff=" + betreff, logDrucken, 3);
        CaBug.druckeLog("inhalt=" + inhalt, logDrucken, 3);
        try {

            Message message = null;
            switch (eclParamM.getParam().paramPortal.absendeMailAdresse) {
            case 2:
                message = new MimeMessage(session2);
                break;
            case 3:
                message = new MimeMessage(session3);
                break;
            case 4:
                message = new MimeMessage(session4);
                break;
            case 5:
                message = new MimeMessage(session5);
                break;
           default:
                message = new MimeMessage(session1);
                break;
            }

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(empfaenger));
            message.setSubject(betreff);
            message.setText(inhalt);

            Transport.send(message);

        } catch (MessagingException e) {
            CaBug.drucke("Mail Fehler 001");
            System.out.println(e);
            return -1;
            //        Logger.getLogger(Mail.class.getName()).log(Level.WARNING, "Cannot send mail", e);
        }

        return 1;
    }


    public int sendenVonAdresse1(String empfaenger, String betreff, String inhalt) {

        CaBug.druckeLog("empfaenger=" + empfaenger, logDrucken, 3);
        CaBug.druckeLog("betreff=" + betreff, logDrucken, 3);
        CaBug.druckeLog("inhalt=" + inhalt, logDrucken, 3);
        try {

            Message message = null;
            message = new MimeMessage(session1);

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(empfaenger));
            message.setSubject(betreff);
            message.setText(inhalt);

            Transport.send(message);

        } catch (MessagingException e) {
            CaBug.drucke("001");
            System.out.println(e);
            return -1;
            //        Logger.getLogger(Mail.class.getName()).log(Level.WARNING, "Cannot send mail", e);
        }

        return 1;
    }

    /**Dateianhang ="", dann wird kein Dateianhang angehängt*/

    public int sendenMitAnhang(String empfaenger, String betreff, String inhalt, String dateianhang) {
        return sendenMitAnhang(empfaenger, "", "", betreff, inhalt, dateianhang);
    }

    public int sendenMitAnhang(String empfaenger, String bcc1, String bcc2, String betreff, String inhalt,
            String dateianhang) {
        
        CaBug.druckeLog("eclParamM.getParam().paramPortal.absendeMailAdresse="
                + eclParamM.getParam().paramPortal.absendeMailAdresse, logDrucken, 3);
        CaBug.druckeLog("empfaenger=" + empfaenger, logDrucken, 3);
        CaBug.druckeLog("betreff=" + betreff, logDrucken, 3);
        CaBug.druckeLog("inhalt=" + inhalt, logDrucken, 3);
        CaBug.druckeLog("dateianhang=" + dateianhang, logDrucken, 3);

        try {
            MimeMultipart content = new MimeMultipart("mixed");
            MimeBodyPart text = new MimeBodyPart();
            text.setText(inhalt);
            content.addBodyPart(text);

            if (!dateianhang.isEmpty()) {
                BodyPart messageBodyPart = new MimeBodyPart();
                messageBodyPart.setDataHandler(new DataHandler(new FileDataSource(dateianhang)));
                messageBodyPart.setFileName(new File(dateianhang).getName());
                content.addBodyPart(messageBodyPart);
            }

            Message msg = null;
            switch (eclParamM.getParam().paramPortal.absendeMailAdresse) {
            case 2:
                msg = new MimeMessage(session2);
                break;
            case 3:
                msg = new MimeMessage(session3);
                break;
            case 4:
                msg = new MimeMessage(session4);
                break;
            case 5:
                msg = new MimeMessage(session5);
                break;
            default:
                msg = new MimeMessage(session1);
                break;
            }

            InternetAddress adressTo = new InternetAddress(empfaenger);
            msg.setRecipient(Message.RecipientType.TO, adressTo);
            if (!bcc1.isEmpty()) {
                InternetAddress adressBcc1 = new InternetAddress(bcc1);
                msg.setRecipient(Message.RecipientType.BCC, adressBcc1);
            }
            if (!bcc2.isEmpty()) {
                InternetAddress adressBcc2 = new InternetAddress(bcc2);
                msg.setRecipient(Message.RecipientType.BCC, adressBcc2);
            }

            msg.setSubject(betreff);
            msg.setContent(content);
            Transport.send(msg);

        } catch (MessagingException e) {
            CaBug.drucke("001");
           System.out.println("Mail Fehler" + e);
            return -1;
            //        Logger.getLogger(Mail.class.getName()).log(Level.WARNING, "Cannot send mail", e);
        }
        return 1;
    }

}
