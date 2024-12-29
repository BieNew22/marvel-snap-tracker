package com.example.marvelsnaptracker.utils;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * 환견 변수를 읽고 관리하는 클래스
 */
public class EnvManager {
    private static final EnvManager instance = new EnvManager();

    public final String BASE_DIR;

    private EnvManager() {
        Dotenv dotenv = Dotenv.load();

        BASE_DIR = dotenv.get("BASE_DIR");
    };

    public static EnvManager getInstance() { return instance;}
}
