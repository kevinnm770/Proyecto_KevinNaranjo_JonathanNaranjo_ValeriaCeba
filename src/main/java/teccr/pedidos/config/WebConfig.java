package teccr.pedidos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Todo lo que pidan en /uploads/** se busca en la carpeta "uploads" del disco
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }
}