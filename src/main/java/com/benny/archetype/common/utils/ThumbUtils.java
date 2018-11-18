package com.benny.archetype.common.utils;

import net.coobird.thumbnailator.Thumbnails;

import java.io.File;
import java.io.IOException;

/**
 * @ClassName: ThumbUtils
 * @Description: 批量压缩照片
 * @author:大贝
 * @date:2018年10月26日 22:42
 */
public class ThumbUtils {
    enum ImageType {

        JPG, JPEG, PNG, GIF;

        public static boolean isImage(String imageType) {
            if (imageType == null) {
                return false;
            }
            for (ImageType type : ImageType.values()) {
                if (type.toString().equals(imageType.toUpperCase())) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 批量把sourceDir目录及子目录里的图片压缩
     * 输出目录为destDir,原子目录也随之创建
     *
     * @param sourceDir
     * @param destDir
     */
    public static void batchConvert(String sourceDir, String destDir,double scale) throws IOException {
        batchConvert(sourceDir, destDir, sourceDir,scale);
    }

    private static void batchConvert(String sourceDir, String destDir, String filePathString,double scale) throws IOException {
        File filePath = new File(filePathString);
        if (filePath.isDirectory()) {
            for (File it : filePath.listFiles()) {
                batchConvert(sourceDir, destDir, it.getAbsolutePath(),scale);
            }
        } else {
            String absolutePath = filePath.getAbsolutePath();
            String relativePath = absolutePath.substring(sourceDir.length(), absolutePath.length());
            convert(filePath.getAbsolutePath(), destDir + relativePath,scale);
        }
    }


    /**
     * 把source路径的图片压缩，输出到dest路径
     *
     * @param source 原图片路径
     * @param dest   压缩后保存的图片路径
     * @throws IOException
     */
    public static void convert(String source, String dest,double scale) throws IOException {

        File outPathDir = new File(getDirectoryPath(dest));
        if (!outPathDir.exists()) {
            outPathDir.mkdirs();
        }

        //-----------------------------------------------
        // 只对符合自定义图片扩展名的文件进行处理
        //-----------------------------------------------
        if (ImageType.isImage(getFileExt(source))) {
            Thumbnails.of(source).scale(scale).toFile(dest);
        }
        System.out.println("---------------------------");
        System.out.println("source: " + source);
        System.out.println("dest: " + dest);
        System.out.println("---------------------------");

    }

    /**
     * 返回文件的扩展名
     *
     * @param filePath
     * @return
     */
    public static String getFileExt(String filePath) {
        if (filePath != null && !"".equals(filePath)) {
            return filePath.substring(filePath.lastIndexOf(".") + 1, filePath.length());
        }
        return null;
    }

    /**
     * 返回文件(非文件夹)filePath的绝对目录路径
     *
     * @param filePath 输入的文件夹
     * @return
     */
    public static String getDirectoryPath(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf(File.separator));
    }

    public static void main(String[] args) throws IOException {

//        ThumbUtils.convert("/Coding/img1.jpg", "/Coding/img2.jpg");

        double scale = 0.7;
        //把/Coding/img目录及子目录所有的图片压缩至/Coding/img-thumb
        ThumbUtils.batchConvert("F:\\毛球\\照片\\freestyle", "F:\\毛球\\照片\\freestyle\\target",0.7);
//        ThumbUtils.batchConvert("F:\\毛球\\照片\\洱海", "F:\\毛球\\照片\\洱海\\target",0.7);
//        ThumbUtils.batchConvert("F:\\毛球\\照片\\绯红", "F:\\毛球\\照片\\绯红\\target",0.7);

    }
}
