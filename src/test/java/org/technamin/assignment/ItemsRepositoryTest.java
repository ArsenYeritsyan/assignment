package org.technamin.assignment;

import org.junit.jupiter.api.Test;
import org.technamin.assignment.service.ItemsRepository;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ItemsRepositoryTest {

    @Test
    void shouldGetExactResponse() {
        var itemList = ItemsRepository.INSTANCE.getItems();
        assertNotNull(itemList);
        assertTrue(itemList.size() > 2);
    }
}
