package ua.kiev.supersergey.judgement_registry_parser.core.dao;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.service.DocumentService;
import ua.kiev.supersergey.judgement_registry_parser.core.service.KeywordService;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DocumentRepositoryTest {
    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentService documentService;

    @Autowired
    private KeywordService keywordService;

    @Test
    public void findAllByKeywordAndRegistryIds() {
        List<String> registryIds = Arrays.asList("75619086", "75621536", "75621542");
        String keyword = "Епіцентр";
        Page<Document> allByKeywordAndRegistryIds = documentRepository.findAllByKeywordAndRegistryIds(keyword, registryIds, PageRequest.of(0, 10));
        assertEquals(registryIds.size(), allByKeywordAndRegistryIds.getNumberOfElements());
    }
}