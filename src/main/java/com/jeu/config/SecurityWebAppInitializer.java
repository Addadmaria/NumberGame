package com.jeu.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Enregistre automatiquement le filtre Spring Security (springSecurityFilterChain)
 * dans le contexte Tomcat SANS web.xml.
 *
 * Cette classe hérite de AbstractSecurityWebApplicationInitializer
 * qui s'occupe de tout via SPI (ServiceLoader de Servlet 3.0+).
 */
public class SecurityWebAppInitializer
        extends AbstractSecurityWebApplicationInitializer {
    // Pas besoin de code ici — tout est géré par la classe parente.
}
