package com.allinonefx.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.allinonefx.model.Customer;
import com.allinonefx.model.CustomerExample;

public interface CustomerMapperExtend extends CustomerMapper {
    Customer selectByFirstnameAndLastname(@Param("first_name") String first_name, @Param("last_name") String last_name);
}