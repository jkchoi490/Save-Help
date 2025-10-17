package com.save_help.Save_Help.helper.service;

import com.save_help.Save_Help.helper.dto.HelperEmergencyContactDto;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HelperEmergencyContactService {

    private final HelperEmergencyContactRepository contactRepository;
    private final HelperRepository helperRepository;
    private final UserRepository userRepository;

    public HelperEmergencyContactDto sendContact(Long senderId, Long receiverId, String message, ContactType type) {
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

        HelperEmergencyContact saved = contactRepository.save(contact);

        return toDto(saved);
    }

    public List<HelperEmergencyContactDto> getContactsForHelper(Long helperId) {
        List<HelperEmergencyContact> contacts = contactRepository.findByHelperId(helperId);
        return contacts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public List<HelperEmergencyContactDto> getContactsForUser(Long userId) {
        List<HelperEmergencyContact> contacts = contactRepository.findByUserId(userId);
        return contacts.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    private HelperEmergencyContactDto toDto(HelperEmergencyContact contact) {
        return new HelperEmergencyContactDto(
                contact.getId(),
                contact.isRead(),
                contact.getMessage(),
                contact.getHelper().getId(),
                contact.getUser().getId()
        );
    }
}
