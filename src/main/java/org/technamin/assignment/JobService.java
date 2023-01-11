package org.technamin.assignment;

import org.technamin.assignment.model.*;
import org.technamin.assignment.service.MongoItemService;
import org.technamin.assignment.service.RabbitMQService;

public class JobService {
    private final MongoItemService mongoService = new MongoItemService();

    public void sendItemToSave(ItemSaveDto saveDto) {
        Item toSave = new Item(saveDto.getId(), saveDto.getSeq(), saveDto.getData(), saveDto.getTime());
        mongoService.save(toSave);
        Information information = new Information(saveDto.getId(), UpdateType.SAVE, toSave.getData());
        RabbitMQService.sendLog(information);
    }

    public void sendItemToUpdate(ItemUpdateDto updateDto) {
        mongoService.updateItemFieldById(updateDto.getDocId(), updateDto.getFieldName(), updateDto.getFieldUpdateValue());
        Information information = new Information(updateDto.getDocId(), UpdateType.UPDATE,
                updateDto.getFieldName(), updateDto.getFieldUpdateValue());
        RabbitMQService.sendLog(information);
    }


}
