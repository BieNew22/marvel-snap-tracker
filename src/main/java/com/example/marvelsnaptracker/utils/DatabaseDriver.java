package com.example.marvelsnaptracker.utils;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * db 와 통신 하는 부분 관리 하는 클래스, singleton
 */
public class DatabaseDriver {

    public void initDB() {
        String dbURL = "jdbc:sqlite:tracker.db";
        String initSqlFile = "init.sql";

        try (Connection conn = DriverManager.getConnection(dbURL)) {
            if (conn != null) {
                // init.sql 으로 db 초기화
                executeInitSql(conn, initSqlFile);
            }
        } catch (SQLException e) {
            System.out.println("Conn error : " + e.getMessage());
            // TODO : 프로그램 종료 코드 추가
            System.exit(-1);
        }
    }

    private void executeInitSql(Connection conn, String initSqlFile) {
        try (Statement stmt = conn.createStatement()) {
            // init.sql 파일 읽기
            BufferedReader br = new BufferedReader(new FileReader(initSqlFile));
            String line;
            StringBuilder sql = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if (!line.startsWith("--") && !line.isEmpty())
                    sql.append(line).append("\n");
            }

            System.out.println(sql.toString());
            // SQL 스크립트 실행
            stmt.execute(sql.toString());
        } catch (IOException e) {
            System.out.println("Error executing init.sql: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());     // 오류 메시지 출력
            System.out.println("SQL State: " + e.getSQLState());    // SQL 상태 코드 출력
            System.out.println("Error Code: " + e.getErrorCode());  // 오류 코드 출력
        }
    }
}
