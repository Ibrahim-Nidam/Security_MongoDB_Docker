package com.logistics.sdg.dto.transporter;

import com.logistics.sdg.model.enums.Specialty;

public record CreateTransporterRequest(String login, String password, Specialty specialty) {
}
