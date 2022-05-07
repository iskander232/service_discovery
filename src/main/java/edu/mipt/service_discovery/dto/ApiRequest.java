package edu.mipt.service_discovery.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class ApiRequest {
    @NonNull
    String version;
    @NonNull
    EnvoyId envoyId;
    EndpointDto monitoringEndpoint;
    List<EndpointMapping> endpointMappings;
}
