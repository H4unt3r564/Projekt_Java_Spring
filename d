[1mdiff --git a/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/configs/SecurityConfig.java b/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/configs/SecurityConfig.java[m
[1mindex ca8d5da..f58f84a 100644[m
[1m--- a/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/configs/SecurityConfig.java[m
[1m+++ b/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/configs/SecurityConfig.java[m
[36m@@ -1,47 +1,47 @@[m
[31m-package com.example.projekt_plemiona.configs;[m
[31m-[m
[31m-import jakarta.servlet.http.HttpServletResponse;[m
[31m-import org.springframework.context.annotation.Bean;[m
[31m-import org.springframework.context.annotation.Configuration;[m
[31m-import org.springframework.security.config.annotation.web.builders.HttpSecurity;[m
[31m-import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;[m
[31m-import org.springframework.security.crypto.password.PasswordEncoder;[m
[31m-import org.springframework.security.web.SecurityFilterChain;[m
[31m-[m
[31m-@Configuration[m
[31m-public class SecurityConfig {[m
[31m-[m
[31m-    @Bean[m
[31m-    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {[m
[31m-        http[m
[31m-                .authorizeHttpRequests(auth -> auth[m
[31m-                        .requestMatchers("/login", "/rejestracja").permitAll()[m
[31m-                        .anyRequest().authenticated()[m
[31m-                )[m
[31m-                .httpBasic(basic -> basic.authenticationEntryPoint((request, response, authException) -> {[m
[31m-                    if (request.getRequestURI().startsWith("/api/")) {[m
[31m-                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);[m
[31m-                        response.getWriter().write("Brak autoryzacji - musisz sie zalogowac przez Basic Auth");[m
[31m-                    } else {[m
[31m-                        response.sendRedirect("/login");[m
[31m-                    }[m
[31m-                }))[m
[31m-                .formLogin(form -> form[m
[31m-                        .loginPage("/login")[m
[31m-                        .defaultSuccessUrl("/wioska", true)[m
[31m-                        .permitAll()[m
[31m-                )[m
[31m-                .logout( logout -> logout[m
[31m-                        .logoutUrl("/logout")[m
[31m-                        .logoutSuccessUrl("/login")[m
[31m-                )[m
[31m-                .csrf(csrf -> csrf.disable());[m
[31m-[m
[31m-        return http.build();[m
[31m-    }[m
[31m-[m
[31m-    @Bean[m
[31m-    public PasswordEncoder passwordEncoder(){[m
[31m-        return new BCryptPasswordEncoder();[m
[31m-    }[m
[31m-}[m
[32m+[m[32mpackage com.example.projekt_plemiona.configs;[m[41m[m
[32m+[m[41m[m
[32m+[m[32mimport jakarta.servlet.http.HttpServletResponse;[m[41m[m
[32m+[m[32mimport org.springframework.context.annotation.Bean;[m[41m[m
[32m+[m[32mimport org.springframework.context.annotation.Configuration;[m[41m[m
[32m+[m[32mimport org.springframework.security.config.annotation.web.builders.HttpSecurity;[m[41m[m
[32m+[m[32mimport org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;[m[41m[m
[32m+[m[32mimport org.springframework.security.crypto.password.PasswordEncoder;[m[41m[m
[32m+[m[32mimport org.springframework.security.web.SecurityFilterChain;[m[41m[m
[32m+[m[41m[m
[32m+[m[32m@Configuration[m[41m[m
[32m+[m[32mpublic class SecurityConfig {[m[41m[m
[32m+[m[41m[m
[32m+[m[32m    @Bean[m[41m[m
[32m+[m[32m    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {[m[41m[m
[32m+[m[32m        http[m[41m[m
[32m+[m[32m                .authorizeHttpRequests(auth -> auth[m[41m[m
[32m+[m[32m                        .requestMatchers("/login", "/rejestracja").permitAll()[m[41m[m
[32m+[m[32m                        .anyRequest().authenticated()[m[41m[m
[32m+[m[32m                )[m[41m[m
[32m+[m[32m                .httpBasic(basic -> basic.authenticationEntryPoint((request, response, authException) -> {[m[41m[m
[32m+[m[32m                    if (request.getRequestURI().startsWith("/api/")) {[m[41m[m
[32m+[m[32m                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);[m[41m[m
[32m+[m[32m                        response.getWriter().write("Brak autoryzacji - musisz sie zalogowac przez Basic Auth");[m[41m[m
[32m+[m[32m                    } else {[m[41m[m
[32m+[m[32m                        response.sendRedirect("/login");[m[41m[m
[32m+[m[32m                    }[m[41m[m
[32m+[m[32m                }))[m[41m[m
[32m+[m[32m                .formLogin(form -> form[m[41m[m
[32m+[m[32m                        .loginPage("/login")[m[41m[m
[32m+[m[32m                        .defaultSuccessUrl("/wioska", true)[m[41m[m
[32m+[m[32m                        .permitAll()[m[41m[m
[32m+[m[32m                )[m[41m[m
[32m+[m[32m                .logout( logout -> logout[m[41m[m
[32m+[m[32m                        .logoutUrl("/logout")[m[41m[m
[32m+[m[32m                        .logoutSuccessUrl("/login")[m[41m[m
[32m+[m[32m                )[m[41m[m
[32m+[m[32m                .csrf(csrf -> csrf.disable());[m[41m[m
[32m+[m[41m[m
[32m+[m[32m        return http.build();[m[41m[m
[32m+[m[32m    }[m[41m[m
[32m+[m[41m[m
[32m+[m[32m    @Bean[m[41m[m
[32m+[m[32m    public PasswordEncoder passwordEncoder(){[m[41m[m
[32m+[m[32m        return new BCryptPasswordEncoder();[m[41m[m
[32m+[m[32m    }[m[41m[m
[32m+[m[32m}[m[41m[m
[1mdiff --git a/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/AuthController.java b/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/AuthController.java[m
[1mindex e97b562..0afad79 100644[m
[1m--- a/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/AuthController.java[m
[1m+++ b/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/AuthController.java[m
[36m@@ -1,27 +1,27 @@[m
[31m-package com.example.projekt_plemiona.controllers;[m
[31m-[m
[31m-import com.example.projekt_plemiona.services.UserService;[m
[31m-import org.springframework.stereotype.Controller;[m
[31m-import org.springframework.web.bind.annotation.GetMapping;[m
[31m-import org.springframework.web.bind.annotation.PostMapping;[m
[31m-import org.springframework.web.bind.annotation.RequestParam;[m
[31m-[m
[31m-@Controller[m
[31m-public class AuthController {[m
[31m-[m
[31m-    private final UserService UserService;[m
[31m-[m
[31m-    public AuthController(UserService UserService) {[m
[31m-        this.UserService = UserService;[m
[31m-    }[m
[31m-[m
[31m-    @PostMapping("/rejestracja")[m
[31m-    public String register([m
[31m-            @RequestParam String username,[m
[31m-            @RequestParam String email,[m
[31m-            @RequestParam String password[m
[31m-    ) {[m
[31m-        UserService.register(username, email, password);[m
[31m-        return "redirect:/login";[m
[31m-    }[m
[32m+[m[32mpackage com.example.projekt_plemiona.controllers;[m[41m[m
[32m+[m[41m[m
[32m+[m[32mimport com.example.projekt_plemiona.services.UserService;[m[41m[m
[32m+[m[32mimport org.springframework.stereotype.Controller;[m[41m[m
[32m+[m[32mimport org.springframework.web.bind.annotation.GetMapping;[m[41m[m
[32m+[m[32mimport org.springframework.web.bind.annotation.PostMapping;[m[41m[m
[32m+[m[32mimport org.springframework.web.bind.annotation.RequestParam;[m[41m[m
[32m+[m[41m[m
[32m+[m[32m@Controller[m[41m[m
[32m+[m[32mpublic class AuthController {[m[41m[m
[32m+[m[41m[m
[32m+[m[32m    private final UserService UserService;[m[41m[m
[32m+[m[41m[m
[32m+[m[32m    public AuthController(UserService UserService) {[m[41m[m
[32m+[m[32m        this.UserService = UserService;[m[41m[m
[32m+[m[32m    }[m[41m[m
[32m+[m[41m[m
[32m+[m[32m    @PostMapping("/rejestracja")[m[41m[m
[32m+[m[32m    public String register([m[41m[m
[32m+[m[32m            @RequestParam String username,[m[41m[m
[32m+[m[32m            @RequestParam String email,[m[41m[m
[32m+[m[32m            @RequestParam String password[m[41m[m
[32m+[m[32m    ) {[m[41m[m
[32m+[m[32m        UserService.register(username, email, password);[m[41m[m
[32m+[m[32m        return "redirect:/login";[m[41m[m
[32m+[m[32m    }[m[41m[m
 }[m
\ No newline at end of file[m
[1mdiff --git a/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/MainController.java b/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/MainController.java[m
[1mindex bf724d2..9cc90d5 100644[m
[1m--- a/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/MainController.java[m
[1m+++ b/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/MainController.java[m
[36m@@ -1,14 +1,14 @@[m
[31m-package com.example.projekt_plemiona.controllers;[m
[31m-[m
[31m-import org.springframework.stereotype.Controller;[m
[31m-import org.springframework.web.bind.annotation.GetMapping;[m
[31m-[m
[31m-@Controller[m
[31m-public class MainController {[m
[31m-[m
[31m-    @GetMapping("/login")[m
[31m-    public String loginPage() {return "logowanie"; }[m
[31m-[m
[31m-    @GetMapping("/rejestracja")[m
[31m-    public String registerPage() {return "rejestracja"; }[m
[31m-}[m
[32m+[m[32mpackage com.example.projekt_plemiona.controllers;[m[41m[m
[32m+[m[41m[m
[32m+[m[32mimport org.springframework.stereotype.Controller;[m[41m[m
[32m+[m[32mimport org.springframework.web.bind.annotation.GetMapping;[m[41m[m
[32m+[m[41m[m
[32m+[m[32m@Controller[m[41m[m
[32m+[m[32mpublic class MainController {[m[41m[m
[32m+[m[41m[m
[32m+[m[32m    @GetMapping("/login")[m[41m[m
[32m+[m[32m    public String loginPage() {return "logowanie"; }[m[41m[m
[32m+[m[41m[m
[32m+[m[32m    @GetMapping("/rejestracja")[m[41m[m
[32m+[m[32m    public String registerPage() {return "rejestracja"; }[m[41m[m
[32m+[m[32m}[m[41m[m
[1mdiff --git a/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/VillageController.java b/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/VillageController.java[m
[1mindex d28fc5c..b25a8e5 100644[m
[1m--- a/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/VillageController.java[m
[1m+++ b/Strona/projekt_plemiona/src/main/java/com/example/projekt_plemiona/controllers/VillageController.java[m
[36m@@ -1,53 +1,53 @@[m
 package com.example.projekt_plemiona.controllers;[m
 [m
[31m-import com.example.projekt_plemiona.configs.SecurityConfig;[m
[32m+[m[32mimport com.example.projekt_plemiona.exceptions.NotEnoughResourcesException;[m
 import com.example.projekt_plemiona.exceptions.UserNotFoundException;[m
[31m-import com.example.projekt_plemiona.models.BuildingType;[m
 import com.example.projekt_plemiona.models.Player;[m
 import com.example.projekt_plemiona.models.Village;[m
 import com.example.projekt_plemiona.models.VillageBuilding;[m
 import com.example.projekt_plemiona.repositories.PlayerRepository;[m
[31m-import com.example.projekt_plemiona.repositories.VillageBuildingRepository;[m
 import com.example.projekt_plemiona.repositories.VillageRepository;[m
[32m+[m[32mimport com.example.projekt_plemiona.services.ResourceService;[m
 import com.example.projekt_plemiona.services.VillageService;[m
[31m-import jakarta.transaction.Transactional;[m
 import org.springframework.security.core.Authentication;[m
[31m-import org.springframework.security.core.context.SecurityContext;[m
 import org.springframework.security.core.context.SecurityContextHolder;[m
 import org.springframework.stereotype.Controller;[m
 import org.springframework.ui.Model;[m
 import org.springframework.web.bind.annotation.GetMapping;[m
 import org.springframework.web.bind.annotation.PostMapping;[m
 import org.springframework.web.bind.annotation.RequestParam;[m
[32m+[m[32mimport org.springframework.web.servlet.mvc.support.RedirectAttributes;[m
 [m
 import java.util.List;[m
[31m-import java.util.Optional;[m
 [m
 @Controller[m
 public class VillageController {[m
 [m
     private final VillageRepository villageRepository;[m
     private final PlayerRepository playerRepository;[m
[31m-    private final VillageBuildingRepository villageBuildingRepository;[m
[31m-[m
     private final VillageService villageService;[m
[32m+[m[32m    private final ResourceService resourceService;[m
[32m+[m
[32m+[m[32m    public VillageController(VillageRepository villageRepository,[m
[32m+[m[32m                             PlayerRepository playerRepository,[m
[32m+[m[32m                             VillageService villageService,[m
[32m+[m[32m                             ResourceService resourceService) {[m
 [m
[31m-    public VillageController(VillageRepository villageRepository, PlayerRepository playerRepository, VillageService villageService, VillageBuildingRepository villageBuildingRepository) {[m
         this.villageRepository = villageRepository;[m
         this.playerRepository = playerRepository;[m
[31m-        this.villageBuildingRepository = villageBuildingRepository;[m
         this.villageService = villageService;[m
[32m+[m[32m        this.resourceService = resourceService;[m
     }[m
 [m
     @GetMapping("/wioska")[m
[31m-    public String showVillage(@RequestParam(required = false) Long id,[m
[31m-                              Model model) {[m
[32m+[m[32m    public String showVillage([m
[32m+[m[32m            @RequestParam(required = false) Long id,[m
[32m+[m[32m            Model model) {[m
 [m
         Authentication auth =[m
                 SecurityContextHolder.getContext().getAuthentication();[m
 [m
[31m-        if (auth == null ||[m
[31m-                !auth.isAuthenticated() ||[m
[32m+[m[32m        if (auth == null || !auth.isAuthenticated() ||[m
                 auth.getName().equals("anonymousUser")) {[m
 [m
             return "redirect:/login";[m
[36m@@ -55,37 +55,41 @@[m [mpublic class VillageController {[m
 [m
         String username = auth.getName();[m
 [m
[31m-        Player loggedPlayer = playerRepository.findByUsername(username)[m
[32m+[m[32m        Player player = playerRepository.findByUsername(username)[m
                 .orElseThrow(() ->[m
                         new UserNotFoundException("Nie ma takiego gracza"));[m
 [m
         Village village;[m
 [m
[31m-        // kliknięcie z mapy[m
[31m-        if(id != null) {[m
[32m+[m[32m        if (id != null) {[m
 [m
             village = villageRepository.findById(id)[m
[31m-                    .orElseThrow(() ->[m
[31m-                            new RuntimeException("Village not found"));[m
[31m-        }[m
[32m+[m[32m                    .orElseThrow();[m
 [m
[31m-        // własna wioska[m
[31m-        else {[m
[32m+[m[32m        } else {[m
 [m
             village = villageRepository[m
[31m-                    .findByPlayer_PlayerId(loggedPlayer.getPlayerId())[m
[32m+[m[32m                    .findAllByPlayer_PlayerId(player.getPlayerId())[m
[32m+[m[32m                    .stream()[m
[32m+[m[32m                    .findFirst()[m
                     .orElseThrow();[m
         }[m
 [m
[32m+[m[32m        village = resourceService.updateResources([m
[32m+[m[32m                village.getVillageId()[m
[32m+[m[32m        );[m
[32m+[m
         Player owner = village.getPlayer();[m
 [m
         boolean isOwner =[m
                 owner.getUsername().equals(username);[m
 [m
         List<VillageB