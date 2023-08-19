package com.skyros.app.mapper;


import com.skyros.app.model.Permission;
import com.skyros.app.vo.PermissionVO;
import org.mapstruct.Mapper;

@Mapper
public interface PermissionMapper extends BaseMapper<Permission, PermissionVO> {

}
