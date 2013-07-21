package net.avh4.data.per;

import org.mockito.MockitoAnnotations;

public class InMemoryRefServiceTest extends RefServiceContract {

    @Override protected RefService createSubject() {
        MockitoAnnotations.initMocks(this);
        return new InMemoryRefService();
    }
}
