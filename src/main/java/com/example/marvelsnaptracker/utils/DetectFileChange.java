package com.example.marvelsnaptracker.utils;

import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class DetectFileChange {
    void dectect() {
        Path path = Paths.get(EnvManager.getInstance().BASE_DIR);

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            path.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

            System.out.println("디렉터리 변경 사항을 감시 중: " + path);

            while (true) {
                WatchKey key = watchService.take();

                for (WatchEvent<?> event: key.pollEvents()) {
                    Path fillName = (Path) event.context();

                    System.out.println(fillName);
                }

                boolean valid = key.reset();
                if (!valid)
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
