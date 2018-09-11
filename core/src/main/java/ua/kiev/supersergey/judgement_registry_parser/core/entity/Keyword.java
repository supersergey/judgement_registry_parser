package ua.kiev.supersergey.judgement_registry_parser.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "keyword")
@EqualsAndHashCode
public class Keyword {
    @Id
    @NotNull
    @Size(min=3, max=30)
    private String keyword;
    @Column(name = "updated_ts")
    @UpdateTimestamp
    private Date updatedTs;
    @Enumerated(value = EnumType.STRING)
    private KeywordStatus status;
    @OneToMany(mappedBy = "keyword", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Document> documents;
}
