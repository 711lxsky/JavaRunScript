package com.backstage.javarunscript.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.backstage.javarunscript.Exception.HttpException;
import com.backstage.javarunscript.domain.dto.LoginUser;
import com.backstage.javarunscript.domain.dto.SignUpUser;
import com.backstage.javarunscript.domain.Result;
import com.backstage.javarunscript.domain.dto.UpdateUser;
import com.backstage.javarunscript.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-17
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @ApiOperation(value = "用户注册")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "signUpUser", value = "用户注册信息", required = true, dataType = "SignUpUser")
    })
    @PostMapping("/sign-up")
    public Result<?> signUp(@RequestBody SignUpUser signUpUser) {
        try {
            return userService.signUp(signUpUser);
        }catch (HttpException e){
            e.printStackTrace();
            return Result.fail(e);
        }

    }

    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "loginUser", value = "用户登录信息", required = true, dataType = "LoginUser")
    })
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginUser loginUser){
        try {
            return userService.login(loginUser);
        }catch (HttpException e){
            e.printStackTrace();
            return Result.fail(e);
        }
    }

    @ApiOperation(value = "更新用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "updateUser", value = "用户更新信息", required = true, dataType = "UpdateUser")
    })
    @SaCheckLogin
    @PostMapping("/update")
    public Result<?> updateUserInfo(@RequestBody UpdateUser updateUser){
        try {
            return userService.updateUserInfo(updateUser);
        }
        catch (HttpException e){
            e.printStackTrace();
            return Result.fail(e);
        }
    }


    @ApiOperation(value = "根据用户ID获取用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户ID", required = true, dataType = "Long")
    })
    @SaCheckLogin
    @GetMapping("/info-user")
    public Result<?> getUserInfo(@RequestParam(name = "userId") Long userId){
        try {
            return userService.getUserInfoByUserId(userId);
        }
        catch (HttpException e){
            e.printStackTrace();
            return Result.fail(e);
        }
    }
}
