package in.saicharan.expensetracker.services;

import in.saicharan.expensetracker.entity.ProfileEntity;
import in.saicharan.expensetracker.repository.ProfileRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileActivationService {
    private final ProfileRepo profileRepo;
    private final EmailService emailService;

    public void sendActivate(ProfileEntity newProfile) {
        newProfile.setActivationCode(UUID.randomUUID().toString()); //for email verf
        String activationLink = "http://localhost:8080/api/v1.0/activate?token=" + newProfile.getActivationCode();
        String body = "Click on the following link to activate to account:" + activationLink;
        String subject = "Activate your Finance Manager Account";
        String to = newProfile.getEmail();
        emailService.sendEmail(to, subject, body);
    }

    public boolean verify(String token) {
        Optional<ProfileEntity> optionalProfile = profileRepo.findByActivationCode(token);

        if (optionalProfile.isPresent()) {
            ProfileEntity profile = optionalProfile.get();
            profile.setIsActive(true);            // mark as activated
            profileRepo.save(profile);
            return true;
        } else {
            return false;
        }
    }


}
