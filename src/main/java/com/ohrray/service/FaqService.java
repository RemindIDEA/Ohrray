package com.ohrray.service;

import com.ohrray.domain.FaqDTO;

import java.util.List;
import java.util.Optional;

public interface FaqService {
    void saveFaq(FaqDTO faqDTO);
    FaqDTO getFaqById(Long id);
    List<FaqDTO> getFaqs();
    void deleteFaq(Long id);
    void updateFaq(FaqDTO faqDTO);
}
