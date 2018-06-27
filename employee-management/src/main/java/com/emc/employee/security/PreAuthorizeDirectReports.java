package com.emc.employee.security;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.access.prepost.PreAuthorize;


@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@emcAuthorization.ifDirectReports(authentication,#id)")
public @interface PreAuthorizeDirectReports {

}
