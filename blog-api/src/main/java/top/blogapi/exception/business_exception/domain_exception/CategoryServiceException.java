package top.blogapi.exception.business_exception.domain_exception;

import org.springframework.http.HttpStatus;
import top.blogapi.exception.business_exception.DomainException;

public class CategoryServiceException extends DomainException {
    public CategoryServiceException(CategoryServiceExceptionBuilder builder) {
        super(builder);
    }
    public static CategoryServiceExceptionBuilder builder() {
        return new CategoryServiceExceptionBuilder();
    }
    public static class CategoryServiceExceptionBuilder extends DomainExceptionBuilder<CategoryServiceExceptionBuilder> {
        public CategoryServiceExceptionBuilder() {
            super.domain("CATEGORY");
        }

        public CategoryServiceExceptionBuilder categoryNotExist(String domain, String msg, String ...operation) {
            return this.domain(domain)
                    .errorCode("CATEGORY_NOT_EXIST")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(msg)
                    .messageKey(domain.toLowerCase()+".invalid_parameters",new Object[]{operation});

        }

        public CategoryServiceExceptionBuilder blogDoesntAddCategories(String domain, HttpStatus httpStatus, String msg,String ...operation) {
            return this.domain(domain)
                    .errorCode("BLOG_DOES_NOT_ADD_CATEGORIES")
                    .httpStatus(httpStatus)
                    .message(msg)
                    .messageKey("blog.blog_doesnt_add_categories",new Object[]{operation});
        }

        @Override
        protected CategoryServiceExceptionBuilder self() {
            return this;
        }

        @Override
        public CategoryServiceException build() {
            return new CategoryServiceException(this);
        }
    }
}
