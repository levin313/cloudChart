package com.levin.cloud.util;



import java.io.File;

public class FileUtils {


    /**
     * 不允许实例化
     */
    private FileUtils() {

    }


    /**
     * 创建目录
     */
    public static File createDir(String dirPath) {
        File dir;
        try {
            dir = new File(dirPath);
            if (!dir.exists()) {
                org.apache.commons.io.FileUtils.forceMkdir(dir);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return dir;
    }

    /**
     * 创建文件
     */
    public static File createFile(String filePath) {
        File file;
        try {
            file = new File(filePath);
            File parentDir = file.getParentFile();
            if (!parentDir.exists()) {
                org.apache.commons.io.FileUtils.forceMkdir(parentDir);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file;
    }

    /**
     * 复制目录（不会复制空目录）
     */
    public static void copyDir(String srcPath, String destPath) {
        try {
            File srcDir = new File(srcPath);
            File destDir = new File(destPath);
            if (srcDir.exists() && srcDir.isDirectory()) {
                org.apache.commons.io.FileUtils.copyDirectoryToDirectory(srcDir, destDir);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 复制文件
     */
    public static void copyFile(String srcPath, String destPath) {
        try {
            File srcFile = new File(srcPath);
            File destDir = new File(destPath);
            if (srcFile.exists() && srcFile.isFile()) {
                org.apache.commons.io.FileUtils.copyFileToDirectory(srcFile, destDir);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除目录
     */
    public static void deleteDir(String dirPath) {
        try {
            File dir = new File(dirPath);
            if (dir.exists() && dir.isDirectory()) {
                org.apache.commons.io.FileUtils.deleteDirectory(dir);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除文件
     */
    public static void deleteFile(String filePath) {
        try {
            File file = new File(filePath);
            if (file.exists() && file.isFile()) {
                org.apache.commons.io.FileUtils.forceDelete(file);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 重命名文件
     */
    public static void renameFile(String srcPath, String destPath) {
        File srcFile = new File(srcPath);
        if (srcFile.exists()) {
            File newFile = new File(destPath);
            boolean result = srcFile.renameTo(newFile);
            if (!result) {
                throw new RuntimeException("重命名文件出错！" + newFile);
            }
        }
    }



    /**
     * 判断文件是否存在
     */
    public static boolean checkFileExists(String filePath) {
        return new File(filePath).exists();
    }

    /**
     * 获取应用根路径
     *
     * @return
     */
    public static String getRootPath() {
        return System.getProperty("user.dir");
    }




}
