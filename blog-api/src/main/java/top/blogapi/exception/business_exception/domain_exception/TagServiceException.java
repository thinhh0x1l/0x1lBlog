package top.blogapi.exception.business_exception.domain_exception;

import org.springframework.http.HttpStatus;
import top.blogapi.exception.business_exception.DomainException;

import java.util.Locale;

public class TagServiceException extends DomainException {
    public TagServiceException(TagServiceExceptionBuilder builder) {
        super(builder);
    }

    public static TagServiceExceptionBuilder builder(){
        return new TagServiceExceptionBuilder();
    }

    public static class TagServiceExceptionBuilder extends DomainExceptionBuilder<TagServiceExceptionBuilder> {
        public TagServiceExceptionBuilder() {
            super.domain("TAG");
        }

        public TagServiceExceptionBuilder tagNotExist(String domain, HttpStatus httpStatus, String msg, String ...operations) {
            return this.domain(domain)
                    .errorCode("TAG_NOT_EXIST")
                    .httpStatus(httpStatus)
                    .message(msg)
                    .messageKey(domain.toLowerCase()+".tag_not_exist", new Object[]{operations});
        }

        public TagServiceExceptionBuilder nameTagIncorrect(String domain, HttpStatus httpStatus, String msg, String ...operations) {
            return this.domain(domain)
                    .errorCode("NAME_TAG_INCORRECT")
                    .httpStatus(httpStatus)
                    .message(msg)
                    .messageKey(domain.toLowerCase()+".name_tag_incorrect", new Object[]{operations});
        }

        public TagServiceExceptionBuilder addTagNotSuccess(String domain, HttpStatus httpStatus, String msg, String ...operations) {
            return this.domain(domain)
                    .errorCode("ADD_TAG_NOT_SUCCESS")
                    .httpStatus(httpStatus)
                    .message(msg)
                    .messageKey(domain.toLowerCase()+".add_tag_not_success", new Object[]{operations});
        }

        @Override
        protected TagServiceExceptionBuilder self() {
            return this;
        }

        @Override
        public TagServiceException build() {
            return new TagServiceException(this);
        }
    }
}
