package com.skyros.app.service;

import com.skyros.app.enums.FileTypeEnum;
import com.skyros.app.enums.PermissionLevelEnum;
import com.skyros.app.mapper.ItemMapper;
import com.skyros.app.mapper.PermissionGroupMapper;
import com.skyros.app.model.File;
import com.skyros.app.model.Item;
import com.skyros.app.model.Permission;
import com.skyros.app.model.PermissionGroup;
import com.skyros.app.repo.FileRepo;
import com.skyros.app.repo.ItemRepo;
import com.skyros.app.repo.PermissionGroupRepo;
import com.skyros.app.repo.PermissionRepo;
import com.skyros.app.vo.AppResponse;
import com.skyros.app.vo.ItemVO;
import com.skyros.app.vo.PermissionGroupVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@Setter
@Getter
public class FileSystemServiceImpl implements FileSystemService {

    @Autowired
    private ItemMapper itemMapper;
    @Autowired
    private ItemRepo itemRepo;
    @Autowired
    private PermissionGroupRepo permissionGroupRepo;
    @Autowired
    private PermissionRepo permissionRepo;
    @Autowired
    private PermissionGroupMapper permissionGroupMapper;
    @Autowired
    private FileRepo fileRepo;


    @Override
    public AppResponse<PermissionGroupVO> createPermissionGroup(PermissionGroupVO vo) {
        PermissionGroup permissionGroup = getPermissionGroupMapper().VOToEntity(vo);
        PermissionGroup savedPermissionGroup = getPermissionGroupRepo().save(permissionGroup);
        return new AppResponse<>(getPermissionGroupMapper().entityToVO(savedPermissionGroup));
    }

    @Override
    public AppResponse<ItemVO> createSpace(ItemVO vo) {
        vo.setType(FileTypeEnum.SPACE);
        Item item = getItemMapper().VOToEntity(vo);
        getPermissionGroupRepo().save(item.getPermissionGroup());
        getPermissionRepo().saveAll(item.getPermissionGroup().getPermissions());
        Item savedItem = getItemRepo().save(item);
        return new AppResponse<>(getItemMapper().entityToVO(savedItem));
    }

    @Override
    public AppResponse<ItemVO> createFolder(ItemVO vo) {
        vo.setType(FileTypeEnum.FOLDER);
        Item item = getItemMapper().VOToEntity(vo);
        PermissionGroup permissionGroup = item.getPermissionGroup();
        List<Permission> permissionList = getPermissionRepo().findPermissionByPermissionGroup(permissionGroup);
        boolean editAllowed = isEditAllowed(permissionList);
        if (editAllowed) {
            Item savedItem = getItemRepo().save(item);
            return new AppResponse<>(getItemMapper().entityToVO(savedItem));
        }
        return new AppResponse<>("action is not allowed");
    }

    @Override
    public AppResponse<ItemVO> createFile(ItemVO vo) {
        vo.setType(FileTypeEnum.FILE);
        ItemVO itemVO = vo.getParent();
        if (null != itemVO) {
            Optional<Item> optionalParent = getItemRepo().findById(itemVO.getId());
            if (optionalParent.isPresent()) {
                Item parent = optionalParent.get();
                PermissionGroup permissionGroup = parent.getPermissionGroup();
                boolean editAllowed = isEditAllowed(permissionGroup.getPermissions());
                if (editAllowed) {
                    Item item = getItemMapper().VOToEntity(vo);
                    Item savedItem = getItemRepo().save(item);
                    //saveFile(file, savedItem);
                    return new AppResponse<>(getItemMapper().entityToVO(savedItem));
                }
            }

        }
        return new AppResponse<>("action is not allowed");
    }

    private void saveFile(MultipartFile file, Item item) {
        try {
            File itemFile = new File();
            itemFile.setFileBinary(file.getBytes());
            itemFile.setItem(item);
            itemFile.setName(file.getName());
            getFileRepo().save(itemFile);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public AppResponse<List<ItemVO>> viewFiles() {
        List<Item> items = getItemRepo().viewItems();
        return new AppResponse<>(getItemMapper().entityListToVOList(items));
    }

    private boolean isEditAllowed(List<Permission> permissionList) {
        if (null == permissionList || permissionList.isEmpty()) {
            return false;
        }
        return permissionList.stream().anyMatch(permission -> PermissionLevelEnum.EDIT.equals(permission.getLevel()));
    }
}
