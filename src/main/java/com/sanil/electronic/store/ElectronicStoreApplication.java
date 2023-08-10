package com.sanil.electronic.store;

import com.sanil.electronic.store.entities.Role;
import com.sanil.electronic.store.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@SpringBootApplication
@EnableWebMvc
public class ElectronicStoreApplication implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Value("${normal.role.id}")
    private  String roleNormalId;

    @Value("${admin.role.id}")
    private  String roleAdminId;

    public static void main(String[] args) {
        SpringApplication.run(ElectronicStoreApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
     //   System.out.println(passwordEncoder.encode("abcd"));

      /*  try {
           Role roleAdmin = Role.builder().roleId( roleAdminId).roleName("ROLE_ADMIN").build();
           Role roleNormal = Role.builder().roleId(roleNormalId).roleName("ROLE_NORMAL").build();
           roleRepository.save(roleNormal);
           roleRepository.save(roleAdmin);

        }catch (Exception e) {
            e.printStackTrace();
        }
         */
    }
}