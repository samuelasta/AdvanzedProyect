package co.edu.uniquindio.application.services;

import co.edu.uniquindio.application.dto.usersDTOs.RequestResetPasswordDTO;
import co.edu.uniquindio.application.dto.usersDTOs.ResetPasswordDTO;
import co.edu.uniquindio.application.model.User;

public interface PasswordResetService {
    void requestPasswordReset(RequestResetPasswordDTO requestResetPasswordDTO) throws Exception;
    void resetPassword(ResetPasswordDTO resetPasswordDTO) throws Exception;
}
