package com.user.authentication.Repository;

import com.mgmt.userprofile.models.User;
import com.user.authentication.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Integer> {
}
