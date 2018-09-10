package ua.kiev.supersergey.judgement_registry_parser.core.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.annotation.Generated;
import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "document")
public class Document {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    private String registryId;
    private String decisionType;
    private LocalDate approvalDate;
    private LocalDate legalDate;
    private String judgementForm;
    private String caseNumber;
    private String court;
    private String judge;
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdTs;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "keyword")
    private Keyword keyword;
}
