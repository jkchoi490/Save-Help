package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.entity.ContactType;
import com.save_help.Save_Help.helper.entity.Helper;
import com.save_help.Save_Help.helper.entity.HelperEmergencyContact;
import com.save_help.Save_Help.helper.repository.HelperEmergencyContactRepository;
import com.save_help.Save_Help.helper.repository.HelperRepository;
import com.save_help.Save_Help.user.entity.User;
import com.save_help.Save_Help.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HelperEmergencyContactService {

    private final HelperEmergencyContactRepository contactRepository;
    private final HelperRepository helperRepository;
    private final UserRepository userRepository;

    public HelperEmergencyContact sendContact(Long senderId, Long receiverId, String message, ContactType type) {
        User user;
        Helper helper;

        if (type == ContactType.USER_TO_HELPER) {
            user = userRepository.findById(senderId).orElseThrow();
            helper = helperRepository.findById(receiverId).orElseThrow();
        } else {
            user = userRepository.findById(receiverId).orElseThrow();
            helper = helperRepository.findById(senderId).orElseThrow();
        }

        HelperEmergencyContact contact = HelperEmergencyContact.builder()
                .contactType(type)
                .message(message)
                .user(user)
                .helper(helper)
                .build();

        return contactRepository.save(contact);
    }

    public List<HelperEmergencyContact> getContactsForHelper(Long helperId) {
        return contactRepository.findByHelperId(helperId);
    }

    public List<HelperEmergencyContact> getContactsForUser(Long userId) {
        return contactRepository.findByUserId(userId);
    }
}
