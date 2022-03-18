package com.zt.mypassword.utils;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import com.zt.mypassword.exception.custom.OperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2019/1/29 15:52
 * description:
 */
@Slf4j
public class FileUtils {

   private static final Snowflake snowflake = IdUtil.getSnowflake();

    public static String handleFileName(String userAgent, String fileName) {
        if (userAgent.contains("MSIE") || userAgent.contains("TRIDENT") || userAgent.contains("EDGE")) {
            //IE下载文件名空格变+号问题
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replace("+", "%20");
        } else {
            fileName = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }
        return fileName;
    }


    /**
     * 下载文件
     *
     * @param request  request
     * @param response response
     * @param path     path
     */
    public static void downLoadFile(HttpServletRequest request, HttpServletResponse response, Path path){
        if (request != null && response != null
                && path != null) {

            String fileName = path.getFileName().toString();
            // filePath是指欲下载的文件的路径。
            if (Files.notExists(path)) {
                throw new OperationException("文件不存在！");
            }

            HttpServletUtils.modificationResponse(request, response, fileName);
            //NIO 实现
            int bufferSize = 131072;
            //读出文件到i/o流

            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(path.toFile());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assert fileInputStream != null;
            FileChannel fileChannel = fileInputStream.getChannel();
            // 6x128 KB = 768KB byte buffer
            ByteBuffer buff = ByteBuffer.allocateDirect(786432);
            byte[] byteArr = new byte[bufferSize];
            int nRead, nGet;
            try {

                while ((nRead = fileChannel.read(buff)) != -1) {
                    if (nRead == 0) {
                        continue;
                    }
                    buff.position(0);
                    buff.limit(nRead);
                    while (buff.hasRemaining()) {
                        int remaining = buff.remaining();
                        nGet = Math.min(remaining, bufferSize);
//                         read bytes from disk
                        buff.get(byteArr, 0, nGet);
//                         write bytes to output
                        response.getOutputStream().write(byteArr, 0, nGet);
                    }
                    buff.clear();
                    log.info("文件下载完毕！");
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    buff.clear();
                    fileChannel.close();
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public static String writeDisk(MultipartFile multipartFile, Path writePath) {
        if (!Files.exists(writePath)) {
            try {
                Files.createDirectories(writePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (multipartFile != null) {
            String originalFileName = multipartFile.getOriginalFilename();//原名称 带后缀
            assert originalFileName != null;
            String filePath = writePath + File.separator + snowflake.nextId() + "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
            try {
                if (!Files.exists(Paths.get(filePath))) {
                    Files.createFile(Paths.get(filePath));
                }
                Files.write(Paths.get(filePath), multipartFile.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return filePath;
        }
        return null;

    }
}
