package edu.mipt.service_discovery.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NonNull;

import java.util.List;

@AllArgsConstructor
@Builder
public class ClusterDto {
    @NonNull
    private String name;
    @NonNull
    private List<EndpointDto> endpointDtos;

}
