package com.github.hugovallada;

import java.util.UUID;

public record RideDTORequest(UUID accountId, Coordinates from, Coordinates to) {
}

