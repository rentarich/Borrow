package si.fri.rso.borrow.models.converters;

import si.fri.rso.borrow.models.dtos.Item;
import si.fri.rso.borrow.models.entities.ItemEntity;

public class ItemConverter {
    public static Item toDto(ItemEntity entity) {
        Item dto = new Item();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());

        return dto;
    }

    public static ItemEntity toEntity(Item dto) {
        ItemEntity entity = new ItemEntity();
        entity.setId(dto.getId());
        entity.setCategory(dto.getCategory());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        return entity;
    }
}
