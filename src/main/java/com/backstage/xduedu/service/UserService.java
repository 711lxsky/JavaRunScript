package com.backstage.xduedu.service;

import com.backstage.xduedu.domain.dto.LoginUser;
import com.backstage.xduedu.domain.dto.SignUpUser;
import com.backstage.xduedu.domain.dto.UpdateUser;
import com.backstage.xduedu.domain.entity.User;
import com.backstage.xduedu.domain.Result;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author zyy
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-03-17 13:38:01
*/
public interface UserService extends IService<User> {

    Result<?> signUp(SignUpUser signUpUser);

    Result<?> login(LoginUser loginUser);

    Result<?> updateUserInfo(UpdateUser updateUser);

    Result<?> getUserInfoByUserId(Long userId);
}
