package com.jeu.config;

import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRegistration;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * Remplace complètement web.xml.
 * Tomcat détecte cette classe automatiquement via SPI (ServiceLoader).
 *
 * Équivalent XML de :
 *   <servlet> DispatcherServlet </servlet>
 *   <listener> ContextLoaderListener </listener>
 */
public class WebAppInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

        // 1. Contexte racine (services, repositories, sécurité)
        AnnotationConfigWebApplicationContext rootContext =
                new AnnotationConfigWebApplicationContext();
        rootContext.register(AppConfig.class, SecurityConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        // 2. Contexte Web (controllers, Thymeleaf)
        AnnotationConfigWebApplicationContext webContext =
                new AnnotationConfigWebApplicationContext();
        webContext.register(WebMvcConfig.class);

        // 3. DispatcherServlet — le "front controller" de Spring MVC
        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcher", new DispatcherServlet(webContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        // 4. Filtre encodage UTF-8
        FilterRegistration.Dynamic encodingFilter =
                servletContext.addFilter("encodingFilter",
                        new CharacterEncodingFilter("UTF-8", true));
        encodingFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}
