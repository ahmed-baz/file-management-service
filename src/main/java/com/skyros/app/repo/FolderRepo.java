package com.skyros.app.repo;


import com.skyros.app.model.Folder;
import com.skyros.app.model.Item;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepo extends BaseRepo<Folder> {

}
