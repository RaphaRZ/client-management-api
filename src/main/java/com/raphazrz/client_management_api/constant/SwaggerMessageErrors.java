package com.raphazrz.client_management_api.constant;

public final class SwaggerMessageErrors {
    public static final String VALIDATION_FAILED_DESCRIPTION = "Validation failed.";
    public static final String VALIDATION_FAILED_VALUE = """
            {
              "message": "Validation failed.",
              "validationErrors": {
                "document": "Document is required."
              },
              "statusCode": 400
            }
            """;

    public static final String INVALID_CONTACT_TYPE_DESCRIPTION = "Invalid contact type.";
    public static final String INVALID_CONTACT_TYPE_VALUE = """
        {
          "message": "Invalid contact type. Valid values are: 1 (PHONE) and 2 (EMAIL).",
          "validationErrors": {},
          "statusCode": 400
        }
        """;

    public static final String INVALID_PARAMETER_ID_DESCRIPTION = "Invalid value for parameter 'id'.";
    public static final String INVALID_PARAMETER_ID_VALUE = """
            {
              "message": "Invalid value for parameter 'id'.",
              "validationErrors": {},
              "statusCode": 400
            }
            """;

    public static final String CLIENT_NOT_FOUND_DESCRIPTION = "Client not found.";
    public static final String CLIENT_NOT_FOUND_VALUE = """
            {
              "message": "Client not found.",
              "validationErrors": {},
              "statusCode": 404
            }
            """;

    public static final String CONTACT_NOT_FOUND_DESCRIPTION = "Contact not found.";
    public static final String CONTACT_NOT_FOUND_VALUE = """
            {
                "message": "Client not found.",
                    "validationErrors": {},
                "statusCode": 404
            }
            """;

    public static final String DOCUMENT_ALREADY_REGISTERED_DESCRIPTION = "Document already registered.";
    public static final String DOCUMENT_ALREADY_REGISTERED_VALUE = """
            {
              "message": "Document already registered.",
              "validationErrors": {},
              "statusCode": 409
            }
            """;

    private SwaggerMessageErrors() {
        throw new UnsupportedOperationException("Utility class");
    }
}
