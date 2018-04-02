package software.netcore.treed.business;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.netcore.treed.data.repository.OtpRepository;
import software.netcore.treed.data.schema.Otp;

/**
 * @since v. 1.0.0
 */
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final OtpRepository otpRepository;

    public Iterable<Otp> getOtps() {
        log.info("Getting all otp");
        return otpRepository.findAll();
    }

    public void saveOtp(Otp otp) {
        log.info("Saving new otp {}", otp);
        otpRepository.save(otp);
    }

    /*public Account login(String username, String password) {

        Optional<Account> optionalAccount = otpRepository.findByUsername(username);
        if (optionalAccount.isPresent()) {
            Account account = optionalAccount.get();
            if (account.getPassword().equals(password)) {
                return account;
            }
        }

        return null;
    }*/

}

