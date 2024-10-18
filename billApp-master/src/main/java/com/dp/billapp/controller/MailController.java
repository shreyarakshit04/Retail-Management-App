package com.dp.billapp.controller;

import com.dp.billapp.serviceImpl.MailService;
import com.itextpdf.html2pdf.HtmlConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.nio.file.Files;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping(value="/mail")
public class MailController {

    @Autowired
    private MailService mailService;

    @PostMapping("/pdf")
    public void generatePdf(@RequestBody EmailBody emailBody) throws IOException, MessagingException {
        mailService.generate(emailBody.getInvoiceId(),emailBody.getCustomerEmail());
    }

    @Data
    static class EmailBody{
        long invoiceId;
        String customerEmail;
    }

}
