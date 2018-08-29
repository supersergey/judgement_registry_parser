package ua.kiev.supersergey.judgement_registry_parser.core.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RunWith(MockitoJUnitRunner.class)
public class ContextTest {
    @Test
    public void test() throws Exception{
        Z z  = new Z();
        Mono<String> monoTest = Mono.just("123");
        monoTest.subscribeOn(Schedulers.parallel()).subscribe(z::doSmth);
        monoTest.subscribe(s -> System.out.println("Printed by main: " + s));
        System.out.println("Exiting main thread");
        Thread.sleep(3500);
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
