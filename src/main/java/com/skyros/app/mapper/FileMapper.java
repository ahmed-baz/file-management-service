package com.skyros.app.mapper;


import com.skyros.app.model.File;
import com.skyros.app.vo.FileVO;
import org.mapstruct.Mapper;

@Mapper
public interface FileMapper extends BaseMapper<File, FileVO> {

}
