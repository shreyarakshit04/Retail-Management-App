package com.dp.billapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.SpringSecurityCoreVersion;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InvoiceRequest {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    String invoiceDate;
    String invoiceId;
    long showroomId;
    long bankId;
    String userContact;
    String paymentType;
    String isGstEnabled;
    List<InvoiceItem> invoiceDetails = new ArrayList<>();
}
