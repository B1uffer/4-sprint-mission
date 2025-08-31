package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@SpringBootTest
@TestPropertySource(properties = {
        "discodeit.storage.type=s3" // 조건부 빈 활성화
})
class S3BinaryContentStorageTest {

    @Autowired
    BinaryContentStorage storage; // S3BinaryContentStorage 주입 기대

    @Test
    @DisplayName("Bean 주입: S3BinaryContentStorage 활성화")
    void beanInjected_whenTypeIsS3() {
        assertThat(storage).isNotNull();
        assertThat(storage.getClass().getSimpleName()).isEqualTo("S3BinaryContentStorage");
    }

    @Test
    @DisplayName("업로드")
    void put_uploadsBytes() {
        UUID id = UUID.randomUUID();
        storage.put(id, "hello s3".getBytes(StandardCharsets.UTF_8));
        assertThat(id).isNotNull();
    }

    @Test
    @DisplayName("다운로드(get)")
    void get_downloadsBytes() throws Exception {
        UUID id = UUID.randomUUID();
        byte[] data = "download me".getBytes(StandardCharsets.UTF_8);
        storage.put(id, data);

        try (InputStream in = storage.get(id)) {
            String s = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            assertThat(s).isEqualTo("download me");
        }
    }

    @Test
    @DisplayName("download(): PresignedUrl 302 Redirect")
    void download_redirectsToPresignedUrl() {
        UUID id = UUID.randomUUID();
        BinaryContentDto dto = new BinaryContentDto(
                id,
                "sample.txt",
                11L,
                "text/plain"
        );
        var resp = storage.download(dto);
        assertThat(resp.getStatusCode().is3xxRedirection()).isTrue();
        assertThat(resp.getHeaders().getLocation()).isNotNull();
        assertThat(resp.getHeaders().getLocation().toString()).startsWith("http");
    }
}
