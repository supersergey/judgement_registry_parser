package ua.kiev.supersergey.judgement_registry_parser.core.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

@Data
@Entity(name = "keyword")
public class Keyword {
    @Id
    @NotNull
    @Size(min=2, max=30)
    private String keyword;
    @Column(name = "updated_ts")
    private Date updatedTs;
    @OneToMany(mappedBy = "keyword")
    private List<Document> documents;
}
