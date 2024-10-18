package com.dp.billapp.model;

import com.vladmihalcea.hibernate.type.array.IntArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import com.vladmihalcea.hibernate.type.json.JsonNodeStringType;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.security.core.SpringSecurityCoreVersion;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(name = "draft")

@TypeDefs({
        @TypeDef(name = "string-array", typeClass = StringArrayType.class),
        @TypeDef(name = "int-array", typeClass = IntArrayType.class),
        @TypeDef(name = "json", typeClass = JsonStringType.class),
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class),
        @TypeDef(name = "jsonb-node", typeClass = JsonNodeBinaryType.class),
        @TypeDef(name = "json-node", typeClass = JsonNodeStringType.class),
})

public class Draft {
    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @Column(name = "invoice_id")
    private String invoiceId;

    @Column(name = "invoice_date")
    private String invoiceDate;

    @Column(name = "showroom_id")
    private long showroomId;

    @Column(name = "bank_id")
    private long bankId;

    @Column(name = "customer_id")
    private long customerId;

    @Column(name = "updated_by")
    private long updatedBy;

    @Column(name = "created_by")
    private long createdBy;

    @Column(name = "created_at")
    private String createdAt;

    @Column(name = "updated_at")
    private String updatedAt;

    @Column(name = "payment_type")
    private String paymentType;

    @Column(name = "is_gst")
    private String isGst;

    @Column(name = "s_gst")
    private double sGst;

    @Column(name = "c_gst")
    private double cGst;

    @Column(name = "total_amount")
    private String totalAmount;

    @Type(type = "json")
    @Column(name = "invoice_details", columnDefinition = "json")
//    InvoiceDetails invoiceDetails;
    List<InvoiceItem> invoiceDetails;
}

