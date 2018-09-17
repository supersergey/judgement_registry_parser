package ua.kiev.supersergey.judgement_registry_parser.core.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import ua.kiev.supersergey.judgement_registry_parser.core.dao.KeywordRepository;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Document;
import ua.kiev.supersergey.judgement_registry_parser.core.entity.Keyword;
import ua.kiev.supersergey.judgement_registry_parser.core.service.TestService;

import java.util.Optional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ContextTest {
    @Autowired
    private KeywordRepository repository;
    @Autowired
    private TestService testService;

    @Test
    public void test() throws Exception{
        Z z  = new Z();
        Mono<String> monoTest = Mono.just("123");
        monoTest.subscribeOn(Schedulers.parallel()).subscribe(z::doSmth);
        monoTest.subscribe(s -> System.out.println("Printed by main: " + s));
        System.out.println("Exiting main thread");
        Thread.sleep(3500);
    }

    @Test
    @Transactional
    public void testLazyLoading() {
        Optional<Keyword> keyword = repository.findByKeywordIgnoreCase("Приват-банк");
//        System.out.println(keyword.get().getDocuments().get(0));
        System.out.println(keyword.get().getDocuments().stream().map(Document::getRegistryId).findFirst().get());
    }

    @Test
    @Transactional
    public void testAsync() throws Exception {
        System.out.println("Staring test");
        testService.someAsyncService();
        Thread.sleep(100);
        System.out.println("Waiting...");
        Thread.sleep(1000);
        System.out.println("Exiting");
    }

    class Z {
        public void doSmth(String s) {
            try {
                System.out.println("Entering z");
                Thread.sleep(3000L);
                System.out.println("Printed by z: " + s);
                System.out.println("Exiting z");
            } catch (Exception ex) {

            }
        }
    }
}
