package com.tuannq.authentication.config;

import com.tuannq.authentication.entity.Users;
import com.tuannq.authentication.security.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<Users> {

  @Override
  public Optional<Users> getCurrentAuditor() {
    return Optional.ofNullable(SecurityContextHolder.getContext())
        .map(SecurityContext::getAuthentication)
        .filter(authentication -> !(authentication instanceof AnonymousAuthenticationToken))
        .map(Authentication::getPrincipal)
        .map(u -> (CustomUserDetails) u)
        .map(CustomUserDetails::getUser);
  }
}
