package com.save_help.Save_Help.dailyNecessities.service;


import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessities;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesDelivery;
import com.save_help.Save_Help.dailyNecessities.entity.DailyNecessitiesDeliveryStatus;
import com.save_help.Save_Help.dailyNecessities.repository.DailyNecessitiesDeliveryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DailyNecessitiesDeliveryService {

    private final DailyNecessitiesDeliveryRepository deliveryRepository;

    @Transactional
    public void updateStatus(Long id, DailyNecessitiesDeliveryStatus status) {
        DailyNecessitiesDelivery delivery = deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송 정보를 찾을 수 없습니다. id=" + id));

        delivery.setStatus(status);
        deliveryRepository.save(delivery);
    }

    @Transactional(readOnly = true)
    public DailyNecessitiesDelivery getDelivery(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 배송 정보를 찾을 수 없습니다. id=" + id));
    }
}