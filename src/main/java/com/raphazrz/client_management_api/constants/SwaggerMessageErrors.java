package com.raphazrz.client_management_api.constants;

public final class SwaggerMessageErrors {
    public static final String CLIENT_NOT_FOUND = """
    {
      "message": "Client not found.",
      "validationErrors": {},
      "statusCode": 404
    }
    """;

    public static final String INVALID_ID = """
    {
      "message": "Invalid value for parameter 'id'.",
      "validationErrors": {},
      "statusCode": 400
    }
    """;

    private SwaggerMessageErrors() {}
}
