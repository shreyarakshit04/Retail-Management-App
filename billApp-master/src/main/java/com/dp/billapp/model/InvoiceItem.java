package com.dp.billapp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.SpringSecurityCoreVersion;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class InvoiceItem {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String noOfPieces;
    private String serialNo;
    private String name;
    private String hsnCode;
    private String purity;
    private String grossWeight;
    private String netWeight;
    private String rateOfGoldPerGram;
    private String valueOfGold;
    private String makingCharges;
    private String hallMarkingCharges;
    private String amount;

}
