//In Summary: The Flow of a Request
//This controller method acts as a gateway for the registration process.
//
//A user sends a POST request with their profile data in JSON format.
//
//The ProfileController receives the request.
//
//Spring converts the JSON to a ProfileDto object.
//
//The controller calls profileService.registerProfile(), passing the DTO to the service layer.
//
//The service performs the registration logic (validating data, saving to the database, etc.).
//
//The service returns a ProfileDto that represents the newly registered user.
//
//The controller will then (in a full implementation) take this returned DTO and wrap it in a ResponseEntity to send a complete HTTP response back to the user.
package in.saicharan.expensetracker.controllers;

import in.saicharan.expensetracker.dto.AuthDTO;
import in.saicharan.expensetracker.dto.ProfileDto;
import in.saicharan.expensetracker.services.ProfileActivationService;
import in.saicharan.expensetracker.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ProfileController {
    private final ProfileService profileService;
    private final ProfileActivationService profileActivationService;

    //sending the dto object to service layer
    @PostMapping("/register")
    public ResponseEntity<ProfileDto> registerProfile(@RequestBody ProfileDto profileDto) {
        //the dto is returned from service layer and stored in registeredProfile
        ProfileDto registeredProfile = profileService.registerProfile(profileDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(registeredProfile);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> verification(@RequestParam("token") String token) {

        //if requestbody token matches with the db activation token, then return verified
        if (profileActivationService.verify(token))
            return ResponseEntity.ok("Profile Created Successfully");
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Profile not Created Successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody AuthDTO authDTO) {
        try {
            if (!profileService.isAccountActive(authDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
            Map<String, Object> response = profileService.authenticateAndGenerateToken(authDTO);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }
}
