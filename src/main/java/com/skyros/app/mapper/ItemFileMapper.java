package com.skyros.app.mapper;


import com.skyros.app.model.Item;
import com.skyros.app.model.ItemFile;
import com.skyros.app.vo.ItemVO;
import org.mapstruct.Mapper;

@Mapper
public interface ItemFileMapper {

    Item fileToItem(ItemFile file);

    ItemVO fileToItemVO(ItemFile file);

    ItemFile itemToFile(Item item);

}
