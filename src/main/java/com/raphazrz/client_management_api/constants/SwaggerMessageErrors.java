package com.raphazrz.client_management_api.constants;

public final class SwaggerMessageErrors {
    public static final String INVALID_PARAMETER_ID_DESCRIPTION = "Invalid value for parameter 'id'.";
    public static final String INVALID_PARAMETER_ID_VALUE = """
    {
      "message": "Invalid value for parameter 'id'.",
      "validationErrors": {},
      "statusCode": 400
    }
    """;



    private SwaggerMessageErrors() {
        throw new UnsupportedOperationException("Utility class");
    }
}
