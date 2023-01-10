package org.technamin.assignment.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.technamin.assignment.model.*;
import org.technamin.assignment.service.MongoItemService;
import org.technamin.assignment.service.RabbitMQService;

import java.util.List;

@RestController
@RequestMapping("/item/")
public class Controller {
    private final MongoItemService mongoService = new MongoItemService();
//    private final DateTimeFormatter format = DateTimeFormatter.ofPattern("uuuuMMddHHmmss");

    @PostMapping("/save/")
    public ResponseEntity<Void> sendItemToSave(@RequestBody ItemSaveDto saveDto) {
//        LocalDateTime ldt = LocalDateTime.parse(saveDto.getTime(), format);
        Item toSave = new Item(saveDto.getId(), saveDto.getSeq(), saveDto.getData(), saveDto.getTime());
        mongoService.save(toSave);
        Information information = new Information(saveDto.getId(), UpdateType.SAVE, toSave.getData());
        RabbitMQService.sendLog(information);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/update/")
    public ResponseEntity<Void> sendItemToUpdate(@RequestBody ItemUpdateDto updateDto) {
//        if (updateDto.getFieldName().equals("time")) {
//            LocalDateTime ldt = LocalDateTime.parse(updateDto.getFieldUpdateValue(), format);
//            mongoService.updateItemFieldById(updateDto.getDoc_id(), updateDto.getFieldName(), ldt.toString());
//        }
        mongoService.updateItemFieldById(updateDto.getDoc_id(), updateDto.getFieldName(), updateDto.getFieldUpdateValue());
        Information information = new Information(updateDto.getDoc_id(), UpdateType.UPDATE,
                updateDto.getFieldName(), updateDto.getFieldUpdateValue());
        RabbitMQService.sendLog(information);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<Item>> findAllItems() {
        return ResponseEntity.ok(mongoService.findAll());
    }


}
