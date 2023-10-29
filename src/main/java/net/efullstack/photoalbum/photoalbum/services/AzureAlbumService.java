package net.efullstack.photoalbum.photoalbum.services;

import com.azure.storage.blob.BlobContainerAsyncClient;
import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.models.BlobContainerItem;
import com.azure.storage.blob.models.BlobItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AzureAlbumService implements AlbumService {
    private final BlobServiceAsyncClient blobServiceAsyncClient;

    @Override
    public Flux<String> all() {
        return blobServiceAsyncClient
                .listBlobContainers()
                .map(BlobContainerItem::getName);
    }

    @Override
    public Mono<String> create(String name) {
        return blobServiceAsyncClient
                .createBlobContainerIfNotExists(name)
                .map(BlobContainerAsyncClient::getBlobContainerName);
    }

    @Override
    public Mono<String> find(String id) {
        return null;
    }

    @Override
    public Mono<String> update(String id, String newName) {
        return null;
    }

    @Override
    public Mono<Void> delete(String albumId) {
        return null;
    }
}
