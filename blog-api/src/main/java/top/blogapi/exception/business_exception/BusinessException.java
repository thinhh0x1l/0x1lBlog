package top.blogapi.exception.business_exception;

import top.blogapi.exception.BaseException;

public class BusinessException extends BaseException {

    public BusinessException(BusinessExceptionBuilder builder) {
        super(builder);
    }
    public static BusinessExceptionBuilder builder() {
        return new BusinessExceptionBuilder();
    }

    public static class BusinessExceptionBuilder extends Builder<BusinessExceptionBuilder>{

        @Override
        protected BusinessExceptionBuilder self() {
            return this;
        }

        @Override
        public BaseException build() {
            return new BusinessException(this);
        }
    }
}
