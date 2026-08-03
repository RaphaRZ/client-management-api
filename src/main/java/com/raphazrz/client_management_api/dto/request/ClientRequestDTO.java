package com.raphazrz.client_management_api.dto.request;

import com.raphazrz.client_management_api.constant.RegexConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;


public record ClientRequestDTO(
        @Schema(example = "Albert")
        @NotBlank(message = "First name is required.")
        String firstName,

        @Schema(example = "Wesker")
        @NotBlank(message = "Last name is required.")
        String lastName,

        @Schema(example = "000.000.000-01")
        @NotBlank(message = "Document is required.")
        @Pattern(
                regexp = RegexConstants.DOCUMENT_REGEX + "|" + RegexConstants.FORMATTED_DOCUMENT_REGEX,
                message = "Document must contain 11 digits or follow the format XXX.XXX.XXX-XX."
        )
        String document) {
}
