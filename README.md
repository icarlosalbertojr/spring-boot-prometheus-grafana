# Monitoring Spring Boot Application

### Main goals:

- [X] Configure Prometheus
- [X] Configure Grafana
- [ ] Obtains JVM metrics
- [ ] Obtains Spring metrics

 
## Configure Prometheus

- **Add dependencies:**
```groovy
implementation 'org.springframework.boot:spring-boot-starter-actuator'
implementation 'io.micrometer:micrometer-registry-prometheus:1.11.2'
```

- **Configure `application.yml`:**

```yaml
management:

  # Expose prometheus metrics
  endpoints:
    web:
      exposure:
        include: "health,prometheus"
    
    # Disable other metrics
    enabled-by-default: false

  # Enable endpoints to consume metrics
  endpoint:
    metrics:
      enabled: true
    health:
      enabled: true
      show-details: always
    prometheus:
      enabled: true
```

- **Configure Prometheus image (`docker-compose.yaml`):**

```yaml
version: "3.3"
volumes:
  prometheus_data:

services:
  prometheus:
    container_name: prometheus
    image: prom/prometheus:latest
    volumes:
      - ./config/prometheus.yml:/etc/prometheus/prometheus.yml
      - prometheus_data:/prometheus
    network_mode: host
    ports:
      - "9090:9090"
```

- **Create a `config` directory in root directory and add `prometheus.yml` file within:**

```yaml
global:
  scrape_interval: 5s
scrape_configs:
  - job_name: "api-test"
    
    # Define the endpoint to get prometheus metrics
    metrics_path: /actuator/prometheus
    static_configs:
      
      # Define host or DNS from API
      - targets: ["localhost:8080"]
        
        # Define labels to get application info
        labels:
          application: "api-test"
```

## Configure Grafana

- **Add Grafana in `docker-compose.yaml`**

    - **Create a volume:**
      ```yaml
      volumes:
          prometheus_data:
          grafana_data:
      ```
    - **Create a service:**
        ```yaml
          grafana:
            container_name: grafana
            image: grafana/grafana:latest
            ports:
              - "3000:3000"
            network_mode: host
        ```
      - **Docker compose file (final version):**
          ```yaml
            version: "3.3"
            volumes:
              prometheus_data:
              grafana_data:
            
            services:
              prometheus:
                container_name: prometheus
                image: prom/prometheus:latest
                volumes:
                  - ./config/prometheus.yml:/etc/prometheus/prometheus.yml
                  - prometheus_data:/prometheus
                network_mode: host
                ports:
                  - "9090:9090"
              grafana:
                container_name: grafana
                image: grafana/grafana:latest
                ports:
                  - "3000:3000"
                network_mode: host
          ```

