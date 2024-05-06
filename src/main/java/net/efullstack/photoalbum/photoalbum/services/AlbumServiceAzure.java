package net.efullstack.photoalbum.photoalbum.services;

import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobRequestConditions;
import com.azure.storage.blob.options.BlobContainerCreateOptions;
import lombok.RequiredArgsConstructor;
import net.efullstack.photoalbum.photoalbum.models.Album;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AlbumServiceAzure implements AlbumService {
    private final BlobServiceAsyncClient blobServiceAsyncClient;

    @Override
    public Flux<Album> getAlbums() {
        return blobServiceAsyncClient
                .listBlobContainers()
                .map(container -> new Album(
                        container.getVersion() ,
                        container.getName(), Objects.nonNull(container.isDeleted()) ? container.isDeleted() : false,
                        Map.of(
                                "lastModified", container.getProperties().getLastModified().format(DateTimeFormatter.BASIC_ISO_DATE)))
                );
    }

    @Override
    public Mono<Void> createAlbum(Album album) {
        return blobServiceAsyncClient
                .createBlobContainerIfNotExistsWithResponse(album.name(), new BlobContainerCreateOptions().setMetadata(Map.of("test", "testMetaData")))
                .then();
    }

    @Override
    @Deprecated
    public Flux<String> all() {
        return blobServiceAsyncClient
                .listBlobContainers()
                .map(BlobContainerItem::getName);
    }

    @Override
    @Deprecated
    public Mono<String> create(String name) {
        return blobServiceAsyncClient
                .createBlobContainerIfNotExists(name)
                .map(BlobContainerAsyncClient::getBlobContainerName);
    }
}
