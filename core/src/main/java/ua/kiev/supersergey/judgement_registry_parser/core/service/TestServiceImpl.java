package ua.kiev.supersergey.judgement_registry_parser.core.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TestServiceImpl implements TestService{
    @Async
    @Override
    @Transactional
    public void someAsyncService() throws Exception {
        System.out.println("Entering async");
        Thread.sleep(1000);
        System.out.println("Exiting async");
    }
}
