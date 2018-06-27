package com.emc.employee.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.access.prepost.PostAuthorize;


@Retention(RetentionPolicy.RUNTIME)
@PostAuthorize("@emcAuthorization.ifDirectReports(authentication,returnObject.id)")
public @interface PostAuthorizeDirectReports {

}
