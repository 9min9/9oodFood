package goodfood.service.store;

import goodfood.entity.store.Store;
import goodfood.repository.infra.StoreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

@SpringBootTest
class StoreServiceTest {

    @Autowired
    StoreRepository storeRepository;

    @Test
    void StoreDuplicateTest () {
        Optional<Store> findStore = storeRepository.findByTitle("올덴브라운");

        assertTrue(findStore.isPresent(), "존재하는 가게");

    }

}