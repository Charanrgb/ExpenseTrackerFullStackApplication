package in.saicharan.expensetracker.services;

import in.saicharan.expensetracker.dto.AuthDTO;
import in.saicharan.expensetracker.dto.ProfileDto;
import in.saicharan.expensetracker.entity.ProfileEntity;
import in.saicharan.expensetracker.repository.ProfileRepo;
import in.saicharan.expensetracker.util.JwtUtil;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepo ProfileRepository; //all operations with db must be done by repo object thats why
    private final ProfileActivationService profileActivationService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    //profileDto object comes as a json argument from Profile controller, fed into this.
    public ProfileDto registerProfile(ProfileDto profileDto) {
        ProfileEntity newProfile = toEntity(profileDto); //converts to entity to save into db
        profileActivationService.sendActivate(newProfile); //sends activation link to email (i created this service)
        newProfile.setPassword(passwordEncoder.encode(newProfile.getPassword()));
        newProfile = ProfileRepository.save(newProfile); //saves into db
        return toDto(newProfile); //converts entity to dto and returns to controller
    }

    //converts dto to entity (dto object is used, thats why all the lines in the dto file
    public ProfileEntity toEntity(ProfileDto profileDto) {
        return ProfileEntity.builder()
                .id(profileDto.getId())
                .fullName(profileDto.getFullName())
                .email(profileDto.getEmail())
                .password(profileDto.getPassword())
                .imageURL(profileDto.getImageURL())
                .createdAt(profileDto.getCreatedAt())
                .updatedAt(profileDto.getUpdatedAt())
                .build();
    }

    //converts entity to dto
    public ProfileDto toDto(ProfileEntity profileEntity) {
        return ProfileDto.builder()
                .id(profileEntity.getId())
                .fullName(profileEntity.getFullName())
                .email(profileEntity.getEmail())
                .imageURL(profileEntity.getImageURL())
                .createdAt(profileEntity.getCreatedAt())
                .updatedAt(profileEntity.getUpdatedAt()).build();
    }

    //checks if the registered user has activated profile or not
    public boolean isAccountActive(String email) {
        return ProfileRepository.findByEmail(email).map(ProfileEntity::getIsActive).orElse(false);
    }

    //If a user is authenticated (logged in), Spring Security stores their identity in SecurityContextHolder
    //.getAuthentication().getName() → usually returns the username/email you used to authenticate.
    public ProfileEntity getCurrentProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return ProfileRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Profile not found with email address" + email));

    }

    //Suppose you're building an expense tracker app.
    //
    //A logged-in user visits /profile with no query parameter → the API calls getPublicProfile(null) and returns their own profile.
    //
    //Another user visits /profile?email=someone@gmail.com → the API calls getPublicProfile("someone@gmail.com") and returns that user’s profile (public info only).

    public ProfileDto getPublicProfile(String email) {
        ProfileEntity currentUser = null;
        if (email == null) {
            currentUser = getCurrentProfile();
        } else {
            currentUser = ProfileRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Profile not found with email" + email));
        }
        return ProfileDto.builder().id(currentUser.getId()).fullName(currentUser.getFullName()).email(currentUser.getEmail()).imageURL(currentUser.getImageURL()).createdAt(currentUser.getCreatedAt()).updatedAt(currentUser.getUpdatedAt()).build();

    }

    public Map<String, Object> authenticateAndGenerateToken(AuthDTO authDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword()));
            //Generate JWT token
            String token = jwtUtil.generateToken(authDTO.getEmail());
            return Map.of("token", token, "user", getPublicProfile(authDTO.getEmail()));
        } catch (Exception e) {
            return Map.of("message", e.getMessage());
        }

    }


}

