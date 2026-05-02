package com.jeu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.spring6.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring6.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;

/**
 * Configuration Spring MVC + Thymeleaf.
 * Équivalent de dispatcher-servlet.xml en version Java.
 *
 * @EnableWebMvc active les fonctionnalités MVC avancées de Spring.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.jeu.controller")
public class WebMvcConfig implements WebMvcConfigurer {

    // ── Résolveur de templates Thymeleaf ─────────────────────────────────────
    @Bean
    public SpringResourceTemplateResolver templateResolver() {
        SpringResourceTemplateResolver resolver =
                new SpringResourceTemplateResolver();
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".html");
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setCharacterEncoding("UTF-8");
        resolver.setCacheable(false); // false en développement
        return resolver;
    }

    // ── Moteur de templates Thymeleaf ─────────────────────────────────────────
    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine engine = new SpringTemplateEngine();
        engine.setTemplateResolver(templateResolver());
        engine.setEnableSpringELCompiler(true);
        // Ajoute le dialecte Spring Security (th:sec:...)
        engine.addDialect(new SpringSecurityDialect());
        return engine;
    }

    // ── Vue Resolver (lie les noms de vues aux templates) ────────────────────
    @Bean
    public ThymeleafViewResolver viewResolver() {
        ThymeleafViewResolver resolver = new ThymeleafViewResolver();
        resolver.setTemplateEngine(templateEngine());
        resolver.setCharacterEncoding("UTF-8");
        resolver.setOrder(1);
        return resolver;
    }

    // ── Ressources statiques (CSS, images) ──────────────────────────────────
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/css/**")
                .addResourceLocations("/static/css/");
        registry.addResourceHandler("/images/**")
                .addResourceLocations("/static/images/");
    }
}
