package edu.mipt.service_discovery;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mipt.service_discovery.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.websocket.server.PathParam;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class ServiceDiscoveryApi {
    int version_id;

    String contoller_url = "http://localhost:8079/controller/add-config";
    List<EnvoyConfig> requests;
    int last_id = 0;

    private final EnvoyConfig request1 = EnvoyConfig.builder()
            .envoy_id(EnvoyId.builder()
                .cluster_id("test-cluster")
                .node_id("test-id")
                .build())
            .endpoint_mappings(Arrays.asList(
                EndpointMapping.builder()
                    .to(EndpointDto.builder()
                        .address("127.0.0.1")
                        .port(8080)
                        .build())
                    .name("first")
                    .from(EndpointDto.builder()
                        .port(10000)
                        .address("127.0.0.1")
                        .build())
                    .build()))
            .version("0")
            .build();
    private final EnvoyConfig request2 =  EnvoyConfig.builder()
        .envoy_id(EnvoyId.builder()
            .cluster_id("test-cluster")
            .node_id("test-id")
            .build())
        .endpoint_mappings(Arrays.asList(
            EndpointMapping.builder()
                .to(EndpointDto.builder()
                    .address("127.0.0.1")
                    .port(8085)
                    .build())
                .name("second")
                .from(EndpointDto.builder()
                    .port(10000)
                    .address("127.0.0.1")
                    .build())
                .build(),
                EndpointMapping.builder()
                    .to(EndpointDto.builder()
                        .address("127.0.0.1")
                        .port(8095)
                        .build())
                    .name("second")
                    .from(EndpointDto.builder()
                        .port(10000)
                        .address("127.0.0.1")
                        .build())
                    .build()))
        .version("1")
        .build();

    private final EnvoyConfig request3 =  EnvoyConfig.builder()
        .envoy_id(EnvoyId.builder()
            .cluster_id("test-cluster")
            .node_id("test-id")
            .build())
        .version("2")
        .build();

    private final EnvoyConfig request4 = EnvoyConfig.builder()
        .envoy_id(EnvoyId.builder()
            .cluster_id("test-cluster")
            .node_id("test-id")
            .build())
        .endpoint_mappings(Arrays.asList(
            EndpointMapping.builder()
                .to(EndpointDto.builder()
                    .address("127.0.0.1")
                    .port(8085)
                    .build())
                .name("fourth")
                .from(EndpointDto.builder()
                    .port(10000)
                    .address("127.0.0.1")
                    .build())
                .build()))
        .version("0")
        .build();

    private final RestTemplate restTemplate;

    public ServiceDiscoveryApi(RestTemplateBuilder restTemplateBuilder) {
        version_id = 0;
        requests = Arrays.asList(request1, request2, request3, request4);

        restTemplate = restTemplateBuilder.build();
    }

    @GetMapping("set_snapshot_id/{id}")
    public ResponseEntity<String> setSnapshotId(@PathVariable int id) {
        if (0 <= id && id < requests.size()) {
            last_id = id;
            version_id++;
            ResponseEntity<String> response = restTemplate.exchange(contoller_url, HttpMethod.POST, buildRequest(requests.get(id), version_id, true), String.class);
            log.info(String.format("[setSnapshotId] version %d, id %d, response %s", version_id, id, response.getBody()));
            return response;
        } else {
            return ResponseEntity.badRequest().body("id is too big or too little");
        }
    }

    @GetMapping("update-result")
    public ResponseEntity<String> updateResult(
            @PathParam("cluster_id") String cluster,
            @PathParam("node_id") String node_id
    ) {
        HttpEntity<String> response = buildRequest(requests.get(last_id), version_id, false);
        log.info(String.format("[updateResult] version %d, id %d, response %s", version_id, last_id, response.getBody()));
        return ResponseEntity.ok(response.getBody());
    }

    @PostMapping("service/version/deploy")
    public void newStatus(@RequestBody EnvoyConfig envoyConfig) {
        log.info("[newStatus]  envoyConfig = {}", envoyConfig);
    }

    private HttpEntity<String> buildRequest(EnvoyConfig envoyConfig, int version_id, boolean isApiRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", MediaType.APPLICATION_JSON_VALUE);

        envoyConfig.setVersion(String.valueOf(version_id));
        ObjectMapper mapper = new ObjectMapper();
        try {
            String json;
            if (isApiRequest) {
                json = mapper.writeValueAsString(AddConfigsRequest.builder().version(envoyConfig.getVersion()).configs(List.of(envoyConfig)).build());
            } else {
                json = mapper.writeValueAsString(envoyConfig);
            }
            log.info(json);
            return new HttpEntity<>(json, headers);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        throw new IllegalStateException("cant convert json");
    }
}
