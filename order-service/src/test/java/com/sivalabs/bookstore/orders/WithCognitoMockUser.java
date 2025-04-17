package com.sivalabs.bookstore.orders;

import java.lang.annotation.*;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithSecurityContext;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@WithSecurityContext(factory = MockOAuth2UserContextFactory.class)
public @interface WithCognitoMockUser {
    String username() default "";

    String[] roles() default {"USER"};

    String email() default "test@test.com";

    TestExecutionEvent setupBefore() default TestExecutionEvent.TEST_METHOD;
}
