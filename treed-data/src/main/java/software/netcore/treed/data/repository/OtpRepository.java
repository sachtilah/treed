package software.netcore.treed.data.repository;

import software.netcore.treed.data.BaseRepository;
import software.netcore.treed.data.schema.Account;
import software.netcore.treed.data.schema.Otp;

import java.util.Optional;

/**
 * @since v. 1.0.0
 */
public interface OtpRepository extends BaseRepository<Otp> {

    Optional<Otp> findByOtpPass(String otpPass);

}
