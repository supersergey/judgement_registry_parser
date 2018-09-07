package ua.kiev.supersergey.judgement_registry_parser.core.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity(name = "document")
@ToString
@EqualsAndHashCode
public class Document {
    @Id
    private String id;
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
