package ua.kiev.supersergey.judgement_registry_parser.core.registryclient.parser;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.nio.file.Files;
import java.nio.file.Paths;

@RunWith(MockitoJUnitRunner.class)
public class RegistryResponseParserTest {

    private RegistryResponseParser parser;
    private byte[] html;

    @Before
    public void init() throws Exception{
        parser = new RegistryResponseParser();
        html = Files.readAllBytes(Paths.get(this.getClass().getClassLoader().getResource("output.html").toURI()));
    }

    @Test
    public void parse() {
//        Mono<Stream<Document>> documents = parser.parse(Mono.just(new String(html, StandardCharsets.UTF_8)));
//        documents.subscribe(documentStream -> documentStream.forEach(System.out::println));
    }
}