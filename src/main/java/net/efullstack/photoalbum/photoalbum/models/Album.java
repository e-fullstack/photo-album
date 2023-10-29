package net.efullstack.photoalbum.photoalbum.models;

import lombok.Builder;

@Builder
public record Album(String id, String name, boolean isDeleted, String version) {
}
