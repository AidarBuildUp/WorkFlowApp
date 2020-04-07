package service.validator;

import exception.validator.NullFieldException;
import exception.validator.ValidationException;

public class NoEmptyStringFieldValidator implements Validator {
    @Override
    public void validate(Object field) throws ValidationException {
        if (field == null) throw new NullFieldException("Field " + field + "can not be empty");
    }
}
