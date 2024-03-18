package com.backstage.xduedu.domain;

import com.backstage.xduedu.Exception.HttpException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.http.HttpStatus;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel("返回结果")
public class Result <T> {

    @ApiModelProperty("返回码")
    private Integer code;

    @ApiModelProperty("返回信息")
    private String message;

    @ApiModelProperty("返回数据")
    private T data;

    public static <T> Result<T> success(String message){
        return new Result<>(HttpStatus.OK.value(), message,null);
    }

    public static <T> Result<T> success(String message, T data){
        return new Result<>(HttpStatus.OK.value(), message,data);
    }

    public static <T> Result<T> success(Integer code, String message){
        return new Result<>(code,message,null);
    }

    public static <T> Result<T> success(Integer code,String message,T data){
        return new Result<>(code,message,data);
    }

    public static <T> Result<T> fail(Integer code,String message){
        return new Result<>(code,message,null);
    }

    public static <T> Result<T> fail(Integer code,String message,T data){
        return new Result<>(code,message,data);
    }

    public static <T> Result<T> fail(HttpException e){
        return new Result<>(e.getHttpStatusCode(), e.getMessage(), null);
    }
}
