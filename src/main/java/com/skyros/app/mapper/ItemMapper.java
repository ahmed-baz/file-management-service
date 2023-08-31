package com.skyros.app.mapper;


import com.skyros.app.model.Item;
import com.skyros.app.vo.ItemVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ItemMapper extends BaseMapper<Item, ItemVO> {

}
