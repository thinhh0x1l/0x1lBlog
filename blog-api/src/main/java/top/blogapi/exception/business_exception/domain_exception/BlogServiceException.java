package top.blogapi.exception.business_exception.domain_exception;

import org.springframework.http.HttpStatus;
import top.blogapi.exception.business_exception.DomainException;

public class BlogServiceException extends DomainException {

    public BlogServiceException(BlogServiceExceptionBuilder builder) {
        super(builder);
    }
    public static BlogServiceExceptionBuilder builder() {
        return new BlogServiceExceptionBuilder();
    }
    public static class BlogServiceExceptionBuilder extends DomainExceptionBuilder<BlogServiceExceptionBuilder> {
        public BlogServiceExceptionBuilder() {
            super.domain("BLOG");
        }
        public BlogServiceExceptionBuilder dataRetrievalFailed(String operation) {
            return this.errorCode("BLOG_DATA_RETRIEVAL_FAILED")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .messageKey("blog.data_retrieval_failed",new Object[]{operation});
        }
        public BlogServiceExceptionBuilder mappingFailed(String entityType) {
            return this.errorCode("BLOG_MAPPING_FAILED")
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .messageKey("blog.mapping_failed",new Object[]{entityType} );
        }

        public BlogServiceExceptionBuilder invalidQuery(String query){
            return this.errorCode("BLOG_INVALID_QUERY")
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .messageKey("blog.invalid_query", new Object[]{query} );
        }

        public BlogServiceExceptionBuilder blogNotFound(String blogId) {
            return this.entityId(blogId)
                    .errorCode("BLOG_NOT_FOUND")
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .messageKey("blog.not_found", new Object[]{blogId} );
        }

        public BlogServiceExceptionBuilder unauthorizedAccess(String blogId, Long userId) {
            return this.entityId(blogId)
                    .errorCode("BLOG_UNAUTHORIZED_ACCESS")
                    .httpStatus(HttpStatus.UNAUTHORIZED)
                    .messageKey("blog.unauthorized_access", new Object[]{blogId, userId} );
        }


        @Override
        protected BlogServiceExceptionBuilder self() {
            return this;
        }

        @Override
        public BlogServiceException


        build() {
            return new BlogServiceException(this);
        }
    }
}
