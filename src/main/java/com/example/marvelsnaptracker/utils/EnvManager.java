package com.example.marvelsnaptracker.utils;

import io.github.cdimascio.dotenv.Dotenv;

/**
 * 환견 변수를 읽고 관리하는 클래스, Singleton
 */
public class EnvManager {
    private static final EnvManager instance = new EnvManager();

    public final String BASE_DIR;
    public final String CARD_IMG_URL;
    public final String CARD_DATA_URL;

    private EnvManager() {
        // .env 로 부터 환견 변수를 읽어서 제공하는 역할.
        Dotenv dotenv = Dotenv.load();

        BASE_DIR = dotenv.get("BASE_DIR");
        CARD_IMG_URL = dotenv.get("CARD_IMG_URL");
        CARD_DATA_URL = dotenv.get("CARD_DATA_URL");
    };

    public static EnvManager getInstance() { return instance;}
}
