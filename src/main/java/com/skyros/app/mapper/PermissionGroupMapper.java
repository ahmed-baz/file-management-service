package com.skyros.app.mapper;


import com.skyros.app.model.PermissionGroup;
import com.skyros.app.vo.PermissionGroupVO;
import org.mapstruct.Mapper;

@Mapper
public interface PermissionGroupMapper extends BaseMapper<PermissionGroup, PermissionGroupVO> {

}
