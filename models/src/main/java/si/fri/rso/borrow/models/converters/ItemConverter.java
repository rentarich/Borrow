package si.fri.rso.borrow.models.converters;

import si.fri.rso.borrow.models.dtos.ItemDTO;
import si.fri.rso.borrow.models.entities.Item;

public class ItemConverter {
    public static ItemDTO toDto(Item entity) {
        ItemDTO dto = new ItemDTO();
        dto.setId(entity.getId());
        dto.setCategory(entity.getCategory());
        dto.setDescription(entity.getDescription());
        dto.setTitle(entity.getTitle());

        return dto;
    }

    public static Item toEntity(ItemDTO dto) {
        Item entity = new Item();
        entity.setId(dto.getId());
        entity.setCategory(dto.getCategory());
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());

        return entity;
    }
}
