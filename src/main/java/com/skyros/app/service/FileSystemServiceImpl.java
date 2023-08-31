package com.skyros.app.service;

import com.skyros.app.enums.FileTypeEnum;
import com.skyros.app.enums.PermissionLevelEnum;
import com.skyros.app.mapper.FolderMapper;
import com.skyros.app.mapper.ItemMapper;
import com.skyros.app.mapper.PermissionGroupMapper;
import com.skyros.app.model.*;
import com.skyros.app.repo.*;
import com.skyros.app.vo.AppResponse;
import com.skyros.app.vo.ItemVO;
import com.skyros.app.vo.PermissionGroupVO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
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
    @Autowired
    private FolderRepo folderRepo;
    @Autowired
    private FolderMapper folderMapper;


    @Override
    public AppResponse<PermissionGroupVO> createPermissionGroup(PermissionGroupVO vo) {
        PermissionGroup permissionGroup = getPermissionGroupMapper().VOToEntity(vo);
        PermissionGroup savedPermissionGroup = getPermissionGroupRepo().save(permissionGroup);
        return new AppResponse<>(getPermissionGroupMapper().entityToVO(savedPermissionGroup));
    }

    @Override
    public AppResponse<ItemVO> createSpace(ItemVO vo) {
        try {
            vo.setType(FileTypeEnum.SPACE);
            Item item = getItemMapper().VOToEntity(vo);
            Item parent = item.getParent();
            if (parent != null && parent.getId() != null) {
                validateParentType(parent, FileTypeEnum.SPACE);
            }
            Item savedItem = getItemRepo().save(item);
            return new AppResponse<>(getItemMapper().entityToVO(savedItem));
        } catch (RuntimeException ex) {
            return new AppResponse<>(ex.getMessage());
        }
    }

    @Override
    public AppResponse<ItemVO> createFolder(ItemVO vo) {
        try {
            vo.setType(FileTypeEnum.FOLDER);
            Item item = getItemMapper().VOToEntity(vo);
            validateParentType(item.getParent(), FileTypeEnum.FOLDER);
            Item parent = getItemRepo().findById(item.getParent().getId()).get();
            List<Permission> permissionList = getPermissionRepo().findPermissionByPermissionGroup(parent.getPermissionGroup());
            boolean editAllowed = isEditAllowed(permissionList);
            if (editAllowed) {
                Folder folder = getFolderMapper().itemToFolder(item);
                Folder savedFolder = getFolderRepo().save(folder);
                return new AppResponse<>(getFolderMapper().folderToItemVO(savedFolder));
            }
            return new AppResponse<>("action is not allowed");
        } catch (RuntimeException ex) {
            return new AppResponse<>(ex.getMessage());
        }
    }


    @Override
    public AppResponse<ItemVO> createFile(MultipartFile file, ItemVO vo) {
        try {
            vo.setType(FileTypeEnum.FILE);
            ItemVO itemVO = vo.getParent();
            Item item = getItemMapper().VOToEntity(vo);
            validateParentType(item.getParent(), FileTypeEnum.FILE);
            if (null != itemVO) {
                Optional<Item> optionalParent = getItemRepo().findById(itemVO.getId());
                if (optionalParent.isPresent()) {
                    Item parent = optionalParent.get();
                    PermissionGroup permissionGroup = parent.getPermissionGroup();
                    boolean editAllowed = isEditAllowed(permissionGroup.getPermissions());
                    if (editAllowed) {
                        Item savedItem = getItemRepo().save(item);
                        saveFile(file, savedItem);
                        return new AppResponse<>(getItemMapper().entityToVO(savedItem));
                    }
                }
            }
            return new AppResponse<>("action is not allowed");
        } catch (RuntimeException ex) {
            return new AppResponse<>(ex.getMessage());
        }
    }

    @Override
    public AppResponse<List<ItemVO>> viewFiles() {
        List<Item> items = getItemRepo().viewItems();
        for (Item item : items) {
            item.getPermissionGroup().setPermissions(new ArrayList<>());
        }
        return new AppResponse<>(getItemMapper().entityListToVOList(items));
    }

    private void saveFile(MultipartFile file, Item item) {
        try {
            File itemFile = new File();
            itemFile.setFileBinary(new String(file.getBytes(), StandardCharsets.UTF_8));
            itemFile.setItem(item);
            itemFile.setName(file.getOriginalFilename());
            getFileRepo().save(itemFile);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void validateParentType(Item parent, FileTypeEnum fileType) {
        Optional<Item> itemOptional = getItemRepo().findById(parent.getId());
        if ((FileTypeEnum.FOLDER.equals(fileType) || FileTypeEnum.FILE.equals(fileType)) && itemOptional.isEmpty()) {
            throw new RuntimeException("the space/folder is not found");
        }
        switch (fileType) {
            case FOLDER:
                if (itemOptional.isPresent()) {
                    Item item = itemOptional.get();
                    if (FileTypeEnum.FILE.equals(item.getType())) {
                        throw new RuntimeException("invalid space/folder config");
                    }
                } else {
                    throw new RuntimeException("the space not found");
                }
                break;
            case FILE:
                if (itemOptional.isPresent()) {
                    Item item = itemOptional.get();
                    if (FileTypeEnum.FILE.equals(item.getType())) {
                        throw new RuntimeException("invalid space/folder config");
                    }
                } else {
                    throw new RuntimeException("the space/folder is not found");
                }
                break;
            case SPACE:
                if (itemOptional.isEmpty()) {
                    throw new RuntimeException("invalid space config");
                } else {
                    Item item = itemOptional.get();
                    if (FileTypeEnum.FILE.equals(item.getType()) || FileTypeEnum.FOLDER.equals(item.getType())) {
                        throw new RuntimeException("invalid space config");
                    }
                }
                break;
            default:
                throw new RuntimeException("invalid file type");
        }
    }

    private boolean isEditAllowed(List<Permission> permissionList) {
        if (null == permissionList || permissionList.isEmpty()) {
            return false;
        }
        return permissionList.stream().anyMatch(permission -> PermissionLevelEnum.EDIT.equals(permission.getLevel()));
    }
}
