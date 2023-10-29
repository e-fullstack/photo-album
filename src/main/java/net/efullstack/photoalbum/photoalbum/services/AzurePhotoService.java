package net.efullstack.photoalbum.photoalbum.services;

import com.azure.storage.blob.BlobServiceAsyncClient;
import com.azure.storage.blob.models.BlobItem;
import com.azure.storage.blob.models.ParallelTransferOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AzurePhotoService implements PhotoService {

    private final BlobServiceAsyncClient blobServiceAsyncClient;
    @Override
    public Mono<Void> upload(String albumId, String fileName, Flux<DataBuffer> content) {
        return blobServiceAsyncClient
                .getBlobContainerAsyncClient(albumId)
                .getBlobAsyncClient(fileName)
                .upload(content.map(DataBuffer::toByteBuffer), new ParallelTransferOptions().setBlockSizeLong(2 * 1024L * 1024L).setMaxConcurrency(5), true)
                .doOnError(e -> log.error("Upload ERROR {}", e.getMessage()))
                .doOnSuccess(r -> log.info("Upload SUCCESS {}", r.getVersionId()))
                .then();
    }

    @Override
    public Flux<String> photos(String albumId) {
        return blobServiceAsyncClient
                .getBlobContainerAsyncClient(albumId)
                .listBlobs()
                .map(BlobItem::getName);
    }
}
