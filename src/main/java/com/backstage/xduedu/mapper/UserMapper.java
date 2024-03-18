package com.backstage.xduedu.mapper;

import com.backstage.xduedu.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author zyy
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2024-03-17 13:38:01
* @Entity com.backstage.xduedu.domain.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

}




