package com.app.benevole;

import com.app.benevole.model.Permission;
import com.app.benevole.model.PermissionParent;
import com.app.benevole.model.Role;
import com.app.benevole.model.User;
import com.app.benevole.repository.PermissionParentRepository;
import com.app.benevole.repository.PermissionRepository;
import com.app.benevole.repository.RoleRepository;
import com.app.benevole.repository.UserRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

@SpringBootApplication
public class BenevoleApplication implements CommandLineRunner {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private PermissionParentRepository parentRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Value(value = "${spring.application.name}")
    private String appTitle;

    public static void main(String[] args) {
        SpringApplication.run(BenevoleApplication.class, args);
    }

    @Bean
    public OpenAPI customOpenAPI(@Value("${application.description}") String appDescription,
                                 @Value("${application.version}") String appVersion) {
        return new OpenAPI()
                .info(new Info()
                        .title(appTitle)
                        .version(appVersion)
                        .description(appDescription)
                        .termsOfService("http://swagger.io/terms/")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .components(new Components()
                        .addSecuritySchemes("Authorization", new SecurityScheme()
                                .type(SecurityScheme.Type.APIKEY)
                                .in(SecurityScheme.In.HEADER)
                                .name("Authorization")))
                .addSecurityItem(new SecurityRequirement().addList("Authorization"));
    }

    @Override
    public void run(String... args) throws Exception {
        List<PermissionParent> parents = parentRepository.findAll();
        if (parents.isEmpty()) {
            // default parent's permission
            PermissionParent parentCategories = parentRepository.save(PermissionParent.builder().name("categories").build());
            PermissionParent parentDist = parentRepository.save(PermissionParent.builder().name("distributions").build());
            PermissionParent parentHourlies = parentRepository.save(PermissionParent.builder().name("horaires").build());
            PermissionParent parentStores = parentRepository.save(PermissionParent.builder().name("stores").build());
            PermissionParent parentNotifications = parentRepository.save(PermissionParent.builder().name("notifications").build());
            PermissionParent parentPermissions = parentRepository.save(PermissionParent.builder().name("permissions").build());
            PermissionParent parentPp = parentRepository.save(PermissionParent.builder().name("parent permissions").build());
            PermissionParent parentRecuperations = parentRepository.save(PermissionParent.builder().name("recuperations").build());
            PermissionParent parentRules = parentRepository.save(PermissionParent.builder().name("rules").build());
            PermissionParent parentTeams = parentRepository.save(PermissionParent.builder().name("teams").build());
            PermissionParent parentUsers = parentRepository.save(PermissionParent.builder().name("users").build());

            // default userRule permission's
            Set<Permission> userPermissions = new HashSet<>(
                    Arrays.asList(
                            new Permission("read distributions", parentDist),
                            new Permission("read distribution", parentDist),
                            new Permission("read horaires", parentHourlies),
                            new Permission("read horaire", parentHourlies),
                            new Permission("select horaire", parentHourlies),
                            new Permission("unselect horaire", parentHourlies),
                            new Permission("read stores", parentStores),
                            new Permission("read store", parentStores),
                            new Permission("read notifications", parentNotifications),
                            new Permission("read notification", parentNotifications),
                            new Permission("create notification", parentNotifications),
                            new Permission("delete notification", parentNotifications),
                            new Permission("read recuperations", parentRecuperations),
                            new Permission("read recuperation", parentRecuperations),
                            new Permission("read user", parentUsers),
                            new Permission("update user", parentUsers)
                    )
            );
            userPermissions = new HashSet<>(permissionRepository.saveAll(userPermissions));
            // create userRule rule with his permission's
            Role userRule = roleRepository.save(
                    Role.builder().name("user").permissions(userPermissions).build());

            // default adminRule permission's
            Set<Permission> adminPermissions = new HashSet<>(
                    Arrays.asList(
                            new Permission("read categories", parentCategories),
                            new Permission("read category", parentCategories),
                            new Permission("create category", parentCategories),
                            new Permission("update category", parentCategories),
                            new Permission("create distribution", parentDist),
                            new Permission("update distribution", parentDist),
                            new Permission("create horaire", parentHourlies),
                            new Permission("update horaire", parentHourlies),
                            new Permission("create store", parentStores),
                            new Permission("update store", parentStores),
                            new Permission("add user permission", parentPermissions),
                            new Permission("remove user permission", parentPermissions),
                            new Permission("create recuperation", parentRecuperations),
                            new Permission("update recuperation", parentRecuperations),
                            new Permission("read rules", parentRules),
                            new Permission("add user rule", parentRules),
                            new Permission("remove user rule", parentRules),
                            new Permission("read teams", parentTeams),
                            new Permission("read team", parentTeams),
                            new Permission("create team", parentTeams),
                            new Permission("update team", parentTeams),
                            new Permission("add team member", parentTeams),
                            new Permission("remove team member", parentTeams),
                            new Permission("read users", parentUsers),
                            new Permission("read user", parentUsers),
                            new Permission("create user", parentUsers),
                            new Permission("update user", parentUsers)
                    )
            );
            adminPermissions.addAll(userPermissions);// adminRule userRule can perform userRule's actions
            adminPermissions = new HashSet<>(permissionRepository.saveAll(adminPermissions));
            // create adminRule rule with his permission's
            Role adminRule = roleRepository.save(
                    Role.builder().name("admin").permissions(adminPermissions).build());

            // default superAdminRule permission's
            Set<Permission> superAdminPermissions = new HashSet<>(
                    Arrays.asList(
                            new Permission("delete category", parentCategories),
                            new Permission("delete distribution", parentDist),
                            new Permission("delete horaire", parentHourlies),
                            new Permission("delete store", parentStores),
                            new Permission("read permissions", parentPermissions),
                            new Permission("read permission", parentPermissions),
                            new Permission("create permission", parentPermissions),
                            new Permission("update permission", parentPermissions),
                            new Permission("delete permission", parentPermissions),
                            new Permission("read parent permissions", parentPp),
                            new Permission("read parent permission", parentPp),
                            new Permission("create parent permission", parentPp),
                            new Permission("update parent permission", parentPp),
                            new Permission("delete parent permission", parentPp),
                            new Permission("add permission to user", parentPermissions),
                            new Permission("remove permission to user", parentPermissions),
                            new Permission("delete recuperation", parentRecuperations),
                            new Permission("read rule", parentRules),
                            new Permission("create rule", parentRules),
                            new Permission("update rule", parentRules),
                            new Permission("delete rule", parentRules),
                            new Permission("delete team", parentTeams),
                            new Permission("delete user", parentUsers)
                    )
            );
            // super adminRule can perform admins actions
            superAdminPermissions.addAll(adminPermissions);
            superAdminPermissions = new HashSet<>(permissionRepository.saveAll(superAdminPermissions));
            // create super adminRule rule with his permission's
            Role superAdminRule = roleRepository.save(
                    Role.builder().name("super admin").permissions(superAdminPermissions).build());

            // create default super admin user
            User superAdmin = User.builder()
                    .email("superadmin@benevole.org").password(passwordEncoder.encode("12345678"))
                    .username("Super Admin").roles(new HashSet<>(Collections.singleton(superAdminRule)))
                    .enabled(true)
                    .build();
            // create default admin user
            User admin = User.builder()
                    .email("admin@benevole.org").password(passwordEncoder.encode("12345678"))
                    .username("Admin").roles(new HashSet<>(Collections.singleton(adminRule)))
                    .enabled(true)
                    .build();
            // create default simple user
            User user = User.builder()
                    .email("user@benevole.org").password(passwordEncoder.encode("12345678"))
                    .username("User").roles(new HashSet<>(Collections.singleton(userRule)))
                    .enabled(true)
                    .build();
            userRepository.save(superAdmin);
            userRepository.save(admin);
            userRepository.save(user);
        }
    }
}
