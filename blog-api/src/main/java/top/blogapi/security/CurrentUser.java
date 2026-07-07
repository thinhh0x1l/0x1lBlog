package top.blogapi.security;

import java.lang.annotation.*;

/**
 * Annotation dùng để inject {@link UserPrincipal} hiện tại vào tham số phương thức controller.
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {
}
