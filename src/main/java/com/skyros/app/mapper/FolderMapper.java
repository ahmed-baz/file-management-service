package com.skyros.app.mapper;


import com.skyros.app.model.Folder;
import com.skyros.app.model.Item;
import com.skyros.app.vo.ItemVO;
import org.mapstruct.Mapper;

@Mapper
public interface FolderMapper {

    Item folderToItem(Folder folder);

    ItemVO folderToItemVO(Folder folder);

    Folder itemToFolder(Item item);

}
