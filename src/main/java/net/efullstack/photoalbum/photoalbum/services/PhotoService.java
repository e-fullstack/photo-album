package net.efullstack.photoalbum.photoalbum.services;

import org.springframework.core.io.buffer.DataBuffer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PhotoService {
    Mono<Void> upload(String albumId, String fileName, Flux<DataBuffer> content);
    Flux<String> photos(String albumId);
}
