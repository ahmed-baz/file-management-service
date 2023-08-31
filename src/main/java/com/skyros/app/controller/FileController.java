package com.skyros.app.controller;


import com.skyros.app.enums.FileTypeEnum;
import com.skyros.app.model.ItemView;
import com.skyros.app.model.PermissionGroup;
import com.skyros.app.service.FileSystemService;
import com.skyros.app.vo.ItemVO;
import com.skyros.app.vo.ItemViewVO;
import com.skyros.app.vo.PermissionGroupVO;
import lombok.Getter;
import lombok.Setter;
import com.skyros.app.vo.AppResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
@RestController
@RequestMapping("app/")
public class FileController {

    @Autowired
    private FileSystemService fileSystemService;

    @PostMapping("permission-group/add")
    public AppResponse<PermissionGroupVO> createPermissionGroup(@RequestBody PermissionGroupVO vo) {
        return getFileSystemService().createPermissionGroup(vo);
    }

    @PostMapping("space/add")
    public AppResponse<ItemVO> createSpace(@RequestBody ItemVO vo) {
        return getFileSystemService().createSpace(vo);
    }

    @PostMapping("folder/add")
    public AppResponse<ItemVO> createFolder(@RequestBody ItemVO vo) {
        return getFileSystemService().createFolder(vo);
    }

    @PostMapping("file/add")
    public AppResponse<ItemVO> createFile(@RequestBody ItemVO vo) {
        return getFileSystemService().createFile(vo);
    }

    @PostMapping("file/view")
    public AppResponse<List<ItemVO>> viewFiles() {
        return getFileSystemService().viewFiles();
    }

    @PostMapping("items/view")
    public AppResponse<List<ItemViewVO>> viewItems() {
        return getFileSystemService().viewItems();
    }


}
