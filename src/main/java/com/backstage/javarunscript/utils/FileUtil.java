package com.backstage.javarunscript.utils;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.util.RandomUtil;
import com.backstage.javarunscript.Exception.HttpException;
import com.backstage.javarunscript.config.FileConfig;
import com.backstage.javarunscript.setting_enum.ExceptionConstant;
import com.backstage.javarunscript.setting_enum.FileSetting;
import com.backstage.javarunscript.setting_enum.StringConstant;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @Author: 711lxsky
 * @Date: 2024-03-16
 */

@Component
public class FileUtil {

    @Resource
    private FileConfig fileConfig;


    public String getCurWordDir(){
        return System.getProperty(fileConfig.getWorker());
    }

    public String getAnalysisDir(){
        return this.getCurWordDir() + fileConfig.getWorkerFolder();
    }

    public String getAnalysisResFileFullName(){
        return this.getAnalysisDir()
                + StringConstant.Separator.getValue()
                + fileConfig.getScriptResFileName();
    }

    public String getUnZipDirForRandomName(){
        return this.getCurWordDir()
                + StringConstant.Separator.getValue()
                + RandomUtil.randomString(fileConfig.getRandomZipFileNameLength())
                + StringConstant.Dot.getValue()
                + FileSetting.ZipType.getValue();
    }

    public String getUnZipDir(String zipFileMainName){
        return this.getCurWordDir() + fileConfig.getWorkerFolder() + StringConstant.Separator.getValue() + zipFileMainName;
    }

    public String getPyScriptFolder() {
        return this.getCurWordDir() + fileConfig.getWorkerFolder() + fileConfig.getScriptsDir();
    }

    public String getPyScriptFullPath(){
        return this.getPyScriptFolder() + StringConstant.Separator.getValue() + fileConfig.getScriptName();
    }

    public InputStream getFileStream(MultipartFile file) throws HttpException {
        try {
            return file.getInputStream();
        }
        catch (IOException e) {
            throw new HttpException(
                    e.getMessage() + " --- " + ExceptionConstant.FileStreamError.getMessage_EN()
            );
        }
    }

    public String getFileMainName(String fullName) throws HttpException {
        Path path = Paths.get(fullName);
        return path.getFileName().toString().replaceAll("\\..*$", "");
    }

    public String getFileOriginalName(MultipartFile file) throws HttpException {
        String fileOriginName = file.getOriginalFilename();
        return StrUtil.judStrIsBlank(fileOriginName);
    }

    public void judFileTypeForZip(InputStream checkFileStream, String fileOriginName) throws HttpException {
        String fileType = FileTypeUtil.getType(checkFileStream, fileOriginName);
        if(!Objects.equals(fileType, FileSetting.ZipType.getValue())) {
            throw new HttpException(ExceptionConstant.FileTypeError.getMessage_EN());
        }
    }

    public void deleteDirectory(String folder) throws HttpException {
        Path path = Paths.get(folder);
        // 如果路径不存在或者是文件，直接删除
        if (Files.notExists(path)) {
            return;
        }
        try (Stream<Path> paths = Files.walk(path)) {
            paths.sorted(Comparator.reverseOrder())
                    .peek(p -> System.out.println("Deleting: " + p)) // 可选，显示即将删除的文件或目录
                    .forEach(p -> {
                        try {
                            Files.delete(p);
                        } catch (IOException e) {
                            // 在这里根据需要处理异常，例如打印日志等
                            throw new HttpException("Failed to delete " + p);
                        }
                    });
        } catch (IOException e) {
            // 处理Files.walk()可能抛出的异常
            throw new HttpException(e.getMessage());
        }

    }

    public void upZipFileToDest(File file, String destDir) throws HttpException {
        try {
            ZipInputStream zipInputStream = new ZipInputStream(Files.newInputStream(file.toPath()));
            if(!new File(destDir).mkdirs()){
                throw new HttpException(ExceptionConstant.FileDirCreatError.getMessage_EN());
            }
            ZipEntry zipEntry = zipInputStream.getNextEntry();
            byte [] buffer = new byte[fileConfig.getBufferLength()];
            while(zipEntry != null){
                File newFile = newFile(new File(destDir), zipEntry);
                if(zipEntry.isDirectory()) {
                    if(! newFile.isDirectory() && ! newFile.mkdirs()) {
                        throw new HttpException(ExceptionConstant.FileDirCreatError.getMessage_EN());
                    }
                }
                else {
                    File parent = newFile.getParentFile();
                    if(! parent.isDirectory() && ! parent.mkdirs()){
                        throw new HttpException(ExceptionConstant.FileDirCreatError.getMessage_EN());
                    }
                    FileOutputStream fos = new FileOutputStream(newFile);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, buffer.length);
                    int len;
                    while ((len = zipInputStream.read(buffer)) > 0) {
                        bos.write(buffer, 0, len);
                    }
                    bos.close();
                }
                zipEntry = zipInputStream.getNextEntry();
            }
            zipInputStream.closeEntry();
            zipInputStream.close();
        } catch (IOException e) {
            throw new HttpException(e.getMessage());
        }
    }

    // 避免Zip文件解压路径遍历漏洞
    private File newFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new HttpException(zipEntry.getName() + ExceptionConstant.EntryError.getMessage_EN());
        }

        return destFile;
    }

    public void zipFileForDir(String sourceFolderDir, String targetZipDir) throws HttpException{
        Path targetZipPath = Paths.get(targetZipDir);
        Path sourceFolderPath = Paths.get(sourceFolderDir);
        try(ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(targetZipPath.toFile().toPath()))) {
            Files.walkFileTree(sourceFolderPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
                    zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(file).toString()));
                    Files.copy(file, zos);
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attributes) throws IOException {
                    if (!sourceFolderPath.equals(dir)) {
                        zos.putNextEntry(new ZipEntry(sourceFolderPath.relativize(dir) + "/"));
                        zos.closeEntry();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new HttpException(e.getMessage());
        }
    }

    public File createFileForZip(String zipFilePath) throws HttpException {
        File zipFile = new File(zipFilePath);
        if(!(zipFile.exists() && zipFile.canRead())){
          throw new HttpException(ExceptionConstant.FileCreateError.getMessage_EN());
        }
        return zipFile;
    }

    public File convertMultipartFileToFile(MultipartFile multipartFile) throws HttpException{
        try {
            // 生成临时文件的文件名，避免文件名冲突
            String fileName = multipartFile.getOriginalFilename();
            File tempFile = File.createTempFile(fileConfig.getUploadFilePrefix(), fileName);

            // 将MultipartFile转换成java.io.File对象
            multipartFile.transferTo(tempFile);

            // 返回转换后的File对象
            return tempFile;
        } catch (IOException e) {
            throw new HttpException(e.getMessage());
        }
    }

    public void deleteFile(File file) throws HttpException{
        String name = file.getName();
        if(!file.delete()){
            throw new HttpException(ExceptionConstant.FileDeleteError.getMessage_EN() + "  " + name);
        }
    }
}
