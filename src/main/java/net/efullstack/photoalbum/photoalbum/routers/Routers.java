package net.efullstack.photoalbum.photoalbum.routers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.efullstack.photoalbum.photoalbum.models.Album;
import net.efullstack.photoalbum.photoalbum.services.AlbumService;
import net.efullstack.photoalbum.photoalbum.services.PhotoService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class Routers {
    private final AlbumService albumService;
    private final PhotoService photoService;

    @Bean
    RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions
                .route()
                .path("/album", builder -> builder
                        .GET("", this::allAlbum)
                        .POST("", this::createAlbum)
                        .GET("/{id}", request -> ServerResponse.noContent().build())
                        .PUT("/{id}", request -> ServerResponse.noContent().build())
                        .DELETE("/{id}", request -> ServerResponse.noContent().build()))
                .path("/{albumId}/photo", builder -> builder
                        .GET("", this::albumPhotos)
                        .POST("", this::upload)
                        .GET("/{id}/view", this::viewPhoto)
                        .GET("/{id}/download", this::downloadPhoto)
                        .GET("/{id}/thumbnail", this::thumbnail)
                        .PUT("/{id}", request -> ServerResponse.noContent().build())
                        .DELETE("/{id}", request -> ServerResponse.noContent().build()))
                .build();
    }

    private Mono<ServerResponse> thumbnail(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> downloadPhoto(ServerRequest request) {
        return null;
    }

    private Mono<ServerResponse> viewPhoto(ServerRequest request) {
        var albumId = request.pathVariable("albumId");
        var photoId = request.pathVariable("id");
        return ServerResponse
                .ok()
                .contentType(MediaType.IMAGE_JPEG)
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .header("Content-Disposition", "inline")
//                .header("Content-Disposition", "attachment")
//                .header("Content-Disposition", "attachment; filename=\"%s\"".formatted(fileName))
                .body(photoService.viewPhoto(albumId,photoId), ByteBuffer.class);
    }

    private Mono<ServerResponse> albumPhotos(ServerRequest request) {
        var albumId = request.pathVariable("albumId");
        return ServerResponse.ok().body(photoService.photos(albumId), Flux.class);
    }

    private Mono<ServerResponse> upload(ServerRequest request) {
        var albumId = request.pathVariable("albumId");
        return request
                .multipartData()
                .map(stringPartMultiValueMap -> stringPartMultiValueMap.get("files"))
                .flatMapMany(Flux::fromIterable)
                .cast(FilePart.class)
                .flatMap(filePart -> {
                    log.info("{} file Processed", filePart.filename());
                    return photoService
                            .upload(albumId, filePart.filename(), filePart.content());
                })
                .then(ServerResponse.ok().build());
    }

    private Mono<ServerResponse> allAlbum(ServerRequest request) {
        return albumService
                .getAlbums()
                .collectList()
                .flatMap(ServerResponse.ok()::bodyValue);
    }

    private Mono<ServerResponse> createAlbum(ServerRequest request) {
        return request
                .bodyToMono(Album.class)
                .flatMap(albumService::createAlbum)
                .then(ServerResponse.ok().build());
    }
}
