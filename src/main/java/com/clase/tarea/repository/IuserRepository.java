package com.clase.tarea.repository;

import com.clase.tarea.model.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IuserRepository extends JpaRepository<UserInfo, Long> {

    Optional<UserInfo> finByUsername(String username);
}
