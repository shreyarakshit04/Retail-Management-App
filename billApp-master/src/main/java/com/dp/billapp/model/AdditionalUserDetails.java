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
public class AdditionalUserDetails {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;
    private String aadharNumber;
    private String voterNumber;
    private String bankAccountNumber;
    private String bankName;
    private String bankIfscCode;

}
