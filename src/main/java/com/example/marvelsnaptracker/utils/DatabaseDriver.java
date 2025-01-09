package com.example.marvelsnaptracker.utils;

import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

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
            StringBuilder sb = new StringBuilder();

            ArrayList<String> queryList = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                // init.sql 에서 주석 제거하기
                if (!line.startsWith("--") && !line.isEmpty())
                    sb.append(line).append("\n");

                // 쿼리 구분하기
                if (line.endsWith(";")) {
                    queryList.add(sb.toString());
                    sb = new StringBuilder();
                }
            }

            // init.sql 의 모든 쿼리 추가
            for (String sql: queryList) {
                stmt.addBatch(sql);
            }

            // 실행.
            stmt.executeBatch();
        } catch (IOException e) {
            System.out.println("Error executing init.sql: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());     // 오류 메시지 출력
            System.out.println("SQL State: " + e.getSQLState());    // SQL 상태 코드 출력
            System.out.println("Error Code: " + e.getErrorCode());  // 오류 코드 출력
        }
    }
}
