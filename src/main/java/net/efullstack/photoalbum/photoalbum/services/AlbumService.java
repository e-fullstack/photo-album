package net.efullstack.photoalbum.photoalbum.services;

import com.azure.storage.blob.models.BlobContainerItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlbumService {
    Flux<String> all();
    Mono<String> create(String name);
    Mono<String> find(String id);
    Mono<String> update(String id, String newName);
    Mono<Void> delete(String albumId);

}
