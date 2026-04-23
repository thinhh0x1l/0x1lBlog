package top.blogapi.client.zing_mp3;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import top.blogapi.dto.response._common.MusicInfo;
import top.blogapi.exception.AppException;
import top.blogapi.exception.ErrorCode;
import top.blogapi.model.vo.BlogDetail;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class MusicService {
    Mp3Service mp3Service;
    ExecutorService executorService = Executors.newFixedThreadPool(4);

    public MusicInfo getCompleteSongData(String songId, int retry) {
        if (retry <= 0) {
            System.out.println("Hết retry");
            throw new AppException(ErrorCode.INTERNAL_ERROR,"Lấy dữ liệu bài hát thất bại");
        }
        try {
            CompletableFuture<Map<String, String>> infoFuture = CompletableFuture
                    .supplyAsync(() -> mp3Service.getSongInfo(songId), executorService)
                    .orTimeout(3, TimeUnit.SECONDS);

            CompletableFuture<String> streamingFuture = CompletableFuture
                    .supplyAsync(() -> mp3Service.getSongStreaming(songId), executorService)
                    .orTimeout(3, TimeUnit.SECONDS);

            CompletableFuture<String> lyricFuture = CompletableFuture
                    .supplyAsync(() -> {
                        try {
                            return mp3Service.getLyric(songId);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }, executorService)
                    .orTimeout(3, TimeUnit.SECONDS);

            CompletableFuture.allOf(infoFuture, streamingFuture, lyricFuture).join();
            var info = infoFuture.join();
            var lyric = lyricFuture.join();
            var streaming = streamingFuture.join();

            return new MusicInfo(
                    info.getOrDefault("title", "Unknown"),
                    lyric,
                    info.getOrDefault("name", "Unknown"),
                    streaming,
                    "#46718b",
                    info.getOrDefault("thumbnail", "")
            );
        } catch (Exception e) {
            System.out.println(e.getCause().toString());
            System.out.println(e.getMessage());
            System.out.println("[BlogOrchestrator-getCompleteSongData()]: ");
            try {
                mp3Service.reLoadRestClient();
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            return getCompleteSongData(songId, retry - 1);
        }
    }
}
