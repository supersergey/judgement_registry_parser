package ua.kiev.supersergey.judgement_registry_parser.core.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "keyword")
@EqualsAndHashCode
public class Keyword {
    @Id
    @NotNull
    @Size(min=2, max=30)
    private String keyword;
    @Column(name = "updated_ts")
    private Date updatedTs;
    @Enumerated(value = EnumType.STRING)
    private KeywordStatus status;
    @OneToMany(mappedBy = "keyword", fetch = FetchType.LAZY)
    private List<Document> documents;

    @Override
    public String toString() {
        return "Keyword{" +
                "keyword='" + keyword + '\'' +
                ", updatedTs=" + updatedTs +
                ", status=" + status +
                ", documents=" + documents.size() +
                '}';
    }
}
