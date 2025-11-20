package com.logistics.sdg.dto.transporter;

import com.logistics.sdg.model.enums.Specialty;
import com.logistics.sdg.model.enums.TransporterStatus;

public record TransporterResponse(String id,
                                  String login,
                                  Specialty specialite,
                                  TransporterStatus statut,
                                  boolean active
) {}
