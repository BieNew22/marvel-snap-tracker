package com.example.marvelsnaptracker.utils;


import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

/**
 * webp 파일을 png으로 바꾸어서 로컬에 저장하는 역할을 수행. singleton
 */
public class WebPToPNGConverter {
    @Getter
    private static final WebPToPNGConverter instance = new WebPToPNGConverter();

    private WebPToPNGConverter() { };

    /**
     * card png 파일 유무 확인 -> 없으면 다운로드
     * @param name 확인할 파일 이름
     * @return boolean (true : 존재O, false : 존재X 및 다운로드 실패)
     */
    public boolean convert(String name) {
        String pngPath = "images/" + name + ".png";
        String webpPath = "images/" + name + ".webp";

        String imageUrl = EnvManager.getInstance().CARD_URL + name + ".webp";
        try {
            // 변환된 파일 유무 확인
            File pngFile = new File(pngPath);

            // 이미 변환된 파일이 존재 함.
            if (pngFile.exists())
                return true;

            // webp 다운로드 하기
            URL link = new URL(imageUrl);

            BufferedInputStream inputStream = new BufferedInputStream(link.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(webpPath);

            byte[] buffer = new byte[1024];
            int byteRead;

            while ((byteRead = inputStream.read(buffer, 0, 1024)) != -1) {
                fileOutputStream.write(buffer, 0, byteRead);
            }

            // webp -> png 변환
            File input = new File(webpPath);
            BufferedImage image = ImageIO.read(input);

            // png으로 변환
            File output = new File(pngPath);
            ImageIO.write(image, "png", output);

            return true;
        } catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
