package com.oneday.reposiratory;

import com.oneday.entity.AppUser;
import com.oneday.entity.type.OAuthProviderType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    public Optional<AppUser> findByUsername(String userName);
    public Optional<AppUser> findByEmail(String email);
    public Optional<AppUser> findByEmailOrUsername(String email, String userName);
    Optional<AppUser> findByProviderNameAndProviderId(
            OAuthProviderType providerType,
            String providerId
    );

}