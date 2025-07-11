package com.nusiss.userservice.config;
import com.nusiss.userservice.dao.PermissionRepository;
import com.nusiss.userservice.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class ApiPermissionLoader implements CommandLineRunner {

    @Autowired
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @Autowired
    private PermissionRepository permissionRepository;

    @Override
    public void run(String... args) {
        requestMappingHandlerMapping.getHandlerMethods().forEach((info, method) -> {

            if (info.getPatternsCondition() == null || info.getMethodsCondition() == null) {
                return; // skip non-standard handlers
            }

            Set<String> patterns = info.getPatternsCondition().getPatterns();
            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();

            if (patterns == null || patterns.isEmpty() || methods == null || methods.isEmpty()) {
                return; // skip if empty
            }

            for (String url : patterns) {
                for (RequestMethod reqMethod : methods) {
                    if (!permissionRepository.existsByEndpointAndMethod(url, reqMethod.name())) {
                        Permission p = new Permission();
                        p.setEndpoint(url);
                        p.setMethod(reqMethod.name());
                        p.setCreateUser("SYSTEM");
                        p.setCreateDatetime(LocalDateTime.now());
                        permissionRepository.save(p);
                        System.out.println("Saved permission: " + url + " [" + reqMethod.name() + "]");
                    }
                }
            }
        });
    }
}
