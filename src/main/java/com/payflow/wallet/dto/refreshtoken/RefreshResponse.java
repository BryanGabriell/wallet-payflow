package com.payflow.wallet.dto.refreshtoken;



public record RefreshResponse(   String accessToken,
                                 String refreshToken,
                                 String tokenType,
                                 String expiresAt) {
}
