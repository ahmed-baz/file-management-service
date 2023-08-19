package com.skyros.app.service;


import com.skyros.app.vo.AppResponse;
import com.skyros.app.vo.ItemVO;
import com.skyros.app.vo.PermissionGroupVO;


import java.util.List;

public interface FileSystemService {

    AppResponse<PermissionGroupVO> createPermissionGroup(PermissionGroupVO vo);

    AppResponse<ItemVO> createSpace(ItemVO vo);

    AppResponse<ItemVO> createFolder(ItemVO vo);

    AppResponse<ItemVO> createFile(ItemVO vo);

    AppResponse<List<ItemVO>> viewFiles();
}
