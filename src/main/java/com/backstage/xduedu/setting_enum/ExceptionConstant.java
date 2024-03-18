package com.backstage.xduedu.setting_enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */

@AllArgsConstructor
@Getter
public enum ExceptionConstant {

    FileStreamError("There are some error for the file stream", "文件流错误"),
    InternalServerError( "Internal Server Error", "服务器未知错误"),
    DateNull("Some date is null", "存在空数据"),
    EncryptError(". Unable to encrypt", "无法进行加密"),
    DecryptError(". Unable to decrypt", "无法进行解密"),
    UserNotFound("Can not find the user information", "无法找到目标用户信息"),
    PasswordDifferent("The first password is different from the second password", "前后密码不一致"),
    EntryError("Entry is outside of the target dir", "条目在目标目录之外"),
    DataNotFound("Can not find the data", "无法找到目标数据"),
    DataError("The data is not we need", "数据错误"),
    FileTypeError("The file's type is not we need", "文件类型错误"),
    FileDirCreatError("Failed to create directory", "创建文件夹失败"),
    FileNameError("The file's name may have some error", "获取文件名错误"),
    FileCreateError("Zip file not found or is not readable", "压缩文件不存在或不可读"),
    ScriptRunError("Failed to run the script", "运行脚本失败"),
    PasswordError("The password check is wrong", "密码校验错误"),
    ThreadInterruptError("There has some error happened during the thread interrupt", "线程阻塞过程中发生错误"),
    FileDeleteError("There has some error happened during the file delete", "删除文件错误"),
    FilePartUploadError("There are some error during upload file to oss with part approach", "分片上传文件至OSS错误");

    private final String Message_EN;
    private final String MMessage_ZH;

}
