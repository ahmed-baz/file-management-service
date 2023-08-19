package com.skyros.app.repo;


import com.skyros.app.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepo extends BaseRepo<Item> {

    @Query(value = "select i.id, i.\"name\", i.\"type\", i.parent_id, i.permission_group_id " +
            "from item i inner join permission_group pg " +
            "on i.permission_group_id = pg.id " +
            "inner join \"permission\" p " +
            "on pg.id = p.permission_group_id " +
            "where p.\"level\" = 'VIEW' "
            , nativeQuery = true)
    List<Item> viewItems();

}
