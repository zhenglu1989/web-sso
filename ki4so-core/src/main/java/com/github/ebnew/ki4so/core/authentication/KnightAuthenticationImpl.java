package com.github.ebnew.ki4so.core.authentication;

import java.util.Date;
import java.util.Map;

/**
 * @author zhenglu
 * @since 15/4/23
 */
public class KnightAuthenticationImpl implements KnightAuthentication {

    private Date authenticatedDate;

    private Map<String,Object> attributes;

    private KnightUser user;

    @Override
    public Map<String, Object> getAttrbutes() {
        return attributes;
    }

    @Override
    public Date getAuthenticateDate() {
        return authenticatedDate;
    }

    @Override
    public KnightUser getUser() {
        return user;
    }

    public void setAuthenticatedDate(Date authenticatedDate) {
        this.authenticatedDate = authenticatedDate;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public void setUser(KnightUser user) {
        this.user = user;
    }
}
