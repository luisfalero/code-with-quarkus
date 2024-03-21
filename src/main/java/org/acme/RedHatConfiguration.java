package org.acme;

import io.smallrye.config.ConfigMapping;

@ConfigMapping(prefix = "redhat")
public interface RedHatConfiguration {

    String operationName();
    String operationNamespace();
    
}
