package com.skyros.app.mapper;

import org.springframework.data.domain.Page;

import java.util.List;


public interface BaseMapper<E, VO> {

    List<VO> entityListToVOList(List<E> entityList);

    VO entityToVO(E e);

    E VOToEntity(VO vo);

    List<E> VOListToEntityList(List<VO> vos);

}
