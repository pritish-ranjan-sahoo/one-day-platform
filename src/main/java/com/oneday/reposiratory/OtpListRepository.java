package com.oneday.reposiratory;

import com.oneday.entity.OtpList;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpListRepository extends JpaRepository<OtpList, Long> {

    public Optional<OtpList> findByEmail(String email);
    public void deleteByEmail(String email);
}