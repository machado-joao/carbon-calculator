package br.com.actionlabs.carboncalc.config;

import java.util.Comparator;
import java.util.TreeMap;

import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Paths;

@Configuration
public class OpenApiSwaggerConfig {

  @Bean
  public GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder()
        .group("spring")
        .packagesToScan("br.com.actionlabs.carboncalc.rest")
        .addOpenApiCustomizer(sortPathsAlphabetically())
        .build();
  }

  private OpenApiCustomizer sortPathsAlphabetically() {
    return openApi -> {
      TreeMap<String, io.swagger.v3.oas.models.PathItem> sortedPaths = new TreeMap<>(Comparator.naturalOrder());
      sortedPaths.putAll(openApi.getPaths());

      Paths paths = new Paths();
      paths.putAll(sortedPaths);

      openApi.setPaths(paths);
    };
  }

}
