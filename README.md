- [Reference](https://learn.microsoft.com/en-us/java/api/com.azure.core.http.rest.pagedflux?view=azure-java-stable)

cURL request

Create Album

```shell
curl --location 'localhost:8080/album' \
--header 'Content-Type: text/plain' \
--data 'album-2'
```

All Album

```shell
curl --location --request GET 'localhost:8080/album' \
--header 'Content-Type: text/plain' \
--data '1'
```

Upload photos to the album

```shell
curl --location 'localhost:8080/album-1/photo' \
--form 'files=@"/path/to/image.png"' \
--form 'files=@"/path/to/image1.png"'
```
Show Album content

```shell
curl --location 'http://localhost:8080/album-1/photo'
```