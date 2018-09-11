package ua.kiev.supersergey.judgement_registry_parser.core.registryclient.parser;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.registryclient.parser.exception.RegistryParserException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
@Slf4j
public class RegistryResponseParser {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public Stream<Document> parse(String inputHtml) {
        log.info("Parsing registry data");
        try {
            Optional<Elements> divResult = Optional.ofNullable(Jsoup.parse(inputHtml).select("div#divresult"));
            Optional<Element> table = Optional.ofNullable(divResult.orElseThrow(RegistryParserException::new).get(0));
            Optional<Elements> rows = Optional.ofNullable(table.orElseThrow(RegistryParserException::new).select("tr"));
            return IntStream.range(1, rows.orElseThrow(RegistryParserException::new).size())
                    .mapToObj(i -> rows.get().get(i))
                    .map(this::convertRowToRegistryResponse);
        } catch (Exception ex) {
            log.error("Could not parse server response, no results found");
            return Stream.empty();
        }
    }

    private Document convertRowToRegistryResponse(Element element) {
        return Document.builder()
                .registryId(element.select("a[href]").text())
                .decisionType(element.select(".VRType").text())
                .approvalDate(parseDateElement(element, ".RegDate"))
                .legalDate(parseDateElement(element, ".LawDate"))
                .judgementForm(element.select(".CSType").text())
                .caseNumber(element.select(".CaseNumber").text())
                .court(element.select(".CourtName").text())
                .judge(element.select(".ChairmenName").text())
                .build();
    }

    private LocalDate parseDateElement(Element element, String cssSelector) {
        String text = element.select(cssSelector).text().trim();
        return StringUtils.isEmpty(text) ? null : LocalDate.parse(text, DATE_TIME_FORMATTER);
    }
}
