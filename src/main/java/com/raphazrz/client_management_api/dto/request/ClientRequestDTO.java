package com.raphazrz.client_management_api.dto.request;

import com.raphazrz.client_management_api.constant.RegexConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record ClientRequestDTO(
        @NotBlank(message = "First name is required.")
        String firstName,

        @NotBlank(message = "Last name is required.")
        String lastName,

        @NotBlank(message = "Document is required.")
        @Pattern(
                regexp = RegexConstants.DOCUMENT_REGEX + "|" + RegexConstants.FORMATTED_DOCUMENT_REGEX,
                message = "Document must contain 11 digits or follow the format XXX.XXX.XXX-XX."
        )
        String document) {
}
