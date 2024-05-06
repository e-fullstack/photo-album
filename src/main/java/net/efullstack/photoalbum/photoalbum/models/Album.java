package net.efullstack.photoalbum.photoalbum.models;

import lombok.Builder;

import java.util.Map;

@Builder
public record Album(String id, String name, boolean isDeleted, Map<String, String> metadata) {
}
