package com.skyros.app.service;

import com.skyros.app.enums.FileTypeEnum;
import com.skyros.app.enums.PermissionLevelEnum;
import com.skyros.app.mapper.*;
import com.skyros.app.model.*;
import com.skyros.app.repo.*;
import com.skyros.app.vo.AppResponse;
import com.skyros.app.vo.ItemVO;
import com.skyros.app.vo.ItemViewVO;
import com.skyros.app.vo.PermissionGroupVO;
import liquibase.pro.packaged.nu;
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
    @Autowired
    private ItemFileRepo itemFileRepo;
    @Autowired
    private ItemFileMapper itemFileMapper;
    @Autowired
    private ItemViewRepo itemViewRepo;
    @Autowired
    private ItemViewMapper itemViewMapper;


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
    public AppResponse<ItemVO> createFile(ItemVO vo) {
        try {
            vo.setType(FileTypeEnum.FILE);
            Item item = getItemMapper().VOToEntity(vo);
            Item parent = item.getParent();
            validateParentType(parent, FileTypeEnum.FILE);
            if (null != parent && parent.getId() != null) {
                Optional<Item> optionalParent = getItemRepo().findById(parent.getId());
                if (optionalParent.isPresent()) {
                    parent = optionalParent.get();
                    PermissionGroup permissionGroup = parent.getPermissionGroup();
                    boolean editAllowed = isEditAllowed(permissionGroup.getPermissions());
                    if (editAllowed) {
                        ItemFile itemFile = getItemFileMapper().itemToFile(item);
                        ItemFile savedFile = getItemFileRepo().save(itemFile);
                        saveFile(vo.getFile(), savedFile);
                        return new AppResponse<>(getItemFileMapper().fileToItemVO(savedFile));
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

    @Override
    public AppResponse<List<ItemViewVO>> viewItems() {
        List<ItemView> all = getItemViewRepo().findAll();
        List<ItemViewVO> itemViewVOS = getItemViewMapper().entityListToVOList(all);
        return new AppResponse<>(itemViewVOS);
    }

    private void saveFile(String file, ItemFile item) {
        try {
            File itemFile = new File();
            itemFile.setFileBinary(file);
            itemFile.setItem(item);
            itemFile.setName(item.getName());
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
