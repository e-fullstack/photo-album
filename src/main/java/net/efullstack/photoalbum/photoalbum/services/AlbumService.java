package net.efullstack.photoalbum.photoalbum.services;

import net.efullstack.photoalbum.photoalbum.models.Album;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AlbumService {
    Flux<Album> getAlbums();
    Mono<Void> createAlbum(Album album);


    @Deprecated
    Flux<String> all();
    @Deprecated
    Mono<String> create(String name);

}
