package com.backstage.javarunscript.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.DateUtil;
import com.backstage.javarunscript.Exception.HttpException;
import com.backstage.javarunscript.domain.Result;
import com.backstage.javarunscript.domain.dto.LoginUser;
import com.backstage.javarunscript.domain.dto.SignUpUser;
import com.backstage.javarunscript.domain.dto.UpdateUser;
import com.backstage.javarunscript.domain.entity.User;
import com.backstage.javarunscript.domain.vo.UserVO;
import com.backstage.javarunscript.mapper.UserMapper;
import com.backstage.javarunscript.service.UserService;
import com.backstage.javarunscript.setting_enum.ExceptionConstant;
import com.backstage.javarunscript.setting_enum.ResultCodeAndMessage;
import com.backstage.javarunscript.utils.SecurityUtil;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Objects;

/**
* @author zyy
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-03-17 13:38:01
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

    @Resource
    private SecurityUtil securityUtil;

    @Override
    public Result<?> signUp(SignUpUser signUpUser) throws HttpException {
        // 数据校验
        if(
                Objects.nonNull(signUpUser)
                        && StringUtils.hasText(signUpUser.getUserName())
                        && StringUtils.hasText(signUpUser.getPassword())
                        && StringUtils.hasText(signUpUser.getValidPassword())
        ){
            // 首先校验两次密码是否一致
            if (!signUpUser.getPassword().equals(signUpUser.getValidPassword())) {
                throw new HttpException(ExceptionConstant.PasswordDifferent.getMessage_EN());
            }
            User newUser = this.parseSignUpUserToNewUser(signUpUser);
            if(!this.save(newUser)){
                throw new HttpException();
            }
            return Result.success(ResultCodeAndMessage.SignUpSuccess.getDescription(),
                    this.parseUserToUserVO(newUser));
        }
        throw new HttpException(ExceptionConstant.DateNull.getMessage_EN());
    }

    @Override
    public Result<?> login(LoginUser loginUser) throws HttpException{
        if(Objects.nonNull(loginUser)
                && Objects.nonNull(loginUser.getUserId())
                && StringUtils.hasText(loginUser.getPassword())
        ){
            User userInfoFromDB = this.getById(loginUser.getUserId());
            if(Objects.isNull(userInfoFromDB)){
                throw new HttpException(ExceptionConstant.UserNotFound.getMessage_EN());
            }
            securityUtil.checkPassword(userInfoFromDB.getPassword(), userInfoFromDB.getSalt(), loginUser.getPassword());
            if(StringUtils.hasText(loginUser.getLoginIP())){
                userInfoFromDB.setLoginIP(loginUser.getLoginIP());
                this.updateById(userInfoFromDB);
            }
            StpUtil.login(userInfoFromDB.getUserId());
            return Result.success(ResultCodeAndMessage.LoginSuccess.getDescription());

        }
        throw new HttpException(ExceptionConstant.DateNull.getMessage_EN());
    }

    @Override
    public Result<?> updateUserInfo(UpdateUser updateUser) throws HttpException{
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        if(Objects.isNull(updateUser)){
            throw new HttpException(ExceptionConstant.DateNull.getMessage_EN());
        }
        User userGetFromDB = this.getById(userId);
        if(Objects.isNull(userGetFromDB)){
            throw new HttpException(ExceptionConstant.UserNotFound.getMessage_EN());
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(User::getUserId, userId);
        if(StringUtils.hasText(updateUser.getUserName())){
            updateWrapper.lambda().set(User::getUserName, updateUser.getUserName());
        }
        if(StringUtils.hasText(updateUser.getPassword())){
            if(!StringUtils.hasText(updateUser.getValidPassword()) || !StringUtils.hasText(updateUser.getOldPassword())){
                throw new HttpException(ExceptionConstant.DateNull.getMessage_EN());
            }
            securityUtil.checkPassword(userGetFromDB.getPassword(), userGetFromDB.getSalt(), updateUser.getOldPassword());
            if(!Objects.equals(updateUser.getPassword(), updateUser.getValidPassword())){
                throw new HttpException(ExceptionConstant.PasswordDifferent.getMessage_EN());
            }
            String salt = securityUtil.getSalt();
            String realPwd = securityUtil.addSaltIntoPassword(salt, updateUser.getPassword());
            updateWrapper.lambda().set(User::getPassword, securityUtil.AESEncryptForPwd(realPwd));
            updateWrapper.lambda().set(User::getSalt, securityUtil.AESEncryptForSalt(salt));
            updateWrapper.lambda().set(User::getUpdateTime, DateUtil.date(System.currentTimeMillis()));
        }
        if(!this.update(updateWrapper)){
            throw new HttpException();
        }
        StpUtil.logout(userId);
        return Result.success(ResultCodeAndMessage.UpdateSuccess.getDescription());
    }

    @Override
    public Result<?> getUserInfoByUserId(Long userId) throws HttpException{
        User userInfo = this.getById(userId);
        if(Objects.isNull(userInfo)){
            throw  new HttpException(ExceptionConstant.UserNotFound.getMessage_EN());
        }
        UserVO userVO = this.parseUserToUserVO(userInfo);
        return Result.success(ResultCodeAndMessage.QuerySuccess.getDescription(), userVO);
    }

    private User parseSignUpUserToNewUser(SignUpUser signUpUser){
        User user = new User();
        user.setUserName(signUpUser.getUserName());
        String salt = securityUtil.getSalt();
        String realPwd = securityUtil.addSaltIntoPassword(salt, signUpUser.getPassword());
        user.setPassword(securityUtil.AESEncryptForPwd(realPwd));
        user.setSalt(securityUtil.AESEncryptForSalt(salt));
        user.setCreateTime(DateUtil.date(System.currentTimeMillis()));
        return user;
    }

    private UserVO parseUserToUserVO(User user){
        UserVO userVO = new UserVO();
        userVO.setUserId(user.getUserId());
        userVO.setUserName(user.getUserName());
        return userVO;
    }
}




