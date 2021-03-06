package edu.mipt.service_discovery.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Data;

import java.util.List;
@Data
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class EnvoyConfig {
    String version;
    EnvoyId envoy_id;
    EndpointDto monitoring_endpoint;
    List<EndpointMapping> endpoint_mappings;
}
