package edu.mipt.service_discovery.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Collection;

@Data
@Builder
public class ApiResponse {
    String version;
    Collection<String> resources;
    String errorDetail;
}
