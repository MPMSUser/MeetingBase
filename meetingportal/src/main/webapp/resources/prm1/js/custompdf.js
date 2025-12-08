/*jshint esversion: 6 */
const { PDFDocument } = PDFLib;

async function fillForm() {
    // Fetch the PDF with form fields
    const formUrl = '../jakarta.faces.resource/pdf/Beitrittserklaerung_sepa.pdf.xhtml?ln=prm1';
    const formPdfBytes = await fetch(formUrl).then(res => res.arrayBuffer());

    // Load a PDF with form fields
    const pdfDoc = await PDFDocument.load(formPdfBytes);

    // Get the form containing all the fields
    const form = pdfDoc.getForm();

    const chkAnrede = form.getRadioGroup('Gruppe1');
    //const chkAnredeOptions = chkAnrede.getOptions();
    const txtName = form.getTextField('Nachname/Firma');
    const txtVorname = form.getTextField('Vorname/Ansprechpartner');
    const txtStrasseHausnummer = form.getTextField('Str./Haus-Nr');
    const txtAdresszusatz = form.getTextField('Adresszusatz');
    const txtPostleitzahlOrt = form.getTextField('PLZ/Ort');
    const txtTelefon = form.getTextField('Telefon');
    const txtEMail = form.getTextField('E-Mail');
    const txtSteuerID = form.getTextField('Steueridentifikationsnummer');
    const txtGeburtsdatum = form.getTextField('Geburtsdatum');

    const chkErklaerung = form.getRadioGroup('Gruppe2');
    const txtMitgliedsnummer = form.getTextField('Mitgliedsnummer');
    const txtAnzahlWeitereAnteile = form.getTextField('Erhöhung Anzahl Geschäftsanteile');

    const radioZahlungsart = form.getRadioGroup('Gruppe4')

    const radioAuszahlung = form.getRadioGroup('Gruppe6')

    const chkKontoInhaberAbweichend = form.getCheckBox('Name Kto-Inhaber abweichend');
    const txtKontoInhaberAbweichendName = form.getTextField('Name Kto-Inhaber Einzahlung');
    const txtKontoInhaberAbweichendStrasse = form.getTextField('Str/Nr Kto-Inhaber Einzahlung');
    const txtKontoInhaberAbweichendPlzOrt = form.getTextField('PLZ/Ort Kto-Inhaber Einzahlung');
    const txtBankAbweichend = form.getTextField('bei Kreditinstitut Einzahlung');
    const txtIBANAbweichend = form.getTextField('IBAN Einzahlung');
    const txtBICAbweichend = form.getTextField('BIC Einzahlung');

    const txtKontoInhaber = form.getTextField('Name Kto-Inhaber Auszahlung');
    const txtBank = form.getTextField('bei Kreditinstitut Auszahlung');
    const txtIBAN = form.getTextField('IBAN Auszahlung');
    const txtBIC = form.getTextField('BIC Auszahlung');

    const radioDigitalMagazin = form.getRadioGroup('Gruppe5');

    var aktionaerAnrede = String(document.getElementById('inhaltsform:hidden-aktionaerAnrede').value);
    var aktionaerNachname = String(document.getElementById('inhaltsform:hidden-aktionaerNachname').value);
    var aktionaerVorname = String(document.getElementById('inhaltsform:hidden-aktionaerVorname').value);
    var aktionaerStrasse = String(document.getElementById('inhaltsform:hidden-aktionaerStrasse').value);
    var aktionaerAdresszusatz = String(document.getElementById('inhaltsform:hidden-aktionaerAdresszusatz').value);
    var aktionaerPLZ = String(document.getElementById('inhaltsform:hidden-aktionaerPLZ').value);
    var aktionaerOrt = String(document.getElementById('inhaltsform:hidden-aktionaerOrt').value);
    var aktionaerTelefon1 = String(document.getElementById('inhaltsform:hidden-aktionaerTelefon1').value);
    var aktionaerEmailPortal = String(document.getElementById('inhaltsform:hidden-aktionaerEmailPortal').value);
    var anmeldeKennungFuerAnzeige = String(document.getElementById('inhaltsform:hidden-anmeldeKennungFuerAnzeige').value);
    var weitereAnteile = String(document.getElementById('inhaltsform:hidden-weitereAnteile').value);
    var bankAktionaersname = String(document.getElementById('inhaltsform:hidden-bankAktionaersname').value);
    var bankBankname = String(document.getElementById('inhaltsform:hidden-bankBankname').value);
    var bankIban = String(document.getElementById('inhaltsform:hidden-bankIban').value);
    var bankBic = String(document.getElementById('inhaltsform:hidden-bankBic').value);
    var versandartNewsletter = String(document.getElementById('inhaltsform:hidden-newsletter').value);
    var steuerId = String(document.getElementById('inhaltsform:hidden-steuerId').value);
    var telefon = String(document.getElementById('inhaltsform:hidden-telefon').value);
    var geburtsdatum = String(document.getElementById('inhaltsform:hidden-geburtsdatum').value);
    var zahlungsart = String(document.getElementById('inhaltsform:hidden-zahlungsart').value);
    var sepaKontoAbweichend = String(document.getElementById('inhaltsform:hidden-sepaKontoAbweichend').value);
    var sepaKontoAbweichendAnrede = String(document.getElementById('inhaltsform:hidden-sepaKontoAbweichendAnrede').value);
    var sepaKontoAbweichendVorname = String(document.getElementById('inhaltsform:hidden-sepaKontoAbweichendVorname').value);
    var sepaKontoAbweichendNachname = String(document.getElementById('inhaltsform:hidden-sepaKontoAbweichendNachname').value);
    var sepaKontoAbweichendBank = String(document.getElementById('inhaltsform:hidden-sepaKontoAbweichendBank').value);
    var sepaKontoAbweichendIBAN = String(document.getElementById('inhaltsform:hidden-sepaKontoAbweichendIBAN').value);
    var sepaKontoAbweichendBIC = String(document.getElementById('inhaltsform:hidden-sepaKontoAbweichendBIC').value);
    var sepaKontoAbweichendStrasse = String(document.getElementById('inhaltsform:hidden-sepaKontoAbweichendStrasse').value);
    var sepaKontoAbweichendPLZ = String(document.getElementById('inhaltsform:hidden-sepaKontoAbweichendPLZ').value);
    var sepaKontoAbweichendOrt = String(document.getElementById('inhaltsform:hidden-sepaKontoAbweichendOrt').value);

    //chkAnrede.select(chkAnredeOptions[0]);
    txtName.setText(aktionaerNachname.substring(0, 40));
    txtVorname.setText(aktionaerVorname);
    txtStrasseHausnummer.setText(aktionaerStrasse);
    txtAdresszusatz.setText(aktionaerAdresszusatz);
    txtPostleitzahlOrt.setText((aktionaerPLZ + " " + aktionaerOrt).trim());
    txtTelefon.setText(telefon.substring(0, 40));
    txtEMail.setText(aktionaerEmailPortal);
    txtSteuerID.setText(steuerId.substring(0, 40));
    txtGeburtsdatum.setText(geburtsdatum.substring(0, 40));
    chkErklaerung.select('Erhöhung');
    txtMitgliedsnummer.setText(anmeldeKennungFuerAnzeige);
    txtAnzahlWeitereAnteile.setText(weitereAnteile);
    radioAuszahlung.select('anderes Konto');
    txtKontoInhaber.setText(bankAktionaersname);
    txtBank.setText(bankBankname);
    txtIBAN.setText(bankIban);
    txtBIC.setText(bankBic);
    console.log(aktionaerAnrede);
    if(aktionaerAnrede == "Herr") {
        chkAnrede.select("Herr");
    } else if(aktionaerAnrede == "Frau") {
        chkAnrede.select("Frau");
    } else if(aktionaerAnrede == "divers") {
        chkAnrede.select("divers");
    } else if(aktionaerAnrede == "Firma") {
        chkAnrede.select("juristische Person");
    }
    if (zahlungsart == "0") {
        radioZahlungsart.select('Überweisung');
    } else if (zahlungsart == "1") {
        radioZahlungsart.select('Lastschrift');

        if (sepaKontoAbweichend == "true") {
            chkKontoInhaberAbweichend.check();
            txtKontoInhaberAbweichendName.setText((sepaKontoAbweichendVorname + " " + sepaKontoAbweichendNachname).trim());
            txtKontoInhaberAbweichendStrasse.setText(sepaKontoAbweichendStrasse);
            txtKontoInhaberAbweichendPlzOrt.setText((sepaKontoAbweichendPLZ + " " + sepaKontoAbweichendOrt).trim());
            txtBankAbweichend.setText(sepaKontoAbweichendBank);
            txtIBANAbweichend.setText(sepaKontoAbweichendIBAN);
            txtBICAbweichend.setText(sepaKontoAbweichendBIC);
        } else {
            radioAuszahlung.select('gleiches Konto');
            txtKontoInhaberAbweichendName.setText('');
            txtBankAbweichend.setText(bankBankname);
            txtIBANAbweichend.setText(bankIban);
            txtBICAbweichend.setText(bankBic);
            txtKontoInhaber.setText("");
            txtBank.setText("");
            txtIBAN.setText("");
            txtBIC.setText("");
        }
    }

    if (versandartNewsletter == 2) {
        radioDigitalMagazin.select('PJ digital');
    } else if (versandartNewsletter == 3) {
        radioDigitalMagazin.select('kein PJ');
    }

    form.flatten();

    const pdfBytes = await pdfDoc.save();
    var base64 = bytesToBase64(pdfBytes);
    let b64 = "data:application/pdf;base64," + base64;
    PDFObject.embed(b64, "#viewBeitragsPDFVorschau", { height: "500px" });
}