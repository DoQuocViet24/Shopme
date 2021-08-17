package com.shopme.admin.setting;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.shopme.common.entity.Setting;
import com.shopme.common.entity.SettingCategory;

@Repository
public interface SettingRepository extends CrudRepository<Setting, String> {

	List<Setting> findByCategory(SettingCategory general);

}
