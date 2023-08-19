package com.skyros.app.service;


import com.skyros.app.vo.AppResponse;
import com.skyros.app.vo.ItemVO;
import com.skyros.app.vo.PermissionGroupVO;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

public interface FileSystemService {

    AppResponse<PermissionGroupVO> createPermissionGroup(PermissionGroupVO vo);

    AppResponse<ItemVO> createSpace(ItemVO vo);

    AppResponse<ItemVO> createFolder(ItemVO vo);

    AppResponse<ItemVO> createFile(MultipartFile file, ItemVO vo);

    AppResponse<List<ItemVO>> viewFiles();
}
