package edu.mipt.service_discovery.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ClusterDto {
    @NonNull
    private String name;
    @NonNull
    private List<EndpointDto> endpointDtos;


    public ClusterDto(EndpointDto endpointDto, String clusterName) {
        this.name = clusterName;
        this.endpointDtos = new ArrayList<>();
        addEndpoint(endpointDto);
    }

    public void addEndpoint(EndpointDto endpointDto) {
        endpointDtos.add(endpointDto);
    }
}
