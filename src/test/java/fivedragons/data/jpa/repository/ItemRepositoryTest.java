package fivedragons.data.jpa.repository;

import fivedragons.data.jpa.entity.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ItemRepositoryTest {

    @Autowired ItemRepository itemRepository;

    @Test
    void save() {
        // given
        Item item = new Item("A001");
        itemRepository.save(item);
    }
}