package br.ifrn.edu.sisconf.security;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import lombok.Getter;

@Getter
public class SisconfAuthenticationToken extends AbstractAuthenticationToken {
    private final SisconfUserDetails principal;
    private Object credentials;

    public SisconfAuthenticationToken(
        SisconfUserDetails principal,
        Object credentials,
        Collection<? extends GrantedAuthority> authorities
    ) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }
}
