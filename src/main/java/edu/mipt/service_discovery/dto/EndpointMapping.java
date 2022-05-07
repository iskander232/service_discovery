package edu.mipt.service_discovery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class EndpointMapping {
    EndpointDto from;
    EndpointDto to;
    String name;
}
