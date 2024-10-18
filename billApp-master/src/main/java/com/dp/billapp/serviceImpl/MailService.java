package com.dp.billapp.serviceImpl;

import com.dp.billapp.model.InvoiceItem;
import com.dp.billapp.model.InvoiceResponse;
import com.itextpdf.html2pdf.HtmlConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Files;
import java.util.List;


@Service
public class MailService{

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private InvoiceServiceImpl invoiceService;

    public void generate(long invoiceId, String customerEmail) throws IOException, MessagingException {

        InvoiceResponse invoiceResponse = invoiceService.getInvoiceById(invoiceId);
        String htmlResponse =  generateHtmlForInvoice(invoiceResponse);
        String byteFileName = invoiceResponse.getInvoiceId();
        byte[] response = generatePdfFromHtml(htmlResponse,byteFileName);
        String pdfName = byteFileName+".pdf";
        OutputStream out = new FileOutputStream(pdfName);
        out.write(response);
        out.close();

        getLocationOfPdf(pdfName,customerEmail);

    }

    private String generateHtmlForInvoice(InvoiceResponse invoiceResponse){

        String invoiceItemList = "";
        double totalAmount = 0;
        List<InvoiceItem> invoiceDetails = invoiceResponse.getInvoiceDetails();
        String item ="";
        for(InvoiceItem invoiceItem:invoiceDetails){
            totalAmount+=Double.parseDouble(invoiceItem.getAmount());
            item += "<tr>\n" +
                    "                        <td>"+invoiceItem.getSerialNo()+"</td>\n" +
                    "                        <td>1</td>\n" +
                    "                        <td>"+invoiceItem.getName()+"</td>\n" +
                    "                        <td>"+invoiceItem.getPurity()+"</td>\n" +
                    "                        <td>"+invoiceItem.getHsnCode()+"</td>\n" +
                    "                        <td>"+invoiceItem.getGrossWeight()+"</td>\n" +
                    "                        <td>"+invoiceItem.getNetWeight()+"</td>\n" +
                    "                        <td>"+invoiceItem.getRateOfGoldPerGram()+"</td>\n" +
                    "                        <td>"+invoiceItem.getValueOfGold()+"</td>\n" +
                    "                        <td>"+invoiceItem.getMakingCharges()+"</td>\n" +
                    "                        <td>"+invoiceItem.getHallMarkingCharges()+"</td>\n" +
                    "                        <td>"+invoiceItem.getAmount()+"</td>\n" +
                    "                    </tr>\n";
        }
        double gstValue = (totalAmount*1.5)/100;

        String bank = "";

        String developer ="<div style=\"margin-top: 15px;\">\n" +
                "\n" +
                "            <img src=\"https://res.cloudinary.com/dq5jpef6l/image/upload/v1677679313/tiltgreen_yanlxi.png\"\n" +
                "                style=\"width: 120px; height: 40px;\" alt=\"\">\n" +
                "        </div>\n" +
                "    </div>\n" ;
        if(invoiceResponse.getPaymentType().equals("bank")){
            bank +=
                    "                <h5\n" +
                    "                    style=\"color: red; font-weight: bold;margin-block-start: 0em;margin-block-end: 0em;margin-top: 1em;margin-bottom: 0.1em;font-size: 15px;\">\n" +
                    "                    Bank Details: </h5>\n" +
                    "                <p\n" +
                    "                    style=\"color: gray;font-size: 12px;margin-block-start: 0em;margin-block-end: 0em;margin-top: 0.2em;margin-bottom: 0.1em;\">\n" +
                    "                    <strong>Name: </strong><span id=\"bankName\">"+invoiceResponse.getBankDetails().getName()+"</span>\n" +
                    "                </p>\n" +
                    "                <p\n" +
                    "                    style=\"color: gray;font-size: 12px;margin-block-start: 0em;margin-block-end: 0em;margin-top: 0.2em;margin-bottom: 0.1em;\">\n" +
                    "                    <strong>Account Number: </strong><span id=\"bankAccNo\">"+invoiceResponse.getBankDetails().getAccountNumber()+"</span>\n" +
                    "                </p>\n" +
                    "                <p\n" +
                    "                    style=\"color: gray;font-size: 12px;margin-block-start: 0em;margin-block-end: 0em;margin-top: 0.2em;margin-bottom: 0.1em;\">\n" +
                    "                    <strong>IFSC Code: </strong><span id=\"bankIfscCode\">"+invoiceResponse.getBankDetails().getIfscCode()+"</span>\n" +
                    "                </p>\n"
                     ;
        }
        else{
            bank =
                    "            <img src=\"https://res.cloudinary.com/dq5jpef6l/image/upload/v1677679313/tiltgreen_yanlxi.png\"\n" +
                    "                style=\"width: 120px; margin-right: 60px;  height: 40px;\" alt=\"\">\n";
            developer = "";
        }


        String html ="<!DOCTYPE html>" +
                "<html>" +
                "<head>" +
                "    <title>Hello, World!</title>" +
                "</head>" +
                "<body style='margin: 0px;'>" +
                "    <div class='page' style='width:210mm;margin: 0px;'>" +
                "        <div class='container'>" +
                "            <div style='background-color:#f8f9fa; border-radius: 5px 5px;display:flex;justify-content: space-between;width: 90%;'>" +
                "                <div class='col-9' style='width: -webkit-fill-available;margin-top: 15px;'>" +
                "                    <div style='display: flex;'>" +
                "                        <img style='margin-right: 20px;margin-left: 20px;border-radius: 50%;' width='50px'" +
                "                            height='50px' id='showroomLogo'" +
                "                            src="+invoiceResponse.getShowroom().getShowroomLogo()+">" +
                "                        <h1 class='text-center mt-3' id='showroomName' style='color: red; font-size: 25px; margin-block-end: 0em;margin-top: 0.1em;margin-bottom: 0.1em;'" +
                "          margin-block-start: 0em;'><strong>"+invoiceResponse.getShowroom().getName()+"</strong></h1>" +
                "                        <img style='margin-left: 20px;border-radius: 50%;' width='50px' height='50px'" +
                "                            id='hallmarkLogo'" +
                "                            src="+invoiceResponse.getShowroom().getHallMarkLogo()+">" +
                "                    </div>" +
                "                    <p style='color: black;text-align: center;font-family: sans-serif;" +
                "        font-size: 10px;margin-left: 30px;'>" +
                "                        <span >" +
                "                            <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"10\" height=\"10\" fill=\"currentColor\"\n" +
                "                                class=\"bi bi-shop-window\" viewBox=\"0 0 16 16\">\n" +
                "                                <path\n" +
                "                                    d=\"M2.97 1.35A1 1 0 0 1 3.73 1h8.54a1 1 0 0 1 .76.35l2.609 3.044A1.5 1.5 0 0 1 16 5.37v.255a2.375 2.375 0 0 1-4.25 1.458A2.371 2.371 0 0 1 9.875 8 2.37 2.37 0 0 1 8 7.083 2.37 2.37 0 0 1 6.125 8a2.37 2.37 0 0 1-1.875-.917A2.375 2.375 0 0 1 0 5.625V5.37a1.5 1.5 0 0 1 .361-.976l2.61-3.045zm1.78 4.275a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0 1.375 1.375 0 1 0 2.75 0V5.37a.5.5 0 0 0-.12-.325L12.27 2H3.73L1.12 5.045A.5.5 0 0 0 1 5.37v.255a1.375 1.375 0 0 0 2.75 0 .5.5 0 0 1 1 0zM1.5 8.5A.5.5 0 0 1 2 9v6h12V9a.5.5 0 0 1 1 0v6h.5a.5.5 0 0 1 0 1H.5a.5.5 0 0 1 0-1H1V9a.5.5 0 0 1 .5-.5zm2 .5a.5.5 0 0 1 .5.5V13h8V9.5a.5.5 0 0 1 1 0V13a1 1 0 0 1-1 1H4a1 1 0 0 1-1-1V9.5a.5.5 0 0 1 .5-.5z\" />\n" +
                "                            </svg>\n" +
                "                        </span>\n" +
                "                        <span class=\"me-3\" id=\"showroomAddress\" style=\"margin-right: 10px;\">"+invoiceResponse.getShowroom().getAddress()+"</span>\n" +
                "                        <span><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"10\" height=\"10\" fill=\"currentColor\"\n" +
                "                                class=\"bi bi-telephone\" viewBox=\"0 0 16 16\">\n" +
                "                                <path\n" +
                "                                    d=\"M3.654 1.328a.678.678 0 0 0-1.015-.063L1.605 2.3c-.483.484-.661 1.169-.45 1.77a17.568 17.568 0 0 0 4.168 6.608 17.569 17.569 0 0 0 6.608 4.168c.601.211 1.286.033 1.77-.45l1.034-1.034a.678.678 0 0 0-.063-1.015l-2.307-1.794a.678.678 0 0 0-.58-.122l-2.19.547a1.745 1.745 0 0 1-1.657-.459L5.482 8.062a1.745 1.745 0 0 1-.46-1.657l.548-2.19a.678.678 0 0 0-.122-.58L3.654 1.328zM1.884.511a1.745 1.745 0 0 1 2.612.163L6.29 2.98c.329.423.445.974.315 1.494l-.547 2.19a.678.678 0 0 0 .178.643l2.457 2.457a.678.678 0 0 0 .644.178l2.189-.547a1.745 1.745 0 0 1 1.494.315l2.306 1.794c.829.645.905 1.87.163 2.611l-1.034 1.034c-.74.74-1.846 1.065-2.877.702a18.634 18.634 0 0 1-7.01-4.42 18.634 18.634 0 0 1-4.42-7.009c-.362-1.03-.037-2.137.703-2.877L1.885.511z\" />\n" +
                "                            </svg>\n" +
                "                        </span>\n" +
                "                        <span id=\"showroomPrimaryContact\">"+invoiceResponse.getShowroom().getPrimaryContact()+" / </span>\n" +
                "                        <span style=\"margin-right: 10px;\" id=\"showroomSecondaryContact\">"+invoiceResponse.getShowroom().getSecondaryContact()+"</span>\n" +
                "                        <span><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"10\" height=\"10\" fill=\"currentColor\"\n" +
                "                                class=\"bi bi-envelope-at\" viewBox=\"0 0 16 16\">\n" +
                "                                <path\n" +
                "                                    d=\"M2 2a2 2 0 0 0-2 2v8.01A2 2 0 0 0 2 14h5.5a.5.5 0 0 0 0-1H2a1 1 0 0 1-.966-.741l5.64-3.471L8 9.583l7-4.2V8.5a.5.5 0 0 0 1 0V4a2 2 0 0 0-2-2H2Zm3.708 6.208L1 11.105V5.383l4.708 2.825ZM1 4.217V4a1 1 0 0 1 1-1h12a1 1 0 0 1 1 1v.217l-7 4.2-7-4.2Z\" />\n" +
                "                                <path\n" +
                "                                    d=\"M14.247 14.269c1.01 0 1.587-.857 1.587-2.025v-.21C15.834 10.43 14.64 9 12.52 9h-.035C10.42 9 9 10.36 9 12.432v.214C9 14.82 10.438 16 12.358 16h.044c.594 0 1.018-.074 1.237-.175v-.73c-.245.11-.673.18-1.18.18h-.044c-1.334 0-2.571-.788-2.571-2.655v-.157c0-1.657 1.058-2.724 2.64-2.724h.04c1.535 0 2.484 1.05 2.484 2.326v.118c0 .975-.324 1.39-.639 1.39-.232 0-.41-.148-.41-.42v-2.19h-.906v.569h-.03c-.084-.298-.368-.63-.954-.63-.778 0-1.259.555-1.259 1.4v.528c0 .892.49 1.434 1.26 1.434.471 0 .896-.227 1.014-.643h.043c.118.42.617.648 1.12.648Zm-2.453-1.588v-.227c0-.546.227-.791.573-.791.297 0 .572.192.572.708v.367c0 .573-.253.744-.564.744-.354 0-.581-.215-.581-.8Z\" />\n" +
                "                            </svg>\n" +
                "                        </span>\n" +
                "                        <span class=\"me-3\" id=\"showroomEmail\" style=\"margin-right: 10px;\"\n" +
                "          >"+invoiceResponse.getShowroom().getEmail()+"</span>\n" +
                "\n" +
                "                    </p>\n" +
                "\n" +
                "                </div>\n" +
                "                <div style=\"background-color: #001d3d;  border-radius:5px 5px 5px 30px; padding: 20px 7px 0px 25px;margin-left: 60px;\">\n" +
                "                    <div style=\"background-color: #001d3d;width: max-content;\">\n" +
                "                        <h6\n" +
                "                            style=\"color: white;margin-block-start: 0.0em;margin-block-end: 0.0em;margin-top: 0.1em;margin-bottom: 0.1em;font-size: 18px;\">\n" +
                "                            <strong>INVOICE</strong>\n" +
                "                        </h6>\n" +
                "                        <p\n" +
                "                            style=\"color: white;margin-block-start: 0.0em;margin-block-end: 0.0em;margin-top: 0.1em;margin-bottom: 0.1em;font-size: 12px;\">\n" +
                "                            Invoice\n" +
                "                            Id: <span class=\"p ms-2 text-end\" id=\"invoice_id\">"+invoiceResponse.getInvoiceId()+"</span></p>\n" +
                "                        <p\n" +
                "                            style=\"color: white;margin-block-start: 0.0em;margin-block-end: 0.0em;margin-top: 0.1em;margin-bottom: 0.1em;font-size: 12px;\">\n" +
                "                            Invoice\n" +
                "                            Date: <span class=\"p ms-2 text-end\" id=\"invoice_date\">"+invoiceResponse.getInvoiceDate()+"</span></p>\n" +
                "                    </div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "\n" +
                "        </div>\n" +
                "\n" +
                "        <!-- customer information -->\n" +
                "\n" +
                "        <div class=\"row\" style=\"display: flex;\n" +
                "                justify-content: space-between;\">\n" +
                "\n" +
                "            <div class=\"col-md-8\">\n" +
                "                <h5\n" +
                "                    style=\"color: red; font-weight: bold;margin-block-start: 0em;margin-block-end: 0em;margin-top: 0.5em;margin-bottom: 0.1em;font-size: 15px;\">\n" +
                "                    Customer</h5>\n" +
                "                <p style=\"color: black;margin-block-start: 0em;font-size: 12px;\n" +
                "                        margin-block-end: 0em;margin-top: 0.2em;margin-bottom: 0em;\"><span id=\"customerName\"\n" +
                "                        style=\"font-family: sans-serif;font-weight: 700;\">"+invoiceResponse.getCustomer().getName()+"</span></p>\n" +
                "                <p\n" +
                "                    style=\"color: gray;font-size: 12px;margin-block-start: 0em;margin-block-end: 0em;margin-top: 0.2em;margin-bottom: 0.1em;\">\n" +
                "                    <span class=\"me-1\"><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"12\" height=\"12\"\n" +
                "                            fill=\"currentColor\" class=\"bi bi-telephone\" viewBox=\"0 0 16 16\">\n" +
                "                            <path\n" +
                "                                d=\"M3.654 1.328a.678.678 0 0 0-1.015-.063L1.605 2.3c-.483.484-.661 1.169-.45 1.77a17.568 17.568 0 0 0 4.168 6.608 17.569 17.569 0 0 0 6.608 4.168c.601.211 1.286.033 1.77-.45l1.034-1.034a.678.678 0 0 0-.063-1.015l-2.307-1.794a.678.678 0 0 0-.58-.122l-2.19.547a1.745 1.745 0 0 1-1.657-.459L5.482 8.062a1.745 1.745 0 0 1-.46-1.657l.548-2.19a.678.678 0 0 0-.122-.58L3.654 1.328zM1.884.511a1.745 1.745 0 0 1 2.612.163L6.29 2.98c.329.423.445.974.315 1.494l-.547 2.19a.678.678 0 0 0 .178.643l2.457 2.457a.678.678 0 0 0 .644.178l2.189-.547a1.745 1.745 0 0 1 1.494.315l2.306 1.794c.829.645.905 1.87.163 2.611l-1.034 1.034c-.74.74-1.846 1.065-2.877.702a18.634 18.634 0 0 1-7.01-4.42 18.634 18.634 0 0 1-4.42-7.009c-.362-1.03-.037-2.137.703-2.877L1.885.511z\" />\n" +
                "                        </svg>\n" +
                "                    </span>\n" +
                "                    <span id=\"customerContact\">"+invoiceResponse.getCustomer().getContact()+"</span>\n" +
                "                </p>\n" +
                "                <p\n" +
                "                    style=\"color: gray;margin-block-start: 0em;font-size: 12px;margin-block-end: 0em;margin-top: 0.2em;margin-bottom: 0.1em;\">\n" +
                "                    <span class=\"me-1\"><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"12\" height=\"12\"\n" +
                "                            fill=\"currentColor\" class=\"bi bi-envelope-at\" viewBox=\"0 0 16 16\">\n" +
                "                            <path\n" +
                "                                d=\"M2 2a2 2 0 0 0-2 2v8.01A2 2 0 0 0 2 14h5.5a.5.5 0 0 0 0-1H2a1 1 0 0 1-.966-.741l5.64-3.471L8 9.583l7-4.2V8.5a.5.5 0 0 0 1 0V4a2 2 0 0 0-2-2H2Zm3.708 6.208L1 11.105V5.383l4.708 2.825ZM1 4.217V4a1 1 0 0 1 1-1h12a1 1 0 0 1 1 1v.217l-7 4.2-7-4.2Z\" />\n" +
                "                            <path\n" +
                "                                d=\"M14.247 14.269c1.01 0 1.587-.857 1.587-2.025v-.21C15.834 10.43 14.64 9 12.52 9h-.035C10.42 9 9 10.36 9 12.432v.214C9 14.82 10.438 16 12.358 16h.044c.594 0 1.018-.074 1.237-.175v-.73c-.245.11-.673.18-1.18.18h-.044c-1.334 0-2.571-.788-2.571-2.655v-.157c0-1.657 1.058-2.724 2.64-2.724h.04c1.535 0 2.484 1.05 2.484 2.326v.118c0 .975-.324 1.39-.639 1.39-.232 0-.41-.148-.41-.42v-2.19h-.906v.569h-.03c-.084-.298-.368-.63-.954-.63-.778 0-1.259.555-1.259 1.4v.528c0 .892.49 1.434 1.26 1.434.471 0 .896-.227 1.014-.643h.043c.118.42.617.648 1.12.648Zm-2.453-1.588v-.227c0-.546.227-.791.573-.791.297 0 .572.192.572.708v.367c0 .573-.253.744-.564.744-.354 0-.581-.215-.581-.8Z\" />\n" +
                "                        </svg>\n" +
                "                    </span>\n" +
                "                    <span id=\"customerEmail\">"+invoiceResponse.getCustomer().getEmail()+"</span>\n" +
                "                </p>\n" +
                "                <p\n" +
                "                    style=\"color: gray;margin-block-start: 0em;font-size: 12px;margin-block-end: 0em;margin-top: 0.2em;margin-bottom: 0.1em;\">\n" +
                "                    <span class=\"me-1\"><svg xmlns=\"http://www.w3.org/2000/svg\" width=\"12\" height=\"12\"\n" +
                "                            fill=\"currentColor\" class=\"bi bi-house\" viewBox=\"0 0 16 16\">\n" +
                "                            <path\n" +
                "                                d=\"M8.707 1.5a1 1 0 0 0-1.414 0L.646 8.146a.5.5 0 0 0 .708.708L2 8.207V13.5A1.5 1.5 0 0 0 3.5 15h9a1.5 1.5 0 0 0 1.5-1.5V8.207l.646.647a.5.5 0 0 0 .708-.708L13 5.793V2.5a.5.5 0 0 0-.5-.5h-1a.5.5 0 0 0-.5.5v1.293L8.707 1.5ZM13 7.207V13.5a.5.5 0 0 1-.5.5h-9a.5.5 0 0 1-.5-.5V7.207l5-5 5 5Z\" />\n" +
                "                        </svg>\n" +
                "                    </span>\n" +
                "                    <span id=\"customerAddress\">"+invoiceResponse.getCustomer().getAddress()+"</span>\n" +
                "                </p>\n" +
                "\n" +
                "            </div>\n" +
                "            <div class=\"col-md-4\" style=\"margin-top: 15px;margin-left: 100px;\">\n" +
                "                <div style=\"font-size: 15px;\n" +
                "                    color: red;\">\n" +
                "                    <span><strong>GSTIN - </strong></span>\n" +
                "                    <span id=\"showroomGstIn\">"+invoiceResponse.getShowroom().getGstIn()+"</span>\n" +
                "                </div>\n" +
                "                <div style=\"font-size: 12px;margin-top: 8px;\">\n" +
                "                    <span><strong>Payment Type - </strong></span>\n" +
                "                    <span id=\"paymentType\">"+invoiceResponse.getPaymentType().toUpperCase()+"</span>\n" +
                "                </div>\n" +
                "\n" +
                "            </div>\n" +
                "        </div>\n" +
                "\n" +
                "        <div>\n" +
                "            <table class=\"table\" id=\"invoiceProductItemsTable\"\n" +
                "                style=\"text-align: center;font-size: 12px;margin-right: 10px;width: 90%;margin-top: 15px;\">\n" +
                "                <tr\n" +
                "                    style=\"background-color: #f8f9fa; color: black;font-family: sans-serif;border-bottom: 1px solid black;\">\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">SL No.</th>\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">No. Of Pieces</th>\n" +
                "                    \n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">Title</th>\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">Purity</th>\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">HSN Code</th>\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">Gross Wt.</th>\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">Net Wt.</th>\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">Rate Of Gold/gm</th>\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">Value Of Gold</th>\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">Making Ch.</th>\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">H.M Ch.</th>\n" +
                "                    <th scope=\"col\" style=\"border-bottom: 1px solid red;\">Amount</th>\n" +
                "                </tr>\n" +
                "\n" +
                "                <tbody>\n" +
                                    item+
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "        <div style=\"display: flex;\">\n" +
                "\n" +
                "<div>\n" +
                bank+
                "</div>\n"+
                "\n" +
                "            <div style=\"margin-top: 10px;margin-left: 350px;\">\n" +
                "                <table style=\"width: 200px;\">\n" +
                "                    <tbody>\n" +
                "                        <tr style=\"font-size: 12px;\">\n" +
                "                            <td>Total:</td>\n" +
                "                            <td id=\"total\">"+totalAmount+"</td>\n" +
                "                        </tr>\n" +
                "\n" +
                "                        <tr id=\"sgstRow\" style=\"font-size: 12px;margin-top: 10px;\">\n" +
                "                            <td>SGST 1.5%: </td>\n" +
                "                            <td id=\"sgst\">"+gstValue+"</td>\n" +
                "                        </tr>\n" +
                "\n" +
                "                        <tr id=\"cgstRow\" style=\"font-size: 12px;margin-top: 10px;\">\n" +
                "                            <td>CGST 1.5%: </td>\n" +
                "                            <td id=\"cgst\">"+gstValue+"</td>\n" +
                "                        </tr>\n" +
                "\n" +
                "                        <tr style=\"font-size: 12px;\">\n" +
                "                            <td style=\"color: red;\"><strong>Net Amount: </strong></td>\n" +
                "                            <td><strong id=\"totalamount\">"+invoiceResponse.getTotalAmount()+"</strong></td>\n" +
                "\n" +
                "                        </tr>\n" +
                "                    </tbody>\n" +
                "                </table>\n" +
                "\n" +
                "            </div>\n" +
                "\n" +
                "\n" +
                "\n" +
                "\n" +
                "        </div>\n" +
                developer+
                "\n" +
                "\n" +
                "\n" +
                "    </div>\n" +
                "\n" +
                "    </div>\n" +
                "\n" +
                "</body>\n" +
                "\n" +
                "\n" +
                "</html>";

        return html;
    }

    private byte[] generatePdfFromHtml(String html, String name) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        HtmlConverter.convertToPdf(html, buffer);
        byte[] pdfAsBytes = buffer.toByteArray();
        try (FileOutputStream fos = new FileOutputStream(name)) {
            fos.write(pdfAsBytes);
        }
        return pdfAsBytes;
    }

    private void getLocationOfPdf(String pdfName, String email) throws MessagingException, IOException {
        String path = "./"+pdfName;
        Resource resource = resourceLoader.getResource(path);
        File file = null;

        try {
            file = resource.getFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (file != null) {
            System.out.println(file.getAbsolutePath());
        }
        sendMail(email,pdfName,path);
    }

    private void sendMail(String email,String pdfName,String path) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(email);
        helper.setFrom("hippycheeksart@gmail.com");
        helper.setSubject("INVOICE || " + pdfName.substring(0,16));
        helper.setText("Here is your invoice.");

        byte[] fileBytes = Files.readAllBytes(new File(path).toPath());
        ByteArrayResource fileResource = new ByteArrayResource(fileBytes);

        helper.addAttachment(pdfName, fileResource);

        javaMailSender.send(message);
    }
}
