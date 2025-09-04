package com.pathmonk.crmapi.web.problem;

import java.time.Instant;

public record ProblemDetails(
        String type,
        String title,
        int status,
        String detail,
        String instance,
        Instant timestamp
) {}