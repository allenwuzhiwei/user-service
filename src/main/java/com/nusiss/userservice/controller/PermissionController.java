package com.nusiss.userservice.controller;

import com.nusiss.userservice.config.ApiResponse;
import com.nusiss.userservice.dao.PermissionRepository;
import com.nusiss.userservice.dto.PermissionDTO;
import com.nusiss.userservice.entity.Permission;
import com.nusiss.userservice.entity.UserRole;
import com.nusiss.userservice.service.PermissionService;
import com.nusiss.userservice.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

//@CrossOrigin(origins = "http://localhost:5000")
@RestController
@RequestMapping("/api/permissions")
public class PermissionController {

    private final RequestMappingHandlerMapping handlerMapping;

    @Autowired
    public PermissionController(ApplicationContext context) {
        this.handlerMapping = context.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
    }

    @Autowired
    UserService userService;

    @RequestMapping(value = "/permission", method = RequestMethod.GET)
    public Boolean checkIfHasPermission(@RequestHeader("authToken") String authToken,
                                        @RequestParam(value = "url") String url,
                                        @RequestParam(value = "method") String method){

        return userService.hasPermission(authToken, url, method);
    }

    /*@PostMapping("/{userId}/roles/{roleId}")
    public UserRole assignRole(@PathVariable Integer userId, @PathVariable Integer roleId) {
        return userService.assignRole(userId, roleId);
    }*/

    @Autowired
    PermissionService permissionService;

    @Autowired
    private PermissionRepository permissionRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> getPermissionById(@PathVariable Integer id) {
        Optional<Permission> p = permissionService.get(id);
        return p.map(value -> ResponseEntity.ok(new ApiResponse<>(true, "Permission found", value)))
                .orElse(ResponseEntity.status(404).body(new ApiResponse<>(false, "Permission not found", null)));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Permission>> createPermission(@RequestBody Permission dto) {
        Permission created = permissionService.create(dto);
        return ResponseEntity.ok(new ApiResponse<>(true, "Permission created successfully", created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Permission>> updatePermission(@PathVariable Integer id, @RequestBody Permission dto) {
        try {
            Permission updated = permissionService.update(id, dto);
            return ResponseEntity.ok(new ApiResponse<>(true, "Permission updated successfully", updated));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(new ApiResponse<>(false, e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePermission(@PathVariable Integer id) {
        permissionService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Permission deleted successfully", null));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PermissionDTO>>> getAllPermissions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createDatetime") String sortBy,
            @RequestParam(defaultValue = "") String endpoint,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort sort = direction.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() :
                Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PermissionDTO> permissions = permissionService.getAllPermissionsDTO(endpoint, pageable);

        ApiResponse<Page<PermissionDTO>> response =
                new ApiResponse<>(true, "Permissions retrieved successfully", permissions);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/findPermissionsByUserId")
    public ResponseEntity<Set<String>> findPermissionsByUserId(@RequestParam("userId") Integer userId){

        return ResponseEntity.ok(permissionService.findPermissionsByUserId(userId));
    }

    @PostMapping("/paged/endpoints")
    public ResponseEntity<Map<String, Object>> listAllApiEndpoints(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String name) {

        List<String> allEndpoints = new ArrayList<>();

        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        for (Map.Entry<RequestMappingInfo, HandlerMethod> entry : handlerMethods.entrySet()) {
            RequestMappingInfo info = entry.getKey();

            Set<PathPattern> pathPatterns = Optional.ofNullable(info.getPathPatternsCondition())
                    .map(condition -> condition.getPatterns())
                    .orElse(Collections.emptySet());

            Set<RequestMethod> methods = info.getMethodsCondition().getMethods();

            for (PathPattern pattern : pathPatterns) {
                String path = pattern.getPatternString();
                if (methods.isEmpty()) {
                    allEndpoints.add("ALL:" + path);
                } else {
                    for (RequestMethod method : methods) {
                        allEndpoints.add(method.name() + ":" + path);
                        if(StringUtils.isNotBlank(path) && !path.contains("/v3")){
                            String desc = generateDescription(method.name(), path);
                            Permission p = new Permission();
                            p.setEndpoint("/users"+path);
                            p.setMethod(method.name());
                            p.setDescription(desc);
                            permissionRepository.save(p);
                        }
                    }
                }
            }
        }

        // Filter and paginate
        List<String> filtered = allEndpoints.stream()
                .filter(endpoint -> endpoint.toLowerCase().contains(name.toLowerCase()))
                .sorted()
                .collect(Collectors.toList());

        int start = page * size;
        int end = Math.min(start + size, filtered.size());
        List<String> paged = (start < end) ? filtered.subList(start, end) : Collections.emptyList();

        Map<String, Object> response = new HashMap<>();
        response.put("content", paged);
        response.put("totalElements", filtered.size());
        response.put("totalPages", (int) Math.ceil((double) filtered.size() / size));
        response.put("page", page);

        return ResponseEntity.ok(Collections.singletonMap("data", response));
    }

    public String generateDescription(String method, String endpoint) {
        // Step 1: Extract resource
        String resource = endpoint.replaceFirst("^/api/", "");
        resource = resource.replaceAll("\\{[^}]+\\}", ""); // remove {id}, etc.
        resource = resource.replaceAll("//+", "/"); // clean double slashes
        resource = resource.replaceAll("^/|/$", ""); // remove leading/trailing slash
        String[] parts = resource.split("/");

        String resourceName = parts.length > 0 ? parts[0] : "resource";
        resourceName = resourceName.replace("-", " "); // optional formatting

        // Step 2: Map HTTP method to action
        String action;
        switch (method.toUpperCase()) {
            case "GET":
                action = endpoint.contains("{") ? "Get" : "Get list of";
                break;
            case "POST":
                action = "Save";
                break;
            case "PUT":
                action = "Update";
                break;
            case "DELETE":
                action = "Delete";
                break;
            default:
                action = "Call";
        }

        // Step 3: Assemble
        return action + " " + resourceName;
    }

}
