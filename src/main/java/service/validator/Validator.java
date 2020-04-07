package service.validator;

import exception.validator.ValidationException;

public interface Validator {
    public void validate (Object object) throws ValidationException;
}
