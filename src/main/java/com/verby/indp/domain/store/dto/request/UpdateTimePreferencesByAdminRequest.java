package com.verby.indp.domain.store.dto.request;

import java.util.List;

public record UpdateTimePreferencesByAdminRequest(
    List<TimePreference> timePreferences
) {
}
