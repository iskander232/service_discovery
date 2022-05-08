package edu.mipt.service_discovery.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@AllArgsConstructor
@Builder
@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ListenerDto {

    @NonNull
    String name;
    @NonNull
    String statPrefix;
    @NonNull
    String clusterName;
    @NonNull
    @Getter
    EndpointDto listenerEndpoint;
}
