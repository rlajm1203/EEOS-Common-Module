## common-discovery 모듈

`common-discovery`는 `serviceId`를 실제 호출 가능한 `URI`로 변환해 주는 `ServiceDiscoveryClient` 인터페이스와 자동 구성(`ServiceDiscoveryAutoConfiguration`)을 제공합니다. Spring Cloud에서 제공하는 `LoadBalancerClient` 구현체(예: Eureka, Consul 등)에만 의존하므로, 구체적인 서비스 디스커버리 솔루션은 이 모듈을 사용하는 애플리케이션이 선택합니다.

### 주요 구성요소
- `ServiceDiscoveryClient`: 서비스 ID를 입력받아 기본 URI 또는 경로가 포함된 URI를 돌려주는 얇은 추상화입니다.
- `LoadBalancerServiceDiscoveryClient`: Spring Cloud `LoadBalancerClient`를 이용해 서비스 인스턴스를 선택하고 URI를 생성하는 기본 구현체입니다.
- `ServiceDiscoveryAutoConfiguration`: 클래스패스에 `LoadBalancerClient`가 존재하고, 사용자가 다른 `ServiceDiscoveryClient` 빈을 정의하지 않은 경우 위 구현체를 자동으로 등록합니다. `META-INF/spring/org.springframework.boot.autoconfigure.AutoConfiguration.imports`에 이 클래스가 선언되어 있어, 이 모듈을 의존성에 추가하기만 하면 자동 구성이 동작합니다.

### 의존성 전략
- 모듈 자체에는 `spring-cloud-commons`만 포함되어 있습니다. 이는 로드밸런서 SPI와 여러 디스커버리 솔루션이 공유하는 공통 추상화입니다.
- 실제 Eureka 서버와 통신하려면 **이 모듈을 사용하는 애플리케이션**에서 `spring-cloud-starter-netflix-eureka-client`(또는 다른 디스커버리 스타터)를 추가해야 합니다. 그러면 Spring Cloud가 `LoadBalancerClient`와 관련 빈을 제공하고, `ServiceDiscoveryAutoConfiguration`이 이를 감지해 `ServiceDiscoveryClient`를 등록합니다.
- 모듈 내부에 Eureka 스타터를 포함하지 않은 이유는, 소비 애플리케이션마다 다른 디스커버리 구현을 사용할 수 있도록 의존성을 최소화하기 위함입니다.

### 사용 방법
1. 애플리케이션의 `build.gradle`에 `common-discovery` 모듈을 의존성으로 추가합니다.
   ```groovy
   implementation project(':common-discovery')
   ```
2. 같은 애플리케이션에 원하는 디스커버리 구현(Eureka 등)을 추가합니다.
   ```groovy
   implementation 'org.springframework.cloud:spring-cloud-starter-netflix-eureka-client'
   ```
3. `@EnableDiscoveryClient`(또는 Spring Cloud 2022+에서 필요한 추가 설정)를 적용해 디스커버리 클라이언트를 활성화합니다.
4. 서비스 코드에서 `ServiceDiscoveryClient`를 주입받아 URI를 얻고, WebClient/RestTemplate 등으로 실제 통신을 수행합니다.
   ```java
   @Service
   public class ExampleService {
       private final ServiceDiscoveryClient discoveryClient;
       private final WebClient webClient;

       public ExampleService(ServiceDiscoveryClient discoveryClient, WebClient.Builder builder) {
           this.discoveryClient = discoveryClient;
           this.webClient = builder.build();
       }

       public Mono<String> callRemote(String serviceId) {
           URI uri = discoveryClient.getServiceUri(serviceId, "/api/v1/ping");
           return webClient.get().uri(uri).retrieve().bodyToMono(String.class);
       }
   }
   ```

### 예외 흐름
- 등록된 서비스 인스턴스가 없으면 `ServiceDiscoveryException`이 발생합니다. 이 예외를 잡아 폴백 로직을 수행하거나, 상위 계층에서 장애 상태를 감지해 재시도/알림을 처리할 수 있습니다.

이 README는 `common-discovery` 모듈의 설계 의도와 사용법을 한눈에 파악할 수 있도록 요약한 문서입니다. 필요한 추가 정보가 있다면 자유롭게 확장해 주세요.
