package com.skyros.app.repo;


import com.skyros.app.model.ItemView;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemViewRepo extends ReadOnlyRepository<ItemView, Long> {

}
