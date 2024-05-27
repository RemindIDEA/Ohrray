package com.ohrray.service;

import com.ohrray.domain.FaqDTO;
import com.ohrray.entity.Faq;
import com.ohrray.repository.FaqRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.ohrray.domain.FaqDTO.convertToDTO;
import static com.ohrray.domain.FaqDTO.convertToEntity;


@Service
@RequiredArgsConstructor
public class FaqServiceImpl implements FaqService {

    private final FaqRepository faqRepository;

    @Transactional
    @Override
    public void saveFaq(FaqDTO faqDTO) {
        Faq faq = convertToEntity(faqDTO);
        faq=faqRepository.save(faq);
        convertToDTO(faq);
    }

    @Override
    public FaqDTO getFaqById(Long id) {
        FaqDTO faqDTO = FaqDTO.convertToDTO(faqRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        return faqDTO;
    }

    @Transactional
    @Override
    public List<FaqDTO> getFaqs() {
        return faqRepository.findAll().stream()
                .map(FaqDTO::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void updateFaq(FaqDTO faqDTO) {
        if (faqDTO.getId() == null) {
            throw new IllegalArgumentException("FAQ ID must not be null");
        }
        Faq existingFaq = faqRepository.findById(faqDTO.getId())
                .orElseThrow(() -> new EntityNotFoundException("FAQ not found with ID: " + faqDTO.getId()));

        System.out.println("Updating FAQ with ID: " + existingFaq.getId() + ", Title: " + faqDTO.getTitle());

        existingFaq.setTitle(faqDTO.getTitle());
        existingFaq.setContent(faqDTO.getContent());
    }

    @Transactional
    @Override
    public void deleteFaq(Long id) {
        if(faqRepository.existsById(id)) {
            faqRepository.deleteById(id);
        }else {
            throw new EntityNotFoundException("Faq not found" + id);
        }
    }

}
